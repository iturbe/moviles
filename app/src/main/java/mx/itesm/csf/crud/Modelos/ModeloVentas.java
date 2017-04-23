package mx.itesm.csf.crud.Modelos;

/**
 * Created by LUCÍA on 4/2/17.
 */

public class ModeloVentas {

    // definimos los componentes de cada objeto de ropa
    private String v_id, p_id, c_id, cantidad;

    public ModeloVentas(){}

    public ModeloVentas(String v_id, String p_id, String c_id, String cantidad) {
        this.v_id = v_id;
        this.p_id = p_id;
        this.c_id = c_id;
        this.cantidad = cantidad;

    }

    public String getV_id() {
        return v_id;
    }

    public void setV_id(String v_id) {
        this.v_id = v_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
