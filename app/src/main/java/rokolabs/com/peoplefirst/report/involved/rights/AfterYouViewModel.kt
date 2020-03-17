package rokolabs.com.peoplefirst.report.involved.rights

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.report.involved.named.NamedActivity
import rokolabs.com.peoplefirst.report.involved.verify.victim.VerifyVictimActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class AfterYouViewModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var activity = context as AfterYouViewActivity
    private var mDisposable = CompositeDisposable()

    var continueSubject: Subject<View> = PublishSubject.create()

    fun initDisposable() {
        mDisposable.addAll(
            continueSubject.subscribe {
                var intent = Intent(activity, VerifyVictimActivity::class.java)
                activity.startActivity(intent)
                activity.finish()
            }
        )
    }

    fun dispose() {
        mDisposable.clear()
    }
}