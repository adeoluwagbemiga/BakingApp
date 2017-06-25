package com.adeoluwa.android.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeoluwa.android.bakingapp.R;
import com.adeoluwa.android.bakingapp.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Merlyne on 6/20/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder> {
    final private ListItemClickListener mListClickListener;
    private List<Step> mList;
    private Context mContext;
    public interface ListItemClickListener{
        void onItemClick(int position);
    }
    public StepsAdapter(ListItemClickListener listItemClickListener, List stepslist){
        this.mListClickListener = listItemClickListener;
        mList = stepslist;
    }
    @Override
    public StepsAdapter.StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int layoutForListItem = R.layout.step_item;
        boolean attachImmediatelyToParent = false;

        View view = inflater.inflate(layoutForListItem, parent, attachImmediatelyToParent);
        StepsAdapter.StepViewHolder viewHolder = new StepsAdapter.StepViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mList) return 0;
        return mList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.txt_step_short_description)
        TextView mShortDescription;
        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Step step = mList.get(position);
            mShortDescription.setText(step.getShortdescription());
        }

        @Override
        public void onClick(View v) {
            int itemClickedIndex = getAdapterPosition();
            mListClickListener.onItemClick(itemClickedIndex);
        }
    }
}
