package rokolabs.com.peoplefirst.report.ui.users.activity

import android.app.Activity.RESULT_OK
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
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.model.responses.UsersResponse
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import rokolabs.com.peoplefirst.utils.addOnPropertyChanged
import java.util.*
import javax.inject.Inject

class UsersModel @Inject
constructor(
    private val context: Context,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: UsersActivity
    var mDisposable = CompositeDisposable()
    var backSubject: Subject<View> = PublishSubject.create()

    var search: ObservableField<String> = ObservableField()
    var mAdapter: UsersAdapter? = null
    var hideCurrentUserFromList = false

    init {
        activity = context as UsersActivity
        mAdapter = UsersAdapter()
    }

    fun initDisposable() {
        mDisposable=CompositeDisposable()
        mDisposable.addAll(
            backSubject.subscribe {
                activity.finish()
            },
            search.addOnPropertyChanged {
                search(it.get()!!)
            },
            mAdapter!!.usersClicks.subscribe {
                val data = Intent()
                data.putExtra("user", it)
                activity.setResult(RESULT_OK, data)
                activity.finish()
            }
        )
        search.set("")
    }

    fun search(string: String) {
        mDisposable.add(mService.getUsers(mRepository.me.value!!.company_id, string)
            .map<UsersResponse> { usersResponse ->
                Collections.sort<User>(usersResponse.data) { o1, o2 -> o1.last_name.compareTo(o2.last_name) }
                usersResponse
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = {
                showToast("Unable to load users");
            }, onSuccess = {
                if (it.success) {
                    if (hideCurrentUserFromList) {
                        it.data.remove(mRepository.me.value)
                    }
                    showUsers(it.data)
                } else {
                    showToast("Unable to load users")
                }
            })
        )
    }

    fun dispose() {
        mDisposable.dispose()
        mDisposable.clear()
    }

    fun showToast(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }

    fun showUsers(users: List<User>) {
        mAdapter?.setEntities(context, users)
        mAdapter?.notifyDataSetChanged()
    }
}