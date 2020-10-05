package com.zanhd.torgon_notes.model;

public class Note {
    private int id;
    private String title;
    private String details;
    private String dateNoteAdded;

    //Constructors
    public Note() {}

    public Note(String title, String details) {
        this.title = title;
        this.details = details;
    }

    public Note(int id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }

    public Note(int id, String title, String details, String dateNoteAdded) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.dateNoteAdded = dateNoteAdded;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDateNoteAdded() {
        return dateNoteAdded;
    }

    public void setDateNoteAdded(String dateNoteAdded) {
        this.dateNoteAdded = dateNoteAdded;
    }
}
