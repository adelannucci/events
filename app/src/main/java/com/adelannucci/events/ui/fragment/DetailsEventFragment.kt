package com.adelannucci.events.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.adelannucci.events.R
import com.adelannucci.events.databinding.FragmentEventDetailsBinding
import com.adelannucci.events.datasource.remote.Event
import com.adelannucci.events.ui.extensions.snackBar
import com.adelannucci.events.ui.viewmodel.DetailsEventViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DetailsEventFragment : Fragment(R.layout.fragment_event_details) {

    private lateinit var binding: FragmentEventDetailsBinding
    private val args by navArgs<DetailsEventFragmentArgs>()
    private val eventId: Long by lazy {
        args.eventId
    }
    private val viewModel: DetailsEventViewModel by viewModel()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findEvent(view)
    }

    private fun findEvent(view: View) {
        viewModel.findById(eventId).observe(viewLifecycleOwner) {
            it?.let { request ->
                request.data?.let { event ->
                    setupViews(event)
                    fillFields(event)
                    setupButtonMap(event)
                    setupButtonCheckIn(event)
                }
                request.error?.let { errorMessageId ->
                    view.snackBar(resources.getString(errorMessageId))
                    navController.popBackStack()
                }
            }
        }
    }

    private fun setupViews(event: Event) {
        if (event.image.isNullOrBlank()) {
            binding.imageEventDetails.visibility = View.GONE
        }

        binding.buttonMaps.visibility = View.VISIBLE
        binding.buttonCheckIn.visibility = View.VISIBLE
    }

    private fun setupButtonMap(event: Event) {
        binding.buttonMaps.setOnClickListener {
            val uri: String = java.lang.String.format(
                Locale.ENGLISH,
                "http://maps.google.com/maps?q=loc:${event.latitude},${event.longitude}"
            )
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }

    private fun setupButtonCheckIn(event: Event) {
        binding.buttonCheckIn.setOnClickListener {
            DetailsEventFragmentDirections
                .actionDetailsEventFragmentToDialogCheckInFragment(event.id)
                .let(navController::navigate)
        }
    }

    private fun fillFields(event: Event) {
        binding.imageEventDetails.load(event.image) {
            error(R.drawable.image_404)
            transformations(CircleCropTransformation())
            scale(Scale.FILL)
        }
        binding.peopleDetailEvent.text = event.people.size.toString()
        binding.priceDetailEvent.text = event.price.toString()
        binding.titleEventDetails.text = event.title
        binding.descriptionEventDetails.text = event.description
    }
}