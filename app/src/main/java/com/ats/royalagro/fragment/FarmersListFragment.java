package com.ats.royalagro.fragment;


import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ats.royalagro.R;
import com.ats.royalagro.activity.DetailRegistrationActivity;
import com.ats.royalagro.util.FarmersIntrestedInterface;
import com.ats.royalagro.util.FarmersNotIntrestedInterface;
import com.ats.royalagro.util.FarmersPendingInterface;

public class FarmersListFragment extends Fragment implements View.OnClickListener {

    public static int countPending = 0, countIntrested = 0, countNotIntrested = 0;

    private EditText edSearch;
    private ListView lvFarmers;

    private LinearLayout ll1, ll2, ll3;

    private ViewPager viewPager;
    public static TabLayout tabFarmer;
    FragmentPagerAdapter adapterViewPager;

    public static String socName;
    public static int socId;
    int empId;

    private FloatingActionButton fabAddFarmer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmers_list, container, false);

        try {
            socName = getArguments().getString("SocietyName");
            socId = getArguments().getInt("SocietyId");
        } catch (Exception e) {
        }
        getActivity().setTitle(socName);


        viewPager = view.findViewById(R.id.viewPagerFarmers);
        tabFarmer = view.findViewById(R.id.tabFarmers);
        fabAddFarmer = view.findViewById(R.id.fabAddFarmer);
        fabAddFarmer.setOnClickListener(this);

        edSearch = view.findViewById(R.id.edFarmers_Search);
        lvFarmers = view.findViewById(R.id.lvFarmers);

        ll1 = view.findViewById(R.id.llFarmer1);
        ll2 = view.findViewById(R.id.llFarmer2);
        ll3 = view.findViewById(R.id.llFarmer3);
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DetailRegistrationActivity.class));
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DetailRegistrationActivity.class));
            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DetailRegistrationActivity.class));
            }
        });


        adapterViewPager = new ViewPagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(adapterViewPager);
        tabFarmer.post(new Runnable() {
            @Override
            public void run() {
                try {
                    tabFarmer.setupWithViewPager(viewPager);
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
                    FarmersPendingInterface fragment = (FarmersPendingInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragment != null) {
                        fragment.fragmentBecameVisible();
                    }
                } else if (position == 1) {
                    FarmersIntrestedInterface fragmentInterested = (FarmersIntrestedInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentInterested != null) {
                        fragmentInterested.fragmentBecameVisible();
                    }
                } else if (position == 2) {
                    FarmersNotIntrestedInterface fragmentInterested = (FarmersNotIntrestedInterface) adapterViewPager.instantiateItem(viewPager, position);
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
    public void onClick(View view) {
        if (view.getId() == R.id.fabAddFarmer) {
            Fragment adf = new DetailProfileFormFragment();
            Bundle args = new Bundle();
            args.putInt("Emp_Id", empId);
            args.putInt("Temp_Id", 0);
            args.putInt("SocietyId", socId);
            args.putString("TempSocData", "");
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "FarmerMaster").commit();
        }
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
                return new FarmerPendingFragment(socId);
            } else if (position == 1) {
                return new FarmerIntrestedFragment(socId);
            } else {
                return new FarmerNotIntrestedFragment(socId);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Pending";
                case 1:
                    return "Intrested";
                case 2:
                    return "Not Intrested";
                default:
                    return null;
            }
        }
    }

}
