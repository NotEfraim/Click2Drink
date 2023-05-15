package com.capstone.click2drink;

public class PromosFetcher {

    private String image_url;
    private String promo_id;

    public PromosFetcher(String image_url, String promo_id){


        this.image_url = image_url;
        this.promo_id = promo_id;
    }


    public String getImage() {
        return image_url;
    }

    public void setImage_url(String image_url){
        this.image_url = image_url;
    }

    public String getPromo_id(){
        return promo_id;
    }
    public void setPromo_id(){
        this.promo_id = promo_id;
    }
}
