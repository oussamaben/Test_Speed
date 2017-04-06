package com.example.dell.test_speed;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecimalFormater=new DecimalFormat("##.##");

        //Request the progress bar to be shown in the title

        requestWindowFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_main);
        bindListeners();

    }

    private void bindListeners() {
       Button mBtnStart = (Button) findViewById(R.id.btnStart);

      TextView mTxtSpeed = (TextView) findViewById(R.id.speed);

       TextView mTxtConnectionSpeed = (TextView) findViewById(R.id.connectionspeeed);

      TextView mTxtProgress = (TextView) findViewById(R.id.progress);

       TextView mTxtNetwork = (TextView) findViewById(R.id.networktype);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressBarVisibility(true);

                mTxtSpeed.setText("Test started");

                mBtnStart.setEnabled(false);

                mTxtNetwork.setText(R.string.network_detecting);

                new Thread(mWorker).start();
            }
        });

        }
    private final Handler mHandler=new Handler(){

        @Override

        public void handleMessage(final Message msg) {

            switch(msg.what){

                case MSG_UPDATE_STATUS:

                    final SpeedInfo info1=(SpeedInfo) msg.obj;

                    mTxtSpeed.setText(String.format(getResources().getString(R.string.update_speed), mDecimalFormater.format(info1.kilobits)));

                    // Title progress is in range 0..10000

                    setProgress(100 * msg.arg1);

                    mTxtProgress.setText(String.format(getResources().getString(R.string.update_downloaded), msg.arg2, EXPECTED_SIZE_IN_BYTES));

                    break;

                case MSG_UPDATE_CONNECTION_TIME:

                    mTxtConnectionSpeed.setText(String.format(getResources().getString(R.string.update_connectionspeed), msg.arg1));

                    break;

                case MSG_COMPLETE_STATUS:

                    final  SpeedInfo info2=(SpeedInfo) msg.obj;

                    mTxtSpeed.setText(String.format(getResources().getString(R.string.update_downloaded_complete), msg.arg1, info2.kilobits));


                    mTxtProgress.setText(String.format(getResources().getString(R.string.update_downloaded), msg.arg1, EXPECTED_SIZE_IN_BYTES));



                    if(networkType(info2.kilobits)==1){

                        mTxtNetwork.setText(R.string.network_3g);

                    }
                    else{

                        mTxtNetwork.setText(R.string.network_edge);

                    }


                    mBtnStart.setEnabled(true);

                    setProgressBarVisibility(false);

                    break;

                default:

                    super.handleMessage(msg);

            }
        }
    };



}

