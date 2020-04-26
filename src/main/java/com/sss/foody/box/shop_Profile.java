package com.sss.foody.box;

public class shop_Profile {
    public String shopimageurl;
   // public String shopinfo;
    public String shoplink;
   // public String shopname;


    public shop_Profile() {
     //   this.shopname = shopname;
        this.shoplink = shoplink;
     //   this.shopinfo = shopinfo;
        this.shopimageurl = shopimageurl;
    }
    public  shop_Profile(String shopname, String shoplink, String shopinfo, String shopimageurl) {
     //   this.shopname = shopname;
        this.shoplink = shoplink;   
     //   this.shopinfo = shopinfo;
        this.shopimageurl = shopimageurl;
    }

    public void setShoplink(String shoplink) { this.shoplink = shoplink; }


    public void setShopimageurl(String shopimageurl) {
        this.shopimageurl = shopimageurl;
    }




    public String getShoplink() {
        return shoplink;
    }



    public String getShopimageurl() {
        return shopimageurl;
    }



}
