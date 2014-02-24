package ntut.mobile.ezscrum.database;

import ntut.mobile.ezscrum.internal.EzScrumServerEnum;
import ntut.mobile.ezscrum.internal.UserInformationEnum;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DB使用方式
 * 
 * ->DB.open // 開啟資料庫
 * 
 * 開始對各個table做操作...
 * 
 * ->DB.close // 關閉資料庫
 */
public class DBManager {
	private final String TAG = "DBManager";
	private static final String DATABASE_NAME = "ntut.mobile.ezscrum.db";
	private static final int DATABASE_VERSION = 3;

	private static final String DB_SERVER = "Server";
	private static final String DB_USER = "User";
		
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String SERVER_CREATE =
				"CREATE TABLE IF NOT EXISTS ".concat(DB_SERVER)
						.concat("(")
				        .concat(EzScrumServerEnum.TAG_ID).concat(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				        .concat(EzScrumServerEnum.TAG_NAME).concat(" TEXT NOT NULL,")
				        .concat(EzScrumServerEnum.TAG_IP).concat(" TEXT NOT NULL,")
				        .concat(EzScrumServerEnum.TAG_PORT).concat(" TEXT NOT NULL")
				        .concat(");"); 
		
		private static final String USER_CREATE =
				"CREATE TABLE IF NOT EXISTS ".concat(DB_USER)
						.concat("(")
				        .concat(UserInformationEnum.TAG_ID).concat(" INTEGER PRIMARY KEY AUTOINCREMENT,")
				        .concat(UserInformationEnum.TAG_SERVER_IP).concat(" TEXT NOT NULL,")
				        .concat(UserInformationEnum.TAG_SERVER_PORT).concat(" TEXT NOT NULL,")
				        .concat(UserInformationEnum.TAG_ACCOUNT).concat(" TEXT NOT NULL,")
				        .concat(UserInformationEnum.TAG_PASSWORD).concat(" TEXT NOT NULL,")
				        .concat(UserInformationEnum.TAG_LAST_LOGIN).concat(" INTEGER")
				        .concat(");");  
		
		public DatabaseHelper(Context context) {	
			 super(context, DATABASE_NAME, null, DATABASE_VERSION);	
		}
			
		/**
		 * 建立DB_table
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SERVER_CREATE);
			db.execSQL(USER_CREATE);
		}
		
		/**
		 * 檢查DB_table是否存在
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS ".concat(DB_SERVER));
			db.execSQL("DROP TABLE IF EXISTS ".concat(DB_USER));
		    onCreate(db);
		}
	}
	
	private Context mContext = null;
	private DatabaseHelper mdbHelper ;
	protected SQLiteDatabase mDB;
	
	public DBManager(Context context) {
		this.mContext = context;
	}
	
	/**
	 * 開啟DB
	* @author Zam
	* @time 2013/4/13
	 */
	public DBManager open() throws SQLException {
		mdbHelper = new DatabaseHelper(mContext);
		mDB = mdbHelper.getWritableDatabase();
		return this;
	}

   /**
	* 關閉DB
	* @author Zam
	* @time 2013/4/13
	*/
	public void close() {
		if (mdbHelper != null) {
			mdbHelper.close();
		}
		if (mDB != null) {
			mDB.close();
		}
	}
	
/*
 * ==============================================
 *     			Server Table CRUD
 * ==============================================
 */	
	
   /**
    * 取得所有server的list
	* @author Zam
	* @time 2013/5/9
	*/
	public boolean insertServer(String name, String ip, String port) {
		ContentValues values = new ContentValues();
		values.put(EzScrumServerEnum.TAG_NAME, name);
		values.put(EzScrumServerEnum.TAG_IP, ip);
		values.put(EzScrumServerEnum.TAG_PORT, port);
		return mDB.insert(DB_SERVER, null, values) != -1;
	}	
	
   /**
    * 取得所有server的list
	* @author Zam
	* @time 2013/4/25
	*/
	public Cursor getServerList() {
		return mDB.query(DB_SERVER, null, null, null, null, null, null);
	}
	
	public long getIDByIP(String ip) {
		Cursor cursor = mDB.query(DB_SERVER, null, EzScrumServerEnum.TAG_IP.concat(" = ?"), new String[] {ip}, null, null, null);
		if (cursor.moveToFirst()) {
			return cursor.getLong(cursor.getColumnIndex(EzScrumServerEnum.TAG_ID));
		} else {
			return -1;
		}
	}
	
/*
 * ==============================================
 *     			User Table CRUD
 * ==============================================
 */
	
   /**
	* 1. 如果帳號存在，則將帳號更新為最後一次登入
    * 2. 如果無此帳號，則開一個新帳號
	* @author Zam
	* @param account 使用者帳號
	* @param password 使用者密碼
	* @return 成功回傳true，反之false
	* @time 2013/4/14
	* @TODO 如果使用者改密碼，要額外判斷如果只有密碼錯誤，要改密碼
	* 前提是Server的認證要通過
	*/
	public boolean insertUser(String account, String password, String serverIP, String serverPort) {
		/**
		 * 1. 如果帳號存在，則將帳號更新為最後一次登入
		 * 2. 如果無此帳號，則開一個新帳號
		 */
		ContentValues values = new ContentValues();
		boolean isAccountExist = isAccountExist(account, password, serverIP, serverPort); 
		if (isAccountExist) {
			return updateUserLoginStatus(account, isAccountExist);
		} else {
			values.put(UserInformationEnum.TAG_ACCOUNT, account);
			values.put(UserInformationEnum.TAG_PASSWORD, password);
			// Sqlite boolean用法 >> 1 : true 0 : false
			values.put(UserInformationEnum.TAG_LAST_LOGIN, 1);
			values.put(UserInformationEnum.TAG_SERVER_IP, serverIP);
			values.put(UserInformationEnum.TAG_SERVER_PORT, serverPort);
			return mDB.insert(DB_USER, null, values) != -1;
		}
	}
	
   /**
	* 更新使用者登入登出狀態
	* @param account 使用者帳號, 如果為null, 則將所有登入的帳號登出
	* @param loginStatus true表示登入, false表示登出
	* @return 成功回傳true，反之false
	* @author Zam
	* @time 2013/4/14
	*/
	public boolean updateUserLoginStatus(String account, boolean loginStatus) {
		ContentValues values = new ContentValues();
		String whereClause = null;
		String[] whereArgs = null;
		/**
		 * Sqlite boolean用法 >> 1 : true 0 : false
		 */
		if (loginStatus) {
			values.put(UserInformationEnum.TAG_LAST_LOGIN, 1);
		} else {
			values.put(UserInformationEnum.TAG_LAST_LOGIN, 0);
		}
		
		/**
		 * 如果有傳入帳號，則針對該帳號做操作
		 */
		if (account != null) {
			whereClause = UserInformationEnum.TAG_ACCOUNT.concat(" = ?");
			whereArgs = new String[] {account};
		}
		return mDB.update(DB_USER, values, whereClause, whereArgs) > 0;
	}
	
   /**
	* 取出使用者最後一次登入的帳號
	* @author Zam
	* @time 2013/4/14
	*/
	public Cursor getLastLoginUser() {
		return mDB.query(DB_USER, null, UserInformationEnum.TAG_LAST_LOGIN.concat(" = 1"), null, null, null, null);
	}
	
   /**
	* 確認Account是否存在
	* @author Zam
	* @param account 使用者帳號
	* @time 2013/4/14
	*/
	public boolean isAccountExist(String account, String password, String serverIP, String serverPort) {
		String selection = UserInformationEnum.TAG_ACCOUNT.concat(" = ? and ")
				.concat(UserInformationEnum.TAG_PASSWORD).concat(" = ? and ")
				.concat(UserInformationEnum.TAG_SERVER_IP).concat(" = ? and ")
		.concat(UserInformationEnum.TAG_SERVER_PORT).concat(" = ?");
		Cursor cursor = mDB.query(DB_USER, null, selection, new String[] {account, password, serverIP, serverPort}, null, null, null);
		return cursor.getCount() != 0 ? true : false;
	}
}
