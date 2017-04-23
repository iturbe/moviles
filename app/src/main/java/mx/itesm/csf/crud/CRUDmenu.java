package mx.itesm.csf.crud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mx.itesm.csf.crud.Clientes.PrincipalClientes;
import mx.itesm.csf.crud.Empleados.PrincipalEmpleados;
import mx.itesm.csf.crud.Ropa.Principal;
import mx.itesm.csf.crud.Ventas.PrincipalVentas;

public class CRUDmenu extends AppCompatActivity {

    Button clientes, empleados, ropa, ventas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudmenu);

        clientes = (Button) findViewById(R.id.clientes);
        empleados = (Button) findViewById(R.id.empleados);
        ropa = (Button) findViewById(R.id.ropa);
        ventas = (Button) findViewById(R.id.ventas);



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

        ventas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CRUDmenu.this,PrincipalVentas.class);
                startActivity(intent);
            }
        });
    }
}
