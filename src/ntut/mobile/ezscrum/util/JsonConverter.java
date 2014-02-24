package ntut.mobile.ezscrum.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.internal.TaskEnum;
import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TagObject;
import ntut.mobile.ezscrum.model.TaskObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonConverter {
	private static final Gson gson = new Gson();
	
	/**
	 * 將 Tag List 從 Json String 轉成 List<TagObject>
	 * @param tagListJson
	 * @return
	 */
	static public List<TagObject> convertJsonToTagList(String tagListJson) {
		Type type = new TypeToken<ArrayList<TagObject>>(){}.getType();
		List<TagObject> tagList = gson.fromJson(tagListJson, type);
		return tagList;
	}

	/**
	 * 將 Sprint List 從 Json String 轉成 List<SprintObject>
	 * @param sprintListJson
	 * @return
	 */
	static public List<SprintObject> convertJsonToSprintList(String sprintListJson) {
		Type type = new TypeToken<ArrayList<SprintObject>>(){}.getType();
		List<SprintObject> sprintList = gson.fromJson(sprintListJson, type);
		return sprintList;
	}
	
	/**
	 * 將 Sprint 從 Json String 轉成 Object
	 * @param sprintJson
	 * @return
	 */
	static public SprintObject convertJsonToSprint(String sprintJson) {
		return gson.fromJson(sprintJson, SprintObject.class);
	}
	
	/**
	 * 將 TaskObject 轉成 Json String
	 * @param task
	 * @return
	 */
	static public String convertTaskToJson(TaskObject task) {
		return gson.toJson(task);
	}

	/**
	 * 將 Task 從 Json String 轉成 TaskObject
	 * @param taskJson
	 * @return
	 * @throws JSONException
	 */
	static public TaskObject convertJsonToTask(String taskJson) throws JSONException {
		JSONObject task = (JSONObject) new JSONObject(taskJson).get(TaskEnum.TAG_TASKINFORMATION);
		return gson.fromJson(task.toString(), TaskObject.class);
	}
	
	/**
	 * 將 Task List 從 Json String 轉成 List<TaskObject>
	 * @param taskListJson
	 * @return
	 * @throws JSONException
	 */
	static public List<TaskObject> convertJsonToTaskList(String taskListJson) throws JSONException {
		JSONObject taskList = new JSONObject(taskListJson);
		
		JSONArray taskArray = taskList.getJSONArray(TaskEnum.TAG_TASKLIST);
		List<TaskObject> taskObjectList = new ArrayList<TaskObject>();
		for (int i = 0; i < taskArray.length(); i++)
			taskObjectList.add(convertJsonToTask(taskArray.get(i).toString()));
		return taskObjectList;
	}
	
	/**
	 * 將 Task List 從 Json String 轉成 List<TaskObject>
	 * 直接使用 gson 轉換成 list
	 * list 裡面的格式與上面的不同
	 * @param taskListJson
	 * @return
	 */
	static public List<TaskObject> convertJsonToTaskList2(String taskListJson) {
		Type type = new TypeToken<ArrayList<TaskObject>>(){}.getType();
		List<TaskObject> taskObjList = gson.fromJson(taskListJson, type);
		return taskObjList;
	}
	
	/**
	 * 將 Story 從 Json String 轉成 StoryObject
	 * @param storyJson
	 * @return
	 */
	static public StoryObject convertJsonToStory(String storyJson) {
		StoryObject storyObj = gson.fromJson(storyJson, StoryObject.class);
		return storyObj;
	}
	
	/**
	 * 將 Story 從 StoryObject 轉成 Json String
	 * @param storyObj
	 * @return
	 */
	static public String convertStoryToJson(StoryObject storyObj) {
		return gson.toJson(storyObj);
	}

	/**
	 * 將 Story List 從 Json String 轉成 List<StoryObject>
	 * @param List<StoryObject>
	 * @return
	 */
	static public List<StoryObject> convertJsonToStoryList(String storyJsonList) {
		Type type = new TypeToken<ArrayList<StoryObject>>(){}.getType();
		List<StoryObject> storyObjList = gson.fromJson(storyJsonList, type);
		return storyObjList;
	}
}