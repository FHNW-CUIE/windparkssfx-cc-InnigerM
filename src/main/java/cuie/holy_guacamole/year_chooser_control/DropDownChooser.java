package cuie.holy_guacamole.year_chooser_control;

import cuie.holy_guacamole.double_slider_control.DoubleSliderControl;
import javafx.scene.layout.VBox;


class DropDownChooser extends VBox {
    private static final String STYLE_CSS = "dropDownChooser.css";

    private final YearChooserControl yearChooserControl;

    private DoubleSliderControl sliderControl;

    DropDownChooser(YearChooserControl yearChooserControl) {
        this.yearChooserControl = yearChooserControl;
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
        yearChooserControl.startingValueProperty().bindBidirectional(sliderControl.minValueProperty());
        yearChooserControl.finishingValueProperty().bindBidirectional(sliderControl.maxValueProperty());
    }
}
