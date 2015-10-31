package com.example.calshare;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by root on 10/08/15.
 */
public class TestListActivity extends Activity {

    ListView mListView;
    ArrayAdapter<String> mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("TestListActivity", "onCreate is Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_list_activity);
        setTitle("TestList");

        mListView = (ListView)findViewById(R.id.testListView);
        mListAdapter = new ArrayAdapter<String>(this, R.layout.test_list_row);
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            items.add(String.valueOf(i));
        }
        mListAdapter.addAll(items);
        mListView.setAdapter(mListAdapter);
        //mSelectedArray = new SparseBooleanArray();

    }

    public void addShift(View view) {
        mListView.setSelection(50);
    }

    public void smoothScroll(View view) {
        mListView.smoothScrollToPosition(75);
    }

    public void openActivity(View view) {
        Intent intent = new Intent(this, AddShiftActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mListView.smoothScrollToPosition(25);
    }
}
