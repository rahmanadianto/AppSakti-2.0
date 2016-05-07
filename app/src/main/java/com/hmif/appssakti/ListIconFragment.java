package com.hmif.appssakti;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.hmif.custom.OnCardClickListener;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListIconFragment extends Fragment implements ObservableScrollViewCallbacks{

	private int layoutId;
	private LinearLayout menuContainer;

	private int mFlexibleSpaceImageHeight;
	private int mActionBarSize;
	private ImageView mImageView;
	private View mOverlayView;
	private Toolbar toolbar;
	private TextView mTitleView;
	private ObservableScrollView mScrollView;
	private JSONObject jsonObject;
	private OnCardClickListener mListener;
	private List<ImageView> listImage;

	/* @param layoutId layout to be used for list item
	 *
	 */
	public static ListIconFragment newInstance(int layoutId, JSONObject jsonObject) {
		ListIconFragment fragment = new ListIconFragment();
		fragment.layoutId = layoutId;
		fragment.jsonObject = jsonObject;
		fragment.listImage = new ArrayList<>();
		return fragment;
	}

	public ListIconFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_list_icon, container, false);
		setUpView(v);
		menuContainer = (LinearLayout) v.findViewById(R.id.menu_container);
		try {
			createMenu(inflater, container);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return v;
	}

	public void setUpView(View v){
		mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_height);
		mActionBarSize = getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);

		mImageView = (ImageView) v.findViewById(R.id.card_header_image);
		mScrollView = (ObservableScrollView) v.findViewById(R.id.scroll_view);
		mScrollView.setScrollViewCallbacks(this);
		mTitleView = (TextView) v.findViewById(R.id.title);
		mOverlayView = v.findViewById(R.id.overlay_view);
		toolbar = (Toolbar) v.findViewById(R.id.toolbar);

		AppCompatActivity activity = (AppCompatActivity) getActivity();
		activity.setSupportActionBar(toolbar);

		try {
			activity.getSupportActionBar().setTitle(null);
		}
		catch (Exception ignored) {}

		ScrollUtils.addOnGlobalLayoutListener(mTitleView, new Runnable() {
			@Override
			public void run() {
				updateFlexibleSpaceText(mScrollView.getCurrentScrollY());
			}
		});
	}

	private void createMenu(LayoutInflater inflater, ViewGroup parent) throws JSONException {
		JSONArray array = null;

		// Get Data
		if(jsonObject.has("menu")){
			array = jsonObject.getJSONArray("isi");
		}
		else if(jsonObject.has("nama fakultas") || jsonObject.has("kategori unit")){
			array = jsonObject.getJSONArray("info");
		}

		// Set Header text and image
		if (jsonObject.has("menu") && jsonObject.getString("menu").equals("Unit")) {
			mTitleView.setText("Unit");
			mImageView.setImageResource(R.drawable.header_unit);
		}
		else if (jsonObject.has("kategori unit")) {
			mTitleView.setText(jsonObject.getString("kategori unit"));
			mImageView.setImageResource(
					getResources().getIdentifier(
							jsonObject.getString("header foto"), "drawable", getActivity().getPackageName()
					)
			);
		}
		else if (jsonObject.has("menu") && jsonObject.getString("menu").equals("Himpunan")) {
			mTitleView.setText("Himpunan");
			mImageView.setImageResource(R.drawable.header_fakultas);
		}
		else if (jsonObject.has("nama fakultas")){
			mTitleView.setText(jsonObject.getString("nama fakultas"));
			mImageView.setImageResource(
					getResources().getIdentifier(
							jsonObject.getString("header foto"), "drawable", getActivity().getPackageName()
					)
			);
		}
		else if (jsonObject.has("menu") && jsonObject.getString("menu").equals("kemahasiswaan")){
			mTitleView.setText("Kemahasiswaan");
			mImageView.setImageResource(R.drawable.home_kemahasiswaan);
		}
		else if (jsonObject.has("menu") && jsonObject.getString("menu").equals("Kantin")) {
			mTitleView.setText("Kantin / Tempat Makan");
			mImageView.setImageResource(R.drawable.hmif);
		}

		// Set content
		int jumlah_content = 0;

		try {
			jumlah_content = array.length();
		}
		catch (NullPointerException ignored) {}

		for (int i = 0; i < jumlah_content; i++){
			View v = inflater.inflate(layoutId, menuContainer, false);

			ImageView icon = (ImageView) v.findViewById(R.id.item_icon);
			listImage.add(icon);
			TextView textGeneral = (TextView) v.findViewById(R.id.item_text_general);
			TextView textDetail = (TextView) v.findViewById(R.id.item_text_detail);

			final JSONObject obj = array.getJSONObject(i);
			if (obj.has("header foto") && !obj.getString("header foto").equals("-")) {
				icon.setImageDrawable(
						ResourcesCompat.getDrawable(
								getResources(),
								getResources().getIdentifier(
										obj.getString("header foto"),
										"drawable",
										getActivity().getApplicationContext().getPackageName()
								),
								null
						)
				);
			}
			else if (obj.has("foto") && !obj.getString("foto").equals("-")) {
				Log.d("KANTIN", obj.getString("foto"));
				icon.setImageDrawable(
						ResourcesCompat.getDrawable(
								getResources(),
								getResources().getIdentifier(
										obj.getString("foto"),
										"drawable",
										getActivity().getApplicationContext().getPackageName()
								),
								null
						)
				);
			}
			else {
				icon.setImageDrawable(
						ResourcesCompat.getDrawable(getResources(), R.drawable.apps_header_logo, null));
			}

			if (obj.has("nama fakultas")){
				textGeneral.setText(obj.getString("nama fakultas"));
				textDetail.setText(obj.getString("nama panjang"));
			}
			else if (obj.has("nama himpunan")){
				textGeneral.setText(obj.getString("nama himpunan"));
				textDetail.setText(obj.getString("kepanjangan"));
			}
			else if (obj.has("kategori unit")){
				textGeneral.setText(obj.getString("kategori unit"));
				textDetail.setVisibility(View.GONE);
			}
			else if (obj.has("nama unit")){
				textGeneral.setText(obj.getString("nama unit"));
				if (obj.getString("kepanjangan").equals("-")){
					textDetail.setVisibility(View.GONE);
				}
				else {
					textDetail.setText(obj.getString("kepanjangan"));
				}
			}
			else if (obj.has("judul")){
				textGeneral.setText(obj.getString("judul"));
				textDetail.setVisibility(View.GONE);
			}
			else if (obj.has("nama")) { // kantin
				textGeneral.setText(obj.getString("nama"));
				textDetail.setVisibility(View.GONE);
			}

			// Set onClick Listener
			if (obj.has("menu") || obj.has("kategori unit") || obj.has("nama fakultas")) {
				v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onCardClicked(MainActivity.MENU_LIST, obj);
					}
				});
			}
			else if (obj.has("judul")) {
				v.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mListener.onCardClicked(MainActivity.PLAIN_INFORMATION, obj);
					}
				});
			}
			else {
				// Set Listener jika bukan kantin, hanya kantin yang memiliki elemen "foto"
				if (!obj.has("foto")) {
					v.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mListener.onCardClicked(MainActivity.INFORMATION, obj);
						}
					});
				}
			}

			menuContainer.addView(v);
		}
	}

	public void updateFlexibleSpaceText(int scrollY) throws NullPointerException{
		float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
		int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
		ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
		ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
		ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange,
				0, 1));
		float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, 2);
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
	public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
		try {
			updateFlexibleSpaceText(scrollY);
		}
		catch (Exception e){
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
	public void onAttach(Context activity){
		super.onAttach(activity);
		try{
			mListener = (OnCardClickListener)activity;
		}catch(ClassCastException e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		releaseMemory();
		super.onDestroy();
	}

	public void releaseMemory(){
		for(ImageView v : listImage){
			v.setImageDrawable(null);
		}
		listImage = null;
	}

}
