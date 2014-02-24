package ntut.mobile.ezscrum.controller.burndownchart;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import ntut.mobile.ezscrum.controller.BaseThread;
import ntut.mobile.ezscrum.internal.ChartType;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.model.BurndownChartObject;
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

public class TaskBurndownChartThread extends BaseThread {
	private String mProjectID;
	private String mUrl;
	private String mSprintID;
	private List<BurndownChartObject> mBurndownChartObjectList;
	private EzScrumJsonReader mJsonReader = new EzScrumJsonReader();
	
	public TaskBurndownChartThread(String projectID, String sprintID) {
		mProjectID = projectID;
		mSprintID = sprintID;
		mBurndownChartObjectList = new ArrayList<BurndownChartObject>();
	}
	
	@Override
	protected void handleWebServiceUrl() {		
		WebServiceUrlMaker.getInstance().changeProjectID(mProjectID);
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithProjectID("/burndown-chart/".concat(mSprintID).concat("/task-burndown-chart"));
		Log.d("get task burndown chart", mUrl);
	}

	@Override
	protected void handleOperation() {
		//	設定http method
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (httpStatusCode == HttpURLConnection.HTTP_OK) {
				HttpEntity getSprintEntity = httpResponse.getEntity();
				if(getSprintEntity != null){
					InputStream instream = getSprintEntity.getContent();
					String responseString = convertStreamToString(instream);
					mBurndownChartObjectList = mJsonReader.readBurndownChart(responseString, ChartType.TASK);
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "class: TaskBurndownChartThread, method: handleGetTaskBurndownChart, " + e.toString() );
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException", "class: TaskBurndownChartThread, method: handleGetTaskBurndownChart, " + e.toString() );
			e.printStackTrace();
		} catch (JSONException e) {
			Log.e(e.getClass().getName(), "class: TaskBurndownChartThread, method: handleGetTaskBurndownChart, " + e.toString());
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public List<BurndownChartObject> getBurndownChartObject() {
		return mBurndownChartObjectList;
	}
}
