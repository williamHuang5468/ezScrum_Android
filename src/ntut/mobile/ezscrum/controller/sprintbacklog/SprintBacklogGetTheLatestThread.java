package ntut.mobile.ezscrum.controller.sprintbacklog;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class SprintBacklogGetTheLatestThread extends BaseThread {
	private String mProjectID;
	private String mUrl;
	private String mResponseString;

	public SprintBacklogGetTheLatestThread(String projectID) {
		this.mProjectID = projectID;
	}
	
	@Override
	protected void handleWebServiceUrl() {
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/sprint-backlog/the-latest/storylist");
		Log.d("get the latest sprint url", this.mUrl);
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
				HttpEntity getTheLatestSprintEntity = httpResponse.getEntity();
				if (getTheLatestSprintEntity != null) {
					InputStream instream = getTheLatestSprintEntity.getContent();
					mResponseString = convertStreamToString(instream);
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class: SprintBacklogGetTheLatestThread, method: handleGetTheLatestSprintInfomation, " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class: SprintBacklogGetTheLatestThread, method: handleGetTheLatestSprintInfomation, " + e.toString());
			e.printStackTrace();
		}
	}
	
	public String getResponseString() {
		return mResponseString;
	}
}
