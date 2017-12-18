package com.bizarrecoding.example.bakemania.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.StepsActivity;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private Context ctx;

    public RecipeAdapter(List<Recipe> items){
        this.recipes = new ArrayList<>();
        this.recipes.addAll(items);
    }

    public void setRecipes() {
        this.recipes = Recipe.listAll(Recipe.class);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_recipe, parent, false);
        return new RecipeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        holder.mTitle.setText(recipe.getName());
        holder.mContent.setText("for "+recipe.getServings()+" servings");
        if(recipe.getImage().length() > 0) {
            holder.mCover.setImageResource(R.color.colorAccent);
        }else {
            holder.mCover.setVisibility(View.GONE);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ctx, StepsActivity.class);
                in.putExtra("Recipe",recipe.getId());
                //Log.d("RECIPE","selected: "+recipe.getId() +"\nrid: "+recipe.getRid());
                ctx.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View root;
        @BindView(R.id.cover) ImageView mCover;
        @BindView(R.id.title) TextView mTitle;
        @BindView(R.id.content) TextView mContent;

        ViewHolder(View view) {
            super(view);
            root = view;
            ButterKnife.bind(this, view);
        }
    }
}
