package com.velsrom.idea.creation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.velsrom.idea.IdeaDataBase;
import com.velsrom.idea.R;

import java.util.ArrayList;

public class CreateIdeaActivity extends AppCompatActivity {

    /*
    * TODO:
    *  - Hacer lista de tipos en otra seccion? Mas facil de editar
    *  - Que codigo no dependa de "hardcode" de lista
    *  -
    * */

    EditText titleEditText;
    Spinner typeSpinner;
    EditText ideaEditText;

    IdeaDataBase ideasDataBase;

    ArrayList<String> ideaTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_idea);

        titleEditText = findViewById(R.id.titleEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        ideaEditText = findViewById(R.id.descripcionEditText);


        ideaTypes = new ArrayList<>();
        ideaTypes.add("Seleccione tipo...");
        ideaTypes.add("Ideas"); //Como retos y apuestas
        ideaTypes.add("Actividades");
        ideaTypes.add("Invensiones");
        ideaTypes.add("Pensamientos");
        ideaTypes.add("Frases");
        ideaTypes.add("Otro");

        SQLiteDatabase database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);

        String[] columns = {"title", "type", "idea"};
        ArrayList<String> nameTypes= new ArrayList();

        ideasDataBase = new IdeaDataBase(database, "ideas", columns, nameTypes);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ideaTypes);
        typeSpinner.setAdapter(arrayAdapter);

    }

    public void saveIdea(View view){
        if(!titleEditText.getText().toString().equals("") &&
                typeSpinner.getSelectedItemPosition() != 0 &&
                !ideaEditText.getText().toString().equals(""))
        {
            try {
                String[] dataForDatabase = {
                        titleEditText.getText().toString(),
                        typeSpinner.getSelectedItem().toString(),
                        ideaEditText.getText().toString()};
                ideasDataBase.addData(dataForDatabase);
                //ideasDataBase.getData();

                Toast.makeText(getApplicationContext(), "Idea saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Llenar los datos correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelIdea(View view){
        finish();
    }
}
