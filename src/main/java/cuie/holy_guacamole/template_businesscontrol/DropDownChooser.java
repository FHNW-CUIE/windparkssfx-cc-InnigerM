package cuie.holy_guacamole.template_businesscontrol;

import cuie.holy_guacamole.double_slider_control.DoubleSliderControl;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


class DropDownChooser extends VBox {
    private static final String STYLE_CSS = "dropDownChooser.css";

    private final BusinessControl businessControl;

    private DoubleSliderControl sliderControl;

    DropDownChooser(BusinessControl businessControl) {
        this.businessControl = businessControl;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupBindings();
    }

    private void initializeSelf() {
        getStyleClass().add("drop-down-chooser");

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getStylesheets().add(stylesheet);
    }

    private void initializeParts() {
        sliderControl = new DoubleSliderControl();
    }

    private void layoutParts() {
        getChildren().addAll(sliderControl);
    }

    private void setupBindings() {
        businessControl.startingValueProperty().bindBidirectional(sliderControl.minValueProperty());
        businessControl.finishingValueProperty().bindBidirectional(sliderControl.maxValueProperty());
    }
}
