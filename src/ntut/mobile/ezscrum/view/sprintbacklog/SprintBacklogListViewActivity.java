package ntut.mobile.ezscrum.view.sprintbacklog;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.sprint.SprintManager;
import ntut.mobile.ezscrum.controller.sprintbacklog.SprintBacklogManager;
import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.util.EzScrumAppUtil;
import ntut.mobile.ezscrum.view.BaseActivity;
import ntut.mobile.ezscrum.view.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class SprintBacklogListViewActivity extends BaseActivity implements Runnable {
	private Context context;
	private LinearLayout mItemList;
	private String mProjectID;
	private String mSprintID;
	private SprintObject mCurrentSprint;
	private List<String> mSprintIDList;
	private List<SprintObject> mSprintList;
	private List<StoryObject> mStoryList;
	private SprintBacklogManager sprintBacklogManager;
	
	// dialog
	private SprintBacklogStoryDetailDialog mStoryDetaiDialog;
	private SprintBacklogStoryOperationDialog mStoryOperationDialog;
	private SprintBacklogTaskDetailDialog mTaskDetaiDialog;
	private SprintBacklogTaskOperationDialog mTaskOperationDialog;
	
	// widgets
	private Spinner mSprintIDSpinner;
	private Button sprintLeftBtn;
	private Button sprintRightBtn;
	private ProgressDialog progressDialog;
	private MenuItem refreshMenuItem;
	
	private ItemClickListener storyClickListener = new ItemClickListener() {
		@Override
		public void onItemClick(int storyIndex, int taskIndex) {
			showStoryDetailInfoDialog(storyIndex, SprintBacklogStoryDetailDialog.MODE_UPDATE);
		}
	};
	
	private ItemLongClickListener storyLongClickListener = new ItemLongClickListener() {
		@Override
		public void onItemLongClick(int storyIndex, int taskIndex) {
			showStoryOperationDialog(storyIndex);
		}
	};
	
	private ItemClickListener taskClickListener = new ItemClickListener() {
		@Override
		public void onItemClick(int storyIndex, int taskIndex) {
			showTaskDetailInfoDialog(storyIndex, taskIndex, SprintBacklogTaskDetailDialog.MODE_UPDATE);
		}
	};
	
	private ItemLongClickListener taskLongClickListener = new ItemLongClickListener() {
		@Override
		public void onItemLongClick(int storyIndex, int taskIndex) {
			showTaskOperationDialog(storyIndex, taskIndex);
		}
	};
	
	/**
	 * 操作 story 或 task 後，要更新畫面的事件 
	 */
	private SprintBacklogUpdateListener mUpdateListener = new SprintBacklogUpdateListener() {
		@Override
		public void update() {
			refreshAdapter();
		}
	};
	
	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sprintbacklog_listview);
		context = this;
		
		initial();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sprintbacklog, menu);
		
		refreshMenuItem = (MenuItem) menu.findItem(R.id.refreshSprintBacklog);
		refreshMenuItem.setTitle(EzScrumAppUtil.getCurrentSystemTime());
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
        	case R.id.addStory:
        		onAddStoryClick(item);
        		break;
        	case R.id.addExistingStory:
        		onAddExistingStoryClick(item);
        		break;
	        case R.id.refreshSprintBacklog:
	        	onRefreshClick(item);
	        	break;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		refreshAdapter();
	}
	
	/**
	 * on add story button click(Action bar)
	 */
	public void onAddStoryClick(MenuItem item) {
		StoryObject newStory = new StoryObject();
		newStory.set_sprint(mSprintID);
		showStoryDetailInfoDialog(newStory, SprintBacklogStoryDetailDialog.MODE_CREATE);
	}

	/**
	 * on add existing story button click(Action bar)
	 */
	public void onAddExistingStoryClick(MenuItem item) {
		SprintBacklogAddExistedStoryDialog addExistedStoryDialog = new SprintBacklogAddExistedStoryDialog(context, mProjectID, mSprintID);
		addExistedStoryDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				refreshAdapter();
			}
		});
	}
	
	/**
	 * on refresh button click(Action bar)
	 */
	public void onRefreshClick(MenuItem item) {
		refreshAdapter();
	}
	
	/**
	 * initial activity component
	 */
	private void initial(){
		Bundle bundle = this.getIntent().getExtras();
		mProjectID = bundle.getString("projectID");
		
		sprintBacklogManager = new SprintBacklogManager();
		
		// Button
		sprintLeftBtn = (Button) findViewById(R.id.sprintBacklog_left_imageButton);
		sprintRightBtn = (Button) findViewById(R.id.sprintBacklog_right_imageButton);
		
		// sprint backlog ID spinner
		mSprintIDSpinner = (Spinner) findViewById(R.id.sprintBacklog_sprintID_spinner);
		
		// sprint backlog story list view
		mItemList = (LinearLayout) findViewById(R.id.sprintBacklog_item_list);
		setTitle(R.string.sprintBacklog);
		
		// sprint id list
		mSprintIDList = new ArrayList<String>();
		
		getSprintBacklogInfoList();
		int size = mSprintList.size() - 1;
		mSprintID = mSprintList.get(size).getSprintID(); // set the-last sprint id

		initialListViewAdapter();
		initSpinner();
		initBtnListener();
	}

	/**
	 * initial story list view
	 */
	private void initialListViewAdapter() {
		getCurrentSprint();
	}
	
	/**
	 * 顯示 story information
	 * @param storyIndex 
	 */
	private void showStoryDetailInfoDialog(int position, int mode) {
		StoryObject story = mStoryList.get(position);
		mStoryDetaiDialog = new SprintBacklogStoryDetailDialog(this, story, mProjectID, mode);
		mStoryDetaiDialog.setUpdateListener(mUpdateListener);
		mStoryDetaiDialog.show();
	}
	
	private void showStoryDetailInfoDialog(StoryObject story, int mode) {
		mStoryDetaiDialog = new SprintBacklogStoryDetailDialog(this, story, mProjectID, mode);
		mStoryDetaiDialog.setUpdateListener(mUpdateListener);
		mStoryDetaiDialog.show();
	}
	
	/**
	 * 彈出操作 story 的 dialog 
	 * @param storyIndex
	 */
	private void showStoryOperationDialog(int storyIndex) {
		StoryObject story = mStoryList.get(storyIndex);
		// 設定對 Story 操作的功能 dialog
		if (mStoryOperationDialog == null) {
			mStoryOperationDialog = new SprintBacklogStoryOperationDialog(this, story, mProjectID);
			mStoryOperationDialog.setUpdateListener(mUpdateListener);
		} else {
			mStoryOperationDialog.setStory(story);
		}
		mStoryOperationDialog.show();
	}
	
	/**
	 * 顯示 task information
	 * @param storyIndex 
	 * @param taskIndex 
	 */
	private void showTaskDetailInfoDialog(int storyIndex, int taskIndex, int mode) {
		StoryObject story = mStoryList.get(storyIndex);
		TaskObject task = story.get_taskList().get(taskIndex);
		if (mTaskDetaiDialog == null) {
			mTaskDetaiDialog = new SprintBacklogTaskDetailDialog(this, task, mProjectID, mode);
			mTaskDetaiDialog.setUpdateListener(mUpdateListener);
		} else {
			mTaskDetaiDialog.setMode(mode);
			mTaskDetaiDialog.setTask(task);
		}
		mTaskDetaiDialog.show();
	}
	
	/**
	 * 彈出操作 task 的 dialog 
	 * @param storyIndex
	 * @param taskIndex
	 */
	private void showTaskOperationDialog(int storyIndex, int taskIndex) {
		StoryObject story = mStoryList.get(storyIndex);
		TaskObject task = story.get_taskList().get(taskIndex);
		
		if (mTaskOperationDialog == null) {
			mTaskOperationDialog = new SprintBacklogTaskOperationDialog(context, task, mProjectID);
			mTaskOperationDialog.setUpdateListener(mUpdateListener);
		} else {
			mTaskOperationDialog.setTask(task);
		}
		mTaskOperationDialog.show();
	}
	
	/**
	 * 取得所有的 sprint list 並從中取出 sprint id list
	 */
	private void getSprintBacklogInfoList() {
		mSprintList = sprintBacklogManager.getAllSprintListInformation(mProjectID);
		// 更新 Sprint ID list
		for (SprintObject sprint : mSprintList)
			mSprintIDList.add(sprint.getSprintID());
	}
	
	private void getCurrentSprint() {
		mCurrentSprint = SprintManager.getSprintWithItem(mProjectID, mSprintID);
		mStoryList = mCurrentSprint.getStoryList();
	}
	
	private void initSpinner() {
		int size = mSprintList.size();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mSprintIDList);

		// �]�w�U�Ԧ����
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// �]�wadpater
		mSprintIDSpinner.setAdapter(adapter);
		mSprintIDSpinner.setSelection(size - 1);

		// �]�wspinner���ƥ�
		mSprintIDSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
						mSprintID = mSprintIDList.get(position);
						btnLocker();
						// 如果沒有 sprint 存在，將原鍵上鎖
						if (mSprintID.trim().length() == 0){
							Toast.makeText(SprintBacklogListViewActivity.this, "Doesn't have any sprint..", Toast.LENGTH_LONG).show();
							return;
						}
//						initialListViewAdapter();
						// 要等畫面讀完才會將原件解鎖
						refreshAdapter();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {}
				});
	}
	
	/**
	 * 控制 Sprint 往前往後的按鈕初始化
	 */
	private void initBtnListener() {
		sprintLeftBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = mSprintIDList.indexOf(mSprintID);
				// 不是第一個 sprint 才能夠往前
				if (position > 0) {
					btnLocker();
					mSprintID = mSprintIDList.get(--position);
					mSprintIDSpinner.setSelection(position);
				}
			}
		});
		
		sprintRightBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = mSprintIDList.indexOf(mSprintID);
				// 不是最後一個 sprint 才能夠往前
				if (position < mSprintList.size() - 1) {
					btnLocker();
					mSprintID = String.valueOf(++position);
					mSprintIDSpinner.setSelection(position);
				}
			}
		});
	}
	
	/**
	 * 將換 sprint 的按鈕上鎖，以免重覆處發事件，導致程式錯誤
	 */
	private void btnLocker() {
		mSprintIDSpinner.setEnabled(false);
		sprintLeftBtn.setAlpha(new Float(0.25));
		sprintLeftBtn.setEnabled(false);
		sprintRightBtn.setAlpha(new Float(0.25));
		sprintRightBtn.setEnabled(false);
	}

	
	/**
	 * 將換 sprint 的按鈕解鎖
	 */
	private void btnUnlocker() {
		int value = Integer.valueOf(mSprintID);
		// 取得第一個 sprint 的 id
		int firstID = Integer.parseInt(mSprintIDList.get(0));
		// 如果是第一個 sprint 往左的按鈕不能打開
		if (value == firstID) {
			sprintLeftBtn.setAlpha(new Float(0.25));
			sprintLeftBtn.setEnabled(false);
		} else {
			sprintLeftBtn.setAlpha(1);
			sprintLeftBtn.setEnabled(true);
		}
		// 取得最後一個 sprint 的 id
		int lastID = Integer.parseInt(mSprintIDList.get(mSprintIDList.size() - 1));
		// 如果是最後一個 sprint 往後的按鈕不能打開
		if (value == lastID) {
			sprintRightBtn.setAlpha(new Float(0.25));
			sprintRightBtn.setEnabled(false);
		} else {
			sprintRightBtn.setAlpha(1);
			sprintRightBtn.setEnabled(true);							
		}
		mSprintIDSpinner.setEnabled(true);
	}
	
	/**
	 * ���s��zadapter, ���s�qserver��X�����dialog
	 */
	public void refreshAdapter() {
		// progress dialog
		progressDialog = ProgressDialog.show(this, "", "Loading...", true);
		
		Thread thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * 將 item view 裡面的資料全部清空
	 */
	public void cleanItemView() {
		while (mItemList.getChildCount() != 0) {
			View item = mItemList.getChildAt(0);
			mItemList.removeViewAt(0);
			item = null;
		}
	}
	
	/**
	 * 將畫面上放置 story 和 task 的 view 初始化，並放入資料
	 */
	public void initItemView() {
		cleanItemView();
		// Sprint 內的所有 story
		for (StoryObject story : mStoryList) {
			int storyIndex = mStoryList.indexOf(story);
			SprintBacklogItem storyItem = new SprintBacklogItem(this, story, storyIndex);
			storyItem.setOnItemClickListener(storyClickListener);
			storyItem.setOnItemLonglickListener(storyLongClickListener);
			mItemList.addView(storyItem.getView());
			// Story 內的所有 task
			List<TaskObject> taskList = story.get_taskList(); 
			for (TaskObject task : taskList) {
				int taskIndex = taskList.indexOf(task);
				task.setStoryID(story.get_id());
				SprintBacklogItem taskItem = new SprintBacklogItem(this, task, storyIndex, taskIndex);
				taskItem.setOnItemClickListener(taskClickListener);
				taskItem.setOnItemLonglickListener(taskLongClickListener);
				mItemList.addView(taskItem.getView());
			}
		}
		btnUnlocker();
	}

	@Override
	public void run() {
		initialListViewAdapter();
		handler.sendEmptyMessage(0);
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			initItemView();
			progressDialog.dismiss(); // progress dialog
			refreshMenuItem.setTitle(EzScrumAppUtil.getCurrentSystemTime());
		}
	};
}
