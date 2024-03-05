package edu.psu.sweng888.activesync.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import edu.psu.sweng888.activesync.R;

public class DebuggingTextItemAdapter<TItem> extends ArrayAdapter<TItem> {

    public DebuggingTextItemAdapter(Context context, ArrayList<TItem> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inflate the debugging layout if none is provided
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                R.layout.debugging_text_list_item,
                parent,
                false
            );
        }

        // Retrieve the object at the current position and call its "toString" method to populate
        // the text field in the view.
        String displayText = "<null>";
        TItem item = getItem(position);
        if (item != null) {
            displayText = item.toString();
        }

        ((TextView) convertView.findViewById(R.id.dbg_text_list_item_text)).setText(displayText);

        return convertView;
    }
}
