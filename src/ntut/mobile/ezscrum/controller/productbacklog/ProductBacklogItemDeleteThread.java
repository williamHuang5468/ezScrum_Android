package ntut.mobile.ezscrum.controller.productbacklog;

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

public class ProductBacklogItemDeleteThread extends BaseThread{
	private String mProjectID, mStoryID;
	private String mUrl;
	private String responseString;
	public ProductBacklogItemDeleteThread(String projectID, String storyID){
		mProjectID = projectID;
		mStoryID = storyID;
	}

	@Override
	protected void handleWebServiceUrl() {
		// 刪除 story web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/product-backlog/storylist/".concat(mStoryID));
		Log.d("delete story url", mUrl);
	}
	
	@Override
	protected void handleOperation() {
		// 設定http method
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(mUrl);
		httpDelete.addHeader("accept", "application/json");
		httpDelete.addHeader("charset", HTTP.UTF_8);
		try {
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			boolean responseStatus = checkResponseStatusCode(httpResponse.getStatusLine().getStatusCode());
			if (responseStatus) {
				HttpEntity deleteStoryEntity = httpResponse.getEntity();
				if ( deleteStoryEntity != null ) {
					// 取出response entity
					InputStream instream = deleteStoryEntity.getContent();
					responseString = convertStreamToString(instream);
					instream.close();
				}
			}
		} catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", "class: ProductBacklogItemDeleteThread, method: handleDeleteStory, " + e.toString());
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class: ProductBacklogItemDeleteThread, method: handleDeleteStory, " + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class: ProductBacklogItemDeleteThread, method: handleDeleteStory, " + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public String getResponseString() {
		return responseString;
	}
}
