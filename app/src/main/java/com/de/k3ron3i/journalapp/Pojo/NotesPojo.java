package com.de.k3ron3i.journalapp.Pojo;

/**
 * Created by k3ron3i on 7/13/18.
 */

public class NotesPojo {

    String mNotes;
    String mNoteLastEdit;
    String rID;


    public NotesPojo() {
    }

    public NotesPojo(String mNotes, String mNoteLastEdit, String rID) {
        this.mNotes = mNotes;
        this.mNoteLastEdit = mNoteLastEdit;
        this.rID = rID;

    }

    public void setmNotes(String Notes) {
        this.mNotes = Notes;
    }

    public String getmNotes() {
        return mNotes;
    }

    public void setmNoteLastEdit(String NoteEdit) {
        this.mNoteLastEdit = NoteEdit;

    }

    public String getmNoteLastEdit() {
        return mNoteLastEdit;
    }

    public void setrID(String rID) {
        this.rID = rID;

    }

    public String getrID() {
        return rID;
    }
}
