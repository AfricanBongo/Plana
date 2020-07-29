package com.plana;

import com.plana.note.Note;
import com.plana.note.NoteData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class NewNoteDialogController {

    @FXML private TextArea txtAreaNote;
    @FXML private TextField txtBrief;
    @FXML private DatePicker dpToBeDone;

    // Automatically set when new note is created
    private LocalDateTime timeCreated;


    // Default set dates to current date
    @FXML
    public void initialize() {
        dpToBeDone.setValue(LocalDate.now());
    }


    // Creates the new note when the finish button is clicked
    @FXML
    public Note createNewNote() {

        String brief;
        String details;

        // if brief area and/or text areas are left blank by user
        if (txtBrief.getText().isEmpty() || txtBrief.getText().trim().isEmpty()) {
            brief = "Untitled";
        } else {
            brief = txtBrief.getText();
        }

        // if a similar note already exists
        if (NoteData.getInstance().checkNote(brief)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setTitle("Duplicate Note");
            alert.setHeaderText(brief +" already exists");
            alert.setContentText("Saving this will overwrite the existing note\nAre you sure?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                NoteData.getInstance().deleteNote(brief);
            } else {
                return null;
            }

        }

        if (txtAreaNote.getText().isEmpty() || txtBrief.getText().trim().isEmpty()) {
            details = "New note...";
        } else {
            details = txtAreaNote.getText();
        }

        // Add dates
        LocalDate dateToBeDone = dpToBeDone.getValue();
        timeCreated = LocalDateTime.now();

        Note newNote = new Note(brief, details, timeCreated, dateToBeDone);
        NoteData.getInstance().addNote(newNote);

        return newNote;
    }

}
