package com.example.guowei.localbluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button openBT;
    private TextView tvcontent;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_CODE = 0x01;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openBT = (Button) findViewById(R.id.btn_open);
        tvcontent = (TextView) findViewById(R.id.tvcontent);
        initBlueTooth();
        openBT.setOnClickListener(this);
    }

    private void initBlueTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter==null){
            showToast("该设备不支持蓝牙");
            openBT.setEnabled(false);
            return;
        }
         openBT.setEnabled(true);
        //获取该设备的蓝牙的一些信息
        String name = mBluetoothAdapter.getName();
        String mac = mBluetoothAdapter.getAddress();
        Log.i(TAG, "initBlueTooth: bluetooth="+name+",mac="+mac);
        //获取该设备的蓝牙的状态
        int state= mBluetoothAdapter.getState();
        Log.i(TAG, "initBlueTooth: state="+state);
        switch(state){
            case BluetoothAdapter.STATE_ON:
                showToast("蓝牙打开。。");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                showToast("蓝牙正在打开。。");
                break;
            case BluetoothAdapter.STATE_OFF:
                showToast("蓝牙关闭。。");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                showToast("蓝牙正在关闭。。");
                break;
        }
        //获取该设备绑定的蓝牙设备
        Set<BluetoothDevice> bluetooths = mBluetoothAdapter.getBondedDevices();
        Log.i(TAG, "initBlueTooth:size== "+bluetooths.size());
        if(bluetooths.size()<=0){
            showToast("没有绑定的蓝牙设备");
            return;
        }
        StringBuffer s= new StringBuffer();
        for(BluetoothDevice b:bluetooths){
            String n=b.getName();
            Log.i(TAG, "initBlueTooth: "+n);
            s.append(n);
            s.append("\n");
        }
        tvcontent.setText(s.toString());

    }

    @Override
    public void onClick(View v) {
        //判断蓝牙设备是否打开
        if(mBluetoothAdapter.isEnabled()){
            showToast("蓝牙正在关闭。。");
            mBluetoothAdapter.disable();
            openBT.setText("打开蓝牙");
        }else{
//            Toast.makeText(this, "蓝牙正在打开。。", Toast.LENGTH_SHORT).show();
//            mBluetoothAdapter.enable();
            openBT.setText("关闭蓝牙");

            Intent it= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(it,REQUEST_CODE);
        }

    }
    private void showToast(String content){
      Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
   }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_CANCELED){
                showToast("请求失败");
            }else{
                showToast("请求成功");
            }
        }
    }
}
