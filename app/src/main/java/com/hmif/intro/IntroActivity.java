package com.hmif.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hmif.appssakti.MainActivity;
import com.hmif.appssakti.R;
import com.hmif.custom.CirclePageIndicator;
import com.hmif.custom.IntroPagerAdapter;

public class IntroActivity extends ActionBarActivity implements ViewPager.OnPageChangeListener,
		View.OnClickListener{

	private final static String PREFERENCE_KEY = "bvhjwhgv";
	private final static String FIRST_TIME = "first_time";
	private static final int NUM_PAGES = 3;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private CirclePageIndicator mIndicator;
	private Button btnLewati;
	private Button btnSelesai;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

//		SharedPreferences preferences = getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
//		if(!preferences.getBoolean(FIRST_TIME, true)){
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
//			finish();
//		}else{
//			SharedPreferences.Editor editor = preferences.edit();
//			editor.putBoolean(FIRST_TIME, false);
//			editor.commit();
//		}

		btnLewati = (Button) findViewById(R.id.btn_lewati);
		btnSelesai = (Button) findViewById(R.id.btn_selesai);
		btnLewati.setOnClickListener(this);
		btnSelesai.setOnClickListener(this);

		btnSelesai.setVisibility(View.GONE);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new IntroPagerAdapter(getSupportFragmentManager(), NUM_PAGES);
		mPager.setAdapter(mPagerAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.circle_indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setOnPageChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_intro, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();


		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {
		switch(position){
			case 0: btnLewati.setVisibility(View.VISIBLE);
					btnSelesai.setVisibility(View.GONE);
					break;
			case 1: btnLewati.setVisibility(View.VISIBLE);
					btnSelesai.setVisibility(View.GONE);
					break;
			case 2: btnLewati.setVisibility(View.GONE);
					btnSelesai.setVisibility(View.VISIBLE);
					break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
