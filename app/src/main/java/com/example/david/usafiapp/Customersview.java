package com.example.david.usafiapp;

public class Customersview {
    private String name, createdon;

    public Customersview() {
    }

    public Customersview(String name, String createdon) {
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
