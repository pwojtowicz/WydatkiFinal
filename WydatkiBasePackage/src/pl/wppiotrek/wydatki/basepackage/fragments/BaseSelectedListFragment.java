package pl.wppiotrek.wydatki.basepackage.fragments;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListViewWithSelectionAdapter;
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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public abstract class BaseSelectedListFragment<T> extends Fragment implements
		IDownloadFromWebListener, OnItemLongClickListener, OnItemClickListener {

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

		if (listView != null) {
			listView.setOnItemLongClickListener(this);
			listView.setOnItemClickListener(this);
		}

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

	private AsyncTaskDownloadContent upadteContentTask;

	private ActionMode mActionMode;

	private Callback multiSelectionItemListener = new Callback() {

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
			adapter.setIsSelectionEnabled(false);
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView.clearChoices();
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {

			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.menu_cab_object_list, menu);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.action_lock:
				changeActiveState(getSelectedItems(), false);
				return true;
			case R.id.action_unlock:
				changeActiveState(getSelectedItems(), true);
				return true;

			default:
				break;
			}
			return false;
		}

	};

	private ModelBase[] itemsToUpdate;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_refresh, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	protected void changeActiveState(ArrayList<ModelBase> selectedItems,
			boolean isActive) {
		int size = selectedItems.size();
		ModelBase[] items = new ModelBase[size];
		for (int i = 0; i < size; i++) {
			ModelBase mb = selectedItems.get(i);
			mb.setActive(isActive);
			items[i] = mb.cloneBase();
		}
		this.itemsToUpdate = items;

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add_new_item) {
			createNewItem(mCallbacks);
			return true;
		} else if (id == R.id.action_refresh) {
			refresh();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void refresh() {
		loadItemsContent();
	}

	protected abstract void createNewItem(IBaseListCallback callbacks);

	protected abstract ProviderType getProviderType();

	protected ArrayList<ModelBase> getSelectedItems() {
		if (adapter != null) {
			ArrayList<ModelBase> items = new ArrayList<ModelBase>();
			SparseBooleanArray selectedItems = listView
					.getCheckedItemPositions();
			int count = selectedItems.size();
			for (int position = 0; position < count; position++) {
				if (selectedItems.valueAt(position)) {
					items.add((ModelBase) adapter.getItem(selectedItems
							.keyAt(position)));
				}
			}
			return items;
		}
		return new ArrayList<ModelBase>();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long itemId) {
		if (mActionMode != null) {
			if (listView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
				reloadActionMode();
			}
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
			int position, long itemId) {
		VibratorSupport.vibrate(50);
		if (mActionMode != null)
			return true;
		if (listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE) {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			listView.clearChoices();
			mActionMode = getActivity().startActionMode(
					multiSelectionItemListener);
			listView.setItemChecked(position, true);
			adapter.setIsSelectionEnabled(true);
			reloadActionMode();
			return true;
		}
		return false;
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

	public void onNoLongerVisibled() {
		if (mActionMode != null) {
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView.clearChoices();
			mActionMode.finish();
			mActionMode = null;
			adapter.setIsSelectionEnabled(false);
			adapter.notifyDataSetChanged();
		}
	}

}
