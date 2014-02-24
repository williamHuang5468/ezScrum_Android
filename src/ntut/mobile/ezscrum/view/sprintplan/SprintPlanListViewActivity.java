package ntut.mobile.ezscrum.view.sprintplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ntut.mobile.ezscrum.controller.sprint.SprintManager;
import ntut.mobile.ezscrum.internal.SprintPlanEnum;
import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.util.EzScrumAppUtil;
import ntut.mobile.ezscrum.view.BaseActivity;
import ntut.mobile.ezscrum.view.R;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @author Reverof
 * 
 */
public class SprintPlanListViewActivity extends BaseActivity implements
		Runnable {
	private Context mContext;
	private ListView mSprintPlanSprintListView;
	private String mProjectID;
	private List<SprintObject> mSprintList;
	private SimpleAdapter mSprintListAdapter;
	private List<HashMap<String, Object>> mSprintHashMapArrayList;
	private SprintManager mSprintManager;

	// widgets
	private ProgressDialog mProgressDialog;
	private MenuItem mRefreshMenuItem;

	@Override
	public void run() {
		getSprintBacklogInfoList();
		initialListViewAdapter();
		handler.sendEmptyMessage(0);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sprintplan_listview);
		mContext = this;
		initial();
		initialSprintPlanListViewClickListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sprintplan, menu);
		mRefreshMenuItem = (MenuItem) menu.findItem(R.id.refreshSprint);
		mRefreshMenuItem.setTitle(EzScrumAppUtil.getCurrentSystemTime());
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.createSprint:
			onCreateSprintClick(item);
			break;
		case R.id.refreshSprint:
			onRefreshClick(item);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 取得最新可新增的SprintID
	 * 
	 * @return
	 */
	public String getCreatableSprintID() {
		String lastCreatableprintIDInString;
		Integer lastCreatableSprintIDInInt;
		lastCreatableprintIDInString = mSprintList.get(mSprintList.size() - 1).getSprintID();
		lastCreatableSprintIDInInt = Integer.valueOf(lastCreatableprintIDInString);
		lastCreatableSprintIDInInt++;
		lastCreatableprintIDInString = String.valueOf(lastCreatableSprintIDInInt);
		return lastCreatableprintIDInString;
	}

	/**
	 * initial activity component
	 */
	private void initial() {
		setLastLoginAccountInfo();

		mSprintManager = new SprintManager();

		// Sprint plan list view
		mSprintPlanSprintListView = (ListView) findViewById(R.id.sprintPlan_sprint_listview);

		// 與 Sprint backlog 點擊 item 顯示顏色相同，尚未做更改
		mSprintPlanSprintListView.setSelector(R.drawable.projectitem_selector);
		setTitle(R.string.sprintPlan);
		getSprintBacklogInfoList();

		initialListViewAdapter();

		// call run
		refreshAdapter();
	}

	private void setLastLoginAccountInfo() {
		// 取得Project ID
		Bundle bundle = this.getIntent().getExtras();
		mProjectID = bundle.getString("projectID");
	}

	/**
	 * 取得 Sprint backlog info list
	 */
	private void getSprintBacklogInfoList() {
		mSprintList = mSprintManager.getSprintAll(mProjectID);
	}

	private void initialListViewAdapter() {
		// 塞資料
		mSprintHashMapArrayList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> sprintMap;
		for (SprintObject sprint : mSprintList) {
			sprintMap = new HashMap<String, Object>();
			sprintMap.put(SprintPlanEnum.TAG_ID, sprint.getSprintID());
			sprintMap
					.put(SprintPlanEnum.TAG_SPRINTGOAL, sprint.getSprintGoal());
			sprintMap.put(SprintPlanEnum.TAG_STARTDATE, sprint.getStartDate());
			sprintMap.put(SprintPlanEnum.TAG_INTERVAL, sprint.getInterval());
			sprintMap.put(SprintPlanEnum.TAG_DEMODATE, sprint.getDemoDate());
			sprintMap.put(SprintPlanEnum.TAG_MEMBERS, sprint.getMembers());
			sprintMap.put(SprintPlanEnum.TAG_HOURSCANCOMMIT,
					sprint.getHoursCanCommit());
			mSprintHashMapArrayList.add(sprintMap);
		}
		// mapping table
		mSprintListAdapter = new SimpleAdapter(mContext,
				mSprintHashMapArrayList, R.layout.sprintplan_sprintitem,
				new String[] { SprintPlanEnum.TAG_ID,
						SprintPlanEnum.TAG_SPRINTGOAL,
						SprintPlanEnum.TAG_STARTDATE,
						SprintPlanEnum.TAG_INTERVAL,
						SprintPlanEnum.TAG_DEMODATE,
						SprintPlanEnum.TAG_MEMBERS,
						SprintPlanEnum.TAG_HOURSCANCOMMIT }, new int[] {
						R.id.sprintPlan_sprintID, R.id.sprintPlan_sprintGoal,
						R.id.sprintPlan_startDate, R.id.sprintPlan_interval,
						R.id.sprintPlan_demoDate, R.id.sprintPlan_members,
						R.id.sprintPlan_hoursToCommit });
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mSprintPlanSprintListView.setAdapter(mSprintListAdapter);
			mProgressDialog.dismiss(); // 取消progress dialog
		}
	};

	/**
	 * 重新整理adapter, 重新從server取出並顯示dialog
	 */
	public void refreshAdapter() {
		// 顯示progress dialog
		mProgressDialog = ProgressDialog.show(this, "", "Loading...", true);
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * 點擊 menu refresh 事件
	 */
	public void onRefreshClick(MenuItem item) {
		refreshAdapter();
	}
	private void initialSprintPlanListViewClickListener() {
		// 設定點擊 sprint 的事件
		mSprintPlanSprintListView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						onEditSprintClick(position);
					}
				});
		// 設定長按 sprint 的事件
		mSprintPlanSprintListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int position, long arg3) {
						onDeleteSprintClick(mSprintList.get(position)
								.getSprintID());
						return true;
					}
				});
	}


	/**
	 *  點擊 Create Sprint Menu Button
	 */
	public void onCreateSprintClick(MenuItem item) {
		SprintObject newSprint = new SprintObject();
		newSprint.setSprintID(getCreatableSprintID());
		final SprintPlanSprintDialog dialog = new SprintPlanSprintDialog(this, newSprint, SprintPlanSprintDialog.CREATE_MODE);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				if(dialog.isExistValueForAll()) {
					mSprintManager.creatSprint(mProjectID, dialog.getSprint());
					refreshAdapter();
				}
			}
		});
		dialog.show();
	}
	
	private void onEditSprintClick(int position) {
		final SprintObject sprint = mSprintList.get(position);
		final SprintPlanSprintDialog dialog = new SprintPlanSprintDialog(this, sprint, SprintPlanSprintDialog.UPDATE_MODE);
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				if(dialog.isExistValueForAll()) {
					mSprintManager.updateSprint(mProjectID, dialog.getSprint());
					refreshAdapter();
				}
			}
		});
		dialog.show();
	}

	private void onDeleteSprintClick(String sprintID) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Delete Sprint #" + sprintID);
		final String _sprintID = sprintID;
		builder.setPositiveButton("Confirm",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mSprintManager.deleteSprint(mProjectID, _sprintID);
						refreshAdapter();
					}
				});
		builder.setNegativeButton("Cancel", null);
		builder.create().show();
	}
}
