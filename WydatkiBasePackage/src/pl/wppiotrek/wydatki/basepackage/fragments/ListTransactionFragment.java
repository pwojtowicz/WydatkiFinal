package pl.wppiotrek.wydatki.basepackage.fragments;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.activities.TransactionDetailsActivity;
import pl.wppiotrek.wydatki.basepackage.activities.TransactionFilterActivity;
import pl.wppiotrek.wydatki.basepackage.adapters.BaseSegmentListAdapter.ERowTypes;
import pl.wppiotrek.wydatki.basepackage.adapters.TransactionListAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;
import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.TransactionFilter;
import pl.wppiotrek.wydatki.basepackage.enums.ViewState;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.basepackage.support.VibratorSupport;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyncTaskDownloadContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.EDownloadState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.TaskParameters;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
		inflater.inflate(R.menu.menu_refresh, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_filter) {
			showFilterActivity();
			return true;
		} else if (id == R.id.action_refresh) {
			refresh();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh() {
		// TODO Auto-generated method stub

	}

	private void linkViews(View convertView) {
		listView = (ListView) convertView.findViewById(R.id.listView);

		listView.setOnItemClickListener(listViewItemClickListener);
		listView.setOnItemLongClickListener(listViewLongItemClickListener);
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

	protected ActionMode mActionMode;

	OnItemLongClickListener listViewLongItemClickListener = new OnItemLongClickListener() {

		private Callback callbackActionMode = new Callback() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				if (listView.getCheckedItemCount() > 1)
					menu.findItem(R.id.action_edit).setVisible(false);
				else
					menu.findItem(R.id.action_edit).setVisible(true);
				return true;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				mActionMode = null;
				adapter.setSelectable(false);
				listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				listView.clearChoices();
			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.menu_trabsaction_cab, menu);
				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				return false;
			}
		};

		@Override
		public boolean onItemLongClick(AdapterView<?> a, View view,
				int position, long arg3) {
			VibratorSupport.vibrate(50);
			if (mActionMode != null) {
				return true;
			}

			if (adapter.getItemViewType(position) == ERowTypes.ROW_CONTENT) {
				if (listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
					listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
					listView.clearChoices();
					mActionMode = getActivity().startActionMode(
							callbackActionMode);
					adapter.setSelectable(true);
					listView.setItemChecked(position, true);
					reloadActionMode();
				}

				return true;
			}
			return false;
		}

	};

	OnItemClickListener listViewItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> a, View view, int position,
				long arg3) {

			if (adapter.getItemViewType(position) == ERowTypes.ROW_CONTENT)
				if (listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
					reloadActionMode();
				} else
					onListItemClick(adapter.getItem(position));
			else {
				onListItemClick(adapter.getActualViewState());
				if (listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE)
					listView.setItemChecked(position, false);
			}

		}
	};

	private ArrayList<BaseTransaction> loadedItems = new ArrayList<BaseTransaction>();

	public void setAccount(Account account) {
		this.currentAccount = account;
	}

	protected void reloadActionMode() {
		if (mActionMode == null)
			return;
		int count = listView.getCheckedItemCount();
		if (count > 0) {
			mActionMode.setTitle("Zaznaczone " + count);
			mActionMode.invalidate();
		} else {
			mActionMode.finish();
			mActionMode = null;
		}
		adapter.notifyDataSetChanged();
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
			if (filter.getAccountId() == null)
				SingletonLoadedWebContent.getInstance().clearTransactions();
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

		if (container.getAllItemsCount() == 0)
			adapter.setViewStateAsNoContent();

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
			Intent intent = new Intent(getActivity(),
					TransactionDetailsActivity.class);
			intent.putExtra(TransactionDetailsActivity.BUNDLE_TRANSACTION,
					(BaseTransaction) item);
			startActivity(intent);

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

	protected void showFilterActivity() {
		Intent intent = new Intent(getActivity(),
				TransactionFilterActivity.class);
		intent.putExtra(TransactionFilterFragment.BUNDLE_TRANSACTION_FILTER,
				filter);
		startActivityForResult(intent, REQUEST_CODE_FILTER);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_FILTER) {
			if (resultCode == Activity.RESULT_OK && data.getExtras() != null) {
				TransactionFilter filter = (TransactionFilter) data
						.getExtras()
						.getSerializable(
								TransactionFilterFragment.BUNDLE_TRANSACTION_FILTER);
				if (filter != null)
					updateFilter(filter);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		reloadNewTransactions();
		reloadDeletedTransactions();
	}

	private void reloadDeletedTransactions() {
		ArrayList<BaseTransaction> deletedTransactions = SingletonLoadedWebContent
				.getInstance().getDeletedTransactions();
		if (deletedTransactions.size() > 0 && adapter != null) {
			for (BaseTransaction baseTransaction : deletedTransactions) {
				adapter.removeItem(baseTransaction);
			}
			adapter.notifyDataSetChanged();
		}
		SingletonLoadedWebContent.getInstance().clearDeletedTransactions();

	}

	private void reloadNewTransactions() {
		if (filter.getAccountId() == null) {
			ArrayList<BaseTransaction> newTransactions = SingletonLoadedWebContent
					.getInstance().getTransactions();
			if (newTransactions.size() > 0 && adapter != null) {
				for (BaseTransaction baseTransaction : newTransactions) {
					adapter.addItemAsStart(baseTransaction);
					this.loadedItems.add(baseTransaction);
				}
				adapter.notifyDataSetChanged();
			}
			SingletonLoadedWebContent.getInstance().clearTransactions();
		}

	}

	public void onNoLongerVisibled() {
		if (mActionMode != null) {
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView.clearChoices();
			mActionMode.finish();
			mActionMode = null;
			adapter.setSelectable(false);
			adapter.notifyDataSetChanged();
		}

	}

}
