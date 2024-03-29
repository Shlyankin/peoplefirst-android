package rokolabs.com.peoplefirst.main.ui.resources

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_resources.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.databinding.FragmentReportsBinding
import rokolabs.com.peoplefirst.databinding.FragmentResourcesBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.model.CounsellingService
import rokolabs.com.peoplefirst.model.EscalationLevel
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import java.util.ArrayList
import javax.inject.Inject

class ResourcesFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: ResourcesViewModel

    var mDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        var binder = DataBindingUtil.inflate<FragmentResourcesBinding>(
            inflater,
            R.layout.fragment_resources,
            container,
            false
        )
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ResourcesViewModel::class.java)
        binder.viewModel = viewModel
        return binder.root
    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()
        list.setLayoutManager(LinearLayoutManager(context))
        listCouncelling.setLayoutManager(LinearLayoutManager(context))
        list.setAdapter(viewModel?.mAdapter)
        listCouncelling.setAdapter(viewModel?.mCouncellingAdapter)
        privacy.setPaintFlags(privacy.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        terms.setPaintFlags(terms.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        mDisposable.addAll(

        )
        activity?.findViewById<ImageView>(R.id.save)?.setOnClickListener {
            viewModel!!.saveResources()
        }
    }

    override fun onPause() {
        super.onPause()
        mDisposable.dispose()
        viewModel.dispose()
        viewModel.showhideSaveButton(false)
    }


}