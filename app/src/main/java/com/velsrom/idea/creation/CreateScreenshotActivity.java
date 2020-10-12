package com.velsrom.idea.creation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.velsrom.idea.MainActivity;
import com.velsrom.idea.R;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CreateScreenshotActivity extends AppCompatActivity {

    /*
    * TODO:
    *   - Otra forma de agarrar imagenes para poder guardar en busqueda
    *   - Hacer copia de imagen o agarrar el path de la original?
    *   - Crear otra actividad para confirmar el grabar imagen seleccionada en lugar de utilizar esta misma (mas facil)
    * */

    public static final int PICK_IMAGE = 1;

    WebView webView;
    ImageView imageView;
    OutputStream outputStream;
    boolean isImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_screenshot);

        webView = findViewById(R.id.webView);
        imageView = findViewById(R.id.imageView);
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new WebViewClient());
        webSettings.setJavaScriptEnabled(true);  //ver vulnerabilidad y si es necesaria
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl("http://www.google.com/");
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================

    public void imageFromGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE) {
            webView.setVisibility(View.GONE);
            imageView.setImageURI(data.getData());
            isImageView = true;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================

    public void saveScreen(View view){
        Bitmap bitmap;
        if(isImageView){
            //imageview
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache(true);
            bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
            imageView.setDrawingCacheEnabled(false);
        }else {
            //webview
            webView.setDrawingCacheEnabled(true);
            webView.buildDrawingCache(true);
            bitmap = Bitmap.createBitmap(webView.getDrawingCache());
            webView.setDrawingCacheEnabled(false);
        }

        saveScreenshot(bitmap);

        finish();
    }

    private void saveScreenshot(Bitmap bitmap){
        File dir = new File(getFilesDir() + "/Screenshots/");
        if(!dir.isFile()){
            boolean mkdir = dir.mkdir();
            if(mkdir){
                Log.i("File creation", "Success: " + mkdir);
            }else{
                Log.i("File creation", "Already created: " + dir);
            }
        }
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            outputStream = new FileOutputStream(file);

        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        Toast.makeText(getApplicationContext(), "Image Saved: " + file.getPath(), Toast.LENGTH_SHORT).show();
        Log.i("File path: ", file.getPath());

        Intent scToBusqueda = new Intent(getApplicationContext(), CreateBusquedaActivity.class);
        scToBusqueda.putExtra("path", file.getPath());
        startActivity(scToBusqueda);
    }

    public void cancelActivity(View view){
        finish();
    }
}
