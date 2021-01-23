package com.ats.royalagro.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.CropData;
import com.ats.royalagro.bean.CropVarietyData;
import com.ats.royalagro.bean.FarmerCropList;
import com.ats.royalagro.bean.FarmerPlotListData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CropDetailsFragment extends Fragment implements View.OnClickListener {

    private EditText edCropArea, edDatePlant, edDateFrom, edDateTo, edYield, edDays;
    private TextView tvPlotId, tvCropId, tvVarietyId;
    private RadioButton rbFree, rbConsumed;
    private Button btnSave, btnReset;
    private Spinner spPlot, spCrop, spVariety;
    Boolean isCropTouch = false;

    ArrayList<String> plotNameArray = new ArrayList<>();
    ArrayList<Integer> plotIdArray = new ArrayList<>();

    ArrayList<String> cropNameArray = new ArrayList<>();
    ArrayList<Integer> cropIdArray = new ArrayList<>();

    ArrayList<String> varietyNameArray = new ArrayList<>();
    ArrayList<Integer> varietyIdArray = new ArrayList<>();


    int yyyy, mm, dd, yr, mn, dy;
    long datePlantMillis, fromDateMillis, toDateMillis;

    int fId, f_crop_id = 0, plotId = 0, cpId = 0, cpVarId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crop_details, container, false);
        getActivity().setTitle("Add Crop");

        fId = getArguments().getInt("FarmerId");

        Gson gson = new Gson();
        String bean = getArguments().getString("Bean");
        FarmerCropList cropBean = gson.fromJson(bean, FarmerCropList.class);


        edCropArea = view.findViewById(R.id.edCrop_Area);
        edDatePlant = view.findViewById(R.id.edCrop_DatePlant);
        edDateFrom = view.findViewById(R.id.edCrop_HarvFromDate);
        edDateTo = view.findViewById(R.id.edCrop_HarvToDate);
        edYield = view.findViewById(R.id.edCrop_YieldPerAcre);
        edDays = view.findViewById(R.id.edCrop_Days);

        tvCropId = view.findViewById(R.id.tvCrop_CropId);
        tvVarietyId = view.findViewById(R.id.tvCrop_VarietyId);
        tvPlotId = view.findViewById(R.id.tvCrop_PlotId);

        spPlot = view.findViewById(R.id.spCrop_Plot);
        spCrop = view.findViewById(R.id.spCrop_CropName);
        spVariety = view.findViewById(R.id.spCrop_Variety);

        btnSave = view.findViewById(R.id.btnCrop_Save);
        btnReset = view.findViewById(R.id.btnCrop_Reset);

        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        edDateFrom.setOnClickListener(this);
        edDateTo.setOnClickListener(this);
        edDatePlant.setOnClickListener(this);

        final Calendar cal = Calendar.getInstance();
        datePlantMillis = cal.getTimeInMillis();
        fromDateMillis = cal.getTimeInMillis();
        toDateMillis = cal.getTimeInMillis();


        spPlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvPlotId.setText("" + plotIdArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCrop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isCropTouch = true;
                }
                return false;
            }
        });

        spCrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvCropId.setText("" + cropIdArray.get(position));
                if (f_crop_id <= 0) {
                    if (isCropTouch) {
                        getCropVariety(cropIdArray.get(position));
                    }
                } else {
                    getCropVariety(cropIdArray.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spVariety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvVarietyId.setText("" + varietyIdArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getPlots(fId);
        getCrops();


        if (cropBean != null) {
            f_crop_id = cropBean.getfCid();
            edCropArea.setText("" + cropBean.getfCropArea());
            if (cropBean.getfDtPlantation() == null) {
                edDatePlant.setText("");
            } else {
                edDatePlant.setText("" + cropBean.getfDtPlantation());
            }
            edDateFrom.setText("" + cropBean.getfHarFrdate());
            edDateTo.setText("" + cropBean.getfHarTodate());
            edYield.setText("" + cropBean.getfYieldPerAcre());
            cpId = cropBean.getCropId();
            cpVarId = cropBean.getVarId();
            plotId = cropBean.getfDid();

            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date d = f.parse(cropBean.getfHarFrdate());
                fromDateMillis = d.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                Date d = f.parse(cropBean.getfHarTodate());
                toDateMillis = d.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        edDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(datePlantMillis);
                    calendar.add(Calendar.DATE, Integer.parseInt(charSequence.toString()));

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    edDateFrom.setText("" + sdf.format(calendar.getTimeInMillis()));
                    fromDateMillis = calendar.getTimeInMillis();

                    calendar.add(Calendar.DATE, 5);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    edDateTo.setText("" + sdf1.format(calendar.getTimeInMillis()));
                    toDateMillis = calendar.getTimeInMillis();

                } catch (Exception e) {
                    edDateFrom.setText("");
                    edDateTo.setText("");
                    edDateTo.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCrop_Save) {

            if (spPlot.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select Plot", Toast.LENGTH_SHORT).show();
                spPlot.requestFocus();
            } else if (spCrop.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select Crop", Toast.LENGTH_SHORT).show();
                spCrop.requestFocus();
            } else if (edCropArea.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Crop Area", Toast.LENGTH_SHORT).show();
                edCropArea.requestFocus();
            }
            /*else if (edDatePlant.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Date Of Plantation", Toast.LENGTH_SHORT).show();
                edDatePlant.requestFocus();
            }*/
            else if (edDateFrom.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Harvest From Date", Toast.LENGTH_SHORT).show();
                edDateFrom.requestFocus();
            } else if (edDateTo.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Harvest To Date", Toast.LENGTH_SHORT).show();
                edDateTo.requestFocus();
            } else if (fromDateMillis > toDateMillis) {
                Toast.makeText(getActivity(), "Harvest To Date Should Be Greater Than Harvest From Date", Toast.LENGTH_SHORT).show();
                edDateTo.requestFocus();
            }
            /*else if (edYield.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Yield Per Acre", Toast.LENGTH_SHORT).show();
                edYield.requestFocus();
            }*/
            else {

                int plotId = 0, cropId = 0, varId = 0;
                try {
                    if (!tvPlotId.getText().toString().isEmpty()) {
                        plotId = Integer.parseInt(tvPlotId.getText().toString());
                    }

                    if (!tvCropId.getText().toString().isEmpty()) {
                        cropId = Integer.parseInt(tvCropId.getText().toString());
                    }

                    if (!tvVarietyId.getText().toString().isEmpty()) {
                        varId = Integer.parseInt(tvVarietyId.getText().toString());
                    }

                } catch (Exception e) {
                }

                //int area = Integer.parseInt(edCropArea.getText().toString());
                String area = edCropArea.getText().toString();

                String plantDate = edDatePlant.getText().toString();
                String fromDate = edDateFrom.getText().toString();
                String toDate = edDateTo.getText().toString();
                //   int yield = Integer.parseInt(edYield.getText().toString());
                String yield = edYield.getText().toString();

                FarmerCropList farmerCropList = new FarmerCropList(f_crop_id, plotId, fId, cropId, varId, area, plantDate, fromDate, toDate, yield, 1);
                addCrop(farmerCropList);

            }


        } else if (v.getId() == R.id.btnCrop_Reset) {
            edCropArea.setText("");
            edDatePlant.setText("");
            edDateFrom.setText("");
            edDateTo.setText("");
            edYield.setText("");
        } else if (v.getId() == R.id.edCrop_DatePlant) {
            edDays.setText("0");
            Calendar purchaseCal = Calendar.getInstance();
            purchaseCal.setTimeInMillis(datePlantMillis);
            int yr = purchaseCal.get(Calendar.YEAR);
            int mn = purchaseCal.get(Calendar.MONTH);
            int dy = purchaseCal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePlantListener, yr, mn, dy);
            dialog.show();
        } else if (v.getId() == R.id.edCrop_HarvFromDate) {
            Calendar purchaseCal = Calendar.getInstance();
            purchaseCal.setTimeInMillis(fromDateMillis);
            int yr = purchaseCal.get(Calendar.YEAR);
            int mn = purchaseCal.get(Calendar.MONTH);
            int dy = purchaseCal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateFromListener, yr, mn, dy);
            dialog.show();
        } else if (v.getId() == R.id.edCrop_HarvToDate) {
            Calendar purchaseCal = Calendar.getInstance();
            purchaseCal.setTimeInMillis(toDateMillis);
            int yr = purchaseCal.get(Calendar.YEAR);
            int mn = purchaseCal.get(Calendar.MONTH);
            int dy = purchaseCal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateToListener, yr, mn, dy);
            dialog.show();
        }
    }

    private DatePickerDialog.OnDateSetListener datePlantListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDatePlant.setText(dd + "-" + mm + "-" + yyyy);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            datePlantMillis = calendar.getTimeInMillis();
        }
    };

    private DatePickerDialog.OnDateSetListener dateFromListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDateFrom.setText(dd + "-" + mm + "-" + yyyy);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            fromDateMillis = calendar.getTimeInMillis();
        }
    };

    private DatePickerDialog.OnDateSetListener dateToListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDateTo.setText(dd + "-" + mm + "-" + yyyy);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            toDateMillis = calendar.getTimeInMillis();
        }
    };


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
            progressDialog.setCancelable(false);
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

                                plotNameArray.clear();
                                plotIdArray.clear();
                                plotNameArray.add("Select Plot");
                                plotIdArray.add(0);
                                for (int i = 0; i < data.getFarmerPlotList().size(); i++) {
                                    plotNameArray.add(data.getFarmerPlotList().get(i).getfPlotNo());
                                    plotIdArray.add(data.getFarmerPlotList().get(i).getfPlotId());
                                }

                                ArrayAdapter<String> plotAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, plotNameArray);
                                spPlot.setAdapter(plotAdapter);

                                int pos = 0;
                                for (int i = 0; i < plotIdArray.size(); i++) {
                                    if (plotId == plotIdArray.get(i)) {
                                        pos = i;
                                    }
                                }
                                spPlot.setSelection(pos);

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
                                Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();
                                Log.e("CROP : ", "----" + data.getCropList());

                                cropNameArray.clear();
                                cropIdArray.clear();
                                cropNameArray.add("Select Crop");
                                cropIdArray.add(0);
                                for (int i = 0; i < data.getCropList().size(); i++) {
                                    cropNameArray.add(data.getCropList().get(i).getCropName());
                                    cropIdArray.add(data.getCropList().get(i).getCropId());
                                }

                                ArrayAdapter<String> cropAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, cropNameArray);
                                spCrop.setAdapter(cropAdapter);

                                int pos = 0;
                                for (int i = 0; i < cropIdArray.size(); i++) {
                                    if (cpId == cropIdArray.get(i)) {
                                        pos = i;
                                    }
                                }
                                spCrop.setSelection(pos);

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

    public void getCropVariety(int crop_id) {
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
            final Call<CropVarietyData> cropVarietyDataCall = api.getCropsVariety(crop_id);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            cropVarietyDataCall.enqueue(new Callback<CropVarietyData>() {
                @Override
                public void onResponse(Call<CropVarietyData> call, retrofit2.Response<CropVarietyData> response) {
                    try {
                        if (response.body() != null) {
                            CropVarietyData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                Log.e("onError : ", "----" + data.getInfo().getMessage());
                                varietyNameArray.clear();
                                varietyIdArray.clear();
                                varietyNameArray.add("Select Crop Variety");
                                varietyIdArray.add(0);
                                ArrayAdapter<String> varietyAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, varietyNameArray);
                                spVariety.setAdapter(varietyAdapter);
                            } else {
                                progressDialog.dismiss();

                                varietyNameArray.clear();
                                varietyIdArray.clear();
                                varietyNameArray.add("Select Crop Variety");
                                varietyIdArray.add(0);
                                for (int i = 0; i < data.getCropVarietyListById().size(); i++) {
                                    varietyNameArray.add(data.getCropVarietyListById().get(i).getVarName());
                                    varietyIdArray.add(data.getCropVarietyListById().get(i).getVarId());
                                }

                                ArrayAdapter<String> varietyAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, varietyNameArray);
                                spVariety.setAdapter(varietyAdapter);

                                int pos = 0;
                                for (int i = 0; i < varietyIdArray.size(); i++) {
                                    if (cpVarId == varietyIdArray.get(i)) {
                                        pos = i;
                                    }
                                }
                                spVariety.setSelection(pos);


                            }
                        } else {
                            progressDialog.dismiss();
                            varietyNameArray.clear();
                            varietyIdArray.clear();
                            varietyNameArray.add("Select Crop Variety");
                            varietyIdArray.add(0);
                            ArrayAdapter<String> varietyAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, varietyNameArray);
                            spVariety.setAdapter(varietyAdapter);
                            Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<CropVarietyData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
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
            progressDialog.setCancelable(false);
            progressDialog.show();

            farmerCropListCall.enqueue(new Callback<FarmerCropList>() {
                @Override
                public void onResponse(Call<FarmerCropList> call, retrofit2.Response<FarmerCropList> response) {
                    try {
                        if (response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            Fragment adf = new CropListFragment();
                            Bundle args = new Bundle();
                            args.putInt("FarmerId", fId);
                            adf.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();
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
                public void onFailure(Call<FarmerCropList> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }

    }

}
