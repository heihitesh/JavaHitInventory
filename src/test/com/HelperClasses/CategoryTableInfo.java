package test.com.HelperClasses;

/**
 * Created by Nilesh Verma on 6/19/2016.
 */
public class CategoryTableInfo {
    private String Name , date, addedBy, desc;
    int sr;

    public CategoryTableInfo( int sr,String Name ,String date , String addedBy , String desc){
        this.Name = Name;
        this.date = date;
        this.addedBy = addedBy;
        this.desc = desc;
        this.sr = sr;
    }

    public String getName(){
        return Name;
    }
    public String getDate(){
        return date;
    }
    public String getAddedBy(){
        return addedBy;
    }
    public String getDesc(){
        return desc;
    }
    public String getSrNo(){
        return String.valueOf(sr);
    }
}
