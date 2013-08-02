package pl.wppiotrek.wydatki.basepackage.webacynctasks;

import pl.wppiotrek.wydatki.basepackage.providers.ProviderManager;
import android.os.AsyncTask;

public class AsyncTaskDownloadContent extends
		AsyncTask<TaskParameters, Void, Object> {

	private IDownloadFromWebListener listener;
	private boolean wasSuccess = false;

	public AsyncTaskDownloadContent(IDownloadFromWebListener listener) {
		this.listener = listener;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		this.listener.onDownloadChangeState(EDownloadState.Begin);
	}

	@Override
	protected Object doInBackground(TaskParameters... params) {
		Object response = null;
		if (params[0] == null)
			return null;
		TaskParameters parameters = params[0];

		ProviderManager provider = new ProviderManager();

		switch (parameters.operation) {
		case GetAll:
			response = provider.getAll(parameters.provider, false);
			break;
		case ChangeActiveState:
			provider.updateActiveStates(parameters.provider, parameters.items);
			break;
		case CreateOrUpdate:
			response = provider.createOrUpdate(parameters.provider,
					parameters.item);
			break;

		default:
			break;
		}

		// response = provider.getAll(parameters.provider, true);
		// response = provider.getById(ProviderType.Categories, 1);

		// response = provider.getAll(ProviderType.Accounts, false);
		// response = provider.getById(ProviderType.Accounts, 1);
		//
		// response = provider.getAll(ProviderType.Parameters, false);
		// response = provider.getById(ProviderType.Parameters, 1);
		//
		// response = provider.getAll(ProviderType.Projects, false);
		// response = provider.getById(ProviderType.Projects, 1);

		return response;

	}

	@Override
	public void onPostExecute(Object result) {
		super.onPostExecute(result);
		this.listener.onDownloadChangeState(EDownloadState.End);
		if (result != null)
			this.listener.onDownloadResult(result);
		else
			this.listener.onDownloadChangeState(EDownloadState.End.Fail);

	}

}
