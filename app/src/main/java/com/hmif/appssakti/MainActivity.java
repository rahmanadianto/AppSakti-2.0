package com.hmif.appssakti;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.hmif.custom.MainMenuListener;
import com.hmif.custom.OnCardClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
		implements NavigationDrawerFragment.NavigationDrawerCallbacks, OnCardClickListener, MainMenuListener {

	public static final int MENU_LIST = 0;
	public static final int INFORMATION = 1;
	public static final int PLAIN_INFORMATION = 2;
	public static JSONObject searchObj;
	public static int searchType;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private ActionBarDrawerToggle drawerToggle;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	private List<String> mainMenuItem;
	private List<Integer> mainMenuIcon;
	private Toolbar toolbar;
	private JSONArray jsonArray;


	private static final int STATE_SHOW_BURGER = 1;
	private static final int STATE_HIDE_BURGER = 0;
	private int state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		drawerToggle = mNavigationDrawerFragment.getDrawerToggle();

		try {
			jsonArray = loadJSON(R.raw.oskm).getJSONArray("OSKM");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		state = STATE_SHOW_BURGER;
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		state = STATE_SHOW_BURGER;
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch(position){
			case 0 : fragmentManager.beginTransaction()
					 	.replace(R.id.container, MainMenuFragment.newInstance(this, mainMenuItem,
								mainMenuIcon))
					 	.commit();
					 break;
			case 1 : fragmentManager.beginTransaction()
						.replace(R.id.container, HardCodedInformationFragment.newInstance(R
								.layout.fragment_about_oskm))
						.commit();
					 break;
			case 2:
//					JSONObject obj = null;
//					try {
//						obj = getJSONObject("ohu").getJSONObject("isi");
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
					fragmentManager.beginTransaction()
						.replace(R.id.container, HardCodedInformationFragment.newInstance(R
								.layout.fragment_ohu))
						.commit();
					break;
			case 3:  fragmentManager.beginTransaction()
						.replace(R.id.container, HardCodedInformationFragment.newInstance(R
								.layout.fragment_about_apps))
						.commit();
					 break;
			case 4:  fragmentManager.beginTransaction()
					.replace(R.id.container, HardCodedInformationFragment.newInstance(R
							.layout.fragment_team))
					.commit();
				break;
//			default: fragmentManager.beginTransaction()
//					.replace(R.id.container, ListIconFragment.newInstance(R.layout
//									.text_icon_item, unitText, unitDetail, unitIcon))
//					.commit();
		}
	}

	public void restoreActionBar() {
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//		actionBar.setDisplayShowTitleEnabled(true);
//		actionBar.setTitle(mTitle);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		FragmentManager fragmentManager = getSupportFragmentManager();
		if(searchObj != null){
			if(searchType == PLAIN_INFORMATION){
				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
						.replace(R.id.container, PlainInformationFragment.newInstance(searchObj))
						.addToBackStack(null)
						.commit();
			}else if(searchType == INFORMATION){
				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
						.replace(R.id.container, LembagaInformationFragment.newInstance(searchObj))
						.addToBackStack(null)
						.commit();
			}else if(searchType == MENU_LIST){
				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
						.replace(R.id.container, ListIconFragment.newInstance(R.layout
								.text_icon_item, searchObj))
						.addToBackStack(null)
						.commit();
			}
			searchObj = null;
			searchType = -1;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.global, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		state = STATE_HIDE_BURGER;

		if(drawerToggle.onOptionsItemSelected(item)){
			FragmentManager manager = getSupportFragmentManager();
			if(manager.getBackStackEntryCount() > 0){
				mNavigationDrawerFragment.closeDrawer();
				manager.popBackStackImmediate();
				if(manager.getBackStackEntryCount() == 0){
					drawerToggle.syncState();
					state = STATE_SHOW_BURGER;
				}
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCardClicked(int type, JSONObject obj) {
		state = STATE_HIDE_BURGER;
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch(type){
			case MENU_LIST:

				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
						.replace(R.id.container, ListIconFragment
								.newInstance(R.layout.text_icon_item, obj))
						.addToBackStack(null)
						.commit();
				break;
			case INFORMATION:
				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
						.replace(R.id.container, LembagaInformationFragment.newInstance(obj))
						.addToBackStack(null)
						.commit();
				break;
			case PLAIN_INFORMATION:
				fragmentManager.beginTransaction()
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
						.replace(R.id.container, PlainInformationFragment.newInstance(obj))
						.addToBackStack(null)
						.commit();
				break;
		}

	}

	private JSONObject loadJSON(int resId) throws IOException, JSONException {
		String line;
		StringBuilder result = new StringBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(getResources()
				.openRawResource(resId)));
		while((line = reader.readLine()) != null){
			result.append(line);
		}
		return new JSONObject(result.toString());
	}

	public JSONObject getJSONObject(String selection) throws JSONException {
		for(int i=0; i<jsonArray.length(); i++){
			JSONObject obj = jsonArray.getJSONObject(i);
			if(obj.getString("menu").equals(selection))
				return obj;
		}
		return null;
	}

	@Override
	public void onMenuClicked(int code) {
		state = STATE_HIDE_BURGER;
		JSONArray array;
		try {
			switch (code) {
				case 0:
					System.out.println(getJSONObject("kemahasiswaan").toString());
					onCardClicked(MENU_LIST, getJSONObject("kemahasiswaan"));
					break;
				case 1:
					array = getJSONObject("lembaga pusat").getJSONArray("isi");
					onCardClicked(INFORMATION, array.getJSONObject(0));
					break;
				case 2:
					array = getJSONObject("lembaga pusat").getJSONArray("isi");
					onCardClicked(INFORMATION, array.getJSONObject(1));
					break;
				case 3:
					onCardClicked(MENU_LIST, getJSONObject("Himpunan"));
					break;
				case 4:
					onCardClicked(MENU_LIST, getJSONObject("Unit"));
					break;
				case 5:
					array = getJSONObject("lembaga pusat").getJSONArray("isi");
					onCardClicked(INFORMATION, array.getJSONObject(2));
					break;
				case 6:
					array = getJSONObject("lembaga pusat").getJSONArray("isi");
					onCardClicked(INFORMATION, array.getJSONObject(3));
					break;
			}
		}catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		FragmentManager manager = getSupportFragmentManager();
		if(manager.getBackStackEntryCount() > 0){
			mNavigationDrawerFragment.closeDrawer();
			manager.popBackStackImmediate();
			if(manager.getBackStackEntryCount() == 0){
				drawerToggle.syncState();
				state = STATE_SHOW_BURGER;
			}
		}else{
			super.onBackPressed();
		}

	}

	public boolean isBurgerShow(){
		return state == STATE_SHOW_BURGER;
	}
}
