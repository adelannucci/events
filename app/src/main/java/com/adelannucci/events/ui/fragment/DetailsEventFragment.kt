package com.adelannucci.events.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.adelannucci.events.R
import com.adelannucci.events.model.Event
import com.adelannucci.events.ui.extensions.snackBar
import com.adelannucci.events.ui.viewmodel.DetailsEventViewModel
import kotlinx.android.synthetic.main.fragment_event_details.*
import org.koin.android.viewmodel.ext.android.viewModel


class DetailsEventFragment : Fragment(R.layout.fragment_event_details) {

    private val args by navArgs<DetailsEventFragmentArgs>()
    private val eventId: Long by lazy {
        args.eventId
    }
    private val viewModel: DetailsEventViewModel by viewModel()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findEvent(view)
    }

    private fun findEvent(view: View) {
        viewModel.findById(eventId).observe(viewLifecycleOwner) {
            it?.let { request ->
                request.data?.let { evento ->
                    setupViews(evento)
                    fillFields(evento)
                }
                request.error?.let { message ->
                    view.snackBar(message)
                    navController.popBackStack()
                }
            }
        }
    }

    private fun setupViews(event: Event) {
        if (event.image.isNullOrBlank()) {
            image_event_details.visibility = View.GONE
        }
    }

    private fun fillFields(event: Event) {
        image_event_details.load(event.image)
        people_detail_event.text = event.people.size.toString()
        price_detail_event.text = event.price.toString()
        title_event_details.text = event.title
        description_event_details.text = event.description
    }
}