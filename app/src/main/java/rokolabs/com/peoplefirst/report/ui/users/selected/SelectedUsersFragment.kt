package rokolabs.com.peoplefirst.report.ui.users.selected

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_selected_users.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.FragmentHappenedBeforeBinding
import rokolabs.com.peoplefirst.databinding.FragmentSelectedUsersBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import rokolabs.com.peoplefirst.model.User
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.ui.details.happened.before.HappenedBeforeModel
import rokolabs.com.peoplefirst.report.ui.users.activity.UsersActivity
import java.util.ArrayList
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SelectedUsersFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var viewModel: SelectedUsersModel
    lateinit var recyclerView: RecyclerView
    var mDisposable = CompositeDisposable()

    var indexOfUserToReplace: Int = -1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        ComponentManager.getInstance().getFragmentComponent(this).inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(SelectedUsersModel::class.java)
        var binder = DataBindingUtil.inflate<FragmentSelectedUsersBinding>(
            inflater,
            R.layout.fragment_selected_users,
            container,
            false
        )
        recyclerView = binder.root.findViewById(R.id.list)
        binder.viewModel = viewModel
        return binder.root
    }

    override fun onResume() {
        super.onResume()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = viewModel.mAdapter
        mDisposable = CompositeDisposable()
        mDisposable.addAll(
            viewModel.addClicks.subscribe {
                if (viewModel.mRetailMode) {
                    viewModel.mUsers.add(User())
                    viewModel.mAdapter?.setEntities(
                        context,
                        viewModel.mUsers,
                        viewModel.mRetailMode
                    )
                    viewModel.mAdapter?.notifyDataSetChanged()
                } else {
                    val intent = Intent(context, UsersActivity::class.java)
                    intent.putExtra("hideCurrentUserFromList", viewModel.hideCurrentUserFromList)
                    startActivityForResult(intent, EditReportActivity.ADD_VICTIM_CODE)
                }
            },
            viewModel.mAdapter?.usersClicks
                ?.debounce(500, TimeUnit.MILLISECONDS)
                ?.subscribe {
                    indexOfUserToReplace = viewModel.mUsers.indexOf(it)
                    val intent = Intent(context, UsersActivity::class.java)
                    intent.putExtra("hideCurrentUserFromList", viewModel.hideCurrentUserFromList)
                    startActivityForResult(intent, EditReportActivity.ADD_VICTIM_CODE)

                }
        )
        viewModel.initDisposable()
    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
        mDisposable.clear()
    }

    fun getUsers(): ArrayList<User> {
        return viewModel.mUsers
    }

    fun setUsers(users: ArrayList<User>) {
        viewModel.mUsers = users
        viewModel.mAdapter?.setEntities(activity, users, viewModel.mRetailMode)
        viewModel.mAdapter?.notifyDataSetChanged()
    }

    fun clear() {
        viewModel.mUsers.clear()
        viewModel.mAdapter?.setEntities(activity, viewModel.mUsers, viewModel.mRetailMode)
        viewModel?.mAdapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EditReportActivity.ADD_VICTIM_CODE && resultCode == Activity.RESULT_OK) {
            val user = data!!.getSerializableExtra("user") as User
            if (viewModel.onUserSelectedCallback != null)
                viewModel.onUserSelectedCallback!!.onUserSelected(user)
            if (indexOfUserToReplace >= 0) {
                viewModel.mUsers.set(indexOfUserToReplace, user)
            } else {
                viewModel.mUsers.add(user)

            }
            if (!viewModel.enableMultiselect) {
                viewModel.mUsers.clear()
                viewModel.mUsers.add(user)
            }
            viewModel.mAdapter?.setEntities(activity, viewModel.mUsers, viewModel.mRetailMode)
            viewModel.mAdapter?.notifyDataSetChanged()
            if (!viewModel.enableMultiselect) {
                viewModel.hideAdd()
                viewModel.showClear()
            }


        }
    }
}