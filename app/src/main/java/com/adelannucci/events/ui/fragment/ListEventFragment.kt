package com.adelannucci.events.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.adelannucci.events.R
import com.adelannucci.events.model.Event
import com.adelannucci.events.ui.adapter.ListEventAdapter
import com.adelannucci.events.ui.extensions.snackBar
import com.adelannucci.events.ui.viewmodel.ListEventViewModel
import kotlinx.android.synthetic.main.fragment_list_event.*
import org.koin.android.viewmodel.ext.android.viewModel

class ListEventFragment : Fragment(R.layout.fragment_list_event) {

    private val viewModel: ListEventViewModel by viewModel()
    private val navController: NavController by lazy {
        findNavController()
    }

    private val adapter by lazy {
        setupAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipe()
        fetchEvents()
    }

    private fun setupSwipe() {
        swipe_refresh_events.setOnRefreshListener {
            fetchEvents {
                swipe_refresh_events.isRefreshing = false
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerview_events.adapter = adapter
    }

    private fun setupAdapter(): ListEventAdapter {
        return ListEventAdapter(
            context = requireContext(),
            handleClick = { id ->
                goToDetails(id)
            })
    }

    private fun fetchEvents(handle: () -> Unit = {}) {
        viewModel.findAll().observe(viewLifecycleOwner) {
            it?.let { request ->
                request.data?.let(this::update)
                request.error?.let { error ->
                    view?.snackBar(error)
                }
            }
            handle()
        }
    }

    private fun update(events: List<Event>) {
        adapter.update(events)
    }

    private fun goToDetails(id: Long) {
        ListEventFragmentDirections
            .actionListEventFragmentToDetailsEventFragment(id)
            .let(navController::navigate)
    }

}