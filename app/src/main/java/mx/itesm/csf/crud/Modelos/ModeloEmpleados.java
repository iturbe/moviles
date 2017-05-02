package mx.itesm.csf.crud.Modelos;

/**
 * Created by biller on 4/2/17.
 */

public class ModeloEmpleados {

    private String nombre, apellido, admin, correo, password;
    private int e_id;

    public ModeloEmpleados() {
    }

    public ModeloEmpleados(int e_id, String nombre, String apellido, String admin, String correo,String password) {
        this.e_id = e_id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.admin = admin;
        this.correo = correo;
        this.password = password;
    }

    public int getE_id() {
        return e_id;
    }

    public void setE_id(int e_id) {
        this.e_id = e_id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}