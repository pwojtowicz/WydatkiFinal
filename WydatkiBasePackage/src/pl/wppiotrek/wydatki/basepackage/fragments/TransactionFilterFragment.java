package pl.wppiotrek.wydatki.basepackage.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.SpinnerAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.SpinnerObject;
import pl.wppiotrek.wydatki.basepackage.entities.TransactionFilter;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class TransactionFilterFragment extends Fragment {

	public static final String BUNDLE_TRANSACTION_FILTER = "pl.wydatki.transactionfilter";
	private EditText etbx_note;
	private EditText etbx_value_from;
	private EditText etbx_value_to;
	private EditText etbx_date_from;
	private EditText etbx_date_to;
	private Spinner spinner_category;
	private TransactionFilter currentFilter;

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
		etbx_date_from = (EditText) convertView
				.findViewById(R.id.etbx_filter_date_from);
		etbx_date_to = (EditText) convertView
				.findViewById(R.id.etbx_filter_date_to);
		spinner_category = (Spinner) convertView
				.findViewById(R.id.spinner_filter_category);
		Button btn_clear = (Button) convertView
				.findViewById(R.id.btn_filter_clear);
		Button btn_save = (Button) convertView
				.findViewById(R.id.btn_filter_save);
		btn_save.setOnClickListener(btnSaveClickListener);
		btn_clear.setOnClickListener(btnClearClickListener);
	}

	OnClickListener btnSaveClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			prepareFilterToSave();
			Intent data = new Intent();
			data.putExtra(BUNDLE_TRANSACTION_FILTER, currentFilter);
			getActivity().setResult(Activity.RESULT_OK, data);
			getActivity().finish();
		}
	};

	OnClickListener btnClearClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			configureViews(new TransactionFilter(currentFilter.getStartAt(),
					currentFilter.getTakeCount()));
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
		spinner_category.setSelection(0);
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
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(BUNDLE_TRANSACTION_FILTER, currentFilter);
		super.onSaveInstanceState(outState);
	}
}
