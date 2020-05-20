
package com.velsrom.idea.creation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.velsrom.idea.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CreateBusquedaActivity extends AppCompatActivity {

    /*
    * FIXME:
    *  - BLOB de imagen, si inserta imagen correctamente?
    *  -
    * */

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    public static final int REQUEST_CODE_SELECT_IMAGE = 2;

    EditText titleEditText;
    TextView imageTextView;
    EditText descripcionEditText;

    String path = "";
    boolean fromSC = false;

    byte[] imgByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_busqueda);

        titleEditText = findViewById(R.id.titleEditText);
        imageTextView = findViewById(R.id.imageTextView);
        descripcionEditText = findViewById(R.id.descripcionEditText);

        Intent getIntentFromSC = getIntent();
        path = getIntentFromSC.getStringExtra("path");

        if(!path.isEmpty()){
            Log.i("Image loaded path: ", path);
            imageTextView.setText("Image Loaded");
            fromSC = true;
        }
    }

    public void loadImage(View view){
        if(ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    CreateBusquedaActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSION
            );
        }else{
            selectImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            selectImage();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if(data != null){
                Uri selectedImageUri = data.getData();
                if(selectedImageUri != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        imageTextView.setText("IMAGE LOADED");
                        File selectedImageFile = new File(getPathFromUri(selectedImageUri));
                        //Con este file se sube al SQLite

                        InputStream imageInputStream = new DataInputStream(new FileInputStream(selectedImageFile));
                        imgByte = new byte[imageInputStream.available()];

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if(cursor == null){
            filePath = contentUri.getPath();
        }else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    //==============================================================================================

    public void saveBusqueda(View view){
        if(!titleEditText.getText().toString().equals("") && !descripcionEditText.getText().toString().equals(""))
        {
            try {
                SQLiteDatabase busquedasDatabase = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);

                busquedasDatabase.execSQL("CREATE TABLE IF NOT EXISTS busquedas (title VARCHAR, path VARCHAR, descripcion VARCHAR)");

                ContentValues cv = new ContentValues();
                cv.put("title", titleEditText.getText().toString());
                cv.put("path", path);
                cv.put("descripcion", descripcionEditText.getText().toString());
                busquedasDatabase.insert("busquedas", null, cv);

                Cursor c = busquedasDatabase.rawQuery("SELECT * FROM busquedas", null);

                int titleIndex = c.getColumnIndex("title");
                int pathIndex = c.getColumnIndex("path");
                int descripcionIndex = c.getColumnIndex("descripcion");

                c.moveToFirst();

                while (!c.isAfterLast()) {
                    Log.i("title", c.getString(titleIndex));
                    Log.i("path", c.getString(pathIndex));
                    Log.i("descripcion", c.getString(descripcionIndex));
                    c.moveToNext();
                }

                Toast.makeText(getApplicationContext(), "Busqueda saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Llenar los datos correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelBusqueda(View view){
//        SQLiteDatabase busquedasDatabase = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
//        Cursor c = busquedasDatabase.rawQuery("SELECT * FROM busquedas", null);
//
//        int titleIndex = c.getColumnIndex("title");
//        int imageIndex = c.getColumnIndex("image");
//        int descripcionIndex = c.getColumnIndex("descripcion");
//
//        c.moveToFirst();
//
//        while (!c.isAfterLast()) {
//            Log.i("title", c.getString(titleIndex));
//            Log.i("image", c.getString(imageIndex));
//            Log.i("descripcion", c.getString(descripcionIndex));
//            c.moveToNext();
//        }

        finish();
    }
}
