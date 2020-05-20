package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    * */

    ArrayList<String> menuOptions;

    FloatingActionsMenu floatingAddMenu;
    FloatingActionButton floatingAddIdea;
    FloatingActionButton floatingAddBusqueda;
    FloatingActionButton floatingAddScreenshot;
    FloatingActionButton floatingAddGlosario;
    FloatingActionButton floatingAddIndice;

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

        Database.execSQL("CREATE TABLE IF NOT EXISTS ideas (title VARCHAR, path VARCHAR, descripcion VARCHAR)");
        Database.execSQL("CREATE TABLE IF NOT EXISTS busquedas (title VARCHAR, path VARCHAR, descripcion VARCHAR)");
        Database.execSQL("CREATE TABLE IF NOT EXISTS glosario (title VARCHAR, path VARCHAR, descripcion VARCHAR)");

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
        menuOptions.add("Indice");
        menuOptions.add("Idea aleatoria (Proximamente)");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuOptions);
        menuListView.setAdapter(adapter);

        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
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
                        Toast.makeText(getApplicationContext(), "Clicked #3", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getApplicationContext(), "Clicked #4", Toast.LENGTH_SHORT).show();
                        break;
                }
                startActivity(intent);
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
}
