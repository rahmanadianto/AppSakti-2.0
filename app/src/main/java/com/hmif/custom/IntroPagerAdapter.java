package com.hmif.custom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.hmif.intro.IntroFragment;

/**
 * Created by gilang on 08/07/2015.
 */
public class IntroPagerAdapter extends FragmentPagerAdapter{

	private int count;

	public IntroPagerAdapter(FragmentManager fm, int count){
		super(fm);
		this.count = count;
	}

	@Override
	public Fragment getItem(int position) {
		IntroFragment fragment = IntroFragment.newInstance(position);
		return fragment;
	}

	@Override
	public int getCount() {
		return count;
	}
}
