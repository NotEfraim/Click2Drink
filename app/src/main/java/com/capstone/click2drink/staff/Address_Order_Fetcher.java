package com.capstone.click2drink.staff;

public class Address_Order_Fetcher {
    //Address
    private String cuser_number;
    private String cuser_address_title;
    private String combine_address;
    private String address_id;



    public Address_Order_Fetcher(String address_id, String cuser_number,
                                 String cuser_address_title, String combine_address){

        //address
        this.cuser_number = cuser_number;
        this.cuser_address_title = cuser_address_title;
        this.combine_address = combine_address;
        this.address_id = address_id;



    }

    //Address
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
    public String getCombine_address(){
        return combine_address;
    }
    public void setCombine_address(){
        this.combine_address = combine_address;
    }




}

