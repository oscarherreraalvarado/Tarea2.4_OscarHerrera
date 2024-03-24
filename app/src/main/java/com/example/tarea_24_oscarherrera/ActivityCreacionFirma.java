package com.example.tarea_24_oscarherrera;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tarea_24_oscarherrera.Configuraciones.Procesos;
import com.example.tarea_24_oscarherrera.Configuraciones.SQLiteConexion;

import java.io.ByteArrayOutputStream;


public class ActivityCreacionFirma extends AppCompatActivity {

    Button btnregresar, btnguardar, btnlimpiar, btnverlista;
    Dibujar lienzo;
    EditText txtinformacion;
    boolean estado;
    SQLiteConexion conexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creacion_firma);

        conexion = new SQLiteConexion(this, Procesos.NAME_DATABASE, null, 1);
        estado = true;

        btnlimpiar = (Button) findViewById(R.id.btnlimpiar);
        btnguardar = (Button) findViewById(R.id.btnguardar);
        btnverlista = (Button) findViewById(R.id.btnverlista);
        btnregresar = (Button) findViewById(R.id.btnregresar);
        txtinformacion = (EditText) findViewById(R.id.txtinformacion);
        lienzo = (Dibujar) findViewById(R.id.lienzo);

        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityCreacionFirma.this, MainActivity.class));
            }
        });

        btnlimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCreacionFirma.this);
                builder.setMessage("¿Desea eliminar su firma?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                lienzo.nuevoDibujo();
                                txtinformacion.setText("");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarfirmas();
            }
        });

        btnverlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityCreacionFirma.this, ActivityListaFirmas.class);
                startActivity(intent);
            }
        });
    }

    private void guardarfirmas() {
        if (lienzo.borrar) {
            Toast.makeText(getApplicationContext(), "Ingrese su firma", Toast.LENGTH_LONG).show();
            return;
        } else if (txtinformacion.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Ingrese una descripción", Toast.LENGTH_LONG).show();
            return;
        }


        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Procesos.informacion, txtinformacion.getText().toString());
        ByteArrayOutputStream bay = new ByteArrayOutputStream(10480);

        Bitmap bitmap = Bitmap.createBitmap(lienzo.getWidth(), lienzo.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        lienzo.draw(canvas);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bay);
        byte[] bl = bay.toByteArray();
        String img = Base64.encodeToString(bl, Base64.DEFAULT);
        values.put(Procesos.imagen, img);

        Long result = db.insert(Procesos.TABLE_FIRMA, Procesos.id, values);
        Toast.makeText(getApplicationContext(), "EXITO SE GUARDO TU FIRMA.", Toast.LENGTH_LONG).show();
        lienzo.nuevoDibujo();
        txtinformacion.setText("");

        db.close();
    }

}