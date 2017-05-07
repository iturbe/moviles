package mx.itesm.csf.crud.Modelos;

import java.io.Serializable;

/**
 * Created by Alonso on 5/2/17.
 */

public class ProductoEnCarrito implements Serializable{

    private String nombre;
    private int precio;
    private int cantidad;
    private int subtotal;
    private int p_id;

    public ProductoEnCarrito(int p_id, String nombre, int precio, int cantidad) {
        this.p_id = p_id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = precio*cantidad;
    }

    public void update(){
        subtotal = precio*cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
        update();
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        update();
    }

    public int getSubtotal() {
        return subtotal;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }
}
