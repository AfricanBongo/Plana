package com.plana.note;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Note implements Comparable{

    // Note info
    private String brief;
    private String details;
    private LocalDate dateToBeDone;
    private LocalDateTime timeCreated;

    // When a new note is created by the programmer
    public Note(String brief, String details, LocalDateTime timeCreated, LocalDate dateToBeDone) {
        this.brief = brief;
        this.details = details;
        this.timeCreated = timeCreated;
        this.dateToBeDone = dateToBeDone;
    }


    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getDateToBeDone() {
        return dateToBeDone;
    }

    public void setDateToBeDone(LocalDate dateToBeDone) {
        this.dateToBeDone = dateToBeDone;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if(obj instanceof Note) {
            return this.getBrief().equals(((Note) obj).getBrief());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.getBrief().hashCode();
    }

    @Override
    public int compareTo(Object o) {
        return this.getBrief().compareTo(((Note) o).getBrief());
    }

    @Override
    public String toString() {
        return this.getBrief();
    }
}
