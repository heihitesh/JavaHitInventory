/*
 * Created by JFormDesigner on Sat Jun 18 15:28:59 IST 2016
 */

package test.com;

import java.beans.*;
import test.com.DataBaseServices.dbsBilling;
import test.com.DataBaseServices.dbsCategory;
import test.com.DataBaseServices.dbsItem;
import test.com.DataBaseServices.dbsBillingItemTabel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.event.*;
import javax.swing.table.*;

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

        //setting Up the Units
        comboBoxAddItemQuantityUnits.addItem("KG");
        comboBoxAddItemQuantityUnits.addItem("g");
        comboBoxAddItemQuantityUnits.addItem("L");
        comboBoxAddItemQuantityUnits.addItem("ml");

        //settingUpTheViewAccording to the ACCESS PARAMETER
        setAccordingToAccessParam();
    }

    private void setAccordingToAccessParam() {
        switch (accessParam){
            case "manager":
                System.out.println("manager");
                tabbedPaneAdding.setVisible(true);
                break;

            case "worker":
                System.out.println("worker");
                tabbedPaneAdding.setVisible(false);
                break;
        }
    }

    private void bMainPageLogOut(ActionEvent e) {
        // TODO add your code here
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

                break;

            case "Billing":


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
                        dbsItem = new dbsItem(myConn, Name, JTabelItemAdd);
                        dbsItem.refreshItemTabel();

                        break;

                    case "Category":
                        System.out.println("Category");
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
                dbsBilling = new dbsBilling(myConn,Name);
                dbsBillingItemTabel = new dbsBillingItemTabel(myConn,JTabelBillingItem);  //creating a new class for handelling items selected

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
        Float normal_price = Float.valueOf(tvAdditemNormalPrice.getText());
        Float selling_price = Float.valueOf(tvAddItemSellingPrice.getText());
        int quantity = Integer.parseInt(tvAddItemQuantity.getText());
        String itemName = String.valueOf(tvAdditemName.getText());
        String itemDesc = String.valueOf(tvAdditemDescription.getText());

        bAddItemCatagoryChoose.setVisible(true);
        comboBoxAddItemCategoryName.setVisible(false);
        bAddItemRefresh.setVisible(false);
        comboBoxAddItemCategoryName.removeAllItems();

        setValuetoNullinAddItem();


        dbsItem.setSalesTabel(normal_price, selling_price);  //setting Data in Sales tabel

        dbsItem.setQuantityTabel(quantity, quantityItemAdded);                            //Setting Data in Quantity Tabel

       dbsItem.setItemTabel(categoryIDItemAdded, dbsItem.getSalesId(),
               dbsItem.getQuantityId(), itemName
               , itemDesc);  //Setting Data in ItemTabel

        dbsItem.refreshItemTabel();

    }

    private void setValuetoNullinAddItem() {
        tvAdditemName.setText("");
        tvAdditemNormalPrice.setText("");
        tvAddItemSellingPrice.setText("");
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
        System.out.println(model.getValueAt(i, 1).toString());
    }

//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

//------------------------------BILLING TABEL --------------------------------------------------------------------------

    String category_name;
    String item_name;
    int quantity;
    String quantity_unit;

    private void comboBoxBillingItemName(ActionEvent e) {
        item_name = String.valueOf(((JComboBox) e.getSource()).getSelectedItem());
        HashMap<String,String> myMap = dbsBilling.getAvailableQuantity(item_name);
        quantity = Integer.parseInt(myMap.getOrDefault("quantity","1")); //used because it was passing a nulll value
        quantity_unit = myMap.getOrDefault("quantity_unit","...");
        System.out.println("Hei" + quantity +quantity_unit);
        tvBillingAvailQty.setText(quantity+" " +quantity_unit);

    }

    private void comboBoxBillingCategoryName(ActionEvent e) {
        // TODO add your code here
        category_name = String.valueOf(((JComboBox) e.getSource()).getSelectedItem());
        System.out.println("category_name" + category_name);
        comboBoxBillingItemName.removeAllItems();
        dbsBilling.createItemList(comboBoxBillingItemName,category_name);
    }

    private void bBillingNewBill(ActionEvent e) {
        // TODO add your code here
        String description = etBillingDescription.getText();
        dbsBilling.setBillingTabel(description,Name);
    }

    private void bBillingDone(ActionEvent e) {
        int quantity_entered = Integer.parseInt(etBillingQuantity.getText());
        if( quantity >= quantity_entered && quantity_entered >=1){
            int billing_id = -1;


            String description = etBillingDescription.getText();

            HashMap<String, Integer> myMap = dbsBilling.getItemIDandSalesInfo(item_name);
            int item_id = myMap.getOrDefault("item_id",-1); //used because it was passing a nulll value
            int selling_price = myMap.getOrDefault("selling_price",-1); //used because it was passing a nulll value
            int normal_price = myMap.getOrDefault("normal_price",-1); //used because it was passing a nulll value

            billing_id = dbsBilling.getBillingTabelID();



            dbsBillingItemTabel.setBillingItemTabel(billing_id,item_id,quantity_entered,
                    quantity_unit,item_name,selling_price,normal_price);

            dbsBilling.updateQuantityTabel(item_id,quantity-quantity_entered);

            ///For tabel
            dbsBillingItemTabel.refreshBillingItemTabel(billing_id);
            System.out.println(item_id);

        }else {
            infoError("Please Enter a Valid Value \n Entered Value is More than Availabe","Try Again");
        }
        // TODO add your code here
    }

    private void bPLUS_Billing_CategoryName(ActionEvent e) {
        bPLUSBillingCategoryName.setVisible(false);
        comboBoxBillingCategoryName.setVisible(true);
        bREFRESH_Billing_CategoryName.setVisible(true);

        dbsBilling.createCategoryList(comboBoxBillingCategoryName);
        // TODO add your code here
    }

    private void bREFRESH_Billing_CategoryName(ActionEvent e) {
        // TODO add your code here
        // TODO add your code here
        comboBoxBillingCategoryName.removeAllItems();
        dbsBilling.createCategoryList(comboBoxBillingCategoryName);
    }

    private void bPLUS_Billing_ItemName(ActionEvent e) {
        bPLUS_Billing_ItemName.setVisible(false);
        comboBoxBillingItemName.setVisible(true);
        bREFRESH_Billing_ItemName.setVisible(true);
        dbsBilling.createItemList(comboBoxBillingItemName,category_name);
        // TODO add your code here
    }

    private void bREFRESH_Billing_ItemName(ActionEvent e) {
        // TODO add your code here
        comboBoxBillingItemName.removeAllItems();
        dbsBilling.createItemList(comboBoxBillingItemName,category_name);
    }

    private void bDeleteALL(ActionEvent e) {
        // TODO add your code heretry
       //
    }

    private void JtableBillingItems(PropertyChangeEvent e) {
        // TODO add your code here
    }

    private void bBillingPrintBill(ActionEvent e) {

        // TODO add your code here
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
        button1 = new JButton();
        panelAddingStuff = new JPanel();
        tabbedPaneAdding = new JTabbedPane();
        panel4 = new JPanel();
        scrollPane2 = new JScrollPane();
        tree1 = new JTree();
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
        label25 = new JLabel();
        tvAdditemNormalPrice = new JTextField();
        tvAddItemSellingPrice = new JTextField();
        label26 = new JLabel();
        tvAddItemQuantity = new JTextField();
        comboBoxAddItemQuantityUnits = new JComboBox();
        label27 = new JLabel();
        scrollPane6 = new JScrollPane();
        tvAdditemDescription = new JTextArea();
        scrollPane7 = new JScrollPane();
        JTabelItemAdd = new JTable();
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
        panel3 = new JPanel();
        tabbedPane3 = new JTabbedPane();
        scrollPane8 = new JScrollPane();
        scrollPane4 = new JScrollPane();
        panel5 = new JPanel();
        label19 = new JLabel();
        label20 = new JLabel();
        comboBoxBillingItemName = new JComboBox();
        label28 = new JLabel();
        comboBoxBillingCategoryName = new JComboBox();
        label29 = new JLabel();
        tvBillingAvailQty = new JLabel();
        label31 = new JLabel();
        etBillingQuantity = new JTextField();
        bBillingDone = new JButton();
        bPLUSBillingCategoryName = new JButton();
        bREFRESH_Billing_CategoryName = new JButton();
        bPLUS_Billing_ItemName = new JButton();
        bREFRESH_Billing_ItemName = new JButton();
        label5 = new JLabel();
        scrollPane9 = new JScrollPane();
        etBillingDescription = new JTextArea();
        scrollPane10 = new JScrollPane();
        JTabelBillingItem = new JTable();
        button3 = new JButton();
        button5 = new JButton();

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

                //---- button1 ----
                button1.setText("LOG OUT");
                button1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        bMainPageLogOut(e);
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
                                    .addComponent(button1)))
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
                            .addComponent(button1)
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

                    //======== panel4 ========
                    {

                        //======== scrollPane2 ========
                        {
                            scrollPane2.setViewportView(tree1);
                        }

                        GroupLayout panel4Layout = new GroupLayout(panel4);
                        panel4.setLayout(panel4Layout);
                        panel4Layout.setHorizontalGroup(
                            panel4Layout.createParallelGroup()
                                .addGroup(panel4Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        );
                        panel4Layout.setVerticalGroup(
                            panel4Layout.createParallelGroup()
                                .addGroup(panel4Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        );
                    }
                    tabbedPaneAdding.addTab("Quantity", panel4);

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
                            label24.setText("Normal Price :");

                            //---- label25 ----
                            label25.setText("Selling Price :");

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
                            label27.setText("Description:");

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

                            GroupLayout panel7Layout = new GroupLayout(panel7);
                            panel7.setLayout(panel7Layout);
                            panel7Layout.setHorizontalGroup(
                                panel7Layout.createParallelGroup()
                                    .addGroup(panel7Layout.createSequentialGroup()
                                        .addGap(85, 85, 85)
                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(label22)
                                            .addComponent(label23)
                                            .addComponent(label24)
                                            .addComponent(label26)
                                            .addComponent(label27))
                                        .addGap(18, 18, 18)
                                        .addGroup(panel7Layout.createParallelGroup()
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addComponent(bAddItemCatagoryChoose)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboBoxAddItemCategoryName, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bAddItemRefresh)
                                                .addContainerGap(496, Short.MAX_VALUE))
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(tvAdditemName, GroupLayout.Alignment.LEADING)
                                                    .addGroup(panel7Layout.createParallelGroup()
                                                        .addGroup(panel7Layout.createSequentialGroup()
                                                            .addComponent(tvAdditemNormalPrice, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                                            .addGap(38, 38, 38)
                                                            .addComponent(label25)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(tvAddItemSellingPrice, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(panel7Layout.createSequentialGroup()
                                                            .addComponent(tvAddItemQuantity, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(comboBoxAddItemQuantityUnits, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 297, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 197, Short.MAX_VALUE))))
                                    .addGroup(panel7Layout.createSequentialGroup()
                                        .addGroup(panel7Layout.createParallelGroup()
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addGap(256, 256, 256)
                                                .addComponent(label21))
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addGap(265, 265, 265)
                                                .addComponent(button4)))
                                        .addGap(66, 406, Short.MAX_VALUE))
                                    .addGroup(panel7Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(scrollPane7, GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
                            );
                            panel7Layout.setVerticalGroup(
                                panel7Layout.createParallelGroup()
                                    .addGroup(panel7Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(label21)
                                        .addGap(49, 49, 49)
                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label22)
                                            .addComponent(tvAdditemName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(12, 12, 12)
                                        .addGroup(panel7Layout.createParallelGroup()
                                            .addGroup(panel7Layout.createSequentialGroup()
                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                    .addComponent(label23)
                                                    .addComponent(bAddItemCatagoryChoose)
                                                    .addComponent(comboBoxAddItemCategoryName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addGroup(panel7Layout.createSequentialGroup()
                                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label24)
                                                            .addComponent(tvAdditemNormalPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(label25)
                                                            .addComponent(tvAddItemSellingPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(panel7Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label26)
                                                            .addComponent(tvAddItemQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                                    .addComponent(comboBoxAddItemQuantityUnits, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addGap(34, 34, 34)
                                                .addGroup(panel7Layout.createParallelGroup()
                                                    .addComponent(label27)
                                                    .addComponent(scrollPane6, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addComponent(button4))
                                            .addComponent(bAddItemRefresh))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                        .addComponent(scrollPane7, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
                                        .addGap(222, 222, 222))
                            );
                        }
                        scrollPane5.setViewportView(panel7);
                    }
                    tabbedPaneAdding.addTab("Item", scrollPane5);

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
                                            .addComponent(separator3, GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(panel6Layout.createParallelGroup()
                                                    .addComponent(label17)
                                                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 562, GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 142, Short.MAX_VALUE))
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
                                                .addContainerGap(196, Short.MAX_VALUE))))
                                    .addGroup(panel6Layout.createSequentialGroup()
                                        .addGroup(panel6Layout.createParallelGroup()
                                            .addGroup(panel6Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(panel6Layout.createParallelGroup()
                                                    .addGroup(panel6Layout.createSequentialGroup()
                                                        .addGap(237, 237, 237)
                                                        .addComponent(label11))
                                                    .addComponent(separator2, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
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
                                                .addGap(0, 390, Short.MAX_VALUE))
                                            .addComponent(separator1, GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE))
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
                }

                GroupLayout panelAddingStuffLayout = new GroupLayout(panelAddingStuff);
                panelAddingStuff.setLayout(panelAddingStuffLayout);
                panelAddingStuffLayout.setHorizontalGroup(
                    panelAddingStuffLayout.createParallelGroup()
                        .addComponent(tabbedPaneAdding, GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                );
                panelAddingStuffLayout.setVerticalGroup(
                    panelAddingStuffLayout.createParallelGroup()
                        .addComponent(tabbedPaneAdding, GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE)
                );
            }
            tabbedPaneAddingStuff.addTab("Adding Stuff", panelAddingStuff);

            //======== panel3 ========
            {

                //======== tabbedPane3 ========
                {
                    tabbedPane3.addChangeListener(new ChangeListener() {
                        @Override
                        public void stateChanged(ChangeEvent e) {
                            tabbedPaneBillingSateChange(e);
                        }
                    });
                    tabbedPane3.addTab("text", scrollPane8);

                    //======== scrollPane4 ========
                    {

                        //======== panel5 ========
                        {

                            //---- label19 ----
                            label19.setText("Billing");
                            label19.setFont(label19.getFont().deriveFont(label19.getFont().getStyle() | Font.BOLD, label19.getFont().getSize() + 5f));

                            //---- label20 ----
                            label20.setText("Item Name :*");

                            //---- comboBoxBillingItemName ----
                            comboBoxBillingItemName.setVisible(false);
                            comboBoxBillingItemName.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    comboBoxBillingItemName(e);
                                }
                            });

                            //---- label28 ----
                            label28.setText("Category Name:*");

                            //---- comboBoxBillingCategoryName ----
                            comboBoxBillingCategoryName.setVisible(false);
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

                            //---- bBillingDone ----
                            bBillingDone.setText("Done");
                            bBillingDone.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bBillingDone(e);
                                }
                            });

                            //---- bPLUSBillingCategoryName ----
                            bPLUSBillingCategoryName.setText("+");
                            bPLUSBillingCategoryName.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bPLUS_Billing_CategoryName(e);
                                }
                            });

                            //---- bREFRESH_Billing_CategoryName ----
                            bREFRESH_Billing_CategoryName.setText("REFRESH");
                            bREFRESH_Billing_CategoryName.setVisible(false);
                            bREFRESH_Billing_CategoryName.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bREFRESH_Billing_CategoryName(e);
                                }
                            });

                            //---- bPLUS_Billing_ItemName ----
                            bPLUS_Billing_ItemName.setText("+");
                            bPLUS_Billing_ItemName.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bPLUS_Billing_ItemName(e);
                                }
                            });

                            //---- bREFRESH_Billing_ItemName ----
                            bREFRESH_Billing_ItemName.setText("REFRESH");
                            bREFRESH_Billing_ItemName.setVisible(false);
                            bREFRESH_Billing_ItemName.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bREFRESH_Billing_ItemName(e);
                                }
                            });

                            //---- label5 ----
                            label5.setText("Description:");

                            //======== scrollPane9 ========
                            {
                                scrollPane9.setViewportView(etBillingDescription);
                            }

                            //======== scrollPane10 ========
                            {

                                //---- JTabelBillingItem ----
                                JTabelBillingItem.addPropertyChangeListener(new PropertyChangeListener() {
                                    @Override
                                    public void propertyChange(PropertyChangeEvent e) {
                                        JtableBillingItems(e);
                                    }
                                });
                                scrollPane10.setViewportView(JTabelBillingItem);
                            }

                            //---- button3 ----
                            button3.setText("New Bill");
                            button3.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bBillingNewBill(e);
                                }
                            });

                            //---- button5 ----
                            button5.setText("Print Bill");
                            button5.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    bBillingPrintBill(e);
                                }
                            });

                            GroupLayout panel5Layout = new GroupLayout(panel5);
                            panel5.setLayout(panel5Layout);
                            panel5Layout.setHorizontalGroup(
                                panel5Layout.createParallelGroup()
                                    .addGroup(GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
                                        .addGap(22, 22, 22)
                                        .addComponent(button3)
                                        .addGap(20, 20, 20)
                                        .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addGap(0, 471, Short.MAX_VALUE)
                                                .addComponent(label19)
                                                .addGap(344, 344, 344))
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addGroup(panel5Layout.createParallelGroup()
                                                    .addGroup(panel5Layout.createSequentialGroup()
                                                        .addGap(56, 56, 56)
                                                        .addComponent(label28))
                                                    .addComponent(label20, GroupLayout.Alignment.TRAILING))
                                                .addGap(18, 18, 18)
                                                .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(panel5Layout.createSequentialGroup()
                                                        .addComponent(bPLUSBillingCategoryName)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(comboBoxBillingCategoryName, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                                                    .addGroup(panel5Layout.createSequentialGroup()
                                                        .addComponent(bPLUS_Billing_ItemName)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(comboBoxBillingItemName, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(panel5Layout.createParallelGroup()
                                                    .addComponent(bREFRESH_Billing_CategoryName)
                                                    .addComponent(bREFRESH_Billing_ItemName))
                                                .addContainerGap(664, Short.MAX_VALUE))
                                            .addGroup(GroupLayout.Alignment.LEADING, panel5Layout.createSequentialGroup()
                                                .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addComponent(label29)
                                                    .addComponent(label31)
                                                    .addComponent(label5))
                                                .addGap(18, 18, 18)
                                                .addGroup(panel5Layout.createParallelGroup()
                                                    .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(etBillingQuantity, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                                        .addComponent(tvBillingAvailQty, GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE))
                                                    .addComponent(scrollPane9, GroupLayout.PREFERRED_SIZE, 363, GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 383, Short.MAX_VALUE))))
                                    .addGroup(panel5Layout.createSequentialGroup()
                                        .addGroup(panel5Layout.createParallelGroup()
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addGap(306, 306, 306)
                                                .addComponent(bBillingDone))
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addGap(46, 46, 46)
                                                .addComponent(scrollPane10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addGap(84, 84, 84)
                                                .addComponent(button5)))
                                        .addGap(0, 349, Short.MAX_VALUE))
                            );
                            panel5Layout.setVerticalGroup(
                                panel5Layout.createParallelGroup()
                                    .addGroup(panel5Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel5Layout.createParallelGroup()
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(panel5Layout.createParallelGroup()
                                                    .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label28)
                                                        .addComponent(bPLUSBillingCategoryName)
                                                        .addComponent(button3))
                                                    .addComponent(bREFRESH_Billing_CategoryName)
                                                    .addComponent(comboBoxBillingCategoryName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label20)
                                                        .addComponent(bPLUS_Billing_ItemName))
                                                    .addComponent(comboBoxBillingItemName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(bREFRESH_Billing_ItemName))
                                                .addGap(30, 30, 30))
                                            .addGroup(GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
                                                .addComponent(label19)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label29)
                                            .addComponent(tvBillingAvailQty))
                                        .addGap(18, 18, 18)
                                        .addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label31)
                                            .addComponent(etBillingQuantity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panel5Layout.createParallelGroup()
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addGap(15, 15, 15)
                                                .addComponent(label5))
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(scrollPane9, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18)
                                        .addComponent(bBillingDone)
                                        .addGroup(panel5Layout.createParallelGroup()
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addGap(170, 170, 170)
                                                .addComponent(scrollPane10, GroupLayout.PREFERRED_SIZE, 288, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(panel5Layout.createSequentialGroup()
                                                .addGap(223, 223, 223)
                                                .addComponent(button5)))
                                        .addGap(148, 148, 148))
                            );
                        }
                        scrollPane4.setViewportView(panel5);
                    }
                    tabbedPane3.addTab("Billing", scrollPane4);
                }

                GroupLayout panel3Layout = new GroupLayout(panel3);
                panel3.setLayout(panel3Layout);
                panel3Layout.setHorizontalGroup(
                    panel3Layout.createParallelGroup()
                        .addComponent(tabbedPane3, GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                );
                panel3Layout.setVerticalGroup(
                    panel3Layout.createParallelGroup()
                        .addComponent(tabbedPane3, GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE)
                );
            }
            tabbedPaneAddingStuff.addTab("Billing", panel3);
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
    private JButton button1;
    private JPanel panelAddingStuff;
    private JTabbedPane tabbedPaneAdding;
    private JPanel panel4;
    private JScrollPane scrollPane2;
    private JTree tree1;
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
    private JLabel label25;
    private JTextField tvAdditemNormalPrice;
    private JTextField tvAddItemSellingPrice;
    private JLabel label26;
    private JTextField tvAddItemQuantity;
    private JComboBox comboBoxAddItemQuantityUnits;
    private JLabel label27;
    private JScrollPane scrollPane6;
    private JTextArea tvAdditemDescription;
    private JScrollPane scrollPane7;
    private JTable JTabelItemAdd;
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
    private JPanel panel3;
    private JTabbedPane tabbedPane3;
    private JScrollPane scrollPane8;
    private JScrollPane scrollPane4;
    private JPanel panel5;
    private JLabel label19;
    private JLabel label20;
    private JComboBox comboBoxBillingItemName;
    private JLabel label28;
    private JComboBox comboBoxBillingCategoryName;
    private JLabel label29;
    private JLabel tvBillingAvailQty;
    private JLabel label31;
    private JTextField etBillingQuantity;
    private JButton bBillingDone;
    private JButton bPLUSBillingCategoryName;
    private JButton bREFRESH_Billing_CategoryName;
    private JButton bPLUS_Billing_ItemName;
    private JButton bREFRESH_Billing_ItemName;
    private JLabel label5;
    private JScrollPane scrollPane9;
    private JTextArea etBillingDescription;
    private JScrollPane scrollPane10;
    private JTable JTabelBillingItem;
    private JButton button3;
    private JButton button5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
