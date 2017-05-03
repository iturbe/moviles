package mx.itesm.csf.crud.Modelos;

/**
 * Created by Alonso on 5/2/17.
 */

public class ProductoEnCarrito {

    private String nombre;
    private int precio, cantidad, subtotal;

    public ProductoEnCarrito(String nombre, int precio, int cantidad, int subtotal) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
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
}
