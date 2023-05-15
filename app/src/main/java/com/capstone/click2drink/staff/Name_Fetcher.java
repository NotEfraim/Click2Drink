package com.capstone.click2drink.staff;

public class Name_Fetcher {
     private String cuser_name;

     public Name_Fetcher(String cuser_name){
         this.cuser_name = cuser_name;
     }

     public String getCuser_name(){
         return cuser_name;
     }
     public void setCuser_name(){
         this.cuser_name = cuser_name;
     }
}
