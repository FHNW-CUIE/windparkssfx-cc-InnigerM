package cuie.holy_guacamole.template_simplecontrol.demo;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import cuie.holy_guacamole.template_simplecontrol.DoubleSliderControl;

public class DemoPane extends BorderPane {

    private final PresentationModel pm;

    // declare the custom control
    private DoubleSliderControl cc;

    // all controls
    private Label minValue;
    private Label maxValue;

    public DemoPane(PresentationModel pm) {
        this.pm = pm;
        initializeControls();
        layoutControls();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        cc = new DoubleSliderControl();

        minValue = new Label();

        maxValue = new Label();
    }

    private void layoutControls() {
        VBox controlPane = new VBox(new Label("SimpleControl Properties"),
                minValue, maxValue);
        controlPane.setPadding(new Insets(0, 50, 0, 50));
        controlPane.setSpacing(10);

        setCenter(cc);
        setRight(controlPane);
    }

    private void setupBindings() {
        minValue.textProperty().bind(pm.pmMinValueProperty().asString());
        maxValue.textProperty().bind(pm.pmMaxValueProperty().asString());

        cc.minValueProperty().bindBidirectional(pm.pmMinValueProperty());
        cc.maxValueProperty().bindBidirectional(pm.pmMaxValueProperty());
    }

}
