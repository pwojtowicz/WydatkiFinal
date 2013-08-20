package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.MultiOperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.Project;

import com.google.gson.reflect.TypeToken;

public class WebProjectProvider extends BaseProvider<Project> {

	public WebProjectProvider() {
		super("project");
	}

	@Override
	public ItemContainer<Project> readAll(boolean onlyActive) {
		String urlToContent = baseApiURL + controller + "?onlyActive="
				+ String.valueOf(onlyActive);
		final TypeToken<ItemContainer<Project>> token = new TypeToken<ItemContainer<Project>>() {
		};
		ItemContainer<Project> result = (ItemContainer<Project>) provider
				.sendGetAll(token, urlToContent, credential, properties);

		if (result != null)
			return result;
		return null;
	}

	@Override
	public Project readById(int id) {
		String urlToContent = baseApiURL + controller + "/"
				+ String.valueOf(id);
		final TypeToken<Project> token = new TypeToken<Project>() {
		};
		return provider.sendGet(token, urlToContent, credential, properties);
	}

	@Override
	public Project createOrUpdate(ModelBase item) {
		String urlToContent = baseApiURL + controller;
		if (item.getId() > 0)
			urlToContent += "/" + item.getId();
		return provider.createOrUpdate(Project.class, urlToContent, credential,
				properties, item);
	}

	@Override
	public MultiOperationResult createOrUpdateMany(ModelBase[] item) {
		// TODO Auto-generated method stub
		return null;
	}

}
