package com.ats.royalagro.retroint;

import com.ats.royalagro.bean.CropData;
import com.ats.royalagro.bean.CropVarietyData;
import com.ats.royalagro.bean.CropVarietyListData;
import com.ats.royalagro.bean.DistrictData;
import com.ats.royalagro.bean.EventData;
import com.ats.royalagro.bean.EventListData;
import com.ats.royalagro.bean.FarmerCropList;
import com.ats.royalagro.bean.FarmerCropListData;
import com.ats.royalagro.bean.FarmerDataByTempId;
import com.ats.royalagro.bean.FarmerHeaderData;
import com.ats.royalagro.bean.FarmerKycList;
import com.ats.royalagro.bean.FarmerPlotList;
import com.ats.royalagro.bean.FarmerPlotListData;
import com.ats.royalagro.bean.GaonData;
import com.ats.royalagro.bean.GetFarmerCropList;
import com.ats.royalagro.bean.Info;
import com.ats.royalagro.bean.KYCListData;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.bean.RegionData;
import com.ats.royalagro.bean.SocDataList;
import com.ats.royalagro.bean.SocietyData;
import com.ats.royalagro.bean.SocietyListData;
import com.ats.royalagro.bean.TalukaData;
import com.ats.royalagro.bean.TempSocietyListData;
import com.ats.royalagro.bean.UserAccessData;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by maxadmin on 6/12/17.
 */

public interface ApiInterface {

    public static final String BASE_URL = "http://97.74.228.55:8080/RoyalAgroWebapi/";
    //public static final String BASE_URL = "http://192.168.2.16:8096/";
     //public static final String BASE_URL = "http://192.168.2.6:8095/";
    public static final String MY_PREF = "Royal";
    public static final String PROFILE_PATH = "http://97.74.228.55:8080/agrouploads/profile/";
    public static final String FIELD_PATH = "http://97.74.228.55:8080/agrouploads/fieldMap/";
    public static final String KYC_PATH = "http://97.74.228.55:8080/agrouploads/kyc/";


    //------OTP-------
    @GET("sendhttp.php")
    Call<JsonObject> sendSMS(@Query("authkey") String authkey, @Query("mobiles") String mobiles, @Query("message") String message, @Query("sender") String sender, @Query("route") String route, @Query("country") String country, @Query("response") String response);

    @POST("empLogin")
    Call<LoginData> doLogin(@Query("empMobile") String empMobile);

    @POST("insertNewEvent")
    Call<EventData> addEvent(@Body EventData eventData);

    @GET("getAllRegions")
    Call<RegionData> getAllRegion();

    @POST("getDistrictsByRegId")
    Call<DistrictData> getDistrictByRegion(@Query("regId") Integer regId);

    @POST("getTalukasByDistId")
    Call<TalukaData> getTalukaByDistrict(@Query("distId") Integer distId);

    @POST("getGaonByTalId")
    Call<GaonData> getGaonByTaluka(@Query("talId") Integer talId);

    @GET("getAllGaon")
    Call<GaonData> getAllGaon();

    @POST("insertSociety")
    Call<SocietyData> addSociety(@Body SocietyData societyData);

    @POST("getAllEventByEmpRefId")
    Call<EventListData> getAllEvents(@Query("empRefId") Integer empRefId);

    @POST("getSocietyByEveIdAndRegBy")
    Call<SocietyListData> getAllSociety(@Query("eveId") Integer eveId, @Query("socRegBy") Integer socRegBy);

    @POST("getSocDataBySocId")
    Call<TempSocietyListData> getAllFarmersBySociety(@Query("socId") Integer socId);

    @POST("getEventByEventId")
    Call<EventListData> getEventById(@Query("eventId") Integer eventId);

    @POST("getSocietyById")
    Call<SocietyListData> getSociety(@Query("socId") Integer socId);

    @POST("updateSocDataStatus")
    Call<Info> updateFarmerFlag(@Query("tempId") Integer tempId, @Query("visitBy") Integer visitBy, @Query("tempStatus") Integer tempStatus, @Query("visitRemarks") String visitRemarks, @Query("visitDatetime") String visitDatetime);

    //getFHeaderByTempId
    //getFarmerHeaderByTempId
    @POST("getFHeaderByTempId")
    Call<FarmerDataByTempId> getFarmerHeaderData(@Query("tempId") Integer tempId);

    @POST("getFarmerHeader")
    Call<FarmerHeaderData> getFarmerHeader(@Query("fId") Integer fId);

    @POST("insertFarmerHeader")
    Call<FarmerHeaderData> addFarmerHeaderData(@Body FarmerHeaderData farmerHeaderData);

    @POST("getFarmerKyc")
    Call<KYCListData> getKYCList(@Query("fId") Integer fId);

    @POST("insertFarmerKyc")
    Call<FarmerKycList> addFarmerKYC(@Body FarmerKycList farmerKycList);

    @POST("getFarmerPlot")
    Call<FarmerPlotListData> getPlotList(@Query("fId") Integer fId);

    @POST("insertFarmerPlot")
    Call<FarmerPlotList> addFarmerPlot(@Body FarmerPlotList farmerPlotList);

    @POST("insertFarmerCrop")
    Call<FarmerCropList> addFarmerCrop(@Body FarmerCropList farmerCropList);

    @POST("getFarmerCrop")
    Call<FarmerCropListData> getFarmerCropList(@Query("fId") Integer fId);

    @POST("getFarmerCropById")
    Call<ArrayList<GetFarmerCropList>> getFarmerCropListById(@Query("fId") Integer fId);


    @POST("deleteEvent")
    Call<Info> deleteEvent(@Query("eventId") Integer eventId);

    @POST("deleteSociety")
    Call<Info> deleteSociety(@Query("socId") Integer socId);

    @GET("getAllCrops")
    Call<CropData> getCrops();

    @GET("getCropVarietiesByCrop")
    Call<CropVarietyData> getCropsVariety(@Query("cropId") Integer cropId);

    @GET("getAllCropVarieties")
    Call<CropVarietyListData> getAllCropsVariety();

    @Multipart
    @POST("fileUpload")
    Call<JSONObject> fileUpload(@Part MultipartBody.Part file, @Part("imageType") RequestBody type, @Part("imageName") RequestBody name);

    @POST("getAccessMgmtByEmpId")
    Call<UserAccessData> getUserAccessData(@Query("empId") Integer empId);

    @POST("getSocietyByEmpId")
    Call<SocietyListData> getAllSocietyByEmp(@Query("empId") Integer empId);

    @POST("insertSocData")
    Call<TempSocietyListData> updateSocietyData(@Body ArrayList<SocDataList> socDataLists);

    @GET("getAllEvent")
    Call<EventListData> getAllEventsByAdmin();

    @GET("getAllSociety")
    Call<SocietyListData> getAllSociety();

    @POST("getEmpByEmpId")
    Call<LoginData> getUserData(@Query("empId") int empId);

    @GET("getAllRegions")
    Call<RegionData> getAllRegions();

    @GET("getAllDistricts")
    Call<DistrictData> getAllDistricts();

    @GET("getAllTalukas")
    Call<TalukaData> getAllTalukas();

    @GET("getAllGaon")
    Call<GaonData> getAllGaons();

    @POST("getAllSocietyByEveId")
    Call<SocietyListData> getAllSocietyByEvent(@Query("eveId") int eveId);


}
