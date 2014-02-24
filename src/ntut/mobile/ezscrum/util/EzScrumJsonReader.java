package ntut.mobile.ezscrum.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ntut.mobile.ezscrum.internal.BurndownChartEnum;
import ntut.mobile.ezscrum.internal.ChartType;
import ntut.mobile.ezscrum.internal.ProductBacklogEnum;
import ntut.mobile.ezscrum.internal.SprintBacklogEnum;
import ntut.mobile.ezscrum.internal.SprintPlanEnum;
import ntut.mobile.ezscrum.model.BurndownChartObject;
import ntut.mobile.ezscrum.model.ProjectObject;
import ntut.mobile.ezscrum.model.SprintObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EzScrumJsonReader {

	public EzScrumJsonReader() {
	}
	
	/****
	 * 解析Web Service Project JSON String
	 * 建立Project Object的List
	 * @param projectListJson
	 * @return
	 * @throws JSONException
	 */
	public List<ProjectObject> readProjectListJson( String projectListJson ) throws JSONException{
		List<ProjectObject> projectList = new ArrayList<ProjectObject>();
		JSONObject projectJSONObject = new JSONObject( projectListJson );
		JSONArray projects = projectJSONObject.getJSONArray("Projects");
		for (int i = 0; i < projects.length(); i++) {
			JSONObject project = projects.getJSONObject(i);
			JSONObject projectProperty = project.getJSONObject("Project");
			//	建立個別專案資料
			ProjectObject projectObject = new ProjectObject();
			projectObject.setId( projectProperty.getString("id") );
			projectObject.setName( projectProperty.getString("Name") );
			projectObject.setComment( projectProperty.getString("Comment") );
			projectObject.setProjectManager( projectProperty.getString("ProjectManager") );
			projectObject.setCreateDate( projectProperty.getString("CreateDate") );
			projectObject.setDemoDate( projectProperty.getString("DemoDate") );
			projectList.add( projectObject );
		}
		return projectList;
	}
	
	public List<SprintObject> readSprintBacklog( String sprintListJson ) throws JSONException{
		List<SprintObject> sprintList = new ArrayList<SprintObject>();
		JSONObject sprintPlanList = new JSONObject( sprintListJson );
		JSONArray sprintPlanArray = sprintPlanList.getJSONArray( SprintPlanEnum.TAG_SPRINTPLANLIST );
		if( sprintPlanArray.length() == 0 ){
			SprintObject sprintObject = new SprintObject();
			sprintObject.setSprintID( "" );
			sprintObject.setSprintGoal( "" );
			sprintObject.setStartDate( "" );
			sprintObject.setDemoDate( "No Plan!" );
			sprintObject.setDemoPlace( "" );
			sprintObject.setHoursCanCommit( "" );
			sprintObject.setInterval( "" );
			sprintObject.setMembers( "" );
			sprintList.add( sprintObject );
		}else{
			for( int i = 0; i < sprintPlanArray.length(); i++ ){
				JSONObject sprint = sprintPlanArray.getJSONObject( i );
				JSONObject sprintProperty = sprint.getJSONObject( SprintPlanEnum.TAG_SPRINTPLAN );
				
				SprintObject sprintObject = new SprintObject();
				sprintObject.setSprintID( sprintProperty.getString( SprintPlanEnum.TAG_ID ) );
				sprintObject.setSprintGoal( sprintProperty.getString( SprintPlanEnum.TAG_SPRINTGOAL ) );
				sprintObject.setStartDate( sprintProperty.getString( SprintPlanEnum.TAG_STARTDATE) );
				sprintObject.setDemoDate( sprintProperty.getString( SprintPlanEnum.TAG_DEMODATE) );
				sprintObject.setDemoPlace( sprintProperty.getString( SprintPlanEnum.TAG_DEMOPLACE ) );
				sprintObject.setHoursCanCommit( sprintProperty.getString( SprintPlanEnum.TAG_HOURSCANCOMMIT ) );
				sprintObject.setInterval( sprintProperty.getString( SprintPlanEnum.TAG_INTERVAL ) );
				sprintObject.setMembers( sprintProperty.getString( SprintPlanEnum.TAG_MEMBERS ) );
				sprintList.add( sprintObject );
			}
		}
		
		return sprintList;
	}
	
	public List<String> readSprintBacklogStoryIDList( String storyIDListJson ) throws JSONException {
		List<String> storyIDList = new ArrayList<String>();
		JSONObject storyIDObject = new JSONObject( storyIDListJson );
		JSONArray storyIDArray = storyIDObject.getJSONArray(SprintPlanEnum.TAG_STORYIDLIST);
		for (int i = 0; i < storyIDArray.length(); i++) {
			String s = storyIDArray.getString(i);
			storyIDList.add(s);
		}
		return storyIDList;
	}
	
	/**
	 * 解析 task id list 的 json String
	 * @param taskIDListJson
	 * @return
	 * @throws JSONException
	 */
	public List<String> readSprintBacklogTaskList(String taskIDListJson) throws JSONException {
		List<String> taskIDList = new ArrayList<String>();
		JSONObject storyJson = new JSONObject(taskIDListJson).getJSONObject(ProductBacklogEnum.TAG_STORY);
		JSONArray taskIDJsonArray = storyJson.getJSONArray(SprintBacklogEnum.TAG_TASKIDLIST);
		for (int i = 0; i < taskIDJsonArray.length(); i++) {
			String s = taskIDJsonArray.getString(i);
			taskIDList.add(s);
		}
		return taskIDList;
	}
	
	/**
	 * 解析tag list 的json String
	 * @param tagListJson
	 * @return
	 * @throws JSONException
	 */
	public List<String> readProductBacklogTagList( String tagListJson ) throws JSONException{
		List<String> tagList = new ArrayList<String>();
		JSONObject tagJSon = new JSONObject(tagListJson);
		JSONArray tagJSonArray = tagJSon.getJSONArray(ProductBacklogEnum.TAG_TAGLIST);
		for( int i = 0; i < tagJSonArray.length(); i++ ){
			tagList.add( tagJSonArray.getString(i) );
		}
		return tagList;
	}
	
	/**
	 * 解析web service Burndown chart JSON 
	 * @param burndownChartJson
	 * @param chartType
	 * @return
	 * @throws JSONException
	 */
	public List<BurndownChartObject> readBurndownChart( String burndownChartJson, ChartType chartType ) throws JSONException {
		List<BurndownChartObject> burndownChartObjectList = new ArrayList<BurndownChartObject>();
		JSONObject burndownChartobject = new JSONObject( burndownChartJson );
		JSONArray burndownChartArray = burndownChartobject.getJSONArray(BurndownChartEnum.TAG_BURNDOWNCHART);
		
		for (int i = 0; i < burndownChartArray.length(); i++) {
			JSONObject data = burndownChartArray.getJSONObject(i);
			String dateStr = data.getString(BurndownChartEnum.TAG_DATE);
			String point = data.getString(BurndownChartEnum.TAG_POINT);
			Date date = new Date();
			
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				date = sdf.parse(dateStr);
				burndownChartObjectList.add(new BurndownChartObject(point, date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		return burndownChartObjectList;
	}
	
	/***
	 * 解析目前正在進行的Sprint information
	 * @param responseString
	 * @return
	 * @throws JSONException 
	 */
	public SprintObject readSprintBacklogCurrentSprintInformation( String currentSprintJson ) throws JSONException {
		if (currentSprintJson.trim().equals(""))
			return null;
		JSONObject jsonObject = new JSONObject( currentSprintJson );
		JSONObject currentSprintProperty = jsonObject.getJSONObject( SprintPlanEnum.TAG_CURRENTSPRINTPLAN );
				
		SprintObject currentSprint = new SprintObject();
		currentSprint.setSprintID( currentSprintProperty.getString( SprintPlanEnum.TAG_ID ) );
		currentSprint.setSprintGoal( currentSprintProperty.getString( SprintPlanEnum.TAG_SPRINTGOAL ) );
		currentSprint.setStartDate( currentSprintProperty.getString( SprintPlanEnum.TAG_STARTDATE) );
		currentSprint.setDemoDate( currentSprintProperty.getString( SprintPlanEnum.TAG_DEMODATE) );
		currentSprint.setDemoPlace( currentSprintProperty.getString( SprintPlanEnum.TAG_DEMOPLACE ) );
		currentSprint.setHoursCanCommit( currentSprintProperty.getString( SprintPlanEnum.TAG_HOURSCANCOMMIT ) );
		currentSprint.setInterval( currentSprintProperty.getString( SprintPlanEnum.TAG_INTERVAL ) );
		currentSprint.setMembers( currentSprintProperty.getString( SprintPlanEnum.TAG_MEMBERS ) );
		return currentSprint;
	}
}
