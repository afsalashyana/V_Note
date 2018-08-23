package com.example.afsal.notes.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.afsal.notes.model.Note;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class NoteController {

    private static final String FILENAME = "MYDATAFILE";
    private SharedPreferences dataSet;
    private Map<String, String> myMap, resultMap;

    public NoteController(Context context) {
        dataSet = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        getAllNotes();
    }

    public Note getNewNoteObject() {
        String Key = new Date().toString();
        Note obj = new Note();
        obj.setKey(Key);
        return obj;
    }

    public Map<String, String> getAllNotes() {
        resultMap = new HashMap<>();
        myMap = (Map<String, String>) dataSet.getAll();
        SortedSet<String> sorted = new TreeSet<>(myMap.keySet());
        for (String str : sorted) {
            resultMap.put(str, myMap.get(str));
        }
        myMap.clear();
        return resultMap;
    }

    public void addNewNote(Note newNote) {
        SharedPreferences.Editor myEditor = dataSet.edit();
        myEditor.putString(newNote.getKey(), newNote.getData());
        myEditor.apply();
    }

    public void deleteNote(Note note) {
        if (dataSet.contains(note.getKey())) {
            SharedPreferences.Editor myEditor = dataSet.edit();
            myEditor.remove(note.getKey());
            myEditor.apply();
        } else
            Log.e("dataHandler", "CAnt Find Note To Delete");
    }
}
