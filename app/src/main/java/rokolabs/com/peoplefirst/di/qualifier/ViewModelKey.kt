package rokolabs.com.peoplefirst.di.qualifier

import androidx.lifecycle.ViewModel
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(RetentionPolicy.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)