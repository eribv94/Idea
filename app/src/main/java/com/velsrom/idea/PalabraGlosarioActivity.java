package com.velsrom.idea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.velsrom.idea.creation.CreateGlosarioActivity;

public class PalabraGlosarioActivity extends AppCompatActivity {

    String word;
    String definicion;
    public static final int EDIT_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palabra_glosario);

        TextView definicionTextView = findViewById(R.id.palabraTextView);
        TextView blackWordTextView = findViewById(R.id.blackTextView);
        TextView greenWordTextView = findViewById(R.id.greenTextView);

        word = getIntent().getStringExtra("WORD");
        definicion = getIntent().getStringExtra("DEFINICION");

        definicionTextView.setText(definicion);
        blackWordTextView.setText(word);
        greenWordTextView.setText(word);
    }

    public void editPalabra(View view){
        Intent editIntent = new Intent(getApplicationContext(), CreateGlosarioActivity.class);
        editIntent.putExtra("WORD", word);
        editIntent.putExtra("DEFINICION", definicion);
        startActivityForResult(editIntent, EDIT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == EDIT_REQUEST_CODE){
            //Update data?
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
