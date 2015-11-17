package com.example.calshare;

import java.sql.SQLException;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.calshare.AddShiftActivity.ShiftValidator;
import com.example.calshare.db.CalshareContract;
import com.example.calshare.db.CalshareDatabaseHelper;
import com.example.calshare.model.Shift;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class EditShiftActivity extends BaseShiftActivity {

	private CalshareDatabaseHelper databaseHelper = null;
	private CharSequence title;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
		Log.i(EditShiftActivity.class.getName(), "destroying edit shift activity");
	}

	private CalshareDatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this, CalshareDatabaseHelper.class);
		}
		return databaseHelper;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_form);
        setTitle("Edit Shift");
        updateControls();
        
        title = getIntent().getCharSequenceExtra("title");
        populateView(getShift(title));
	}
	
	private void updateControls() {
		
	}

	
	/**
	 * Handler for save button
	 * @param view
	 */
	public void saveShift(View view) {
		boolean hasErrors = new ShiftValidator(this).validate();
		if (!hasErrors) {
			EditText titleEditText = (EditText)findViewById(R.id.addShiftNameEditText);
			String newTitle = titleEditText.getText().toString();
			
			EditText startTimeEditText = (EditText)findViewById(R.id.startTimeEditText);
			String newStartTime = startTimeEditText.getText().toString();
		
			EditText endTimeEditText = (EditText)findViewById(R.id.endTimeEditText);
			String newEndTime = endTimeEditText.getText().toString();
			
			Shift shift = getShift(title);
			shift.setTitle(newTitle);
			shift.setStarttime(newStartTime);
			shift.setEndtime(newEndTime);

			// Save the changes
			try {
				getHelper().getShiftDao().update(shift);
				Log.i(EditShiftActivity.class.getName(), "saved shift");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finish();
		}
	}
	
	/**
	 * Populate view using the given domain object
	 * @param shift
	 */
	private void populateView(Shift shift) {
		if (shift != null) {
			EditText titleEditText = (EditText)findViewById(R.id.addShiftNameEditText);
			titleEditText.setText(shift.getTitle());

			EditText startTimeEditText = (EditText)findViewById(R.id.startTimeEditText);
			startTimeEditText.setText(shift.getStarttime());

			EditText endTimeEditText = (EditText)findViewById(R.id.endTimeEditText);
			endTimeEditText.setText(shift.getEndtime());
		}
	}

	/**
	 * Handler for cancel button
	 * @param view
	 */
	public void cancel(View view) {
		finish();
	}
	
	/**
	 * Get the Shift domain object from DB via its title
	 * @param title
	 * @return
	 */
	private Shift getShift(CharSequence title) {
		List<Shift> shifts = null;
        try {
			shifts = getHelper().getShiftDao().queryForEq(CalshareContract.Shift.COLUMN_NAME_SHIFT_TITLE, title);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        if (shifts != null && shifts.size() != 0) {
        	Log.i(EditShiftActivity.class.getName(), "found Shift object with title \'" + title + "\'");
        	return shifts.get(0);
        }
        else {
        	return null;
        }
	}
	
}
