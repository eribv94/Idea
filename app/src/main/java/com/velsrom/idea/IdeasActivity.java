package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.velsrom.idea.creation.CreateIdeaActivity;

import java.util.ArrayList;

public class IdeasActivity extends AppCompatActivity {

    /*
    * TODO:
    *  * Mejorar el mostrar idea y su descripcion (tal vez con cuadro flotante?)
    *  - Agregar fechas para poder ordenar las ideas?
    *  - Agregar id para referencias?
    *  - Anadir seccion de editar (usar CreateIdeaActivity, en este caso)
    *  - Agregar a base de datos una lista de "tipos" para su mejor busqueda, en lugar de agregarlos aqui con "hardcode"
    *
    * */

//    ListView ideasListView;
//    Spinner typeSpinner;
//
//    ArrayList<ArrayList<String>> ideasOptions;
//    ArrayAdapter<String> ideasAdapter;
//    ArrayList<String> adapterListTitles = new ArrayList<>();
//    ArrayList<String> spinnerListTitles = new ArrayList<>();

    QueryCreator queryCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ideas);
        getSupportActionBar().hide();


//        ideasOptions = new ArrayList<>();

//        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
//        queryCreator = new QueryCreator(Database);
//        ideasOptions = queryCreator.createQuery("SELECT * FROM ideas");

//        adapterListTitles = new ArrayList<>();
//        for(ArrayList<String> array : ideasOptions){
//            adapterListTitles.add(array.get(0));
//        }

//        ideasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), ideasOptions.get(position).get(0), Toast.LENGTH_SHORT).show();
//
//                //SELECCIONAR IDEA
//            }
//        });
//
//        ideasListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                //BORRAR IDEA/MENU DE QUE HACER CON IDEA (editar, eliminar)
//                try {
//                    Database.delete("ideas", "title = " + "\'" + ideasOptions.get(position).get(0) + "\'", null);
//                    adapterListTitles.remove(position);
//                    ideasAdapter.notifyDataSetChanged();
//                    Toast.makeText(getApplicationContext(), "Idea eliminada", Toast.LENGTH_SHORT).show();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                return true;
//            }
//        });
    }

    public void ideaTypeSelected(View view){
        int viewTag = Integer.parseInt(view.getTag().toString());

        //MANDAR A LA ACTIVIDAD CON LAS IDEAS DEL CUADRO SELECCIONADO

        switch (viewTag){
            case 0: //Ideas
                //ideasOptions = queryCreator.createQuery("SELECT * FROM ideas WHERE type = \"Ideas\"");
                Toast.makeText(this, "Ideas", Toast.LENGTH_SHORT).show();
                break;
            case 1: //Actividades
                //ideasOptions = queryCreator.createQuery("SELECT * FROM ideas WHERE type = \"Actividades\"");
                Toast.makeText(this, "Actividades", Toast.LENGTH_SHORT).show();
                break;
            case 2: //Invensiones
                //ideasOptions = queryCreator.createQuery("SELECT * FROM ideas WHERE type = \"Invensiones\"");
                Toast.makeText(this, "Invensiones", Toast.LENGTH_SHORT).show();
                break;
            case 3: //Pensamientos
                //ideasOptions = queryCreator.createQuery("SELECT * FROM ideas WHERE type = \"Pensamientos\"");
                Toast.makeText(this, "Pensamientos", Toast.LENGTH_SHORT).show();
                break;
            case 4: //Frases
                //ideasOptions = queryCreator.createQuery("SELECT * FROM ideas WHERE type = \"Frases\"");
                Toast.makeText(this, "Frases", Toast.LENGTH_SHORT).show();
                break;
            case 5: //Otros
                //ideasOptions = queryCreator.createQuery("SELECT * FROM ideas");
                Toast.makeText(this, "Otros", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void addIdea(View view){
        Intent intent = new Intent(getApplicationContext(), CreateIdeaActivity.class);
        startActivity(intent);
    }
}
