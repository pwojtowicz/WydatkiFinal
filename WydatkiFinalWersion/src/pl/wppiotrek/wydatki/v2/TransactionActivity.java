package pl.wppiotrek.wydatki.v2;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.adapters.FragmentAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.FragmentInfo;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.Transaction;
import pl.wppiotrek.wydatki.basepackage.enums.OperationType;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction.ITransactionListener;
import pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction.InvokeTransactionFragment;
import pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction.InvokeTransactionFragment.ValidationHelper;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyncTaskDownloadContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.EDownloadState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.TaskParameters;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TransactionActivity extends FragmentActivity implements
		ITransactionListener, IDownloadFromWebListener {

	public static final String BUNDLE_IS_TRANSFER = "isTransfer";

	ViewPager mViewPager;

	private ArrayList<FragmentInfo> fragments;

	private FragmentAdapter fAdapter;
	int i = 1;
	boolean isTransfer = false;

	private AsyncTaskDownloadContent task;

	private AlertDialog dialog;

	private ArrayList<Transaction> sendTransactions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			isTransfer = bundle.getBoolean(BUNDLE_IS_TRANSFER, false);
		}

		fragments = new ArrayList<FragmentInfo>();

		Bundle b = new Bundle();
		b.putBoolean(InvokeTransactionFragment.BUNDLE_IS_NEW_TRANSFER,
				isTransfer);
		b.putInt("INDEX", i);

		fragments.add(new FragmentInfo(new InvokeTransactionFragment(),
				"Transakcja " + String.valueOf(i), b));

		for (int j = 0; j < 5; j++) {
			i++;
			b = new Bundle();
			b.putBoolean(InvokeTransactionFragment.BUNDLE_IS_NEW_TRANSFER,
					!isTransfer);
			b.putInt("INDEX", i);

			fragments.add(new FragmentInfo(new InvokeTransactionFragment(),
					"Transakcja " + String.valueOf(i), b));
		}
		i++;
		b = new Bundle();
		b.putBoolean(InvokeTransactionFragment.BUNDLE_IS_NEW_TRANSFER,
				!isTransfer);
		b.putInt("INDEX", i);

		fragments.add(new FragmentInfo(new InvokeTransactionFragment(),
				"Transakcja " + String.valueOf(i), b));

		fAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(fAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.transaction, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_save) {
			prepareToSave();
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void prepareToSave() {
		if (task == null) {
			ArrayList<Transaction> transactions = new ArrayList<Transaction>();
			String errorMessage = null;
			for (FragmentInfo fragment : fragments) {
				ValidationHelper validation = ((InvokeTransactionFragment) fragment
						.getFragment()).getCurrentTransaction();
				if (validation.isValid())
					transactions.add(validation.getItem());
				else {
					errorMessage = validation.getErrorMessage();
					break;
				}
			}

			if (errorMessage != null) {
				// DialogFactoryManager.create(DialogStyle.Information, this,
				// null,
				// errorMessage, null).show();
				Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("B³¹d");
				builder.setMessage("Nie wszystkie pola usupe³nione poprawnie:/n"
						+ errorMessage);
				builder.setCancelable(false);
				builder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			} else {
				sendTransactionsToAPI(transactions);
			}
		}
	}

	private void sendTransactionsToAPI(ArrayList<Transaction> items) {
		if (task == null) {
			task = new AsyncTaskDownloadContent(this);
			Transaction[] itemsArray = items.toArray(new Transaction[items
					.size()]);
			TaskParameters params = new TaskParameters(
					ProviderType.Transactions,
					OperationType.CreateOrUpdateMany, itemsArray);
			task.execute(params);
			this.sendTransactions = items;
		}
	}

	@Override
	public void onDownloadChangeState(EDownloadState newState) {
		switch (newState) {
		case Begin:
			dialog = new ProgressDialog(this);
			dialog.setMessage("Wysy³anie danych");
			dialog.setCancelable(false);
			dialog.show();
			break;
		case End:
			task = null;
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			break;
		case Fail:
			dialogonResult(new OperationResult(false));
			break;
		case Success:
			break;
		}

	}

	@Override
	public void onDownloadResult(Object response) {
		OperationResult or = (OperationResult) response;
		dialogonResult(or);
		if (or.isSuccess)
			updateAccountsBalance();
	}

	private void dialogonResult(OperationResult result) {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
		Builder builder = new AlertDialog.Builder(this);

		if (result.isSuccess) {
			builder.setTitle("Powodzenie");
			builder.setMessage("Zapisano poprawnie");
			builder.setPositiveButton("OK", null);

		} else {
			builder.setTitle("B³¹d");
			builder.setMessage("B³¹d wysy³ania danych");
			builder.setNegativeButton("Anuluj", null);
			builder.setPositiveButton("Próbuj ponownie",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							prepareToSave();
						}
					});
		}

		builder.setCancelable(false);
		dialog = builder.create();
		dialog.show();
	}

	private void updateAccountsBalance() {
		if (this.sendTransactions != null && this.sendTransactions.size() > 0) {
			SingletonLoadedWebContent globals = SingletonLoadedWebContent
					.getInstance();

			for (BaseTransaction item : this.sendTransactions) {
				Account from = null;
				Account to = null;
				if (item.getAccMinus() > 0)
					from = globals.getAccountById(item.getAccMinus());

				if (item.getAccPlus() > 0)
					to = globals.getAccountById(item.getAccPlus());

				if (from != null)
					from.setBalance(from.getBalance() - item.getValue());
				if (to != null)
					to.setBalance(to.getBalance() + item.getValue());

			}
		}
	}
}
