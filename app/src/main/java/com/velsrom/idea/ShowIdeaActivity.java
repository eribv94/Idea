package com.velsrom.idea;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ShowIdeaActivity extends AppCompatActivity {

    private ViewFlipper titelViewFlipper;
    private ViewFlipper textViewFlipper;
    private EditText titleEditText, textEditText;
    private TextView titleTextView, textTextView;
    private ImageView searchImageView;

    String title, text, tableName, imagePath;
    int id;
    boolean isEdit = false;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_idea);

        title = getIntent().getStringExtra("TITLE");
        text = getIntent().getStringExtra("TEXT");
        id = getIntent().getIntExtra("ID", -1);
        tableName = getIntent().getStringExtra("TABLE_NAME");

        if(tableName.equals("Busquedas")){
            imagePath = getIntent().getStringExtra("IMAGE_PATH");
        }

        titelViewFlipper = findViewById(R.id.titleViewFlipper);
        textViewFlipper = findViewById(R.id.textViewFlipper);
        titleTextView = findViewById(R.id.titleTextView);
        titleEditText = findViewById(R.id.titleEditText);
        textTextView = findViewById(R.id.textTextView);
        textEditText = findViewById(R.id.textEditText);
        searchImageView = findViewById(R.id.searchImageView);

        database = this.openOrCreateDatabase("Idea", MODE_PRIVATE, null);
//        IdeaDataBase ideaDataBase = new IdeaDataBase(Database, tableName);

        titleTextView.setText(title);
        textTextView.setText(text);
        if(tableName.equals("Busquedas") && !imagePath.equals("")){
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            searchImageView.setImageBitmap(myBitmap);
        }
    }

    public void toggleView(View view){
        String newTitle;
        String newText;
        titelViewFlipper.showNext();
        textViewFlipper.showNext();
        if (isEdit){    //si es EditText a TextView, se edito
            newTitle = titleEditText.getText().toString();
            newText = textEditText.getText().toString();
            String q = "";
            if(tableName.equals("Ideas")) {
                q = "UPDATE " + tableName + " SET title = \"" + newTitle + "\", idea = \"" + newText + "\" WHERE id = \'" + id + "\'";
            }else if(tableName.equals("Busquedas")){
                q = "UPDATE " + tableName + " SET title = \"" + newTitle + "\", descripcion = \"" + newText + "\" WHERE id = \'" + id + "\'";
            }
            database.execSQL(q);
            titleTextView.setText(newTitle);
            textTextView.setText(newText);
            titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.ITALIC);
        }
        else {          //si TextView a EditText
            newTitle = titleTextView.getText().toString();
            newText = textTextView.getText().toString();
            titleEditText.setText(newTitle);
            textEditText.setText(newText);
        }
        isEdit = !isEdit;
    }
}
