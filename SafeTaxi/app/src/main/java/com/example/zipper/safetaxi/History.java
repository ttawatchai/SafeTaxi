package com.example.zipper.safetaxi;

import java.util.List;

/**
 * Created by ZIPPER on 4/13/2017.
 */

public class History {
    public  String Des;
    public String cost;
    public String form;
    public String location;
    public String Driver;
    public String taxicode;
    public String meter;
    public String rate;


    public History(String Des, String cost,String Driver)
    {
        this.Des=Des;
        this.cost = cost;
        this.form = form;
        this.Driver=Driver;


    }

    public History(String Des, String cost, String taxicode, String Driver) {
        this.Des=Des;
        this.cost = cost;
        this.taxicode = taxicode;
        this.Driver=Driver;
    }
}
