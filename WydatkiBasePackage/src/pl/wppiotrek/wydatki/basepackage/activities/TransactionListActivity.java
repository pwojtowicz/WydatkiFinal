package pl.wppiotrek.wydatki.basepackage.activities;

import pl.wppiotrek.wydatki.basepackage.R;
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
		}
		return super.onOptionsItemSelected(item);
	}

}
