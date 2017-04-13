package com.example.zipper.safetaxi;

import retrofit2.http.POST;

/**
 * Created by ZIPPER on 4/5/2017.
 */

public class Position {
    public  String latitude;
    public String longtitde;
    public Position(String lat, String lng)
    {
        this.latitude=lat;
        this.longtitde=lng;
    }
}
