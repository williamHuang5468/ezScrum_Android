package ntut.mobile.ezscrum.controller.productbacklog;

import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.model.TagObject;
import ntut.mobile.ezscrum.util.JsonConverter;
import android.util.Log;

public class ProductBacklogItemManager { 
	private String responseString;
	public ProductBacklogItemManager() {}
	
	public List<StoryObject> retrieveExistedProductBacklogAllItems(String projectID) {
		List<StoryObject> allStory = retrieveProductBacklogAllItems(projectID);
		List<StoryObject> existedStory = new ArrayList<StoryObject>();
		for (StoryObject story: allStory) {
			String sprint = story.get_sprint();
			if (sprint.equals("-1") || sprint.equals("0"))
				existedStory.add(story);
		}
		return existedStory;
	}
	
	/**
	 * 取得 product backlog 裡，所有的story
	 * @param userName
	 * @param password
	 * @param projectID
	 * @return
	 */
	public List<StoryObject> retrieveProductBacklogAllItems(String projectID) {
		List<StoryObject> storyList = null;
		ProductBacklogGetAllItemsThread thread = new ProductBacklogGetAllItemsThread(projectID);
		thread.start();
		try {
			thread.join();
			storyList = thread.getStoryList();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: ProductBacklogItemManager, " +
										  "method: retrieveProductBacklogAllItems, "
										  + e.toString() );
			e.printStackTrace();
		}
		return storyList;
	}
	
	/***
	 * 新增 story to product backlog
	 * @param newStory 
	 * @param projectID
	 * @param userName
	 * @param password 
	 */
	public void addProductBacklogItem(String projectID, StoryObject newStory) {
		ProductBacklogItemAddThread addThread = new ProductBacklogItemAddThread(projectID, newStory);
		addThread.start();
		try {
			addThread.join();
			responseString = addThread.getResponseString();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: ProductBacklogItemManager, " +
										  "method: addProductBacklogItem");
			e.printStackTrace();
		}
	}
	
	/***
	 * 刪除  story in product backlog
	 * @param story 
	 * @param projectID 
	 * @param password
	 * @param userName
	 */
	public void deleteProductBacklogItem(String projectID, String storyID) {
		ProductBacklogItemDeleteThread deleteThread = new ProductBacklogItemDeleteThread(projectID, storyID);
		deleteThread.start();
		try {
			deleteThread.join();
			responseString = deleteThread.getResponseString();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: ProductBacklogItemManager, " +
										  "method: deleteProductBacklogItem");
			e.printStackTrace();
		}
	}
	
	/**
	 * add existed story into sprint
	 */
	public void addExistedProductBacklogItem(String projectID, String sprintID, StoryObject story) {
		story.set_sprint(sprintID);
		updateProductBacklogItem(projectID, story);
	}
	
	/**
	 * drop story
	 */
	public void dropProductBacklogItem(String projectID, StoryObject story) {
		story.set_sprint("0");
		updateProductBacklogItem(projectID, story);
	}
	
	/**
	 * update story in product backlog
	 */
	public void updateProductBacklogItem(String projectID, StoryObject updateStory) {
		ProductBacklogItemUpdateThread updateThread = new ProductBacklogItemUpdateThread(projectID, updateStory);
		updateThread.start();
		try {
			updateThread.join();
			responseString = updateThread.getResponseString();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: ProductBacklogItemManager, "
					+ "method: updateProductBacklogItem");
			e.printStackTrace();
		}
	}
	
	/**
	 * 取得單一 story 的資訊
	 * @param userName
	 * @param password
	 * @param projectID
	 * @param storyID
	 */
	public StoryObject retrieveProductBacklogItem(String projectID, String storyID) {
		StoryObject story = null;
		ProductBacklogItemRetrieveThread retrieveThread = new ProductBacklogItemRetrieveThread(projectID, storyID);
		retrieveThread.start();
		try {
			retrieveThread.join();
			String storyJson = retrieveThread.getResponseString();
			story = JsonConverter.convertJsonToStory(storyJson);
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: ProductBacklogItemManager, "
					+ "method: updateProductBacklogItem");
			e.printStackTrace();
		}
		return story;
	}
	
	/**
	 * 取的專案底下所有的tag
	 * @param userName
	 * @param password
	 * @param projectID
	 * @return
	 */
	public List<TagObject> readProductBacklogTagList(String projectID) {
		List<TagObject> tagList = null ;
		ProductBacklogGetTagsThread productBacklogGetTagsThread = new ProductBacklogGetTagsThread(projectID);
		productBacklogGetTagsThread.start();
		try {
			productBacklogGetTagsThread.join();
			tagList = productBacklogGetTagsThread.getTagList();
		} catch (InterruptedException e) {
			Log.e("InterruptedException", "class: ProductBacklogItemManager, "
										+ "method: readProductBacklogTagList");
			e.printStackTrace();
		}
		return tagList;
	}
	
	public String getResponseString() {
		return responseString;
	}
	
}
