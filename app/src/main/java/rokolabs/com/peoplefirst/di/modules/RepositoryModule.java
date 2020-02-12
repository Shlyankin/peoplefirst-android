package rokolabs.com.peoplefirst.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rokolabs.com.peoplefirst.api.PeopleFirstService;
import rokolabs.com.peoplefirst.di.qualifier.AppContext;
import rokolabs.com.peoplefirst.repository.HarassmentRepository;

/**
 * Created by S on 19.05.2018.
 */

@Module
public class    RepositoryModule {

    @Singleton
    @Provides
    public HarassmentRepository provideHarassmentRepository(PeopleFirstService service, @AppContext Context context) {
        return new HarassmentRepository(service, context);
    }
}
