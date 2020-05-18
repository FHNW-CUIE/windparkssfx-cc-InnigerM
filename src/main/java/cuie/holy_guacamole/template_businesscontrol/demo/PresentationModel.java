package cuie.holy_guacamole.template_businesscontrol.demo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PresentationModel {
    private final IntegerProperty startingYear = new SimpleIntegerProperty(2008);
    private final StringProperty startingYearLabel = new SimpleStringProperty("Starting Year");
    private final BooleanProperty startingYearReadOnly = new SimpleBooleanProperty(false);
    private final BooleanProperty startingYearMandatory = new SimpleBooleanProperty(true);

    private final IntegerProperty finishingYear = new SimpleIntegerProperty(2012);
    private final StringProperty finishingYearLabel = new SimpleStringProperty("Finishing Year");
    private final BooleanProperty finishingYearReadOnly = new SimpleBooleanProperty(false);
    private final BooleanProperty finishingYearMandatory = new SimpleBooleanProperty(true);

    public int getStartingYear() {
        return startingYear.get();
    }

    public IntegerProperty startingYearProperty() {
        return startingYear;
    }

    public void setStartingYear(int startingYear) {
        this.startingYear.set(startingYear);
    }

    public String getStartingYearLabel() {
        return startingYearLabel.get();
    }

    public StringProperty startingYearLabelProperty() {
        return startingYearLabel;
    }

    public void setStartingYearLabel(String startingYearLabel) {
        this.startingYearLabel.set(startingYearLabel);
    }

    public boolean getStartingYearReadOnly() {
        return startingYearReadOnly.get();
    }

    public BooleanProperty startingYearReadOnlyProperty() {
        return startingYearReadOnly;
    }

    public void setStartingYearReadOnly(boolean startingYearReadOnly) {
        this.startingYearReadOnly.set(startingYearReadOnly);
    }

    public boolean getStartingYearMandatory() {
        return startingYearMandatory.get();
    }

    public BooleanProperty startingYearMandatoryProperty() {
        return startingYearMandatory;
    }

    public void setStartingYearMandatory(boolean startingYearMandatory) {
        this.startingYearMandatory.set(startingYearMandatory);
    }

    public int getFinishingYear() {
        return finishingYear.get();
    }

    public IntegerProperty finishingYearProperty() {
        return finishingYear;
    }

    public void setFinishingYear(int finishingYear) {
        this.finishingYear.set(finishingYear);
    }

    public String getFinishingYearLabel() {
        return finishingYearLabel.get();
    }

    public StringProperty finishingYearLabelProperty() {
        return finishingYearLabel;
    }

    public void setFinishingYearLabel(String finishingYearLabel) {
        this.finishingYearLabel.set(finishingYearLabel);
    }

    public boolean isFinishingYearReadOnly() {
        return finishingYearReadOnly.get();
    }

    public BooleanProperty finishingYearReadOnlyProperty() {
        return finishingYearReadOnly;
    }

    public void setFinishingYearReadOnly(boolean finishingYearReadOnly) {
        this.finishingYearReadOnly.set(finishingYearReadOnly);
    }

    public boolean isFinishingYearMandatory() {
        return finishingYearMandatory.get();
    }

    public BooleanProperty finishingYearMandatoryProperty() {
        return finishingYearMandatory;
    }

    public void setFinishingYearMandatory(boolean finishingYearMandatory) {
        this.finishingYearMandatory.set(finishingYearMandatory);
    }
}
