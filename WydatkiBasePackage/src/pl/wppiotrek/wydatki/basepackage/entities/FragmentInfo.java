package pl.wppiotrek.wydatki.basepackage.entities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FragmentInfo {

	private Fragment fragment;
	private String title;

	public FragmentInfo(Fragment fragment, String title) {
		this.fragment = fragment;
		this.title = title;
	}

	public FragmentInfo(Fragment fragment, String title, Bundle arguments) {

		this(fragment, title);
		fragment.setArguments(arguments);
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
