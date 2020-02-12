package rokolabs.com.peoplefirst.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_welcome_retail.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.auth.login.LoginActivity
import rokolabs.com.peoplefirst.auth.registration.CreateAccountRetailActivityKotlin
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import javax.inject.Inject

class WelcomeRetailActivity : AppCompatActivity() {
    @Inject
    lateinit var mRepository: HarassmentRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        setContentView(R.layout.activity_welcome_retail)
        super.onCreate(savedInstanceState)
        if ("" != Utils.getPermanentValue("x-access-token", this)) {
            gotoReportList()
            mRepository.getMe()
        } else if ("" != Utils.getPermanentValue("activation_sent", this)) {
//            val intent = Intent(this, EmailCodeRetailActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        createAccount.setOnClickListener {
            val intent = Intent(this, CreateAccountRetailActivityKotlin::class.java)
            startActivity(intent)
            finish()
        }
        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun gotoReportList() {
//        val intent = Intent(this, ReportsActivity::class.java)
//        startActivity(intent)
//        finish()
    }
}