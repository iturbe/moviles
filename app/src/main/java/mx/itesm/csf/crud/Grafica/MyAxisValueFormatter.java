package mx.itesm.csf.crud.Grafica;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Alonso on 5/3/17.
 */

public class MyAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public MyAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }
}
