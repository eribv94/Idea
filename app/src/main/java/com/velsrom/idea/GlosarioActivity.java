package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GlosarioActivity extends AppCompatActivity {

    TextView[] selectedSection;
    int currentSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glosario);

        selectedSection = new TextView[]{
                findViewById(R.id.aeTextView),
                findViewById(R.id.fjTextView),
                findViewById(R.id.koTextView),
                findViewById(R.id.ptTextView),
                findViewById(R.id.uzTextView)
        };
    }

    public void onClickLetters(View view){
        Log.i("Clicked", "Clicked");
        int selected = Integer.valueOf(view.getTag().toString());
        selectedSection[currentSelected].setBackgroundColor(Color.WHITE);
        selectedSection[currentSelected].setTextColor(Color.BLACK);
        selectedSection[selected].setBackgroundColor(Color.GRAY);
        selectedSection[selected].setTextColor(Color.WHITE);
        currentSelected = selected;
    }
}
