package com.pmt.pmtnote.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.pmt.pmtnote.R;
import com.pmt.pmtnote.models.Note;

public class EditNoteActivity extends AppCompatActivity {
    Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        EditText etEditNote = (EditText)findViewById(R.id.etEditNote);
        note = (Note)getIntent().getSerializableExtra("note");
        etEditNote.setText(note.text);
    }

    public void onEditNote(View view) {
        EditText etName = (EditText) findViewById(R.id.etEditNote);

        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        note.text = etName.getText().toString();
        data.putExtra("note", note);
        data.putExtra("position", getIntent().getIntExtra("position",0));
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }
}
