package com.smallcake.demo.demobluetooth

import android.bluetooth.BluetoothDevice
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class DeviceAdapter : BaseQuickAdapter<BluetoothDevice, BaseViewHolder>(R.layout.item_device) {

    override fun convert(holder: BaseViewHolder, item: BluetoothDevice) {
        holder.getView<TextView>(R.id.textView).text = "${item.name} - ${item.address}"
    }


}