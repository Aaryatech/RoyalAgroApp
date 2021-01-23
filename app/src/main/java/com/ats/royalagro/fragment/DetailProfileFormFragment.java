package com.ats.royalagro.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.FarmerDataByTempId;
import com.ats.royalagro.bean.SocDataList;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.PermissionUtil;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailProfileFormFragment extends Fragment implements View.OnClickListener {

    private ImageView ivImage;
    private TextView tvName;
    private LinearLayout llPersonal, llBank, llKYC, llPlot, llCrop, llProfilePhoto;
    int fId;
    public static int tempId, socId;
    public static String socData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_profile_form, container, false);
        getActivity().setTitle("Farmer");

        try {
            tempId = getArguments().getInt("Temp_Id");
            socId = getArguments().getInt("SocietyId");
            socData = getArguments().getString("TempSocData");
            Log.e("tempId : ", "-----------------------" + tempId);
            Log.e("socId : ", "-----------------------" + socId);
            Log.e("socData : ", "-----------------------" + socData);
        } catch (Exception e) {
            Log.e("Add Farmers : ", "EXCEPTION ---------------" + e.getMessage());
            e.printStackTrace();
        }

        Log.e("TEMP ID : ", "---------------------" + tempId);

        if (PermissionUtil.checkAndRequestPermissions(getActivity())) {

        }

        ivImage = view.findViewById(R.id.ivProfileImage);
        tvName = view.findViewById(R.id.tvProfileName);

        llPersonal = view.findViewById(R.id.llLandOwnerProfile);
        llBank = view.findViewById(R.id.llBankProfile);
        llKYC = view.findViewById(R.id.llKYCProfile);
        llPlot = view.findViewById(R.id.llPlotProfile);
        llCrop = view.findViewById(R.id.llCropProfile);
        llProfilePhoto = view.findViewById(R.id.llLandOwnerProfilePhoto);


        llPersonal.setOnClickListener(this);
        llBank.setOnClickListener(this);
        llKYC.setOnClickListener(this);
        llPlot.setOnClickListener(this);
        llCrop.setOnClickListener(this);
        llProfilePhoto.setOnClickListener(this);

        try {
            Picasso.with(getContext())
                    .load("img")
                    .placeholder(R.drawable.img)
                    .error(R.drawable.img)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(ivImage);

        } catch (Exception e) {
        }

        if (tempId > 0) {
            try {
                Log.e("Add FARMER   ","----------------------------IF BLOCK");
                Gson gson = new Gson();
                SocDataList socDataList = gson.fromJson(socData, SocDataList.class);
                tvName.setText("" + socDataList.getFarmerFname() + " " + socDataList.getFarmerMname() + " " + socDataList.getFarmerLname());
            } catch (Exception e) {
                Log.e("Add FARMER   ","--EXCEPTION-------------------------"+e.getMessage());
            }
            getFarmerHeaderData(tempId);

        }



        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llLandOwnerProfile) {
            Fragment adf = new PersonalDetailsFragment();
            Bundle args = new Bundle();
            args.putInt("FarmerId", fId);
            args.putInt("TempId", tempId);
            args.putInt("SocietyId", socId);
            args.putString("TempSocData", socData);
            adf.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();
        } else if (v.getId() == R.id.llLandOwnerProfilePhoto) {
            if (fId > 0) {
                Fragment adf = new ProfilePhotoFragment();
                Bundle args = new Bundle();
                args.putInt("FarmerId", fId);
                args.putInt("TempId", tempId);
                args.putInt("SocietyId", socId);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();
            } else {
                Toast.makeText(getActivity(), "First Fill Land Owner Profile", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.llBankProfile) {
            if (fId > 0) {
                Fragment adf = new BankDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("FarmerId", fId);
                args.putInt("TempId", tempId);
                args.putInt("SocietyId", socId);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();
            } else {
                Toast.makeText(getActivity(), "First Fill Land Owner Profile", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.llKYCProfile) {
            if (fId > 0) {
                Fragment adf = new KYCListFragment();
                Bundle args = new Bundle();
                args.putInt("FarmerId", fId);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();
            } else {
                Toast.makeText(getActivity(), "First Fill Land Owner Profile", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.llPlotProfile) {
            if (fId > 0) {
                Fragment adf = new PlotListFragment();
                Bundle args = new Bundle();
                args.putInt("FarmerId", fId);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();
            } else {
                Toast.makeText(getActivity(), "First Fill Land Owner Profile", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.llCropProfile) {
            if (fId > 0) {
                Fragment adf = new CropListFragment();
                Bundle args = new Bundle();
                args.putInt("FarmerId", fId);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();
            } else {
                Toast.makeText(getActivity(), "First Fill Land Owner Profile", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void getFarmerHeaderData(final int tempId) {
        Log.e("getFarmerData  :","-------------------------------"+tempId);
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
            final Call<FarmerDataByTempId> dataByTempIdCall = api.getFarmerHeaderData(tempId);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            dataByTempIdCall.enqueue(new Callback<FarmerDataByTempId>() {
                @Override
                public void onResponse(Call<FarmerDataByTempId> call, retrofit2.Response<FarmerDataByTempId> response) {
                    try {
                        if (response.body() != null) {
                            FarmerDataByTempId data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();
                                if (data.getFarmerHeader() != null) {
                                    Log.e("Header : ", "------" + data);
                                    fId = data.getFarmerHeader().getfId();
                                    tvName.setText("" + data.getFarmerHeader().getfName());
                                    Log.e("Profile Pic : ", "------" + data.getFarmerHeader().getfProfilePic());

                                    String image = ApiInterface.PROFILE_PATH + "" + data.getFarmerHeader().getfProfilePic();
                                    try {
                                        Picasso.with(getContext())
                                                .load(image)
                                                .placeholder(R.drawable.img)
                                                .error(R.drawable.img)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_STORE)
                                                .into(ivImage);
                                    } catch (Exception e) {
                                    }

                                }
                            }
                        } else {
                            progressDialog.dismiss();
                            Log.e("data : ", "----Null");

                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();

                        Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<FarmerDataByTempId> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();

                    Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_all_society);
        item.setVisible(false);
    }


}
