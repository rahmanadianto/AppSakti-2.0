package com.hmif.appssakti;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePageFragment extends Fragment {

    String title;
    String content;

    public ImagePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_page, container, false);

        Bundle bundle = getArguments();
        title = bundle.getString("title");
        content = bundle.getString("image");

        TextView textTitle = (TextView) view.findViewById(R.id.image_title);
        textTitle.setText(title);
        ImageView imageContent = (ImageView) view.findViewById(R.id.image_content);
        MainActivity.loadImage(getContext(), imageContent, content);

        return view;
    }

}
