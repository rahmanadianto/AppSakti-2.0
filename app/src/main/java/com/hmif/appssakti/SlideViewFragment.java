package com.hmif.appssakti;

import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Rahman Adianto
 */

public class SlideViewFragment extends Fragment {

    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private JSONArray array;
    private String image;
    private int arrayLength;
    private Toolbar toolbar;
    private String toolbarTitle;

    public SlideViewFragment() {
        // Required empty public constructor
    }

    public static SlideViewFragment newInstance(JSONObject data) {

        SlideViewFragment fragment = new SlideViewFragment();

        try {
            if (data.has("menu"))
                fragment.toolbarTitle = data.getString("menu");
            else if (data.has("judul"))
                fragment.toolbarTitle = data.getString("judul");

            if (data.has("content image"))
                fragment.image = data.getString("content image");
            else
                fragment.image = "";

            fragment.array = data.getJSONArray("pages");
            fragment.arrayLength = fragment.array.length();
        }
        catch (JSONException ex) {

        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide_view, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.hmif_color));
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(toolbarTitle);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getChildFragmentManager(), getActivity());

        if (image != "")
            addImagePage("Bagan Kemahasiswaan KM ITB", image);

        for (int i = 0; i < arrayLength; i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                addPage(obj.getString("page_title"), obj.getString("page_content"));
            }
            catch (JSONException ex) {
                Log.d("Fragment", ex.toString());
            }
        }

        viewPager.setAdapter(adapter);

        return view;
    }

    private void addPage(String title, String content) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);

        PageFragment pageFragment = new PageFragment();
        pageFragment.setArguments(bundle);

        try {
            adapter.addFrag(pageFragment);
            adapter.notifyDataSetChanged();

            viewPager.setCurrentItem(0);
        }
        catch (Exception ex) {
        }
    }

    private void addImagePage(String title, String image) {

        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("image", image);

        ImagePageFragment pageFragment = new ImagePageFragment();
        pageFragment.setArguments(bundle);

        try {
            adapter.addFrag(pageFragment);
            adapter.notifyDataSetChanged();

            viewPager.setCurrentItem(0);
        }
        catch (Exception ex) {
        }
    }

}
