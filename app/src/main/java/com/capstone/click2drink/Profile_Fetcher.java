package com.capstone.click2drink;

public class Profile_Fetcher {



        private String cuser_name;
        private String cuser_email;
        private String cuser_phone_number;
        private String cuser_bday;
        private String cuser_current_address;

        public Profile_Fetcher(String cuser_name, String cuser_email,String cuser_phone_number,
                               String cuser_bday, String cuser_current_address){

            this.cuser_name = cuser_name;
            this.cuser_email = cuser_email;
            this.cuser_phone_number = cuser_phone_number;
            this.cuser_bday = cuser_bday;
            this.cuser_current_address = cuser_current_address;

        }

        public String getCuser_name(){
            return cuser_name;
        }

        public void setCuser_name(){
            this.cuser_name = cuser_name;
        }

        public String getCuser_email(){
            return cuser_email;
        }

        public void setCuser_email(){
            this.cuser_email = cuser_email;
        }

        public String getCuser_phone_number(){
            return cuser_phone_number;
        }

        public void setCuser_phone_number(){
            this.cuser_phone_number = cuser_phone_number;
        }

        public String getCuser_bday(){
            return cuser_bday;
        }

        public void setCuser_bday(){
            this.cuser_bday = cuser_bday;
        }

        public String getCuser_current_address(){
            return cuser_current_address;
        }

        public void setCuser_current_address(){
            this.cuser_current_address = cuser_current_address;
        }


}
