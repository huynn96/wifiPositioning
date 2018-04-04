package com.uet.wifiposition.ui.main.home.tracking;

import android.content.Context;
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
import com.uet.wifiposition.remote.model.getposition.LocationModel;
import com.uet.wifiposition.remote.model.motion.Acceleration;
import com.uet.wifiposition.remote.model.motion.Direction;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;
import com.uet.wifiposition.ui.base.BaseMvpFragment;
import com.uet.wifiposition.ui.customview.TrackingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by ducnd on 11/24/17.
 */

public class TrackingFragment extends BaseMvpFragment<TrackingContact.Presenter> implements SensorEventListener {
    private TrackingView trackingView;
    private int transactionId;
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

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_tracking;
    }

    @Override
    public void findViewByIds(View view) {
        trackingView = view.findViewById(R.id.tracking_view);
        startButton = this.getActivity().findViewById(R.id.start_pause_tracking_button);
        resultActivity = this.getActivity().findViewById(R.id.result_activity_tracking);
    }

    @Override
    public void initComponents(View view) {
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
            socket = IO.socket("http://192.168.1.40:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
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
                accelerationData = new ArrayList<>();
                directionData = new ArrayList<>();
                socket.emit("localization", goGson.toJson(request));
                socket.on("localization", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject response = (JSONObject)args[0];
                        getActivity().runOnUiThread(new Runnable() {
                            public void run(){
                                finishPostMotion(response);
                            }
                        });
                        Log.d("Socket_RESPONSE", response.toString());
                    }
                });
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

    public void finishPostMotion(JSONObject response) {
        try {
            double offset = response.getDouble("offset");
            int direction = response.getInt("direction");
            Log.d("RESULT", String.valueOf(offset));
            resultActivity.setText(String.format("%s (m)\n%s (degree)", offset, direction));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void errorPostMotion(Throwable error) {
//        showMessage(error.getMessage());
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
