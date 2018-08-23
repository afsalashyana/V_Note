package com.example.afsal.notes.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.afsal.notes.R;
import com.example.afsal.notes.controller.NoteController;
import com.example.afsal.notes.model.Note;
import com.example.afsal.notes.pref.SettingsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    public static final String KEY = "NOTE_KEY";
    public static final int NEW_NOTE = 100;
    public static final int DELETE_ID = 1010;
    public static final int SHARE_ID = 1011;
    public static final String DATA = "NOTE_DATA";
    public static final int UPDATE_NOTE = 101;

    long currentNote;
    ListView listHolder;
    ArrayAdapter adapter;
    Map<String, String> noteMap;
    List<String> noteListVals;
    List<String> noteListKeys;
    NoteController dataHandeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_act);

        dataHandeler = new NoteController(this);
        listHolder = findViewById(R.id.listView);
        listHolder.setOnItemClickListener(this);

        noteMap = dataHandeler.getAllNotes();
        noteListVals = new ArrayList<>(noteMap.values());
        noteListKeys = new ArrayList<>(noteMap.keySet());

        adapter = new ArrayAdapter<>(getBaseContext(), R.layout.list_lay, noteListVals);
        listHolder.setAdapter(adapter);

        registerForContextMenu(listHolder);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewNote();
            }
        });

        handleIncomingData();

        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean fullRequest = myPrefs.getBoolean("full_screen_switch", false);
        if (fullRequest) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void handleIncomingData() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    createNewNote(sharedText);
                }
            }
        }
    }

    public void createNewNote() {
        Intent newIntent = new Intent(getBaseContext(), NewNote.class);
        Note note = dataHandeler.getNewNoteObject();
        newIntent.putExtra(KEY, note.getKey());
        newIntent.putExtra(DATA, "");
        startActivityForResult(newIntent, NEW_NOTE);
    }

    public void createNewNote(String data) {
        Intent newIntent = new Intent(getBaseContext(), NewNote.class);
        Note note = dataHandeler.getNewNoteObject();
        newIntent.putExtra(KEY, note.getKey());
        newIntent.putExtra(DATA, data);
        startActivityForResult(newIntent, NEW_NOTE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Note tempNote = new Note();
        tempNote.setData(noteListVals.get(position));
        tempNote.setKey(noteListKeys.get(position));

        Intent newIntent = new Intent(getBaseContext(), NewNote.class);
        newIntent.putExtra(KEY, tempNote.getKey());
        newIntent.putExtra(DATA, tempNote.getData());
        startActivityForResult(newIntent, UPDATE_NOTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case NEW_NOTE:
                    String key = data.getStringExtra(NewNote.KEY_ID);
                    String value = data.getStringExtra(NewNote.DATA_ID);
                    Log.d("LOG", "Back From NEW_NOTE Activity --- " + data);
                    Note note = dataHandeler.getNewNoteObject();
                    note.setKey(key);
                    note.setData(value);
                    dataHandeler.addNewNote(note);
                    Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
                    Refresh();
                    break;
                case UPDATE_NOTE:
                    key = data.getStringExtra(NewNote.KEY_ID);
                    value = data.getStringExtra(NewNote.DATA_ID);
                    Log.d("LOG", "Back From UPDATE Activity --- " + data);
                    note = dataHandeler.getNewNoteObject();
                    note.setKey(key);
                    note.setData(value);
                    dataHandeler.addNewNote(note);
                    Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
                    Refresh();
                    break;
            }
        else
            Toast.makeText(this, "Note Discarded", Toast.LENGTH_LONG).show();

    }

    private void Refresh() {
        noteMap = dataHandeler.getAllNotes();
        adapter = new ArrayAdapter<>(getBaseContext(), R.layout.list_lay, new ArrayList(noteMap.values()));
        noteListKeys.clear();
        noteListVals.clear();
        noteListVals = new ArrayList<>(noteMap.values());
        noteListKeys = new ArrayList<>(noteMap.keySet());
        listHolder.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        currentNote = ((AdapterView.AdapterContextMenuInfo) menuInfo).id;
        menu.add(0, DELETE_ID, 0, "Delete");
        menu.add(0, SHARE_ID, 1, "Share");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                int location = (int) currentNote;
                Log.d("LOG", "Item to delete - KEY - " + noteListKeys.get(location) + " - VAL - " + noteListVals.get(location));
                Note deleteNote = new Note();
                deleteNote.setData(noteListVals.get(location));
                deleteNote.setKey(noteListKeys.get(location));
                dataHandeler.deleteNote(deleteNote);
                Toast.makeText(getBaseContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
                Refresh();
                break;
            case SHARE_ID:
                location = (int) currentNote;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, noteListVals.get(location));
                startActivity(Intent.createChooser(share, "Share Note With "));
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu, menu);
        return true;
    }

    public void loadPreferences(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
