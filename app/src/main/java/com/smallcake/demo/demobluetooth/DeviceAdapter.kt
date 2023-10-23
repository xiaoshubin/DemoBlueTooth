package com.smallcake.demo.demobluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder


class DeviceAdapter : BaseQuickAdapter<BluetoothDevice, QuickViewHolder>() {
    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: BluetoothDevice?) {
        if (item==null)return
        holder.getView<TextView>(R.id.textView).text = "${item.name} - ${item.address}"
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_device, parent)
    }


}