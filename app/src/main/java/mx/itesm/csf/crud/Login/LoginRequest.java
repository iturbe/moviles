package mx.itesm.csf.crud.Login;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Pablo on 31/03/2017.
 */

public class LoginRequest extends StringRequest{
    private static final String LOGIN_REQUEST_URL="http://ubiquitous.csf.itesm.mx/~pddm-1020365/parcial2/entregaParcial2/servicio.login2.php";
    private Map<String, String> params;

    public LoginRequest(String correo, String password, Response.Listener<String> listener){
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("correo", correo);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}