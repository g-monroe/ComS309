package com.example.mytest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textview = findViewById(R.id.simpleText);
        textview.setText("Hello Simon");

        Button nextScreen = findViewById(R.id.button);
        nextScreen.setText("This takes you to next screen");
    }

    public void nextView(View view) {
        Intent intent = new Intent(this, SubActivity.class);
        startActivity(intent);
    }
}
