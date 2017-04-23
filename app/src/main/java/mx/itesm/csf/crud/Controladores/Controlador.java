package mx.itesm.csf.crud.Controladores;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;


// Nota: Esta clase tiene que ejecutarse cuando se inicia la aplicación. Así que
// hay que agregar esta clase al AndroidManifest.xml utilizando el atributo name para la etiqueta <application>.

// definimos la clase controlador quien actuará como intermediario entre el Modelo y la Vista, administrando
// el flujo de información entre ellos y las transformaciones para adaptar los datos a las necesidades de cada uno.
public class Controlador extends Application {

    // Si utilizamos la forma this.getClass().getName(), nos regresará mx.itesm.csf.crud.Controlador
    // Mientras que si usamos: private String TAG = this.getClass().getSimpleName(), nos regresará solo Controlador.
    // por lo que de esta forma creamo un TAG para el log que nos muestre la clase y lo que vayamos a mostrar
    private static final String TAG = Controlador.class.getSimpleName();
    private static Controlador instance  ;
    RequestQueue miRequestQueue;

    // definimos el método onCreate
    @Override
    public void onCreate() {
        // Cuando vamos a sobreescribir un método, tenemos la opción de reemplazar completamente el
        // método en nuestra clase, o de extender el método existente de la clase padre.
        super.onCreate();
        instance = this;
    }

    // Esta solución aprovecha las garantías del modelo de memoria Java sobre la inicialización de clases
    // para garantizar la seguridad de los subprocesos. Cada clase sólo se puede cargar una vez, y sólo se
    // cargará cuando sea necesario.
    // La palabra  synchronized garantiza que un método sólo puede ser invocado por un subproceso a la vez.
    // Esto significa que puede utilizarlos sin crear una instancia de una clase.
    // Los métodos estáticos son implícitamente finales, ya que la modificación se realiza en función del tipo de objeto
    // y los métodos estáticos se conectan a una clase, no a un objeto.
    public static synchronized Controlador getInstance()
    {
        return instance;
    }


    // método para obtener la cola de peticiones de Volley
    // ver https://developer.android.com/training/volley/requestqueue.html
    private RequestQueue getRequestQueue()
    {
        if(miRequestQueue == null)
        {
            miRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return miRequestQueue;
    }


    // método para anexar peticiones a la cola de Volley
    // Recordemos que <T> es un marcador de posición, donde T puede ser prácticamente cualquier tipo.
    // Esto es típicamente útil si T es parte ya sea como un parámetro o como un tipo de retorno
    // es un tipo de parámetros genérico
    public <T> void agregaAlRequestQueue(Request<T> req, String tag)
    {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.e("Anexando request a la cola de volley: %s", req.getUrl());
        getRequestQueue().add(req);
    }

    // método para crear peticiones volley
    public <T> void agregaAlRequestQueue (Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    // metodo que permite cancelar todas las peticiones de volley
    public void cancelaTodosLosRequests(Object req)
    {
        if (miRequestQueue != null)
        {
            miRequestQueue.cancelAll(req);
        }
    }

}
