package mx.itesm.csf.crud.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.itesm.csf.crud.Modelos.ModeloVentas;
import mx.itesm.csf.crud.R;
import mx.itesm.csf.crud.Ventas.InsertarVentas;

/**
 * Created by LUCÍA on 4/2/17.
 */

public class AdaptadorVentas extends RecyclerView.Adapter<AdaptadorVentas.ContenedorDeDatos> {
    // definimos una lista en donde vamos a incorporar todos los autos de nuestro JSON
    private List<ModeloVentas> misElementos ;

    // Context representa el estado actual de la aplicación y permite obtener información acerca de su entorno de ejecución
    // permite el acceso a recursos y clases específicos de la aplicación y también efectuar operaciones como lanzar actividades,
    // generación de intents, etc.
    private Context context;

    // creamos el método para enviar los parámetros de contexto y la lista de elementos
    public AdaptadorVentas (Context context, List<ModeloVentas> elementos)
    {
        this.misElementos = elementos;
        this.context = context;
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    @Override
    public AdaptadorVentas.ContenedorDeDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filas,parent,false);
        AdaptadorVentas.ContenedorDeDatos contenedorDeDatos = new AdaptadorVentas.ContenedorDeDatos(layout);
        return contenedorDeDatos;
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(AdaptadorVentas.ContenedorDeDatos titulo, int position) {
        ModeloVentas datamodel  = misElementos.get(position);
        titulo.Cantidad.setText("Cliente # " + datamodel.getC_id() + " | Producto # " + datamodel.getP_id() + " | Cantidad: " + datamodel.getCantidad());
        titulo.clave_venta.setText("# " + datamodel.getV_id() + ": ");

        titulo.datamodel = datamodel;
    }

    // obtenemos el tamaño de elementos que tenemos en la lista
    @Override
    public int getItemCount() {
        return misElementos.size();
    }

    // Para poder seguir con la implementación del adaptador debemos definir primero el ViewHolder necesario
    // para nuestra app. Lo definiremos como clase ContenedorDeDatos interna a nuestro adaptador, extendiendo de la clase
    // RecyclerView.ViewHolder, sólo tendremos que incluir como atributos las referencias
    // a los controles del layout de un elemento de la lista (en nuestro caso los dos TextView) e inicializarlas en el
    // constructor utilizando como siempre el método findViewById() sobre la vista recibida como parámetro.
    class ContenedorDeDatos extends RecyclerView.ViewHolder
    {
        TextView Cantidad, clave_venta;
        ModeloVentas datamodel;

        // definimos nuestra vista
        public  ContenedorDeDatos (View view)
        {
            super(view);

            // mapeamos los componentes de la interfaz
            Cantidad = (TextView) view.findViewById(R.id.Nombre);
            clave_venta = (TextView) view.findViewById(R.id.clave_articulo);

            // definimos el listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, InsertarVentas.class);
                    update.putExtra("update",1);
                    update.putExtra("clave_venta",datamodel.getV_id());
                    update.putExtra("cantidad",datamodel.getCantidad());
                    update.putExtra("clave_cliente",datamodel.getC_id());
                    update.putExtra("clave_producto",datamodel.getP_id());

                    Log.d("clave venta enviada", datamodel.getV_id()+"");
                    Log.d("clave cliente enviada", datamodel.getC_id()+"");
                    Log.d("cantidad enviada", datamodel.getCantidad()+"");
                    Log.d("clave producto enviada", datamodel.getP_id()+"");

                    context.startActivity(update);
                }
            });
        }
    }
}
