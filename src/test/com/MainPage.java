/*
 * Created by JFormDesigner on Sat Jun 18 15:28:59 IST 2016
 */

package test.com;

import java.beans.*;
//import org.jdesktop.swingx.*;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import test.com.DataBaseServices.*;

import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.event.*;
import javax.swing.table.*;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;

/**
 * @author Hitesh Verma
 */
public class MainPage extends JFrame {

    String Name, userName, accessParam, phoneNo;
    Connection myConn;
    ResultSet myRs;
    ResultSet myRsItem;

    int categoryIDItemAdded;
    String quantityItemAdded;

    dbsItem dbsItem;
    dbsCategory dbsCategory;
    dbsBilling dbsBilling;
    dbsBillingItemTabel dbsBillingItemTabel;
    dbsRefundBillAvailable dbsRefundBillAvailable;
    dbsAnalysis dbsAnalysis;

    public MainPage(String name, String userName, String phone_no, String accessparam, Connection myConn, ResultSet myRs) {
        initComponents();

        this.Name = name;
        this.userName = userName;
        this.phoneNo = phone_no;
        this.accessParam = accessparam;
        this.myConn = myConn;
        this.myRs = myRs;

        //Setting up the User Info
        tvMainPageName.setText(Name);
        tvMainPageUserName.setText(userName);
        tvMainPagePhoneNo.setText(phone_no);
        tvMainPageAccessParm.setText(accessparam);


        //settingUpTheViewAccording to the ACCESS PARAMETER
        setAccordingToAccessParam();
        setTheme();
    }

    public void setTheme() {
        String s = "de.javasoft.plaf.synthetica.SyntheticaClassyLookAndFeel";
        try {
            javax.swing.UIManager.setLookAndFeel(s);
            SwingUtilities.updateComponentTreeUI(MainPage.this);

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    private void setAccordingToAccessParam() {
        switch (accessParam) {
            case "manager":
                System.out.println("manager");
                tabbedPaneAdding.setVisible(true);
                panelRefund.setVisible(true);
                tabbedPaneAnalysis.setVisible(true);
                break;

            case "worker":
                System.out.println("worker");
                tabbedPaneAdding.setVisible(false);
                panelRefund.setVisible(false);
                tabbedPaneAnalysis.setVisible(false);
                break;
        }
    }

    private void bLogOut(ActionEvent e) {
        // TODO add your code here
        this.setVisible(false);
        // new MainPage().setVisible(true);
        new LoginSignUp().setVisible(true);
    }

    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void infoError(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
    }

    //-----------------------MAIN TABBED PANE -----------------------------------------------

    private void tabbedPaneInventoryStateChanged(ChangeEvent e) {
        // TODO add your code here
        JTabbedPane pane = (JTabbedPane) e.getSource();
        int index = pane.getSelectedIndex();
        switch (String.valueOf(pane.getTitleAt(index))) {
            case "Main Page":
                break;

            case "Adding Stuff":
                //setting Up the Units
                comboBoxAddItemQuantityUnits.removeAllItems();
                comboBoxAddItemQuantityUnits.addItem("KG");
                comboBoxAddItemQuantityUnits.addItem("g");
                comboBoxAddItemQuantityUnits.addItem("L");
                comboBoxAddItemQuantityUnits.addItem("ml");
                comboBoxAddItemQuantityUnits.addItem("piece");
                break;

            case "Billing":
                break;

            case "Analysis":
                dbsAnalysis = new dbsAnalysis(myConn, JTableWorkerAnalysis);
                comboBoxWorkerAnalysisName.removeAllItems();
                dbsAnalysis.createWorkerList(comboBoxWorkerAnalysisName);

                break;

        }

    }

    //----------------ADDING STUFF TABBED PANE --------------------------------------

    private void tabbedPaneAddingStuffStateChanged(ChangeEvent e) {
        // TODO add your code here

        JTabbedPane pane = (JTabbedPane) e.getSource();
        int index = pane.getSelectedIndex();
        switch (String.valueOf(pane.getTitleAt(index))) {
            case "Quantity":

                break;

            case "Item":
                dbsItem = new dbsItem(myConn, Name, JTabelItemAdd, JtabelItemSpecifiItemDetails);
                dbsItem.refreshItemTabel();

                break;

            case "Category":
                dbsCategory = new dbsCategory(myConn, Name, CategoryTable);
                dbsCategory.refreshCategoryTabel();
                break;

        }

    }

    //----------------ADDING BILLING TABBED PANE ------------------------------------

    private void tabbedPaneBillingSateChange(ChangeEvent e) {
        // TODO add your code here
        JTabbedPane pane = (JTabbedPane) e.getSource();
        int index = pane.getSelectedIndex();
        switch (String.valueOf(pane.getTitleAt(index))) {
            case "Billing":
                dbsBilling = new dbsBilling(myConn, Name);
                dbsBillingItemTabel = new dbsBillingItemTabel(myConn, JTabelBillingItem);  //creating a new class for handelling items selected
                break;

            case "Refund":
                dbsRefundBillAvailable = new dbsRefundBillAvailable(myConn, JTableRefundBillAvailable, JTableRefundBillitemAvailable);
                dbsRefundBillAvailable.refreshRefundBillTabel();
                break;


        }
    }

    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


    //-----------------------CATEGORY TABLE --------------------------------------------------

    private void bAddCategory(ActionEvent e) {
        String CategoryName = etCategoryAddName.getText();
        String CategoryDescription = etCategoryDescription.getText();
        dbsCategory.setCategoryTable(CategoryName, CategoryDescription);  // add the Data to CategoryTabel
        dbsCategory.refreshCategoryTabel();
    }

    private void bDeleteCategory(ActionEvent e) {
        // TODO add your code here
        String itemDelete = tvDeletecategorylabel.getText();
        dbsCategory.deleteCategoryItem(itemDelete);
        dbsCategory.deleteAlltheData();
        dbsCategory.refreshCategoryTabel();
    }

    private void mouseClickedCategorytabel(MouseEvent e) {
        int i = CategoryTable.getSelectedRow();
        TableModel model = CategoryTable.getModel();
        // Display Slected Row In JTexteFields
        tvDeletecategorylabel.setText(model.getValueAt(i, 1).toString());
        // TODO add your code here
    }  //handelling The events in JTABLE

    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


    //------------------------ITEM TABEL--------------------------------------------------------------------------------

    String itemName, itemRemark;
    float itemPrice;
    int itemQty;

    private void onClickPLUS(ActionEvent e) {
        // TODO add your code here the + button

        bAddItemCatagoryChoose.setVisible(false);
        comboBoxAddItemCategoryName.setVisible(true);
        bAddItemRefresh.setVisible(true);
        dbsItem.refreshCategoryList(comboBoxAddItemCategoryName);
        // refreshItemPanel();
    }

    public void bItemSave(ActionEvent e) {
        // TODO add your code here
        Float price = Float.valueOf(tvAdditemNormalPrice.getText());
        int quantity = Integer.parseInt(tvAddItemQuantity.getText());
        String itemName = String.valueOf(tvAdditemName.getText());
        String itemDesc = String.valueOf(tvAdditemDescription.getText());

        bAddItemCatagoryChoose.setVisible(true);
        comboBoxAddItemCategoryName.setVisible(false);
        bAddItemRefresh.setVisible(false);
        comboBoxAddItemCategoryName.removeAllItems();

        setValuetoNullinAddItem();

        dbsItem.setSalesTabel(price);  //setting Data in Sales tabel

        dbsItem.setQuantityTabel(quantity, quantityItemAdded);                            //Setting Data in Quantity Tabel

        dbsItem.setItemTabel(categoryIDItemAdded, dbsItem.getSalesId(),
                dbsItem.getQuantityId(), itemName
                , itemDesc);  //Setting Data in ItemTabel

        dbsItem.refreshItemTabel();

    }

    private void setValuetoNullinAddItem() {
        tvAdditemName.setText("");
        tvAdditemNormalPrice.setText("");
        tvAddItemQuantity.setText("");
        tvAdditemDescription.setText("");
    }

    private void bAddItemRefresh(ActionEvent e) {
        // TODO add your code here
        comboBoxAddItemCategoryName.removeAllItems();
        dbsItem.refreshCategoryList(comboBoxAddItemCategoryName);
        //refreshItemPanel();
    }

    private void comboBoxAddItemCategoryNameActionPerformed(ActionEvent e) {
        // TODO add your code here
        ResultSet myResultSet;
        String Category_name = String.valueOf(((JComboBox) e.getSource()).getSelectedItem());
        int cat_id = 0;
        //Selecting From the Data Base
        try {

            PreparedStatement myPStmt = myConn.prepareStatement("select category_id from hit.category where category_name = ?");
            myPStmt.setString(1, Category_name);
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                categoryIDItemAdded = myResultSet.getInt("category_id");
            }


            //   System.out.println("index: " + comboBoxAddItemCategoryName.getSelectedIndex() + "   "
            //         + ((JComboBox) e.getSource()).getSelectedItem());
        } catch (SQLException e1)

        {
            infoError(String.valueOf(e), " TRY AGAIN");
        }
        //categoryIDItemAdded = dbsItem.setComboBoxCategoryList(e);


    }

    private void comboBoxAddItemQuantityUnitsActionPerformed(ActionEvent e) {
        // TODO add your code here
        quantityItemAdded = String.valueOf(((JComboBox) e.getSource()).getSelectedItem());
    }

    private void mouseClickedItemTabel(MouseEvent e) {
        // TODO add your code here
        int i = JTabelItemAdd.getSelectedRow();
        TableModel model = JTabelItemAdd.getModel();
        // Display Slected Row In JTexteFields

        itemName = model.getValueAt(i, 1).toString();
        tvItemUpdateItemName.setText(itemName);
        tvItemDeleteitemName.setText(itemName);

        itemPrice = Float.parseFloat(model.getValueAt(i, 6).toString());
        etItemUpdatePrice.setText(String.valueOf(itemPrice));
        //this will change the string 2 kg to 2 (only Integer value)
        String temp_qty = model.getValueAt(i, 3).toString();
        itemQty = Integer.parseInt(temp_qty.replaceAll("[\\D]", ""));

        itemRemark = model.getValueAt(i, 7).toString();

        //updated table
        dbsItem.refreshItemSpecificTabel(itemName);


    }

    private void bItemUpdateItems(ActionEvent e) {
        // TODO add your code here
        int addQty = Integer.parseInt(etItemUpdateQtyAdd.getText());
        float newPrice = Float.parseFloat(etItemUpdatePrice.getText());
        String newRemark = etItemUpdateRemark.getText();

        String table_id = dbsItem.checkTableID(itemName);
        if (table_id == null) {  //table ID is null never Updated any Value in this row
            //1) change the column table_id to the table name
            //2)create a table with the name of the Item Name
            dbsItem.createItemSpecificTable(itemName);
            //3) Inserting the values in the table
            dbsItem.setItemSpecificTable(itemName, itemQty, itemPrice, addQty, newPrice, itemRemark);
            //4) Updating the Quantity Table
            dbsItem.updateQuantityTable(itemQty + addQty, itemName);
            //5) Update the Sales Tabel
            dbsItem.updateSalesTable(newPrice, itemName);
            //6) change the Remark
            dbsItem.updateRemark(newRemark, itemName);
            //7) change Item table table_id to not NULL
            dbsItem.ChangeTabelID_ItemTable(itemName);
            //8) Update both the tables
            dbsItem.refreshItemTabel();
            dbsItem.refreshItemSpecificTabel(itemName);


        } else { //if the table is already created only just increment the tabel
            dbsItem.setItemSpecificTable(itemName, itemQty, itemPrice, addQty, newPrice, itemRemark);

            dbsItem.updateQuantityTable(itemQty + addQty, itemName);
            //5) Update the Sales Tabel
            dbsItem.updateSalesTable(newPrice, itemName);
            //6) change the Remark
            dbsItem.updateRemark(newRemark, itemName);
            //7) change Item table table_id to not NULL
            dbsItem.ChangeTabelID_ItemTable(itemName);
            //8) Update both the tables
            dbsItem.refreshItemTabel();
            dbsItem.refreshItemSpecificTabel(itemName);
        }

        tvItemUpdateItemName.setText("...");
        etItemUpdateQtyAdd.setText("");
        etItemUpdatePrice.setText("");
        etItemUpdateRemark.setText("");
    }

    private void bItemDeleteItem(ActionEvent e) {
        // TODO add your code here
        dbsItem.deleteItem(itemName);
        dbsItem.deleteSpecificItemTable(itemName);
        //8) Update both the tables
        dbsItem.refreshItemTabel();
        dbsItem.refreshItemSpecificTabel(itemName);
    } //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%^^^^^^^^^^^^^^^^^recode require

    private void bItemRefreshTable(ActionEvent e) {
        // TODO add your code here
        dbsItem.refreshItemTabel();
        dbsItem.refreshItemSpecificTabel(itemName);
    }



//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

//------------------------------BILLING TABEL --------------------------------------------------------------------------

    String category_name;
    String item_name;
    int quantity;
    String quantity_unit;
    int billing_id = -1;
    float total_amount = 0;


    private void comboBoxBillingItemName(ActionEvent e) {
        item_name = String.valueOf(((JComboBox) e.getSource()).getSelectedItem());
        HashMap<String, String> myMap = dbsBilling.getAvailableQuantity(item_name);
        quantity = Integer.parseInt(myMap.getOrDefault("quantity", "1")); //used because it was passing a nulll value
        quantity_unit = myMap.getOrDefault("quantity_unit", "...");
        tvBillingAvailQty.setText(quantity + " " + quantity_unit);

    }

    private void comboBoxBillingCategoryName(ActionEvent e) {
        // TODO add your code here
        category_name = String.valueOf(((JComboBox) e.getSource()).getSelectedItem());
        System.out.println("category_name" + category_name);
        comboBoxBillingItemName.removeAllItems();
        dbsBilling.createItemList(comboBoxBillingItemName, category_name);
    }

    private void bBillingNewBill(ActionEvent e) {
        // TODO add your code here
        dbsBilling.setBillingTabel(userName);

        billing_id = dbsBilling.getBillingTabelID();

        tvBillingBillNo.setText("Billing No: " + billing_id + ".");

        //setting the visibility to true
        panelBillingMain.setVisible(true);
        tvBillingAvailQty.setText(".....");
        etBillingQuantity.setText("");
        dbsBillingItemTabel.refreshBillingItemTabel(billing_id);

        //clearing the total amount
        total_amount = 0;
        tvBillingTotalAmount.setText("00.00");

        panelBillingUpdate.setVisible(false);

        ///ComboBox
        comboBoxBillingCategoryName.removeAllItems();
        dbsBilling.createCategoryList(comboBoxBillingCategoryName);

        comboBoxBillingItemName.removeAllItems();
        dbsBilling.createItemList(comboBoxBillingItemName, category_name);

    }

    private void bREFRESH_Billing_CategoryName(ActionEvent e) {
        // TODO add your code here
        // TODO add your code here
        comboBoxBillingCategoryName.removeAllItems();
        dbsBilling.createCategoryList(comboBoxBillingCategoryName);
    }


    private void bREFRESH_Billing_ItemName(ActionEvent e) {
        // TODO add your code here
        comboBoxBillingItemName.removeAllItems();
        dbsBilling.createItemList(comboBoxBillingItemName, category_name);
    }

    private void bDeleteALL(ActionEvent e) {
        // TODO add your code heretry
        //
    }

    private void bBillingPrintBill(ActionEvent e) {
        String CustomerName = etBillingCustomerName.getText();
        String VehicleNo = etBillingVehicleNo.getText();

        if(etBillingCustomerName.getText().length() >=1 && etBillingCustomerName.getText().length() >=1) {

            try {
                // String AddressofImage = "C:\\Users\\Nilesh Verma\\Documents\\NetBeansProjects\\JDBCTest01\\src\\jdbctest01\\report1.jrxml";
                Map parametersMap = new HashMap();
                parametersMap.put("billing_id", billing_id);
                parametersMap.put("name", userName);
                parametersMap.put("customer_name", CustomerName);
                parametersMap.put("vehicle_no", VehicleNo);
                parametersMap.put("vat_no", "1234567");
                // parametersMap.put("bill_no",5);
                InputStream loc = getClass().getResourceAsStream("/Invoice.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                        loc, parametersMap, myConn);
                JasperViewer.viewReport(jasperPrint, false); //to view the report in the jasper Viewer

                //hide the main Pannel of the billing
                panelBillingMain.setVisible(false);

                //update the amount of the Billing tabel
                dbsBilling.setTotalAmountInBillingTable(billing_id, total_amount, CustomerName, VehicleNo);

                panelBillingUpdate.setVisible(false);


            } catch (Exception ex) {
                infoError(String.valueOf(ex), "Error");
            }
        }else{
            infoError("Please Enter CustomerName and Vehicle No", "Error");
        }
    }

    private void bBillingAdditemToBill(ActionEvent e) {

        int quantity_entered = Integer.parseInt(etBillingQuantity.getText());
        if (quantity >= quantity_entered && quantity_entered >= 1) {
            Float price = dbsBilling.getSalesInfo(item_name);

            dbsBillingItemTabel.setBillingItemTabel(billing_id, quantity_entered,
                    quantity_unit, item_name, price);

            dbsBilling.updateQuantityTabel(item_name, quantity - quantity_entered);  // remove items their
            ///For tabel
            dbsBillingItemTabel.refreshBillingItemTabel(billing_id);

            //set the total amount
            //total_amount = total_amount + price * quantity_entered;
            //tvBillingTotalAmount.setText(" " + total_amount);

            total_amount = dbsBillingItemTabel.getTotalBillingAmount();

            tvBillingTotalAmount.setText(String.valueOf(total_amount));
            dbsBillingItemTabel.setTotalAnalysisBilling();

            //update the Quantity Text Refresh the item List
            tvBillingAvailQty.setText("...");
            etBillingQuantity.setText("");
            comboBoxBillingItemName.removeAllItems();
            dbsBilling.createItemList(comboBoxBillingItemName, category_name);
            panelBillingUpdate.setVisible(false);

        } else {
            infoError("Please Enter a Valid Value \n Entered Value is More than Availabe", "Try Again");
        }
        // TODO add your code here
    }


    ////UPDATE----------------------------------
    int quantityAvailableUpdate;
    int qtyInTable;
    String itemBillingName;
    Float UpdatePrice;

    private void JTabelBillingItemMouseClicked(MouseEvent e) {

        panelBillingUpdate.setVisible(true);

        int i = JTabelBillingItem.getSelectedRow();
        TableModel model = JTabelBillingItem.getModel();
        // Display Slected Row In JTexteFields
        itemBillingName = model.getValueAt(i, 1).toString();
        qtyInTable = Integer.parseInt(model.getValueAt(i, 2).toString());
        UpdatePrice = Float.parseFloat(model.getValueAt(i, 3).toString());
        tvBillingItemName.setText(itemBillingName);


        HashMap<String, String> myMap2 = dbsBilling.getAvailableQuantity(itemBillingName);
        quantityAvailableUpdate = Integer.parseInt(myMap2.getOrDefault("quantity", "1")); //used because it was passing a nulll value
        String quantity_unit = myMap2.getOrDefault("quantity_unit", "...");
        int totalAvailQTY = quantityAvailableUpdate + qtyInTable;
        tvBillingUpdateQtyAvaliable.setText(quantityAvailableUpdate + " + " + qtyInTable + " = " + totalAvailQTY + " " + quantity_unit);

    }


    private void bBillingDelete(ActionEvent e) {
        // TODO add your code here
        dbsBillingItemTabel.deleteItemFormItemTable(itemBillingName, billing_id);
        panelBillingUpdate.setVisible(false);
        dbsBilling.updateQuantityTabel(itemBillingName, quantityAvailableUpdate + qtyInTable);  // remove items their

        dbsBillingItemTabel.refreshBillingItemTabel(billing_id);
        comboBoxBillingItemName.removeAllItems();
        dbsBilling.createItemList(comboBoxBillingItemName, category_name);

        //update the Total text field
        tvBillingTotalAmount.setText(String.valueOf(dbsBillingItemTabel.getTotalBillingAmount()));
        dbsBillingItemTabel.setTotalAnalysisBilling();

    }

    private void bBillingUpdateQty(ActionEvent e) {
        // TODO add your code here
        if (Integer.parseInt(etBillingUpdate.getText()) <= quantityAvailableUpdate + qtyInTable && etBillingUpdate.getText().length() >= 1) {
            int quantity = (quantityAvailableUpdate + qtyInTable) - Integer.parseInt(etBillingUpdate.getText());

            dbsBillingItemTabel.UpdateQtyandTotalBillingItemTable(Integer.parseInt(etBillingUpdate.getText()), UpdatePrice, itemBillingName, billing_id);

            dbsBilling.updateQuantityTabel(itemBillingName, quantity);  // remove items their


            comboBoxBillingItemName.removeAllItems();
            dbsBilling.createItemList(comboBoxBillingItemName, category_name);


            //Update the text Fields
            HashMap<String, String> myMap2 = dbsBilling.getAvailableQuantity(itemBillingName);
            quantityAvailableUpdate = Integer.parseInt(myMap2.getOrDefault("quantity", "1")); //used because it was passing a nulll value
            String quantity_unit = myMap2.getOrDefault("quantity_unit", "...");
            tvBillingUpdateQtyAvaliable.setText(quantityAvailableUpdate + " " + quantity_unit);

            dbsBillingItemTabel.refreshBillingItemTabel(billing_id);
            tvBillingTotalAmount.setText(String.valueOf(dbsBillingItemTabel.getTotalBillingAmount()));
            dbsBillingItemTabel.setTotalAnalysisBilling();


            quantityAvailableUpdate = 0;
            qtyInTable = 0;
            UpdatePrice = Float.valueOf(0);

            tvBillingUpdateQtyAvaliable.setText("");
            tvBillingItemName.setText("");
            etBillingUpdate.setText("");
            panelBillingUpdate.setVisible(false);

        } else {
            infoError("Please Enter A Value Less Than Available Qty", "Try Again");
        }
    }


    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    //-----------------------------------REFUND TABLE -----------------------------------------------------------------

    int billing_no;
    int Qty_in_bill;
    String item_selected;
    int billing_item_ID;

    private void mouseClickedRefundBillingTabel(MouseEvent e) {
        // TODO add your code here
        int i = JTableRefundBillAvailable.getSelectedRow();
        TableModel model = JTableRefundBillAvailable.getModel();
        // Display Slected Row In JTexteFields
        //Refresh the Selected JTabel sending the Billing ID
        billing_no = Integer.parseInt(String.valueOf(model.getValueAt(i, 0)));
        dbsRefundBillAvailable.refreshRefundSelectedItemTabel(billing_no);
        // TODO add your code here

    }

    private void mouseClickedRefundBillingItemsTabel(MouseEvent e) {
        // TODO add your code here
        String Qty_in_Bill;
        //changeing the type of billing to refund from normal
        int i = JTableRefundBillitemAvailable.getSelectedRow();
        TableModel model = JTableRefundBillitemAvailable.getModel();
        // Display Slected Row In JTexteFields
        //Refresh the Selected JTabel sending the Billing ID
        Qty_in_Bill = String.valueOf(model.getValueAt(i, 2));

        item_selected = String.valueOf(model.getValueAt(i, 1));
        //this will change the string 2 kg to 2 (only Integer value)
        Qty_in_bill = Integer.parseInt(Qty_in_Bill.replaceAll("[\\D]", ""));

        billing_item_ID = dbsRefundBillAvailable.getBillingItem_ID(item_selected, billing_no);

        if (model.getValueAt(i, 4).equals("refund")) {
            panelRefundINFO.setVisible(true);
            HashMap<String, String> myMap = dbsRefundBillAvailable.getRefunditemTabelINFO(billing_item_ID);

            String qty = myMap.getOrDefault("quantity", " "); //used because it was passing a nulll value
            String qty_refund = myMap.getOrDefault("quantity_refund", " "); //used because it was passing a nulll value
            String date = myMap.getOrDefault("date", " "); //used because it was passing a nulll value
            String time = myMap.getOrDefault("time", " "); //used because it was passing a nulll value

            tvRefundIntialQty.setText(qty);
            tvRefundQtyRefund.setText(qty_refund);
            tvRefundDateofRefund.setText(date);
            tvRefundTimeofRefund.setText(time);
        } else {
            panelRefundINFO.setVisible(false);
        }


        tvRefundQtyofItems.setText(Qty_in_Bill + ".");

    }

    private void bRefundCancelTheWholeBill(ActionEvent e) {
        // TODO add your code here
        System.out.println("clicked");
        dbsRefundBillAvailable.UpdateBillingTable(billing_id);
    }

    private void onClickRefundBillItems(ActionEvent e) {
        // TODO add your code here
        int Refund_Qty = Integer.parseInt(etRefundNewQuantity.getText());
        if (Refund_Qty <= Qty_in_bill && Refund_Qty >= 1) {
            //1)  update normal to refund in Billing_item_table
            dbsRefundBillAvailable.UpdateBillingItemTable_NORMALVALUE(billing_item_ID);
            //2) increment the refundbill_item tabel id Setting the time and refund of the Items
            dbsRefundBillAvailable.CreateRefundBillItemsTable_ID();
            //3) get the last refrence refund ID of the refundBill item table
            int refund_Ref_id = dbsRefundBillAvailable.getRefunBillItem_ID();
            //4) put the refrence id into in the Billing Item Table
            dbsRefundBillAvailable.UpdateBillingItemTable_REF_ID(billing_item_ID, refund_Ref_id);
            //5) Update the refundBillitem Table its quantity
            dbsRefundBillAvailable.UpdateRefundBillItem_QTY(refund_Ref_id, Refund_Qty, Qty_in_bill);
            //6) Finally Update the Main Quantity Table
            dbsRefundBillAvailable.UpdateQuantityTable(item_selected, Refund_Qty);
        }
    }


    //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

    //---------------------------------------ANALYSIS------------------------------------------------------------------

    String worker_name;

    private void comboBoxWorkerAnalysisName(ActionEvent e) {
        // TODO add your code here
        worker_name = String.valueOf(((JComboBox) e.getSource()).getSelectedItem());
    }

    private void bWorkerAnalysisCHECK(ActionEvent e) {
        // TODO add your code here
        dbsAnalysis.getWorkerAnalysis(worker_name);
        String total = String.valueOf(dbsAnalysis.getTotalAnalysisBilling());
        tvWorkerAnalysisTotal.setText(total);
        dbsAnalysis.setTotalAnalysisBilling();

    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Hitesh Verma
        tabbedPaneAddingStuff = new JTabbedPane();
        panel1 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        tvMainPageName = new JLabel();
        label4 = new JLabel();
        tvMainPageUserName = new JLabel();
        label6 = new JLabel();
        tvMainPagePhoneNo = new JLabel();
        label8 = new JLabel();
        tvMainPageAccessParm = new JLabel();
        label10 = new JLabel();
        bLogOut = new JButton();
        panelAddingStuff = new JPanel();
        tabbedPaneAdding = new JTabbedPane();
        CatagoryPanel = new JScrollPane();
        panel6 = new JPanel();
        label11 = new JLabel();
        label12 = new JLabel();
        etCategoryAddName = new JTextField();
        label13 = new JLabel();
        scrollPane3 = new JScrollPane();
        etCategoryDescription = new JTextArea();
        separator1 = new JSeparator();
        button2 = new JButton();
        label14 = new JLabel();
        bDeleteCategory = new JButton();
        separator2 = new JSeparator();
        label16 = new JLabel();
        separator3 = new JSeparator();
        label17 = new JLabel();
        scrollPane1 = new JScrollPane();
        CategoryTable = new JTable();
        label3 = new JLabel();
        tvDeletecategorylabel = new JLabel();
        bDelete = new JButton();
        scrollPane5 = new JScrollPane();
        panel7 = new JPanel();
        label21 = new JLabel();
        label22 = new JLabel();
        tvAdditemName = new JTextField();
        label23 = new JLabel();
        comboBoxAddItemCategoryName = new JComboBox();
        bAddItemCatagoryChoose = new JButton();
        button4 = new JButton();
        bAddItemRefresh = new JButton();
        label24 = new JLabel();
        tvAdditemNormalPrice = new JTextField();
        label26 = new JLabel();
        tvAddItemQuantity = new JTextField();
        comboBoxAddItemQuantityUnits = new JComboBox();
        label27 = new JLabel();
        scrollPane6 = new JScrollPane();
        tvAdditemDescription = new JTextArea();
        scrollPane7 = new JScrollPane();
        JTabelItemAdd = new JTable();
        label18 = new JLabel();
        label33 = new JLabel();
        tvItemUpdateItemName = new JLabel();
        label37 = new JLabel();
        label39 = new JLabel();
        bItemUpdate = new JButton();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        etItemUpdateQtyAdd = new JTextField();
        etItemUpdatePrice = new JTextField();
        label41 = new JLabel();
        scrollPane14 = new JScrollPane();
        etItemUpdateRemark = new JTextArea();
        tvItemUpdateQtyUnit = new JLabel();
        scrollPane15 = new JScrollPane();
        JtabelItemSpecifiItemDetails = new JTable();
        label35 = new JLabel();
        tvItemDeleteitemName = new JLabel();
        button6 = new JButton();
        bItemRefreshTable = new JButton();
        panel3 = new JPanel();
        tabbedPane = new JTabbedPane();
        scrollPane4 = new JScrollPane();
        panel5 = new JPanel();
        button3 = new JButton();
        panelBillingMain = new JPanel();
        label20 = new JLabel();
        comboBoxBillingItemName = new JComboBox();
        label28 = new JLabel();
        comboBoxBillingCategoryName = new JComboBox();
        label29 = new JLabel();
        tvBillingAvailQty = new JLabel();
        label31 = new JLabel();
        etBillingQuantity = new JTextField();
        bBillingAdditemToBill = new JButton();
        bREFRESH_Billing_CategoryName = new JButton();
        bREFRESH_Billing_ItemName = new JButton();
        scrollPane10 = new JScrollPane();
        JTabelBillingItem = new JTable();
        bBillingPrintBill = new JButton();
        tvBillingBillNo = new JLabel();
        label15 = new JLabel();
        tvBillingTotalAmount = new JLabel();
        label19 = new JLabel();
        panelBillingUpdate = new JPanel();
        bBillingDelete = new JButton();
        bBillingUpdateQty = new JButton();
        etBillingUpdate = new JTextField();
        tvBillingItemName = new JLabel();
        tvBillingUpdateQtyAvaliable = new JLabel();
        label43 = new JLabel();
        label44 = new JLabel();
        label47 = new JLabel();
        label45 = new JLabel();
        etBillingCustomerName = new JTextField();
        label46 = new JLabel();
        etBillingVehicleNo = new JTextField();
        tabbedPaneRefund = new JScrollPane();
        panelRefund = new JPanel();
        label9 = new JLabel();
        textField1 = new JTextField();
        scrollPane12 = new JScrollPane();
        JTableRefundBillAvailable = new JTable();
        scrollPane13 = new JScrollPane();
        JTableRefundBillitemAvailable = new JTable();
        label30 = new JLabel();
        etRefundNewQuantity = new JTextField();
        button5 = new JButton();
        bRefundBillItems = new JButton();
        tvRefundQtyofItems = new JLabel();
        panelRefundINFO = new JPanel();
        label7 = new JLabel();
        tvRefundIntialQty = new JLabel();
        label32 = new JLabel();
        tvRefundQtyRefund = new JLabel();
        label34 = new JLabel();
        tvRefundDateofRefund = new JLabel();
        label36 = new JLabel();
        tvRefundTimeofRefund = new JLabel();
        label40 = new JLabel();
        panel2 = new JPanel();
        tabbedPaneAnalysis = new JTabbedPane();
        panel9 = new JPanel();
        scrollPane9 = new JScrollPane();
        panel10 = new JPanel();
        label25 = new JLabel();
        comboBoxWorkerAnalysisName = new JComboBox();
        scrollPane16 = new JScrollPane();
        JTableWorkerAnalysis = new JTable();
        bWorkerAnalysisCHECK = new JButton();
        label38 = new JLabel();
        tvWorkerAnalysisTotal = new JLabel();
        label42 = new JLabel();
        panel11 = new JPanel();
        label5 = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();

        //======== tabbedPaneAddingStuff ========
        {
            tabbedPaneAddingStuff.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    tabbedPaneInventoryStateChanged(e);
                }
            });

            //======== panel1 ========
            {

                // JFormDesigner evaluation mark
                panel1.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


                //---- label1 ----
                label1.setText("THE HITESH INVENTORY SYSTEM V0.1(Test)");
                label1.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 18));

                //---- label2 ----
                label2.setText("Name :-");

                //---- tvMainPageName ----
                tvMainPageName.setText("----");

                //---- label4 ----
                label4.setText("User Name :-");

                //---- tvMainPageUserName ----
                tvMainPageUserName.setText("----");

                //---- label6 ----
                label6.setText("Phone No:-");

                //---- tvMainPagePhoneNo ----
                tvMainPagePhoneNo.setText("----");

                //---- label8 ----
                label8.setText("Access Parameter Level :-");

                //---- tvMainPageAccessParm ----
                tvMainPageAccessParm.setText("----");

                //---- label10 ----
                label10.setText("User Logged In As");
                label10.setFont(label10.getFont().deriveFont(Font.BOLD|Font.ITALIC, label10.getFont().getSize() + 3f));

                //---- bLogOut ----
                bLogOut.setText("LOG OUT");
                bLogOut.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        bLogOut(e);
                    }
                });

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addGroup(panel1Layout.createParallelGroup()
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addGap(120, 120, 120)
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(label8)
                                                .addComponent(label6)
                                                .addComponent(label4)
                                                .addComponent(label2))
                                            .addGap(27, 27, 27)
                                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(tvMainPageUserName, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                                .addComponent(tvMainPageName, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                                .addComponent(tvMainPagePhoneNo, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                                .addComponent(tvMainPageAccessParm, GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)))
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGap(91, 91, 91)
                                            .addComponent(label10, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE))))
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addGap(74, 74, 74)
                                    .addComponent(label1, GroupLayout.PREFERRED_SIZE, 407, GroupLayout.PREFERRED_SIZE))
                                .addGroup(panel1Layout.createSequentialGroup()
                                    .addGap(245, 245, 245)
                                    .addComponent(bLogOut)))
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addComponent(label1, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(label10)
                            .addGap(12, 12, 12)
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label2)
                                .addComponent(tvMainPageName))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label4)
                                .addComponent(tvMainPageUserName))
                            .addGap(18, 18, 18)
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label6)
                                .addComponent(tvMainPagePhoneNo))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label8)
                                .addComponent(tvMainPageAccessParm))
                            .addGap(51, 51, 51)
                            .addComponent(bLogOut)
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
            }
            tabbedPaneAddingStuff.addTab("Main Page", panel1);

            //======== panelAddingStuff ========
            {

                //======== tabbedPaneAdding ========
                {
                    tabbedPaneAdding.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            tabbedPaneAddingStuffStateChanged(e);
                        }
                    });

                    //======== CatagoryPanel ========
                    {

                        //======== panel6 ========
                        {

                            //---- label11 ----
                            label11.setText("Category");
                            label11.setFont(label11.getFont().deriveFont(label11.getFont().getStyle() | Font.BOLD, label11.getFont().getSize() + 4f));

                            //---- label12 ----
                            label12.setText("Name Of the Category :*");

                            //---- label13 ----
                            label13.setText("Description :");

                            //======== scrollPane3 ========
                            {
                                scrollPane3.setViewportView(etCategoryDescription);
                            }

                            //---- button2 ----
                            button2.setText("Add");
                            button2.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bAddCategory(e);
                                }
                            });

                            //---- label14 ----
                            label14.setText("Delete Category");
                            label14.setFont(label14.getFont().deriveFont(label14.getFont().getStyle() | Font.BOLD, label14.getFont().getSize() + 3f));

                            //---- bDeleteCategory ----
                            bDeleteCategory.setText("Delete");
                            bDeleteCategory.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bDeleteCategory(e);
                                }
                            });

                            //---- label16 ----
                            label16.setText("Add Category");
                            label16.setFont(label16.getFont().deriveFont(label16.getFont().getStyle() | Font.BOLD, label16.getFont().getSize() + 4f));

                            //---- label17 ----
                            label17.setText("Display All Category ");
                            label17.setFont(label17.getFont().deriveFont(label17.getFont().getStyle() | Font.BOLD, label17.getFont().getSize() + 4f));

                            //======== scrollPane1 ========
                            {

                                //---- CategoryTable ----
                                CategoryTable.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        mouseClickedCategorytabel(e);
                                    }
                                });
                                scrollPane1.setViewportView(CategoryTable);
                            }

                            //---- label3 ----
                            label3.setFont(label3.getFont().deriveFont(label3.getFont().getStyle() | Font.BOLD, label3.getFont().getSize() + 2f));
                            label3.setHorizontalAlignment(SwingConstants.RIGHT);

                            //---- tvDeletecategorylabel ----
                            tvDeletecategorylabel.setText(".....................");
                            tvDeletecategorylabel.setFont(tvDeletecategorylabel.getFont().deriveFont(tvDeletecategorylabel.getFont().getStyle() | Font.BOLD, tvDeletecategorylabel.getFont().getSize() + 2f));
                            tvDeletecategorylabel.setHorizontalAlignment(SwingConstants.RIGHT);

                            //---- bDelete ----
                            bDelete.setText("DELETE ALL");
                            bDelete.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bDeleteALL(e);
                                }
                            });

                            GroupLayout panel6Layout = new GroupLayout(panel6);
                            panel6.setLayout(panel6Layout);
                            panel6Layout.setHorizontalGroup(
                                panel6Layout.createParallelGroup()
                                    .addGroup(panel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel6Layout.createParallelGroup()
                                            .addComponent(separator3, GroupLayout.DEFAULT_SIZE, 1374, Short.MAX_VALUE)
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(panel6Layout.createParallelGroup()
                                                    .addComponent(label17)
                                                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 562, GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 806, Short.MAX_VALUE))
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addComponent(label14)
                                                .addGap(115, 115, 115)
                                                .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addComponent(label3, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(tvDeletecategorylabel))
                                                .addGap(18, 18, 18)
                                                .addComponent(bDeleteCategory)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bDelete)
                                                .addContainerGap(860, Short.MAX_VALUE))))
                                    .addGroup(panel6Layout.createSequentialGroup()
                                        .addGroup(panel6Layout.createParallelGroup()
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(panel6Layout.createParallelGroup()
                                                    .addGroup(panel6Layout.createSequentialGroup()
                                                        .addGap(237, 237, 237)
                                                        .addComponent(label11))
                                                    .addComponent(separator2, GroupLayout.DEFAULT_SIZE, 1368, Short.MAX_VALUE)
                                                    .addGroup(panel6Layout.createSequentialGroup()
                                                        .addGroup(panel6Layout.createParallelGroup()
                                                            .addGroup(panel6Layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(label16)
                                                                .addGap(70, 70, 70))
                                                            .addGroup(GroupLayout.Alignment.TRAILING, panel6Layout.createSequentialGroup()
                                                                .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(label12)
                                                                    .addComponent(label13))
                                                                .addGap(18, 18, 18)))
                                                        .addGroup(panel6Layout.createParallelGroup()
                                                            .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 308, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(etCategoryAddName, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)))))
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addGap(268, 268, 268)
                                                .addComponent(button2)
                                                .addGap(0, 1054, Short.MAX_VALUE))
                                            .addComponent(separator1, GroupLayout.DEFAULT_SIZE, 1374, Short.MAX_VALUE))
                                        .addContainerGap())
                            );
                            panel6Layout.setVerticalGroup(
                                panel6Layout.createParallelGroup()
                                    .addGroup(panel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(label11)
                                        .addGap(11, 11, 11)
                                        .addComponent(separator2, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
                                        .addGap(7, 7, 7)
                                        .addComponent(label16)
                                        .addGap(46, 46, 46)
                                        .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(etCategoryAddName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label12))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(panel6Layout.createParallelGroup()
                                            .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label13))
                                        .addGap(18, 18, 18)
                                        .addComponent(button2)
                                        .addGap(28, 28, 28)
                                        .addComponent(separator3, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(label17)
                                        .addGap(18, 18, 18)
                                        .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 233, GroupLayout.PREFERRED_SIZE)
                                        .addGap(31, 31, 31)
                                        .addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(panel6Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label14)
                                            .addComponent(label3)
                                            .addComponent(bDeleteCategory)
                                            .addComponent(tvDeletecategorylabel)
                                            .addComponent(bDelete))
                                        .addContainerGap(270, Short.MAX_VALUE))
                            );
                        }
                        CatagoryPanel.setViewportView(panel6);
                    }
                    tabbedPaneAdding.addTab("Category", CatagoryPanel);

                    //======== scrollPane5 ========
                    {

                        //======== panel7 ========
                        {

                            //---- label21 ----
                            label21.setText("Add Item");
                            label21.setFont(label21.getFont().deriveFont(label21.getFont().getStyle() | Font.BOLD, label21.getFont().getSize() + 3f));

                            //---- label22 ----
                            label22.setText("Item Name* :");

                            //---- label23 ----
                            label23.setText("Category Name*:");

                            //---- comboBoxAddItemCategoryName ----
                            comboBoxAddItemCategoryName.setVisible(false);
                            comboBoxAddItemCategoryName.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    comboBoxAddItemCategoryNameActionPerformed(e);
                                }
                            });

                            //---- bAddItemCatagoryChoose ----
                            bAddItemCatagoryChoose.setText("+");
                            bAddItemCatagoryChoose.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    onClickPLUS(e);
                                }
                            });

                            //---- button4 ----
                            button4.setText("Save");
                            button4.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bItemSave(e);
                                }
                            });

                            //---- bAddItemRefresh ----
                            bAddItemRefresh.setText("Refresh List");
                            bAddItemRefresh.setVisible(false);
                            bAddItemRefresh.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bAddItemRefresh(e);
                                }
                            });

                            //---- label24 ----
                            label24.setText("Price :");

                            //---- label26 ----
                            label26.setText("Quantity:");

                            //---- comboBoxAddItemQuantityUnits ----
                            comboBoxAddItemQuantityUnits.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    comboBoxAddItemQuantityUnitsActionPerformed(e);
                                }
                            });

                            //---- label27 ----
                            label27.setText("Remark:-");

                            //======== scrollPane6 ========
                            {
                                scrollPane6.setViewportView(tvAdditemDescription);
                            }

                            //======== scrollPane7 ========
                            {

                                //---- JTabelItemAdd ----
                                JTabelItemAdd.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        mouseClickedItemTabel(e);
                                    }
                                });
                                scrollPane7.setViewportView(JTabelItemAdd);
                            }

                            //---- label18 ----
                            label18.setText("Update Item");

                            //---- label33 ----
                            label33.setText("Item Name:-");

                            //---- tvItemUpdateItemName ----
                            tvItemUpdateItemName.setText("text");

                            //---- label37 ----
                            label37.setText("Quantity Add:*-");

                            //---- label39 ----
                            label39.setText("Price:-");

                            //---- bItemUpdate ----
                            bItemUpdate.setText("Update");
                            bItemUpdate.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bItemUpdateItems(e);
                                }
                            });

                            //---- separator4 ----
                            separator4.setOrientation(SwingConstants.VERTICAL);

                            //---- separator5 ----
                            separator5.setOrientation(SwingConstants.VERTICAL);

                            //---- label41 ----
                            label41.setText("Remark:-");

                            //======== scrollPane14 ========
                            {
                                scrollPane14.setViewportView(etItemUpdateRemark);
                            }

                            //---- tvItemUpdateQtyUnit ----
                            tvItemUpdateQtyUnit.setText("text");

                            //======== scrollPane15 ========
                            {
                                scrollPane15.setViewportView(JtabelItemSpecifiItemDetails);
                            }

                            //---- label35 ----
                            label35.setText("Delete");
                            label35.setFont(label35.getFont().deriveFont(label35.getFont().getStyle() | Font.BOLD, label35.getFont().getSize() + 4f));

                            //---- tvItemDeleteitemName ----
                            tvItemDeleteitemName.setText("ItemName");

                            //---- button6 ----
                            button6.setText("Delete");
                            button6.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bItemDeleteItem(e);
                                }
                            });

                            //---- bItemRefreshTable ----
                            bItemRefreshTable.setText("Refresh");
                            bItemRefreshTable.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bItemRefreshTable(e);
                                }
                            });

                            GroupLayout panel7Layout = new GroupLayout(panel7);
                            panel7.setLayout(panel7Layout);
                            panel7Layout.setHorizontalGroup(
                                panel7Layout.createParallelGroup()
                                    .addGroup(panel7Layout.createSequentialGroup()
                                        .addGroup(panel7Layout.createParallelGroup()
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addGap(256, 256, 256)
                                                .addComponent(label21)
                                                .addGap(488, 488, 488)
                                                .addComponent(label18))
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addGap(85, 85, 85)
                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addComponent(label22)
                                                    .addComponent(label23)
                                                    .addComponent(label24)
                                                    .addComponent(label26)
                                                    .addComponent(label27))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(panel7Layout.createParallelGroup()
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addComponent(bAddItemCatagoryChoose)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(comboBoxAddItemCategoryName, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(12, 12, 12)
                                                        .addComponent(bAddItemRefresh)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 313, Short.MAX_VALUE)
                                                        .addComponent(separator5, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(41, 41, 41))
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addGroup(panel7Layout.createParallelGroup()
                                                            .addGroup(panel7Layout.createSequentialGroup()
                                                                .addComponent(tvAdditemNormalPrice, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                            .addGroup(panel7Layout.createSequentialGroup()
                                                                .addComponent(tvAdditemName, GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)))
                                                        .addGap(61, 61, 61))
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addGroup(panel7Layout.createParallelGroup()
                                                            .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(panel7Layout.createSequentialGroup()
                                                                .addComponent(tvAddItemQuantity, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(comboBoxAddItemQuantityUnits, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(panel7Layout.createSequentialGroup()
                                                                .addGap(77, 77, 77)
                                                                .addComponent(button4)))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)))
                                                .addGroup(panel7Layout.createParallelGroup()
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                                                        .addComponent(separator4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(44, 44, 44)
                                                        .addGroup(panel7Layout.createParallelGroup()
                                                            .addGroup(GroupLayout.Alignment.TRAILING, panel7Layout.createSequentialGroup()
                                                                .addComponent(bItemUpdate)
                                                                .addGap(167, 167, 167))
                                                            .addGroup(GroupLayout.Alignment.TRAILING, panel7Layout.createSequentialGroup()
                                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(label33)
                                                                    .addComponent(label39)
                                                                    .addComponent(label37)
                                                                    .addComponent(label41))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                                            .addComponent(etItemUpdatePrice, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                                                                            .addComponent(etItemUpdateQtyAdd, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE))
                                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(tvItemUpdateQtyUnit, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                                    .addComponent(scrollPane14, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                                                                    .addComponent(tvItemUpdateItemName, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)))))
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addGap(84, 84, 84)
                                                        .addComponent(label35)
                                                        .addGap(0, 305, Short.MAX_VALUE)))))
                                        .addContainerGap(429, Short.MAX_VALUE))
                                    .addGroup(GroupLayout.Alignment.TRAILING, panel7Layout.createSequentialGroup()
                                        .addGap(0, 726, Short.MAX_VALUE)
                                        .addComponent(tvItemDeleteitemName)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(button6)
                                        .addGap(659, 659, 659))
                                    .addGroup(panel7Layout.createSequentialGroup()
                                        .addGroup(panel7Layout.createParallelGroup()
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(panel7Layout.createParallelGroup()
                                                    .addComponent(scrollPane15, GroupLayout.PREFERRED_SIZE, 1123, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(scrollPane7, GroupLayout.PREFERRED_SIZE, 1306, GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addGap(559, 559, 559)
                                                .addComponent(bItemRefreshTable)))
                                        .addGap(0, 198, Short.MAX_VALUE))
                            );
                            panel7Layout.setVerticalGroup(
                                panel7Layout.createParallelGroup()
                                    .addGroup(panel7Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel7Layout.createParallelGroup()
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label21)
                                                    .addComponent(label18))
                                                .addGap(33, 33, 33)
                                                .addGroup(panel7Layout.createParallelGroup()
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label22)
                                                            .addComponent(tvAdditemName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addGap(2, 2, 2))
                                                    .addGroup(GroupLayout.Alignment.TRAILING, panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label33)
                                                        .addComponent(tvItemUpdateItemName)))
                                                .addGroup(panel7Layout.createParallelGroup()
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addGroup(panel7Layout.createParallelGroup()
                                                            .addGroup(panel7Layout.createSequentialGroup()
                                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(label23)
                                                                    .addComponent(bAddItemCatagoryChoose)
                                                                    .addComponent(comboBoxAddItemCategoryName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18))
                                                            .addGroup(GroupLayout.Alignment.TRAILING, panel7Layout.createSequentialGroup()
                                                                .addGap(17, 17, 17)
                                                                .addComponent(separator5, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)))
                                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label24)
                                                            .addComponent(tvAdditemNormalPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(panel7Layout.createParallelGroup()
                                                            .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(label26)
                                                                .addComponent(tvAddItemQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                            .addComponent(comboBoxAddItemQuantityUnits, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addGap(34, 34, 34)
                                                        .addGroup(panel7Layout.createParallelGroup()
                                                            .addComponent(label27)
                                                            .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addComponent(button4)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(bItemRefreshTable)
                                                        .addGap(6, 6, 6))
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addGroup(panel7Layout.createParallelGroup()
                                                            .addComponent(bAddItemRefresh)
                                                            .addGroup(panel7Layout.createSequentialGroup()
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                    .addComponent(label37)
                                                                    .addComponent(etItemUpdateQtyAdd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(tvItemUpdateQtyUnit))))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label39)
                                                            .addComponent(etItemUpdatePrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label41)
                                                            .addComponent(scrollPane14, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(bItemUpdate)
                                                        .addGap(40, 40, 40)
                                                        .addComponent(label35)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(tvItemDeleteitemName)
                                                            .addComponent(button6))
                                                        .addGap(39, 39, 39))))
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addComponent(separator4, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                                                .addGap(103, 103, 103)))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(scrollPane7, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane15, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
                                        .addGap(316, 316, 316))
                            );
                        }
                        scrollPane5.setViewportView(panel7);
                    }
                    tabbedPaneAdding.addTab("Item", scrollPane5);
                }

                GroupLayout panelAddingStuffLayout = new GroupLayout(panelAddingStuff);
                panelAddingStuff.setLayout(panelAddingStuffLayout);
                panelAddingStuffLayout.setHorizontalGroup(
                    panelAddingStuffLayout.createParallelGroup()
                        .addComponent(tabbedPaneAdding, GroupLayout.DEFAULT_SIZE, 1397, Short.MAX_VALUE)
                );
                panelAddingStuffLayout.setVerticalGroup(
                    panelAddingStuffLayout.createParallelGroup()
                        .addComponent(tabbedPaneAdding, GroupLayout.DEFAULT_SIZE, 977, Short.MAX_VALUE)
                );
            }
            tabbedPaneAddingStuff.addTab("Adding Stuff", panelAddingStuff);

            //======== panel3 ========
            {

                //======== tabbedPane ========
                {
                    tabbedPane.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            tabbedPaneBillingSateChange(e);
                        }
                    });

                    //======== scrollPane4 ========
                    {

                        //======== panel5 ========
                        {

                            //---- button3 ----
                            button3.setText("New Bill");
                            button3.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bBillingNewBill(e);
                                }
                            });

                            //======== panelBillingMain ========
                            {
                                panelBillingMain.setVisible(false);

                                //---- label20 ----
                                label20.setText("Item Name :*");

                                //---- comboBoxBillingItemName ----
                                comboBoxBillingItemName.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        comboBoxBillingItemName(e);
                                    }
                                });

                                //---- label28 ----
                                label28.setText("Category Name:*");

                                //---- comboBoxBillingCategoryName ----
                                comboBoxBillingCategoryName.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        comboBoxBillingCategoryName(e);
                                    }
                                });

                                //---- label29 ----
                                label29.setText("Available Quantity :");

                                //---- tvBillingAvailQty ----
                                tvBillingAvailQty.setText("..........");

                                //---- label31 ----
                                label31.setText("Quantity :*");

                                //---- bBillingAdditemToBill ----
                                bBillingAdditemToBill.setText("Add Item to Bill");
                                bBillingAdditemToBill.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bBillingAdditemToBill(e);
                                    }
                                });

                                //---- bREFRESH_Billing_CategoryName ----
                                bREFRESH_Billing_CategoryName.setText("REFRESH");
                                bREFRESH_Billing_CategoryName.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bREFRESH_Billing_CategoryName(e);
                                    }
                                });

                                //---- bREFRESH_Billing_ItemName ----
                                bREFRESH_Billing_ItemName.setText("REFRESH");
                                bREFRESH_Billing_ItemName.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bREFRESH_Billing_ItemName(e);
                                    }
                                });

                                //======== scrollPane10 ========
                                {

                                    //---- JTabelBillingItem ----
                                    JTabelBillingItem.addMouseListener(new MouseAdapter() {
                                        @Override
                                        public void mouseClicked(MouseEvent e) {
                                            JTabelBillingItemMouseClicked(e);
                                        }
                                    });
                                    scrollPane10.setViewportView(JTabelBillingItem);
                                }

                                //---- bBillingPrintBill ----
                                bBillingPrintBill.setText("Print Bill");
                                bBillingPrintBill.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bBillingPrintBill(e);
                                    }
                                });

                                //---- tvBillingBillNo ----
                                tvBillingBillNo.setText("Billing Id");
                                tvBillingBillNo.setFont(tvBillingBillNo.getFont().deriveFont(tvBillingBillNo.getFont().getStyle() | Font.BOLD, tvBillingBillNo.getFont().getSize() + 7f));

                                //---- label15 ----
                                label15.setText("Total Amount :-");

                                //---- tvBillingTotalAmount ----
                                tvBillingTotalAmount.setText("00.00");
                                tvBillingTotalAmount.setFont(tvBillingTotalAmount.getFont().deriveFont(tvBillingTotalAmount.getFont().getStyle() | Font.BOLD, tvBillingTotalAmount.getFont().getSize() + 3f));

                                //---- label19 ----
                                label19.setText("Gcd");

                                //======== panelBillingUpdate ========
                                {
                                    panelBillingUpdate.setBackground(new Color(153, 204, 255));
                                    panelBillingUpdate.setVisible(false);

                                    //---- bBillingDelete ----
                                    bBillingDelete.setText("Delete The Item");
                                    bBillingDelete.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            bBillingDelete(e);
                                        }
                                    });

                                    //---- bBillingUpdateQty ----
                                    bBillingUpdateQty.setText("Update Qty");
                                    bBillingUpdateQty.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            bBillingUpdateQty(e);
                                        }
                                    });

                                    //---- tvBillingItemName ----
                                    tvBillingItemName.setText("text");
                                    tvBillingItemName.setHorizontalAlignment(SwingConstants.LEFT);
                                    tvBillingItemName.setFont(tvBillingItemName.getFont().deriveFont(tvBillingItemName.getFont().getStyle() | Font.BOLD, tvBillingItemName.getFont().getSize() + 3f));

                                    //---- tvBillingUpdateQtyAvaliable ----
                                    tvBillingUpdateQtyAvaliable.setText("text");
                                    tvBillingUpdateQtyAvaliable.setFont(tvBillingUpdateQtyAvaliable.getFont().deriveFont(tvBillingUpdateQtyAvaliable.getFont().getStyle() | Font.BOLD, tvBillingUpdateQtyAvaliable.getFont().getSize() + 3f));

                                    //---- label43 ----
                                    label43.setText("Item Name:");

                                    //---- label44 ----
                                    label44.setText("Available Quantity:");

                                    //---- label47 ----
                                    label47.setText("UPDATE");
                                    label47.setFont(label47.getFont().deriveFont(label47.getFont().getStyle() | Font.BOLD, label47.getFont().getSize() + 3f));

                                    GroupLayout panelBillingUpdateLayout = new GroupLayout(panelBillingUpdate);
                                    panelBillingUpdate.setLayout(panelBillingUpdateLayout);
                                    panelBillingUpdateLayout.setHorizontalGroup(
                                        panelBillingUpdateLayout.createParallelGroup()
                                            .addGroup(panelBillingUpdateLayout.createSequentialGroup()
                                                .addGroup(panelBillingUpdateLayout.createParallelGroup()
                                                    .addGroup(panelBillingUpdateLayout.createSequentialGroup()
                                                        .addGap(160, 160, 160)
                                                        .addComponent(bBillingDelete))
                                                    .addGroup(panelBillingUpdateLayout.createSequentialGroup()
                                                        .addGap(14, 14, 14)
                                                        .addGroup(panelBillingUpdateLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(etBillingUpdate, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(panelBillingUpdateLayout.createSequentialGroup()
                                                                .addComponent(label43)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(tvBillingItemName, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(bBillingUpdateQty))
                                                    .addGroup(panelBillingUpdateLayout.createSequentialGroup()
                                                        .addGap(157, 157, 157)
                                                        .addComponent(label47, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(panelBillingUpdateLayout.createSequentialGroup()
                                                        .addGap(208, 208, 208)
                                                        .addComponent(label44)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(tvBillingUpdateQtyAvaliable, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)))
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    );
                                    panelBillingUpdateLayout.setVerticalGroup(
                                        panelBillingUpdateLayout.createParallelGroup()
                                            .addGroup(panelBillingUpdateLayout.createSequentialGroup()
                                                .addComponent(label47)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelBillingUpdateLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label43)
                                                    .addComponent(tvBillingItemName)
                                                    .addComponent(label44)
                                                    .addComponent(tvBillingUpdateQtyAvaliable))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(panelBillingUpdateLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(etBillingUpdate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(bBillingUpdateQty))
                                                .addGap(16, 16, 16)
                                                .addComponent(bBillingDelete, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
                                    );
                                }

                                //---- label45 ----
                                label45.setText("Customer Name:");

                                //---- label46 ----
                                label46.setText("Vehicle No:");

                                GroupLayout panelBillingMainLayout = new GroupLayout(panelBillingMain);
                                panelBillingMain.setLayout(panelBillingMainLayout);
                                panelBillingMainLayout.setHorizontalGroup(
                                    panelBillingMainLayout.createParallelGroup()
                                        .addGroup(panelBillingMainLayout.createSequentialGroup()
                                            .addGroup(panelBillingMainLayout.createParallelGroup()
                                                .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                            .addComponent(tvBillingBillNo)
                                                            .addGap(174, 174, 174)
                                                            .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                .addComponent(label20)
                                                                .addComponent(label31)))
                                                        .addComponent(label29)
                                                        .addComponent(label45)
                                                        .addComponent(label46)
                                                        .addComponent(label28))
                                                    .addGroup(panelBillingMainLayout.createParallelGroup()
                                                        .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                            .addGap(91, 91, 91)
                                                            .addComponent(bBillingPrintBill))
                                                        .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                            .addGap(12, 12, 12)
                                                            .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(etBillingCustomerName, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                                                .addComponent(etBillingVehicleNo, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                                                .addComponent(tvBillingAvailQty, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                                                .addComponent(etBillingQuantity, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(bBillingAdditemToBill, GroupLayout.PREFERRED_SIZE, 261, GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                                    .addComponent(comboBoxBillingCategoryName, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(bREFRESH_Billing_CategoryName, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE))
                                                                .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                                    .addComponent(comboBoxBillingItemName, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(bREFRESH_Billing_ItemName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                                                .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                    .addGap(296, 296, 296)
                                                    .addComponent(label15)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(tvBillingTotalAmount)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(label19))
                                                .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(scrollPane10, GroupLayout.PREFERRED_SIZE, 856, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(panelBillingUpdate, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)))
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                );
                                panelBillingMainLayout.setVerticalGroup(
                                    panelBillingMainLayout.createParallelGroup()
                                        .addGroup(panelBillingMainLayout.createSequentialGroup()
                                            .addContainerGap()
                                            .addGroup(panelBillingMainLayout.createParallelGroup()
                                                .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                    .addComponent(tvBillingBillNo)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                    .addGroup(panelBillingMainLayout.createParallelGroup()
                                                        .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                            .addGap(26, 26, 26)
                                                            .addComponent(label28)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(label20))
                                                        .addGroup(panelBillingMainLayout.createSequentialGroup()
                                                            .addGap(22, 22, 22)
                                                            .addGroup(panelBillingMainLayout.createParallelGroup()
                                                                .addComponent(comboBoxBillingCategoryName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(bREFRESH_Billing_CategoryName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                            .addGap(18, 18, 18)
                                                            .addGroup(panelBillingMainLayout.createParallelGroup()
                                                                .addComponent(comboBoxBillingItemName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(bREFRESH_Billing_ItemName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                                    .addGap(16, 16, 16)
                                                    .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label31)
                                                        .addComponent(etBillingQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label29)
                                                        .addComponent(tvBillingAvailQty))
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(bBillingAdditemToBill)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label45)
                                                        .addComponent(etBillingCustomerName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addGap(18, 18, 18)
                                                    .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label46)
                                                        .addComponent(etBillingVehicleNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addGap(16, 16, 16)))
                                            .addComponent(bBillingPrintBill)
                                            .addGap(13, 13, 13)
                                            .addGroup(panelBillingMainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label15)
                                                .addComponent(tvBillingTotalAmount)
                                                .addComponent(label19))
                                            .addGap(34, 34, 34)
                                            .addGroup(panelBillingMainLayout.createParallelGroup()
                                                .addComponent(scrollPane10, GroupLayout.PREFERRED_SIZE, 258, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(panelBillingUpdate, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE))
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                );
                            }

                            GroupLayout panel5Layout = new GroupLayout(panel5);
                            panel5.setLayout(panel5Layout);
                            panel5Layout.setHorizontalGroup(
                                panel5Layout.createParallelGroup()
                                    .addGroup(panel5Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel5Layout.createParallelGroup()
                                            .addComponent(panelBillingMain, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addComponent(button3, GroupLayout.PREFERRED_SIZE, 335, GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 1238, Short.MAX_VALUE))))
                            );
                            panel5Layout.setVerticalGroup(
                                panel5Layout.createParallelGroup()
                                    .addGroup(panel5Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(button3)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(panelBillingMain, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                            );
                        }
                        scrollPane4.setViewportView(panel5);
                    }
                    tabbedPane.addTab("Billing", scrollPane4);

                    //======== tabbedPaneRefund ========
                    {

                        //======== panelRefund ========
                        {

                            //---- label9 ----
                            label9.setText("Billing Id :*");

                            //======== scrollPane12 ========
                            {
                                scrollPane12.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        mouseClickedRefundBillingTabel(e);
                                    }
                                });

                                //---- JTableRefundBillAvailable ----
                                JTableRefundBillAvailable.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        mouseClickedRefundBillingTabel(e);
                                    }
                                });
                                scrollPane12.setViewportView(JTableRefundBillAvailable);
                            }

                            //======== scrollPane13 ========
                            {

                                //---- JTableRefundBillitemAvailable ----
                                JTableRefundBillitemAvailable.addMouseListener(new MouseAdapter() {
                                    @Override
                                    public void mouseClicked(MouseEvent e) {
                                        mouseClickedRefundBillingItemsTabel(e);
                                    }
                                });
                                scrollPane13.setViewportView(JTableRefundBillitemAvailable);
                            }

                            //---- label30 ----
                            label30.setText("New Quantity:*");

                            //---- button5 ----
                            button5.setText("Cancel The Whole Bill");
                            button5.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bRefundCancelTheWholeBill(e);
                                }
                            });

                            //---- bRefundBillItems ----
                            bRefundBillItems.setText("Update the Item Refund");
                            bRefundBillItems.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    onClickRefundBillItems(e);
                                }
                            });

                            //---- tvRefundQtyofItems ----
                            tvRefundQtyofItems.setText("Qty of Items");

                            //======== panelRefundINFO ========
                            {

                                //---- label7 ----
                                label7.setText("Initial Quantity:-");

                                //---- tvRefundIntialQty ----
                                tvRefundIntialQty.setText("...");

                                //---- label32 ----
                                label32.setText("Quantity Refunded:-");

                                //---- tvRefundQtyRefund ----
                                tvRefundQtyRefund.setText("...");

                                //---- label34 ----
                                label34.setText("Date of Refund:-");

                                //---- tvRefundDateofRefund ----
                                tvRefundDateofRefund.setText("...");

                                //---- label36 ----
                                label36.setText("Time of Refund:-");

                                //---- tvRefundTimeofRefund ----
                                tvRefundTimeofRefund.setText("...");

                                GroupLayout panelRefundINFOLayout = new GroupLayout(panelRefundINFO);
                                panelRefundINFO.setLayout(panelRefundINFOLayout);
                                panelRefundINFOLayout.setHorizontalGroup(
                                    panelRefundINFOLayout.createParallelGroup()
                                        .addGroup(panelRefundINFOLayout.createSequentialGroup()
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(panelRefundINFOLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(label36)
                                                .addComponent(label34)
                                                .addComponent(label32)
                                                .addComponent(label7))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panelRefundINFOLayout.createParallelGroup()
                                                .addComponent(tvRefundIntialQty)
                                                .addComponent(tvRefundQtyRefund)
                                                .addComponent(tvRefundDateofRefund)
                                                .addComponent(tvRefundTimeofRefund)))
                                );
                                panelRefundINFOLayout.setVerticalGroup(
                                    panelRefundINFOLayout.createParallelGroup()
                                        .addGroup(panelRefundINFOLayout.createSequentialGroup()
                                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(panelRefundINFOLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label7)
                                                .addComponent(tvRefundIntialQty))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panelRefundINFOLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label32)
                                                .addComponent(tvRefundQtyRefund))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panelRefundINFOLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label34)
                                                .addComponent(tvRefundDateofRefund))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(panelRefundINFOLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label36)
                                                .addComponent(tvRefundTimeofRefund)))
                                );
                            }

                            //---- label40 ----
                            label40.setText("Quantity :-");

                            GroupLayout panelRefundLayout = new GroupLayout(panelRefund);
                            panelRefund.setLayout(panelRefundLayout);
                            panelRefundLayout.setHorizontalGroup(
                                panelRefundLayout.createParallelGroup()
                                    .addGroup(panelRefundLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panelRefundLayout.createParallelGroup()
                                            .addComponent(scrollPane12, GroupLayout.PREFERRED_SIZE, 511, GroupLayout.PREFERRED_SIZE)
                                            .addGroup(panelRefundLayout.createSequentialGroup()
                                                .addGap(28, 28, 28)
                                                .addGroup(panelRefundLayout.createParallelGroup()
                                                    .addGroup(panelRefundLayout.createSequentialGroup()
                                                        .addComponent(button5)
                                                        .addGap(59, 59, 59)
                                                        .addComponent(bRefundBillItems))
                                                    .addGroup(panelRefundLayout.createSequentialGroup()
                                                        .addGroup(panelRefundLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(label30)
                                                            .addGroup(panelRefundLayout.createParallelGroup()
                                                                .addComponent(label40)
                                                                .addComponent(label9)))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(panelRefundLayout.createParallelGroup()
                                                            .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(panelRefundLayout.createSequentialGroup()
                                                                .addGroup(panelRefundLayout.createParallelGroup()
                                                                    .addComponent(tvRefundQtyofItems, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
                                                                    .addComponent(etRefundNewQuantity, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 384, Short.MAX_VALUE)
                                                                .addComponent(panelRefundINFO, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))))
                                        .addGap(97, 97, 97)
                                        .addComponent(scrollPane13, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addGap(110, 110, 110))
                            );
                            panelRefundLayout.setVerticalGroup(
                                panelRefundLayout.createParallelGroup()
                                    .addGroup(panelRefundLayout.createSequentialGroup()
                                        .addGroup(panelRefundLayout.createParallelGroup()
                                            .addGroup(panelRefundLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(scrollPane13, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panelRefundLayout.createSequentialGroup()
                                                .addGap(47, 47, 47)
                                                .addGroup(panelRefundLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label9)
                                                    .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(panelRefundLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label40)
                                                    .addComponent(tvRefundQtyofItems))
                                                .addGap(16, 16, 16)
                                                .addGroup(panelRefundLayout.createParallelGroup()
                                                    .addGroup(panelRefundLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label30)
                                                        .addComponent(etRefundNewQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(panelRefundINFO, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                                                .addGroup(panelRefundLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(button5)
                                                    .addComponent(bRefundBillItems))
                                                .addGap(103, 103, 103)
                                                .addComponent(scrollPane12, GroupLayout.PREFERRED_SIZE, 323, GroupLayout.PREFERRED_SIZE)))
                                        .addContainerGap(165, Short.MAX_VALUE))
                            );
                        }
                        tabbedPaneRefund.setViewportView(panelRefund);
                    }
                    tabbedPane.addTab("Refund", tabbedPaneRefund);
                }

                GroupLayout panel3Layout = new GroupLayout(panel3);
                panel3.setLayout(panel3Layout);
                panel3Layout.setHorizontalGroup(
                    panel3Layout.createParallelGroup()
                        .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1397, Short.MAX_VALUE)
                );
                panel3Layout.setVerticalGroup(
                    panel3Layout.createParallelGroup()
                        .addComponent(tabbedPane)
                );
            }
            tabbedPaneAddingStuff.addTab("Billing", panel3);

            //======== panel2 ========
            {

                //======== tabbedPaneAnalysis ========
                {

                    //======== panel9 ========
                    {

                        //======== scrollPane9 ========
                        {

                            //======== panel10 ========
                            {

                                //---- label25 ----
                                label25.setText("Worker:-");

                                //---- comboBoxWorkerAnalysisName ----
                                comboBoxWorkerAnalysisName.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        comboBoxWorkerAnalysisName(e);
                                    }
                                });

                                //======== scrollPane16 ========
                                {
                                    scrollPane16.setViewportView(JTableWorkerAnalysis);
                                }

                                //---- bWorkerAnalysisCHECK ----
                                bWorkerAnalysisCHECK.setText("Check");
                                bWorkerAnalysisCHECK.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bWorkerAnalysisCHECK(e);
                                    }
                                });

                                //---- label38 ----
                                label38.setText("Total Bill:-");

                                //---- tvWorkerAnalysisTotal ----
                                tvWorkerAnalysisTotal.setText("0.00");
                                tvWorkerAnalysisTotal.setFont(tvWorkerAnalysisTotal.getFont().deriveFont(tvWorkerAnalysisTotal.getFont().getStyle() | Font.BOLD, tvWorkerAnalysisTotal.getFont().getSize() + 1f));
                                tvWorkerAnalysisTotal.setHorizontalAlignment(SwingConstants.CENTER);

                                //---- label42 ----
                                label42.setText("Gcd");

                                GroupLayout panel10Layout = new GroupLayout(panel10);
                                panel10.setLayout(panel10Layout);
                                panel10Layout.setHorizontalGroup(
                                    panel10Layout.createParallelGroup()
                                        .addGroup(panel10Layout.createSequentialGroup()
                                            .addGroup(panel10Layout.createParallelGroup()
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addGap(136, 136, 136)
                                                    .addComponent(label38)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(tvWorkerAnalysisTotal, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(label42))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addGap(76, 76, 76)
                                                    .addComponent(label25)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(comboBoxWorkerAnalysisName, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(scrollPane16, GroupLayout.PREFERRED_SIZE, 938, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(panel10Layout.createSequentialGroup()
                                                    .addGap(182, 182, 182)
                                                    .addComponent(bWorkerAnalysisCHECK)))
                                            .addContainerGap(449, Short.MAX_VALUE))
                                );
                                panel10Layout.setVerticalGroup(
                                    panel10Layout.createParallelGroup()
                                        .addGroup(panel10Layout.createSequentialGroup()
                                            .addGap(62, 62, 62)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(label25)
                                                .addComponent(comboBoxWorkerAnalysisName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                            .addGap(29, 29, 29)
                                            .addComponent(bWorkerAnalysisCHECK)
                                            .addGap(18, 18, 18)
                                            .addGroup(panel10Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label38)
                                                .addComponent(label42)
                                                .addComponent(tvWorkerAnalysisTotal))
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(scrollPane16, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE)
                                            .addContainerGap(433, Short.MAX_VALUE))
                                );
                            }
                            scrollPane9.setViewportView(panel10);
                        }

                        GroupLayout panel9Layout = new GroupLayout(panel9);
                        panel9.setLayout(panel9Layout);
                        panel9Layout.setHorizontalGroup(
                            panel9Layout.createParallelGroup()
                                .addComponent(scrollPane9)
                        );
                        panel9Layout.setVerticalGroup(
                            panel9Layout.createParallelGroup()
                                .addGroup(panel9Layout.createSequentialGroup()
                                    .addComponent(scrollPane9)
                                    .addContainerGap())
                        );
                    }
                    tabbedPaneAnalysis.addTab("Worker", panel9);

                    //======== panel11 ========
                    {

                        //---- label5 ----
                        label5.setText("Item Name:-");

                        GroupLayout panel11Layout = new GroupLayout(panel11);
                        panel11.setLayout(panel11Layout);
                        panel11Layout.setHorizontalGroup(
                            panel11Layout.createParallelGroup()
                                .addGroup(panel11Layout.createSequentialGroup()
                                    .addGap(79, 79, 79)
                                    .addComponent(label5)
                                    .addContainerGap(1249, Short.MAX_VALUE))
                        );
                        panel11Layout.setVerticalGroup(
                            panel11Layout.createParallelGroup()
                                .addGroup(panel11Layout.createSequentialGroup()
                                    .addGap(71, 71, 71)
                                    .addComponent(label5)
                                    .addContainerGap(866, Short.MAX_VALUE))
                        );
                    }
                    tabbedPaneAnalysis.addTab("Item", panel11);
                }

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addComponent(tabbedPaneAnalysis)
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addComponent(tabbedPaneAnalysis)
                );
            }
            tabbedPaneAddingStuff.addTab("Analysis", panel2);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPaneAddingStuff, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPaneAddingStuff, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Hitesh Verma
    private JTabbedPane tabbedPaneAddingStuff;
    private JPanel panel1;
    private JLabel label1;
    private JLabel label2;
    private JLabel tvMainPageName;
    private JLabel label4;
    private JLabel tvMainPageUserName;
    private JLabel label6;
    private JLabel tvMainPagePhoneNo;
    private JLabel label8;
    private JLabel tvMainPageAccessParm;
    private JLabel label10;
    private JButton bLogOut;
    private JPanel panelAddingStuff;
    private JTabbedPane tabbedPaneAdding;
    private JScrollPane CatagoryPanel;
    private JPanel panel6;
    private JLabel label11;
    private JLabel label12;
    private JTextField etCategoryAddName;
    private JLabel label13;
    private JScrollPane scrollPane3;
    private JTextArea etCategoryDescription;
    private JSeparator separator1;
    private JButton button2;
    private JLabel label14;
    private JButton bDeleteCategory;
    private JSeparator separator2;
    private JLabel label16;
    private JSeparator separator3;
    private JLabel label17;
    private JScrollPane scrollPane1;
    private JTable CategoryTable;
    private JLabel label3;
    private JLabel tvDeletecategorylabel;
    private JButton bDelete;
    private JScrollPane scrollPane5;
    private JPanel panel7;
    private JLabel label21;
    private JLabel label22;
    private JTextField tvAdditemName;
    private JLabel label23;
    private JComboBox comboBoxAddItemCategoryName;
    private JButton bAddItemCatagoryChoose;
    private JButton button4;
    private JButton bAddItemRefresh;
    private JLabel label24;
    private JTextField tvAdditemNormalPrice;
    private JLabel label26;
    private JTextField tvAddItemQuantity;
    private JComboBox comboBoxAddItemQuantityUnits;
    private JLabel label27;
    private JScrollPane scrollPane6;
    private JTextArea tvAdditemDescription;
    private JScrollPane scrollPane7;
    private JTable JTabelItemAdd;
    private JLabel label18;
    private JLabel label33;
    private JLabel tvItemUpdateItemName;
    private JLabel label37;
    private JLabel label39;
    private JButton bItemUpdate;
    private JSeparator separator4;
    private JSeparator separator5;
    private JTextField etItemUpdateQtyAdd;
    private JTextField etItemUpdatePrice;
    private JLabel label41;
    private JScrollPane scrollPane14;
    private JTextArea etItemUpdateRemark;
    private JLabel tvItemUpdateQtyUnit;
    private JScrollPane scrollPane15;
    private JTable JtabelItemSpecifiItemDetails;
    private JLabel label35;
    private JLabel tvItemDeleteitemName;
    private JButton button6;
    private JButton bItemRefreshTable;
    private JPanel panel3;
    private JTabbedPane tabbedPane;
    private JScrollPane scrollPane4;
    private JPanel panel5;
    private JButton button3;
    private JPanel panelBillingMain;
    private JLabel label20;
    private JComboBox comboBoxBillingItemName;
    private JLabel label28;
    private JComboBox comboBoxBillingCategoryName;
    private JLabel label29;
    private JLabel tvBillingAvailQty;
    private JLabel label31;
    private JTextField etBillingQuantity;
    private JButton bBillingAdditemToBill;
    private JButton bREFRESH_Billing_CategoryName;
    private JButton bREFRESH_Billing_ItemName;
    private JScrollPane scrollPane10;
    private JTable JTabelBillingItem;
    private JButton bBillingPrintBill;
    private JLabel tvBillingBillNo;
    private JLabel label15;
    private JLabel tvBillingTotalAmount;
    private JLabel label19;
    private JPanel panelBillingUpdate;
    private JButton bBillingDelete;
    private JButton bBillingUpdateQty;
    private JTextField etBillingUpdate;
    private JLabel tvBillingItemName;
    private JLabel tvBillingUpdateQtyAvaliable;
    private JLabel label43;
    private JLabel label44;
    private JLabel label47;
    private JLabel label45;
    private JTextField etBillingCustomerName;
    private JLabel label46;
    private JTextField etBillingVehicleNo;
    private JScrollPane tabbedPaneRefund;
    private JPanel panelRefund;
    private JLabel label9;
    private JTextField textField1;
    private JScrollPane scrollPane12;
    private JTable JTableRefundBillAvailable;
    private JScrollPane scrollPane13;
    private JTable JTableRefundBillitemAvailable;
    private JLabel label30;
    private JTextField etRefundNewQuantity;
    private JButton button5;
    private JButton bRefundBillItems;
    private JLabel tvRefundQtyofItems;
    private JPanel panelRefundINFO;
    private JLabel label7;
    private JLabel tvRefundIntialQty;
    private JLabel label32;
    private JLabel tvRefundQtyRefund;
    private JLabel label34;
    private JLabel tvRefundDateofRefund;
    private JLabel label36;
    private JLabel tvRefundTimeofRefund;
    private JLabel label40;
    private JPanel panel2;
    private JTabbedPane tabbedPaneAnalysis;
    private JPanel panel9;
    private JScrollPane scrollPane9;
    private JPanel panel10;
    private JLabel label25;
    private JComboBox comboBoxWorkerAnalysisName;
    private JScrollPane scrollPane16;
    private JTable JTableWorkerAnalysis;
    private JButton bWorkerAnalysisCHECK;
    private JLabel label38;
    private JLabel tvWorkerAnalysisTotal;
    private JLabel label42;
    private JPanel panel11;
    private JLabel label5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
