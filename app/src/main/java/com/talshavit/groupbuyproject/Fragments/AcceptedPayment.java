package com.talshavit.groupbuyproject.Fragments;

import android.animation.Animator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.talshavit.groupbuyproject.General.GlobalResources;
import com.talshavit.groupbuyproject.R;

public class AcceptedPayment extends Fragment {

    private LottieAnimationView lottieAnimationView;

    public AcceptedPayment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accepted_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        initLottie();
    }

    private void initLottie() {
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                GlobalResources.replaceFragment(requireActivity().getSupportFragmentManager(), new HomeFragment());
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
    }


    private void findViews(View view) {
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
    }
}