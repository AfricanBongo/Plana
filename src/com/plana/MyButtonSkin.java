package com.plana;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.control.skin.ButtonSkin;
import javafx.util.Duration;

public class MyButtonSkin extends ButtonSkin {

    // Features to apply to button
    enum FeatureType {
        SCALE, FLICKER, SOUND
    }
    public MyButtonSkin(Button button, FeatureType type) {
        super(button);

        switch (type) {
            case SCALE:
                addScale(button);
                break;
            case FLICKER:
                addFlicker(button);
                break;
        }
    }


    // Grows and shrinks the button
    private void addScale(Button button) {
        // put in scale transitions
        final ScaleTransition grow = new ScaleTransition(Duration.millis(100));
        grow.setNode(button);
        grow.setToX(1.25);
        grow.setToY(1.25);
        button.setOnMouseEntered(e -> grow.playFromStart());

        final ScaleTransition shrink = new ScaleTransition(Duration.millis(100));
        shrink.setNode(button);
        shrink.setToX(1);
        shrink.setToY(1);
        button.setOnMouseExited(e -> shrink.playFromStart());
        button.setOnAction(e ->shrink.playFromStart());
    }


    // Flickers the button by adjusting opacity
    private void addFlicker(Button button) {


    }
}
