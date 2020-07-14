package com.multicastdns.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.multicastdns.R
import com.multicastdns.dataClass.ScannedData
import java.util.*

class AdapterScanNetwork (context: Context?): RecyclerView.Adapter<AdapterScanNetwork.ViewHolder>() {
    var mContext: Context? = context
    var dataList: MutableList<ScannedData> =
        ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.row_data, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(@NonNull viewHolder: ViewHolder, i: Int) {
        val data = dataList[i]
        viewHolder.txt_service_ip.setText("IP Address  " + data.hostAddress)
        viewHolder.txt_service_name.setText("Service Name  " + data.serviceName)
        viewHolder.txt_service_type.setText("Service Type  " + data.serviceType)
        viewHolder.txt_service_port.setText("Port  " + data.port)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_service_name: TextView
        var txt_service_type: TextView
        var txt_service_ip: TextView
        var txt_service_port: TextView

        init {
            txt_service_port = itemView.findViewById(R.id.txt_service_port)
            txt_service_ip = itemView.findViewById(R.id.txt_service_ip)
            txt_service_name = itemView.findViewById(R.id.txt_service_name)
            txt_service_type = itemView.findViewById(R.id.txt_service_type)
        }
    }

    fun updateList(data: ScannedData) {
        dataList.add(data)
        removeDuplicate()
        notifyDataSetChanged()
    }

     fun removeDuplicate() {
        val treeList =
            TreeSet(Comparator<ScannedData> { o1, o2 ->
                o1.hostAddress.toString().compareTo(o2.hostAddress.toString())
            })
        treeList.addAll(dataList)
        dataList.clear()
        dataList.addAll(treeList)
    }

    fun refreshAdapter() {
        dataList.clear()
        notifyDataSetChanged()
    }


}