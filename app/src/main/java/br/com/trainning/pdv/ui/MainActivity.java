package br.com.trainning.pdv.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import br.com.trainning.pdv.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            //



        }else if(id == R.id.action_edit){

            Log.d("MainActivity", "Selecionou editar Produto");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
