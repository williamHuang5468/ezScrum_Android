package ntut.mobile.ezscrum.model;

import java.util.ArrayList;
import java.util.List;

public class StoryObject {
	private String id = "";
	private String name = "";
	private String notes = "";
	private String howToDemo = "";
	private String importance = "";
	private String value = "";
	private String estimation = "";
	private String status = "";
	private String sprint = "";
	private String release = "";
	private String description = "";
	private List<TagObject> tagList = new ArrayList<TagObject>();
	private List<String> taskIDList = new ArrayList<String>();
	private List<TaskObject> taskList = new ArrayList<TaskObject>();
	
	public String get_id() {
		return id;
	}
	
	public void set_id(String id) {
		this.id = id;
	}
	
	public String get_name() {
		return name;
	}
	
	public void set_name(String name) {
		this.name = name;
	}
	
	public String get_notes() {
		return notes;
	}

	public void set_notes(String notes) {
		this.notes = notes;
	}

	public String get_howToDemo() {
		return howToDemo;
	}

	public void set_howToDemo(String howToDemo) {
		this.howToDemo = howToDemo;
	}

	public String get_importance() {
		return importance;
	}
	
	public void set_importance(String importance) {
		this.importance = importance;
	}
	
	public String get_value() {
		return value;
	}
	
	public void set_value(String value) {
		this.value = value;
	}
	
	public String get_estimation() {
		return estimation;
	}
	
	public void set_estimation(String estimation) {
		this.estimation = estimation;
	}
	
	public String get_status() {
		return status;
	}
	
	public void set_status(String status) {
		this.status = status;
	}
	
	public List<TagObject> get_tagList() {
		return tagList;
	}
	
	public void set_tagList(List<TagObject> tagList) {
		this.tagList = tagList;
	}
	
	public List<String> get_taskIDList() {
		return taskIDList;
	}
	
	public void set_taskIDList(List<String> taskList) {
		this.taskIDList = taskList;
	}
	
	public List<TaskObject> get_taskList() {
		return taskList;
	}
	
	public void set_taskList(List<TaskObject> taskList) {
		this.taskList = taskList;
	}
	
	/**
	 * 若tag名稱沒有重複才加入
	 * @param tag
	 * @author pig
	 */
	public void add_tag(TagObject tag) {
		for(int i = 0; i < tagList.size(); i++) {
			if(tagList.get(i).equals(tag)) {
				return;
			}
		}
		tagList.add(tag);
	}
	
	public void add_task(TaskObject task) {
		taskList.add(task);
	}
	
	public void set_task_list(List<TaskObject> taskList) {
		this.taskList = taskList;
	}

	public String get_sprint() {
		return sprint;
	}

	public void set_sprint(String _sprint) {
		this.sprint = _sprint;
	}

	public String get_release() {
		return release;
	}

	public void set_release(String release) {
		this.release = release;
	}

	public String get_description() {
		return description;
	}

	public void set_description(String description) {
		this.description = description;
	}
	
	public void remove_tag(TagObject tag){
		for(int i = 0; i < this.tagList.size(); i++){
			if(tagList.get(i).equals(tag)){
				tagList.remove(i);
				break;
			}
		}
	}
	
	public String toString() {
		String story = 
				"id: " + id + ", " + 
				 "name: " + name + ", " + 
				 "notes: " + notes + ", " + 
				 "howToDemo: " + howToDemo + ", " + 
				 "importance: " + importance + ", " + 
				 "value: " + value + ", " + 
				 "estimation: " + estimation + ", " + 
				 "status: " + status + ", " + 
				 "tagList: " + tagList.toString() + ", " +  
				 "taskIDList: " + taskIDList.toString() + ", " + 
				 "taskList: " + taskList.toString() + ", " + 
				 "sprint: " + sprint;
		return story;
	}
}
