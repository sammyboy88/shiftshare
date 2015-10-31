package com.example.calshare;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.example.calshare.db.CalshareDatabaseHelper;

import java.sql.SQLException;

public class AddShiftActivity extends BaseShiftActivity {
	
	private CalshareDatabaseHelper mDbHelper = new CalshareDatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shift_form);
        setTitle("Add Shift");

	}
	
	public void saveShift(View view) {
		boolean hasErrors = new ShiftValidator(this).validate();
		if (!hasErrors) {
			EditText titleEditText = (EditText)findViewById(R.id.addShiftNameEditText);
			String title = titleEditText.getText().toString();
			
			EditText startTimeEditText = (EditText)findViewById(R.id.startTimeEditText);
			String startTime = startTimeEditText.getText().toString();
		
			EditText endTimeEditText = (EditText)findViewById(R.id.endTimeEditText);
			String endTime = endTimeEditText.getText().toString();

			com.example.calshare.model.Shift newShift = new com.example.calshare.model.Shift();
			newShift.setTitle(title);
			newShift.setStarttime(startTime);
			newShift.setEndtime(endTime);

			// Insert the new row, returning the primary key value of the new row
			new SaveShiftTask().execute(newShift);
		}
	}
	
	/**
	 * click handler method for cancel button
	 * @param view
	 */
	public void cancel(View view) {
		finish();
	}
	
	static class ShiftValidator {
		
		private Activity activity;
		
		public ShiftValidator(Activity activity) {
			this.activity = activity;
		}
		
		boolean validate() {
			boolean hasErrors = false;
			EditText titleEditText = (EditText)activity.findViewById(R.id.addShiftNameEditText);
			String title = titleEditText.getText().toString();
			if (title.trim().equals("")) {
				titleEditText.setError("Please enter a value");
				hasErrors = true;
			}
			
			return hasErrors;
		}
	}
	
	/**
	 * Asynchronously save a Shift
	 * @author silkyoak
	 *
	 */
	class SaveShiftTask extends AsyncTask<com.example.calshare.model.Shift, Void, Void> {

		@Override
		protected Void doInBackground(com.example.calshare.model.Shift... params) {
			long newRowId = 0;

			try {
				newRowId = mDbHelper.getShiftDao().create(params[0]);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}

			System.out.println("new row id: " + newRowId);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			AddShiftActivity.this.finish();
		}
		
	}

}
