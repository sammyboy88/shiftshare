package com.example.calshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

public class YearFragment extends Fragment {

	static final String[] numbers = new String[] { 
		"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.year_layout, container, false);

        GridView gridview = (GridView) v.findViewById(R.id.yearGridView);
        int height = gridview.getHeight();
        System.out.println("height: " +height);
        //ArrayAdapter<String> array = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, numbers);
        ListAdapter array = new YearGridAdapter(getActivity());
	    gridview.setAdapter(array);
//	    
//    	GridView gridView = new GridView(getActivity());
//    	ArrayAdapter<String> array = new ArrayAdapter<String>(getActivity(), R.layout.simple_list_item_1, numbers);
//    	gridView.setAdapter(array);
        return v;
    }
	
}
