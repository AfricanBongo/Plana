package com.plana.user;

import com.plana.note.NoteData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/// Singleton User operations class
public class UserData {

    private static final UserData instance = new UserData();

    private User currentUser;
    public final static String USERS_FILE = "PlanaUsers.txt";
    private ObservableList<User> users;

    private UserData() {}

    public User getCurrentUser() {
        return currentUser;
    }

    public static UserData getInstance() {
        return instance;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = checkUser(currentUser);
    }


    /// Imitates Save feature
    public void save() throws Exception {
        saveUsers();
        saveCurrentUserNotes();
    }


    /// Load users from memory into program
    public void loadUsers() throws IOException{
        users = FXCollections.observableArrayList();
        Path path = Paths.get(USERS_FILE);

        /// Check if the file already exists
        File file = new File(USERS_FILE);

        String input;

        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            while ((input = br.readLine()) != null) {

                String[] userInfo = input.split("\t");

                User user = new User(userInfo[0], userInfo[1], userInfo[2], userInfo[3], Boolean.valueOf(userInfo[4]));
                users.add(user);
            }
        }
    }


    /// Get user from users list, and sign in
    public boolean signInUser(String username, String password, boolean stayLoggedIn) throws Exception {
        User user = checkUser(username);

        if (user != null && user.getPassword().equals(password)) {
            setCurrentUser(user);

            /// if the user wants to stay logged even when the program exits
            user.setCurrentUser(stayLoggedIn);
            loadCurrentUserNotes();
            return true;
        }

        return false;
    }


    /// Update user to memory
    public void saveUsers() throws IOException{
        Path path = Paths.get(USERS_FILE);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {

            for (User user : users) {

                /// Save current user when found
                if (user.getUserID().equals(getCurrentUser())) {
                    user.replace(getCurrentUser());
                }

                /// Write users to disk
                bw.write(String.format("%s\t%s\t%s\t%s\t%b",
                        user.getUsername(), user.getEncryptedPassword(), user.getUserID(),
                        user.getUserFilename(), user.isCurrentUser()));

                bw.newLine();
            }
        }
    }


    /// Load the current user's notes to memory
    public void loadCurrentUserNotes() throws Exception {
        NoteData.getInstance().loadNotes(getCurrentUser().getUserFilename());
    }


    /// Save the current user's notes to memory
    public void saveCurrentUserNotes() throws Exception {
        NoteData.getInstance().saveNotes(getCurrentUser().getUserFilename());
    }


    /// Check if there's a user who's already logged in, and log in if there is
    public boolean checkForCurrentUser() throws Exception {
        for (User user : users) {
            if (user.isCurrentUser()) {
                setCurrentUser(user);
                loadCurrentUserNotes();
                return true;
            }
        }

        return false;
    }


    /// Add new user
    public boolean addUser(User newUser) throws IOException {
        
        User userExisting = checkUser(newUser);
        
        if (userExisting == null) {
            users.add(newUser);
            createNewFile(newUser);
            return true;
        }
        
        return false;
    }


    /// Create new file for a new user
    private void createNewFile(User user) throws IOException {
        File newFile = new File(user.getUserFilename());
        newFile.createNewFile();
    }


    /// Delete a user account
    public boolean removeUser(String deleteUser) throws Exception{

        /// Fill with placeholders, you only need the username to find the account
        User tobeDeleted = checkUser(new User(deleteUser, deleteUser, false));

        return removeUser(tobeDeleted);

    }

    public boolean removeUser(User user) throws Exception{
        users.remove(user);
        File userFile = new File(user.getUserFilename());
        saveUsers();
        return userFile.delete();
    }
    
    
    public ObservableList<User> getUsers() {
        return users;
    }


    /// Check if a user already exists
    public User checkUser(User userToBeChecked) {
        for (User user : users) {
            if (user.getUserID().equals(userToBeChecked.getUserID()) || user.getUsername().equals(userToBeChecked.getUsername())) {
                return user;
            }
        }

        return null;
    }

    /// Check if user already exists using string argument
    public User checkUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }
}
