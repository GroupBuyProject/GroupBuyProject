package com.talshavit.groupbuyproject.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.Helpers.Interfaces.OnReplaceButton;
import com.talshavit.groupbuyproject.Helpers.ItemsAdapterView;
import com.talshavit.groupbuyproject.Helpers.Interfaces.OnItemChangeListener;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.Cart;
import com.talshavit.groupbuyproject.Models.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class CartFragment extends Fragment implements OnItemChangeListener, OnReplaceButton {

    private Cart cart;
    private ItemsAdapterView itemsAdapterView;
    private RecyclerView recyclerViewItem;
    private AppCompatButton checkout, replace;
    private AppCompatImageButton exit_button;
    private TextView totalAmount, textView;
    private double totalPrice;
    private AppCompatTextView text;
    private ImageView imageView, check;
    private GridLayout gridLayout;
    private GridLayout.LayoutParams params;
    private MaterialButton confirm_button;
    private ArrayList<Item> itemsWithSimilar;
    private ArrayList<HashMap<Item, Item>> similarItemsToShow;
    private ArrayList<HashMap<Item, Item>> itemsToReplace;
    private int counterFotItems;

    public CartFragment() {
        this.cart = GlobalResources.cart;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initView();
    }

    private void initView() {
        itemsToReplace = new ArrayList<>();
        initAdapter(recyclerViewItem, itemsAdapterView);
        onCheckOut();
        updateTotalPrice();
        checkLimit();
    }

    private void checkLimit() {
        itemsWithSimilar = new ArrayList<>();
        checkItemsWithSimilar();
        if (GlobalResources.limitAmount > 0 && itemsWithSimilar.size() > 0) {
            replace.setVisibility(View.VISIBLE);
            replace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogReplaceOrder();
                }
            });
        }
    }

    private void onCheckOut() {
        checkout.setOnClickListener(view -> {
            if (!cart.getItems().isEmpty()) {
                GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new MapFragment(totalPrice));
            } else
                Toast.makeText(getContext(), "אין פירטים בעגלה", Toast.LENGTH_SHORT).show();
        });
    }

    private void dialogReplaceOrder() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_pop_up_completion_order);
        itemsWithSimilar = new ArrayList<>();

        findViewsCompletion(dialog);

        checkItemsWithSimilar();
        checkMaxDifferencePrice();

        addItemsToGridLayout(gridLayout, similarItemsToShow);

        onConfirmButton(dialog);
        onExitButton(dialog);

        dialog.setCancelable(false);
        dialog.show();

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
    }

    private void onExitButton(Dialog dialog) {
        exit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void onConfirmButton(Dialog dialog) {

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < itemsToReplace.size(); i++) {
                    Item mainItem = itemsToReplace.get(i).entrySet().iterator().next().getKey();
                    Item similarItem = itemsToReplace.get(i).entrySet().iterator().next().getValue();
                    itemsAdapterView.addToCart(similarItem, mainItem.getCount());
                    itemsAdapterView.removeFromCart(mainItem);
                    updateTotalPrice();
                }
                dialog.dismiss();
                updateReplaceButtonVisibility();
            }
        });
    }

    private void checkMaxDifferencePrice() {
        similarItemsToShow = new ArrayList<>();
        counterFotItems = 0;
        for (int i = 0; i < itemsWithSimilar.size(); i++) {
            Double maxDifference = 0.0;
            Item maxItem = null;
            Item mainItem = itemsWithSimilar.get(i);
            Double mainPrice;
            if (Double.parseDouble(mainItem.getSale()) > 0.0)
                mainPrice = Double.parseDouble(mainItem.getSale());
            else
                mainPrice = Double.parseDouble(mainItem.getPrice());
            for (int j = 0; j < itemsWithSimilar.get(i).getSimilarItems().size() - 1; j++) {
                int ID = itemsWithSimilar.get(i).getSimilarItems().get(j);
                for (Item item : GlobalResources.items) {
                    if (item.getId().equals(String.valueOf(ID))) {
                        Double similarItemPrice;
                        if (Double.parseDouble(item.getSale()) > 0.0)
                            similarItemPrice = Double.parseDouble(item.getSale());
                        else
                            similarItemPrice = Double.parseDouble(item.getPrice());
                        if (mainPrice - similarItemPrice > maxDifference) {
                            maxDifference = mainPrice - similarItemPrice;
                            maxItem = item;
                        }
                    }
                }
            }
            if (maxItem != null) {
                HashMap<Item, Item> itemHashMap = new HashMap<>();
                itemHashMap.put(mainItem, maxItem);
                similarItemsToShow.add(itemHashMap);
                counterFotItems++;
            }
        }
    }

    private void checkItemsWithSimilar() {
        itemsWithSimilar = new ArrayList<>();
        for (int i = 0; i < cart.items.size(); i++) {
            if (cart.items.get(i).getSimilarItems() != null && cart.items.get(i).getSimilarItems().size() > 0) {
                itemsWithSimilar.add(cart.items.get(i));
            }
        }
    }

    private void addItemsToGridLayout(GridLayout gridLayout, ArrayList<HashMap<Item, Item>> similarItems) {
        for (int i = 0; i < similarItems.size(); i++) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_replace, null);

            findViewsGridAdapter(itemView);

            Item mainItem = similarItems.get(i).entrySet().iterator().next().getKey();
            Item similarItem = similarItems.get(i).entrySet().iterator().next().getValue();
            double mainPrice, similarPrice, price;
            if (Double.parseDouble(mainItem.getSale()) > 0.0)
                mainPrice = Double.parseDouble(mainItem.getSale()) * mainItem.getCount();
            else
                mainPrice = Double.parseDouble(mainItem.getPrice()) * mainItem.getCount();
            if (Double.parseDouble(similarItem.getSale()) > 0.0)
                similarPrice = Double.parseDouble(similarItem.getSale()) * mainItem.getCount();
            else
                similarPrice = Double.parseDouble(similarItem.getPrice()) * mainItem.getCount();
            price = mainPrice - similarPrice;
            String formattedValue = String.format("%.2f", price);
            textView.setText("החלף " + mainItem.getName() + "\n" + "ב" + similarItem.getName() + " ותחסוך " + formattedValue + " ₪");
            Glide.with(getContext()).load(similarItem.getImg()).into(imageView);

            initGridAdapter(itemView, gridLayout, i);

            onImageClick(mainItem, similarItem, itemView);
        }
    }

    private void onImageClick(Item mainItem, Item similarItem, View itemView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            boolean isChecked = false;

            @Override
            public void onClick(View view) {
                check = itemView.findViewById(R.id.check);
                isChecked = !isChecked;
                if (!isChecked) {
                    check.setVisibility(View.INVISIBLE);
                    Iterator<HashMap<Item, Item>> iterator = itemsToReplace.iterator();
                    while (iterator.hasNext()) {
                        HashMap<Item, Item> map = iterator.next();
                        if (map.containsKey(mainItem)) {
                            iterator.remove();
                            break;
                        }
                    }
                    counterFotItems++;
                } else {
                    check.setVisibility(View.VISIBLE);
                    HashMap<Item, Item> itemHashMap = new HashMap<>();
                    itemHashMap.put(mainItem, similarItem);
                    itemsToReplace.add(itemHashMap);
                    counterFotItems--;
                }
            }
        });
    }

    private void updateReplaceButtonVisibility() {
        if (counterFotItems == 0) {
            replace.setVisibility(View.GONE);
        } else {
            replace.setVisibility(View.VISIBLE);
        }
    }

    private void findViewsGridAdapter(View itemView) {
        imageView = itemView.findViewById(R.id.item_image);
        textView = itemView.findViewById(R.id.item_name);
    }

    private void initGridAdapter(View itemView, GridLayout gridLayout, int i) {
        params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(i % 3, 1f);
        params.rowSpec = GridLayout.spec(i / 3);

        gridLayout.addView(itemView, params);
    }

//    private void addItemsToGridLayout(GridLayout gridLayout, ArrayList<Item> relatedItems) {
//        for (int i = 0; i < relatedItems.size(); i++) {
//            View itemView = LayoutInflater.from(context).inflate(R.layout.grid_item_completion, null);
//
//            findViewsGridAdapter(itemView);
//
//            Item item = relatedItems.get(i);
//            textView.setText(item.getName());
//            Glide.with(context).load(item.getImg()).into(imageView);
//
//            setCountText(item);
//            initGridAdapter(itemView, gridLayout, i);
//
//            onImageClick(imageView, linearLayout, countInDialog);
//            onMinusClick2(countInDialog, item.getCategory().equals(Category.FruitsAndVegetables), item);
//            onPlusClick2(countInDialog, item.getCategory().equals(Category.FruitsAndVegetables), item);
//        }
//    }

//    private void findViewsGridAdapter(View itemView) {
//        imageView = itemView.findViewById(R.id.item_image);
//        textView = itemView.findViewById(R.id.item_name);
//        linearLayout = itemView.findViewById(R.id.add_item_controls);
//        minus = itemView.findViewById(R.id.minus);
//        plus = itemView.findViewById(R.id.plus);
//        countInDialog = itemView.findViewById(R.id.count);
//    }

    private void findViewsCompletion(Dialog dialog) {
        text = dialog.findViewById(R.id.text);
        gridLayout = dialog.findViewById(R.id.gridLayout);
        confirm_button = dialog.findViewById(R.id.confirm_button);
        exit_button = dialog.findViewById(R.id.exit_button);
        text.setText("האם תרצה להחליף למוצר זול יותר?");
    }

    private void findViews(View view) {
        recyclerViewItem = view.findViewById(R.id.recyclerView);
        itemsAdapterView = new ItemsAdapterView(getContext(), cart.items, "CartFragment", -1, this, this::onButtonChangeVisibility);
        checkout = view.findViewById(R.id.checkout);
        replace = view.findViewById(R.id.replace);
        totalAmount = view.findViewById(R.id.totalAmount);
    }

    private void initAdapter(RecyclerView recyclerView, RecyclerView.Adapter myAdapter) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void updateTotalPrice() {
        totalPrice = 0.0;
        for (Item item : cart.getItems()) {
            if (Double.parseDouble(item.getSale()) > 0.0)
                totalPrice += Double.parseDouble(item.getSale()) * item.getCount();
            else
                totalPrice += Double.parseDouble(item.getPrice()) * item.getCount();
        }
        String total = String.format("%.2f", totalPrice);
        totalAmount.setText("₪ " + total);
    }

    @Override
    public void onItemQuantityChanged() {
        updateTotalPrice();
    }

    @Override
    public void onButtonChangeVisibility() {
        checkItemsWithSimilar();
        updateReplaceButtonVisibility();
    }
}
