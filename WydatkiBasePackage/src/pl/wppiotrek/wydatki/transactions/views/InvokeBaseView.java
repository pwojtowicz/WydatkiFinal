package pl.wppiotrek.wydatki.transactions.views;

import pl.wppiotrek.wydatki.basepackage.entities.InvokeTransactionParameter;
import android.content.Context;
import android.view.View;

public abstract class InvokeBaseView {

	protected View view;
	protected String value;
	protected Context context;
	protected InvokeTransactionParameter content;

	public InvokeBaseView(Context context, View view,
			InvokeTransactionParameter content) {
		this.view = view;
		this.context = context;
		this.content = content;
	}

	public View getView() {
		if (view != null) {
			manageView();
		}
		return view;
	}

	protected void manageView() {
		this.linkViews();
		this.clean();
		this.fillViews();
		if (content != null) {
			if (content.getDataSource() != null)
				this.setDataSource();

			this.setDefaultValue();
		}
		this.saveValue();
	}

	protected void save() {
		content.setValueAndNotify(value);
	}

	public abstract void linkViews();

	public abstract void fillViews();

	public abstract void setDataSource();

	public abstract void setDefaultValue();

	protected abstract void saveValue();

	protected abstract void clean();
}
