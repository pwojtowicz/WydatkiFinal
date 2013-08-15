package pl.wppiotrek.wydatki.transactions.views;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.enums.ViewState;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ControlListRowView {

	protected View view;

	protected Context context;

	private ViewState content;

	private TextView stateTextView;

	private ProgressBar roundProggresBar;

	public ControlListRowView(Context context, View view, ViewState content) {
		this.view = view;
		this.context = context;
		this.content = content;
		linkViews();
		fillViews();
	}

	public void linkViews() {
		stateTextView = (TextView) view
				.findViewById(R.id.control_row_view_content_textview);

		roundProggresBar = (ProgressBar) view
				.findViewById(R.id.control_row_view_progress_indicator);

	}

	public void fillViews() {
		String message = "";
		int progressIndicatorVisibility = ProgressBar.INVISIBLE;
		switch (content) {
		case DownloadMore:
			message = "Pobierz nastêpne";
			progressIndicatorVisibility = ProgressBar.INVISIBLE;
			break;
		case NoObjects:
			message = "Brak elementów";
			progressIndicatorVisibility = ProgressBar.INVISIBLE;
			break;
		case Downloading:
			message = "Pobieranie transakcji";
			progressIndicatorVisibility = ProgressBar.VISIBLE;
			break;
		case DownloadException:
			message = "Spróbuj ponownie";
			progressIndicatorVisibility = ProgressBar.INVISIBLE;
			break;
		}
		stateTextView.setText(message.toString());
		roundProggresBar.setVisibility(progressIndicatorVisibility);
		view.setTag(content);
	}

	public View getView() {
		return view;
	}
}
