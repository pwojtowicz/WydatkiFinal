package pl.wppiotrek.wydatki.basepackage.activities;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.FragmentAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.FragmentInfo;
import pl.wppiotrek.wydatki.basepackage.entities.MultiOperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.MultiOperationResult.ObjectResult;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.enums.OperationType;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction.ITransactionListener;
import pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction.NewInvokeTransactionFragment;
import pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction.ValidationHelper;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyncTaskDownloadContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.TaskParameters;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TransactionActivity extends ExpensesBaseActivity implements
		ITransactionListener {

	public static final String BUNDLE_IS_TRANSFER = "isTransfer";
	public static final String BUNDLE_TRANSACTION = "pl.wydatki.transactionToEdit";

	ViewPager mViewPager;

	private ArrayList<FragmentInfo> fragments;

	private FragmentAdapter fAdapter;
	int i = 1;
	boolean isTransfer = false;

	private AsyncTaskDownloadContent task;

	private AlertDialog dialog;

	private ArrayList<BaseTransaction> sendTransactions;
	private BaseTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			transaction = (BaseTransaction) bundle
					.getSerializable(BUNDLE_TRANSACTION);
			if (transaction != null)
				if (transaction.getAccMinus() > 0
						&& transaction.getAccPlus() > 0)
					isTransfer = true;
				else
					isTransfer = false;
			else
				isTransfer = bundle.getBoolean(BUNDLE_IS_TRANSFER, false);

		}

		fragments = new ArrayList<FragmentInfo>();

		Bundle b = new Bundle();
		b.putBoolean(NewInvokeTransactionFragment.BUNDLE_IS_TRANSER, isTransfer);
		b.putInt("INDEX", i);
		if (transaction != null)
			b.putSerializable(
					NewInvokeTransactionFragment.BUNDLE_EDIT_TRANSACTION,
					transaction);

		fragments.add(new FragmentInfo(new NewInvokeTransactionFragment(),
				"Transakcja " + String.valueOf(i), b));
		fragments.add(new FragmentInfo(new NewInvokeTransactionFragment(),
				"Transakcja " + String.valueOf(i), b));
		fragments.add(new FragmentInfo(new NewInvokeTransactionFragment(),
				"Transakcja " + String.valueOf(i), b));
		fragments.add(new FragmentInfo(new NewInvokeTransactionFragment(),
				"Transakcja " + String.valueOf(i), b));
		fragments.add(new FragmentInfo(new NewInvokeTransactionFragment(),
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
			ArrayList<BaseTransaction> transactions = new ArrayList<BaseTransaction>();
			String errorMessage = null;
			for (FragmentInfo fragment : fragments) {
				ValidationHelper validation = ((NewInvokeTransactionFragment) fragment
						.getFragment()).getCurrentTransaction();
				if (validation.isValid()) {
					BaseTransaction bt = validation.getItem();
					bt.setObjectHash(String.valueOf(System.identityHashCode(bt)));
					transactions.add(bt);
				} else {
					errorMessage = validation.getErrorMessage();
					break;
				}
			}

			if (errorMessage != null) {
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

	private void sendTransactionsToAPI(ArrayList<BaseTransaction> items) {
		if (task == null) {
			task = new AsyncTaskDownloadContent(this);
			BaseTransaction[] itemsArray = items
					.toArray(new BaseTransaction[items.size()]);
			TaskParameters params = new TaskParameters(
					ProviderType.Transactions,
					OperationType.CreateOrUpdateMany, itemsArray);
			task.execute(params);
			this.sendTransactions = items;
		}
	}

	@Override
	public void onDownloadResult(Object response) {
		MultiOperationResult or = (MultiOperationResult) response;
		dialogonResult(new OperationResult(or.isSuccess));
		if (or.isSuccess) {
			updateNewTransactions(or);
			updateAccountsBalance();
		}
	}

	private void updateNewTransactions(MultiOperationResult or) {
		SingletonLoadedWebContent globals = SingletonLoadedWebContent
				.getInstance();
		if (sendTransactions.size() > 0)
			for (BaseTransaction item : sendTransactions) {
				for (ObjectResult orItem : or.getObjectResults()) {
					if (orItem.getObjectHash().equals(item.getObjectHash()))
						item.setId(orItem.getObjectId());
				}
				globals.addTransaction(item);
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
			builder.setMessage("Zapisano poprawnie");
			OnClickListener onPositiveClickAfterSuccess = new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			};
			builder.setPositiveButton("OK", onPositiveClickAfterSuccess);

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
