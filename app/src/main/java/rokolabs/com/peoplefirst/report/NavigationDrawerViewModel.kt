package rokolabs.com.peoplefirst.report

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class NavigationDrawerViewModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    private var mActivity: EditReportActivity = context as EditReportActivity
    private var mDisposable = CompositeDisposable()
    var currentPos: ObservableField<Int> = ObservableField()
    var menuItemClick: Subject<View> = PublishSubject.create()

    var harassmentTypeDone: ObservableField<Boolean> = ObservableField()
    var harassmentreasonsDone: ObservableField<Boolean> = ObservableField()
    var detailsDone: ObservableField<Boolean> = ObservableField()
    var victimDone: ObservableField<Boolean> = ObservableField()
    var agressorsDone: ObservableField<Boolean> = ObservableField()
    var witnessesDone: ObservableField<Boolean> = ObservableField()
    var locationCityDone: ObservableField<Boolean> = ObservableField()
    var dateTimeDone: ObservableField<Boolean> = ObservableField()
    var happenedBeforeDone: ObservableField<Boolean> = ObservableField()

    init {
        currentPos.set(R.id.nav_main_questions)
        initDisposable()
    }

    fun getPrevFragmentId(): Int {
        val currentFragmentId = currentPos.get()!!
        when(mRepository.named) {
            HarassmentRepository.VICTIM -> {
                return getPrevFragmentIdVictim(currentFragmentId)
            }
            HarassmentRepository.AGGRESSOR -> {
                return getPrevFragmentIdAggressor(currentFragmentId)

            }
            HarassmentRepository.WITNESS -> {
                return getPrevFragmentIdWitness(currentFragmentId)
            }
            else -> {
                // TODO: надо подумать что по дефолту возвращать.
                //  Скорее всего просто "у вас нет доступа. попробуйте позже".
                //  Но сейчас во время создания репорта mRepository.named = -1, поэтому victim
                return getPrevFragmentIdVictim(currentFragmentId)
            }
        }
    }

    fun getNextFragmentId(): Int {
        val currentFragmentId = currentPos.get()!!
        when(mRepository.named) {
            HarassmentRepository.VICTIM -> {
                return getNextFragmentIdVictim(currentFragmentId)
            }
            HarassmentRepository.AGGRESSOR -> {
                return getNextFragmentIdAggressor(currentFragmentId)

            }
            HarassmentRepository.WITNESS -> {
                return getNextFragmentIdWitness(currentFragmentId)
            }
            else -> {
                // TODO: надо подумать что по дефолту возвращать.
                //  Скорее всего просто "у вас нет доступа. попробуйте позже".
                //  Но сейчас во время создания репорта mRepository.named = -1, поэтому victim
                return getNextFragmentIdVictim(currentFragmentId)
            }
        }
    }

    private fun getPrevFragmentIdVictim(currentFragmentId: Int): Int {
        when(currentFragmentId) {
            R.id.nav_harassment_type -> {
                return R.id.nav_main_questions
            }
            R.id.nav_harassment_reasons -> {
                return R.id.nav_harassment_type
            }
            R.id.nav_report_happened_before -> {
                return R.id.nav_harassment_reasons
            }
            R.id.nav_report_what_happened -> {
                return R.id.nav_report_happened_before
            }
            R.id.nav_report_who_being_harassed -> {
                return R.id.nav_report_what_happened
            }
            R.id.nav_report_who_agressor_was -> {
                return R.id.nav_report_who_being_harassed
            }
            R.id.nav_report_were_any_witnesses -> {
                return R.id.nav_report_who_agressor_was
            }
            R.id.nav_report_place -> {
                return R.id.nav_report_were_any_witnesses
            }
            R.id.nav_report_date_time -> {
                return R.id.nav_report_place
            }
            R.id.nav_report_how_resolved -> {
                return R.id.nav_report_date_time
            }
            R.id.nav_report_witness_information -> {
                return R.id.nav_report_how_resolved
            }
            R.id.nav_report_summary -> {
                return R.id.nav_report_witness_information
            }
            R.id.nav_report_profile_confirmation -> {
                return R.id.nav_report_summary
            }
            else -> {
                return R.id.nav_main_questions
            }
        }
    }

    private fun getPrevFragmentIdAggressor(currentFragmentId: Int): Int {
        when(currentFragmentId) {
            R.id.nav_main_questions -> {
                return R.id.nav_harassment_type
            }
            R.id.nav_harassment_type -> {
                return R.id.nav_harassment_reasons
            }
            R.id.nav_harassment_reasons -> {
                return R.id.nav_report_happened_before
            }
            R.id.nav_report_happened_before -> {
                return R.id.nav_report_what_happened
            }
            R.id.nav_report_what_happened -> {
                return R.id.nav_report_who_being_harassed
            }
            R.id.nav_report_who_being_harassed -> {
                return R.id.nav_report_who_agressor_was
            }
            R.id.nav_report_who_agressor_was -> {
                return R.id.nav_report_were_any_witnesses
            }
            R.id.nav_report_were_any_witnesses -> {
                return R.id.nav_report_place
            }
            R.id.nav_report_place -> {
                return R.id.nav_report_date_time
            }
            R.id.nav_report_date_time -> {
                return R.id.nav_report_how_resolved
            }
            R.id.nav_report_how_resolved -> {
                return R.id.nav_report_witness_information
            }
            R.id.nav_report_witness_information -> {
                return R.id.nav_report_summary
            }
            R.id.nav_report_summary -> {
                return R.id.nav_report_profile_confirmation
            }
            else -> {
                return R.id.nav_main_questions
            }
        }
    }

    private fun getPrevFragmentIdWitness(currentFragmentId: Int): Int {
        when(currentFragmentId) {
            R.id.nav_harassment_type, R.id.nav_harassment_type_title -> {
                return R.id.nav_harassment_type
            }
            R.id.nav_harassment_reasons -> {
                return R.id.nav_harassment_reasons
            }
            R.id.nav_report_happened_before -> {
                return R.id.nav_report_happened_before
            }
            R.id.nav_report_what_happened -> {
                return R.id.nav_report_what_happened
            }
            R.id.nav_report_who_being_harassed, R.id.nav_report_who_being_harassed_title -> {
                return R.id.nav_report_who_being_harassed
            }
            R.id.nav_report_who_agressor_was -> {
                return R.id.nav_report_who_agressor_was
            }
            R.id.nav_report_were_any_witnesses -> {
                return R.id.nav_report_were_any_witnesses
            }
            R.id.nav_report_place, R.id.nav_report_place_title -> {
                return R.id.nav_report_place
            }
            R.id.nav_report_date_time, R.id.nav_report_date_time_title -> {
                return R.id.nav_report_date_time
            }
            R.id.nav_report_how_resolved -> {
                return R.id.nav_report_how_resolved
            }
            R.id.nav_report_witness_information -> {
                return R.id.nav_report_witness_information
            }
            R.id.nav_report_summary -> {
                return R.id.nav_report_summary
            }
            R.id.nav_report_profile_confirmation -> {
                return R.id.nav_report_profile_confirmation
            }
            else -> {
                return R.id.nav_main_questions
            }
        }
    }

    private fun getNextFragmentIdVictim(currentFragmentId: Int): Int {
        when(currentFragmentId) {
            R.id.nav_main_questions, R.id.nav_harassment_type_title -> {
                return R.id.nav_harassment_type
            }
            R.id.nav_harassment_type -> {
                return R.id.nav_harassment_reasons
            }
            R.id.nav_harassment_reasons -> {
                return R.id.nav_report_happened_before
            }
            R.id.nav_report_happened_before -> {
                return R.id.nav_report_what_happened
            }
            R.id.nav_report_what_happened -> {
                return R.id.nav_report_who_being_harassed
            }
            R.id.nav_report_who_being_harassed -> {
                return R.id.nav_report_who_agressor_was
            }
            R.id.nav_report_who_agressor_was -> {
                return R.id.nav_report_were_any_witnesses
            }
            R.id.nav_report_were_any_witnesses -> {
                return R.id.nav_report_place
            }
            R.id.nav_report_place -> {
                return R.id.nav_report_date_time
            }
            R.id.nav_report_date_time -> {
                return R.id.nav_report_how_resolved
            }
            R.id.nav_report_how_resolved -> {
                return R.id.nav_report_summary
            }
            R.id.nav_report_summary -> {
                return R.id.nav_report_profile_confirmation
            }
            else -> {
                return R.id.nav_main_questions
            }
        }
    }

    private fun getNextFragmentIdAggressor(currentFragmentId: Int): Int {
        when(currentFragmentId) {
            R.id.nav_main_questions -> {
                return R.id.nav_harassment_type
            }
            R.id.nav_harassment_type -> {
                return R.id.nav_harassment_reasons
            }
            R.id.nav_harassment_reasons -> {
                return R.id.nav_report_happened_before
            }
            R.id.nav_report_happened_before -> {
                return R.id.nav_report_what_happened
            }
            R.id.nav_report_what_happened -> {
                return R.id.nav_report_who_being_harassed
            }
            R.id.nav_report_who_being_harassed -> {
                return R.id.nav_report_who_agressor_was
            }
            R.id.nav_report_who_agressor_was -> {
                return R.id.nav_report_were_any_witnesses
            }
            R.id.nav_report_were_any_witnesses -> {
                return R.id.nav_report_place
            }
            R.id.nav_report_place -> {
                return R.id.nav_report_date_time
            }
            R.id.nav_report_date_time -> {
                return R.id.nav_report_how_resolved
            }
            R.id.nav_report_how_resolved -> {
                return R.id.nav_report_witness_information
            }
            R.id.nav_report_witness_information -> {
                return R.id.nav_report_summary
            }
            R.id.nav_report_summary -> {
                return R.id.nav_report_profile_confirmation
            }
            else -> {
                return R.id.nav_main_questions
            }
        }
    }

    private fun getNextFragmentIdWitness(currentFragmentId: Int): Int {
        when(currentFragmentId) {
            R.id.nav_harassment_type, R.id.nav_harassment_type_title -> {
                return R.id.nav_harassment_type
            }
            R.id.nav_harassment_reasons -> {
                return R.id.nav_harassment_reasons
            }
            R.id.nav_report_happened_before -> {
                return R.id.nav_report_happened_before
            }
            R.id.nav_report_what_happened -> {
                return R.id.nav_report_what_happened
            }
            R.id.nav_report_who_being_harassed, R.id.nav_report_who_being_harassed_title -> {
                return R.id.nav_report_who_being_harassed
            }
            R.id.nav_report_who_agressor_was -> {
                return R.id.nav_report_who_agressor_was
            }
            R.id.nav_report_were_any_witnesses -> {
                return R.id.nav_report_were_any_witnesses
            }
            R.id.nav_report_place, R.id.nav_report_place_title -> {
                return R.id.nav_report_place
            }
            R.id.nav_report_date_time, R.id.nav_report_date_time_title -> {
                return R.id.nav_report_date_time
            }
            R.id.nav_report_how_resolved -> {
                return R.id.nav_report_how_resolved
            }
            R.id.nav_report_witness_information -> {
                return R.id.nav_report_witness_information
            }
            R.id.nav_report_summary -> {
                return R.id.nav_report_summary
            }
            R.id.nav_report_profile_confirmation -> {
                return R.id.nav_report_profile_confirmation
            }
            else -> {
                return R.id.nav_main_questions
            }
        }
    }


    fun initDisposable() {
        mDisposable = CompositeDisposable()
        mDisposable.addAll(
            menuItemClick.subscribe {
                mActivity.navigateTo(it)
            },
            mRepository.currentReport.subscribe { report ->
                harassmentTypeDone.set(report.harassment_types.size > 0)
                harassmentreasonsDone.set(report.harassment_reasons.size > 0)
                detailsDone.set(report.details != null && "" != report.details)
                victimDone.set(report.victim != null)
                agressorsDone.set(report.aggressors.size > 0)
                witnessesDone.set(report.witnesses.size > 0)
                locationCityDone.set(
                    (report.location_city != null && report.location_city.length > 0) ||
                            (report.location_details != null && report.location_details.length > 0)
                )
                dateTimeDone.set(report.datetime != null)
                happenedBeforeDone.set(report.happened_before != null) // TODO: всегда светится галочка. Надо поправить
            }
        )
    }

    fun dispose() {
        mDisposable.dispose()
    }
}