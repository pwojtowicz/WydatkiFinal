package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.TransactionFilter;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;

public class ProviderManager {

	public ItemContainer getAllTransactionWithFilter(TransactionFilter filter) {
		WebTransactionProvider provider = new WebTransactionProvider();
		return provider.readAllByFilter(filter);
	}

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

	public Object createOrUpdateMany(ProviderType providerType,
			ModelBase[] items) {
		IWebRequestRepository provider = getProvider(providerType);
		return provider.createOrUpdateMany(items);
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
		case Transactions:
			return new WebTransactionProvider();
		}
		return null;
	}

}
