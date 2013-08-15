package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;

import com.google.gson.reflect.TypeToken;

class WebAccountProvider extends BaseProvider<Account> {

	public WebAccountProvider() {
		super("accounts");
	}

	@Override
	public ItemContainer<Account> readAll(boolean onlyActive) {
		String urlToContent = baseApiURL + controller + "?onlyActive="
				+ String.valueOf(onlyActive);
		final TypeToken<ItemContainer<Account>> token = new TypeToken<ItemContainer<Account>>() {
		};
		ItemContainer<Account> result = (ItemContainer<Account>) provider
				.sendGetAll(token, urlToContent, credential, properties);

		if (result != null)
			return result;
		return null;

	}

	@Override
	public Account readById(int id) {
		String urlToContent = baseApiURL + controller + "/"
				+ String.valueOf(id);
		final TypeToken<Account> token = new TypeToken<Account>() {
		};
		return provider.sendGet(token, urlToContent, credential, properties);
	}

	@Override
	public Account createOrUpdate(ModelBase item) {
		String urlToContent = baseApiURL + controller;
		if (item.getId() > 0)
			urlToContent += "/" + item.getId();
		return provider.createOrUpdate(Account.class, urlToContent, credential,
				properties, item);
	}

	@Override
	public OperationResult createOrUpdateMany(ModelBase[] item) {
		return null;
	}

}
