package com.hmif.intro;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hmif.appssakti.MainActivity;
import com.hmif.appssakti.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class IntroFragment extends Fragment {

	private int position;
	private ImageView image;
	private TextView judulIntro;
	private TextView detailIntro;

	public static IntroFragment newInstance(int position){
		IntroFragment fragment = new IntroFragment();
		fragment.position = position;
		return fragment;
	}

	public IntroFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_intro, container, false);
		image = (ImageView) v.findViewById(R.id.image);
		judulIntro = (TextView) v.findViewById(R.id.judul_intro);
		detailIntro = (TextView) v.findViewById(R.id.detail_intro);

		switch (position){
			case 0: image.setImageResource(R.drawable.intro1);
					judulIntro.setText("Memajukan ilmu pengetahuan");
					detailIntro.setText("Cakap dan mandiri dalam memelihara dan memajukan ilmu pengetahuan");
					break;
			case 1: image.setImageResource(R.drawable.intro2);
					judulIntro.setText("Memangku Jabatan");
					detailIntro.setText("Cakap memangku jabatan dan atau pekerjaan dalam masyarakat");
					break;
			case 2: image.setImageResource(R.drawable.intro3);
					judulIntro.setText("Kesejahteraan Masyarakatnya");
					detailIntro.setText("Memiliki keinsafan tanggung jawab atas kesejahteraan masyarakatnya");
					break;
		}
		return v;
	}
}
