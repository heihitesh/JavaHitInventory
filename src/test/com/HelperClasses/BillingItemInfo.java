package test.com.HelperClasses;

/**
 * Created by Nilesh Verma on 6/22/2016.
 */
public class BillingItemInfo {
    private String itemName , quantity, category;
    int n_price ,s_price,total=1000;
    int sr;

    public BillingItemInfo(int sr, String item_name, String quantity, String category, int n_price, int s_price, int total){

        this.sr=sr;
        this.itemName = item_name;
        this.quantity = quantity;
        this.category = category;
        this.n_price = n_price;
        this.s_price = s_price;
        this.total = total;
    }

    public String getItemName(){
        return itemName;
    }
    public String getQuantity(){
        return quantity;
    }
    public String getCategory(){
        return category;
    }
    public String getTotal(){return String.valueOf(total);}
    public String getSr(){
        return String.valueOf(sr);
    }
    public String getN_price(){ return String.valueOf(n_price);}
    public String getS_price(){ return String.valueOf(s_price);}
}
