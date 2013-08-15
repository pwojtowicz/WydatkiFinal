package pl.wppiotrek.wydatki.basepackage.fragments;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListViewWithSelectionAdapter;
import pl.wppiotrek.wydatki.basepackage.adapters.ListViewWithSelectionAdapter.IOnSelectionChangeListener;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.enums.OperationType;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.interfaces.IBaseListCallback;
import pl.wppiotrek.wydatki.basepackage.singletons.SingletonLoadedWebContent;
import pl.wppiotrek.wydatki.basepackage.support.VibratorSupport;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.AsyncTaskDownloadContent;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.IDownloadFromWebListener;
import pl.wppiotrek.wydatki.basepackage.webacynctasks.TaskParameters;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public abstract class BaseSelectedListFragment<T> extends Fragment implements
		IOnSelectionChangeListener, IDownloadFromWebListener,
		OnItemLongClickListener {

	SingletonLoadedWebContent singleton = SingletonLoadedWebContent
			.getInstance();

	public static String BUNDLE_LIST_CAN_BE_SELECTED = "is_selection_enabled";
	public static String BUNDLE_LIST_LONG_CLICK_ENABLED = "is_long_click_enabled";

	protected boolean isSelectionEnabled = false;
	protected ListViewWithSelectionAdapter adapter;
	protected ListView listView;

	protected ArrayList<T> items;
	protected AsyncTaskDownloadContent downloadContentTask;

	protected IBaseListCallback mCallbacks;

	private IBaseListCallback sCallbacks = new IBaseListCallback() {

		@Override
		public void onAddItemAction(ModelBase item) {

		}
	};

	private boolean isLongClickEnabled;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof IBaseListCallback)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (IBaseListCallback) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sCallbacks;
	}

	@Override
	public void onResume() {
		super.onResume();

		if (listView != null)
			listView.setOnItemLongClickListener(this);

		reloadListView();
	}

	private void reloadListView() {

		if (loadContentFromSingleton()) {
			reloadItemsAdapter();
			return;
		}
		loadItemsContent();

	}

	protected abstract void reloadItemsAdapter();

	protected void loadItemsContent() {
		if (downloadContentTask != null) {
			downloadContentTask.cancel(true);
			downloadContentTask = null;
		}
		downloadContentTask = new AsyncTaskDownloadContent(this);
		downloadContentTask.execute(new TaskParameters(getProviderType(),
				OperationType.GetAll));
	}

	protected abstract boolean loadContentFromSingleton();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		Bundle arguments = getArguments();
		if (arguments != null) {
			isSelectionEnabled = arguments.getBoolean(
					BUNDLE_LIST_CAN_BE_SELECTED, false);
			isLongClickEnabled = arguments.getBoolean(
					BUNDLE_LIST_LONG_CLICK_ENABLED, false);
		}
	}

	protected OnClickListener lockSelectedItems = new OnClickListener() {

		@Override
		public void onClick(View v) {
			boolean newState = false;
			if (v == btn_lock)
				newState = false;
			else if (v == btn_unlock)
				newState = true;

			ModelBase[] items = getSelectedItems(newState);
			changeItemsActivityStates(items);
		}
	};
	private AsyncTaskDownloadContent upadteContentTask;

	private ImageButton btn_lock;
	private ImageButton btn_unlock;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add_new_item) {
			createNewItem(mCallbacks);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected abstract void createNewItem(IBaseListCallback callbacks);

	protected void changeItemsActivityStates(ModelBase[] items) {
		if (items != null && items.length > 0) {
			if (upadteContentTask != null) {
				upadteContentTask.cancel(true);
				upadteContentTask = null;
			}
			upadteContentTask = new AsyncTaskDownloadContent(this);
			upadteContentTask.execute(new TaskParameters(getProviderType(),
					OperationType.ChangeActiveState, items));
		}

	}

	protected abstract ProviderType getProviderType();

	protected ModelBase[] getSelectedItems(boolean newState) {
		if (adapter != null) {
			ModelBase[] returnItems = adapter.getSelectedItems();

			ModelBase[] items = new ModelBase[returnItems.length];
			for (int i = 0; i < returnItems.length; i++) {
				ModelBase it = returnItems[i];

				ModelBase item = new ModelBase();
				item.setId(it.getId());
				item.setActive(newState);
				items[i] = item;
			}
			return items;
		}
		return null;
	}

	protected void linkFooterActions(View footer_selected) {
		footer_selected.setVisibility(View.GONE);

		if (footer_selected != null) {
			btn_lock = (ImageButton) footer_selected
					.findViewById(R.id.ibtn_lock);
			btn_lock.setOnClickListener(lockSelectedItems);

			btn_unlock = (ImageButton) footer_selected
					.findViewById(R.id.ibtn_unlock);
			btn_unlock.setOnClickListener(lockSelectedItems);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long arg3) {
		VibratorSupport.vibrate(50);
		ModelBase item = (ModelBase) adapter.getItem(position);

		mCallbacks.onAddItemAction(item);

		return false;
	}

}
