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
import com.ats.royalagro.bean.CropData;
import com.ats.royalagro.bean.CropVarietyListData;
import com.ats.royalagro.bean.FarmerCropList;
import com.ats.royalagro.bean.FarmerCropListData;
import com.ats.royalagro.bean.FarmerPlotListData;
import com.ats.royalagro.bean.GetFarmerCropList;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CropListFragment extends Fragment {

    private FloatingActionButton fab;
    public static int fId;
    //ArrayList<FarmerCropList> cropLists = new ArrayList<>();
    CropListAdapter adapter;
    ListView lvCrop;

    ArrayList<GetFarmerCropList> cropLists = new ArrayList<>();

    ArrayList<String> cropNameArray = new ArrayList<>();
    ArrayList<Integer> cropIdArray = new ArrayList<>();

    ArrayList<String> varietyNameArray = new ArrayList<>();
    ArrayList<Integer> varietyIdArray = new ArrayList<>();

    ArrayList<String> plotNameArray = new ArrayList<>();
    ArrayList<Integer> plotIdArray = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_list, container, false);
        getActivity().setTitle("Crop Details");

        try {
            fId = getArguments().getInt("FarmerId");
        } catch (Exception e) {
        }

        lvCrop = view.findViewById(R.id.lvCropList);

        fab = view.findViewById(R.id.fabAddCrop);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment adf = new CropDetailsFragment();
                Bundle args = new Bundle();
                args.putInt("FarmerId", fId);
                args.putString("Bean", null);
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "CropMaster").commit();
            }
        });

        // getCrops();
        getAllCropVariety();
       // getPlots(fId);
        getCrops(fId);

        return view;
    }


    public void getCrops(int fId) {
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
            final Call<ArrayList<GetFarmerCropList>> cropListDataCall = api.getFarmerCropListById(fId);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            cropListDataCall.enqueue(new Callback<ArrayList<GetFarmerCropList>>() {
                @Override
                public void onResponse(Call<ArrayList<GetFarmerCropList>> call, retrofit2.Response<ArrayList<GetFarmerCropList>> response) {
                    try {
                        if (response.body() != null) {
                            cropLists.clear();

                            ArrayList<GetFarmerCropList> data = response.body();
                            cropLists = data;
                            progressDialog.dismiss();

                            adapter = new CropListAdapter(getContext(), cropLists);
                            lvCrop.setAdapter(adapter);
                            Log.e("data : ", "----" + cropLists);
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
                public void onFailure(Call<ArrayList<GetFarmerCropList>> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class CropListAdapter extends BaseAdapter {

        Context context;
        private ArrayList<GetFarmerCropList> originalValues;
        private ArrayList<GetFarmerCropList> displayedValues;
        LayoutInflater inflater;

        public CropListAdapter(Context context, ArrayList<GetFarmerCropList> eventLists) {
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
            v = inflater.inflate(R.layout.custom_crop_list_layout, null);

            TextView tvName = v.findViewById(R.id.tvCropList_Name);
            TextView tvArea = v.findViewById(R.id.tvCropList_Area);
            TextView tvPlantDate = v.findViewById(R.id.tvCropList_PlantDate);
            TextView tvHarvestFrom = v.findViewById(R.id.tvCropList_HarvestDateFrom);
            TextView tvHarvestTo = v.findViewById(R.id.tvCropList_HarvestDateTo);
            TextView tvYield = v.findViewById(R.id.tvCropList_Yield);
            TextView tvDays = v.findViewById(R.id.tvCropList_Days);
            TextView tvPlot = v.findViewById(R.id.tvCropList_Plot);
            ImageView ivPopup = v.findViewById(R.id.ivCropList_Menu);



           /* int pos = 0;
            for (int i = 0; i < cropIdArray.size(); i++) {
                if (displayedValues.get(position).getCropId() == cropIdArray.get(i)) {
                    pos = i;
                }
            }

            int varPos = 0;
            for (int i = 0; i < varietyIdArray.size(); i++) {
                if (displayedValues.get(position).getVarId() == varietyIdArray.get(i)) {
                    varPos = i;
                }
            }*/
          /*  try {
                if (varietyNameArray.size() > 0) {

                    // tvName.setText("" + cropNameArray.get(pos) + " - " + varietyNameArray.get(varPos));
                    tvName.setText("" + cropNameArray.get(pos));
                } else {
                    tvName.setText("" + cropNameArray.get(pos));
                }
            } catch (Exception e) {
            }*/

            int varPos = 0;
            for (int i = 0; i < varietyIdArray.size(); i++) {
                if (displayedValues.get(position).getVarId() == varietyIdArray.get(i)) {
                    varPos = i;
                }
            }

            int plotPos = 0;
            for (int i = 0; i < plotIdArray.size(); i++) {
                Log.e("PLOT ID : ", "--------------" + displayedValues.get(position).getfDid());
                if (displayedValues.get(position).getfDid() == plotIdArray.get(i)) {
                    Log.e("i", " : --------------" + plotIdArray.get(i));
                    plotPos = i;
                }
            }

            try {
                tvName.setText("Crop : " + displayedValues.get(position).getCropName() + " - " + varietyNameArray.get(varPos));

                Log.e("PLot Pos", " : ----------" + plotPos);
                tvPlot.setText("Plot : " + plotNameArray.get(plotPos));

                tvArea.setText("Area : " + displayedValues.get(position).getfCropArea());
                tvYield.setText("Yield Per Acre : " + displayedValues.get(position).getfYieldPerAcre());

            } catch (Exception e) {
            }

            if (displayedValues.get(position).getfDtPlantation() == null) {
                tvPlantDate.setText("Date Plantation : ");
            } else {
                tvPlantDate.setText("Date Plantation : " + displayedValues.get(position).getfDtPlantation());
            }

            if (displayedValues.get(position).getfHarFrdate() == null) {
                tvHarvestFrom.setText("Harvest From : ");
            } else {
                tvHarvestFrom.setText("Harvest From : " + displayedValues.get(position).getfHarFrdate());
            }

            if (displayedValues.get(position).getfHarTodate() == null) {
                tvHarvestTo.setText("Harvest To : ");
            } else {
                tvHarvestTo.setText("Harvest To : " + displayedValues.get(position).getfHarTodate());
            }


            SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

            try {
                tvDays.setVisibility(View.VISIBLE);
                Date date1 = myFormat.parse(displayedValues.get(position).getfDtPlantation());
                Date date2 = myFormat.parse(displayedValues.get(position).getfHarFrdate());
                long diff = date2.getTime() - date1.getTime();
                System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                tvDays.setText("No Of Days : " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

            } catch (ParseException e) {
                tvDays.setVisibility(View.GONE);
                e.printStackTrace();
            } catch (Exception e) {
                tvDays.setVisibility(View.GONE);
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

                                FarmerCropList farmerCropList = new FarmerCropList(displayedValues.get(position).getfCid(), displayedValues.get(position).getfDid(), displayedValues.get(position).getfId(), displayedValues.get(position).getCropId(), displayedValues.get(position).getVarId(), displayedValues.get(position).getfCropArea(), displayedValues.get(position).getfDtPlantation(), displayedValues.get(position).getfHarFrdate(), displayedValues.get(position).getfHarTodate(), displayedValues.get(position).getfYieldPerAcre(), displayedValues.get(position).getfStatus());
                                Gson gson = new Gson();
                                String json = gson.toJson(farmerCropList);

                                Fragment adf = new CropDetailsFragment();
                                Bundle args = new Bundle();
                                args.putInt("FarmerId", fId);
                                args.putString("Bean", json);
                                adf.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "CropMaster").commit();

                            } else if (menuItem.getItemId() == R.id.society_delete) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
                                builder.setTitle("Confirm Action");
                                builder.setMessage("Do You Really Want To Delete?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        FarmerCropList farmerCropList = new FarmerCropList(displayedValues.get(position).getfCid(), displayedValues.get(position).getfDid(), displayedValues.get(position).getfId(), displayedValues.get(position).getCropId(), displayedValues.get(position).getVarId(), displayedValues.get(position).getfCropArea(), displayedValues.get(position).getfDtPlantation(), displayedValues.get(position).getfHarFrdate(), displayedValues.get(position).getfHarTodate(), displayedValues.get(position).getfYieldPerAcre(), 0);
                                        addCrop(farmerCropList);
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

    public void getCrops() {
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
            final Call<CropData> cropDataCall = api.getCrops();

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            cropDataCall.enqueue(new Callback<CropData>() {
                @Override
                public void onResponse(Call<CropData> call, retrofit2.Response<CropData> response) {
                    try {
                        if (response.body() != null) {
                            CropData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();

                                cropNameArray.clear();
                                cropIdArray.clear();
                                for (int i = 0; i < data.getCropList().size(); i++) {
                                    cropNameArray.add(data.getCropList().get(i).getCropName());
                                    cropIdArray.add(data.getCropList().get(i).getCropId());
                                }
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
                public void onFailure(Call<CropData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllCropVariety() {
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
            final Call<CropVarietyListData> cropVarietyListDataCall = api.getAllCropsVariety();

//            final AlertDialog progressDialog = new SpotsDialog(getContext());
//            progressDialog.show();

            cropVarietyListDataCall.enqueue(new Callback<CropVarietyListData>() {
                @Override
                public void onResponse(Call<CropVarietyListData> call, retrofit2.Response<CropVarietyListData> response) {
                    try {
                        if (response.body() != null) {
                            CropVarietyListData data = response.body();
                            if (data.getInfo().getError()) {
                                //  progressDialog.dismiss();
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                //  progressDialog.dismiss();

                                varietyNameArray.clear();
                                varietyIdArray.clear();
                                for (int i = 0; i < data.getCropVarietyList().size(); i++) {
                                    varietyNameArray.add(data.getCropVarietyList().get(i).getVarName());
                                    varietyIdArray.add(data.getCropVarietyList().get(i).getVarId());
                                }


                            }
                        } else {
                            // progressDialog.dismiss();
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        //   progressDialog.dismiss();
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<CropVarietyListData> call, Throwable t) {
                    // progressDialog.dismiss();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
        }
    }


    public void addCrop(FarmerCropList farmerCropList) {
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
            Call<FarmerCropList> farmerCropListCall = api.addFarmerCrop(farmerCropList);

            final android.app.AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            farmerCropListCall.enqueue(new Callback<FarmerCropList>() {
                @Override
                public void onResponse(Call<FarmerCropList> call, retrofit2.Response<FarmerCropList> response) {
                    try {
                        if (response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                            lvCrop.setAdapter(null);
                            getCrops(fId);

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
                public void onFailure(Call<FarmerCropList> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }

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


            plotListDataCall.enqueue(new Callback<FarmerPlotListData>() {
                @Override
                public void onResponse(Call<FarmerPlotListData> call, retrofit2.Response<FarmerPlotListData> response) {
                    try {
                        if (response.body() != null) {
                            FarmerPlotListData data = response.body();
                            if (data.getInfo().getError()) {
                                Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                plotIdArray.clear();
                                plotNameArray.clear();
                                for (int i = 0; i < data.getFarmerPlotList().size(); i++) {
                                    plotNameArray.add(data.getFarmerPlotList().get(i).getfPlotNo());
                                    plotIdArray.add(data.getFarmerPlotList().get(i).getfPlotId());
                                }

                                Log.e("PLOTS : ", "----" + data.getFarmerPlotList());

                            }
                        } else {
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<FarmerPlotListData> call, Throwable t) {
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getAllCropVariety();
      //  getPlots(fId);
    }
}
