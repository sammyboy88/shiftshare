package com.example.calshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.joda.time.YearMonth;

public class TestYearFragment extends Fragment {

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



        return v;
    }
	
}
