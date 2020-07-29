package com.plana.user;


import java.util.Base64;

public class User {

    /// User info
    private String username;
    private String encryptedPassword;
    private final String userID;
    private final String userFilename;

    /// Last login info
    boolean currentUser;

    /// Constructor
    public User(String username, String password, boolean currentUser) {
        this.username = username;
        this.encryptedPassword = encrypt(password);
        this.userID = produceID();
        this.userFilename = produceFilename();
        this.currentUser = currentUser;
    }

    /// Used when fetching user from memory
    public User(String username, String encryptedPassword, String userID, String userFilename, boolean currentUser) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.userID = userID;
        this.userFilename = userFilename;
        this.currentUser = currentUser;
    }

    /// Encrypt the password
    /// Got this from StackOverflow, credit to Al-Mothafar
    private String encrypt(String password) {
        String b64encoded = Base64.getEncoder().encodeToString(password.getBytes());

        /// reverse the String
        String reverse = new StringBuffer(b64encoded).reverse().toString();

        StringBuilder temp = new StringBuilder();
        final int OFFSET = 4;

        for (int i = 0; i < reverse.length(); i++) {
            temp.append((char) (reverse.charAt(i) + OFFSET));
        }

        return temp.toString();
    }


    /// Decrypt the password
    private String decrypt() {
        StringBuilder temp = new StringBuilder();
        final int OFFSET = 4;

        for (int i = 0; i < encryptedPassword.length(); i++) {
            temp.append((char) (encryptedPassword.charAt(i) - OFFSET));
        }

        String reversed = new StringBuffer(temp.reverse()).toString();

        return new String(Base64.getDecoder().decode(reversed));
    }

    private String produceID() {
        return Base64.getEncoder().encodeToString(this.username.getBytes());
    }

    private String produceFilename() {
        return this.userID + "notes.txt";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public String getPassword() {
        return decrypt();
    }

    public void replace(User user) {
        this.username = user.getUsername();
        setPassword(user.getPassword());
        this.currentUser = user.isCurrentUser();
        return;
    }
    /// Encrypt the password given as the argument
    public void setPassword(String password) {
        this.encryptedPassword = encrypt(password);
    }

    public String getUserID() {
        return userID;
    }

    public String getUserFilename() {
        return userFilename;
    }

    public boolean isCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(boolean currentUser) {
        this.currentUser = currentUser;
    }
}
