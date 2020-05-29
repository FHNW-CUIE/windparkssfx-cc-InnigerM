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
    private static final PseudoClass STARTING_CONVERTIBLE_CLASS = PseudoClass.getPseudoClass("starting-convertible");
    private static final PseudoClass FINISHING_CONVERTIBLE_CLASS = PseudoClass.getPseudoClass("finishing-convertible");


    //todo: durch die eigenen regulaeren Ausdruecke ersetzen
    static final String FORMATTED_INTEGER_PATTERN = "%d";

    private static final String INTEGER_REGEX = "(19|20)[0-9]{2}";
    private static final String CONVERTING_REGEX = "[0-9]{2}";
    private static final Pattern INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX);
    private static final Pattern CONVERTING_PATTERN = Pattern.compile(CONVERTING_REGEX);

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

    private final BooleanProperty startingConvertible = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(STARTING_CONVERTIBLE_CLASS, get());
        }
    };

    private final BooleanProperty finishingConvertible = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(FINISHING_CONVERTIBLE_CLASS, get());
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

    public void resetStarting() {
        setStartingUserFacingText(convertToString(getStartingValue()));
    }

    public void increaseStarting() {
        setStartingValue(getStartingValue() + 1);
    }

    public void decreaseStarting() {
        setStartingValue(getStartingValue() - 1);
    }

    public void completeStarting() {
        int year = getStartingValue() % 100;
        if (year >= 50) {
            year += 1900;
        } else {
            year += 2000;
        }
        setStartingValue(year);
        setFinishingValue(year + 3);
    }

    public void resetFinishing() {
        setFinishingUserFacingText(convertToString(getFinishingValue()));
    }

    public void increaseFinishing() {
        setFinishingValue(getFinishingValue() + 1);
    }

    public void decreaseFinishing() {
        setFinishingValue(getFinishingValue() - 1);
    }

    public void completeFinishing() {
        int year = getFinishingValue() % 100;
        if (year >= 50) {
            year += 1900;
        } else {
            year += 2000;
        }
        setFinishingValue(year);
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

            if (isInteger(userInput) | isConvertible(userInput)) {
                setStartingInvalid(false);
                setStartingErrorMessage(null);
                setStartingValue(convertToInt(userInput));
                setStartingConvertible(false);
            } else {
                setStartingConvertible(false);
                setStartingInvalid(true);
                setStartingErrorMessage("Not an Integer");
            }

            if(finishingLowerStarting()) {
                setStartingInvalid(true);
                setFinishingInvalid(true);
            } else {
                setFinishingInvalid(false);
            }

            if (isConvertible(userInput)) {
                setStartingConvertible(true);
                setStartingInvalid(false);
            }
        });

        finishingUserFacingText.addListener((observable, oldValue, userInput) -> {
            if (isMandatory() && (userInput == null || userInput.isEmpty())) {
                setFinishingInvalid(true);
                setFinishingErrorMessage("Mandatory Field");
                return;
            }

            if (isInteger(userInput) | isConvertible(userInput)) {
                setFinishingInvalid(false);
                setFinishingErrorMessage(null);
                setFinishingValue(convertToInt(userInput));
                setFinishingConvertible(false);
            } else {
                setFinishingInvalid(true);
                setFinishingErrorMessage("Not an Integer");
            }

            if(finishingLowerStarting()) {
                setStartingInvalid(true);
                setFinishingInvalid(true);
            } else {
                setStartingInvalid(false);
            }

            if (isConvertible(userInput)) {
                setFinishingConvertible(true);
                setFinishingInvalid(false);
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

    private boolean finishingLowerStarting() {
        return (getStartingValue() > getFinishingValue());
    }

    private boolean isConvertible(String userInput) {
        return CONVERTING_PATTERN.matcher(userInput).matches();
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

    public boolean isStartingConvertible() {
        return startingConvertible.get();
    }

    public BooleanProperty startingConvertibleProperty() {
        return startingConvertible;
    }

    public void setStartingConvertible(boolean startingConvertible) {
        this.startingConvertible.set(startingConvertible);
    }

    public boolean isFinishingConvertible() {
        return finishingConvertible.get();
    }

    public BooleanProperty finishingConvertibleProperty() {
        return finishingConvertible;
    }

    public void setFinishingConvertible(boolean finishingConvertible) {
        this.finishingConvertible.set(finishingConvertible);
    }
}
