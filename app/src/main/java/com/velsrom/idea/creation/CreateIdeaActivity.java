package com.velsrom.idea.creation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.velsrom.idea.R;

import java.util.ArrayList;

public class CreateIdeaActivity extends AppCompatActivity {

    EditText titleEditText;
    Spinner typeSpinner;
    EditText ideaEditText;

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


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ideaTypes);
        typeSpinner.setAdapter(arrayAdapter);

    }

    public void saveIdea(View view){
        if(!titleEditText.getText().toString().equals("") &&
                typeSpinner.getSelectedItemPosition() != 0 &&
                !ideaEditText.getText().toString().equals(""))
        {
            try {
                SQLiteDatabase ideasDatabase = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);

                ideasDatabase.execSQL("CREATE TABLE IF NOT EXISTS ideas (title VARCHAR, type VARCHAR, idea VARCHAR)");

                ContentValues cv = new ContentValues();
                cv.put("title", titleEditText.getText().toString());
                cv.put("type", typeSpinner.getSelectedItem().toString());
                cv.put("idea", ideaEditText.getText().toString());
                ideasDatabase.insert("ideas", null, cv);

                Cursor c = ideasDatabase.rawQuery("SELECT * FROM ideas", null);

                int titleIndex = c.getColumnIndex("title");
                int typeIndex = c.getColumnIndex("type");
                int ideaIndex = c.getColumnIndex("idea");

                c.moveToFirst();

                while (!c.isAfterLast()) {
                    Log.i("title", c.getString(titleIndex));
                    Log.i("type", c.getString(typeIndex));
                    Log.i("idea", c.getString(ideaIndex));
                    c.moveToNext();
                }

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
