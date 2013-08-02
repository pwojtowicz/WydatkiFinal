package pl.wppiotrek.wydatki.basepackage.webacynctasks;

import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.providers.ProviderManager;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import android.os.AsyncTask;

public class AsyckTaskGetStartObjects extends AsyncTask<Void, Void, Boolean> {

	private IDownloadFromWebListener listener;

	public AsyckTaskGetStartObjects(IDownloadFromWebListener listener) {
		this.listener = listener;
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		this.listener.onDownloadChangeState(EDownloadState.Begin);
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		boolean wasSuccess = false;

		ProviderManager manager = new ProviderManager();
		SingletonLoadedWebContent singleton = SingletonLoadedWebContent
				.getInstance();

		ItemContainer<Account> accountsContainer = manager.getAll(
				ProviderType.Accounts, false);
		if (accountsContainer != null) {
			singleton.setAccounts(accountsContainer.getItemsList());
		}

		ItemContainer categoriesContainer = manager.getAll(
				ProviderType.Categories, false);
		if (categoriesContainer != null) {
			singleton.setCategories(categoriesContainer.getItemsList());
		}

		ItemContainer parametersContainer = manager.getAll(
				ProviderType.Parameters, false);
		if (parametersContainer != null) {
			singleton.setParameters(parametersContainer.getItemsList());
		}

		ItemContainer projectsContainer = manager.getAll(ProviderType.Projects,
				false);
		if (projectsContainer != null) {
			singleton.setProjects(projectsContainer.getItemsList());
		}

		singleton.setContentLoadedAtStart(true);

		return wasSuccess;
	}

	@Override
	public void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		this.listener.onDownloadChangeState(EDownloadState.End);
		if (result != null)
			this.listener.onDownloadResult(result);
		else
			this.listener.onDownloadChangeState(EDownloadState.End.Fail);

	}

}
