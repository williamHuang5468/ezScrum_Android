package ntut.mobile.ezscrum.controller.sprint;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.SprintObject;
import ntut.mobile.ezscrum.util.JsonConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class SprintGetAllThread extends BaseThread {
	private String mUrl, mProjectID;
	private List<SprintObject> mSprintList;
	
	public SprintGetAllThread(String projectID) {
		mProjectID = projectID;
		mSprintList = new ArrayList<SprintObject>();
	}

	@Override
	protected void handleWebServiceUrl() {
		// create sprint web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/sprint/all");
		Log.d("Get sprint in project url", mUrl);
	}

	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			boolean responseStatus = checkResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
			if (responseStatus) {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent();
					String sprintListJson = convertStreamToString(instream);
					instream.close();
					mSprintList = JsonConverter.convertJsonToSprintList(sprintListJson);
				}
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", "class:SprintGetAllThread");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class:SprintGetAllThread" + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class:SprintGetAllThread" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	public List<SprintObject> getSprintList() {
		return mSprintList;
	}
}
