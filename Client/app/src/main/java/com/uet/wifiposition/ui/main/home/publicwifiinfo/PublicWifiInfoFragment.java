package com.uet.wifiposition.ui.main.home.publicwifiinfo;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.WifiInfoModel;
import com.uet.wifiposition.remote.model.getbuilding.BuildingModel;
import com.uet.wifiposition.remote.model.getbuilding.GetBuildingsResponse;
import com.uet.wifiposition.remote.model.getbuilding.GetRoomsResponse;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getbuilding.RoomModel;
import com.uet.wifiposition.remote.requestbody.ItemPostReferencePointGaussRequest;
import com.uet.wifiposition.remote.requestbody.PostReferencePointGaussRequest;
import com.uet.wifiposition.ui.base.BaseMvpFragment;
import com.uet.wifiposition.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ducnd on 9/22/17.
 */

public class PublicWifiInfoFragment extends BaseMvpFragment<PublicWifiInfoContact.Presenter> implements PublicWifiInfoContact.View,
        PublicWifiInfoAdapter.IPublicWifiInfoAdapter, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private PublicWifiInfoAdapter mAdapterBuilding;
    private PublicWifiInfoAdapter mAdapterRoom;
    private Spinner spBuilding, spRoom;
    private List<BuildingModel> buildingModels;
    private List<RoomModel> roomModels;
    private IPublicWifiInfo mInterf;
    private EditText edtX, edtY;

    public void setInterf(IPublicWifiInfo interf) {
        mInterf = interf;
    }

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_public_wifi_info;
    }

    @Override
    public void findViewByIds(View view) {
        spBuilding = (Spinner) view.findViewById(R.id.sp_building);
        spRoom = (Spinner) view.findViewById(R.id.sp_room);
        edtX = (EditText) view.findViewById(R.id.edt_x);
        edtY = (EditText) view.findViewById(R.id.edt_y);
        setProgress(view.findViewById(R.id.progress));
    }

    @Override
    public void initComponents(View view) {
        mPresenter = new PublicWifiInfoPresenter(this);
        mAdapterBuilding = new PublicWifiInfoAdapter(this);
        spBuilding.setAdapter(mAdapterBuilding);
        mAdapterRoom = new PublicWifiInfoAdapter(new PublicWifiInfoAdapter.IPublicWifiInfoAdapter() {
            @Override
            public int getCount() {
                if (roomModels == null) {
                    return 0;
                } else {
                    return roomModels.size();
                }
            }

            @Override
            public String getName(int position) {
                return roomModels.get(position).getRoomName();
            }
        });
        spRoom.setAdapter(mAdapterRoom);

        spBuilding.setOnItemSelectedListener(this);
        spRoom.setOnItemSelectedListener(this);

        mPresenter.getBuilding();
    }

    @Override
    public void setEvents(View view) {
        view.findViewById(R.id.btn_upload).setOnClickListener(this);
    }

    @Override
    public void finishGetBuildings(GetBuildingsResponse response) {
        buildingModels = response.getBuildingModels();
        mAdapterBuilding.notifyDataSetChanged();
    }

    @Override
    public void errorGetBuildings(Throwable error) {

    }

    @Override
    public void finishGetRooms(GetRoomsResponse response) {
        roomModels = response.getRoomModels();
        mAdapterRoom.notifyDataSetChanged();
    }

    @Override
    public void errorGetRooms(Throwable error) {

    }

    @Override
    public int getCount() {
        if (buildingModels == null) {
            return 0;
        } else {
            return buildingModels.size();
        }

    }

    @Override
    public String getName(int position) {
        return buildingModels.get(position).getBuildingName();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.setTag(position);
        switch (parent.getId()) {
            case R.id.sp_building:
                mPresenter.getRooms(buildingModels.get(position).getBuildingId());
                break;
            case R.id.sp_room:
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void finishPostRefencePoint(PostReferencePoint response) {
        showMessage("upload success");
    }

    @Override
    public void errorPostReferencePoint(Throwable error) {
        showMessage(error.getMessage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_upload:
                if (StringUtils.isEmpty(edtX.getText().toString()) || StringUtils.isEmpty(edtY.getText().toString())) {
                    showMessage(R.string.Please_input_enought_info);
                    return;
                }
                if (buildingModels == null || buildingModels.size() == 0) {
                    showMessage(R.string.Have_not_building);
                    return;
                }
                if (roomModels == null || roomModels.size() == 0) {
                    showMessage(R.string.Have_not_room);
                    return;
                }
                int x = Integer.parseInt(edtX.getText().toString());
                int y = Integer.parseInt(edtY.getText().toString());
                List<WifiInfoModel> infoModels = mInterf.getListWifiInfoChoose();
                if (infoModels == null) {
                    showMessage(R.string.Loading);
                    return;
                }
                if (infoModels.size() == 0) {
                    showMessage(R.string.Please_choose_wifi_to_upload);
                    return;
                }
                upload(x, y, infoModels);
                break;
            default:
                break;
        }
    }

    private void upload(int x, int y, List<WifiInfoModel> infoModels) {
        List<ItemPostReferencePointGaussRequest> items = new ArrayList<>();
        for (WifiInfoModel infoModel : infoModels) {
            ItemPostReferencePointGaussRequest item = new ItemPostReferencePointGaussRequest();
            item.setAppName(infoModel.getName());
            item.setMacAddress(infoModel.getMacAddress());
            item.setListRss(infoModel.getRss());

            items.add(item);
        }

        PostReferencePointGaussRequest request = new PostReferencePointGaussRequest();
        request.setItemPostReferencePointGaussRequests(items);
        int roomId = roomModels.get((int) spRoom.getTag()).getRoomId();
        request.setRoomId(roomId);
        request.setX(x);
        request.setY(y);
        mPresenter.postReferencePointGauss(request);


//        List<InfoReferencePointInput> pointInputs = new ArrayList<>();
//        for (WifiInfoModel infoModel : infoModels) {
//            pointInputs.add(new InfoReferencePointInput(infoModel.getMacAddress(), infoModel.getName(), infoModel.getLevel()));
//        }
//        int buildingId = buildingModels.get((int) spBuilding.getTag()).getBuildingId();
//        int roomId = roomModels.get((int) spRoom.getTag()).getRoomId();
//        mPresenter.postReferencePoint(buildingId, roomId, x, y, pointInputs);
    }

    public int getBuildingId() {
        if (buildingModels == null || buildingModels.size() == 0) {
            return -1;
        }
        return buildingModels.get((int) spBuilding.getTag()).getBuildingId();
    }

    public int getRoomId() {
        if (roomModels == null || roomModels.size() == 0) {
            return -1;
        }
        return roomModels.get((int) spRoom.getTag()).getRoomId();
    }

    @Override
    public void finishPostReferencePointGauss(PostReferencePoint response) {
        showMessage("upload success");
    }

    @Override
    public void errorPostReferencePointGauss(Throwable error) {
        showMessage(error.getMessage());
    }

    public interface IPublicWifiInfo {
        List<WifiInfoModel> getListWifiInfoChoose();
    }
}
