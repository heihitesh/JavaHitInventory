package test.com.HelperClasses;

/**
 * Created by Nilesh Verma on 6/26/2016.
 */
public class RefundBillItemInfo {
    String item_name, srNo, Qty,type;
    float Sell_amt;

    public RefundBillItemInfo(int count, String itemName, String quantity, float selling_price, String type) {
        this.item_name = itemName;
        this.srNo = String.valueOf(count);
        this.Qty = quantity;
        this.Sell_amt = selling_price;
        this.type = type;
    }

    public String getSrNO() {
        return srNo;
    }

    public String getItemName() {
        return item_name;
    }

    public String getQuantity() {
        return Qty;
    }

    public String getAmount() {
        return String.valueOf(Sell_amt);
    }

    public  String getType(){return type;}
}
