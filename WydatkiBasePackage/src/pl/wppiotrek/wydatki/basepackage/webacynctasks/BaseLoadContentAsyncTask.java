package pl.wppiotrek.wydatki.basepackage.webacynctasks;

import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonCurrentLoadingContent;
import android.os.AsyncTask;

public class BaseLoadContentAsyncTask extends
		AsyncTask<TaskParameters, Void, Object> {

	private ProviderType provider;

	@Override
	protected Object doInBackground(TaskParameters... params) {
		TaskParameters parameters = params[0];
		this.provider = parameters.provider;
		try {
			switch (this.provider) {
			case Accounts:
				SingletonCurrentLoadingContent.pauseAccount();
				break;
			case Categories:

				break;
			case Parameters:

				break;
			case Projects:

				break;
			case Transactions:
				while (SingletonCurrentLoadingContent.accountPaused.get()) {
					synchronized (SingletonCurrentLoadingContent.accountPaused) {
						SingletonCurrentLoadingContent.accountPaused.wait();
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public void onPostExecute(Object result) {
		super.onPostExecute(result);
		switch (this.provider) {
		case Accounts:
			SingletonCurrentLoadingContent.resumeAccount();
			break;
		case Categories:

			break;
		case Parameters:

			break;
		case Projects:

			break;
		case Transactions:

			break;
		default:
			break;
		}
	}

}
