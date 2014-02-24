package ntut.mobile.ezscrum.controller.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.TaskObject;

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

public class TaskUpdateThread extends BaseThread {
	private String mUrl, mProjectID, mResponseString;
	private TaskObject mTask;

	public TaskUpdateThread(String projectID, TaskObject task) {
		mProjectID = projectID;
		mTask = task;
		mResponseString = "";
	}
	
	@Override
	protected void handleWebServiceUrl() {
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/task/update");
	}

	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(mUrl);
		httpPost.addHeader("accept", "application/json");
		httpPost.addHeader("charset", HTTP.UTF_8);
		try {
			Gson gson = new Gson();
			StringEntity stringEntity = new StringEntity(gson.toJson(mTask), HTTP.UTF_8);
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				InputStream instream = httpEntity.getContent();
				mResponseString = convertStreamToString(instream);
				instream.close();
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", "class:TaskUpdateThread");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class:TaskUpdateThread" + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class:TaskUpdateThread" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public boolean updateTask() {
		return Boolean.parseBoolean(mResponseString);
	}
}
