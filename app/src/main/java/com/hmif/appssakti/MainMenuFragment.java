package com.hmif.appssakti;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hmif.custom.MainMenuListener;

import java.util.List;

/**
 * Created by gilang on 15/07/2015.
 */
public class MainMenuFragment extends Fragment {

	private List<String> mainMenuItem;
	private List<Integer> mainMenuIcon;
	private Context context;
	private Toolbar toolbar;
	private MainMenuListener mListener;
	private static String CATEGORY = "menu";
	private int[] viewId;
	private SearchView searchView;

	public MainMenuFragment(){}

	public static MainMenuFragment newInstance(Context context, List<String> mainMenuItem,
											   List<Integer> mainMenuIcon){
		MainMenuFragment fragment = new MainMenuFragment();
		fragment.mainMenuIcon = mainMenuIcon;
		fragment.mainMenuItem = mainMenuItem;
		fragment.context = context;
		fragment.viewId = new int[9];
		//Kemahasiswaan
		fragment.viewId[0] = R.id.card1;
		//Kongres
		fragment.viewId[1] = R.id.card2;
		//Kabinet
		fragment.viewId[2] = R.id.card3;
		//Himpunan
		fragment.viewId[3] = R.id.card4;
		//Unit
		fragment.viewId[4] = R.id.card5;
		//MWA-WM
		fragment.viewId[5] = R.id.card6;
		//Team Beasiswa
		fragment.viewId[6] = R.id.card7;

		//Kantin
		fragment.viewId[7] = R.id.card8;
		//Ruang
		fragment.viewId[8] = R.id.card9;

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_main_menu, parent, false);

		toolbar = (Toolbar) v.findViewById(R.id.toolbar);
		toolbar.setBackgroundColor(getResources().getColor(R.color.hmif_color));
		((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
		(getActivity()).setTitle(null);

		for(int i = 0; i < 9; i++){
			View card = v.findViewById(viewId[i]);
			final int itemId = i;
			card.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//this method implemented in MainActivity
					mListener.onMenuClicked(itemId);
				}
			});
		}
		return v;
	}

	@Override
	public void onAttach(Context activity){
		super.onAttach(activity);
		try{
			mListener = (MainMenuListener) activity;
		}
		catch(ClassCastException e){
			throw new ClassCastException("Activity must implement OnCardClickListener");
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		MenuItem menuItem = menu.findItem(R.id.action_search);

		SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

		searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

		// Assumes current activity is the searchable activity
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
		searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

		super.onCreateOptionsMenu(menu, inflater);
	}
}
