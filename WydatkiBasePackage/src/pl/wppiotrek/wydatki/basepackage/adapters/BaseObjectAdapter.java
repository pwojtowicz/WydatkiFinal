package pl.wppiotrek.wydatki.basepackage.adapters;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public abstract class BaseObjectAdapter<T> extends BaseAdapter {

	protected ArrayList<T> items;
	protected Context context;
	protected LayoutInflater inflater;
	protected Boolean isCheckable = false;
	protected IOnAdapterCheckboxClick listener;
	protected ArrayList<Integer> selectedItemsId = new ArrayList<Integer>();

	public BaseObjectAdapter(Context context, ArrayList<T> items,
			IOnAdapterCheckboxClick listener) {
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = listener;
		if (this.listener != null)
			isCheckable = true;
	}

	public BaseObjectAdapter(Context context, ArrayList<T> items,
			IOnAdapterCheckboxClick listener, String selectedItems) {
		this(context, items, listener);
		// if (selectedItems != null)
		// selectedItemsId = ListSupport
		// .StringToIntegerArrayList(selectedItems);
	}

	public void reloadItems(ArrayList<T> list) {
		this.items = list;
		this.notifyDataSetChanged();
	}

	public void addItem(T item) {
		items.add(item);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	protected void setCheckableViewState(View convertView, ModelBase item) {
		CheckBox cbx_selected = (CheckBox) convertView
				.findViewById(R.id.row_cbx_selected);

		cbx_selected.setTag(item.getId());
		cbx_selected.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				int objectId = (Integer) view.getTag();
				if (((CheckBox) view).isChecked())
					selectedItemsId.add(objectId);
				else
					selectedItemsId.remove(selectedItemsId.indexOf(objectId));

				if (listener != null)
					listener.OnCheckBoxSelected(selectedItemsId.size());

			}
		});

		if (isCheckable)
			cbx_selected.setVisibility(CheckBox.VISIBLE);
		else
			cbx_selected.setVisibility(CheckBox.GONE);

		if (selectedItemsId.contains(item.getId()))
			cbx_selected.setChecked(true);
		else
			cbx_selected.setChecked(false);
	}

	@Override
	public Object getItem(int index) {
		return items.get(index);
	}

	@Override
	public long getItemId(int index) {
		return ((ModelBase) items.get(index)).getId();
	}

	public ArrayList<Integer> getSelectedItemsList() {
		return selectedItemsId;
	}

	public void setCheckableState(IOnAdapterCheckboxClick listener,
			boolean isCheckable) {
		if (!isCheckable)
			selectedItemsId = new ArrayList<Integer>();
		this.listener = listener;
		this.isCheckable = isCheckable;
		this.notifyDataSetChanged();
	}

	public interface IOnAdapterCheckboxClick {

		void OnCheckBoxSelected(int size);

	}

}
