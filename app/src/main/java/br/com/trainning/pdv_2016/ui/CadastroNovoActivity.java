package br.com.trainning.pdv_2016.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mapzen.android.lost.api.LocationServices;
import com.mapzen.android.lost.api.LostApiClient;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.trainning.pdv_2016.R;
import br.com.trainning.pdv_2016.domain.model.Produto;
import br.com.trainning.pdv_2016.domain.network.APIClient;
import br.com.trainning.pdv_2016.domain.util.Base64Util;
import br.com.trainning.pdv_2016.domain.util.ImageInputHelper;
import butterknife.Bind;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import se.emilsjolander.sprinkles.Query;

public class CadastroNovoActivity extends BaseActivity implements ImageInputHelper.ImageActionListener{

    @Bind(R.id.editTextDescricao)
    EditText editTextDescricao;
    @Bind(R.id.editTextUnidade)
    EditText editTextUnidade;
    @Bind(R.id.editTextPreco)
    EditText editTextPreco;
    @Bind(R.id.editTextCodigo)
    EditText editTextCodigo;

    @Bind(R.id.imageViewFoto)
    ImageView imageViewFoto;
    @Bind(R.id.imageButtonCamera)
    ImageButton imageButtonCamera;
    @Bind(R.id.imageButtonGaleria)
    ImageButton imageButtonGaleria;

    private ImageInputHelper imageInputHelper;
    private Produto produto;

    private double latitude = 0.0d;
    private double longitude = 0.0d;
    private AlertDialog dialog;

    Callback<String> callbackNovoProduto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_novo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        configureNovoProdutoCallback();

        dialog = new SpotsDialog(this,"Enviando para Servidor...");

        LostApiClient lostApiClient = new LostApiClient.Builder(this).build();
        lostApiClient.connect();

        Location location = LocationServices.FusedLocationApi.getLastLocation();
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        Log.d("LOCATION","Latitude:"+latitude);
        Log.d("LOCATION","longitude:"+longitude);

        imageInputHelper = new ImageInputHelper(this);
        imageInputHelper.setImageActionListener(this);

         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Log.d("Cadastro",editTextDescricao.getText().toString());
                Log.d("Cadastro",editTextUnidade.getText().toString());
                Log.d("Cadastro",editTextPreco.getText().toString());
                Log.d("Cadastro",editTextCodigo.getText().toString());
                produto = new Produto();
                produto.setId(0L);
                produto.setDescricao(editTextDescricao.getText().toString());
                produto.setUnidade(editTextUnidade.getText().toString());
                produto.setCodigoBarras(editTextCodigo.getText().toString());
                if(!editTextPreco.getText().toString().equals("")){
                    produto.setPreco(Double.parseDouble(editTextPreco.getText().toString()));
                }else{
                    produto.setPreco(0.0);
                }

                Bitmap imagem = ((BitmapDrawable)imageViewFoto.getDrawable()).getBitmap();

                produto.setFoto(Base64Util.encodeTobase64(imagem));

                produto.setLatitude(latitude);
                produto.setLongitude(longitude);
                produto.setStatus(0);

                produto.save();

                dialog.show();
                new APIClient().getRestService().createProduto(produto.getCodigoBarras(),produto.getDescricao(),produto.getUnidade(),produto.getPreco(),produto.getFoto(),produto.getStatus(),produto.getLatitude(),produto.getLongitude(),callbackNovoProduto);




            }
        });
    }

    @OnClick(R.id.imageButtonGaleria)
    public void onClickGaleria(){
        imageInputHelper.selectImageFromGallery();
    }

    @OnClick(R.id.imageButtonCamera)
    public void onClickCamera(){
        imageInputHelper.takePhotoWithCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {

        imageInputHelper.requestCropImage(uri, 100, 100, 0, 0);
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {

        imageInputHelper.requestCropImage(uri, 100, 100, 0, 0);
    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {
        try {
            // getting bitmap from uri
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            imageViewFoto.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configureNovoProdutoCallback() {

        callbackNovoProduto = new Callback<String>() {

            @Override public void success(String resultado, Response response) {
                dialog.dismiss();

                finish();


            }

            @Override public void failure(RetrofitError error) {
                dialog.dismiss();

                Snackbar.make(findViewById(android.R.id.content).getRootView(),"Houve um problema de conexão ! Por favor verifique e tente novamente!",Snackbar.LENGTH_SHORT).show();

                Log.e("RETROFIT", "Error:"+error.getMessage());
            }
        };
    }

}
