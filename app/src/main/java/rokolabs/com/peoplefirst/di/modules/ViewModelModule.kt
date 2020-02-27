package rokolabs.com.peoplefirst.di.modules


import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import rokolabs.com.peoplefirst.di.qualifier.ViewModelKey
import rokolabs.com.peoplefirst.main.ui.profile.ProfileViewModel
import rokolabs.com.peoplefirst.main.ui.reports.ReportsModel
import rokolabs.com.peoplefirst.main.ui.resources.ResourcesViewModel
import rokolabs.com.peoplefirst.report.ui.agressor.WhoAgressorWasModel
import rokolabs.com.peoplefirst.report.ui.details.happened.before.HappenedBeforeModel
import rokolabs.com.peoplefirst.report.ui.details.what.happened.WhatHappenedModel
import rokolabs.com.peoplefirst.report.ui.harassment.reasons.HarassmentReasonsModel
import rokolabs.com.peoplefirst.report.ui.harassment.type.HarassmentTypeModel
import rokolabs.com.peoplefirst.report.ui.home.MainQuestionsModel
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersModel
import rokolabs.com.peoplefirst.report.ui.victim.WhoBeingHarassedModel
import rokolabs.com.peoplefirst.report.ui.witness.WereAnyWitnessModel
import rokolabs.com.peoplefirst.report.ui.witness.information.WitnessInformationModel

@Module
abstract class ViewModelModule {

    //    @Binds
//    @IntoMap
//    @ViewModelKey(LoginViewModel::class)
//    internal abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(HappenedBeforeModel::class)
    internal abstract fun happenedeBeforeModel(happenedBeforeModel: HappenedBeforeModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(MainQuestionsModel::class)
    internal abstract fun mainQuestionsModel(mainQuestionsModel: MainQuestionsModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(HarassmentTypeModel::class)
    internal abstract fun harassmentTypeModel(harassmentTypeModel: HarassmentTypeModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HarassmentReasonsModel::class)
    internal abstract fun harassmentReasonsModel(harassmentReasonsModel: HarassmentReasonsModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(WhatHappenedModel::class)
    internal abstract fun whatHappenedModel(whatHappenedModel: WhatHappenedModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(WhoBeingHarassedModel::class)
    internal abstract fun whoBeingHarassedModel(whoBeingHarassedModel: WhoBeingHarassedModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectedUsersModel::class)
    internal abstract fun selectedUsersModel(selectedUsersModel: SelectedUsersModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportsModel::class)
    internal abstract fun reportsModel(reportsModel: ReportsModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResourcesViewModel::class)
    internal abstract fun resourcesViewModel(resourcesViewModel: ResourcesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WhoAgressorWasModel::class)
    internal abstract fun whoAgressorWasModel(whoAgressorWasModel: WhoAgressorWasModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WereAnyWitnessModel::class)
    internal abstract fun whereAnyWitnessModel(wereAnyWitnessModel: WereAnyWitnessModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WitnessInformationModel::class)
    internal abstract fun witnessInformationModel(witnessInformationModel: WitnessInformationModel): ViewModel
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
