package com.talshavit.groupbuyproject.Helpers.SpecificOrderFromHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.Item;

import java.util.ArrayList;

public class SpecificOrderFromHistoryAdapter extends RecyclerView.Adapter<SpecificOrderFromHistoryViewAdapter> {

    private ArrayList<Item> items;
    private Context context;

    public SpecificOrderFromHistoryAdapter() {
    }

    public SpecificOrderFromHistoryAdapter(Context context, ArrayList<Item> items) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public SpecificOrderFromHistoryViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_items_for_order, parent, false);
        return new SpecificOrderFromHistoryViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecificOrderFromHistoryViewAdapter holder, int position) {
        Glide.with(context).load(items.get(position).getImg()).into(holder.itemImg);
        holder.itemCount.setText(String.valueOf(items.get(position).getCount()));
        holder.itemName.setText(items.get(position).getName());
        holder.itemPrice.setText(items.get(position).getPrice());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
