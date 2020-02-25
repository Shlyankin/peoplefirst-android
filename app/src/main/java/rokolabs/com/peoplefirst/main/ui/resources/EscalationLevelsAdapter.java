package rokolabs.com.peoplefirst.main.ui.resources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import rokolabs.com.peoplefirst.R;
import rokolabs.com.peoplefirst.model.EscalationLevel;

/**
 * Created by S on 19.04.2018.
 */

public class EscalationLevelsAdapter extends RecyclerView.Adapter<EscalationLevelsAdapter.ViewHolder> {

    private PublishSubject<EscalationLevel> mLevelClick = PublishSubject.create();

    public PublishSubject<EscalationLevel> getLevelClick() {
        return mLevelClick;
    }

    public BehaviorSubject<Boolean> editable = BehaviorSubject.create();
    private ArrayList<EscalationLevel> mItems;

    private Context mContext;

    public void setItems(Context context, ArrayList<EscalationLevel> mItems) {
        mContext = context;
        this.mItems = mItems;
    }

    public ArrayList<EscalationLevel> getmItems() {
        return mItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_escalation, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mName = v.findViewById(R.id.first_name);
        vh.mContact = v.findViewById(R.id.contact);
        vh.mDays = v.findViewById(R.id.days);
        return vh;

    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(mItems.get(position).name);
        holder.mContact.setText(mItems.get(position).contact);
        holder.mDays.setText(String.valueOf(mItems.get(position).days));
        editable.observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
            holder.mContact.setEnabled(aBoolean);
            holder.mDays.setEnabled(aBoolean);
            holder.mName.setEnabled(aBoolean);
            if (aBoolean) {
                RxTextView.afterTextChangeEvents(holder.mContact)
                        .skip(1)
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(e -> {
                            if (position < mItems.size()) {
                                mItems.get(position).contact = e.getEditable().toString();
                            }
                        });
                RxTextView.afterTextChangeEvents(holder.mDays)
                        .skip(1)
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(e -> {
                            if (position < mItems.size()) {
                                String days = e.getEditable().toString();
                                try {
                                    mItems.get(position).days = Integer.parseInt(days);
                                } catch (NumberFormatException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                RxTextView.afterTextChangeEvents(holder.mName)
                        .skip(1)
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(e -> {
                            if (position < mItems.size()) {
                                mItems.get(position).name = e.getEditable().toString();
                            }
                        });
            }
        });
        holder.itemView.setOnClickListener(buttonView -> {
            mLevelClick.onNext(mItems.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText mName;
        public EditText mContact;
        public EditText mDays;

        public ViewHolder(View v) {
            super(v);
        }
    }

}
