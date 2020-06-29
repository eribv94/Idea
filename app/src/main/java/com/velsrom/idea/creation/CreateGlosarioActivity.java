package com.velsrom.idea.creation;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.velsrom.idea.IdeaDataBase;
import com.velsrom.idea.R;

import java.util.ArrayList;

public class CreateGlosarioActivity extends AppCompatActivity {

    EditText wordEditText, descripcionEditText;
    String word, definicion;
    int id;
    boolean isEdit = false;

    IdeaDataBase glosarioDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_glosario);

        word = getIntent().getStringExtra("WORD");
        definicion = getIntent().getStringExtra("DEFINICION");
        id = getIntent().getIntExtra("ID", -1);

        if(id != -1){
            isEdit = true;
        }

        wordEditText = findViewById(R.id.wordEditText);
        descripcionEditText = findViewById(R.id.descripcionEditText);

        SQLiteDatabase database = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);

        String[] columns = {"palabra", "definicion", "id"};
        ArrayList<String> nameTypes= new ArrayList();

        glosarioDataBase = new IdeaDataBase(database, "glosario", columns, nameTypes);

        if(isEdit){
            wordEditText.setText(word);
            descripcionEditText.setText(definicion);
        }
    }

    public void saveWord(View view){

        String palabra = wordEditText.getText().toString();
        String capitalizedPalabra = palabra.substring(0, 1).toUpperCase() + palabra.substring(1);
        String definicionPalabra = descripcionEditText.getText().toString();

        if (!palabra.equals("") && !definicionPalabra.equals("")) {
            if(isEdit) {
                glosarioDataBase.editRow(palabra, definicionPalabra, id);
            }else {
                try {
                    String[] dataForDatabase = {capitalizedPalabra, definicionPalabra};
                    glosarioDataBase.addData(dataForDatabase);

                    Toast.makeText(getApplicationContext(), "Word saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Llenar los datos correctamente", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View view){
        finish();
    }
}
