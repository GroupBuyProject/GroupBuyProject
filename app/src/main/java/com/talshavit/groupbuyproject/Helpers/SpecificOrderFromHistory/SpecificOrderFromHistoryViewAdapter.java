package com.talshavit.groupbuyproject.Helpers.SpecificOrderFromHistory;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.talshavit.groupbuyproject.R;


public class SpecificOrderFromHistoryViewAdapter extends RecyclerView.ViewHolder {
    public AppCompatTextView itemName, itemCount, itemPrice;
    public ShapeableImageView itemImg;

    public SpecificOrderFromHistoryViewAdapter(@NonNull View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View itemView) {
        itemName = itemView.findViewById(R.id.itemName);
        itemCount = itemView.findViewById(R.id.itemCount);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        itemImg = itemView.findViewById(R.id.itemImg);
    }
}
