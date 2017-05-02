package mx.itesm.csf.crud.Modelos;

/**
 * Created by LUCÍA on 4/2/17.
 */

public class ModeloVentas {

    // definimos los componentes de cada objeto de ropa
    private int v_id, p_id, c_id, cantidad;

    public ModeloVentas(){}

    public ModeloVentas(int v_id, int p_id, int c_id, int cantidad) {
        this.v_id = v_id;
        this.p_id = p_id;
        this.c_id = c_id;
        this.cantidad = cantidad;

    }

    public int getV_id() {
        return v_id;
    }

    public void setV_id(int v_id) {
        this.v_id = v_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
