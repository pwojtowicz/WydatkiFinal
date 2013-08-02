package pl.wppiotrek.wydatki.basepackage.adapters;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.entities.FragmentInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentAdapter extends FragmentPagerAdapter {

	protected ArrayList<FragmentInfo> fragments = new ArrayList<FragmentInfo>();

	public SimpleFragmentAdapter(FragmentManager fm,
			ArrayList<FragmentInfo> fragments) {
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

	public void addFragment(FragmentInfo newFragment) {
		fragments.add(newFragment);
		this.notifyDataSetChanged();
	}

	public void removeFragment(int fragmentIndex) {
		fragments.remove(fragmentIndex);
		this.notifyDataSetChanged();
	}

	// @Override
	// public void destroyItem(View collection, int position, Object view) {
	// ((ViewPager) collection).removeView((View) view);
	// }

}
