package com.ats.royalagro.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.District;
import com.ats.royalagro.bean.DistrictData;
import com.ats.royalagro.bean.DistrictList;
import com.ats.royalagro.bean.EventData;
import com.ats.royalagro.bean.EventList;
import com.ats.royalagro.bean.EventListData;
import com.ats.royalagro.bean.GaonData;
import com.ats.royalagro.bean.GaonList;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.bean.Region;
import com.ats.royalagro.bean.RegionData;
import com.ats.royalagro.bean.RegionList;
import com.ats.royalagro.bean.TalukaData;
import com.ats.royalagro.bean.TalukaList;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

import static android.content.Context.MODE_PRIVATE;

public class AddMeetingsFragment extends Fragment implements View.OnClickListener {

    private Boolean isDistTouch = false, isTalukaTouch = false, isGaonTouch = false, isRegionTouch = false;
    int empId, eveId, empType;
    int regionId, distId, talId, villageId;

    ArrayList<EventList> eventArray = new ArrayList<>();

    ArrayList<TalukaList> talukaArray = new ArrayList<>();
    ArrayList<GaonList> gaonArray = new ArrayList<>();

    private EditText edEventName, edDate, edTime, edPlace, edAppxAtt, edOrgName, edOrgMobile, edOrgMobile2, edRemark, edRegion, edDistrict, edTaluka, edGaon;
    private Spinner spEventType, spRegion, spTaluka, spDistrict, spVillage;
    private Button btnSubmit, btnReset;
    private LinearLayout llRegion, llDistrict, llTaluka, llVillage;
    private TextView tvTypeId, tvRegionId, tvDistrictId, tvTalukaId, tvVillageId, tvTime;

    int mYear, mMonth, mDay, mHour, mMinute, mSeconds;
    long dateMillis;

    ArrayList<String> regionNameArray = new ArrayList<>();
    ArrayList<Integer> regionIdArray = new ArrayList<>();


    ArrayList<String> districtNameArray = new ArrayList<>();
    ArrayList<Integer> districtIdArray = new ArrayList<>();
    ArrayList<Integer> distRegIdArray = new ArrayList<>();

    ArrayList<String> talukaNameArray = new ArrayList<>();
    ArrayList<Integer> talukaIdArray = new ArrayList<>();

    ArrayList<String> gaonNameArray = new ArrayList<>();
    ArrayList<Integer> gaonIdArray = new ArrayList<>();

    GaonDataAdapter gaonAdapter;
    TalukaDataAdapter talukaAdapter;
    Dialog dialog, dialogTaluka;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_meetings, container, false);
        getActivity().setTitle("Add New Meeting");

        try {
            eveId = getArguments().getInt("EventId");
        } catch (Exception e) {
            eveId = 0;
        }

        //Log.e("Event Id : ", "---------" + eveId);


        edEventName = view.findViewById(R.id.edAddMeeting_EventName);
        edDate = view.findViewById(R.id.edAddMeeting_Date);
        edTime = view.findViewById(R.id.edAddMeeting_Time);
        edPlace = view.findViewById(R.id.edAddMeeting_EventPlace);
        edAppxAtt = view.findViewById(R.id.edAddMeeting_AppxAttendance);
        edOrgName = view.findViewById(R.id.edAddMeeting_OrgName);
        edOrgMobile = view.findViewById(R.id.edAddMeeting_OrgMobile);
        edOrgMobile2 = view.findViewById(R.id.edAddMeeting_OrgMobile2);
        edRemark = view.findViewById(R.id.edAddMeeting_Remark);
        edRegion = view.findViewById(R.id.edAddMeeting_Region);
        edDistrict = view.findViewById(R.id.edAddMeeting_District);
        edTaluka = view.findViewById(R.id.edAddMeeting_Taluka);
        edGaon = view.findViewById(R.id.edAddMeeting_Village);

        tvTypeId = view.findViewById(R.id.tvAddMeeting_HideType);
        tvRegionId = view.findViewById(R.id.tvAddMeeting_HideRegion);
        tvDistrictId = view.findViewById(R.id.tvAddMeeting_HideDistrict);
        tvTalukaId = view.findViewById(R.id.tvAddMeeting_HideTaluka);
        tvVillageId = view.findViewById(R.id.tvAddMeeting_HideVillage);
        tvTime = view.findViewById(R.id.tvAddMeeting_HideTime);

        spEventType = view.findViewById(R.id.spAddMeeting_Type);
        spDistrict = view.findViewById(R.id.spAddMeeting_District);
        spRegion = view.findViewById(R.id.spAddMeeting_Region);
        spTaluka = view.findViewById(R.id.spAddMeeting_Taluka);
        spVillage = view.findViewById(R.id.spAddMeeting_Village);

        llDistrict = view.findViewById(R.id.llAddMeeting_District);
        llRegion = view.findViewById(R.id.llAddMeeting_Region);
        llTaluka = view.findViewById(R.id.llAddMeeting_Taluka);
        llVillage = view.findViewById(R.id.llAddMeeting_Village);

        btnSubmit = view.findViewById(R.id.btnAddMeeting_Save);
        btnReset = view.findViewById(R.id.btnAddMeeting_Reset);

        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        edDate.setOnClickListener(this);
        edTime.setOnClickListener(this);
        edGaon.setOnClickListener(this);
        edTaluka.setOnClickListener(this);

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
            getAdminAccessData();
        } else {
            getAccessData();
        }

        ArrayList<String> eventTypeArray = new ArrayList<>();
        eventTypeArray.add("Select Type");
        eventTypeArray.add("Region");
        eventTypeArray.add("District");
        eventTypeArray.add("Taluka");
        eventTypeArray.add("Village");

        ArrayAdapter<String> typeAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, eventTypeArray);
        spEventType.setAdapter(typeAdpt);

        spEventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    llRegion.setVisibility(View.GONE);
                    llDistrict.setVisibility(View.GONE);
                    llTaluka.setVisibility(View.GONE);
                    llVillage.setVisibility(View.GONE);
                } else if (position == 1) {
                    tvTypeId.setText("0");
                    llRegion.setVisibility(View.VISIBLE);
                    llDistrict.setVisibility(View.GONE);
                    llTaluka.setVisibility(View.GONE);
                    llVillage.setVisibility(View.GONE);
                } else if (position == 2) {
                    tvTypeId.setText("1");
                    llRegion.setVisibility(View.VISIBLE);
                    llDistrict.setVisibility(View.VISIBLE);
                    llTaluka.setVisibility(View.GONE);
                    llVillage.setVisibility(View.GONE);
                } else if (position == 3) {
                    tvTypeId.setText("2");
                    llRegion.setVisibility(View.VISIBLE);
                    llDistrict.setVisibility(View.VISIBLE);
                    llTaluka.setVisibility(View.VISIBLE);
                    llVillage.setVisibility(View.GONE);
                } else if (position == 4) {
                    tvTypeId.setText("3");
                    llRegion.setVisibility(View.VISIBLE);
                    llDistrict.setVisibility(View.VISIBLE);
                    llTaluka.setVisibility(View.VISIBLE);
                    llVillage.setVisibility(View.VISIBLE);
                }

             /*   if (position != 0) {
                    getAllRegionData();
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        dateMillis = c.getTimeInMillis();


        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvRegionId.setText("" + regionIdArray.get(position));
                Log.e("in spRegion", "------------");
              /*  try {
                    if (districtIdArray.size() > 0) {
                        ArrayList<String> tempDistName = new ArrayList<>();
                        ArrayList<Integer> tempDistId = new ArrayList<>();

                        Log.e("Dist Id : ", " --- " + distRegIdArray.toString());
                        for (int i = 0; i < districtIdArray.size(); i++) {
                            if (regionIdArray.get(position) == distRegIdArray.get(i)) {
                                tempDistId.add(districtIdArray.get(i));
                                tempDistName.add(districtNameArray.get(i));

                            }
                        }
                        Log.e("in spRegion", "------------Name : " + tempDistName);
                        ArrayAdapter<String> distAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, tempDistName);
                        spDistrict.setAdapter(distAdpt);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("in spRegion", "------------Exception : " + e.getMessage());
                }*/

               /* if (eveId <= 0) {
                    if (isRegionTouch) {
                        isDistTouch = false;
                        getDistrictData(regionIdArray.get(position));
                        spTaluka.setAdapter(null);
                        spVillage.setAdapter(null);
                    }

                } else {
                    getDistrictData(regionIdArray.get(position));
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvDistrictId.setText("" + districtIdArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


/*
        spRegion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isRegionTouch = true;
                }
                return false;
            }
        });

        /*
        spDistrict.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isDistTouch = true;
                }
                return false;
            }
        });
        spTaluka.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isTalukaTouch = true;
                }
                return false;
            }
        });
        spVillage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isGaonTouch = true;
                }
                return false;
            }
        });

        spRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvRegionId.setText("" + regionIdArray.get(position));
                if (eveId <= 0) {
                    if (isRegionTouch) {
                        isDistTouch = false;
                        getDistrictData(regionIdArray.get(position));
                        spTaluka.setAdapter(null);
                        spVillage.setAdapter(null);
                    }

                } else {
                    getDistrictData(regionIdArray.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvDistrictId.setText("" + districtIdArray.get(position));
                if (eveId <= 0) {
                    if (isDistTouch) {
                        isDistTouch = false;
                        getTalukaData(districtIdArray.get(position));
                    }
                } else {
                    getTalukaData(districtIdArray.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Log.e("not selected", "----");
            }
        });

        spTaluka.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvTalukaId.setText("" + talukaIdArray.get(position));
                if (eveId <= 0) {
                    if (isTalukaTouch) {
                        isTalukaTouch = false;
                        getGaonData(talukaIdArray.get(position));
                    }
                } else {
                    getGaonData(talukaIdArray.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spVillage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvVillageId.setText("" + gaonIdArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/


        if (eveId > 0) {
            getEvent(eveId);
        }


        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnAddMeeting_Save) {

            Boolean isValidate = false;

            if (edEventName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Event Name", Toast.LENGTH_SHORT).show();
                edEventName.requestFocus();
            } else if (edDate.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Event Date", Toast.LENGTH_SHORT).show();
                edDate.requestFocus();
            } else if (edTime.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Event Time", Toast.LENGTH_SHORT).show();
                edTime.requestFocus();
            } else if (spEventType.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select Event Type", Toast.LENGTH_SHORT).show();
                spEventType.requestFocus();
            } else if (spEventType.getSelectedItemPosition() == 1) {
                isValidate = true;
                /*if (spRegion.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select Region", Toast.LENGTH_SHORT).show();
                    spRegion.requestFocus();
                }
                if (edRegion.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Region", Toast.LENGTH_SHORT).show();
                    edRegion.requestFocus();
                }
                else {
                    isValidate = true;
                }*/
            } else if (spEventType.getSelectedItemPosition() == 2) {
                isValidate = true;
                /*if (spRegion.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select Region", Toast.LENGTH_SHORT).show();
                    spRegion.requestFocus();
                } else if (spDistrict.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_SHORT).show();
                    spDistrict.requestFocus();
                }
                if (edRegion.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Region", Toast.LENGTH_SHORT).show();
                    edRegion.requestFocus();
                } else if (edDistrict.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_SHORT).show();
                    edDistrict.requestFocus();
                } else {
                    isValidate = true;
                }*/
            } else if (spEventType.getSelectedItemPosition() == 3) {
                /*if (spRegion.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select Region", Toast.LENGTH_SHORT).show();
                    spRegion.requestFocus();
                } else if (spDistrict.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_SHORT).show();
                    spDistrict.requestFocus();
                } else if (spTaluka.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select Taluka", Toast.LENGTH_SHORT).show();
                    spTaluka.requestFocus();
                }
                if (edRegion.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Region", Toast.LENGTH_SHORT).show();
                    edRegion.requestFocus();
                } else if (edDistrict.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_SHORT).show();
                    edDistrict.requestFocus();
                }
                */
                if (edTaluka.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Taluka", Toast.LENGTH_SHORT).show();
                    edTaluka.requestFocus();
                } else {
                    isValidate = true;
                }
            } else if (spEventType.getSelectedItemPosition() == 4) {
                /*if (spRegion.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select Region", Toast.LENGTH_SHORT).show();
                    spRegion.requestFocus();
                } else if (spDistrict.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_SHORT).show();
                    spDistrict.requestFocus();
                } else if (spTaluka.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select Taluka", Toast.LENGTH_SHORT).show();
                    spTaluka.requestFocus();
                } else if (spVillage.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Please Select Village", Toast.LENGTH_SHORT).show();
                    spVillage.requestFocus();
                }*/
/*                if (edRegion.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Region", Toast.LENGTH_SHORT).show();
                    edRegion.requestFocus();
                } else if (edDistrict.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select District", Toast.LENGTH_SHORT).show();
                    edDistrict.requestFocus();
                } else */
                if (edTaluka.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Taluka", Toast.LENGTH_SHORT).show();
                    edTaluka.requestFocus();
                } else if (edGaon.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Select Village", Toast.LENGTH_SHORT).show();
                    edGaon.requestFocus();
                } else {
                    isValidate = true;
                }
            }

            if (isValidate) {
                if (edPlace.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Event Place", Toast.LENGTH_SHORT).show();
                    edPlace.requestFocus();
                } else if (edAppxAtt.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Approximate Attendance", Toast.LENGTH_SHORT).show();
                    edAppxAtt.requestFocus();
                } else if (edOrgName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Organiser Name", Toast.LENGTH_SHORT).show();
                    edOrgName.requestFocus();
                } else if (edOrgMobile.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Organiser Mobile Number", Toast.LENGTH_SHORT).show();
                    edOrgMobile.requestFocus();
                } else if (edOrgMobile.getText().toString().length() != 10) {
                    Toast.makeText(getActivity(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                    edOrgMobile.requestFocus();
                }
                /*else if (edOrgMobile2.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Organiser Alternate Mobile Number", Toast.LENGTH_SHORT).show();
                    edOrgMobile2.requestFocus();
                }
                else if (edOrgMobile2.getText().toString().length() != 10) {
                    Toast.makeText(getActivity(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                    edOrgMobile2.requestFocus();
                }
                else if (edRemark.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Remark", Toast.LENGTH_SHORT).show();
                    edRemark.requestFocus();
                }
                 */
                else {

                    String eventName = edEventName.getText().toString();
                    String date = edDate.getText().toString();
                    String time = tvTime.getText().toString();
                    int eventType = spEventType.getSelectedItemPosition() - 1;
                    int regId = 0, disId = 0, talId = 0, gaonId = 0;

                    if (!tvRegionId.getText().toString().isEmpty()) {
                        regId = Integer.parseInt(tvRegionId.getText().toString());
                    } else {
                        regId = 0;
                    }
                    if (!tvDistrictId.getText().toString().isEmpty()) {
                        disId = Integer.parseInt(tvDistrictId.getText().toString());
                    } else {
                        disId = 0;
                    }
                    if (!tvTalukaId.getText().toString().isEmpty()) {
                        talId = Integer.parseInt(tvTalukaId.getText().toString());
                    } else {
                        talId = 0;
                    }
                    if (!tvVillageId.getText().toString().isEmpty()) {
                        gaonId = Integer.parseInt(tvVillageId.getText().toString());
                    } else {
                        gaonId = 0;
                    }

                    if (eventType == 0) {
                        disId = 0;
                        talId = 0;
                        gaonId = 0;
                    } else if (eventType == 1) {
                        talId = 0;
                        gaonId = 0;
                    } else if (eventType == 2) {
                        gaonId = 0;
                    }


                    String eventPlace = edPlace.getText().toString();
                    int appxAtt = Integer.parseInt(edAppxAtt.getText().toString());
                    String orgName = edOrgName.getText().toString();
                    String mob1 = edOrgMobile.getText().toString();
                    String mob2 = edOrgMobile2.getText().toString();
                    String remark = edRemark.getText().toString();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currDateTime = sdf.format(new Date());

                    //Log.e("current date time : ", "--- " + currDateTime);

                    EventData eventData = new EventData(eveId, date, time, eventType, regId, disId, talId, gaonId, eventName, eventPlace, appxAtt, orgName, mob1, mob2, remark, "", 1, currDateTime, empId);
                    addNewEvent(eventData);

                }
            }

            // EventData eventData=new EventData();


        } else if (v.getId() == R.id.btnAddMeeting_Reset) {

            edEventName.setText("");
            edDate.setText("");
            edTime.setText("");
            edPlace.setText("");
            edAppxAtt.setText("");
            edOrgName.setText("");
            edOrgMobile2.setText("");
            edOrgMobile.setText("");
            edRemark.setText("");
            spEventType.setSelection(0);
            tvTime.setText("");
            tvVillageId.setText("");
            tvTalukaId.setText("");
            tvDistrictId.setText("");
            tvRegionId.setText("");
            tvTypeId.setText("");
            spRegion.setAdapter(null);
            spDistrict.setAdapter(null);
            spTaluka.setAdapter(null);
            spVillage.setAdapter(null);

        } else if (v.getId() == R.id.edAddMeeting_Date) {
            Calendar purchaseCal = Calendar.getInstance();
            purchaseCal.setTimeInMillis(dateMillis);
            int yr = purchaseCal.get(Calendar.YEAR);
            int mn = purchaseCal.get(Calendar.MONTH);
            int dy = purchaseCal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), dateListener, yr, mn, dy);
            dialog.show();

        } else if (v.getId() == R.id.edAddMeeting_Time) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();

            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            mSeconds = c.get(Calendar.SECOND);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                            //Log.e("Time : ", "----" + hourOfDay + ":" + minute + ":00");
                            tvTime.setText("" + hourOfDay + ":" + minute + ":00");
                            edTime.setText(amPm);
                        }
                    }, mHour, mMinute, false);


            timePickerDialog.show();
        } else if (v.getId() == R.id.edAddMeeting_Village) {
            showGaonDialog();
        } else if (v.getId() == R.id.edAddMeeting_Taluka) {
            showTalukaDialog();
        }

    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month + 1;
            mDay = dayOfMonth;
            edDate.setText(mDay + "-" + mMonth + "-" + mYear);

            Calendar calendar = Calendar.getInstance();
            calendar.set(mYear, mMonth - 1, mDay);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            dateMillis = calendar.getTimeInMillis();

            Date parsed = null;
            String dt = mDay + "-" + mMonth + "-" + mYear;
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                //utilDate = format.format(format.parse(dt));
                Log.e("UTIL DATE : ", "--- " + format.format(format.parse(dt)));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }
    };


    public void addNewEvent(EventData eventData) {

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
            Call<EventData> eventDataCall = api.addEvent(eventData);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.show();


            eventDataCall.enqueue(new Callback<EventData>() {
                @Override
                public void onResponse(Call<EventData> call, retrofit2.Response<EventData> response) {
                    try {
                        if (response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_main, new MeetingsFragment(), "HomeFragment");
                            ft.commit();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Add Event", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Add Event", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<EventData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Add Event", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
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

                                regionNameArray.clear();
                                regionIdArray.clear();
                                regionNameArray.add("Select Region");
                                regionIdArray.add(0);
                                for (int i = 0; i < data.getRegionList().size(); i++) {
                                    regionNameArray.add(data.getRegionList().get(i).getRegName());
                                    regionIdArray.add(data.getRegionList().get(i).getRegId());
                                }

                                ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, regionNameArray);
                                spRegion.setAdapter(regionAdapter);

                                int pos = 0;
                                for (int i = 0; i < regionIdArray.size(); i++) {
                                    if (regionId == regionIdArray.get(i)) {
                                        pos = i;
                                    }
                                }

                                spRegion.setSelection(pos);


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
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDistrictData(int regId) {
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
            Call<DistrictData> districtDataCall = api.getDistrictByRegion(regId);

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
                                // Toast.makeText(getActivity(), "No District Found", Toast.LENGTH_SHORT).show();
                                districtNameArray.clear();
                                districtIdArray.clear();
                                districtNameArray.add("Select District");
                                districtIdArray.add(0);
                                ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, districtNameArray);
                                spDistrict.setAdapter(distAdapter);

                            } else {
                                progressDialog.dismiss();

                                districtNameArray.clear();
                                districtIdArray.clear();
                                districtNameArray.add("Select District");
                                districtIdArray.add(0);
                                for (int i = 0; i < data.getDistrictList().size(); i++) {
                                    districtNameArray.add(data.getDistrictList().get(i).getDistName());
                                    districtIdArray.add(data.getDistrictList().get(i).getDistId());
                                }
                                ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, districtNameArray);
                                spDistrict.setAdapter(distAdapter);

                                int pos = 0;
                                for (int i = 0; i < districtIdArray.size(); i++) {
                                    if (distId == districtIdArray.get(i)) {
                                        pos = i;
                                    }
                                }
                                spDistrict.setSelection(pos);

                            }
                        } else {
                            progressDialog.dismiss();
                            //Toast.makeText(getActivity(), "No District Found", Toast.LENGTH_SHORT).show();

                            districtNameArray.clear();
                            districtIdArray.clear();
                            districtNameArray.add("Select District");
                            districtIdArray.add(0);
                            ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, districtNameArray);
                            spDistrict.setAdapter(distAdapter);
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //Toast.makeText(getActivity(), "No District Found", Toast.LENGTH_SHORT).show();

                        districtNameArray.clear();
                        districtIdArray.clear();
                        districtNameArray.add("Select District");
                        districtIdArray.add(0);
                        ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, districtNameArray);
                        spDistrict.setAdapter(distAdapter);
                    }
                }

                @Override
                public void onFailure(Call<DistrictData> call, Throwable t) {
                    progressDialog.dismiss();
                    districtNameArray.clear();
                    districtIdArray.clear();
                    districtNameArray.add("Select District");
                    districtIdArray.add(0);
                    ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, districtNameArray);
                    spDistrict.setAdapter(distAdapter);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getTalukaData(int distId) {
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
            Call<TalukaData> talukaDataCall = api.getTalukaByDistrict(distId);

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
                                // Toast.makeText(getActivity(), "No Taluka Found", Toast.LENGTH_SHORT).show();
                                talukaNameArray.clear();
                                talukaIdArray.clear();
                                talukaNameArray.add("Select Taluka");
                                talukaIdArray.add(0);
                                ArrayAdapter<String> talAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, talukaNameArray);
                                spTaluka.setAdapter(talAdpt);
                            } else {
                                progressDialog.dismiss();

                                talukaNameArray.clear();
                                talukaIdArray.clear();
                                talukaNameArray.add("Select Taluka");
                                talukaIdArray.add(0);
                                for (int i = 0; i < data.getTalukaList().size(); i++) {
                                    talukaNameArray.add(data.getTalukaList().get(i).getTalName());
                                    talukaIdArray.add(data.getTalukaList().get(i).getTalId());
                                }
                                ArrayAdapter<String> talAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, talukaNameArray);
                                spTaluka.setAdapter(talAdpt);

                                int pos = 0;
                                for (int i = 0; i < talukaIdArray.size(); i++) {
                                    if (talId == talukaIdArray.get(i)) {
                                        pos = i;
                                    }
                                }
                                spTaluka.setSelection(pos);

                            }
                        } else {
                            progressDialog.dismiss();
                            //  Toast.makeText(getActivity(), "No Taluka Found", Toast.LENGTH_SHORT).show();
                            talukaNameArray.clear();
                            talukaIdArray.clear();
                            talukaNameArray.add("Select Taluka");
                            talukaIdArray.add(0);
                            ArrayAdapter<String> talAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, talukaNameArray);
                            spTaluka.setAdapter(talAdpt);
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //  Toast.makeText(getActivity(), "No Taluka Found", Toast.LENGTH_SHORT).show();
                        talukaNameArray.clear();
                        talukaIdArray.clear();
                        talukaNameArray.add("Select Taluka");
                        talukaIdArray.add(0);
                        ArrayAdapter<String> talAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, talukaNameArray);
                        spTaluka.setAdapter(talAdpt);
                    }
                }

                @Override
                public void onFailure(Call<TalukaData> call, Throwable t) {
                    progressDialog.dismiss();
                    talukaNameArray.clear();
                    talukaIdArray.clear();
                    talukaNameArray.add("Select Taluka");
                    talukaIdArray.add(0);
                    ArrayAdapter<String> talAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, talukaNameArray);
                    spTaluka.setAdapter(talAdpt);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void getGaonData(int talId) {
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
            Call<GaonData> gaonDataCall = api.getGaonByTaluka(talId);

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

    public void getEvent(int event_Id) {
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
            Call<EventListData> eventListDataCall = api.getEventById(event_Id);

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
                                // Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                                //Log.e("onError : ", "----" + data.getInfo().getMessage());
                            } else {
                                progressDialog.dismiss();


                                for (int i = 0; i < data.getEventList().size(); i++) {
                                    eventArray.add(data.getEventList().get(i));
                                }
                                if (eventArray.size() > 0) {
                                    setData(eventArray.get(0));
                                }

                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                            //Log.e("data : ", "----Null");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        //Log.e("Exception : ", "----" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<EventListData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "No Meetings Found", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void setData(EventList eventList) {
        edEventName.setText("" + eventList.getEveName());
        edDate.setText("" + eventList.getEveDate());
        //  edTime.setText("" + eventList.getEveTime());
        tvTime.setText("" + eventList.getEveTime());

        String hours = eventList.getEveTime().substring(0, 2);
        String minute = eventList.getEveTime().substring(3, 5);
        int hr = 0, mn = 0;
        try {
            hr = Integer.parseInt(hours);
            mn = Integer.parseInt(minute);
        } catch (Exception e) {

        }


        String amPm = hr % 12 + ":" + mn + " " + ((hr >= 12) ? "PM" : "AM");
        edTime.setText("" + amPm);


        if (eventList.getEveType() == 0) {
            spEventType.setSelection(1);
        } else if (eventList.getEveType() == 1) {
            spEventType.setSelection(2);
        } else if (eventList.getEveType() == 2) {
            spEventType.setSelection(3);
        } else if (eventList.getEveType() == 3) {
            spEventType.setSelection(4);
        }

        regionId = eventList.getRegId();
        distId = eventList.getDistId();
        talId = eventList.getTalId();
        villageId = eventList.getGaonId();

        tvRegionId.setText("" + eventList.getRegId());
        tvDistrictId.setText("" + eventList.getDistId());
        tvTalukaId.setText("" + eventList.getTalId());
        tvVillageId.setText("" + eventList.getGaonId());

        int regPos = 0;
        for (int i = 0; i < regionIdArray.size(); i++) {
            if (regionId == regionIdArray.get(i)) {
                regPos = i;
            }
        }
        spRegion.setSelection(regPos);

        int distPos = 0;
        for (int i = 0; i < districtIdArray.size(); i++) {
            if (distId == districtIdArray.get(i)) {
                distPos = i;
            }
        }
        spDistrict.setSelection(distPos);

        String talName = "";
        if (talukaArray.size() > 0) {
            for (int i = 0; i < talukaArray.size(); i++) {
                if (talId == talukaArray.get(i).getTalId()) {
                    talName = talukaArray.get(i).getTalName();
                }
            }
        }
        edTaluka.setText("" + talName);

        String gaonName = "";
        if (gaonArray.size() > 0) {
            for (int i = 0; i < gaonArray.size(); i++) {
                if (villageId == gaonArray.get(i).getGaonId()) {
                    gaonName = gaonArray.get(i).getGaonName();
                }
            }
        }
        edGaon.setText("" + gaonName);


        edPlace.setText("" + eventList.getEvePlace());
        edAppxAtt.setText("" + eventList.getEveApproxAttendance());
        edOrgName.setText("" + eventList.getEveOrgName());
        edOrgMobile.setText("" + eventList.getEveOrgMobile());
        edOrgMobile2.setText("" + eventList.getEveOrgMobile2());
        edRemark.setText("" + eventList.getEveRemarks());

    }

    public void getAccessData() {
        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();

        String jsonRegion = pref.getString("Region", "");
        Region region = gson.fromJson(jsonRegion, Region.class);
        Log.e("Region : ", "---------------" + region);

        regionNameArray.clear();
        regionIdArray.clear();
        if (region != null) {
            regionNameArray.add("" + region.getRegName());
            regionIdArray.add(region.getRegId());
            ArrayAdapter<String> regAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, regionNameArray);
            spRegion.setAdapter(regAdapter);
        }

        String jsonDist = pref.getString("District", "");
        District district = gson.fromJson(jsonDist, District.class);
        Log.e("District : ", "---------------" + district);

        districtNameArray.clear();
        districtIdArray.clear();
        distRegIdArray.clear();
        if (district != null) {
            districtNameArray.add("" + district.getDistName());
            districtIdArray.add(district.getDistId());
            distRegIdArray.add(district.getRegId());
            ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, districtNameArray);
            spDistrict.setAdapter(distAdapter);

        }

        talukaArray.clear();
        String jsonTal = pref.getString("Taluka", "");
        Type type = new TypeToken<ArrayList<TalukaList>>() {
        }.getType();
        talukaArray = gson.fromJson(jsonTal, type);
        Log.e("Taluka : ", "---------------" + talukaArray);

        gaonArray.clear();
        String jsonGaon = pref.getString("Gaon", "");
        Type type1 = new TypeToken<ArrayList<GaonList>>() {
        }.getType();
        gaonArray = gson.fromJson(jsonGaon, type1);
        Log.e("Gaon : ", "---------------" + gaonArray);
    }

    public void getAdminAccessData() {
        try {
            SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
            Gson gson = new Gson();

            String jsonReg = pref.getString("RegionArray", "");
            Type typeReg = new TypeToken<ArrayList<RegionList>>() {
            }.getType();
            ArrayList<RegionList> regionListArray = gson.fromJson(jsonReg, typeReg);
            Log.e("All REGIONS : ", "---------------" + regionListArray);

            regionNameArray.clear();
            regionIdArray.clear();
            if (regionListArray.size() > 0) {
                for (int i = 0; i < regionListArray.size(); i++) {
                    regionNameArray.add(regionListArray.get(i).getRegName());
                    regionIdArray.add(regionListArray.get(i).getRegId());
                }
                ArrayAdapter<String> regAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, regionNameArray);
                spRegion.setAdapter(regAdapter);

            }

            String jsonDist = pref.getString("DistrictArray", "");
            Type typeDist = new TypeToken<ArrayList<DistrictList>>() {
            }.getType();
            ArrayList<DistrictList> districtListArray = gson.fromJson(jsonDist, typeDist);

            districtNameArray.clear();
            districtIdArray.clear();
            if (districtListArray.size() > 0) {
                for (int i = 0; i < districtListArray.size(); i++) {
                    districtNameArray.add(districtListArray.get(i).getDistName());
                    districtIdArray.add(districtListArray.get(i).getDistId());
                }
                ArrayAdapter<String> distAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, districtNameArray);
                spDistrict.setAdapter(distAdapter);

            }

            talukaArray.clear();
            String jsonTal = pref.getString("TalukaArray", "");
            Type type = new TypeToken<ArrayList<TalukaList>>() {
            }.getType();
            talukaArray = gson.fromJson(jsonTal, type);
            Log.e("Taluka : ", "---------------" + talukaArray);

            gaonArray.clear();
            String jsonGaon = pref.getString("GaonArray", "");
            Type type1 = new TypeToken<ArrayList<GaonList>>() {
            }.getType();
            gaonArray = gson.fromJson(jsonGaon, type1);
            Log.e("Gaon : ", "---------------" + gaonArray);
        } catch (Exception e) {
        }
    }


    public void showGaonDialog() {

        if (tvTalukaId.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Please Select Taluka First", Toast.LENGTH_SHORT).show();
            edTaluka.requestFocus();
        } else {
            dialog = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = li.inflate(R.layout.search_dialog_layout, null, false);
            dialog.setContentView(v);
            dialog.setCancelable(true);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            int taluka_Id = 0;
            try {
                taluka_Id = Integer.parseInt(tvTalukaId.getText().toString());
            } catch (Exception e) {
                ListView list1 = dialog.findViewById(R.id.lvSearchDialog_List);
                gaonAdapter = new GaonDataAdapter(getActivity().getApplicationContext(), gaonArray);
                list1.setAdapter(gaonAdapter);

            }

            ArrayList<GaonList> filterGaonArray = new ArrayList<>();
            if (gaonArray.size() > 0) {
                for (int i = 0; i < gaonArray.size(); i++) {
                    if (taluka_Id == gaonArray.get(i).getTalId()) {
                        filterGaonArray.add(gaonArray.get(i));
                    }
                }
            }

            ListView list1 = dialog.findViewById(R.id.lvSearchDialog_List);
            gaonAdapter = new GaonDataAdapter(getActivity().getApplicationContext(), filterGaonArray);
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
                    edGaon.setText("" + displayedValues.get(position).getGaonName());
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

    public void showTalukaDialog() {

        dialogTaluka = new Dialog(getContext(), android.R.style.Theme_Light_NoTitleBar);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.search_dialog_layout, null, false);
        dialogTaluka.setContentView(v);
        dialogTaluka.setCancelable(true);
        dialogTaluka.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        int district_id = 0;
        try {
            district_id = Integer.parseInt(tvDistrictId.getText().toString());
        } catch (Exception e) {
            ListView list1 = dialogTaluka.findViewById(R.id.lvSearchDialog_List);
            talukaAdapter = new TalukaDataAdapter(getActivity().getApplicationContext(), talukaArray);
            list1.setAdapter(talukaAdapter);

        }

        ArrayList<TalukaList> filterTalArray = new ArrayList<>();
        if (talukaArray.size() > 0) {
            for (int i = 0; i < talukaArray.size(); i++) {
                if (district_id == talukaArray.get(i).getDistId()) {
                    filterTalArray.add(talukaArray.get(i));
                }
            }
        }
        ListView list1 = dialogTaluka.findViewById(R.id.lvSearchDialog_List);
        talukaAdapter = new TalukaDataAdapter(getActivity().getApplicationContext(), filterTalArray);
        list1.setAdapter(talukaAdapter);

        EditText edSearch = dialogTaluka.findViewById(R.id.edSearchDialog_Search);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (talukaAdapter != null)
                    talukaAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialogTaluka.show();
    }

    public class TalukaDataAdapter extends BaseAdapter implements Filterable {

        private ArrayList<TalukaList> originalValues;
        private ArrayList<TalukaList> displayedValues;
        LayoutInflater inflater;

        public TalukaDataAdapter(Context context, ArrayList<TalukaList> arrayList) {
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
            TalukaDataAdapter.ViewHolder holder = null;

            if (v == null) {
                v = inflater.inflate(R.layout.custom_search_dialog_layout, null);
                holder = new TalukaDataAdapter.ViewHolder();
                holder.tvName = v.findViewById(R.id.tvCustomSearch_Name);
                holder.llBack = v.findViewById(R.id.llCustomSearch_back);
                v.setTag(holder);
            } else {
                holder = (TalukaDataAdapter.ViewHolder) v.getTag();
            }

            holder.tvName.setText("" + displayedValues.get(position).getTalName());

            holder.llBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogTaluka.dismiss();
                    edTaluka.setText("" + displayedValues.get(position).getTalName());
                    tvTalukaId.setText("" + displayedValues.get(position).getTalId());
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
                    ArrayList<TalukaList> filteredArrayList = new ArrayList<>();
                    if (originalValues == null) {
                        originalValues = new ArrayList<TalukaList>(displayedValues);
                    }

                    if (charSequence == null || charSequence.length() == 0) {
                        results.count = originalValues.size();
                        results.values = originalValues;
                    } else {
                        charSequence = charSequence.toString().toLowerCase();
                        for (int i = 0; i < originalValues.size(); i++) {
                            String name = originalValues.get(i).getTalName();
                            if (name.toLowerCase().startsWith(charSequence.toString())) {
                                filteredArrayList.add(new TalukaList(originalValues.get(i).getTalId(), originalValues.get(i).getDistId(), originalValues.get(i).getRegId(), originalValues.get(i).getTalName(), originalValues.get(i).getTalDistNsk(), originalValues.get(i).getTalDistDist(), originalValues.get(i).getTalDistReg(), originalValues.get(i).getTalRemarks(), originalValues.get(i).getIsUsed()));
                            }
                        }
                        results.count = filteredArrayList.size();
                        results.values = filteredArrayList;
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    displayedValues = (ArrayList<TalukaList>) filterResults.values;
                    notifyDataSetChanged();
                }
            };

            return filter;
        }
    }

}
