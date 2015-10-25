package se.loco.app;

import utils.MyApplication;
import adapters.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Session;

public class TabsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "Start", "Booze", "Buddys" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);
		checkIfUserLoggedIn(getApplicationContext());

		MyApplication myApplication = (MyApplication) getApplication();

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);

		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#504e4e")));
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// action with ID action_settings was selected
		case R.id.menuSettings:
			Intent intent = new Intent(getApplicationContext(), Settings.class);
			startActivity(intent);
			break;

		case R.id.menuMyDrinkList:

			Intent intenti = new Intent(getApplicationContext(),
					MyDrinkListActivity.class);
			startActivity(intenti);
			break;

		case R.id.menuLogout:

			callFacebookLogout(getApplicationContext());

			break;
		default:
			break;
		}

		return true;
	}

	public static void checkIfUserLoggedIn(Context context) {

		Session session = Session.getActiveSession();
		if (session == null) {

			// clear your preferences if saved

			Intent i = new Intent();
			i.setClass(context, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		}

	}

	public static void callFacebookLogout(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved

				Intent i = new Intent();
				i.setClass(context, MainActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);

			}
		} else {

			session = new Session(context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			// clear your preferences if saved

			Intent i = new Intent();
			i.setClass(context, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		}

	}

}
