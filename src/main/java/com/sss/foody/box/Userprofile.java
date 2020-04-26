package com.sss.foody.box;

public class Userprofile {

    public String username;
    public String mobile;

    public Userprofile(String username,String mobile) {
        this.mobile = mobile;
        this.username = username;
    }
    public String getmobile() {
        return mobile;
    }

    public void setmobile(String usermobile) {
        this.mobile = usermobile;
    }


    public String getusername() {
        return username;
    }

    public void setusername(String userName) {
        this.username = userName;
    }
}


