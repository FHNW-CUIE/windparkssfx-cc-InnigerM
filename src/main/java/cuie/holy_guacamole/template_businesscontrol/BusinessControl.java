package cuie.holy_guacamole.template_businesscontrol;

import java.util.regex.Pattern;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.text.Font;

//todo: umbenennen
public class BusinessControl extends Control {
    private static final PseudoClass MANDATORY_CLASS = PseudoClass.getPseudoClass("mandatory");
    private static final PseudoClass STARTING_INVALID_CLASS = PseudoClass.getPseudoClass("starting-invalid");
    private static final PseudoClass FINISHING_INVALID_CLASS = PseudoClass.getPseudoClass("finishing-invalid");


    //todo: durch die eigenen regulaeren Ausdruecke ersetzen
    static final String FORMATTED_INTEGER_PATTERN = "%,d";

    private static final String INTEGER_REGEX = "[+-]?[\\d']{1,14}";
    private static final Pattern INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX);

    //todo: Integer bei Bedarf ersetzen
    private final IntegerProperty startingValue = new SimpleIntegerProperty();
    private final StringProperty startingUserFacingText = new SimpleStringProperty();

    private final IntegerProperty finishingValue = new SimpleIntegerProperty();
    private final StringProperty finishingUserFacingText = new SimpleStringProperty();

    private final BooleanProperty mandatory = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(MANDATORY_CLASS, get());
        }
    };

    private final BooleanProperty startingInvalid = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(STARTING_INVALID_CLASS, get());
        }
    };

    private final BooleanProperty finishingInvalid = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(FINISHING_INVALID_CLASS, get());
        }
    };

    //todo: ergaenzen um convertible

    private final BooleanProperty readOnly = new SimpleBooleanProperty();
    private final StringProperty startingLabel = new SimpleStringProperty();
    private final StringProperty finishingLabel = new SimpleStringProperty();
    private final StringProperty startingErrorMessage = new SimpleStringProperty();
    private final StringProperty finishingErrorMessage = new SimpleStringProperty();


    public BusinessControl() {
        initializeSelf();
        addValueChangeListener();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new BusinessSkin(this);
    }

    public void reset() {
        setStartingUserFacingText(convertToString(getStartingValue()));
        setFinishingUserFacingText(convertToString(getFinishingValue()));
    }

    public void increase() {
        setStartingValue(getStartingValue() + 1);
    }

    public void decrease() {
        setStartingValue(getStartingValue() - 1);
    }

    private void initializeSelf() {
        getStyleClass().add("business-control");

        setStartingUserFacingText(convertToString(getStartingValue()));
        setFinishingUserFacingText(convertToString(getFinishingValue()));
    }

    //todo: durch geeignete Konvertierungslogik ersetzen
    private void addValueChangeListener() {
        startingUserFacingText.addListener((observable, oldValue, userInput) -> {
            if (isMandatory() && (userInput == null || userInput.isEmpty())) {
                setStartingInvalid(true);
                setStartingErrorMessage("Mandatory Field");
                return;
            }

            if (isInteger(userInput)) {
                setStartingInvalid(false);
                setStartingErrorMessage(null);
                setStartingValue(convertToInt(userInput));
            } else {
                setStartingInvalid(true);
                setStartingErrorMessage("Not an Integer");
            }
        });

        finishingUserFacingText.addListener((observable, oldValue, userInput) -> {
            if (isMandatory() && (userInput == null || userInput.isEmpty())) {
                setFinishingInvalid(true);
                setFinishingErrorMessage("Mandatory Field");
                return;
            }

            if (isInteger(userInput)) {
                setFinishingInvalid(false);
                setFinishingErrorMessage(null);
                setFinishingValue(convertToInt(userInput));
            } else {
                setFinishingInvalid(true);
                setFinishingErrorMessage("Not an Integer");
            }
        });

        startingValueProperty().addListener((observable, oldValue, newValue) -> {
            setStartingInvalid(false);
            setStartingErrorMessage(null);
            setStartingUserFacingText(convertToString(newValue.intValue()));
        });

        finishingValueProperty().addListener((observable, oldValue, newValue) -> {
            setFinishingInvalid(false);
            setFinishingErrorMessage(null);
            setFinishingUserFacingText(convertToString(newValue.intValue()));
        });
    }

    //todo: Forgiving Format implementieren

    public void loadFonts(String... font) {
        for (String f : font) {
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    public void addStylesheetFiles(String... stylesheetFile) {
        for (String file : stylesheetFile) {
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }

    private boolean isInteger(String userInput) {
        return INTEGER_PATTERN.matcher(userInput).matches();
    }

    private int convertToInt(String userInput) {
        return Integer.parseInt(userInput);
    }

    private String convertToString(int newValue) {
        return String.format(FORMATTED_INTEGER_PATTERN, newValue);
    }


    // alle  Getter und Setter
    public int getStartingValue() {
        return startingValue.get();
    }

    public IntegerProperty startingValueProperty() {
        return startingValue;
    }

    public void setStartingValue(int startingValue) {
        this.startingValue.set(startingValue);
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }

    public boolean isMandatory() {
        return mandatory.get();
    }

    public BooleanProperty mandatoryProperty() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory.set(mandatory);
    }

    public String getStartingLabel() {
        return startingLabel.get();
    }

    public StringProperty startingLabelProperty() {
        return startingLabel;
    }

    public void setStartingLabel(String startingLabel) {
        this.startingLabel.set(startingLabel);
    }

    public String getFinishingLabel() {
        return finishingLabel.get();
    }

    public StringProperty finishingLabelProperty() {
        return finishingLabel;
    }

    public void setFinishingLabel(String finishingLabel) {
        this.finishingLabel.set(finishingLabel);
    }

    public BooleanProperty startingInvalidProperty() {
        return startingInvalid;
    }

    public void setStartingInvalid(boolean startingInvalid) {
        this.startingInvalid.set(startingInvalid);
    }

    public boolean getStartingInvalid() {
        return startingInvalid.get();
    }

    public BooleanProperty finishingInvalidProperty() {
        return finishingInvalid;
    }

    public void setFinishingInvalid(boolean finishingInvalid) {
        this.finishingInvalid.set(finishingInvalid);
    }

    public boolean getFinishingInvalid() {
        return finishingInvalid.get();
    }

    public String getStartingErrorMessage() {
        return startingErrorMessage.get();
    }

    public StringProperty startingErrorMessageProperty() {
        return startingErrorMessage;
    }

    public void setStartingErrorMessage(String startingErrorMessage) {
        this.startingErrorMessage.set(startingErrorMessage);
    }

    public String getFinishingErrorMessage() {
        return finishingErrorMessage.get();
    }

    public StringProperty finishingErrorMessageProperty() {
        return finishingErrorMessage;
    }

    public void setFinishingErrorMessage(String finishingErrorMessage) {
        this.finishingErrorMessage.set(finishingErrorMessage);
    }

    public String getStartingUserFacingText() {
        return startingUserFacingText.get();
    }

    public StringProperty startingUserFacingTextProperty() {
        return startingUserFacingText;
    }

    public void setStartingUserFacingText(String startingUserFacingText) {
        this.startingUserFacingText.set(startingUserFacingText);
    }

    public int getFinishingValue() {
        return finishingValue.get();
    }

    public IntegerProperty finishingValueProperty() {
        return finishingValue;
    }

    public void setFinishingValue(int finishingValue) {
        this.finishingValue.set(finishingValue);
    }

    public String getFinishingUserFacingText() {
        return finishingUserFacingText.get();
    }

    public StringProperty finishingUserFacingTextProperty() {
        return finishingUserFacingText;
    }

    public void setFinishingUserFacingText(String finishingUserFacingText) {
        this.finishingUserFacingText.set(finishingUserFacingText);
    }
}
