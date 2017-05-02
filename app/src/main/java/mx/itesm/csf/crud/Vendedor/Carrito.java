package mx.itesm.csf.crud.Vendedor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import mx.itesm.csf.crud.R;

public class Carrito extends AppCompatActivity {

    String nombreCliente, apellidoCliente;
    int idCliente;
    TextView clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        //obtener datos del cliente que vamos a atender
        Intent thisIntent = getIntent();
        nombreCliente = thisIntent.getStringExtra("nombre");
        apellidoCliente = thisIntent.getStringExtra("apellido");
        idCliente = thisIntent.getIntExtra("c_id", -1);

        //escribir mensaje de bienvenida/verificación
        clientName = (TextView) findViewById(R.id.clientName);
        clientName.setText("Carrito de " + nombreCliente + " " + apellidoCliente + " (" + idCliente + ")");

    }
}
