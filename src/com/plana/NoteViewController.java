package com.plana;

import com.plana.note.Note;
import com.plana.note.NoteData;
import com.plana.user.UserData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class NoteViewController {

    @FXML private BorderPane mainBorderPane;
    @FXML private ToolBar noteToolBar;

    /// View menu button items
    @FXML private MenuItem aToZMenuItem;
    @FXML private MenuItem zToAMenuItem;
    @FXML private MenuItem urgentMenuItem;
    @FXML private MenuItem allMenuItem;

    /// Account menu button items
    @FXML private MenuItem signoutMenuItem;
    @FXML private MenuItem accountinfoMenuItem;
    @FXML private MenuItem deleteaccountMenuItem;

    /// Note view controls
    @FXML private ContextMenu listCellContextMenu;
    @FXML private Label datesLabel;
    @FXML private Button addButton;
    @FXML private ListView<Note> noteListView;
    @FXML private TextArea detailsTextArea;

    /// Sort according to time created
    private Comparator<Note> noteTimeComparator;

    /// Predicates for the date filters
    private Predicate<Note> displayAllItems;
    private Predicate<Note> displayUrgentItems;

    /// ListView lists
    private FilteredList<Note> noteFilteredList;
    private SortedList<Note> noteSortedList;


    /// Display new screen to add new note
    @FXML
    public void initialize() {

        /// Add context menu to listview
        listCellContextMenu = new ContextMenu();

        /// Create delete menu item, deletes selected note
        MenuItem deleteMenuItem = new MenuItem("Delete");

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Note note = noteListView.getSelectionModel().getSelectedItem();
                deleteNote(note);
            }
        });

        listCellContextMenu.getItems().addAll(deleteMenuItem);

        /// Add scale transition to the buttons
        addButton.setSkin(new MyButtonSkin(addButton, MyButtonSkin.FeatureType.SCALE));



        /// Display note details at the right side of program
        noteListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Note>() {

            final String urgent = null;

            @Override
            public void changed(ObservableValue<? extends Note> observableValue, Note note, Note t1) {
                /// saves the edits done in the text area to the previously selected ietm
                if (note != null) {
                    note.setDetails(detailsTextArea.getText());
                }

                /// show details in text area
                if (t1 != null) {

                    Note selectedItem = noteListView.getSelectionModel().getSelectedItem();

                    detailsTextArea.setText(selectedItem.getDetails());
                    detailsTextArea.setFont(new Font("Roboto Regular", 14));
                    datesLabel.setText(" Date Due: " + NoteData.NOTE_DTF.format(t1.getDateToBeDone()) + "\t||\t" +
                            "Date Created: " + NoteData.NOTE_DTF.format(t1.getTimeCreated()));
                }

            }
        });


        /// Set predicates
        displayAllItems = new Predicate<Note>() {
            @Override
            public boolean test(Note note) {
                return true;
            }
        };

        displayUrgentItems = new Predicate<Note>() {
            @Override
            public boolean test(Note note) {
                return (note.getDateToBeDone().isBefore(LocalDate.now().plusDays(1)));
            }
        };

        /// Set listview filter
        noteFilteredList = new FilteredList<Note>(NoteData.getInstance().getNoteList(), displayAllItems);


        /// Initialize to sort according to time created
        noteTimeComparator = new Comparator<Note>() {
            @Override
            public int compare(Note note, Note t1) {
                return note.getTimeCreated().compareTo(t1.getTimeCreated());
            }
        };


        /// View sorted list according to urgency
        noteSortedList = new SortedList<>(noteFilteredList, noteTimeComparator);


        /// Bind noteList to listview
        noteListView.setItems(noteSortedList);


        /// Colour notes according to their urgency
        noteListView.setCellFactory(new Callback<ListView<Note>, ListCell<Note>>() {
            @Override
            public ListCell<Note> call(ListView<Note> noteListView) {
                ListCell<Note> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Note note, boolean b) {
                        super.updateItem(note, b);

                        if (isEmpty()) {
                            setText(null);
                        } else {
                            setText(note.getBrief());
                            setFont(new Font("Roboto Regular", 18));
                        }
                    }
                };


                /// Add context menu to listview
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listCellContextMenu);
                            }
                        });

                return cell;
            }
        });

        /// Configure list view
        noteListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        noteListView.getSelectionModel().selectFirst();


    } /// End of method


    /// Shown when trying create a new note
    @FXML
    public void showDialogPane() {

        /// Create dialog layout
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Note");
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.initStyle(StageStyle.UNDECORATED);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialogueNote.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
            return;
        }

        /// Add finish and cancel buttons to dialog pane
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        /// Wait for input
        Optional<ButtonType> inputResult = dialog.showAndWait();

        /// Verify input and update list and listview
        if (inputResult.isPresent() && inputResult.get() == ButtonType.FINISH) {
            NewNoteDialogController dialogController = fxmlLoader.getController();
            Note newNote = dialogController.createNewNote();
            noteListView.getSelectionModel().select(newNote);
        }
    }


    /// Delete an item from the list
    @FXML
    public void deleteNote(Note note) {
        NoteData.getInstance().deleteNote(note);

        /// in the case that the list view is empty
        if (noteListView.getItems().isEmpty()) {
            detailsTextArea.clear();
            datesLabel.setText("");
        }
    }


    /// Allow delete and add key to be used in the program
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        /// Deletes an item from the list when the delete key is pressed
        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            Note note = noteListView.getSelectionModel().getSelectedItem();
            deleteNote(note);
        }
        /// Adds an item to the list when the add key is pressed
        if (keyEvent.getCode().equals(KeyCode.ADD)) {
            showDialogPane();
        }
    }



    /// Sort list methods
    @FXML
    public void sortList(ActionEvent actionEvent) {

        /// Ascending alphabetical order
        if (actionEvent.getSource().equals(aToZMenuItem)) {

            noteSortedList = new SortedList<>(noteFilteredList, new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return note.getBrief().compareToIgnoreCase(t1.getBrief());
                }
            });
        }

        /// Descending alphabetical order
        else if (actionEvent.getSource().equals(zToAMenuItem)) {

            noteSortedList = new SortedList<>(noteFilteredList, new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return t1.getBrief().compareToIgnoreCase(note.getBrief());
                }
            });
        }

        /// Set listview to sorted list
        noteListView.setItems(noteSortedList);
    }


    /// Filter list methods
    @FXML
    public void filterList(ActionEvent actionEvent) {

        /// Show urgent only
        if (actionEvent.getSource().equals(urgentMenuItem)) {
            noteFilteredList.setPredicate(displayUrgentItems);
        }
        /// Show all items
        else if (actionEvent.getSource().equals(allMenuItem)) {
            noteFilteredList.setPredicate(displayAllItems);
            noteSortedList = new SortedList<>(noteFilteredList, noteTimeComparator);
        }

        /// In the case where no note is displayed
        if (noteListView.getItems().isEmpty()) {
            detailsTextArea.clear();
        }

        noteListView.getSelectionModel().selectFirst();
    }


    /// Handle selection from account menu items
    @FXML
    public void handleAccountMenuSelection(ActionEvent actionEvent) throws Exception {

        /// Sign out
        if (actionEvent.getSource().equals(signoutMenuItem)) {
            UserData.getInstance().getCurrentUser().setCurrentUser(false);
            UserData.getInstance().save();
            ScreenChanger.getInstance().getStage().close();
            ScreenChanger.getInstance().getScreen(ScreenChanger.Screen.HOME);
        }

        /// Delete current user account
        else if (actionEvent.getSource().equals(deleteaccountMenuItem)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Delete Account");
            alert.setContentText("Are you sure you want to delete this account?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                if (UserData.getInstance().removeUser(UserData.getInstance().getCurrentUser())) {
                    ScreenChanger.getInstance().getStage().close();
                    ScreenChanger.getInstance().getScreen(ScreenChanger.Screen.HOME);
                }
            }

        }

        /// Show account info, allow user to change the info
        else if (actionEvent.getSource().equals(accountinfoMenuItem)) {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Account info");
            dialog.initOwner(mainBorderPane.getScene().getWindow());

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("accountinfo.fxml"));

            try {
                dialog.getDialogPane().setContent(fxmlLoader.load());
            } catch (IOException e){
                e.printStackTrace();
            }

            /// Add apply and close buttons to dialog pane
            dialog.getDialogPane().getButtonTypes().add(ButtonType.APPLY);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            Optional<ButtonType> inputResult = dialog.showAndWait();

            /// Verify input and update user account info
            if (inputResult.isPresent() && inputResult.get().equals(ButtonType.APPLY)) {
                AccountController accountController = fxmlLoader.getController();
                accountController.processResults();
            }
        }

    }
}
