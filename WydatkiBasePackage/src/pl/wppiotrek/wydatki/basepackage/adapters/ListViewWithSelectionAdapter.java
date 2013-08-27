package pl.wppiotrek.wydatki.basepackage.adapters;

import android.content.Context;

public abstract class ListViewWithSelectionAdapter<T, K> extends
		BaseSegmentListAdapter<T, K> {

	protected boolean isSelectionEnabled;

	public ListViewWithSelectionAdapter(Context context, int headerResourceId,
			int rowResourceId) {
		super(context, headerResourceId, rowResourceId);
	}

	public void setIsSelectionEnabled(boolean isSelectionEnabled) {
		this.isSelectionEnabled = isSelectionEnabled;
		notifyDataSetChanged();
	}

}
