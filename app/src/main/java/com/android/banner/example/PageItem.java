package com.android.banner.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Hohenheim on 16/2/29.
 */
public class PageItem extends Fragment {
    private int mColorId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mColorId = getArguments().getInt(String.valueOf(hashCode()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_item, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.test_image_view);
        imageView.setBackgroundColor(ContextCompat.getColor(getContext(), mColorId));
        return view;
    }
}