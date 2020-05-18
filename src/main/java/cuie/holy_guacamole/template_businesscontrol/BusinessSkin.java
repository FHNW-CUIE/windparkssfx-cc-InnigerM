package cuie.holy_guacamole.template_businesscontrol;

import java.util.Arrays;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Duration;

//todo: durch eigenen Skin ersetzen
class BusinessSkin extends SkinBase<BusinessControl> {
    private static final int IMG_SIZE = 12;
    private static final int IMG_OFFSET = 4;

    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP = "\uf106";

    private enum StartingState {
        VALID("Valid", "valid.png"),
        INVALID("Invalid", "invalid.png");

        public final String text;
        public final ImageView imageView;

        StartingState(final String text, final String file) {
            this.text = text;
            String url = BusinessSkin.class.getResource("/icons/" + file).toExternalForm();
            this.imageView = new ImageView(new Image(url,
                    IMG_SIZE, IMG_SIZE,
                    true, false));
        }
    }

    private enum FinishingState {
        VALID("Valid", "valid.png"),
        INVALID("Invalid", "invalid.png");

        public final String text;
        public final ImageView imageView;

        FinishingState(final String text, final String file) {
            this.text = text;
            String url = BusinessSkin.class.getResource("/icons/" + file).toExternalForm();
            this.imageView = new ImageView(new Image(url,
                    IMG_SIZE, IMG_SIZE,
                    true, false));
        }
    }

    private static final String STYLE_CSS = "style.css";

    // all parts
    private TextField startingYear;
    private TextField finishingYear;
    private Label startingReadOnlyYear;
    private Label finishingReadOnlyYear;
    private Popup popup;
    private Pane dropDownChooser;
    private Button startingChooserButton;
    private Button finishingChooserButton;

    private StackPane startingPane;
    private StackPane finishingPane;
    private VBox drawingPane;

    private Animation invalidStartingInputAnimation;
    private FadeTransition fadeOutValidStartingIconAnimation;

    private Animation invalidFinishingInputAnimation;
    private FadeTransition fadeOutValidFinishingIconAnimation;

    BusinessSkin(BusinessControl control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        setupAnimations();
        setupEventHandlers();
        setupValueChangedListeners();
        setupBindings();
    }

    private void initializeSelf() {
        getSkinnable().loadFonts("/fonts/Lato/Lato-Lig.ttf", "/fonts/Lato/Lato-Reg.ttf", "/fonts/ds_digital/DS-DIGI.TTF", "/fonts/fontawesome-webfont.ttf");
        getSkinnable().addStylesheetFiles(STYLE_CSS);
    }

    private void initializeParts() {
        startingYear = new TextField();
        startingYear.getStyleClass().addAll("editable-node", "starting-node");

        finishingYear = new TextField();
        finishingYear.getStyleClass().addAll("editable-node", "finishing-node");

        startingReadOnlyYear = new Label();
        startingReadOnlyYear.getStyleClass().add("read-only-node");

        finishingReadOnlyYear = new Label();
        finishingReadOnlyYear.getStyleClass().add("read-only-node");

        StartingState.VALID.imageView.setOpacity(0.0);

        FinishingState.VALID.imageView.setOpacity(0.0);

        startingChooserButton = new Button(ANGLE_DOWN);
        startingChooserButton.getStyleClass().add("chooser-button");

        finishingChooserButton = new Button(ANGLE_DOWN);
        finishingChooserButton.getStyleClass().add("chooser-button");

        dropDownChooser = new DropDownChooser(getSkinnable());

        popup = new Popup();
        popup.getContent().addAll(dropDownChooser);

        startingPane = new StackPane();
        startingPane.getStyleClass().add("starting-pane");

        finishingPane = new StackPane();
        finishingPane.getStyleClass().add("finishing-pane");

        drawingPane = new VBox();
        drawingPane.getStyleClass().add("drawing-pane");
    }

    private void layoutParts() {
        StackPane.setAlignment(startingChooserButton, Pos.CENTER_RIGHT);
        startingPane.getChildren().addAll(startingYear, startingChooserButton, startingReadOnlyYear);

        StackPane.setAlignment(finishingChooserButton, Pos.CENTER_RIGHT);
        finishingPane.getChildren().addAll(finishingYear, finishingChooserButton, finishingReadOnlyYear);

        Arrays.stream(StartingState.values())
                .map(startingState -> startingState.imageView)
                .forEach(startingImageView -> {
                    startingImageView.setManaged(false);
                    startingPane.getChildren().add(startingImageView);
                });

        Arrays.stream(FinishingState.values())
                .map(finishingState -> finishingState.imageView)
                .forEach(finishingImageView -> {
                    finishingImageView.setManaged(false);
                    finishingPane.getChildren().add(finishingImageView);
                });

        StackPane.setAlignment(startingYear, Pos.CENTER_LEFT);
        StackPane.setAlignment(startingReadOnlyYear, Pos.CENTER_LEFT);

        StackPane.setAlignment(finishingYear, Pos.CENTER_LEFT);
        StackPane.setAlignment(finishingReadOnlyYear, Pos.CENTER_LEFT);

        drawingPane.getChildren().addAll(startingPane, finishingPane);
        getChildren().add(drawingPane);
    }

    private void setupAnimations() {
        int delta = 5;
        Duration duration = Duration.millis(30);

        TranslateTransition moveStartingRight = getTranslateTransition(delta, duration, startingYear);
        TranslateTransition moveStartingLeft = getTranslateTransition(-delta, duration, startingYear);

        TranslateTransition moveFinishingRight = getTranslateTransition(delta, duration, finishingYear);
        TranslateTransition moveFinishingLeft = getTranslateTransition(-delta, duration, finishingYear);

        invalidStartingInputAnimation = new SequentialTransition(moveStartingRight, moveStartingLeft);
        invalidStartingInputAnimation.setCycleCount(3);

        invalidFinishingInputAnimation = new SequentialTransition(moveFinishingRight, moveFinishingLeft);
        invalidFinishingInputAnimation.setCycleCount(3);

        fadeOutValidStartingIconAnimation = new FadeTransition(Duration.millis(500), StartingState.VALID.imageView);
        fadeOutValidStartingIconAnimation.setDelay(Duration.seconds(1));
        fadeOutValidStartingIconAnimation.setFromValue(1.0);
        fadeOutValidStartingIconAnimation.setToValue(0.0);

        fadeOutValidFinishingIconAnimation = new FadeTransition(Duration.millis(500), FinishingState.VALID.imageView);
        fadeOutValidFinishingIconAnimation.setDelay(Duration.seconds(1));
        fadeOutValidFinishingIconAnimation.setFromValue(1.0);
        fadeOutValidFinishingIconAnimation.setToValue(0.0);
    }

    private TranslateTransition getTranslateTransition(int delta, Duration duration, Node animatedNode) {
        TranslateTransition moveRight = new TranslateTransition(duration, animatedNode);
        moveRight.setFromX(0.0);
        moveRight.setByX(delta);
        moveRight.setAutoReverse(true);
        moveRight.setCycleCount(2);
        moveRight.setInterpolator(Interpolator.LINEAR);
        return moveRight;
    }

    private void setupEventHandlers() {
        startingChooserButton.setOnAction(event -> {
            if (popup.isShowing()) {
                popup.hide();
            } else {
                popup.show(startingYear.getScene().getWindow());
            }
        });

        popup.setOnHidden(event -> startingChooserButton.setText(ANGLE_DOWN));

        popup.setOnShown(event -> {
            startingChooserButton.setText(ANGLE_UP);
            Point2D location = startingYear.localToScreen(startingYear.getWidth() - dropDownChooser.getPrefWidth() - 3,
                    startingYear.getHeight() - 3);

            popup.setX(location.getX());
            popup.setY(location.getY());
        });

        startingYear.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    getSkinnable().reset();
                    event.consume();
                    break;
                case UP:
                    getSkinnable().increase();
                    event.consume();
                    break;
                case DOWN:
                    getSkinnable().decrease();
                    event.consume();
                    break;
            }
        });
    }

    private void setupValueChangedListeners() {
        getSkinnable().startingInvalidProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                startInvalidStartingInputAnimation();
            } else {
                StartingState.VALID.imageView.setOpacity(1.0);
                startFadeOutValidStartingIconTransition();
            }
        });

        getSkinnable().finishingInvalidProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                startInvalidFinishingInputAnimation();
            } else {
                FinishingState.VALID.imageView.setOpacity(1.0);
                startFadeOutValidFinishingIconTransition();
            }
        });
    }

    private void setupBindings() {
        startingReadOnlyYear.textProperty().bind(getSkinnable().startingValueProperty().asString(BusinessControl.FORMATTED_INTEGER_PATTERN));
        startingYear.textProperty().bindBidirectional(getSkinnable().startingUserFacingTextProperty());

        finishingReadOnlyYear.textProperty().bind(getSkinnable().finishingValueProperty().asString(BusinessControl.FORMATTED_INTEGER_PATTERN));
        finishingYear.textProperty().bindBidirectional(getSkinnable().finishingUserFacingTextProperty());

        startingYear.promptTextProperty().bind(getSkinnable().startingLabelProperty());

        finishingYear.promptTextProperty().bind(getSkinnable().finishingLabelProperty());

        startingYear.visibleProperty().bind(getSkinnable().readOnlyProperty().not());
        startingChooserButton.visibleProperty().bind(getSkinnable().readOnlyProperty().not());
        startingReadOnlyYear.visibleProperty().bind(getSkinnable().readOnlyProperty());

        finishingYear.visibleProperty().bind(getSkinnable().readOnlyProperty().not());
        finishingChooserButton.visibleProperty().bind(getSkinnable().readOnlyProperty().not());
        finishingReadOnlyYear.visibleProperty().bind(getSkinnable().readOnlyProperty());

        StartingState.INVALID.imageView.visibleProperty().bind(getSkinnable().startingInvalidProperty());

        FinishingState.INVALID.imageView.visibleProperty().bind(getSkinnable().finishingInvalidProperty());

        StartingState.INVALID.imageView.xProperty().bind(startingYear.translateXProperty().add(startingYear.layoutXProperty()).subtract(IMG_OFFSET));
        StartingState.INVALID.imageView.yProperty().bind(startingYear.translateYProperty().add(startingYear.layoutYProperty()).subtract(IMG_OFFSET));
        StartingState.VALID.imageView.xProperty().bind(startingYear.layoutXProperty().subtract(IMG_OFFSET));
        StartingState.VALID.imageView.yProperty().bind(startingYear.layoutYProperty().subtract(IMG_OFFSET));

        FinishingState.INVALID.imageView.xProperty().bind(finishingYear.translateXProperty().add(finishingYear.layoutXProperty()).subtract(IMG_OFFSET));
        FinishingState.INVALID.imageView.yProperty().bind(finishingYear.translateYProperty().add(finishingYear.layoutYProperty()).subtract(IMG_OFFSET));
        FinishingState.VALID.imageView.xProperty().bind(finishingYear.layoutXProperty().subtract(IMG_OFFSET));
        FinishingState.VALID.imageView.yProperty().bind(finishingYear.layoutYProperty().subtract(IMG_OFFSET));
    }

    private void startFadeOutValidStartingIconTransition() {
        if (fadeOutValidStartingIconAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            return;
        }
        fadeOutValidStartingIconAnimation.play();
    }

    private void startFadeOutValidFinishingIconTransition() {
        if (fadeOutValidFinishingIconAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            return;
        }
        fadeOutValidFinishingIconAnimation.play();
    }

    private void startInvalidStartingInputAnimation() {
        if (invalidStartingInputAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            invalidStartingInputAnimation.stop();
        }
        invalidStartingInputAnimation.play();
    }

    private void startInvalidFinishingInputAnimation() {
        if (invalidFinishingInputAnimation.getStatus().equals(Animation.Status.RUNNING)) {
            invalidFinishingInputAnimation.stop();
        }
        invalidFinishingInputAnimation.play();
    }

    private void loadFonts(String... font) {
        for (String f : font) {
            Font.loadFont(getClass().getResourceAsStream(f), 0);
        }
    }
}
