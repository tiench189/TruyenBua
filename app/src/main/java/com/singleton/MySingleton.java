package com.singleton;

import java.util.ArrayList;
import java.util.List;

import com.td.truyenbuavd.model.TDCategory;
import com.td.truyenbuavd.model.TDStory;




public class MySingleton {

    private static MySingleton instance;
    
    public List<TDStory> listStory;
    
    public List<TDCategory> listCategory;
    
    public int currentCate = 1;
    
    public int firstID = 0;

    public static void initInstance() {
        if (instance == null) {
            // Create the instance
            instance = new MySingleton();
        }
    }

    public static MySingleton getInstance() {
        // Return the instance
        return instance;
    }

    private MySingleton() {
        // Constructor hidden because this is a singleton
    }

    public void customSingletonMethod() {
        listStory = new ArrayList<TDStory>();
        listCategory = new ArrayList<TDCategory>();
    }
}
