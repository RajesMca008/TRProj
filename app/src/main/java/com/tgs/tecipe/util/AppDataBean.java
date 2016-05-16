package com.tgs.tecipe.util;

import java.util.ArrayList;

/**
 * Created by rrallabandi on 5/12/2016.
 */
public class AppDataBean {
    private static AppDataBean ourInstance = new AppDataBean();

    private ArrayList<Item> vegList=null;
    private ArrayList<Item> nonVegList=null;

    public ArrayList<Item> getNonVegList() {
        return nonVegList;
    }

    public void setNonVegList(ArrayList<Item> nonVegList) {
        this.nonVegList = nonVegList;
    }

    public ArrayList<Item> getVegList() {
        return vegList;
    }

    public void setVegList(ArrayList<Item> vegList) {
        this.vegList = vegList;
    }

    public static AppDataBean getInstance() {
        return ourInstance;
    }

    private AppDataBean() {
    }
}
