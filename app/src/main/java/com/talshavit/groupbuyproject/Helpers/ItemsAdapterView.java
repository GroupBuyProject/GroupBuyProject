package com.talshavit.groupbuyproject.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.talshavit.groupbuyproject.GlobalResources;
import com.talshavit.groupbuyproject.models.Category;
import com.talshavit.groupbuyproject.models.Item;
import com.talshavit.groupbuyproject.R;

import java.util.ArrayList;
import java.util.Collections;

public class ItemsAdapterView extends RecyclerView.Adapter<MyViewHolderItems> {

    private ArrayList<Item> allItems;
    private ArrayList<Item> allItemsFull;
    private Context context;
    private String type;
    private boolean isFruitAndVeg = false;
    private LinearLayout lastVisibleControls = null, linearLayout;
    private ImageView imageView;
    private TextView textView, red_text, count_text, dont_show_text;
    private AppCompatImageButton minus, plus;
    private AppCompatTextView count;
    private GridLayout.LayoutParams params;
    private int category ;

    public ItemsAdapterView() {
    }

    public ItemsAdapterView(Context context, ArrayList<Item> allItems, String type, int category) {
        this.context = context;
        this.allItems = allItems;
        this.allItemsFull = new ArrayList<>(allItems);
        this.type = type;
        this.category = category;
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
        onMinusButton(holder.minus, holder.count, isFruitAndVeg, allItems.get(position));
        onPlusButton(holder.plus, holder.count, isFruitAndVeg, allItems.get(position));
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
                    Item itemToRemove = GlobalResources.cart.getItems().get(position);
                    removeFromCart(itemToRemove);
                    //removeFromCart(position);
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
                if (holder.addItemButton.getText().equals("עדכון")) {
                    if (count.getText().equals("0") || count.getText().equals("0.0")) {
                        if (!GlobalResources.cart.items.isEmpty()) {
                            Item itemToRemove = GlobalResources.allItemsByCategories[category].get(position);
                            removeFromCart(itemToRemove);
                            //removeFromCart(position);
                            if (GlobalResources.items.contains(GlobalResources.items.get(position))) {
                                if (isFruitAndVeg)
                                    GlobalResources.items.get(position).setCount(0.0);
                                else
                                    GlobalResources.items.get(position).setCount(0);
                                holder.addItemButton.setText("הוספה");
                            }
                        }
                    }
                }
                if (isFruitAndVeg) {
                    changeToDouble(holder, position);
                } else {
                    changeToInt(holder, position);
                }
                checkIfCloseToLimit();
            }
        });
    }

    private void checkIfCloseToLimit() {
        if (GlobalResources.limitAmount > 0) {
            GlobalResources.orderPrice = 0;
            double temporaryAmount = GlobalResources.limitAmount;
            for (int i = 0; i < GlobalResources.cart.items.size(); i++) {
                GlobalResources.orderPrice += Double.parseDouble(GlobalResources.cart.items.get(i).getPrice()) * GlobalResources.cart.items.get(i).getCount();
                if ((temporaryAmount - GlobalResources.orderPrice) <= GlobalResources.limitPercent && (temporaryAmount - GlobalResources.orderPrice) > 0) {
                    openDialog(temporaryAmount - GlobalResources.orderPrice);
                }
                if ((temporaryAmount - GlobalResources.orderPrice) <= 0 && GlobalResources.countForShowingDialog == 0) {
                    openDialog(0.0);
                }
            }
        }
    }

    private void openDialog(double count) {
        Dialog dialog = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_for_alerting_limit, null);
        dialog.setContentView(dialogView);

        findViewsDialog(dialogView);
        initDialog(count);

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    private void initDialog(double count) {
        if (count > 0.0) {
            red_text.setText("אתה מתקרב לסכום המוגבל");
            String formattedCount = String.format("%.2f", this.count);
            count_text.setText("נותר לך עוד " + formattedCount + " ש''ח");
        } else {
            dont_show_text.setPaintFlags(dont_show_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            dont_show_text.setVisibility(View.VISIBLE);
            dont_show_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalResources.countForShowingDialog += 1;
                }
            });
        }
    }

    private void findViewsDialog(View dialogView) {
        red_text = dialogView.findViewById(R.id.red_text);
        count_text = dialogView.findViewById(R.id.count_text);
        dont_show_text = dialogView.findViewById(R.id.dont_show_text);
    }

    private void removeFromCart(Item itemToRemove) {
        if (GlobalResources.cart.getItems().contains(itemToRemove)) {
            GlobalResources.cart.getItems().remove(itemToRemove);
        }

        for (Item item : GlobalResources.items) {
            if (item.getId().equals(itemToRemove.getId())) {
                if (isFruitAndVeg) {
                    item.setCount(0.0);
                } else {
                    item.setCount(0);
                }
                break;
            }
        }

        notifyDataSetChanged();
    }

    private void animateToCart(View imageView) {
        imageView.animate().setDuration(1000)
                .rotationYBy(360f)
                .start();
    }

    private void changeToInt(MyViewHolderItems holder, int position) {
        int countValue = Integer.parseInt(holder.count.getText().toString());
        if (countValue > 0) {
            boolean found = false;
            for (Item item : GlobalResources.cart.items) {
                if (item.getId().equals(allItems.get(position).getId())) {
                    item.setCount(countValue);
                    found = true;
                    break;
                }
            }
            if (!found) {
                allItems.get(position).setCount(countValue);
                GlobalResources.cart.items.add(allItems.get(position));
            } else {
                int index = GlobalResources.items.indexOf(allItems.get(position));
                GlobalResources.items.get(index).setCount(countValue);
            }
            holder.addItemButton.setText("עדכון");
            dialogCompletionOrder(allItems.get(position));
            animateToCart(holder.img);
        }
    }

    private void changeToDouble(MyViewHolderItems holder, int position) {
        double countValue = Double.parseDouble(holder.count.getText().toString());
        if (countValue > 0) {
            boolean found = false;
            for (Item item : GlobalResources.cart.items) {
                if (item.getId().equals(allItems.get(position).getId())) {
                    item.setCount(countValue);
                    found = true;
                    break;
                }
            }
            if (!found) {
                allItems.get(position).setCount(countValue);
                GlobalResources.cart.items.add(allItems.get(position));
            } else {
                int index = GlobalResources.items.indexOf(allItems.get(position));
                GlobalResources.items.get(index).setCount(countValue);
            }
            holder.addItemButton.setText("עדכון");
            dialogCompletionOrder(allItems.get(position));
            animateToCart(holder.img);
        }
    }

    private void dialogCompletionOrder(Item currentItem) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pop_up_completion_order);

        GridLayout gridLayout = dialog.findViewById(R.id.gridLayout);
        MaterialButton confirm_button = dialog.findViewById(R.id.confirm_button);

        ArrayList<Item> relatedItems = getRandomRelatedItems(currentItem.getRelatedItems());
        addItemsToGridLayout(gridLayout, relatedItems);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Item item : relatedItems) {
                    changeItemCount(item, item.getCount());
                }
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    private ArrayList<Item> getRandomRelatedItems(ArrayList<Integer> relatedItemIds) {
        ArrayList<Item> relatedItems = new ArrayList<>();
        Collections.shuffle(relatedItemIds);

        for (int i = 0; i < Math.min(relatedItemIds.size(), 6); i++) {
            Integer id = relatedItemIds.get(i);
            for (Item item : GlobalResources.items) {
                if (item.getId().equals(String.valueOf(id))) {
                    relatedItems.add(item);
                    break;
                }
            }
        }
        return relatedItems;
    }

    private void addItemsToGridLayout(GridLayout gridLayout, ArrayList<Item> relatedItems) {
        for (int i = 0; i < relatedItems.size(); i++) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.grid_item_completion, null);

            findViewsGridAdapter(itemView);

            Item item = relatedItems.get(i);
            textView.setText(item.getName());
            Glide.with(context).load(item.getImg()).into(imageView);

            isFruitAndVeg = item.getCategory().equals(Category.FruitsAndVegetables);
            if (isFruitAndVeg) {
                count.setText(String.valueOf(item.getCount()));
            } else {
                int integer = (int) item.getCount();
                count.setText(String.valueOf(integer));
            }

            initGridAdapter(itemView, gridLayout, i);

            onImageClick(imageView, linearLayout, count);
            onMinusButton(minus, count, isFruitAndVeg, item);
            onPlusButton(plus, count, isFruitAndVeg, item);
        }
    }

    private void findViewsGridAdapter(View itemView) {
        imageView = itemView.findViewById(R.id.item_image);
        textView = itemView.findViewById(R.id.item_name);
        linearLayout = itemView.findViewById(R.id.add_item_controls);
        minus = itemView.findViewById(R.id.minus);
        plus = itemView.findViewById(R.id.plus);
        count = itemView.findViewById(R.id.count);
    }

    private void initGridAdapter(View itemView, GridLayout gridLayout, int i) {
        params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(i % 3, 1f);
        params.rowSpec = GridLayout.spec(i / 3);

        gridLayout.addView(itemView, params);
    }

    private void onImageClick(ImageView imageView, LinearLayout linearLayout, AppCompatTextView count) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastVisibleControls != null && lastVisibleControls != linearLayout) {
                    lastVisibleControls.setVisibility(View.GONE);
                }
                linearLayout.setVisibility(View.VISIBLE);
                lastVisibleControls = linearLayout;
            }
        });
    }

    private void onMinusClick(AppCompatTextView countTextView, boolean isFruitAndVeg, Item item) {
        double currentCount = Double.parseDouble(countTextView.getText().toString());
        if (isFruitAndVeg) {
            if (currentCount != 0.0) {
                currentCount -= 0.5;
                countTextView.setText(String.valueOf(currentCount));
            } else {
                countTextView.setText(String.valueOf(currentCount));
            }
        } else {
            if (currentCount != 0.0) {
                currentCount -= 1.0;
                int res = (int) currentCount;
                countTextView.setText(String.valueOf(res));
            } else {
                countTextView.setText("0");
            }
        }
        item.setCount(currentCount);
    }

    private void onMinusButton(AppCompatImageButton minusButton, AppCompatTextView countTextView, boolean isFruitAndVeg, Item item) {
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMinusClick(countTextView, isFruitAndVeg, item);
            }
        });
    }

    private void onPlusClick(AppCompatTextView countTextView, boolean isFruitAndVeg, Item item) {
        double currentCount = Double.parseDouble(countTextView.getText().toString());
        if (isFruitAndVeg) {
            currentCount += 0.5;
            countTextView.setText(String.valueOf(currentCount));
        } else {
            currentCount += 1.0;
            int res = (int) currentCount;
            countTextView.setText(String.valueOf(res));
        }
        item.setCount(currentCount);
    }


    private void onPlusButton(AppCompatImageButton plusButton, AppCompatTextView countTextView, boolean isFruitAndVeg, Item item) {
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlusClick(countTextView, isFruitAndVeg, item);
            }
        });
    }

    private void changeItemCount(Item item, double countValue) {
        boolean found = false;
        for (Item cartItem : GlobalResources.cart.items) {
            if (cartItem.getId().equals(item.getId())) {
                cartItem.setCount(cartItem.getCount() + countValue);
                found = true;
                break;
            }
        }
        if (!found && countValue > 0) {
            item.setCount(countValue);
            GlobalResources.cart.items.add(item);
        }
        notifyDataSetChanged();
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

    public void changeCount() {
        for (int i = 0; i < allItems.size(); i++) {
            if (allItems.get(i).getCount() != 0) {
                allItems.get(i).setCount(0);
            }
        }
    }
}
