package ntut.mobile.ezscrum.view.projectlist;

import java.util.List;

import ntut.mobile.ezscrum.model.ProjectObject;
import ntut.mobile.ezscrum.view.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProjectListAdapter extends BaseAdapter{
	private List<ProjectObject> projectList;
	private LayoutInflater inflater;
//	private Context context;

	public ProjectListAdapter( Context context, List<ProjectObject> projectList ) {
//		this.context = context;
		this.projectList = projectList;
		this.inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return this.projectList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.projectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate( R.layout.projectitem, null );
		}
		ProjectObject projectItem = projectList.get( position);
		TextView projectID = (TextView) convertView.findViewById( R.id.projectIDTextView );
		TextView projectDemoDate = (TextView) convertView.findViewById( R.id.projectDemoDateTextView );
		projectID.setText( projectItem.getId() );
		projectDemoDate.setText( projectItem.getDemoDate() );
		convertView.setBackgroundResource(R.drawable.projectlist_background);
		
		return convertView;
	}
	
	/**
	 * 更新專案列表的資訊
	 * @param position
	 * @param projectObject
	 */
	public void refreshProjectList(List<ProjectObject> projectList) {
		this.projectList = projectList;
		this.notifyDataSetChanged();
	}
	
}
