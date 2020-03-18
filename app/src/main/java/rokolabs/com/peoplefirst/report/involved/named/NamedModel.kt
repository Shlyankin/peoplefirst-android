package rokolabs.com.peoplefirst.report.involved.named

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.report.involved.verify.agressor.VerifyAggressorActivity
import rokolabs.com.peoplefirst.report.involved.verify.witness.VerifyWitnessActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class NamedModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity = context as NamedActivity
    private var mDisposable = CompositeDisposable()
    var continueSubject: Subject<View> = PublishSubject.create()

    fun initDisposable() {
        mDisposable.addAll(
            continueSubject.subscribe {
                if(mRepository.named == HarassmentRepository.WITNESS) {
                    val intent = Intent(activity, VerifyWitnessActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                }
                if(mRepository.named == HarassmentRepository.AGGRESSOR) {
                    val intent = Intent(activity, VerifyAggressorActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                }
            }
        )
    }

    fun dispose() {
        mDisposable.clear()
    }
}