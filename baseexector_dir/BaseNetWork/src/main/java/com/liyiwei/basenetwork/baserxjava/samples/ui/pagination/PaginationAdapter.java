package com.liyiwei.basenetwork.baserxjava.samples.ui.pagination;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by amitshekhar on 15/03/17.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

//    List<String> items = new ArrayList<>();
//
//    public PaginationAdapter() {
//
//    }
//
//    void addItems(List<String> items) {
//        this.items.addAll(items);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return ItemViewHolder.create(parent);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ((ItemViewHolder) holder).bind(items.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }

//    private static class ItemViewHolder extends RecyclerView.ViewHolder {
//        ItemViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        static ItemViewHolder create(ViewGroup parent) {
//            return new ItemViewHolder(
//                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pagination, parent, false));
//        }
//
//        void bind(String content) {
//            ((TextView) itemView).setText(content);
//        }
//    }
}
