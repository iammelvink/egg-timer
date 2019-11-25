package com.iammelvink.egg_timer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private TextView timerText;
    private SeekBar timerSeekBar;
    private Button goBtn;
    private MediaPlayer mPlayer;
    private boolean counterActive = false;

    public void resetTimer() {
        timerText.setText("00:00");
        timerSeekBar.setProgress(0);
        timerSeekBar.setEnabled(true);
        countDownTimer.cancel();
        mPlayer.start();
        goBtn.setText("GO!");
        counterActive = false;
    }

    public void startTimerNow(View view) {

        /*Stop timer*/
        if (counterActive) {

            resetTimer();

            Log.i("Timer stopped: ", "0");
            /*Start timer*/
        } else {
            counterActive = true;
            timerSeekBar.setEnabled(false);
            goBtn.setText("STOP!");

            Log.i("Button event: ", "button was pressed!");

            /*
             *timerSeekBar.getProgress() * 1000 +100
             * +100 milliseconds fixes update to zero lag */
            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.i("Time left: ", Long.toString(millisUntilFinished));

                    /*Save current time to variable*/
                    int timerProgress = updateTimer((int) millisUntilFinished / 1000);

                    /*Sync timer text to SeekBar location/progress*/
                    timerSeekBar.setProgress(timerProgress);
                }

                @Override
                public void onFinish() {
                    Log.i("Timer over: ", "0");

                    /*Play audio when timer is done*/
                    mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                    mPlayer.start();
                    /*Sync timer text to SeekBar location/progress*/

                    resetTimer();
                }
            }.start();
        }
    }

    public int updateTimer(int progress) {
        /*Get minutes*/
        int minutes = progress / 60;

        /*Get seconds*/
        int seconds = progress - (minutes * 60);

        String minString = Integer.toString(minutes);
        String secString = Integer.toString(seconds);

        /*Put zero in front of single digit numbers*/
        if (minutes < 10) {
            minString = "0" + minString;
        }
        if (seconds < 10) {
            secString = "0" + secString;
        }

        /*Set times to TextView*/
        timerText.setText(minString + ":" + secString);

        return progress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Getting SeekBar and TextView*/
        timerSeekBar = (SeekBar) findViewById(R.id.TimerSeekBar);
        timerText = (TextView) findViewById(R.id.lblTime);
        goBtn = (Button) findViewById(R.id.btnGo);

        /*30 minutes = 1800 seconds*/
        final int MAX = 1800;

        /*Default to 0 seconds*/
        final int STARTINGPOSITION = 0;

        timerSeekBar.setMax(MAX);
        timerSeekBar.setProgress(STARTINGPOSITION);

        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
