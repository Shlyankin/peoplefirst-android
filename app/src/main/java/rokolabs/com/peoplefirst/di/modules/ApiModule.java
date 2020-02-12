package rokolabs.com.peoplefirst.di.modules;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import rokolabs.com.peoplefirst.api.PeopleFirstApi;

/**
 * Created by S on 17.05.2018.
 */

@Module(includes = {RetrofitModule.class})
public class ApiModule {
	@Provides
	@Singleton
	public PeopleFirstApi provideLTechApi(Retrofit retrofit) {
		return retrofit.create(PeopleFirstApi.class);
	}
}
