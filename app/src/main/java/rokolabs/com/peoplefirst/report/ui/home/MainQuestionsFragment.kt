package rokolabs.com.peoplefirst.report.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import rokolabs.com.peoplefirst.R

class MainQuestionsFragment : Fragment() {

    private lateinit var homeViewModel: MainQuestionsModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(MainQuestionsModel::class.java)
        val root = inflater.inflate(R.layout.fragment_main_questions, container, false)
        return root
    }
}