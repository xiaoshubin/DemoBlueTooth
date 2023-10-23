package com.smallcake.demo.demobluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.smallcake.demo.demobluetooth.BluetoothController.bluetoothAdapter
import java.util.*

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
     *  打开蓝牙
     */
    @SuppressLint("MissingPermission")
    fun open(activity: AppCompatActivity, request:Int){
//        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity,arrayOf(Manifest.permission.BLUETOOTH_CONNECT),100)
//            return
//        }
//        activity.startActivityForResult(intent,request)
        //静默打开蓝牙,无需用户同意
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
     * 系统会弹出一个对话框,确定后就是打开300秒
     */
    @SuppressLint("MissingPermission")
    fun enableVisiably(activity: AppCompatActivity){
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,10)//300秒,也就是5分钟可见
        activity.startActivity(discoverableIntent)
    }

    /**
     * 查找设备
     * 需要扫描权限
     * <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     */
    fun findDevice(context: Context){
        if (bluetoothAdapter==null)return
        XXPermissions.with(context)
            .permission(Permission.BLUETOOTH_SCAN)
            .request { permissions, all ->
                if (all)
                bluetoothAdapter.startDiscovery()
            }

    }

    /**
     * 获取蓝牙设备列表
     */
    @SuppressLint("MissingPermission")
    fun getBindDeviceList(): List<BluetoothDevice> {
        return (bluetoothAdapter.bondedDevices).toList()
    }

}