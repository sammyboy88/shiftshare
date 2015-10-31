package com.example.calshare;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

public abstract class BaseShiftActivity extends FragmentActivity {

	private static final String VIEW_ID_KEYNAME = "viewId";
	
	public void showStartTimePickerDialog(View view) {
		showTimePickerDialog(view, R.id.startTimeEditText, new StartTimePickerFragment());
	}
	
	public void showEndTimePickerDialog(View view) {
		showTimePickerDialog(view, R.id.endTimeEditText, new TimePickerFragment());
	}
	
	protected void showTimePickerDialog(View view, int viewId, DialogFragment newFragment) {
		Bundle bundle = new Bundle();
		bundle.putInt(VIEW_ID_KEYNAME, viewId);
		newFragment.setArguments(bundle);
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	public static class TimePickerFragment extends DialogFragment 
		implements TimePickerDialog.OnTimeSetListener {
	
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			int viewId = getArguments().getInt(VIEW_ID_KEYNAME);
			EditText editText = (EditText)getActivity().findViewById(viewId);
			editText.setText(padInt(hourOfDay) + ":" + padInt(minute));
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int viewId = getArguments().getInt(VIEW_ID_KEYNAME);
			EditText editText = (EditText)getActivity().findViewById(viewId);
			String hourMinString = editText.getText().toString();
			
			int hour = 0;
			int minute = 0;
			if (!hourMinString.isEmpty()) {
				String[] hourMin = editText.getText().toString().split(":");
				hour = Integer.parseInt(hourMin[0]);
				minute = Integer.parseInt(hourMin[1]);
			}
			
			return new TimePickerDialog(getActivity(), this, hour, minute, true);
		}
	
		protected String padInt(int singleDigit) {
			if (singleDigit >= 0 && singleDigit < 10) {
				return "0" + singleDigit;
			}
			else {
				return Integer.toString(singleDigit);
			}
		}
	
	}

	public static class StartTimePickerFragment extends TimePickerFragment {

		/**
		 * If the end time is empty, default it to the start time.
		 * @param view
		 * @param hourOfDay
		 * @param minute
		 */
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			super.onTimeSet(view, hourOfDay, minute);
			if (getArguments().getInt(VIEW_ID_KEYNAME) == R.id.startTimeEditText) {
				EditText editText = (EditText)getActivity().findViewById(R.id.endTimeEditText);
				String hourMinString = editText.getText().toString();
				if (hourMinString.isEmpty()) {
					editText.setText(padInt(hourOfDay) + ":" + padInt(minute));
				}
			}
		}
	}
	
}
