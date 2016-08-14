package com.hmif.appssakti;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gilang on 06/07/2015.
 */
public class LembagaInformationFragment extends Fragment implements ObservableScrollViewCallbacks{

	private static final int GREY = 0;
	private static final int WHITE = 1;

	private int colorCode;
	private TextView namaSingkat;
	private TextView nama;
	private TextView[] socmedText;
	private ImageView[] socmedImage;
	private TextView alamat;
	private LinearLayout scrollContainer;

	private int mFlexibleSpaceImageHeight;
	private int mActionBarSize;
	private ImageView mImageView;
	private View mOverlayView;
	private Toolbar toolbar;
	private ImageView mTitleImageView;
	private ObservableScrollView mScrollView;
	private View mHeaderView;
	private JSONObject jsonObject;


	public static LembagaInformationFragment newInstance(JSONObject obj){
		LembagaInformationFragment fragment = new LembagaInformationFragment();
		fragment.jsonObject = obj;
		fragment.colorCode = fragment.getColorCode();
		return fragment;
	}

	public LembagaInformationFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_information, container, false);
		setUpHeader(v);
		try {
			setupView(inflater, v, container);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return v;
	}

	public void setupView(LayoutInflater inflater, View v, ViewGroup container) throws JSONException {
		scrollContainer = (LinearLayout) v.findViewById(R.id.scroll_container);
		namaSingkat = (TextView) v.findViewById(R.id.nama_lembaga_singkatan);
		nama = (TextView) v.findViewById(R.id.nama_lembaga);
		socmedText = new TextView[4];
		socmedText[0] = (TextView) v.findViewById(R.id.socmed1_text);
		socmedText[1] = (TextView) v.findViewById(R.id.socmed2_text);
		socmedText[2] = (TextView) v.findViewById(R.id.socmed3_text);
		socmedText[3] = (TextView) v.findViewById(R.id.socmed4_text);
		socmedImage = new ImageView[4];
		socmedImage[0] = (ImageView) v.findViewById(R.id.socmed1_image);
		socmedImage[1] = (ImageView) v.findViewById(R.id.socmed2_image);
		socmedImage[2] = (ImageView) v.findViewById(R.id.socmed3_image);
		socmedImage[3] = (ImageView) v.findViewById(R.id.socmed4_image);
		alamat = (TextView) v.findViewById(R.id.alamat_lembaga);

		if(jsonObject.has("nama himpunan")) {
			namaSingkat.setText(jsonObject.getString("nama himpunan"));
		}else if(jsonObject.has("nama unit")){
			namaSingkat.setText(jsonObject.getString("nama unit"));
		}else if(jsonObject.has("nama lembaga")){
			namaSingkat.setText(jsonObject.getString("nama lembaga"));
		}

		if(!jsonObject.getString("kepanjangan").equals("-")){
			nama.setText(jsonObject.getString("kepanjangan"));
		}else{
			nama.setVisibility(View.GONE);
		}
		if(!jsonObject.getString("sekretariat").equals("-")) {
			alamat.setText(jsonObject.getString("sekretariat"));
		}else{
			alamat.setVisibility(View.GONE);
		}
		if(colorCode == WHITE){
			namaSingkat.setTextColor(Color.WHITE);
			nama.setTextColor(Color.WHITE);
			alamat.setTextColor(Color.WHITE);
			socmedText[0].setTextColor(Color.WHITE);
			socmedText[1].setTextColor(Color.WHITE);
			socmedText[2].setTextColor(Color.WHITE);
			socmedText[3].setTextColor(Color.WHITE);
		}
		setSocmedInfo();

		if (!jsonObject.getString("logo").equals("-")) {
			MainActivity.loadImage(getContext(), mTitleImageView, jsonObject.getString("logo"));
		}
		else{
			mTitleImageView.setVisibility(View.GONE);
		}

		String color = jsonObject.getString("color");
		mOverlayView.setBackgroundColor(Color.parseColor("#" + color));
		mHeaderView.setBackgroundColor(Color.parseColor("#" + color));
		mImageView.setBackgroundColor(Color.parseColor("#" + color));

		MainActivity.loadImage(getContext(), mImageView, jsonObject.getString("header foto"));

		List<String> titles = new ArrayList<>();
		List<String> contents = new ArrayList<>();
		if(!jsonObject.getString("deskripsi arah gerak dan kegiatan").equals("-")) {
			titles.add("Deskripsi arah gerak dan kegiatan :");
			contents.add(jsonObject.getString("deskripsi arah gerak dan kegiatan"));
		}
		if(!jsonObject.getString("Program Kerja Unggulan").equals("-")) {
			titles.add("Program kerja unggulan :");
			contents.add(jsonObject.getString("Program Kerja Unggulan"));
		}

		for(int i=0; i<titles.size(); i++){
			View v2 = inflater.inflate(R.layout.title_content_item, container, false);
			TextView title = (TextView) v2.findViewById(R.id.title);
			TextView content = (TextView) v2.findViewById(R.id.content);

			title.setText(titles.get(i));
			content.setText(contents.get(i));
			LinearLayout layout = (LinearLayout) v.findViewById(R.id.container_info);
			layout.addView(v2);
		}
	}

	public void setSocmedInfo() throws JSONException {

		String[] socmedKey = {"facebook", "twitter", "line", "website"};
		String[] socmedIcon = {"icon_fb_", "icon_twitter_", "icon_line_", "icon_web_"};

		for(int i = 0; i < 4; i++){

			if(!jsonObject.getString(socmedKey[i]).equals("-")){

				socmedText[i].setText(jsonObject.getString(socmedKey[i]));

				//website
				if(i == 3){
					Linkify.addLinks(socmedText[3], Linkify.WEB_URLS);
				}

				if(colorCode == GREY){
					socmedImage[i].setImageResource(getResources().getIdentifier
							(socmedIcon[i] + "grey", "drawable", getActivity().getPackageName()));
				}else if(colorCode == WHITE){
					socmedImage[i].setImageResource(getResources().getIdentifier
							(socmedIcon[i] + "white", "drawable", getActivity().getPackageName()));
				}

			}else{
				socmedText[i].setVisibility(View.GONE);
				socmedImage[i].setVisibility(View.GONE);
			}
		}
	}

	public void setUpHeader(View v){
		mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_height);
		mActionBarSize = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

		mImageView = (ImageView) v.findViewById(R.id.card_header_image);
		mScrollView = (ObservableScrollView) v.findViewById(R.id.scroll_view);
		mScrollView.setScrollViewCallbacks(this);
		mTitleImageView = (ImageView) v.findViewById(R.id.title_image);
		mOverlayView = (View) v.findViewById(R.id.overlay_view);
		mHeaderView = (View) v.findViewById(R.id.information_header);
		toolbar = (Toolbar) v.findViewById(R.id.toolbar);

		AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.setSupportActionBar(toolbar);
		activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		activity.getSupportActionBar().setTitle(null);

		ScrollUtils.addOnGlobalLayoutListener(mTitleImageView, new Runnable() {
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
		float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, 1);
		ViewHelper.setPivotX(mTitleImageView, 0);
		ViewHelper.setPivotY(mTitleImageView, 0);
		ViewHelper.setScaleX(mTitleImageView, scale);
		ViewHelper.setScaleY(mTitleImageView, scale);

		int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleImageView.getHeight() * scale);
		int minTitleTranslationY = ((int) mActionBarSize / 2) - (mTitleImageView.getHeight() / 2);
		int titleTranslationY = (int)ScrollUtils.getFloat(maxTitleTranslationY - scrollY - minTitleTranslationY,
				minTitleTranslationY, maxTitleTranslationY);
		ViewHelper.setTranslationY(mTitleImageView, titleTranslationY);

		int actionbarPadding = getResources().getDimensionPixelOffset(R.dimen
				.information_padding);
		int actionbarOffset = getResources().getDimensionPixelOffset(R.dimen
				.actionbar_title_horizontal_offset);
		float titlePositionY = 1f - (float)(titleTranslationY - minTitleTranslationY)/(float)
				(maxTitleTranslationY - minTitleTranslationY);
		ViewHelper.setTranslationX(mTitleImageView, ScrollUtils.getFloat((titlePositionY * (float)
						(actionbarOffset - actionbarPadding)) + actionbarPadding, actionbarPadding,
				actionbarOffset));

//		if(-scrollY <= minOverlayTransitionY){
//			mScrollView.scrollVerticallyTo(-minOverlayTransitionY);
//		}
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		releaseMemory();
	}

	public void releaseMemory(){
		mTitleImageView.setImageDrawable(null);
		mImageView.setImageDrawable(null);
		mTitleImageView = null;
		mImageView = null;
	}

	private int getColorCode(){
		int color = 0;
		try {
			color = (int)Long.parseLong(jsonObject.getString("color"), 16);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = (color >> 0) & 0xFF;
		return getColorScheme(r, g, b);
	}


	public int getColorScheme(int r, int g, int b){
		double a = 1 - (0.299 * r + 0.587 * g + 0.114 * b) / 255;
		if(a < 0.5) {
			return 0;
		}
		return 1;
	}

}
