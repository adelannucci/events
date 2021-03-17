package com.adelannucci.events.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.adelannucci.events.R
import com.adelannucci.events.databinding.FragmentListEventBinding
import com.adelannucci.events.datasource.remote.Event
import com.adelannucci.events.ui.adapter.ListEventAdapter
import com.adelannucci.events.ui.extensions.snackBar
import com.adelannucci.events.ui.viewmodel.ListEventViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListEventFragment : Fragment(R.layout.fragment_list_event) {

    private lateinit var binding: FragmentListEventBinding
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupSwipe() {
        binding.swipeRefreshEvents.setOnRefreshListener {
            fetchEvents {
                binding.swipeRefreshEvents.isRefreshing = false
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerviewEvents.adapter = adapter
    }

    private fun setupAdapter(): ListEventAdapter {
        return ListEventAdapter(
            handleClick = { id ->
                goToDetails(id)
            })
    }

    private fun fetchEvents(handle: () -> Unit = {}) {
        viewModel.findAll().observe(viewLifecycleOwner) {
            it?.let { request ->
                request.data?.let(this::update)
                request.error?.let { errorMessageId ->
                    setupVisibilityEmptyEvents(false)
                    view?.snackBar(resources.getString(errorMessageId))
                }
            }
            handle()
        }
    }

    private fun update(events: List<Event>) {
        adapter.update(events)
        setupVisibilityEmptyEvents(events.isNotEmpty())
    }

    private fun setupVisibilityEmptyEvents(hasEvents: Boolean) {
        if (hasEvents) {
            binding.recyclerviewEvents.visibility = View.VISIBLE
            binding.emptyListMessage.visibility = View.GONE
            binding.emptyList.visibility = View.GONE
        } else {
            binding.recyclerviewEvents.visibility = View.GONE
            binding.emptyListMessage.visibility = View.VISIBLE
            binding.emptyList.visibility = View.VISIBLE

        }
    }

    private fun goToDetails(id: Long) {
        ListEventFragmentDirections
            .actionListEventFragmentToDetailsEventFragment(id)
            .let(navController::navigate)
    }

}