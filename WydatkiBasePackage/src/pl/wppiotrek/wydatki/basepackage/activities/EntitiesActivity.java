package pl.wppiotrek.wydatki.basepackage.activities;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.SimpleFragmentAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.FragmentInfo;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.fragments.BaseSelectedListFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.ListAccountFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.ListCategoryFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.ListParametersFragment;
import pl.wppiotrek.wydatki.basepackage.fragments.ListProjectsFragment;
import pl.wppiotrek.wydatki.basepackage.helpers.StaticBundleValues;
import pl.wppiotrek.wydatki.basepackage.interfaces.IBaseListCallback;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class EntitiesActivity extends FragmentActivity implements
		ActionBar.TabListener, IBaseListCallback {
	SimpleFragmentAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;

	private ArrayList<FragmentInfo> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entities);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		fragments = new ArrayList<FragmentInfo>();
		Bundle arguments = new Bundle();
		arguments.putBoolean(
				BaseSelectedListFragment.BUNDLE_LIST_CAN_BE_SELECTED, true);

		fragments.add(new FragmentInfo(new ListAccountFragment(), "Konta",
				arguments));
		fragments.add(new FragmentInfo(new ListCategoryFragment(), "Kategorie",
				arguments));
		fragments.add(new FragmentInfo(new ListParametersFragment(),
				"Parametry", arguments));
		fragments.add(new FragmentInfo(new ListProjectsFragment(), "Projekty",
				arguments));

		mSectionsPagerAdapter = new SimpleFragmentAdapter(
				getSupportFragmentManager(), fragments);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					private int actualPage = 0;

					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
						if (actualPage != position)
							onFragmentHide(actualPage);
						actualPage = position;
					}

					private void onFragmentHide(int previousPage) {
						FragmentInfo fi = fragments.get(previousPage);
						if (fi != null)
							((BaseSelectedListFragment) fi.getFragment())
									.onNoLongerVisibled();
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {

			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(
				pl.wppiotrek.wydatki.basepackage.R.menu.menu_entities_selectable,
				menu);
		return true;
	}

	@Override
	public void onAddItemAction(ModelBase item) {
		Intent intent = new Intent(this, EditItemActivity.class);
		intent.putExtra(StaticBundleValues.BUNDLE_EDIT_ITEM, item);
		startActivity(intent);
	}
}
