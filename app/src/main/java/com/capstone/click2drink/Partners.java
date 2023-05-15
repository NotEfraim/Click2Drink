package com.capstone.click2drink;

public class Partners {

    private String shop_id;
    private String image_url;
    private String brgy;
    private String contact_number;
    private String fb_page;
    private String status;
    private String municipality;
    private String PARTNERS_URL ="http://click2drinkph.store/uploads/shop_logo/";

    public Partners(String shop_id,String fb_page, String image_url, String brgy, String municipality,
                    String contact_number, String status){


        this.shop_id = shop_id;
        this.image_url = image_url;
        this.brgy = brgy;
        this.municipality = municipality;
        this.contact_number = contact_number;
        this.fb_page = fb_page;
        this.status = status;


    }

    public String getShop_id(){
        return shop_id;
    }
    public void setShop_id(){
        this.shop_id = shop_id;
    }
    public String getImage() {
        return PARTNERS_URL+image_url;
    }

    public void setImage_url(String image_url){
        this.image_url = image_url;
    }

    public String getAddress(){
        return brgy+" "+municipality;
    }

    public void setAddress(){
        this.brgy = brgy;
        this.municipality = municipality;
    }

    public String getContact_number(){
        return contact_number;
    }

    public void setContact_number(){
        this.contact_number = contact_number;
    }

    public String getFbPage(){
        return fb_page;
    }

    public void setFb_page(){
        this.fb_page = fb_page;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(){
        this.status = status;
    }



}

