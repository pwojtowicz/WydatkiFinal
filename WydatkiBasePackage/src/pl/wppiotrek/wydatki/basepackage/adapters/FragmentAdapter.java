package pl.wppiotrek.wydatki.basepackage.adapters;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.entities.FragmentInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

	protected ArrayList<FragmentInfo> fragments = new ArrayList<FragmentInfo>();

	public FragmentAdapter(FragmentManager fm, ArrayList<FragmentInfo> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int i) {

		FragmentInfo fragmentObject = fragments.get(i);

		return fragmentObject.getFragment();
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return FragmentPagerAdapter.POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		FragmentInfo fo = fragments.get(position);
		if (fo != null) {
			return fo.getTitle();
		}
		return "";
	}

}
