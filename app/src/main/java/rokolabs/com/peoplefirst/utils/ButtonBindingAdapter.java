package rokolabs.com.peoplefirst.utils;

import android.view.View;

import androidx.databinding.BindingAdapter;

import io.reactivex.subjects.Subject;

public class ButtonBindingAdapter {

    @BindingAdapter("reactiveClick")
    public static void reactiveClick(View view, Subject<View> subject) {
        view.setOnClickListener(subject::onNext);
    }


}
