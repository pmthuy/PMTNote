package com.pmt.pmtnote.Dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
public class EditNoteDialog extends DialogFragment {

    private EditText mEditNote;
    private EditText etDueDate;
    private Spinner spPriority;
    private Button btEditNote;
    private static Note note;
    private static int position = 0;

    public interface EditNoteDialogListener {
        void onFinishEditDialog(Note note, int position);
    }


    public EditNoteDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditNoteDialog newInstance(Note note, int position) {
        EditNoteDialog.note = note;
        EditNoteDialog.position = position;
        EditNoteDialog frag = new EditNoteDialog();
        return frag;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 2. Setup a callback when the "Save" button is pressed on keyboard

        Log.d("TAG", "onCreateView");
        return inflater.inflate(R.layout.fragment_edit_note, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("TAG","onViewCreated");
        // Spinner element
        spPriority = (Spinner) view.findViewById(R.id.spPriority);

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
        spPriority.setAdapter(dataAdapter);
        spPriority.setSelection(0);

        btEditNote = (Button) view.findViewById(R.id.btnSaveEditNote);

        mEditNote = (EditText) view.findViewById(R.id.etEditNote);
        mEditNote.setText(EditNoteDialog.note.text);
        etDueDate = (EditText) view.findViewById(R.id.etDueDate);
        etDueDate.setText(note.dueDate);
        if(note.priority > 0 && note.priority < 4)
        spPriority.setSelection(note.priority - 1);
        // Show soft keyboard automatically and request focus to field
        mEditNote.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ((Button) view.findViewById(R.id.btnSaveEditNote)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditNoteDialogListener listener = (EditNoteDialogListener) getActivity();
                note.text = mEditNote.getText().toString();
                note.dueDate = etDueDate.getText().toString();
                note.priority = spPriority.getSelectedItemPosition() + 1;
                listener.onFinishEditDialog(note, position);
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });
    }

}