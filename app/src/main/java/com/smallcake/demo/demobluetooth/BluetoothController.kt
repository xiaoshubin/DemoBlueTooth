package com.smallcake.demo.demobluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.smallcake.demo.demobluetooth.BluetoothController.bluetoothAdapter

/**
 * 蓝牙适配器
 */
object BluetoothController {
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    //是否支持蓝牙
    fun isSupportBluetooth():Boolean = bluetoothAdapter!=null
    //判断当前蓝牙状态,true打开，false关闭或不支持蓝牙
    fun isOpenBluetooth():Boolean = bluetoothAdapter?.isEnabled?:false
    /**
     *  打开蓝牙,需要申请蓝牙连接权限
     */
    @SuppressLint("MissingPermission")
    fun open(activity: AppCompatActivity, request:Int){
//        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity,arrayOf(Manifest.permission.BLUETOOTH_CONNECT),100)
//            return
//        }
//        activity.startActivityForResult(intent,request)
        bluetoothAdapter.enable()
    }
    /**
     * 关闭蓝牙
     */
    @SuppressLint("MissingPermission")
    fun close(activity: AppCompatActivity){
        bluetoothAdapter?.disable()
    }
    /**
     * 打开蓝牙的可见性
     */
    @SuppressLint("MissingPermission")
    fun enableVisiably(activity: AppCompatActivity){
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300)//300秒,也就是5分钟可见
        activity.startActivity(discoverableIntent)
    }

    /**
     * 查找设备
     */
    @SuppressLint("MissingPermission")
    fun findDevice(){
        if (bluetoothAdapter==null)return
        bluetoothAdapter.startDiscovery()
    }

    /**
     * 获取蓝牙设备列表
     */
    @SuppressLint("MissingPermission")
    fun getBindDeviceList(): List<BluetoothDevice>? {
        return (bluetoothAdapter.bondedDevices).toList()
    }

}