package com.de.k3ron3i.journalapp.DbHelper;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.de.k3ron3i.journalapp.DbHelper.Definitions;

public class NotesDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "noteslist.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public NotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_NOTESLIST_TABLE = "CREATE TABLE " + Definitions.NoteslistEntry.TABLE_NAME + " (" +
                Definitions.NoteslistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Definitions.NoteslistEntry.COLUMN_NOTE_HEAD + " TEXT NOT NULL, " +
                Definitions.NoteslistEntry.COLUMN_NOTE_CONTENT + " TEXT NOT NULL, " +
                Definitions.NoteslistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTESLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Definitions.NoteslistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}