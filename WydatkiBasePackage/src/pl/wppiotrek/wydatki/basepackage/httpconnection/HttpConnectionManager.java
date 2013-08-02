package pl.wppiotrek.wydatki.basepackage.httpconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;

public class HttpConnectionManager {

	public static HttpResponseBundle sendRequest(EHttpRequestType requestType,
			HttpAuthorizationCredentials credential,
			HttpRequestProperties properties, String address,
			IHttpConnectionListener listener, String json) throws HttpException {

		if (listener != null && !listener.checkIsNetworkAvailable())
			throw new HttpException(EHttpExceptionType.Offline);

		switch (requestType) {
		case GET:
			return requestGet(address, properties, credential, json);
		case POST:
		case PUT:
			return requestPut(address, properties, credential, json);
		case DELETE:
			return requestDelete(json);
		default:
			return null;
		}
	}

	private static HttpURLConnection getConnection(String address,
			EHttpRequestType requestType) throws IOException {
		URL url = new URL(address);
		HttpURLConnection connection = null;
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(requestType.toString());
		return connection;
	}

	private static void authenticate(HttpURLConnection connection,
			HttpAuthorizationCredentials credential) {

		String authenticationData = credential.userName + ":"
				+ credential.password;
		String encodedString = new String(
				Base64.encodeBase64(authenticationData.getBytes()));
		String safeString = encodedString.replace('+', '-').replace('/', '_');
		connection
				.setRequestProperty("Authorization", "Basic " + encodedString);
		connection.setRequestProperty("Authorization", "Basic " + safeString);
	}

	private static HttpURLConnection setConnectionHeader(
			HttpURLConnection connection, HttpRequestProperties properties) {
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Accept-Encoding", "gzip");

		connection.setConnectTimeout(properties.CONNECTION_TIMEOUT);
		connection.setReadTimeout(properties.READ_TIMEOUT);

		if (properties.ContentType != null)
			connection.setRequestProperty("Content-Type",
					properties.ContentType);
		return connection;
	}

	private static HttpResponseBundle requestGet(String address,
			HttpRequestProperties properties,
			HttpAuthorizationCredentials credential, String json)
			throws HttpException {
		HttpResponseBundle response = new HttpResponseBundle();
		HttpURLConnection connection = null;
		int responseCode = 0;
		int fileSize = 0;
		try {
			connection = getConnection(address, EHttpRequestType.GET);
			if (credential != null)
				authenticate(connection, credential);
			connection = setConnectionHeader(connection, properties);
			connection.connect();
			responseCode = connection.getResponseCode();

			fileSize = connection.getContentLength();
			InputStream in = getConnectionInputStream(connection);

			try {
				response.setResponseContent(readInputStream(in));
			} catch (Exception e) {
				throw new HttpException(EHttpExceptionType.ReadResponseFail);
			}

		} catch (IOException e) {
			throw new HttpException(EHttpExceptionType.ConnectionFail);
		}

		response.setResponseStatusCode(responseCode);
		return response;
	}

	public static InputStream getConnectionInputStream(
			HttpURLConnection connection) throws IOException {
		InputStream input = null;
		String encoding = connection.getHeaderField("Content-Encoding");
		int responseCode = connection.getResponseCode();

		if (responseCode == 400)
			input = connection.getErrorStream();
		else
			input = connection.getInputStream();

		if (encoding != null && encoding.equals("gzip"))
			input = new GZIPInputStream(input);

		return input;
	}

	private static HttpResponseBundle requestPut(String address,
			HttpRequestProperties properties,
			HttpAuthorizationCredentials credential, String json)
			throws HttpException {
		properties.ContentType = "application/json";
		HttpResponseBundle response = new HttpResponseBundle();
		HttpURLConnection connection = null;
		int responseCode = 0;
		try {
			connection = getConnection(address, EHttpRequestType.PUT);
			if (credential != null)
				authenticate(connection, credential);

			connection.setDoOutput(true);
			connection.setChunkedStreamingMode(0);

			connection = setConnectionHeader(connection, properties);

			byte[] content = json.toString().getBytes();
			OutputStream output = null;
			try {
				output = connection.getOutputStream();

				int bufferLength = 4096;
				for (int i = 0; i < content.length; i += bufferLength) {

					if (content.length - i >= bufferLength) {
						output.write(content, i, bufferLength);
					} else {
						output.write(content, i, content.length - i);
					}
				}
			} finally {
				if (output != null) {
					output.close();
				}
			}
			boolean isSendOutput = true;

			responseCode = connection.getResponseCode();

			InputStream in = getConnectionInputStream(connection);

			try {
				response.setResponseContent(readInputStream(in));
			} catch (Exception e) {
				throw new HttpException(EHttpExceptionType.ReadResponseFail);
			}

		} catch (IOException e) {
			throw new HttpException(EHttpExceptionType.ConnectionFail);
		}

		response.setResponseStatusCode(responseCode);
		return response;
	}

	private static HttpResponseBundle requestDelete(String json) {
		HttpResponseBundle response = new HttpResponseBundle();

		response.setResponseStatusCode(HttpsURLConnection.HTTP_OK);
		return response;
	}

	private static String readInputStream(InputStream iStream)
			throws IOException {
		InputStreamReader iStreamReader = new InputStreamReader(iStream);
		BufferedReader bReader = new BufferedReader(iStreamReader);
		String line = null;
		StringBuilder builder = new StringBuilder();
		try {
			while ((line = bReader.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception ex) {

		} finally {
			bReader.close();
			iStreamReader.close();
			iStream.close();
		}
		return builder.toString();
	}

}
