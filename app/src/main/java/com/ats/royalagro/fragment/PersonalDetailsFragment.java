package com.ats.royalagro.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.activity.HomeActivity;
import com.ats.royalagro.bean.FarmerHeaderData;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.bean.SocDataList;
import com.ats.royalagro.bean.TempSocietyListData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.PermissionUtil;
import com.ats.royalagro.util.RealPathUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class PersonalDetailsFragment extends Fragment implements View.OnClickListener {

    private EditText edFName, edMName, edLName, edDOB, edFarmName, edMobile1, edMobile2, edLandline, edEmail, edAddress, edAtPost, edAreaTotal;
    private RadioButton rbMale, rbFemale, rbTransgender;
    private Button btnSave, btnReset, btnPhoto;
    private ImageView ivPhoto;

    int yyyy, mm, dd;
    long dobMillis;

    int fId, tempId, socId, empId;
    String tempData;
    SocDataList socDataList;

    public static String path, imagePath;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "RoyalAgro");
    File f;

    int fAccType = 0, fCurr_Status = 0, reg_doc_no = 0, f_reg = 0;
    String accName = "", ownerRel = "", aadhar = "", mob = "", bankName = "", branchName = "", ifsc = "", accNo = "", filedPic = "", profilePic = "", reg_dateTime = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);
        getActivity().setTitle("Land Owner Profile");

        createFolder();

        if (PermissionUtil.checkAndRequestPermissions(getActivity())) {

        }

        fId = getArguments().getInt("FarmerId");
        tempId = getArguments().getInt("TempId");
        socId = getArguments().getInt("SocietyId");
        tempData = getArguments().getString("TempSocData");


        //Log.e("Farmer Id : ", "--------" + fId);

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
        }

        socDataList = gson.fromJson(tempData, SocDataList.class);
        Log.e("Temp Soc Data : ", "---------------" + socDataList);


        edFName = view.findViewById(R.id.edPersonal_FirstName);
        edMName = view.findViewById(R.id.edPersonal_MiddleName);
        edLName = view.findViewById(R.id.edPersonal_LastName);
        edDOB = view.findViewById(R.id.edPersonal_DOB);
        edFarmName = view.findViewById(R.id.edPersonal_FarmName);
        edMobile1 = view.findViewById(R.id.edPersonal_Mobile1);
        edMobile2 = view.findViewById(R.id.edPersonal_Mobile2);
        edLandline = view.findViewById(R.id.edPersonal_Landline);
        edEmail = view.findViewById(R.id.edPersonal_Email);
        edAddress = view.findViewById(R.id.edPersonal_Address);
        edAtPost = view.findViewById(R.id.edPersonal_AtPost);
        edAreaTotal = view.findViewById(R.id.edPersonal_AreaTotal);

        btnPhoto = view.findViewById(R.id.btnPersonal_Image);
        ivPhoto = view.findViewById(R.id.ivPersonal_Image);

        rbMale = view.findViewById(R.id.rbMale);
        rbFemale = view.findViewById(R.id.rbFemale);
        rbTransgender = view.findViewById(R.id.rbTransgender);

        btnSave = view.findViewById(R.id.btnPersonal_Save);
        btnReset = view.findViewById(R.id.btnPersonal_Reset);

        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        edDOB.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);


        if (fId > 0) {
            getFarmerData(fId);
        } else {

            if (tempId > 0) {

                edFName.setText("" + socDataList.getFarmerFname());
                edMName.setText("" + socDataList.getFarmerMname());
                edLName.setText("" + socDataList.getFarmerLname());
                if (socDataList.getFarmerMobile() != null) {
                    edMobile1.setText("" + socDataList.getFarmerMobile());
                }
                if (socDataList.getFarmerMobile2() != null) {
                    edMobile2.setText("" + socDataList.getFarmerMobile2());
                }
                edAddress.setText("" + socDataList.getFarmerAddr());
                edAreaTotal.setText("" + socDataList.getFarmerAreaAcre());

            }


        }

        return view;
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnPersonal_Save) {

/*
            if (imagePath != null || imagePath.isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Profile Image", Toast.LENGTH_SHORT).show();
                btnPhoto.requestFocus();
            } else
*/

            if (edFName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
                edFName.requestFocus();
            } else if (edMName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Middle Name", Toast.LENGTH_SHORT).show();
                edMName.requestFocus();
            } else if (edLName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                edLName.requestFocus();
            } else if (edDOB.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Date Of Birth", Toast.LENGTH_SHORT).show();
                edDOB.requestFocus();
            } else if (!rbMale.isChecked() && !rbFemale.isChecked() && !rbTransgender.isChecked()) {
                Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                rbMale.requestFocus();
            } else if (edFarmName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Farm Name", Toast.LENGTH_SHORT).show();
                edFarmName.requestFocus();
            } else if (edMobile1.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile1.requestFocus();
            } else if (edMobile1.getText().toString().length() != 10) {
                Toast.makeText(getActivity(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile1.requestFocus();
            }
            /*else if (edMobile2.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Alternate Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile2.requestFocus();
            } else if (edMobile2.getText().toString().length() != 10) {
                Toast.makeText(getActivity(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile2.requestFocus();
            } else if (edLandline.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Landline Number", Toast.LENGTH_SHORT).show();
                edLandline.requestFocus();
            }*/
            else if (edAddress.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Address", Toast.LENGTH_SHORT).show();
                edAddress.requestFocus();
            } else if (edAtPost.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter At Post", Toast.LENGTH_SHORT).show();
                edAtPost.requestFocus();
            } else if (edAreaTotal.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Total Area In Acre", Toast.LENGTH_SHORT).show();
                edAreaTotal.requestFocus();
            } else {

                String fName = edFName.getText().toString();
                String mName = edMName.getText().toString();
                String lName = edLName.getText().toString();
                String dob = edDOB.getText().toString();
                int gen = 0;
                if (rbMale.isChecked()) {
                    gen = 0;
                } else if (rbFemale.isChecked()) {
                    gen = 1;
                }
                if (rbTransgender.isChecked()) {
                    gen = 2;
                }

                String farm = edFarmName.getText().toString();
                String mobile1 = edMobile1.getText().toString();
                String mobile2 = edMobile2.getText().toString();
                String landline = edLandline.getText().toString();
                String email = edEmail.getText().toString();
                String address = edAddress.getText().toString();
                String atPOst = edAtPost.getText().toString();
                String area = edAreaTotal.getText().toString();

                String fullName = fName + " " + mName + " " + lName;

              /*  int areaTotal = 0;
                try {
                    areaTotal = Integer.parseInt(area);
                } catch (Exception e) {
                }*/

                if (imagePath == null || imagePath.isEmpty()) {
                    imagePath = "";
                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = new java.util.Date();
                String dateTime = dateFormat.format(date);

                profilePic = tempId + "_Profile.jpg";
                filedPic = tempId + "_Field.jpg";

//                FarmerHeaderData data = new FarmerHeaderData(fId, tempId, socId, fName, mName, lName, fullName, gen, dob, farm, mobile1, mobile2, landline, email, address, atPOst, areaTotal, empId, dateTime, 1);
                FarmerHeaderData data = new FarmerHeaderData(fId, tempId, socId, fName, mName, lName, fullName, gen, dob, farm, mobile1, mobile2, landline, email, address, atPOst, area, fAccType, accName, ownerRel, aadhar, mob, email, bankName, branchName, ifsc, accNo, filedPic, fCurr_Status, reg_doc_no, reg_dateTime, f_reg, empId, dateTime, 1, profilePic);

                if (socDataList == null) {

                    Log.e("SOC DATA LIST", "--------------------------- NULL");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String todaysDate = sdf.format(Calendar.getInstance().getTimeInMillis());

                    socDataList = new SocDataList(0, socId, fName, mName, lName, address, "", "", "", mobile1, mobile2, area, "", empId, todaysDate, 1, empId, todaysDate, 1, "visited");

                    Log.e("SOC DATA : ", "--------------------------------" + socDataList);

                    addTempData(socDataList, data);
                   /* if (imagePath.isEmpty()) {
                        addTempData(socDataList, data);
                    } else {
                        if (!profilePic.isEmpty()) {
                            data.setfProfilePic(profilePic);
                            sendImageTempData(profilePic, "0", data, socDataList);
                        } else {
                            File imgFile = new File(imagePath);
                            int pos = imgFile.getName().lastIndexOf(".");
                            String ext = imgFile.getName().substring(pos + 1);
                            profilePic = tempId + "_" + System.currentTimeMillis() + "." + ext;
                            data.setfProfilePic(profilePic);
                            sendImageTempData(profilePic, "0", data, socDataList);
                        }
                    }*/

                } else {

                    socDataList.setFarmerFname(fName);
                    socDataList.setFarmerMname(mName);
                    socDataList.setFarmerLname(lName);
                    socDataList.setFarmerMobile(mobile1);
                    socDataList.setFarmerMobile2(mobile2);


                    addFarmer(data, socDataList);

                  /*  if (imagePath.isEmpty()) {
                        addFarmer(data, socDataList);
                    } else {
                        if (!profilePic.isEmpty()) {
                            data.setfProfilePic(profilePic);
                            sendImage(profilePic, "0", data, socDataList);
                        } else {
                            File imgFile = new File(imagePath);
                            int pos = imgFile.getName().lastIndexOf(".");
                            String ext = imgFile.getName().substring(pos + 1);
                            profilePic = tempId + "_" + System.currentTimeMillis() + "." + ext;
                            data.setfProfilePic(profilePic);
                            sendImage(profilePic, "0", data, socDataList);
                        }
                    }*/
                }


            }


        } else if (v.getId() == R.id.btnPersonal_Reset) {
            edFName.setText("");
            edMName.setText("");
            edLName.setText("");
            edDOB.setText("");
            edFarmName.setText("");
            edMobile1.setText("");
            edMobile2.setText("");
            edLandline.setText("");
            edEmail.setText("");
            edAddress.setText("");
            edAtPost.setText("");
            edAreaTotal.setText("");
            edFName.requestFocus();
        } else if (v.getId() == R.id.edPersonal_DOB) {
            Calendar purchaseCal = Calendar.getInstance();
            purchaseCal.setTimeInMillis(dobMillis);
            int yr = purchaseCal.get(Calendar.YEAR);
            int mn = purchaseCal.get(Calendar.MONTH);
            int dy = purchaseCal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), dobListener, yr, mn, dy);
            dialog.show();
        } else if (v.getId() == R.id.btnPersonal_Image) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
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
//                        String authorities = getContext().getPackageName() + ".fileprovider";
//                        Uri imageUri = FileProvider.getUriForFile(getContext(), authorities, f);

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
                        e.printStackTrace();
                        //Log.e("select camera : ", " Exception : " + e.getMessage());
                    }
                }
            });
            builder.show();
        }
    }

    private DatePickerDialog.OnDateSetListener dobListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            yyyy = year;
            mm = month + 1;
            dd = dayOfMonth;
            edDOB.setText(dd + "-" + mm + "-" + yyyy);

            Calendar calendar = Calendar.getInstance();
            calendar.set(yyyy, mm - 1, dd);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
            dobMillis = calendar.getTimeInMillis();
        }
    };

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

                    myBitmap = shrinkBitmap(imgFile.getAbsolutePath(), 720, 720);
                    //Log.e("Camera : ", "Shrink IMAGE SIZE : " + myBitmap.getByteCount());

                    try {
                        FileOutputStream out = new FileOutputStream(path);
                        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Log.e("Image Saved  ", "---------------");

                    } catch (Exception e) {
                        Log.e("Exception : ", "--------" + e.getMessage());
                        e.printStackTrace();
                    }
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
                try {

                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    //Log.e("Image Saved  ", "---------------");

                } catch (Exception e) {
                    // Log.e("Exception : ", "--------" + e.getMessage());
                    e.printStackTrace();
                }
                //Log.e("Gallery : Path : ", " ---- " + imagePath);

                // sendImage();
            } catch (Exception e) {
            }
        }
    }

    public static Bitmap shrinkBitmap(String file, int width, int height) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }

        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
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


    public void getFarmerData(int fId) {
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
            final Call<FarmerHeaderData> headerDataCall = api.getFarmerHeader(fId);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.show();

            headerDataCall.enqueue(new Callback<FarmerHeaderData>() {
                @Override
                public void onResponse(Call<FarmerHeaderData> call, retrofit2.Response<FarmerHeaderData> response) {
                    try {
                        if (response.body() != null) {
                            FarmerHeaderData data = response.body();
                            if (data.getError()) {
                                progressDialog.dismiss();
                                //Log.e("onError : ", "----" + data.getMessage());
                            } else {
                                progressDialog.dismiss();
                                Log.e("Header  : ", "----" + data);
                                setData(data);
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
                public void onFailure(Call<FarmerHeaderData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    //Log.e("onFailure : ", "----" + t.getMessage());
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }


    public void setData(FarmerHeaderData data) {
        edFName.setText("" + data.getfFname());
        edMName.setText("" + data.getfMname());
        edLName.setText("" + data.getfSname());
        edDOB.setText("" + data.getfDob());

        int gender = data.getfGender();
        if (gender == 0) {
            rbMale.setChecked(true);
        } else if (gender == 1) {
            rbFemale.setChecked(true);
        } else if (gender == 2) {
            rbTransgender.setChecked(true);
        }

        edFarmName.setText("" + data.getfFarmname());
        edMobile1.setText("" + data.getfMobile());
        edMobile2.setText("" + data.getfMobile2());
        edLandline.setText("" + data.getfLandline());
        edEmail.setText("" + data.getfEmail());
        edAddress.setText("" + data.getfAddress());
        edAtPost.setText("" + data.getfAtpost());
        edAreaTotal.setText("" + data.getfAreaTotal());

        profilePic = data.getfProfilePic();
        fAccType = data.getfAcctype();
        accName = data.getfAccname();
        ownerRel = data.getfOwnerRel();
        aadhar = data.getfAccAadhar();
        mob = data.getfOtherMobile();
        bankName = data.getfBankName();
        branchName = data.getfBankBranch();
        ifsc = data.getfIfsc();
        accNo = data.getfAccno();
        filedPic = data.getfFieldmapPic();
        fCurr_Status = data.getfCurrStatus();
        reg_doc_no = data.getfRegDocNo();
        reg_dateTime = data.getfRegDocDate();
        f_reg = data.getfReg();

        String image = ApiInterface.PROFILE_PATH + "" + data.getfProfilePic();
        try {
            Picasso.with(getContext())
                    .load(image)
                    .placeholder(R.drawable.img)
                    .error(R.drawable.img)
                    .into(ivPhoto);
        } catch (Exception e) {
        }

    }

    public void addFarmer(FarmerHeaderData farmerHeaderData, final SocDataList socData) {
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
            Call<FarmerHeaderData> headerDataCall = api.addFarmerHeaderData(farmerHeaderData);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.show();


            headerDataCall.enqueue(new Callback<FarmerHeaderData>() {
                @Override
                public void onResponse(Call<FarmerHeaderData> call, retrofit2.Response<FarmerHeaderData> response) {
                    try {
                        Log.e("Response : ----", "" + response.body());
                        if (response.body() != null) {
                            progressDialog.dismiss();

                            updateTempData(socData);

                            //                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
//                            Fragment adf = new BankDetailsFragment();
//                            Bundle args = new Bundle();
//                            args.putInt("FarmerId", fId);
//                            args.putInt("TempId", tempId);
//                            args.putInt("SocietyId", socId);
//                            adf.setArguments(args);
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();

//                            Fragment adf = new DetailProfileFormFragment();
//                            Bundle args = new Bundle();
//                            args.putInt("Emp_Id", empId);
//                            args.putInt("Temp_Id", tempId);
//                            args.putInt("SocietyId", socId);
//                            adf.setArguments(args);
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf).addToBackStack("FarmerMaster").commit();

                        } else {
                            Log.e("Error : ", "-- null");
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Exc : ", "--" + e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FarmerHeaderData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    Log.e("Error Fail: ", "--" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateTempData(final SocDataList socDataList) {
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

            ArrayList<SocDataList> socList = new ArrayList<>();
            socList.add(socDataList);

            Call<TempSocietyListData> headerDataCall = api.updateSocietyData(socList);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.show();


            headerDataCall.enqueue(new Callback<TempSocietyListData>() {
                @Override
                public void onResponse(Call<TempSocietyListData> call, retrofit2.Response<TempSocietyListData> response) {
                    try {
                        if (response.body() != null) {

                            TempSocietyListData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                Fragment adf = new DetailProfileFormFragment();
                                Bundle args = new Bundle();
                                args.putInt("Emp_Id", empId);
                                args.putInt("Temp_Id", tempId);
                                args.putInt("SocietyId", socId);
                                args.putString("TempSocData", tempData);
                                adf.setArguments(args);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "FarmerMaster").commit();

                            }
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
                public void onFailure(Call<TempSocietyListData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }


    public void addTempData(final SocDataList socDataList, final FarmerHeaderData farmerHeaderData) {
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

            ArrayList<SocDataList> socList = new ArrayList<>();
            socList.add(socDataList);

            Call<TempSocietyListData> headerDataCall = api.updateSocietyData(socList);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.show();


            headerDataCall.enqueue(new Callback<TempSocietyListData>() {
                @Override
                public void onResponse(Call<TempSocietyListData> call, retrofit2.Response<TempSocietyListData> response) {
                    try {
                        if (response.body() != null) {

                            TempSocietyListData data = response.body();
                            if (data.getInfo().getError()) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                                farmerHeaderData.setTempId(data.getSocDataList().get(0).getTempId());
                                farmerHeaderData.setfProfilePic(data.getSocDataList().get(0).getTempId() + "_Profile.jpg");
                                farmerHeaderData.setfFieldmapPic(data.getSocDataList().get(0).getTempId() + "_Field.jpg");
                                addFarmerFromTempData(farmerHeaderData);
                            }
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
                public void onFailure(Call<TempSocietyListData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void addFarmerFromTempData(final FarmerHeaderData farmerHeaderData) {
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
            Call<FarmerHeaderData> headerDataCall = api.addFarmerHeaderData(farmerHeaderData);

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.show();


            headerDataCall.enqueue(new Callback<FarmerHeaderData>() {
                @Override
                public void onResponse(Call<FarmerHeaderData> call, retrofit2.Response<FarmerHeaderData> response) {
                    try {
                        Log.e("Response : ----", "" + response.body());
                        if (response.body() != null) {
                            progressDialog.dismiss();

                            Fragment adf = new DetailProfileFormFragment();
                            Bundle args = new Bundle();
                            args.putInt("Emp_Id", empId);
                            args.putInt("Temp_Id", farmerHeaderData.getTempId());
                            args.putInt("SocietyId", socId);
                            args.putString("TempSocData", tempData);
                            adf.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "FarmerMaster").commit();

                        } else {
                            Log.e("Error : ", "-- null");
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("Exc : ", "--" + e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<FarmerHeaderData> call, Throwable t) {
                    progressDialog.dismiss();
                    t.printStackTrace();
                    Log.e("Error Fail: ", "--" + t.getMessage());
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendImage(String filename, String type, final FarmerHeaderData farmerHeaderData, final SocDataList socData) {
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

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.show();

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
                    progressDialog.dismiss();
                    // Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    addFarmer(farmerHeaderData, socData);
                    Log.e("Response : ", "--" + response.body());
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("Error : ", "--" + t.getMessage());
                    t.printStackTrace();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImageTempData(String filename, String type, final FarmerHeaderData farmerHeaderData, final SocDataList socData) {

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

            final AlertDialog progressDialog = new SpotsDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.show();

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
                    // Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    addTempData(socData, farmerHeaderData);
                    Log.e("Response : ", "--" + response.body());
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("Error : ", "--" + t.getMessage());
                    t.printStackTrace();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
