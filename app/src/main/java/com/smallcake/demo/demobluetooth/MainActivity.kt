package com.smallcake.demo.demobluetooth

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private  val TAG = "蓝牙Demo"
    private lateinit var btnIsSupport:Button
    private lateinit var btnIsOpen:Button
    private var mToast:Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnIsSupport = findViewById<Button>(R.id.button_is_support)
        btnIsOpen = findViewById<Button>(R.id.button_is_open)



    }
    fun isSupport(view: View){
        btnIsSupport.text =  if (BluetoothController.isSupportBluetooth())"支持蓝牙" else "不支持蓝牙"
    }
    fun isOpen(view: View){
        btnIsOpen.text =  if (BluetoothController.isOpenBluetooth())"蓝牙已打开" else "蓝牙未打开"
    }
    fun open(view: View){
        showToast("已打开蓝牙")
        BluetoothController.open(this,10000)
    }
    fun close(view: View){
        showToast("已关闭蓝牙")
        BluetoothController.close(this)
    }
    private fun showToast(text:String){
        if (mToast==null)mToast = Toast.makeText(this@MainActivity,text,Toast.LENGTH_SHORT)
        else mToast?.setText(text)
        mToast?.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==100){
            showToast("申请打开蓝牙权限")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.BLUETOOTH_CONNECT),101)
            }
        }
        if (requestCode==101){
            showToast("正在申请打开蓝牙权限")
        }
    }


}