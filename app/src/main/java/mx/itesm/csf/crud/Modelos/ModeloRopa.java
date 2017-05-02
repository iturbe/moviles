package mx.itesm.csf.crud.Modelos;

/**
 * Created by Alonso on 4/2/17.
 */

public class ModeloRopa {

    // definimos los componentes de cada objeto de ropa
    private String nombre, imagen, stock, precio;
    private  int p_id;

    public ModeloRopa(){}

    public ModeloRopa(int p_id, String nombre, String imagen, String stock, String precio) {
        this.p_id = p_id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.stock = stock;
        this.precio = precio;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) { this.p_id = p_id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
