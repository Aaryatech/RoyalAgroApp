package com.ats.royalagro.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ats.royalagro.R;
import com.ats.royalagro.util.MeetingsDoneInterface;
import com.ats.royalagro.util.MeetingsUpcomingInterface;

public class MeetingsFragment extends Fragment implements View.OnClickListener {

    private EditText edSearch;
    private FloatingActionButton fab;
    private ListView lvMettings;
    private LinearLayout ll1, ll2, ll3;

    private ViewPager viewPager;
    public static TabLayout tab;
    FragmentPagerAdapter adapterViewPager;

    public static int countDone = 0, countUpcoming = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetings, container, false);
        getActivity().setTitle("Meetings");

        viewPager = view.findViewById(R.id.viewPagerMeetings);
        tab = view.findViewById(R.id.tabMeetings);

        edSearch = view.findViewById(R.id.edMeetings_Search);
        fab = view.findViewById(R.id.fabAddMettings);
        lvMettings = view.findViewById(R.id.lvMeetings);

        fab.setOnClickListener(this);

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
                    MeetingsUpcomingInterface fragmentInterested = (MeetingsUpcomingInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentInterested != null) {
                        fragmentInterested.fragmentBecameVisible();
                    }

                } else if (position == 1) {
                    MeetingsDoneInterface fragment = (MeetingsDoneInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragment != null) {
                        fragment.fragmentBecameVisible();
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
        if (v.getId() == R.id.fabAddMettings) {
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.content_main, new AddMeetingsFragment(), "MeetingMaster");
//            transaction.commit();
            Fragment adf = new AddMeetingsFragment();
            Bundle args = new Bundle();
            args.putInt("EventId", 0);
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "MeetingMaster").commit();

        }

    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public ViewPagerAdapter(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MeetingsUpcomingFragment();
            } else {
                return new MeetingsDoneFragment();
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
                    return "Upcoming";
                case 1:
                    return "Done";
                default:
                    return null;
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_all_society);
        item.setVisible(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_society:
                Fragment adf = new SocietyFragment();
                Bundle args = new Bundle();
                args.putString("Title", "All Society");
                args.putInt("EventId", 0);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "MeetingMaster").commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
