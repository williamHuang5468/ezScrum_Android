package ntut.mobile.ezscrum.view.sprintbacklog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.view.R;
import ntut.mobile.ezscrum.view.R.id;
import ntut.mobile.ezscrum.view.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SprintBacklogAddExistedStoryAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<StoryObject> storyList;
	private static HashMap<String, Boolean> storyCheckBoxHashMap;
	

	public SprintBacklogAddExistedStoryAdapter( Context context, List<StoryObject> storyList ){
		this.context = context;
		this.inflater = LayoutInflater.from( this.context );
		this.storyList = storyList;
		initialStoryCheckBoxValue();
	}
	
	/**
	 * 設定每個story check box的初始值
	 * key:story id
	 * value:false
	 */
	private void initialStoryCheckBoxValue() {
		storyCheckBoxHashMap = new HashMap<String, Boolean>();
		for( StoryObject story:storyList ){
			storyCheckBoxHashMap.put( story.get_id(), false );
		}
	}

	@Override
	public int getCount() {
		return storyList.size();
	}

	@Override
	public Object getItem(int position) {
		return storyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		String id = storyList.get(position).get_id();
		long itemId = Long.parseLong(id);
		return itemId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.sprintbacklog_addexistingstory_item, null );
		}
		StoryObject story = storyList.get(position);
		CheckBox storyCheckBox = (CheckBox) convertView.findViewById( R.id.sprintBacklog_addexistingstory_story_checkBox );
		TextView storyIDTextView = (TextView) convertView.findViewById( R.id.sprintBacklog_addexistingstory_storyID );
		TextView nameTextView = (TextView) convertView.findViewById( R.id.sprintBacklog_addexistingstory_name );
		TextView importanceTextView = (TextView) convertView.findViewById( R.id.sprintBacklog_addexistingstory_importance );
		TextView valueTextView = (TextView) convertView.findViewById( R.id.sprintBacklog_addexistingstory_value );
		TextView estimationTextView = (TextView) convertView.findViewById( R.id.sprintBacklog_addexistingstory_estimation );
		storyIDTextView.setText( story.get_id() );
		nameTextView.setText( story.get_name() );
		importanceTextView.setText( story.get_importance() );
		valueTextView.setText( story.get_value() );
		estimationTextView.setText( story.get_estimation() );
		storyCheckBox.setChecked( storyCheckBoxHashMap.get(story.get_id()) );
		return convertView;
	}
	
	/**
	 * 當adapter觸發事件時，修改對應的tag item的check box value
	 * @param id
	 */
	public void changeStroyCheckBoxValue(String id) {
		boolean isChecked = storyCheckBoxHashMap.get(id);
		if( isChecked ){
			storyCheckBoxHashMap.put( id, false );
		}else{
			storyCheckBoxHashMap.put( id, true );
		}
	}
	
	public void changeAllCheckBoxValue( boolean flag){
        Iterator iterator = storyCheckBoxHashMap.entrySet().iterator();
        while( iterator.hasNext() ){
            Map.Entry mapEntry = (Map.Entry)iterator.next();
			Boolean isCheck = (Boolean)( mapEntry.getValue() );
			String id = (String)( mapEntry.getKey() );
			storyCheckBoxHashMap.put(id, flag);
        }
	}
	
	public HashMap<String, Boolean> getStoryCheckBoxHashMap() {
		return storyCheckBoxHashMap;
	}
}
