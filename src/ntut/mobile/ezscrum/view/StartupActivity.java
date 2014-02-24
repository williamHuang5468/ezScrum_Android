package ntut.mobile.ezscrum.view;

import ntut.mobile.ezscrum.controller.login.LoginManager;
import ntut.mobile.ezscrum.database.DBManager;
import ntut.mobile.ezscrum.internal.EzScrumServerEnum;
import ntut.mobile.ezscrum.internal.UserInformationEnum;
import ntut.mobile.ezscrum.internal.WebServiceUrlMaker;
import ntut.mobile.ezscrum.view.projectlist.ProjectListViewActivity;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 起始畫面
 * 一開始會先顯示logo
 * 等到確認使用者有無上次的登入資料
 * 1. 若有直接轉換到專案列表
 * 2. 若無則加載登入頁面
 */
public class StartupActivity extends Activity {
	// 追蹤login任務以便要取消login
	private UserLoginTask mAuthTask = null;
	// 存放login輸入的帳密
	private String mAccount;
	private String mPassword;
	private String mServerIP;
	private String mServerPort;
	// UI references.
	private EditText mAccountView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private Spinner mServerSpinner;
	private Button mAddServerButton;
	private Handler mHandler;
	private final int mDelay = 1000;
	private DBManager mDBManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		mDBManager = new DBManager(StartupActivity.this);
		/**
		 *  用thread開出去做，判斷使用者是否有登入
		 */
		mHandler = new Handler();
		mHandler.postDelayed(startupRunner, mDelay);	// 延遲一些時間，讓使用者可以看到ezScrum logo
	}
	
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	// 將工作移除
    	if (mHandler != null) {
    		mHandler.removeCallbacks(startupRunner);
    	}
    }
    
    /**
     * 檢查使用者是否有登入過
     * @author Zam
     * @time 2013/4/18
     */
	private Runnable startupRunner = new Runnable() {
		@Override
		public void run() {
			/**
			 * 1. 有登入過直接到project list頁面
			 * 2. 無登入則跳轉到登入頁面
			 */
			if (isLogin()) {
				startActivity(new Intent(StartupActivity.this, ProjectListViewActivity.class));
				finish();
			} else {
				initLoginServer();
				initLoginForm();
			}			
		}
		
		private boolean isLogin() {
			mDBManager.open();
			Cursor cursor = mDBManager.getLastLoginUser();
			// 若有拿到資料，代表存在已登入過的User
			boolean isLogin = cursor.getCount() != 0 ? true : false;
			if (isLogin) {
				cursor.moveToFirst();
				mServerIP = cursor.getString(cursor.getColumnIndex(UserInformationEnum.TAG_SERVER_IP));
				mServerPort = cursor.getString(cursor.getColumnIndex(UserInformationEnum.TAG_SERVER_PORT));
				mAccount = cursor.getString(cursor.getColumnIndex(UserInformationEnum.TAG_ACCOUNT));
				mPassword = cursor.getString(cursor.getColumnIndex(UserInformationEnum.TAG_PASSWORD));
				WebServiceUrlMaker.getInstance().changeIPAndPort(mServerIP, mServerPort);
				WebServiceUrlMaker.getInstance().changeAccountAndPassword(mAccount, mPassword);
			}
			cursor.close();
			mDBManager.close();
			return isLogin;
		}
	};

	/**
	 * 初始login server
     * @author Zam
     * @time 2013/5/9
     */
	private void initLoginServer() {
		findViewById(R.id.login_view).setVisibility(View.VISIBLE);
		mServerSpinner = (Spinner) findViewById(R.id.server_list);
		mDBManager.open();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(StartupActivity.this, R.layout.textview_server_info, mDBManager.getServerList(), new String[] {EzScrumServerEnum.TAG_NAME, EzScrumServerEnum.TAG_IP, EzScrumServerEnum.TAG_PORT}, new int[] {R.id.textview_server_name, R.id.textview_server_ip, R.id.textview_server_port}, 0);
		mServerSpinner.setAdapter(adapter);
		mServerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
				Cursor cursor = (Cursor) parent.getItemAtPosition(position);
				mServerIP = cursor.getString(cursor.getColumnIndex(EzScrumServerEnum.TAG_IP));
				mServerPort = cursor.getString(cursor.getColumnIndex(EzScrumServerEnum.TAG_PORT));
				WebServiceUrlMaker.getInstance().changeIPAndPort(mServerIP, mServerPort);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		mDBManager.close();
		mAddServerButton = (Button) findViewById(R.id.add_server_button);
		mAddServerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(StartupActivity.this);
				dialog.setTitle("Add server");
				View layout = getLayoutInflater().inflate(R.layout.dialog_add_server, null);
				// Dialog 上面的原件初始化
				final EditText editTextServerName = (EditText) layout.findViewById(R.id.server_name);
				final EditText editTextServerIP1 = (EditText) layout.findViewById(R.id.server_ip1);
				final EditText editTextServerIP2 = (EditText) layout.findViewById(R.id.server_ip2);
				final EditText editTextServerIP3 = (EditText) layout.findViewById(R.id.server_ip3);
				final EditText editTextServerIP4 = (EditText) layout.findViewById(R.id.server_ip4);
				final EditText editTextServerPort = (EditText) layout.findViewById(R.id.server_port);
				ServerIpTextWatcher ServerIP1Watcher = new ServerIpTextWatcher(editTextServerIP1, editTextServerIP2);
				editTextServerIP1.addTextChangedListener(ServerIP1Watcher);
				editTextServerIP1.setOnFocusChangeListener(ServerIP1Watcher);
				ServerIpTextWatcher ServerIP2Watcher = new ServerIpTextWatcher(editTextServerIP2, editTextServerIP3);
				editTextServerIP2.addTextChangedListener(ServerIP2Watcher);
				editTextServerIP2.setOnFocusChangeListener(ServerIP2Watcher);
				ServerIpTextWatcher ServerIP3Watcher = new ServerIpTextWatcher(editTextServerIP3, editTextServerIP4);
				editTextServerIP3.addTextChangedListener(ServerIP3Watcher);
				editTextServerIP3.setOnFocusChangeListener(ServerIP3Watcher);
				ServerIpTextWatcher ServerIP4Watcher = new ServerIpTextWatcher(editTextServerIP4, editTextServerPort);
				editTextServerIP4.addTextChangedListener(ServerIP4Watcher);
				editTextServerIP4.setOnFocusChangeListener(ServerIP4Watcher);
				dialog.setView(layout);
				dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String name = editTextServerName.getText().toString().trim();
						// 組裝 IP 為 xxx.xxx.xxx.xxx
						String ip = editTextServerIP1.getText().toString().trim() + "." + 
									editTextServerIP2.getText().toString().trim() + "." + 
									editTextServerIP3.getText().toString().trim() + "." + 
									editTextServerIP4.getText().toString().trim();
						String port = editTextServerPort.getText().toString().trim();
						if (TextUtils.isEmpty(name) || TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
							Toast.makeText(StartupActivity.this, "Please fill all", Toast.LENGTH_LONG).show();
						} else {
							mDBManager.open();
							mDBManager.insertServer(name, ip, port);
							SimpleCursorAdapter adapter = new SimpleCursorAdapter(StartupActivity.this, R.layout.textview_server_info, mDBManager.getServerList(), new String[] {EzScrumServerEnum.TAG_NAME, EzScrumServerEnum.TAG_IP, EzScrumServerEnum.TAG_PORT}, new int[] {R.id.textview_server_name, R.id.textview_server_ip, R.id.textview_server_port}, 0);
							mServerSpinner.setAdapter(adapter);
							mDBManager.close();
						}
					}
				});
				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {}
				});
				dialog.show();
			}
		});
	}

	/**
	 * 初始login表單
     * @author Zam
     * @time 2013/4/18
     */
	private void initLoginForm() {
		mAccountView = (EditText) findViewById(R.id.account);
		mAccountView.setText(mAccount);
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
	}
	
	/**
	 * 登入前的資料驗正與登入動作執行
     * @author Zam
     * @time 2013/4/18
     */
	public void attemptLogin() {
		if (mAuthTask != null)	return;
		// Reset errors.
		mAccountView.setError(null);
		mPasswordView.setError(null);
		// Store values at the time of the login attempt.
		mAccount = mAccountView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// 驗正密碼的正確性
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}
		// 驗正帳號的正確性
		if (TextUtils.isEmpty(mAccount)) {
			mAccountView.setError(getString(R.string.error_field_required));
			focusView = mAccountView;
			cancel = true;
		} 
		
		/**
		 * 1. 如果有錯誤則不執行登入，將畫面的游標轉換到第一個錯誤的地方
		 * 2. 顯示login progress，並執行login
		 */
		if (cancel) {
			focusView.requestFocus();
		} else {	
			// 關閉鍵盤
			InputMethodManager man= (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			man.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);		
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}
	
	/**
	 * 顯示Progress並將login表單隱藏
     * @author Zam
     * @time 2013/4/18
     */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		/**
		 * 1. 如果動態顯示的API存在則顯示動畫載入progress
		 * 2. 不支援則簡單的顯示與隱藏
		 */
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//			mLoginFormView.setVisibility(View.VISIBLE);
//			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//				@Override
//				public void onAnimationEnd(Animator animation) {
//					mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//				}
//			});	
//			mLoginStatusView.setVisibility(View.VISIBLE);
//			mLoginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//				@Override
//				public void onAnimationEnd(Animator animation) {
//					mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
//				}
//			});
//		} else {
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
//		}
	}
	
	/**
	 * 登入作業
     * @author Zam
     * @time 2013/4/18
     */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {			
			LoginManager loginManager = new LoginManager(StartupActivity.this, mAccount, mPassword, mServerIP, mServerPort);
			return loginManager.checkLoginInfo();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			if (success) {
				startActivity(new Intent(StartupActivity.this, ProjectListViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
				finish();
			} else {
				showProgress(false);
				Toast.makeText(StartupActivity.this, "帳號或密碼錯誤", Toast.LENGTH_LONG).show();
			}
		}
		
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
