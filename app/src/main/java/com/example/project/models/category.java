package com.example.project.models;

import java.util.List;

public class category {

    private int id;
    private String name;
    private List<item> itemlist;

    public category(String name, List<item> itemlist) {
        this.name = name;
        this.itemlist = itemlist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<item> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<item> itemlist) {
        this.itemlist = itemlist;
    }
}
