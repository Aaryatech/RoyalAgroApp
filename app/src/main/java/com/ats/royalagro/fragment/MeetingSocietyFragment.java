package com.ats.royalagro.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.royalagro.R;
import com.ats.royalagro.util.MeetingSocNewInterface;
import com.ats.royalagro.util.MeetingSocRegisterInterface;
import com.ats.royalagro.util.SocietyDetailsInterface;
import com.ats.royalagro.util.SocietyNoDetailsInterface;

public class MeetingSocietyFragment extends Fragment {

    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tab;
    FragmentPagerAdapter adapterViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_society, container, false);

        String title = getArguments().getString("Title");
        getActivity().setTitle("" + title);

        viewPager = view.findViewById(R.id.viewPagerMeetingSociety);
        tab = view.findViewById(R.id.tabMeetingSociety);

        fab = view.findViewById(R.id.fabAddSociety1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main, new AddSocietyFragment());
                transaction.addToBackStack("");
                transaction.commit();
            }
        });


        adapterViewPager = new ViewPagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapterViewPager);
        tab.post(new Runnable() {
            @Override
            public void run() {
                try {
                    tab.setupWithViewPager(viewPager);
                } catch (Exception e) {
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Log.e("POSITION : ", "----------------------" + position);

                if (position == 0) {
                    MeetingSocNewInterface fragment = (MeetingSocNewInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragment != null) {
                        fragment.fragmentBecameVisible();
                    }
                } else if (position == 1) {
                    MeetingSocRegisterInterface fragmentInterested = (MeetingSocRegisterInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentInterested != null) {
                        fragmentInterested.fragmentBecameVisible();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {


        private Context mContext;
        private String jobId, jobProId;

        public ViewPagerAdapter(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
            this.jobId = jobId;
            this.jobProId = jobProId;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MeetingSocNewFragment();
            } else {
                return new MeetingSocRegisterFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "New (1) ";
                case 1:
                    return "Registered (2)";
                default:
                    return null;
            }
        }
    }
}
