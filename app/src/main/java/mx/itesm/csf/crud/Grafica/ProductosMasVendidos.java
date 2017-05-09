package mx.itesm.csf.crud.Grafica;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.itesm.csf.crud.Clientes.PrincipalClientes;
import mx.itesm.csf.crud.Controladores.Controlador;
import mx.itesm.csf.crud.Controladores.Servicios;
import mx.itesm.csf.crud.R;

public class ProductosMasVendidos extends AppCompatActivity {
    List<BarEntry> entries = new ArrayList<>();
    BarChart chart;
    ProgressDialog barra_de_progreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_mas_vendidos);
        barra_de_progreso = new ProgressDialog(ProductosMasVendidos.this);

        chart = (BarChart) findViewById(R.id.graficados);
        cargarJSON();
    }

    // creamos nuestro método cargarJSON() con la librería Volley
    private void cargarJSON()
    {
        barra_de_progreso.setMessage("Cargando datos...");
        barra_de_progreso.setCancelable(false);
        barra_de_progreso.show();
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, Servicios.PRODUCTOS_MAS_VENDIDOS,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        barra_de_progreso.cancel();
                        Log.d("Objeto","respuesta : " + response.toString() + " " + response.length());
                        for(int i = 0 ; i < response.length(); i++) {
                            try {
                                if (i == 0) { //solo para el confirmation
                                    JSONObject first = response.getJSONObject(i);
                                    if (first.getString("codigo") == "01"){
                                        Toast.makeText(getApplicationContext(), "Recibiendo datos...", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    JSONObject data = response.getJSONObject(i);
                                    entries.add(new BarEntry(i, data.getInt("cantidad_total"), data.getString("nombre")));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d("size al acabar", "int " + entries.size());
                        BarDataSet dataset = new BarDataSet(entries, "Productos");
                        BarData bardata = new BarData(dataset);
                        chart.setData(bardata);
                        chart.animateY(5000);
                        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                        chart.getDescription().setText(""); // redundante ya que la descripción está en el top bar
                        chart.getLegend().setEnabled(false);

                        XAxis xAxis = chart.getXAxis();
                        xAxis.setDrawAxisLine(false);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawLabels(false);
                        xAxis.setDrawGridLines(false);

                        dataset.notifyDataSetChanged();

                        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                            @Override
                            public void onValueSelected(Entry e, Highlight h) {
                                Toast.makeText(getApplicationContext(), e.getData().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected() {

                            }
                        });
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
