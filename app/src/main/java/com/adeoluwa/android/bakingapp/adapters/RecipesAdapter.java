package com.adeoluwa.android.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeoluwa.android.bakingapp.R;
import com.adeoluwa.android.bakingapp.models.Recipe;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Merlyne on 6/20/2017.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    final private ListItemClickListener mListClickListener;
    private List<Recipe> mList;
    private Context mContext;

    public interface ListItemClickListener{
        void onItemClick(int position);
    }
    public RecipesAdapter(List<Recipe> recipelist, ListItemClickListener listener){
        mList = recipelist;
        mListClickListener = listener;
    }
    @Override
    public RecipesAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        int layoutForListItem = R.layout.recipe_item;
        boolean attachImmediatelyToParent = false;

        View view = inflater.inflate(layoutForListItem, parent, attachImmediatelyToParent);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mList) return 0;
        return mList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        @BindView(R.id.img_recipe_image)
        ImageView recipeimage;
        @BindView(R.id.txt_recipe_name)
        TextView recipename;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Recipe recipe = mList.get(position);
            recipename.setText(recipe.getName());
            Glide.with(mContext)
                    .load(recipe.getImageurl())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(recipeimage);
        }

        @Override
        public void onClick(View v) {
            int itemClickedIndex = getAdapterPosition();
            mListClickListener.onItemClick(itemClickedIndex);
        }
    }
}
