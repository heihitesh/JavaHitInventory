package test.com.HelperClasses;

/**
 * Created by Nilesh Verma on 6/22/2016.
 */
public class BillingItemInfo {
    private String itemName , quantity;
    float price,total;
    int sr;

    public BillingItemInfo(int sr, String item_name, String quantity, float price, float total){

        this.sr=sr;
        this.itemName = item_name;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }


    public String getItemName(){
        return itemName;
    }
    public String getQuantity(){
        return quantity;
    }
    public String getTotal(){return String.valueOf(total);}
    public String getSr(){
        return String.valueOf(sr);
    }
    public String getPrice() {
        return String.valueOf(price);
    }
}
