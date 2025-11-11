package com.example.project.models;

import java.util.Date;

public class historyitem {
    private int id;
    private int userid;
    private String name;
    private Double price;
    private int  quantity ;
    private String image ;
    private Date orderdate;

    public historyitem() {
    }

    public historyitem(int userid, String name, Double price, int quantity, String image, Date orderdate) {
        this.userid = userid;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.orderdate = orderdate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }
}
