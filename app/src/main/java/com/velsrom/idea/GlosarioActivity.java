package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GlosarioActivity extends AppCompatActivity {

    /*
    * FIXME:
    *   - Ver como el pop up puede hacer scroll
    *   - Edicion de palabras (editar definicion)
    * */

    TextView[] selectedSection;
    ListView glosarioLV;

    ArrayAdapter<String> wordsAdapter;
    ArrayList<String> wordsArray;

    Dialog dialog;

    int currentSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glosario);

        dialog = new Dialog(this);

        glosarioLV = findViewById(R.id.glosarioListView);
        selectedSection = new TextView[]{
                findViewById(R.id.aeTextView),
                findViewById(R.id.fjTextView),
                findViewById(R.id.koTextView),
                findViewById(R.id.ptTextView),
                findViewById(R.id.uzTextView)
        };

        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);

        wordsArray = getWords("SELECT * FROM glosario WHERE (palabra BETWEEN 'A%'  AND 'E%') OR palabra LIKE 'E%' ORDER BY palabra ASC");

        wordsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, wordsArray);
        glosarioLV.setAdapter(wordsAdapter);

        glosarioLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), wordsArray.get(position), Toast.LENGTH_SHORT).show();

                openDialog(wordsArray.get(position));
            }
        });

        glosarioLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //BORRAR IDEA/MENU DE QUE HACER CON IDEA (editar, eliminar)
                try {
                    Database.delete("glosario", "palabra = " + "\'" + wordsArray.get(position)+ "\'", null);
                    wordsArray.remove(position);
                    wordsAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Palabra eliminada", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    public void openDialog(String word){
        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery("SELECT * FROM glosario WHERE palabra = \'" + word + "\' ", null);

        int definicionIdx = c.getColumnIndex("definicion");
        c.moveToFirst();
        String definicion = c.getString(definicionIdx);

        TextView dialogText;
        dialog.setContentView(R.layout.layout_dialog);
        dialogText = dialog.findViewById(R.id.definicionTextView);
        dialogText.setText(definicion);
        dialog.show();

    }

    public void onClickLetters(View view){
        int selected = Integer.valueOf(view.getTag().toString());
        selectedSection[currentSelected].setBackgroundColor(Color.WHITE);
        selectedSection[currentSelected].setTextColor(Color.BLACK);
        selectedSection[selected].setBackgroundColor(Color.GRAY);
        selectedSection[selected].setTextColor(Color.WHITE);
        currentSelected = selected;

        String query = "SELECT * FROM glosario WHERE (palabra BETWEEN";

        switch (view.getTag().toString()){
            case "0":
                query += " 'A%'  AND 'E%') OR palabra LIKE 'E%'";
                break;
            case "1":
                query += " 'F%'  AND 'J%') OR palabra LIKE 'J%'";
                break;
            case "2":
                query += " 'K%'  AND 'O%') OR palabra LIKE 'O%'";
                break;
            case "3":
                query += " 'P%'  AND 'T%') OR palabra LIKE 'T%'";
                break;
            case "4":
                query += " 'U%'  AND 'Z%') OR palabra LIKE 'Z%'";
                break;
        }

        query += "ORDER BY palabra ASC";
        wordsArray.clear();
        wordsArray.addAll(getWords(query));
//        adapterWordsList = getWords(query);     //Por que no se podra?
        wordsAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> getWords(String query){
        SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery(query, null);
        ArrayList<String> array = new ArrayList<>();

        int palabraIndex = c.getColumnIndex("palabra");

        c.moveToFirst();

        while (!c.isAfterLast()) {
            array.add(c.getString(palabraIndex));
            c.moveToNext();
        }

        return array;
    }
}
