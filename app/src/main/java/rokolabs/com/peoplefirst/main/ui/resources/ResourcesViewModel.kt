package rokolabs.com.peoplefirst.main.ui.resources

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.fragment_resources.*
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.CounsellingService
import rokolabs.com.peoplefirst.model.EscalationLevel
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import java.util.ArrayList

class ResourcesViewModel(
    context: Context,
    mRepository: HarassmentRepository,
    mService: PeopleFirstService
) {
    private lateinit var context: Context
    private lateinit var mRepository: HarassmentRepository
    private lateinit var mService: PeopleFirstService
    var toastSubject: Subject<String> = PublishSubject.create()
    var termsClick: Subject<View> = PublishSubject.create()
    var privacyClick: Subject<View> = PublishSubject.create()
    var addClick: Subject<View> = PublishSubject.create()
    var mDisposable = CompositeDisposable()
    var mAdapter: EscalationLevelsAdapter = EscalationLevelsAdapter()
    var mCouncellingAdapter: CounsellingServicesAdapter = CounsellingServicesAdapter()
    var caption: ObservableField<String> = ObservableField()
    var councelling: ObservableField<String> = ObservableField()

    init {
        this.context = context
        this.mRepository = mRepository
        this.mService = mService
    }

    fun initDisposable() {
//        mAdapter.editable.onNext(true)
        mDisposable.addAll(
            mService.escalationLevels
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) {
                        showLevels(response.data)
                    }
                }, { throwable -> showToast("Unable to get escalation levels") })
            , mService.getCounsellingServices(mRepository.me.value!!.company.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) {
                        showCounselling(response.data)
                    }
                }, { throwable -> showToast("Unable to get counseling services") }
                ),
            mRepository.me.subscribe { user ->
                showCompanyName(user.company.name)
                mAdapter.editable.onNext(user.is_retail)

            }
        )
    }

    fun showLevels(levels: ArrayList<EscalationLevel>) {
        mAdapter?.setItems(context, levels)
        mAdapter?.notifyDataSetChanged()
    }

    fun showCounselling(levels: ArrayList<CounsellingService>) {
        mCouncellingAdapter?.setItems(context, levels)
        mCouncellingAdapter?.notifyDataSetChanged()
        if(levels.size==0){
            councelling.set("")
        }else if(mRepository.me.hasValue()){
            var name=mRepository.me.value!!.company.name
            councelling.set("$name Counseling Services")
        }
    }

    fun showCompanyName(name: String) {
        caption.set("$name Escalation")
        councelling.set("$name Counseling Services")
    }

    fun showToast(string: String) {
        toastSubject.onNext(string)
//        Toast.makeText(context,string, Toast.LENGTH_LONG).show()
    }

    fun dispose() {
        mDisposable.dispose()
    }
}