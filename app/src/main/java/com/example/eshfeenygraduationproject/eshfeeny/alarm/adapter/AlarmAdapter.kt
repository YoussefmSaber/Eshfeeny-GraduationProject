package com.example.eshfeenygraduationproject.eshfeeny.alarm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.alarm.Alarm
import com.example.eshfeenygraduationproject.databinding.AlarmItemViewBinding
import com.example.eshfeenygraduationproject.eshfeeny.alarm.fragment.AlarmFragmentDirections
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlarmAdapter : ListAdapter<Alarm, AlarmAdapter.ViewHolder>(AlarmDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = AlarmItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemBinding: AlarmItemViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(alarm: Alarm) {
            // Bind alarm data to the view
            val pattern = "hh:mm a" // Desired time format
            val sdf = SimpleDateFormat(pattern, Locale.getDefault())
            val formattedTime = sdf.format(Date(alarm.alarmTime[0]))
            itemBinding.timeTextView.text = formattedTime
            itemBinding.MedicineNameTextView.text = alarm.name
            itemBinding.MedicineDescTextView.text = alarm.notes

            itemBinding.cardVew.setOnClickListener {

            }
        }
    }
}