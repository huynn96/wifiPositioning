package com.uet.wifiposition.remote.interact.main;

import android.util.Log;

import com.google.gson.Gson;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.uet.wifiposition.common.Constants;
import com.uet.wifiposition.remote.interact.resource.Rest;
import com.uet.wifiposition.remote.interact.interf.IInteractor;
import com.uet.wifiposition.remote.model.getbuilding.GetBuildingsResponse;
import com.uet.wifiposition.remote.model.getbuilding.GetRoomsResponse;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getposition.PostMotionResponse;
import com.uet.wifiposition.remote.requestbody.GetLocationRequest;
import com.uet.wifiposition.remote.requestbody.PostRPGaussianMotionRequestBody;
import com.uet.wifiposition.remote.requestbody.PostReferencePointGaussRequest;
import com.uet.wifiposition.remote.requestbody.PostReferencePointRequestBody;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.BuildConfig;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ducnd on 9/29/17.
 */

public class Interactor implements IInteractor {
    private static String BASE_API_URL = "";
    private static final IInteractor instance = new Interactor();
    private Rest mRes;
    private Scheduler mScheduel;
    private Gson goGson = new Gson();

    public static IInteractor getInstance() {
        return instance;
    }

    private Interactor() {

    }

    public void init() {
        mScheduel = Schedulers.from(Executors.newFixedThreadPool(5));
        mRes = createRetrofit(BASE_API_URL).create(Rest.class);
    }

    private static Retrofit createRetrofit(String endpoint) {
        return new Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }

    private static OkHttpClient createOkHttp() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new LoggingInterceptor.Builder()
                .loggable(Constants.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .addHeader("Content-Type", "application/json")
                .build());
        return client
                .retryOnConnectionFailure(true)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public Observable<GetBuildingsResponse> getBuilding() {
        return mRes.getBuildings()
                .subscribeOn(mScheduel)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<GetRoomsResponse> getRooms(int buildingId) {
        return mRes.getRooms(buildingId)
                .subscribeOn(mScheduel)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PostReferencePoint> postReferencePoint(int buildingId, int roomId, int x, int y, List<InfoReferencePointInput> infos) {
        PostReferencePointRequestBody requestBody = new PostReferencePointRequestBody();
        requestBody.setBuildingId(buildingId);
        requestBody.setRoomId(roomId);
        requestBody.setX(x);
        requestBody.setY(y);
        requestBody.setInfos(infos);
        RequestBody requestBodyD = RequestBody.create(MediaType.parse("application/json"), goGson.toJson(requestBody));
        return mRes.postReferencePoint(requestBodyD)
                .subscribeOn(mScheduel)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PostReferencePoint> postReferencePointGauss(PostReferencePointGaussRequest postReferencePointGaussRequest) {
        RequestBody requestBodyD = RequestBody.create(MediaType.parse("application/json"), goGson.toJson(postReferencePointGaussRequest));
        return mRes.postReferencePointGauss(requestBodyD)
                .subscribeOn(mScheduel)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<PostMotionResponse> postMotionInfo(PostRPGaussianMotionRequestBody postMotionSensorInfoRequestBody) {
        RequestBody requestBodyD = RequestBody.create(MediaType.parse("application/json"), goGson.toJson(postMotionSensorInfoRequestBody));
        return mRes.postMotionInfo(requestBodyD)
                .subscribeOn(mScheduel)
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void setUrl(String url) {
        BASE_API_URL = url;
    }

    @Override
    public Observable<PostMotionResponse> getLocation(GetLocationRequest request) {
        String text = goGson.toJson(request);
        Log.d("request location:", text);
        RequestBody requestBodyD = RequestBody.create(MediaType.parse("application/json"), goGson.toJson(request));
        return mRes.getLocation(requestBodyD)
                .subscribeOn(mScheduel)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
