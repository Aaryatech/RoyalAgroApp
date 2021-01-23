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
import android.widget.ImageView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.activity.HomeActivity;
import com.ats.royalagro.bean.FarmerHeaderData;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.bean.SocDataList;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.ats.royalagro.util.PermissionUtil;
import com.ats.royalagro.util.RealPathUtil;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
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

public class ProfilePhotoFragment extends Fragment implements View.OnClickListener {

    private Button btnClick, btnUpload, btnFieldClick, btnFieldUpload;
    private ImageView ivImage, ivFieldImage;

    public static String path, imagePath, imagePath1;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "RoyalAgro");
    File f;

    int fId, tempId, socId, empId;
    String tempData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_photo, container, false);
        getActivity().setTitle("Profile Photo");

        btnClick = view.findViewById(R.id.btnProfilePhoto_Click);
        btnUpload = view.findViewById(R.id.btnProfilePhoto_Upload);
        ivImage = view.findViewById(R.id.ivProfilePhoto_Image);
        btnFieldClick = view.findViewById(R.id.btnFieldPhoto_Click);
        btnFieldUpload = view.findViewById(R.id.btnFieldPhoto_Upload);
        ivFieldImage = view.findViewById(R.id.ivFieldPhoto_Image);
        btnClick.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnFieldUpload.setOnClickListener(this);
        btnFieldClick.setOnClickListener(this);


        if (PermissionUtil.checkAndRequestPermissions(getActivity())) {

        }

        createFolder();


        fId = getArguments().getInt("FarmerId");
        tempId = getArguments().getInt("TempId");
        socId = getArguments().getInt("SocietyId");
        tempData = getArguments().getString("TempSocData");

        SharedPreferences pref = getContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData loginBean = gson.fromJson(json2, LoginData.class);
        //Log.e("LoginBean : ", "---------------" + loginBean);
        if (loginBean != null) {
            empId = loginBean.getEmpId();
        }


        if (fId > 0) {
            getFarmerData(fId);
        }


        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnProfilePhoto_Click) {
            cameraDialog();
        } else if (view.getId() == R.id.btnProfilePhoto_Upload) {
            if (imagePath == null || imagePath.isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
            } else {
                String profilePic;
                profilePic = tempId + "_Profile.jpg";

                sendImage(profilePic, "0");
            }
        } else if (view.getId() == R.id.btnFieldPhoto_Click) {
            cameraDialog1();
        } else if (view.getId() == R.id.btnFieldPhoto_Upload) {
            if (imagePath1 == null || imagePath1.isEmpty()) {
                Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
            } else {
                String fieldPic;
                fieldPic = tempId + "_Field.jpg";

                sendImage(fieldPic, "1");
            }
        }
    }

    public void createFolder() {
        if (!folder.exists()) {
            folder.mkdir();
        }
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

                                String image = ApiInterface.PROFILE_PATH + "" + data.getfProfilePic();
                                try {
                                    Picasso.with(getContext())
                                            .load(image)
                                            .placeholder(R.drawable.img)
                                            .error(R.drawable.img)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                            .into(ivImage);
                                } catch (Exception e) {
                                }

                                String fieldImage = ApiInterface.FIELD_PATH + "" + data.getfFieldmapPic();
                                try {
                                    Picasso.with(getContext())
                                            .load(fieldImage)
                                            .placeholder(R.drawable.img)
                                            .error(R.drawable.img)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                            .into(ivFieldImage);
                                } catch (Exception e) {
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


    private void sendImage(String filename, final String type) {
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
            File imgFile;
            if (type.equals("0")) {
                imgFile = new File(imagePath);
            } else {
                imgFile = new File(imagePath1);
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imgFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgFile.getName(), requestFile);

            RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), type);

            RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), filename);

            Call<JSONObject> call = api.fileUpload(body, imgType, imgName);
            call.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    Log.e("Response : ", "--" + response.body());
                    if (type.equals("0")) {
                        imagePath = "";
                    } else {
                        imagePath1 = "";
                    }

//                    Fragment adf = new DetailProfileFormFragment();
//                    Bundle args = new Bundle();
//                    args.putInt("Emp_Id", empId);
//                    args.putInt("Temp_Id", tempId);
//                    args.putInt("SocietyId", socId);
//                    args.putString("TempSocData", tempData);
//                    adf.setArguments(args);
//                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, adf, "FarmerMaster").commit();

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

    public void cameraDialog() {
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

    public void cameraDialog1() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
        builder.setTitle("Choose");
        builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent pictureActionIntent = null;
                pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pictureActionIntent, 201);
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
                        startActivityForResult(intent, 202);

                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        f = new File(folder + File.separator, "Camera.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(intent, 202);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.e("select camera : ", " Exception : " + e.getMessage());
                }
            }
        });
        builder.show();
    }


    //--------------------------IMAGE-----------------------------------------

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String realPath;
        Bitmap bitmap = null, bitmap1 = null;

        if (resultCode == getActivity().RESULT_OK && requestCode == 102) {
            try {
                Uri uriFromPath = Uri.fromFile(f);
                //Log.e("FILE : ", "-----------" + f.getAbsolutePath());
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //Log.e("BITMAP : ", "-----------" + myBitmap);
                    ivImage.setImageBitmap(myBitmap);

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

                Log.e("PATH : ", "-------" + uriFromPath);
                Log.e("REAL PATH : ", "-------" + realPath);

                HomeActivity regActivity = (HomeActivity) getActivity();
                bitmap = getBitmapFromCameraData(data, regActivity);

                ivImage.setImageBitmap(bitmap);

                imagePath = uriFromPath.getPath();
                try {
                    // bitmap = shrinkBitmap(bitmap.getAbsolutePath(), 720, 720);
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
        } else if (resultCode == getActivity().RESULT_OK && requestCode == 202) {
            try {
                Uri uriFromPath = Uri.fromFile(f);
                //Log.e("FILE : ", "-----------" + f.getAbsolutePath());
                String path = f.getAbsolutePath();
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //Log.e("BITMAP : ", "-----------" + myBitmap);
                    ivFieldImage.setImageBitmap(myBitmap);

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

                imagePath1 = f.getAbsolutePath();
                //Log.e("Camera : Path : ", " ---- " + imagePath);

            } catch (Exception e) {
                e.printStackTrace();
                //Log.e("In Camera ", "-Exc : " + e.getMessage());
            }

        } else if (resultCode == getActivity().RESULT_OK && requestCode == 201) {
            try {
                String realPath1 = RealPathUtil.getRealPathFromURI_API19(getActivity(), data.getData());

                Uri uriFromPath = Uri.fromFile(new File(realPath1));

                Log.e("PATH : ", "-------" + uriFromPath);
                Log.e("REAL PATH : ", "-------" + realPath1);

                HomeActivity regActivity = (HomeActivity) getActivity();
                bitmap1 = getBitmapFromCameraData(data, regActivity);

                ivFieldImage.setImageBitmap(bitmap1);

                imagePath1 = uriFromPath.getPath();
                try {
                    // bitmap = shrinkBitmap(bitmap.getAbsolutePath(), 720, 720);
                    FileOutputStream out = new FileOutputStream(uriFromPath.getPath());
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, out);
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

        Bitmap bitm = shrinkBitmap(picturePath, 720, 720);

        return bitm;
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("On PAUSE", "----------------------------");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("On DESTROY VIEW", "------------------------------");
        imagePath = "";
        imagePath1 = "";
    }
}
