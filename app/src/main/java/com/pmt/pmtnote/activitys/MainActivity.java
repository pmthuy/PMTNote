package com.pmt.pmtnote.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pmt.pmtnote.Addapters.NoteAddapter;
import com.pmt.pmtnote.Dialogs.AddNoteDialog;
import com.pmt.pmtnote.Dialogs.EditNoteDialog;
import com.pmt.pmtnote.R;
import com.pmt.pmtnote.models.Note;
import com.pmt.pmtnote.services.DatabaseManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements EditNoteDialog.EditNoteDialogListener, AddNoteDialog.AddNoteDialogListener {
    ArrayList<Note> notes;
    NoteAddapter notesAddapter;
    ListView lvNotes;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //create DataManager Instance
        DatabaseManager.getInstance(this);
        //set view to main
        setContentView(R.layout.activity_main);

        setupData();
    }

    //set up data and UI
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
                        showEditDialog(note, position);
                    }
                }
        );
    }

    private void showEditDialog(Note note, int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditNoteDialog editNoteDialog = EditNoteDialog.newInstance(note, position);
        editNoteDialog.show(fm, "fragment_edit_note");
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
        FragmentManager fm = getSupportFragmentManager();
        AddNoteDialog addNoteDialog = AddNoteDialog.newInstance();
        addNoteDialog.show(fm, "fragment_add_note");
    }

    private void readData(){
        try{
            notes = DatabaseManager.getInstance(this).getAllNote();
        }catch (Exception e){
            //genarate null data
            notes = new ArrayList<Note>();
        }
    }

    @Override
    public void onFinishEditDialog(Note note, int position) {
        note = DatabaseManager.getInstance(getApplicationContext()).addOrUpdateNote(note);
        notes.set(position, note);
        notesAddapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishAddDialog(Note note) {
        note = DatabaseManager.getInstance(getApplicationContext()).addOrUpdateNote(note);
        notesAddapter.add(note);
    }
}
