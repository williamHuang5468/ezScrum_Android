package ntut.mobile.ezscrum.controller.sprintbacklog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

public class SprintBacklogGetStoryIDListThread extends BaseThread{
	private String mProjectID;
	private String mUrl;
	private String mSprintID;
	private String mResponseString;
	private List<String> mStoryIDList;
	private EzScrumJsonReader mJsonReader = new EzScrumJsonReader();

	public SprintBacklogGetStoryIDListThread(String projectID, String sprintID) {
		this.mProjectID = projectID;
		this.mSprintID = sprintID;
		mStoryIDList = new ArrayList<String>();
	}
	
	@Override
	protected void handleWebServiceUrl() {
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/sprint-backlog/".concat(mSprintID).concat("/storylist"));
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
			if (httpStatusCode == HttpURLConnection.HTTP_OK){
				HttpEntity getSprintEntity = httpResponse.getEntity();
				if (getSprintEntity != null) {
					InputStream instream = getSprintEntity.getContent();
					mResponseString = convertStreamToString(instream);
					mStoryIDList = mJsonReader.readSprintBacklogStoryIDList(mResponseString);
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class: SprintBacklogGetThread, method: handleGetSprintInfomation, " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class: SprintBacklogGetThread, method: handleGetSprintInfomation, " + e.toString());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e(e.getClass().getName(), "class: SprintBacklogGetThread, method: handleGetSprintInformation, " + e.toString());
		}
	}
	
	public List<String> getSprintStoryIDList() {
		return mStoryIDList;
	}
}
