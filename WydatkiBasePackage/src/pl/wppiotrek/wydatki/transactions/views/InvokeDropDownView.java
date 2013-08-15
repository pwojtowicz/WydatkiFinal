package pl.wppiotrek.wydatki.transactions.views;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.InvokeTransactionParameter;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class InvokeDropDownView extends InvokeBaseView {

	private TextView label;
	private Spinner field;
	private int selectedPosition = -1;

	public InvokeDropDownView(Context context, View view,
			InvokeTransactionParameter content) {
		super(context, view, content);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void linkViews() {
		label = (TextView) view.findViewById(R.id.invoke_action_label);
		field = (Spinner) view.findViewById(R.id.invoke_action_dropdown_spiner);

		field.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				value = content.getDataSource()[position];
				saveValue();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				int p = 0;
			}
		});
	}

	@Override
	public void fillViews() {
		if (content != null) {
			if (label != null)
				label.setText(content.getName());
		}

	}

	@Override
	protected void clean() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataSource() {
		ArrayAdapter aa = new ArrayAdapter(context,
				android.R.layout.simple_spinner_item, content.getDataSource());

		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		field.setAdapter(aa);

		field.setSelection(0);
	}

	@Override
	public void setDefaultValue() {
		String[] values = content.getDataSource();
		if (content.getDefaultValue() != null)
			value = (String) content.getDefaultValue();
		if (content.getValue() != null)
			value = (String) content.getValue();

		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(value)) {
				field.setSelection(i);
				break;
			}
		}
	}

	@Override
	protected void saveValue() {
		save();
	}

	// public class CategorySpinnerAdapter extends SpinnerAdapter {
	//
	// public CategorySpinnerAdapter(Context context, int textViewResourceId,
	// SpinnerObject[] objects) {
	// super(context, textViewResourceId, objects);
	// // TODO Auto-generated constructor stub
	// }
	//
	// }

}
