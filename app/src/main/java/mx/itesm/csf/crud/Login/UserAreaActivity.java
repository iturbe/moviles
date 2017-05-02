package mx.itesm.csf.crud.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import mx.itesm.csf.crud.CRUDmenu;
import mx.itesm.csf.crud.Clientes.PrincipalClientes;
import mx.itesm.csf.crud.R;
import mx.itesm.csf.crud.Ventas.PrincipalVentas;

// IMPORTANTE: ESTA ACTIVIDAD SE ENCUENTRA DEPRECADA, NOS LA SALTAMOS POR COMPLETO Y SIMPLEMENTE MANDAMOS AL USUARIO A LA ACTIVIDAD CORRESPONDIENTE (CRUDMENU O PRINCIPALVENTAS)

public class UserAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etMail = (EditText) findViewById(R.id.etCorreo);
        //final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);

        Intent intent = getIntent();
        //String usuario = intent.getStringExtra("usuario");
        //String email = intent.getStringExtra("email");
        int e_id = intent.getIntExtra("e_id", -1);
        String nombre = intent.getStringExtra("nombre");
        String apellido = intent.getStringExtra("apellido");
        int admin = intent.getIntExtra("admin", -1);
        String correo = intent.getStringExtra("correo");

        if (admin == 1) {
            Intent otherIntent = new Intent(UserAreaActivity.this,CRUDmenu.class);
            otherIntent.putExtra("nombre", nombre);
            otherIntent.putExtra("apellido", apellido);
            startActivity(otherIntent);
        } else {
            String message = nombre + " " + apellido + " bienvenido a tu cuenta, tu email es " + correo + ", tu id es: " + e_id + "" + " y tu num de admin es: " + admin + "";
            welcomeMessage.setText(message);
            //etMail.setText(email);
            Intent otherIntent = new Intent(UserAreaActivity.this,PrincipalVentas.class);
            otherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //clear previous activities
            startActivity(otherIntent);
        }
    }
}
