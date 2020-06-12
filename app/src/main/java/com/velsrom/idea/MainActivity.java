package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.velsrom.idea.creation.CreateBusquedaActivity;
import com.velsrom.idea.creation.CreateGlosarioActivity;
import com.velsrom.idea.creation.CreateIdeaActivity;
import com.velsrom.idea.creation.CreateScreenshotActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
    * TODO:
    *  - Sig version: poder ordenar de diferentes maneras las ideas/busquedas (fecha, titulo asc-desc, etc.)
    *  - 
    * */

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //==========================================================================================
        // Si es la primera vez que usa el app, no tendra las tablas de bases de datos. Ejecutar esto (posible cambio)
        // poner booleano unico para ver si es la primera vez usando el app? O if con ese booleano para
        // ejecute este codigo 1 vez y cree la base de datos (hacer metodo)
        //==========================================================================================

        SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);

        Database.execSQL("CREATE TABLE IF NOT EXISTS ideas (title VARCHAR, type VARCHAR, idea VARCHAR)");
        Database.execSQL("CREATE TABLE IF NOT EXISTS busquedas (title VARCHAR, path VARCHAR, descripcion VARCHAR)");
        Database.execSQL("CREATE TABLE IF NOT EXISTS glosario (palabra VARCHAR, definicion VARCHAR)");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION);
            }
        } else {
            // Permission has already been granted

        }
    }

    public void onClickSection(View view){
        int tagClicked = Integer.parseInt(view.getTag().toString());

        Intent intent = null;
        boolean isIntent = true;
        switch (tagClicked) {
            case 0:
                intent = new Intent(MainActivity.this, IdeasMenuActivity.class);
                break;
            case 1:
                intent = new Intent(MainActivity.this, BusquedasActivity.class);
                break;
            case 2:
                intent = new Intent(MainActivity.this, GlosarioActivity.class);
                break;
            case 3:
                intent = new Intent(MainActivity.this, CreateScreenshotActivity.class);
                break;
            case 4:
                randomBusqueda();
                isIntent = false;
                break;
            case 5:
                intent = new Intent(MainActivity.this, CreateIdeaActivity.class);
                break;
            case 6:
                intent = new Intent(MainActivity.this, CreateBusquedaActivity.class);
                break;
            case 7:
                intent = new Intent(MainActivity.this, CreateGlosarioActivity.class);
                break;
        }
        if(isIntent) {startActivity(intent);}
    }

    //======================================= SECCION RANDOM  ======================================

    public void randomBusqueda(){

        dialog = new Dialog(this);

        ArrayList<ArrayList<String>> busquedasOptions;

        SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        IdeaDataBase ideaDataBase = new IdeaDataBase(Database);
        busquedasOptions = ideaDataBase.createQuery("SELECT * FROM busquedas");

        int numero = (int) (Math.random() * busquedasOptions.size());

        openDialog(busquedasOptions.get(numero).get(0));

    }

    public void openDialog(String word){
        //SI TIENE IMAGEN, QUE LA MUESTRE TAMBIEN
        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery("SELECT * FROM busquedas WHERE title = \'" + word + "\' ", null);

        int descripcionIdx = c.getColumnIndex("descripcion");
        int pathIdx = c.getColumnIndex("path");
        c.moveToFirst();
        String descripcion = c.getString(descripcionIdx);
        String path = c.getString(pathIdx);

        Context context = this; //???
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView titleBox = new TextView(context);
        titleBox.setText(descripcion);
        layout.addView(titleBox);

        final ImageView image = new ImageView(context);

        if(!path.equals("")){
            Bitmap myBitmap = BitmapFactory.decodeFile(path);
            image.setImageBitmap(myBitmap);
            layout.addView(image);
        }

        dialog.setContentView(layout);
        dialog.show();

    }
}
