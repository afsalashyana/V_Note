package com.example.afsal.testsubject2;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class noteHandlerDB {

    public static final String TABLE_NAME = "NOTE";
    String[] cols = {"ID,DATA"};
    SQLiteDatabase mydatabase;
    Context baseContext;

    public noteHandlerDB(Context context, SQLiteDatabase db) {
        mydatabase = db;
        baseContext = context;

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (ID VARCHAR PRIMARY KEY,DATA VARCHAR);");
    }

    public void addNewNote(noteClass newNote) {
        String command = "INSERT INTO "+ TABLE_NAME +" VALUES('" + newNote.getKey() + "', '" + newNote.getData() + "');";
        mydatabase.execSQL(command);
        Log.d("DB", "Command to insert data - " + command);
    }

    public void insertUpdate(noteClass newNote) {
        String command = "UPDATE "+ TABLE_NAME +" SET DATA = '" + newNote.getData() + "' WHERE ID = '" + newNote.getKey() + "';";
        mydatabase.execSQL(command);
        Log.d("DB", "Command to insert data - " + command);
    }

    public Map<String, String> getAllNotes() {
        Map<String, String> myMap = new HashMap<>();
        Cursor ans = mydatabase.rawQuery("SELECT * FROM "+ TABLE_NAME , null);
        ans.moveToFirst();
        for (int i = 0; i < ans.getCount(); i++) {
            Log.d("DB", ans.getString(0) + " ----- " + ans.getString(1));
            myMap.put(ans.getString(0), ans.getString(1));
            ans.moveToNext();
        }
        return myMap;
    }

    public noteClass getNewNoteObject() {
        Locale locale = new Locale("en_US");
        Locale.setDefault(locale);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss Z");
        String Key = dateFormat.format(new Date());
        Log.d("LOG", "Making Object with key = " + Key);
        noteClass obj = new noteClass();
        obj.setKey(Key);
        return obj;
    }

    public void deleteNote(noteClass note) {
        String command = "DELETE FROM "+ TABLE_NAME +" WHERE ID = '" + note.getKey() + "';";
        mydatabase.execSQL(command);
    }

}
