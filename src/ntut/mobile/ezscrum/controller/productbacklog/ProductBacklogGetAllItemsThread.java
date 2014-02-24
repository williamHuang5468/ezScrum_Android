package ntut.mobile.ezscrum.controller.productbacklog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.StoryObject;
import ntut.mobile.ezscrum.util.JsonConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;

// 取得所有story的thread
public class ProductBacklogGetAllItemsThread extends BaseThread {
	
	private String mUrl;
	private String mProjectID;
	private List<StoryObject> mStoryList;
	
	public ProductBacklogGetAllItemsThread(String projectID) {
		this.mProjectID = projectID;
		mStoryList = new ArrayList<StoryObject>();
	}
	
	@Override
	protected void handleWebServiceUrl() {
		// 設定取得 Product backlog 所有 Story 的web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/product-backlog/storylist");
		Log.d("get all story url", mUrl);
	}
	
	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		HttpResponse httpResponse;
		try {
			// Execute HTTP Get Request
			httpResponse = httpClient.execute(httpGet);
			boolean responseStatus = checkResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
			if (responseStatus) {
				HttpEntity storyHttpEntity = httpResponse.getEntity();
				if (storyHttpEntity != null) {
					// 取出response entity並解析json
					InputStream instream = storyHttpEntity.getContent();
					String storyListJson = convertStreamToString(instream);
					instream.close();
					mStoryList = JsonConverter.convertJsonToStoryList(storyListJson);
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException","class:ProductBacklogManager" + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class:ProductBacklogManager" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	public List<StoryObject> getStoryList() {
		return mStoryList;
	}
}