package ntut.mobile.ezscrum.controller.task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.TaskObject;
import ntut.mobile.ezscrum.util.JsonConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class TaskGetExistedListThread extends BaseThread{
	private String mUrl, mProjectID;
	private List<TaskObject> mExistedTaskList;

	public TaskGetExistedListThread(String projectID) {
		this.mProjectID = projectID;
		mExistedTaskList = new ArrayList<TaskObject>();
	}
	
	@Override
	protected void handleWebServiceUrl() {
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/task/existed");
	}
	
	@Override
	protected void handleOperation() {
		//	設定http method
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (httpStatusCode == HttpURLConnection.HTTP_OK) {
				HttpEntity getSprintEntity = httpResponse.getEntity();
				if (getSprintEntity != null) {
					InputStream instream = getSprintEntity.getContent();
					String existedTaskListJson = convertStreamToString(instream);
					mExistedTaskList = new JsonConverter().convertJsonToTaskList2(existedTaskListJson);
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class: TaskGetExistedListThread, method: handleGetSprintInfomation, " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class: TaskGetExistedListThread, method: handleGetSprintInfomation, " + e.toString());
			e.printStackTrace();
		}
	}
	
	public List<TaskObject> getExistedTaskList() {
		return mExistedTaskList;
	}
}
