package com.talshavit.groupbuyproject.Helpers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.talshavit.groupbuyproject.General.Constants;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.Interfaces.OnItemChangeListener;
import com.talshavit.groupbuyproject.Helpers.Interfaces.OnReplaceButton;
import com.talshavit.groupbuyproject.Models.Category;
import com.talshavit.groupbuyproject.Models.Item;
import com.talshavit.groupbuyproject.R;

import java.util.ArrayList;
import java.util.Collections;

public class ItemsAdapterView extends RecyclerView.Adapter<MyViewHolderItems> {
    private ArrayList<Item> allItems, allItemsFull, filteredItems;
    private Context context;
    private String type;
    private LinearLayout lastVisibleControls = null, linearLayout;
    private ImageView imageView;
    private TextView textView, red_text, count_text, dont_show_text;
    private AppCompatImageButton minus, plus, exit_button;
    private AppCompatTextView countInDialog;
    private GridLayout.LayoutParams params;
    private GridLayout gridLayout;
    private MaterialButton confirm_button;
    private int category;
    private Item itemToRemove, currentItem;
    private TextInputEditText inputComments;
    private MaterialTextView remainingCharacters;
    private AlertDialog.Builder builder;
    private View dialogView;
    private OnItemChangeListener itemChangeListener;
    private OnReplaceButton onReplaceButton;
    private Double totalPricePerOrder;

    public ItemsAdapterView() {
    }

    public ItemsAdapterView(Context context, ArrayList<Item> allItems, String type, int category) {
        this.context = context;
        this.allItems = allItems;
        this.allItemsFull = new ArrayList<>(allItems);
        this.filteredItems = new ArrayList<>(allItems);
        this.type = type;
        this.category = category;
    }

    public ItemsAdapterView(Context context, ArrayList<Item> allItems, String type, int category, OnItemChangeListener itemChangeListener, OnReplaceButton onReplaceButton) {
        this.context = context;
        this.allItems = allItems;
        this.allItemsFull = new ArrayList<>(allItems);
        this.filteredItems = new ArrayList<>(allItems);
        this.type = type;
        this.category = category;
        this.itemChangeListener = itemChangeListener;
        this.onReplaceButton = onReplaceButton;
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
        holder.weight.setText(allItems.get(position).getWeight());
        holder.price.setText("₪ " + allItems.get(position).getPrice());
        if (Double.parseDouble(allItems.get(position).getSale()) > 0.0) {
            holder.sale.setText("₪ " + allItems.get(position).getSale());
            holder.sale.setVisibility(View.VISIBLE);
            holder.price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (allItems.get(position).getCategory().equals(Category.FruitsAndVegetables))
            holder.count.setText(String.valueOf(allItems.get(position).getCount()));
        else
            holder.count.setText(String.valueOf((int) allItems.get(position).getCount()));
        String image = allItems.get(position).getImg();
        Glide.with(context).load(image).into(holder.img);
        double sum = Double.parseDouble(holder.count.getText().toString());
        if (sum != 0.0) {
            holder.addItemButton.setText("עדכון");
        }
        onMinusButton(holder.minus, holder.count, allItems.get(position).getCategory().equals(Category.FruitsAndVegetables));
        onPlusButton(holder.plus, holder.count, allItems.get(position).getCategory().equals(Category.FruitsAndVegetables));
        onAddItemButton(holder, position);
        onXbutton(holder, position);
        checkCategory(holder, position);
        onCommentButton(holder, position);
        totalPriceCart(holder, position);
    }

    private void totalPriceCart(MyViewHolderItems holder, int position) {
        if (type.equals("CartFragment")) {
            setTotalPrice(holder, position);
            holder.totalPrice.setVisibility(View.VISIBLE);
        }
    }

    private void onCommentButton(MyViewHolderItems holder, int position) {
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initViewDialog();
                dialogFindViews(dialogView);

                if (type.equals("AllItemsFragment")) {
                    currentItem = GlobalResources.allItemsByCategories[category].get(position);
                } else {
                    currentItem = GlobalResources.cart.items.get(position);
                }

                setComment();
                commentTextWatcher();
                setBuilder();
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    private void setBuilder() {
        builder.setTitle("")
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    String comments = inputComments.getText().toString();
                    currentItem.setComment(comments);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> dialog.cancel());
    }

    private void commentTextWatcher() {
        inputComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remaining = 200 - s.length();
                remainingCharacters.setText("נותרו לך " + remaining + " תווים");
            }

            @Override
            public void afterTextChanged(Editable s) {
                int remaining = Constants.MAX_CHARACTER_COMMENT - s.length();
                currentItem.setRemainingCharacters(remaining);
            }
        });
    }

    private void setComment() {
        inputComments.setText(currentItem.getComment());
        remainingCharacters.setText("נותרו לך " + currentItem.getRemainingCharacters() + " תווים");
    }

    private void initViewDialog() {
        builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.dialog_add_comment, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
    }

    private void dialogFindViews(View dialogView) {
        inputComments = dialogView.findViewById(R.id.edit_text_comments);
        remainingCharacters = dialogView.findViewById(R.id.text_remaining_characters);
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
                    itemChangeListener.onItemQuantityChanged();
                }
            });
        } else
            holder.xButton.setVisibility(View.GONE);
    }

    private void onAddItemButton(MyViewHolderItems holder, int position) {
        if (type.equals("CartFragment")) {
            holder.addItemButton.setText(R.string.updateItem);
        }
        holder.addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allItems.get(position).getCategory().equals(Category.FruitsAndVegetables)) {
                    changeToDouble(holder, position);
                } else {
                    changeToInt(holder, position);
                }
                if (holder.addItemButton.getText().equals("עדכון")) {
                    if (holder.count.getText().equals("0") || holder.count.getText().equals("0.0")) {
                        if (!GlobalResources.cart.items.isEmpty()) {
                            if (GlobalResources.items.contains(GlobalResources.items.get(position))) {
                                if (allItems.get(position).getCategory().equals(Category.FruitsAndVegetables))
                                    GlobalResources.items.get(position).setCount(0.0);
                                else
                                    GlobalResources.items.get(position).setCount(0);
                                holder.addItemButton.setText(R.string.addItem);
                            }
                            if (type.equals("CartFragment")) {
                                itemToRemove = GlobalResources.cart.getItems().get(position);
                                removeFromCart(itemToRemove);
                            } else {
                                itemToRemove = GlobalResources.allItemsByCategories[category].get(position);
                                removeFromCart(itemToRemove);
                            }
                        }
                    }
                }
                if (type.equals("CartFragment")) {
                    if (position < allItems.size()) {
                        setTotalPrice(holder, position);
                    }
                    itemChangeListener.onItemQuantityChanged();
                }
                checkIfCloseToLimit();
            }
        });
    }

    private void setTotalPrice(MyViewHolderItems holder, int position) {
        Item item = allItems.get(position);
        if (Double.parseDouble(item.getSale()) > 0.0)
            totalPricePerOrder = Double.parseDouble(item.getSale()) * item.getCount();
        else
            totalPricePerOrder = Double.parseDouble(item.getPrice()) * item.getCount();
        if (totalPricePerOrder > 0.0) {
            String formattedValue = String.format("%.2f", totalPricePerOrder);
            holder.totalPrice.setText("סהכ לתשלום " + formattedValue + " ₪");
        }
    }


    private void checkIfCloseToLimit() {
        if (GlobalResources.limitAmount > 0) {
            GlobalResources.orderPrice = 0;
            double temporaryAmount = GlobalResources.limitAmount;
            for (int i = 0; i < GlobalResources.cart.items.size(); i++) {
                Double price;
                Item item = GlobalResources.cart.items.get(i);
                if (Double.parseDouble(item.getSale()) > 0.0) {
                    price = Double.parseDouble(item.getSale());
                } else {
                    price = Double.parseDouble(item.getPrice());
                }
                GlobalResources.orderPrice += price * GlobalResources.cart.items.get(i).getCount();
            }
            if ((temporaryAmount - GlobalResources.orderPrice) <= GlobalResources.limitPercent && (temporaryAmount - GlobalResources.orderPrice) > 0) {
                openDialog(temporaryAmount - GlobalResources.orderPrice);
            }
            if ((temporaryAmount - GlobalResources.orderPrice) <= 0 && GlobalResources.countForShowingDialog == 0) {
                openDialog(0.0);
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
            String formattedCount = String.format("%.2f", count);
            count_text.setText("נותר לך עוד " + formattedCount + "₪ ");
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

    public void removeFromCart(Item itemToRemove) {
        if (GlobalResources.cart.getItems().contains(itemToRemove)) {
            GlobalResources.cart.getItems().remove(itemToRemove);
        }

        for (Item item : GlobalResources.items) {
            if (item.getId().equals(itemToRemove.getId())) {
                if (item.getCategory().equals(Category.FruitsAndVegetables)) {
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
            holder.addItemButton.setText(R.string.updateItem);
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
            holder.addItemButton.setText(R.string.updateItem);
            dialogCompletionOrder(allItems.get(position));
            animateToCart(holder.img);
        }
    }

    private void dialogCompletionOrder(Item currentItem) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pop_up_completion_order);

        findViewsCompletion(dialog);

        ArrayList<Item> relatedItems = getRandomRelatedItems(currentItem.getRelatedItems());
        addItemsToGridLayout(gridLayout, relatedItems);

        onConfirmButton(relatedItems, dialog);
        onExitButton(dialog);

        dialog.setCancelable(false);
        showDialog(dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    private void showDialog(Dialog dialog) {
        if (GlobalResources.isSwitchForCompleteOrder) {
            GlobalResources.countForShowingDialogCompletion += 1;
        }
        if (GlobalResources.countForShowingDialogCompletion == 5) {
            dialog.show();
            GlobalResources.countForShowingDialogCompletion = 0;
        }
        if (!GlobalResources.isSwitchForCompleteOrder) {
            GlobalResources.countForShowingDialogCompletion = 0;
        }
    }

    private void onExitButton(Dialog dialog) {
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void onConfirmButton(ArrayList<Item> relatedItems, Dialog dialog) {
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Item item : relatedItems) {
                    changeItemCount(item, item.getCount());
                }
                dialog.dismiss();
                if (type.equals("CartFragment"))
                    itemChangeListener.onItemQuantityChanged();
            }
        });
    }

    private void findViewsCompletion(Dialog dialog) {
        gridLayout = dialog.findViewById(R.id.gridLayout);
        confirm_button = dialog.findViewById(R.id.confirm_button);
        exit_button = dialog.findViewById(R.id.exit_button);
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

            setCountText(item);
            initGridAdapter(itemView, gridLayout, i);

            onImageClick(imageView, linearLayout, countInDialog);
            onMinusClick2(countInDialog, item.getCategory().equals(Category.FruitsAndVegetables), item);
            onPlusClick2(countInDialog, item.getCategory().equals(Category.FruitsAndVegetables), item);
        }
    }

    private void setCountText(Item item) {
        Boolean isBool = item.getCategory().equals(Category.FruitsAndVegetables);
        if (isBool) {
            countInDialog.setText(String.valueOf(item.getCount()));
        } else {
            int integer = (int) item.getCount();
            countInDialog.setText(String.valueOf(integer));
        }
    }

    private void findViewsGridAdapter(View itemView) {
        imageView = itemView.findViewById(R.id.item_image);
        textView = itemView.findViewById(R.id.item_name);
        linearLayout = itemView.findViewById(R.id.add_item_controls);
        minus = itemView.findViewById(R.id.minus);
        plus = itemView.findViewById(R.id.plus);
        countInDialog = itemView.findViewById(R.id.count);
    }

    private void initGridAdapter(View itemView, GridLayout gridLayout, int i) {
        params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(i % 3, 1f);
        params.rowSpec = GridLayout.spec(i / 3);

        gridLayout.addView(itemView, params);
    }

    private void onImageClick(ImageView imageView, LinearLayout linearLayout, AppCompatTextView
            count) {
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

    private void onMinusClick(AppCompatTextView countTextView, boolean isFruitAndVeg) {
        double currentCount = Double.parseDouble(countTextView.getText().toString());
        if (isFruitAndVeg) {
            if (currentCount != 0.0) {
                currentCount -= 0.5;
            }
            countTextView.setText(String.valueOf(currentCount));
        } else {
            if (currentCount != 0.0) {
                currentCount -= 1.0;
                int res = (int) currentCount;
                countTextView.setText(String.valueOf(res));
            } else {
                countTextView.setText("0");
            }
        }
    }

    private void onMinusClick2(AppCompatTextView countTextView, boolean isFruitAndVeg, Item item) {
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double currentCount = Double.parseDouble(countTextView.getText().toString());
                if (isFruitAndVeg) {
                    if (currentCount != 0.0) {
                        currentCount -= 0.5;
                    }
                    countTextView.setText(String.valueOf(currentCount));
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
        });
    }

    private void onMinusButton(AppCompatImageButton minusButton, AppCompatTextView
            countTextView, boolean isFruitAndVeg) {
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMinusClick(countTextView, isFruitAndVeg);
            }
        });
    }

    private void onPlusClick(AppCompatTextView countTextView, boolean isFruitAndVeg) {
        double currentCount = Double.parseDouble(countTextView.getText().toString());
        if (isFruitAndVeg) {
            currentCount += 0.5;
            countTextView.setText(String.valueOf(currentCount));
        } else {
            currentCount += 1.0;
            int res = (int) currentCount;
            countTextView.setText(String.valueOf(res));
        }
    }

    private void onPlusClick2(AppCompatTextView countTextView, boolean isFruitAndVeg, Item item) {
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double currentCount = Double.parseDouble(countTextView.getText().toString());
                Boolean isBool = item.getCategory().equals(Category.FruitsAndVegetables);
                if (isBool) {
                    currentCount += 0.5;
                    countTextView.setText(String.valueOf(currentCount));
                } else {
                    currentCount += 1.0;
                    int res = (int) currentCount;
                    countTextView.setText(String.valueOf(res));
                }
                item.setCount(currentCount);
            }
        });
    }

    private void onPlusButton(AppCompatImageButton plusButton, AppCompatTextView countTextView,
                              boolean isFruitAndVeg) {
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlusClick(countTextView, isFruitAndVeg);
            }
        });
    }

    private void changeItemCount(Item item, double countValue) {
        boolean found = false;
        for (Item cartItem : GlobalResources.cart.items) {
            if (cartItem.getId().equals(item.getId())) {
                cartItem.setCount(countValue);
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

    public void addToCart(Item item, double count) {
        boolean found = false;
        for (Item cartItem : GlobalResources.cart.items) {
            if (cartItem.getId().equals(item.getId())) {
                cartItem.setCount(cartItem.getCount() + count);
                found = true;
                break;
            }
        }
        if (!found) {
            item.setCount(count);
            GlobalResources.cart.items.add(item);
        }
        onReplaceButton.onButtonChangeVisibility();
        notifyDataSetChanged();
    }

    public void filterByCategory(Category category, TextView noItemsTxt) {
        boolean hasSale = false;
        if (category == null) {
            hasSale = true;
            filteredItems = new ArrayList<>(allItemsFull);
        } else {
            filteredItems.clear();
            for (Item item : allItemsFull) {
                if (item.getCategory().equals(category)) {
                    if (Double.parseDouble(item.getSale()) > 0.0) {
                        hasSale = true;
                        filteredItems.add(item);
                    }
                }
            }
        }
        allItems.clear();
        allItems.addAll(filteredItems);
        if (!hasSale)
            noItemsTxt.setVisibility(View.VISIBLE);
        else {
            noItemsTxt.setVisibility(View.GONE);
        }

        notifyDataSetChanged();
    }
}
