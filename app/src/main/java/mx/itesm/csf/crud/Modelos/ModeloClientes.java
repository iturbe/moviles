package mx.itesm.csf.crud.Modelos;

/**
 * Created by biller on 4/2/17.
 */

public class ModeloClientes {
    private String nombre, apellido;
    private int c_id;

    public ModeloClientes() {}

    public ModeloClientes(int c_id, String nombre, String apellido) {
        this.c_id = c_id;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}