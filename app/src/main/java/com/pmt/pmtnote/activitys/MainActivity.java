package com.pmt.pmtnote.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.pmt.pmtnote.Addapters.NoteAddapter;
import com.pmt.pmtnote.R;
import com.pmt.pmtnote.models.Note;
import com.pmt.pmtnote.services.DatabaseManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Note> notes;
    NoteAddapter notesAddapter;
    ListView lvNotes;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main", "INIT Porject");
        DatabaseManager db = DatabaseManager.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupData();
    }

    private void setupData(){

        readData();
        notesAddapter = new NoteAddapter(this, notes);

        lvNotes = (ListView)findViewById(R.id.lvNotes);
        lvNotes.setAdapter(notesAddapter);
        setupDeleteNoteHandle();
        setupEditNoteHandle();
    }

    private void setupEditNoteHandle() {
        lvNotes.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Note note = notes.get(position);
                        Intent i = new Intent(MainActivity.this, EditNoteActivity.class);
                        i.putExtra("note", note); // pass arbitrary data to launched activity
                        i.putExtra("position", position); // pass arbitrary data to launched activity

                        startActivityForResult(i, REQUEST_CODE);
//                        setContentView(R.layout.activity_edit_note);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
//            setContentView(R.layout.activity_main);

            Note note = (Note)data.getSerializableExtra("note");
            int position = data.getExtras().getInt("position",0);
            notes.set(position, note);
            notesAddapter.notifyDataSetChanged();
        }
    }
    private void setupDeleteNoteHandle() {
        lvNotes.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Note note = notes.get(position);
                        DatabaseManager.getInstance(MainActivity.this).deleteNote(note);
                        notes.remove(position);
                        notesAddapter.notifyDataSetChanged();
                        return false;
                    }
                }
        );
    }

    public void onAddNote(View view) {
        EditText etNewNote = (EditText)findViewById(R.id.etNewNote);
        Note note = new Note(etNewNote.getText().toString());
        note = DatabaseManager.getInstance(this).addOrUpdateNote(note);
        notesAddapter.add(note);
        etNewNote.setText("");
    }

    private void readData(){
        try{
            notes = DatabaseManager.getInstance(this).getAllNote();
        }catch (Exception e){
            //genarate default data
            notes = new ArrayList<Note>();
        }
    }

}
