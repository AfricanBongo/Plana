package com.plana;

import com.plana.user.UserData;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        ScreenChanger.getInstance().getScreen(ScreenChanger.Screen.SPLASH);
    }

    @Override
    public void stop() throws Exception {
        if (UserData.getInstance().getCurrentUser() != null) {
            UserData.getInstance().save();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
