package test.com.DataBaseServices;

import test.com.HelperClasses.BillingItemInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static test.com.MainPage.infoError;

/**
 * Created by Nilesh Verma on 6/22/2016.
 */
public class dbsBillingItemTabel {
    Connection myConn;
    //int Billing_id;
    JTable Billingtabel;
    public dbsBillingItemTabel(Connection myConn, JTable JTabelBillingItem) {
        this.myConn = myConn;
       // this.Billing_id = billing_id;
        Billingtabel = JTabelBillingItem;
        settingtheJTabelHeader(JTabelBillingItem);
    }

    public void setBillingItemTabel(int billing_id, int item_id, int quantity_entered, String quantityUnit, String itemname, int selling_price, int normal_price) {
        try {
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("insert into hit.billingitemselected(billing_id,item_id," +
                    "item_name,normal_price,selling_price,quantity,quantity_unit) values(?,?,?,?,?,?,?);");
            myPStmt.setInt(1, billing_id);
            myPStmt.setInt(2, item_id);
            myPStmt.setString(3, itemname);
            myPStmt.setInt(4, normal_price);
            myPStmt.setInt(5, selling_price);
            myPStmt.setInt(6, quantity_entered);
            myPStmt.setString(7, quantityUnit);

            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                JOptionPane.showMessageDialog(null, "Success", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public ArrayList<BillingItemInfo> getItemTabelList(int billing_id) {

        ArrayList<BillingItemInfo> itemBilling = new ArrayList<BillingItemInfo>();
        ResultSet rs;
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("SELECT * FROM hit.billingitemselected where billing_id=?");
            myPStmt.setInt(1, billing_id);
            rs = myPStmt.executeQuery();
            BillingItemInfo billingItem;
            int count = 0;
            while (rs.next()) {
                billingItem = new BillingItemInfo(count + 1, rs.getString("item_name"), rs.getString("quantity"),
                        "cat", rs.getInt("normal_price"), rs.getInt("selling_price"), 10000);
                itemBilling.add(billingItem);
                count++;
            }


        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return itemBilling;

    }

    public void Show_Info_In_BillingTable(int billing_id) {
        ArrayList<BillingItemInfo> list = getItemTabelList(billing_id);
        DefaultTableModel model = (DefaultTableModel) Billingtabel.getModel();
        Object[] row = new Object[7];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getSr();
            row[1] = list.get(i).getItemName();
            row[2] = list.get(i).getQuantity();
            row[3] = list.get(i).getCategory();
            row[4] = list.get(i).getN_price();
            row[5] = list.get(i).getS_price();
            row[6] = list.get(i).getTotal();
            model.addRow(row);

        }
    }


    public void settingtheJTabelHeader(JTable JTabelItemAdd) {

        JTabelItemAdd.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Sr.no", "Item Name", "Quantity", "Category", "Normal Price", "Selling Price", "Total"
                }
        ));

    }

    public void refreshBillingItemTabel(int billing_id) {
        DefaultTableModel model = (DefaultTableModel) Billingtabel.getModel();
        model.setRowCount(0);
        Show_Info_In_BillingTable(billing_id);
    }
}
