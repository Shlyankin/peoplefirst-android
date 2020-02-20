package rokolabs.com.peoplefirst.report

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reports.*
import rokolabs.com.peoplefirst.R
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.app_bar_edit_report.*
import rokolabs.com.peoplefirst.databinding.ActivityEditReportBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject


class EditReportActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var title: TextView
    lateinit var navigationDrawerViewModel: NavigationDrawerViewModel
    lateinit var navController: NavController
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        navigationDrawerViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(NavigationDrawerViewModel::class.java)
        var binder = DataBindingUtil.setContentView<ActivityEditReportBinding>(
            this,
            R.layout.activity_edit_report
        )
        binder.drawerViewModel = navigationDrawerViewModel
        setSupportActionBar(toolbar)
        title = findViewById(R.id.toolbar_title)
        drawerLayout= findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_incedent_type,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_tools,
                R.id.nav_share,
                R.id.nav_send
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent))
        drawerLayout.setDrawerElevation(0F)
        val toggle = object : ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                if (slideOffset >= 0.9f) {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home_white)
                } else {
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.navbar_mobile)
                }
                title.alpha = 1.0f - slideOffset
                val slideX = drawerView.getWidth() * slideOffset
                content.setTranslationX(slideX)
            }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            var handler = Handler()
            handler.postDelayed({
                supportActionBar?.setHomeAsUpIndicator(R.drawable.navbar_mobile)
            }, 200)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun navigateTo(view: View) {
        when (view.id) {
            R.id.menuItem1 -> {
                var t =navController.navigate(R.id.nav_share)
            }
            R.id.menuItem2 -> {

            }
            R.id.menuItem3 -> {

            }
            R.id.menuItem4 -> {

            }
            R.id.menuItem5 -> {

            }
            R.id.menuItem6 -> {

            }
            R.id.menuItem7 -> {

            }
            R.id.menuItem8->{

            }
            R.id.menuItem9->{

            }

        }
        drawerLayout.closeDrawers()
    }
}
