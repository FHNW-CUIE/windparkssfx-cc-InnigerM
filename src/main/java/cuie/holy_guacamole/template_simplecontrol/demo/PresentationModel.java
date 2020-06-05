package cuie.holy_guacamole.template_simplecontrol.demo;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class PresentationModel {
    private final DoubleProperty pmMinValue = new SimpleDoubleProperty(2000);
    private final DoubleProperty pmMaxValue = new SimpleDoubleProperty(2020);
    private final ObjectProperty<Color> baseColor = new SimpleObjectProperty<>();

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

    public Color getBaseColor() {
        return baseColor.get();
    }

    public ObjectProperty<Color> baseColorProperty() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor.set(baseColor);
    }
}
