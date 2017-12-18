package com.bizarrecoding.example.bakemania.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizarrecoding.example.bakemania.R;
import com.bizarrecoding.example.bakemania.fragments.StepListFragment.StepClickListener;
import com.bizarrecoding.example.bakemania.objects.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final List<Step> mSteps;
    private final StepClickListener mListener;

    public StepAdapter(List<Step> steps, StepClickListener listener) {
        mSteps = steps;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Step step = mSteps.get(position);
        holder.mIdView.setText("Step #"+position);
        holder.mContentView.setText(step.getShortDescription());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onStepClickListener(step);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        final View mView;
        @BindView(R.id.stepLayout) LinearLayout mLayout;
        @BindView(R.id.id) TextView mIdView;
        @BindView(R.id.content) TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
