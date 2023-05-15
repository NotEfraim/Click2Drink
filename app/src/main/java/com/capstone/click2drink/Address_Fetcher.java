package com.capstone.click2drink;

public class Address_Fetcher {

    private String cuser_number;
    private String cuser_address_title;
    private String cuser_house_number;
    private String cuser_brgy;
    private String cuser_municipality;
    private String cuser_province;
    private String cuser_zipcode;
    private String address_id;

    public Address_Fetcher(String address_id, String cuser_number,String cuser_address_title,String cuser_house_number
            ,String cuser_brgy, String cuser_municipality,String cuser_province,String cuser_zipcode){

        this.cuser_number = cuser_number;
        this.cuser_address_title = cuser_address_title;
        this.cuser_house_number = cuser_house_number;
        this.cuser_brgy = cuser_brgy;
        this.cuser_municipality = cuser_municipality;
        this.cuser_province = cuser_province;
        this.cuser_zipcode = cuser_zipcode;
        this.address_id = address_id;

    }

    public String getAddress_id(){
        return address_id;
    }
    public void setAddress_id(){
        this.address_id = address_id;
    }
    public String getCuser_number(){
        return cuser_number;
    }
    public void setCuser_number(){
        this.cuser_number = cuser_number;
    }
    public String getCuser_address_title(){
        return cuser_address_title;
    }
    public void setCuser_address_title(){
        this.cuser_address_title = cuser_address_title;
    }
    public String getCuser_house_number(){
        return cuser_house_number;
    }
    public void setCuser_house_number(){
        this.cuser_house_number = cuser_house_number;
    }
    public String getCuser_brgy(){
        return cuser_brgy;
    }
    public void setCuser_brgy(){
        this.cuser_brgy = cuser_brgy;
    }
    public String getCuser_municipality(){
        return cuser_municipality;
    }
    public void setCuser_municipality(){
        this.cuser_municipality = cuser_municipality;
    }
    public String getCuser_province(){
        return cuser_province;
    }
    public void setCuser_province(){
        this.cuser_province = cuser_province;
    }
    public String getCuser_zipcode(){
        return cuser_zipcode;
    }
    public void setCuser_zipcode(){
        this.cuser_zipcode = cuser_zipcode;
    }

}
