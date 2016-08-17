package com.inkubatorit.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inkubatorit.appssakti.MainActivity;
import com.inkubatorit.appssakti.R;

import java.util.List;

/**
 * Created by gilang on 02/07/2015.
 */
public class IconTextAdapter extends BaseAdapter{

	private List<String> itemsTextGeneral;
//	private List<String> itemsTextDetail;
	private List<String> itemsIcon;
	private Boolean network;
	private Context context;
	private int layoutId;

	public IconTextAdapter(Context context, int layoutId, List<String> itemsTextGeneral,
						   @Nullable List<String> itemsTextDetail, List<String> itemsIcon, Boolean network){
		this.itemsTextGeneral = itemsTextGeneral;
//		this.itemsTextDetail = itemsTextDetail;
		this.network = network;
		this.itemsIcon = itemsIcon;
		this.layoutId = layoutId;
		this.context = context;
	}

	@Override
	public int getCount() {
		return itemsTextGeneral.size();
	}

	@Override
	public Object getItem(int position) {
		return itemsTextGeneral.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null){
			LayoutInflater mInflater = LayoutInflater.from(context);
			v = mInflater.inflate(layoutId, parent, false);
		}

		TextView textGeneral = (TextView) v.findViewById(R.id.item_text_general);
//		TextView textDetail = (TextView) v.findViewById(R.id.item_text_detail);
		ImageView mImageView = (ImageView) v.findViewById(R.id.item_icon);

		textGeneral.setText(itemsTextGeneral.get(position));
		try {
			if(itemsIcon.get(position) != null)
				loadImage(mImageView, itemsIcon.get(position));
			else
				mImageView.setImageDrawable(null);
		}
		catch (Exception e) {

		}

//		if(textDetail != null){
//			if(itemsTextDetail != null){
//				textDetail.setText(itemsTextDetail.get(position));
//			}else{
//				textDetail.setVisibility(View.GONE);
//			}
//		}

		return v;
	}

	private void loadImage(ImageView dest, String uri) {

		if (network == true) {
			MainActivity.loadImage(context, dest, uri);
		}
		else {
			Glide.with(context)
					.load(context.getResources().getIdentifier(uri, "drawable", context.getPackageName()))
					.into(dest);
		}

	}
}
