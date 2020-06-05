package cuie.holy_guacamole.template_businesscontrol.demo;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import cuie.holy_guacamole.template_businesscontrol.BusinessControl;

class DemoPane extends BorderPane {
    private BusinessControl businessControl;

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

        businessControl = new BusinessControl();

        startingYearSlider = new Slider(1990, 2039, 0);
        finishingYearSlider = new Slider(1990, 2039, 0);

        readOnlyBox = new CheckBox();
        readOnlyBox.setSelected(false);

        mandatoryBox = new CheckBox();
        mandatoryBox.setSelected(true);

        labelField = new TextField();
    }

    private void layoutControls() {
        setCenter(businessControl);
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

        businessControl.startingValueProperty().bindBidirectional(model.startingYearProperty());
        businessControl.finishingValueProperty().bindBidirectional(model.finishingYearProperty());
        businessControl.startingLabelProperty().bind(model.startingYearLabelProperty());
        businessControl.readOnlyProperty().bind(model.startingYearReadOnlyProperty());
        businessControl.mandatoryProperty().bind(model.startingYearMandatoryProperty());
    }

}
