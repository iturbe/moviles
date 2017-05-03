package mx.itesm.csf.crud.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.itesm.csf.crud.Modelos.ModeloRopa;
import mx.itesm.csf.crud.Modelos.ProductoEnCarrito;
import mx.itesm.csf.crud.R;
import mx.itesm.csf.crud.Ropa.InsertarDatos;
import mx.itesm.csf.crud.Vendedor.InsertarProducto;

/**
 * Created by Alonso on 5/2/17.
 */

public class AdaptadorCarrito extends RecyclerView.Adapter<AdaptadorCarrito.ContenedorDeDatos> {

    // definimos una lista en donde vamos a incorporar todos los autos de nuestro JSON
    private List<ProductoEnCarrito> misElementos ;

    // Context representa el estado actual de la aplicación y permite obtener información acerca de su entorno de ejecución
    // permite el acceso a recursos y clases específicos de la aplicación y también efectuar operaciones como lanzar actividades,
    // generación de intents, etc.
    private Context context;

    // creamos el método para enviar los parámetros de contexto y la lista de elementos
    public AdaptadorCarrito (Context context, List<ProductoEnCarrito> elementos)
    {
        this.misElementos = elementos;
        this.context = context;
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    @Override
    public ContenedorDeDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filas,parent,false);
        ContenedorDeDatos contenedorDeDatos = new ContenedorDeDatos(layout);
        return contenedorDeDatos;
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(ContenedorDeDatos titulo, int position) {
        ProductoEnCarrito producto  = misElementos.get(position);
        titulo.Nombre.setText(producto.getNombre() + " | $" + producto.getPrecio() + " (x" + producto.getCantidad() + ")" + " = $" + producto.getSubtotal());
        titulo.Clave_auto.setText(position+1 + " "); //el +1 es para que el índice no empiece en cero

        titulo.datamodel = producto;
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
        TextView Nombre, Clave_auto;
        ProductoEnCarrito datamodel;

        // definimos nuestra vista
        public  ContenedorDeDatos (View view)
        {
            super(view);

            // mapeamos los componentes de la interfaz
            Nombre = (TextView) view.findViewById(R.id.Nombre);
            Clave_auto = (TextView) view.findViewById(R.id.Clave_auto);

            // definimos el listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, InsertarProducto.class);
                    update.putExtra("update",1);
                    update.putExtra("cantidad",datamodel.getCantidad());
                    context.startActivity(update);
                }
            });
        }
    }
}