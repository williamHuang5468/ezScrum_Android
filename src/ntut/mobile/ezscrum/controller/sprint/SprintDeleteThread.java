package ntut.mobile.ezscrum.controller.sprint;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

public class SprintDeleteThread extends BaseThread {
	private String mProjectID, mSprintID;
	private String mResponseString;
	private String mUrl;

	public SprintDeleteThread(String projectID, String sprintID) {
		mProjectID = projectID;
		mSprintID = sprintID;
		mResponseString = "";
	}
	
	@Override
	protected void handleWebServiceUrl() {
		// delete sprint web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/sprint/delete/".concat(mSprintID));
		Log.d("Delete sprint url", mUrl);
	}

	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(mUrl);
		httpDelete.addHeader("accept", "application/json");
		httpDelete.addHeader("charset", HTTP.UTF_8);
		try {
			HttpResponse httpResponse = httpClient.execute(httpDelete);
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
