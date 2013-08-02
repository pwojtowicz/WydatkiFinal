package pl.wppiotrek.wydatki.basepackage.fragments;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListCategoryAdapter;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
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

public class ListCategoryFragment extends BaseSelectedListFragment<Category>
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
		footer_selected = (LinearLayout) convertView
				.findViewById(R.id.footer_list_selected_items_options);
		linkFooterActions(footer_selected);
	}

	protected boolean loadContentFromSingleton() {
		if (singleton.isContentLoadedAtStart()) {
			items = singleton.getCategories();
			return true;
		}
		return false;
	}

	protected void reloadItemsAdapter() {
		if (adapter == null) {
			adapter = new ListCategoryAdapter(getActivity(),
					isSelectionEnabled, this);
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
			ItemContainer<Category> container = (ItemContainer<Category>) response;
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
		return ProviderType.Categories;
	}

	@Override
	protected void createNewItem(IBaseListCallback callbacks) {
		callbacks.onAddItemAction(new Category());
	}
}
