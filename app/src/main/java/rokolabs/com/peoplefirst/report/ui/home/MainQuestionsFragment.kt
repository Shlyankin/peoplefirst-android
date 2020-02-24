package rokolabs.com.peoplefirst.report.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentMainQuestionsBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class MainQuestionsFragment : Fragment() {

    private lateinit var homeViewModel: MainQuestionsModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        homeViewModel =
            ViewModelProviders.of(this,viewModelFactory).get(MainQuestionsModel::class.java)
        var binder=DataBindingUtil.inflate<FragmentMainQuestionsBinding>(inflater,R.layout.fragment_main_questions,container,false)
        binder.viewModel=homeViewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        homeViewModel.dispose()
    }
}