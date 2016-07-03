package test.com.HelperClasses;

/**
 * Created by Nilesh Verma on 6/26/2016.
 */
public class RefundBillTableInfo {

    private String made_by, date, time;
    int bill_no;
    float amount;


    public RefundBillTableInfo(int billing_id, String made_by, String date, String time, float amount) {
        this.bill_no = billing_id;
        this.made_by = made_by;
        this.date = date;
        this.time = time;
        this.amount = amount;

    }

    public String getBillNo() {
        return String.valueOf(bill_no);
    }

    public String getMadeBy() {
        return made_by;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return String.valueOf(amount);
    }

    public String getTime() {
        return String.valueOf(time);
    }


}
