package pl.wppiotrek.wydatki.basepackage.httpconnection;

public class HttpResponseBundle {

	private int responseStatusCode;
	private String responseContent;

	public int getResponseStatusCode() {
		return responseStatusCode;
	}

	public void setResponseStatusCode(int responseStatusCode) {
		this.responseStatusCode = responseStatusCode;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}

}
