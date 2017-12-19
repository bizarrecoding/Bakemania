package com.bizarrecoding.example.bakemania.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.adapters.IngredientAdapter;
import com.bizarrecoding.example.bakemania.adapters.StepAdapter;
import com.bizarrecoding.example.bakemania.objects.Ingredient;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.bizarrecoding.example.bakemania.objects.Step;
import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepListFragment extends Fragment {


    @BindView(R.id.nScroll) NestedScrollView nScrollView;
    @BindView(R.id.ingredientList) RecyclerView ingredientList;
    @BindView(R.id.stepList) RecyclerView stepList;
    @BindView(R.id.recipeImage) ImageView rImage;


    private StepClickListener mListener;
    private Recipe recipe;
    private int scrolled = 0;

    public interface StepClickListener{
        void onStepClickListener(Step step);
    }

    public StepListFragment() {
    }

    public static StepListFragment newInstance(Long recipeId) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putLong("Recipe",recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SugarContext.init(getContext());
        if (getArguments() != null) {
            Long recipeId = getArguments().getLong("Recipe");
            recipe = Recipe.findById(Recipe.class,recipeId);
        }
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey("scrolled")){
                scrolled = savedInstanceState.getInt("scrolled");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("scrolled",nScrollView.getScrollY());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);
        ButterKnife.bind(this,view);
        Context context = view.getContext();


        if(recipe.getImage().length()>0) {
            Picasso.with(getContext()).load(recipe.getImage()).into(rImage);
        }else {
            rImage.setVisibility(View.GONE);
            //Picasso.with(getContext()).load(R.drawable.nutella_pie).into(rImage);
        }
        List<Ingredient> ingredients = Step.find(Ingredient.class,"rid=?", String.valueOf(recipe.getRid()));
        ingredientList.setLayoutManager(new LinearLayoutManager(context));
        ingredientList.setAdapter(new IngredientAdapter(ingredients,getActivity()));

        List<Step> steps = Step.find(Step.class,"rid=?", String.valueOf(recipe.getRid()));
        stepList.setLayoutManager(new LinearLayoutManager(context));
        stepList.setAdapter(new StepAdapter(steps, mListener));

        nScrollView.scrollTo(nScrollView.getScrollX(),scrolled);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepClickListener) {
            mListener = (StepClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StepCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Ingredient> ingredients = Step.find(Ingredient.class,"rid=?", String.valueOf(recipe.getRid()));
        ingredientList.setAdapter(new IngredientAdapter(ingredients,getActivity()));
    }
}
