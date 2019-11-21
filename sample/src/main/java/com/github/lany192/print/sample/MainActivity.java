package com.github.lany192.print.sample;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.lany192.print.AppInfo;
import com.github.lany192.print.bt.BluetoothActivity;
import com.github.lany192.print.print.PrintMsgEvent;
import com.github.lany192.print.print.PrintUtil;
import com.github.lany192.print.print.PrinterMsgType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BluetoothActivity implements View.OnClickListener {

     TextView tv_bluename;
     TextView tv_blueadress;
      boolean mBtEnable = true;
    int PERMISSION_REQUEST_COARSE_LOCATION=2;
    /**
     * bluetooth adapter
     */
    BluetoothAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_bluename =findViewById(R.id.tv_bluename);
        tv_blueadress =findViewById(R.id.tv_blueadress);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button).setOnClickListener(this);
        //6.0以上的手机要地理位置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
        }
        EventBus.getDefault().register(MainActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        BluetoothController.init(this);
    }




    @Override
    public void btStatusChanged(Intent intent) {
        super.btStatusChanged(intent);
        BluetoothController.init(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button4:
             startActivity(new Intent(MainActivity.this,SearchBluetoothActivity.class));
                break;
            case R.id.button5:
                if (TextUtils.isEmpty(AppInfo.btAddress)){
                    ToastUtil.showToast(MainActivity.this,"请连接蓝牙...");
                    startActivity(new Intent(MainActivity.this,SearchBluetoothActivity.class));
                }else {
                    if ( mAdapter.getState()==BluetoothAdapter.STATE_OFF ){//蓝牙被关闭时强制打开
                         mAdapter.enable();
                        ToastUtil.showToast(MainActivity.this,"蓝牙被关闭请打开...");
                    }else {
                        ToastUtil.showToast(MainActivity.this,"打印测试...");
                        Intent intent = new Intent(getApplicationContext(), BtService.class);
                        intent.setAction(PrintUtil.ACTION_PRINT_TEST);
                        startService(intent);
                    }

                }
                break;
            case R.id.button6:
                if (TextUtils.isEmpty(AppInfo.btAddress)){
                    ToastUtil.showToast(MainActivity.this,"请连接蓝牙...");
                    startActivity(new Intent(MainActivity.this,SearchBluetoothActivity.class));
                }else {
                    ToastUtil.showToast(MainActivity.this,"打印测试...");
                    Intent intent2 = new Intent(getApplicationContext(), BtService.class);
                    intent2.setAction(PrintUtil.ACTION_PRINT_TEST_TWO);
                    startService(intent2);

                }
                case R.id.button:
                if (TextUtils.isEmpty(AppInfo.btAddress)){
                    ToastUtil.showToast(MainActivity.this,"请连接蓝牙...");
                    startActivity(new Intent(MainActivity.this,SearchBluetoothActivity.class));
                }else {
                    ToastUtil.showToast(MainActivity.this,"打印图片...");
                    Intent intent2 = new Intent(getApplicationContext(), BtService.class);
                    intent2.setAction(PrintUtil.ACTION_PRINT_BITMAP);
                    startService(intent2);

                }
//                startActivity(new Intent(MainActivity.this,TextActivity.class));
                break;
        }

    }
    /**
     * handle printer message
     *
     * @param event print msg event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PrintMsgEvent event) {
        if (event.type == PrinterMsgType.MESSAGE_TOAST) {
            ToastUtil.showToast(MainActivity.this,event.msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().register(MainActivity.this);
    }
}
