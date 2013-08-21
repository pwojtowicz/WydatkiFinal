package pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.InvokeTransactionAdapter;
import pl.wppiotrek.wydatki.basepackage.adapters.SpinnerAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.Project;
import pl.wppiotrek.wydatki.basepackage.entities.SpinnerObject;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class NewInvokeTransactionFragment extends Fragment {

	private static final String BUNDLE_EDIT_TRANSACTION = "pl.wydatki.editTransaction";

	private static final String BUNDLE_IS_TRANSER = "pl.wydatki.isTransfer";

	BaseTransaction currentTransaction = null;

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

	private SingletonLoadedWebContent globals = SingletonLoadedWebContent
			.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getBundles();
	}

	protected void getBundles() {
		if (getArguments() != null) {
			currentTransaction = (BaseTransaction) getArguments()
					.getSerializable(BUNDLE_EDIT_TRANSACTION);

			if (currentTransaction == null)
				isTransfer = getArguments()
						.getBoolean(BUNDLE_IS_TRANSER, false);
			else {
				if (currentTransaction.getAccMinus() > 0
						&& currentTransaction.getAccPlus() > 0)
					isTransfer = true;
			}
		}
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

		// btn_time.setOnClickListener(this);
		// btn_date.setOnClickListener(this);

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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		configureViews();
		reloadSpinners();
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
		}

	}

	private void reloadSpinners() {
		reloadAccouns();
		reloadCategories();
		reloadProjects();

	}

	private void reloadAccouns() {
		// TODO Auto-generated method stub

	}

	private void reloadCategories() {
		// TODO Auto-generated method stub

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

		SpinnerObject[] strArray = new SpinnerObject[items.size()];
		items.toArray(strArray);

		SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),
				android.R.layout.simple_spinner_item, strArray);

		projects.setAdapter(adapter);
	}
}
