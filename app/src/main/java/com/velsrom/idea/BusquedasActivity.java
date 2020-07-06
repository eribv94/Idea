package com.velsrom.idea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.velsrom.idea.creation.CreateBusquedaActivity;
import com.velsrom.idea.creation.CreateGlosarioActivity;

import java.io.File;
import java.util.ArrayList;

public class BusquedasActivity extends AppCompatActivity {

    public static final int EDIT_BUSQUEDA_REQUEST = 1001;
    public static final int ADD_BUSQUEDA_REQUEST = 1002;
    public static final int SHOW_BUSQUEDA_REQUEST = 1003;

    ListView busquedasListView;

    ArrayList<ArrayList<String>> busquedasOptions;
    ArrayAdapter<String> busquedasAdapter;
    ArrayList<String> adapterListTitles = new ArrayList<>();

    Dialog dialog;

    IdeaDataBase ideaDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_busquedas);

        busquedasListView = findViewById(R.id.busquedaListView);

        dialog = new Dialog(this);

        busquedasOptions = new ArrayList<>();

        final SQLiteDatabase Database = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);
        String[] columns = {"title", "path", "descripcion", "id"};
        ArrayList<String> nameTypes= new ArrayList();
        ideaDataBase = new IdeaDataBase(Database, "busquedas", columns, nameTypes);
        busquedasOptions = ideaDataBase.getAllElements("SELECT * FROM busquedas");

        adapterListTitles = new ArrayList<>();
        for(ArrayList<String> array : busquedasOptions){
            adapterListTitles.add(array.get(0));
        }
        busquedasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adapterListTitles);
        busquedasListView.setAdapter(busquedasAdapter);

        busquedasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent showIdeaIntent = new Intent(getApplicationContext(), ShowIdeaActivity.class);
                showIdeaIntent.putExtra("TITLE", busquedasOptions.get(position).get(0));
                showIdeaIntent.putExtra("TEXT", busquedasOptions.get(position).get(2));
                showIdeaIntent.putExtra("IMAGE_PATH", busquedasOptions.get(position).get(1));
                showIdeaIntent.putExtra("ID", Integer.valueOf(busquedasOptions.get(position).get(3)));
                showIdeaIntent.putExtra("TABLE_NAME", "Busquedas");
                startActivityForResult(showIdeaIntent, SHOW_BUSQUEDA_REQUEST);
            }
        });

        registerForContextMenu(busquedasListView);
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
        final int index = info.position;
        View view = info.targetView;

        switch (item.getItemId()) {
            case R.id.edit:
                Intent editIntent = new Intent(getApplicationContext(), CreateBusquedaActivity.class);
                editIntent.putExtra("ID", Integer.valueOf(busquedasOptions.get(index).get(3)));
                editIntent.putExtra("BUSQUEDA", busquedasOptions.get(index).get(0));
                editIntent.putExtra("DESCRIPCION", busquedasOptions.get(index).get(2));
                startActivityForResult(editIntent, EDIT_BUSQUEDA_REQUEST);
                return true;
            case R.id.delete:
                try {
                    new AlertDialog.Builder(this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Search")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ideaDataBase.deleteRow(busquedasOptions.get(index).get(0));
                                    busquedasOptions.remove(index);
                                    adapterListTitles.remove(index);
                                    busquedasAdapter.notifyDataSetChanged();
                                    Toast.makeText(getApplicationContext(), "Busqueda eliminada", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == ADD_BUSQUEDA_REQUEST){
            updateDataInListView();
        }
        else if(requestCode == EDIT_BUSQUEDA_REQUEST){
            updateDataInListView();
        }
        else if(requestCode == SHOW_BUSQUEDA_REQUEST){
            updateDataInListView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addBusqueda(View view){
        Intent intent = new Intent(getApplicationContext(), CreateBusquedaActivity.class);
        startActivityForResult(intent, ADD_BUSQUEDA_REQUEST);
    }

    public void updateDataInListView(){
        busquedasOptions.clear();
        adapterListTitles.clear();
        busquedasOptions = ideaDataBase.getAllElements("SELECT * FROM busquedas");  //Dar el query para que busque otra vez
        for(ArrayList<String> array : busquedasOptions){
            adapterListTitles.add(array.get(0));
        }
        busquedasAdapter.notifyDataSetChanged();
    }

    public void openDialog(String word){
        //SI TIENE IMAGEN, QUE LA MUESTRE TAMBIEN
        final SQLiteDatabase Database = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);
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
}
