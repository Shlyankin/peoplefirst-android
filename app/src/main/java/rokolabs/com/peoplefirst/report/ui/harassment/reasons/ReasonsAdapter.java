package rokolabs.com.peoplefirst.report.ui.harassment.reasons;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;
import rokolabs.com.peoplefirst.R;
import rokolabs.com.peoplefirst.model.HarassmentReason;

/**
 * Created by S on 19.04.2018.
 */

public class ReasonsAdapter extends RecyclerView.Adapter<ReasonsAdapter.ViewHolder> {

    private PublishSubject<HarassmentReason> mTypeClick = PublishSubject.create();

    public PublishSubject<HarassmentReason> getTypeClick() {
        return mTypeClick;
    }

    private ArrayList<HarassmentReason> mItems;
    private ArrayList<HarassmentReason> mSelected;

    private Context mContext;
    private Typeface typeface;

    public void setItems(Context context, ArrayList<HarassmentReason> mItems, ArrayList<HarassmentReason> selected) {
        mContext = context;
        this.mItems = mItems;
        this.mSelected = selected;
        typeface = ResourcesCompat.getFont(context, R.font.ratio_regular);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_harassment_type, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mType = v.findViewById(R.id.type);
        vh.mType.setTypeface(typeface);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mType.setText(mItems.get(position).title);
        if (mSelected.indexOf(mItems.get(position)) >= 0)
            holder.mType.setChecked(true);
        else
            holder.mType.setChecked(false);
        holder.mType.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mTypeClick.onNext(mItems.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return mItems == null? 0 : mItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mType;
        public ViewHolder(View v) {
            super(v);
        }
    }

}
