package com.pinhsiang.fitracker

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.pinhsiang.fitracker.databinding.ActivityMainBinding
import com.pinhsiang.fitracker.databinding.NavHeaderMainBinding
import com.pinhsiang.fitracker.ext.getVmFactory
import com.pinhsiang.fitracker.inbody.InBodyFragmentDirections
import com.pinhsiang.fitracker.nutrition.NutritionFragmentDirections
import com.pinhsiang.fitracker.user.UserManager
import com.pinhsiang.fitracker.util.CurrentFragmentType
import com.pinhsiang.fitracker.workout.WorkoutFragmentDirections

const val TAG = "Fitracker"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var navHeaderBinding: NavHeaderMainBinding

    /**
     * Lazily initialize our [MainViewModel].
     */
    private val viewModel by viewModels<MainViewModel> { getVmFactory() }

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

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Set onclick listener of overview/analysis icon on the right of toolbar.
        binding.overviewOrAnalysis.setOnClickListener {

            val navController = findNavController(R.id.main_page_fragment)

            when (viewModel.currentFragmentType.value) {

                CurrentFragmentType.WORKOUT ->
                    navController.navigate(
                        WorkoutFragmentDirections.ActionWorkoutFragmentToWorkoutAnalysisFragment()
                    )

                CurrentFragmentType.WORKOUT_ANALYSIS ->
                    navController.navigate(
                        NavGraphDirections.actionGlobalWorkoutFragment()
                    )

                CurrentFragmentType.NUTRITION ->
                    navController.navigate(
                        NutritionFragmentDirections.ActionNutritionFragmentToNutritionAnalysisFragment()
                    )

                CurrentFragmentType.NUTRITION_ANALYSIS ->
                    navController.navigate(
                        NavGraphDirections.actionGlobalNutritionFragment()
                    )

                CurrentFragmentType.INBODY ->
                    navController.navigate(
                        InBodyFragmentDirections.ActionInbodyFragmentToInbodyAnalysisFragment()
                    )

                CurrentFragmentType.INBODY_ANALYSIS ->
                    navController.navigate(
                        NavGraphDirections.actionGlobalInbodyFragment()
                    )

                else -> {
                }
            }
        }

        // On tool fragments, hide drawer icon, the only way to exit tool fragments is clicking back key or up key.
        binding.btnBackToOverview.setOnClickListener {
            onBackPressed()
        }

        toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navHeaderBinding = NavHeaderMainBinding.inflate(LayoutInflater.from(this), binding.navView, false)
        navHeaderBinding.viewModel = viewModel
        navHeaderBinding.lifecycleOwner = this

        setupDrawer()

        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Observe current fragment
        viewModel.currentFragmentType.observe(this, Observer {
            Log.i(TAG, "********************************************")
            Log.i(TAG, "** ${viewModel.currentFragmentType.value} **")
            Log.i(TAG, "********************************************")
        })

        setupNavController()
    }

    // Get the height of status bar from system to set top padding of nav_header.
    private val statusBarHeight: Int
        get() {
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            return when {
                resourceId > 0 -> resources.getDimensionPixelSize(resourceId)
                else -> 0
            }
        }

    private fun setupDrawer() {

        val navView: NavigationView = binding.navView
        navView.addHeaderView(navHeaderBinding.root)
        navView.setNavigationItemSelectedListener(this)
        navView.getHeaderView(0).setPadding(0, statusBarHeight, 0, 0)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val tools = navView.menu.findItem(R.id.tools)
        val spannableString = SpannableString(tools.title)
        spannableString.setSpan(TextAppearanceSpan(this, R.style.NavViewTitle), 0, spannableString.length, 0)
        tools.title = spannableString
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

    // Drawer navigation
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

                clearUserInfo()
                FirebaseAuth.getInstance().signOut()
                toLoginActivity()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun clearUserInfo() {
        UserManager.userUid = ""
        UserManager.userDocId = ""
        UserManager.userName = ""
        UserManager.userEmail = ""
        UserManager.userAvatarUrl = ""
    }

    private fun toLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
     *  Set up [NavController.addOnDestinationChangedListener] to record the current fragment,
     *  it is better than another design which changes the [CurrentFragmentType] enum value
     *  by [MainViewModel] at [onCreateView].
     */
    private fun setupNavController() {

        val toolbarTitleMainPage = binding.titleToolbar
        val toolbarTitleTools = binding.titleToolbarTools

        findNavController(R.id.main_page_fragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value =
                when (navController.currentDestination?.id) {

                    R.id.workoutFragment -> CurrentFragmentType.WORKOUT.apply {
                        toolbarTitleMainPage.text = getString(R.string.workout_title)
                    }

                    R.id.motionFragment -> CurrentFragmentType.WORKOUT_MOTION.apply {
                        toolbarTitleMainPage.text = getString(R.string.motion_title)
                    }

                    R.id.workoutRecordFragment -> CurrentFragmentType.WORKOUT_RECORD.apply {
                        toolbarTitleMainPage.text = getString(R.string.workout_record_title)
                    }

                    R.id.workoutAnalysisFragment -> CurrentFragmentType.WORKOUT_ANALYSIS.apply {
                        toolbarTitleMainPage.text = getString(R.string.workout_analysis_title)
                    }

                    R.id.nutritionFragment -> CurrentFragmentType.NUTRITION.apply {
                        toolbarTitleMainPage.text = getString(R.string.nutrition_title)
                    }

                    R.id.nutritionRecordFragment -> CurrentFragmentType.NUTRITION_RECORD.apply {
                        toolbarTitleMainPage.text = getString(R.string.nutrition_record_title)
                    }

                    R.id.nutritionAnalysisFragment -> CurrentFragmentType.NUTRITION_ANALYSIS.apply {
                        toolbarTitleMainPage.text = getString(R.string.nutrition_analysis_title)
                    }

                    R.id.inbodyFragment -> CurrentFragmentType.INBODY.apply {
                        toolbarTitleMainPage.text = getString(R.string.inbody_title)
                    }

                    R.id.inbodyRecordFragment -> CurrentFragmentType.INBODY_RECORD.apply {
                        toolbarTitleMainPage.text = getString(R.string.inbody_record_title)
                    }

                    R.id.inbodyAnalysisFragment -> CurrentFragmentType.INBODY_ANALYSIS.apply {
                        toolbarTitleMainPage.text = getString(R.string.inbody_analysis_title)
                    }

                    R.id.timerFragment -> CurrentFragmentType.TIMER.apply {
                        toolbarTitleTools.text = getString(R.string.timer_title)
                    }

                    R.id.recommendedFragment -> CurrentFragmentType.RECOMMENDED.apply {
                        toolbarTitleTools.text = getString(R.string.recommended_title)
                    }

                    R.id.RMFragment -> CurrentFragmentType.ONE_RM.apply {
                        toolbarTitleTools.text = getString(R.string.one_rm_title)
                    }

                    R.id.TDEEFragment -> CurrentFragmentType.TDEE.apply {
                        toolbarTitleTools.text = getString(R.string.tdee_title)
                    }

                    else -> viewModel.currentFragmentType.value
                }

            when (navController.currentDestination?.id) {

                R.id.timerFragment -> {
                    toggle.isDrawerIndicatorEnabled = false
                }

                R.id.recommendedFragment -> {
                    toggle.isDrawerIndicatorEnabled = false
                }

                R.id.RMFragment -> {
                    toggle.isDrawerIndicatorEnabled = false
                }

                R.id.TDEEFragment -> {
                    toggle.isDrawerIndicatorEnabled = false
                }

                else -> {
                    toggle.isDrawerIndicatorEnabled = true
                }
            }
        }
    }

    private fun goToHomeScreen() {
        
        val intent =
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
    }
}
