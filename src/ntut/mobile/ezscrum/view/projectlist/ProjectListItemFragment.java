package ntut.mobile.ezscrum.view.projectlist;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.projectlist.ProjectListManager;
import ntut.mobile.ezscrum.controller.sprintbacklog.SprintBacklogManager;
import ntut.mobile.ezscrum.model.ProjectObject;
import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.view.R;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class ProjectListItemFragment extends ListFragment{
	private List<ProjectObject> mProjectList;
	private List<SprintObject> mSprintList;
	private ProjectObject mProjectObject = null;
	private SprintObject mCurrentSprintObject = null;
	private ProjectListManager mProjectListManager;
	private SprintBacklogManager mSprintBacklogManager;
	private ProjectListItemListener mProjectListItemListener;
	private int mPosition;
	
	private ProjectListAdapter mProjectListAdapter;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mProjectList = getProjectListData();
		mSprintList = getSprintListData();
		mProjectListAdapter = new ProjectListAdapter(getActivity().getApplicationContext(), mProjectList);
		setListAdapter(mProjectListAdapter);
		getListView().setSelector(R.drawable.projectitem_selector);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
//		super.onListItemClick(l, v, position, id);
		mPosition = position;
		l.setItemChecked(position, true);
		if( mProjectListItemListener != null){
			mProjectObject = mProjectList.get(position);
			mCurrentSprintObject = mSprintList.get(position);
			mProjectListItemListener.onProjectListItemSelected(mProjectObject, mCurrentSprintObject);
		}
	}
	
	public void setProjectListItemListener(ProjectListItemListener projectListItemListener) {
		this.mProjectListItemListener = projectListItemListener;
	}
	
	private List<SprintObject> getSprintListData() {
		List<SprintObject> sprintList = new ArrayList<SprintObject>();
		for(ProjectObject project: mProjectList){
			String projectID = project.getId();
			SprintObject sprint = getTheCurrentSprintInformation(projectID);
			sprintList.add(sprint);
		}
		return sprintList;
	}
	
	/**	取得該帳號參與的所有專案資訊	**/
	private List<ProjectObject> getProjectListData() {
		mProjectListManager = new ProjectListManager();
		mProjectListManager.readProjectList();
		return mProjectListManager.getProjectList();
	}
	
	/**
	 * 取得專案正在進行的sprint information
	 * @param projectID
	 * @return
	 */
	private SprintObject getTheCurrentSprintInformation(String projectID) {
		mSprintBacklogManager = new SprintBacklogManager();
		SprintObject currentSprint = mSprintBacklogManager.getTheCurrentSprintInformation(projectID);
		return currentSprint;
	}
	
	/**
	 * 更新專案列表和資訊
	 */
	public void refreshProjectInformation(){
		mProjectList = getProjectListData();
		mSprintList = getSprintListData();
		mProjectListAdapter.refreshProjectList(mProjectList);
		
		if((mProjectObject != null) && (mCurrentSprintObject != null)) {
			mProjectObject = mProjectList.get(mPosition);
			mCurrentSprintObject = mSprintList.get(mPosition);
			mProjectListItemListener.onProjectListItemSelected( mProjectObject, mCurrentSprintObject );
		}
	}

}
