package pl.wppiotrek.wydatki.basepackage.support;

import pl.wppiotrek.wydatki.basepackage.BaseApplicationClasses.WydatkiAppContext;
import android.content.Context;
import android.os.Vibrator;

public class VibratorSupport {

	public static void vibrate(int miliseconds) {
		Vibrator vibrator = (Vibrator) WydatkiAppContext.getAppContext()
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(miliseconds);
	}
}
