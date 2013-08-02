package pl.wppiotrek.wydatki.basepackage.fragments.edit;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import pl.wppiotrek.wydatki.basepackage.helpers.UnitConverter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

public class EditAccountFragment extends BaseEditItemFragment<Account> {

	private EditText etbx_name;
	private Switch switch_isActive;
	private Switch switch_inSum;
	private Switch switch_isVisible;
	private EditText etbx_balance;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.fragment_edit_account,
				null);

		linkViews(convertView);

		return convertView;
	}

	private void linkViews(View view) {
		etbx_name = (EditText) view.findViewById(R.id.etbx_name);
		etbx_balance = (EditText) view.findViewById(R.id.etbx_balance);
		switch_isActive = (Switch) view.findViewById(R.id.switch_active);
		switch_inSum = (Switch) view
				.findViewById(R.id.switch_sum_in_global_balance);
		switch_isVisible = (Switch) view
				.findViewById(R.id.switch_visible_for_all);
	}

	@Override
	public Account prepareToSave(Account item) {

		item.setName(etbx_name.getText().toString());
		item.setBalance(etbx_balance.getText().toString());
		item.setIsSumInGlobalBalance(switch_inSum.isChecked());
		item.setIsVisibleForAll(switch_isVisible.isChecked());
		item.setActive(switch_isActive.isChecked());

		return item;
	}

	@Override
	public boolean isValid(Account item) {
		boolean result = true;

		if (item.getName().length() == 0)
			result = false;

		return result;
	}

	@Override
	public void itemToEdit(Account item) {
		etbx_name.setText(item.getName());

		switch_inSum.setChecked(item.isSumInGlobalBalance());
		switch_isActive.setChecked(item.isActive());
		switch_isVisible.setChecked(item.isVisibleForAll());

		etbx_balance.setText(UnitConverter.doubleToString(item.getBalance()));
	}

	@Override
	public void configureNewItem(Account item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected ProviderType getProviderType() {
		return ProviderType.Accounts;
	}

}
