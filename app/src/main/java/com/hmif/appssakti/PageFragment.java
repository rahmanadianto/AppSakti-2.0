package com.hmif.appssakti;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends Fragment {

    String title;
    String content;

    public PageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        Bundle bundle = getArguments();
        title = bundle.getString("title");
        content = bundle.getString("content");

        TextView textTitle = (TextView) view.findViewById(R.id.page_title);
        textTitle.setText(Html.fromHtml(title));
        TextView textContent = (TextView) view.findViewById(R.id.page_content);
        textContent.setText(Html.fromHtml(content));

        return view;
    }

}
