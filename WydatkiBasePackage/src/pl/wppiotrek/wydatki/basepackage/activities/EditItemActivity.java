package pl.wppiotrek.wydatki.basepackage.activities;

import java.util.NoSuchElementException;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.entities.Project;
import pl.wppiotrek.wydatki.basepackage.fragments.edit.BaseEditItemFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.edit.EditAccountFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.edit.EditCategoryFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.edit.EditParameterFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.edit.EditProjectFragment;
import pl.wppiotrek.wydatki.basepackage.helpers.StaticBundleValues;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class EditItemActivity extends FragmentActivity {

	private BaseEditItemFragment details;
	private Class<? extends ModelBase> itemClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		linkViews();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			itemClass = ((ModelBase) extras
					.getSerializable(StaticBundleValues.BUNDLE_EDIT_ITEM))
					.getClass();
		}

	}

	private void linkViews() {

	}

	@Override
	public void onResume() {
		super.onResume();
		configureViews();
	}

	private void configureViews() {

		if (itemClass.equals(Parameter.class)) {
			details = new EditParameterFragment();
		} else if (itemClass.equals(Project.class)) {
			details = new EditProjectFragment();
		} else if (itemClass.equals(Account.class)) {
			details = new EditAccountFragment();
		} else if (itemClass.equals(Category.class)) {
			details = new EditCategoryFragment();
		} else {
			throw new NoSuchElementException();
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

	public void onSaveClick(View v) {
		if (details != null)
			details.onSaveClick();
	}

	public void onCancelClick(View v) {
		if (details != null)
			details.onCancelClick();
	}
}
