package com.ats.royalagro.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.FarmerPlotList;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.PermissionUtil;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlotDetailsFragment extends Fragment implements View.OnClickListener {

    private EditText edGatNo, edPlotNo, edPlotArea, edPlotRemark, edOwner, edMobile, edAadhar, edPan, edWaterRes, edWaterRemark, edRelation;
    private Button btnSave, btnReset;
    private Spinner spRelation;
    int fId, plotId = 0;

    private static Pattern aadhaarPattern = Pattern.compile("^[2-9]{1}[0-9]{11}$");
    private static Pattern patternPAN = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plot_details, container, false);
        getActivity().setTitle("Add New Plot");

        if (PermissionUtil.checkAndRequestPermissions(getActivity())) {

        }

        fId = getArguments().getInt("FarmerId");

        Gson gson = new Gson();
        String bean = getArguments().getString("Bean");
        FarmerPlotList plotBean = gson.fromJson(bean, FarmerPlotList.class);

        edGatNo = view.findViewById(R.id.edPlot_GatNo);
        edPlotNo = view.findViewById(R.id.edPlot_PlotNo);
        edPlotArea = view.findViewById(R.id.edPlot_PlotArea);
        edOwner = view.findViewById(R.id.edPlot_OwnerName);
        edMobile = view.findViewById(R.id.edPlot_Mobile);
        edAadhar = view.findViewById(R.id.edPlot_Aadhar);
        edPan = view.findViewById(R.id.edPlot_PAN);
        edWaterRes = view.findViewById(R.id.edPlot_WaterRes);
        edWaterRemark = view.findViewById(R.id.edPlot_WaterResRemark);
        edRelation = view.findViewById(R.id.edPlot_Relation);

        btnSave = view.findViewById(R.id.btnPlot_Save);
        btnReset = view.findViewById(R.id.btnPlot_Reset);

        spRelation = view.findViewById(R.id.spPlot_Relation);

        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);


      /*  ArrayList<String> relArray = new ArrayList<>();
        relArray.add("Select Relation");
        relArray.add("Grand Father");
        relArray.add("Grand Mother");
        relArray.add("Father");
        relArray.add("Mother");
        relArray.add("Brother");
        relArray.add("Sister");
        relArray.add("Son");
        relArray.add("Daughter");
        relArray.add("Other");

        ArrayAdapter<String> spAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, relArray);
        spRelation.setAdapter(spAdpt);*/

        if (plotBean != null) {
            plotId = plotBean.getfPlotId();
            edGatNo.setText("" + plotBean.getfGatNo());
            edWaterRemark.setText("" + plotBean.getfWaterResourceRemark());
            edWaterRes.setText("" + plotBean.getWaterResource());
            edPan.setText("" + plotBean.getPanNo());
            edAadhar.setText("" + plotBean.getAadharNo());
            edMobile.setText("" + plotBean.getMobileNo());
            edRelation.setText("" + plotBean.getRelation());
            edPlotArea.setText("" + plotBean.getfPlotArea());
            edPlotNo.setText("" + plotBean.getfPlotNo());
            edOwner.setText("" + plotBean.getPlotOwnerName());
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPlot_Save) {
            if (edGatNo.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Gat Number", Toast.LENGTH_SHORT).show();
                edGatNo.requestFocus();
            } else if (edPlotNo.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Plot Number", Toast.LENGTH_SHORT).show();
                edPlotNo.requestFocus();
            } else if (edPlotArea.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Plot Area", Toast.LENGTH_SHORT).show();
                edPlotArea.requestFocus();
            } else if (edOwner.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Plot Owner Name", Toast.LENGTH_SHORT).show();
                edOwner.requestFocus();
            }
            /*else if (edRelation.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Relation With Plot Owner", Toast.LENGTH_SHORT).show();
                edRelation.requestFocus();
            }*/
            /*else if (edMobile.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            }
            else if (edMobile.getText().toString().length() != 10) {
                Toast.makeText(getActivity(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            }*/
            else if (edAadhar.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Aadhar Number", Toast.LENGTH_SHORT).show();
                edAadhar.requestFocus();
            } else if (!isValidAadhar(edAadhar.getText().toString())) {
                Toast.makeText(getActivity(), "Please Enter Valid Aadhar Number", Toast.LENGTH_SHORT).show();
                edAadhar.requestFocus();
            }
            /*else if (edPan.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter PAN Number", Toast.LENGTH_SHORT).show();
                edPan.requestFocus();
            } else if (!isValidPAN(edPan.getText().toString())) {
                Toast.makeText(getActivity(), "Please Enter Valid PAN Number", Toast.LENGTH_SHORT).show();
                edPan.requestFocus();
            }*/
            /*else if (edWaterRes.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Water Resources", Toast.LENGTH_SHORT).show();
                edWaterRes.requestFocus();
            }*/

            else {
                String gatNo = edGatNo.getText().toString();
                String plotNo = edPlotNo.getText().toString();
                String area = edPlotArea.getText().toString();
                String ownerName = edOwner.getText().toString();
                String relation = edRelation.getText().toString();
                String mobile = edMobile.getText().toString();
                String aadhar = edAadhar.getText().toString();
                String pan = edPan.getText().toString();
                String waterRes = edWaterRes.getText().toString();
                String waterRemark = edWaterRemark.getText().toString();


                FarmerPlotList farmerPlotList = new FarmerPlotList(plotId, fId, gatNo, plotNo, ownerName, mobile, aadhar, pan, relation, area, waterRemark, waterRes, 1);
                addPlot(farmerPlotList);
            }


        } else if (v.getId() == R.id.btnPlot_Reset) {
            edGatNo.setText("");
            edPlotNo.setText("");
            edPlotArea.setText("");
            edPlotRemark.setText("");
            edOwner.setText("");
            edRelation.setText("");
            edMobile.setText("");
            edAadhar.setText("");
            edPan.setText("");
            edWaterRes.setText("");
            edWaterRemark.setText("");
            edGatNo.requestFocus();
        }
    }

    public static boolean isValidAadhar(String name) {
        Matcher matcher = aadhaarPattern.matcher(name);
        return matcher.find();
    }

    public static boolean isValidPAN(String name) {
        Matcher matcher = patternPAN.matcher(name);
        return matcher.find();
    }


    public void addPlot(FarmerPlotList farmerPlotList) {
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
            progressDialog.setCancelable(false);
            progressDialog.show();

            farmerPlotListCall.enqueue(new Callback<FarmerPlotList>() {
                @Override
                public void onResponse(Call<FarmerPlotList> call, retrofit2.Response<FarmerPlotList> response) {
                    try {
                        if (response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            Fragment adf = new PlotListFragment();
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
                public void onFailure(Call<FarmerPlotList> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }

    }

}
