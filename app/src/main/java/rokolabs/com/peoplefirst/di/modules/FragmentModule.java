package rokolabs.com.peoplefirst.di.modules;


import androidx.fragment.app.Fragment;

import dagger.Module;
import dagger.Provides;
import rokolabs.com.peoplefirst.di.scopes.PerFragment;


@Module
public final class FragmentModule {

    private final Fragment fragment;

    public FragmentModule(final Fragment fragment) {
        this.fragment = fragment;
    }
    @Provides
    @PerFragment
    Fragment provideFragment(){return fragment;}
}