package ntut.mobile.ezscrum.controller.login;

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

import com.google.gson.Gson;

public class CheckLoginInfoThread extends BaseThread {
	private String mUrl;
	private boolean mIsLoginInfoCorrect = false;	// 預設為false, 防止Gson發生exception沒有拿到boolean值

	public CheckLoginInfoThread() {
	}

	@Override
	protected void handleWebServiceUrl() {
		mUrl = WebServiceUrlMaker.getInstance().combineUrlWithoutProjectID("user/login");
		System.out.println("mUrl: " + mUrl);
	}

	@Override
	protected void handleOperation() {
		// 設定http method
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(mUrl);
		httpGet.addHeader("accept", "application/json");
		httpGet.addHeader("charset", HTTP.UTF_8);
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGet);
			int httpStatusCode = httpResponse.getStatusLine().getStatusCode();
			if (httpStatusCode == HttpURLConnection.HTTP_OK) {
				HttpEntity getLoginEntity = httpResponse.getEntity();
				if (getLoginEntity != null) {
					InputStream instream = getLoginEntity.getContent();
					String response = convertStreamToString(instream);
					mIsLoginInfoCorrect = new Gson().fromJson(response, Boolean.class);
					instream.close();
				}
			}
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException",
					"class: CheckLoginInfoThread, method: handleCheckLoginInfo, "
							+ e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("IOException",
					"class: CheckLoginInfoThread, method: handleCheckLoginInfo, "
							+ e.toString());
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	public boolean getIsLoginInfoCorrect() {
		return mIsLoginInfoCorrect;
	}
}
