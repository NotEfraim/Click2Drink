package com.capstone.click2drink;

public class Truck_Class {

    private String truck_id;
    private String product_id;
    private String product_name;
    private String product_price;
    private String product_location;
    private String image_url;
    private String order_number;
    private String shop_id;

    public Truck_Class(String truck_id,String product_id,String product_name,String product_price,
                       String product_location, String image_url, String order_number, String shop_id){

        this.truck_id = truck_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_location = product_location;
        this.image_url = image_url;
        this.order_number = order_number;
        this.shop_id = shop_id;
    }

    public String getTruck_id(){
        return truck_id;
    }
    public String getOrder_number(){
        return order_number;
    }
    public void setOrder_number(){
        this.order_number = order_number;
    }

    public void setTruck_id(){
        this.truck_id = truck_id;
    }
    public String getTruckProduct_id(){
        return product_id;
    }
    public void setTruckProduct_id(){
        this.product_id = product_id;
    }
    public String getTruckProduct_name(){
        return product_name;
    }
    public void setTruckProduct_name(){
        this.product_name = product_name;
    }
    public String getTruckProduct_price(){
        return product_price;
    }
    public void setTruckProduct_price(){
        this.product_price = product_price;
    }
    public String getTruckProduct_location(){
        return product_location;
    }
    public void setTruckProduct_location(){
        this.product_location = product_location;
    }
    public String getTruckImage_url(){
        return image_url;
    }
    public void setTruckImage_url(){
        this.image_url = image_url;
    }

    public String getShop_id(){
        return shop_id;
    }
    public void setShop_id(){
        this.shop_id = shop_id;
    }
}
