package com.velsrom.idea.creation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.velsrom.idea.IdeaDataBase;
import com.velsrom.idea.R;

import java.util.ArrayList;

public class CreateGlosarioActivity extends AppCompatActivity {

    EditText wordEditText, descripcionEditText;

    IdeaDataBase glosarioDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_glosario);

        wordEditText = findViewById(R.id.wordEditText);
        descripcionEditText = findViewById(R.id.descripcionEditText);

        SQLiteDatabase database = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);

        String[] columns = {"palabra", "definicion"};
        ArrayList<String> nameTypes= new ArrayList();

        glosarioDataBase = new IdeaDataBase(database, "glosario", columns, nameTypes);
    }

    public void saveWord(View view){
        String word = wordEditText.getText().toString();
        String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
        String definicion = descripcionEditText.getText().toString();

        if(!word.equals("") && !definicion.equals(""))
        {
            try {
                String[] dataForDatabase = {capitalizedWord, definicion};
                glosarioDataBase.addData(dataForDatabase);
                glosarioDataBase.getData();

                Toast.makeText(getApplicationContext(), "Word saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Llenar los datos correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view){
        finish();
    }
}
