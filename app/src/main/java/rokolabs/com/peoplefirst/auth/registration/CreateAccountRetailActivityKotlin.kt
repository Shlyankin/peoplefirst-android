package rokolabs.com.peoplefirst.auth.registration

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
//import rokolabs.com.peoplefirst.BaseActivity
import rokolabs.com.peoplefirst.R
//import rokolabs.com.peoplefirst.WelcomeRetailActivity
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.databinding.ActivityCreateAccountRetailNewBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.model.RetailRegistrationRequest
import javax.inject.Inject

class CreateAccountRetailActivityKotlin : AppCompatActivity() {
    @Inject
    lateinit var mService: PeopleFirstService
    var viewModel:CreateAccountRetailViewModel=CreateAccountRetailViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        super.onCreate(savedInstanceState)
        var binding:ActivityCreateAccountRetailNewBinding=DataBindingUtil.setContentView(this,R.layout.activity_create_account_retail_new)
        binding.viewModel=viewModel
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        viewModel.registerClicks.subscribe {
            if(viewModel.checkConditions()){
                mService.retailRegistration(viewModel.getRetailRegistrationRequest())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeBy(onError = {it.printStackTrace()},
                                onSuccess = {
                                    if(it.isSuccessful){
                                        Toast.makeText(this,"Registration successful",Toast.LENGTH_LONG).show()
//                                        startActivity(Intent(this,WelcomeRetailActivity::class.java))
                                        finish()
                                    }
                                })
            }
        }
    }
}