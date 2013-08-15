package pl.wppiotrek.wydatki.transactions.views;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.InvokeTransactionParameter;
import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class InvokeEditTextView extends InvokeBaseView {

	private TextView label;
	private EditText field;

	public InvokeEditTextView(Context context, View view,
			InvokeTransactionParameter content) {
		super(context, view, content);
	}

	@Override
	public void linkViews() {
		label = (TextView) view.findViewById(R.id.invoke_action_label);
		field = (EditText) view.findViewById(R.id.invoke_action_text_edittext);
		field.setSingleLine();

		field.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View view, boolean hasFocus) {
				System.out.println("field onFocusChange:"
						+ String.valueOf(hasFocus));
				if (!hasFocus) {
					content.setHasFocus(false);
					saveValue();
				} else
					content.setHasFocus(true);
			}
		});
		field.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText et = (EditText) v;
				et.clearFocus();
				et.requestFocus();
			}
		});

		if (content.hasFocus())
			field.requestFocus();
	}

	@Override
	protected void saveValue() {
		value = field.getText().toString();
		save();

	}

	@Override
	public void fillViews() {
		if (content.getTypeId() == ViewType.NUMBER) {
			field.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		} else {
			field.setInputType(InputType.TYPE_CLASS_TEXT);
		}

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

	}

	@Override
	public void setDefaultValue() {
		if (content.getDefaultValue() != null)
			value = (String) content.getDefaultValue();

		if (content.getValue() != null)
			value = content.getValue();
		field.setText(value);
	}

}
