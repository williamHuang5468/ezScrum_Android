package ntut.mobile.ezscrum.controller.story;

import java.io.IOException;
import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.google.gson.Gson;

public class StoryAddExistedTaskThread extends BaseThread {
	private String mUrl, mProjectID, mStoryID;
	private List<String> mTaskIDs;

	public StoryAddExistedTaskThread(String projectID, String storyID, List<String> taskIDs) {
		mProjectID = projectID;
		mStoryID = storyID;
		mTaskIDs = taskIDs;
	}
	
	@Override
	protected void handleWebServiceUrl() {
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/story/".concat(mStoryID).concat("/add-existed-task"));
	}

	@Override
	protected void handleOperation() {
		// 設定http method
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(mUrl);
		httpPost.addHeader("accept", "application/json");
		httpPost.addHeader("charset", HTTP.UTF_8);
		try {
			// 將要加入的所有 task ID 組裝成 json 
			Gson gson = new Gson();
			StringEntity stringEntity = new StringEntity(gson.toJson(mTaskIDs), HTTP.UTF_8);
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class: StoryAddExistedTaskThread, method: handleOperation, " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class: StoryAddExistedTaskThread, method: handleOperation, " + e.toString());
			e.printStackTrace();
		}
	}
}
