package com.ats.royalagro.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.District;
import com.ats.royalagro.bean.GaonList;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.bean.Region;
import com.ats.royalagro.bean.TalukaList;
import com.ats.royalagro.bean.UserAccessData;
import com.ats.royalagro.fragment.AddMeetingsFragment;
import com.ats.royalagro.fragment.AddSocietyFragment;
import com.ats.royalagro.fragment.BankDetailsFragment;
import com.ats.royalagro.fragment.CropDetailsFragment;
import com.ats.royalagro.fragment.CropListFragment;
import com.ats.royalagro.fragment.DetailProfileFormFragment;
import com.ats.royalagro.fragment.FarmersListFragment;
import com.ats.royalagro.fragment.HomeFragment;
import com.ats.royalagro.fragment.KYCDetailsFragment;
import com.ats.royalagro.fragment.KYCListFragment;
import com.ats.royalagro.fragment.MeetingsFragment;
import com.ats.royalagro.fragment.PersonalDetailsFragment;
import com.ats.royalagro.fragment.PlotDetailsFragment;
import com.ats.royalagro.fragment.PlotListFragment;
import com.ats.royalagro.fragment.ProfilePhotoFragment;
import com.ats.royalagro.fragment.SocietyFragment;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.PermissionUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private TextView tvMenuHome, tvMenuMeetings, tvMenuSociety, tvMenuLogout;
    int empId, empType;
    String empName, empMobile, empEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvMenuHome = findViewById(R.id.tvMenuHome);
        tvMenuMeetings = findViewById(R.id.tvMenuMeetings);
        tvMenuSociety = findViewById(R.id.tvMenuSociety);
        tvMenuLogout = findViewById(R.id.tvMenuLogout);
        tvMenuSociety.setVisibility(View.GONE);

        tvMenuHome.setOnClickListener(this);
        tvMenuMeetings.setOnClickListener(this);
        tvMenuSociety.setOnClickListener(this);
        tvMenuLogout.setOnClickListener(this);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
            empName = loginBean.getEmpName();
            empEmail = loginBean.getEmpEmail();
            empMobile = loginBean.getEmpMobile1();
            empType = loginBean.getEmpType();

        } else {
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }

       // if (empType == 0) {
            tvMenuLogout.setVisibility(View.VISIBLE);
        //}


        if (PermissionUtil.checkAndRequestPermissions(HomeActivity.this)) {

        }

        getEmpAccessData(empId);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView tvName = navigationView.findViewById(R.id.tvNavHead_Name);
        TextView tvEmail = navigationView.findViewById(R.id.tvNavHead_Email);
        tvName.setText("" + empName);
        tvEmail.setText("" + empEmail);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, new HomeFragment(), "Home");
            transaction.commit();
        }


    }

    @Override
    public void onBackPressed() {

        Fragment home = getSupportFragmentManager().findFragmentByTag("Home");
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
        Fragment meetingMaster = getSupportFragmentManager().findFragmentByTag("MeetingMaster");
        Fragment societyMaster = getSupportFragmentManager().findFragmentByTag("SocietyMaster");
        Fragment profileMaster = getSupportFragmentManager().findFragmentByTag("ProfileMaster");
        Fragment kycMaster = getSupportFragmentManager().findFragmentByTag("KYCMaster");
        Fragment plotMaster = getSupportFragmentManager().findFragmentByTag("PlotMaster");
        Fragment cropMaster = getSupportFragmentManager().findFragmentByTag("CropMaster");
        Fragment farmerMaster = getSupportFragmentManager().findFragmentByTag("FarmerMaster");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (home instanceof HomeFragment && home.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Confirm Action");
            builder.setMessage("Do You Really Want To Exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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
        } else if (homeFragment instanceof MeetingsFragment && homeFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, new HomeFragment(), "HomeFragment");
            ft.commit();
        } else if (meetingMaster instanceof AddMeetingsFragment && meetingMaster.isVisible() ||
                meetingMaster instanceof SocietyFragment && meetingMaster.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, new MeetingsFragment(), "HomeFragment");
            ft.commit();
        } else if (societyMaster instanceof AddSocietyFragment && societyMaster.isVisible() ||
                societyMaster instanceof FarmersListFragment && societyMaster.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, new SocietyFragment(), "MeetingMaster");
            ft.commit();
        } else if (farmerMaster instanceof DetailProfileFormFragment && farmerMaster.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, new FarmersListFragment(), "SocietyMaster");
            ft.commit();
        } else if (profileMaster instanceof PersonalDetailsFragment && profileMaster.isVisible() ||
                profileMaster instanceof ProfilePhotoFragment && profileMaster.isVisible() ||
                profileMaster instanceof BankDetailsFragment && profileMaster.isVisible() ||
                profileMaster instanceof KYCListFragment && profileMaster.isVisible() ||
                profileMaster instanceof PlotListFragment && profileMaster.isVisible() ||
                profileMaster instanceof CropListFragment && profileMaster.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, new DetailProfileFormFragment(), "FarmerMaster");
            ft.commit();
        } else if (kycMaster instanceof KYCDetailsFragment && kycMaster.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, new KYCListFragment(), "ProfileMaster");
            ft.commit();
        } else if (plotMaster instanceof PlotDetailsFragment && plotMaster.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, new PlotListFragment(), "ProfileMaster");
            ft.commit();
        } else if (cropMaster instanceof CropDetailsFragment && cropMaster.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, new CropListFragment(), "ProfileMaster");
            ft.commit();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvMenuHome) {
            Fragment fragment = new HomeFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment, "Home");
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.tvMenuMeetings) {
            Fragment fragment = new MeetingsFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment, "HomeFragment");
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.tvMenuSociety) {
            Fragment fragment = new SocietyFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment, "HomeFragment");
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (v.getId() == R.id.tvMenuLogout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Logout");
            builder.setMessage("Are You Sure You Want To Logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
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
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    public void getEmpAccessData(int emp_id) {

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {

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

            final android.app.AlertDialog progressDialog = new SpotsDialog(HomeActivity.this);
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

                                SharedPreferences pref = getApplicationContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
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
                            Log.e("HOME_ACTIVITY : ", "---- No Access Data");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HOME_ACTIVITY : ", "----  Access Data -- Exception : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UserAccessData> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }


}
