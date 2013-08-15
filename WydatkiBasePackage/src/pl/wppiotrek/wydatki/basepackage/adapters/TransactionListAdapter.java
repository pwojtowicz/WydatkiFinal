package pl.wppiotrek.wydatki.basepackage.adapters;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.BaseSegmentListAdapter.ERowTypes;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.enums.ViewState;
import pl.wppiotrek.wydatki.basepackage.helpers.UnitConverter;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TransactionListAdapter extends BaseAdapter {

	private static final int MAX_TYPE_COUNT = 5;
	private ArrayList<Object> items = new ArrayList<Object>();
	private LayoutInflater inflater;
	private Context context;
	private SingletonLoadedWebContent globals = SingletonLoadedWebContent
			.getInstance();

	private View controlView;
	private ViewState actualControlState = ViewState.Normal;
	private int takeNextCount = 0;

	public TransactionListAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getItemViewType(int index) {
		if (items.size() == 0)
			return ERowTypes.ROW_HEADER;

		Object o = items.get(index);
		if (o != null && o instanceof String)
			return ERowTypes.ROW_HEADER;
		else
			return ERowTypes.ROW_CONTENT;
	}

	@Override
	public int getViewTypeCount() {
		return MAX_TYPE_COUNT;
	}

	@Override
	public int getCount() {
		return items.size() > 0 ? items.size() : 1;
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int actualRowType = getItemViewType(position);

		if (convertView == null)
			convertView = prepareView(convertView, actualRowType);

		fillView(actualRowType, position, convertView);

		return convertView;
	}

	protected View prepareView(View convertView, int actualRowType) {
		switch (actualRowType) {

		case ERowTypes.ROW_HEADER:
			convertView = inflater.inflate(R.layout.row_control_view, null);
			break;
		case ERowTypes.ROW_CONTENT:
			convertView = inflater.inflate(R.layout.row_transaction_layout,
					null);
			convertView.setTag(loadContentControlls(convertView));
			break;
		}
		return convertView;
	}

	private void fillView(int actualRowType, int position, View convertView) {
		if (actualRowType != ERowTypes.ROW_CONTENT)
			this.controlView = convertView;

		switch (actualRowType) {
		case ERowTypes.ROW_HEADER:
			fillControlView(convertView);
			break;
		case ERowTypes.ROW_CONTENT:
			fillTransactionView((BaseTransaction) getItem(position),
					convertView);
			break;
		}
	}

	private void fillControlView(View convertView) {
		TextView tv_text = (TextView) convertView
				.findViewById(R.id.control_row_view_content_textview);
		String string = "";

		ProgressBar pb = (ProgressBar) convertView
				.findViewById(R.id.control_row_view_progress_indicator);
		pb.setVisibility(View.GONE);

		switch (actualControlState) {
		case DownloadMore:
			string = String.format("Pobierz kolejne (%d)", this.takeNextCount);
			break;
		case DownloadException:
			string = "Spróbuj ponownie";
			break;
		case NoObjects:
			string = "Brak transakcji";
			break;
		case Downloading:
			string = "Pobieranie";
			pb.setVisibility(View.VISIBLE);
			break;
		}
		tv_text.setText(string);
	}

	private void fillTransactionView(BaseTransaction transaction,
			View convertView) {
		TransactionAdapterObjectHandler container = (TransactionAdapterObjectHandler) convertView
				.getTag();
		if (container != null) {
			if (transaction != null) {
				StringBuilder accounts = new StringBuilder();
				if (transaction.getAccMinus() > 0) {
					Account accMinus = globals.getAccountById(transaction
							.getAccMinus());

					if (accMinus != null)
						accounts.append(accMinus.getName());
				}

				if (transaction.getAccMinus() > 0
						&& transaction.getAccPlus() > 0)
					accounts.append(" >> ");

				if (transaction.getAccPlus() > 0)
					accounts.append(globals.getAccountById(
							transaction.getAccPlus()).getName());

				String categoryName = "";

				if (transaction.getCategoryId() > 0) {
					Category cat = globals.getCategoryById(transaction
							.getCategoryId());
					if (cat != null)
						categoryName = cat.getName();
				}

				container.accounts.setText(accounts.toString());

				StringBuilder note = new StringBuilder();
				if (transaction.getNote() != null
						&& transaction.getNote().length() > 0)
					note.append(" (" + transaction.getNote() + ")");
				else
					note.append("");

				container.note.setText(String.format("%s %s", categoryName,
						note.toString()));

				container.value.setText(UnitConverter
						.doubleToCurrency(transaction.getValue()));

				container.date.setText(UnitConverter.dateTimeString(transaction
						.getDate()));

				if (transaction.getAccMinus() > 0
						&& transaction.getAccPlus() > 0) {
					container.value.setTextColor(context.getResources()
							.getColor(R.color.yellow));
				} else if (transaction.getAccMinus() > 0)
					container.value.setTextColor(context.getResources()
							.getColor(R.color.darkRed));
				else if (transaction.getAccPlus() > 0)
					container.value.setTextColor(context.getResources()
							.getColor(R.color.darkGreen));
				container.transaction = transaction;
			}
		}
	}

	private TransactionAdapterObjectHandler loadContentControlls(
			View convertView) {
		TransactionAdapterObjectHandler container = new TransactionAdapterObjectHandler();
		container.value = (TextView) convertView
				.findViewById(R.id.row_transaction_value);
		container.accounts = (TextView) convertView
				.findViewById(R.id.row_transaction_accounts);
		container.note = (TextView) convertView
				.findViewById(R.id.row_transaction_note);
		container.date = (TextView) convertView
				.findViewById(R.id.row_transaction_date);
		return container;
	}

	public class TransactionAdapterObjectHandler {
		public BaseTransaction transaction;
		public TextView value;
		public TextView accounts;
		public TextView note;
		public TextView date;
	}

	public void clearItems() {
		items = new ArrayList<Object>();
		notifyDataSetChanged();
	}

	public void addItem(BaseTransaction item) {
		items.add(item);
	}

	public void addDownloadMoreItem(boolean hasMore, int takeNextCount) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) instanceof String)
				items.remove(i);
		}
		this.takeNextCount = takeNextCount;
		actualControlState = ViewState.DownloadMore;
		items.add("");
	}

	public void setViewStateAsDownloadAgain() {
		actualControlState = ViewState.DownloadException;
		this.notifyDataSetChanged();
	}

	public void setViewStateAsDownloading() {
		actualControlState = ViewState.Downloading;
		this.notifyDataSetChanged();
	}

	public ViewState getActualViewState() {
		return actualControlState;
	}

	public void removeControlView() {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) instanceof String)
				items.remove(i);
		}
	}

}
