package com.talshavit.groupbuyproject.Helpers;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.talshavit.groupbuyproject.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public AppCompatTextView date, count, time;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews(itemView);
    }

    private void findViews(View itemView) {
        count = itemView.findViewById(R.id.count);
        date = itemView.findViewById(R.id.date);
        time = itemView.findViewById(R.id.time);
    }
}
