package com.adelannucci.events.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.adelannucci.events.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
    }

    private fun setupNavigation() {
        navController.addOnDestinationChangedListener {
                _,
                destination,
                _,
            ->
            title = destination.label

        }
    }
}