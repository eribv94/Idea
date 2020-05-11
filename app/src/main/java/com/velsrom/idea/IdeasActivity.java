package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

    ListView ideasListView;
    Spinner typeSpinner;

    ArrayList<ArrayList<String>> ideasOptions;
    ArrayAdapter<String> ideasAdapter;
    ArrayList<String> adapterListTitles = new ArrayList<>();
    ArrayList<String> spinnerListTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas);

        ideasListView = findViewById(R.id.ideasListView);
        typeSpinner = findViewById(R.id.typesSpinner);

        ideasOptions = new ArrayList<>();

        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        ideasOptions = queryCreator("SELECT * FROM ideas");

        adapterListTitles = new ArrayList<>();
        for(ArrayList<String> array : ideasOptions){
            adapterListTitles.add(array.get(0));
        }
        ideasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adapterListTitles);
        ideasListView.setAdapter(ideasAdapter);

        spinnerListTitles = new ArrayList<>();
        spinnerListTitles.add("Todo");
        spinnerListTitles.add("Ideas");
        spinnerListTitles.add("Actividades");
        spinnerListTitles.add("Invensiones");
        spinnerListTitles.add("Pensamientos");
        spinnerListTitles.add("Frases");

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerListTitles);
        typeSpinner.setAdapter(spinnerAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //Seleccion de filtro en ideas
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                ideasOptions.clear();

                switch (position){
                    case 0: //Todas
                        ideasOptions = queryCreator("SELECT * FROM ideas");
                        break;
                    case 1: //Ideas
                        ideasOptions = queryCreator("SELECT * FROM ideas WHERE type = \"Ideas\"");
                        break;
                    case 2: //Actividades
                        ideasOptions = queryCreator("SELECT * FROM ideas WHERE type = \"Actividades\"");
                        break;
                    case 3: //Invensiones
                        ideasOptions = queryCreator("SELECT * FROM ideas WHERE type = \"Invensiones\"");
                        break;
                    case 4: //Pensamientos
                        ideasOptions = queryCreator("SELECT * FROM ideas WHERE type = \"Pensamientos\"");
                        break;
                    case 5: //Frases
                        ideasOptions = queryCreator("SELECT * FROM ideas WHERE type = \"Frases\"");
                        break;
                }

                adapterListTitles.clear();
                for(ArrayList<String> array : ideasOptions){
                    adapterListTitles.add(array.get(0));
                }
                ideasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { }
        });

        ideasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ideasOptions.get(position).get(0), Toast.LENGTH_SHORT).show();

                //SELECCIONAR IDEA
            }
        });

        ideasListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //BORRAR IDEA/MENU DE QUE HACER CON IDEA (editar, eliminar)
                try {
                    Database.delete("ideas", "title = " + "\'" + ideasOptions.get(position).get(0) + "\'", null);
                    adapterListTitles.remove(position);
                    ideasAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Idea eliminada", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private ArrayList<ArrayList<String>> queryCreator(String query) {
        SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery(query, null);
        ArrayList<ArrayList<String>> array = new ArrayList<>();

        int titleIndex = c.getColumnIndex("title");
        int typeIndex = c.getColumnIndex("type");
        int ideaIndex = c.getColumnIndex("idea");

        c.moveToFirst();

        while (!c.isAfterLast()) {
            //Crea arreglo de una idea con [titulo, tipo, idea] para poder agregar despuies al arreglo de ideas
            ArrayList<String> idea = new ArrayList<>();
            idea.add(c.getString(titleIndex));
            idea.add(c.getString(typeIndex));
            idea.add(c.getString(ideaIndex));
            //Se agrega idea al arreglo de ideas
            array.add(idea);
            c.moveToNext();
        }

        return array;
    }


}
