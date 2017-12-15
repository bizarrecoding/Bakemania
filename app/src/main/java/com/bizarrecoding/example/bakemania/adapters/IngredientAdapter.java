package com.bizarrecoding.example.bakemania.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bizarrecoding.example.bakemania.BakeListWidget;
import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.objects.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final Context ctx;
    private List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> list, Context ctx){
        this.ingredients = list;
        this.ctx= ctx;
    }

    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_ingredient, parent, false);
        return new IngredientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IngredientAdapter.ViewHolder holder, final int position) {
        final Ingredient i = ingredients.get(position);
        holder.ingredient.setText(i.getName());
        holder.ingredient.setTextColor(i.getTaken() == 0 ? Color.BLACK : Color.GRAY);
        holder.ingredient.setChecked(i.getTaken()==1);
        holder.ingredient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                update(position);
                holder.ingredient.setTextColor(isChecked ? Color.GRAY : Color.BLACK);
            }
        });
        holder.unit.setText(i.getQuantity()+i.getMeasure().toLowerCase());
    }

    private void update(int position){
        Ingredient i = ingredients.get(position);
        int taken = i.getTaken();
        if(taken==0){
            Ingredient.executeQuery("UPDATE INGREDIENT SET TAKEN = 1 WHERE ID = ?",new String[]{String.valueOf(i.getId())});
        }else{
            Ingredient.executeQuery("UPDATE INGREDIENT SET TAKEN = 0 WHERE ID = ?",new String[]{String.valueOf(i.getId())});
        }
        Intent in = new Intent(ctx,BakeListWidget.class);
        in.setAction(BakeListWidget.INGREDIENT_UPDATE);
        in.putExtra("ingredient",i.getId());
        ctx.sendBroadcast(in);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredientsLabels) CheckBox ingredient;
        @BindView(R.id.unitsLabels) TextView unit;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
