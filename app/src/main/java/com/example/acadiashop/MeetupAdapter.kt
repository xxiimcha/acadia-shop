package com.example.acadiashop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MeetupAdapter(private val meetups: List<Meetup>) : RecyclerView.Adapter<MeetupAdapter.MeetupViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetupViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_meetup, parent, false)
        return MeetupViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeetupViewHolder, position: Int) {
        val meetup = meetups[position]
        holder.bind(meetup)
    }

    override fun getItemCount() = meetups.size

    class MeetupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMeetPlace: TextView = itemView.findViewById(R.id.tvMeetPlace)
        private val tvMeetTime: TextView = itemView.findViewById(R.id.tvMeetTime)

        fun bind(meetup: Meetup) {
            tvMeetPlace.text = meetup.meet_place
            tvMeetTime.text = meetup.meet_time
        }
    }
}
