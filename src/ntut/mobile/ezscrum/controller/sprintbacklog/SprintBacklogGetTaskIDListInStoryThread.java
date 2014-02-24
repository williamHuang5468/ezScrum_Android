package ntut.mobile.ezscrum.controller.sprintbacklog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.util.EzScrumJsonReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import android.util.Log;

// 取得所有story的thread
public class SprintBacklogGetTaskIDListInStoryThread extends BaseThread {
	
	private String mUrl;
	private String mProjectID, mSprintID, mStoryID;
	private EzScrumJsonReader mJsonReader = new EzScrumJsonReader();
	private List<String> mTaskIDList;
	
	public SprintBacklogGetTaskIDListInStoryThread(String projectID, String sprintID, String storyID) {
		this.mProjectID = projectID;
		this.mSprintID = sprintID;
		this.mStoryID = storyID;
		mTaskIDList = new ArrayList<String>();
	}
	
	@Override
	protected void handleWebServiceUrl() {
		// 設定取得 Sprint backlog 所有 Story 中的 Task 的  web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/sprint-backlog/".concat(mSprintID).concat("/").concat(mStoryID).concat("/task-id-list"));
	}
	
	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		HttpResponse httpResponse;
		try {
			// Execute HTTP Get Request
			httpResponse = httpClient.execute(httpGet);
			boolean responseStatus = checkResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
			if (responseStatus) {
				HttpEntity storyHttpEntity = httpResponse.getEntity();
				if (storyHttpEntity != null) {
					// 取出response entity並解析json
					InputStream instream = storyHttpEntity.getContent();
					String storyListJson = convertStreamToString(instream);
					instream.close();
					Log.d("Get all items", storyListJson);
					mTaskIDList = mJsonReader.readSprintBacklogTaskList(storyListJson); // 解析json
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException","class:ProductBacklogManager" + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class:ProductBacklogManager" + e.toString());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e("JSONException", "EzScrumJsonReader"+ "method:readProjectListJson" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	public List<String> getTaskIDList() {
		return mTaskIDList;
	}
}