package mx.itesm.csf.crud.Ventas;

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

import mx.itesm.csf.crud.Controladores.Controlador;
import mx.itesm.csf.crud.Controladores.Servicios;
import mx.itesm.csf.crud.Empleados.PrincipalEmpleados;
import mx.itesm.csf.crud.R;

import static mx.itesm.csf.crud.Controladores.Servicios.VENTAS_DELETE;

public class BorrarVentas extends AppCompatActivity {

    // como siempre, definimos los componente de nuestra interfaz
    EditText clave_venta;
    Button boton_borrar;
    ProgressDialog barra_de_progreso;
    Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_borrar_venta);

        // mapeamos componentes de la interfaz
        clave_venta = (EditText) findViewById(R.id.clave_venta);
        boton_borrar = (Button) findViewById(R.id.boton_borrar);
        barra_de_progreso = new ProgressDialog(BorrarVentas.this);

        // creamos un listener para el boton que eliminará al registro
        boton_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borraRegistro();
            }
        });
    }

    // metodo para borrar el registro
    private void borraRegistro()
    {
        barra_de_progreso.setMessage("Eliminando registro...");
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();

        // usamos el método POST --> ver filminas del porqué hacemos esto
        StringRequest requestBorrar = new StringRequest(Request.Method.POST, VENTAS_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barra_de_progreso.cancel();
                        Log.d("volley","Respuesta : " + response.toString());
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(BorrarVentas.this,"Respuesta: " +res.getString("Mensaje"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(BorrarVentas.this, PrincipalVentas.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear previous activities
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_de_progreso.cancel();
                        Log.d("volley", "Error : " + error.getMessage());
                        Toast.makeText(BorrarVentas.this, "Respuesta: Error al eliminar registro", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map.put("v_id", clave_venta.getText().toString());
                Log.d("Parámetros enviados: ", VENTAS_DELETE + map.toString());
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

        Controlador.getInstance().agregaAlRequestQueue(requestBorrar);
    }
}
