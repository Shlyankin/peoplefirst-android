package rokolabs.com.peoplefirst.report

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import kotlinx.android.synthetic.main.activity_reports.*
import rokolabs.com.peoplefirst.R
import androidx.core.view.ViewCompat.setScaleY
import androidx.core.view.ViewCompat.setScaleX
import androidx.core.view.ViewCompat.setTranslationX
import android.opengl.ETC1.getWidth
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_edit_report.*
import kotlinx.android.synthetic.main.app_bar_edit_report.*
import rokolabs.com.peoplefirst.databinding.ActivityEditReportBinding


class EditReportActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var title:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binder=DataBindingUtil.setContentView<ActivityEditReportBinding>(this,R.layout.activity_edit_report)
        binder.drawerViewModel= NavigationDrawerViewModel()
//        setContentView(R.layout.activity_edit_report)
        setSupportActionBar(toolbar)
        title=findViewById(R.id.toolbar_title)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_home,
//                R.id.nav_gallery,
//                R.id.nav_slideshow,
//                R.id.nav_tools,
//                R.id.nav_share,
//                R.id.nav_send
//            ), drawerLayout
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent))
        drawerLayout.setDrawerElevation(0F)
        val toggle = object : ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                if(slideOffset>=0.9f){
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home_white)
                }else{
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.navbar_mobile)
                }
                title.alpha=1.0f-slideOffset
                val slideX = drawerView.getWidth() * slideOffset
                content.setTranslationX(slideX)
            }
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            var handler= Handler()
            handler.postDelayed({
                supportActionBar?.setHomeAsUpIndicator(R.drawable.navbar_mobile)
            },200)
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.title=""
//        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
