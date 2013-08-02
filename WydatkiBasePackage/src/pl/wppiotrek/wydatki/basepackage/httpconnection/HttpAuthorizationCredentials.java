package pl.wppiotrek.wydatki.basepackage.httpconnection;

public class HttpAuthorizationCredentials {
	public String userName;
	public String password;

	public HttpAuthorizationCredentials(String userName, String password) {
		this.password = password;
		this.userName = userName;
	}
}
