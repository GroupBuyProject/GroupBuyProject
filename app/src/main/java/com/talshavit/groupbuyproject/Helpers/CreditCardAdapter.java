package com.talshavit.groupbuyproject.Helpers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.talshavit.groupbuyproject.General.Constants;
import com.talshavit.groupbuyproject.Helpers.Interfaces.OnCardDeletedListener;
import com.talshavit.groupbuyproject.R;
import com.talshavit.groupbuyproject.Models.Payment;

import java.util.List;

public class CreditCardAdapter extends RecyclerView.Adapter<CreditCardAdapter.CreditCardViewHolder> {
    private List<Payment> cards;
    private int selectedPosition = -1;
    private OnCardClickListener onCardClickListener;

    private DatabaseReference userReference;
    private OnCardDeletedListener onCardDeletedListener;

    public CreditCardAdapter(List<Payment> cards, OnCardClickListener listener, int initialSelectedPosition, DatabaseReference userReference, OnCardDeletedListener onCardDeletedListener) {
        this.cards = cards;
        this.onCardClickListener = listener;
        this.selectedPosition = initialSelectedPosition;
        this.userReference = userReference;
        this.onCardDeletedListener = onCardDeletedListener;
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
        holder.cardNumber.setText("**** **** **** " + String.valueOf(card.getCardNumber()).substring(Constants.SUBSTRING_CARD));
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            onCardClickListener.onCardClick(card);
        });
        holder.checkIcon.setVisibility(selectedPosition == holder.getAdapterPosition() ? View.VISIBLE : View.GONE);
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCardFromFirebase(card);
                cards.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cards.size());
                if (onCardDeletedListener != null) {
                    onCardDeletedListener.onCardDeleted();
                }
            }
        });
    }

    private void updateSelectedCardAfterDeletion() {
        if (cards.size() > 0) {
            selectedPosition = Math.max(0, selectedPosition - 1);
            onCardClickListener.onCardClick(cards.get(selectedPosition));
        } else {
            selectedPosition = -1;
            onCardClickListener.onCardClick(null);
        }
    }

    private void deleteCardFromFirebase(Payment card) {
        userReference.child("PaymentsInfo").child(card.getUniqueId()).removeValue();
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class CreditCardViewHolder extends RecyclerView.ViewHolder {
        TextView cardNumber;
        ImageView cardIcon, checkIcon;
        ImageButton delete_button;

        public CreditCardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumber = itemView.findViewById(R.id.card_number);
            cardIcon = itemView.findViewById(R.id.card_icon);
            checkIcon = itemView.findViewById(R.id.check_icon);
            delete_button = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnCardClickListener {
        void onCardClick(Payment card);

    }
}

