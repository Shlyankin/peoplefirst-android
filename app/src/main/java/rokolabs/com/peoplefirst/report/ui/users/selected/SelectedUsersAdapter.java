package rokolabs.com.peoplefirst.report.ui.users.selected;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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


    public void setEntities(Context context, List<User> entities, boolean retailMode) {
        mContext = context;
        mUsers = entities;
        mRetailMode = retailMode;
    }

    public void setRetailMeEmail(String s) {
        mRetailMeEmail = s;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_selected_user, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mFirstName = v.findViewById(R.id.firstName);
        vh.mLastName = v.findViewById(R.id.lastName);
        vh.mCompany = v.findViewById(R.id.company);
        vh.mEmail = v.findViewById(R.id.email);

        if (mRetailMode) {
            RxTextView.afterTextChangeEvents(vh.mFirstName)
                    .skip(1)
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                        Integer pos = (Integer)e.component1().getTag();
                        mUsers.get(pos).first_name = e.getEditable().toString();
                    });

            RxTextView.afterTextChangeEvents(vh.mLastName)
                    .skip(1)
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                        Integer pos = (Integer)e.component1().getTag();
                        mUsers.get(pos).last_name = e.getEditable().toString();
                    });

            RxTextView.afterTextChangeEvents(vh.mEmail)
                    .skip(1)
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .filter(e -> Utils.isValidEmail(e.component2()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> {
                        Integer pos = (Integer)e.component1().getTag();
                        mUsers.get(pos).email = e.getEditable().toString();
                        if (!"".equals(e.component2().toString()) && !emailDomainValid(e.component2().toString())) {
                            e.component1().setError("People included in your report must have the same email domain as your username");
                        }
                    });
        }
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

        } else {
            holder.mEmail.setVisibility(View.GONE);
            holder.mCompany.setText(mUsers.get(position).company.name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickDetailsSubject.onNext(mUsers.get(position));
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

    public Observable<User> getUsersClicks(){
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
        public ViewHolder(View v) {
            super(v);
        }
    }

}

