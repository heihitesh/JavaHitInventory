package test.com.DataBaseServices;

import test.com.HelperClasses.ItemSpecificTabelInfo;
import test.com.HelperClasses.ItemTabelInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nilesh Verma on 6/20/2016.
 */
public class dbsItem {
    Connection myConn;
    String Name;
    //getting date from the system
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    JTable JItemTabel,JItemSpecificTable;

    public dbsItem(Connection myConn, String NameOfthePerson, JTable jTabelItemAdd, JTable JTabelItemSpecific) {
        this.myConn = myConn;
        this.Name = NameOfthePerson;
        this.JItemTabel = jTabelItemAdd;
        this.JItemSpecificTable = JTabelItemSpecific;
        settingtheJTabelHeader();
        settingtheJTabelItemSpecifHeader();
    }


    ///---------------------------------------ADD ITEM------------------------------------------------------------------
    public void setItemTabel(int category_id, int sales_id, int quantity_id, String name, String description) {

        if (true) {
            try {
                //----------------------------INSERT-------------------------------------
                PreparedStatement myPStmt = myConn.prepareStatement("INSERT INTO hit.item (`category_id`,`item_name`,`added_by`,`date`" +
                        ",`description`,`sales_id`,`quantity_id`)\n" +
                        " VALUES (?,?,?,?,?,?,?); ");

                myPStmt.setInt(1, category_id);
                myPStmt.setString(2, name);
                myPStmt.setString(3, Name);  //added By
                myPStmt.setString(4, String.valueOf(dateFormat.format(date)));
                myPStmt.setString(5, description);  //added By
                myPStmt.setInt(6, sales_id);
                myPStmt.setInt(7, quantity_id);
                int RowsAffected = myPStmt.executeUpdate();
                if (RowsAffected >= 1) {
                    JOptionPane.showMessageDialog(null, " Added Successfully ", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
                }


            } catch (SQLException sqlException) {
                JOptionPane.showMessageDialog(null, "Possible Error :" +
                        "\n 1) Category Name already Present \n  2) Error code:" + sqlException, "Try Again", JOptionPane.ERROR_MESSAGE);
            } catch (Exception exc) {
                JOptionPane.showMessageDialog(null, exc, "TRY again !!!", JOptionPane.ERROR_MESSAGE);
            }
            // TODO add your code here
        } else {
            JOptionPane.showMessageDialog(null, "Enter Valid Value in Category", "TRY again !!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setSalesTabel(Float normalPrice) {

        try {
            PreparedStatement myPStmt = myConn.prepareStatement("INSERT INTO hit.sales (`price`) VALUES (?); ");
            myPStmt.setFloat(1, normalPrice);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                // JOptionPane.showMessageDialog(null, " Added Successfully ", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Possible Error :" +
                    "\n 1) Category Name already Present \n  2) Error code:" + sqlException, "Try Again", JOptionPane.ERROR_MESSAGE);
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc, "TRY again !!!", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void setQuantityTabel(int quantity, String quantityItemAdded) {
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("INSERT INTO hit.quantity (`quantity`,`quantity_unit`) VALUES (?,?); ");
            myPStmt.setInt(1, quantity);
            myPStmt.setString(2, quantityItemAdded);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                // JOptionPane.showMessageDialog(null, " Added Successfully ", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Possible Error :" +
                    "\n 1) Category Name already Present \n  2) Error code:" + sqlException, "Try Again", JOptionPane.ERROR_MESSAGE);
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc, "TRY again !!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public int getSalesId() {
        int sales_id = 0;
        try {
            ///this will get the last item id in the sales tabel
            Statement st = myConn.createStatement();
            ResultSet rs = st.executeQuery("select * from hit.sales Order by sales_id DESC limit 1");
            while (rs.next()) {
                sales_id = rs.getInt("sales_id");
            }

        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return sales_id;
    }

    public int getQuantityId() {
        int quantity_id = 0;
        try {
            ///this will get the last item id in the sales tabel
            Statement st = myConn.createStatement();
            ResultSet rs = st.executeQuery("select * from hit.quantity Order by quantity_id DESC limit 1");
            while (rs.next()) {
                quantity_id = rs.getInt("quantity_id");
            }

        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return quantity_id;
    }

    public void refreshCategoryList(JComboBox comboBoxAddItemCategoryName) {
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
            infoError(String.valueOf(exc), "TRY AGAIN");
        }

    }

    public int setComboBoxCategoryList(ActionEvent e) {
        ResultSet myResultSet;
        String Category_name = String.valueOf(((JComboBox) e.getSource()).getSelectedItem());
        int cat_id = 0;
        //Selecting From the Data Base
        try {

            PreparedStatement myPStmt = myConn.prepareStatement("select category_id from hit.category where category_name = ?");
            myPStmt.setString(1, Category_name);
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                cat_id = myResultSet.getInt("category_id");

                System.out.println("ID" + cat_id);
            }


            //   System.out.println("index: " + comboBoxAddItemCategoryName.getSelectedIndex() + "   "
            //         + ((JComboBox) e.getSource()).getSelectedItem());
        } catch (SQLException e1)

        {
            infoError(String.valueOf(e), "TRY AGAIN");
        }
        return cat_id;
    }

    /// ----------------------------------- ADD ITEM JTABLE --------------------------------------------------------
    public ArrayList<ItemTabelInfo> getItemTabelList() {

        ArrayList<ItemTabelInfo> item = new ArrayList<ItemTabelInfo>();


        try {
            Statement st = myConn.createStatement();
            ResultSet rs = st.executeQuery("select item.item_name,category.category_name, concat(quantity.quantity ,\" \",quantity.quantity_unit) as quantity,\n" +
                    "item.added_by,item.date,sales.price,item.description from hit.item,hit.quantity,hit.sales,hit.category where item.category_id = category.category_id and\n" +
                    "item.quantity_id = quantity.quantity_id and item.sales_id = sales.sales_id");
            ItemTabelInfo itemtabel;
            int count = 0;
            while (rs.next()) {
                itemtabel = new ItemTabelInfo(count + 1, rs.getString("item_name"), rs.getString("category_name"),
                        rs.getString("quantity"), rs.getString("added_by"), rs.getString("date")
                        , rs.getString("price"), rs.getString("description"));
                item.add(itemtabel);
                count++;

            }


        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return item;

    }

    public void Show_Info_In_ItemTable() {
        ArrayList<ItemTabelInfo> list = getItemTabelList();
        DefaultTableModel model = (DefaultTableModel) JItemTabel.getModel();
        Object[] row = new Object[8];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getSrNo();
            row[1] = list.get(i).getItemName();
            row[2] = list.get(i).getCatagory();
            row[3] = list.get(i).getQuantity();
            row[4] = list.get(i).getAddedBy();
            row[5] = list.get(i).getDate();
            row[6] = list.get(i).getPrice();
            row[7] = list.get(i).getDesc();
            model.addRow(row);

        }
    }

    public void refreshItemTabel() {
        DefaultTableModel model = (DefaultTableModel) JItemTabel.getModel();
        model.setRowCount(0);
        Show_Info_In_ItemTable();
    }

    public void settingtheJTabelHeader() {

        JItemTabel.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Sr.no", "Item Name", "Category", "Quantity", "Added By", "Added On", "Price", "Description"
                }
        ));

    }

    //-----------------------------------Item Specific Tabel ===========================================================

    public void settingtheJTabelItemSpecifHeader() {
        JItemSpecificTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Sr.no", "Quantity", "Quantity Added", "Total Quantity", "Initial Price", "Updated Price", "Date", "Remark",
                }
        ));
    }

    public ArrayList<ItemSpecificTabelInfo> getItemSpecificTabelList(String itemName) {

        ArrayList<ItemSpecificTabelInfo> item = new ArrayList<ItemSpecificTabelInfo>();


        try {
            Statement st = myConn.createStatement();
            ResultSet rs = st.executeQuery("select * from item_table.`"+itemName+"`");
            ItemSpecificTabelInfo tabel;
            int count = 0;
            while (rs.next()) {
                tabel = new ItemSpecificTabelInfo(count + 1, rs.getString("quantity"), rs.getString("quantity_added"),
                        rs.getString("quantity_total"), rs.getString("price"), rs.getString("updated_price"), rs.getString("date")
                        , rs.getString("remark"));
                item.add(tabel);
                count++;

            }

        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return item;

    }

    public void Show_Info_In_ItemSpecificTable(String itemName) {
        ArrayList<ItemSpecificTabelInfo> list = getItemSpecificTabelList(itemName);
        DefaultTableModel model = (DefaultTableModel) JItemSpecificTable.getModel();
        Object[] row = new Object[8];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getSrNo();
            row[1] = list.get(i).getQuantity();
            row[2] = list.get(i).getQtyAdded();
            row[3] = list.get(i).getQtyTotal();
            row[4] = list.get(i).getPrice();
            row[5] = list.get(i).getUpdatedPrice();
            row[6] = list.get(i).getDate();
            row[7] = list.get(i).getRemark();
            model.addRow(row);
        }
    }

    public void refreshItemSpecificTabel(String itemName) {
        DefaultTableModel model = (DefaultTableModel) JItemSpecificTable.getModel();
        model.setRowCount(0);
        Show_Info_In_ItemSpecificTable(itemName);
    }

    /// --------------------------------------MESSAGE SHOWING--------------------------------------------
    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void infoError(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
    }

    public String checkTableID(String itemName) {
        String table_id = "";
        try {
            ResultSet myResultSet;
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("select table_id from hit.item where item_name=?");
            myPStmt.setString(1, itemName);
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                table_id = myResultSet.getString("table_id");
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), "TRY AGAIN");
        }
        return table_id;
    }

    public void createItemSpecificTable(String itemName) {

        try {
            ResultSet myResultSet;
            //Selecting From the Data Base
            String table_name = itemName;
            String sqlCmd = "CREATE TABLE item_table.`" + table_name + "`(item_ref_id int(10) NOT NULL AUTO_INCREMENT,\n" +
                    "quantity VARCHAR(10) not Null,\n" +
                    "quantity_added VARCHAR(10)  Null,\n" +
                    "quantity_total VARCHAR(10) not Null,\n" +
                    "price float not Null,\n" +
                    "updated_price float not Null,\n" +
                    "date date not null,\n" +
                    "remark VARCHAR(100) null,\n" +
                    "PRIMARY KEY ( item_ref_id ));";

            Statement stmt = myConn.createStatement();
            stmt.executeUpdate(sqlCmd);
        } catch (Exception exc) {
            infoError(String.valueOf(exc), "TRY AGAIN");
        }
    }

    public void setItemSpecificTable(String itemName, int itemQty, float itemPrice, int addQty, float newPrice, String itemRemark) {

        try {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("insert into item_table.`" + itemName + "`(quantity,quantity_added," +
                    "quantity_total,price,updated_price,date,remark) values(?,?,?,?,?,?,?);");
            myPStmt.setString(1, String.valueOf(itemQty));
            myPStmt.setString(2, String.valueOf(addQty));
            myPStmt.setString(3, String.valueOf(itemQty + addQty));
            myPStmt.setFloat(4, itemPrice);
            myPStmt.setFloat(5, newPrice);
            myPStmt.setString(6, String.valueOf(dateFormat.format(date)));
            myPStmt.setString(7, itemRemark);

            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                JOptionPane.showMessageDialog(null, "Success", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public void updateQuantityTable(int addQty, String itemName) {

        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.quantity set quantity=? where quantity_id =" +
                    "(select quantity_id from hit.item \n" +
                    "where item_name = ?);");
            myPStmt.setInt(1, addQty);
            myPStmt.setString(2, itemName);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                    //success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public void updateSalesTable(float newPrice, String itemName) {
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.sales set price=? where sales_id =" +
                    "(select sales_id from hit.item \n" +
                    "where item_name = ?);");
            myPStmt.setFloat(1, newPrice);
            myPStmt.setString(2, itemName);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                //success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public void updateRemark(String newRemark, String itemName) {
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.item set description=? where item_name=?;");
            myPStmt.setString(1, newRemark);
            myPStmt.setString(2, itemName);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                //success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }

    public void ChangeTabelID_ItemTable(String itemName) {

        try {
            PreparedStatement myPStmt = myConn.prepareStatement("update hit.item set table_id=? where item_name=?;");
            myPStmt.setString(1, itemName);
            myPStmt.setString(2, itemName);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                //success
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), " TRY AGAIN");
        }
    }


    public void deleteItem(String itemName) {
        try {
            //----------------------------INSERT-------------------------------------

            PreparedStatement myPStmt = myConn.prepareStatement("delete from hit.item where item_name = ?;");
            myPStmt.setString(1, itemName);
            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
                JOptionPane.showMessageDialog(null, " Successfully ", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
            }


        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Possible Error :" +
                    "\n 1) Category Name already Present \n  2) Error code:" + sqlException, "Try Again", JOptionPane.ERROR_MESSAGE);
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc, "TRY again !!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteSpecificItemTable(String itemName) {
        try {
            //----------------------------INSERT-------------------------------------

            PreparedStatement myPStmt = myConn.prepareStatement("drop table item_table.`"+itemName+"`;");

            int RowsAffected = myPStmt.executeUpdate();
            if (RowsAffected >= 1) {
            }
        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, "Possible Error :" +
                    "\n 1) Category Name already Present \n  2) Error code:" + sqlException, "Try Again", JOptionPane.ERROR_MESSAGE);
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc, "TRY again !!!", JOptionPane.ERROR_MESSAGE);
        }
    }
}
