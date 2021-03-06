package pl.wppiotrek.wydatki.basepackage.adapters;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListParameterAdapter.ParameterContainer;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.helpers.ParameterTypes;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListParameterAdapter extends
		ListViewWithSelectionAdapter<Parameter, ParameterContainer> {

	public ListParameterAdapter(Context context) {
		super(context, 0, R.layout.row_parameter_layout);
	}

	@Override
	protected ParameterContainer loadContentControlls(View convertView) {
		ParameterContainer container = new ParameterContainer();
		container.Lock = (ImageView) convertView
				.findViewById(R.id.row_parameter_lock);
		container.Name = (TextView) convertView
				.findViewById(R.id.row_parameter_name);
		container.Default = (TextView) convertView
				.findViewById(R.id.row_parameter_default);
		container.Type = (TextView) convertView
				.findViewById(R.id.row_parameter_type);
		container.Selection = (CheckBox) convertView
				.findViewById(R.id.row_cbx_selected);

		return container;
	}

	@Override
	protected void fillContentRow(View convertView, Parameter object,
			ParameterContainer controlContainer, int position,
			boolean isItemSelected) {
		if (!isSelectionEnabled)
			controlContainer.Selection.setVisibility(View.GONE);
		else {
			controlContainer.Selection.setVisibility(View.VISIBLE);
			controlContainer.Selection.setChecked(isItemSelected);
		}
		if (object.isActive())
			controlContainer.Lock.setVisibility(ImageView.GONE);
		else
			controlContainer.Lock.setVisibility(ImageView.VISIBLE);

		controlContainer.Name.setText(object.getName());

		controlContainer.Type.setText(ParameterTypes.getParameterName(object
				.getTypeId()));

		controlContainer.Default.setText(object.getDefaultValue());
	}

	class ParameterContainer {
		CheckBox Selection;
		TextView Name;
		TextView Default;
		TextView Type;
		ImageView Lock;
	}

}
