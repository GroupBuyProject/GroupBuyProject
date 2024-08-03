package com.talshavit.groupbuyproject.Helpers.History;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.talshavit.groupbuyproject.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView date, count, time;
    public MaterialCardView cardView;

    public FrameLayout layout;
    public AppCompatImageButton addToCartButton;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View itemView) {
        layout = itemView.findViewById(R.id.layout);
        count = itemView.findViewById(R.id.count);
        date = itemView.findViewById(R.id.date);
        time = itemView.findViewById(R.id.time);
        addToCartButton = itemView.findViewById(R.id.addToCartButton);
        cardView = itemView.findViewById(R.id.cardView);
    }
}
