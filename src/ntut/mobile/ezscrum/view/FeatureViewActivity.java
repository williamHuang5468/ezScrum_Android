package ntut.mobile.ezscrum.view;

import android.app.Activity;
import android.os.Bundle;

public class FeatureViewActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feature_view);

		FeatureListItemFragment featureListFrag = (FeatureListItemFragment) getFragmentManager()
				.findFragmentById(R.id.featureListFragment);
		final FeatureListItemContentFragment featureContentFrag = (FeatureListItemContentFragment) getFragmentManager()
				.findFragmentById(R.id.featureContentFragment);
		featureListFrag.setFeatureListItemListener(new FeatureListItemListener() {
			
			@Override
			public void onFeatureListItemSelected(String text) {	
				featureContentFrag.setContentTextView(text);
			}
		});
	}
	@Override
	public void onBackPressed() {
		finish();
//		super.onBackPressed();
	}
}
