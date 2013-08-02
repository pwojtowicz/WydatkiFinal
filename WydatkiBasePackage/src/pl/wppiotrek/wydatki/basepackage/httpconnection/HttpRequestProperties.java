package pl.wppiotrek.wydatki.basepackage.httpconnection;

public class HttpRequestProperties {

	public int CONNECTION_TIMEOUT = 10000;
	public int READ_TIMEOUT = 5000;
	public String Accept;
	public String AcceptEncoding;
	public String ContentType;

	private HttpRequestProperties(Builder builder) {
		this.CONNECTION_TIMEOUT = builder.CONNECTION_TIMEOUT;
		this.READ_TIMEOUT = builder.READ_TIMEOUT;
		this.Accept = builder.Accept;
		this.AcceptEncoding = builder.AcceptEncoding;
		this.ContentType = builder.ContentType;
	}

	public static class Builder {

		public int CONNECTION_TIMEOUT = 10000;
		public int READ_TIMEOUT = 5000;
		public String Accept;
		public String AcceptEncoding;
		public String ContentType;

		public Builder setConnectionTimeOut(int timeout) {
			this.CONNECTION_TIMEOUT = timeout;
			return this;
		}

		public Builder setReadTimeOut(int timeout) {
			this.READ_TIMEOUT = timeout;
			return this;
		}

		public Builder setAccept(String accept) {
			this.Accept = accept;
			return this;
		}

		public Builder setAcceptEncoding(String acceptEncoding) {
			this.AcceptEncoding = acceptEncoding;
			return this;
		}

		public Builder setContentType(String contentType) {
			this.ContentType = contentType;
			return this;
		}

		public HttpRequestProperties build() {
			return new HttpRequestProperties(this);
		}
	}

}
