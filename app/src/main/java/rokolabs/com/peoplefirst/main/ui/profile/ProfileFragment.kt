package rokolabs.com.peoplefirst.main.ui.profile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.auth.login.LoginActivity
import rokolabs.com.peoplefirst.databinding.FragmentProfileBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import javax.inject.Inject

class ProfileFragment : Fragment() {
    @Inject
    lateinit var mRepository: HarassmentRepository
    @Inject
    lateinit var mService: PeopleFirstService

    private lateinit var viewModel: ProfileViewModel

    private var mDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        var binder = DataBindingUtil.inflate<FragmentProfileBinding>(
            inflater,
            R.layout.fragment_profile,
            container,
            false
        )
        viewModel = ProfileViewModel(context!!, mRepository, mService)
        binder.viewModel = viewModel
        return binder.root
    }

    @SuppressLint("HardwareIds")
    override fun onResume() {
        super.onResume()
        activity?.findViewById<ImageView>(R.id.save)?.setOnClickListener {
            viewModel.saveUser()
        }
        mDisposable.add(
            viewModel.editeable.addOnPropertyChanged {
                var edit = it.get()!!
                if (edit) {
                    activity?.findViewById<ImageView>(R.id.save)?.visibility = View.VISIBLE
                } else {
                    activity?.findViewById<ImageView>(R.id.save)?.visibility = View.GONE
                }
            })
        viewModel.initDisposable()
        viewModel.enableEditing()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
        mDisposable.dispose()
        activity?.findViewById<ImageView>(R.id.save)?.visibility = View.GONE
    }
}