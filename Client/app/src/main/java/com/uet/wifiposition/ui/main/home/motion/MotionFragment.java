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
import android.widget.Spinner;

import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.getbuilding.PostReferencePoint;
import com.uet.wifiposition.remote.model.motion.Acceleration;
import com.uet.wifiposition.remote.requestbody.PostMotionSensorInfoRequestBody;
import com.uet.wifiposition.ui.base.BaseMvpFragment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ducnd on 11/24/17.
 */

public class MotionFragment extends BaseMvpFragment<MotionContact.Presenter> implements SensorEventListener, MotionContact.View {
    private Sensor myAcceleration;
    private SensorManager SM;
    private Button startButton;
    private CountDownTimer timer;
    private List<Acceleration> accelerationData;
    private MotionPresenter motionPresenter;
    private Spinner spinner;

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_motion;
    }

    @Override
    public void findViewByIds(View view) {
        startButton = this.getActivity().findViewById(R.id.start_pause_motion_button);
        spinner = this.getActivity().findViewById(R.id.activity_type);
    }

    @Override
    public void initComponents(View view) {
        motionPresenter = new MotionPresenter(this);
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
    }

    public int getTypeActivity(String typeActivity) {
        switch (typeActivity) {
            case "Standing":
                return 0;
            case "Walking":
                return 1;
            case "Running":
                return 2;
            case "Irregular movement":
                return 3;
            default:
                return -1;
        }
    }

    public void startButton() {
        accelerationData = new ArrayList<>();
        SM.registerListener(this, myAcceleration, SensorManager.SENSOR_DELAY_GAME);
    }

    public void onPause() {
        super.onPause();
        PostMotionSensorInfoRequestBody request = new PostMotionSensorInfoRequestBody();
        request.setAccelerations(accelerationData);
        request.setTypeActivity(getTypeActivity((String) spinner.getSelectedItem()));
        motionPresenter.postMotionInfo(request);
        SM.unregisterListener(this);
    }

    @Override
    public void setEvents(View view) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long time = System.currentTimeMillis();
        accelerationData.add(new Acceleration(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2], time));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void finishPostMotion(PostReferencePoint response) {
        showMessage("upload success");
    }

    @Override
    public void errorPostMotion(Throwable error) {
        showMessage(error.getMessage());
    }
}
