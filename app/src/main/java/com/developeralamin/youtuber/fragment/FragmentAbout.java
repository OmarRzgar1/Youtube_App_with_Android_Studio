package com.developeralamin.youtuber.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developeralamin.youtuber.BuildConfig;
import com.developeralamin.youtuber.R;

public class FragmentAbout extends Fragment {

    public FragmentAbout() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);


        return view;
    }

}
