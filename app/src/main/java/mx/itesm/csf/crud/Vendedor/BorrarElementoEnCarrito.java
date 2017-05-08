package mx.itesm.csf.crud.Vendedor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import mx.itesm.csf.crud.Modelos.ProductoEnCarrito;
import mx.itesm.csf.crud.R;

public class BorrarElementoEnCarrito extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner carrito;
    ArrayList <String> elementos = new ArrayList<>();
    ArrayAdapter <String> myAdapter;
    Context myContext;
    Button borrar;
    int toDelete = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_elemento_en_carrito);

        //componentes de interfaz
        carrito = (Spinner)findViewById(R.id.spinner);
        borrar = (Button) findViewById(R.id.button);

        //obtener lista de productos en carrito
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        elementos = (ArrayList<String>)bundle.getSerializable("carrito");


        // setup del spinner
        myContext = getApplicationContext();
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, elementos);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carrito.setPrompt(myContext.getString(R.string.pick_customer));
        carrito.setAdapter(myAdapter);
        carrito.setOnItemSelectedListener(this);
        Log.d("prompt", carrito.getPrompt().toString());
    }

    public void buttonClicked(View view){
        Intent intent = new Intent(BorrarElementoEnCarrito.this, Carrito.class);
        intent.putExtra("elemento", toDelete);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); //clear previous activities
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        borrar.setText(getResources().getString(R.string.delete) + " " + elementos.get(position));
        toDelete = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){
    }
}
