package com.plana;

import com.plana.user.UserData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// Controls the screens of the application
public class ScreenChanger {

    /// The types of screens the app can display
    enum Screen{
        HOME, NOTEVIEW, SPLASH
    }

    private static final ScreenChanger instance = new ScreenChanger();
    private Scene main;
    private Stage stage;
    private final Image icon;

    private ScreenChanger() {
        this.icon = new Image(getClass().getResourceAsStream("planaicon.png"));
    }


    /// Load a stage and view a screen
    public void getScreen(Screen screen) throws Exception{
        /// Show home/signin pane
        if (screen == Screen.HOME) {
            main = new Scene(FXMLLoader.load(getClass().getResource("home.fxml")), 1000, 600);
            stage = new Stage(StageStyle.UNDECORATED);

        }
        /// Show noteview pane, but first load the notes into the listview
        else if (screen == Screen.NOTEVIEW) {
            UserData.getInstance().loadCurrentUserNotes();
            main = new Scene(FXMLLoader.load(getClass().getResource("noteview.fxml")), 1000, 600);
            stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Plana");

        }
        /// Show splashscreen pane
        else if (screen == Screen.SPLASH) {
            UserData.getInstance().loadUsers();
            main = new Scene(FXMLLoader.load(getClass().getResource("splashscreen.fxml")));
            stage = new Stage(StageStyle.UNDECORATED);
        }

        stage.setScene(main);
        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }

    public static ScreenChanger getInstance() {
        return instance;
    }
}
