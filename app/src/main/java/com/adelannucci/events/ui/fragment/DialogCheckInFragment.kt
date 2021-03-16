package com.adelannucci.events.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.adelannucci.events.R
import com.adelannucci.events.databinding.FragmentDialogCheckInBinding
import com.adelannucci.events.datasource.remote.CheckIn
import com.adelannucci.events.ui.viewmodel.DialogCheckInViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class DialogCheckInFragment : DialogFragment() {
    private lateinit var binding: FragmentDialogCheckInBinding
    private val args by navArgs<DialogCheckInFragmentArgs>()
    private val eventId: Long by lazy {
        args.eventId
    }
    private val viewModel: DialogCheckInViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogCheckInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingButtonCheckIn()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun settingButtonCheckIn() {
        binding.buttonLogin.setOnClickListener {
            clearFields()
            val email = binding.textInputEmail.editText?.text.toString()
            val name = binding.textInputName.editText?.text.toString()
            if (validateFields(email, name)) {
                send(CheckIn(eventId = eventId, name = name, email = email))
            }
        }
    }

    private fun clearFields() {
        binding.textInputEmail.error = null
        binding.textInputName.error = null
    }

    private fun validateFields(
        email: String,
        password: String
    ): Boolean {
        var valid = true

        if (email.isBlank()) {
            binding.textInputEmail.error = resources.getString(R.string.email_field_required)
            valid = false
        }

        if (password.isBlank()) {
            binding.textInputName.error = resources.getString(R.string.name_field_required)
            valid = false
        }
        return valid
    }

    private fun setupFeedback() {
        binding.textInputEmail.visibility = View.GONE
        binding.textInputName.visibility = View.GONE
        binding.buttonLogin.visibility = View.GONE
        binding.feedback.visibility = View.VISIBLE
        binding.buttonOk.visibility = View.VISIBLE
        binding.buttonOk.setOnClickListener {
            dismiss()
        }
    }

    private fun send(checkIn: CheckIn) {
        viewModel.checkIn(checkIn).observe(viewLifecycleOwner, { isSuccessful ->
            setupFeedback()
            if (isSuccessful) {
                binding.feedback.text = getString(R.string.check_in_succesfull)
            } else {
                val message = resources.getString(R.string.generic_check_in_error)
                binding.feedback.text = message
            }

        })
    }
}