package pl.wppiotrek.wydatki.basepackage.fragments;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListAccountAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.interfaces.IBaseListCallback;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.EDownloadState;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListAccountFragment extends BaseSelectedListFragment<Account>
		implements IDownloadFromWebListener {

	private TextView tv_account_balance;
	private LinearLayout footer_selected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.fragment_list_accounts,
				null);

		linkViews(convertView);
		return convertView;
	}

	private void linkViews(View convertView) {
		listView = (ListView) convertView.findViewById(R.id.listView);
		tv_account_balance = (TextView) convertView
				.findViewById(R.id.tv_account_balance);
		footer_selected = (LinearLayout) convertView
				.findViewById(R.id.footer_list_selected_items_options);
		linkFooterActions(footer_selected);
	}

	protected boolean loadContentFromSingleton() {
		if (singleton.isContentLoadedAtStart()) {
			items = singleton.getAccounts();
			return true;
		}
		return false;
	}

	protected void reloadItemsAdapter() {
		if (adapter == null) {
			adapter = new ListAccountAdapter(getActivity(), isSelectionEnabled,
					this);
		}
		if (listView.getAdapter() == null)
			listView.setAdapter(adapter);

		double balance = 0.0;
		if (!items.isEmpty())
			for (Account item : items) {
				if (item.isSumInGlobalBalance())
					balance += item.getBalance();
			}

		adapter.clearList();
		adapter.addItems(items);
		adapter.updateList();

		tv_account_balance.setText(String.format("%.2f z³", balance));
	}

	@Override
	public void onDownloadChangeState(EDownloadState newState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownloadResult(Object response) {
		if (response instanceof ItemContainer<?>) {
			ItemContainer<Account> container = (ItemContainer<Account>) response;
			items = container.getItemsList();
			reloadItemsAdapter();

		}
	}

	@Override
	public void onListSelectionChanged(int numberOfSelectedItems) {
		if (numberOfSelectedItems > 0) {
			footer_selected.setVisibility(View.VISIBLE);
		} else {
			footer_selected.setVisibility(View.GONE);
		}
	}

	@Override
	protected ProviderType getProviderType() {
		return ProviderType.Accounts;
	}

	@Override
	protected void createNewItem(IBaseListCallback callbacks) {
		callbacks.onAddItemAction(new Account());
	}

}
