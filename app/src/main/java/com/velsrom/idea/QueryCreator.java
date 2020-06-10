package com.velsrom.idea;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class QueryCreator {

    SQLiteDatabase database;
    String query;

    public QueryCreator(SQLiteDatabase database) {
        this.database = database;
    }

    public ArrayList<ArrayList<String>> createQuery(String query) {
        Cursor c = database.rawQuery(query, null);
        ArrayList<ArrayList<String>> array = new ArrayList<>();

        int titleIndex = c.getColumnIndex("title");
        int typeIndex = c.getColumnIndex("type");
        int ideaIndex = c.getColumnIndex("idea");

        c.moveToFirst();

        while (!c.isAfterLast()) {
            //Crea arreglo de una idea con [titulo, tipo, idea] para poder agregar despuies al arreglo de ideas
            ArrayList<String> idea = new ArrayList<>();
            idea.add(c.getString(titleIndex));
            idea.add(c.getString(typeIndex));
            idea.add(c.getString(ideaIndex));
            //Se agrega idea al arreglo de ideas
            array.add(idea);
            c.moveToNext();
        }

        return array;
    }
}
