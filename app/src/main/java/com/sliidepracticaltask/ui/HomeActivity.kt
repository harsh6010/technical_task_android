package com.sliidepracticaltask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import com.sliidepracticaltask.R
import com.sliidepracticaltask.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val mNavController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)

    }
    lateinit var mBinding: ActivityHomeBinding
    private lateinit var navGraph: NavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        init()
        setup()
    }

    private fun init() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        navGraph = mNavController.navInflater.inflate(R.navigation.home_nav_graph)

    }

    private fun setup() {
        // Code to switch default starting destination in nav graph
        /*navGraph.startDestination = R.id.dashboardFragment
        mNavController.graph = navGraph*/
    }
}