package com.hmif.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hmif.appssakti.R;

import java.util.List;

/**
 * Created by gilang on 06/07/2015.
 */
public class TitleContentAdapter extends BaseAdapter {

	private List<String> titles;
	private List<String> contents;
	private Context context;

	public TitleContentAdapter(Context context, List<String> titles, List<String> contents){
		this.titles = titles;
		this.contents = contents;
		this.context = context;
	}

	@Override
	public int getCount() {
		return contents.size();
	}

	@Override
	public Object getItem(int position) {
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(v == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			v = inflater.inflate(R.layout.title_content_item, parent, false);
		}

		TextView mTitle = (TextView) v.findViewById(R.id.title);
		TextView mContent = (TextView) v.findViewById(R.id.content);

		mTitle.setText(titles.get(position));
		mContent.setText(contents.get(position));

		return v;
	}
}
