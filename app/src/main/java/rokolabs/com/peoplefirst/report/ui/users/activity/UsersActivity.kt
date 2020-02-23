package rokolabs.com.peoplefirst.report.ui.users.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_users.*
import rokolabs.com.peoplefirst.R
import rokolabs.com.peoplefirst.databinding.ActivityUsersBinding
import rokolabs.com.peoplefirst.di.ComponentManager
import rokolabs.com.peoplefirst.di.factory.ViewModelFactory
import javax.inject.Inject

class UsersActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var recyclerView: RecyclerView
    lateinit var viewModel :UsersModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ComponentManager.getInstance().getActivityComponent(this).inject(this)
        viewModel= ViewModelProviders.of(this,viewModelFactory).get(UsersModel::class.java)
        viewModel.hideCurrentUserFromList = intent.getBooleanExtra("hideCurrentUserFromList", false)
        var binder = DataBindingUtil.setContentView<ActivityUsersBinding>(this,R.layout.activity_users)
        recyclerView=findViewById(R.id.list)
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=viewModel.mAdapter
        binder.viewModel=viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDisposable()

    }

    override fun onPause() {
        super.onPause()
        viewModel.dispose()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        ComponentManager.getInstance().removeActivityComponent(this)
    }
}
