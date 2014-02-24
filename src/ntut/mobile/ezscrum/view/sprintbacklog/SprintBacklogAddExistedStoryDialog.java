package ntut.mobile.ezscrum.view.sprintbacklog;

import java.util.HashMap;
import java.util.List;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.util.EzScrumAppUtil;
import ntut.mobile.ezscrum.view.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SprintBacklogAddExistedStoryDialog extends AlertDialog{
	private Context mContext;
	private String mProjectID, mSprintID;
	private List<StoryObject> mStoryList;
	private View mAddExistingStoryView;
	private ListView mAddExistingStoryListview;
	private ImageButton mClearImageButton, mRefreshImageButton;
	private TextView mRefreshTimeTextView;
	private CheckBox mStoryCheckBox;
	private ProductBacklogItemManager mProductBacklogItemManager;
	private SprintBacklogAddExistedStoryAdapter mAddExistedStoryAdapter;
	protected SprintBacklogAddExistedStoryDialog(Context context, String projectID, String sprintID) {
		super(context);
		
		//	初始化使用者資訊
		mProductBacklogItemManager = new ProductBacklogItemManager();
		this.mProjectID = projectID;
		this.mSprintID = sprintID;
		this.mContext = context;
		
		mAddExistingStoryView = getLayoutInflater().inflate(R.layout.sprintbacklog_addexistingstory_listview, null);
		initialAlertDialogWidgets();
		this.setCanceledOnTouchOutside(false);
		this.setView( mAddExistingStoryView );
		this.setTitle( " Add existing story " );
		this.show();
		initialViewWidgets();
		handleAlertDialogWidgets();
		handleViewWidgets();
	}
	
	/**
	 * 處理list view上面元件事件
	 */
	private void handleViewWidgets() {
		//	設定 story item 點擊事件
		mAddExistingStoryListview.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int postion, long arg3) {
				String id = mStoryList.get(postion).get_id();
				mAddExistedStoryAdapter.changeStroyCheckBoxValue( id );
				mAddExistedStoryAdapter.notifyDataSetChanged();
			}
		});
		
		//	設定全選或取消全選
		mStoryCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if( isChecked ){
					mAddExistedStoryAdapter.changeAllCheckBoxValue(mStoryCheckBox.isChecked());
				}else{
					mAddExistedStoryAdapter.changeAllCheckBoxValue(mStoryCheckBox.isChecked());
				}
				mAddExistedStoryAdapter.notifyDataSetChanged();
			}
		});
		
		//	清除使用者所有所選的story	
		mClearImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mStoryCheckBox.setChecked( false );
				mAddExistedStoryAdapter.changeAllCheckBoxValue( false );
				mAddExistedStoryAdapter.notifyDataSetChanged();
			}
		});
		
		//	更新add existing story alert dialog
		mRefreshImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getProductBacklogAllItems();
				mRefreshTimeTextView.setText( EzScrumAppUtil.getCurrentSystemTime() );
				mAddExistedStoryAdapter.notifyDataSetChanged();
			}
		});
	}
	
	/***
	 * 過濾以加入sprint的story
	 */
	private void getProductBacklogAllItems() {
		//	取得server story information
		mStoryList = mProductBacklogItemManager.retrieveExistedProductBacklogAllItems(mProjectID);
	}
	/**
	 * 初始化 view 的元件
	 */
	private void initialViewWidgets() {
		mClearImageButton = (ImageButton) findViewById(R.id.sprintbacklog_addexistingstory_clear_imageButton);
		mRefreshImageButton = (ImageButton) findViewById(R.id.sprintbacklog_addexistingstory_refresh_imageButton);
		mRefreshTimeTextView = (TextView) findViewById(R.id.sprintbacklog_addexistingstory_refreshTime_textView);
		mStoryCheckBox = (CheckBox) findViewById(R.id.sprintBacklog_addexistingstory_rowLabel_story_checkBox);
		mAddExistingStoryListview = (ListView) findViewById(R.id.sprintBacklog_addexistingstory_listview);
		
		getProductBacklogAllItems();
		mAddExistedStoryAdapter = new SprintBacklogAddExistedStoryAdapter(mContext, mStoryList);
		mAddExistingStoryListview.setAdapter(mAddExistedStoryAdapter);
		mAddExistingStoryListview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mRefreshTimeTextView.setText(EzScrumAppUtil.getCurrentSystemTime());
	}
	
	/**
	 * 處理 AlertDialog 的事件
	 */
	private void handleAlertDialogWidgets() {
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		params.width = 800;
		params.height = 600;
		this.getWindow().setAttributes(params);
	}
	
	/**
	 * 初始化 AlertDialog 的元件
	 */
	private void initialAlertDialogWidgets() {
		this.setButton(BUTTON_POSITIVE, "Add story", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				HashMap<String, Boolean> storyCheckBoxHashMap = mAddExistedStoryAdapter.getStoryCheckBoxHashMap();
				for (StoryObject updateStory:mStoryList) {
					String id = updateStory.get_id();
					if (storyCheckBoxHashMap.get(id)) {
						mProductBacklogItemManager.addExistedProductBacklogItem(mProjectID, mSprintID, updateStory);
					}
				}
			}
		});
		this.setButton(BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	}
}
