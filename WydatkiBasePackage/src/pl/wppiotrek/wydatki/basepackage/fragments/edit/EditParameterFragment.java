package pl.wppiotrek.wydatki.basepackage.fragments.edit;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.enums.ParameterDataType;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

public class EditParameterFragment extends BaseEditItemFragment<Parameter> {

	private EditText etbx_name;
	private Switch switch_isActive;
	private EditText etbx_values;
	private EditText etbx_default_value;
	private LinearLayout ll_parameter_datasource_list;
	private LinearLayout ll_parameter_datasource_default_item;
	private LinearLayout ll_parameter_datasource_detault_text;
	private LinearLayout ll_parameter_datasource_default_switch;
	private Spinner spinner_data_type;
	private Switch switch_default_value;
	private Spinner spinner_default_value;

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
		etbx_values.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				configureParameterListDefaultValue();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		etbx_default_value = (EditText) view
				.findViewById(R.id.etbx_default_value);

		switch_default_value = (Switch) view
				.findViewById(R.id.switch_default_value);

		spinner_data_type = (Spinner) view.findViewById(R.id.spinner_items);

		spinner_data_type
				.setOnItemSelectedListener(spinner_data_type_selected_listener);

		spinner_default_value = (Spinner) view
				.findViewById(R.id.spinner_default_value);
		ll_parameter_datasource_list = (LinearLayout) view
				.findViewById(R.id.ll_parameter_datasource_list);
		ll_parameter_datasource_default_item = (LinearLayout) view
				.findViewById(R.id.ll_parameter_datasource_default_item);
		ll_parameter_datasource_detault_text = (LinearLayout) view
				.findViewById(R.id.ll_parameter_datasource_detault_text);
		ll_parameter_datasource_default_switch = (LinearLayout) view
				.findViewById(R.id.ll_parameter_datasource_default_switch);

	}

	@Override
	public Parameter prepareToSave(Parameter item) {

		item.setName(etbx_name.getText().toString());
		item.setActive(switch_isActive.isChecked());

		switch (ParameterDataType.fromInteger(item.getTypeId())) {
		case Text:
		case Number:
			item.setDefaultValue(etbx_default_value.getText().toString());
			break;
		case List:
			item.setDataSource(etbx_values.getText().toString());
			if (spinner_default_value.getSelectedItemPosition() == 0)
				item.setDefaultValue("");
			else
				item.setDefaultValue((String) spinner_default_value
						.getSelectedItem());
			break;
		case ChoiseBoolean:
			if (switch_default_value.isChecked())
				item.setDefaultValue("True");
			else
				item.setDefaultValue("False");
			break;
		default:
			break;
		}

		return item;
	}

	@Override
	public boolean isValid(Parameter item) {

		if (item.getName().length() == 0)
			return false;

		if (item.getTypeId() == ParameterDataType
				.fromType(ParameterDataType.List) && etbx_values.length() == 0)
			return false;

		return true;
	}

	@Override
	protected ProviderType getProviderType() {
		return ProviderType.Parameters;
	}

	@Override
	public void configureItemView(Parameter item) {
		loadSpinnerItems();

		if (item.getId() > 0) {
			etbx_name.setText(item.getName());
			switch_isActive.setChecked(item.isActive());
			spinner_data_type.setSelection(item.getTypeId() - 1);
		}

	}

	private void loadSpinnerItems() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getActivity(), R.array.parameter_types,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_data_type.setAdapter(adapter);

	}

	OnItemSelectedListener spinner_data_type_selected_listener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			changeActualParameterType(pos + 1);
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	};
	private boolean isFirstChange = true;

	protected void changeActualParameterType(int pos) {
		getActualItem().setTypeId(pos);
		ll_parameter_datasource_list.setVisibility(View.GONE);
		ll_parameter_datasource_default_item.setVisibility(View.GONE);
		ll_parameter_datasource_detault_text.setVisibility(View.GONE);
		ll_parameter_datasource_default_switch.setVisibility(View.GONE);

		switch (ParameterDataType.fromInteger(pos)) {
		case Text:
			ll_parameter_datasource_detault_text.setVisibility(View.VISIBLE);
			break;
		case Number:
			ll_parameter_datasource_detault_text.setVisibility(View.VISIBLE);
			break;
		case List:
			ll_parameter_datasource_list.setVisibility(View.VISIBLE);
			ll_parameter_datasource_default_item.setVisibility(View.VISIBLE);
			break;
		case ChoiseBoolean:
			ll_parameter_datasource_default_switch.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		configureViewByItemParameterType();
	}

	private void configureViewByItemParameterType() {
		Parameter actual = getActualItem();

		if (isFirstChange) {
			isFirstChange = false;
		} else {
			actual.setDefaultValue("");
			actual.setDataSource("");
		}

		switch (ParameterDataType.fromInteger(actual.getTypeId())) {
		case Text:
			etbx_default_value.setText(actual.getDefaultValue());
			etbx_default_value.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case Number:
			etbx_default_value.setText(actual.getDefaultValue());
			etbx_default_value.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			break;
		case List:
			etbx_values.setText(actual.getDataSource());
			changeListDefaultValue();
			break;
		case ChoiseBoolean:
			if (actual.getDefaultValue() != null
					&& actual.getDefaultValue().equals("True"))
				switch_default_value.setChecked(true);
			else
				switch_default_value.setChecked(false);
			break;
		default:
			break;
		}
	}

	private void changeListDefaultValue() {
		String values = etbx_values.getText().toString();
		String items = "(nie wybrano);" + values;
		String[] valuesInAdapter = items.split(";");

		int pos = 0;
		String defaultValue = getActualItem().getDefaultValue();
		for (int i = 0; i < valuesInAdapter.length; i++) {
			if (valuesInAdapter[i].equals(defaultValue)) {
				pos = i;
				break;
			}
		}

		spinner_default_value.setSelection(pos);

	}

	protected void configureParameterListDefaultValue() {
		String items = "(nie wybrano);" + etbx_values.getText().toString();
		String[] values = items.split(";");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, values);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_default_value.setAdapter(adapter);
	}

}
