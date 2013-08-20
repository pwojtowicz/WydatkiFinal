package pl.wppiotrek.wydatki.basepackage.activities;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.fragments.TransactionFilterFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class TransactionFilterActivity extends FragmentActivity {

	private TransactionFilterFragment details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_filter);

		if (details == null) {
			details = new TransactionFilterFragment();
			details.setArguments(getIntent().getExtras());
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
