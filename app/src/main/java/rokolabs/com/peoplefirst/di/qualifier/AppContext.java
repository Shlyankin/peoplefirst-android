package rokolabs.com.peoplefirst.di.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by S on 19.04.2018.
 */

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface AppContext {
}