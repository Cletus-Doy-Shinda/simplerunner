package ca.simplerunner.app;

import ca.simplerunner.R;
import ca.simplerunner.misc.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * This Activity is responsible for showing detailed views
 * of a run, including:
 * The Map showing where the user ran
 * Split table showing split times for each km
 * Main View showing avg pace, avg speed, time and distance
 * 
 * @author Abe Friesen
 *
 */
public class RunView extends FragmentActivity implements ActionBar.TabListener {

	ViewPager viewPager;
	TabsPagerAdapter tabAdapter;
	long runID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.runview);
		this.runID = getIntent().getLongExtra("runID", 69);
		viewPager = (ViewPager) findViewById(R.id.pager);
		tabAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabAdapter);
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		actionBar.addTab(actionBar.newTab().setText("Main")
				.setTabListener(this), 0, true);
		actionBar.addTab(actionBar.newTab().setText("Split")
				.setTabListener(this), 1, false);
		actionBar.addTab(actionBar.newTab().setText("Map")
				.setTabListener(this), 2, false);

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
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
	
	/*
	 * Populate Action bar with menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.back_action, menu);

		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Handle menu item chosen
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_back:
			cleanDestroy();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// Nothing to do here		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// Nothing to do here
	}

	/*
	 * Hack to remove GoogleMap
	 */
	public void removeMapFragment() {
		FragmentManager fm = getSupportFragmentManager();
		Fragment f = fm.findFragmentById(R.id.map);
		fm.beginTransaction().remove(f).commit();
	}

	@Override
	public void onBackPressed() {
		cleanDestroy();
	}
	
	/*
	 * This is needed to destroy the Google Map only when
	 * the viewPager hasn't already destroyed it.
	 */
	private void cleanDestroy() {
		if(viewPager.getCurrentItem() >= 1) {
			removeMapFragment();
		}
		this.finish();
	}
	
	/*
	 * Returns runID
	 */
	public long getRunID() {
		return this.runID;
	}
}
