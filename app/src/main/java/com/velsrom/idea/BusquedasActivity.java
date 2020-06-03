package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class BusquedasActivity extends AppCompatActivity {

    ListView busquedasListView;

    ArrayList<ArrayList<String>> busquedasOptions;
    ArrayAdapter<String> busquedasAdapter;
    ArrayList<String> adapterListTitles = new ArrayList<>();

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busquedas);

        busquedasListView = findViewById(R.id.busquedaListView);

        dialog = new Dialog(this);

        busquedasOptions = new ArrayList<>();

        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        busquedasOptions = queryCreator("SELECT * FROM busquedas");

        adapterListTitles = new ArrayList<>();
        for(ArrayList<String> array : busquedasOptions){
            adapterListTitles.add(array.get(0));
        }
        busquedasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adapterListTitles);
        busquedasListView.setAdapter(busquedasAdapter);

        busquedasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), busquedasOptions.get(position).get(0), Toast.LENGTH_SHORT).show();

                openDialog(busquedasOptions.get(position).get(0));
                //SELECCIONAR IDEA
            }
        });

        busquedasListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Database.delete("busquedas", "title = " + "\'" + busquedasOptions.get(position).get(0) + "\'", null);
                    adapterListTitles.remove(position);
                    busquedasAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Busqueda eliminada", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    public void openDialog(String word){
        //SI TIENE IMAGEN, QUE LA MUESTRE TAMBIEN
        final SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery("SELECT * FROM busquedas WHERE title = \'" + word + "\' ", null);

        int descripcionIdx = c.getColumnIndex("descripcion");
        int pathIdx = c.getColumnIndex("path");
        c.moveToFirst();
        String descripcion = c.getString(descripcionIdx);
        String path = c.getString(pathIdx);

        Context context = this; //???
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView titleBox = new TextView(context);
        titleBox.setText(descripcion);
        layout.addView(titleBox);

        final ImageView image = new ImageView(context);

        if(!path.equals("")){
            Bitmap myBitmap = BitmapFactory.decodeFile(path);
            image.setImageBitmap(myBitmap);
            layout.addView(image);
        }

        dialog.setContentView(layout);
        dialog.show();

    }

    private ArrayList<ArrayList<String>> queryCreator(String query) {
        SQLiteDatabase Database = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery(query, null);
        ArrayList<ArrayList<String>> array = new ArrayList<>();

        int titleIndex = c.getColumnIndex("title");
        int pathIndex = c.getColumnIndex("path");
        int descripcionIndex = c.getColumnIndex("descripcion");

        c.moveToFirst();

        while (!c.isAfterLast()) {
                //Crea arreglo de una idea con [titulo, tipo, idea] para poder agregar despuies al arreglo de ideas
            ArrayList<String> idea = new ArrayList<>();
            idea.add(c.getString(titleIndex));
            idea.add(c.getString(pathIndex));
            idea.add(c.getString(descripcionIndex));
                //Se agrega idea al arreglo de ideas
            array.add(idea);
            c.moveToNext();
        }

        return array;
    }


}
