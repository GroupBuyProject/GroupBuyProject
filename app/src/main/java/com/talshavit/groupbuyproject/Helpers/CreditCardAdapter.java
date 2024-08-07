package com.talshavit.groupbuyproject.Helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.Payment;

import java.util.List;

public class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder> {
    private List<Payment> cards;
    private int selectedPosition = -1;
    private OnCardClickListener onCardClickListener;

    public CreditCardAdapter(List<Payment> cards, OnCardClickListener listener,int initialSelectedPosition) {
        this.cards = cards;
        this.onCardClickListener = listener;
        this.selectedPosition = initialSelectedPosition;
    }

    @NonNull
    @Override
    public CreditCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit_card, parent, false);
        return new CreditCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditCardViewHolder holder, int position) {
        Payment card = cards.get(position);
        holder.cardNumber.setText("**** **** **** " + String.valueOf(card.getCardNumber()).substring(12));
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            onCardClickListener.onCardClick(card);
        });
        holder.checkIcon.setVisibility(selectedPosition == holder.getAdapterPosition() ? View.VISIBLE : View.GONE);
    }


    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class CreditCardViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumber;
        ImageView cardIcon, checkIcon;

        public CreditCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.card_number);
            cardIcon = itemView.findViewById(R.id.card_icon);
            checkIcon = itemView.findViewById(R.id.check_icon);
        }
    }

    public interface OnCardClickListener {
        void onCardClick(Payment card);
    }
}

