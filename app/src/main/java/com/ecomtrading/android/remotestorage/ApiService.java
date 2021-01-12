package com.ecomtrading.android.remotestorage;

import com.ecomtrading.android.data.AccessToken;
import com.ecomtrading.android.data.Community;
import com.ecomtrading.android.data.GeoDistrict;
import com.ecomtrading.android.data.MasterData;
import com.ecomtrading.android.data.SaveCommunityResponse;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ravi on 31/01/18.
 */

public interface ApiService {
    @FormUrlEncoded
    @POST("token")
    Call<AccessToken> getAccessToken(
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType);

    @GET("CommunityRegistration/Masters?sMasterType=Common")
    Call<List<MasterData>> getMasterDataCommon(@Header("Authorization") String token);

    @GET("CommunityRegistration/Masters?sMasterType=Geo District")
    Call<List<GeoDistrict>> getMasterDataGeoDistrict(@Header("Authorization") String token);

    @POST("InstructionSheet/SaveCommunityData")
    Call<SaveCommunityResponse> postCommunityData(@Header("Authorization") String token, @Body Community community);

}
