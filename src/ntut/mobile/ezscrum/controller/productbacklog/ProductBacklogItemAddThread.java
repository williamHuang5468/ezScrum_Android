package ntut.mobile.ezscrum.controller.productbacklog;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.util.JsonConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

// 新增Story的thread
public class ProductBacklogItemAddThread extends BaseThread {
	private String mProjectID;
	private StoryObject mNewStory;
	private String mResponseString;
	private String mUrl;
	
	public ProductBacklogItemAddThread(String projectID, StoryObject story) {
		mProjectID = projectID;
		mNewStory = story;
	}
	
	@Override
	protected void handleWebServiceUrl() {
		// 新增 Story web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/story/create");
	}
	
	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(mUrl);
		httpPost.addHeader("accept", "application/json");
		httpPost.addHeader("charset", HTTP.UTF_8);
		try {
			String newStoryJson = JsonConverter.convertStoryToJson(mNewStory);
			StringEntity stringEntity = new StringEntity(newStoryJson, HTTP.UTF_8);
			stringEntity.setContentType("application/json");
			httpPost.setEntity(stringEntity);
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			boolean responseStatus = checkResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
			if (responseStatus) {
				HttpEntity httpEntity = httpResponse.getEntity();
				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent();
					mResponseString = convertStreamToString(instream);		// storyID 和 status
					instream.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", "class:ProductBacklogItemManager");
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class:ProductBacklogItemManager" + e.toString());
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
	