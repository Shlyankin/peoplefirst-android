package rokolabs.com.peoplefirst.report.ui.users.selected;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import rokolabs.com.peoplefirst.R;
import rokolabs.com.peoplefirst.model.User;
import rokolabs.com.peoplefirst.utils.Utils;

/**
 * Created by S on 19.05.2018.
 */

public class SelectedUsersAdapter extends RecyclerView.Adapter<SelectedUsersAdapter.ViewHolder> {


    private List<User> mUsers = null;
    private Context mContext;

    private boolean mRetailMode = false;
    private String mRetailMeEmail = "";

    private final PublishSubject<User> onClickDetailsSubject = PublishSubject.create();
    public final BehaviorSubject<User> onUserEdited = BehaviorSubject.create();

    public void setEntities(Context context, List<User> entities, boolean retailMode) {
        mContext = context;
        mUsers = entities;
        mRetailMode = retailMode;
    }

    public void setRetailMeEmail(String s) {
        mRetailMeEmail = s;
    }

    @SuppressLint("CheckResult")
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_selected_user, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mFirstName = v.findViewById(R.id.firstName);
        vh.mLastName = v.findViewById(R.id.lastName);
        vh.mCompany = v.findViewById(R.id.company);
        vh.mEmail = v.findViewById(R.id.email);
        vh.mClick = v.findViewById(R.id.clickView);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mFirstName.setText(mUsers.get(position).first_name);
        holder.mFirstName.setTag(new Integer(position));
        holder.mLastName.setText(mUsers.get(position).last_name);
        holder.mLastName.setTag(new Integer(position));
        if (mRetailMode) {
            holder.mEmail.setText(mUsers.get(position).email);
            holder.mEmail.setTag(new Integer(position));
            holder.mCompany.setVisibility(View.GONE);
            holder.mClick.setVisibility(View.GONE);
//            holder.mEmail.setEnabled(true);
//            holder.mFirstName.setEnabled(true);
//            holder.mLastName.setEnabled(true);
        } else {
//            holder.mCompany.setEnabled(false);
//            holder.mEmail.setEnabled(false);
//            holder.mFirstName.setEnabled(false);
//            holder.mLastName.setEnabled(false);
            holder.mClick.setVisibility(View.VISIBLE);
            holder.mEmail.setVisibility(View.GONE);
            if (mUsers.get(position).company != null)
                holder.mCompany.setText(mUsers.get(position).company.name);
            holder.mClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickDetailsSubject.onNext(mUsers.get(position));
                }
            });
        }
        if (mRetailMode) {
            RxTextView.afterTextChangeEvents(holder.mFirstName)
                    .skip(1)
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                        Integer pos = (Integer) e.component1().getTag();
                        if (mUsers.size() > pos) {
                            mUsers.get(pos).id = null;
                            mUsers.get(pos).first_name = e.getEditable().toString();
                            if (mRetailMode) {
                                onUserEdited.onNext(mUsers.get(pos));
                            }
                        }
                    });

            RxTextView.afterTextChangeEvents(holder.mLastName)
                    .skip(1)
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                        Integer pos = (Integer) e.component1().getTag();
                        if (mUsers.size() > pos) {
                            mUsers.get(pos).id = null;
                            mUsers.get(pos).last_name = e.getEditable().toString();
                            if (mRetailMode) {
                                onUserEdited.onNext(mUsers.get(pos));
                            }
                        }
                    });

            RxTextView.afterTextChangeEvents(holder.mEmail)
                    .skip(1)
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .filter(e -> Utils.isValidEmail(e.component2()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                        Integer pos = (Integer) e.component1().getTag();
                        if (mUsers.size() > pos) {
                            mUsers.get(pos).id = null;
                            mUsers.get(pos).email = e.getEditable().toString();
                            if (mRetailMode) {
                                onUserEdited.onNext(mUsers.get(pos));
                            }
                        }
                        if (!"".equals(e.component2().toString()) && !emailDomainValid(e.component2().toString())) {
                            e.component1().setError("People included in your report must have the same email domain as your username");
                        }
                    });
        }

    }

    public boolean emailDomainValid(String inputEmail) {
        return getDomain(mRetailMeEmail).equals(getDomain(inputEmail));
    }

    public static String getDomain(String email) {
        return email.substring(email.indexOf("@") + 1);
    }

    public Observable<User> getUsersClicks() {
        return onClickDetailsSubject;
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mFirstName;
        public TextView mLastName;
        public TextView mCompany;
        public TextView mEmail;
        public LinearLayout mClick;

        public ViewHolder(View v) {
            super(v);
        }
    }

}

