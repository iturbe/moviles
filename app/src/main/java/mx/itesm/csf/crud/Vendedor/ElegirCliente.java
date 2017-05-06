package mx.itesm.csf.crud.Vendedor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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

import mx.itesm.csf.crud.Controladores.Controlador;
import mx.itesm.csf.crud.Controladores.Servicios;
import mx.itesm.csf.crud.Modelos.ModeloClientes;
import mx.itesm.csf.crud.R;

public class ElegirCliente extends Activity implements OnItemSelectedListener {

    List <ModeloClientes> listaClientes = new ArrayList<>();
    ArrayList <String> nombresClientes = new ArrayList<>();
    Button botonContinuar;
    Spinner clientes;
    ProgressDialog barra_de_progreso;
    TextView welcomeMessage;
    ArrayAdapter<String> myAdapter;
    Context myContext;
    Intent next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_cliente);

        // Mapeamos los elementos de nuestra vista
        botonContinuar = (Button) findViewById(R.id.button);
        barra_de_progreso = new ProgressDialog(ElegirCliente.this);
        welcomeMessage = (TextView) findViewById(R.id.welcome);
        clientes = (Spinner)findViewById(R.id.spinner);
        next = new Intent(ElegirCliente.this, Carrito.class);

        // para movernos a la próxima ventana
        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(next);
            }
        });

        //obtener los extras (nombre del empleado)
        Intent intent = getIntent();
        String nombre = intent.getStringExtra("nombre");
        String apellido = intent.getStringExtra("apellido");
        int employeeNumber = intent.getIntExtra("e_id", -1);

        //crear mensaje de bienvenida
        String message = welcomeMessage.getText() + ", " + nombre + " " + apellido + " (# " + employeeNumber + ")";
        welcomeMessage.setText(message);

        //obtenemos el prompt desde strings.xml
        myContext = getApplicationContext();

        // llamar la funcion cargarJSON con Volley para obtener los datos
        cargarJSON();

        // setup del spinner
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nombresClientes);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clientes.setPrompt(myContext.getString(R.string.pick_customer));
        clientes.setAdapter(myAdapter);
        clientes.setOnItemSelectedListener(this);
        Log.d("prompt", clientes.getPrompt().toString());

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        botonContinuar.setText(getResources().getString(R.string.shop_with) + " " + nombresClientes.get(position));

        //limpiar
        next.removeExtra("c_id");
        next.removeExtra("nombre");
        next.removeExtra("apellido");

        //poner los buenos
        next.putExtra("c_id", listaClientes.get(position).getC_id());
        Log.d("c_id", " " + listaClientes.get(position).getC_id());
        next.putExtra("nombre", listaClientes.get(position).getNombre());
        next.putExtra("apellido", listaClientes.get(position).getApellido());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){
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
                                    listaClientes.add(cliente);
                                    nombresClientes.add(data.getString("nombre") + " " + data.getString("apellido"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                        clientes.setPrompt(myContext.getString(R.string.pick_customer));
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
                HashMap< String, String > headers = new HashMap <> ();
                String encodedCredentials = Base64.encodeToString("admin@tiendita.com:root".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);
                return headers;
            }
        };

        Controlador.getInstance().agregaAlRequestQueue(reqData);
    }
}
