package com.example.tarea_24_oscarherrera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btncrear, btnlista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btncrear = findViewById(R.id.btncrear);
        btnlista = findViewById(R.id.btnlista);

        btncrear.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ActivityCreacionFirma.class));
        });

        btnlista.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ActivityListaFirmas.class));
        });
    }
}
