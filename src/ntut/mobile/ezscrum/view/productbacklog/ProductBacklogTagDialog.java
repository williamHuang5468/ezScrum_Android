package ntut.mobile.ezscrum.view.productbacklog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TagObject;
import ntut.mobile.ezscrum.view.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ListView;

public class ProductBacklogTagDialog extends AlertDialog {
	private View tagManageView;
	private ListView tagManageListview;
	private ProductBacklogTagAdapter tagAdapter;
	private StoryObject story;
	
	protected ProductBacklogTagDialog(Context context, ProductBacklogItemManager productBacklogItemManager, StoryObject story, List<TagObject> tagList, Map<String, Integer> pairTagAndImage) {
		super(context);
		this.story = story;
		tagAdapter = new ProductBacklogTagAdapter(context, pairTagAndImage, story, tagList);
		tagManageView = getLayoutInflater().inflate(R.layout.productbacklog_tag_listview, null);
		String title = " Add Tag ";
		initialAlertDialogWidgets();
		this.setCanceledOnTouchOutside(false);
		this.setView(tagManageView);
		this.setTitle(title);
		this.show();
		initialViewWidgets();
		handleViewWidgets();
	}
	
	/**
	 * 初始化 view 的元件
	 */
	private void initialViewWidgets() {
		tagManageListview = (ListView) findViewById(R.id.productbacklog_tag_listview);
		tagManageListview.setAdapter(tagAdapter);
		tagManageListview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}
	
	/**
	 * 處理 tag alert dialog item 的觸發事件
	 */
	private void handleViewWidgets() {
		//	設定 tag item 點擊事件
/*		tagManageListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int postion, long arg3) {
				tagAdapter.changeTagCheckBoxValue(tagList.get(postion));
			}
		});*/
	}
	
	/**
	 * 初始化  AlertDialog 的元件
	 */
	private void initialAlertDialogWidgets() {
		this.setButton(BUTTON_POSITIVE, "Add tag", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				handleStoryTag();
			}
		});
		this.setButton(BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
	} 
	
	/**
	 * 處理dialog裡的tag check box是否有被勾選.
	 * 勾選的話，則新增tag至story中，在更新.
	 * 沒有勾選的話，則刪除tag，在更新.
	 */
	private void handleStoryTag(){
		HashMap<TagObject, Boolean> tagHashMap = tagAdapter.getTagCheckBoxHashMap();
		Iterator iterator = tagHashMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry mapEntry = (Map.Entry)iterator.next();
			boolean isCheck = (Boolean)(mapEntry.getValue());
			TagObject tagInMap = (TagObject)(mapEntry.getKey());
			if(isCheck){
				boolean isContain = false;
				for (TagObject tag : story.get_tagList()) {
					if (tag.equals(tagInMap)) {
						isContain = true;
						break;
					}
				}
				if(!isContain)
					story.add_tag(tagInMap);
			} else {
				story.remove_tag(tagInMap);
			}
        }
	}
	
	public StoryObject getStoryObject(){
		return story;
	}
}
