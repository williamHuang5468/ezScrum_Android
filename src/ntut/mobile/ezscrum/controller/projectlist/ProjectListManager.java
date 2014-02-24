package ntut.mobile.ezscrum.controller.projectlist;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.model.ProjectObject;
import android.util.Log;

public class ProjectListManager {
	
	private List<ProjectObject> projectList;
	
	public ProjectListManager(){
		this.projectList = new ArrayList<ProjectObject>();
	}
	
	/****
	 * 讀取該帳號所參與的專案資訊
	 * @param userName
	 * @param password
	 */
	public void readProjectList(){
		ProjectListGetThread getThread = new ProjectListGetThread();
		getThread.start();
		try {
			getThread.join();
			projectList = getThread.getProjectListData();
		} catch (InterruptedException e) {
			Log.e("InterruptedException:", "Class: ProjectListManager, method: readProjectList, " + e.toString());
			e.printStackTrace();
		}
	}
	public List<ProjectObject> getProjectList(){
		return projectList;
	}
}
