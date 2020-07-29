package com.plana;

import com.plana.user.UserData;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Optional;

/// Controller for the welcome screen
public class HomeController {

    @FXML private Button exitButton;
    @FXML private BorderPane homeBorderPane;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox loggedCheckBox;
    @FXML private Button signInButton;
    @FXML private Hyperlink newAccountHyperlink;
    @FXML private Label errorLabel;

    /// To be used when dragging the window
    private double xOffset = 0;
    private double yOffset = 0;


    @FXML
    public void initialize() {
        /// Allow the home view to be draggable
        homeBorderPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            }
        });

        homeBorderPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ScreenChanger.getInstance().getStage().setX(mouseEvent.getScreenX() - xOffset);
                ScreenChanger.getInstance().getStage().setY(mouseEvent.getScreenY() - yOffset);
            }
        });

        exitButton.setOnAction(e -> ScreenChanger.getInstance().getStage().close());
    }



    @FXML
    public void handleButtonPressed(ActionEvent actionEvent) throws Exception {

        String username = usernameTextField.getText();
        String password = passwordField.getText();
        boolean stayLoggedIn = loggedCheckBox.isSelected();

        /// Go to note view when signed in
        if (actionEvent.getSource().equals(signInButton)) {
            if (UserData.getInstance().signInUser(username, password, stayLoggedIn)) {

                /// Add, load and show Note View Screen
                startNoteViewScreen();

            } else {
                /// Show error label
                errorLabel.setTextFill(Color.RED);
            }
        }

        /// Create a new account
        else if (actionEvent.getSource().equals(newAccountHyperlink)) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Create a new account");
            dialog.initOwner(homeBorderPane.getScene().getWindow());

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("register.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

            /// Add finish and cancel buttons to dialog pane
            dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> inputResult = dialog.showAndWait();

            /// Verify input and create new account
            if (inputResult.isPresent() && inputResult.get().equals(ButtonType.FINISH)) {
                RegisterController registerController = fxmlLoader.getController();

                /// If an account has been successfully created, go to note view
                if (registerController.processResults()) {
                    startNoteViewScreen();
                }
            }
        }
    }


    /// Hide error label when fields are being typed
    @FXML
    public void handleKeyReleased() {
        errorLabel.setTextFill(Color.WHITE);
    }


    @FXML
    public void startNoteViewScreen() throws Exception {
        /// Add, load and show Note View Screen
        ScreenChanger.getInstance().getStage().close();
        ScreenChanger.getInstance().getScreen(ScreenChanger.Screen.NOTEVIEW);
    }

}
