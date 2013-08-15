package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;

import com.google.gson.reflect.TypeToken;

public class WebCategoriesProvider extends BaseProvider<Category> {

	public WebCategoriesProvider() {
		super("category");
	}

	@Override
	public ItemContainer<Category> readAll(boolean onlyActive) {
		String urlToContent = baseApiURL + controller + "?onlyActive="
				+ String.valueOf(onlyActive);
		final TypeToken<ItemContainer<Category>> token = new TypeToken<ItemContainer<Category>>() {
		};
		ItemContainer<Category> result = (ItemContainer<Category>) provider
				.sendGetAll(token, urlToContent, credential, properties);

		if (result != null)
			return result;
		return null;
	}

	@Override
	public Category readById(int id) {
		String urlToContent = baseApiURL + controller + "/"
				+ String.valueOf(id);
		final TypeToken<Category> token = new TypeToken<Category>() {
		};
		return provider.sendGet(token, urlToContent, credential, properties);
	}

	@Override
	public Category createOrUpdate(ModelBase item) {
		String urlToContent = baseApiURL + controller;
		if (item.getId() > 0)
			urlToContent += "/" + item.getId();
		return provider.createOrUpdate(Category.class, urlToContent,
				credential, properties, item);
	}

	@Override
	public OperationResult createOrUpdateMany(ModelBase[] item) {
		// TODO Auto-generated method stub
		return null;
	}

}
