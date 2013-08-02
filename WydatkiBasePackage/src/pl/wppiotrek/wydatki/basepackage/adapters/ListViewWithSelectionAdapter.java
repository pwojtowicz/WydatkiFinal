package pl.wppiotrek.wydatki.basepackage.adapters;

import java.util.HashSet;
import java.util.Set;

import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public abstract class ListViewWithSelectionAdapter<T, K> extends
		BaseSegmentListAdapter<T, K> {
	private IOnSelectionChangeListener fakeChangeStateListener = new IOnSelectionChangeListener() {

		@Override
		public void onListSelectionChanged(int numberOfSelectedItems) {

		}
	};
	protected IOnSelectionChangeListener changeStateListener = fakeChangeStateListener;
	protected Set<ModelBase> selectedItems = new HashSet<ModelBase>();

	protected OnCheckedChangeListener changeSelectionListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton view, boolean isChecked) {
			ModelBase item = (ModelBase) view.getTag();
			if (item != null) {
				if (isChecked) {
					selectedItems.add(item);
				} else {
					selectedItems.remove(item);
				}
				changeStateListener
						.onListSelectionChanged(selectedItems.size());
			}
		}
	};

	public interface IOnSelectionChangeListener {
		public void onListSelectionChanged(int numberOfSelectedItems);
	}

	protected boolean isSelectionEnabled;

	public ListViewWithSelectionAdapter(Context context, int headerResourceId,
			int rowResourceId, boolean isSelectionEnabled) {
		super(context, headerResourceId, rowResourceId);
		this.isSelectionEnabled = isSelectionEnabled;
	}

	public ListViewWithSelectionAdapter(Context context, int headerResourceId,
			int rowResourceId, boolean isSelectionEnabled,
			IOnSelectionChangeListener changeStateListener) {
		this(context, headerResourceId, rowResourceId, isSelectionEnabled);
		this.changeStateListener = changeStateListener;
	}

	public ModelBase[] getSelectedItems() {
		return selectedItems.toArray(new ModelBase[selectedItems.size()]);

	}

}
