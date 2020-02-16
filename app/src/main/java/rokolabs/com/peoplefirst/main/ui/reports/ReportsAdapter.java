package rokolabs.com.peoplefirst.main.ui.reports;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import rokolabs.com.peoplefirst.R;
import rokolabs.com.peoplefirst.model.HarassmentType;
import rokolabs.com.peoplefirst.model.Report;
import rokolabs.com.peoplefirst.utils.Utils;

/**
 * Created by S on 19.05.2018.
 */

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder> {


    private ArrayList<Report> mEntities = null;
    private Context mContext;

    private final PublishSubject<Report> onClickDetailsSubject = PublishSubject.create();

    private final PublishSubject<Report> onClickEditSubject = PublishSubject.create();

    public void setEntities(Context context, ArrayList<Report> entities) {
        mContext = context;
        mEntities = entities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_report, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.mDateTime = v.findViewById(R.id.dateTime);
        vh.mLocation = v.findViewById(R.id.location);
        vh.mCheckboxWrapper = v.findViewById(R.id.checkboxWrapper);
        vh.mDetails = v.findViewById(R.id.details);
        vh.mEdit = v.findViewById(R.id.edit);
        return vh;
    }

    private void resize(View view, float scaleX, float scaleY) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) (view.getWidth() * scaleX);
        layoutParams.height = (int) (view.getHeight() * scaleY);
        view.setLayoutParams(layoutParams);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mDateTime.setText(Utils.dateFormat(mEntities.get(position).datetime));
        holder.mLocation.setText((mEntities.get(position).location_city == null ? "" : mEntities.get(position).location_city) + " "
                + (mEntities.get(position).location_details == null ? "" : mEntities.get(position).location_details) );
        holder.mCheckboxWrapper.removeAllViews();
        for (HarassmentType ht : mEntities.get(position).harassment_types.subList(0, Math.min(mEntities.get(position).harassment_types.size(), 3))) {
            CheckBox cb = new CheckBox(mContext);
            cb.setText(ht.title);
            cb.setChecked(true);
            cb.setTextSize(12);
            cb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            cb.setButtonDrawable(R.drawable.selector_checkbox);
            cb.setEnabled(false);
            final float scale = mContext.getResources().getDisplayMetrics().density;
            cb.setPadding(cb.getPaddingLeft() + (int)(6.0f * scale + 0.5f),
                    cb.getPaddingTop(),
                    cb.getPaddingRight(),
                    cb.getPaddingBottom());
            cb.setMaxLines(1);
            cb.setEllipsize(TextUtils.TruncateAt.END);
            holder.mCheckboxWrapper.addView(cb);
        }
        holder.mCheckboxWrapper.requestLayout();

        holder.mDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDetailsSubject.onNext(mEntities.get(position));
            }
        });
        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEditSubject.onNext(mEntities.get(position));
            }
        });

    }


    public Observable<Report> getDetailsClicks(){
        return onClickDetailsSubject;
    }
    public PublishSubject<Report> getOnClickEditSubject() {
        return onClickEditSubject;
    }

    @Override
    public int getItemCount() {
        return mEntities == null ? 0 : mEntities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDateTime;
        public TextView mLocation;
        public LinearLayout mCheckboxWrapper;
        public TextView mDetails;
        public TextView mEdit;
        public ViewHolder(View v) {
            super(v);
        }
    }

}

