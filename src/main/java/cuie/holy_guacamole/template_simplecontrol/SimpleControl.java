package cuie.holy_guacamole.template_simplecontrol;

import java.util.List;
import java.util.Locale;

import javafx.beans.property.*;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

/**
 * ToDo: CustomControl kurz beschreiben
 * <p>
 * ToDo: Autoren ergänzen / ersetzen
 *
 * @author
 */
//ToDo: Umbenennen.
public class SimpleControl extends Region {
    // wird gebraucht fuer StyleableProperties
    private static final StyleablePropertyFactory<SimpleControl> FACTORY = new StyleablePropertyFactory<>(Region.getClassCssMetaData());

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    private static final Locale CH = new Locale("de", "CH");

    private static final double MARGIN = 25;

    private static final double ARTBOARD_WIDTH = 500;  // ToDo: Breite der "Zeichnung" aus dem Grafik-Tool übernehmen
    private static final double ARTBOARD_HEIGHT = 100;  // ToDo: Anpassen an die Breite der Zeichnung

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH = 25;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final int MIN_VALUE = 1990;
    private static final int MAX_VALUE = 2030;

    private static double width;

    //Parts
    private Circle leftThumb;
    private Circle rightThumb;
    private Line line;

    // Properties
    private final IntegerProperty minValue = new SimpleIntegerProperty();
    private final IntegerProperty maxValue = new SimpleIntegerProperty();

    // ToDo: ergänzen mit allen CSS stylable properties
    private static final CssMetaData<SimpleControl, Color> BASE_COLOR_META_DATA = FACTORY.createColorCssMetaData("-base-color", s -> s.baseColor);

    private final StyleableObjectProperty<Color> baseColor = new SimpleStyleableObjectProperty<Color>(BASE_COLOR_META_DATA) {
        @Override
        protected void invalidated() {
            setStyle(String.format("%s: %s;", getCssMetaData().getProperty(), colorToCss(get())));
            applyCss();
        }
    };

    // fuer Resizing benoetigt
    private Pane drawingPane;

    public SimpleControl() {
        initializeSelf();
        initializeParts();
        initializeDrawingPane();
        layoutParts();
        setupEventHandlers();
        setupValueChangeListeners();
        setupBindings();
    }

    private void initializeSelf() {
        loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/Lato/Lato-Reg.ttf");
        addStylesheetFiles("style.css");

        getStyleClass().add("simple-control");  // ToDo: an den Namen der Klasse (des CustomControls) anpassen
    }

    private void initializeParts() {
        double center = ARTBOARD_WIDTH * 0.5;

        leftThumb = new Circle(MARGIN, MARGIN, MARGIN / 2);
        leftThumb.getStyleClass().add("left-thumb");
        setMinValue(MIN_VALUE);

        rightThumb = new Circle(ARTBOARD_WIDTH - MARGIN, MARGIN, MARGIN / 2);
        rightThumb.getStyleClass().add("right-thumb");
        setMaxValue(MAX_VALUE);

        line = new Line(MARGIN, MARGIN, ARTBOARD_WIDTH - MARGIN, MARGIN);
    }

    private void initializeDrawingPane() {
        drawingPane = new Pane();
        drawingPane.getStyleClass().add("drawing-pane");
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void layoutParts() {
        drawingPane.getChildren().addAll(line, leftThumb, rightThumb);

        getChildren().add(drawingPane);
    }

    private void setupEventHandlers() {
        leftThumb.setOnMouseDragged(event -> {
            int newValue = (int) mousePositionToValue(event);
            if (newValue < getMaxValue() && newValue >= MIN_VALUE && newValue <= MAX_VALUE) {
                setMinValue(newValue);
            }
        });

        rightThumb.setOnMouseDragged(event -> {
            int newValue = (int) mousePositionToValue(event);
            if (newValue > getMinValue() && newValue >= MIN_VALUE && newValue <= MAX_VALUE) {
                setMaxValue(newValue);
            }
        });
    }

    private void setupValueChangeListeners() {
        minValueProperty().addListener((observable, oldValue, newValue) -> updateUI());
        maxValueProperty().addListener((observable, oldValue, newValue) -> updateUI());
    }

    private void setupBindings() {
        //ToDo: dieses Binding ersetzen
    }

    private void updateUI() {
        double leftPosition = valueToThumbPosition(getMinValue());
        double rightPosition = valueToThumbPosition(getMaxValue());
        if (leftPosition < rightPosition) {
            if (leftPosition > MARGIN && leftPosition < width - MARGIN) {
                leftThumb.setCenterX(leftPosition);
            }
            if (rightPosition > MARGIN && rightPosition < width - MARGIN) {
                rightThumb.setCenterX(rightPosition);
            }
        }
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    //ToDo: ueberpruefen ob dieser Resizing-Ansatz anwendbar ist.
    private void resize() {
        Insets padding = getPadding();
        double availableWidth = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        width = availableWidth;

        if (availableWidth > 0 && availableHeight > 0) {
            //ToDo: ueberpruefen ob die drawingPane immer zentriert werden soll (eventuell ist zum Beispiel linksbuendig angemessener)
            leftThumb.setCenterX(valueToThumbPosition(getMinValue()));
            rightThumb.setCenterX(valueToThumbPosition(getMaxValue()));
            line.setStartX(MARGIN);
            line.setStartY(MARGIN);
            line.setEndX(width - MARGIN);
            line.setEndY(MARGIN);
        }
    }

    private void relocateDrawingPaneCentered() {
        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
    }

    private void relocateDrawingPaneCenterBottom(double scaleY, double paddingBottom) {
        double visualHeight = ARTBOARD_HEIGHT * scaleY;
        double visualSpace = getHeight() - visualHeight;
        double y = visualSpace + (visualHeight - ARTBOARD_HEIGHT) * 0.5 - paddingBottom;

        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, y);
    }

    private void relocateDrawingPaneCenterTop(double scaleY, double paddingTop) {
        double visualHeight = ARTBOARD_HEIGHT * scaleY;
        double y = (visualHeight - ARTBOARD_HEIGHT) * 0.5 + paddingTop;

        drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, y);
    }

    // Sammlung nuetzlicher Funktionen

    //ToDo: diese Funktionen anschauen und für die Umsetzung des CustomControls benutzen

    private void loadFonts(String... font) {
        for (String f : font) {
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }

    private void addStylesheetFiles(String... stylesheetFile) {
        for (String file : stylesheetFile) {
            String stylesheet = getClass().getResource(file).toExternalForm();
            getStylesheets().add(stylesheet);
        }
    }

    /**
     * Umrechnen einer Prozentangabe, zwischen 0 und 100, in den tatsaechlichen Wert innerhalb des angegebenen Wertebereichs.
     *
     * @param percentage Wert in Prozent
     * @param minValue   untere Grenze des Wertebereichs
     * @param maxValue   obere Grenze des Wertebereichs
     * @return value der akuelle Wert
     */
    private double percentageToValue(double percentage, double minValue, double maxValue) {
        return ((maxValue - minValue) * percentage) + minValue;
    }

    /**
     * Umrechnen des angegebenen Werts in eine Prozentangabe zwischen 0 und 100.
     *
     * @param value    der aktuelle Wert
     * @param minValue untere Grenze des Wertebereichs
     * @param maxValue obere Grenze des Wertebereichs
     * @return Prozentangabe des aktuellen Werts
     */
    private double valueToPercentage(double value, double minValue, double maxValue) {
        return (value - minValue) / (maxValue - minValue);
    }

    private double mousePositionToValue(MouseEvent event) {
        double percentage = ((event.getSceneX() - MARGIN) / (getWidth() - MARGIN * 2));
        return percentageToValue(percentage, MIN_VALUE, MAX_VALUE);
    }

    private double valueToThumbPosition(double value) {
        double percentage = valueToPercentage(value, MIN_VALUE, MAX_VALUE);
        return (getWidth() - MARGIN * 2) * percentage + MARGIN / 2;
    }

    private String colorToCss(final Color color) {
        return color.toString().replace("0x", "#");
    }


    // compute sizes

    @Override
    protected double computeMinWidth(double height) {
        Insets padding = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets padding = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets padding = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets padding = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    // alle getter und setter  (generiert via "Code -> Generate... -> Getter and Setter)

    // ToDo: ersetzen durch die Getter und Setter Ihres CustomControls
    public int getMinValue() {
        return minValue.get();
    }

    public IntegerProperty minValueProperty() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue.set(minValue);
    }

    public int getMaxValue() {
        return maxValue.get();
    }

    public IntegerProperty maxValueProperty() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue.set(maxValue);
    }

    public Color getBaseColor() {
        return baseColor.get();
    }

    public StyleableObjectProperty<Color> baseColorProperty() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor.set(baseColor);
    }
}