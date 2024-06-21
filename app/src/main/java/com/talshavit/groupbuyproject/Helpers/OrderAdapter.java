package com.talshavit.groupbuyproject.Helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private ArrayList<Order> orders;

    public OrderAdapter() {
    }

    public OrderAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.count.setText(String.valueOf(orders.get(position).getPrice())+" ש\"ח");
        holder.date.setText(orders.get(position).getDate());
        holder.time.setText(orders.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
