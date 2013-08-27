package pl.wppiotrek.wydatki.basepackage.providers;

import java.lang.reflect.Type;
import java.util.Date;

import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.MultiOperationResult;
import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.httpconnection.EHttpRequestType;
import pl.wppiotrek.wydatki.basepackage.httpconnection.HttpAuthorizationCredentials;
import pl.wppiotrek.wydatki.basepackage.httpconnection.HttpConnectionManager;
import pl.wppiotrek.wydatki.basepackage.httpconnection.HttpException;
import pl.wppiotrek.wydatki.basepackage.httpconnection.HttpRequestProperties;
import pl.wppiotrek.wydatki.basepackage.httpconnection.HttpResponseBundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

class HttpRequestProvider<T> {

	public T sendGet(TypeToken<T> token, String urlAddress,
			HttpAuthorizationCredentials credential,
			HttpRequestProperties properties) {
		try {
			HttpResponseBundle response = HttpConnectionManager.sendRequest(
					EHttpRequestType.GET, credential, properties, urlAddress,
					null, null);

			if (response.getResponseStatusCode() == 200) {
				final Type type = token.getType();

				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(Date.class,
						new DateTimeDeserializer());

				final Gson gson = gsonBuilder.create();
				try {

					return gson.fromJson(response.getResponseContent(), type);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		} catch (HttpException ex) {

		}
		return null;

	}

	public ItemContainer sendGetAll(TypeToken<ItemContainer<T>> token,
			String urlAddress, HttpAuthorizationCredentials credential,
			HttpRequestProperties properties) {

		try {
			HttpResponseBundle response = HttpConnectionManager.sendRequest(
					EHttpRequestType.GET, credential, properties, urlAddress,
					null, null);

			if (response.getResponseStatusCode() == 200) {

				final Type type = token.getType();
				GsonBuilder gsonBuilder = new GsonBuilder();
				gsonBuilder.registerTypeAdapter(Date.class,
						new DateTimeDeserializer());

				final Gson gson = gsonBuilder.create();
				try {

					return gson.fromJson(response.getResponseContent(), type);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		} catch (HttpException e) {
			e.printStackTrace();
		}
		return null;
	}

	public OperationResult updateActiveStates(String urlToContent,
			HttpAuthorizationCredentials credential,
			HttpRequestProperties properties, ModelBase[] items) {
		final Gson gson = new GsonBuilder().create();

		String json = gson.toJson(items);

		try {
			HttpResponseBundle response = HttpConnectionManager.sendRequest(
					EHttpRequestType.PUT, credential, properties, urlToContent,
					null, json);

			if (response != null) {
				if (response.getResponseStatusCode() == 200)
					try {

						return gson.fromJson(response.getResponseContent(),
								OperationResult.class);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}

		} catch (HttpException e) {
			e.printStackTrace();
		}
		return null;

	}

	public T createOrUpdate(Class<?> classes, String urlToContent,
			HttpAuthorizationCredentials credential,
			HttpRequestProperties properties, ModelBase item) {
		Gson gson = new GsonBuilder().create();

		String json = gson.toJson(item);

		try {
			HttpResponseBundle response = HttpConnectionManager.sendRequest(
					EHttpRequestType.PUT, credential, properties, urlToContent,
					null, json);

			if (response != null) {
				if (response.getResponseStatusCode() == 200)
					try {
						GsonBuilder gsonBuilder = new GsonBuilder();
						gsonBuilder.registerTypeAdapter(Date.class,
								new DateTimeDeserializer());
						gson = gsonBuilder.create();

						return (T) gson.fromJson(response.getResponseContent(),
								classes);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
			}
		} catch (HttpException e) {
			e.printStackTrace();
		}
		return null;
	}

	public MultiOperationResult createOrUpdateMany(String urlToContent,
			HttpAuthorizationCredentials credential,
			HttpRequestProperties properties, ModelBase[] items) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateTimeSerializer());

		String json = gsonBuilder.create().toJson(items);

		try {
			HttpResponseBundle response = HttpConnectionManager.sendRequest(
					EHttpRequestType.PUT, credential, properties, urlToContent,
					null, json);

			gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(Date.class,
					new DateTimeDeserializer());

			final Gson gson = gsonBuilder.create();
			try {
				return gson.fromJson(response.getResponseContent(),
						MultiOperationResult.class);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (HttpException e) {
			e.printStackTrace();
		}
		return new MultiOperationResult(false);
	}

	public OperationResult delete(String urlToContent,
			HttpAuthorizationCredentials credential,
			HttpRequestProperties properties, ModelBase item) {
		try {
			HttpResponseBundle response = HttpConnectionManager.sendRequest(
					EHttpRequestType.DELETE, credential, properties,
					urlToContent, null, null);
			if (response.getResponseStatusCode() == 200) {
				final Gson gson = new GsonBuilder().create();

				return gson.fromJson(response.getResponseContent(),
						OperationResult.class);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		}
		return new OperationResult(false);
	}
}
