package com.ats.royalagro.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.Info;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.bean.SocietyList;
import com.ats.royalagro.bean.SocietyListData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.SocietyNoDetailsInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.royalagro.fragment.SocietyFragment.countNew;
import static com.ats.royalagro.fragment.SocietyFragment.countReg;
import static com.ats.royalagro.fragment.SocietyFragment.tabSoc;

public class SocietyNoDetailsFragment extends Fragment implements SocietyNoDetailsInterface {

    private LinearLayout ll1, ll2;
    int eventId, empId, empType;
    String eventName;
    private ListView lvRegistered;

    SocietyRegisteredAdapter socRegAdapter;
    private ArrayList<SocietyList> societyArrayList = new ArrayList<>();

    public SocietyNoDetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public SocietyNoDetailsFragment(int eventId, String eventName) {

        this.eventId = eventId;
        this.eventName = eventName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_society_no_details, container, false);

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
            empType = loginBean.getEmpType();
        }

        lvRegistered = view.findViewById(R.id.lvSocietyRegistered);


        ll1 = view.findViewById(R.id.llSocNoDet_1);
        ll2 = view.findViewById(R.id.llSocNoDet_2);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main, new FarmersListFragment(), "SocietyMaster");
                transaction.commit();
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main, new FarmersListFragment(), "SocietyMaster");
                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void fragmentBecameVisible() {
        if (eventId == 0) {
            if (empType == 0) {
                getAllSocietyByAdmin();
            } else {
                getAllSocietyData(empId);
            }
        } else {
            if (empType == 0) {
                getSocietyDataByEvent(eventId);
            } else {
                getSocietyData(empId, eventId);
            }
        }
    }

    public void getAllSocietyData(int empId) {
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
            Call<SocietyListData> societyListDataCall = api.getAllSocietyByEmp(empId);

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
                                try {
                                    Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                }
                                Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();

                                societyArrayList.clear();
                                countNew = 0;
                                countReg = 0;
                                for (int i = 0; i < data.getSocietyList().size(); i++) {
                                    if (data.getSocietyList().get(i).getSocStatus() == 0) {
                                        countNew++;
                                    } else if (data.getSocietyList().get(i).getSocStatus() == 1) {
                                        countReg++;
                                        societyArrayList.add(data.getSocietyList().get(i));
                                    }
                                }


                                socRegAdapter = new SocietyRegisteredAdapter(getContext(), societyArrayList);
                                lvRegistered.setAdapter(socRegAdapter);
                                //Log.e("COUNT : ----", "countNew : " + countNew + "\ncountReg : " + countReg);

                                TabLayout.Tab tab0 = tabSoc.getTabAt(0);
                                tab0.setText("New (" + countNew + ")");

                                TabLayout.Tab tab1 = tabSoc.getTabAt(1);
                                tab1.setText("Registered (" + countReg + ")");

                            }
                        } else {
                            progressDialog.dismiss();
                            try {
                                Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            }
                            Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        try {
                            Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                        } catch (Exception e1) {
                        }
                        e.printStackTrace();
                        Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SocietyListData> call, Throwable t) {
                    progressDialog.dismiss();
                    try {
                        Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                    t.printStackTrace();
                    Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }


    public void getAllSocietyByAdmin() {
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
            Call<SocietyListData> societyListDataCall = api.getAllSociety();

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            societyListDataCall.enqueue(new Callback<SocietyListData>() {
                @Override
                public void onResponse(Call<SocietyListData> call, retrofit2.Response<SocietyListData> response) {
                    try {
                        if (response.body() != null) {
                            SocietyListData data = response.body();

                            progressDialog.dismiss();

                            societyArrayList.clear();
                            countNew = 0;
                            countReg = 0;
                            for (int i = 0; i < data.getSocietyList().size(); i++) {
                                if (data.getSocietyList().get(i).getSocStatus() == 0) {
                                    countNew++;
                                } else if (data.getSocietyList().get(i).getSocStatus() == 1) {
                                    countReg++;
                                    societyArrayList.add(data.getSocietyList().get(i));
                                }
                            }


                            socRegAdapter = new SocietyRegisteredAdapter(getContext(), societyArrayList);
                            lvRegistered.setAdapter(socRegAdapter);
                            //Log.e("COUNT : ----", "countNew : " + countNew + "\ncountReg : " + countReg);

                            TabLayout.Tab tab0 = tabSoc.getTabAt(0);
                            tab0.setText("New (" + countNew + ")");

                            TabLayout.Tab tab1 = tabSoc.getTabAt(1);
                            tab1.setText("Registered (" + countReg + ")");


                        } else {
                            progressDialog.dismiss();
                            try {
                                Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            }
                            Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        try {
                            Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                        } catch (Exception e1) {
                        }
                        e.printStackTrace();
                        Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SocietyListData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getSocietyData(int empId, int eventId) {
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
            Call<SocietyListData> societyListDataCall = api.getAllSociety(eventId, empId);

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
                                try {
                                    Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                }
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();


                                societyArrayList.clear();
                                countNew = 0;
                                countReg = 0;
                                for (int i = 0; i < data.getSocietyList().size(); i++) {
                                    if (data.getSocietyList().get(i).getSocStatus() == 0) {
                                        countNew++;
                                    } else if (data.getSocietyList().get(i).getSocStatus() == 1) {
                                        countReg++;
                                        societyArrayList.add(data.getSocietyList().get(i));
                                    }
                                }


                                socRegAdapter = new SocietyRegisteredAdapter(getContext(), societyArrayList);
                                lvRegistered.setAdapter(socRegAdapter);
                                //Log.e("COUNT : ----", "countNew : " + countNew + "\ncountReg : " + countReg);

                                TabLayout.Tab tab0 = tabSoc.getTabAt(0);
                                tab0.setText("New (" + countNew + ")");

                                TabLayout.Tab tab1 = tabSoc.getTabAt(1);
                                tab1.setText("Registered (" + countReg + ")");

                            }
                        } else {
                            progressDialog.dismiss();
                            try {
                                Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                            } catch (Exception e1) {
                            }
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        try {
                            Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                        } catch (Exception e1) {
                        }
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SocietyListData> call, Throwable t) {
                    progressDialog.dismiss();
                    try {
                        Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getSocietyDataByEvent(int eventId) {
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
            Call<SocietyListData> societyListDataCall = api.getAllSocietyByEvent(eventId);

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
                                try {
                                    Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                }
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();


                                societyArrayList.clear();
                                countNew = 0;
                                countReg = 0;
                                for (int i = 0; i < data.getSocietyList().size(); i++) {
                                    if (data.getSocietyList().get(i).getSocStatus() == 0) {
                                        countNew++;
                                    } else if (data.getSocietyList().get(i).getSocStatus() == 1) {
                                        countReg++;
                                        societyArrayList.add(data.getSocietyList().get(i));
                                    }
                                }


                                socRegAdapter = new SocietyRegisteredAdapter(getContext(), societyArrayList);
                                lvRegistered.setAdapter(socRegAdapter);
                                //Log.e("COUNT : ----", "countNew : " + countNew + "\ncountReg : " + countReg);

                                TabLayout.Tab tab0 = tabSoc.getTabAt(0);
                                tab0.setText("New (" + countNew + ")");

                                TabLayout.Tab tab1 = tabSoc.getTabAt(1);
                                tab1.setText("Registered (" + countReg + ")");

                            }
                        } else {
                            progressDialog.dismiss();
                            try {
                                Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            }
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        try {
                            Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                        } catch (Exception e1) {
                        }
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<SocietyListData> call, Throwable t) {
                    progressDialog.dismiss();
                    try {
                        Toast.makeText(getActivity(), "No Society Found", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class SocietyRegisteredAdapter extends BaseAdapter implements Filterable {

        Context context;
        private ArrayList<SocietyList> originalValues;
        private ArrayList<SocietyList> displayedValues;
        LayoutInflater inflater;

        public SocietyRegisteredAdapter(Context context, ArrayList<SocietyList> societyLists) {
            this.context = context;
            this.originalValues = societyLists;
            this.displayedValues = societyLists;
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
            v = inflater.inflate(R.layout.custom_society_item_layout, null);

            LinearLayout llBack = v.findViewById(R.id.llSocietyItem);
            TextView tvSocName = v.findViewById(R.id.tvSocietyItem_Name);
            TextView tvRepName = v.findViewById(R.id.tvSocietyItem_RepName);
            TextView tvRepMobile = v.findViewById(R.id.tvSocietyItem_RepMobile);
            TextView tvRepEmail = v.findViewById(R.id.tvSocietyItem_RepEmail);
            ImageView ivPopup = v.findViewById(R.id.ivSocietyItem_popup);

            tvSocName.setText("" + displayedValues.get(position).getSocName());
            tvRepName.setText("" + displayedValues.get(position).getRepName());
            tvRepMobile.setText("" + displayedValues.get(position).getRepMobile());
            tvRepEmail.setText("" + displayedValues.get(position).getRepEmailId());

            //  llBack.setBackgroundColor(Color.parseColor("#fc9697"));

            llBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment adf = new FarmersListFragment();
                    Bundle args = new Bundle();
                    args.putString("SocietyName", displayedValues.get(position).getSocName());
                    args.putInt("SocietyId", displayedValues.get(position).getSocId());
                    adf.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "SocietyMaster").commit();
                }
            });

            tvRepMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = displayedValues.get(position).getRepMobile();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + number));
                    int result = getContext().checkCallingOrSelfPermission(android.Manifest.permission.CALL_PHONE);
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        startActivity(intent);
                    } else {
                        //  Toast.makeText(getActivity(), "Device not supported", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ivPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.society_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.society_edit) {

                                Fragment adf = new AddSocietyFragment();
                                Bundle args = new Bundle();
                                args.putString("EventName", eventName);
                                args.putInt("EventId", eventId);
                                args.putInt("SocietyId", displayedValues.get(position).getSocId());
                                adf.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "SocietyMaster").commit();
                            } else if (menuItem.getItemId() == R.id.society_delete) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogTheme);
                                builder.setTitle("Confirm Action");
                                builder.setMessage("Do You Really Want To Delete Society?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteSoc(displayedValues.get(position).getSocId());
                                        dialog.dismiss();
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                android.support.v7.app.AlertDialog dialog = builder.create();
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

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults results = new FilterResults();
                    ArrayList<SocietyList> filteredArrayList = new ArrayList<SocietyList>();

                    if (originalValues == null) {
                        originalValues = new ArrayList<SocietyList>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String socName = originalValues.get(i).getSocName();
                            String repName = originalValues.get(i).getRepName();
                            if (socName.toLowerCase().startsWith(charSequence.toString()) || repName.toLowerCase().startsWith(charSequence.toString())) {
                                filteredArrayList.add(new SocietyList(originalValues.get(i).getSocId(), originalValues.get(i).getSocType(), originalValues.get(i).getSocRegiNo(), originalValues.get(i).getSocName(), originalValues.get(i).getEveId(), originalValues.get(i).getGaonId(), originalValues.get(i).getRepName(), originalValues.get(i).getRepDesignation(), originalValues.get(i).getRepMobile(), originalValues.get(i).getRepMobile2(), originalValues.get(i).getRepEmailId(), originalValues.get(i).getSocLandline(), originalValues.get(i).getSocFarmersApprox(), originalValues.get(i).getSocStatus(), originalValues.get(i).getSocPerCharges(), originalValues.get(i).getSocIsStorage(), originalValues.get(i).getSocStorageSqft(), originalValues.get(i).getSocEnteredBy(), originalValues.get(i).getSocEnterDatetime(), originalValues.get(i).getSocLatLon(), originalValues.get(i).getSocRegBy(), originalValues.get(i).getSocRegDatetime(), originalValues.get(i).getSocBlockBy(), originalValues.get(i).getSocBlockDatetime(), originalValues.get(i).getIsUsed()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<SocietyList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };


            return filter;
        }
    }

    public void deleteSoc(int socId) {
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
            Call<Info> infoCall = api.deleteSociety(socId);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            infoCall.enqueue(new Callback<Info>() {
                @Override
                public void onResponse(Call<Info> call, retrofit2.Response<Info> response) {
                    try {
                        if (response.body() != null) {
                            Info data = response.body();
                            if (data.getError()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                                //Log.e("onError : ", "----" + data.getMessage());
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                lvRegistered.setAdapter(null);
                                if (eventId == 0) {
                                    if (empType == 0) {
                                        getAllSocietyByAdmin();
                                    } else {
                                        getAllSocietyData(empId);
                                    }
                                } else {
                                    if (empType == 0) {
                                        getSocietyDataByEvent(eventId);
                                    } else {
                                        getSocietyData(empId, eventId);
                                    }
                                }
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Delete", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);

        SearchView searchView = (SearchView) item.getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorWhite));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorWhite));
        ImageView v = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        v.setImageResource(R.drawable.ic_plus); //Changing the image

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (socRegAdapter != null)
                    socRegAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchView.setQueryHint("Search");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

}
