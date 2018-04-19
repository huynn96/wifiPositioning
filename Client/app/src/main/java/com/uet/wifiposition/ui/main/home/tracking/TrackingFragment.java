package com.uet.wifiposition.ui.main.home.tracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.WifiInfoModel;
import com.uet.wifiposition.remote.model.getbuilding.ExtendGetLocationModel;
import com.uet.wifiposition.remote.model.getbuilding.InfoReferencePointInput;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.getposition.GetLocationResponse;
import com.uet.wifiposition.remote.model.getposition.PostMotionResponse;
import com.uet.wifiposition.remote.model.getposition.LocationModel;
import com.uet.wifiposition.remote.model.motion.Acceleration;
import com.uet.wifiposition.remote.model.motion.Direction;
import com.uet.wifiposition.remote.requestbody.GetLocationRequest;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;
import com.uet.wifiposition.remote.requestbody.PostRPGaussianMotionRequestBody;
import com.uet.wifiposition.ui.base.BaseMvpFragment;
import com.uet.wifiposition.ui.customview.TrackingView;
import com.uet.wifiposition.ui.main.home.publicwifiinfo.PublicWifiInfoFragment;
import com.uet.wifiposition.ui.main.home.scanwifi.ScanWifiInfoFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by ducnd on 11/24/17.
 */

public class TrackingFragment extends BaseMvpFragment<TrackingContact.Presenter> implements SensorEventListener, TrackingContact.View {
    private TrackingView trackingView;
    private int transactionId;
    private Sensor myAcceleration;
    private Sensor myCompass;
    private SensorManager SM;
    private Button collectButton, localizationButton;
    private CountDownTimer timer;
    private List<Acceleration> accelerationData;
    private List<Direction> directionData;
    private TextView resultActivity;
    private Socket socket;
    private Gson goGson = new Gson();
    private float accelerometerValues[];
    private float geomagneticMatrix[];
    private TrackingPresenter mPresenter;
    private LocationModel lastLocation;
    private SharedPreferences sharedPref;
    private String typePositioning;
    private List<LocationModel> oldCandidates;

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_tracking;
    }

    @Override
    public void findViewByIds(View view) {
        trackingView = view.findViewById(R.id.tracking_view);
        collectButton = this.getActivity().findViewById(R.id.collect);
        localizationButton = this.getActivity().findViewById(R.id.localization);
        resultActivity = this.getActivity().findViewById(R.id.result_activity);
    }

    @Override
    public void initComponents(View view) {
        oldCandidates = new ArrayList<>();
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(getString(R.string.step_length_params_a), (float) -0.07);
        editor.putFloat(getString(R.string.step_length_params_b), (float) 0.46);
        editor.apply();
        mPresenter = new TrackingPresenter(this);
        SM = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        myAcceleration = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myCompass = SM.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        final Boolean[] start = {true};
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start[0]) {
                    collectButton.setText("Pause");
                    start[0] = false;
                    startButton();
                    typePositioning = "collect";
                } else {
                    collectButton.setText("Collect");
                    start[0] = true;
                    onPause();
                }
            }
        });

        localizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (start[0]) {
                    localizationButton.setText("Pause");
                    start[0] = false;
                    startButton();
                    typePositioning = "localization";
                } else {
                    localizationButton.setText("Localization");
                    start[0] = true;
                    onPause();
                }
            }
        });

        try {
            socket = IO.socket("http://192.168.1.40:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        socket.on("localization", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                getActivity().runOnUiThread(new Runnable() {
                    public void run(){
                        finishPostMotion1(response);
                    }
                });
                Log.d("Socket_RESPONSE", response.toString());
            }
        });
    }

    public void startButton() {
        trackingView.clearPath();
        lastLocation = new LocationModel(1,1);
        lastLocation.setTransactionId(0);
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
                float a = sharedPref.getFloat(getString(R.string.step_length_params_a), (float) -0.07);
                float b = sharedPref.getFloat(getString(R.string.step_length_params_b), (float) 0.46);
                Log.d("A", String.valueOf(a));
                Log.d("B", String.valueOf(b));
                request.setAccelerations(accelerationData);
                request.setDirections(directionData);
                request.setA(a);
                request.setB(b);
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
            SensorManager.getOrientation(R,values);
            long time = System.nanoTime();
            int direction = normalizeDegrees(Math.toDegrees(values[0]));
            int pitch = normalizeDegrees(Math.toDegrees(values[1]));
            int roll = normalizeDegrees(Math.toDegrees(values[2]));
            directionData.add(new Direction(direction, pitch, roll, time));
            int[] list = {direction, pitch, roll};
//            Log.d("DIRECTION", Arrays.toString(list));
        }
    }

    private int normalizeDegrees(double rads){
        return (int)((rads+360)%360);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void finishPostMotion1(JSONObject response) {
        try {
            double offset = response.getDouble("offset");
            int direction = response.getInt("direction");
            int stepCount = response.getInt("stepCount");
            resultActivity.setText(String.format("%s (degree)", direction));
            if (typePositioning == "collect") {
                sendMotion(offset, direction, stepCount);
            }
            if (typePositioning == "localization") {
                sendLocalization(offset, direction, stepCount);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendLocalization(double offset, int direction, int stepCount) {
        GetLocationRequest request = new GetLocationRequest();
        ScanWifiInfoFragment scan = (ScanWifiInfoFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 0);
        List<WifiInfoModel> wifiInfoModels = scan.getListWifiInfoChoose();
        if (wifiInfoModels == null || wifiInfoModels.size() == 0) {
            showMessage(R.string.Loading);
            return;
        }
        PublicWifiInfoFragment publicInfo = (PublicWifiInfoFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 1);
        int buildingId = publicInfo.getBuildingId();
        int roomId = publicInfo.getRoomId();
        if (buildingId == -1 || roomId == -1) {
            showMessage(R.string.Loading);
            return;
        }
        List<InfoReferencePointInput> infoReferencePointInputs = new ArrayList<>();
        for (WifiInfoModel wifiInfoModel : wifiInfoModels) {
            Log.d("Wifi", String.valueOf(wifiInfoModel));
            infoReferencePointInputs.add(new InfoReferencePointInput(wifiInfoModel.getMacAddress(), wifiInfoModel.getName(), wifiInfoModel.getLevel()));
        }
        request.setOldCandidates(oldCandidates);
        request.setBuildingId(buildingId);
        request.setRoomId(roomId);
        request.setInfos(infoReferencePointInputs);
        request.setDirection(direction);
        request.setOffset(offset);
        request.setStepCount(stepCount);
        mPresenter.getLocation(request);
    }

    private void sendMotion(double offset, int direction, int stepCount) {
        PostRPGaussianMotionRequestBody request = new PostRPGaussianMotionRequestBody();
        ScanWifiInfoFragment scan = (ScanWifiInfoFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 0);
        List<WifiInfoModel> wifiInfoModels = scan.getListWifiInfoChoose();
        if (wifiInfoModels == null || wifiInfoModels.size() == 0) {
            showMessage(R.string.Loading);
            return;
        }
        PublicWifiInfoFragment publicInfo = (PublicWifiInfoFragment) getFragmentManager().findFragmentByTag("android:switcher:" + R.id.vp_position + ":" + 1);
        int buildingId = publicInfo.getBuildingId();
        int roomId = publicInfo.getRoomId();
        if (buildingId == -1 || roomId == -1) {
            showMessage(R.string.Loading);
            return;
        }
        List<InfoReferencePointInput> infoReferencePointInputs = new ArrayList<>();
        for (WifiInfoModel wifiInfoModel : wifiInfoModels) {
            Log.d("Wifi", String.valueOf(wifiInfoModel));
            infoReferencePointInputs.add(new InfoReferencePointInput(wifiInfoModel.getMacAddress(), wifiInfoModel.getName(), wifiInfoModel.getLevel()));
        }
        ExtendGetLocationModel location = new ExtendGetLocationModel();
        if (lastLocation.getTransactionId() == 0) {
            location.setFirst(true);
            location.setX(1);
            location.setY(1);
            location.setTransactionId(1);
        } else {
            location.setFirst(false);
            location.setTransactionId(lastLocation.getTransactionId() + 1);
            location.setX(lastLocation.getX());
            location.setY(lastLocation.getY());
        }
        request.setBuildingId(buildingId);
        request.setRoomId(roomId);
        request.setInfos(infoReferencePointInputs);
        request.setExtendGetLocationModel(location);
        request.setDirection(direction);
        request.setOffset(offset);
        request.setStepCount(stepCount);
        mPresenter.postMotionInfo(request);
    }

    @Override
    public void finishGetLocation(GetLocationResponse response) {
        oldCandidates = response.getCandidates();
        responseTracking(response.getPosition());
    }

    @Override
    public void errorGetLocation(Throwable error) {

    }

    @Override
    public void finishPostMotion(PostMotionResponse response) {
        lastLocation = response.getLocationModel();
        responseTracking(response.getLocationModel());
    }

    @Override
    public void errorPostMotion(Throwable error) {

    }

    public void responseTracking(LocationModel locationModel) {
        trackingView.addPath(locationModel.getX(), locationModel.getY());
        transactionId = locationModel.getTransactionId();
    }

    public boolean isPathEmpty() {
        return trackingView.isPathEmpty();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
