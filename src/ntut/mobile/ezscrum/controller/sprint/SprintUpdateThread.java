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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.google.gson.Gson;

public class SprintUpdateThread extends BaseThread {
	private String mProjectID;
	private SprintObject mSprint;
	private String mResponseString;
	private String mUrl;

	public SprintUpdateThread(String projectID, SprintObject sprint) {
		mProjectID = projectID;
		mSprint = sprint;
		mResponseString = "";
	}
	
	@Override
	protected void handleWebServiceUrl() {
		// update sprint web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/sprint/update");
		Log.d("create sprint url", mUrl);
	}

	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(mUrl);
		httpPut.addHeader("accept", "application/json");
		httpPut.addHeader("charset", HTTP.UTF_8);
		try {
			Gson gson = new Gson();
			StringEntity stringEntity = new StringEntity(gson.toJson(mSprint), HTTP.UTF_8);
			stringEntity.setContentType("application/json");
			httpPut.setEntity(stringEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPut);
			boolean responseStatus = checkResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
			if (responseStatus) {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent();
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
