package ntut.mobile.ezscrum.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class ServerIpTextWatcher implements TextWatcher, OnFocusChangeListener {
	EditText mView, mNextView;
	
	public ServerIpTextWatcher(EditText main, EditText next) {
		mView = main;
		mNextView = next;
	}
	
	private void valueValidator() {
		String text = mView.getText().toString(); 
		if (text.length() > 0) {
			if (Integer.parseInt(text) > 255 || Integer.parseInt(text) < 0) {
				mView.setError("value must between 1 ~ 255");
			}
			else {
				mView.setError(null);
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable editable) {
		int selection = mView.getSelectionStart();
		if (editable.length() > 0 && selection > 0) {
			// 如果輸入 '.' 則跳至下一個欄位，並清除點後的數值
			if (editable.charAt(selection - 1) == '.') {
				mView.setText(editable.subSequence(0, selection - 1));
				mNextView.requestFocus();
				mNextView.setSelection(0);
			} else {
				// 如果字數大於 3 會將多餘的部分刪去，並調整游標到正確的位置
				if (editable.length() > 3) {
					// 限制游標最大值為 3
					if(selection > 3)
						selection = 3;
					StringBuilder text = new StringBuilder(editable);
					text.deleteCharAt(selection);
					mView.setText(text);
					mView.setSelection(selection);
				}
				// 如果游標到了第三個位置則自動跳到下一欄位
				if (mView.getSelectionStart() == 3) {
					mNextView.requestFocus();
					mNextView.setSelection(0);
				}
				valueValidator();
			}
		}
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		valueValidator();
	}
}
