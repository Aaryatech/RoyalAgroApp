package com.ats.royalagro.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.royalagro.R;
import com.ats.royalagro.util.MeetingSocRegisterInterface;

public class MeetingSocRegisterFragment extends Fragment implements MeetingSocRegisterInterface{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_soc_register, container, false);

        return view;
    }

    @Override
    public void fragmentBecameVisible() {

    }
}
