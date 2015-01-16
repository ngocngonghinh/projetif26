package utt.fr.if26.project.helpers;

import utt.fr.if26.project.R;
import android.support.v4.app.Fragment;
import android.widget.ViewSwitcher;

public class ViewHelper {
	public static void switchViewSwitcher(Fragment frag, int position) {
        if (frag != null && frag.getView() != null) {
            ViewSwitcher switcher = (ViewSwitcher) frag.getView().findViewById(R.id.viewSwitcher);
            if (switcher != null) {
                switcher.setDisplayedChild(position);
            }
        }
    }
}