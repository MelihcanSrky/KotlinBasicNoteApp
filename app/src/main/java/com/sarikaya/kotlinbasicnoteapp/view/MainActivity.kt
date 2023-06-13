package com.sarikaya.kotlinbasicnoteapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.sarikaya.kotlinbasicnoteapp.R
import com.sarikaya.kotlinbasicnoteapp.databinding.ActivityMainBinding
import com.sarikaya.kotlinbasicnoteapp.storage.SharedPrefManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        systemAcitonBarColor()
        setFragmentStartDestination()
    }

    private fun systemAcitonBarColor() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
    }

    private fun setFragmentStartDestination() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navhostFragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navgraph)
        graph.setStartDestination(R.id.notesPageFragment)
        if (SharedPrefManager.getInstance(this).user.accessToken == "null") {
            graph.setStartDestination(R.id.registerPageFragment)
        }

        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }
}
