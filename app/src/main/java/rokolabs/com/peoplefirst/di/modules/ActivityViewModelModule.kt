package rokolabs.com.peoplefirst.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import rokolabs.com.peoplefirst.auth.login.LoginViewModel
import rokolabs.com.peoplefirst.auth.password.reset.send.request.ResetPasswordViewModel
import rokolabs.com.peoplefirst.auth.password.reset.update.SetNewPasswordViewModel
import rokolabs.com.peoplefirst.auth.registration.CreateAccountRetailViewModel
import rokolabs.com.peoplefirst.di.qualifier.ViewModelKey
import rokolabs.com.peoplefirst.report.NavigationDrawerViewModel
import rokolabs.com.peoplefirst.report.ui.place.search.AddressLookupActivity
import rokolabs.com.peoplefirst.report.ui.place.search.AddressLookupModel
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

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetNewPasswordViewModel::class)
    internal abstract fun setNewPasswordViewModel(setNewPasswordViewModel: SetNewPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResetPasswordViewModel::class)
    internal abstract fun resetPasswordViewModel(resetPasswordViewModel: ResetPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateAccountRetailViewModel::class)
    internal abstract fun createAccountRetailViewModel(createAccountRetailViewModel: CreateAccountRetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddressLookupModel::class)
    internal abstract fun addressLookupModel(addressLookupModel: AddressLookupModel): ViewModel
}