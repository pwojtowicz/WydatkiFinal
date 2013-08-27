package pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.InvokeTransactionAdapter;
import pl.wppiotrek.wydatki.basepackage.adapters.SpinnerAdapter;
import pl.wppiotrek.wydatki.basepackage.adapters.SpinnerAdapter.SpinerHelper;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.InvokeTransactionParameter;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.entities.Project;
import pl.wppiotrek.wydatki.basepackage.entities.SpinnerObject;
import pl.wppiotrek.wydatki.basepackage.helpers.UnitConverter;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class NewInvokeTransactionFragment extends Fragment implements
		OnClickListener {

	public static final String BUNDLE_EDIT_TRANSACTION = "pl.wydatki.editTransaction";
	public static final String BUNDLE_IS_TRANSER = "pl.wydatki.isTransfer";

	private ListView list;
	private LinearLayout ll_accountTo;
	private LinearLayout ll_category;
	private LinearLayout ll_project;
	private ToggleButton isPositive;
	private TextView additionParametersTextView;
	private Spinner accountFrom;
	private Spinner accountTo;
	private Spinner categories;
	private Spinner projects;
	private EditText note;
	private EditText value;
	private Button btn_time;
	private Button btn_date;
	private InvokeTransactionAdapter adapter;
	private boolean isTransfer;
	private TransactionHelper helper;

	private SingletonLoadedWebContent globals = SingletonLoadedWebContent
			.getInstance();
	private BaseTransaction currentTransaction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getBundles();
	}

	protected void getBundles() {
		if (getArguments() != null) {
			currentTransaction = (BaseTransaction) getArguments()
					.getSerializable(BUNDLE_EDIT_TRANSACTION);
			helper = new TransactionHelper();
			if (currentTransaction == null) {
				isTransfer = getArguments()
						.getBoolean(BUNDLE_IS_TRANSER, false);

				helper.setDate(new Date());
			} else {
				updateHelperByTransaction();
			}
		}
	}

	protected void updateHelperByTransaction() {
		helper.transaction = currentTransaction;
		helper.setDate(currentTransaction.getDate());
		if (currentTransaction.getAccMinus() > 0
				&& currentTransaction.getAccPlus() > 0)
			isTransfer = true;
		else {
			helper.singleAccountId = currentTransaction.getAccMinus() > 0 ? currentTransaction
					.getAccMinus() : currentTransaction.getAccPlus();
		}
		helper.value = String.valueOf(currentTransaction.getValue());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_transaction_layout, null);
		linkViews(view);
		return view;
	}

	private void linkViews(View view) {

		list = (ListView) view.findViewById(R.id.invoke_transactions_listview);

		LayoutInflater layoutInflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View header = layoutInflater.inflate(
				R.layout.invoke_transaction_header, null);

		View footer = layoutInflater.inflate(
				R.layout.invoke_transaction_footer, null);

		ll_accountTo = (LinearLayout) header
				.findViewById(R.id.invoke_transaction_header_ll_account_to);

		ll_category = (LinearLayout) header
				.findViewById(R.id.invoke_transaction_header_ll_category);

		ll_project = (LinearLayout) footer
				.findViewById(R.id.invoke_transaction_footer_ll_project);

		isPositive = (ToggleButton) header
				.findViewById(R.id.invoke_transaction_header_tbn_ispositive);

		additionParametersTextView = (TextView) header
				.findViewById(R.id.invoke_transaction_header_addition_parameters);

		accountFrom = (Spinner) header
				.findViewById(R.id.invoke_transaction_header_spinner_account_from);
		accountTo = (Spinner) header
				.findViewById(R.id.invoke_transaction_header_spinner_account_to);
		categories = (Spinner) header
				.findViewById(R.id.invoke_transaction_header_spinner_category);

		projects = (Spinner) footer
				.findViewById(R.id.invoke_transaction_footer_spinner_project);

		note = (EditText) header
				.findViewById(R.id.invoke_transaction_header_etbx_note);
		value = (EditText) header
				.findViewById(R.id.invoke_transaction_header_etbx_value);

		btn_time = (Button) header
				.findViewById(R.id.invoke_transaction_header_btn_time);

		btn_date = (Button) header
				.findViewById(R.id.invoke_transaction_header_btn_date);

		btn_time.setOnClickListener(this);
		btn_date.setOnClickListener(this);

		list.addHeaderView(header);
		list.addFooterView(footer);

		list.setClickable(true);
		list.setItemsCanFocus(true);

		Button btn_calculate = (Button) header
				.findViewById(R.id.invoke_transaction_header_btn_calculator);
		btn_calculate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// showCalculatorInput();
			}
		});
	}

	@Override
	public void onClick(View view) {
		if (view == btn_time)
			new TimePickerDialog(getActivity(), mTimeSetListener, helper.hour,
					helper.minute, true).show();
		else
			new DatePickerDialog(getActivity(), mDateSetListener, helper.year,
					helper.month, helper.day).show();
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			helper.year = year;
			helper.month = monthOfYear;
			helper.day = dayOfMonth;
			setDate();

		}
	};

	private void setDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, helper.year);
		cal.set(Calendar.MONTH, helper.month);
		cal.set(Calendar.DAY_OF_MONTH, helper.day);

		btn_date.setText(UnitConverter.convertDateToString(cal.getTime()));
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			helper.hour = hourOfDay;
			helper.minute = minuteOfHour;
			setTime();
		}
	};
	private boolean hasAdditionalParameters;

	private void setTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, helper.hour);
		cal.set(Calendar.MINUTE, helper.minute);

		btn_time.setText(UnitConverter.convertTimeToString(cal.getTime()));
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		configureViews();
		reloadSpinners();
	}

	@Override
	public void onStart() {
		super.onStart();
		restoreHelper();
	}

	private void restoreHelper() {
		if (helper != null) {
			note.setText(helper.transaction.getNote());
			value.setText(helper.value);

			if (!isTransfer) {
				if (helper.transaction.getAccPlus() > 0)
					isPositive.setChecked(true);
			}

			setDate();
			setTime();

			if (accountFrom != null)
				accountFrom.setSelection(helper.accMinusPosition);
			if (accountTo != null)
				accountTo.setSelection(helper.accPlusPosition);
			if (categories != null)
				categories.setSelection(helper.categoryPosition);
			if (projects != null)
				projects.setSelection(helper.projektPosition);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		saveHelper();
	}

	private void saveHelper() {
		helper.transaction.setNote(note.getText().toString());
		helper.value = value.getText().toString();
		if (helper.value.length() > 0) {
			try {
				double value = Double.parseDouble(helper.value);
				helper.transaction.setValue(value);
			} catch (Exception e) {
			}
		}

		if (isTransfer) {
			if (accountFrom != null)
				helper.accMinusPosition = accountFrom.getSelectedItemPosition();
			if (accountTo != null)
				helper.accPlusPosition = accountTo.getSelectedItemPosition();
		} else {

		}
		if (categories != null)
			helper.categoryPosition = categories.getSelectedItemPosition();
		if (projects != null)
			helper.projektPosition = projects.getSelectedItemPosition();

	}

	private void configureViews() {
		if (adapter == null)
			adapter = new InvokeTransactionAdapter(getActivity(), null);

		list.setAdapter(adapter);

		if (isTransfer) {
			ll_accountTo.setVisibility(LinearLayout.VISIBLE);
			ll_category.setVisibility(LinearLayout.GONE);
			ll_project.setVisibility(LinearLayout.GONE);
			isPositive.setVisibility(ToggleButton.GONE);
			additionParametersTextView.setVisibility(TextView.GONE);
		} else {
			ll_accountTo.setVisibility(LinearLayout.GONE);
			ll_category.setVisibility(LinearLayout.VISIBLE);
			ll_project.setVisibility(LinearLayout.VISIBLE);
			isPositive.setVisibility(ToggleButton.VISIBLE);

			categories.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> adapterView,
						View view, int index, long id) {
					SpinerHelper oh = (SpinerHelper) view.getTag();
					onCategoryChange(oh.object);
				}

				public void onNothingSelected(AdapterView<?> adapterView) {
				}
			});
		}

	}

	protected void onCategoryChange(SpinnerObject object) {
		if (!isTransfer) {
			// ArrayList<Category> cat = globals.getCategoriesList();
			ArrayList<InvokeTransactionParameter> parameters = new ArrayList<InvokeTransactionParameter>();
			// if (cat != null) {

			addParameterForCategoryId(object.getId(), parameters);

			// }
			if (parameters.size() > 0) {
				additionParametersTextView.setVisibility(TextView.VISIBLE);
				hasAdditionalParameters = true;
			} else {
				additionParametersTextView.setVisibility(TextView.GONE);
				hasAdditionalParameters = false;
			}
			Category c = globals.getCategoryById(object.getId());
			if (c != null) {
				isPositive.setChecked(c.isPositive());
			}
			if (adapter == null)
				adapter = new InvokeTransactionAdapter(getActivity(),
						parameters);
			else
				adapter.setParamaters(parameters);

			list.setAdapter(adapter);

		}
	}

	private void addParameterForCategoryId(int categoryId,
			ArrayList<InvokeTransactionParameter> parameters) {
		Category item = globals.getCategoryById(categoryId);
		if (item != null) {
			if (item.getId() == categoryId) {

				if (item.getParentId() > 0)
					addParameterForCategoryId(item.getParentId(), parameters);

				if (item.getAttributes() != null)
					for (Parameter parameter : item.getAttributes()) {
						parameter = globals.getParameterById(parameter.getId());

						InvokeTransactionParameter param = new InvokeTransactionParameter(
								parameter.getId(), parameter.getName(),
								parameter.getTypeId(),
								parameter.getDefaultValue(),
								parameter.getDataSource());
						parameters.add(param);

					}
			}
		}
	}

	private void reloadSpinners() {
		if (isTransfer) {
			reloadAccouns(accountFrom, helper.transaction.getAccMinus());
			reloadAccouns(accountTo, helper.transaction.getAccPlus());
		} else {
			reloadAccouns(accountFrom, helper.singleAccountId);
			reloadCategories();
			reloadProjects();
		}
	}

	private void reloadAccouns(Spinner spinner, int accountId) {
		if (spinner != null) {
			ArrayList<Account> accounts = globals.getAccounts();

			if (accounts != null) {
				ArrayList<SpinnerObject> items = new ArrayList<SpinnerObject>();
				for (Account item : accounts) {
					if (item.isActive())
						items.add(new SpinnerObject(item.getId(), item
								.getName()
								+ " "
								+ UnitConverter.doubleToCurrency(item
										.getBalance())));
				}

				int position = searchActualSelectedSpinnerPosition(items,
						accountId);

				SpinnerObject[] strArray = new SpinnerObject[items.size()];
				items.toArray(strArray);

				SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),
						android.R.layout.simple_spinner_item, strArray);
				spinner.setAdapter(adapter);
				spinner.setSelection(position, true);

			}
		}
	}

	private void reloadCategories() {
		ArrayList<Category> loadedItems = globals.getCategories();
		if (loadedItems != null) {
			sortCategories(loadedItems);
			ArrayList<SpinnerObject> items = new ArrayList<SpinnerObject>();
			items.add(new SpinnerObject(-1, getText(R.string.no_selected_value)
					.toString()));

			int blockCategoryId = -1;
			for (Category item : loadedItems) {
				if (!item.isActive()) {
					blockCategoryId = item.getId();
					continue;
				}
				if (item.isActive() && item.getParentId() != blockCategoryId) {
					if (item.getParentId() > 0) {
						items.add(new SpinnerObject(item.getId(), item.getLvl()
								+ item.getName()));
					} else {
						items.add(new SpinnerObject(item.getId(), item
								.getName()));
					}
				}
				if (item.getParentId() == blockCategoryId)
					blockCategoryId = item.getId();

			}

			int position = searchActualSelectedSpinnerPosition(items,
					helper.transaction.getCategoryId());

			SpinnerObject[] strArray = new SpinnerObject[items.size()];
			items.toArray(strArray);

			SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),
					android.R.layout.simple_spinner_item, strArray);
			categories.setAdapter(adapter);
			categories.setSelection(position, true);
		}
	}

	private int searchActualSelectedSpinnerPosition(
			ArrayList<SpinnerObject> items, int searchValue) {
		if (searchValue > 0) {
			int i = 0;
			for (SpinnerObject item : items) {
				if (item.getId() == searchValue)
					return i;
				i++;
			}
		}
		return 0;
	}

	private void sortCategories(ArrayList<Category> categories) {
		Collections.sort(categories, new Comparator<Category>() {
			@Override
			public int compare(Category lhs, Category rhs) {
				return lhs.getRn().compareToIgnoreCase(rhs.getRn());
			}
		});
	}

	private void reloadProjects() {
		ArrayList<Project> loadedItems = globals.getProjects();
		ArrayList<SpinnerObject> items = new ArrayList<SpinnerObject>();

		items.add(new SpinnerObject(-1, getText(R.string.no_selected_value)
				.toString()));

		if (projects != null)
			for (Project item : loadedItems) {
				if (item.isActive())
					items.add(new SpinnerObject(item.getId(), item.getName()));
			}

		int position = searchActualSelectedSpinnerPosition(items,
				helper.transaction.getProjectId());

		SpinnerObject[] strArray = new SpinnerObject[items.size()];
		items.toArray(strArray);

		SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),
				android.R.layout.simple_spinner_item, strArray);

		projects.setAdapter(adapter);
		projects.setSelection(position, true);
	}

	public ValidationHelper getCurrentTransaction() {
		saveHelper();
		prepareToSaved();

		ValidationHelper result = new ValidationHelper();
		result.isValid = true;
		result.item = helper.transaction;

		return result;
	}

	private void prepareToSaved() {
		if (categories != null) {
			SpinnerObject so = (SpinnerObject) categories.getSelectedItem();
			if (so != null) {
				helper.transaction.setCategoryId(so.getId());
			}
		}
		if (isTransfer) {
			if (accountFrom != null) {
				SpinnerObject so = (SpinnerObject) accountFrom
						.getSelectedItem();
				if (so != null) {
					helper.transaction.setAccMinus(so.getId());
				}
			}
			if (accountTo != null) {
				SpinnerObject so = (SpinnerObject) accountTo.getSelectedItem();
				if (so != null) {
					helper.transaction.setAccPlus(so.getId());
				}
			}
		} else {
			SpinnerObject so = (SpinnerObject) accountFrom.getSelectedItem();
			if (so != null) {
				if (isPositive.isChecked())
					helper.transaction.setAccPlus(so.getId());
				else
					helper.transaction.setAccMinus(so.getId());
				helper.singleAccountId = so.getId();
			}
		}
		if (projects != null) {
			SpinnerObject so = (SpinnerObject) projects.getSelectedItem();
			if (so != null) {
				helper.transaction.setProjectId(so.getId());
			}
		}
		helper.transaction.setDate(helper.getDate());

	}
}
