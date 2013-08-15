package pl.wppiotrek.wydatki.activities;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.adapters.FragmentAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.FragmentInfo;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.entities.Project;
import pl.wppiotrek.wydatki.basepackage.fragments.BaseSelectedListFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.ListAccountFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.ListTransactionFragment;
import pl.wppiotrek.wydatki.basepackage.helpers.StaticBundleValues;
import pl.wppiotrek.wydatki.basepackage.interfaces.IBaseListCallback;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyckTaskGetStartObjects;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.EDownloadState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import pl.wppiotrek.wydatki.v2.EditItemActivity;
import pl.wppiotrek.wydatki.v2.R;
import pl.wppiotrek.wydatki.v2.TransactionFilterActivity;
import pl.wppiotrek.wydatki.v2.TransactionListActivity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements
		IDownloadFromWebListener, IBaseListCallback {

	private AlertDialog dialog;
	private FragmentAdapter fAdapter;
	private ViewPager mViewPager;
	private ArrayList<FragmentInfo> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// public void onEntitiesClick(View v) {
	// startActivity(new Intent(this, EntitiesActivity.class));
	// }
	//
	// public void newTransactionClick(View v) {
	// Intent intent = new Intent(this, TransactionActivity.class);
	// intent.putExtra(TransactionActivity.BUNDLE_IS_TRANSFER, false);
	// startActivity(intent);
	// }
	//
	// public void newTransferClick(View v) {
	// Intent intent = new Intent(this, TransactionActivity.class);
	// intent.putExtra(TransactionActivity.BUNDLE_IS_TRANSFER, true);
	// startActivity(intent);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.create_items, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		ModelBase itemObject = null;
		int itemId = item.getItemId();

		switch (itemId) {
		case R.id.action_create_account:
			itemObject = new Account();
			break;
		case R.id.action_create_category:
			itemObject = new Category();
			break;
		case R.id.action_create_parameter:
			itemObject = new Parameter();
			break;
		case R.id.action_create_project:
			itemObject = new Project();
			break;
		case R.id.action_filter:
			Intent intent = new Intent(this, TransactionFilterActivity.class);
			startActivity(intent);
			break;

		}
		if (itemObject != null) {
			Intent intent = new Intent(this, EditItemActivity.class);

			intent.putExtra(StaticBundleValues.BUNDLE_EDIT_ITEM, itemObject);
			startActivity(intent);
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onStart() {
		super.onStart();
		reloadContent();
	}

	protected void reloadContent() {
		if (!SingletonLoadedWebContent.getInstance().isContentLoadedAtStart()) {
			AsyckTaskGetStartObjects task = new AsyckTaskGetStartObjects(this);
			task.execute((Void) null);
		} else {
			createFragments();
		}
	}

	private void createFragments() {
		fragments = new ArrayList<FragmentInfo>();

		Bundle arguments = new Bundle();
		arguments.putBoolean(
				BaseSelectedListFragment.BUNDLE_LIST_CAN_BE_SELECTED, false);
		arguments.putBoolean(
				BaseSelectedListFragment.BUNDLE_LIST_LONG_CLICK_ENABLED, true);

		fragments.add(new FragmentInfo(new ListAccountFragment(), "Konta",
				arguments));

		fragments.add(new FragmentInfo(new ListTransactionFragment(),
				"Transakcje", arguments));

		fAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(fAdapter);

	}

	@Override
	public void onDownloadChangeState(EDownloadState newState) {
		switch (newState) {
		case Begin:
			dialog = new ProgressDialog(this);
			dialog.setMessage("Pobieranie danych");
			dialog.setCancelable(false);
			dialog.show();
			break;
		case End:
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			break;
		case Fail:
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("B��d");
			builder.setMessage("B��d pobierania danych");
			builder.setNegativeButton("OK", null);
			builder.setCancelable(false);
			dialog = builder.create();
			dialog.show();
			break;
		case Success:

			break;
		}
	}

	@Override
	public void onDownloadResult(Object response) {
		SingletonLoadedWebContent.getInstance().setContentLoadedAtStart(true);
		reloadContent();

	}

	@Override
	public void onAddItemAction(ModelBase item) {
		Intent intent = new Intent(this, TransactionListActivity.class);
		intent.putExtra(TransactionListActivity.BUNDLE_ACCOUNT_TO_SHOW, item);
		startActivity(intent);
	}
}
