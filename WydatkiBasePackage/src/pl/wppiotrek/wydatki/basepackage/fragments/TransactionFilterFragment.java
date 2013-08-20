package pl.wppiotrek.wydatki.basepackage.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.SpinnerAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.SpinnerObject;
import pl.wppiotrek.wydatki.basepackage.entities.TransactionFilter;
import pl.wppiotrek.wydatki.basepackage.helpers.UnitConverter;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class TransactionFilterFragment extends Fragment {

	public static final String BUNDLE_TRANSACTION_FILTER = "pl.wydatki.transactionfilter";
	private EditText etbx_note;
	private EditText etbx_value_from;
	private EditText etbx_value_to;
	private Spinner spinner_category;
	private TransactionFilter currentFilter;
	private Button btn_date_from_date;
	// private Button btn_date_from_time;
	private Button btn_date_to_date;
	// private Button btn_date_to_time;

	private int Year;
	private int month;
	private int day;
	private int hour;
	private int minute;

	private boolean setFromDateOrTime = false;
	private boolean setToDateOrTime = false;

	OnClickListener btnClickDateOrTimeListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			setFromDateOrTime = false;
			setToDateOrTime = false;
			if (view == btn_date_from_date)// || view == btn_date_from_time)
				setFromDateOrTime = true;
			if (view == btn_date_to_date)// || view == btn_date_to_time)
				setToDateOrTime = true;
			getActualButtonDate();

			// if (view == btn_date_from_time || view == btn_date_to_time)
			// new TimePickerDialog(getActivity(), mTimeSetListener, hour,
			// minute, true).show();
			// else
			if (view == btn_date_from_date || view == btn_date_to_date)
				new DatePickerDialog(getActivity(), mDateSetListener, Year,
						month, day).show();
		}

		private void getActualButtonDate() {
			Calendar cal = Calendar.getInstance();
			Date date = null;
			if (setFromDateOrTime) {
				date = currentFilter.getDateFrom();
			}
			if (setToDateOrTime) {
				date = currentFilter.getDateTo();
			}
			if (date != null)
				cal.setTime(date);
			else {
				cal.setTime(new Date());
				if (setFromDateOrTime) {
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
				}
				if (setToDateOrTime) {
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 59);
				}
			}
			Year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH);
			day = cal.get(Calendar.DAY_OF_MONTH);
			hour = cal.get(Calendar.HOUR_OF_DAY);
			minute = cal.get(Calendar.MINUTE);
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Year = year;
			month = monthOfYear;
			day = dayOfMonth;
			setDate();
		}
	};
	private Button btn_clear_date_from;
	private Button btn_clear_date_to;

	protected void setDate() {
		Calendar cal = Calendar.getInstance();
		// if (setFromDateOrTime)
		// cal.setTime(currentFilter.getDateFrom() != null ? currentFilter
		// .getDateFrom() : new Date());
		// if (setToDateOrTime)
		// cal.setTime(currentFilter.getDateTo() != null ? currentFilter
		// .getDateTo() : new Date());

		cal.set(Calendar.YEAR, Year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		if (setToDateOrTime) {
			btn_date_to_date.setText(UnitConverter.convertDateToString(cal
					.getTime()));
			currentFilter.setDateTo(cal.getTime());
		}
		if (setFromDateOrTime) {
			btn_date_from_date.setText(UnitConverter.convertDateToString(cal
					.getTime()));
			currentFilter.setDateFrom(cal.getTime());
		}

	}

	// private TimePickerDialog.OnTimeSetListener mTimeSetListener = new
	// TimePickerDialog.OnTimeSetListener() {
	// public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
	// hour = hourOfDay;
	// minute = minuteOfHour;
	// setTime();
	// }
	// };

	// protected void setTime() {
	// Calendar cal = Calendar.getInstance();
	// if (setFromDateOrTime)
	// cal.setTime(currentFilter.getDateFrom() != null ? currentFilter
	// .getDateFrom() : new Date());
	// if (setToDateOrTime)
	// cal.setTime(currentFilter.getDateTo() != null ? currentFilter
	// .getDateTo() : new Date());
	//
	// cal.set(Calendar.HOUR_OF_DAY, hour);
	// cal.set(Calendar.MINUTE, minute);
	// if (setToDateOrTime) {
	// btn_date_to_time.setText(UnitConverter.convertTimeToString(cal
	// .getTime()));
	// currentFilter.setDateTo(cal.getTime());
	// }
	// if (setFromDateOrTime) {
	// btn_date_from_time.setText(UnitConverter.convertTimeToString(cal
	// .getTime()));
	// currentFilter.setDateFrom(cal.getTime());
	// }
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(
				R.layout.fragment_filter_transactions, null);

		linkViews(convertView);
		return convertView;
	}

	private void linkViews(View convertView) {
		etbx_note = (EditText) convertView.findViewById(R.id.etbx_filter_note);
		etbx_value_from = (EditText) convertView
				.findViewById(R.id.etbx_filter_value_from);
		etbx_value_to = (EditText) convertView
				.findViewById(R.id.etbx_filter_value_to);
		btn_date_from_date = (Button) convertView
				.findViewById(R.id.btn_filter_date_from_date);
		btn_clear_date_from = (Button) convertView
				.findViewById(R.id.btn_filter_clear_date_from);
		btn_date_to_date = (Button) convertView
				.findViewById(R.id.btn_filter_date_to_date);
		btn_clear_date_to = (Button) convertView
				.findViewById(R.id.btn_filter_clear_date_to);

		btn_date_from_date.setOnClickListener(btnClickDateOrTimeListener);

		btn_clear_date_from.setOnClickListener(btnClickClearListener);

		btn_date_to_date.setOnClickListener(btnClickDateOrTimeListener);
		btn_clear_date_to.setOnClickListener(btnClickClearListener);

		spinner_category = (Spinner) convertView
				.findViewById(R.id.spinner_filter_category);
		OnItemSelectedListener categorySelectedListener = new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> adapter, View v,
					int position, long id) {
				Object o = adapter.getItemAtPosition(position);
				if (o != null && o instanceof SpinnerObject) {
					currentFilter.setCategoryId(((SpinnerObject) o).getId());
					currentFilter.setCategorySpinnerPosition(position);
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		};
		spinner_category.setOnItemSelectedListener(categorySelectedListener);

		Button btn_clear = (Button) convertView
				.findViewById(R.id.btn_filter_clear);
		Button btn_save = (Button) convertView
				.findViewById(R.id.btn_filter_save);
		btn_save.setOnClickListener(btnSaveClickListener);
		btn_clear.setOnClickListener(btnClearClickListener);
	}

	OnClickListener btnClickClearListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (view == btn_clear_date_from) {
				currentFilter.setDateFrom(null);
				btn_date_from_date.setText("data");
			} else if (view == btn_clear_date_to) {
				currentFilter.setDateTo(null);
				btn_date_to_date.setText("data");
			}

		}
	};

	OnClickListener btnSaveClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			prepareFilterToSave();
			currentFilter.setStartAt(0);
			Intent data = new Intent();
			data.putExtra(BUNDLE_TRANSACTION_FILTER, currentFilter);
			getActivity().setResult(Activity.RESULT_OK, data);
			getActivity().finish();
		}
	};

	OnClickListener btnClearClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TransactionFilter newFilter = new TransactionFilter(
					currentFilter.getStartAt(), currentFilter.getTakeCount());
			newFilter.setAccountId(currentFilter.getAccountId());
			configureViews(newFilter);
		}
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			configureViews((TransactionFilter) savedInstanceState
					.getSerializable(BUNDLE_TRANSACTION_FILTER));
		} else if (getArguments() != null) {
			configureViews((TransactionFilter) getArguments().getSerializable(
					BUNDLE_TRANSACTION_FILTER));
		} else
			configureViews(new TransactionFilter());
	}

	protected void prepareFilterToSave() {
		String note = etbx_note.getText().toString();

		String valueFrom = etbx_value_from.getText().toString();
		String valueTo = etbx_value_to.getText().toString();

		this.currentFilter.setNote(note);

		try {
			if (valueFrom.length() > 0)
				this.currentFilter.setValueFrom(Double.parseDouble(valueFrom));
			else
				this.currentFilter.setValueFrom(null);

			if (valueTo.length() > 0)
				this.currentFilter.setValueTo(Double.parseDouble(valueTo));
			else
				this.currentFilter.setValueTo(null);
		} catch (Exception ex) {

		}
	}

	private void configureViews(TransactionFilter filter) {
		this.currentFilter = filter;
		reloadCategoriesIfNeeded();
		if (currentFilter.getNote() != null)
			etbx_note.setText(currentFilter.getNote());
		else
			etbx_note.setText("");
		if (currentFilter.getValueFrom() != null)
			etbx_value_from.setText(currentFilter.getValueFrom().toString());
		else
			etbx_value_from.setText("");
		if (currentFilter.getValueTo() != null)
			etbx_value_to.setText(currentFilter.getValueTo().toString());
		else
			etbx_value_to.setText("");

		if (currentFilter.getDateFrom() != null) {
			btn_date_from_date.setText(UnitConverter
					.convertDateToString(currentFilter.getDateFrom()));
		}

		if (currentFilter.getDateTo() != null) {
			btn_date_to_date.setText(UnitConverter
					.convertDateToString(currentFilter.getDateTo()));
		}

	}

	private void reloadCategoriesIfNeeded() {

		ArrayList<Category> categories = SingletonLoadedWebContent
				.getInstance().getCategories();
		sortCategories(categories);
		if (categories != null) {
			ArrayList<SpinnerObject> items = new ArrayList<SpinnerObject>();
			items.add(new SpinnerObject(-1, getText(R.string.no_selected_value)
					.toString()));

			for (Category cat : categories) {
				items.add(new SpinnerObject(cat.getId(), cat.getName()));
			}

			SpinnerObject[] strArray = new SpinnerObject[items.size()];
			items.toArray(strArray);

			SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),
					android.R.layout.simple_spinner_item, strArray);

			spinner_category.setAdapter(adapter);

			if (currentFilter.getCategoryId() != null
					&& currentFilter.getCategoryId() > 0
					&& currentFilter.getCategorySpinnerPosition() > 0) {
				spinner_category.setSelection(currentFilter
						.getCategorySpinnerPosition());
			} else
				spinner_category.setSelection(0);
		}
	}

	private void sortCategories(ArrayList<Category> categories) {
		Collections.sort(categories, new Comparator<Category>() {
			@Override
			public int compare(Category lhs, Category rhs) {
				return lhs.getRn().compareToIgnoreCase(rhs.getRn());
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(BUNDLE_TRANSACTION_FILTER, currentFilter);
		super.onSaveInstanceState(outState);
	}
}
