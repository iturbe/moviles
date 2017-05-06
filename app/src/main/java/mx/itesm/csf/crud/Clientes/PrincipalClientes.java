package mx.itesm.csf.crud.Clientes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.itesm.csf.crud.Adaptadores.AdaptadorClientes;
import mx.itesm.csf.crud.Controladores.Controlador;
import mx.itesm.csf.crud.Controladores.Servicios;
import mx.itesm.csf.crud.Modelos.ModeloClientes;
import mx.itesm.csf.crud.R;

public class PrincipalClientes extends AppCompatActivity {


    RecyclerView miRecyclerview;
    RecyclerView.Adapter miAdaptador;
    RecyclerView.LayoutManager miAdministrador;
    List<ModeloClientes> misElementos;
    Button botonInsertar, botonBorrar;
    ProgressDialog barra_de_progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_principal_clientes);

        // Mapeamos los elementos de nuestra vista y la del CardView
        miRecyclerview = (RecyclerView) findViewById(R.id.reciclador);
        botonInsertar = (Button) findViewById(R.id.botonInsertar);
        botonBorrar = (Button) findViewById(R.id.botonBorrar);
        barra_de_progreso = new ProgressDialog(PrincipalClientes.this);
        misElementos = new ArrayList<>();

        // Vamos a llamar la funcion cargarJSON con Volley para procesar los datos
        cargarJSON();

        // utilizamos los componentes de CardView
        miAdministrador = new LinearLayoutManager(PrincipalClientes.this,LinearLayoutManager.VERTICAL,false);
        miRecyclerview.setLayoutManager(miAdministrador);
        miAdaptador = new AdaptadorClientes(PrincipalClientes.this,misElementos);
        miRecyclerview.setAdapter(miAdaptador);


        // LayoutManager  se encarga del layout de todas las vistas dentro del RecyclerView, concretando con el LinearLayoutManager,
        // permite entre otros acceder a elementos mostrados en la pantalla.
        // https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager.html
        miAdministrador = new LinearLayoutManager(PrincipalClientes.this,LinearLayoutManager.VERTICAL,false);
        miRecyclerview.setLayoutManager(miAdministrador);
        miAdaptador = new AdaptadorClientes(PrincipalClientes.this,misElementos);
        miRecyclerview.setAdapter(miAdaptador);

        // definimos los listeners para cada boton de nuestra interfaz
        botonInsertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalClientes.this,InsertarClientes.class);
                startActivity(intent);
            }
        });

        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent hapus = new Intent(PrincipalClientes.this,BorrarClientes.class);
                startActivity(hapus);
            }
        });
    }

    // creamos nuestro método cargarJSON() con la librería Volley
    private void cargarJSON()
    {
        barra_de_progreso.setMessage(getResources().getString(R.string.loading_data));
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, Servicios.CLIENTES_READ,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        barra_de_progreso.cancel();
                        Log.d("Objeto","respuesta : " + response.toString());
                        for(int i = 0 ; i < response.length(); i++)
                        {
                            try {
                                if (i == 0) { //solo para el confirmation
                                    JSONObject first = response.getJSONObject(i);
                                    if (first.getString("codigo") == "01"){
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.receiving_data), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    JSONObject data = response.getJSONObject(i);
                                    ModeloClientes cliente = new ModeloClientes();
                                    cliente.setC_id(data.getInt("c_id"));
                                    cliente.setNombre(data.getString("nombre"));
                                    cliente.setApellido(data.getString("apellido"));
                                    misElementos.add(cliente);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        miAdaptador.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_de_progreso.cancel();
                        Log.d("Volley", "Error : " + error.getMessage());
                    }
                }){
            @Override
            public Map< String, String > getHeaders() throws AuthFailureError {
                HashMap< String, String > headers = new HashMap < String, String > ();
                String encodedCredentials = Base64.encodeToString("admin@tiendita.com:root".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);
                return headers;
            }
        };

        Controlador.getInstance().agregaAlRequestQueue(reqData);
    }

}
