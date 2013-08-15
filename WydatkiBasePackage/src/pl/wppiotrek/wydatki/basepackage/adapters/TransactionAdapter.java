package pl.wppiotrek.wydatki.basepackage.adapters;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.enums.ViewState;
import pl.wppiotrek.wydatki.basepackage.helpers.UnitConverter;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.transactions.views.ControlListRowView;
import pl.wppiotrek.wydatki.transactions.views.ViewType;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TransactionAdapter extends BaseObjectAdapter<BaseTransaction> {

	private View controlView;
	private ViewState actualControlState = ViewState.Normal;
	private SingletonLoadedWebContent globals;

	public TransactionAdapter(Context context, ArrayList<BaseTransaction> list,
			boolean hasMore, IOnAdapterCheckboxClick listener) {
		super(context, list, listener);
		globals = SingletonLoadedWebContent.getInstance();

		if (hasMore) {
			this.items.add(null);
			actualControlState = ViewState.DownloadMore;
		}
	}

	public int getCount() {
		return items.size();
	}

	public Object getItem(int index) {
		return items.get(index);
	}

	public long getItemId(int index) {
		return index;
	}

	@Override
	public int getItemViewType(int index) {
		Object o = items.get(index);
		if (o != null && !(o instanceof String))
			return ViewType.DEFAULT;
		else
			return ViewType.CONTROL;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		TransactionAdapterObjectHandler oh = null;
		int viewType = getItemViewType(index);
		Object obj = null;
		if (convertView != null) {
			obj = convertView.getTag();
		}

		Object o = getItem(index);
		if (convertView == null
				|| (obj != null && viewType == ViewType.DEFAULT && obj instanceof ViewState)
				|| (obj != null && viewType == ViewType.CONTROL && obj instanceof TransactionAdapterObjectHandler)) {

			switch (viewType) {
			case ViewType.DEFAULT: {
				oh = new TransactionAdapterObjectHandler();
				convertView = inflater.inflate(R.layout.row_transaction_layout,
						null);
				oh.value = (TextView) convertView
						.findViewById(R.id.row_transaction_value);
				oh.accounts = (TextView) convertView
						.findViewById(R.id.row_transaction_accounts);
				oh.note = (TextView) convertView
						.findViewById(R.id.row_transaction_note);
				oh.date = (TextView) convertView
						.findViewById(R.id.row_transaction_date);
				convertView.setTag(oh);
			}
				break;
			case ViewType.CONTROL:
				convertView = inflater.inflate(R.layout.row_control_view, null);
				break;
			}
		}
		if (o != null) {
			TransactionAdapterObjectHandler ohs = (TransactionAdapterObjectHandler) convertView
					.getTag();
			fillRow(convertView, o, index);
			super.setCheckableViewState(convertView, (ModelBase) o);
		} else {
			setDownloadView(convertView);
		}

		return convertView;
	}

	private void fillRow(View convertView, Object o, int index) {
		TransactionAdapterObjectHandler oh = (TransactionAdapterObjectHandler) convertView
				.getTag();
		BaseTransaction transaction = (BaseTransaction) o;
		if (transaction != null) {
			StringBuilder accounts = new StringBuilder();
			if (transaction.getAccMinus() > 0) {
				Account accMinus = globals.getAccountById(transaction
						.getAccMinus());

				if (accMinus != null)
					accounts.append(accMinus.getName());
			}

			if (transaction.getAccMinus() > 0 && transaction.getAccPlus() > 0)
				accounts.append(" >> ");

			if (transaction.getAccPlus() > 0)
				accounts.append(globals
						.getAccountById(transaction.getAccPlus()).getName());

			String categoryName = "";

			if (transaction.getCategoryId() > 0) {
				Category cat = globals.getCategoryById(transaction
						.getCategoryId());
				if (cat != null)
					categoryName = cat.getName();
			}

			oh.accounts.setText(accounts.toString());

			StringBuilder note = new StringBuilder();
			if (transaction.getNote() != null
					&& transaction.getNote().length() > 0)
				note.append(" (" + transaction.getNote() + ")");
			else
				note.append("");

			oh.note.setText(String.format("%s %s", categoryName,
					note.toString()));

			oh.value.setText(UnitConverter.doubleToCurrency(transaction
					.getValue()));

			oh.date.setText(UnitConverter.dateTimeString(transaction.getDate()));

			if (transaction.getAccMinus() > 0 && transaction.getAccPlus() > 0) {
				oh.value.setTextColor(context.getResources().getColor(
						R.color.yellow));
			} else if (transaction.getAccMinus() > 0)
				oh.value.setTextColor(context.getResources().getColor(
						R.color.darkRed));
			else if (transaction.getAccPlus() > 0)
				oh.value.setTextColor(context.getResources().getColor(
						R.color.darkGreen));
			oh.transaction = transaction;
		}

	}

	private void setDownloadView(View convertView) {
		ControlListRowView control = new ControlListRowView(context,
				convertView, actualControlState);

		controlView = control.getView();
	}

	public class TransactionAdapterObjectHandler {
		public BaseTransaction transaction;
		public TextView value;
		public TextView accounts;
		public TextView note;
		public TextView date;

	}

	public void getMoreTransactions() {
		actualControlState = ViewState.Downloading;
		// setDownloadView(controlView);
		this.notifyDataSetChanged();
	}

	public void clearItems() {
		items.clear();
		notifyDataSetChanged();
	}

}
