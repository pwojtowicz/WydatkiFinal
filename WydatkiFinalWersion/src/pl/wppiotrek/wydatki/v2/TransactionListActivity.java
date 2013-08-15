package pl.wppiotrek.wydatki.v2;

import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.fragments.ListTransactionFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class TransactionListActivity extends FragmentActivity {

	public static final String BUNDLE_ACCOUNT_TO_SHOW = "pl.wydatki.transaction_list_account_to_show";
	private static final int REQUEST_CODE_FILTER = 123;
	private ListTransactionFragment details;
	private Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_list);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			account = (Account) extras.getSerializable(BUNDLE_ACCOUNT_TO_SHOW);

		}

		prepareTransactionFragment();
	}

	private void prepareTransactionFragment() {
		if (account != null) {

			setTitle(account.getName());

			if (details == null) {
				details = new ListTransactionFragment();
				details.setAccount(account);

			}

			if (details != null) {
				details.setArguments(getIntent().getExtras());
				FragmentTransaction ft = getSupportFragmentManager()
						.beginTransaction();
				ft.replace(R.id.details, details);

				ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
				ft.commit();
			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
			// case R.id.action_filter:
			// // Intent intent = new Intent(this,
			// TransactionFilterActivity.class);
			// // intent.putExtra(
			// // TransactionFilterFragment.BUNDLE_TRANSACTION_FILTER,
			// // details.getFilter());
			// // startActivityForResult(intent, REQUEST_CODE_FILTER);
			// // return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// if (requestCode == REQUEST_CODE_FILTER) {
	// if (resultCode == RESULT_OK && data.getExtras() != null) {
	// TransactionFilter filter = (TransactionFilter) data
	// .getExtras()
	// .getSerializable(
	// TransactionFilterFragment.BUNDLE_TRANSACTION_FILTER);
	// if (filter != null && details != null)
	// details.updateFilter(filter);
	// }
	// }
	// }
}
