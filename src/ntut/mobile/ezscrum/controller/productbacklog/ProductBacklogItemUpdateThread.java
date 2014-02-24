package ntut.mobile.ezscrum.controller.productbacklog;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.StoryObject;

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

// 更新Story的thread
public class ProductBacklogItemUpdateThread extends BaseThread {
	private String mProjectID;
	private String mUrl;
	private StoryObject mEditedStory;
	private String mResponseString;
	
	public ProductBacklogItemUpdateThread(String projectID, StoryObject story) {
		this.mProjectID = projectID;
		this.mEditedStory = story;
	}

	@Override
	protected void handleWebServiceUrl() {
		// 更新 story web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/story/update");
	}
	
	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(mUrl);
		httpPut.addHeader("accept", "application");
		httpPut.addHeader("charset", HTTP.UTF_8);
		Gson gson = new Gson();
		try {
			StringEntity storyEntity = new StringEntity(gson.toJson(mEditedStory), HTTP.UTF_8);
			storyEntity.setContentType("application/json");
			httpPut.setEntity(storyEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPut);
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
			Log.e("UnsupportedEncodingException", "class:ProductBacklogItemManager");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class:ProductBacklogItemManager"
					+ e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class:ProductBacklogItemManager" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public String getResponseString() {
		return mResponseString;
	}
}
	