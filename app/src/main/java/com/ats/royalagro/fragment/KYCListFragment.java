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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.FarmerKycList;
import com.ats.royalagro.bean.KYCListData;
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

public class KYCListFragment extends Fragment {

    private FloatingActionButton fab;
    ArrayList<FarmerKycList> kycLists = new ArrayList<>();
    private ListView lvKyc;
    public static int fId;

    KYCListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kyclist, container, false);
        getActivity().setTitle("KYC Details");

        lvKyc = view.findViewById(R.id.lvKYC);

        try {
            fId = getArguments().getInt("FarmerId");
        } catch (Exception e) {
        }


        fab = view.findViewById(R.id.fabAddKYC);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment adf = new KYCDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("FarmerId", fId);
                args.putString("Bean", null);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "KYCMaster").commit();
            }
        });

        getKYC(fId);

        return view;
    }

    public void getKYC(int fId) {
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
            final Call<KYCListData> kycListDataCall = api.getKYCList(fId);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            kycListDataCall.enqueue(new Callback<KYCListData>() {
                @Override
                public void onResponse(Call<KYCListData> call, retrofit2.Response<KYCListData> response) {
                    try {
                        if (response.body() != null) {
                            KYCListData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();
                                kycLists.clear();
                                for (int i = 0; i < data.getFarmerKycList().size(); i++) {
                                    kycLists.add(data.getFarmerKycList().get(i));
                                }

                                adapter = new KYCListAdapter(getContext(), kycLists);
                                lvKyc.setAdapter(adapter);
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
                public void onFailure(Call<KYCListData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }


    class KYCListAdapter extends BaseAdapter {

        Context context;
        private ArrayList<FarmerKycList> originalValues;
        private ArrayList<FarmerKycList> displayedValues;
        LayoutInflater inflater;

        public KYCListAdapter(Context context, ArrayList<FarmerKycList> eventLists) {
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
            v = inflater.inflate(R.layout.custom_kyc_list_layout, null);

            TextView tvName = v.findViewById(R.id.tvKYCList_Name);
            CheckBox cbPhoto = v.findViewById(R.id.cbKYCList_Photo);
            ImageView ivPopup = v.findViewById(R.id.ivKYCList_menu);

            int type = displayedValues.get(position).getKycType();
            if (type == 0) {
                tvName.setText("Aadhar Card");
            } else if (type == 1) {
                tvName.setText("Election Card");
            } else if (type == 2) {
                tvName.setText("PAN");
            } else if (type == 3) {
                tvName.setText("Passport");
            } else if (type == 4) {
                tvName.setText("Driving Liscense");
            } else if (type == 5) {
                tvName.setText("Bank Passbook");
            } else if (type == 6) {
                tvName.setText("7-12");
            }

            if (displayedValues.get(position).getKycPhoto().isEmpty()) {
                cbPhoto.setChecked(false);
            } else {
                cbPhoto.setChecked(true);
            }

            ivPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.society_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.society_edit) {

                                FarmerKycList farmerKycList = new FarmerKycList(displayedValues.get(position).getKycId(), displayedValues.get(position).getfId(), displayedValues.get(position).getKycType(), displayedValues.get(position).getKycNo(), displayedValues.get(position).getKycPhoto(), displayedValues.get(position).getKycRemarks(), displayedValues.get(position).getKycIsVerified(), displayedValues.get(position).getKycVerifBy(), displayedValues.get(position).getKycVerifDate(),displayedValues.get(position).getIsUsed());
                                Gson gson = new Gson();
                                String json = gson.toJson(farmerKycList);

                                Fragment adf = new KYCDetailsFragment();
                                Bundle args = new Bundle();
                                args.putInt("FarmerId", fId);
                                args.putString("Bean", json);
                                adf.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "KYCMaster").commit();
                            } else if (menuItem.getItemId() == R.id.society_delete) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                                builder.setTitle("Confirm Action");
                                builder.setMessage("Do You Really Want To Delete?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        FarmerKycList farmerKycList = new FarmerKycList(displayedValues.get(position).getKycId(), displayedValues.get(position).getfId(), displayedValues.get(position).getKycType(), displayedValues.get(position).getKycNo(), displayedValues.get(position).getKycPhoto(), displayedValues.get(position).getKycRemarks(), displayedValues.get(position).getKycIsVerified(), displayedValues.get(position).getKycVerifBy(), displayedValues.get(position).getKycVerifDate(),0);
                                        addKYCData(farmerKycList);
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
                                // Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
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

    public void addKYCData(FarmerKycList farmerKycList) {
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
            Call<FarmerKycList> farmerKycListCall = api.addFarmerKYC(farmerKycList);

            final android.app.AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();


            farmerKycListCall.enqueue(new Callback<FarmerKycList>() {
                @Override
                public void onResponse(Call<FarmerKycList> call, retrofit2.Response<FarmerKycList> response) {
                    try {
                        if (response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                            lvKyc.setAdapter(null);
                            getKYC(fId);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FarmerKycList> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }

    }

}
