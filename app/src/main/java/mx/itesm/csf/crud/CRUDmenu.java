package mx.itesm.csf.crud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mx.itesm.csf.crud.Clientes.PrincipalClientes;
import mx.itesm.csf.crud.Empleados.PrincipalEmpleados;
import mx.itesm.csf.crud.Grafica.ProductosMasVendidos;
import mx.itesm.csf.crud.Ropa.Principal;
import mx.itesm.csf.crud.Ventas.PrincipalVentas;

public class CRUDmenu extends AppCompatActivity {

    Button clientes, empleados, ropa, ventas, graficas;


    // no tengo idea por qué el override del botón de ventas no estaba sirviendo así que puse el onclick directamente en el xml y ésta es la función que manda llamar
    //cabe mencionar que real estaba escrita exactamente igual que las demás
    public void openVentas (View view){
        Intent intent = new Intent(CRUDmenu.this,PrincipalVentas.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudmenu);

        clientes = (Button) findViewById(R.id.clientes);
        empleados = (Button) findViewById(R.id.empleados);
        ropa = (Button) findViewById(R.id.ropa);
        ventas = (Button) findViewById(R.id.ventas);
        graficas = (Button) findViewById(R.id.graficas);

        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String apellido = intent.getStringExtra("apellido");

        // update mensaje de bienvenida en la parte superior de la actividad
        TextView ventas = (TextView) findViewById(R.id.bienvenido);
        String welcomeMessage = getResources().getString(R.string.welcome) + ", " + nombre + " " + apellido + ".";
        ventas.setText(welcomeMessage);

        //esta actividad si necesita ser accesible con el back button, la dejamos como está
        clientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CRUDmenu.this,PrincipalClientes.class);
                startActivity(intent);
            }
        });

        empleados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CRUDmenu.this,PrincipalEmpleados.class);
                startActivity(intent);
            }
        });

        ropa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CRUDmenu.this,Principal.class);
                startActivity(intent);
            }
        });

        graficas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CRUDmenu.this, ProductosMasVendidos.class);
                startActivity(intent);
            }
        });
    }
}
