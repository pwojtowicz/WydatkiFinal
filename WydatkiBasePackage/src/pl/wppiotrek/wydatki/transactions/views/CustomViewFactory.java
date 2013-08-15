package pl.wppiotrek.wydatki.transactions.views;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.InvokeTransactionParameter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class CustomViewFactory {

	public static View createOrFill(int viewType, Context context,
			InvokeTransactionParameter paramInstance, boolean fillOnly,
			View viewToFill) {

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = null;

		switch (viewType) {
		case ViewType.NUMBER:
		case ViewType.TEXT_BOX:
			if (view == null)
				view = layoutInflater.inflate(R.layout.invoke_action_text_box,
						null);
			InvokeEditTextView textBox = new InvokeEditTextView(context, view,
					paramInstance);
			return textBox.getView();
		case ViewType.CHECKBOX:
			if (view == null)
				view = layoutInflater.inflate(R.layout.invoke_action_check_box,
						null);
			InvokeCheckBoxView checkBox = new InvokeCheckBoxView(context, view,
					paramInstance);
			return checkBox.getView();

		case ViewType.DROP_DOWN_LIST:
			if (view == null)
				view = layoutInflater.inflate(
						R.layout.invoke_action_drop_down_list, null);
			InvokeDropDownView dropdown = new InvokeDropDownView(context, view,
					paramInstance);
			return dropdown.getView();

		default:
			break;
		}

		return null;
	}

}
