package rokolabs.com.peoplefirst;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import rokolabs.com.peoplefirst.di.ComponentManager;

/**
 * Created by S on 17.05.2018.
 */

public class App extends Application {

    public static final String MIXPANEL_TOKEN = "12fc390ac2c0ca160e6d6116e99071bb";

    @Override
    public void onCreate() {
        super.onCreate();
        ComponentManager.getInstance().init(this);

        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(this, MIXPANEL_TOKEN);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }


}
