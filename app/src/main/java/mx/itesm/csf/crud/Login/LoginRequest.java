package mx.itesm.csf.crud.Login;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import mx.itesm.csf.crud.Controladores.Servicios;



public class LoginRequest extends StringRequest{
    private Map<String, String> params;

    public LoginRequest(String correo, String password, Response.Listener<String> listener){
        super(Method.POST, Servicios.LOGIN_REQUEST_URL_CORRECTED, listener, null);
        params = new HashMap<>();
        params.put("correo", correo);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
    @Override
    public Map < String, String > getHeaders() throws AuthFailureError {
        HashMap < String, String > headers = new HashMap <> ();
        String encodedCredentials = Base64.encodeToString("admin@tiendita.com:root".getBytes(), Base64.NO_WRAP);
        headers.put("Authorization", "Basic " + encodedCredentials);
        return headers;
    }
}