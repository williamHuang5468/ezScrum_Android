package ntut.mobile.ezscrum.view.productbacklog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TagObject;
import ntut.mobile.ezscrum.view.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProductBacklogTagAdapter extends BaseAdapter{
	private StoryObject story;
	private List<TagObject> tagList;
	private Context context;
	private LayoutInflater inflater;
	private HashMap<TagObject, Boolean> tagCheckBoxHashMap;
	private Map<String, Integer> pairTagAndImage;
	
	public ProductBacklogTagAdapter(Context context, Map<String, Integer> pairTagAndImage, StoryObject story, List<TagObject> tagList){
		this.context = context;
		this.inflater = LayoutInflater.from(this.context);
		this.pairTagAndImage = pairTagAndImage;
		this.story = story;
		this.tagList = tagList;
		initialTagCheckBoxValue();
	}

	/**
	 * 設定每個tag check box的初始值
	 * key:tag name
	 * value:false
	 */
	private void initialTagCheckBoxValue() {
		tagCheckBoxHashMap = new HashMap<TagObject, Boolean>();
		
		//	初始化tag hash map
		for(TagObject tag : tagList){
			tagCheckBoxHashMap.put(tag, false);
		}
		
		// 根據每個story的tag修改tag hash map value
		for(TagObject storyTag : story.get_tagList()){
			for(TagObject tag : tagList){
				if(storyTag.equals(tag)){
					tagCheckBoxHashMap.put(storyTag, true);
					break;
				} else {
					tagCheckBoxHashMap.put(storyTag, false);
				}
			}
		}
	}

	@Override
	public int getCount() {
		return tagList.size();
	}

	@Override
	public Object getItem(int position) {
		return tagList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.productbacklog_tag_item, null);
		}
		CheckBox tagCheckBox = (CheckBox) convertView.findViewById(R.id.productbacklog_tag_checkBox);
		TextView tagNameTextView = (TextView) convertView.findViewById(R.id.productbacklog_tagName_textView);
		ImageButton tagColorImageButton = (ImageButton) convertView.findViewById(R.id.productbacklog_tagColor_imageButton);
		
		final TagObject tag = tagList.get(position);
		int imageResource = this.pairTagAndImage.get(tag.getTagName());
		tagNameTextView.setText(tag.getTagName());
		tagCheckBox.setChecked(tagCheckBoxHashMap.get(tag));
		tagColorImageButton.setImageResource(imageResource);
		
		tagCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeTagCheckBoxValue(tag);
			}
		});
		return convertView;
	}
	
	/**
	 * 當adapter觸發事件時，修改對應的tag item的check box value
	 * @param tag
	 */
	public void changeTagCheckBoxValue(TagObject tag) {
		boolean isChecked = tagCheckBoxHashMap.get(tag);
		if(isChecked){
			tagCheckBoxHashMap.put(tag, false);
		}else{
			tagCheckBoxHashMap.put(tag, true);
		}
	}
	
	public HashMap<TagObject, Boolean> getTagCheckBoxHashMap() {
		return tagCheckBoxHashMap;
	}
}
