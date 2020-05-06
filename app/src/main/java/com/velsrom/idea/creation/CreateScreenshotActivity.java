package com.velsrom.idea.creation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.velsrom.idea.R;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateScreenshotActivity extends AppCompatActivity {

    /*
    * FIXME:
    *   - Ver por que no sube el SC bien
    *   - Otra forma de agarrar imagenes para poder guardar en notas y ver como
    * */

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_screenshot);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.google.com");
    }

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================

    public void imageFromGallery(View view){

    }

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================

    public void screenshotActivity(View view){
        Bitmap bitmap = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        //Save screenshot in internal storage
        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = getApplicationContext().openFileOutput("webviewScreenshot.png", Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.i("IMAGE SAVED", "SUCCESS");
        } catch (Exception e) {
            Log.i("IMAGE SAVED", "ERROR");
            Log.i("saveToInternalStorage()", e.getMessage());
        }
        finish();
    }

    public void cancelActivity(View view){
        finish();
    }
}



// NOTAS =====

//    public void imageFromGallery(View view){
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(i, "Select Image"), 101);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 101 && resultCode == RESULT_OK && data != null){
//            Uri uri = data.getData();
//
//            String path = getRealPathFromURI(this, uri);
//
//            String name = getFileName(uri);
//
//            try {
//                insertInPrivateStorage(name, path);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String getRealPathFromURI(Context context, Uri contentUri){
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor != null) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        }
//        return null;
//    }
//
//    private String getFileName(Uri uri){
//        String result = null;
//        if (uri.getScheme().equals("content")) {
//            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//            try {
//                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                }
//            } finally {
//                cursor.close();
//            }
//        }
//        if (result == null) {
//            result = uri.getPath();
//            int cut = result.lastIndexOf('/');
//            if (cut != -1) {
//                result = result.substring(cut + 1);
//            }
//        }
//        return result;
//    }
//
//    private void insertInPrivateStorage(String name, String path) throws IOException {
//        FileOutputStream fos  = openFileOutput(name,MODE_APPEND);
//
//        File file = new File(path);
//
//        byte[] bytes = getBytesFromFile(file);
//
//        fos.write(bytes);
//        fos.close();
//
//        Toast.makeText(getApplicationContext(),"File saved in :"+ getFilesDir() + "/"+name,Toast.LENGTH_SHORT).show();
//    }
//
//    private byte[] getBytesFromFile(File file) throws IOException {
//        byte[] data = FileUtils.readFileToByteArray(file);
//        return data;
//
//    }
