package pl.wppiotrek.wydatki.basepackage.fragments.edit;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

public class EditCategoryFragment extends BaseEditItemFragment<Category> {

	private EditText etbx_name;
	private Switch switch_isActive;
	private Switch switch_category_default_state;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.fragment_edit_category,
				null);
		linkViews(convertView);

		return convertView;
	}

	private void linkViews(View view) {
		etbx_name = (EditText) view.findViewById(R.id.etbx_name);
		switch_isActive = (Switch) view.findViewById(R.id.switch_active);

		switch_category_default_state = (Switch) view
				.findViewById(R.id.switch_category_default_state);
	}

	@Override
	public Category prepareToSave(Category item) {
		item.setName(etbx_name.getText().toString());
		item.setActive(switch_isActive.isChecked());
		return item;
	}

	@Override
	public boolean isValid(Category item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void itemToEdit(Category item) {
		etbx_name.setText(item.getName());

		switch_isActive.setChecked(item.isActive());
		switch_category_default_state.setChecked(item.isPositive());
	}

	@Override
	protected ProviderType getProviderType() {
		return ProviderType.Categories;
	}

	@Override
	public void configureNewItem(Category item) {
		// TODO Auto-generated method stub

	}

}
