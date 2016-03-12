package br.com.trainning.pdv.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.trainning.pdv.R;
import br.com.trainning.pdv.domain.model.Produto;
import butterknife.Bind;
import se.emilsjolander.sprinkles.Query;

public class EditarProdutoActivity extends BaseActivity {
    @Bind(R.id.spinner)
    Spinner spinner;

    @Bind(R.id.editTextDescricao)
    EditText editTextDescricao;
    @Bind(R.id.editTextUnidade) EditText editTextUnidade;
    @Bind(R.id.editTextPreco) EditText editTextPreco;
    @Bind(R.id.editTextCodigo) EditText editTextCodigo;

    @Bind(R.id.imageViewFoto)
    ImageView imageViewFoto;
    @Bind(R.id.imageButtonCamera)
    ImageView imageButtonCamera;
    @Bind(R.id.imageButtonGaleria)
    ImageView getImageButtonGaleria;

    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_produto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // List<Produto> produtos = Query.many(Produto.class, "select * from produto where id = ? order by codigo_barra",1).get().asList(); // listagem de produto com filtro
        List<Produto> produtos = Query.many(Produto.class, "select * from produto order by codigo_barra").get().asList();

        produto = new Produto();

        List<String> barcodeList = new ArrayList<>();

        for(Produto produto: produtos){
            barcodeList.add(produto.getCodigoBarras());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,barcodeList);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);//para usar a lista coloca dentro do adapter

        //
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String barCode = parent.getItemAtPosition(position).toString();

                Log.d("BARCODE", "selecionado-->" +barCode);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

}
