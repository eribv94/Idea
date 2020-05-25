package com.velsrom.idea;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/* FIXME:
    - Ver si es necesario el "dataTypes", posiblemente quitarlo (todos son VARCHAR)
* */

public class IdeaDataBase {
    SQLiteDatabase database;
    String databaseName;
    String[] columnNames;
    ArrayList<String> dataTypes;

    public IdeaDataBase(SQLiteDatabase database, String databaseName, String[] columnNames, ArrayList<String> dataTypes) {
        this.database = database;
        this.databaseName = databaseName;
        this.columnNames = columnNames;
        this.dataTypes = dataTypes;
    }

    public void addData(String[] array){
        StringBuilder query = new StringBuilder("CREATE TABLE IF NOT EXISTS " + databaseName + " (");
        for (int i = 0; i < columnNames.length; i++) {
            query.append(" " + columnNames[i] + " VARCHAR,");
        }
        query.setCharAt(query.length() - 1, ')');
        database.execSQL(query.toString());

        ContentValues cv = new ContentValues();
        for (int i = 0; i < columnNames.length; i++) {
            cv.put(columnNames[i], array[i]);
        }
        database.insert(databaseName, null, cv);
    }

    public void getData(){
        String query = "SELECT * FROM " + databaseName;
        Cursor c = database.rawQuery(query, null);

        int[] idxs = new int[columnNames.length];

        for (int i = 0; i < idxs.length; i++) {
            idxs[i] = c.getColumnIndex(columnNames[i]);
        }
        c.moveToFirst();

        while (!c.isAfterLast()) {
            for (int i = 0; i < idxs.length; i++){
                Log.i(dataTypes.get(i), c.getString(idxs[i]));
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