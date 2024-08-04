package com.talshavit.groupbuyproject.Helpers.History;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.talshavit.groupbuyproject.Fragments.AllItemsFragment;
import com.talshavit.groupbuyproject.Fragments.CartFragment;
import com.talshavit.groupbuyproject.Fragments.OrderFragment;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.models.Item;
import com.talshavit.groupbuyproject.models.Order;

import java.util.ArrayList;
import java.util.Optional;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {

    private ArrayList<Order> orders;

    private FragmentManager fragmentManager;


    public HistoryAdapter() {
    }

    public HistoryAdapter(FragmentManager fragmentManager, ArrayList<Order> orders) {
        this.fragmentManager = fragmentManager;
        this.orders = orders;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_order, parent, false);
        return new HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.count.setText(String.valueOf(orders.get(position).getPrice()) + " ש\"ח");
        holder.date.setText(orders.get(position).getDate());
        holder.time.setText(orders.get(position).getTime());
        onClick(holder, position);
        onAddToCartButton(holder, position);
    }

    private void onClick(HistoryViewHolder holder, int position) {
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalResources.replaceFragment(fragmentManager, new OrderFragment(orders.get(position)));
            }
        });
    }

    private void onAddToCartButton(HistoryViewHolder holder, int position) {
        holder.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDEDBF")));
                GlobalResources.cart.setItems(orders.get(position).getCopiedCart().items);
                for (int i = 0; i < orders.get(position).getCart().items.size(); i++) {
                    int finalI = i;
                    Optional<Item> isExist = GlobalResources.cart.items.stream()
                            .filter(item -> orders.get(position).getCart().items.get(finalI).getId().equals(item.getId()))
                            .findFirst();

                    if (isExist.isPresent()) {
                        Item item = isExist.get();
                        item.setCount(orders.get(position).getCart().items.get(finalI).getCount());
                    }
                }
                openCart(view);
            }
        });
    }

    private void openCart(View view) {
        GlobalResources.replaceFragment(fragmentManager, new CartFragment());
        ((MainActivity) view.getContext()).selectCartTab();
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

}
