package com.velsrom.idea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.velsrom.idea.creation.CreateGlosarioActivity;

public class PalabraGlosarioActivity extends AppCompatActivity {

    TextView definicionTextView;
    TextView blackWordTextView;
    TextView greenWordTextView;

    String word, definicion;
    int id;
    public static final int EDIT_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_palabra_glosario);

        definicionTextView = findViewById(R.id.palabraTextView);
        blackWordTextView = findViewById(R.id.blackTextView);
        greenWordTextView = findViewById(R.id.greenTextView);

        word = getIntent().getStringExtra("WORD");
        definicion = getIntent().getStringExtra("DEFINICION");
        id = getIntent().getIntExtra("ID", -1);

        definicionTextView.setText(definicion);
        blackWordTextView.setText(word);
        greenWordTextView.setText(word);
    }

    public void editPalabra(View view){
        Intent editIntent = new Intent(getApplicationContext(), CreateGlosarioActivity.class);
        editIntent.putExtra("WORD", word);
        editIntent.putExtra("DEFINICION", definicion);
        editIntent.putExtra("ID", id);
        startActivityForResult(editIntent, EDIT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == EDIT_REQUEST_CODE){
            refreshContent();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void refreshContent(){
        final SQLiteDatabase Database = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);
        Cursor c = Database.rawQuery("SELECT * FROM glosario WHERE id = \'" + id + "\' ", null);

        int plabraIdx = c.getColumnIndex("palabra");
        int definicionIdx = c.getColumnIndex("definicion");
        c.moveToFirst();
        word = c.getString(plabraIdx);
        definicion = c.getString(definicionIdx);

        definicionTextView.setText(definicion);
        blackWordTextView.setText(word);
        greenWordTextView.setText(word);
    }
}
