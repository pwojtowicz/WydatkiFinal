package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;

import com.google.gson.reflect.TypeToken;

public class WebParameterProvider extends BaseProvider<Parameter> {

	public WebParameterProvider() {
		super("parameter");
	}

	@Override
	public ItemContainer<Parameter> readAll(boolean onlyActive) {
		String urlToContent = baseApiURL + controller + "?onlyActive="
				+ String.valueOf(onlyActive);
		final TypeToken<ItemContainer<Parameter>> token = new TypeToken<ItemContainer<Parameter>>() {
		};
		ItemContainer<Parameter> result = (ItemContainer<Parameter>) provider
				.sendGetAll(token, urlToContent, credential, properties);

		if (result != null)
			return result;
		return null;
	}

	@Override
	public Parameter readById(int id) {
		String urlToContent = baseApiURL + controller + "/"
				+ String.valueOf(id);
		final TypeToken<Parameter> token = new TypeToken<Parameter>() {
		};
		return provider.sendGet(token, urlToContent, credential, properties);
	}

	@Override
	public Parameter createOrUpdate(ModelBase item) {
		String urlToContent = baseApiURL + controller;
		if (item.getId() > 0)
			urlToContent += "/" + item.getId();
		return provider.createOrUpdate(Parameter.class, urlToContent,
				credential, properties, item);
	}

}
