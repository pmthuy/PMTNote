package com.pmt.pmtnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> notes;
    ArrayAdapter<String> notesAddapter;
    ListView lvNotes;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupData();
    }

    private void setupData(){
        notes = new ArrayList<String>();
        notes.add("Note 1");
        notes.add("Note 2");
        notes.add("Note 3");
        readFile();
        notesAddapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, notes);

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
                        String note = notes.get(position).toString();
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

            String note = data.getExtras().getString("note");
            int position = data.getExtras().getInt("position",0);
            notes.set(position, note);
            notesAddapter.notifyDataSetChanged();
            writeFile();
        }
    }
    private void setupDeleteNoteHandle() {
        lvNotes.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        notes.remove(position);
                        notesAddapter.notifyDataSetChanged();
                        writeFile();
                        return false;
                    }
                }
        );
    }

    public void onAddNote(View view) {
        EditText etNewNote = (EditText)findViewById(R.id.etNewNote);
        notesAddapter.add(etNewNote.getText().toString());
        etNewNote.setText("");
        writeFile();
    }

    private void readFile(){
        File file = new File(getFilesDir(), "todo.txt");
        try{
            notes = new ArrayList<String>(FileUtils.readLines(file));
        }catch (IOException e){
            notes = new ArrayList<>();
        }
    }
    private void writeFile(){
        File file = new File(getFilesDir(), "todo.txt");
        try{
            FileUtils.writeLines(file, notes);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
