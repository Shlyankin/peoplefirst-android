package rokolabs.com.peoplefirst.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import rokolabs.com.peoplefirst.App;
import rokolabs.com.peoplefirst.api.PeopleFirstService;
import rokolabs.com.peoplefirst.di.modules.AppModule;
import rokolabs.com.peoplefirst.di.modules.BaseUrl;
import rokolabs.com.peoplefirst.di.modules.PeopleFirstModule;
import rokolabs.com.peoplefirst.di.modules.RepositoryModule;
import rokolabs.com.peoplefirst.di.modules.RetrofitModule;
import rokolabs.com.peoplefirst.di.qualifier.AppContext;
import rokolabs.com.peoplefirst.repository.HarassmentRepository;

/**
 * Created by S on 19.04.2018.
 */

@Singleton
@Component(modules = {AppModule.class, PeopleFirstModule.class, RetrofitModule.class, RepositoryModule.class})
public interface AppComponent {

    @AppContext
    Context context();

    PeopleFirstService peopleFirstService();

    HarassmentRepository harassmentRepository();

    BaseUrl baseUrl();

    void inject(final App theApp);
}
