package pl.wppiotrek.wydatki.basepackage.activities;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.TransactionDetailsAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.DoubleStringObject;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.entities.Transaction;
import pl.wppiotrek.wydatki.basepackage.enums.OperationType;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.helpers.ParameterTypes;
import pl.wppiotrek.wydatki.basepackage.helpers.UnitConverter;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyncTaskDownloadContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.TaskParameters;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;

public class TransactionDetailsActivity extends ExpensesBaseActivity implements
		OnClickListener {

	public static final String BUNDLE_TRANSACTION = "pl.wydatki.transactiondetails";
	private ListView listView;
	private Button btn_OK;
	private Button btn_CANCEL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.transaction_details_input);

		linkViews();
		configureViews((BaseTransaction) getIntent().getExtras()
				.getSerializable(BUNDLE_TRANSACTION));
	}

	private void linkViews() {
		listView = (ListView) findViewById(R.id.transaction_details_listview);
		btn_OK = (Button) findViewById(R.id.bOK);
		btn_CANCEL = (Button) findViewById(R.id.bCancel);

		btn_OK.setOnClickListener(this);
		btn_CANCEL.setOnClickListener(this);

	}

	public void onClick(View view) {
		finish();

	}

	private void configureViews(BaseTransaction loadTransaction) {
		SingletonLoadedWebContent globals = SingletonLoadedWebContent
				.getInstance();
		Transaction transaction = new Transaction(loadTransaction);
		transaction.setCategory(globals.getCategoryById(loadTransaction
				.getCategoryId()));

		if (transaction != null) {

			ArrayList<DoubleStringObject> items = new ArrayList<DoubleStringObject>();
			items.add(new DoubleStringObject("Data", UnitConverter
					.dateTimeString(transaction.getDate()),
					ParameterTypes.ParameterText));
			if (transaction.getAccMinus() > 0) {
				items.add(new DoubleStringObject("Z rachunku", globals
						.getAccountById(transaction.getAccMinus()).getName(),
						ParameterTypes.ParameterText));
				items.add(new DoubleStringObject("Kwota wydana", transaction
						.getValue() * -1, ParameterTypes.ParameterCurrency));
			}
			if (transaction.getAccPlus() > 0) {
				items.add(new DoubleStringObject("Na rachunek", globals
						.getAccountById(transaction.getAccPlus()).getName(),
						ParameterTypes.ParameterText));
				items.add(new DoubleStringObject("Kwota wp¸ywu", transaction
						.getValue(), ParameterTypes.ParameterCurrency));
			}
			if (transaction.getCategory() != null) {
				items.add(new DoubleStringObject("Kategoria", transaction
						.getCategory().getName(), ParameterTypes.ParameterText));
			}

			if (transaction.getNote() != null
					&& transaction.getNote().length() > 0)
				items.add(new DoubleStringObject("Notatka", transaction
						.getNote(), ParameterTypes.ParameterText));

			getCategoryParameters(globals, transaction,
					loadTransaction.getParameters(), items);

			if (transaction.getProjectId() > 0) {
				items.add(new DoubleStringObject("Projekt", globals
						.getProjectById(transaction.getProjectId()).getName(),
						ParameterTypes.ParameterText));
			}

			TransactionDetailsAdapter adapter = new TransactionDetailsAdapter(
					this, items);
			listView.setAdapter(adapter);
		}

	}

	protected void getCategoryParameters(SingletonLoadedWebContent globals,
			Transaction transaction, Parameter[] loadedParameters,
			ArrayList<DoubleStringObject> items) {
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		if (transaction.getCategory() != null) {

			if (transaction.getCategory().getAttributes() != null) {
				for (Parameter parameter : transaction.getCategory()
						.getAttributes()) {
					parameters.add(parameter);
				}
			}
			int parentId = transaction.getCategory().getParentId();
			if (parentId > 0) {
				Category category = globals.getCategoryById(parentId);
				if (category != null && category.getAttributes() != null) {
					for (Parameter parameter : category.getAttributes()) {
						parameters.add(parameter);
					}
				}
			}
		}

		for (Parameter parameter : parameters) {
			Parameter param = globals.getParameterById(parameter.getId());

			for (Parameter tmp : loadedParameters) {
				if (tmp.getId() == parameter.getId())
					param.setValue(tmp.getValue());
			}

			items.add(new DoubleStringObject(param.getName(), param.getValue(),
					param.getTypeId()));
		}
	}

	public void onClickRemoveTransaction(View v) {
		BaseTransaction item = (BaseTransaction) getIntent().getExtras()
				.getSerializable(BUNDLE_TRANSACTION);
		if (item != null) {
			task = new AsyncTaskDownloadContent(this);
			TaskParameters params = new TaskParameters(
					ProviderType.Transactions, OperationType.Delete, item);
			task.execute(params);
		}
	}

	@Override
	public void onDownloadResult(Object response) {
		OperationResult or = (OperationResult) response;
		dialogonResult(or);
		if (or.isSuccess) {
			updateDeletedTransactions();
			updateAccountsBalance();
		}
	}

	protected void dialogonResult(OperationResult result) {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		Builder builder = new AlertDialog.Builder(this);

		if (result.isSuccess) {
			builder.setTitle("Powodzenie");
			builder.setMessage("Skasowano transakcjê");
			DialogInterface.OnClickListener onPositiveClickAfterSuccess = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			};
			builder.setPositiveButton("OK", onPositiveClickAfterSuccess);

		} else {
			builder.setTitle("B³¹d");
			builder.setMessage("Usuwanie zakoñczone niepowodzeniem");
			builder.setNegativeButton("Anuluj", null);
			builder.setPositiveButton("Próbuj ponownie",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							onClickRemoveTransaction(null);
						}
					});
		}

		builder.setCancelable(false);
		dialog = builder.create();
		dialog.show();
	}

	private void updateDeletedTransactions() {
		SingletonLoadedWebContent globals = SingletonLoadedWebContent
				.getInstance();
		BaseTransaction item = (BaseTransaction) getIntent().getExtras()
				.getSerializable(BUNDLE_TRANSACTION);
		if (item != null)
			globals.addDeletedTransaction(item);
	}

	private void updateAccountsBalance() {
		BaseTransaction item = (BaseTransaction) getIntent().getExtras()
				.getSerializable(BUNDLE_TRANSACTION);
		if (item != null) {
			SingletonLoadedWebContent globals = SingletonLoadedWebContent
					.getInstance();
			Account from = null;
			Account to = null;
			if (item.getAccMinus() > 0)
				from = globals.getAccountById(item.getAccMinus());

			if (item.getAccPlus() > 0)
				to = globals.getAccountById(item.getAccPlus());

			if (from != null)
				from.setBalance(from.getBalance() + item.getValue());
			if (to != null)
				to.setBalance(to.getBalance() - item.getValue());
		}
	}

	public void onClickEditTransaction(View v) {
		BaseTransaction item = (BaseTransaction) getIntent().getExtras()
				.getSerializable(BUNDLE_TRANSACTION);

		Intent intent = new Intent(this, TransactionActivity.class);
		intent.putExtra(TransactionActivity.BUNDLE_TRANSACTION, item);
		startActivity(intent);
		finish();
	}
}
