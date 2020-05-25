package com.velsrom.idea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class IdeaDataBase {
    SQLiteDatabase Database;
    String databaseName;
    String[] columnNames;
    ArrayList<String> dataType;

    public IdeaDataBase(SQLiteDatabase database, String databaseName, String[] columnNames, ArrayList<String> dataTypes) {
        this.Database = database;
        this.databaseName = databaseName;
        this.columnNames = columnNames;
        this.dataType = dataTypes;
    }

    public void addData(String[] array){

        String query = "CREATE TABLE IF NOT EXISTS" + databaseName + "(title VARCHAR, path VARCHAR, descripcion VARCHAR)";
        Database.execSQL(query);

        ContentValues cv = new ContentValues();
        for (int i = 0; i < columnNames.length - 1; i++) {
            cv.put(columnNames[i], array[i]);
        }
        Database.insert("busquedas", null, cv);
    }

    public void getData(){
        String query = "SELECT * FROM " + databaseName;
        Cursor c = Database.rawQuery(query, null);

        int[] idxs = new int[columnNames.length];

        for (int i = 0; i < idxs.length; i++) {
            idxs[i] = c.getColumnIndex(columnNames[i]);
        }
        c.moveToFirst();

        while (!c.isAfterLast()) {
            for (int i = 0; i < idxs.length; i++){
                Log.i(dataType.get(i), c.getString(idxs[i]));
            }
            c.moveToNext();
        }
    }
}

//    SQLiteDatabase busquedasDatabase = this.openOrCreateDatabase("Ideas", MODE_PRIVATE, null);
//
//    busquedasDatabase.execSQL("CREATE TABLE IF NOT EXISTS busquedas (title VARCHAR, path VARCHAR, descripcion VARCHAR)");
//
//    ContentValues cv = new ContentValues();
//    cv.put("title", titleEditText.getText().toString());
//    cv.put("path", path);
//    cv.put("descripcion", descripcionEditText.getText().toString());
//    busquedasDatabase.insert("busquedas", null, cv);
//
//    Cursor c = busquedasDatabase.rawQuery("SELECT * FROM busquedas", null);
//
//    int titleIndex = c.getColumnIndex("title");
//    int pathIndex = c.getColumnIndex("path");
//    int descripcionIndex = c.getColumnIndex("descripcion");
//
//    c.moveToFirst();
//
//    while (!c.isAfterLast()) {
//    Log.i("title", c.getString(titleIndex));
//    Log.i("path", c.getString(pathIndex));
//    Log.i("descripcion", c.getString(descripcionIndex));
//    c.moveToNext();
//    }
//
//    Toast.makeText(getApplicationContext(), "Busqueda saved", Toast.LENGTH_SHORT).show();