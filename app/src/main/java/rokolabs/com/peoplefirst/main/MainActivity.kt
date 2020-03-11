package rokolabs.com.peoplefirst.main

import android.os.Bundle
import android.os.Handler
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.toolbar_with_logo.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    @Inject
    lateinit var mRepository: HarassmentRepository
    @Inject
    lateinit var mService: PeopleFirstService
    var mDisposable = CompositeDisposable()
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        setContentView(R.layout.activity_main)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_reports,
                R.id.nav_home_profile,
                R.id.nav_resources
            ), drawerLayout
        )
        NavigationUI.setupActionBarWithNavController(
            this,
            navController,
            appBarConfiguration
        )
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            var handler = Handler()
            handler.postDelayed({
                supportActionBar?.setHomeAsUpIndicator(R.drawable.navbar_mobile)
            }, 200)
        }
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration)

        navView.getMenu().getItem(0).setChecked(true);
        navView.setupWithNavController(navController)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayShowHomeEnabled(true)
        mDisposable.add(
            mRepository.me.subscribe {
                if (it.retail == 1) {
                    mDisposable.add(
                        mService.escalationLevels
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                { response ->
                                    if (response.success) {
                                        if (response.data.size == 0) {
                                            navController.navigate(R.id.nav_resources)
                                        }
                                    }
                                },
                                { throwable ->
                                    Toast.makeText(
                                        this,
                                        "Unable to get escalation levels",
                                        Toast.LENGTH_LONG
                                    ).show()
                                })
                    )
                }
            }
        )
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mDisposable.clear()
    }
}
