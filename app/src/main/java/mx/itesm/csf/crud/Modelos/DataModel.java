package mx.itesm.csf.crud.Modelos;


public class DataModel {

    // definimos los componentes de nuestro vehículo
    private String Clave_auto, Nombre, imagen,Precio, Clave_marca;
    //private Long Precio, Clave_marca;

    public DataModel(){}

    public DataModel (String Clave_auto, String Nombre, String Precio, String imagen, String Clave_marca) {
        this.Clave_auto = Clave_auto;
        this.Nombre = Nombre;
        this.Precio = Precio;
        this.imagen = imagen;
        this.Clave_marca = Clave_marca;
    }

    // Clave_auto
    public String getClave_auto() {
        return Clave_auto;
    }

    public void setClave_auto(String Clave_auto) {
        this.Clave_auto = Clave_auto;
    }

    // Nombre
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    // Precio
    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String Precio) {
        this.Precio = Precio;
    }

    // Imagen
    public String getimagen() {
        return imagen;
    }

    public void setimagen(String imagen) {
        this.imagen = imagen;
    }

    // Clave de la marca
    public String getClave_marca() {
        return Clave_marca;
    }

    public void setClave_marca(String Clave_marca) {
        this.Clave_marca = Clave_marca;
    }

}
