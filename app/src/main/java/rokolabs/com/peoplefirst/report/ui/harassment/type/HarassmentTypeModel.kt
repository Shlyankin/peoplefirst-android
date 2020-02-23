package rokolabs.com.peoplefirst.report.ui.harassment.type

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_edit_profile_drawer.view.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.HarassmentType
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import java.util.*
import javax.inject.Inject

class HarassmentTypeModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var selectedHarassments = ArrayList<HarassmentType>()
    var mAdapter: TypesAdapter? = null
    private var mDisposable = CompositeDisposable()
    private var acitivty: EditReportActivity
    var nextClick: PublishSubject<View> = PublishSubject.create()
    var prevClick: PublishSubject<View> = PublishSubject.create()
    var prevEnabled:ObservableField<Boolean> = ObservableField()
    init {
        acitivty = context as EditReportActivity
        mAdapter = TypesAdapter()
        when (mRepository.named) {
            HarassmentRepository.VICTIM -> {
                // victim verify flow
                makePreviousButtonActive()
                mRepository.currentVictimTestimony.subscribe { testimony ->
                    selectedHarassments = testimony.harassment_types
                }
            }
            HarassmentRepository.WITNESS -> {
                // witness verify flow
                makePreviousButtonActive()
                mRepository.currentWitnessTestimony.subscribe { report ->
                    selectedHarassments = report.harassment_types
                }
            }
            else ->
                // victim flow
                mRepository.currentReport.subscribe { report ->
                    if (report !== Report.EMPTY) selectedHarassments = report.harassment_types
                }
        }
//        if (mRepository.named == HarassmentRepository.EMPTY) {
//            acitivty.toggleDrawer()
//        }
    }

    fun initDisposable() {
        mDisposable=CompositeDisposable()
        mDisposable.addAll(
            nextClick.subscribe {
                if (save()) {
                    acitivty.navigateTo(R.id.menuItem2)
                }
            },
            prevClick.subscribe {
                if (mRepository.named == HarassmentRepository.VICTIM) {
//                    val intent = Intent(this, VerifyVictimActivity::class.java)
//                    startActivity(intent)
//                    save()
//                    overridePendingTransition(R.anim.enter_back, R.anim.exit_back)
                } else if (mRepository.named == HarassmentRepository.WITNESS) {
//                    val intent = Intent(this, VerifyActivity::class.java)
//                    startActivity(intent)
//                    save()
//                    overridePendingTransition(R.anim.enter_back, R.anim.exit_back)
                }
            },
            mAdapter!!.typeClick.subscribe {
                if (selectedHarassments.indexOf(it) >= 0)
                    selectedHarassments.remove(it)
                else
                    selectedHarassments.add(it)
            },
            mService.harassmentTypes
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onError = {
                    showToast("Unable to get harassment types")
                },onSuccess = {
                    if (it.success) {
                        showTypes(it.data)
                    }
                })

        )
    }

    fun dispose() {
        mDisposable.dispose()
        mDisposable.clear()
    }

    fun save(): Boolean {
        val intent = Intent()
        intent.putExtra("types", selectedHarassments)
//        setResult(RESULT_OK, intent)
        if (selectedHarassments.size == 0) {
            showToast("This info is required")
            return false
        }
        if (mRepository.named == HarassmentRepository.VICTIM) {
            mRepository.currentVictimTestimony.getValue()!!.harassment_types = selectedHarassments
            mRepository.currentVictimTestimony.onNext(mRepository.currentVictimTestimony.getValue()!!)
        } else if (mRepository.named == HarassmentRepository.WITNESS) {
            mRepository.currentWitnessTestimony.getValue()!!.harassment_types = selectedHarassments
            mRepository.currentWitnessTestimony.onNext(mRepository.currentWitnessTestimony.getValue()!!)
        } else if (mRepository.currentReport.getValue() !== Report.EMPTY && mRepository.currentReport.getValue() != null) {
            mRepository.currentReport.getValue()!!.harassment_types = selectedHarassments
            mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
        }
//        finish()
        return true
    }

    fun showTypes(types: ArrayList<HarassmentType>) {
        if (mRepository.named == HarassmentRepository.VICTIM) {
            mAdapter?.setItems(
                context, types,
                if (mRepository.currentVictimTestimony.value != null && mRepository.currentVictimTestimony.value?.harassment_types != null)
                    mRepository.currentVictimTestimony.value?.harassment_types
                else
                    ArrayList()
            )
        } else if (mRepository.named == HarassmentRepository.WITNESS) {
            mAdapter?.setItems(
                context, types,
                if (mRepository.currentWitnessTestimony.value != null && mRepository.currentWitnessTestimony.value?.harassment_types != null)
                    mRepository.currentWitnessTestimony.value?.harassment_types
                else
                    ArrayList()
            )
        } else {
            mAdapter?.setItems(
                context, types,
                if (mRepository.currentReport.value !== Report.EMPTY
                    && mRepository.currentReport.value != null && mRepository.currentReport.value?.harassment_types != null
                )
                    mRepository.currentReport.value?.harassment_types
                else
                    ArrayList()
            )
        }
        mAdapter?.notifyDataSetChanged()
    }

    private fun makePreviousButtonActive() {
        prevEnabled.set(true)
    }
    fun showToast(string: String){
        Toast.makeText(context,string,Toast.LENGTH_LONG).show()
    }
}
