package pl.wppiotrek.wydatki.basepackage.adapters;

import pl.wppiotrek.wydatki.basepackage.R;
import pl.wppiotrek.wydatki.basepackage.adapters.ListProjectAdapter.ProjectContainer;
import pl.wppiotrek.wydatki.basepackage.entities.Project;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListProjectAdapter extends
		ListViewWithSelectionAdapter<Project, ProjectContainer> {

	public ListProjectAdapter(Context context, boolean isSelectionEnabled,
			IOnSelectionChangeListener changeStateListener) {
		super(context, 0, R.layout.row_project_layout, isSelectionEnabled,
				changeStateListener);
	}

	@Override
	protected ProjectContainer loadContentControlls(View convertView) {
		ProjectContainer container = new ProjectContainer();
		container.Lock = (ImageView) convertView
				.findViewById(R.id.row_project_lock);
		container.Name = (TextView) convertView
				.findViewById(R.id.row_project_name);
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
	protected void fillContentRow(View convertView, Project object,
			ProjectContainer controlContainer, int position) {
		controlContainer.Selection.setTag(object);
		controlContainer.Name.setText(object.getName());

		if (object.isActive())
			controlContainer.Lock.setVisibility(ImageView.GONE);
		else
			controlContainer.Lock.setVisibility(ImageView.VISIBLE);
	}

	class ProjectContainer {
		CheckBox Selection;
		TextView Name;
		ImageView Lock;
	}

}
