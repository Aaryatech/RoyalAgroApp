package com.ats.royalagro.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ats.royalagro.R;
import com.ats.royalagro.fragment.BankDetailsFragment;
import com.ats.royalagro.fragment.CropDetailsFragment;
import com.ats.royalagro.fragment.KYCDetailsFragment;
import com.ats.royalagro.fragment.PersonalDetailsFragment;
import com.ats.royalagro.fragment.PlotDetailsFragment;

public class DetailRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    public static TextView tvPersonal, tvAccount, tvCrop, tvPlot, tvKYC;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_registration);

        tvPersonal = findViewById(R.id.tvDetailReg_Personal);
        tvAccount = findViewById(R.id.tvDetailReg_Account);
        tvCrop = findViewById(R.id.tvDetailReg_Crop);
        tvPlot = findViewById(R.id.tvDetailReg_Plot);
        tvKYC = findViewById(R.id.tvDetailReg_KYC);

        tvPersonal.setOnClickListener(this);
        tvAccount.setOnClickListener(this);
        tvCrop.setOnClickListener(this);
        tvPlot.setOnClickListener(this);
        tvKYC.setOnClickListener(this);

        frameLayout = (FrameLayout) findViewById(R.id.frame_Registration);

        Fragment adf = new PersonalDetailsFragment();
        Bundle args = new Bundle();
        args.putString("Emp_Id", "1");
        args.putString("Action", "0");
        adf.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_Registration, adf).commit();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvDetailReg_Personal) {
            Fragment adf = new PersonalDetailsFragment();
            Bundle args = new Bundle();
            args.putString("Emp_Id", "1");
            args.putString("Action", "0");
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Registration, adf).commit();
        } else if (v.getId() == R.id.tvDetailReg_Account) {
            Fragment adf = new BankDetailsFragment();
            Bundle args = new Bundle();
            args.putString("Emp_Id", "1");
            args.putString("Action", "0");
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Registration, adf).commit();
        } else if (v.getId() == R.id.tvDetailReg_Crop) {
            Fragment adf = new CropDetailsFragment();
            Bundle args = new Bundle();
            args.putString("Emp_Id", "1");
            args.putString("Action", "0");
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Registration, adf).commit();
        } else if (v.getId() == R.id.tvDetailReg_Plot) {
            Fragment adf = new PlotDetailsFragment();
            Bundle args = new Bundle();
            args.putString("Emp_Id", "1");
            args.putString("Action", "0");
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Registration, adf).commit();
        } else if (v.getId() == R.id.tvDetailReg_KYC) {
            Fragment adf = new KYCDetailsFragment();
            Bundle args = new Bundle();
            args.putString("Emp_Id", "1");
            args.putString("Action", "0");
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_Registration, adf).commit();
        }
    }
}
