package com.uet.wifiposition.ui.main.home.motion;

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
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.motion.Acceleration;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;
import com.uet.wifiposition.ui.base.BaseMvpFragment;

import org.json.JSONArray;
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

public class MotionFragment extends BaseMvpFragment<MotionContact.Presenter> implements SensorEventListener {
    private Sensor myAcceleration;
    private SensorManager SM;
    private Button startButton;
    private CountDownTimer timer;
    private List<Acceleration> accelerationData;
    private TextView resultActivity;
    private long lastTime;
    private Socket socket;
    private Gson goGson = new Gson();

    public MotionFragment() {
    }

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_motion;
    }

    @Override
    public void findViewByIds(View view) {
        startButton = this.getActivity().findViewById(R.id.start_pause_motion_button);
        resultActivity = this.getActivity().findViewById(R.id.result_activity);
    }

    @Override
    public void initComponents(View view) {
        SM = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        myAcceleration = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
        lastTime = 1;
        SM.registerListener(this, myAcceleration, SensorManager.SENSOR_DELAY_FASTEST);
        timer = new CountDownTimer(3000000, 2000) {

            public void onTick(long millisUntilFinished) {
                Log.d("COUNT", String.valueOf(accelerationData.size()));
                PostMotionSensorInfoRequestBody request = new PostMotionSensorInfoRequestBody();
                request.setAccelerations(accelerationData);
                accelerationData = new ArrayList<>();
                socket.emit("localization", goGson.toJson(request));
                socket.on("localization", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject response = (JSONObject)args[0];
                        finishPostMotion(response);
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
        long time = System.nanoTime();
        accelerationData.add(new Acceleration(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], time));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void finishPostMotion(JSONObject response) {
        try {
            JSONArray result = response.getJSONArray("result");
            Log.d("RESULT", String.valueOf(result));
            if (lastTime < response.getLong("lastTime")) {
                int a = result.getInt(0);
                if (a == 0) {
                    resultActivity.setText("Standing");
                }
                if (a == 1) {
                    resultActivity.setText("Walking");
                }
                lastTime = response.getLong("lastTime");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void errorPostMotion(Throwable error) {
//        showMessage(error.getMessage());
    }
}
