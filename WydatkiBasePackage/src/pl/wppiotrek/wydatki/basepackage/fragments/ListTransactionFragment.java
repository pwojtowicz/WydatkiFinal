package pl.wppiotrek.wydatki.basepackage.fragments;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.BaseSegmentListAdapter.ERowTypes;
import pl.wppiotrek.wydatki.basepackage.adapters.TransactionListAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.TransactionFilter;
import pl.wppiotrek.wydatki.basepackage.enums.ViewState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyncTaskDownloadContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.EDownloadState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.TaskParameters;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListTransactionFragment extends Fragment implements
		IDownloadFromWebListener {

	private static final int REQUEST_CODE_FILTER = 123;

	private ListView listView;
	private Account currentAccount;
	private TransactionFilter filter = null;
	private RelativeLayout rl_filter_active_layout;
	private AlertDialog dialog;
	private AsyncTaskDownloadContent task;
	private TransactionListAdapter adapter;
	private TextView tv_loaded_count;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(
				R.layout.fragment_list_transactions, null);
		linkViews(convertView);
		setHasOptionsMenu(true);
		return convertView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.transaction_list, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_filter) {
			Intent intent = new Intent(
					"pl.wppiotrek.wydatki.v2.TransactionFilterActivity");
			intent.putExtra(
					TransactionFilterFragment.BUNDLE_TRANSACTION_FILTER, filter);
			startActivityForResult(intent, REQUEST_CODE_FILTER);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void linkViews(View convertView) {
		listView = (ListView) convertView.findViewById(R.id.listView);

		listView.setOnItemClickListener(listViewItemClickListener);
		rl_filter_active_layout = (RelativeLayout) convertView
				.findViewById(R.id.rl_filter_active_layout);
		tv_loaded_count = (TextView) convertView
				.findViewById(R.id.tv_transaction_loaded_count);
		tv_loaded_count.setVisibility(View.GONE);
		Button btn_clearFilter = (Button) convertView
				.findViewById(R.id.btn_transaction_list_clear_filter);
		btn_clearFilter.setOnClickListener(clearFilterClickListener);
	}

	OnClickListener clearFilterClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			filter.setCategoryId(null);
			filter.setDateFrom(null);
			filter.setDateTo(null);
			filter.setNote(null);
			filter.setValueFrom(null);
			filter.setValueTo(null);
			configureViews(true);
		}
	};

	OnItemClickListener listViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> a, View view, int position,
				long arg3) {
			if (adapter.getViewTypeCount() == ERowTypes.ROW_CONTENT)
				onListItemClick(adapter.getItem(position));
			else
				onListItemClick(adapter.getActualViewState());
		}
	};

	private ArrayList<BaseTransaction> loadedItems = new ArrayList<BaseTransaction>();

	public void setAccount(Account account) {
		this.currentAccount = account;
	}

	public TransactionFilter getFilter() {
		return filter;
	}

	public void updateFilter(TransactionFilter newFilter) {
		this.filter = newFilter;
		configureViews(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		configureViews(false);
	}

	private void configureViews(boolean reloadItems) {
		if (filter == null) {
			filter = new TransactionFilter();
			if (currentAccount != null)
				filter.setAccountId(currentAccount.getId());
		}

		if (filter.isExtendFiltering())
			rl_filter_active_layout.setVisibility(View.VISIBLE);
		else
			rl_filter_active_layout.setVisibility(View.GONE);

		reloadTransactions(reloadItems);
	}

	private void reloadTransactions(boolean reloadItems) {
		if (reloadItems && adapter != null) {
			adapter.clearItems();
			loadedItems = new ArrayList<BaseTransaction>();
		}
		downloadTransactions();
	}

	protected void downloadTransactions() {
		if (task == null) {
			task = new AsyncTaskDownloadContent(this);

			TaskParameters param = new TaskParameters(filter);
			task.execute(param);
		}
	}

	@Override
	public void onDownloadChangeState(EDownloadState newState) {
		switch (newState) {
		case Begin:
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Pobieranie danych");
			dialog.setCancelable(false);
			dialog.show();
			break;
		case End:
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}
			task = null;
			break;
		case Fail:
			Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("B³¹d");
			builder.setMessage("B³¹d pobierania danych");
			builder.setNegativeButton("OK", null);
			builder.setCancelable(false);
			dialog = builder.create();
			dialog.show();
			setAdapterAsDownloadAgain();
			break;
		case Success:

			break;
		}
	}

	@Override
	public void onDownloadResult(Object response) {
		ItemContainer<BaseTransaction> container = (ItemContainer<BaseTransaction>) response;

		if (adapter == null) {
			adapter = new TransactionListAdapter(getActivity());
			listView.setAdapter(adapter);
		}
		{
			for (BaseTransaction item : container.getItemsList()) {
				adapter.addItem(item);
				this.loadedItems.add(item);
			}
			if (container.getAllItemsCount() > this.loadedItems.size())
				adapter.addDownloadMoreItem(true, filter.getTakeCount());
			else
				adapter.removeControlView();
		}
		adapter.notifyDataSetChanged();

		if (this.loadedItems.size() > 0) {
			tv_loaded_count.setVisibility(View.VISIBLE);
			tv_loaded_count.setText(String.format("Wczytano %d z %d",
					this.loadedItems.size(), container.getAllItemsCount()));
		} else
			tv_loaded_count.setVisibility(View.GONE);
	}

	protected void onListItemClick(Object item) {
		if (item instanceof BaseTransaction) {

		} else if (item instanceof ViewState) {
			ViewState vs = (ViewState) item;
			switch (vs) {
			case NoObjects:
			case Downloading:
				break;
			case DownloadMore:
				downloadMore();
				break;
			case DownloadException:
				downloadAgain();
				break;

			}
		}
	}

	private void downloadMore() {
		filter.setStartAt(filter.getStartAt() + filter.getTakeCount());
		adapter.setViewStateAsDownloading();
		downloadTransactions();
	}

	private void setAdapterAsDownloadAgain() {
		if (adapter != null)
			adapter.setViewStateAsDownloadAgain();
	}

	private void downloadAgain() {
		downloadTransactions();
	}

}
