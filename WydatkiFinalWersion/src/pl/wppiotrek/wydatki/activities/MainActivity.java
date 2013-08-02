package pl.wppiotrek.wydatki.activities;

import pl.wppiotrek.wydatki.EntitiesActivity;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.entities.Project;
import pl.wppiotrek.wydatki.basepackage.helpers.StaticBundleValues;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyckTaskGetStartObjects;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.EDownloadState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import pl.wppiotrek.wydatki.v2.EditItemActivity;
import pl.wppiotrek.wydatki.v2.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity implements
		IDownloadFromWebListener {

	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onEntitiesClick(View v) {
		startActivity(new Intent(this, EntitiesActivity.class));
	}

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
		super.onStop();
		if (!SingletonLoadedWebContent.getInstance().isContentLoadedAtStart()) {
			AsyckTaskGetStartObjects task = new AsyckTaskGetStartObjects(this);
			task.execute((Void) null);
		}
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
			builder.setTitle("B³¹d");
			builder.setMessage("B³¹d pobierania danych");
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
		// TODO Auto-generated method stub

	}
}
