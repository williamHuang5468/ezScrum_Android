package ntut.mobile.ezscrum.controller.burndownchart;

import java.util.List;

import android.util.Log;

import ntut.mobile.ezscrum.model.BurndownChartObject;

public class BurndownChartManager {
	public List<BurndownChartObject> getStoryBurndownChart(String projectID, String sprintID) {
		StoryBurndownChartThread burndownChartThread = new StoryBurndownChartThread(projectID, sprintID);
		burndownChartThread.start();
		try {
			burndownChartThread.join();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: BurndownChartManager, " + "method: getStoryBurndownChart" + e.toString());
			e.printStackTrace();
		}
		return burndownChartThread.getBurndownChartObject();
	}
	
	public List<BurndownChartObject> getTaskBurndownChart(String projectID, String sprintID) {
		TaskBurndownChartThread burndownChartThread = new TaskBurndownChartThread(projectID, sprintID);
		burndownChartThread.start();
		try {
			burndownChartThread.join();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: BurndownChartManager, " + "method: getTaskBurndownChart" + e.toString());
			e.printStackTrace();
		}
		return burndownChartThread.getBurndownChartObject();
	}
}
