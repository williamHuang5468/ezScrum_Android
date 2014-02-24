package ntut.mobile.ezscrum.controller.projectlist;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.ProjectObject;
import ntut.mobile.ezscrum.util.EzScrumJsonReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;

import android.util.Log;

public class ProjectListGetThread extends BaseThread {
	private String mUrl;
	private List<ProjectObject> mProjectList;
	private EzScrumJsonReader mJsonReader;

	public ProjectListGetThread() {
		this.mJsonReader = new EzScrumJsonReader();
		this.mProjectList = new ArrayList<ProjectObject>();
	}
	
	@Override
	protected void handleWebServiceUrl() {
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithoutProjectID("projects");
		Log.d("get project list url", mUrl);
	}
	
	@Override
	protected void handleOperation() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		// Execute HTTP get Request
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGet);
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if(httpStatusCode == HttpURLConnection.HTTP_OK) {
				HttpEntity projectHttpEntity = httpResponse.getEntity();
				if (projectHttpEntity != null) {
					// 取出response entity並解析json
					InputStream instream = projectHttpEntity.getContent();
					String projectListJson = convertStreamToString(instream);
					instream.close();
					mProjectList = mJsonReader.readProjectListJson(projectListJson);	//	解析json
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException","class:ProjectListManager"  + e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException","class:ProjectListManager"  + e.toString());
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e("JSONException","EzScrumJsonReader"  + "method:readProjectListJson" + e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	/**
	 * 取得所有專案資訊。
	 * @return 專案清單
	 */
	public List<ProjectObject> getProjectListData() {
		return mProjectList;
	}
}
