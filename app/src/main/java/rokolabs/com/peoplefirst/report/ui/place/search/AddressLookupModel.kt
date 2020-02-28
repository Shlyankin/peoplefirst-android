package rokolabs.com.peoplefirst.report.ui.place.search

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersFragment
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import javax.inject.Inject

class AddressLookupModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: AddressLookupActivity
    var mDisposable = CompositeDisposable()

    var input: ObservableField<String> = ObservableField()
    var progressBarVisibility: ObservableField<Int> = ObservableField()

    init {
        activity = context as AddressLookupActivity
        progressBarVisibility.set(View.GONE)
    }

    fun initDisposable() {
        mDisposable.addAll(

        )
    }

    fun dispose() {
        mDisposable.clear()
    }
}