package com.sss.foody.box;

public class cardData {
  //  private String itemName;
    //private String itemDescription;
    private String itemImageUrl;

    public cardData( String itemImageUrl) {
     //   this.itemName = itemName;
   //     this.itemDescription = itemDescription;
        this.itemImageUrl = itemImageUrl;
    }

  /*  public String getItemName() {
        return itemName;
    }

   public String getItemDescription() {
        return itemDescription;
    }*/

    public String getItemImage() {
        return itemImageUrl;
    }
}
