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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideas);

        ideasListView = findViewById(R.id.ideasListView);
        typeSpinner = findViewById(R.id.typesSpinner);

        final ArrayList<ArrayList<String>> ideasOptions = new ArrayList<>();

        SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
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

        ArrayList<String> listTitlesForAdapter = new ArrayList<>();
        ArrayList<String> listTitlesForSpinner = new ArrayList<>();
        listTitlesForSpinner.add("Ideas");
        listTitlesForSpinner.add("Actividades");
        listTitlesForSpinner.add("Invensiones");
        listTitlesForSpinner.add("Pensamientos");
        listTitlesForSpinner.add("Frases");

        for(ArrayList<String> array : ideasOptions){
            listTitlesForAdapter.add(array.get(0));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTitlesForAdapter);
        ideasListView.setAdapter(adapter);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listTitlesForSpinner);
        typeSpinner.setAdapter(arrayAdapter);

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
                Toast.makeText(getApplicationContext(), ideasOptions.get(position).get(2), Toast.LENGTH_SHORT).show();

                //BORRAR IDEA/MENU DE QUE HACER CON IDEA (editar, eliminar)
                return true;
            }
        });
    }


}
