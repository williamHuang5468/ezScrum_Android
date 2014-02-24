package ntut.mobile.ezscrum.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public abstract class BaseThread extends Thread{

	@Override
	public void run() {
		super.run();
		handleWebServiceUrl();
		handleOperation();
	}

	protected abstract void handleWebServiceUrl();
	protected abstract void handleOperation();
	
	protected String convertStreamToString(InputStream instream)throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
		String line = "";
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		return builder.toString();
	}
	
	protected boolean checkResponseStatusCode(int statusCode) {
		if (statusCode == HttpURLConnection.HTTP_OK) {
			return true;
		} else {
			return false;
		}
	}
}
