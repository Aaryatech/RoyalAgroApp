package com.ats.royalagro.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.activity.HomeActivity;
import com.ats.royalagro.bean.FarmerKycList;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.PermissionUtil;
import com.ats.royalagro.util.RealPathUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class KYCDetailsFragment extends Fragment implements View.OnClickListener {

    private EditText edKYCNo, edRemark;
    private Spinner spKYCType;
    private Button btnSave, btnReset, btnPhoto;
    private ImageView ivPhoto;

    public static String path, imagePath;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "RoyalAgro");
    File f;

    int fId = 0, empId, kycId = 0, isVerified = 0;
    String kyc_pic = "";

    android.app.AlertDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kycdetails, container, false);
        getActivity().setTitle("Add KYC Document");

        createFolder();

        if (PermissionUtil.checkAndRequestPermissions(getActivity())) {

        }

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
        }

        fId = getArguments().getInt("FarmerId");

        String bean = getArguments().getString("Bean");
        FarmerKycList kycBean = gson.fromJson(bean, FarmerKycList.class);

        edKYCNo = view.findViewById(R.id.edKyc_Number);
        edRemark = view.findViewById(R.id.edKyc_Remark);

        spKYCType = view.findViewById(R.id.spKyc_Type);
        ivPhoto = view.findViewById(R.id.ivKyc_Photo);

        btnSave = view.findViewById(R.id.btnKyc_Save);
        btnReset = view.findViewById(R.id.btnKyc_Reset);
        btnPhoto = view.findViewById(R.id.btnKyc_Photo);

        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);

        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Select");
        spinnerArray.add("Aadhar Card");
        spinnerArray.add("Election Card");
        spinnerArray.add("PAN");
        spinnerArray.add("Passport");
        spinnerArray.add("Driving Lic");
        spinnerArray.add("Bank Passbook");
        spinnerArray.add("7-12");

        ArrayAdapter<String> spinnerAdpt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spKYCType.setAdapter(spinnerAdpt);


        if (kycBean != null) {
            kycId = kycBean.getKycId();
            edKYCNo.setText("" + kycBean.getKycNo());
            edRemark.setText("" + kycBean.getKycRemarks());
            kyc_pic = kycBean.getKycPhoto();
            int kycType = kycBean.getKycType();
            spKYCType.setSelection(kycType + 1);
            isVerified = kycBean.getKycIsVerified();

            String image = ApiInterface.KYC_PATH + "" + kycBean.getKycPhoto();
            if (kycBean.getKycPhoto().isEmpty()) {
                Log.e("Image : ", " -- Empty");
                ivPhoto.setImageResource(android.R.color.transparent);
            } else {
                Log.e("Image : ", " -- NOt Empty");
                try {
                    Picasso.with(getContext())
                            .load("photo")
                            .placeholder(R.drawable.kyc_default)
                            .error(R.drawable.kyc_default)
                            .into(ivPhoto);
                } catch (Exception e) {
                }
            }


        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnKyc_Save) {
            if (spKYCType.getSelectedItemPosition() == 0) {
                Toast.makeText(getActivity(), "Please Select KYC Type", Toast.LENGTH_SHORT).show();
                spKYCType.requestFocus();
            } else if (edKYCNo.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter KYC Number", Toast.LENGTH_SHORT).show();
                edKYCNo.requestFocus();
            }
            /*else if (edRemark.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Remark", Toast.LENGTH_SHORT).show();
                edRemark.requestFocus();
            }*/
            else {

                progressDialog = new SpotsDialog(getContext());
                progressDialog.show();

                int type = spKYCType.getSelectedItemPosition() - 1;
                String kycNO = edKYCNo.getText().toString();
                String remark = edRemark.getText().toString();

                if (imagePath == null) {
                    imagePath = "";
                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = new java.util.Date();
                String dateTime = dateFormat.format(date);

                FarmerKycList farmerKycList = new FarmerKycList(kycId, fId, type, kycNO, kyc_pic, remark, isVerified, empId, dateTime, 1);

                if (imagePath.isEmpty()) {
                    addKYCData(farmerKycList);
                } else {
                    if (!kyc_pic.isEmpty()) {
                        farmerKycList.setKycPhoto(kyc_pic);
                        sendImage(kyc_pic, "2", farmerKycList);
                    } else {
                        File imgFile = new File(imagePath);
                        int pos = imgFile.getName().lastIndexOf(".");
                        String ext = imgFile.getName().substring(pos + 1);
                        kyc_pic = fId + "_" + System.currentTimeMillis() + "." + ext;
                        farmerKycList.setKycPhoto(kyc_pic);
                        sendImage(kyc_pic, "2", farmerKycList);
                    }
                }
            }


        } else if (v.getId() == R.id.btnKyc_Reset) {
            spKYCType.setSelection(0);
            edRemark.setText("");
            edKYCNo.setText("");
            imagePath = null;

        } else if (v.getId() == R.id.btnKyc_Photo) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            builder.setTitle("Choose");
            builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent pictureActionIntent = null;
                    pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pictureActionIntent, 101);
                }
            });
            builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        //  Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "Camera.jpg");

                            String authorities = com.ats.royalagro.BuildConfig.APPLICATION_ID + ".provider";
                            Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, f);

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 102);

                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "Camera.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 102);

                        }
                    } catch (Exception e) {
                        //Log.e("select camera : ", " Exception : " + e.getMessage());
                    }
                }
            });
            builder.show();
        }
    }


    //--------------------------IMAGE-----------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String realPath;
        Bitmap bitmap = null;

        if (resultCode == getActivity().RESULT_OK && requestCode == 102) {
            try {
                Uri uriFromPath = Uri.fromFile(f);
                //Log.e("FILE : ", "-----------" + f.getAbsolutePath());
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //Log.e("BITMAP : ", "-----------" + myBitmap);
                    ivPhoto.setImageBitmap(myBitmap);
                }

                imagePath = f.getAbsolutePath();
                //Log.e("Camera : Path : ", " ---- " + imagePath);

            } catch (Exception e) {
                e.printStackTrace();
                //Log.e("In Camera ", "-Exc : " + e.getMessage());
            }

        } else if (resultCode == getActivity().RESULT_OK && requestCode == 101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), data.getData());

                Uri uriFromPath = Uri.fromFile(new File(realPath));

                //Log.e("PATH : ", "-------" + uriFromPath);

                HomeActivity regActivity = (HomeActivity) getActivity();
                bitmap = getBitmapFromCameraData(data, regActivity);

                ivPhoto.setImageBitmap(bitmap);

                imagePath = uriFromPath.getPath();
                //Log.e("Gallery : Path : ", " ---- " + imagePath);

                // sendImage();
            } catch (Exception e) {
            }

        }

    }

    public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

        String picturePath = cursor.getString(columnIndex);
        path = picturePath;
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void addKYCData(FarmerKycList farmerKycList) {
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
            Call<FarmerKycList> farmerKycListCall = api.addFarmerKYC(farmerKycList);

//            final android.app.AlertDialog progressDialog = new SpotsDialog(getContext());
//            progressDialog.show();


            farmerKycListCall.enqueue(new Callback<FarmerKycList>() {
                @Override
                public void onResponse(Call<FarmerKycList> call, retrofit2.Response<FarmerKycList> response) {
                    try {
                        if (response.body() != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            Fragment adf = new KYCListFragment();
                            Bundle args = new Bundle();
                            args.putInt("FarmerId", fId);
                            adf.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();
                        } else {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }

                            Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FarmerKycList> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImage(String filename, String type, final FarmerKycList farmerKycList) {
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

            Log.e("IMAGE PATH : ", "-------- : " + imagePath);

            File imgFile = new File(imagePath);

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imgFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);

            RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), type);

            RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), filename);

            Call<JSONObject> call = api.fileUpload(body, imgType, imgName);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    addKYCData(farmerKycList);
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
