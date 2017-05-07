package mx.itesm.csf.crud.Vendedor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mx.itesm.csf.crud.Controladores.Controlador;
import mx.itesm.csf.crud.Modelos.ProductoEnCarrito;
import mx.itesm.csf.crud.R;

import static mx.itesm.csf.crud.Controladores.Servicios.VENTAS_CREATE;

public class Checkout extends AppCompatActivity {

    ArrayList<ProductoEnCarrito> carrito;
    ProgressDialog barra_de_progreso;
    Map<String, String> map;
    int idCliente;
    Button continuar;
    TextView orden;
    int counter = 0;
    int successful = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        idCliente = intent.getIntExtra("c_id", -1);

        carrito = (ArrayList<ProductoEnCarrito>)bundle.getSerializable("carrito");

        continuar = (Button) findViewById(R.id.confirmButton);
        barra_de_progreso = new ProgressDialog(Checkout.this);
        map = new HashMap<>();

        String order = "";
        int total = 0;
        for (int a = 0; a < carrito.size(); a++){
            order += carrito.get(a).getNombre() + " | $" + carrito.get(a).getPrecio() + " (x " + carrito.get(a).getCantidad() + ") = " + carrito.get(a).getSubtotal() + "\n";
            total += carrito.get(a).getSubtotal();
        }

        order += "\n\n Total: $" + total;

        orden = (TextView) findViewById(R.id.order);
        orden.setText(order);
        Log.d("Here we go", "bois");
    }

    public void finalizar(View view){
        //efectuar las ventas
        for (int a = 0; a < carrito.size(); a++){
            vender(carrito.get(a).getP_id(), idCliente, a, carrito.get(a).getCantidad());
        }
    }

    //insertar venta
    private void vender(final int productID, final int customerID, final int cartItemNumber, final int quantity)
    {
        barra_de_progreso.setMessage(getResources().getString(R.string.inserting_data));
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();

        StringRequest enviaDatos = new StringRequest(Request.Method.POST, VENTAS_CREATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        barra_de_progreso.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            //Toast.makeText(Checkout.this, getResources().getString(R.string.response) + " : " + res.getString("mensaje") , Toast.LENGTH_SHORT).show();
                            Log.d("Parámetros: ", response.toString());
                            //Log.d("code", res.getInt("Codigo") + "");
                            if (res.getInt("Codigo") == 1){ //venta exitosa
                                successful++;
                                //Log.d("haha", "yes");
                                Log.d("successful ventas", successful+"");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        counter++; //se ejecutó una venta
                        Log.d("total ventas", counter + "");

                        //Toast.makeText(Checkout.this, "Venta del cartItem #" + cartItemNumber + " exitosa!" , Toast.LENGTH_SHORT).show();
                        //Log.d("vendido", quantity + " " + productID + " (p_id)");

                        if (counter == carrito.size()){ //ya se procesaron todas las ventas
                            if (counter == successful){ //todas las ventas retornaron exitosas
                                Toast.makeText(Checkout.this, getResources().getString(R.string.sale_success) , Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Checkout.this, ElegirCliente.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //clear previous activities but keep the previous running instance
                                startActivity(intent);
                            } else {
                                Toast.makeText(Checkout.this, getResources().getString(R.string.sale_error) , Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        barra_de_progreso.cancel();
                        Toast.makeText(Checkout.this, getResources().getString(R.string.response) + " : " + getResources().getString(R.string.insert_error), Toast.LENGTH_SHORT).show();
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
                HashMap< String, String > headers = new HashMap <> ();
                String encodedCredentials = Base64.encodeToString("admin@tiendita.com:root".getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", "Basic " + encodedCredentials);
                return headers;
            }
        };

        Controlador.getInstance().agregaAlRequestQueue(enviaDatos);
    }
}
