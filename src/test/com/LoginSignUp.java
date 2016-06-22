/*
 * Created by JFormDesigner on Sat Jun 18 05:05:42 IST 2016
 */

package test.com;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle;

//import org.jdesktop.layout.GroupLayout;
//import org.jdesktop.layout.LayoutStyle;

import java.sql.*;

/**
 * @author Hitesh Verma
 */
public class LoginSignUp extends JFrame {
    static Connection myConn = null;
    static ResultSet myRs = null;
    static PreparedStatement myPStmt = null;
    String AccessPerson = "";
    static Statement stmt = null;
    static User user = new User();

    public LoginSignUp() {
        initComponents();
    }

    public static void main(String[] args) throws SQLException {
        LoginSignUp loginSignUp = new LoginSignUp();
        loginSignUp.setVisible(true);
        loginSignUp.setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            // 1. Get a connection to database
            myConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?user=login", "login", "hitesh123");

            System.out.println("Database connection successful!\n");

            System.out.println("Creating statement...");
            stmt = myConn.createStatement();

            String sql = "SELECT * FROM hit.employees";
            ResultSet rs = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int id = rs.getInt("id");
                String first = rs.getString("first_name");
                String last = rs.getString("last_name");

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", First: " + first);
                System.out.println(", Last: " + last);
            }


        } catch (Exception e) {
            String exception = String.valueOf(e);
            infoError(exception, "ERROR!!!");
        }


    }


    private void bLogin(ActionEvent evt) {
        // TODO add your handling code here:
        String LoginUName = loginLUName.getText();
        String LoginPass = loginLPass.getText();
        ///if it matches with the data base the proceed
        if (LoginUName.length() >= 1 && LoginPass.length() >= 1) {

            switch (AccessPerson) {

                case "manager":

                    try {

                       PreparedStatement myPStmt = myConn.prepareStatement("select * from hit.manager "
                                + "where user_name=? and password=?");
                        myPStmt.setString(1, LoginUName);
                        myPStmt.setString(2, LoginPass);
                        // 3. Execute SQL query

                        //myRs = myStmt.executeQuery("select * from demo.employees order by last_name);

                        myRs = myPStmt.executeQuery();

                        // 4. Process the result set




                        if (myRs.next()) {

                            String FirstName = myRs.getString("first_name");
                            String LastName = myRs.getString("last_name");
                            String UserName = myRs.getString("user_name");
                            String Phone_no = myRs.getString("phone_no");
                            String Accessparam = myRs.getString("access_param");

                            ////User login info is stored in User clas

                            this.setVisible(false);
                           // new MainPage().setVisible(true);
                            new MainPage(FirstName+" "+LastName,UserName,Phone_no,Accessparam , myConn ,myRs).setVisible(true);

                            JOptionPane.showMessageDialog(this, "Manager Login \n " + FirstName + " "
                                    + LastName);



                        } else {
                            JOptionPane.showMessageDialog(null, "Incorrect User_Name or Password ", "TRY AGAIN", JOptionPane.ERROR_MESSAGE);
                        }



                    } catch (SQLException e) {
                        infoError(String.valueOf(e), "TRY AGAIN");
                    }

                    break;


                default:  //represents the worker
                    try {

                        PreparedStatement myPStmt = myConn.prepareStatement("select * from hit.employees "
                                + "where user_name=? and password=?");
                        myPStmt.setString(1, LoginUName);
                        myPStmt.setString(2, LoginPass);
                        // 3. Execute SQL query

                        //myRs = myStmt.executeQuery("select * from demo.employees order by last_name);

                        myRs = myPStmt.executeQuery();

                        // 4. Process the result set
                        if (myRs.next()) {

                            String FirstName = myRs.getString("first_name");
                            String LastName = myRs.getString("last_name");
                            String UserName = myRs.getString("user_name");
                            String Phone_no = myRs.getString("phone_no");
                            String Accessparam = myRs.getString("access_param");

                            this.setVisible(false);
                            // new MainPage().setVisible(true);
                            new MainPage(FirstName+" "+LastName,UserName,Phone_no,Accessparam , myConn ,myRs).setVisible(true);
                            // System.out.println(myRs.getString("last_name") + ", " + myRs.getString("first_name"));
                            JOptionPane.showMessageDialog(this, "Worker Login \n " + FirstName + " "
                                    + LastName);

                        } else {
                            JOptionPane.showMessageDialog(null, "Incorrect User_Name or Password ", "TRY AGAIN", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException e) {
                        infoError(String.valueOf(e), "TRY AGAIN");
                    }

                    break;


            }


        } else {
            infoError("Please Enter Your Info", "TRY AGAIN");
        }

    }

    private void bSignUp(ActionEvent evt) {
        String SignUPPass = signUpLPass.getText();
        String SignUPCPass = signUpLCPass.getText();
        String SignUPLFirstName = signUpLFirstName.getText();
        String SignUPLLastName = signUpLLastName.getText();
        String SignUpLPhoneNo = signUpLPhoneNo.getText();
        String SignUPLUName = signUpLUname.getText();
        String SignUpMasterKey = signUPMasterKey.getText();
        if (SignUPCPass.equals(SignUPPass) && SignUPPass != null && SignUPPass.length() >= 1
                && SignUPLFirstName.length() >= 1 && SignUPLUName.length() >= 1 && SignUpMasterKey.equals("hitesh123")) {

            ///--------STOREING DATA IN THE DATA BASE ------

            try {
                //----------------------------INSERT-------------------------------------
                myPStmt = myConn.prepareStatement("insert into hit.employees "
                        + "(first_name,last_name,user_name,password,access_param,phone_no) "
                        + "values " + "(?,?,?,?,?,?)");
                myPStmt.setString(1, SignUPLFirstName);
                myPStmt.setString(2, SignUPLLastName);
                myPStmt.setString(3, SignUPLUName);
                myPStmt.setString(4, SignUPPass);
                myPStmt.setString(5, "worker");
                myPStmt.setString(6, SignUpLPhoneNo);


                int RowsAffected = myPStmt.executeUpdate();
                if (RowsAffected >= 1) {
                    //it will return the no of row affected
                    infoBox("Name :"
                            + SignUPLFirstName + " " + SignUPLLastName + "." + "\n" +
                            "User Name :" + SignUPLUName
                            + "\n" + "Password :"
                            + SignUPPass + "\n"
                            + "Phone No: " + SignUpLPhoneNo, "SUCCESS PLEASE NOW LOGIN");

                }
                myRs = myPStmt.executeQuery("select * from hit.employees");
                while (myRs.next()) {
                    System.out.println(myRs.getString("last_name") + myRs.getString("first_name"));

                }
            } catch (SQLException e) {
                String exc = String.valueOf(e);
                infoError(exc, "Please Choose a Different UserName :)");
            } catch (Exception e) {
                String exc = String.valueOf(e);
                infoError(exc, "ERROR PLEASE TRY AGAIN");
            }
          /*  finally{
                if (myPStmt != null) {
                    try {
                        myPStmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SignUpPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
			}
                if (myRs != null) {
                    try {
                        myRs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SignUpPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
			}


			if (myConn != null) {
                    try {
                        myConn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(SignUpPage.class.getName()).log(Level.SEVERE, null, ex);
                    }
			}
            }
            */


        } else {
            infoError("Possible reasons : \n 1 )EMPTY Password \n 2) Password DOESN'T MATCH \n"
                    + " 3) User Name or First Name Field EMPTY \n 4) INVALID MASTER KEY.. :( ", "FAILURE");

        }
    }

    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
    }


    public static void infoError(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.ERROR_MESSAGE);
    }

    private void loginRWorkerActionPerformed(ActionEvent e) {
        AccessPerson = "worker";
    }

    private void loginRManagerActionPerformed(ActionEvent e) {
        AccessPerson = "manager";
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Hitesh Verma
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        label1 = new JLabel();
        loginLUName = new JTextField();
        label2 = new JLabel();
        label3 = new JLabel();
        loginRWorker = new JRadioButton();
        loginRManager = new JRadioButton();
        loginBLogin = new JButton();
        loginLPass = new JPasswordField();
        panel3 = new JPanel();
        label4 = new JLabel();
        label5 = new JLabel();
        signUpLFirstName = new JTextField();
        label6 = new JLabel();
        signUpLLastName = new JTextField();
        label7 = new JLabel();
        signUpLUname = new JTextField();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        signUpBSave = new JButton();
        signUpLPass = new JPasswordField();
        signUpLCPass = new JPasswordField();
        signUPMasterKey = new JPasswordField();
        label11 = new JLabel();
        signUpLPhoneNo = new JTextField();

        //======== this ========
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {

                // JFormDesigner evaluation mark
                panel1.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), panel1.getBorder())); panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


                //---- label1 ----
                label1.setText("LOGIN");
                label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 3f));

                //---- label2 ----
                label2.setText("User Name :");

                //---- label3 ----
                label3.setText("Password :");

                //---- loginRWorker ----
                loginRWorker.setText("Worker");
                loginRWorker.setSelected(true);
                loginRWorker.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loginRWorkerActionPerformed(e);
                    }
                });

                //---- loginRManager ----
                loginRManager.setText("Manager");
                loginRManager.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loginRManagerActionPerformed(e);
                    }
                });

                //---- loginBLogin ----
                loginBLogin.setText("LOGIN");
                loginBLogin.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        bLogin(e);
                    }
                });

                GroupLayout panel1Layout = new GroupLayout(panel1);
                panel1.setLayout(panel1Layout);
                panel1Layout.setHorizontalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addContainerGap(59, Short.MAX_VALUE)
                            .addGroup(panel1Layout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label2)
                                        .addGroup(panel1Layout.createParallelGroup()
                                            .addComponent(loginRWorker)
                                            .addComponent(label3)))
                                    .addGroup(panel1Layout.createParallelGroup()
                                        .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(loginRManager)
                                            .addGap(87, 87, 87))
                                        .addGroup(panel1Layout.createSequentialGroup()
                                            .addGap(77, 77, 77)
                                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(loginLUName, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                                .addComponent(loginLPass, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                                            .addGap(47, 47, 47))))
                                .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addComponent(label1)
                                    .addGap(183, 183, 183))
                                .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                    .addComponent(loginBLogin)
                                    .addGap(175, 175, 175))))
                );
                panel1Layout.setVerticalGroup(
                    panel1Layout.createParallelGroup()
                        .addGroup(panel1Layout.createSequentialGroup()
                            .addComponent(label1)
                            .addGap(47, 47, 47)
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(label2)
                                .addComponent(loginLUName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label3)
                                .addComponent(loginLPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(panel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loginRWorker)
                                .addComponent(loginRManager))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                            .addComponent(loginBLogin)
                            .addGap(75, 75, 75))
                );
            }
            tabbedPane1.addTab("LOGIN", panel1);

            //======== panel3 ========
            {

                //---- label4 ----
                label4.setText("SIGN UP");
                label4.setFont(new Font("Segoe UI", Font.BOLD, 20));

                //---- label5 ----
                label5.setText("First Name : ");

                //---- label6 ----
                label6.setText("Last Name :");

                //---- label7 ----
                label7.setText("User Name :");

                //---- label8 ----
                label8.setText("Password :");

                //---- label9 ----
                label9.setText("Confirm Password :");

                //---- label10 ----
                label10.setText("Master Key :");

                //---- signUpBSave ----
                signUpBSave.setText("Sign Up");
                signUpBSave.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        bSignUp(e);
                    }
                });

                //---- label11 ----
                label11.setText("Phone No :");

                GroupLayout panel3Layout = new GroupLayout(panel3);
                panel3.setLayout(panel3Layout);
                panel3Layout.setHorizontalGroup(
                    panel3Layout.createParallelGroup()
                        .addGroup(panel3Layout.createSequentialGroup()
                            .addGroup(panel3Layout.createParallelGroup()
                                .addGroup(panel3Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(label5)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(panel3Layout.createParallelGroup()
                                        .addGroup(panel3Layout.createSequentialGroup()
                                            .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(label9)
                                                .addComponent(label8)
                                                .addComponent(label11)
                                                .addComponent(label10)
                                                .addComponent(label7))
                                            .addGap(33, 33, 33)
                                            .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(signUpLPass, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                                .addComponent(signUpLCPass, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                                .addComponent(signUPMasterKey, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                                .addComponent(signUpLPhoneNo, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                                                .addComponent(signUpLUname, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)))
                                        .addGroup(panel3Layout.createSequentialGroup()
                                            .addComponent(signUpLFirstName, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(label6)
                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(signUpLLastName, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))))
                                .addGroup(panel3Layout.createSequentialGroup()
                                    .addGap(141, 141, 141)
                                    .addComponent(label4, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap(20, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                            .addGap(0, 150, Short.MAX_VALUE)
                            .addComponent(signUpBSave)
                            .addGap(161, 161, 161))
                );
                panel3Layout.setVerticalGroup(
                    panel3Layout.createParallelGroup()
                        .addGroup(panel3Layout.createSequentialGroup()
                            .addComponent(label4)
                            .addGap(12, 12, 12)
                            .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(label5)
                                .addComponent(signUpLFirstName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label6)
                                .addComponent(signUpLLastName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(signUpLUname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label7))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(signUpLPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label8))
                            .addGap(8, 8, 8)
                            .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(signUpLCPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label9))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(signUpLPhoneNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label11))
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(signUPMasterKey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(label10))
                            .addGap(46, 46, 46)
                            .addComponent(signUpBSave)
                            .addContainerGap())
                );
            }
            tabbedPane1.addTab("SIGN UP", panel3);
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(tabbedPane1)
        );
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(loginRWorker);
        buttonGroup1.add(loginRManager);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Hitesh Verma
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JLabel label1;
    private JTextField loginLUName;
    private JLabel label2;
    private JLabel label3;
    private JRadioButton loginRWorker;
    private JRadioButton loginRManager;
    private JButton loginBLogin;
    private JPasswordField loginLPass;
    private JPanel panel3;
    private JLabel label4;
    private JLabel label5;
    private JTextField signUpLFirstName;
    private JLabel label6;
    private JTextField signUpLLastName;
    private JLabel label7;
    private JTextField signUpLUname;
    private JLabel label8;
    private JLabel label9;
    private JLabel label10;
    private JButton signUpBSave;
    private JPasswordField signUpLPass;
    private JPasswordField signUpLCPass;
    private JPasswordField signUPMasterKey;
    private JLabel label11;
    private JTextField signUpLPhoneNo;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


}
