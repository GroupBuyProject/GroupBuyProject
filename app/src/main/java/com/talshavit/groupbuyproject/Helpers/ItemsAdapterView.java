package com.talshavit.groupbuyproject.Helpers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.MainActivity;
import com.talshavit.groupbuyproject.models.Category;
import com.talshavit.groupbuyproject.models.Item;
import com.talshavit.groupbuyproject.R;

import java.util.ArrayList;

public class ItemsAdapterView extends RecyclerView.Adapter<MyViewHolderItems> {

    private ArrayList<Item> allItems;
    private ArrayList<Item> allItemsFull;
    private Context context;
    private String type;
    private boolean isFruitAndVeg = false;

    public ItemsAdapterView() {
    }

    public ItemsAdapterView(Context context, ArrayList<Item> allItems, String type) {
        this.context = context;
        this.allItems = allItems;
        this.allItemsFull = new ArrayList<>(allItems);
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolderItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolderItems(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderItems holder, int position) {
        isFruitAndVeg = checkCategory(holder, position);
        holder.itemName.setText(allItems.get(position).getName());
        holder.company.setText(allItems.get(position).getCompany());
        holder.weight.setText(allItems.get(position).getWeight());
        holder.price.setText(allItems.get(position).getPrice());
        if (isFruitAndVeg)
            holder.count.setText(String.valueOf(allItems.get(position).getCount()));
        else
            holder.count.setText(String.valueOf((int) allItems.get(position).getCount()));
        String image = allItems.get(position).getImg();
        Glide.with(context).load(image).into(holder.img);
        double sum = Double.parseDouble(holder.count.getText().toString());
        if (sum != 0.0) {
            holder.addItemButton.setText("עדכון");
        }
        onMinusButton(holder, position);
        onPlusButton(holder, position);
        onAddItemButton(holder, position);
        onXbutton(holder, position);
        checkCategory(holder, position);
    }

    private boolean checkCategory(MyViewHolderItems holder, int position) {
        if (allItems.get(position).getCategory().equals(Category.FruitsAndVegetables)) {
            holder.unit.setText(R.string.unitFruit);
            return true;
        }
        return false;
    }

    private void onXbutton(MyViewHolderItems holder, int position) {
        if (type.equals("CartFragment")) {
            holder.xButton.setVisibility(View.VISIBLE);
            holder.xButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalResources.cart.items.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else
            holder.xButton.setVisibility(View.GONE);
    }

    private void onAddItemButton(MyViewHolderItems holder, int position) {
        if (type.equals("CartFragment")) {
            holder.addItemButton.setText("עדכון");
        }
        holder.addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFruitAndVeg) {
                    changeToDouble(holder, position);
                } else {
                    changeToInt(holder, position);
                }
            }
        });
    }

    private void animateToCart(View imageView) {
        imageView.animate().setDuration(1000)
                .rotationYBy(360f)
                .start();
    }

    private void changeToInt(MyViewHolderItems holder, int position) {
        int count = Integer.parseInt(holder.count.getText().toString());
        if (count > 0) {
            allItems.get(position).setCount(count);
            if (!GlobalResources.cart.items.contains(allItems.get(position))) {
                GlobalResources.cart.items.add(allItems.get(position));
            } else {
                GlobalResources.cart.items.set(position, allItems.get(position));
            }
            holder.addItemButton.setText("עדכון");
            animateToCart(holder.img);
        }
    }

    private void changeToDouble(MyViewHolderItems holder, int position) {
        double count = Double.parseDouble(holder.count.getText().toString());
        if (count > 0) {
            allItems.get(position).setCount(count);
            if (!GlobalResources.cart.items.contains(allItems.get(position))) {
                GlobalResources.cart.items.add(allItems.get(position));
            } else {
                GlobalResources.cart.items.set(position, allItems.get(position));
            }
            holder.addItemButton.setText("עדכון");
            animateToCart(holder.img);
        }
    }

    private void onMinusButton(MyViewHolderItems holder, int position) {
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFruitAndVeg) {
                    double currentCount = Double.parseDouble(holder.count.getText().toString());
                    if (currentCount != 0.0) {
                        currentCount -= 0.5;
                        holder.count.setText(String.valueOf(currentCount));
                    }
                } else {
                    double currentCount = Double.parseDouble(holder.count.getText().toString());
                    if (currentCount != 0.0) {
                        currentCount -= 1.0;
                        int res = (int) currentCount;
                        holder.count.setText(String.valueOf(res));
                    }
                }
            }
        });
    }

    private void onPlusButton(MyViewHolderItems holder, int position) {
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFruitAndVeg) {
                    double currentCount = Double.parseDouble(holder.count.getText().toString());
                    currentCount += 0.5;
                    holder.count.setText(String.valueOf(currentCount));
                } else {
                    double currentCount = Double.parseDouble(holder.count.getText().toString());
                    currentCount += 1.0;
                    int res = (int) currentCount;
                    holder.count.setText(String.valueOf(res));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public void filterByItemAndCompany(String textItem, String textCompany) {
        ArrayList<Item> filteredList = new ArrayList<>();
        for (Item item : allItemsFull) {
            if (item.getName().contains(textItem) &&
                    item.getCompany().contains(textCompany)) {
                filteredList.add(item);
            }
        }
        allItems.clear();
        allItems.addAll(filteredList);
        notifyDataSetChanged();
        GlobalResources.items = allItemsFull;
    }

    public void changeCount(){
        for(int i=0; i<allItems.size(); i++){
            if(allItems.get(i).getCount()!=0){
                allItems.get(i).setCount(0);
            }
        }
    }
}
