package com.de.k3ron3i.journalapp.DbHelper;

import android.provider.BaseColumns;

public class Definitions {

    public static final class NoteslistEntry implements BaseColumns {
        public static final String TABLE_NAME = "noteslist";
        public static final String COLUMN_NOTE_EDIT_DATE = "dateedited";
        public static final String COLUMN_NOTE_CONTENT= "notecontent";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
