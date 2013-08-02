package pl.wppiotrek.wydatki.basepackage.fragments.edit;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

public class EditParameterFragment extends BaseEditItemFragment<Parameter> {

	private EditText etbx_name;
	private Switch switch_isActive;
	private EditText etbx_values;
	private EditText etbx_default_value;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.fragment_edit_parameter,
				null);
		linkViews(convertView);

		return convertView;
	}

	private void linkViews(View view) {
		etbx_name = (EditText) view.findViewById(R.id.etbx_name);
		switch_isActive = (Switch) view.findViewById(R.id.switch_active);

		etbx_values = (EditText) view.findViewById(R.id.etbx_values);
		etbx_default_value = (EditText) view
				.findViewById(R.id.etbx_default_value);

	}

	@Override
	public Parameter prepareToSave(Parameter item) {

		item.setName(etbx_name.getText().toString());
		item.setActive(switch_isActive.isChecked());

		return item;
	}

	@Override
	public boolean isValid(Parameter item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void itemToEdit(Parameter item) {
		etbx_name.setText(item.getName());

		switch_isActive.setChecked(item.isActive());
		etbx_default_value.setText(item.getDefaultValue());
		etbx_values.setText(item.getDataSource());

	}

	@Override
	protected ProviderType getProviderType() {
		return ProviderType.Parameters;
	}

	@Override
	public void configureNewItem(Parameter item) {
		// TODO Auto-generated method stub

	}

}
