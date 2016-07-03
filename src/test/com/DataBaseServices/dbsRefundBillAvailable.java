package test.com.DataBaseServices;

import test.com.HelperClasses.RefundBillItemInfo;
import test.com.HelperClasses.RefundBillTableInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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

import static test.com.MainPage.infoError;

/**
 * Created by Nilesh Verma on 6/26/2016.
 */
public class dbsRefundBillAvailable {
    Connection myConn;
    JTable Bill_table;
    JTable bill_item_table;

    public dbsRefundBillAvailable(Connection myConn, JTable JTableRefundBillAvailable, JTable jTableRefundBillitemAvailable) {
        this.myConn = myConn;
        this.Bill_table = JTableRefundBillAvailable;
        this.bill_item_table = jTableRefundBillitemAvailable;
        settingtheJTabelHeaderofBilling(JTableRefundBillAvailable);
        settingtheJTabelHeaderofBillingItem(jTableRefundBillitemAvailable);
    }

    public void settingtheJTabelHeaderofBilling(JTable JTabelItemAdd) {

        JTabelItemAdd.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Bill.no", "Made By", "Date", "Time", "Amount"
                }
        ));

    }

    public void settingtheJTabelHeaderofBillingItem(JTable settingtheJTabelHeaderofBillingItem) {

        settingtheJTabelHeaderofBillingItem.setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Sr.no", "Item Name", "Quantity", "Amount", "Type"
                }
        ));

    }


    public ArrayList<RefundBillTableInfo> getRefundTabelList() {

        ArrayList<RefundBillTableInfo> itemRefund = new ArrayList<RefundBillTableInfo>();
        ResultSet rs;
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("SELECT * FROM hit.billing where billing_type='normal';");
            rs = myPStmt.executeQuery();
            RefundBillTableInfo refund;
            while (rs.next()) {
                refund = new RefundBillTableInfo(rs.getInt("billing_id"), rs.getString("made_by")
                        , rs.getString("date"), rs.getString("time"), rs.getFloat("amount"));
                itemRefund.add(refund);
            }
        } catch (Exception e) {
            infoError("Error" + e, "Try Again");
        }
        return itemRefund;

    }

    public void Show_Info_In_BillTable() {
        ArrayList<RefundBillTableInfo> list = getRefundTabelList();
        DefaultTableModel model = (DefaultTableModel) Bill_table.getModel();
        Object[] row = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getBillNo();
            row[1] = list.get(i).getMadeBy();
            row[2] = list.get(i).getDate();
            row[3] = list.get(i).getTime();
            row[4] = list.get(i).getAmount();
            model.addRow(row);

        }


    }

    public void refreshRefundBillTabel() {
        DefaultTableModel model = (DefaultTableModel) Bill_table.getModel();
        model.setRowCount(0);
        Show_Info_In_BillTable();
    }


    ///for selected item tabel
    public void refreshRefundSelectedItemTabel(int billing_id) {
        DefaultTableModel model = (DefaultTableModel) bill_item_table.getModel();
        model.setRowCount(0);
        Show_Info_In_BillItemTable(billing_id);
    }

    private void Show_Info_In_BillItemTable(int billing_id) {
        ArrayList<RefundBillItemInfo> list = getRefundBillItem(billing_id);
        DefaultTableModel model = (DefaultTableModel) bill_item_table.getModel();
        Object[] row = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getSrNO();
            row[1] = list.get(i).getItemName();
            row[2] = list.get(i).getQuantity();
            row[3] = list.get(i).getAmount();
            row[4] = list.get(i).getType();
            model.addRow(row);
        }

        setRowColor();


        ///Setting Background Color of the Row

    }

    public ArrayList<RefundBillItemInfo> getRefundBillItem(int billing_id) {
        ArrayList<RefundBillItemInfo> itemRefund = new ArrayList<RefundBillItemInfo>();
        ResultSet rs;
        try {

            PreparedStatement myPStmt = myConn.prepareStatement("SELECT item_name,price," +
                    "concat(quantity,' ',quantity_unit) as quantity,type FROM hit.billingitemselected where billing_id=?;");
            myPStmt.setInt(1, billing_id);
            rs = myPStmt.executeQuery();
            RefundBillItemInfo refund;
            int count = 1;
            while (rs.next()) {
                refund = new RefundBillItemInfo(count, rs.getString("item_name"), rs.getString("quantity")
                        , rs.getFloat("price"), rs.getString("type"));
                itemRefund.add(refund);
                count++;
            }
        } catch (Exception e) {
            infoError("Error" + e, "Try Again");
        }
        return itemRefund;

    }


    //////////for logic

    public void UpdateBillingTable(int billing_id) {
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.billing set billing_type='refund' where billing_id =?");
            myPStmt.setInt(1, billing_id);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
//success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public void UpdateBillingItemTable_NORMALVALUE(int billing_item_id) {
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.billingitemselected set type='refund' " +
                    "where billing_item_id=?");
            myPStmt.setInt(1, billing_item_id);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
//success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public void CreateRefundBillItemsTable_ID() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
        try {
            //----------------------------INSERT-------------------------------------
            PreparedStatement myPStmt = myConn.prepareStatement("INSERT INTO hit.refundbill_item (`date`,`time`)\n" +
                    " VALUES (?,?); ");
            myPStmt.setString(2, String.valueOf(timeFormat.format(date)));
            myPStmt.setString(1, String.valueOf(dateFormat.format(date)));
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                //success
            }


        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc, "TRY again !!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getRefunBillItem_ID() {

        int refund_id = 0;
        try {
            ///this will get the last item id in the sales tabel
            Statement st = myConn.createStatement();
            ResultSet rs = st.executeQuery("select * from hit.refundbill_item Order by refund_id DESC limit 1");
            while (rs.next()) {
                refund_id = rs.getInt("refund_id");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "TRY again !!!", JOptionPane.ERROR_MESSAGE);

        }
        return refund_id;
    }

    public void UpdateBillingItemTable_REF_ID(int billing_item_ID, int refund_ref_id) {
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.billingitemselected set ref_id_item=? " +
                    "where billing_item_id =?");
            myPStmt.setInt(1, refund_ref_id);
            myPStmt.setInt(2, billing_item_ID);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
//success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public int getBillingItem_ID(String item_selected, int billing_no) {
        int bill_item_ID = 0;
        try {
            ResultSet rs;
            PreparedStatement myPStmt = myConn.prepareStatement("SELECT billing_item_id FROM " +
                    "hit.billingitemselected where billing_id=? and item_name=?;");
            myPStmt.setInt(1, billing_no);
            myPStmt.setString(2, item_selected);
            rs = myPStmt.executeQuery();
            while (rs.next()) {
                bill_item_ID = rs.getInt("billing_item_id");
            }
        } catch (Exception e) {
            infoError("Error" + e, "Try Again");
        }
        return bill_item_ID;
    }

    public void UpdateRefundBillItem_QTY(int refund_ref_id, int refund_qty, int qty_in_bill) {
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.refundbill_item set quantity=?,quantity_refund=? " +
                    "where refund_id =?");
            myPStmt.setInt(1, qty_in_bill);
            myPStmt.setInt(2, refund_qty);
            myPStmt.setInt(3, refund_ref_id);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
//success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }

    }

    public void UpdateQuantityTable(String item_selected, int refund_qty) {
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.quantity set" +
                    " quantity=quantity+? where quantity_id=(select " +
                    "quantity_id from hit.item where item_name=?)");
            myPStmt.setInt(1, refund_qty);
            myPStmt.setString(2, item_selected);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
//success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public void setRowColor() {

        bill_item_table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                String status = (String) table.getModel().getValueAt(row, 4);
                if ("refund".equals(status)) {
                    setBackground(Color.RED);
                    setForeground(Color.WHITE);
                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }
                return this;
            }
        });

    }

    public HashMap<String, String> getRefunditemTabelINFO(int billing_item_id) {

        Map<String, String> myMap = new HashMap<String, String>();
        try {
            ResultSet myResultSet;
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("select * from hit.refunditem_info where billing_item_id=?;");
            myPStmt.setInt(1, billing_item_id);
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                myMap.put("quantity", myResultSet.getString("quantity"));
                myMap.put("quantity_refund", myResultSet.getString("quantity_refund"));
                myMap.put("date", myResultSet.getString("date"));
                myMap.put("time", myResultSet.getString("time"));
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
        return (HashMap<String, String>) myMap;
    }

}
