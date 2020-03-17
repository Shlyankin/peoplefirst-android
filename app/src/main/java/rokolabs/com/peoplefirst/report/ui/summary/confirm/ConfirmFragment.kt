package rokolabs.com.peoplefirst.report.ui.summary.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_confirm.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentConfirmBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class ConfirmFragment : Fragment() {
    @Inject
    lateinit var mRepository: HarassmentRepository
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: ConfirmModel
    var mDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConfirmModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentConfirmBinding>(
            inflater,
            R.layout.fragment_confirm,
            container,
            false
        )
        binder.viewModel = viewModel


        return binder.root
    }

    override fun onResume() {
        super.onResume()
        anonym.setTypeface(ResourcesCompat.getFont(context!!, R.font.ratio_regular))
        if (mRepository.named != HarassmentRepository.WITNESS) anonym.setVisibility(View.GONE)

//        if (getIntent().hasExtra("readOnly")) mDone.setVisibility(View.GONE)

        if (mRepository.named == HarassmentRepository.WITNESS) {
            if (mRepository.me.getValue() == mRepository.currentWitnessTestimony.getValue()?.victim) {
                anonym.setVisibility(View.GONE)
            }
        }
        viewModel.initDisposable()

    }

    fun showReadOnly(boolean: Boolean) {
        viewModel.readOnly.set(boolean)
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
        mDisposable.clear()
    }
}