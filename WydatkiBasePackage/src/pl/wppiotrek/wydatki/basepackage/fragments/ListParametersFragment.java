package pl.wppiotrek.wydatki.basepackage.fragments;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListParameterAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.ItemContainer;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
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

public class ListParametersFragment extends BaseSelectedListFragment<Parameter>
		implements IDownloadFromWebListener {

	private LinearLayout footer_selected;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.fragment_list_view, null);

		linkViews(convertView);
		return convertView;
	}

	private void linkViews(View convertView) {
		listView = (ListView) convertView.findViewById(R.id.listView);

	}

	protected boolean loadContentFromSingleton() {
		if (singleton.isContentLoadedAtStart()) {
			items = singleton.getParameters();
			return true;
		}
		return false;
	}

	protected void reloadItemsAdapter() {
		if (adapter == null) {
			adapter = new ListParameterAdapter(getActivity());
		}
		if (listView.getAdapter() == null)
			listView.setAdapter(adapter);

		adapter.clearList();
		adapter.addItems(items);
		adapter.updateList();

	}

	@Override
	public void onDownloadChangeState(EDownloadState newState) {

	}

	@Override
	public void onDownloadResult(Object response) {
		if (response instanceof ItemContainer<?>) {
			ItemContainer<Parameter> container = (ItemContainer<Parameter>) response;
			items = container.getItemsList();
			reloadItemsAdapter();
		}

	}

	@Override
	protected ProviderType getProviderType() {
		return ProviderType.Parameters;
	}

	@Override
	protected void createNewItem(IBaseListCallback callbacks) {
		callbacks.onAddItemAction(new Parameter());
	}

}
