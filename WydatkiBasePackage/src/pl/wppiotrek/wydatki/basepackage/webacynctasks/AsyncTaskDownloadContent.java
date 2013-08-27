package pl.wppiotrek.wydatki.basepackage.webacynctasks;

import pl.wppiotrek.wydatki.basepackage.providers.ProviderManager;

public class AsyncTaskDownloadContent extends BaseLoadContentAsyncTask {

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
		super.doInBackground(params);
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
			response = provider.updateActiveStates(parameters.provider,
					parameters.items);
			break;
		case CreateOrUpdate:
			response = provider.createOrUpdate(parameters.provider,
					parameters.item);
			break;
		case CreateOrUpdateMany:
			response = provider.createOrUpdateMany(parameters.provider,
					parameters.items);
			break;

		case GetTransactionWithFiltering:
			response = provider
					.getAllTransactionWithFilter(parameters.transactionFilter);
			break;
		case Delete:
			response = provider.delete(parameters.provider, parameters.item);

		default:
			break;
		}

		return response;

	}

	@Override
	public void onPostExecute(Object result) {
		super.onPostExecute(result);
		this.listener.onDownloadChangeState(EDownloadState.End);
		if (result != null)
			this.listener.onDownloadResult(result);
		else
			this.listener.onDownloadChangeState(EDownloadState.Fail);

	}

}
