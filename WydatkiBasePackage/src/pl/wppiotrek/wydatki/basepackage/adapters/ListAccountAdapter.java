package pl.wppiotrek.wydatki.basepackage.adapters;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListAccountAdapter.AccountContainer;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.helpers.AccountImages;
import pl.wppiotrek.wydatki.basepackage.helpers.UnitConverter;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAccountAdapter extends
		ListViewWithSelectionAdapter<Account, AccountContainer> {

	public ListAccountAdapter(Context context, boolean isSelectionEnabled,
			IOnSelectionChangeListener changeStateListener) {
		super(context, 0, R.layout.row_account_layout, isSelectionEnabled,
				changeStateListener);
	}

	@Override
	protected AccountContainer loadContentControlls(View convertView) {
		AccountContainer container = new AccountContainer();
		container.Image = (ImageView) convertView
				.findViewById(R.id.row_account_image);
		container.Lock = (ImageView) convertView
				.findViewById(R.id.row_account_lock);
		container.Name = (TextView) convertView
				.findViewById(R.id.row_account_name);
		container.LastActionDate = (TextView) convertView
				.findViewById(R.id.row_account_last_action_date);
		container.Balance = (TextView) convertView
				.findViewById(R.id.row_account_balance);
		container.Selection = (CheckBox) convertView
				.findViewById(R.id.row_cbx_selected);
		if (!isSelectionEnabled)
			container.Selection.setVisibility(View.GONE);
		else
			container.Selection
					.setOnCheckedChangeListener(changeSelectionListener);
		return container;
	}

	@Override
	protected void fillContentRow(View convertView, Account object,
			AccountContainer controlContainer, int position) {

		controlContainer.Selection.setTag(object);
		controlContainer.Name.setText(object.getName());

		controlContainer.LastActionDate.setText(UnitConverter
				.convertDateTimeToString(object.getLastActionDate()));

		controlContainer.Balance.setText(UnitConverter.doubleToCurrency(object
				.getBalance()));

		if (object.isActive())
			controlContainer.Lock.setVisibility(ImageView.GONE);
		else
			controlContainer.Lock.setVisibility(ImageView.VISIBLE);

		if (object.getBalance() < 0)
			controlContainer.Balance.setTextColor(context.getResources()
					.getColor(R.color.darkRed));
		else if (object.getBalance() > 0)
			controlContainer.Balance.setTextColor(context.getResources()
					.getColor(R.color.darkGreen));

		controlContainer.Image.setImageDrawable(AccountImages
				.getImageForImageIndex(object.getImageIndex(), context));

	}

	class AccountContainer {
		CheckBox Selection;
		TextView Name;
		TextView LastActionDate;
		TextView Balance;
		ImageView Image;
		ImageView Lock;
	}
}
