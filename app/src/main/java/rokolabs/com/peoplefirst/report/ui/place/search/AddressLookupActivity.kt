package rokolabs.com.peoplefirst.report.ui.place.search

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_address_lookup.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.auth.login.LoginViewModel
import rokolabs.com.peoplefirst.databinding.ActivityAddressLookupBinding
import rokolabs.com.peoplefirst.databinding.ActivityLoginBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.services.FetchAddressIntentService
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import java.lang.ref.WeakReference
import java.util.ArrayList
import javax.inject.Inject

class AddressLookupActivity : AppCompatActivity() {
    @Inject
    lateinit var viewmodelFactory: ViewModelFactory

    lateinit var viewModel: AddressLookupModel
    var mDisposable = CompositeDisposable()
    var addressList: ViewGroup? = null
    var messageView: TextView? = null

    internal lateinit var resultReceiver: AddressResultReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewmodelFactory).get(AddressLookupModel::class.java)
        var binder =
            DataBindingUtil.setContentView<ActivityAddressLookupBinding>(
                this,
                R.layout.activity_address_lookup
            )
        resultReceiver = AddressResultReceiver(Handler())
        resultReceiver.setActivity(this)
        binder.viewModel = viewModel
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        if (!intent.getStringExtra("location").isEmpty()) {
            viewModel.input.set(intent.getStringExtra("location"))
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24px)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        mDisposable.addAll(
            viewModel.input.addOnPropertyChanged {
                if (it.toString().length >= 2) {
                    viewModel.progressBarVisibility.set(View.VISIBLE)
                    startIntentService(it.get().toString())
                }
            }
        )
        addressList = address_list
        messageView = message
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("address", input.text.toString())
        setResult(RESULT_OK, intent)
        finish()
    }

    protected fun startIntentService(text: String) {
        val intent = Intent(this, FetchAddressIntentService::class.java)
        intent.putExtra("rokolabs.com.peoplefirst.RECEIVER", resultReceiver)
        intent.putExtra("rokolabs.com.peoplefirst.LOCATION_DATA_EXTRA", text)
        startService(intent)
    }

    companion object {
        class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {
            var activity: WeakReference<AddressLookupActivity>? = null
            fun setActivity(addressLookupActivity: AddressLookupActivity) {
                activity = WeakReference(addressLookupActivity)
            }

            override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
                super.onReceiveResult(resultCode, resultData)

                val message = resultData?.getString("message")
                val resultAddressList =
                    resultData?.getSerializable("addressList") as ArrayList<String>?

                activity?.get()!!.viewModel.progressBarVisibility.set(View.GONE)

                if (resultAddressList != null && !resultAddressList.isEmpty()) {
                    activity?.get()!!.addressList?.removeAllViews()
                    activity?.get()!!.messageView?.setVisibility(View.GONE)
                    activity?.get()!!.addressList?.setVisibility(View.VISIBLE)

                    for (address in resultAddressList) {
                        val addressView =
                            activity?.get()!!.layoutInflater.inflate(
                                R.layout.listitem_address,
                                null
                            )
                        (addressView.findViewById<View>(R.id.text) as TextView).text = address
                        activity?.get()!!.addressList?.addView(addressView)
                        addressView.setOnClickListener { v ->
                            val intent = Intent()
                            intent.putExtra(
                                "address",
                                (v.findViewById<View>(R.id.text) as TextView).text.toString()
                            )
                            activity?.get()!!.setResult(RESULT_OK, intent)
                            activity?.get()!!.finish()
                        }
                    }
                } else {
                    activity?.get()!!.addressList?.setVisibility(View.GONE)
                    activity?.get()!!.messageView?.setVisibility(View.VISIBLE)
                    activity?.get()!!.messageView?.setText(message)
                }
            }
        }
    }
}
