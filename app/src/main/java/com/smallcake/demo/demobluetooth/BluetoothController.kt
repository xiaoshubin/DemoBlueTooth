package com.smallcake.demo.demobluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * 蓝牙适配器
 */
object BluetoothController {
    private const val TAG = "BluetoothController"
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
    /**
     * 与扫描到的设备进行配对
     */

    fun pair(device: BluetoothDevice){
        //在配对之前，停止搜索
        //在配对之前，停止搜索
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery()
        }
        if (device.bondState !== BluetoothDevice.BOND_BONDED) { //没配对才配对
            try {
                val createBondMethod: Method = BluetoothDevice::class.java.getMethod("createBond")
                val returnValue = createBondMethod.invoke(device) as Boolean
                if (returnValue) {
                    Log.d(TAG, "配对成功...")
                }
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
        }else{
            Log.d(TAG, "已经配对过了...")
        }
    }
    fun unpair(device: BluetoothDevice) {
        Log.d(TAG, "attemp to cancel bond:" + device.name)
        try {
            val removeBondMethod = device.javaClass.getMethod("removeBond")
            val returnValue = removeBondMethod.invoke(device) as Boolean
            if (returnValue) {
                Log.d(TAG, "解配对成功...")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "attemp to cancel bond fail!")
        }
    }
}