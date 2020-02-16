package rokolabs.com.peoplefirst.main.ui.resources;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;
import rokolabs.com.peoplefirst.R;
import rokolabs.com.peoplefirst.model.CounsellingService;

/**
 * Created by S on 19.04.2018.
 */

public class CounsellingServicesAdapter extends RecyclerView.Adapter<CounsellingServicesAdapter.ViewHolder> {

    private PublishSubject<CounsellingService> mLevelClick = PublishSubject.create();

    public PublishSubject<CounsellingService> getLevelClick() {
        return mLevelClick;
    }

    private ArrayList<CounsellingService> mItems;

    private Context mContext;

    public void setItems(Context context, ArrayList<CounsellingService> mItems) {
        mContext = context;
        this.mItems = mItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_escalation, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mName = v.findViewById(R.id.first_name);
        vh.mContact = v.findViewById(R.id.contact);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(mItems.get(position).name);
        holder.mContact.setText(mItems.get(position).contact);
        holder.itemView.setOnClickListener(buttonView -> {
            mLevelClick.onNext(mItems.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return mItems == null? 0 : mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mContact;
        public ViewHolder(View v) {
            super(v);
        }
    }

}
