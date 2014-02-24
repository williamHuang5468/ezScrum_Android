package ntut.mobile.ezscrum.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.nfc.FormatException;
import android.util.Base64;
import android.util.Log;

public final class WebServiceUrlMaker {
	private static String EZSCRUM_URL;
	private static WebServiceUrlMaker UNIQUE_INSTANCE = null;
	private final String EZSCRUM_HTTP = "http://";
	private final String EZSCRUM_WEB_SERVICE = "/ezScrum/web-service/";
	private String mAccount, mPassword, mIP, mPort;
	private String mProjectID;
	private String mPrefixUrl, mPostfixUrl;
	
	private WebServiceUrlMaker() {
		changeIPAndPort("", "");
		changeAccountAndPassword("", "");
		changeProjectID("");
	}

	/**
	 * 取得 UNIQUE_INSTANCE，若其為 null 會產生一個出來
	 * @return
	 */
	public static WebServiceUrlMaker getInstance() {
		if (UNIQUE_INSTANCE == null) {
			UNIQUE_INSTANCE = new WebServiceUrlMaker();
		}
		return UNIQUE_INSTANCE;
	}
	
	public String getAccount() {
		return mAccount;
	}
	
	public String getPassword() {
		return mPassword;
	}
	
	public String getProjectID() {
		return mProjectID;
	}
	
	/**
	 * 變更使用者使用者的Server位址資訊
	 * @param account
	 * @param password
	 * @throws FormatException
	 */
	public void changeIPAndPort(String ip, String port) {
		checkStringIsNotNull(ip);
		checkStringIsNotNull(port);
		mIP = ip;
		mPort = port;
		EZSCRUM_URL = EZSCRUM_HTTP.concat(mIP).concat(":").concat(mPort).concat(EZSCRUM_WEB_SERVICE);
	}
	
	/**
	 * 變更使用者的帳號與密碼，之後會更新網址的後段
	 * @param account
	 * @param password
	 * @throws FormatException
	 */
	public void changeAccountAndPassword(String account, String password) {
		checkStringIsNotNull(account);
		checkStringIsNotNull(password);
		mAccount = account;
		mPassword = password;
		updatePostfixUrl();
	}

	/**
	 * 變更當前使用者操作的專案，之後會更新網址的前段
	 * @param account
	 * @param password
	 * @throws FormatException
	 */
	public void changeProjectID(String projectID) {
		checkStringIsNotNull(projectID);
		this.mProjectID = projectID;
		updatePrefixUrl();
	}
	
	/**
	 * 組和出整段的網址並回傳，包含專案ID資訊
	 * @param intermediateUrl 網址的中段
	 * @return 整段的網址
	 */
	public String combineUrlWithProjectID(String intermediateUrl) {
		checkStringIsNotNull(intermediateUrl);
		return mPrefixUrl.concat(intermediateUrl).concat(mPostfixUrl);
	}

	/**
	 * 組和出整段的網址並回傳，不包含專案ID資訊
	 * @param intermediateUrl 網址的中段
	 * @return 整段的網址
	 */
	public String combineUrlWithoutProjectID(String intermediateUrl) {
		checkStringIsNotNull(intermediateUrl);
		return EZSCRUM_URL.concat(intermediateUrl).concat(mPostfixUrl);
	}

	private void updatePrefixUrl() {
		mPrefixUrl = EZSCRUM_URL.concat(encodeUrl(mProjectID));
	}
	
	private void updatePostfixUrl() {
		// user information 加密
		String encodeAccount = Base64.encodeToString(mAccount.getBytes(), Base64.DEFAULT);
		String encodePassword = Base64.encodeToString(mPassword.getBytes(), Base64.DEFAULT);
		// 設定成 mPostfixUrl
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair(UserInformationEnum.TAG_USERNAME,	encodeAccount));
		params.add(new BasicNameValuePair(UserInformationEnum.TAG_PASSWORD,	encodePassword));
		mPostfixUrl = "?".concat(URLEncodedUtils.format(params, "UTF-8"));
	}

	private String encodeUrl(String url) {
		String result = "";
		try {
			result = URLEncoder.encode( url, "UTF-8" ).replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", "class: ProductBacklogItemDeleteThread, method: encodeUrl, " + e.toString() );
			e.printStackTrace();
		}
		return result;
	}

	private void checkStringIsNotNull(String string) throws IllegalArgumentException {
		if (string == null) {
			throw new IllegalArgumentException("The string can not be null!");
		}
	}
}
