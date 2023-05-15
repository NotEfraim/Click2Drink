package com.capstone.click2drink;

public class Invoice_Fetcher {

    private String id;
    private String order_number;
    private String price;
    private String quantity;
    private String order_status;
    private String date_ordered;

    public Invoice_Fetcher(String id, String order_number, String price,
                           String quantity, String order_status, String date_ordered){

        this.id = id;
        this.order_number = order_number;
        this.price = price;
        this.quantity = quantity;
        this.order_status = order_status;
        this.date_ordered = date_ordered;

    }

    public String getOrderId(){
        return id;
    }
    public void setOrderId(){
        this.id = id;
    }

    public String getOrder_number(){
        return order_number;
    }
    public void setOrder_number(){
        this.order_number = order_number;
    }

    public String getPrice(){
        return price;
    }
    public void setPrice(){
        this.price = price;
    }

    public String getQuantity(){
        return quantity;
    }
    public void setQuantity(){
        this.quantity = quantity;
    }

    public String getOrder_status(){
        return order_status;
    }
    public void setOrder_status(){
        this.order_status = order_status;
    }

    public String getDate_ordered(){
        return date_ordered;
    }
    public void setDate_ordered(){
        this.date_ordered = date_ordered;
    }
}
