package rokolabs.com.peoplefirst.report.ui.date


import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.Report
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.Utils
import java.util.*
import javax.inject.Inject

class DateTimeSelectionViewModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var mActivity: EditReportActivity = context as EditReportActivity
    private var mDisposable = CompositeDisposable()

    var nextClick: Subject<View> = PublishSubject.create()
    var prevClick: Subject<View> = PublishSubject.create()
    var dateClick: Subject<View> = PublishSubject.create()

    var date: ObservableField<String> = ObservableField()
    var time: ObservableField<String> = ObservableField()

    var dateListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePickerDialog?,
            year: Int,
            monthOfYear: Int,
            dayOfMonth: Int
        ) {
            val dateStr =
                String.format("%02d", monthOfYear + 1) + "/" + String.format(
                    "%02d",
                    dayOfMonth
                ) + "/" + year
            date.set(dateStr)
            showtimeDialog()
        }

    }
    var timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute, second ->
        val timeS =
            String.format("%02d", hourOfDay) + ":" + String.format(
                "%02d",
                minute
            ) + ":" + String.format("%02d", second)
        time.set(Utils.format24ToFormat12(timeS))
    }

    init {

    }

    fun initDisposable() {
        mDisposable.addAll(
            nextClick.subscribe {
                if (save()) {
                    // mActivity.navigateTo(R.id.nav_report_how_resolved)]
                    mActivity.navigateNext()
                }
            },
            prevClick.subscribe {
                previous()
            },
            mActivity.onBackPressedObject.subscribe {
                previous()
            },
            mRepository.currentReport.subscribe {
                if (it !== Report.EMPTY && it.datetime != null) {
                    try {
                        date.set(
                            Utils.normalDateToGay(
                                it.datetime.split(
                                    " "
                                ).toTypedArray().get(0)
                            )
                        )
                        time.set(
                            Utils.format24ToFormat12(
                                it.datetime.split(
                                    " "
                                ).toTypedArray().get(1)
                            )
                        )
                    } catch (e: Exception) {
                    }
                }
            },
            dateClick.subscribe {
                showDateDialog()
            }
        )
    }

    fun showDateDialog() {
        val now = Calendar.getInstance()
        val dpd =
            DatePickerDialog.newInstance(
                dateListener,
                now[Calendar.YEAR],  // Initial year selection
                now[Calendar.MONTH],  // Initial month selection
                now[Calendar.DAY_OF_MONTH] // Inital day selection
            )
        dpd.show(
            mActivity.fragmentManager
            , "Datepickerdialog"
        )
    }

    fun showtimeDialog() {
        val now = Calendar.getInstance()
        val dpd =
            com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                timeListener,
                now[Calendar.HOUR_OF_DAY],  // Initial year selection
                now[Calendar.MINUTE],  // Initial month selection
                true
            )
        dpd.show(mActivity.fragmentManager, "Timepickerdialog")
    }

    fun save(): Boolean {
        val intent = Intent()
        if (mRepository.currentReport.value !== Report.EMPTY && mRepository.currentReport.value != null) {
            mRepository.currentReport.value!!.datetime =
                (Utils.gayDateToNormal(date.get().toString())
                        + " "
                        + Utils.format12ToFormat24(time.get().toString()))
            if ("" == date.get().toString()) {
                Toast.makeText(context, "Date should be selected to proceed", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if ("" == time.get().toString()) {
                Toast.makeText(context, "Time should be selected to proceed", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            if (Utils.isDateInFuture(mRepository.currentReport.value!!.datetime)) {
                Toast.makeText(context, "Dates in future are not allowed", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            mRepository.currentReport.onNext(mRepository.currentReport.value!!)
        }
        return true
    }

    fun previous() {
        // mActivity.navigateTo(R.id.nav_report_place)
        mActivity.navigatePrev()
    }

    fun dispose() {
        mDisposable.clear()
    }
}