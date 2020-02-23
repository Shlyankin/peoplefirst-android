package rokolabs.com.peoplefirst.report.ui.users.selected

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import rokolabs.com.peoplefirst.api.PeopleFirstService
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.ui.users.activity.UsersActivity
import rokolabs.com.peoplefirst.repository.HarassmentRepository
import java.util.ArrayList
import javax.inject.Inject

class SelectedUsersModel @Inject
constructor(
    private val context: Context,
    private val selectedUsersFragment: Fragment,
    private val mService: PeopleFirstService,
    private val mRepository: HarassmentRepository
) : ViewModel() {
    var activity: EditReportActivity
    var mDisposable = CompositeDisposable()
    var addClicks: Subject<View> = PublishSubject.create()
    var clearClicks: Subject<View> = PublishSubject.create()
    var addVisibility: ObservableField<Int> = ObservableField()
    var clearVisibility: ObservableField<Int> = ObservableField()
    var buttonText: ObservableField<String> = ObservableField()

    var enableMultiselect = true
    var hideCurrentUserFromList = false
    var mAdapter: SelectedUsersAdapter? = null
    var mUsers = ArrayList<User>()
    var mRetailMode = false
    var mRetailMeEmail = ""
        set(value) {
            field = value
            if (mAdapter != null)
                mAdapter?.setRetailMeEmail(value)
        }
    var onUserSelectedCallback: OnUserSelectedCallback? = null

    init {
        mAdapter=SelectedUsersAdapter()
        activity = context as EditReportActivity
        setButtonText("Add another user")
    }

    fun initDisposable() {
        mDisposable=CompositeDisposable()
        mDisposable.addAll(
            addClicks.subscribe {
                if (mRetailMode) {
                    mUsers.add(User())
                    mAdapter?.setEntities(context, mUsers, mRetailMode)
                    mAdapter?.notifyDataSetChanged()
                } else {
                    val intent = Intent(context, UsersActivity::class.java)
                    intent.putExtra("hideCurrentUserFromList", hideCurrentUserFromList)
                    selectedUsersFragment.startActivityForResult(intent, EditReportActivity.ADD_VICTIM_CODE)
                }
            },
            clearClicks.subscribe {
                clear()
                showAdd()
                hideClear()
            }
        )
    }
    fun dispose(){
        mDisposable.dispose()
        mDisposable.clear()
    }
    fun disableMultiselect() {
        enableMultiselect = false
    }

    fun showAdd() {
        addVisibility.set(View.VISIBLE)
    }

    fun hideAdd() {
        addVisibility.set(View.GONE)
    }

    fun hideClear() {
        clearVisibility.set(View.GONE)
    }

    fun showClear() {
        clearVisibility.set(View.VISIBLE)
    }

    fun conformsToDomain(): Boolean {
        for (u in mUsers) {
            if (!SelectedUsersAdapter.getDomain(mRetailMeEmail).equals(
                    SelectedUsersAdapter.getDomain(
                        u.email
                    )
                )
            )
                return false
        }
        return true
    }

    fun setButtonText(text: String) {
        buttonText.set(text)
    }

    fun clear() {
        mUsers.clear()
        mAdapter?.setEntities(context, mUsers, mRetailMode)
        mAdapter?.notifyDataSetChanged()
    }

    interface OnUserSelectedCallback {
        fun onUserSelected(user: User)
    }
}