package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.MultiOperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.TransactionFilter;

import com.google.gson.reflect.TypeToken;

public class WebTransactionProvider extends BaseProvider<BaseTransaction> {

	public WebTransactionProvider() {
		super("transactions");
	}

	public ItemContainer<BaseTransaction> readAllByFilter(
			TransactionFilter filter) {
		String urlToContent = baseApiURL + controller
				+ filter.getFilterString();
		System.out.println(urlToContent);
		final TypeToken<ItemContainer<BaseTransaction>> token = new TypeToken<ItemContainer<BaseTransaction>>() {
		};
		ItemContainer<BaseTransaction> result = (ItemContainer<BaseTransaction>) provider
				.sendGetAll(token, urlToContent, credential, properties);

		if (result != null)
			return result;
		return null;

	}

	@Override
	public ItemContainer<BaseTransaction> readAll(boolean onlyActive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseTransaction readById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OperationResult updateActiveStates(ModelBase[] items) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseTransaction createOrUpdate(ModelBase item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MultiOperationResult createOrUpdateMany(ModelBase[] items) {
		String urlToContent = baseApiURL + controller;
		return provider.createOrUpdateMany(urlToContent, credential,
				properties, items);
	}

}
