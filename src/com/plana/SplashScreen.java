package com.plana;

import com.plana.user.UserData;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class SplashScreen {
    @FXML private AnchorPane splashScreenAnchorPane;
    @FXML private Button continueButton;
    @FXML private Button exitButton;


    /// Show fade animation when the app is first lanuched
    @FXML
    public void initialize() {
        splashScreenAnchorPane.setOpacity(0);

        final FadeTransition show = new FadeTransition(new Duration(1000), splashScreenAnchorPane);
        show.setToValue(1);
        show.playFromStart();

    }


    @FXML
    public void handleButtonPressed(ActionEvent actionEvent) throws Exception{

        ScreenChanger.getInstance().getStage().close();

        /// Continue to sign in screen or note view screen
        if (actionEvent.getSource().equals(continueButton)) {
            if (UserData.getInstance().checkForCurrentUser()) {

                ScreenChanger.getInstance().getScreen(ScreenChanger.Screen.NOTEVIEW);
            } else {
                ScreenChanger.getInstance().getScreen(ScreenChanger.Screen.HOME);
            }
        }

    }
}
