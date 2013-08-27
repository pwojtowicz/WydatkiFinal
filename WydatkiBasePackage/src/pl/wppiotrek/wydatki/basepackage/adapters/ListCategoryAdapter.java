package pl.wppiotrek.wydatki.basepackage.adapters;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListCategoryAdapter.CategoryContainer;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListCategoryAdapter extends
		ListViewWithSelectionAdapter<Category, CategoryContainer> {

	public ListCategoryAdapter(Context context) {
		super(context, 0, R.layout.row_category_layout);
	}

	@Override
	protected CategoryContainer loadContentControlls(View convertView) {
		CategoryContainer container = new CategoryContainer();
		container.Lock = (ImageView) convertView
				.findViewById(R.id.row_category_lock);
		container.Name = (TextView) convertView
				.findViewById(R.id.row_category_name);
		container.Selection = (CheckBox) convertView
				.findViewById(R.id.row_cbx_selected);

		return container;
	}

	@Override
	protected void fillContentRow(View convertView, Category object,
			CategoryContainer controlContainer, int position,
			boolean isItemSelected) {
		if (!isSelectionEnabled)
			controlContainer.Selection.setVisibility(View.GONE);
		else {
			controlContainer.Selection.setVisibility(View.VISIBLE);
			controlContainer.Selection.setChecked(isItemSelected);
		}
		controlContainer.Name.setText(object.getName());

		if (object.isActive())
			controlContainer.Lock.setVisibility(ImageView.GONE);
		else
			controlContainer.Lock.setVisibility(ImageView.VISIBLE);
	}

	class CategoryContainer {
		CheckBox Selection;
		TextView Name;
		ImageView Lock;
	}

}
