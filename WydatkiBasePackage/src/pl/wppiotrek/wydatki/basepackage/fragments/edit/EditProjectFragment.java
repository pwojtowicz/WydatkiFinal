package pl.wppiotrek.wydatki.basepackage.fragments.edit;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.entities.Project;
import pl.wppiotrek.wydatki.basepackage.enums.ProviderType;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

public class EditProjectFragment extends BaseEditItemFragment<Project> {

	private EditText etbx_name;
	private Switch switch_isActive;
	private Switch switch_isVisible;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View convertView = inflater.inflate(R.layout.fragment_edit_project,
				null);
		linkViews(convertView);

		return convertView;
	}

	private void linkViews(View view) {
		etbx_name = (EditText) view.findViewById(R.id.etbx_name);

		switch_isActive = (Switch) view.findViewById(R.id.switch_active);
		switch_isVisible = (Switch) view
				.findViewById(R.id.switch_visible_for_all);
	}

	@Override
	public Project prepareToSave(Project item) {
		item.setName(etbx_name.getText().toString());
		item.setIsVisibleForAll(switch_isVisible.isChecked());
		item.setActive(switch_isActive.isChecked());
		return item;
	}

	@Override
	public boolean isValid(Project item) {
		boolean result = true;
		if (item.getName().length() == 0)
			result = false;
		return result;
	}

	@Override
	protected ProviderType getProviderType() {
		return ProviderType.Projects;
	}

	@Override
	public void configureItemView(Project item) {
		if (item.getId() > 0) {
			etbx_name.setText(item.getName());
			switch_isActive.setChecked(item.isActive());
			switch_isVisible.setChecked(item.getIsVisibleForAll());
		}

	}

}
