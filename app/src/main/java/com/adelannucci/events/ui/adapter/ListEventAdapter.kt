package com.adelannucci.events.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.adelannucci.events.databinding.EventItemBinding
import com.adelannucci.events.datasource.remote.Event

class ListEventAdapter(
    private val context: Context,
    events: List<Event> = listOf(),
    val handleClick: (id: Long) -> Unit,
) : RecyclerView.Adapter<ListEventAdapter.ViewHolder>() {

    private val events = events.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: EventItemBinding =
            EventItemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun update(events: List<Event>) {
        this.events.clear()
        this.events.addAll(events)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: EventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
            setupView()
        }

        private fun setupImage() {
            if (this.event.image.isNullOrBlank()) {
                binding.imageEvent.visibility = View.GONE
            } else {
                binding.imageEvent.visibility = View.VISIBLE
            }
        }

        private fun setupView() {
            binding.titleEvent.text = this.event.title
            binding.descriptionText.text = this.event.description
            binding.peopleEvent.text = "${event.people.size}"
            binding.imageEvent.load(this.event.image)
        }

    }
}