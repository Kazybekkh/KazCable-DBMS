/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kazcabels.model;


public class Supply{
    private String name;
    private double price;
    private int quantity_supplied;
    private String supply_type;
    public Supply(String name, double price, int quantity, String supply_type){
        this.name = name;
        this.price = price;
        this.quantity_supplied = quantity;
        this.supply_type = supply_type;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}

    public int getQuantity_supplied() {return quantity_supplied;}

    public void setQuantity_supplied(int quantity_supplied) {this.quantity_supplied = quantity_supplied;}

    public String getSupply_type() {
        return supply_type;
    }

    public void setSupply_type(String supply_type) {
        this.supply_type = supply_type;
    }
    public String[] getRow(){
        return new String[]{this.getName(), "0", this.getPrice()+"", this.getQuantity_supplied()+"", "",this.getSupply_type()};
    }
    
    
}
