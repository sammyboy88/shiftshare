package com.example.calshare;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.calshare.adapter.ShiftListAdapter;
import com.example.calshare.db.CalshareDatabaseHelper;
import com.example.calshare.model.Shift;
import com.j256.ormlite.dao.Dao;

import static com.example.calshare.R.layout.shift_list_row;

/*
 * Activity that displays a list of Shifts
 */
public class ShiftActivity extends Activity {

	private CalshareDatabaseHelper mDbHelper = new CalshareDatabaseHelper(this);
	private ListView mShiftListView;
	private ShiftListAdapter mListAdapter;
	private Integer lastSelectedPosition;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("ShiftActivity", "onCreate is Called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_activity);
        setTitle("Shift");

		mShiftListView = (ListView)findViewById(R.id.shiftListView);
		//mListAdapter = new ShiftListAdapter(this, shift_list_row, new ArrayList<Shift>());
        //mSelectedArray = new SparseBooleanArray();
        
        // Set up the CAB1
		mShiftListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		mShiftListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode arg0) {
				mListAdapter.clearSelection();

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.shift_cab, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.shift_delete:
						deleteSelectedItems();
						mode.finish(); // Action picked, so close the CAB

						// refresh the list of shifts
						mListAdapter.clearSelection();
						new ReadShiftsTask().execute(new Void[]{});
						return true;
					default:
						return false;
				}

			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode, int position,
												  long id, boolean checked) {
				if (checked) {
					mListAdapter.setNewSelection(position, checked);
				} else {
					mListAdapter.removeSelection(position);
				}

				//boolean currentVal = mSelectedArray.get(position);
				//mSelectedArray.put(position, !currentVal);
				System.out.println("onItemCheckedStatechanged: " + position);
			}
		});
        
        // click listener for the listView for when user short presses on a list item
        mShiftListView.setOnItemClickListener(new ShiftItemClickListener());
        
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("calling on Resume, last selected position is " + lastSelectedPosition);
		// scroll to the last selected item if there is one
		if (lastSelectedPosition != null) {
			mShiftListView.post(new Runnable() {
				@Override
				public void run() {
					System.out.println("scrolling now");
					mShiftListView.smoothScrollToPosition(lastSelectedPosition);
					mListAdapter.notifyDataSetChanged();
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		//lastSelectedPosition = mShiftListView.getFirstVisiblePosition();
	}

	/**
	 * Executes the DeleteShiftsTask
	 */
	protected void deleteSelectedItems() {
		List<CharSequence> titleList = new ArrayList<CharSequence>();
		for (int i = 0; i < mListAdapter.selectedCount(); i++) {
			int key = mListAdapter.keyAt(i);
			if (mListAdapter.isPositionChecked(key)) {
				TableLayout tableLayout = (TableLayout)mShiftListView.getAdapter().getView(key, null, mShiftListView);
				CharSequence title = getTitleFromShiftRow(tableLayout);
				titleList.add(title);
			}
		}
		String[] titleStrings = titleList.toArray(new String[]{});
		new DeleteShiftsTask().execute(titleStrings);
	}


	public void addShift(View view) {
		Intent intent = new Intent(this, AddShiftActivity.class);
		startActivity(intent);
	}

	/**
	 * Executes the ReadShiftsTask
	 */
	@Override
	protected void onStart() {
		super.onStart();
		new ReadShiftsTask().execute(new Void[]{});
	}

	/**
	 * A helper method to get the title from a TableLayout view object corresponding to a shift row.
	 * Used in shiftItemClickListener() and deleteSelectedItems()
	 *
	 * @param tableLayout
	 * @return
	 */
	static CharSequence getTitleFromShiftRow(TableLayout tableLayout) {
		TableRow row = (TableRow)tableLayout.getChildAt(0);
		TextView textView = (TextView)row.getChildAt(0);
		CharSequence title = textView.getText();
		return title;
	}

	/**
	 * When a shift in the list is clicked, start the EditShiftActivity to open the shift
	 */
    private class ShiftItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			lastSelectedPosition = position;
			TableLayout tableLayout = (TableLayout)view;
			CharSequence title = getTitleFromShiftRow(tableLayout);

			Intent intent = new Intent(ShiftActivity.this, EditShiftActivity.class);
			intent.putExtra("title", title);
			startActivity(intent);
		}
    }

	class DeleteShiftsTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {

			try {
				Dao<Shift, String> dao = mDbHelper.getShiftDao();
				dao.deleteIds(Arrays.asList(params));
			} catch (SQLException e) {
				e.printStackTrace();
			}


			return null;
		}

	}
	
	class ReadShiftsTask extends AsyncTask<Void, Void, List<com.example.calshare.model.Shift>> {
        
		
		@Override
		protected List<com.example.calshare.model.Shift> doInBackground(Void... params) {

			try {
				Dao<com.example.calshare.model.Shift, String> shiftDao = mDbHelper.getDao(com.example.calshare.model.Shift.class);
				return shiftDao.queryForAll();
			}
			catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<com.example.calshare.model.Shift>();
			}
		}

		@Override
		protected void onPostExecute(List<com.example.calshare.model.Shift> results) {

			if (mListAdapter == null) {
				mListAdapter = new ShiftListAdapter(ShiftActivity.this, shift_list_row, results);
				mShiftListView.setAdapter(mListAdapter);
			}
			else { // replace the contents of the adapter
				mListAdapter.refreshAdapter(results);

			}
			//customAdapter.setSelectedRows(mSelectedArray);

		}
		
	}
	
	
}
