package rokolabs.com.peoplefirst.main.ui.resources

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import okhttp3.ResponseBody
import retrofit2.HttpException
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.main.MainActivity
import rokolabs.com.peoplefirst.model.CounsellingService
import rokolabs.com.peoplefirst.model.EscalationLevel
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import java.util.*
import javax.inject.Inject

class ResourcesViewModel
@Inject constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: MainActivity
    var mDisposable = CompositeDisposable()

    var saveButtonStatus: BehaviorSubject<Boolean> = BehaviorSubject.create()
    var toastSubject: Subject<String> = PublishSubject.create()
    var termsClick: Subject<View> = PublishSubject.create()
    var privacyClick: Subject<View> = PublishSubject.create()
    var addClick: Subject<View> = PublishSubject.create()

    var mAdapter: EscalationLevelsAdapter = EscalationLevelsAdapter()
    var mCouncellingAdapter: CounsellingServicesAdapter = CounsellingServicesAdapter()
    var caption: ObservableField<String> = ObservableField()
    var councelling: ObservableField<String> = ObservableField()
    var addButtonVisibility: ObservableField<Boolean> = ObservableField()

    init {
        activity = context as MainActivity
    }

    fun initDisposable() {
        mDisposable.addAll(
            mService.getCounsellingServices(mRepository.me.value!!.company.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) {
                        showCounselling(response.data)
                    }
                }, { throwable -> showToast("Unable to get counseling services") }
                ),
            addClick.subscribe {
                var items = mAdapter.getmItems()
                items.add(EscalationLevel("name", "contact"))
                mAdapter.setItems(context, items)
                mAdapter.notifyDataSetChanged()
            },
            toastSubject.subscribe {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            },
            termsClick.subscribe {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.peoplefirstrh.com/terms-of-service")
                )
                activity.startActivity(browserIntent)
            },
            privacyClick.subscribe {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.peoplefirstrh.com/privacy-policy")
                )
                activity.startActivity(browserIntent)
            },
            saveButtonStatus.subscribe {
                showhideSaveButton(it)
            }
        )
        updateEscalationLevels()
    }

    fun updateEscalationLevels() {
        mDisposable.add(mRepository.me.subscribe { user ->
            showCompanyName(user.company.name)
            mDisposable.add(
                mService.escalationLevels
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(onError = {
                        showToast("Unable to get escalation levels")
                    }, onSuccess = {
                        if (it.success) {
                            if (it.data.size == 0) {
                                mAdapter.editable.onNext(user.retail == 1)
                                saveButtonStatus.onNext(user.retail == 1)
                                addButtonVisibility.set(user.retail == 1)
                            } else {
                                mAdapter.editable.onNext(false)
                                saveButtonStatus.onNext(false)
                                addButtonVisibility.set(false)
                            }
                            showLevels(it.data)
                        }
                    })
            )
        })
    }

    fun showLevels(levels: ArrayList<EscalationLevel>) {
        mAdapter?.setItems(context, levels)
        mAdapter?.notifyDataSetChanged()
    }

    fun showCounselling(levels: ArrayList<CounsellingService>) {
        mCouncellingAdapter?.setItems(context, levels)
        mCouncellingAdapter?.notifyDataSetChanged()
        if (levels.size == 0) {
            councelling.set("")
        } else if (mRepository.me.hasValue()) {
            var name = mRepository.me.value!!.company.name
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

    fun showhideSaveButton(boolean: Boolean) {
        activity?.findViewById<ImageView>(R.id.save)?.visibility =
            if (boolean) View.VISIBLE else View.GONE
    }

    fun saveResources() {
        Observable.just(mAdapter.getmItems())
            .flatMap {
                mService.addEscalationLevels(it)
                    .toObservable()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                it.printStackTrace()
                if (it is HttpException) {
                    var http = it as HttpException
                    var t = (http.response().errorBody() as ResponseBody).string()
                    if (t.contains("email", true)) {
                        showToast("All contacts should be email")
                        return@subscribeBy
                    }
                    var u = 0
                    u++
                }
                showToast("Unable to save escalation levels")
            }, onNext = {
                updateEscalationLevels()
                showToast("Escalation levels updated")

            })
    }

    fun dispose() {
        mDisposable.clear()
    }
}