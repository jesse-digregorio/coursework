package com.jesse.course.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteActivity extends AppCompatActivity {

    EditText noteEditText;
    int noteId;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.jesse.course.notes", Context.MODE_PRIVATE);
        noteEditText = (EditText) findViewById(R.id.noteEditText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId !=  -1) {
            noteEditText.setText(MainActivity.getNoteById(noteId));
        }
        else {
            MainActivity.notesList.add("");
            noteId = MainActivity.notesList.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notesList.set(noteId, charSequence.toString());
                MainActivity.arrayAdapter.notifyDataSetChanged();

                HashSet<String> set = new HashSet<String>(MainActivity.notesList);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setNoteEditText(String noteContent) {
        noteEditText.setText(noteContent);
    }

    public String getNoteEditText() {
        return noteEditText.getText().toString();
    }


}
