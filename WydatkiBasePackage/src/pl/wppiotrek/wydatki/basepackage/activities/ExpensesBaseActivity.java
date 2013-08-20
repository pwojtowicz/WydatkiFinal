package pl.wppiotrek.wydatki.basepackage.activities;

import pl.wppiotrek.wydatki.basepackage.entities.OperationResult;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyncTaskDownloadContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.EDownloadState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

public abstract class ExpensesBaseActivity extends FragmentActivity implements
		IDownloadFromWebListener {

	protected AlertDialog dialog;
	protected AsyncTaskDownloadContent task;

	@Override
	public void onDownloadChangeState(EDownloadState newState) {
		switch (newState) {
		case Begin:
			dialog = new ProgressDialog(this);
			dialog.setMessage("Wysy³anie danych");
			dialog.setCancelable(false);
			dialog.show();
			break;
		case End:
			task = null;
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			break;
		case Fail:
			dialogonResult(new OperationResult(false));
			break;
		case Success:
			break;
		}

	}

	protected abstract void dialogonResult(OperationResult result);
}
