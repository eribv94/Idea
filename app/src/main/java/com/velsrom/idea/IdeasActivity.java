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

    ListView ideasListView;
    Spinner typeSpinner;

    ArrayAdapter<String> ideasAdapter;
    ArrayList<String> listTitlesForAdapter = new ArrayList<>();
    ArrayList<String> listTitlesForSpinner = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas);

        ideasListView = findViewById(R.id.ideasListView);
        typeSpinner = findViewById(R.id.typesSpinner);

        final ArrayList<ArrayList<String>> ideasOptions = new ArrayList<>();

        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery("SELECT * FROM ideas", null);

        int titleIndex = c.getColumnIndex("title");
        int typeIndex = c.getColumnIndex("type");
        int ideaIndex = c.getColumnIndex("idea");

        c.moveToFirst();

        while (!c.isAfterLast()) {
            ArrayList<String> idea = new ArrayList<>();
            idea.add(c.getString(titleIndex));
            idea.add(c.getString(typeIndex));
            idea.add(c.getString(ideaIndex));
            ideasOptions.add(idea);
            c.moveToNext();
        }

        listTitlesForAdapter = new ArrayList<>();
        listTitlesForSpinner = new ArrayList<>();
        listTitlesForSpinner.add("Todo");
        listTitlesForSpinner.add("Ideas");
        listTitlesForSpinner.add("Actividades");
        listTitlesForSpinner.add("Invensiones");
        listTitlesForSpinner.add("Pensamientos");
        listTitlesForSpinner.add("Frases");

        for(ArrayList<String> array : ideasOptions){
            listTitlesForAdapter.add(array.get(0));
        }

        ideasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTitlesForAdapter);
        ideasListView.setAdapter(ideasAdapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listTitlesForSpinner);
        typeSpinner.setAdapter(spinnerAdapter);

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
                    listTitlesForAdapter.remove(position);

                    ideasAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Idea eliminada", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });
    }


}
