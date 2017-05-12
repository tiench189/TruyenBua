package com.td.truyenbuavd.model;

import android.graphics.Bitmap;

public class TDStory {
    
    public int id;
    public String name;
    public int categoryID;
    public String imgUrl;
    public Bitmap imgBm;
    
    public TDStory() {
        
    }
    
    public TDStory(int id, String name, int categoryID, String imgUrl) {
        super();
        this.id = id;
        this.name = name;
        this.categoryID = categoryID;
        this.imgUrl = imgUrl;
        this.imgBm = null;
    }

    public TDStory(int id, String name, int categoryID, String imgUrl,
            Bitmap imgBm) {
        super();
        this.id = id;
        this.name = name;
        this.categoryID = categoryID;
        this.imgUrl = imgUrl;
        this.imgBm = imgBm;
    }
    
    

}
