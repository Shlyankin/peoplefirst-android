package rokolabs.com.peoplefirst.main

import android.os.Bundle
import android.os.Handler
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import kotlinx.android.synthetic.main.toolbar_with_logo.*
import rokolabs.com.peoplefirst.R

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_reports,
                R.id.nav_profile,
                R.id.nav_resources
            ), drawerLayout
        )
        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
            appBarConfiguration
        )
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            var handler=Handler()
            handler.postDelayed({
                supportActionBar?.setHomeAsUpIndicator(R.drawable.navbar_mobile)
            },200)
        }
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)
        navView.setNavigationItemSelectedListener(this);
        navView.getMenu().getItem(0).setChecked(true);
        navView.setupWithNavController(navController)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayShowHomeEnabled(true)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }
}
