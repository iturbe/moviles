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
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static mx.itesm.csf.crud.Controladores.Servicios.VENTAS_CREATE;

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
    Button botonCheckout, botonEscaneado, botonBorrar;

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

        carrito = new ArrayList<>();

        map = new HashMap<>();

        miRecyclerview = (RecyclerView) findViewById(R.id.reciclador);
        botonCheckout = (Button) findViewById(R.id.checkout);
        botonEscaneado = (Button) findViewById(R.id.agregarPorEscaneado);
        botonBorrar = (Button) findViewById(R.id.borrar);

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

    // función que efectúa el proceso de checkout del carrito
    public void checkout(View view){
        if (carrito.isEmpty()){
            Toast.makeText(Carrito.this, "No hay nada en el carrito!", Toast.LENGTH_SHORT).show();
        } else {
            for (int a = 0; a < carrito.size(); a++){
                vender(carrito.get(a).getP_id(), idCliente, a, carrito.get(a).getCantidad());
            }
        }
    }

    // funcion para el escaneado de un código QR
    public void leer(View view){
        IntentIntegrator integrator = new IntentIntegrator(this);

        integrator.addExtra("SCAN_WIDTH", 800);
        integrator.addExtra("SCAN_HEIGHT", 800);
        integrator.addExtra("PROMPT_MESSAGE", getResources().getString(R.string.prompt));
        integrator.initiateScan();
    }

    // cuadro de diálogo para el QR result
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

    // para el lector QR
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
    private void cargarJSON(){
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
                                carrito.add(new ProductoEnCarrito(productID, response.getString("nombre"), response.getInt("precio"), desiredQuantity));
                                Toast.makeText(Carrito.this, "Agregado al carrito: " + response.getString("nombre") + " (x" + desiredQuantity + ")", Toast.LENGTH_SHORT).show();
                                Log.d("why not", "a ver");
                            } else {
                                Toast.makeText(Carrito.this, "No se pudo agregar al carrito. No existe un producto con ese ID o no hay suficiente stock.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                HashMap< String, String > headers = new HashMap <> ();
                String encodedCredentials = Base64.encodeToString("admin@tiendita.com:root".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);
                return headers;
            }
        };

        Controlador.getInstance().agregaAlRequestQueue(reqData);
    }

    //llama un numberPicker y guarda el resultado en desiredQuantity
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
                        //Toast.makeText(Carrito.this, "El usuario quiere " + desiredQuantity + " productos", Toast.LENGTH_SHORT).show();
                        cargarJSON();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();

    }

    //insertar venta
    private void vender(final int productID, final int customerID, final int cartItemNumber, final int quantity)
    {
        barra_de_progreso.setMessage("Insertar datos");
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();

        StringRequest enviaDatos = new StringRequest(Request.Method.POST, VENTAS_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barra_de_progreso.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            Toast.makeText(Carrito.this, "Respuesta : "+   res.getString("mensaje") , Toast.LENGTH_SHORT).show();
                            Log.d("Parámetros: ", response.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notificar de una venta exitosa
                        Toast.makeText(Carrito.this, "Venta del cartItem #" + cartItemNumber + " exitosa!" , Toast.LENGTH_SHORT).show();
                        Log.d("vendido", quantity + " " + productID + " (p_id)");
                        //Intent intent = new Intent(InsertarVentas.this, PrincipalVentas.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); //clear previous activities
                        //startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_de_progreso.cancel();
                        Toast.makeText(Carrito.this, "Respuesta: Error al insertar datos", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.clear();
                map.put("p_id", Integer.toString(productID));
                map.put("c_id", Integer.toString(customerID));
                map.put("cantidad", Integer.toString(quantity));
                return map;
            }
            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                HashMap < String, String > headers = new HashMap <> ();
                String encodedCredentials = Base64.encodeToString("admin@tiendita.com:root".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);
                return headers;
            }
        };

        Controlador.getInstance().agregaAlRequestQueue(enviaDatos);
    }
}