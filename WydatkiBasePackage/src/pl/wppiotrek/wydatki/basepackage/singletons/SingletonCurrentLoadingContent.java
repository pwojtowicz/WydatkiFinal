package pl.wppiotrek.wydatki.basepackage.singletons;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingletonCurrentLoadingContent {

	public static AtomicBoolean accountPaused = new AtomicBoolean(false);

	public static void pauseAccount() {
		if (accountPaused.compareAndSet(false, true)) {
			synchronized (accountPaused) {
				accountPaused.notify();
			}
		}

	}

	public static void resumeAccount() {
		if (accountPaused.compareAndSet(true, false)) {
			synchronized (accountPaused) {
				accountPaused.notify();
			}
		}
	}

}
