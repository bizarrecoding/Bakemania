package com.bizarrecoding.example.bakemania.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.adapters.StepAdapter;
import com.bizarrecoding.example.bakemania.objects.Recipe;
import com.bizarrecoding.example.bakemania.objects.Step;

public class StepListFragment extends Fragment {

    private StepClickListener mListener;
    private Recipe recipe;

    public interface StepClickListener{
        void onStepClickListener(Step step);
    }

    public StepListFragment() {
    }

    public static StepListFragment newInstance(Recipe recipe) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putParcelable("Recipe",recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = (Recipe) getArguments().getParcelable("Recipe");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new StepAdapter(recipe.getSteps(), mListener));
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
