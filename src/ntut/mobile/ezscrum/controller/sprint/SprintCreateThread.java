package ntut.mobile.ezscrum.controller.sprint;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.SprintObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.google.gson.Gson;

public class SprintCreateThread extends BaseThread {
	private String mProjectID;
	private SprintObject mSprint;
	private String mResponseString;
	private String mUrl;

	public SprintCreateThread(String projectID,  SprintObject sprint) {
		mProjectID = projectID;
		mSprint = sprint;
		mResponseString = "";
	}
	
	@Override
	protected void handleWebServiceUrl() {
		// create sprint web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/sprint/create");
		Log.d("create sprint url", mUrl);
	}

	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(mUrl);
		httpPost.addHeader("accept", "application/json");
		httpPost.addHeader("charset", HTTP.UTF_8);
		try {
			Gson gson = new Gson();
			StringEntity sprintEntity = new StringEntity(gson.toJson(mSprint), HTTP.UTF_8);
			sprintEntity.setContentType("application/json");
			httpPost.setEntity(sprintEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			boolean responseStatus = checkResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
			if (responseStatus) {
				HttpEntity storyHttpEntity = httpResponse.getEntity();
				if (storyHttpEntity != null) {
					InputStream instream = storyHttpEntity.getContent();
					mResponseString = convertStreamToString(instream);
					instream.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", "class:SprintCreateThread");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class:SprintCreateThread" + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class:SprintCreateThread" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	public String getResponseString() {
		return mResponseString;
	}
}
