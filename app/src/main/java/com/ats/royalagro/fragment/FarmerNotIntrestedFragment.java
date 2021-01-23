package com.ats.royalagro.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.ats.royalagro.bean.SocDataList;
import com.ats.royalagro.bean.TempSocietyListData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.FarmersNotIntrestedInterface;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.royalagro.fragment.FarmersListFragment.countIntrested;
import static com.ats.royalagro.fragment.FarmersListFragment.countNotIntrested;
import static com.ats.royalagro.fragment.FarmersListFragment.countPending;
import static com.ats.royalagro.fragment.FarmersListFragment.tabFarmer;

public class FarmerNotIntrestedFragment extends Fragment implements FarmersNotIntrestedInterface {

    int socId, empId, empType;
    FarmerNotIntrestedAdapter adapter;
    private ArrayList<SocDataList> farmerArrayList = new ArrayList<>();
    private ListView lvNotIntrested;

    public FarmerNotIntrestedFragment() {
    }

    @SuppressLint("ValidFragment")
    public FarmerNotIntrestedFragment(int socId) {
        this.socId = socId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farmer_not_intrested, container, false);

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
            empType = loginBean.getEmpType();
        }

        lvNotIntrested = view.findViewById(R.id.lvFarmersNotIntrestedList);

        return view;
    }

    @Override
    public void fragmentBecameVisible() {
        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
        }

        getFarmersBySociety(socId);
    }

    public void getFarmersBySociety(int socId) {
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
            final Call<TempSocietyListData> tempSocietyListDataCall = api.getAllFarmersBySociety(socId);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            tempSocietyListDataCall.enqueue(new Callback<TempSocietyListData>() {
                @Override
                public void onResponse(Call<TempSocietyListData> call, retrofit2.Response<TempSocietyListData> response) {
                    try {
                        if (response.body() != null) {
                            TempSocietyListData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();

                                farmerArrayList.clear();
                                countPending = 0;
                                countIntrested = 0;
                                countNotIntrested = 0;
                                for (int i = 0; i < data.getSocDataList().size(); i++) {
                                    if (data.getSocDataList().get(i).getTempStatus() == 0) {
                                        countPending++;
                                    } else if (data.getSocDataList().get(i).getTempStatus() == 1) {
                                        countIntrested++;
                                    } else if (data.getSocDataList().get(i).getTempStatus() == 2) {
                                        countNotIntrested++;
                                        farmerArrayList.add(data.getSocDataList().get(i));
                                    }
                                }


                                adapter = new FarmerNotIntrestedAdapter(getContext(), farmerArrayList);
                                lvNotIntrested.setAdapter(adapter);
                                //Log.e("COUNT : ----", "countNew : " + countNew + "\ncountReg : " + countReg);

                                TabLayout.Tab tab0 = tabFarmer.getTabAt(0);
                                tab0.setText("Pending\n(" + countPending + ")");

                                TabLayout.Tab tab1 = tabFarmer.getTabAt(1);
                                tab1.setText("Intrested\n(" + countIntrested + ")");

                                TabLayout.Tab tab2 = tabFarmer.getTabAt(2);
                                tab2.setText("Not Intrested\n(" + countNotIntrested + ")");

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
                public void onFailure(Call<TempSocietyListData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class FarmerNotIntrestedAdapter extends BaseAdapter implements Filterable {

        Context context;
        private ArrayList<SocDataList> originalValues;
        private ArrayList<SocDataList> displayedValues;
        LayoutInflater inflater;

        public FarmerNotIntrestedAdapter(Context context, ArrayList<SocDataList> societyLists) {
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
            v = inflater.inflate(R.layout.custom_farmer_item_layout, null);

            LinearLayout llBack = v.findViewById(R.id.llFarmers_back);
            TextView tvName = v.findViewById(R.id.tvFarmers_Name);
            TextView tvMobile1 = v.findViewById(R.id.tvFarmers_Mobile1);
            TextView tvMobile2 = v.findViewById(R.id.tvFarmers_Mobile2);
            TextView tvArea = v.findViewById(R.id.tvFarmers_Area);
            ImageView ivPopup = v.findViewById(R.id.ivFarmers_popup);

            tvName.setText("" + displayedValues.get(position).getFarmerFname() + " " + displayedValues.get(position).getFarmerLname() + " - " + displayedValues.get(position).getFarmerVillege());
            if (displayedValues.get(position).getFarmerMobile() != null) {
                tvMobile1.setText("" + displayedValues.get(position).getFarmerMobile());
            }
            if (displayedValues.get(position).getFarmerMobile2() != null) {
                tvMobile2.setText("" + displayedValues.get(position).getFarmerMobile2());
            }
            tvArea.setText("Area : " + displayedValues.get(position).getFarmerAreaAcre() + " acre");

            llBack.setBackgroundColor(Color.parseColor("#d6d4d4"));


            if (empType == 3 || empType == 0) {
                ivPopup.setVisibility(View.VISIBLE);
            } else {
                ivPopup.setVisibility(View.GONE);
            }
/*
            if (empType != 3) {
                ivPopup.setVisibility(View.GONE);
            }
*/

            ivPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.farmer_notinst_intrested, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.f_notinst_intrested) {
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                java.util.Date date = new java.util.Date();

                                updateStatus(displayedValues.get(position).getTempId(), empId, 1, "Intrested", dateFormat.format(date));
                            }
                            return true;
                        }
                    });
                    //  ShowPopupMenuIcon.setForceShowIcon(popupMenu);
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
                    ArrayList<SocDataList> filteredArrayList = new ArrayList<SocDataList>();

                    if (originalValues == null) {
                        originalValues = new ArrayList<SocDataList>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String name = originalValues.get(i).getFarmerFname();
                            String mName = originalValues.get(i).getFarmerMname();
                            String lName = originalValues.get(i).getFarmerLname();
                            String fName = name + " " + mName + " " + lName;
                            String fLName = name + " " + lName;
                            String gName = originalValues.get(i).getFarmerVillege();

                            if (name.toLowerCase().startsWith(charSequence.toString()) || name.toLowerCase().contains(charSequence.toString()) || mName.toLowerCase().startsWith(charSequence.toString()) || mName.toLowerCase().contains(charSequence.toString()) || lName.toLowerCase().startsWith(charSequence.toString()) || lName.toLowerCase().contains(charSequence.toString()) || fName.toLowerCase().startsWith(charSequence.toString()) || fName.toLowerCase().contains(charSequence.toString()) || fLName.toLowerCase().startsWith(charSequence.toString()) || fLName.toLowerCase().contains(charSequence.toString()) || gName.toLowerCase().startsWith(charSequence.toString()) || gName.toLowerCase().contains(charSequence.toString())) {
                                filteredArrayList.add(new SocDataList(originalValues.get(i).getTempId(), originalValues.get(i).getSocId(), originalValues.get(i).getFarmerFname(), originalValues.get(i).getFarmerMname(), originalValues.get(i).getFarmerLname(), originalValues.get(i).getFarmerAddr(), originalValues.get(i).getFarmerVillege(), originalValues.get(i).getFarmerTal(), originalValues.get(i).getFarmerDist(), originalValues.get(i).getFarmerMobile(), originalValues.get(i).getFarmerMobile2(), originalValues.get(i).getFarmerAreaAcre(), originalValues.get(i).getFarmerGatNo(), originalValues.get(i).getEnterBy(), originalValues.get(i).getEnterDatetime(), originalValues.get(i).getEnterMode(), originalValues.get(i).getVisitBy(), originalValues.get(i).getVisitDatetime(), originalValues.get(i).getTempStatus(), originalValues.get(i).getVisitRemarks()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<SocDataList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };


            return filter;
        }
    }

    public void updateStatus(int tempId, int empId, int status, String remark, String dateTime) {

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
            Call<Info> infoCall = api.updateFarmerFlag(tempId, empId, status, remark, dateTime);

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
                                Toast.makeText(getActivity(), "Unable To Update Status", Toast.LENGTH_SHORT).show();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                getFarmersBySociety(socId);
                            }
                          /*  Fragment adf = new SocietyFragment();
                            Bundle args = new Bundle();
                            args.putInt("EventId", eventId);
                            args.putString("Title", eventName);
                            adf.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "MeetingMaster").commit();
*/

                        } else {
                            //Log.e("null--", "----------------");
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Sorry Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Log.e("Exception--", "----------------" + e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Sorry Please Try Again", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<Info> call, Throwable t) {
                    progressDialog.dismiss();
                    //Log.e("Failure--", "----------------" + t.getMessage());
                    Toast.makeText(getActivity(), "Sorry Please Try Again", Toast.LENGTH_SHORT).show();

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
                if (adapter != null)
                    adapter.getFilter().filter(charSequence.toString());
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
