package rokolabs.com.peoplefirst.report.ui.agressor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentReportAgressorBinding
import rokolabs.com.peoplefirst.databinding.FragmentWhatHappenedBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.report.ui.details.what.happened.WhatHappenedModel
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersFragment
import javax.inject.Inject

class WhoAgressorWasFragment :Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var whoAgressorWasModel: WhoAgressorWasModel
    lateinit var mSelectedUsers: SelectedUsersFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        whoAgressorWasModel= ViewModelProviders.of(this,viewModelFactory).get(WhoAgressorWasModel::class.java)
        var binder= DataBindingUtil.inflate<FragmentReportAgressorBinding>(inflater, R.layout.fragment_report_agressor,container,false)
        binder.viewModel=whoAgressorWasModel
        return binder.root
    }
    override fun onResume() {
        super.onResume()
        mSelectedUsers = childFragmentManager.findFragmentById(R.id.selectedUsers) as SelectedUsersFragment
        mSelectedUsers.viewModel.disableMultiselect()
        mSelectedUsers.viewModel.hideCurrentUserFromList = true
        whoAgressorWasModel.mSelectedUsers=mSelectedUsers
        whoAgressorWasModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        whoAgressorWasModel.dispose()
    }
}