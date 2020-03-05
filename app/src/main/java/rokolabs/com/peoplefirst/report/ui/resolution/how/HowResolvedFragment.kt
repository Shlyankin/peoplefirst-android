package rokolabs.com.peoplefirst.report.ui.resolution.how

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHowResolvedBinding
import rokolabs.com.peoplefirst.databinding.FragmentPlaceBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.ui.place.PlaceModel
import rokolabs.com.peoplefirst.report.ui.place.search.AddressLookupActivity
import javax.inject.Inject

class HowResolvedFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: HowResolvedModel
    var mDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HowResolvedModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentHowResolvedBinding>(
            inflater,
            R.layout.fragment_how_resolved,
            container,
            false
        )
        binder.viewModel = viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()

    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
        mDisposable.clear()
    }
}