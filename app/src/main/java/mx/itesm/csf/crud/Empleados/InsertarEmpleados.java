package mx.itesm.csf.crud.Empleados;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mx.itesm.csf.crud.Clientes.InsertarClientes;
import mx.itesm.csf.crud.Clientes.PrincipalClientes;
import mx.itesm.csf.crud.Controladores.Controlador;
import mx.itesm.csf.crud.R;

import static mx.itesm.csf.crud.Controladores.Servicios.EMPLEADOS_CREATE;
import static mx.itesm.csf.crud.Controladores.Servicios.EMPLEADOS_UPDATE;

public class InsertarEmpleados extends AppCompatActivity {

    // definimos los componentes de nuestra interfaz
    EditText clave,nombre,apellido,admin, correo, password;
    Button boton_cancelar,boton_guardar;
    ProgressDialog barra_de_progreso;
    Map<String,String> map = new HashMap<>();
    int intent_clave;
    int update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insertar_empleados);

        /* obtenemos los datos del intento*/
        Intent datos = getIntent();
        update = datos.getIntExtra("update",0);
        Log.d("update recibido", update+"");
        intent_clave = datos.getIntExtra("clave", -1);
        String intent_nombre = datos.getStringExtra("nombre");
        String intent_apellido = datos.getStringExtra("apellido");
        String intent_admin = datos.getStringExtra("admin");
        String intent_correo = datos.getStringExtra("correo");
        String intent_password= datos.getStringExtra("password");

        // hacemos referencia a nuestra interfaz gráfica XML
        clave = (EditText) findViewById(R.id.clave_empleado);
        nombre = (EditText) findViewById(R.id.nombre_empleado);
        apellido = (EditText) findViewById(R.id.apellido_empleado);
        admin = (EditText) findViewById(R.id.admin);
        correo = (EditText) findViewById(R.id.correo_empleado);
        password = (EditText) findViewById(R.id.password_empleado);

        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);
        boton_guardar = (Button) findViewById(R.id.boton_guardar);
        barra_de_progreso = new ProgressDialog(InsertarEmpleados.this);


        // condición para inserción
        if(update == 1)
        {
            boton_guardar.setText(getResources().getString(R.string.update_data));
            clave.setText(Integer.toString(intent_clave));
            clave.setVisibility(View.GONE);
            nombre.setText(intent_nombre);
            apellido.setText(intent_apellido);
            admin.setText(intent_admin);
            correo.setText(intent_correo);
            password.setText(intent_password);

        }

        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update == 1)
                {
                    Log.d("estamos en", "actualizar datos");
                    actualizarDatos();
                }else {
                    Log.d("estamos en", "GUARDAR datos");
                    guardarDatos();
                }
            }
        });


        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent principal = new Intent(InsertarEmpleados.this,PrincipalEmpleados.class);
                startActivity(principal);
            }
        });
    }


    private void actualizarDatos()
    {
        barra_de_progreso.setMessage(getResources().getString(R.string.update_data));
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();

        StringRequest updateReq = new StringRequest(Request.Method.POST, EMPLEADOS_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barra_de_progreso.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertarEmpleados.this, getResources().getString(R.string.response) +  " : " + res.getString("Mensaje") , Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // comentado para que se quede en esta sección de mi app y ver los errores en caso de fallo al insertar
                        Intent intent = new Intent(InsertarEmpleados.this, PrincipalEmpleados.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear previous activities
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_de_progreso.cancel();
                        Toast.makeText(InsertarEmpleados.this, getResources().getString(R.string.response) + " : " + getResources().getString(R.string.insert_error), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                //map.put("e_id", Integer.toString(intent_clave));
                map.put("e_id",clave.getText().toString());
                map.put("nombre",nombre.getText().toString());
                map.put("apellido",apellido.getText().toString());
                map.put("admin",admin.getText().toString());
                map.put("correo",correo.getText().toString());
                map.put("password",password.getText().toString());
                Log.d("Parámetros: ", EMPLEADOS_UPDATE + map.toString());

                return map;
            }
            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                HashMap < String, String > headers = new HashMap < String, String > ();
                String encodedCredentials = Base64.encodeToString("admin@tiendita.com:root".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);
                return headers;
            }
        };

        Controlador.getInstance().agregaAlRequestQueue(updateReq);
    }



    private void guardarDatos()
    {
        barra_de_progreso.setMessage(getResources().getString(R.string.insert_data));
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();

        StringRequest enviaDatos = new StringRequest(Request.Method.POST, EMPLEADOS_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barra_de_progreso.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertarEmpleados.this, getResources().getString(R.string.response) + " : " + res.getString("mensaje") , Toast.LENGTH_SHORT).show();
                            Log.d("Parámetros: ", response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(InsertarEmpleados.this, PrincipalEmpleados.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear previous activities
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_de_progreso.cancel();
                        Toast.makeText(InsertarEmpleados.this, getResources().getString(R.string.response) + " : " + getResources().getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map.put("nombre",nombre.getText().toString());
                map.put("apellido",apellido.getText().toString());
                map.put("admin",admin.getText().toString());
                map.put("correo",correo.getText().toString());
                map.put("password",password.getText().toString());
                return map;
            }
            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                HashMap < String, String > headers = new HashMap < String, String > ();
                String encodedCredentials = Base64.encodeToString("admin@tiendita.com:root".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);
                return headers;
            }
        };

        Controlador.getInstance().agregaAlRequestQueue(enviaDatos);
    }
}
