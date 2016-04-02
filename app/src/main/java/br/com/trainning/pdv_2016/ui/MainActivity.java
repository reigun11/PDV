package br.com.trainning.pdv_2016.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.util.ArrayList;
import java.util.List;

import br.com.trainning.pdv_2016.R;
import br.com.trainning.pdv_2016.domain.adapter.CustomArrayAdapter;
import br.com.trainning.pdv_2016.domain.model.Carrinho;
import br.com.trainning.pdv_2016.domain.model.Compra;
import br.com.trainning.pdv_2016.domain.model.Item;
import br.com.trainning.pdv_2016.domain.model.ItemProduto;
import br.com.trainning.pdv_2016.domain.model.Produto;
import br.com.trainning.pdv_2016.domain.network.APIClient;
import br.com.trainning.pdv_2016.domain.util.Util;
import butterknife.Bind;
import dmax.dialog.SpotsDialog;
import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

public class MainActivity extends BaseActivity {


    private ZXingLibConfig zxingLibConfig;

    private List<ItemProduto> list;
    private int quantidadeItens;
    private double valorTotal;
    private CustomArrayAdapter adapter;

    private AlertDialog dialog;

    private Callback<List<Produto>> callbackProdutos;
    private Callback<String> callbackCompra;

    private String idCompra;
    private Carrinho carrinho;
    private Compra compra;

    @Bind(R.id.listView)
    SwipeMenuListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configureProdutoCallback();
        configureCompraCallback();

        dialog = new SpotsDialog(this,"Carregando....");

        List<Item> itens = Query.all(Item.class).get().asList();
        for(Item item:itens){
            item.delete();
        }
        idCompra = Util.getUniquePsuedoID();
        carrinho =new Carrinho();
        carrinho.setIdCompra(idCompra);
        carrinho.setEncerrada(0);
        carrinho.setEnviada(0);



        zxingLibConfig = new ZXingLibConfig();
        zxingLibConfig.useFrontLight = true;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    IntentIntegrator.initiateScan(MainActivity.this, zxingLibConfig);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(Util.convertPixelsToDp(590.0f,MainActivity.this));
                // set item title

                openItem.setIcon(R.drawable.ic_exposure_plus_1_black_36dp);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Util.convertPixelsToDp(590.0f,MainActivity.this));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_remove_shopping_cart_white_36dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ItemProduto itemProduto = adapter.getItem(position);
                Item item = Query.one(Item.class,"select * from item where id = ?", itemProduto.getIdItem()).get();
                switch (index) {
                    case 0:
                        //Toast.makeText(getApplicationContext(), "Action 1 for " + itemProduto.getDescricao(), Toast.LENGTH_SHORT).show();
                        item.setQuantidade(item.getQuantidade()+1);
                        item.save();
                        list.clear();
                        popularLista();
                        break;
                    case 1:
                        //Toast.makeText(getApplicationContext(), "Action 2 for " + itemProduto.getDescricao(), Toast.LENGTH_SHORT).show();
                        item.delete();
                        list.clear();
                        popularLista();

                        break;
                }
                return false;
            }
        });


        popularLista();



    }

    @Override
    protected void onRestart() {
        super.onRestart();
        List<Produto> produtos = Query.all(Produto.class).get().asList();
        if(produtos!=null){
            for(Produto p: produtos){
                Log.d("Produto:","id------------->"+p.getId());
                Log.d("Produto:","descricao------>"+p.getDescricao());
                Log.d("Produto:","unidade-------->"+p.getUnidade());
                Log.d("Produto:","codigo barras-->"+p.getCodigoBarras());
                Log.d("Produto:","preco---------->"+p.getPreco());
                Log.d("Produto:","foto----------->"+p.getFoto());
                Log.d("Produto:","--------------------------------");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_novo) {

            Intent telaCadastroNovoIntent = new Intent(MainActivity.this,CadastroNovoActivity.class);
            startActivity(telaCadastroNovoIntent);

        }else if(id == R.id.action_edit){
            Intent telaEditarIntent = new Intent(MainActivity.this,EditarProdutoActivity.class);
            startActivity(telaEditarIntent);
        } else if(id == R.id.action_mapa){
            Intent mapaIntent = new Intent(MainActivity.this,MapaActivity.class);
            startActivity(mapaIntent);
        }else if(id == R.id.action_sincronia){

            dialog.show();
            new APIClient().getRestService().getAllProdutos(callbackProdutos);

        } else if(id == R.id.action_fecha_compra){

            List<Item> itens = Query.all(Item.class).get().asList();
            int quantidadeItens = 0;
            double precoTotal = 0.0d;
            Produto produto;
            for(Item it:itens){
                quantidadeItens += it.getQuantidade();
                produto = Query.one(Produto.class,"select * from produto where codigo_barra = ?",it.getIdProduto()).get();
                precoTotal += it.getQuantidade()* produto.getPreco();
            }

            compra = new Compra();
            compra.setCarrinho(carrinho);
            compra.setItens(itens);

            MaterialStyledDialog dialog = new MaterialStyledDialog(this)
                    .setTitle("Fechar Compra ?")
                    .setDescription("Quantidade de volumes: "+quantidadeItens+" - Total R$ "+precoTotal)
                    .setPositive("Sim", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {

                            dialog.dismiss();
                            MainActivity.this.dialog.show();
                            new APIClient().getRestService().enviarCompra(compra, callbackCompra);
                            Log.d("MaterialStyledDialogs", "Do something!");
                        }
                    })
                    .setNegative("Não", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog dialog, DialogAction which) {

                            dialog.dismiss();
                            // nada !!!!!!
                            Log.d("MaterialStyledDialogs", "Cancelado pelo usuário");
                        }
                    })
                    .build();
            dialog.show();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:

                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                String result = scanResult.getContents();
                if (result != null) {

                        Log.d("SCANBARCODE","BarCode: "+result);

                    Produto produto = Query.one(Produto.class,"select * from produto where codigo_barra = ?",result).get();
                    if(produto!=null){

                        Item item = new Item();
                        item.setId(0L);
                        item.setIdCompra(idCompra);
                        item.setIdProduto(produto.getCodigoBarras());
                        item.setQuantidade(1);
                        item.save();
                        popularLista();





                    }else{

                        Toast.makeText(MainActivity.this, "Produto não localizado !", Toast.LENGTH_SHORT).show();
                    }



                }
                break;

            default:
        }

    }

    public void popularLista(){
        List<Item> listaItem = Query.many(Item.class, "select * from item where id_compra = ? order by id", idCompra).get().asList();

        Log.d("TAMANHOLISTA",""+ listaItem.size());

        ItemProduto itemProduto;
        Produto produto;
        list = new ArrayList<>();
        valorTotal=0.0d;
        quantidadeItens = 0;

        for(Item item:listaItem){

            produto = Query.one(Produto.class,"select * from produto where codigo_barra = ?", item.getIdProduto()).get();
            itemProduto = new ItemProduto();
            itemProduto.setIdCompra(idCompra);
            itemProduto.setIdItem(item.getId());
            itemProduto.setUnidade(produto.getUnidade());
            itemProduto.setFoto(produto.getFoto());
            itemProduto.setDescricao(produto.getDescricao());
            itemProduto.setQuantidade(item.getQuantidade());
            itemProduto.setPreco(produto.getPreco());
            list.add(itemProduto);
            valorTotal+=item.getQuantidade()*produto.getPreco();
            quantidadeItens += item.getQuantidade();
        }
        getSupportActionBar().setTitle("PDV "+ Util.getFormatedCurrency(String.valueOf(valorTotal)));
        adapter = new CustomArrayAdapter(this, R.layout.list_item, list);
        listView.setAdapter(adapter);
    }


    private void configureProdutoCallback() {

        callbackProdutos = new Callback<List<Produto>>() {

            @Override public void success(List<Produto> resultado, Response response) {

                List<Produto> lp = Query.all(Produto.class).get().asList();

                for(Produto p:lp){
                    p.delete();
                }

                for(Produto produto:resultado){
                    produto.setId(0L);
                    produto.save();
                }

                dialog.dismiss();

            }

            @Override public void failure(RetrofitError error) {

                dialog.dismiss();
                Log.e("RETROFIT", "Error:"+error.getMessage());
            }
        };
    }


    private void configureCompraCallback() {

        callbackCompra = new Callback<String>() {

            @Override public void success(String resultado, Response response) {

                List<Item> itens = Query.all(Item.class).get().asList();
                for(Item it:itens){
                    it.delete();
                }
                carrinho = new Carrinho();
                 idCompra=Util.getUniquePsuedoID();
                carrinho.setIdCompra(idCompra);
                carrinho.setEnviada(0);
                carrinho.setEncerrada(0);
                popularLista();

                dialog.dismiss();

            }

            @Override public void failure(RetrofitError error) {

                dialog.dismiss();
                Log.e("RETROFIT", "Error:"+error.getMessage());
            }
        };
    }

}
