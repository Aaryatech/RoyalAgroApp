package com.ats.royalagro.fragment;


import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.District;
import com.ats.royalagro.bean.DistrictData;
import com.ats.royalagro.bean.DistrictList;
import com.ats.royalagro.bean.GaonData;
import com.ats.royalagro.bean.GaonList;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.bean.Region;
import com.ats.royalagro.bean.RegionData;
import com.ats.royalagro.bean.RegionList;
import com.ats.royalagro.bean.TalukaData;
import com.ats.royalagro.bean.TalukaList;
import com.ats.royalagro.bean.UserAccessData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {


    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "RoyalAgro");
    File f;

    int empId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");

        createFolder();

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
        }

        TextView tvImage = view.findViewById(R.id.tvImageDemo);
        tvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(getContext(), ImageDemo.class));
            }
        });


        String jsonRegion = pref.getString("Region", "");
        Region region = gson.fromJson(jsonRegion, Region.class);
        Log.e("Region : ", "---------------" + region);

        String jsonDist = pref.getString("District", "");
        District district = gson.fromJson(jsonDist, District.class);
        Log.e("District : ", "---------------" + district);

        String jsonTal = pref.getString("Taluka", "");
        Type type = new TypeToken<ArrayList<TalukaList>>() {
        }.getType();
        ArrayList<TalukaList> talList = gson.fromJson(jsonTal, type);
        Log.e("Taluka : ", "---------------" + talList);

        String jsonGaon = pref.getString("Gaon", "");
        Type type1 = new TypeToken<ArrayList<GaonList>>() {
        }.getType();
        ArrayList<GaonList> gaonList = gson.fromJson(jsonGaon, type1);
        Log.e("Gaon : ", "---------------" + gaonList);


        String jsonReg = pref.getString("RegionArray", "");
        Type typeReg = new TypeToken<ArrayList<RegionList>>() {
        }.getType();
        ArrayList<RegionList> regionLists = gson.fromJson(jsonReg, typeReg);
        Log.e("All REGIONS : ", "---------------" + regionLists);

        return view;
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_sync);
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
            case R.id.action_sync:
                getEmpAccessData(empId);
                getUserData(empId);
                getAllRegionData();
                getAllDistrictData();
                getAllTalukaData();
                getAllGaonData();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void getEmpAccessData(int emp_id) {

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
            Call<UserAccessData> userAccessDataCall = api.getUserAccessData(emp_id);

            final android.app.AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            userAccessDataCall.enqueue(new Callback<UserAccessData>() {
                @Override
                public void onResponse(Call<UserAccessData> call, retrofit2.Response<UserAccessData> response) {
                    try {
                        if (response.body() != null) {
                            UserAccessData data = response.body();
                            if (data.getError()) {
                                progressDialog.dismiss();
                                Log.e("Error : ", "-------------");
                            } else {
                                progressDialog.dismiss();
                                Log.e("Access Data : ", "-------------" + data);
                                Region region = data.getRegion();
                                District district = data.getDistrict();

                                ArrayList<TalukaList> talukaLists = new ArrayList<>();
                                if (data.getTalukaList().size() > 0) {
                                    for (int i = 0; i < data.getTalukaList().size(); i++) {
                                        talukaLists.add(data.getTalukaList().get(i));
                                    }
                                }

                                ArrayList<GaonList> gaonLists = new ArrayList<>();
                                if (data.getGaonList().size() > 0) {
                                    for (int i = 0; i < data.getGaonList().size(); i++) {
                                        gaonLists.add(data.getGaonList().get(i));
                                    }
                                }

                                SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                Gson gson = new Gson();
                                String jsonRegion = gson.toJson(region);
                                editor.putString("Region", jsonRegion);
                                String jsonDistrict = gson.toJson(district);
                                editor.putString("District", jsonDistrict);
                                String jsonTaluka = gson.toJson(talukaLists);
                                editor.putString("Taluka", jsonTaluka);
                                String jsonGaon = gson.toJson(gaonLists);
                                editor.putString("Gaon", jsonGaon);
                                editor.apply();
                                editor.apply();
                            }

                        } else {
                            progressDialog.dismiss();
                            Log.e("HOME_FRAGMENT : ", "---- No Access Data");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HOME_FRAGMENT : ", "----  Access Data -- Exception : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UserAccessData> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserData(int emp_id) {

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
            Call<LoginData> loginDataCall = api.getUserData(emp_id);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();


            loginDataCall.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, retrofit2.Response<LoginData> response) {
                    if (response.body() != null) {
                        LoginData data = response.body();
                        if (data.getError()) {
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            Gson gson = new Gson();
                            String json = gson.toJson(data);
                            editor.putString("loginData", json);
                            editor.apply();
                            editor.apply();


                        }

                    } else {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void getAllRegionData() {
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
            Call<RegionData> regionDataCall = api.getAllRegion();

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            regionDataCall.enqueue(new Callback<RegionData>() {
                @Override
                public void onResponse(Call<RegionData> call, retrofit2.Response<RegionData> response) {
                    try {
                        if (response.body() != null) {
                            RegionData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();

                                ArrayList<RegionList> regionArray = new ArrayList<>();
                                if (data.getRegionList().size() > 0) {
                                    for (int i = 0; i < data.getRegionList().size(); i++) {
                                        regionArray.add(data.getRegionList().get(i));
                                    }
                                }

                                SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                Gson gson = new Gson();
                                String jsonRegion = gson.toJson(regionArray);
                                editor.putString("RegionArray", jsonRegion);
                                editor.apply();
                                editor.apply();

                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<RegionData> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void getAllDistrictData() {
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
            Call<DistrictData> districtDataCall = api.getAllDistricts();

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            districtDataCall.enqueue(new Callback<DistrictData>() {
                @Override
                public void onResponse(Call<DistrictData> call, retrofit2.Response<DistrictData> response) {
                    try {
                        if (response.body() != null) {
                            DistrictData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();

                                ArrayList<DistrictList> distArray = new ArrayList<>();
                                if (data.getDistrictList().size() > 0) {
                                    for (int i = 0; i < data.getDistrictList().size(); i++) {
                                        distArray.add(data.getDistrictList().get(i));
                                    }
                                }

                                SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                Gson gson = new Gson();
                                String jsonRegion = gson.toJson(distArray);
                                editor.putString("DistrictArray", jsonRegion);
                                editor.apply();
                                editor.apply();

                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<DistrictData> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void getAllTalukaData() {
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
            Call<TalukaData> talukaDataCall = api.getAllTalukas();

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            talukaDataCall.enqueue(new Callback<TalukaData>() {
                @Override
                public void onResponse(Call<TalukaData> call, retrofit2.Response<TalukaData> response) {
                    try {
                        if (response.body() != null) {
                            TalukaData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();

                                ArrayList<TalukaList> talukaArray = new ArrayList<>();
                                if (data.getTalukaList().size() > 0) {
                                    for (int i = 0; i < data.getTalukaList().size(); i++) {
                                        talukaArray.add(data.getTalukaList().get(i));
                                    }
                                }

                                SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                Gson gson = new Gson();
                                String jsonRegion = gson.toJson(talukaArray);
                                editor.putString("TalukaArray", jsonRegion);
                                editor.apply();
                                editor.apply();

                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<TalukaData> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void getAllGaonData() {
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
            Call<GaonData> gaonDataCall = api.getAllGaons();

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            gaonDataCall.enqueue(new Callback<GaonData>() {
                @Override
                public void onResponse(Call<GaonData> call, retrofit2.Response<GaonData> response) {
                    try {
                        if (response.body() != null) {
                            GaonData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();

                                ArrayList<GaonList> gaonArray = new ArrayList<>();
                                if (data.getGaonList().size() > 0) {
                                    for (int i = 0; i < data.getGaonList().size(); i++) {
                                        gaonArray.add(data.getGaonList().get(i));
                                    }
                                }

                                SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                Gson gson = new Gson();
                                String jsonRegion = gson.toJson(gaonArray);
                                editor.putString("GaonArray", jsonRegion);
                                editor.apply();
                                editor.apply();

                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<GaonData> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }
    }


}
