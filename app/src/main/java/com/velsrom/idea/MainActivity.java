package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.velsrom.idea.creation.CreateIdeaActivity;
import com.velsrom.idea.creation.CreateScreenshotActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
    * TODO:
    *  - Crear activity para el anadir imagen, o bien, usar la actividad de aniadir idea/busqueda pero con una imagen ya precargada
    *  - Activifdad de anadir notas poner la opcion de aniadir imagen como 2da opcion para cargar SC
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

        floatingAddMenu = findViewById(R.id.floatingMenu);
        floatingAddIdea = findViewById(R.id.action_idea);
        floatingAddBusqueda = findViewById(R.id.action_investigacion);
        floatingAddScreenshot = findViewById(R.id.action_screenshot);
        floatingAddGlosario = findViewById(R.id.action_glosario);
        floatingAddIndice = findViewById(R.id.action_indice);

        ListView menuListView = findViewById(R.id.menuListView);

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
                Toast.makeText(getApplicationContext(), menuOptions.get(position), Toast.LENGTH_SHORT).show();

                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, IdeasActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Log.i("CASE NUMBER", "1");
                        break;
                    case 2:
                        Log.i("CASE NUMBER", "2");
                        intent = new Intent(MainActivity.this, GlosarioActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        Log.i("CASE NUMBER", "3");
                        break;
                    case 4:
                        Log.i("CASE NUMBER", "4");
                        break;
                }
            }
        });

//=================================== FLOATING ADD MENU LISTENERS ==================================

        floatingAddIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "idea cliked", Toast.LENGTH_SHORT).show();
                floatingAddMenu.collapse();

                //Ir a actividad donde puedas apuntar una idea/pensamiento/sueno
                Intent ideaIntent = new Intent(MainActivity.this, CreateIdeaActivity.class);
                startActivity(ideaIntent);
            }
        });

        floatingAddBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "busqueda clicked", Toast.LENGTH_SHORT).show();
                floatingAddMenu.collapse();

                //Ir a actividad dodne aniades una nota/recordatorio/que hacer despues
                Intent intent = new Intent(MainActivity.this, CreateBusquedaActivity.class);
                startActivity(intent);
            }
        });

        floatingAddScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "sc clicked", Toast.LENGTH_SHORT).show();
                floatingAddMenu.collapse();

                //Ir a actividad para screenshot. (actividad con nav egador y que al picar un boton se haga screenshot del cuadro del navegador
                Intent intent = new Intent(MainActivity.this, CreateScreenshotActivity.class);
                startActivity(intent);
            }
        });

        floatingAddGlosario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "sc glosario", Toast.LENGTH_SHORT).show();
                floatingAddMenu.collapse();

                //Ir a actividad para glosario. (actividad con nav egador y que al picar un boton se haga screenshot del cuadro del navegador
                //Intent intent = new Intent(MainActivity.this, IdeasActivity.class);
                //startActivity(intent);
            }
        });

        floatingAddIndice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "sc indice", Toast.LENGTH_SHORT).show();
                floatingAddMenu.collapse();

                //Ir a actividad para indice. (actividad con nav egador y que al picar un boton se haga screenshot del cuadro del navegador
                //Intent intent = new Intent(MainActivity.this, IdeasActivity.class);
                //startActivity(intent);
            }
        });
    }
}
