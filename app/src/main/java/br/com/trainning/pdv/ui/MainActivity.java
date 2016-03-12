package br.com.trainning.pdv.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import br.com.trainning.pdv.R;
import br.com.trainning.pdv.domain.adapter.CustomArrayAdapter;
import br.com.trainning.pdv.domain.model.Item;
import br.com.trainning.pdv.domain.model.ItemProduto;
import br.com.trainning.pdv.domain.model.Produto;
import br.com.trainning.pdv.domain.util.Util;
import butterknife.Bind;
import jim.h.common.android.lib.zxing.config.ZXingLibConfig;
import jim.h.common.android.lib.zxing.integrator.IntentIntegrator;
import jim.h.common.android.lib.zxing.integrator.IntentResult;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

public class MainActivity extends BaseActivity {

    private ZXingLibConfig zxingLibConfig;

    private List<ItemProduto> list;
    private int quantidadeItens;
    private double valorTotal;
    private CustomArrayAdapter adapter;

    @Bind(R.id.listView)
    SwipeMenuListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                openItem.setWidth(Util.convertPixelsToDp(590,getApplicationContext()));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(Util.convertPixelsToDp(590,getApplicationContext()));
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
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        popularLista();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        List<Produto> produtos = Query.all(Produto.class).get().asList();
        if(produtos!= null){
            for (Produto p:produtos) {
                Log.d("Produto:", "id-->" + p.getId());
                Log.d("Produto:", "descricao-->" + p.getDescricao());
                Log.d("Produto:", "unidade-->" + p.getUnidade());
                Log.d("Produto:", "Codigo barras-->" + p.getCodigoBarras());
                Log.d("Produto:", "preco-->" + p.getPreco());
                Log.d("Produto:", "foto-->" + p.getFoto());
                Log.d("Produto:", "------------------------");
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
        int id = item.getItemId();// pega o elemento do item e aciona

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_novo) { // Reconhecer o elemento action_novo e direciona para a tela de CadastroNovoActivity
            //Log.d("MainActivity", "Selecionou novo Produto"); // mostra do debug a a atividade da tela principal quando acionado este elemento
            //return true;
            Intent telaCadastroNovoIntent = new Intent(MainActivity.this,CadastroNovoActivity.class); //Intenção é  criar a funcao e direcionar a activity atual para a CadastroNovoActivity
            startActivity(telaCadastroNovoIntent); //roda a intencao




        }else if(id == R.id.action_edit){
            //Log.d("MainActivity", "Selecionou editar Produto");
           // return true;5
            Intent telaEditarIntent = new Intent(MainActivity.this,EditarProdutoActivity.class); //Intenção é  criar a funcao e direcionar a activity atual para a CadastroNovoActivity
            startActivity(telaEditarIntent);
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
                    Log.d("SCANBARCODE", "BraCode:" +result);

                    Produto produto = Query.one(Produto.class, "select * from produto where codigo_barra = ?", result).get();
                    if (produto!=null){

                        Item item = new Item();
                        item.setId(0L);
                        item.setIdCompra(1L);
                        item.setIdProduto(produto.getCodigoBarras()) ;
                        item.setQuantidade(1);
                        item.save();
                        popularLista();

                    }else{
                        Toast.makeText(MainActivity.this, "Produto não Localizado!", Toast.LENGTH_SHORT).show();
                    }



                }
                break;

            default:
        }

    }

    public void popularLista(){
        List<Item> listaItem = Query.many(Item.class, "select * from Item where id_compra = ? order by id", 1).get().asList();

        Log.d("TAMANHOLISTA",""+ listaItem.size());

        ItemProduto itemProduto;
        Produto produto;
        list = new ArrayList<>();
        valorTotal=0.0d;
        quantidadeItens = 0;

        for(Item item:listaItem){

            produto = Query.one(Produto.class,"select * from Produto where codigo_barra = ?", item.getIdProduto()).get();
            itemProduto = new ItemProduto();
            itemProduto.setIdCompra(1);
            itemProduto.setIdItem(item.getId());
            itemProduto.setFoto(produto.getFoto());
            itemProduto.setUnidade(produto.getUnidade());
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

}
