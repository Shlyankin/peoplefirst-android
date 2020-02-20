package rokolabs.com.peoplefirst.di.modules


import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import rokolabs.com.peoplefirst.auth.login.LoginViewModel
import rokolabs.com.peoplefirst.di.qualifier.ViewModelKey
import rokolabs.com.peoplefirst.report.EditReportActivity
import rokolabs.com.peoplefirst.report.NavigationDrawerViewModel

@Module
abstract class ViewModelModule {

//    @Binds
//    @IntoMap
//    @ViewModelKey(LoginViewModel::class)
//    internal abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavigationDrawerViewModel::class)
    internal abstract fun orderListViewModel(orderListViewModel: NavigationDrawerViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(OrderViewModel::class)
//    internal abstract fun orderViewModel(orderViewModel: OrderViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(PositionListViewModel::class)
//    internal abstract fun positionListViewModel(positionListViewModel: PositionListViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(MapViewModel::class)
//    internal abstract fun mapViewModel(mapViewModel: MapViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(LoadStatusViewModel::class)
//    internal abstract fun loadStatusViewModel(loadStatusViewModel: LoadStatusViewModel): ViewModel



}
