package rokolabs.com.peoplefirst.report.ui.details.what.happened

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHappenedBeforeBinding
import rokolabs.com.peoplefirst.databinding.FragmentWhatHappenedBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.ui.details.happened.before.HappenedBeforeModel
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class WhatHappenedFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: WhatHappenedModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel= ViewModelProviders.of(this,viewModelFactory).get(WhatHappenedModel::class.java)
        var binder= DataBindingUtil.inflate<FragmentWhatHappenedBinding>(inflater, R.layout.fragment_what_happened,container,false)
        binder.viewModel=viewModel
        return binder.root
    }
    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }
}