package com.example.thibault.apptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.buttonGen);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final TextView textDebug = findViewById(R.id.textViewDebug);
                textDebug.setText("Clicked !");
            }
        });
    }
}
