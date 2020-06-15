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
    *  - Hacer map "ideasTypes" para poder sacar id mas facil y seleccinar en el spinner mas facil
    * */

    EditText titleEditText;
    Spinner typeSpinner;
    EditText ideaEditText;

    IdeaDataBase ideasDataBase;

    ArrayList<String> ideaTypes;

    boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_idea);

        String type = getIntent().getStringExtra("IDEA_TYPE");
        String editRowName = getIntent().getStringExtra("EDIT_ROW_NAME");
        String editRowText = getIntent().getStringExtra("EDIT_ROW_TEXT");
        isEdit = getIntent().getBooleanExtra("IS_EDIT", false);

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

        SQLiteDatabase database = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);

        String[] columns = {"title", "type", "idea"};
        ArrayList<String> nameTypes= new ArrayList();

        ideasDataBase = new IdeaDataBase(database, "ideas", columns, nameTypes);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ideaTypes);
        typeSpinner.setAdapter(arrayAdapter);

        switch (type){  //Tratar de hacerlo MAP
            case "Ideas": //Ideas
                typeSpinner.setSelection(1);
                break;
            case "Actividades": //Actividades
                typeSpinner.setSelection(2);
                break;
            case "Invensiones": //Invensiones
                typeSpinner.setSelection(3);
                break;
            case "Pensamientos": //Pensamientos
                typeSpinner.setSelection(4);
                break;
            case "Frases": //Frases
                typeSpinner.setSelection(5);
                break;
            case "Otros": //Otros
                typeSpinner.setSelection(6);
                break;
        }

        if(isEdit){
            titleEditText.setText(editRowName);
            titleEditText.setEnabled(false);
            ideaEditText.setText(editRowText);
        }
    }

    public void saveIdea(View view){
        if(!titleEditText.getText().toString().equals("") &&
                typeSpinner.getSelectedItemPosition() != 0 &&
                !ideaEditText.getText().toString().equals(""))
        {
            try {
                if(isEdit){
                    ideasDataBase.editRow( titleEditText.getText().toString(), ideaEditText.getText().toString());
                }else {
                    String[] dataForDatabase = {
                            titleEditText.getText().toString(),
                            typeSpinner.getSelectedItem().toString(),
                            ideaEditText.getText().toString()};
                    ideasDataBase.addData(dataForDatabase);

                    Toast.makeText(getApplicationContext(), "Idea saved", Toast.LENGTH_SHORT).show();
                }
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
