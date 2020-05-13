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

import com.velsrom.idea.R;

public class CreateGlosarioActivity extends AppCompatActivity {

    EditText wordEditText, descripcionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_glosario);

        wordEditText = findViewById(R.id.wordEditText);
        descripcionEditText = findViewById(R.id.descripcionEditText);
    }

    public void saveWord(View view){
        String word = wordEditText.getText().toString();
        String definicion = descripcionEditText.getText().toString();

        if(!word.equals("") && !definicion.equals(""))
        {
            try {
                SQLiteDatabase ideasDatabase = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);

                ideasDatabase.execSQL("CREATE TABLE IF NOT EXISTS glosario (palabra VARCHAR, definicion VARCHAR)");

                ContentValues cv = new ContentValues();
                String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
                cv.put("palabra", capitalizedWord);
                cv.put("definicion", definicion);
                ideasDatabase.insert("glosario", null, cv);

                Cursor c = ideasDatabase.rawQuery("SELECT * FROM ideas", null);

                int titleIndex = c.getColumnIndex("palabra");
                int ideaIndex = c.getColumnIndex("definicion");

                c.moveToFirst();

                while (!c.isAfterLast()) {
                    Log.i("palabra", c.getString(titleIndex));
                    Log.i("definicion", c.getString(ideaIndex));
                    c.moveToNext();
                }

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
