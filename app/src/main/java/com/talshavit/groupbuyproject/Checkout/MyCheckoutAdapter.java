package com.talshavit.groupbuyproject.Checkout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.talshavit.groupbuyproject.ExistPaymentDetailsFragment;
import com.talshavit.groupbuyproject.Fragments.CheckoutFragment;
import com.talshavit.groupbuyproject.NewPaymentDetailsFragment;

public class MyCheckoutAdapter extends FragmentStatePagerAdapter {
    private final int numOfTabes;
    private double price;

    public MyCheckoutAdapter(@NonNull FragmentManager fm, int numOfTabes, double price) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabes = numOfTabes;
        this.price = price;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewPaymentDetailsFragment(price);
            case 1:
                return new ExistPaymentDetailsFragment(price);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabes;
    }
}

