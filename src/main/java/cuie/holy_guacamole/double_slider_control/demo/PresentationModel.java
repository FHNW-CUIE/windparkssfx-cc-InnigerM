package cuie.holy_guacamole.double_slider_control.demo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PresentationModel {
    private final DoubleProperty pmMinValue = new SimpleDoubleProperty(2000);
    private final DoubleProperty pmMaxValue = new SimpleDoubleProperty(2020);

    public double getPmMinValue() {
        return pmMinValue.get();
    }

    public DoubleProperty pmMinValueProperty() {
        return pmMinValue;
    }

    public void setPmMinValue(double pmMinValue) {
        this.pmMinValue.set(pmMinValue);
    }

    public double getPmMaxValue() {
        return pmMaxValue.get();
    }

    public DoubleProperty pmMaxValueProperty() {
        return pmMaxValue;
    }

    public void setPmMaxValue(double pmMaxValue) {
        this.pmMaxValue.set(pmMaxValue);
    }
}
