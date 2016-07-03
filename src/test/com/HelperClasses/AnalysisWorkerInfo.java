package test.com.HelperClasses;

/**
 * Created by Nilesh Verma on 7/2/2016.
 */
public class AnalysisWorkerInfo {

    String billing_id,date,time,amount,type;
    int sr;


    public AnalysisWorkerInfo(int i, String billing_id, String date, String time, String amount, String type_type) {
        this.sr = i ;
        this.billing_id = billing_id;
        this.date = date;
        this.time = time;
        this.amount = amount;
        this.type = type_type;
    }


    public String getBillID(){
        return billing_id;
    }
    public String getDate(){
        return date;
    }
    public String getTime(){return time;}
    public String getAmount(){return amount;}
    public String getType() {
        return type;
    }
    public String getSrno(){ return String.valueOf(sr);}

}
