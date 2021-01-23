package com.ats.royalagro.fragment;


import android.app.AlertDialog;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.activity.HomeActivity;
import com.ats.royalagro.bean.FarmerHeaderData;
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
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class BankDetailsFragment extends Fragment implements View.OnClickListener {

    private RadioButton rbSelf, rbOther;
    private EditText edAccName, edOwnerRel, edAadhar, edMobile, edEmail, edBankName, edBankBranch, edIFSC, edAccNumber;
    private Button btnSave, btnReset, btnPhoto;
    private ImageView ivPhoto;

    public static String path, imagePath;
    String fName = "", mName = "", lName = "", dob = "", farm = "", mobile1 = "", mobile2 = "", lanline = "", email = "", address = "", atPost = "", profile_pic = "", field_pic = "", reg_dateTime = "";
    int gender = 0, fCurr_Status = 0, reg_doc_no = 0, f_reg = 0;

    String areaTotal = "0";

    int fId, tempId, socId, empId;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "RoyalAgro");
    File f;

    private static Pattern aadhaarPattern = Pattern.compile("^[2-9]{1}[0-9]{11}$");
    private static Pattern ifscPattern = Pattern.compile("^[A-Za-z]{4}\\d{7}$");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_details, container, false);
        getActivity().setTitle("Bank Account Profile");

        createFolder();

        if (PermissionUtil.checkAndRequestPermissions(getActivity())) {

        }

        fId = getArguments().getInt("FarmerId");
        tempId = getArguments().getInt("TempId");
        socId = getArguments().getInt("SocietyId");
        //Log.e("Farmer Id : ", "--------" + fId);

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
        }


        rbSelf = view.findViewById(R.id.rbSelf);
        rbOther = view.findViewById(R.id.rbOther);

        edAccName = view.findViewById(R.id.edAccount_Name);
        edOwnerRel = view.findViewById(R.id.edAccount_OwnerRelation);
        edAadhar = view.findViewById(R.id.edAccount_Aadhar);
        edMobile = view.findViewById(R.id.edAccount_OwnerMobile);
        edEmail = view.findViewById(R.id.edAccount_OwnerEmail);
        edBankName = view.findViewById(R.id.edAccount_BankName);
        edBankBranch = view.findViewById(R.id.edAccount_BankBranch);
        edIFSC = view.findViewById(R.id.edAccount_IFSC);
        edAccNumber = view.findViewById(R.id.edAccount_AccNumber);

        ivPhoto = view.findViewById(R.id.ivAccount_Image);

        btnSave = view.findViewById(R.id.btnAccount_Save);
        btnReset = view.findViewById(R.id.btnAccount_Reset);
        btnPhoto = view.findViewById(R.id.btnAccount_Images);

        btnSave.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        rbSelf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edOwnerRel.setText("Self");
                } else {
                    edOwnerRel.setText("");
                }
            }
        });

        if (fId > 0) {
            getFarmerData(fId);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAccount_Save) {

            if (!rbSelf.isChecked() && !rbOther.isChecked()) {
                Toast.makeText(getActivity(), "Please Select Account Type", Toast.LENGTH_SHORT).show();
                rbSelf.requestFocus();
            } else if (edAccName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Account Name", Toast.LENGTH_SHORT).show();
                edAccName.requestFocus();
            } else if (edOwnerRel.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Relation With Owner", Toast.LENGTH_SHORT).show();
                edOwnerRel.requestFocus();
            } else if (edAadhar.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Aadhar Number", Toast.LENGTH_SHORT).show();
                edAadhar.requestFocus();
            } else if (!isValidAadhar(edAadhar.getText().toString())) {
                Toast.makeText(getActivity(), "Please Enter Valid Aadhar Number", Toast.LENGTH_SHORT).show();
                edAadhar.requestFocus();
            } else if (edMobile.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else if (edMobile.getText().toString().length() != 10) {
                Toast.makeText(getActivity(), "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            }
            /*else if (edEmail.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                edEmail.requestFocus();
            }*/
            else if (edBankName.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Bank Name", Toast.LENGTH_SHORT).show();
                edBankName.requestFocus();
            } else if (edBankBranch.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Bank Branch", Toast.LENGTH_SHORT).show();
                edBankBranch.requestFocus();
            } else if (edIFSC.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter IFSC Code", Toast.LENGTH_SHORT).show();
                edIFSC.requestFocus();
            }
            /*else if (!isValidIFSC(edIFSC.getText().toString())) {
                Toast.makeText(getActivity(), "Please Enter Valid IFSC Code", Toast.LENGTH_SHORT).show();
                edIFSC.requestFocus();
            }*/
            else if (edAccNumber.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "Please Enter Account Number", Toast.LENGTH_SHORT).show();
                edAccNumber.requestFocus();
            }
//            else if (imagePath == null || imagePath.isEmpty()) {
//                Toast.makeText(getActivity(), "Please Capture Field Image", Toast.LENGTH_SHORT).show();
//                btnPhoto.requestFocus();
//            }
            else {

                int type = 0;
                if (rbSelf.isChecked()) {
                    type = 0;
                } else {
                    type = 1;
                }

                String accName = edAccName.getText().toString();
                String relation = edOwnerRel.getText().toString();
                String aadhar = edAadhar.getText().toString();
                String mob = edMobile.getText().toString();
                String mail = edEmail.getText().toString();
                String bankName = edBankName.getText().toString();
                String bankBranch = edBankBranch.getText().toString();
                String ifsc = edIFSC.getText().toString();
                String accNumber = edAccNumber.getText().toString();

                String fullName = fName + " " + mName + " " + lName;

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = new java.util.Date();
                String dateTime = dateFormat.format(date);

                if (imagePath == null || imagePath.isEmpty()) {
                    imagePath = "";
                }

                field_pic = tempId + "_Field.jpg";

                FarmerHeaderData data = new FarmerHeaderData(fId, tempId, socId, fName, mName, lName, fullName, gender, dob, farm, mobile1, mobile2, lanline, email, address, atPost, areaTotal, type, accName, relation, aadhar, mob, mail, bankName, bankBranch, ifsc, accNumber, field_pic, fCurr_Status, reg_doc_no, reg_dateTime, f_reg, empId, dateTime, 1, profile_pic);
                addFarmer(data);
                /*  if (imagePath.isEmpty()) {
                    addFarmer(data);
                } else {
                    if (!field_pic.isEmpty()) {
                        data.setfProfilePic(field_pic);
                        sendImage(field_pic, "1", data);
                    } else {
                        File imgFile = new File(imagePath);
                        int pos = imgFile.getName().lastIndexOf(".");
                        String ext = imgFile.getName().substring(pos + 1);
                        field_pic = tempId + "_" + System.currentTimeMillis() + "." + ext;
                        data.setfFieldmapPic(field_pic);
                        sendImage(field_pic, "1", data);
                    }
                }*/
            }


        } else if (v.getId() == R.id.btnAccount_Reset) {
            edAccName.setText("");
            edOwnerRel.setText("");
            edAadhar.setText("");
            edMobile.setText("");
            edEmail.setText("");
            edBankName.setText("");
            edBankBranch.setText("");
            edIFSC.setText("");
            edAccNumber.setText("");
        } else if (v.getId() == R.id.btnAccount_Images) {
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

    public static boolean isValidAadhar(String name) {
        Matcher matcher = aadhaarPattern.matcher(name);
        return matcher.find();
    }

    public static boolean isValidIFSC(String name) {
        Matcher matcher = ifscPattern.matcher(name);
        return matcher.find();
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
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
        fName = data.getfFname();
        mName = data.getfMname();
        lName = data.getfSname();
        dob = data.getfDob();
        gender = data.getfGender();
        farm = data.getfFarmname();
        mobile1 = data.getfMobile();
        mobile2 = data.getfMobile2();
        lanline = data.getfLandline();
        email = data.getfEmail();
        address = data.getfAddress();
        atPost = data.getfAtpost();
        areaTotal = data.getfAreaTotal();
        profile_pic = data.getfProfilePic();
        field_pic = data.getfFieldmapPic();
        fCurr_Status = data.getfCurrStatus();
        reg_doc_no = data.getfRegDocNo();
        reg_dateTime = data.getfRegDocDate();
        f_reg = data.getfReg();

        int accType = data.getfAcctype();
        if (accType == 0) {
            rbSelf.setChecked(true);
        } else {
            rbOther.setChecked(true);
        }
        edAccName.setText("" + data.getfAccname());
        edAadhar.setText("" + data.getfAccAadhar());
        edMobile.setText("" + data.getfOtherMobile());
        edBankName.setText("" + data.getfBankName());
        edBankBranch.setText("" + data.getfBankBranch());
        edIFSC.setText("" + data.getfIfsc());
        edAccNumber.setText("" + data.getfAccno());
        edOwnerRel.setText("" + data.getfOwnerRel());
        edEmail.setText("" + data.getfOtherEmail());

        String image = ApiInterface.FIELD_PATH + "" + data.getfFieldmapPic();
        Log.e("Field Image", "--- " + image);
        ivPhoto.setImageResource(android.R.color.transparent);
        if (data.getfFieldmapPic().isEmpty()) {
            ivPhoto.setImageResource(android.R.color.transparent);
        } else {
            try {
                Picasso.with(getContext())
                        .load(image)
                        .placeholder(R.drawable.kyc_default)
                        .error(R.drawable.kyc_default)
                        .into(ivPhoto);
            } catch (Exception e) {
            }
        }
    }

    public void addFarmer(FarmerHeaderData farmerHeaderData) {
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
                        if (response.body() != null) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
//                            Fragment adf = new BankDetailsFragment();
//                            Bundle args = new Bundle();
//                            args.putInt("FarmerId", fId);
//                            args.putInt("TempId", tempId);
//                            args.putInt("SocietyId", socId);
//                            adf.setArguments(args);
//                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "ProfileMaster").commit();

                            Fragment adf = new DetailProfileFormFragment();
                            Bundle args = new Bundle();
                            args.putInt("Emp_Id", empId);
                            args.putInt("Temp_Id", tempId);
                            args.putInt("SocietyId", socId);
                            adf.setArguments(args);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "FarmerMaster").commit();


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
                public void onFailure(Call<FarmerHeaderData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendImage(String filename, String type, final FarmerHeaderData farmerHeaderData) {
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
                    addFarmer(farmerHeaderData);
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Unable To Save", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
