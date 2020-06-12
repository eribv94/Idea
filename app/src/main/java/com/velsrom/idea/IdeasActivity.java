package com.velsrom.idea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.velsrom.idea.creation.CreateIdeaActivity;

import java.util.ArrayList;

public class IdeasActivity extends AppCompatActivity {
    /*
     * TODO:
     *  * Mejorar el mostrar idea y su descripcion (tal vez con cuadro flotante?)
     *  - ????:
     *  - Agregar fechas para poder ordenar las ideas   ????
     *  - Agregar id para referencias                   ????
     *  - Agregar a base de datos una lista de "tipos" para su mejor busqueda, en lugar de agregarlos aqui con "hardcode"  ????
     *
     * */

    ListView ideasListView;

    ArrayList<ArrayList<String>> ideasOptions;
    ArrayAdapter<String> ideasAdapter;
    ArrayList<String> adapterListTitles;

    String ideaType;
    String q;

    IdeaDataBase ideaDataBase;

    private static final int CREATE_IDEA_IN_IDEA_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas);

        TextView ideaTypeTextView = findViewById(R.id.ideaTypeTextView);
        ideasListView = findViewById(R.id.dataTypeListView);
        registerForContextMenu(ideasListView);

        ideaType = getIntent().getStringExtra("IDEA_TYPE");

        ideaTypeTextView.setText(ideaType);

        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        ideaDataBase = new IdeaDataBase(Database);

        q = null;
        switch (ideaType){
            case "Ideas": //Ideas
                q = "SELECT * FROM ideas WHERE type = \"Ideas\"";
                break;
            case "Actividades": //Actividades
                q = "SELECT * FROM ideas WHERE type = \"Actividades\"";
                break;
            case "Invensiones": //Invensiones
                q = "SELECT * FROM ideas WHERE type = \"Invensiones\"";
                break;
            case "Pensamientos": //Pensamientos
                q = "SELECT * FROM ideas WHERE type = \"Pensamientos\"";
                break;
            case "Frases": //Frases
                q = "SELECT * FROM ideas WHERE type = \"Frases\"";
                break;
            case "Otros": //Otros
                q = "SELECT * FROM ideas WHERE type = \"Otros\"";
                break;
        }

        ideasOptions = new ArrayList<>();
        adapterListTitles = new ArrayList<>();
        ideasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adapterListTitles);
        ideasListView.setAdapter(ideasAdapter);

        updateDataInListView();

        ideasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ideasOptions.get(position).get(0), Toast.LENGTH_SHORT).show();

                //SELECCIONAR IDEA, MOSTRAR SU INFO
            }
        });
    }

    public void updateDataInListView(){
        ideasOptions.clear();
        adapterListTitles.clear();

        if(q != null) {
            ideasOptions = ideaDataBase.createQuery(q);
            for(ArrayList<String> array : ideasOptions){
                adapterListTitles.add(array.get(0));
            }

            ideasAdapter.notifyDataSetChanged();
        }
        else {
            Log.i("-- DATA BASE REQUEST --", "FAILED IN SEARCH FOR IDEA TYPE FOR ACTIVITY in IdeasActivity.java");
            finish();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.longpress_options, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        View view = info.targetView;

        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(getApplicationContext(), "Edit idea", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                try {
                    ideaDataBase.deleteRow(ideasOptions.get(index).get(0));
                    adapterListTitles.remove(index);
                    ideasAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Idea eliminada", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void addIdea(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateIdeaActivity.class);
        intent.putExtra("IDEA_TYPE", ideaType);
        startActivityForResult(intent, CREATE_IDEA_IN_IDEA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CREATE_IDEA_IN_IDEA_REQUEST){
            updateDataInListView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
