package pl.wppiotrek.wydatki.basepackage.webacynctasks;

public interface IDownloadFromWebListener {

	public void onDownloadChangeState(EDownloadState newState);

	public void onDownloadResult(Object response);
}
