package com.example.calshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.calshare.R;
import com.example.calshare.model.Shift;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.example.calshare.R.layout.shift_list_row;

/**
 * Displays a list of Shifts
 */
public class ShiftListAdapter extends ArrayAdapter<Shift> {


    private SparseBooleanArray selectedRows = new SparseBooleanArray();

    public ShiftListAdapter(Context context, int resource, List<Shift> objects) {
        super(context, resource, objects);
    }

    public ShiftListAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public void setNewSelection(int position, boolean value) {
        selectedRows.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        return selectedRows.get(position);
    }

    public void removeSelection(int position) {
        selectedRows.delete(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedRows.clear();
        notifyDataSetChanged();
    }

    public int selectedCount() {
        return selectedRows.size();
    }

    public int keyAt(int index) {
        return selectedRows.keyAt(index);
    }

    public synchronized void refreshAdapter(List<Shift> newShiftList) {
        clear();
        addAll(newShiftList);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(shift_list_row, null);
        }

        Shift p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) view.findViewById(R.id.id);
            TextView tt2 = (TextView) view.findViewById(R.id.startTime);
            TextView tt3 = (TextView) view.findViewById(R.id.endTime);

            if (tt1 != null) {
                tt1.setText(p.getTitle());
            }

            if (tt2 != null) {
                tt2.setText(p.getStarttime());
            }

            if (tt3 != null) {
                tt3.setText(p.getEndtime());
            }
        }

        view.setBackgroundColor(Color.TRANSPARENT); //default color
        if (selectedRows.get(position)) {
            // multiple selection color
            view.setBackgroundColor(Color.parseColor("#ff18b5b4"));
        }
        return view;

    }
}
