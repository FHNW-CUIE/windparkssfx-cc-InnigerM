package cuie.holy_guacamole.year_chooser_control.demo;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import cuie.holy_guacamole.year_chooser_control.YearChooserControl;

class DemoPane extends BorderPane {
    private YearChooserControl yearChooserControl;

    private Slider startingYearSlider;
    private Slider finishingYearSlider;

    private CheckBox readOnlyBox;
    private CheckBox mandatoryBox;
    private TextField labelField;

    private PresentationModel model;

    DemoPane(PresentationModel model) {
        this.model = model;

        initializeControls();
        layoutControls();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        yearChooserControl = new YearChooserControl();

        startingYearSlider = new Slider(1990, 2029, 0);
        finishingYearSlider = new Slider(1990, 2029, 0);

        readOnlyBox = new CheckBox();
        readOnlyBox.setSelected(false);

        mandatoryBox = new CheckBox();
        mandatoryBox.setSelected(true);

        labelField = new TextField();
    }

    private void layoutControls() {
        setCenter(yearChooserControl);
        VBox box = new VBox(10,
                new Label("Business Control Properties"),
                new Label("Starting Year"), startingYearSlider,
                new Label("Finishing Year"), finishingYearSlider,
                new Label("readOnly"), readOnlyBox,
                new Label("mandatory"), mandatoryBox,
                new Label("Label"), labelField);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void setupValueChangeListeners() {
    }

    private void setupBindings() {
        startingYearSlider.valueProperty().bindBidirectional(model.startingYearProperty());
        finishingYearSlider.valueProperty().bindBidirectional(model.finishingYearProperty());
        labelField.textProperty().bindBidirectional(model.startingYearLabelProperty());
        readOnlyBox.selectedProperty().bindBidirectional(model.startingYearReadOnlyProperty());
        mandatoryBox.selectedProperty().bindBidirectional(model.startingYearMandatoryProperty());

        yearChooserControl.startingValueProperty().bindBidirectional(model.startingYearProperty());
        yearChooserControl.finishingValueProperty().bindBidirectional(model.finishingYearProperty());
        yearChooserControl.startingLabelProperty().bind(model.startingYearLabelProperty());
        yearChooserControl.readOnlyProperty().bind(model.startingYearReadOnlyProperty());
        yearChooserControl.mandatoryProperty().bind(model.startingYearMandatoryProperty());
    }

}
