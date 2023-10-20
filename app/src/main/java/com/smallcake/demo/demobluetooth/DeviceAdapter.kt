package com.smallcake.demo.demobluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder


class DeviceAdapter : BaseQuickAdapter<BluetoothDevice, QuickViewHolder>() {
    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: BluetoothDevice?) {
        holder.getView<TextView>(R.id.textView).text = item?.name
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_device, parent)
    }


}