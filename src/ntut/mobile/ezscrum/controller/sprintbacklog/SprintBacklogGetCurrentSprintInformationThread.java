package ntut.mobile.ezscrum.controller.sprintbacklog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.SprintObject;
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

public class SprintBacklogGetCurrentSprintInformationThread extends BaseThread {
	private String mProjectID;
	private String mUrl;
	private EzScrumJsonReader mJsonReader;
	private SprintObject mCurrentSprint;

	public SprintBacklogGetCurrentSprintInformationThread(String projectID) {
		this.mProjectID = projectID;
		this.mJsonReader = new EzScrumJsonReader();
		this.mCurrentSprint = null;
	}
	
	@Override
	protected void handleWebServiceUrl() {
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/sprint-backlog/current-sprint");
		Log.d("get the current sprint url", this.mUrl);
	}
	
	@Override
	protected void handleOperation() {
		//	設定http method
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		try {
			HttpResponse httpResponse = httpClient.execute( httpGet );
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (httpStatusCode == HttpURLConnection.HTTP_OK) {
				HttpEntity getTheCurrentSprintInfoEntity = httpResponse.getEntity();
				if (getTheCurrentSprintInfoEntity != null) {
					InputStream instream = getTheCurrentSprintInfoEntity.getContent();
					String responseString = convertStreamToString(instream);
					mCurrentSprint = mJsonReader.readSprintBacklogCurrentSprintInformation(responseString);
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class: SprintBacklogGetCurrentSprintInformationThread, method: handleGetTheCurrentSprintInfomation, " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class: SprintBacklogGetCurrentSprintInformationThread, method: handleGetTheCurrentSprintInfomation, " + e.toString());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e("JSONException", "class: SprintBacklogGetCurrentSprintInformationThread, method: handleGetTheCurrentSprintInfomation, " + e.toString());
			e.printStackTrace();
		}
	}

	public SprintObject getCurrentSprint() {
		return mCurrentSprint;
	}
}
