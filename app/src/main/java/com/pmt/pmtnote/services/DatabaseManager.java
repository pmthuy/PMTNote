package com.pmt.pmtnote.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pmt.pmtnote.models.Note;
import com.pmt.pmtnote.utils.EnumValue;

import java.util.ArrayList;

/**
 * Created by thuypm on 2/23/2016.
 */
public class DatabaseManager extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "todoApp";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_NOTES = "noteDatas";

    // Note Table Columns
    private static final String KEY_NOTE_ID = "id";
    private static final String KEY_NOTE_TEXT = "text";
    private static final String KEY_NOTE_DUEDATE = "duedate";
    private static final String KEY_NOTE_PRIORITY = "priority";


    private static DatabaseManager sInstance;

    public static synchronized DatabaseManager getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseManager(context.getApplicationContext());
            Log.d("DatabaseManager","---- init DatabaseManager");
        }else{
            Log.d("DatabaseManager","---- init DatabaseManager fail");
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES +
                "(" +
                KEY_NOTE_ID + " INTEGER PRIMARY KEY," +
                KEY_NOTE_TEXT + " TEXT," +
                KEY_NOTE_DUEDATE + " TEXT," +
                KEY_NOTE_PRIORITY + " INTEGER" +
                ")";
        Log.d(this.getClass().getName(), "---- onCreate: "+ CREATE_NOTES_TABLE);
        db.execSQL(CREATE_NOTES_TABLE);

        //Create default data
        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTE_TEXT, "Finish homeword");
        cv.put(KEY_NOTE_DUEDATE, "28/02/2016");
        cv.put(KEY_NOTE_PRIORITY, EnumValue.PRIORITY_HIGHT);
        db.insert(TABLE_NOTES, null, cv);
        cv.clear();

        cv.put(KEY_NOTE_TEXT, "Pay bill");
        cv.put(KEY_NOTE_DUEDATE, "28/02/2016");
        cv.put(KEY_NOTE_PRIORITY, EnumValue.PRIORITY_NOMAL);
        db.insert(TABLE_NOTES, null, cv);
        cv.clear();
        cv.put(KEY_NOTE_TEXT, "Come back home");
        cv.put(KEY_NOTE_DUEDATE, "28/02/2016");
        cv.put(KEY_NOTE_PRIORITY, EnumValue.PRIORITY_HIGHT);
        db.insert(TABLE_NOTES, null, cv);
        cv.clear();
        cv.put(KEY_NOTE_TEXT, "Buy new Phone for dad");
        cv.put(KEY_NOTE_DUEDATE, "28/02/2016");
        cv.put(KEY_NOTE_PRIORITY, EnumValue.PRIORITY_LOW);
        db.insert(TABLE_NOTES, null, cv);
        cv.clear();
        cv.put(KEY_NOTE_TEXT, "Buy new suilt for mom");
        cv.put(KEY_NOTE_DUEDATE, "28/02/2016");
        cv.put(KEY_NOTE_PRIORITY, EnumValue.PRIORITY_LOW);
        db.insert(TABLE_NOTES, null, cv);
        Log.d(this.getClass().getName(), "---- Init default data complete");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            //drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
            onCreate(db);
        }
    }
    // delete a note in the database
    public Note deleteNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_NOTES, KEY_NOTE_ID + "= ?", new String[]{(String.valueOf(note.id))});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "Error while trying to delete note " + note.toString(), e);
        } finally {
            db.endTransaction();
        }
        return note;
    }

    // Insert or update a note in the database
    public Note addOrUpdateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        long noteId = -1;

        db.beginTransaction();
        try {
            noteId = note.id;
            ContentValues values = new ContentValues();
            values.put(KEY_NOTE_TEXT, note.text);
            values.put(KEY_NOTE_DUEDATE, note.dueDate);
            values.put(KEY_NOTE_PRIORITY, note.priority);

            // First try to update the note in case the note already exists in the database
            int rows = db.update(TABLE_NOTES, values, KEY_NOTE_ID + "= ?", new String[]{(String.valueOf(noteId))});
            // Check if update succeeded
            if (rows != 1) {
                // note with this id did not already exist, so insert new note
                noteId = db.insertOrThrow(TABLE_NOTES, null, values);
                note.id = noteId;
                Log.d(this.getClass().getName(),"---- insert data: " + note.text);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(this.getClass().getName(), "Error while trying to add or update note", e);
        } finally {
            db.endTransaction();
        }
        return note;
    }

    // get all notes in the database
    public ArrayList<Note> getAllNote() {
        Log.d(this.getClass().getName(),"---- getAllNote: ");
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        db.beginTransaction();

        String selectQuery = "SELECT  * FROM " + TABLE_NOTES + " ORDER BY " + KEY_NOTE_ID;
        Log.d(this.getClass().getName(),"---- selectQuery: "+selectQuery);

        Cursor cursor      = db.rawQuery(selectQuery, null);
        String[] data      = null;
        Note note;
        Log.d(this.getClass().getName(),"---- cursor: "+cursor);
        if (cursor.moveToFirst()) {
            do {
                // get the data into array, or class variable
                note = new Note(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
                notes.add(note);
                Log.d(this.getClass().getName(), note.toString());
            } while (cursor.moveToNext());
        }
        Log.d(this.getClass().getName(),"---- size: " + notes.size());
        cursor.close();
        return notes;
    }

}