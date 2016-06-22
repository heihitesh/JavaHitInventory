package test.com.DataBaseServices;

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
    JTable JItemTabel;

    public dbsItem(Connection myConn, String NameOfthePerson, JTable JTabelItemAdd) {
        this.myConn = myConn;
        this.Name = NameOfthePerson;
        this.JItemTabel = JTabelItemAdd;
        settingtheJTabelHeader(JTabelItemAdd);
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
                    JOptionPane.showMessageDialog(null," Added Successfully ", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
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

    public void setSalesTabel(Float normalPrice, Float sellingPrice) {

        try {
            PreparedStatement myPStmt = myConn.prepareStatement("INSERT INTO hit.sales (`normal_price`,`selling_price`) VALUES (?,?); ");
            myPStmt.setFloat(1, normalPrice);
            myPStmt.setFloat(2, sellingPrice);
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

    public int setComboBoxCategoryList(ActionEvent e){
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
                    "item.added_by,item.date,sales.normal_price,sales.selling_price,item.description from hit.item,hit.quantity,hit.sales,hit.category where item.category_id = category.category_id and\n" +
                    "item.quantity_id = quantity.quantity_id and item.sales_id = sales.sales_id");
            ItemTabelInfo itemtabel;
            int count = 0;
            while (rs.next()) {
                itemtabel = new ItemTabelInfo(count + 1, rs.getString("item_name"), rs.getString("category_name"),
                        rs.getString("quantity"), rs.getString("added_by"), rs.getString("date"), rs.getString("normal_price")
                        , rs.getString("selling_price"), rs.getString("description"));
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
        Object[] row = new Object[9];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getSrNo();
            row[1] = list.get(i).getItemName();
            row[2] = list.get(i).getCatagory();
            row[3] = list.get(i).getQuantity();
            row[4] = list.get(i).getAddedBy();
            row[5] = list.get(i).getDate();
            row[6] = list.get(i).getNormalPrice();
            row[7] = list.get(i).getSellingPrice();
            row[8] = list.get(i).getDesc();
            model.addRow(row);

        }
    }

    public void refreshItemTabel() {
        DefaultTableModel model = (DefaultTableModel) JItemTabel.getModel();
        model.setRowCount(0);
        Show_Info_In_ItemTable();
    }

    public void settingtheJTabelHeader(JTable JTabelItemAdd) {

        JTabelItemAdd.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Sr.no", "Item Name", "Category", "Quantity", "Added By", "Added On", "Normal Price", "Selling Price", "Description"
                }
        ));

    }

    /// --------------------------------------MESSAGE SHOWING--------------------------------------------
    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void infoError(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
    }

}
