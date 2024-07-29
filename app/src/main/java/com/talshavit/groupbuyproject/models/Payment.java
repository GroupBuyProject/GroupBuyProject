package com.talshavit.groupbuyproject.models;

public class Payment {
    private long cardNumber;
    private int idNumber;
    private int year;
    private int month;
    private int cvv;

    public Payment() {
    }
    public Payment(long cardNumber, int idNumber, int year, int month, int cvv) {
        this.cardNumber = cardNumber;
        this.idNumber = idNumber;
        this.year = year;
        this.month = month;
        this.cvv = cvv;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public Payment setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public Payment setIdNumber(int idNumber) {
        this.idNumber = idNumber;
        return this;
    }

    public int getYear() {
        return year;
    }

    public Payment setYear(int year) {
        this.year = year;
        return this;
    }

    public int getMonth() {
        return month;
    }

    public Payment setMonth(int month) {
        this.month = month;
        return this;
    }

    public int getCvv() {
        return cvv;
    }

    public Payment setCvv(int cvv) {
        this.cvv = cvv;
        return this;
    }
}
