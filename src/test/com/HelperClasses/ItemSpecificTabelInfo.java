package test.com.HelperClasses;

/**
 * Created by Nilesh Verma on 6/20/2016.
 */
public class ItemSpecificTabelInfo {
    private String date, quantity_added, remark, quantity_total, price, updated_price, quantity;
    int sr;


    public ItemSpecificTabelInfo(int i, String quantity, String quantity_added, String quantity_total, String price, String updated_price, String date, String remark) {
        this.sr = i;
        this.quantity = quantity;
        this.quantity_added = quantity_added;
        this.quantity_total = quantity_total;
        this.price = price;
        this.updated_price = updated_price;
        this.date = date;
        this.remark = remark;
    }


    public String getQtyAdded() {
        return quantity_added;
    }

    public String getQtyTotal() {
        return quantity_total;
    }

    public String getPrice() {
        return price;
    }

    public String getSrNo() {
        return String.valueOf(sr);
    }

    public String getUpdatedPrice() {
        return updated_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getRemark() {
        return remark;
    }




}
