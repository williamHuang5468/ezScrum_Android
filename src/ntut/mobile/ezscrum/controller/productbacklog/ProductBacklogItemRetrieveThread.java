package ntut.mobile.ezscrum.controller.productbacklog;

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

// 更新Story的thread
public class ProductBacklogItemRetrieveThread extends BaseThread {
	private String mProjectID;
	private String mUrl;
	private String mStoryID;
	private String mResponseString;
	
	public ProductBacklogItemRetrieveThread(String projectID, String storyID) {
		this.mProjectID = projectID;
		this.mStoryID = storyID;
	}
	
	@Override
	protected void handleWebServiceUrl() {
		// 取得單一 story web service url
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/product-backlog/storylist/".concat(mStoryID));
	}
	
	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		try {
			// Execute HTTP Get Request
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (httpStatusCode == HttpURLConnection.HTTP_OK) {
				HttpEntity storyHttpEntity = httpResponse.getEntity();
				if (storyHttpEntity != null) {
					// 取出response entity並解析json
					InputStream instream = storyHttpEntity.getContent();
					mResponseString = convertStreamToString(instream);
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException","class: ProductBacklogItemRetrieveThread" + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class: ProductBacklogItemRetrieveThread" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public String getResponseString() {
		return mResponseString;
	}

}
	