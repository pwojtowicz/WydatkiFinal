package pl.wppiotrek.wydatki.basepackage.BaseApplicationClasses;

import pl.wppiotrek.wydatki.basepackage.database.DataBaseManager;
import android.app.Application;

public class WydatkiAppContext extends Application {

	public void onCreate() {
		super.onCreate();
		DataBaseManager.inicjalizeInstance(getApplicationContext());

		boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
				.getBoolean("firstrun", true);
		if (firstrun) {

			getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
					.putBoolean("firstrun", false).commit();

			// new DataBaseHelper(this).onFirstRun();
		}

	}
}
