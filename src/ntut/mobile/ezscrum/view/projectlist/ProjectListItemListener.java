package ntut.mobile.ezscrum.view.projectlist;

import ntut.mobile.ezscrum.model.ProjectObject;
import ntut.mobile.ezscrum.model.SprintObject;


public interface ProjectListItemListener {
	void onProjectListItemSelected(ProjectObject projectObject, SprintObject theLatestSprintObject);
}
