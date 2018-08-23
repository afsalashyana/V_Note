package com.example.afsal.notes.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.afsal.notes.R;

public class NewNote extends AppCompatActivity {

    public static final String TO_SAVE = "TEXT_TO_SAVE";
    public static final String DATA_ID = "1001";
    public static final String KEY_ID = "1002";
    String key, dataVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_note);
        key = getIntent().getStringExtra(MainActivity.KEY);
        dataVal = getIntent().getStringExtra(MainActivity.DATA);
        EditText editText = findViewById(R.id.input_panel);
        editText.setText(dataVal);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lay, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveNote();
                return true;
        }
        return false;
    }

    private void saveNote() {
        EditText editText = findViewById(R.id.input_panel);
        if (editText.getText().length() > 1) {
            Intent output = new Intent();
            output.putExtra(DATA_ID, editText.getText().toString());
            output.putExtra(KEY_ID, key);
            setResult(RESULT_OK, output);
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        saveNote();
        super.onBackPressed();
    }

    public void discardNote(MenuItem item) {
        setResult(RESULT_CANCELED);
        Toast.makeText(getApplicationContext(), "Changes Discarded", Toast.LENGTH_SHORT).show();
        finish();
    }

}
