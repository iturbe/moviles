package mx.itesm.csf.crud.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.itesm.csf.crud.Clientes.InsertarClientes;
import mx.itesm.csf.crud.Ropa.InsertarDatos;
import mx.itesm.csf.crud.Modelos.ModeloClientes;
import mx.itesm.csf.crud.R;

/**
 * Created by biller on 4/2/17.
 */

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ContenedorDeDatos>{
    // definimos una lista en donde vamos a incorporar todos los autos de nuestro JSON
    private List<ModeloClientes> misElementos ;

    // Context representa el estado actual de la aplicación y permite obtener información acerca de su entorno de ejecución
    // permite el acceso a recursos y clases específicos de la aplicación y también efectuar operaciones como lanzar actividades,
    // generación de intents, etc.
    private Context context;

    // creamos el método para enviar los parámetros de contexto y la lista de elementos
    public AdaptadorClientes (Context context, List<ModeloClientes> elementos)
    {
        this.misElementos = elementos;
        this.context = context;
    }

    // Creamos el ViewHolder con las vista de un elemento sin personalizar
    @Override
    public AdaptadorClientes.ContenedorDeDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filas,parent,false);
        AdaptadorClientes.ContenedorDeDatos contenedorDeDatos = new AdaptadorClientes.ContenedorDeDatos(layout);
        return contenedorDeDatos;
    }

    // Usando como base el ViewHolder y lo personalizamos
    @Override
    public void onBindViewHolder(AdaptadorClientes.ContenedorDeDatos titulo, int position) {
        ModeloClientes datamodel  = misElementos.get(position);
        titulo.Nombre.setText(datamodel.getNombre() + " " + datamodel.getApellido());
        titulo.Clave_auto.setText(datamodel.getC_id() + " ");

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
        TextView Nombre,Clave_auto;
        ModeloClientes datamodel;

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
                    Intent update = new Intent(context, InsertarClientes.class);
                    update.putExtra("update",1);
                    //update.putExtra("clave",datamodel.getClave_auto());
                    update.putExtra("clave",datamodel.getC_id());
                    update.putExtra("nombre",datamodel.getNombre());
                    update.putExtra("apellido",datamodel.getApellido());
                    context.startActivity(update);
                }
            });
        }
    }
}
