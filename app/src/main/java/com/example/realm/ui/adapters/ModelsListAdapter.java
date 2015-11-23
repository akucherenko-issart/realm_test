package com.example.realm.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realm.R;
import com.example.realm.model.Model;

import java.util.List;

/**
 * Models recycler adapter
 */
public class ModelsListAdapter extends RecyclerView.Adapter<ModelsListAdapter.ViewHolder>  {

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) itemView.findViewById(R.id.text_uuid);
        }

        public void bind(final Model model) {
            mTextView.setText(model.uuid);
        }
    }

    private List<Model> mModels;

    public void setModels(List<Model> models) {
        mModels = models;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.model_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Model model = mModels.get(position);
        if (model != null) {
            viewHolder.bind(model);
        }
    }

    @Override
    public int getItemCount() {
        return (mModels != null)? mModels.size(): 0;
    }
}
