package com.example.david.usafiapp;

public class Salesview {
    private String name, createdon;

    public Salesview() {
    }

    public Salesview(String name, String createdon) {
        this.name = name;
        this.createdon = createdon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDate() {
        return createdon;
    }

    public void setDate(String createdon) {
        this.createdon = createdon;
    }

}
