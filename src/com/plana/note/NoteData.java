package com.plana.note;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

/// Singleton Note operations class
public class NoteData {

    /// Instance of the singleton class
    private static final NoteData instance = new NoteData();

    /// Date format
    public final static DateTimeFormatter NOTE_DTF = DateTimeFormatter.ofPattern("dd MMMM, yyyy");
    public final static DateTimeFormatter CREATED_TIME_DTF = DateTimeFormatter.ofPattern("HH:mm:ss dd MMMM, yyyy");

    /// Store list of notes fetched from memory, allow for data binding
    private ObservableList<Note> noteList;

    private NoteData() {
    }

    /// getters
    public static NoteData getInstance() {
        return instance;
    }

    public ObservableList<Note> getNoteList() {
        return noteList;
    }

    public void addNote(Note newNote) {
        noteList.add(newNote);
    }

    public void deleteNote(Note noteToBeDeleted) {
        noteList.remove(noteToBeDeleted);
    }

    /// Load notes into memory
    public void loadNotes(String filename) throws IOException {
        noteList = FXCollections.observableArrayList();
        Path path = Paths.get(filename);

        String input;

        try (BufferedReader br = Files.newBufferedReader(path)) {

            while ((input = br.readLine()) != null) {
                String[] noteInfo = input.split("\t");

                String brief = noteInfo[0];
                String details = noteInfo[1];
                LocalDateTime timeCreated = LocalDateTime.parse(noteInfo[2], CREATED_TIME_DTF);
                LocalDate dateToBeDone = LocalDate.parse(noteInfo[3], NOTE_DTF);

                Note newNote = new Note(brief, details, timeCreated, dateToBeDone);
                noteList.add(newNote);
            }
        }

    } /// End of method


    /// Saves notes into memory
    public void saveNotes(String filename) throws IOException {
        Path path = Paths.get(filename);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {

            Note currentNote;
            Iterator<Note> noteIterator = noteList.iterator();

            /// Iterate though list adding items to memory
            while (noteIterator.hasNext()) {
                currentNote = noteIterator.next();

                bw.write(String.format("%s\t%s\t%s\t%s",
                        currentNote.getBrief(), currentNote.getDetails(),
                        currentNote.getTimeCreated().format(CREATED_TIME_DTF),
                        currentNote.getDateToBeDone().format(NOTE_DTF)));

                bw.newLine();
            }
        }
    }


    /// Delete a note using the brief description string
    public boolean deleteNote(String string) {
        for (Note note: noteList) {
            if (note.getBrief().equals(string)) {
                noteList.remove(note);
                return true;
            }
        }

        return false;
    }


    /// Check if exists
    public boolean checkNote(String string) {
        for (Note note: noteList) {
            if (note.getBrief().equals(string)) {
                return true;
            }
        }

        return false;
    }

}
