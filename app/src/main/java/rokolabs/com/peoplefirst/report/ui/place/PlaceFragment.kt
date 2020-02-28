package rokolabs.com.peoplefirst.report.ui.place

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_harassment_reason.*
import kotlinx.android.synthetic.main.activity_place.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHarassmentReasonsBinding
import rokolabs.com.peoplefirst.databinding.FragmentPlaceBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.ui.harassment.reasons.HarassmentReasonsModel
import rokolabs.com.peoplefirst.report.ui.place.search.AddressLookupActivity
import javax.inject.Inject

class PlaceFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: PlaceModel
    var mDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlaceModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentPlaceBinding>(
            inflater,
            R.layout.fragment__place,
            container,
            false
        )
        binder.viewModel = viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
//        location.isEnabled=false
        mDisposable.addAll(viewModel.locationClick.subscribe {
            if (!Geocoder.isPresent()) return@subscribe
            val intent = Intent(context, AddressLookupActivity::class.java)
            intent.putExtra("location", viewModel.location.get().toString())
            startActivityForResult(intent, 0)
        })
        viewModel.initDisposable()

    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != 0 || resultCode != RESULT_OK) super.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (data == null) return

        val address = data.getStringExtra("address")
        viewModel.location.set(address)
    }
}