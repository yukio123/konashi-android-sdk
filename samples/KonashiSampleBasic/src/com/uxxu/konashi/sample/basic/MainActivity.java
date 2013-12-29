package com.uxxu.konashi.sample.basic;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxxu.konashi.lib.*;

public class MainActivity extends Activity {
    private static final String TAG = "KonashiSample";
    
    private KonashiManager mKonashiManager;
    private Button findButton;
    private Button onButton;
    private Button offButton;
    private Button readAioButton;
    private TextView mSwStateTextView;
    private LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mContainer = (LinearLayout)findViewById(R.id.container);
        mContainer.setVisibility(View.GONE);
        
        findButton = (Button)findViewById(R.id.find_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mKonashiManager.find(MainActivity.this);
                mKonashiManager.findWithName(MainActivity.this, "konashi#4-0452");
            }
        });
        
        onButton = (Button)findViewById(R.id.on_button);
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKonashiManager.digitalWrite(Konashi.LED2, Konashi.HIGH);
            }
        });
        
        offButton = (Button)findViewById(R.id.off_button);
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKonashiManager.digitalWrite(Konashi.LED2, Konashi.LOW);
            }
        });
        
        readAioButton = (Button)findViewById(R.id.read_aio_button);
        readAioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mKonashiManager.analogReadRequest(Konashi.AIO1);
                //mKonashiManager.reset();
                //mKonashiManager.batteryLevelReadRequest();
                mKonashiManager.signalStrengthReadRequest();
            }
        });
        
        mSwStateTextView = (TextView)findViewById(R.id.sw_state);
        mSwStateTextView.setText("OFF");
        
        // Initialize konashi manager
        mKonashiManager = new KonashiManager();
        mKonashiManager.initialize(getApplicationContext());
        mKonashiManager.addObserver(mKonashiObserver);
    }
    
    @Override
    protected void onDestroy() {
        if(mKonashiManager!=null){
            mKonashiManager.disconnect();
            mKonashiManager.close();
            mKonashiManager = null;
        }
        
        super.onDestroy();
    }

    private final KonashiObserver mKonashiObserver = new KonashiObserver(MainActivity.this) {
        @Override
        public void onConnected() {
            Log.d(TAG, "#########onConnected");
        }

        @Override
        public void onDisconncted() {
            Log.d(TAG, "#########onDisconncted");
        }

        @Override
        public void onReady(){
            Log.d(TAG, "onKonashiReady");
            
            mContainer.setVisibility(View.VISIBLE);
            
            mKonashiManager.pinMode(Konashi.LED2, Konashi.OUTPUT);
            /*mKonashiManager.pwmMode(Konashi.LED3, Konashi.PWM_ENABLE_LED_MODE);
            mKonashiManager.pwmLedDrive(Konashi.LED3, 20.0);*/
        }
        
        @Override
        public void onUpdatePioInput(){
            Log.d(TAG, "onUpdatePioInput: " + mKonashiManager.digitalRead(Konashi.S1));
            
            if(mKonashiManager.digitalRead(Konashi.S1)==Konashi.HIGH){
                mSwStateTextView.setText("ON");
            } else {
                mSwStateTextView.setText("OFF");
            }
        }
        
        @Override
        public void onUpdateBatteryLevel() {
            Log.d(TAG, "onUpdateBatteryLevel: " + mKonashiManager.getBatteryLevel());
        }

        @Override
        public void onUpdateSignalStrength() {
            Log.d(TAG, "onUpdateSignalStrength: " + mKonashiManager.getSignalStrength());
        }

        @Override
        public void onCancelSelectKonashi() {
            Log.d(TAG, "onCancelSelectKonashi");
        }
    };
}
