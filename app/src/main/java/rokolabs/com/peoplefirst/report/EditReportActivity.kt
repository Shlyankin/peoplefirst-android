package rokolabs.com.peoplefirst.report

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reports.*
import rokolabs.com.peoplefirst.R
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import kotlinx.android.synthetic.main.activity_edit_report.*
import kotlinx.android.synthetic.main.app_bar_edit_report.*
import rokolabs.com.peoplefirst.databinding.ActivityEditReportBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.services.FileUploadService
import rokolabs.com.peoplefirst.utils.Utils
import java.io.File
import java.util.ArrayList
import javax.inject.Inject


class EditReportActivity : AppCompatActivity() {
    companion object {
        val PERMISSION_REQUEST_CODE = 1
        val CHOOSE_FILE_CODE = 2
        val ADD_VICTIM_CODE=3
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var mRepository: HarassmentRepository
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
        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_main_questions,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_tools,
                R.id.nav_share,
                R.id.nav_send
            ), drawerLayout
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
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun toggleDrawer() {
        var t = drawerLayout.isDrawerOpen(nav_view)
        if (t) {
            drawerLayout.closeDrawer(nav_view)
        } else {
            drawerLayout.openDrawer(nav_view)
        }
    }

    fun navigateTo(id: Int) {
        var pos = when (id) {
            R.id.menuItem1 -> {
                R.id.nav_harassment_type
            }
            R.id.menuItem2 -> {
                R.id.nav_harassment_reasons
            }
            R.id.menuItem3 -> {
                R.id.nav_report_happened_before
            }
            R.id.menuItem4 -> {
                R.id.nav_report_what_happened
            }
            R.id.menuItem5 -> {
                R.id.nav_report_who_being_harassed
            }
            R.id.menuItem6 -> {
                R.id.nav_harassment_type
            }
            R.id.menuItem7 -> {
                R.id.nav_harassment_type
            }
            R.id.menuItem8 -> {
                R.id.nav_harassment_type
            }
            R.id.menuItem9 -> {
                R.id.nav_harassment_type
            }
            else -> {
                R.id.nav_main_questions
            }

        }
        navController.navigate(pos)
        navigationDrawerViewModel.currentPos.set(pos)
        drawerLayout.closeDrawers()
    }

    fun chooseFile() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
            return
        }
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "*/*"
        startActivityForResult(intent, CHOOSE_FILE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != CHOOSE_FILE_CODE || resultCode != RESULT_OK || data == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        when (requestCode) {
            CHOOSE_FILE_CODE -> kotlin.run {
                val files = ArrayList<File>()
                val uriList = ArrayList<Uri>()

                if (data.clipData != null) {
                    val clipData = data.clipData
                    for (i in 0 until clipData!!.itemCount) {
                        uriList.add(clipData.getItemAt(i).uri)
                    }
                } else if (data.data != null) {
                    uriList.add(data.data!!)
                }

                for (i in uriList.indices) {
                    val s = Utils.getFilePath(this, uriList[i])
                    files.add(File(s!!))
                }

                if (!files.isEmpty()) {
                    mRepository.currentReport.getValue()?.attachments?.addAll(files)
                    val intent = Intent(this, FileUploadService::class.java)
                    intent.putExtra("files", files)
//            startService(intent)
                }
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chooseFile()
        }
    }

    fun navigateTo(view: View) {
        navigateTo(view.id)
    }
}
