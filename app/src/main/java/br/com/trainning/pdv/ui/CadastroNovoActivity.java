package br.com.trainning.pdv.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import br.com.trainning.pdv.R;

public class CadastroNovoActivity extends AppCompatActivity {

    EditText editTextDescricao;
    EditText editTextTextUnidade;
    EditText editTextPreco;
    EditText editTextCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_novo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextDescricao = (EditText)findViewById(R.id.editTextDescricao);
        editTextTextUnidade = (EditText)findViewById(R.id.editTextUnidade);
        editTextPreco = (EditText)findViewById(R.id.editTextPreco);
        editTextCodigo = (EditText)findViewById(R.id.editTextCodigo);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Cadastro", editTextDescricao.getText().toString());
                Log.d("Cadastro", editTextTextUnidade.getText().toString());
                Log.d("Cadastro", editTextPreco.getText().toString());
                Log.d("Cadastro", editTextCodigo.getText().toString());

            }
        });
    }

}
