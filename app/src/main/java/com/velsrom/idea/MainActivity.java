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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.velsrom.idea.creation.CreateBusquedaActivity;
import com.velsrom.idea.creation.CreateGlosarioActivity;
import com.velsrom.idea.creation.CreateIdeaActivity;
import com.velsrom.idea.creation.CreateScreenshotActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
    * TODO:
    *   - Crear clase para uso de base de datos en lugar de repetir el mismo codigo de anadir y obtener info!!!!!!!!!!!!!!
    *   - Crear activity para ver las busquedas (similar a la de ideas?)
    *   - Seccion edit para las notas
    * */

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    ArrayList<String> menuOptions;

    FloatingActionsMenu floatingAddMenu;
    FloatingActionButton floatingAddIdea;
    FloatingActionButton floatingAddBusqueda;
    FloatingActionButton floatingAddScreenshot;
    FloatingActionButton floatingAddGlosario;
    FloatingActionButton floatingAddIndice;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //==========================================================================================
        //==========================================================================================
        //==========================================================================================

        ListView menuListView = findViewById(R.id.menuListView);

        floatingAddMenu = findViewById(R.id.floatingMenu);
        floatingAddIdea = findViewById(R.id.action_idea);
        floatingAddBusqueda = findViewById(R.id.action_investigacion);
        floatingAddScreenshot = findViewById(R.id.action_screenshot);
        floatingAddGlosario = findViewById(R.id.action_glosario);
        floatingAddIndice = findViewById(R.id.action_indice);

        menuOptions = new ArrayList<>();
        menuOptions.add("Ver ideas");
        menuOptions.add("Que buscar/aprender");
        menuOptions.add("Glosario");
        menuOptions.add("Idea aleatoria");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuOptions);
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                boolean isIntent = true;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, IdeasActivity.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, BusquedasActivity.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, GlosarioActivity.class);
                        break;
                    case 3:
                        randomBusqueda();
                        isIntent = false;
                        break;
                }
                if(isIntent) {startActivity(intent);}
            }
        });

//=================================== FLOATING ADD MENU LISTENERS ==================================

        floatingAddIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingAddMenu.collapse();

                Intent ideaIntent = new Intent(MainActivity.this, CreateIdeaActivity.class);
                startActivity(ideaIntent);
            }
        });

        floatingAddBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingAddMenu.collapse();

                Intent intent = new Intent(MainActivity.this, CreateBusquedaActivity.class);
                startActivity(intent);
            }
        });

        floatingAddScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingAddMenu.collapse();

                Intent intent = new Intent(MainActivity.this, CreateScreenshotActivity.class);
                startActivity(intent);
            }
        });

        floatingAddGlosario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingAddMenu.collapse();

                Intent intent = new Intent(MainActivity.this, CreateGlosarioActivity.class);
                startActivity(intent);
            }
        });

        floatingAddIndice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingAddMenu.collapse();

                //Intent AQUI!
            }
        });
    }


    public void randomBusqueda(){

        dialog = new Dialog(this);

        ArrayList<ArrayList<String>> busquedasOptions;

        //final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        busquedasOptions = queryCreator("SELECT * FROM busquedas");

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

    private ArrayList<ArrayList<String>> queryCreator(String query) {
        SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery(query, null);
        ArrayList<ArrayList<String>> array = new ArrayList<>();

        int titleIndex = c.getColumnIndex("title");
        int pathIndex = c.getColumnIndex("path");
        int descripcionIndex = c.getColumnIndex("descripcion");

        c.moveToFirst();

        while (!c.isAfterLast()) {
            //Crea arreglo de una idea con [titulo, tipo, idea] para poder agregar despuies al arreglo de ideas
            ArrayList<String> idea = new ArrayList<>();
            idea.add(c.getString(titleIndex));
            idea.add(c.getString(pathIndex));
            idea.add(c.getString(descripcionIndex));
            //Se agrega idea al arreglo de ideas
            array.add(idea);
            c.moveToNext();
        }

        return array;
    }
}
