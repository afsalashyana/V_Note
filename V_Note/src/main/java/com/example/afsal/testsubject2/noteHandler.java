package com.example.afsal.testsubject2;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class noteHandler {

    public static final String FILENAME = "MYDATAFILE";
    SharedPreferences dataSet;
    Map<String, String> myMap, resultMap;

    public noteHandler(Context context) {
        dataSet = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        getAllNotes();
    }

    public noteClass getNewNoteObject() {
        String Key = new Date().toString();
        noteClass obj = new noteClass();
        obj.setKey(Key);
        return obj;
    }

    public Map<String, String> getAllNotes() {
        resultMap = new HashMap<String, String>();
        myMap = (Map<String, String>) dataSet.getAll();
        SortedSet<String> sorted = new TreeSet(myMap.keySet());
        for (String str : sorted) {
            resultMap.put(str, myMap.get(str));
        }
        myMap.clear();
        return resultMap;
    }

    public void addNewNote(noteClass newNote) {
        SharedPreferences.Editor myEditor = dataSet.edit();
        myEditor.putString(newNote.getKey(), newNote.getData());
        myEditor.commit();
    }

    public void deleteNote(noteClass note) {
        if (dataSet.contains(note.getKey())) {
            SharedPreferences.Editor myEditor = dataSet.edit();
            myEditor.remove(note.getKey());
            myEditor.commit();
        } else
            Log.e("dataHandler", "CAnt Find Note To Delete");
    }
}
