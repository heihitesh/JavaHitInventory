package test.com.DataBaseServices;

import test.com.HelperClasses.AnalysisWorkerInfo;
import test.com.HelperClasses.BillingItemInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import static test.com.MainPage.infoError;

/**
 * Created by Nilesh Verma on 7/1/2016.
 */
public class dbsAnalysis {
    Connection myConn;
    JTable table;
    Float totalAnalysisBilling= Float.valueOf(0);
    public dbsAnalysis(Connection myConn, JTable JTableWorkerAnalysis) {
        this.myConn = myConn;
        this.table = JTableWorkerAnalysis;
        settingtheJTabelHeader(JTableWorkerAnalysis);
    }




    public void createWorkerList(JComboBox comboBoxAddItemCategoryName) {
        try {
            ResultSet myResultSet;
            //Selecting From the Data Base
            PreparedStatement myPStmt = myConn.prepareStatement("select user_name from hit.employees order by ? asc");
            myPStmt.setString(1, "user_name");
            myResultSet = myPStmt.executeQuery();
            while (myResultSet.next()) {
                String FirstName = myResultSet.getString("user_name");
                comboBoxAddItemCategoryName.addItem(FirstName);
            }
        } catch (Exception exc) {
            infoError(String.valueOf(exc), "TRY AGAIN");
        }
    }





    public void settingtheJTabelHeader(JTable JTabelItemAdd) {

        JTabelItemAdd.setModel(new DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "Sr.no", "Bill No", "Amount","Date","Time","Type"
                }
        ));

    }


    public void getWorkerAnalysis(String worker_name) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        Show_Analysis_of_workerTable(worker_name);
    }

    private void Show_Analysis_of_workerTable(String worker_name) {

        ArrayList<AnalysisWorkerInfo> list = getItemTabelList(worker_name);
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        Object[] row = new Object[6];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getSrno();
            row[1] = list.get(i).getBillID();
            row[2] = list.get(i).getAmount();
            row[3] = list.get(i).getDate();
            row[4] = list.get(i).getTime();
            row[5] = list.get(i).getType();
            model.addRow(row);

        }

    }

    public ArrayList<AnalysisWorkerInfo> getItemTabelList(String worker_name) {

        ArrayList<AnalysisWorkerInfo> itemBilling = new ArrayList<AnalysisWorkerInfo>();
        ResultSet rs;
        try {
            PreparedStatement myPStmt = myConn.prepareStatement("SELECT * FROM hit.billing where made_by=?");
            myPStmt.setString(1, worker_name);
            rs = myPStmt.executeQuery();
            AnalysisWorkerInfo billingItem;
            int count = 0;
            while (rs.next()) {
                billingItem = new AnalysisWorkerInfo(count + 1, rs.getString("billing_id"), rs.getString("date"),
                        rs.getString("time"),rs.getString("amount"),rs.getString("billing_type"));
                itemBilling.add(billingItem);
                totalAnalysisBilling = totalAnalysisBilling + rs.getFloat("amount");
                count++;
            }
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
        return itemBilling;

    }

    public Float getTotalAnalysisBilling() {
        return totalAnalysisBilling;
    }

    public void setTotalAnalysisBilling() {
        this.totalAnalysisBilling = Float.valueOf(0);
    }
}
