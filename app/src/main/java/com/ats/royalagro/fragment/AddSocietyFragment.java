package com.ats.royalagro.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.activity.MapsActivity;
import com.ats.royalagro.bean.GaonData;
import com.ats.royalagro.bean.GaonList;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.bean.SocietyData;
import com.ats.royalagro.bean.SocietyList;
import com.ats.royalagro.bean.SocietyListData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

import static android.content.Context.MODE_PRIVATE;
import static com.ats.royalagro.activity.MapsActivity.myLatLong;

public class AddSocietyFragment extends Fragment implements View.OnClickListener {

    private EditText edRegNo, edReprName, edReprDesg, edMobile1, edMobile2, edLandline, edEmail, edApproxFarmers, edSqFt, edSocName, edVillage;
    private Spinner spSocType, spVillage;
    private Button btnSave, btnNext, btnReset;
    private ImageView ivLoc;
    private RadioButton rbYes, rbNo;
    private TextView tvVillageId, tvLoc;
    private RadioGroup rg;

    int villageId;

    private ArrayList<String> gaonNameArray = new ArrayList<>();
    private ArrayList<Integer> gaonIdArray = new ArrayList<>();

    private ArrayList<SocietyList> societyArrayList = new ArrayList<>();
    private ArrayList<GaonList> gaonArray = new ArrayList<>();

    int empId, eventId, socId, empType;
    String eventName;

    Dialog dialog;
    GaonDataAdapter gaonAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_society, container, false);
        getActivity().setTitle("Add Society");

        eventId = getArguments().getInt("EventId");
        eventName = getArguments().getString("EventName");
        socId = getArguments().getInt("SocietyId");

        edRegNo = view.findViewById(R.id.edAddSociety_RegNo);
        edReprName = view.findViewById(R.id.edAddSociety_Representative);
        edReprDesg = view.findViewById(R.id.edAddSociety_ReprDesg);
        edMobile1 = view.findViewById(R.id.edAddSociety_ReprMobile1);
        edMobile2 = view.findViewById(R.id.edAddSociety_Repr_Mobile2);
        edLandline = view.findViewById(R.id.edAddSociety_Landline);
        edEmail = view.findViewById(R.id.edAddSociety_Email);
        edApproxFarmers = view.findViewById(R.id.edAddSociety_AppxFarmers);
        edSqFt = view.findViewById(R.id.edAddSociety_Sqft);
        edSocName = view.findViewById(R.id.edAddSociety_SocName);
        edVillage = view.findViewById(R.id.edAddSociety_Village);

        rg = view.findViewById(R.id.rgAddSociety);

        tvVillageId = view.findViewById(R.id.tvAddSociety_HideVillageId);
        tvLoc = view.findViewById(R.id.tvAddSociety_Loc);

        rbYes = view.findViewById(R.id.rbY);
        rbNo = view.findViewById(R.id.rbN);

        ivLoc = view.findViewById(R.id.ivSocLocation);
        ivLoc.setOnClickListener(this);

        spSocType = view.findViewById(R.id.spAddSociety_SocType);
        spVillage = view.findViewById(R.id.spAddSociety_Village);

        btnSave = view.findViewById(R.id.btnAddSociety_Save);
        btnNext = view.findViewById(R.id.btnAddSociety_Next);
        btnReset = view.findViewById(R.id.btnAddSociety_Reset);

        btnSave.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        edVillage.setOnClickListener(this);

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
            empType = loginBean.getEmpType();
        }

        if (empType == 0) {
            String jsonGaon = pref.getString("GaonArray", "");
            Type type1 = new TypeToken<ArrayList<GaonList>>() {
            }.getType();
            gaonArray = gson.fromJson(jsonGaon, type1);
            Log.e("Gaon : ", "---------------" + gaonArray);
        } else {
            String jsonGaon = pref.getString("Gaon", "");
            Type type1 = new TypeToken<ArrayList<GaonList>>() {
            }.getType();
            gaonArray = gson.fromJson(jsonGaon, type1);
            Log.e("Gaon : ", "---------------" + gaonArray);
        }

        ArrayList<String> socTypeArray = new ArrayList<>();
        socTypeArray.add("Select Society");
        socTypeArray.add("Society");
        socTypeArray.add("FPO");
        socTypeArray.add("FPC");

        ArrayAdapter<String> socTypeAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, socTypeArray);
        spSocType.setAdapter(socTypeAdpt);


        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvVillageId.setText("" + gaonIdArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rbNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edSqFt.setText("0");
                } else {
                    edSqFt.setText("");
                }
            }
        });

        if (socId > 0) {
            getSocietyData(socId);
        }
        /*else {
            getAllGaonData();
        }*/

        setHasOptionsMenu(false);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAddSociety_Save) {

            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            Boolean isValidate = false;

            if (spSocType.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select Society Type", Toast.LENGTH_SHORT).show();
                spSocType.requestFocus();
            } else if (edRegNo.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Registeration Number", Toast.LENGTH_SHORT).show();
                edRegNo.requestFocus();
            }
            /*else if (spVillage.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please select Village", Toast.LENGTH_SHORT).show();
                spVillage.requestFocus();
            }*/
            else if (edVillage.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please select Village", Toast.LENGTH_SHORT).show();
                edVillage.requestFocus();
            } else if (edSocName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Society Name", Toast.LENGTH_SHORT).show();
                edSocName.requestFocus();
            } else if (edReprName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Representative Name", Toast.LENGTH_SHORT).show();
                edReprName.requestFocus();
            } else if (edReprDesg.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Representative Designation", Toast.LENGTH_SHORT).show();
                edReprDesg.requestFocus();
            } else if (edMobile1.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Representative Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile1.requestFocus();
            } else if (edMobile1.getText().toString().length() != 10) {
                Toast.makeText(getActivity(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile1.requestFocus();
            }
          /*  else if (edMobile2.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Representative Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile2.requestFocus();
            } else if (edMobile2.getText().toString().length() != 10) {
                Toast.makeText(getActivity(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile2.requestFocus();
            }
            else if (edLandline.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Representative Landline Number", Toast.LENGTH_SHORT).show();
                edLandline.requestFocus();
            }*/
            /*else if (edEmail.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Representative Email Id", Toast.LENGTH_SHORT).show();
                edEmail.requestFocus();
            }
            else if (!edEmail.getText().toString().matches(emailPattern)) {
                edEmail.setError("Please Enter Valid Email Address");
                edEmail.requestFocus();
            }*/

            else if (edApproxFarmers.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Approximate Farmers Count", Toast.LENGTH_SHORT).show();
                edApproxFarmers.requestFocus();
            } else if (tvLoc.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Location", Toast.LENGTH_SHORT).show();
                tvLoc.requestFocus();
            } else if (!rbYes.isChecked() && !rbNo.isChecked()) {
                Toast.makeText(getActivity(), "Please Select Warehouse Available", Toast.LENGTH_SHORT).show();
                rbYes.requestFocus();
            } else {

                int socType = spSocType.getSelectedItemPosition() - 1;
                String regNo = edRegNo.getText().toString();
                String socName = edSocName.getText().toString();
                String repName = edReprName.getText().toString();
                String repDesg = edReprDesg.getText().toString();
                String mob1 = edMobile1.getText().toString();
                String mob2 = edMobile2.getText().toString();
                String landline = edLandline.getText().toString();
                String email = edEmail.getText().toString();
                String location = tvLoc.getText().toString();

//                try {
//                    location = getArguments().getString("Location");
//                } catch (Exception e) {
//
//                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currDateTime = sdf.format(new Date());


                int appxFarmer = 0, villageId = 0;
                try {
                    appxFarmer = Integer.parseInt(edApproxFarmers.getText().toString());
                    villageId = Integer.parseInt(tvVillageId.getText().toString());
                } catch (Exception e) {
                }


                int storage;
                if (rbYes.isChecked()) {
                    storage = 1;
                    if (edSqFt.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), "Please Enter Warehouse Square Feet Area", Toast.LENGTH_SHORT).show();
                        edSqFt.requestFocus();
                    } else {
                        //Log.e("rbYes----", "----------");
                        int sqFt = 0;
                        try {
                            sqFt = Integer.parseInt(edSqFt.getText().toString());
                        } catch (Exception e) {
                        }

                        SocietyData societyData = new SocietyData(socId, socType, regNo, socName, eventId, villageId, repName, repDesg, mob1, mob2, email, landline, appxFarmer, 0, 0, storage, sqFt, empId, currDateTime, location, empId, currDateTime, 0, currDateTime, 1);
                        addNewSociety(societyData);
                    }
                } else {
                    storage = 0;
                    //Log.e("ELSE----", "----------");
                    SocietyData societyData = new SocietyData(socId, socType, regNo, socName, eventId, villageId, repName, repDesg, mob1, mob2, email, landline, appxFarmer, 0, 0, storage, 0, empId, currDateTime, location, empId, currDateTime, 0, currDateTime, 1);
                    addNewSociety(societyData);
                }

            }


        } else if (v.getId() == R.id.btnAddSociety_Next) {

        } else if (v.getId() == R.id.btnAddSociety_Reset) {

            spSocType.setSelection(0);
            edRegNo.setText("");
            spVillage.setSelection(0);
            tvVillageId.setText("");
            edSocName.setText("");
            edReprName.setText("");
            edReprDesg.setText("");
            edMobile1.setText("");
            edMobile2.setText("");
            edLandline.setText("");
            edEmail.setText("");
            edApproxFarmers.setText("");
            tvLoc.setText("");
            edSqFt.setText("");
            myLatLong = "";
            rg.clearCheck();
            edRegNo.requestFocus();
            edVillage.setText("");

        } else if (v.getId() == R.id.ivSocLocation) {
            startActivity(new Intent(getContext(), MapsActivity.class));
        } else if (v.getId() == R.id.edAddSociety_Village) {
            if (gaonArray!=null) {
                showGaonDialog();
            } else {
                Toast.makeText(getActivity(), "Please Sync Data", Toast.LENGTH_SHORT).show();
            }
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
            Call<GaonData> gaonDataCall = api.getAllGaon();

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
                                //   Toast.makeText(getActivity(), "No Village Found", Toast.LENGTH_SHORT).show();
                                gaonNameArray.clear();
                                gaonIdArray.clear();
                                gaonNameArray.add("Select Village");
                                gaonIdArray.add(0);
                                ArrayAdapter<String> gaonAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, gaonNameArray);
                                spVillage.setAdapter(gaonAdpt);
                            } else {
                                progressDialog.dismiss();

                                gaonNameArray.clear();
                                gaonIdArray.clear();
                                gaonNameArray.add("Select Village");
                                gaonIdArray.add(0);
                                for (int i = 0; i < data.getGaonList().size(); i++) {
                                    gaonNameArray.add(data.getGaonList().get(i).getGaonName());
                                    gaonIdArray.add(data.getGaonList().get(i).getGaonId());
                                }
                                ArrayAdapter<String> gaonAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, gaonNameArray);
                                spVillage.setAdapter(gaonAdpt);

                                int pos = 0;
                                for (int i = 0; i < gaonIdArray.size(); i++) {
                                    if (villageId == gaonIdArray.get(i)) {
                                        pos = i;
                                    }
                                }
                                //Log.e("Position : ","-----"+pos);
                                spVillage.setSelection(pos);

                            }
                        } else {
                            progressDialog.dismiss();
                            //  Toast.makeText(getActivity(), "No Village Found", Toast.LENGTH_SHORT).show();
                            gaonNameArray.clear();
                            gaonIdArray.clear();
                            gaonNameArray.add("Select Village");
                            gaonIdArray.add(0);
                            ArrayAdapter<String> gaonAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, gaonNameArray);
                            spVillage.setAdapter(gaonAdpt);
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //  Toast.makeText(getActivity(), "No Village Found", Toast.LENGTH_SHORT).show();
                        gaonNameArray.clear();
                        gaonIdArray.clear();
                        gaonNameArray.add("Select Village");
                        gaonIdArray.add(0);
                        ArrayAdapter<String> gaonAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, gaonNameArray);
                        spVillage.setAdapter(gaonAdpt);
                    }
                }

                @Override
                public void onFailure(Call<GaonData> call, Throwable t) {
                    progressDialog.dismiss();
                    gaonNameArray.clear();
                    gaonIdArray.clear();
                    gaonNameArray.add("Select Village");
                    gaonIdArray.add(0);
                    ArrayAdapter<String> gaonAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, gaonNameArray);
                    spVillage.setAdapter(gaonAdpt);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void addNewSociety(SocietyData societyData) {

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
            Call<SocietyData> societyDataCall = api.addSociety(societyData);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();


            societyDataCall.enqueue(new Callback<SocietyData>() {
                @Override
                public void onResponse(Call<SocietyData> call, retrofit2.Response<SocietyData> response) {
                    try {
                        if (response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            myLatLong = "";
                            Fragment adf = new SocietyFragment();
                            Bundle args = new Bundle();
                            args.putInt("EventId", eventId);
                            args.putString("Title", eventName);
                            adf.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "MeetingMaster").commit();
                            Log.e("Society : ", " --- " + response.body());

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Add Society", Toast.LENGTH_SHORT).show();
                            Log.e("Society : ", " --- " + response.body());
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Add Society", Toast.LENGTH_SHORT).show();
                        Log.e("Society : Exception", " --- " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<SocietyData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Add Society", Toast.LENGTH_SHORT).show();
                    Log.e("Society : ", " --- Failure : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.e("On Resume : ", "-------------------");
        if (myLatLong == null) {
            tvLoc.setText("");
        } else {
            tvLoc.setText("" + myLatLong);
        }
    }

    public void getSocietyData(int societyId) {
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
            Call<SocietyListData> societyListDataCall = api.getSociety(societyId);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            societyListDataCall.enqueue(new Callback<SocietyListData>() {
                @Override
                public void onResponse(Call<SocietyListData> call, retrofit2.Response<SocietyListData> response) {
                    try {
                        if (response.body() != null) {
                            SocietyListData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();

                                societyArrayList.clear();
                                for (int i = 0; i < data.getSocietyList().size(); i++) {
                                    societyArrayList.add(data.getSocietyList().get(i));
                                }

                                if (societyArrayList.size() > 0) {
                                    setData(societyArrayList.get(0));
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
                public void onFailure(Call<SocietyListData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void setData(SocietyList data) {

        int socType = data.getSocType();
        spSocType.setSelection(socType + 1);
        edRegNo.setText("" + data.getSocRegiNo());
        edSocName.setText("" + data.getSocName());
        edReprName.setText("" + data.getRepName());
        edReprDesg.setText("" + data.getRepDesignation());
        edMobile1.setText("" + data.getRepMobile());
        edMobile2.setText("" + data.getRepMobile2());
        edLandline.setText("" + data.getSocLandline());
        edEmail.setText("" + data.getRepEmailId());
        edApproxFarmers.setText("" + data.getSocFarmersApprox());
        tvLoc.setText("" + data.getSocLatLon());
        int storage = data.getSocIsStorage();
        if (storage == 1) {
            rbYes.setChecked(true);
        } else {
            rbNo.setChecked(true);
        }
        edSqFt.setText("" + data.getSocStorageSqft());

        villageId = data.getGaonId();

        String villageName = "";
        if (gaonArray.size() > 0) {
            for (int i = 0; i < gaonArray.size(); i++) {
                if (villageId == gaonArray.get(i).getGaonId()) {
                    villageName = gaonArray.get(i).getGaonName();
                }
            }
        }
        edVillage.setText("" + villageName);
        tvVillageId.setText("" + villageId);


        //  getAllGaonData();

    }

    public void showGaonDialog() {

        dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.search_dialog_layout, null, false);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ListView list1 = dialog.findViewById(R.id.lvSearchDialog_List);
        gaonAdapter = new GaonDataAdapter(getActivity().getApplicationContext(), gaonArray);
        list1.setAdapter(gaonAdapter);


        EditText edSearch = dialog.findViewById(R.id.edSearchDialog_Search);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (gaonAdapter != null)
                    gaonAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialog.show();
    }

    public class GaonDataAdapter extends BaseAdapter implements Filterable {

        private ArrayList<GaonList> originalValues;
        private ArrayList<GaonList> displayedValues;
        LayoutInflater inflater;

        public GaonDataAdapter(Context context, ArrayList<GaonList> arrayList) {
            this.originalValues = arrayList;
            this.displayedValues = arrayList;
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

        public class ViewHolder {
            TextView tvName;
            LinearLayout llBack;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            GaonDataAdapter.ViewHolder holder = null;

            if (v == null) {
                v = inflater.inflate(R.layout.custom_search_dialog_layout, null);
                holder = new GaonDataAdapter.ViewHolder();
                holder.tvName = v.findViewById(R.id.tvCustomSearch_Name);
                holder.llBack = v.findViewById(R.id.llCustomSearch_back);
                v.setTag(holder);
            } else {
                holder = (GaonDataAdapter.ViewHolder) v.getTag();
            }

            holder.tvName.setText("" + displayedValues.get(position).getGaonName());

            holder.llBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    edVillage.setText("" + displayedValues.get(position).getGaonName());
                    tvVillageId.setText("" + displayedValues.get(position).getGaonId());
                }
            });

            return v;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults results = new FilterResults();
                    ArrayList<GaonList> filteredArrayList = new ArrayList<>();
                    if (originalValues == null) {
                        originalValues = new ArrayList<GaonList>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String name = originalValues.get(i).getGaonName();
                            if (name.toLowerCase().startsWith(charSequence.toString())) {
                                filteredArrayList.add(new GaonList(originalValues.get(i).getGaonId(), originalValues.get(i).getTalId(), originalValues.get(i).getDistId(), originalValues.get(i).getRegId(), originalValues.get(i).getGaonName(), originalValues.get(i).getGaonDistNsk(), originalValues.get(i).getGaonDistTal(), originalValues.get(i).getGaonDistDist(), originalValues.get(i).getGaonDistReg(), originalValues.get(i).getGaonRemarks(), originalValues.get(i).getIsUsed()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<GaonList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };

            return filter;
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
