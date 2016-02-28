package com.pmt.pmtnote.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.pmt.pmtnote.R;
import com.pmt.pmtnote.models.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thuypm on 2/24/2016.
 */
public class AddNoteDialog extends DialogFragment {

    private EditText mAddNote;
    private EditText etAddDueDate;
    private Spinner spAddPriority;
    private Button btnSaveAddNote;
    private static Note note;
    private static int position = 0;

    public interface AddNoteDialogListener {
        void onFinishAddDialog(Note note);
    }


    public AddNoteDialog() {
    }

    public static AddNoteDialog newInstance() {
        AddNoteDialog frag = new AddNoteDialog();
        AddNoteDialog.note = new Note();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 2. Setup a callback when the "Save" button is pressed on keyboard

        Log.d("TAG", "onCreateView");
        return inflater.inflate(R.layout.fragment_add_note, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG","onViewCreated");
        // Spinner element
        spAddPriority = (Spinner) view.findViewById(R.id.spAddPriority);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Hight");
        categories.add("Nomal");
        categories.add("Low");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spAddPriority.setAdapter(dataAdapter);
        spAddPriority.setSelection(0);

        btnSaveAddNote = (Button) view.findViewById(R.id.btnSaveAddNote);

        mAddNote = (EditText) view.findViewById(R.id.etAddNote);
        etAddDueDate = (EditText) view.findViewById(R.id.etAddDueDate);

        ((Button) view.findViewById(R.id.btnSaveAddNote)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddNoteDialogListener listener = (AddNoteDialogListener) getActivity();
                note.text = mAddNote.getText().toString();
                note.dueDate = etAddDueDate.getText().toString();
                note.priority = spAddPriority.getSelectedItemPosition() + 1;
                listener.onFinishAddDialog(note);
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });
    }

}