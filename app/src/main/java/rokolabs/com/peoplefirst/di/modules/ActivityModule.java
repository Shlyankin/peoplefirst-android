package rokolabs.com.peoplefirst.di.modules;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import rokolabs.com.peoplefirst.di.scopes.PerActivity;

/**
 * Created by S on 19.04.2018.
 */

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    Context provideContext() {
        return activity;
    }

}