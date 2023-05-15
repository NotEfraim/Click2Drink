package com.capstone.click2drink;

public class Product_Dispenser {

    private String product_name;
    private String product_price;
    private String image_url;
    private String location;
    private String product_id;
    private String shop_id;
    private String URL ="http://click2drinkph.store/uploads/productimages/";

    // Pwede mo to gamitin sa lahat ng product
    // same lang kase ban ban
    public Product_Dispenser(String product_id,String product_name, String product_price, String image_url,
                             String location,String shop_id){

        this.product_name = product_name;
        this.product_price = product_price;
        this.image_url = image_url;
        this.location = location;
        this.product_id = product_id;
        this.shop_id = shop_id;


    }

    public String getProduct_id(){
        return product_id;
    }
    public void setProduct_id(){
        this.product_id=product_id;
    }

    public String getShop_id(){
        return shop_id;

    }
    public void setShop_id(){
        this.shop_id = shop_id;
    }

    public String getProduct_name(){
        return product_name;
    }
    public void setProduct_name(){
        this.product_name = product_name;
    }

    public String getProduct_price(){
        return product_price;
    }
    public void setProduct_price() {
        this.product_price = product_price;
    }

    public String getImage_url(){
        return URL+image_url;
    }
    public void setImage_url(){
        this.image_url = image_url;
    }

    public String getLocation(){
        return location;
    }
    public void setLocation(){
        this.location = location; }





}
