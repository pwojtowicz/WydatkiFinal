package pl.wppiotrek.wydatki.basepackage.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import pl.wppiotrek.wydatki.basepackage.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class BaseSegmentListAdapter<T, K> extends BaseAdapter {

	private static final int MAX_TYPE_COUNT = 3;

	private LayoutInflater inflater;
	private int rowResourceId;
	private int headerResourceId;
	private int rowNoDataResourceId = R.layout.row_single_text_layout;
	private int rowNoDataTextResourceId = R.string.list_no_content;
	protected Context context;

	public final ArrayList<Object> elements = new ArrayList<Object>();
	private final HashMap<Integer, Integer> elementsType = new HashMap<Integer, Integer>();

	public BaseSegmentListAdapter(Context context, int headerResourceId,
			int rowResourceId) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.rowResourceId = rowResourceId;
		this.headerResourceId = headerResourceId;

		clearList();

	}

	public void clearList() {
		elements.clear();
		elementsType.clear();
	}

	public void updateList() {
		if (this.elements == null || this.elements.size() == 0)
			addNoData();
		this.notifyDataSetChanged();
	}

	public void addItem(final T item) {
		elements.add(item);
		elementsType.put(elements.size() - 1, ERowTypes.ROW_CONTENT);
	}

	public void addItems(ArrayList<T> eventsSt) {
		for (T t : eventsSt) {
			addItem(t);
		}

	}

	public void addHeader(final String header) {
		elements.add(header);
		elementsType.put(elements.size() - 1, ERowTypes.ROW_HEADER);
	}

	private void addNoData() {
		elements.add(context.getString(rowNoDataTextResourceId));
		elementsType.put(elements.size() - 1, ERowTypes.ROW_NO_DATA);
	}

	@Override
	public int getViewTypeCount() {
		return MAX_TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {
		return elementsType.get(position);
	}

	@Override
	public int getCount() {
		return elements.size();

	}

	@Override
	public Object getItem(int position) {
		return elements.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		int actualRowType = getItemViewType(position);

		if (convertView == null)
			switch (actualRowType) {
			case ERowTypes.ROW_NO_DATA:
				convertView = inflater.inflate(rowNoDataResourceId, null);
				break;
			case ERowTypes.ROW_HEADER:
				convertView = inflater.inflate(headerResourceId, null);
				break;
			case ERowTypes.ROW_CONTENT:
				convertView = inflater.inflate(rowResourceId, null);
				convertView.setTag(loadContentControlls(convertView));
				break;
			}

		Object object = getItem(position);
		if (actualRowType == ERowTypes.ROW_CONTENT) {

			fillContentRow(convertView, (T) object, (K) convertView.getTag(),
					position);
		} else if (actualRowType == ERowTypes.ROW_HEADER) {
			fillHeaderRow(convertView, (String) object, position);
		} else if (actualRowType == ERowTypes.ROW_NO_DATA) {
			fillNoDataRow(convertView, (String) object, position);
		}
		return convertView;
	}

	private void fillNoDataRow(View convertView, String object, int position) {
		((TextView) convertView.findViewById(R.id.tv_text)).setText(object);
	}

	protected abstract K loadContentControlls(View convertView);

	protected abstract void fillContentRow(View convertView, T object,
			K controlContainer, int position);

	public void fillHeaderRow(View convertView, String object, int position) {
		TextView tv = (TextView) convertView.findViewById(R.id.tv_text);
		tv.setText(object);
	}

	public class ERowTypes {
		public static final int ROW_NO_DATA = 0;
		public static final int ROW_HEADER = 1;
		public static final int ROW_CONTENT = 2;
		public static final int ROW_DOWNLOAD_MORE = 3;
		public static final int ROW_TRY_AGAIN = 4;
		public static final int ROW_DOWNLOADING = 5;
	}
}
