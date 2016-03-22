package com.hmif.appssakti;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gilang on 19/07/2015.
 */
public class PlainInformationFragment extends Fragment implements ObservableScrollViewCallbacks{

	private int mFlexibleSpaceImageHeight;
	private int mActionBarSize;
	private ImageView mImageView;
	private View mOverlayView;
	private Toolbar toolbar;
	private TextView mTitleView;
	private ObservableScrollView mScrollView;
	private TextView mInfoView;
	private ImageView mContentImage;
	private JSONObject jsonObject;

	public PlainInformationFragment(){}

	public static PlainInformationFragment newInstance(JSONObject obj){
		PlainInformationFragment fragment = new PlainInformationFragment();
		fragment.jsonObject = obj;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_plain_information, parent, false);
		try {
			setUpView(v);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return v;
	}

	public void setUpView(View v) throws JSONException {
		mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_height);
		mActionBarSize = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

		mImageView = (ImageView) v.findViewById(R.id.card_header_image);
		mScrollView = (ObservableScrollView) v.findViewById(R.id.scroll_view);
		mScrollView.setScrollViewCallbacks(this);
		mTitleView = (TextView) v.findViewById(R.id.title);
		mOverlayView = (View) v.findViewById(R.id.overlay_view);
		mInfoView = (TextView) v.findViewById(R.id.information_text);
		mContentImage = (ImageView) v.findViewById(R.id.content_image);
		toolbar = (Toolbar) v.findViewById(R.id.toolbar);

		AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.setSupportActionBar(toolbar);
		//activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		activity.getSupportActionBar().setTitle(null);

		mTitleView.setText(jsonObject.getString("judul"));
		mInfoView.setText(Html.fromHtml(jsonObject.getString("isi").replace("\\n", "</br>")));
		if(jsonObject.has("content image")){
			mContentImage.setImageResource(getResources().getIdentifier(jsonObject.getString
							("content image"), "drawable", getActivity().getApplicationContext()
					.getPackageName()));
		}else{
			mContentImage.setVisibility(View.GONE);
		}

		ScrollUtils.addOnGlobalLayoutListener(mTitleView, new Runnable() {
			@Override
			public void run() {
				updateFlexibleSpaceText(mScrollView.getCurrentScrollY());
			}
		});
	}

	public void updateFlexibleSpaceText(int scrollY) throws NullPointerException{
		float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
		int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
		ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
		ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
		ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange,
				0, 1));
		float maxScale = 2;
		if(mTitleView.getText().length() > 20){
			maxScale = 0;
		}
		float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0,
				maxScale);

		ViewHelper.setPivotX(mTitleView, 0);
		ViewHelper.setPivotY(mTitleView, 0);
		ViewHelper.setScaleX(mTitleView, scale);
		ViewHelper.setScaleY(mTitleView, scale);

		int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
		int minTitleTranslationY = ((int) mActionBarSize / 2) - (mTitleView.getHeight() / 2);
		int titleTranslationY = (int)ScrollUtils.getFloat(maxTitleTranslationY - scrollY - minTitleTranslationY,
				minTitleTranslationY, maxTitleTranslationY);
		ViewHelper.setTranslationY(mTitleView, titleTranslationY);

		int actionbarPadding = getResources().getDimensionPixelOffset(R.dimen
				.actionbar_title_padding);
		int actionbarOffset = getResources().getDimensionPixelOffset(R.dimen
				.actionbar_title_horizontal_offset);
		float titlePositionY = 1f - (float)(titleTranslationY - minTitleTranslationY)/(float)
				(maxTitleTranslationY - minTitleTranslationY);
		ViewHelper.setTranslationX(mTitleView, ScrollUtils.getFloat((titlePositionY * (float)
						(actionbarOffset - actionbarPadding)) + actionbarPadding, actionbarPadding,
				actionbarOffset));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseMemory();
		System.gc();
	}

	public void releaseMemory(){
		mImageView.setImageDrawable(null);
		mContentImage.setImageDrawable(null);
		mImageView = null;
		mContentImage = null;
	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
		try {
			updateFlexibleSpaceText(scrollY);
		}catch (NullPointerException e){
			e.printStackTrace();
		}
	}

	@Override
	public void onDownMotionEvent() {

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {

	}
}
