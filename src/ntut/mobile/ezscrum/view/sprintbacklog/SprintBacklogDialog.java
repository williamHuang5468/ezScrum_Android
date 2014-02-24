package ntut.mobile.ezscrum.view.sprintbacklog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

interface SprintBacklogUpdateListener {
	public void update();
}

public class SprintBacklogDialog extends AlertDialog {
	protected SprintBacklogUpdateListener mUpdateListener;
	protected Context mContext;
	protected String mProjectID;

	private OnClickListener mOnCancelListener = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {}
	};
	
	public SprintBacklogDialog(Context context, String projecID) {
		super(context);
		mContext = context;
		mProjectID = projecID; 
		setCanceledOnTouchOutside(false);
		setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", mOnCancelListener);
	}
	
	protected void update() {
		if (mUpdateListener != null)
			mUpdateListener.update();
	}
	
	public void setUpdateListener(SprintBacklogUpdateListener listener) {
		mUpdateListener = listener;
	}
}
