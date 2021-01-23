package com.ats.royalagro.fragment;


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
import com.ats.royalagro.bean.EventList;
import com.ats.royalagro.bean.EventListData;
import com.ats.royalagro.bean.Info;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.MeetingsDoneInterface;
import com.google.gson.Gson;

import java.text.DateFormat;
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

import static android.content.Context.MODE_PRIVATE;
import static com.ats.royalagro.fragment.MeetingsFragment.countDone;
import static com.ats.royalagro.fragment.MeetingsFragment.countUpcoming;
import static com.ats.royalagro.fragment.MeetingsFragment.tab;

public class MeetingsDoneFragment extends Fragment implements MeetingsDoneInterface {

    private LinearLayout ll1;
    private ArrayList<EventList> eventArrayList = new ArrayList<>();
    int empId, empType;

    private ListView lvMeetings;
    MeetingDoneAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meetings_done, container, false);

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
            empType = loginBean.getEmpType();
        }

        lvMeetings = view.findViewById(R.id.lvMeetingDone);

        ll1 = view.findViewById(R.id.llMeetDone1);
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment adf = new SocietyFragment();
                Bundle args = new Bundle();
                args.putString("Title", "Meeting 3");
                adf.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "MeetingMaster").commit();
            }
        });


        return view;
    }

    @Override
    public void fragmentBecameVisible() {
        if (empType == 0) {
            getAllEventsByAdmin();
        } else {
            getAllEvents(empId);
        }
    }


    public void getAllEvents(int emp_Id) {
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
            Call<EventListData> eventListDataCall = api.getAllEvents(emp_Id);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            eventListDataCall.enqueue(new Callback<EventListData>() {
                @Override
                public void onResponse(Call<EventListData> call, retrofit2.Response<EventListData> response) {
                    try {
                        if (response.body() != null) {
                            EventListData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                try {
                                    Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                                } catch (Exception e1) {
                                }
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();


                                Calendar cal = Calendar.getInstance();
                                long currDate = cal.getTimeInMillis();
                                eventArrayList.clear();
                                countUpcoming = 0;
                                countDone = 0;
                                for (int i = 0; i < data.getEventList().size(); i++) {
                                    String dt = data.getEventList().get(i).getEveDate() + " " + data.getEventList().get(i).getEveTime();
                                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    Date date = (Date) formatter.parse(dt);
                                    long dateMill = date.getTime();
                                    if (dateMill > currDate) {
                                        countUpcoming++;
                                        //Log.e("Upcoming", "---------");
                                    } else {
                                        countDone++;
                                        eventArrayList.add(data.getEventList().get(i));
                                    }
                                }


                                adapter = new MeetingDoneAdapter(getContext(), eventArrayList);
                                lvMeetings.setAdapter(adapter);
                                //Log.e("COUNT : ----", "Done : " + countDone + "\nUpcoming : " + countUpcoming);

                                TabLayout.Tab tab0 = tab.getTabAt(0);
                                tab0.setText("Upcoming (" + countUpcoming + ")");

                                TabLayout.Tab tab1 = tab.getTabAt(1);
                                tab1.setText("Done (" + countDone + ")");

                            }
                        } else {
                            progressDialog.dismiss();
                            try {
                                Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                            } catch (Exception e1) {
                            }
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        try {
                            Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                        } catch (Exception e1) {
                        }
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<EventListData> call, Throwable t) {
                    progressDialog.dismiss();
                    try {
                        Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                    } catch (Exception e1) {
                    }
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllEventsByAdmin() {
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
            Call<EventListData> eventListDataCall = api.getAllEventsByAdmin();

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();

            eventListDataCall.enqueue(new Callback<EventListData>() {
                @Override
                public void onResponse(Call<EventListData> call, retrofit2.Response<EventListData> response) {
                    try {
                        if (response.body() != null) {
                            EventListData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                try {
                                    Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                                } catch (Exception e1) {
                                }
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();


                                Calendar cal = Calendar.getInstance();
                                long currDate = cal.getTimeInMillis();
                                eventArrayList.clear();
                                countUpcoming = 0;
                                countDone = 0;
                                for (int i = 0; i < data.getEventList().size(); i++) {
                                    String dt = data.getEventList().get(i).getEveDate() + " " + data.getEventList().get(i).getEveTime();
                                    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                    Date date = (Date) formatter.parse(dt);
                                    long dateMill = date.getTime();
                                    if (dateMill > currDate) {
                                        countUpcoming++;
                                        //Log.e("Upcoming", "---------");
                                    } else {
                                        countDone++;
                                        eventArrayList.add(data.getEventList().get(i));
                                    }
                                }


                                adapter = new MeetingDoneAdapter(getContext(), eventArrayList);
                                lvMeetings.setAdapter(adapter);
                                //Log.e("COUNT : ----", "Done : " + countDone + "\nUpcoming : " + countUpcoming);

                                TabLayout.Tab tab0 = tab.getTabAt(0);
                                tab0.setText("Upcoming (" + countUpcoming + ")");

                                TabLayout.Tab tab1 = tab.getTabAt(1);
                                tab1.setText("Done (" + countDone + ")");

                            }
                        } else {
                            progressDialog.dismiss();
                            try {
                                Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                            } catch (Exception e1) {
                            }
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        try {
                            Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                        } catch (Exception e1) {
                        }
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<EventListData> call, Throwable t) {
                    progressDialog.dismiss();
                    try {
                        Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                    } catch (Exception e1) {
                    }
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    class MeetingDoneAdapter extends BaseAdapter implements Filterable {

        Context context;
        private ArrayList<EventList> originalValues;
        private ArrayList<EventList> displayedValues;
        LayoutInflater inflater;

        public MeetingDoneAdapter(Context context, ArrayList<EventList> eventLists) {
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
            v = inflater.inflate(R.layout.custom_meeting_item_layout, null);

            LinearLayout llBack = v.findViewById(R.id.llMeetingDone);
            TextView tvName = v.findViewById(R.id.tvMeetingDone_Name);
            TextView tvDate = v.findViewById(R.id.tvMeetingDone_Date);
            //TextView tvDate = v.findViewById(R.id.tvMeetingDone_Date);
            TextView tvType = v.findViewById(R.id.tvMeetingDone_Type);
            TextView tvPlace = v.findViewById(R.id.tvMeetingDone_Place);
            TextView tvMobile = v.findViewById(R.id.tvMeetingDone_Mobile);
            TextView tvEmail = v.findViewById(R.id.tvMeetingDone_Email);
            TextView tvCount = v.findViewById(R.id.tvMeetingDone_Count);
            ImageView ivPopup = v.findViewById(R.id.ivMeeting_popup);

            tvEmail.setVisibility(View.GONE);

            tvName.setText("" + displayedValues.get(position).getEveName());

            String hours = displayedValues.get(position).getEveTime().substring(0, 2);
            String minute = displayedValues.get(position).getEveTime().substring(3, 5);
            int hr = 0, mn = 0;
            try {
                hr = Integer.parseInt(hours);
                mn = Integer.parseInt(minute);
            } catch (Exception e) {

            }
            String amPm = hr % 12 + ":" + mn + " " + ((hr >= 12) ? "PM" : "AM");

            tvDate.setText("" + displayedValues.get(position).getEveDate() + " " + amPm);
            if (displayedValues.get(position).getEveType() == 0) {
                tvType.setText("R");
            } else if (displayedValues.get(position).getEveType() == 1) {
                tvType.setText("D");
            } else if (displayedValues.get(position).getEveType() == 2) {
                tvType.setText("T");
            } else if (displayedValues.get(position).getEveType() == 3) {
                tvType.setText("G");
            }

            tvPlace.setText("" + displayedValues.get(position).getEvePlace());
            tvMobile.setText("" + displayedValues.get(position).getEveOrgMobile());
            tvCount.setText("" + displayedValues.get(position).getEveApproxAttendance());

            llBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment adf = new SocietyFragment();
                    Bundle args = new Bundle();
                    args.putString("Title", displayedValues.get(position).getEveName());
                    args.putInt("EventId", displayedValues.get(position).getEveId());
                    adf.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "MeetingMaster").commit();
                }
            });

            ivPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.meeting_master_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.meeting_edit) {
                                Fragment adf = new AddMeetingsFragment();
                                Bundle args = new Bundle();
                                args.putInt("EventId", displayedValues.get(position).getEveId());
                                adf.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "MeetingMaster").commit();
                            } else if (menuItem.getItemId() == R.id.meeting_delete) {
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.AlertDialogTheme);
                                builder.setTitle("Confirm Action");
                                builder.setMessage("Do You Really Want To Delete Meeting?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteMeeting(displayedValues.get(position).getEveId());
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
                    // ShowPopupMenuIcon.setForceShowIcon(popupMenu);
                    popupMenu.show();
                }
            });

            tvMobile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = displayedValues.get(position).getEveOrgMobile();
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

            return v;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults results = new FilterResults();
                    ArrayList<EventList> filteredArrayList = new ArrayList<EventList>();

                    if (originalValues == null) {
                        originalValues = new ArrayList<EventList>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String eveName = originalValues.get(i).getEveName();
                            String orgName = originalValues.get(i).getEveOrgName();
                            String loc = originalValues.get(i).getEvePlace();
                            if (eveName.toLowerCase().startsWith(charSequence.toString()) || orgName.toLowerCase().startsWith(charSequence.toString()) || loc.toLowerCase().startsWith(charSequence.toString())) {
                                filteredArrayList.add(new EventList(originalValues.get(i).getEveId(), originalValues.get(i).getEveDate(), originalValues.get(i).getEveTime(), originalValues.get(i).getEveType(), originalValues.get(i).getRegId(), originalValues.get(i).getDistId(), originalValues.get(i).getTalId(), originalValues.get(i).getGaonId(), originalValues.get(i).getEveName(), originalValues.get(i).getEvePlace(), originalValues.get(i).getEveApproxAttendance(), originalValues.get(i).getEveOrgName(), originalValues.get(i).getEveOrgMobile(), originalValues.get(i).getEveOrgMobile2(), originalValues.get(i).getEveRemarks(), originalValues.get(i).getEveOutput(), originalValues.get(i).getEveStatus(), originalValues.get(i).getEmpRefDatetime(), originalValues.get(i).getEmpRefId()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<EventList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };


            return filter;
        }
    }


    public void deleteMeeting(int eventId) {
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
            Call<Info> infoCall = api.deleteEvent(eventId);

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
                                if (empType == 0) {
                                    getAllEventsByAdmin();
                                } else {
                                    getAllEvents(empId);
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
