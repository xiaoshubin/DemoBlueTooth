package com.smallcake.demo.demobluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothClass
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.util.*

class MainActivity : AppCompatActivity() {
    private  val TAG = "蓝牙Demo"
    private lateinit var btnIsSupport:Button
    private lateinit var btnIsOpen:Button
    private var mToast:Toast? = null
    private lateinit var recyclerView:RecyclerView
    private val mAdapter = DeviceAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnIsSupport = findViewById<Button>(R.id.button_is_support)
        btnIsOpen = findViewById<Button>(R.id.button_is_open)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener{adapter,view,position->
            val item = adapter.getItem(position) as BluetoothDevice
            AlertDialog.Builder(this@MainActivity)
                .setTitle("提醒")
                .setMessage("是否绑定设备:${item.name}")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定") { dialog, which ->
                    XXPermissions.with(this)
                        .permission(Permission.BLUETOOTH_CONNECT)
                        .request{p,all->
                            if (all)item.createBond()//点击后进行绑定
                        }

                }
                .create().show()

        }
        registerBluetoothReceiver()


    }

    /**
     * 注册蓝牙广播
     */
    private fun registerBluetoothReceiver() {
        //注册广播,监听蓝牙状态
        val filter = IntentFilter()
        //开始查找
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        //结束查找
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        //查找设备
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        //设备扫描模式改变
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        //绑定状态
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        //注册广播
        registerReceiver(mReceiver, filter)
    }

    private val  mReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            when(action){
                BluetoothAdapter.ACTION_DISCOVERY_STARTED->{
                    Log.i(TAG,"开始查找")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED->{
                    Log.i(TAG,"结束查找")
                    showToast("结束查找")
                }
                BluetoothAdapter.ACTION_SCAN_MODE_CHANGED->{
                    val scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,0)
                    Log.i(TAG,"设备扫描模式改变[$scanMode]")
                    if (scanMode==BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){//状态可见
                        Log.i(TAG,"设备扫描模式改变[$scanMode]")
                    }else{
                        Log.i(TAG,"设备扫描模式改变[$scanMode]")
                    }
                }
                BluetoothDevice.ACTION_FOUND->{
                    Log.i(TAG,"查找设备")
                    val device : BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val isAdd =  mAdapter.items.find { it.address==device?.address }!=null
                    if (!isAdd)device?.let { mAdapter.add(it) }
                }
                /**
                 * 蓝牙绑定状态：
                BOND_NONE：远程设备未绑定。
                BOND_BONDING：正在与远程设备进行绑定。
                BOND_BONDED：远程设备已绑定。
                 只有没有绑定过的设备才能绑定,否则调用createBond()后,这里不会接收到绑定状态的改变
                 */
                BluetoothDevice.ACTION_BOND_STATE_CHANGED->{
                    Log.i(TAG,"绑定状态")
                    val device : BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device==null){
                        showToast("暂无设备")
                        return
                    }
                    when(intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,0)){
                        BluetoothDevice.BOND_BONDED->Log.i(TAG,"Bonded ${device.name}")
                        BluetoothDevice.BOND_BONDING->Log.i(TAG,"Bonding ${device.name}")
                        BluetoothDevice.BOND_NONE->Log.i(TAG,"not bond ${device.name}")

                    }

                }
            }
        }

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

    /**
     * 会弹出一个权限允许框,提示是否在300秒可以被其他设备发现
     */
    fun enableVisiably(view: View){
        BluetoothController.enableVisiably(this)
    }
    fun find(view: View){
        mAdapter.submitList(null)
        BluetoothController.findDevice(this)
    }
    fun getDeviceList(view: View){
        val list = BluetoothController.getBindDeviceList()
        mAdapter.submitList(list)
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