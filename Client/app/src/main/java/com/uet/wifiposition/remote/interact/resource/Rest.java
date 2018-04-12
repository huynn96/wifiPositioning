package com.uet.wifiposition.remote.interact.resource;

import com.uet.wifiposition.remote.model.getbuilding.GetBuildingsResponse;
import com.uet.wifiposition.remote.model.getbuilding.GetRoomsResponse;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getposition.PostMotionResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ducnd on 9/29/17.
 */

public interface Rest {
    @GET("api/mobile/buildings")
    Observable<GetBuildingsResponse> getBuildings();

    @GET("api/mobile/rooms")
    Observable<GetRoomsResponse> getRooms(@Query("buildingId") int buildingId);

    @POST("api/mobile/add-wifi-info")
    Observable<PostReferencePoint> postReferencePoint(@Body RequestBody requestBody);

    @POST("api/mobile/add-wifi-info")
    Observable<PostReferencePoint> postReferencePointGauss(@Body RequestBody requestBodyD);

    @POST("api/mobile/localization")
    Observable<PostMotionResponse> getLocation(@Body RequestBody requestBody);

    @POST("api/mobile/motion-info")
    Observable<PostMotionResponse> postMotionInfo(@Body RequestBody requestBody);
}
