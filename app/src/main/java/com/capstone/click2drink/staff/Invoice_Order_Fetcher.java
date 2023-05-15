package com.capstone.click2drink.staff;

public class Invoice_Order_Fetcher {

    //Invoice
    private String order_number;
    private String total_price;
    private String total_gallons;
    private String order_status;
    private String date_ordered;
    private String address_id;

    public Invoice_Order_Fetcher(String order_number, String total_price,
                                 String total_gallons, String order_status, String date_ordered, String adress_id){

        //invoice

        this.order_number = order_number;
        this.total_price = total_price;
        this.total_gallons = total_gallons;
        this.order_status = order_status;
        this.date_ordered = date_ordered;
        this.address_id = adress_id;

    }

    // Invoice
    public String getOrder_number(){
        return order_number;
    }
    public void setOrder_number(){
        this.order_number = order_number;
    }

    public String getTotal_price(){
        return total_price;
    }
    public void setTotal_price(){
        this.total_price = total_price;
    }

    public String getTotal_gallons(){
        return total_gallons;
    }
    public void setTotal_gallons(){
        this.total_gallons = total_gallons;
    }

    public String getOrderStatus(){
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

    public String getAddress_id(){
        return address_id;
    }
    public void setAddress_id(){
        this.address_id = address_id;
    }

}
