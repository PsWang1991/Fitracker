package com.pinhsiang.fitracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.pinhsiang.fitracker.databinding.ActivityMainBinding
import com.pinhsiang.fitracker.inbody.InbodyFragmentDirections
import com.pinhsiang.fitracker.nutrition.NutritionFragmentDirections
import com.pinhsiang.fitracker.user.UserManager
import com.pinhsiang.fitracker.util.CurrentFragmentType
import com.pinhsiang.fitracker.workout.WorkoutFragmentDirections

const val TAG = "Fitracker"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    /**
     * Lazily initialize our [MainViewModel].
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
        toggle = ActionBarDrawerToggle(
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
            when (viewModel.currentFragmentType.value) {
                CurrentFragmentType.WORKOUT -> goToHomeScreen()
                CurrentFragmentType.WORKOUT_ANALYSIS -> goToHomeScreen()
                CurrentFragmentType.NUTRITION -> goToHomeScreen()
                CurrentFragmentType.NUTRITION_ANALYSIS -> goToHomeScreen()
                CurrentFragmentType.INBODY -> goToHomeScreen()
                CurrentFragmentType.INBODY_ANALYSIS -> goToHomeScreen()
                else -> super.onBackPressed()
            }
        }
    }

    // For drawer.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navControllerDrawer = findNavController(R.id.main_page_fragment)
        when (item.itemId) {
            R.id.nav_timer -> {
                navControllerDrawer.navigate(NavGraphDirections.actionGlobalTimerFragment())
            }
            R.id.nav_tdee -> {
                navControllerDrawer.navigate(NavGraphDirections.actionGlobalTDEEFragment())
            }
            R.id.nav_1rm -> {
                navControllerDrawer.navigate(NavGraphDirections.actionGlobalRMFragment())
            }
            R.id.nav_recommended -> {
                navControllerDrawer.navigate(NavGraphDirections.actionGlobalRecommendedFragment())
            }
            R.id.nav_log_out -> {
                val auth = FirebaseAuth.getInstance()
                UserManager.userUid = ""
                UserManager.userDocId = ""
                Log.i(TAG, "current user = ${auth.currentUser}")
                auth.signOut()
                Log.i(TAG, "current user = ${auth.currentUser}")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
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
                    supportActionBar?.title = getString(R.string.workout_title)
                }
                R.id.motionFragment -> CurrentFragmentType.WORKOUT_MOTION.apply {
                    supportActionBar?.title = getString(R.string.motion_title)
                }
                R.id.workoutRecordFragment -> CurrentFragmentType.WORKOUT_RECORD.apply {
                    supportActionBar?.title = getString(R.string.workout_record_title)
                }
                R.id.workoutAnalysisFragment -> CurrentFragmentType.WORKOUT_ANALYSIS.apply {
                    supportActionBar?.title = getString(R.string.workout_analysis_title)
                }
                R.id.nutritionFragment -> CurrentFragmentType.NUTRITION.apply {
                    supportActionBar?.title = getString(R.string.nutrition_title)
                }
                R.id.nutritionRecordFragment -> CurrentFragmentType.NUTRITION_RECORD.apply {
                    supportActionBar?.title = getString(R.string.nutrition_record_title)
                }
                R.id.nutritionAnalysisFragment -> CurrentFragmentType.NUTRITION_ANALYSIS.apply {
                    supportActionBar?.title = getString(R.string.nutrition_analysis_title)
                }
                R.id.inbodyFragment -> CurrentFragmentType.INBODY.apply {
                    supportActionBar?.title = getString(R.string.inbody_title)
                }
                R.id.inbodyRecordFragment -> CurrentFragmentType.INBODY_RECORD.apply {
                    supportActionBar?.title = getString(R.string.inbody_record_title)
                }
                R.id.inbodyAnalysisFragment -> CurrentFragmentType.INBODY_ANALYSIS.apply {
                    supportActionBar?.title = getString(R.string.inbody_analysis_title)
                }
                R.id.timerFragment -> CurrentFragmentType.TIMER.apply {
                    supportActionBar?.title = getString(R.string.timer_title)
                }
                R.id.recommendedFragment -> CurrentFragmentType.RECOMMENDED.apply {
                    supportActionBar?.title = getString(R.string.recommended_title)
                }
                R.id.RMFragment -> CurrentFragmentType.ONE_RM.apply {
                    supportActionBar?.title = getString(R.string.one_rm_title)
                }
                R.id.TDEEFragment -> CurrentFragmentType.TDEE.apply {
                    supportActionBar?.title = getString(R.string.total_daily_energy_extracted)
                }
                else -> viewModel.currentFragmentType.value
            }
            when (navController.currentDestination?.id) {
                R.id.timerFragment -> toggle.isDrawerIndicatorEnabled = false
                R.id.recommendedFragment -> toggle.isDrawerIndicatorEnabled = false
                R.id.RMFragment -> toggle.isDrawerIndicatorEnabled = false
                R.id.TDEEFragment -> toggle.isDrawerIndicatorEnabled = false
                else -> toggle.isDrawerIndicatorEnabled = true
            }
        }
    }

    private fun goToHomeScreen() {
        val intent =
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
