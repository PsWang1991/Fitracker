package com.pinhsiang.fitracker

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pinhsiang.fitracker.databinding.ActivityMainBinding
import com.pinhsiang.fitracker.inbody.InbodyFragmentDirections
import com.pinhsiang.fitracker.nutrition.NutritionFragmentDirections
import com.pinhsiang.fitracker.nutrition.analysis.NutritionAnalysisFragmentDirections
import com.pinhsiang.fitracker.util.CurrentFragmentType
import com.pinhsiang.fitracker.workout.WorkoutFragmentDirections

const val TAG = "Fitracker"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding

    /**
     * Lazily initialize our [OverviewViewModel].
     */
    private lateinit var viewModelFactory: MainViewModelFactory
    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val navControllerBottom = findNavController(R.id.main_page_fragment)
        when (item.itemId) {
            R.id.navigation_workout -> {
                navControllerBottom.navigate(NavGraphDirections.actionGlobalWorkoutFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_nutrition -> {
                navControllerBottom.navigate(NavGraphDirections.actionGlobalNutritionFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_inbody -> {
                navControllerBottom.navigate(NavGraphDirections.actionGlobalInbodyFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModelFactory = MainViewModelFactory()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Set toolbar.
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Set onclick listener of record/analysis icon on toolbar.
        binding.recordOrAnalysis.setOnClickListener {
            val navControllerBottom = findNavController(R.id.main_page_fragment)
            when (viewModel.currentFragmentType.value) {
                CurrentFragmentType.WORKOUT ->
                    navControllerBottom.navigate(
                        WorkoutFragmentDirections.ActionWorkoutFragmentToWorkoutAnalysisFragment()
                    )
                CurrentFragmentType.WORKOUT_ANALYSIS ->
                    navControllerBottom.navigate(
                        NavGraphDirections.actionGlobalWorkoutFragment()
                    )
                CurrentFragmentType.NUTRITION ->
                    navControllerBottom.navigate(
                        NutritionFragmentDirections.ActionNutritionFragmentToNutritionAnalysisFragment()
                    )
                CurrentFragmentType.NUTRITION_ANALYSIS ->
                    navControllerBottom.navigate(
                        NavGraphDirections.actionGlobalNutritionFragment()
                    )
                CurrentFragmentType.INBODY ->
                    navControllerBottom.navigate(
                        InbodyFragmentDirections.ActionInbodyFragmentToInbodyAnalysisFragment()
                    )
                CurrentFragmentType.INBODY_ANALYSIS ->
                    navControllerBottom.navigate(
                        NavGraphDirections.actionGlobalInbodyFragment()
                    )

            }

        }

        // Set drawer
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        // Set bottom navigation view
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Observe current fragment
        viewModel.currentFragmentType.observe(this, Observer {
            Log.i(TAG, "********************************************")
            Log.i(TAG, "** ${viewModel.currentFragmentType.value} **")
            Log.i(TAG, "********************************************")
        })

        setupNavController()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_timer -> {
                Toast.makeText(applicationContext, "Timer", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_tdee -> {
                Toast.makeText(applicationContext, "TDEE", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_1rm -> {
                Toast.makeText(applicationContext, "1 RM", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_recommended -> {
                Toast.makeText(applicationContext, "247365", Toast.LENGTH_SHORT).show()
            }
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Set up [NavController.addOnDestinationChangedListener] to record the current fragment, it is better than another design
     * which is change the [CurrentFragmentType] enum value by [MainViewModel] at [onCreateView].
     * Also change the background of record/analysis icon on toolbar.
     */
    private fun setupNavController() {
        findNavController(R.id.main_page_fragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.workoutFragment -> CurrentFragmentType.WORKOUT.apply {
                    binding.recordOrAnalysis.background = FitrackerApplication.appContext.getDrawable(R.drawable.ic_show_chart_black_24dp)
                }
                R.id.workoutAnalysisFragment -> CurrentFragmentType.WORKOUT_ANALYSIS.apply {
                    binding.recordOrAnalysis.background = FitrackerApplication.appContext.getDrawable(R.drawable.ic_record_black_24dp)
                }
                R.id.nutritionFragment -> CurrentFragmentType.NUTRITION.apply {
                    binding.recordOrAnalysis.background = FitrackerApplication.appContext.getDrawable(R.drawable.ic_show_chart_black_24dp)
                }
                R.id.nutritionAnalysisFragment -> CurrentFragmentType.NUTRITION_ANALYSIS.apply {
                    binding.recordOrAnalysis.background = FitrackerApplication.appContext.getDrawable(R.drawable.ic_record_black_24dp)
                }
                R.id.inbodyFragment -> CurrentFragmentType.INBODY.apply {
                    binding.recordOrAnalysis.background = FitrackerApplication.appContext.getDrawable(R.drawable.ic_show_chart_black_24dp)
                }
                R.id.inbodyAnalysisFragment -> CurrentFragmentType.INBODY_ANALYSIS.apply {
                    binding.recordOrAnalysis.background = FitrackerApplication.appContext.getDrawable(R.drawable.ic_record_black_24dp)
                }
                else -> viewModel.currentFragmentType.value
            }
        }
    }
}
