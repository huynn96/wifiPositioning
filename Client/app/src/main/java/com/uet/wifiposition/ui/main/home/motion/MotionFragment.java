package com.uet.wifiposition.ui.main.home.motion;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.getbuilding.BuildingModel;
import com.uet.wifiposition.remote.model.getbuilding.GetBuildingsResponse;
import com.uet.wifiposition.remote.model.getbuilding.GetRoomsResponse;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getbuilding.RoomModel;
import com.uet.wifiposition.remote.model.getposition.PostMotionResponse;
import com.uet.wifiposition.remote.model.motion.Acceleration;
import com.uet.wifiposition.remote.model.motion.Direction;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;
import com.uet.wifiposition.remote.requestbody.PostRPGaussianMotionRequestBody;
import com.uet.wifiposition.ui.base.BaseMvpFragment;
import com.uet.wifiposition.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by ducnd on 11/24/17.
 */

public class MotionFragment extends BaseMvpFragment<MotionContact.Presenter> implements MotionContact.View, SensorEventListener, AdapterView.OnItemSelectedListener, MotionAdapter.IMotionAdapter {
    private Sensor myAcceleration;
    private Sensor myCompass;
    private SensorManager SM;
    private Button startButton;
    private CountDownTimer timer;
    private List<Acceleration> accelerationData;
    private List<Direction> directionData;
    private TextView resultActivity;
    private Socket socket;
    private Gson goGson = new Gson();
    private float accelerometerValues[];
    private float geomagneticMatrix[];
    private List<BuildingModel> buildingModels;
    private List<RoomModel> roomModels;
    private Spinner spBuilding, spRoom;
    private EditText edtX1, edtY1, edtX2, edtY2;
    private MotionAdapter mAdapterBuilding;
    private MotionAdapter mAdapterRoom;

    public MotionFragment() {
    }

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_motion;
    }

    @Override
    public void findViewByIds(View view) {
        startButton = this.getActivity().findViewById(R.id.start_pause_motion_button);
        spBuilding = (Spinner) view.findViewById(R.id.sp_building);
        spRoom = (Spinner) view.findViewById(R.id.sp_room);
        edtX1 = (EditText) view.findViewById(R.id.edt_x1);
        edtY1 = (EditText) view.findViewById(R.id.edt_y1);
        edtX2 = (EditText) view.findViewById(R.id.edt_x2);
        edtY2 = (EditText) view.findViewById(R.id.edt_y2);
        setProgress(view.findViewById(R.id.progress));
        resultActivity = this.getActivity().findViewById(R.id.result_activity_motion);
    }

    @Override
    public void initComponents(View view) {
        mPresenter = new MotionPresenter(this);
        mAdapterBuilding = new MotionAdapter(this);
        spBuilding.setAdapter(mAdapterBuilding);
        mAdapterRoom = new MotionAdapter(new MotionAdapter.IMotionAdapter() {
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
        SM = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        myAcceleration = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myCompass = SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        final Boolean[] start = {true};
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start[0]) {
                    startButton.setText("Pause");
                    start[0] = false;
                    startButton();
                } else {
                    startButton.setText("Start");
                    start[0] = true;
                    onPause();
                }
            }
        });

        try {
            socket = IO.socket("http://192.168.43.145:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
//        socket.connect();
        socket.on("localization", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        finishPostMotion1(response);
                    }
                });
                Log.d("Socket_RESPONSE", response.toString());
            }
        });
    }

    public void startButton() {
        accelerationData = new ArrayList<>();
        directionData = new ArrayList<>();
        accelerometerValues = null;
        geomagneticMatrix = null;
        SM.registerListener(this, myAcceleration, SensorManager.SENSOR_DELAY_GAME);
        SM.registerListener(this, myCompass, SensorManager.SENSOR_DELAY_UI);
        timer = new CountDownTimer(3000000, 3000) {

            public void onTick(long millisUntilFinished) {
                Log.d("COUNT", String.valueOf(accelerationData.size()));
                PostMotionSensorInfoRequestBody request = new PostMotionSensorInfoRequestBody();
                request.setAccelerations(accelerationData);
                request.setDirections(directionData);
                if (accelerationData.size() > 0) {
                    socket.emit("localization", goGson.toJson(request));
                }
                accelerationData = new ArrayList<>();
                directionData = new ArrayList<>();
            }

            public void onFinish() {

            }

        }.start();
    }

    public void onPause() {
        super.onPause();
        SM.unregisterListener(this);
        timer.cancel();
    }

    @Override
    public void setEvents(View view) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;

        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                long time = System.nanoTime();
                accelerationData.add(new Acceleration(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], time));
                accelerometerValues = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomagneticMatrix = sensorEvent.values.clone();
                break;
        }

        if (geomagneticMatrix != null && accelerometerValues != null && sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            float[] R = new float[16];
            float[] I = new float[16];
            float[] outR = new float[16];
            //Get the rotation matrix, then remap it from camera surface to world coordinates
            SensorManager.getRotationMatrix(R, I, accelerometerValues, geomagneticMatrix);
            float values[] = new float[4];
            SensorManager.getOrientation(R, values);
            long time = System.nanoTime();
            int direction = normalizeDegrees(Math.toDegrees(values[0]));
            int pitch = normalizeDegrees(Math.toDegrees(values[1]));
            int roll = normalizeDegrees(Math.toDegrees(values[2]));
            directionData.add(new Direction(direction, pitch, roll, time));
            int[] list = {direction, pitch, roll};
        }
    }

    private int normalizeDegrees(double rads) {
        return (int) ((rads + 360) % 360);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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

    public void finishPostMotion1(JSONObject response) {
        try {
            if (StringUtils.isEmpty(edtX1.getText().toString()) || StringUtils.isEmpty(edtY1.getText().toString())) {
                showMessage(R.string.Please_input_enought_info);
                return;
            }
            if (StringUtils.isEmpty(edtX2.getText().toString()) || StringUtils.isEmpty(edtY2.getText().toString())) {
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
            double offset = response.getDouble("offset");
            int direction = response.getInt("direction");
            Log.d("DIRECTION", String.valueOf(direction));
            Log.d("OFFSET", String.valueOf(offset));
            resultActivity.setText(String.format("%s (m)\n%s (degree)", offset, direction));
            PostRPGaussianMotionRequestBody requestBody = new PostRPGaussianMotionRequestBody();
            requestBody.setDirection(direction);
            requestBody.setOffset(offset);
            requestBody.setRoomId(roomModels.get((int) spRoom.getTag()).getRoomId());
            mPresenter.postMotionInfo(requestBody);
            onPause();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void finishPostMotion(PostMotionResponse response) {
        showMessage("upload success");
    }

    @Override
    public void errorPostMotion(Throwable error) {
        showMessage(error.getMessage());
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
}
