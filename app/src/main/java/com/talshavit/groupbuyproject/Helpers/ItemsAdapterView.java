package com.talshavit.groupbuyproject.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.models.Item;
import com.talshavit.groupbuyproject.R;

import java.util.ArrayList;

public class ItemsAdapterView extends RecyclerView.Adapter<MyViewHolderItems> {

    private ArrayList<Item> allItems;
    private Context context;
    private int currentCount;


    public ItemsAdapterView(Context context, ArrayList<Item> allItems) {
        this.context = context;
        this.allItems = allItems;
    }

    @NonNull
    @Override
    public MyViewHolderItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolderItems(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItems holder, int position) {
        holder.itemName.setText(allItems.get(position).getName());
        holder.company.setText(allItems.get(position).getCompany());
        holder.count.setText(String.valueOf(allItems.get(position).getCount()));
        String image = allItems.get(position).getImg();
        Glide.with(context).load(image).into(holder.img);
        onPlusOrMinus(holder);
        onAddItemButton(holder, position);
    }

    private void onAddItemButton(MyViewHolderItems holder, int position) {
        holder.addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(holder.count.getText().toString());
                if(count>0){
                    allItems.get(position).setCount(count);
                    GlobalResources.cart.items.add(allItems.get(position));
                }
            }
        });
    }

    private void onPlusOrMinus(MyViewHolderItems holder) {
        onMinusButton(holder, currentCount);
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCount = Integer.parseInt(holder.count.getText().toString());
                if (currentCount != 0) {
                    currentCount--;
                    holder.count.setText(String.valueOf(currentCount));
                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCount = Integer.parseInt(holder.count.getText().toString());
                currentCount++;
                holder.count.setText(String.valueOf(currentCount));
            }
        });
    }

    private void onMinusButton(MyViewHolderItems holder, int currentCount) {
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }
}
