package rokolabs.com.peoplefirst.report.ui.agressor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class WhoAgressorWasFragment :Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
}