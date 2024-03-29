package rokolabs.com.peoplefirst.di.component;

import android.content.Context;

import dagger.Component;
import rokolabs.com.peoplefirst.di.modules.FragmentModule;
import rokolabs.com.peoplefirst.di.modules.ViewModelModule;
import rokolabs.com.peoplefirst.di.scopes.PerFragment;
import rokolabs.com.peoplefirst.main.ui.profile.HomeProfileFragment;
import rokolabs.com.peoplefirst.main.ui.reports.ReportsFragment;
import rokolabs.com.peoplefirst.main.ui.resources.ResourcesFragment;
import rokolabs.com.peoplefirst.profile.ProfileFragment;
import rokolabs.com.peoplefirst.report.ui.agressor.WhoAgressorWasFragment;
import rokolabs.com.peoplefirst.report.ui.date.DateTimeSelectionFragment;
import rokolabs.com.peoplefirst.report.ui.details.happened.before.HappenedBeforeFragment;
import rokolabs.com.peoplefirst.report.ui.details.what.happened.WhatHappenedFragment;
import rokolabs.com.peoplefirst.report.ui.harassment.reasons.HarassmentReasonsFragment;
import rokolabs.com.peoplefirst.report.ui.harassment.type.HarassmentTypeFragment;
import rokolabs.com.peoplefirst.report.ui.home.MainQuestionsFragment;
import rokolabs.com.peoplefirst.report.ui.notification.result.ResultNotificationFragment;
import rokolabs.com.peoplefirst.report.ui.notification.result.ResultNotificationModel;
import rokolabs.com.peoplefirst.report.ui.place.PlaceFragment;
import rokolabs.com.peoplefirst.report.ui.profile.confirmation.ProfileConfirmationFragment;
import rokolabs.com.peoplefirst.report.ui.resolution.how.HowResolvedFragment;
import rokolabs.com.peoplefirst.report.ui.summary.ReportSummaryFragment;
import rokolabs.com.peoplefirst.report.ui.summary.confirm.ConfirmFragment;
import rokolabs.com.peoplefirst.report.ui.users.selected.SelectedUsersFragment;
import rokolabs.com.peoplefirst.report.ui.victim.WhoBeingHarassedFragment;
import rokolabs.com.peoplefirst.report.ui.witness.WereAnyWitnessesFragment;
import rokolabs.com.peoplefirst.report.ui.witness.information.WitnessInformationFragment;

@PerFragment
@Component(dependencies = ActivityComponent.class, modules = {FragmentModule.class, ViewModelModule.class})
public interface FragmentComponent {
    Context context();

    void inject(ReportsFragment fragment);

    void inject(HomeProfileFragment fragment);

    void inject(ResourcesFragment fragment);

    void inject(HarassmentTypeFragment fragment);

    void inject(HarassmentReasonsFragment fragment);

    void inject(HappenedBeforeFragment fragment);

    void inject(WhatHappenedFragment fragment);

    void inject(WhoBeingHarassedFragment fragment);

    void inject(SelectedUsersFragment fragment);

    void inject(MainQuestionsFragment fragment);

    void inject(WhoAgressorWasFragment fragment);

    void inject(WereAnyWitnessesFragment fragment);

    void inject(WitnessInformationFragment fragment);

    void inject(PlaceFragment fragment);

    void inject(DateTimeSelectionFragment fragment);

    void inject(HowResolvedFragment fragment);

    void inject(ReportSummaryFragment fragment);

    void inject(ProfileConfirmationFragment fragment);

    void inject(ProfileFragment fragment);

    void inject(ResultNotificationFragment fragment);
    void inject(ConfirmFragment fragment);

}

