package utt.fr.if26.project.pages;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import utt.fr.if26.project.R;
import utt.fr.if26.project.pages.adapters.PagerAdapter;
import utt.fr.if26.project.pages.tabs.ContactsTab;
import utt.fr.if26.project.pages.tabs.FriendsInvitationTab;
import utt.fr.if26.project.pages.tabs.MapTab;
import utt.fr.if26.project.support.ExceptionHandler;
import utt.fr.if26.project.support.MyTabFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TabsPage extends SherlockFragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

	// Declare variables
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;

	private SherlockFragment mContactsTab, mMapTab, mFriendsInvitationTab;

	private static void addTab(SherlockFragmentActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
		tabSpec.setContent(new MyTabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.tabs_page, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.mViewPager = (ViewPager) view.findViewById(R.id.viewpager);

		// Create FragmentTabHost
		mTabHost = (TabHost) view.findViewById(R.id.tabhost);
		mTabHost.setup();

		addTab(getSherlockActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.invitationsTab)).setIndicator(getString(R.string.invitationsTab)));
		addTab(getSherlockActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.contactsTab)).setIndicator(getString(R.string.contactsTab)));
		addTab(getSherlockActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.mapTab)).setIndicator(getString(R.string.mapTab)));

		mTabHost.setOnTabChangedListener(this);

		// Set the tab as per the saved state
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

		List<Fragment> fragments = new ArrayList<Fragment>();

		// this.mFeedsTab = FeedsTab.newIntance();
		this.mContactsTab = ContactsTab.newIntance();
		this.mFriendsInvitationTab = FriendsInvitationTab.newIntance();
		this.mMapTab = MapTab.newIntance();

		// fragments.add(mFeedsTab);
		fragments.add(mFriendsInvitationTab);
		fragments.add(mContactsTab);
		fragments.add(mMapTab);

		this.mPagerAdapter = new PagerAdapter(getChildFragmentManager(), fragments);

		this.mViewPager.setAdapter(mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			ExceptionHandler.getInstance().mHandler(e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			ExceptionHandler.getInstance().mHandler(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mTabHost = null;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		this.mTabHost.setCurrentTab(arg0);
		if (arg0 != 2) {
			if (mMapTab != null && getActivity() != null) {
				((MapTab) mMapTab).stoptimertask();
			}
		} else {
			if (mMapTab != null && getActivity() != null) {
				((MapTab) mMapTab).startTimer();
			}
		}
	}

	@Override
	public void onTabChanged(String tabId) {
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);
	}
}
