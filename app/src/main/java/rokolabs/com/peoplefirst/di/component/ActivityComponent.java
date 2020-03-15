package rokolabs.com.peoplefirst.di.component;


import android.content.Context;

import dagger.Component;
import rokolabs.com.peoplefirst.SplashActivity;
import rokolabs.com.peoplefirst.api.PeopleFirstService;
import rokolabs.com.peoplefirst.auth.login.LoginActivity;
import rokolabs.com.peoplefirst.auth.password.reset.send.request.ResetPasswordActivity;
import rokolabs.com.peoplefirst.auth.password.reset.update.SetNewPasswordActivity;
import rokolabs.com.peoplefirst.auth.registration.CreateAccountRetailActivity;
import rokolabs.com.peoplefirst.di.modules.ActivityModule;
import rokolabs.com.peoplefirst.di.modules.ActivityViewModelModule;
import rokolabs.com.peoplefirst.di.modules.BaseUrl;
import rokolabs.com.peoplefirst.di.scopes.PerActivity;
import rokolabs.com.peoplefirst.main.MainActivity;
import rokolabs.com.peoplefirst.report.EditReportActivity;
import rokolabs.com.peoplefirst.report.involved.named.NamedActivity;
import rokolabs.com.peoplefirst.report.involved.rights.AfterYouViewActivity;
import rokolabs.com.peoplefirst.report.involved.verify.victim.VerifyVictimActivity;
import rokolabs.com.peoplefirst.report.involved.verify.witness.VerifyActivity;
import rokolabs.com.peoplefirst.report.involved.victim.CollegueBelievesActivity;
import rokolabs.com.peoplefirst.report.ui.place.search.AddressLookupActivity;
import rokolabs.com.peoplefirst.report.ui.users.activity.UsersActivity;
import rokolabs.com.peoplefirst.repository.HarassmentRepository;
import rokolabs.com.peoplefirst.resolution.confirm.ConfirmResolutionActivity;
import rokolabs.com.peoplefirst.resolution.result.ResolutionStatusActivity;

/**
 * Created by S on 17.05.2018.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ActivityViewModelModule.class})
public interface ActivityComponent {

    Context context();

    PeopleFirstService peopleFirstService();

    HarassmentRepository harassmentRepository();

    BaseUrl baseUrl();

    void inject(ResetPasswordActivity activity);

    void inject(CreateAccountRetailActivity activity);

    void inject(SetNewPasswordActivity activity);

    void inject(LoginActivity activity);

    void inject(EditReportActivity activity);

    void inject(UsersActivity activity);

    void inject(AddressLookupActivity activity);

    void inject(MainActivity activity);

    void inject(ConfirmResolutionActivity activity);

    void inject(ResolutionStatusActivity activity);

    void inject(CollegueBelievesActivity activity);

    void inject(NamedActivity activity);

    void inject(SplashActivity activity);

    void inject(AfterYouViewActivity activity);

    void inject(VerifyActivity activity);

    void inject(VerifyVictimActivity activity);

}
