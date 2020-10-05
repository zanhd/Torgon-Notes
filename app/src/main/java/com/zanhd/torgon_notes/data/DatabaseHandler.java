package com.zanhd.torgon_notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.zanhd.torgon_notes.model.Note;
import com.zanhd.torgon_notes.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BABY_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_TITLE + " TEXT,"
                + Constants.KEY_DETAILS + " TEXT,"
                + Constants.KEY_DATE_NAME + " LONG);";
        db.execSQL(CREATE_BABY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(db);
    }

    //CRUD operations
    //Adding a note
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TITLE,note.getTitle());
        values.put(Constants.KEY_DETAILS,note.getDetails());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME,null,values);

        //Log.d("checking", "AddNote: " + note.getTitle());
    }
    //fetching a note by its id
    public Note getNote(int id) {
        Note note = new Note();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID, Constants.KEY_TITLE, Constants.KEY_DETAILS, Constants.KEY_DATE_NAME},
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor != null) {
            cursor.moveToFirst();

            note.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
            note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
            note.setDetails(cursor.getString(cursor.getColumnIndex(Constants.KEY_DETAILS)));

            //convert Timestamp to something readable
            DateFormat dateFormat = DateFormat.getDateInstance();
            String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))));
            note.setDateNoteAdded(date);
        }

        return  note;
    }
    //fetching all notes from db
    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME,
                new String[]{Constants.KEY_ID, Constants.KEY_TITLE, Constants.KEY_DETAILS, Constants.KEY_DATE_NAME},
                null,
                null,null,null,Constants.KEY_DATE_NAME + " DESC");


        if(cursor.moveToFirst()) {
            do{
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.KEY_TITLE)));
                note.setDetails(cursor.getString(cursor.getColumnIndex(Constants.KEY_DETAILS)));

                //convert Timestamp to something readable
                DateFormat dateFormat = DateFormat.getDateInstance();
                String date = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))));
                note.setDateNoteAdded(date);

                noteList.add(note);
            }while(cursor.moveToNext());
        }

        return noteList;
    }

    //updating Note
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_TITLE,note.getTitle());
        values.put(Constants.KEY_DETAILS,note.getDetails());
        values.put(Constants.KEY_DATE_NAME,java.lang.System.currentTimeMillis());

        //update the note in database
        return db.update(Constants.TABLE_NAME,values,Constants.KEY_ID + "=?",new String[]{String.valueOf(note.getId())});
    }

    //delete Note
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.KEY_ID + "=?",new String[]{String.valueOf(id)});
        db.close();
    }

    //getNotesCount
    public int getNotesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery,null);
        return cursor.getCount();
    }
}
