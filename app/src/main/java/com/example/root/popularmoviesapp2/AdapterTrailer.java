package com.example.root.popularmoviesapp2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterTrailer extends RecyclerView.Adapter<AdapterTrailer.ViewHolder> {
    private Context context;
    private List<DataTrailer> list;

    public AdapterTrailer(Context context, List<DataTrailer> list1) {
        this.context = context;
        this.list = list1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.trailerName.setText(list.get(position).getName());
        holder.trailerLink.setText("https://www.youtube.com/watch?v=" + list.get(position).getKey());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trailerName;
        TextView trailerLink;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerName = itemView.findViewById(R.id.trailerName);
            trailerLink = itemView.findViewById(R.id.trailerId);
        }
    }
}