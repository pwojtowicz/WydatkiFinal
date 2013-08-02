package pl.wppiotrek.wydatki.basepackage.httpconnection;

public class HttpException extends Exception {

	EHttpExceptionType exceptionType;

	public HttpException(EHttpExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}
}
