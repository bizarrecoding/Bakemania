package com.bizarrecoding.example.bakemania.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.adapters.StepAdapter;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.bizarrecoding.example.bakemania.objects.Step;

import java.util.List;
import java.util.StringTokenizer;

public class StepListFragment extends Fragment {

    private StepClickListener mListener;
    private Recipe recipe;

    public interface StepClickListener{
        void onStepClickListener(Step step);
    }

    public StepListFragment() {
    }

    public static StepListFragment newInstance(Long recipeId) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putLong("Recipe",recipeId);
        Log.d("Recipe","id: "+recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Long recipeId = getArguments().getLong("Recipe");
            recipe = Recipe.findById(Recipe.class,recipeId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            List<Step> steps = Step.find(Step.class,"rid=?", String.valueOf(recipe.getRid()));
            Log.d("STEPS","rid: "+recipe.getRid()+" found: "+steps.size());
            recyclerView.setAdapter(new StepAdapter(steps, mListener));
        }
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
}
