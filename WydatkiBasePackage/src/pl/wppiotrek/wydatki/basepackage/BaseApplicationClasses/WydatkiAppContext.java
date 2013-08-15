package pl.wppiotrek.wydatki.basepackage.BaseApplicationClasses;

import pl.wppiotrek.wydatki.basepackage.database.DataBaseManager;
import android.app.Application;
import android.content.Context;

public class WydatkiAppContext extends Application {

	private static Context context;

	public void onCreate() {
		super.onCreate();
		DataBaseManager.inicjalizeInstance(getApplicationContext());

		boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
				.getBoolean("firstrun", true);
		if (firstrun) {

			getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
					.putBoolean("firstrun", false).commit();
		}
		this.context = this;
	}

	public static Context getAppContext() {
		return context;
	}
}
