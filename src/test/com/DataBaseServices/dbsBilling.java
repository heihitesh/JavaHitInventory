package test.com.DataBaseServices;

import test.com.HelperClasses.BillingItemInfo;
import test.com.HelperClasses.ItemTabelInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nilesh Verma on 6/20/2016.
 */
public class dbsBilling {
    Connection myConn;
    String Name;


    public dbsBilling(Connection myConn, String NameOfthePerson) {
        this.myConn = myConn;
        this.Name = NameOfthePerson;


    }

    public void createCategoryList(JComboBox comboBoxAddItemCategoryName) {
        try {
            ResultSet myResultSet;
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("select category_name from hit.category order by ? asc");
            myPStmt.setString(1, "category_name");
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                String FirstName = myResultSet.getString("category_name");
                comboBoxAddItemCategoryName.addItem(FirstName);
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), "---TRY AGAIN");
        }
    }

    //-------------------- MESSAGE SHOWING-----------------------
    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void infoError(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
    }

    public void createItemList(JComboBox comboBoxBillingItemName, String category_name) {

        try {
            ResultSet myResultSet;
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("select item_name from hit.itemNameAcctoCategory where category_name=?;");
            myPStmt.setString(1, category_name);
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                String FirstName = myResultSet.getString("item_name");
                comboBoxBillingItemName.addItem(FirstName);
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }


    public HashMap<String, String> getAvailableQuantity(String item_name) {
        Map<String, String> myMap = new HashMap<String, String>();
        try {
            ResultSet myResultSet;
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("select quantity,quantity_unit from hit.quantityAcctoItemName where item_name=?;");
            myPStmt.setString(1, item_name);
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                String quantity = myResultSet.getString("quantity");
                String quantity_unit = myResultSet.getString("quantity_unit");
                myMap.put("quantity", quantity);
                myMap.put("quantity_unit", quantity_unit);
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
        return (HashMap<String, String>) myMap;
    }

    public HashMap<String,Integer>  getItemIDandSalesInfo(String item_name) {
        Map<String, Integer> myMap = new HashMap<String, Integer>();
        try {
            ResultSet myResultSet;
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("select * from hit.itemInfo where item_name=?;");
            myPStmt.setString(1, item_name);
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                myMap.put("item_id",myResultSet.getInt("item_id"));
                myMap.put("selling_price",myResultSet.getInt("selling_price"));
                myMap.put("normal_price",myResultSet.getInt("selling_price"));

            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
        return (HashMap<String, Integer>) myMap;
    }

    public void setBillingTabel(String description, String name) {
        try {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("insert into hit.billing(made_by,date,time,description_billing) values(?,?,?,?);");
            myPStmt.setString(1, name);
            myPStmt.setString(2, String.valueOf(dateFormat.format(date)));
            myPStmt.setString(3, String.valueOf(timeFormat.format(date)));
            myPStmt.setString(4, description);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                JOptionPane.showMessageDialog(null, "Success", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public void updateQuantityTabel(int item_id, int quantity_entered) {

        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.quantity set quantity=? where quantity_id =" +
                    "(select quantity_id from hit.item \n" +
                    "where item_id = ?);");
            myPStmt.setInt(1, quantity_entered);
            myPStmt.setInt(2, item_id);

            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {

            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }




    public int getBillingTabelID() {
        int Billing_id = -1;
        try{
            ResultSet rs;
        PreparedStatement myPStmt = myConn.prepareStatement("SELECT * FROM hit.billing ORDER BY billing_id DESC LIMIT 1;");
        rs = myPStmt.executeQuery();
        while (rs.next()) {
           Billing_id = rs.getInt("billing_id");
        }

    } catch (Exception e) {
        System.out.println("Exception" + e);
    }
        return Billing_id;
    }



    }

