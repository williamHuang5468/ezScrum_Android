package ntut.mobile.ezscrum.controller.login;

import ntut.mobile.ezscrum.database.DBManager;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import android.content.Context;
import android.util.Log;

public class LoginManager {
	private static Context mContext; 
	private String mAccount;
	private String mPassword;
	private String mServerIP;
	private String mServerPort;
	private DBManager mDBManager;

	public LoginManager(Context context, String userName, String passwd, String ip, String port) {
		mContext = context;
		mAccount = userName;
		mPassword = passwd;
		mServerIP = ip;
		mServerPort = port;
		mDBManager = new DBManager(mContext);
	}
	
	/**
	 * 確認是否在伺服器端有此帳密
	 * @param account 帳號
	 * @param password 密碼
	 * @author Zam
	 * @time 2013/4/14
	 */
	public boolean checkLoginInfo() {
		CheckLoginInfoThread getThread = new CheckLoginInfoThread();
		boolean isLoginInfoCorrect = false;
		boolean result = false;
		WebServiceUrlMaker.getInstance().changeAccountAndPassword(mAccount, mPassword);
		getThread.start();
		try {
			getThread.join();
			isLoginInfoCorrect = getThread.getIsLoginInfoCorrect();
			
			/**
			 * 輸入正確，則輸入資料庫(若資料庫已存在此資料，則更新為最後一次登入的帳號) 
			 */
			if (isLoginInfoCorrect) {
				mDBManager.open();
				result = mDBManager.insertUser(mAccount, mPassword, mServerIP, mServerPort);
				mDBManager.close();
			}
			return result;
		} catch (InterruptedException e) {
			Log.e("InterruptedException:", "Class: LoginManager, method: checkLoginInfo, " + e.toString());
			e.printStackTrace();
		}
		return isLoginInfoCorrect;
	}
}
