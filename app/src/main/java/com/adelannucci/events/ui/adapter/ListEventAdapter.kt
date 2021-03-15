package com.adelannucci.events.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adelannucci.events.R
import com.adelannucci.events.model.Event
import kotlinx.android.synthetic.main.event_item.view.*

class ListEventAdapter(
    private val context: Context,
    events: List<Event> = listOf(),
    val handleClick: (id: Long) -> Unit,
) : RecyclerView.Adapter<ListEventAdapter.ViewHolder>() {

    private val events = events.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    R.layout.event_item,
                    parent,
                    false
                )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun update(events: List<Event>) {
        this.events.clear()
        this.events.addAll(events)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title = itemView.title_event
        private val description = itemView.description_text
        private val peoples = itemView.people_event
        private val image = itemView.image_event
        private lateinit var event: Event

        init {
            itemView.setOnClickListener {
                if (::event.isInitialized) {
                    handleClick(event.id)
                }
            }
        }

        fun bind(event: Event) {
            this.event = event
            setupImage()
            fillFields()
        }

        private fun setupImage() {
            if (this.event.image.isNullOrBlank()) {
                image.visibility = View.GONE
            } else {
                image.visibility = View.VISIBLE
            }
        }

        private fun fillFields() {
            title.text = this.event.title
            description.text = this.event.description
            peoples.text = "${event.people.size}"
            image.load(this.event.image)
        }

    }
}