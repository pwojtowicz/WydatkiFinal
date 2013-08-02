package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;

public class ProviderManager {

	public ItemContainer getAll(ProviderType providerType, boolean onlyActive) {
		IWebRequestRepository provider = getProvider(providerType);
		return provider.readAll(onlyActive);
	}

	public Object getById(ProviderType providerType, int objectId) {
		IWebRequestRepository provider = getProvider(providerType);
		return provider.readById(objectId);
	}

	public OperationResult updateActiveStates(ProviderType providerType,
			ModelBase[] items) {
		IWebRequestRepository provider = getProvider(providerType);
		return provider.updateActiveStates(items);
	}

	public Object createOrUpdate(ProviderType providerType, ModelBase item) {
		IWebRequestRepository provider = getProvider(providerType);
		return provider.createOrUpdate(item);
	}

	private IWebRequestRepository getProvider(ProviderType providerType) {
		switch (providerType) {
		case Accounts:
			return new WebAccountProvider();
		case Parameters:
			return new WebParameterProvider();
		case Projects:
			return new WebProjectProvider();
		case Categories:
			return new WebCategoriesProvider();
		}
		return null;
	}

}
