package com.plana;

import com.plana.user.UserData;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AccountController {

    @FXML private TextField usernameTextField;
    @FXML private TextField passwordField;
    @FXML private Button revealPasswordButton;

    /// Display the account info to the dialog pane
    @FXML
    public void initialize() {
        usernameTextField.setText( UserData.getInstance().getCurrentUser().getUsername());
        passwordField.setPromptText("Password is: " +  UserData.getInstance().getCurrentUser().getPassword() + ", type to change");
    }


    /// Process if there are any changes
    @FXML
    public void processResults() {
        String username = usernameTextField.getText();
        String password;

        /// If the password field is not empty change password
        if (!(passwordField.getText().trim().isEmpty() || passwordField.getText().isEmpty())) {
            password = passwordField.getText();
            UserData.getInstance().getCurrentUser().setPassword(password);
        }

        UserData.getInstance().getCurrentUser().setUsername(username);
    }
}
