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
import mx.itesm.csf.crud.Ropa.Principal;
import mx.itesm.csf.crud.R;

import static mx.itesm.csf.crud.Controladores.Servicios.VENTAS_CREATE;
import static mx.itesm.csf.crud.Controladores.Servicios.VENTAS_UPDATE;

public class InsertarVentas extends AppCompatActivity {

    // definimos los componentes de nuestra interfaz
    EditText clave_venta, clave_producto, clave_cliente, cantidad;
    Button boton_cancelar,boton_guardar;
    ProgressDialog barra_de_progreso;
    Map<String, String> map = new HashMap<>();
    int update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_insertar_venta);

        /* obtenemos los datos del intento*/
        Intent datos = getIntent();
        update = datos.getIntExtra("update",0);
        int intent_clave_venta = datos.getIntExtra("clave_venta", -1);
        int intent_clave_producto = datos.getIntExtra("clave_producto", -1);
        int intent_clave_cliente = datos.getIntExtra("clave_cliente", -1);
        int intent_cantidad = datos.getIntExtra("cantidad", -1);

        // hacemos referencia a nuestra interfaz gráfica XML
        clave_venta = (EditText) findViewById(R.id.clave_venta);
        clave_producto = (EditText) findViewById(R.id.clave_producto);
        clave_cliente = (EditText) findViewById(R.id.clave_cliente);
        cantidad = (EditText) findViewById(R.id.cantidad);

        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);
        boton_guardar = (Button) findViewById(R.id.boton_guardar);
        barra_de_progreso = new ProgressDialog(InsertarVentas.this);


        // condición para inserción
        if(update == 1)
        {
            boton_guardar.setText(getResources().getString(R.string.update_data));
            clave_venta.setText(Integer.toString(intent_clave_venta));
            clave_producto.setText(Integer.toString(intent_clave_producto));
            clave_cliente.setText(Integer.toString(intent_clave_cliente));
            clave_venta.setVisibility(View.GONE);
            cantidad.setText(Integer.toString(intent_cantidad));
        }

        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(update == 1)
                {
                    actualizarDatos();
                }else {
                    guardarDatos();
                }
            }
        });


        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent principal = new Intent(InsertarVentas.this,PrincipalVentas.class);
                startActivity(principal);
            }
        });
    }


    private void actualizarDatos()
    {
        barra_de_progreso.setMessage(getResources().getString(R.string.update_data));
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();

        StringRequest updateReq = new StringRequest(Request.Method.POST, VENTAS_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barra_de_progreso.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertarVentas.this, getResources().getString(R.string.response) + " : " + res.getString("Mensaje") , Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // comentado para que se quede en esta sección de mi app y ver los errores en caso de fallo al insertar
                        Intent intent = new Intent(InsertarVentas.this, PrincipalVentas.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear previous activities
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_de_progreso.cancel();
                        Toast.makeText(InsertarVentas.this, getResources().getString(R.string.response) + " : " + getResources().getString(R.string.insert_error), Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map.put("v_id",clave_venta.getText().toString());
                map.put("p_id",clave_producto.getText().toString());
                map.put("c_id",clave_cliente.getText().toString());
                map.put("cantidad",cantidad.getText().toString());
                Log.d("Parámetros: ", VENTAS_UPDATE + map.toString());

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

        StringRequest enviaDatos = new StringRequest(Request.Method.POST, VENTAS_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barra_de_progreso.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(InsertarVentas.this, getResources().getString(R.string.response) + " : " + res.getString("mensaje") , Toast.LENGTH_SHORT).show();
                            Log.d("Parámetros: ", response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(InsertarVentas.this, PrincipalVentas.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear previous activities
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_de_progreso.cancel();
                        Toast.makeText(InsertarVentas.this, getResources().getString(R.string.response) + " : " + getResources().getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map.put("p_id",clave_producto.getText().toString());
                map.put("c_id",clave_cliente.getText().toString());
                map.put("cantidad",cantidad.getText().toString());
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
