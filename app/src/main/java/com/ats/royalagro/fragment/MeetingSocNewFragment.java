package com.ats.royalagro.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.royalagro.R;
import com.ats.royalagro.util.MeetingSocNewInterface;

public class MeetingSocNewFragment extends Fragment implements MeetingSocNewInterface{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_soc_new, container, false);

        return view;
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
