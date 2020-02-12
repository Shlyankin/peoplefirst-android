package rokolabs.com.peoplefirst.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rokolabs.com.peoplefirst.api.PeopleFirstApi;
import rokolabs.com.peoplefirst.api.PeopleFirstService;

/**
 * Created by S on 19.04.2018.
 */

@Module(includes = ApiModule.class)
public class PeopleFirstModule {
    @Provides
    @Singleton
    public PeopleFirstService provideNSodeService(PeopleFirstApi api) {
        return new PeopleFirstService(api);
    }
}
