package com.td.truyenbuavd.model;

public class TDCategory {
    
    public int id;
    public String name;
    public int currentId;
    
    public TDCategory() {
        this.currentId = 0;
    }

    public TDCategory(int id, String name) {
        super();
        this.id = id;
        this.name = name;
        this.currentId = 0;
    }

    public TDCategory(int id, String name, int currentId) {
        super();
        this.id = id;
        this.name = name;
        this.currentId = currentId;
    }
    
}
