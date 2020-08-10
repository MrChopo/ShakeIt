package com.example.shakeitbitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private TextView textView;
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayer;
    private boolean isFirstShake = true;
    private ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintLayout);
        textView = findViewById(R.id.textView);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.cadillac_right_version);
        mediaPlayer.setVolume(1, 1);
        AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int values = 15;//range(0-15)
        mgr.setStreamVolume(AudioManager.STREAM_MUSIC, values, 0);


        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

                textView.setText("" + l/1000);

            }

            @Override
            public void onFinish() {

                mediaPlayer.pause();
                constraintLayout.setBackgroundResource(0);
                isFirstShake = true;
                textView.setText("Oops!");

            }
        };


        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                handleShakeEvent(count);
            }
        });


    }

    private void handleShakeEvent(int count) {

        if (isFirstShake) {
            firstShake();
        }
        countDownTimer.cancel();
        countDownTimer.start();

    }

    private void firstShake() {
        mediaPlayer.start();
        isFirstShake = false;
        constraintLayout.setBackgroundResource(R.drawable.jakal);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        //mSensorManager.unregisterListener(mShakeDetector);
        //mediaPlayer.pause();
        super.onPause();
    }


}