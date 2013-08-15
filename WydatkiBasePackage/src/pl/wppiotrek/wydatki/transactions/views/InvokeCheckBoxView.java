package pl.wppiotrek.wydatki.transactions.views;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.InvokeTransactionParameter;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class InvokeCheckBoxView extends InvokeBaseView {

	private Switch field;

	public InvokeCheckBoxView(Context context, View view,
			InvokeTransactionParameter content) {
		super(context, view, content);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void linkViews() {
		field = (Switch) view.findViewById(R.id.invoke_action_switch);

		field.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				value = String.valueOf(isChecked);
				saveValue();
			}
		});
	}

	@Override
	public void fillViews() {
		if (content != null) {
			if (field != null)
				field.setText(content.getName());
		}

	}

	@Override
	protected void clean() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDataSource() {

	}

	@Override
	public void setDefaultValue() {
		boolean isChecked = content.getDefaultValue() != null ? Boolean
				.valueOf(content.getDefaultValue().toString()) : false;

		if (content.getValue() != null) {
			isChecked = Boolean.parseBoolean(content.getValue());
		}

		field.setChecked(isChecked);
	}

	@Override
	protected void saveValue() {
		save();
	}

}
