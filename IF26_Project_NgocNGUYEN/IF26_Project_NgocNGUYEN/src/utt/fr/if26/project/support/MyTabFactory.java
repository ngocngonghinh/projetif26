package utt.fr.if26.project.support;

import android.content.Context;
import android.view.View;
import android.widget.TabHost.TabContentFactory;

/**
 * Tab Manager
 */
public class MyTabFactory implements TabContentFactory {
	private final Context mContext;

	public MyTabFactory(Context context) {
		mContext = context;
	}

	public View createTabContent(String tag) {
		View v = new View(mContext);
		v.setMinimumWidth(0);
		v.setMinimumHeight(0);
		return v;
	}
}
