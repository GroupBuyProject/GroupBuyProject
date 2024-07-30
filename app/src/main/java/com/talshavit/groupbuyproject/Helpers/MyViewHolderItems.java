package com.talshavit.groupbuyproject.Helpers;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.talshavit.groupbuyproject.R;

public class MyViewHolderItems extends RecyclerView.ViewHolder{

    AppCompatTextView itemName, weight, company, unit, count, price;
    ShapeableImageView img;
    AppCompatButton addItemButton;
    AppCompatImageButton minus, plus, comment, xButton;


    public MyViewHolderItems(@NonNull View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View itemView) {
        itemName = itemView.findViewById(R.id.itemName);
        weight = itemView.findViewById(R.id.weight);
        company = itemView.findViewById(R.id.company);
        unit = itemView.findViewById(R.id.unit);
        count = itemView.findViewById(R.id.count);
        price = itemView.findViewById(R.id.price);
        img = itemView.findViewById(R.id.img);
        addItemButton = itemView.findViewById(R.id.addItemButton);
        minus = itemView.findViewById(R.id.minus);
        plus = itemView.findViewById(R.id.plus);
        comment = itemView.findViewById(R.id.comment);
        xButton = itemView.findViewById(R.id.xButton);
    }
}
