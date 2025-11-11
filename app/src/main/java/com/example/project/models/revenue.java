package com.example.project.models;

import java.util.Date;
import java.util.List;

public class revenue {
    private int id;
    private String username;
    private Double totalprice;
    private Date orderdate;

    public revenue(String username, Double totalprice, Date orderdate) {
        this.username = username;
        this.totalprice = totalprice;
        this.orderdate = orderdate;
    }


    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public Double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(Double totalprice) {
        this.totalprice = totalprice;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
