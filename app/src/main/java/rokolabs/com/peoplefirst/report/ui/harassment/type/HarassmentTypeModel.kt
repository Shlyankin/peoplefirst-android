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
    var prevEnabled: ObservableField<Boolean> = ObservableField()

    init {
        acitivty = context as EditReportActivity
        mAdapter = TypesAdapter()
    }

    fun initDisposable() {
        mDisposable = CompositeDisposable()
        mDisposable.addAll(
            nextClick.subscribe {
                if (save()) {
                    acitivty.navigateTo(R.id.nav_harassment_reasons)
                }
            },
            prevClick.subscribe {
                previous()
            },
            acitivty.onBackPressedObject.subscribe {
                previous()
            },
            mAdapter?.typeClick?.subscribe {
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
                }, onSuccess = {
                    if (it.success) {
                        showTypes(it.data)
                    }
                }),
            mRepository.currentReport.subscribe {
                if (it !== Report.EMPTY)
                    selectedHarassments = it.harassment_types
            }

        )
    }

    fun previous() {
        if (acitivty.mode.get() == EditReportActivity.MODE_CREATE_NEW) {
            acitivty.navigateTo(0)
        } else {
            acitivty.finish()
        }
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
        if (mRepository.currentReport.getValue() !== Report.EMPTY && mRepository.currentReport.getValue() != null) {
            mRepository.currentReport.getValue()!!.harassment_types = selectedHarassments
            mRepository.currentReport.onNext(mRepository.currentReport.getValue()!!)
        }
//        finish()
        return true
    }

    fun showTypes(types: ArrayList<HarassmentType>) {
        mAdapter?.setItems(
            context, types,
            if (mRepository.currentReport.value !== Report.EMPTY
                && mRepository.currentReport.value != null && mRepository.currentReport.value?.harassment_types != null
            )
                mRepository.currentReport.value?.harassment_types
            else
                ArrayList()
        )
        mAdapter?.notifyDataSetChanged()
    }

    private fun makePreviousButtonActive() {
        prevEnabled.set(true)
    }

    fun showToast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }
}
