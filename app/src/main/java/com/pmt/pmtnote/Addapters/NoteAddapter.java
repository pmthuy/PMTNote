package com.pmt.pmtnote.Addapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pmt.pmtnote.R;
import com.pmt.pmtnote.models.Note;

import java.util.ArrayList;

/**
 * Created by thuypm on 2/23/2016.
 */
public class NoteAddapter extends ArrayAdapter<Note> {
    // View lookup cache
    private static class ViewHolder {
        TextView tvNoteItem;
        TextView tvNoteDuedate;
        TextView tvNotePriority;
    }

    public NoteAddapter(Context context, ArrayList<Note> notes) {
        super(context, R.layout.simple_list_item, notes);
    }

    private ViewHolder setUI(ViewHolder viewHolder, String text, String dueDate, int priorityId){
        viewHolder.tvNoteItem.setText(text);
        viewHolder.tvNoteDuedate.setText(dueDate);
        switch (priorityId){
            case 1:
                viewHolder.tvNotePriority.setTextColor(Color.RED);
                viewHolder.tvNotePriority.setText("Hight");
                break;
            case 2:
                viewHolder.tvNotePriority.setTextColor(Color.BLUE);
                viewHolder.tvNotePriority.setText("Nomal");
                break;
            case 3:
                viewHolder.tvNotePriority.setTextColor(Color.GREEN);
                viewHolder.tvNotePriority.setText("Low");
                break;
        }
        return viewHolder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Note note = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.simple_list_item, parent, false);
            viewHolder.tvNoteItem = (TextView) convertView.findViewById(R.id.tvNoteItem);
            viewHolder.tvNoteDuedate = (TextView) convertView.findViewById(R.id.tvNoteDueDate);
            viewHolder.tvNotePriority = (TextView) convertView.findViewById(R.id.tvNotePriority);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder = setUI(viewHolder, note.text, note.dueDate, note.priority);
        // Return the completed view to render on screen
        return convertView;
    }

}
