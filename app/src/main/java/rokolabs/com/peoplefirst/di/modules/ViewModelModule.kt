package rokolabs.com.peoplefirst.di.modules


import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import rokolabs.com.peoplefirst.di.qualifier.ViewModelKey
import rokolabs.com.peoplefirst.report.NavigationDrawerViewModel
import rokolabs.com.peoplefirst.report.ui.harassment.reasons.HarassmentReasonsModel
import rokolabs.com.peoplefirst.report.ui.harassment.type.HarassmentTypeModel

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
    @Binds
    @IntoMap
    @ViewModelKey(HarassmentTypeModel::class)
    internal abstract fun harassmentTypeModel(harassmentTypeModel: HarassmentTypeModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(HarassmentReasonsModel::class)
    internal abstract fun harassmentReasonsModel(harassmentReasonsModel: HarassmentReasonsModel): ViewModel
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
