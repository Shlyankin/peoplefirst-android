package rokolabs.com.peoplefirst.report.ui.harassment.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_harassment_type.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHarassmentTypeBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class HarassmentTypeFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: HarassmentTypeModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this,viewModelFactory).get(HarassmentTypeModel::class.java)
        var binder=DataBindingUtil.inflate<FragmentHarassmentTypeBinding>(inflater,R.layout.fragment_harassment_type,container,false)
        binder.viewModel=viewModel

        return binder.root
    }

    override fun onResume() {
        super.onResume()
        list.setLayoutManager(LinearLayoutManager(context))
        list.setAdapter(viewModel.mAdapter)
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}