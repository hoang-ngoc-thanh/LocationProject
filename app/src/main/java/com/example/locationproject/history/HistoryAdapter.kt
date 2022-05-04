package com.example.locationproject.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.locationproject.R
import com.example.locationproject.room.LocationInfo
import com.example.locationproject.utils.DateTime

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val mListData: MutableList<LocationInfo> = ArrayList()

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
        private val tvLat = itemView.findViewById<TextView>(R.id.tvLat)
        private val tvLng = itemView.findViewById<TextView>(R.id.tvLng)

        fun bind(locationInfo: LocationInfo) {
            tvTime.text = DateTime.getTime(locationInfo.time ?: 0)
            tvLat.text = locationInfo.lat.toString()
            tvLng.text = locationInfo.lng.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(mListData[position])
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    fun setListLocation(list: MutableList<LocationInfo>) {
        mListData.apply {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }
}