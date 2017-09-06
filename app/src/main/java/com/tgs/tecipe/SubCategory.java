package com.tgs.tecipe;

import java.io.Serializable;

/**
 * Created by Rajesh on 8/26/2017.
 */

class SubCategory  implements Serializable {

    private String storieTitle="";

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    private String imageName="";

    public String getStorieTitle() {
        return storieTitle;
    }

    public void setStorieTitle(String storieTitle) {
        this.storieTitle = storieTitle;
    }

    public String getStorie() {
        return storie;
    }

    public void setStorie(String storie) {
        this.storie = storie;
    }

    public int getStorieID() {
        return storieID;
    }

    public void setStorieID(int storieID) {
        this.storieID = storieID;
    }

    private String storie="";
    private int storieID;

}

