package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.velsrom.idea.creation.CreateIdeaActivity;

public class IdeasMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ideas_menu);
    }

    public void ideaTypeSelected(View view){
        int viewTag = Integer.parseInt(view.getTag().toString());
        Intent intent = new Intent(getApplicationContext(), IdeasActivity.class);
        String ideaType = null;
        //MANDAR A LA ACTIVIDAD CON LAS IDEAS DEL CUADRO SELECCIONADO

        switch (viewTag){
            case 0:
                ideaType = "Ideas";
                break;
            case 1:
                ideaType = "Actividades";
                break;
            case 2:
                ideaType = "Invensiones";
                break;
            case 3:
                ideaType = "Pensamientos";
                break;
            case 4:
                ideaType = "Frases";
                break;
            case 5:
                ideaType = "Otros";
                break;
        }
        intent.putExtra("IDEA_TYPE", ideaType);
        startActivity(intent);
    }

    public void addIdea(View view){
        Intent intent = new Intent(getApplicationContext(), CreateIdeaActivity.class);
        startActivity(intent);
    }
}
