package rokolabs.com.peoplefirst.di.component;

import android.content.Context;

import dagger.Component;
import rokolabs.com.peoplefirst.di.modules.FragmentModule;
import rokolabs.com.peoplefirst.di.scopes.PerFragment;
import rokolabs.com.peoplefirst.main.ui.profile.ProfileFragment;
import rokolabs.com.peoplefirst.main.ui.reports.ReportsFragment;
import rokolabs.com.peoplefirst.main.ui.resources.ResourcesFragment;

@PerFragment
@Component(dependencies = ActivityComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Context context();
    void inject(ReportsFragment fragment);

    void inject(ProfileFragment fragment);
    void inject(ResourcesFragment fragment);
}
