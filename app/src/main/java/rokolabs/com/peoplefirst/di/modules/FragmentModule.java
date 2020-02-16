package rokolabs.com.peoplefirst.di.modules;


import androidx.fragment.app.Fragment;

import dagger.Module;


@Module
public final class FragmentModule {

    private final Fragment fragment;

    public FragmentModule(final Fragment fragment) {
        this.fragment = fragment;
    }

}