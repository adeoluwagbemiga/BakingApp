package com.adeoluwa.android.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adeoluwa.android.bakingapp.R;
import com.adeoluwa.android.bakingapp.models.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Merlyne on 6/20/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {
    private List<Ingredient> mList;
    private Context mContext;
    public IngredientsAdapter(List<Ingredient> ingredientlist){
        mList = ingredientlist;
    }
    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int layoutForListItem = R.layout.ingredient_item;
        boolean attachImmediatelyToParent = false;

        View view = inflater.inflate(layoutForListItem, parent, attachImmediatelyToParent);
        IngredientsAdapter.IngredientViewHolder viewHolder = new IngredientsAdapter.IngredientViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mList) return 0;
        return mList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_ingredient_quantity)
        TextView mQuantity;
        @BindView(R.id.txt_ingredient_measure) TextView mMeasure;
        @BindView(R.id.txt_ingredient_description) TextView mDescription;
        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            Ingredient ingredient = mList.get(position);
            mQuantity.setText(String.valueOf(ingredient.getQuantity()));
            mDescription.setText(" " + ingredient.getIngredient());
            mMeasure.setText(" " + ingredient.getMeasure());
        }
    }
}
