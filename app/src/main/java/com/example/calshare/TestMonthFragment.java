package com.example.calshare;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.calshare.db.CalshareDatabaseHelper;
import com.example.calshare.db.DateShiftDatabaseManager;
import com.example.calshare.model.Shift;
import com.example.calshare.view.DateLinearLayout;
import com.example.calshare.view.DateTextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import java.util.Map;

public class TestMonthFragment extends Fragment {

    private MonthGridAdapter monthGridAdapter;
    private GridView monthGridView;
    private TextView shiftDetailTextView;

//    private LocalDate previouslyLongClickedDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i("MonthFragment", "onCreateView");
        View v = inflater.inflate(R.layout.month_layout, container, false);
        monthGridView = (GridView) v.findViewById(R.id.monthGridView);

        return v;

    }


}
