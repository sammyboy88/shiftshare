package com.example.calshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import org.joda.time.YearMonth;

public class YearFragment extends Fragment {

	static final String[] numbers = new String[] { 
		"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    private GridView yearGridView;
    private YearGridAdapter yearAdapter;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.year_layout, container, false);

        yearGridView = (GridView) v.findViewById(R.id.yearGridView);
        int height = yearGridView.getHeight();
        System.out.println("height: " +height);

        yearAdapter = new YearGridAdapter(getActivity());
        yearGridView.setAdapter(yearAdapter);

        yearGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // set the selected YearMonth on the MonthGridAdapter
                // invalidate the month grid view
                // then switch to the month view
                Log.i("YearFragment", "selected month " + i+1);
                YearMonth selectedYearMonth = new YearMonth(yearAdapter.getYear(), i+1);
                CalendarActivity calendarActivity = (CalendarActivity)getActivity();
                calendarActivity.switchToMonth(selectedYearMonth);
            }
        });

        return v;
    }
	
}
