package com.example.adeogo.scratch;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.adeogo.scratch.data.PetContract.PetEntry;

/**
 * Created by Adeogo on 1/23/2017.
 */

public class PetCursorAdapter extends CursorAdapter {
    public PetCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView nameView = (TextView) view.findViewById(R.id.name);
        TextView summaryView = (TextView) view.findViewById(R.id.summary);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
        String summary = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));

        // Populate fields with extracted properties
        nameView.setText(name);
        summaryView.setText(summary);

    }
}
