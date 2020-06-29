
package com.velsrom.idea.creation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.velsrom.idea.IdeaDataBase;
import com.velsrom.idea.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class CreateBusquedaActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    public static final int REQUEST_CODE_SELECT_IMAGE = 2;

    EditText titleEditText;
    TextView imageTextView;
    EditText descripcionEditText;

    IdeaDataBase busquedaDataBase;

    String path, busqueda, descripcion;
    int id;
    boolean fromSC = false;
    boolean isEdit = false;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_busqueda);

        titleEditText = findViewById(R.id.titleEditText);
        imageTextView = findViewById(R.id.imageTextView);
        descripcionEditText = findViewById(R.id.descripcionEditText);

        path = getIntent().getStringExtra("path");
        busqueda = getIntent().getStringExtra("BUSQUEDA");
        descripcion = getIntent().getStringExtra("DESCRIPCION");
        id = getIntent().getIntExtra("ID", -1);

        if(id != -1){
            isEdit = true;
        }

        if(path != null && !path.isEmpty()){
            Log.i("Image loaded path: ", path);
            imageTextView.setText("Image Loaded");
            fromSC = true;
        }else {
            path="";
        }

        SQLiteDatabase databse = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);

        String[] columns = {"title", "path", "descripcion"};
        ArrayList<String> nameTypes= new ArrayList();

        busquedaDataBase = new IdeaDataBase(databse, "busquedas", columns, nameTypes);
    }

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================

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
        Intent intent = new Intent(Intent.ACTION_PICK);
        if(intent.resolveActivity(getPackageManager()) != null){
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_SELECT_IMAGE);
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
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        fromSC = true;
                        imageTextView.setText("Image Loaded");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //==============================================================================================
    //==============================================================================================
    //==============================================================================================

    public void saveBusqueda(View view){
        OutputStream outputStream = null;
        if(!titleEditText.getText().toString().equals(""))
        {
            try {
                if(isEdit){
                    busquedaDataBase.editRow( titleEditText.getText().toString(), descripcionEditText.getText().toString(), id);
                }else {
                    if (fromSC) {
                        File dir = new File(getFilesDir() + "/Screenshots/");
                        if (!dir.isFile()) {
                            boolean mkdir = dir.mkdir();
                            if (mkdir) {
                                Log.i("File creation", "Success: " + mkdir);
                            } else {
                                Log.i("File creation", "Already created: " + dir);
                            }
                        }
                        File file = new File(dir, System.currentTimeMillis() + ".jpg");
                        try {
                            outputStream = new FileOutputStream(file);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        path = file.getPath();
                        Toast.makeText(getApplicationContext(), "Image Saved: " + path, Toast.LENGTH_SHORT).show();
                    }
                    String[] dataForDatabase = {titleEditText.getText().toString(), path, descripcionEditText.getText().toString()};
                    busquedaDataBase.addData(dataForDatabase);
                    Toast.makeText(getApplicationContext(), "Busqueda Saved", Toast.LENGTH_SHORT).show();
                }
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
        finish();
    }
}
