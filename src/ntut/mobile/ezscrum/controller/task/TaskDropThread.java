package ntut.mobile.ezscrum.controller.task;

import java.io.IOException;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class TaskDropThread extends BaseThread {
	private String mUrl, mProjectID, mTaskID, mStoryID;

	public TaskDropThread(String projectID, String taskID, String storyID) {
		mProjectID = projectID;
		mTaskID = taskID;
		mStoryID = storyID;
	}
	
	@Override
	protected void handleWebServiceUrl() {
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/task/drop/".concat(mTaskID).concat("/from/").concat(mStoryID));
	}

	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(mUrl);
		httpDelete.addHeader("accept", "application/json");
		httpDelete.addHeader("charset", HTTP.UTF_8);
		try {
			httpClient.execute(httpDelete);
		} catch (IOException e) {
			Log.e("IOException", "class:TaskDropThread" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
}
