package com.victor.aidlclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.victor.IAidl;

public class MainActivity extends Activity {

    private String TAG = "MainActivity";
    private TextView currentTv, serverTv;
    private Button sayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();
        initViews();
    }

    private void initViews() {
        currentTv = (TextView) findViewById(R.id.tv_current_pid);
        currentTv.setText(android.os.Process.myPid() + "");
        serverTv = (TextView) findViewById(R.id.tv_server_pid);
        sayBtn = (Button) findViewById(R.id.btn_say);
        sayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = myAidl.sayHello("Victor");
                    int pid = myAidl.getPid();
                    serverTv.setText(result + "服务端PID为:" + pid);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private IAidl myAidl;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            myAidl = IAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }
    };

    private void initService() {
        //传入Server端的service的名字供本进程绑定
        Intent intent = new Intent("android.intent.action.AIDLService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }
}
