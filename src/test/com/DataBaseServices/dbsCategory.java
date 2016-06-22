package test.com.DataBaseServices;

import test.com.HelperClasses.CategoryTableInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Nilesh Verma on 6/20/2016.
 */
public class dbsCategory {
    Connection myConn;
    String Name;
    public JTable JCategoryTable;

    public dbsCategory(Connection myConn, String name, JTable categoryTable) {
        this.myConn = myConn;
        this.Name = name;
        this.JCategoryTable = categoryTable;
        settingtheJTabelHeader(categoryTable);

    }

  //------------------------------------CATEGORY TABEL-------------------
    public void setCategoryTable(String categoryName, String categoryDescription){

        //getting date from the system
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if (categoryName.length() >= 1) {
            try {
                //----------------------------INSERT-------------------------------------
                PreparedStatement myPStmt = myConn.prepareStatement("INSERT INTO hit.category (`category_name`,`description`,`created_by`,`date`)\n" +
                        " VALUES (?,?,?,?); ");

                myPStmt.setString(1, categoryName);
                myPStmt.setString(2, categoryDescription);
                myPStmt.setString(3, Name);
                myPStmt.setString(4, String.valueOf(dateFormat.format(date)));


                int RowsAffected = myPStmt.executeUpdate();
                if (RowsAffected >= 1) {
                    JOptionPane.showMessageDialog(null, categoryName + " Added Successfully ", "Success !!!", JOptionPane.INFORMATION_MESSAGE);
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

    public void deleteCategoryItem(String itemDelete){

        try {
            //----------------------------INSERT-------------------------------------

            PreparedStatement myPStmt = myConn.prepareStatement("delete from hit.category where category_name = ?;");

            myPStmt.setString(1, itemDelete);
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

    public void deleteAlltheData() {
        try{
            Statement stmt = myConn.createStatement();
            String delItemValue = "delete from hit.item where item.category_id not in (select category.category_id from hit.category);";
            String delSalesValue = "delete from hit.sales where sales.sales_id not in (select item.sales_id from hit.item);";
            String delQuantityValue = "delete from hit.quantity where quantity.quantity_id not in (select item.quantity_id from hit.item);";

            stmt.executeUpdate(delItemValue);
            stmt.executeUpdate(delSalesValue);
            stmt.executeUpdate(delQuantityValue);


        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex, "TRY again !!!", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void refreshCategoryTabel() {
        DefaultTableModel model = (DefaultTableModel) JCategoryTable.getModel();
        model.setRowCount(0);
        Show_Info_In_CategoryTable();
    }

    ///---------------------------------CATEGORY JTABEL-----------------------

    public ArrayList<CategoryTableInfo> getCategoryItemList() {

        ArrayList<CategoryTableInfo> categoryItems = new ArrayList<CategoryTableInfo>();


        try {
            Statement st = myConn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM hit.category");
            CategoryTableInfo categoryTableInfo;
            int count = 0;
            while (rs.next()) {
                categoryTableInfo = new CategoryTableInfo(count + 1, rs.getString("category_name"), rs.getString("date"), rs.getString("created_by"), rs.getString("description"));
                categoryItems.add(categoryTableInfo);
                count++;

            }


        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return categoryItems;

    }

    public void Show_Info_In_CategoryTable() {
        ArrayList<CategoryTableInfo> list = getCategoryItemList();
        DefaultTableModel model = (DefaultTableModel) JCategoryTable.getModel();
        Object[] row = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getSrNo();
            row[1] = list.get(i).getName();
            row[2] = list.get(i).getDate();
            row[3] = list.get(i).getAddedBy();
            row[4] = list.get(i).getDesc();
            model.addRow(row);

        }
    }

    public void settingtheJTabelHeader(JTable categoryTable) {

        categoryTable.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Sr.no", "Category Name", "Added On", "Added By", "Description"
                }
        ));


    }


}
