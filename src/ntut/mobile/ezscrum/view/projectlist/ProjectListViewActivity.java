package ntut.mobile.ezscrum.view.projectlist;

import ntut.mobile.ezscrum.model.ProjectObject;
import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.util.EzScrumAppUtil;
import ntut.mobile.ezscrum.view.BaseActivity;
import ntut.mobile.ezscrum.view.R;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ProjectListViewActivity extends BaseActivity implements Runnable{
	
	private MenuItem refreshMenuItem;
//	private ProjectListItemInfoFragment projectListItemInfoFragment;
	private ProjectListItemFragment projectListItemFragment;
	private ProgressDialog progressDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projectlist_view);
        setTitle(R.string.projectList);
        final ProjectListItemInfoFragment projectListItemInfoFragment = (ProjectListItemInfoFragment) getFragmentManager().findFragmentById(R.id.projectListItemInfoFragment);
//        projectListItemInfoFragment = (ProjectListItemInfoFragment) getFragmentManager().findFragmentById(R.id.projectListItemInfoFragment);
        
//        ProjectListItemFragment projectListItemFragment = (ProjectListItemFragment) getFragmentManager().findFragmentById(R.id.projectListFragment);
        projectListItemFragment = (ProjectListItemFragment) getFragmentManager().findFragmentById(R.id.projectListFragment);
        projectListItemFragment.setProjectListItemListener(new ProjectListItemListener() {
			@Override
			public void onProjectListItemSelected(ProjectObject projectObject, SprintObject theLatestSprintObject) {
				projectListItemInfoFragment.loadProjectListItemInfo(projectObject, theLatestSprintObject);
				projectListItemInfoFragment.loadBurndownChart(projectObject, theLatestSprintObject);
			}
        });
        
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.refreshProjectList:
	        	refreshProjectInformation();
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.projectlist, menu);
		refreshMenuItem = (MenuItem) menu.findItem(R.id.refreshProjectList);
		refreshMenuItem.setTitle( EzScrumAppUtil.getCurrentSystemTime() );
		return super.onCreateOptionsMenu(menu);
	}
	
	/**
	 * 重新整理adapter, 重新從server取出並顯示dialog
	 */
	private void refreshProjectInformation() {
		// 顯示progress dialog
		progressDialog = ProgressDialog.show(this, "", "Loading...", true);
		
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		handler.sendEmptyMessage(0);
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			projectListItemFragment.refreshProjectInformation();
			progressDialog.dismiss(); // 取消progress dialog
			refreshMenuItem.setTitle( EzScrumAppUtil.getCurrentSystemTime() );
		}
	};
}