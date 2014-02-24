package ntut.mobile.ezscrum.view.projectlist;

import ntut.mobile.ezscrum.internal.ChartType;
import ntut.mobile.ezscrum.model.ProjectObject;
import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.view.FeatureViewActivity;
import ntut.mobile.ezscrum.view.R;
import ntut.mobile.ezscrum.view.burndownchat.BurndownChart;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProjectListItemInfoFragment extends Fragment{
	
	private LinearLayout storyBurndownChart;
	private LinearLayout taskBurndownChart;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.projectlist_rightview, container, false);
	}
	public void loadProjectListItemInfo( final ProjectObject projectObject, SprintObject theLatestSprintObject ){
		//	與 activity 溝通，取得projectListItemInfo view ，並更改值。
		View projectItemView = getActivity().findViewById( R.id.projectListItemInfoFragment );
		//	projectDescription
		TextView projectDescription = (TextView) projectItemView.findViewById( R.id.projectDescriptionTextView );
		TextView projectIDDescription = (TextView) projectItemView.findViewById( R.id.projectDescriptionIDTextView );
		TextView comment = (TextView) projectItemView.findViewById( R.id.projectDescriptionCommentTextView );
		TextView projectIDDescriptionLabel = (TextView) projectItemView.findViewById( R.id.projectDescriptionIDLabelTextView );
		TextView commentLabel = (TextView) projectItemView.findViewById( R.id.projectDescriptionCommentLabelTextView );
		//	sprintDescription
		TextView demoDateLabel = (TextView) projectItemView.findViewById( R.id.projectDemoDateLabelTextView );
		TextView sprintGoalLabel = (TextView) projectItemView.findViewById( R.id.projectSprintGoalLabelTextView );
		TextView storyPointsLabel = (TextView) projectItemView.findViewById( R.id.projectStoryPointsLabelTextView );
		TextView taskPointsLabel = (TextView) projectItemView.findViewById( R.id.projectTaskPointsLabelTextView );
		TextView sprintDescription = (TextView) projectItemView.findViewById( R.id.sprintDescriptionTextView );
		TextView demoDate = (TextView) projectItemView.findViewById( R.id.projectDemoDateTextView );
		TextView sprintGoal = (TextView) projectItemView.findViewById( R.id.projectSprintGoalTextView );
		TextView storyPoints = (TextView) projectItemView.findViewById( R.id.projectStoryPointsTextView );
		TextView taskPoints = (TextView) projectItemView.findViewById( R.id.projectTaskPointsTextView );
		//	burndownChart
		TextView burndownChart = (TextView) projectItemView.findViewById( R.id.burndownChartTextView );
		storyBurndownChart = (LinearLayout) projectItemView.findViewById( R.id.storyBurndownChart );
		taskBurndownChart = (LinearLayout) projectItemView.findViewById( R.id.taskBurndownChart );
		
		Button enterProjBtn = (Button) projectItemView.findViewById(R.id.enterFeatureBtn);
		
		projectDescription.setBackgroundResource(R.drawable.border);
		projectDescription.setText( "Project Description" );
		projectIDDescriptionLabel.setText( "Project Name : ");
		projectIDDescription.setText( projectObject.getName() );
		commentLabel.setText( "Comment : ");
		comment.setText( projectObject.getComment() );
		sprintDescription.setBackgroundResource(R.drawable.border);
		sprintDescription.setText( "Sprint Description" );
		demoDateLabel.setText( "Demo Date : " );
		sprintGoalLabel.setText( "Sprint Goal : " );
		storyPointsLabel.setText( "Story Points : ");
		taskPointsLabel.setText( "Task Points : ");
		if (theLatestSprintObject != null) {
			demoDate.setText(theLatestSprintObject.getDemoDate());
			sprintGoal.setText(theLatestSprintObject.getSprintGoal());
		} else {
			demoDate.setText("");
			sprintGoal.setText("");
		}
		storyPoints.setText( "story points is missing" );
		taskPoints.setText( "task points is missing" );
		burndownChart.setBackgroundResource(R.drawable.border);
		burndownChart.setText( "Burndown Chart" );
		enterProjBtn.setVisibility(1);
		enterProjBtn.setOnClickListener(new OnClickListener() {		
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("projectID", projectObject.getId());
				intent.setClass(getActivity(), FeatureViewActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	/**
	 * 取得並設定burndown chart的view
	 * @param projectObject
	 * @param theLatestSprintObject
	 */
	public void loadBurndownChart(ProjectObject projectObject, SprintObject theLatestSprintObject) {
		storyBurndownChart.removeAllViews();
		taskBurndownChart.removeAllViews();
		if (theLatestSprintObject != null) {
			View burndownChartStoryView = new BurndownChart(getActivity().getApplicationContext(),
					ChartType.STORY, projectObject.getId(), theLatestSprintObject.getSprintID()).getBurndownChartView();
			View burndownChartTaskView = new BurndownChart(getActivity().getApplicationContext(),
					ChartType.TASK, projectObject.getId(), theLatestSprintObject.getSprintID()).getBurndownChartView();
			storyBurndownChart.addView(burndownChartStoryView);
			taskBurndownChart.addView(burndownChartTaskView);
		}
	}
}
