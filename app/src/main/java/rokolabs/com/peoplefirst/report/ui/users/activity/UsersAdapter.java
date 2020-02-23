package rokolabs.com.peoplefirst.report.ui.users.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import rokolabs.com.peoplefirst.R;
import rokolabs.com.peoplefirst.model.User;

/**
 * Created by S on 19.05.2018.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {


    private List<User> mUsers = null;
    private Context mContext;

    private final PublishSubject<User> onClickDetailsSubject = PublishSubject.create();


    public void setEntities(Context context, List<User> entities) {
        mContext = context;
        mUsers = entities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_user, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mName = v.findViewById(R.id.first_name);
        vh.mCompany = v.findViewById(R.id.company);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(mUsers.get(position).first_name + " " + mUsers.get(position).last_name);
        holder.mCompany.setText((mUsers.get(position).company!= null ? mUsers.get(position).company.name : ""));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDetailsSubject.onNext(mUsers.get(position));
            }
        });

    }

    public Observable<User> getUsersClicks(){
        return onClickDetailsSubject;
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mCompany;
        public ViewHolder(View v) {
            super(v);
        }
    }

}

