package ntut.mobile.ezscrum.view;

import ntut.mobile.ezscrum.database.DBManager;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * 實做此App中，各Activity相同的元件與事件
 */
public class BaseActivity extends Activity {
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.common, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	        	DBManager dbManager = new DBManager(this);
	        	dbManager.open();
	        	boolean result = dbManager.updateUserLoginStatus(null, false);
	        	dbManager.close();
	        	if (result) {
		        	startActivity(new Intent(this, StartupActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		        	finish();
	        	} else {
	        		Toast.makeText(this, "登出發生錯誤", Toast.LENGTH_LONG).show();
	        	}
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
