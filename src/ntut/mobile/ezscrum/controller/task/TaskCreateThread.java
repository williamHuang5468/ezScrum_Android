package ntut.mobile.ezscrum.controller.task;

import java.io.IOException;
import java.io.InputStream;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.util.JsonConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class TaskCreateThread extends BaseThread {
	private String mUrl, mProjectID, mStoryID, mNewTaskID = "";
	private TaskObject mTask;

	public TaskCreateThread(String projectID, String storyID, TaskObject task) {
		mProjectID = projectID;
		mStoryID = storyID;
		mTask = task;
	}
	
	@Override
	protected void handleWebServiceUrl() {
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/task/create/".concat(mStoryID));
	}

	@Override
	protected void handleOperation() {
//		設定http method
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(mUrl);
		httpPost.addHeader("accept", "application/json");
		httpPost.addHeader("charset", HTTP.UTF_8);
		try {
			String newTaskJson = new JsonConverter().convertTaskToJson(mTask);
			StringEntity taskEntity = new StringEntity(newTaskJson, HTTP.UTF_8);
			taskEntity.setContentType("application/json");
			httpPost.setEntity(taskEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			boolean responseStatus = checkResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
			if (responseStatus) {
				HttpEntity taskHttpEntity = httpResponse.getEntity();
				if (taskHttpEntity != null) {
					InputStream instream = taskHttpEntity.getContent();
					mNewTaskID = convertStreamToString(instream);
					instream.close();
				}
			}
		} catch (IOException e) {
			Log.e("IOException", "class:TaskCreateThread" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public String getNewTaskID() {
		return mNewTaskID;
	}
}
