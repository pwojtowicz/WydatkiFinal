package pl.wppiotrek.wydatki.basepackage.fragments.edit;

import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.enums.OperationType;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.helpers.StaticBundleValues;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyncTaskDownloadContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.EDownloadState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.TaskParameters;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class BaseEditItemFragment<T extends ModelBase> extends
		Fragment implements IDownloadFromWebListener {

	private T actualItem;
	private AlertDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle extras = getArguments();
		if (extras != null) {
			actualItem = (T) extras
					.getSerializable(StaticBundleValues.BUNDLE_EDIT_ITEM);
		}
	}

	protected T getActualItem() {
		return actualItem;
	}

	public void onSaveClick() {
		actualItem = prepareToSave(actualItem);

		if (isValid(actualItem)) {
			saveItem(actualItem);
		}
	}

	public void onCancelClick() {

	}

	@Override
	public void onResume() {
		super.onResume();
		configureItemView(actualItem);
	}

	public abstract T prepareToSave(T itemToSave);

	public abstract boolean isValid(T itemToValid);

	public void saveItem(T item) {
		AsyncTaskDownloadContent task = new AsyncTaskDownloadContent(this);
		task.execute(new TaskParameters(getProviderType(),
				OperationType.CreateOrUpdate, (ModelBase) item));
	}

	// public abstract void itemToEdit(T item);

	public abstract void configureItemView(T item);

	protected abstract ProviderType getProviderType();

	@Override
	public void onDownloadChangeState(EDownloadState newState) {
		switch (newState) {
		case Begin:
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Wysy³anie danych");
			dialog.setCancelable(false);
			dialog.show();
			break;
		case End:
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			break;
		case Fail:
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("B³¹d");
			builder.setMessage("B³¹d wysy³ania danych");
			builder.setPositiveButton("Spróbuj ponornie", tryAgainDialogClick);
			builder.setNegativeButton("Anuluj", null);
			builder.setCancelable(false);
			dialog = builder.create();
			dialog.show();
			break;
		case Success:

			break;
		}
	}

	private OnClickListener tryAgainDialogClick = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {

		}
	};

	private OnClickListener successDialogClickOK = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (getActivity() != null)
				getActivity().finish();
		}
	};

	@Override
	public void onDownloadResult(Object response) {
		if (getActivity() != null) {
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Sukces");
			builder.setMessage("Dane zapisane poprawnie");
			builder.setPositiveButton("OK", successDialogClickOK);
			builder.create().show();
			afterUpdateOrCreate((T) response);
		}
	}

	private void afterUpdateOrCreate(T responseItem) {
		SingletonLoadedWebContent.getInstance().insertItemToLoadedContent(
				responseItem);
	}
}
