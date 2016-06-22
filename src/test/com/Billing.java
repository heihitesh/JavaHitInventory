/*
 * Created by JFormDesigner on Wed Jun 22 19:49:36 IST 2016
 */

package test.com;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;

/**
 * @author Hitesh Verma
 */
public class Billing extends JFrame {
    public Billing() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Hitesh Verma
        label1 = new JLabel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        label2 = new JLabel();
        tvBillTotal = new JLabel();
        tvBillMadeBy = new JLabel();
        tvBillID = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();

        //---- label1 ----
        label1.setText("Bill");
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 6f));

        //======== scrollPane1 ========
        {
            scrollPane1.setViewportView(table1);
        }

        //---- label2 ----
        label2.setText("total:-");

        //---- tvBillTotal ----
        tvBillTotal.setText(".....................");

        //---- tvBillMadeBy ----
        tvBillMadeBy.setText("Name");

        //---- tvBillID ----
        tvBillID.setText("Bill Id:");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(303, 303, 303)
                                    .addComponent(label2)
                                    .addGap(18, 18, 18)
                                    .addComponent(tvBillTotal, GroupLayout.PREFERRED_SIZE, 158, GroupLayout.PREFERRED_SIZE))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(27, 27, 27)
                                    .addComponent(tvBillMadeBy, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
                                    .addGap(195, 195, 195)
                                    .addComponent(label1, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                                    .addGap(182, 182, 182)
                                    .addComponent(tvBillID)))
                            .addGap(0, 119, Short.MAX_VALUE)))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(tvBillMadeBy, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
                                .addComponent(tvBillID)))
                        .addComponent(label1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addGap(66, 66, 66)
                    .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(label2)
                        .addComponent(tvBillTotal))
                    .addContainerGap(161, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Hitesh Verma
    private JLabel label1;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JLabel label2;
    private JLabel tvBillTotal;
    private JLabel tvBillMadeBy;
    private JLabel tvBillID;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
