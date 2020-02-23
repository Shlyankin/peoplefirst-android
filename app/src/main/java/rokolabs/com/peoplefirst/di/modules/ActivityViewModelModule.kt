package rokolabs.com.peoplefirst.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import rokolabs.com.peoplefirst.di.qualifier.ViewModelKey
import rokolabs.com.peoplefirst.report.NavigationDrawerViewModel
import rokolabs.com.peoplefirst.report.ui.users.activity.UsersModel
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersModel

@Module
abstract class ActivityViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UsersModel::class)
    internal abstract fun usersModel(usersModel: UsersModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(NavigationDrawerViewModel::class)
    internal abstract fun orderListViewModel(orderListViewModel: NavigationDrawerViewModel): ViewModel
}