package mx.itesm.csf.crud.Vendedor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.itesm.csf.crud.Adaptadores.AdaptadorVentas;
import mx.itesm.csf.crud.Controladores.Controlador;
import mx.itesm.csf.crud.Controladores.Servicios;
import mx.itesm.csf.crud.Modelos.ModeloRopa;
import mx.itesm.csf.crud.Modelos.ModeloVentas;
import mx.itesm.csf.crud.Modelos.ProductoEnCarrito;
import mx.itesm.csf.crud.R;
import mx.itesm.csf.crud.Ventas.PrincipalVentas;
import mx.itesm.csf.crud.Adaptadores.AdaptadorCarrito;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Carrito extends AppCompatActivity {

    String nombreCliente, apellidoCliente;
    int idCliente;
    TextView clientName;
    ArrayList <ProductoEnCarrito> carrito;
    ProgressDialog barra_de_progreso;
    Map<String, String> map;

    RecyclerView miRecyclerview;
    RecyclerView.Adapter miAdaptador;
    RecyclerView.LayoutManager miAdministrador;
    Button botonLista, botonEscaneado, botonBorrar;

    int productID;
    int desiredQuantity;

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
        barra_de_progreso = new ProgressDialog(Carrito.this);

        //BORRAR ARTÍCULOS DUMMY
        carrito = new ArrayList<>();
        carrito.add(new ProductoEnCarrito("iPhone", 12000, 1, 12000));
        carrito.add(new ProductoEnCarrito("audifonos", 400, 2, 800));

        map = new HashMap<>();

        miRecyclerview = (RecyclerView) findViewById(R.id.reciclador);
        botonLista = (Button) findViewById(R.id.agregarPorLista);
        botonEscaneado = (Button) findViewById(R.id.agregarPorEscaneado);
        botonBorrar = (Button) findViewById(R.id.borrar);
        //barra_de_progreso = new ProgressDialog(Carrito.this);

        // utilizamos los componentes de CardView
        miAdministrador = new LinearLayoutManager(Carrito.this, LinearLayoutManager.VERTICAL,false);
        miRecyclerview.setLayoutManager(miAdministrador);
        miAdaptador = new AdaptadorCarrito(Carrito.this, carrito);
        miRecyclerview.setAdapter(miAdaptador);


        // LayoutManager  se encarga del layout de todas las vistas dentro del RecyclerView, concretando con el LinearLayoutManager,
        // permite entre otros acceder a elementos mostrados en la pantalla.
        // https://developer.android.com/reference/android/support/v7/widget/LinearLayoutManager.html
        miAdministrador = new LinearLayoutManager(Carrito.this, LinearLayoutManager.VERTICAL,false);
        miAdaptador = new AdaptadorCarrito(Carrito.this, carrito);
        miRecyclerview.setAdapter(miAdaptador);
        miRecyclerview.setLayoutManager(miAdministrador);
        miAdaptador.notifyDataSetChanged();

    }

    // funcion para el escaneado de un código QR
    public void leer(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.addExtra("SCAN_WIDTH", 800);
        integrator.addExtra("SCAN_HEIGHT", 800);
        integrator.addExtra("PROMPT_MESSAGE", getResources().getString(R.string.prompt));
        integrator.initiateScan();
    }

    public void showDialog(final String result){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        String temp = getResources().getString(R.string.dialog_message);
        String post = getResources().getString(R.string.post_dialog_message);
        builder.setMessage(temp + " " + result + " " + post);
        builder.setPositiveButton(R.string.addToCart, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                productID = Integer.parseInt(result);
                callNumberPicker();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //NO HACER NADA
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult.getContents() != null){
            System.out.println("Información Encontrada");
            System.out.println(scanResult.getContents());
            System.out.println(scanResult.getFormatName());
            showDialog(scanResult.getContents());

        }
    }

    // creamos nuestro método cargarJSON() con la librería Volley
    private void cargarJSON()
    {
        barra_de_progreso.setMessage("Cargando datos...");
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();

        String requester = Servicios.CHECAR_STOCK + "?p_id=" + productID + "&cantidad=" + desiredQuantity;
        Log.d("parameters", requester);

        JsonObjectRequest reqData = new JsonObjectRequest(Request.Method.GET, requester, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        barra_de_progreso.cancel();
                        Log.d("Objeto","respuesta : " + response.toString());

                        try {
                            if (response.getInt("success") == 1){
                                Toast.makeText(Carrito.this, "Si se puede agregar al carrito", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Carrito.this, "No se puede agregar al carrito", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //notifydatasetchanged
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

    private void callNumberPicker(){

        Context thisContext = Carrito.this;

        final NumberPicker picker = new NumberPicker(thisContext);
        picker.setMinValue(1);
        picker.setMaxValue(99);

        final FrameLayout layout = new FrameLayout(thisContext);
        layout.addView(picker, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));

        new AlertDialog.Builder(thisContext)
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        desiredQuantity = picker.getValue();
                        Toast.makeText(Carrito.this, "El usuario quiere " + desiredQuantity + " productos", Toast.LENGTH_SHORT).show();
                        cargarJSON();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();

    }
}
