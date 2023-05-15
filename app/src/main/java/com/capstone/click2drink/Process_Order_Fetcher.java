package com.capstone.click2drink;


public class Process_Order_Fetcher{

    private String order_id;
    private String product_id;
    private String shop_id;
    private String product_name;
    private String product_price;
    private String product_quantity;
    private String image_url;

   public Process_Order_Fetcher(String order_id, String product_id,
                                String shop_id, String product_name, String
                                        product_price, String image_url, String product_quantity){

       this.order_id = order_id;
       this.product_id = product_id;
       this.shop_id = shop_id;
       this.product_name = product_name;
       this.product_price = product_price;
       this.image_url = image_url;
       this.product_quantity = product_quantity;

   }

   public String getOrder_id(){
       return order_id;
   }
   public void setOrder_id(){
       this.order_id = order_id;
   }

   public String getProduct_id(){
       return product_id;
   }
   public void setProduct_id(){
       this.product_id = product_id;
   }

   public String getShop_id(){
       return shop_id;
   }
   public void setShop_id(){
       this.shop_id = shop_id;
   }

   public String get_Product_name(){
       return product_name;

   }
   public void setProduct_name(){
       this.product_name = product_name;
   }

   public String get_Product_price(){
       return product_price;
   }
   public void setProduct_price(){
       this.product_price = product_price;
   }

   public String get_product_Image_url(){
       return image_url;
   }
   public void set_product_Image_url(){
       this.image_url = image_url;
   }

   public String get_Product_quantity(){
       return product_quantity;
   }

   public void setProduct_quantity(){
       this.product_quantity = product_quantity;
   }



}

