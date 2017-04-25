package com.example.zipper.safetaxi;

import java.util.List;

/**
 * Created by ZIPPER on 4/15/2017.
 */

public class StatusFriend {
    public  String status;
    public String name;
    public List<String> set,set1;
    public StatusFriend(String name, String status)
    {
        this.status=status;
        this.name=name;
    }


    public StatusFriend(List<String> set, List<String> set1) {

        this.set1=set1;
        this.set=set;
    }
}


