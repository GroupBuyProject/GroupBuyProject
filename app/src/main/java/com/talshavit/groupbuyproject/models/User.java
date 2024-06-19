package com.talshavit.groupbuyproject.models;

import android.util.Log;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<History> histories;

    public User() {
        this.histories = new ArrayList<>();
    }

    public User(String name, ArrayList<History> histories) {
        this.name = name;
        this.histories = histories;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<History> getHistories() {
        return histories;
    }

    public User setHistories(ArrayList<History> histories) {
        this.histories = histories;
        return this;
    }

    public void addHistory(History history){
        histories.add(history);
    }
}
