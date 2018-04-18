package com.uet.wifiposition.remote.interact.interf;

import com.uet.wifiposition.remote.model.getbuilding.GetBuildingsResponse;
import com.uet.wifiposition.remote.model.getbuilding.GetRoomsResponse;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getposition.GetLocationResponse;
import com.uet.wifiposition.remote.model.getposition.PostMotionResponse;
import com.uet.wifiposition.remote.requestbody.GetLocationRequest;
import com.uet.wifiposition.remote.requestbody.PostRPGaussianMotionRequestBody;
import com.uet.wifiposition.remote.requestbody.PostReferencePointGaussRequest;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ducnd on 9/29/17.
 */

public interface IInteractor {
    void init();

    Observable<GetBuildingsResponse> getBuilding();

    Observable<GetRoomsResponse> getRooms(int buildingId);

    Observable<PostReferencePoint> postReferencePoint(int buildingId, int roomId, int x, int y, List<InfoReferencePointInput> infos);
    Observable<PostReferencePoint> postReferencePointGauss(PostReferencePointGaussRequest postReferencePointGaussRequest);

    Observable<PostMotionResponse> postMotionInfo(PostRPGaussianMotionRequestBody postMotionSensorInfoRequestBody);

    Observable<GetLocationResponse> getLocation(GetLocationRequest request);
}
