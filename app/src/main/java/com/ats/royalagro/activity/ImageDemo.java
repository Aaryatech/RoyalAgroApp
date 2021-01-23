package com.ats.royalagro.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.RealPathUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageDemo extends AppCompatActivity {

    public static String path, imagePath;

    File folder = new File(Environment.getExternalStorageDirectory() + File.separator, "RoyalAgro");
    File f;

    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_demo);

        ivPhoto = findViewById(R.id.ivImageDemo);

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImage();
            }
        });

        Button btn = findViewById(R.id.btnImg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ImageDemo.this, R.style.AlertDialogTheme);
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
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            f = new File(folder + File.separator, "Camera.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 102);
                        } catch (Exception e) {
                            //Log.e("select camera : ", " Exception : " + e.getMessage());
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String realPath;
        Bitmap bitmap = null;

        if (resultCode == this.RESULT_OK && requestCode == 102) {
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

        } else if (resultCode == this.RESULT_OK && requestCode == 101) {
            try {
                realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());

                Uri uriFromPath = Uri.fromFile(new File(realPath));

                //Log.e("PATH : ", "-------" + uriFromPath);

                bitmap = getBitmapFromCameraData(data, this);

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


    private void sendImage() {
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

        RequestBody imgType = RequestBody.create(MediaType.parse("text/plain"), "0");

        int pos = imgFile.getName().lastIndexOf(".");
        String ext = imgFile.getName().substring(pos + 1);
        RequestBody imgName = RequestBody.create(MediaType.parse("text/plain"), "1_" + System.currentTimeMillis()+"."+ext);

        Call<JSONObject> call = api.fileUpload(body, imgType, imgName);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to upload image, Please try again...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
