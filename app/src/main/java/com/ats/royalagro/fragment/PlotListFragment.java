package com.ats.royalagro.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.FarmerPlotList;
import com.ats.royalagro.bean.FarmerPlotListData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlotListFragment extends Fragment {

    private FloatingActionButton fab;
    ArrayList<FarmerPlotList> plotLists = new ArrayList<>();
    private ListView lvPlot;
    PlotListAdapter adapter;
    public static int fId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plot_list, container, false);
        getActivity().setTitle("Plot Details");

        try {
            fId = getArguments().getInt("FarmerId");
        } catch (Exception e) {
        }

        lvPlot = view.findViewById(R.id.lvPlotsList);
        fab = view.findViewById(R.id.fabAddPlot);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment adf = new PlotDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("FarmerId", fId);
                args.putString("Bean", null);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "PlotMaster").commit();
            }
        });

        getPlots(fId);

        return view;
    }

    public void getPlots(int fId) {
        if (CheckNetwork.isInternetAvailable(getContext())) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            ApiInterface api = retrofit.create(ApiInterface.class);
            final Call<FarmerPlotListData> plotListDataCall = api.getPlotList(fId);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            plotListDataCall.enqueue(new Callback<FarmerPlotListData>() {
                @Override
                public void onResponse(Call<FarmerPlotListData> call, retrofit2.Response<FarmerPlotListData> response) {
                    try {
                        if (response.body() != null) {
                            FarmerPlotListData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();
                                plotLists.clear();
                                for (int i = 0; i < data.getFarmerPlotList().size(); i++) {
                                    plotLists.add(data.getFarmerPlotList().get(i));
                                }

                                adapter = new PlotListAdapter(getContext(), plotLists);
                                lvPlot.setAdapter(adapter);
                            }
                        } else {
                            progressDialog.dismiss();
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<FarmerPlotListData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class PlotListAdapter extends BaseAdapter {

        Context context;
        private ArrayList<FarmerPlotList> originalValues;
        private ArrayList<FarmerPlotList> displayedValues;
        LayoutInflater inflater;

        public PlotListAdapter(Context context, ArrayList<FarmerPlotList> eventLists) {
            this.context = context;
            this.originalValues = eventLists;
            this.displayedValues = eventLists;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return displayedValues.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            v = inflater.inflate(R.layout.custom_plot_list_layout, null);

            TextView tvName = v.findViewById(R.id.tvPlotList_Name);
            TextView tvPlotNo = v.findViewById(R.id.tvPlotList_PlotNo);
            TextView tvPlotArea = v.findViewById(R.id.tvPlotList_plotArea);
            TextView tvPlotWaterRes = v.findViewById(R.id.tvPlotList_WaterRes);
            ImageView ivPopup = v.findViewById(R.id.ivPlotList_Menu);

            tvName.setText("" + displayedValues.get(position).getPlotOwnerName() + " - " + displayedValues.get(position).getRelation());
            tvPlotNo.setText("Plot No. : " + displayedValues.get(position).getfPlotNo());
            tvPlotArea.setText("Area : " + displayedValues.get(position).getfPlotArea());
            tvPlotWaterRes.setText("Water Resource : " + displayedValues.get(position).getWaterResource());

            ivPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.society_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.society_edit) {

                                FarmerPlotList farmerPlotList = new FarmerPlotList(displayedValues.get(position).getfPlotId(), displayedValues.get(position).getfId(), displayedValues.get(position).getfGatNo(), displayedValues.get(position).getfPlotNo(), displayedValues.get(position).getPlotOwnerName(), displayedValues.get(position).getMobileNo(), displayedValues.get(position).getAadharNo(), displayedValues.get(position).getPanNo(), displayedValues.get(position).getRelation(), displayedValues.get(position).getfPlotArea(), displayedValues.get(position).getfWaterResourceRemark(), displayedValues.get(position).getWaterResource(), displayedValues.get(position).getIsUsed());
                                Gson gson = new Gson();
                                String json = gson.toJson(farmerPlotList);

                                Fragment adf = new PlotDetailsFragment();
                                Bundle args = new Bundle();
                                args.putInt("FarmerId", fId);
                                args.putString("Bean", json);
                                adf.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "PlotMaster").commit();

                            } else if (menuItem.getItemId() == R.id.society_delete) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                                builder.setTitle("Confirm Action");
                                builder.setMessage("Do You Really Want To Delete?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        FarmerPlotList farmerPlotList = new FarmerPlotList(displayedValues.get(position).getfPlotId(), displayedValues.get(position).getfId(), displayedValues.get(position).getfGatNo(), displayedValues.get(position).getfPlotNo(), displayedValues.get(position).getPlotOwnerName(), displayedValues.get(position).getMobileNo(), displayedValues.get(position).getAadharNo(), displayedValues.get(position).getPanNo(), displayedValues.get(position).getRelation(), displayedValues.get(position).getfPlotArea(), displayedValues.get(position).getfWaterResourceRemark(), displayedValues.get(position).getWaterResource(), 0);
                                        addPlot(farmerPlotList);
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            return true;
                        }
                    });
                    //   ShowPopupMenuIcon.setForceShowIcon(popupMenu);
                    popupMenu.show();
                }
            });

            return v;
        }
    }


    public void addPlot(final FarmerPlotList farmerPlotList) {
        if (CheckNetwork.isInternetAvailable(getContext())) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<FarmerPlotList> farmerPlotListCall = api.addFarmerPlot(farmerPlotList);

            final android.app.AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            farmerPlotListCall.enqueue(new Callback<FarmerPlotList>() {
                @Override
                public void onResponse(Call<FarmerPlotList> call, retrofit2.Response<FarmerPlotList> response) {
                    try {
                        if (response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                            lvPlot.setAdapter(null);
                            getPlots(fId);

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FarmerPlotList> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }

    }

}
