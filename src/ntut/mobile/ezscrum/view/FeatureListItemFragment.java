package ntut.mobile.ezscrum.view;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.productbacklog.ProductBacklogItemManager;
import ntut.mobile.ezscrum.view.productbacklog.ProductBacklogListViewActivity;
import ntut.mobile.ezscrum.view.sprintbacklog.SprintBacklogListViewActivity;
import ntut.mobile.ezscrum.view.sprintplan.SprintPlanListViewActivity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FeatureListItemFragment extends ListFragment{
	
	private FeatureListItemListener itemListener;
	private List<String> featureList;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		featureList = new ArrayList<String>();
		featureList.add("Product Backlog");
		featureList.add("Sprint Plan");
		featureList.add("Sprint Backlog");
		featureList.add("TaskBoard");
		setListAdapter(getAdapter(getActivity().getApplicationContext()));
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		l.setItemChecked(position, true);
	    // 取得並傳遞project ID給下一個activity
		String projectID = getActivity().getIntent().getExtras().getString("projectID");
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("projectID", projectID);
		intent.putExtras(bundle);
		
		if (itemListener != null) {
			itemListener.onFeatureListItemSelected(featureList.get(position));
		}
		
		// 當點擊Product Backlog, 進入該activity
		if (featureList.get(position).equals("Product Backlog")) {
			intent.setClass(getActivity(), ProductBacklogListViewActivity.class);
			startActivity(intent);
		}
		
		// 當點擊 Sprint Plan , 進入該 activity
		if (featureList.get(position).equals("Sprint Plan")) {
			intent.setClass(getActivity(), SprintPlanListViewActivity.class);
			startActivity(intent);
		}
		
		// 當點擊Sprint Backlog, 進入該activity
		if (featureList.get(position).equals("Sprint Backlog")) {
			intent.setClass(getActivity(), SprintBacklogListViewActivity.class);
			startActivity(intent);
		}
		
		// 當點擊Product Backlog, 進入該activity
		if (featureList.get(position).equals("TaskBoard")) {
//			SprintBacklogAddExistedStoryDialog addExistedStoryDialog = new SprintBacklogAddExistedStoryDialog( getActivity(), projectID );
//			addExistedStoryDialog.show();
			ProductBacklogItemManager pbim = new ProductBacklogItemManager();
			pbim.readProductBacklogTagList(projectID);
		}
	}
	
	
	public BaseAdapter getAdapter(final Context context) {		
		return new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = LayoutInflater.from(context).inflate(R.layout.featuretitem, null );
				}
				TextView tv = (TextView) convertView.findViewById(R.id.featureItemTextView);
				tv.setText(featureList.get(position));
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				return featureList.get(position);
			}
			
			@Override
			public int getCount() {
				return featureList.size();
			}
		};
	}
	public void setFeatureListItemListener(FeatureListItemListener listener) {
		itemListener = listener;
	}
}
