package com.talshavit.groupbuyproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;

import com.airbnb.lottie.LottieAnimationView;
import com.talshavit.groupbuyproject.General.GlobalResources;

public class LoadFromMongoDB extends AppCompatActivity {
    private LottieAnimationView lottieAnimationView;
    private boolean dataLoaded = false;
    private boolean animationFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_from_mongo_db);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        LottieFunc();
        loadMongoData();
    }

    private void LottieFunc() {
        lottieAnimationView.playAnimation();

        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationFinished = true;
                checkIfFinished();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                animationFinished = true;
                checkIfFinished();
            }
        });
    }

    private void loadMongoData() {
        GlobalResources.getAllMarkets(new GlobalResources.ApiServiceCallback() {
            @Override
            public void onSuccess() {
                dataLoaded = true;
                checkIfFinished();
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private void checkIfFinished() {
        if (dataLoaded && animationFinished) {
            navigateToMainActivity();
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoadFromMongoDB.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}