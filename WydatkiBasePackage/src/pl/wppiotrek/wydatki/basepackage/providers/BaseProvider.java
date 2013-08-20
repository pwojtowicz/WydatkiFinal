package pl.wppiotrek.wydatki.basepackage.providers;

import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.httpconnection.HttpAuthorizationCredentials;
import pl.wppiotrek.wydatki.basepackage.httpconnection.HttpRequestProperties;

abstract class BaseProvider<T> implements IWebRequestRepository<T> {

	protected String controller = "";
	protected HttpRequestProvider<T> provider = new HttpRequestProvider<T>();

	protected static String baseApiURL = "http://192.168.0.101:1234/api/";
	protected HttpAuthorizationCredentials credential = new HttpAuthorizationCredentials(
			"wppiotrek85", "123qwe");
	protected HttpRequestProperties properties = new HttpRequestProperties.Builder()
			.setConnectionTimeOut(10000).setReadTimeOut(3000).build();

	public BaseProvider(String controller) {
		this.controller = controller;
	}

	@Override
	public OperationResult updateActiveStates(ModelBase[] items) {
		String urlToContent = baseApiURL + controller + "/UpdateActiveState";
		return provider.updateActiveStates(urlToContent, credential,
				properties, items);
	}

	@Override
	public OperationResult delateItem(ModelBase item) {
		String urlToContent = baseApiURL + controller + "/" + item.getId();
		return provider.delete(urlToContent, credential, properties, item);
	}
}
