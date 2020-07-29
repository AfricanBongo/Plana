package com.plana;

import com.plana.user.User;
import com.plana.user.UserData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.StageStyle;

public class RegisterController {

    @FXML private TextField usernameTextField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox signInCheckBox;


    @FXML
    public boolean processResults() throws Exception {

        /// Don't create because username already exists
        if (!checkUsername()) {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            boolean goingToLogIn = signInCheckBox.isSelected();

            User newUser = new User(username, password, goingToLogIn);

            if (UserData.getInstance().addUser(newUser)) {
                /// Sign in immediately with the new account
                UserData.getInstance().setCurrentUser(newUser);
                UserData.getInstance().loadCurrentUserNotes();
                return true;
            }

        } else {
            /// Notify user that the account already exists
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setHeaderText("Error in creation");
            alert.setContentText("Username: '" + usernameTextField.getText() + "', already exists");
            alert.showAndWait();
        }

        return false;
    }


    /// Checks the username field to see if the user already exists
    private boolean checkUsername() {
        return UserData.getInstance().checkUser(usernameTextField.getText()) != null;
    }
}
