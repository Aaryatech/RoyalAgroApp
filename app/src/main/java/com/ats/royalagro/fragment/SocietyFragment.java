package com.ats.royalagro.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.ats.royalagro.R;
import com.ats.royalagro.util.SocietyDetailsInterface;
import com.ats.royalagro.util.SocietyNoDetailsInterface;

public class SocietyFragment extends Fragment implements View.OnClickListener {

    public static int countNew = 0, countReg = 0;
    private EditText edSearch;
    private FloatingActionButton fab;
    private ListView lvMettings;

    private ViewPager viewPager;
    public static TabLayout tabSoc;
    FragmentPagerAdapter adapterViewPager;
    public static int eventId;
    public static String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_society, container, false);

        try {
            title = getArguments().getString("Title");
            eventId = getArguments().getInt("EventId");
        } catch (Exception e) {
        }
        getActivity().setTitle(title);

        viewPager = view.findViewById(R.id.viewPagerSociety);
        tabSoc = view.findViewById(R.id.tabSociety);

        edSearch = view.findViewById(R.id.edSociety_Search);
        fab = view.findViewById(R.id.fabAddSociety);
        lvMettings = view.findViewById(R.id.lvSociety);

        if (eventId==0){
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(this);

        Log.e("EVENT ID : ","----------"+eventId);

        adapterViewPager = new ViewPagerAdapter(getChildFragmentManager(), getContext(), eventId,title);
        viewPager.setAdapter(adapterViewPager);
        tabSoc.post(new Runnable() {
            @Override
            public void run() {
                try {
                    tabSoc.setupWithViewPager(viewPager);
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
                    SocietyDetailsInterface fragment = (SocietyDetailsInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragment != null) {
                        fragment.fragmentBecameVisible();
                    }
                } else if (position == 1) {
                    SocietyNoDetailsInterface fragmentInterested = (SocietyNoDetailsInterface) adapterViewPager.instantiateItem(viewPager, position);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fabAddSociety) {
            Fragment adf = new AddSocietyFragment();
            Bundle args = new Bundle();
            args.putString("EventName", title);
            args.putInt("EventId", eventId);
            args.putInt("SocietyId", 0);
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "SocietyMaster").commit();

        }

    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {


        private Context mContext;
        private int eventId;
        private String eventName;

        public ViewPagerAdapter(FragmentManager fm, Context mContext, int eventId,String eventName) {
            super(fm);
            this.mContext = mContext;
            this.eventId = eventId;
            this.eventName=eventName;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SocietyDetailsFragment(eventId,eventName);
            } else {
                return new SocietyNoDetailsFragment(eventId,eventName);
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
                    return "New";
                case 1:
                    return "Registered";
                default:
                    return null;
            }
        }
    }
}
