package com.velsrom.idea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.velsrom.idea.creation.CreateGlosarioActivity;
import com.velsrom.idea.creation.CreateIdeaActivity;

import java.util.ArrayList;

public class GlosarioActivity extends AppCompatActivity {

    /*
    * FIXME:
    *   - Ver como el pop up puede hacer scroll
    *   - Edicion de palabras (editar definicion)
    * */

    public static final int EDIT_GLOSARIO_REQUEST = 1001;
    public static final int ADD_GLOSARIO_REQUEST = 1002;

    TextView[] selectedSection;
    ListView glosarioLV;

    IdeaDataBase ideaDataBase;

    ArrayAdapter<String> wordsAdapter;
    ArrayList<ArrayList<String>> wordsArray;
    ArrayList<String> palabraArray;

    String lastQuery;

    int currentSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_glosario);

        glosarioLV = findViewById(R.id.glosarioListView);
        selectedSection = new TextView[]{
                findViewById(R.id.aeTextView),
                findViewById(R.id.fjTextView),
                findViewById(R.id.koTextView),
                findViewById(R.id.ptTextView),
                findViewById(R.id.uzTextView)
        };

        final SQLiteDatabase Database = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);
        String[] columns = {"palabra", "definicion", "id"};
        ArrayList<String> nameTypes= new ArrayList();
        ideaDataBase = new IdeaDataBase(Database, "glosario", columns, nameTypes);

        palabraArray = new ArrayList<>();
        lastQuery = "SELECT * FROM glosario WHERE (palabra BETWEEN 'A%'  AND 'E%') OR palabra LIKE 'E%' ORDER BY palabra ASC";
        wordsArray = ideaDataBase.getAllElements(lastQuery);
        for(ArrayList<String> array : wordsArray){
            palabraArray.add(array.get(0));
        }

        wordsAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, palabraArray);
        glosarioLV.setAdapter(wordsAdapter);

        glosarioLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent definicionIntent = new Intent(getApplicationContext(), PalabraGlosarioActivity.class);
                definicionIntent.putExtra("WORD", palabraArray.get(position));
                definicionIntent.putExtra("DEFINICION", wordsArray.get(position).get(1));
                definicionIntent.putExtra("ID", Integer.valueOf(wordsArray.get(position).get(2)));
                startActivity(definicionIntent);
            }
        });

        registerForContextMenu(glosarioLV);
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
        final int idx = info.position;
        View view = info.targetView;

        switch (item.getItemId()) {
            case R.id.edit:
                Intent editIntent = new Intent(getApplicationContext(), CreateGlosarioActivity.class);
                editIntent.putExtra("ID", Integer.valueOf(wordsArray.get(idx).get(2)));
                editIntent.putExtra("WORD", wordsArray.get(idx).get(0));
                editIntent.putExtra("DEFINICION", wordsArray.get(idx).get(1));
                startActivityForResult(editIntent, EDIT_GLOSARIO_REQUEST);
                return true;
            case R.id.delete:
                try {
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Word")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ideaDataBase.deleteRow(palabraArray.get(idx));
                                    palabraArray.remove(idx);
                                    wordsAdapter.notifyDataSetChanged();

                                    Toast.makeText(getApplicationContext(), "Palabra eliminada", Toast.LENGTH_SHORT).show();
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();

                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void addPalabra(View view){
        Intent addIntent = new Intent(getApplicationContext(), CreateGlosarioActivity.class);
        startActivityForResult(addIntent, ADD_GLOSARIO_REQUEST);
    }

    public void updateDataInListView(){
        wordsArray.clear();
        palabraArray.clear();
        wordsArray = ideaDataBase.getAllElements(lastQuery);  //Dar el query para que busque otra vez
        for(ArrayList<String> array : wordsArray){
            palabraArray.add(array.get(0));
        }
        wordsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == ADD_GLOSARIO_REQUEST){
            updateDataInListView();
        }
        else if(requestCode == EDIT_GLOSARIO_REQUEST){
            updateDataInListView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickLetters(View view){
        int selected = Integer.valueOf(view.getTag().toString());
        selectedSection[currentSelected].setBackgroundColor(Color.WHITE);
        selectedSection[currentSelected].setTextColor(Color.BLACK);
        selectedSection[selected].setBackgroundColor(Color.GRAY);
        selectedSection[selected].setTextColor(Color.WHITE);
        currentSelected = selected;

        lastQuery = "SELECT * FROM glosario WHERE (palabra BETWEEN";

        switch (view.getTag().toString()){
            case "0":
                lastQuery += " 'A%'  AND 'E%') OR palabra LIKE 'E%'";
                break;
            case "1":
                lastQuery += " 'F%'  AND 'J%') OR palabra LIKE 'J%'";
                break;
            case "2":
                lastQuery += " 'K%'  AND 'O%') OR palabra LIKE 'O%'";
                break;
            case "3":
                lastQuery += " 'P%'  AND 'T%') OR palabra LIKE 'T%'";
                break;
            case "4":
                lastQuery += " 'U%'  AND 'Z%') OR palabra LIKE 'Z%'";
                break;
        }

        lastQuery += "ORDER BY palabra ASC";
        wordsArray.clear();
        palabraArray.clear();
        wordsArray = ideaDataBase.getAllElements(lastQuery);
        for(ArrayList<String> array : wordsArray){
            palabraArray.add(array.get(0));
        }
        wordsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDataInListView();
    }
}
