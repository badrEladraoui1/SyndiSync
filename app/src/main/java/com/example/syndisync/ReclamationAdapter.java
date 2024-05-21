package com.example.syndisync;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReclamationAdapter extends RecyclerView.Adapter<ReclamationAdapter.ReclamationViewHolder> {

    private List<CharSequence> reclamationList;

    public ReclamationAdapter(List<CharSequence> reclamationList) {
        this.reclamationList = reclamationList;
    }

    @NonNull
    @Override
    public ReclamationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reclamation_item, parent, false);
        return new ReclamationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReclamationViewHolder holder, int position) {
        CharSequence description = reclamationList.get(position);
        holder.descriptionTextView.setText(description, TextView.BufferType.SPANNABLE);
    }

    @Override
    public int getItemCount() {
        return reclamationList.size();
    }

    static class ReclamationViewHolder extends RecyclerView.ViewHolder {

        TextView descriptionTextView;

        public ReclamationViewHolder(@NonNull View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}