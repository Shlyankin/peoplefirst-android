package rokolabs.com.peoplefirst.di.component;


import android.content.Context;

import dagger.Component;
import rokolabs.com.peoplefirst.SplashActivity;
import rokolabs.com.peoplefirst.api.PeopleFirstService;
import rokolabs.com.peoplefirst.auth.WelcomeRetailActivity;
import rokolabs.com.peoplefirst.auth.login.LoginActivity;
import rokolabs.com.peoplefirst.auth.password.reset.send.request.ResetPasswordActivityKotlin;
import rokolabs.com.peoplefirst.auth.password.reset.update.SetNewPasswordActivity;
import rokolabs.com.peoplefirst.auth.registration.CreateAccountRetailActivityKotlin;
import rokolabs.com.peoplefirst.di.modules.ActivityModule;
import rokolabs.com.peoplefirst.di.modules.ActivityViewModelModule;
import rokolabs.com.peoplefirst.di.modules.BaseUrl;
import rokolabs.com.peoplefirst.di.modules.ViewModelModule;
import rokolabs.com.peoplefirst.di.scopes.PerActivity;
import rokolabs.com.peoplefirst.report.EditReportActivity;
import rokolabs.com.peoplefirst.report.ui.users.activity.UsersActivity;
import rokolabs.com.peoplefirst.repository.HarassmentRepository;

/**
 * Created by S on 17.05.2018.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = { ActivityModule.class, ActivityViewModelModule.class})
public interface ActivityComponent {

    Context context();

    PeopleFirstService peopleFirstService();

    HarassmentRepository harassmentRepository();

    BaseUrl baseUrl();

    void inject(ResetPasswordActivityKotlin activity);
    void inject(CreateAccountRetailActivityKotlin activity);
//    void inject(ReportsActivity activity);
    void inject(SetNewPasswordActivity activity);

    void inject(LoginActivity activity);
    void inject(EditReportActivity activity);
    void inject(UsersActivity activity);
    //
//    void inject(HarassmentTypeActivity activity);
//
//    void inject(ReportSummaryActivity activity);
//
//    void inject(WhoBeingHarassedActivity activity);
//
//    void inject(WhoAgressorActivity activity);
//
//    void inject(PlaceActivity activity);
//
//    void inject(WhatHappenedActivity activity);
//
//    void inject(DateTimeActivity activity);
//
//    void inject(WitnessActivity activity);
//
//    void inject(NamedActivity activity);
//
//    void inject(ProfileActivity activity);
//
//    void inject(UsersActivity activity);
//
//    void inject(WitnessYesNoActivity activity);
//
//    void inject(HowResolvedActivity activity);
//
//    void inject(HarassmentReasonActivity activity);
//
//    void inject(VerifyActivity activity);
//
//    void inject(ConfirmActivity activity);
//
//    void inject(ReportDetailsActivity activity);
//
//    void inject(ConfirmResolutionActivity activity);
//
//    void inject(ResolutionStatusActivity activity);
//
//    void inject(EscalationLevelsActivity activity);
//
//    void inject(SubmittedReportActivity activity);
//
//    void inject(TransparentProcessActivity activity);
//
//    void inject(EscalationPathActivity activity);
//
//    void inject(PublishingActivity activity);
//
//    void inject(InfoActivity activity);
//
//    void inject(UserActivationActivity activity);
//
//    void inject(CollegueBelievesActivity activity);
//
//    void inject(HappenedBeforeActivity activity);
//
//    void inject(UpdatedReportActivity activity);
//
//    void inject(ResetPasswordActivity activity);
//
//    void inject(VerifyVictimActivity activity);
//
//    void inject(RejectedReportActivity activity);
//
    void inject(WelcomeRetailActivity activity);
//
//    void inject(EmailCodeRetailActivity activity);
//
//    void inject(CreateAccountRetailActivity activity);
//
//    void inject(HelpSubmitActivity activity);
//
//    void inject(RetailEscalationActivity activity);
    void inject(SplashActivity activity);

}
