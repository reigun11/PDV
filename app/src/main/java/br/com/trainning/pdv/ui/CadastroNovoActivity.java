package br.com.trainning.pdv.ui;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import br.com.trainning.pdv.R;
import br.com.trainning.pdv.domain.ImageInputHelper;
import butterknife.Bind;
import butterknife.OnClick;

public class CadastroNovoActivity extends BaseActivity implements ImageInputHelper.ImageActionListener{

    @Bind(R.id.editTextDescricao) EditText editTextDescricao;
    @Bind(R.id.editTextUnidade) EditText editTextUnidade;
    @Bind(R.id.editTextPreco) EditText editTextPreco;
    @Bind(R.id.editTextCodigo) EditText editTextCodigo;

    @Bind(R.id.imageViewFoto)
    ImageView imageViewFoto;
    @Bind(R.id.imageButtonCamera)
    ImageView imageButtonCamera;
    @Bind(R.id.imageButtonGaleria)
    ImageView getImageButtonGaleria;

    private ImageInputHelper imageInputHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_novo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageInputHelper = new ImageInputHelper(this);
        imageInputHelper.setImageActionListener(this);// essa classe ira receber  de volta

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Cadastro", editTextDescricao.getText().toString());
                Log.d("Cadastro", editTextUnidade.getText().toString());
                Log.d("Cadastro", editTextPreco.getText().toString());
                Log.d("Cadastro", editTextCodigo.getText().toString());

            }
        });
    }

    @OnClick(R.id.imageButtonGaleria)
    public void onClickGaleria(){
        imageInputHelper.selectImageFromGallery();
    }

    @OnClick(R.id.imageButtonCamera)
    public  void onClickCamera(){
        imageInputHelper.takePhotoWithCamera();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {
        // cropping the selected image. crop intent will have aspect ratio 16/9 and result image
        // will have size 800x450
        imageInputHelper.requestCropImage(uri, 100, 100, 0, 0);
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {
        // cropping the taken photo. crop intent will have aspect ratio 16/9 and result image
        // will have size 100x100 or 880x500 16.9
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

}
