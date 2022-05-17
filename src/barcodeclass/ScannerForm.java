/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package barcodeclass;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Janine
 */
public class ScannerForm extends javax.swing.JFrame {

    /**
     * Creates new form ScannerForm
     */
    public ScannerForm() {
        initComponents();
        connect();
        setBuysTable();
        setfilterTable();
        showFilterTable();
        this.setVisible(true);
    }
    
    DefaultTableModel buymdl=new DefaultTableModel();
    Object[] buyitem=new Object[6];
    DefaultTableModel itemsmdl=new DefaultTableModel();
    Object[] items=new Object[4];
    TableRowSorter sorter=new TableRowSorter<DefaultTableModel>(itemsmdl);
    //Salestbl=Item_ID,Quantity,Unit_Price,Total_Amount,Transaction_DateTime,Receipt_ID
    //Receiptstbl=Receipt_DateTime,Gross_Amount,Discount,Total_Due
    Object[] todbsales=new Object[5];
    
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    void connect(){
        try{
            conn=DriverManager.getConnection("jdbc:sqlite:src//mainDB.db");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Can't Locate Database!");
        }
    }
    void setfilterTable(){
        String[] columns={"Item Code","ItemName","Unit Price","Stock"};
        itemsmdl.setColumnIdentifiers(columns);
        filtertable.setModel(itemsmdl);
        for(int i=0; i<filtertable.getColumnCount();i++){
            filtertable.setDefaultEditor(filtertable.getColumnClass(i), null);
        }
        filtertable.getColumnModel().getColumn(0).setMinWidth(100);
        filtertable.getColumnModel().getColumn(1).setMinWidth(120);
        filtertable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        filtertable.setRowSorter(sorter);
    }
    void setBuysTable(){
        String[] columns={"Item Code","Quantity","Item","Price","Subtotal","DateTime"};
        buymdl.setColumnIdentifiers(columns);
        buyitemstbl.setModel(buymdl);
        for(int i=0;i<5;i++){
            TableColumn buytbl=buyitemstbl.getColumnModel().getColumn(i);
            if(i==1){
                buytbl.setMinWidth(40);
                buytbl.setMaxWidth(40);
                buytbl.setPreferredWidth(40);
            }
            else if(i==4){
                buytbl.setMinWidth(60);
                buytbl.setMaxWidth(60);
                buytbl.setPreferredWidth(60);
            }
            else if(i==2){
                buytbl.setMinWidth(110);
                buytbl.setMaxWidth(110);
                buytbl.setPreferredWidth(110);
            }
            else{
                buytbl.setMinWidth(0);
                buytbl.setMaxWidth(0);
                buytbl.setPreferredWidth(0);
            }
        }
        for(int i=0; i<buyitemstbl.getColumnCount();i++){
            buyitemstbl.setDefaultEditor(buyitemstbl.getColumnClass(i), null);
        }
        buyitemstbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        buyitemstbl.setBackground(Color.WHITE);
    }
    
    void updateBuysTable(){
        buyitem[0]=itemcodetxtfld.getText();
        buyitem[1]=quantitytxtfld.getText();
        buyitem[2]=itemnametxtfld.getText();
        buyitem[3]=unitpricetxtfld.getText();
        buyitem[4]=subtotaltxtfld.getText();
        buyitem[5]=new SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date());
        buymdl.addRow(buyitem);
    }
    
    void filterItems(){
        RowFilter<DefaultTableModel,Object> fltr=null;
        fltr=RowFilter.regexFilter(filterfld.getText().trim(),0);
        sorter.setRowFilter(fltr);
    }
    
    void showFilterTable(){
        try{
            pst=conn.prepareStatement("Select * from Itemstbl join Pricetbl on Itemstbl.Price_ID=Pricetbl.Price_ID join Quantitytbl on "
                    + "Itemstbl.Stock_ID=Quantitytbl.Stock_ID WHERE Remaining !='0'");
            rs=pst.executeQuery();
            while(rs.next()){
                items[0]=rs.getString("Item_Code");
                items[1]=rs.getString("Item_Name");
                items[2]=rs.getFloat("Selling_Price");
                items[3]=rs.getInt("Remaining");
                itemsmdl.addRow(items);
            }
        }
        catch(Exception e){        
        }
    }
    
    void resetflds(){
        filterfld.setText("");
        itemcodetxtfld.setText("");
        itemnametxtfld.setText("");
        unitpricetxtfld.setText("");
        quantitytxtfld.setText("");
        subtotaltxtfld.setText("");
        filterfld.requestFocus();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainpanel = new javax.swing.JPanel();
        receiptscroll = new javax.swing.JScrollPane();
        receiptpanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        receiptno = new javax.swing.JLabel();
        itemslistpanel = new javax.swing.JPanel();
        buyitemstbl = new javax.swing.JTable();
        datelbl = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        filterfld = new javax.swing.JTextField();
        itemcodetxtfld = new javax.swing.JTextField();
        itemnametxtfld = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        unitpricetxtfld = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        quantitytxtfld = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        filterscroll = new javax.swing.JScrollPane();
        filtertable = new javax.swing.JTable();
        addtobtn = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        subtotaltxtfld = new javax.swing.JTextField();
        printreceiptbtn = new javax.swing.JButton();
        totalduetxtfld = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        closebtn = new javax.swing.JLabel();
        loginadmin = new javax.swing.JLabel();
        searchlbl = new javax.swing.JLabel();
        removebtn = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        grosssaletxtfld = new javax.swing.JTextField();
        discounttxtfld = new javax.swing.JTextField();
        discardbtn = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        mainpanel.setBackground(new java.awt.Color(204, 51, 255));

        receiptscroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        receiptpanel.setBackground(new java.awt.Color(255, 255, 255));
        receiptpanel.setMaximumSize(new java.awt.Dimension(238, 32767));
        receiptpanel.setMinimumSize(new java.awt.Dimension(238, 360));
        receiptpanel.setPreferredSize(new java.awt.Dimension(238, 600));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PETERUSH");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Gulod, Bendita I, Magallanes, Cavite");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("TIN: 203-361-692-000");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("RECEIPT #");

        receiptno.setText("0000");

        itemslistpanel.setBackground(new java.awt.Color(255, 255, 255));
        itemslistpanel.setMaximumSize(new java.awt.Dimension(238, 32767));
        itemslistpanel.setMinimumSize(new java.awt.Dimension(238, 238));
        itemslistpanel.setPreferredSize(new java.awt.Dimension(238, 500));

        buyitemstbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        buyitemstbl.setOpaque(false);
        buyitemstbl.setShowHorizontalLines(false);
        buyitemstbl.setShowVerticalLines(false);
        buyitemstbl.setTableHeader(null);
        buyitemstbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buyitemstblMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout itemslistpanelLayout = new javax.swing.GroupLayout(itemslistpanel);
        itemslistpanel.setLayout(itemslistpanelLayout);
        itemslistpanelLayout.setHorizontalGroup(
            itemslistpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemslistpanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(buyitemstbl, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        itemslistpanelLayout.setVerticalGroup(
            itemslistpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemslistpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buyitemstbl, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        datelbl.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        datelbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        datelbl.setText("Date");
        datelbl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("GENERAL MERCHANDISE");

        javax.swing.GroupLayout receiptpanelLayout = new javax.swing.GroupLayout(receiptpanel);
        receiptpanel.setLayout(receiptpanelLayout);
        receiptpanelLayout.setHorizontalGroup(
            receiptpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, receiptpanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(itemslistpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, receiptpanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(receiptno, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
            .addGroup(receiptpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(receiptpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(datelbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        receiptpanelLayout.setVerticalGroup(
            receiptpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(receiptpanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datelbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(receiptpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(receiptno)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemslistpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(161, Short.MAX_VALUE))
        );

        receiptscroll.setViewportView(receiptpanel);
        receiptpanel.getAccessibleContext().setAccessibleDescription("");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("ITEM CODE:");

        filterfld.setForeground(new java.awt.Color(0, 0, 0));
        filterfld.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        filterfld.setMinimumSize(new java.awt.Dimension(0, 20));
        filterfld.setPreferredSize(new java.awt.Dimension(0, 20));
        filterfld.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                filterfldFocusGained(evt);
            }
        });
        filterfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterfldActionPerformed(evt);
            }
        });
        filterfld.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                filterfldKeyTyped(evt);
            }
        });

        itemcodetxtfld.setEditable(false);
        itemcodetxtfld.setForeground(new java.awt.Color(0, 0, 0));

        itemnametxtfld.setEditable(false);
        itemnametxtfld.setForeground(new java.awt.Color(0, 0, 0));

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("ITEM NAME:");

        unitpricetxtfld.setEditable(false);
        unitpricetxtfld.setForeground(new java.awt.Color(0, 0, 0));

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("UNIT PRICE:");

        quantitytxtfld.setForeground(new java.awt.Color(0, 0, 0));
        quantitytxtfld.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                quantitytxtfldFocusGained(evt);
            }
        });
        quantitytxtfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantitytxtfldActionPerformed(evt);
            }
        });

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("QUANTITY:");

        filtertable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        filterscroll.setViewportView(filtertable);

        addtobtn.setText("Add to Receipt >>");
        addtobtn.setEnabled(false);
        addtobtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addtobtnActionPerformed(evt);
            }
        });

        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("SUBTOTAL:");

        subtotaltxtfld.setEditable(false);
        subtotaltxtfld.setForeground(new java.awt.Color(0, 0, 0));

        printreceiptbtn.setBackground(new java.awt.Color(51, 255, 51));
        printreceiptbtn.setForeground(new java.awt.Color(0, 0, 0));
        printreceiptbtn.setText("Print Receipt");
        printreceiptbtn.setBorder(null);
        printreceiptbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printreceiptbtnActionPerformed(evt);
            }
        });

        totalduetxtfld.setEditable(false);
        totalduetxtfld.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        totalduetxtfld.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        totalduetxtfld.setText("0.0");
        totalduetxtfld.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 0, 0), new java.awt.Color(204, 0, 204)));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("TOTAL DUE:");

        closebtn.setBackground(new java.awt.Color(255, 102, 102));
        closebtn.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        closebtn.setForeground(new java.awt.Color(255, 0, 0));
        closebtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        closebtn.setText("X");
        closebtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closebtnMouseClicked(evt);
            }
        });

        loginadmin.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        loginadmin.setForeground(new java.awt.Color(102, 255, 0));
        loginadmin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginadmin.setText("Admin");
        loginadmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginadminMouseClicked(evt);
            }
        });

        searchlbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/searchlogo.png"))); // NOI18N
        searchlbl.setIconTextGap(0);
        searchlbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchlblMouseClicked(evt);
            }
        });

        removebtn.setText("<< Remove Item");
        removebtn.setEnabled(false);
        removebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removebtnActionPerformed(evt);
            }
        });

        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("TOTAL SALE:");

        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("DISCOUNT:");

        grosssaletxtfld.setEditable(false);
        grosssaletxtfld.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        grosssaletxtfld.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        grosssaletxtfld.setText("0.0");

        discounttxtfld.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        discounttxtfld.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        discounttxtfld.setText("0.0");
        discounttxtfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discounttxtfldActionPerformed(evt);
            }
        });

        discardbtn.setBackground(new java.awt.Color(255, 51, 51));
        discardbtn.setForeground(new java.awt.Color(0, 0, 0));
        discardbtn.setText("Discard");
        discardbtn.setBorder(null);
        discardbtn.setPreferredSize(new java.awt.Dimension(73, 16));
        discardbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discardbtnActionPerformed(evt);
            }
        });

        jLabel14.setBackground(new java.awt.Color(255, 255, 51));
        jLabel14.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("PETERUSH GENERAL MERCHANDISE");
        jLabel14.setOpaque(true);

        javax.swing.GroupLayout mainpanelLayout = new javax.swing.GroupLayout(mainpanel);
        mainpanel.setLayout(mainpanelLayout);
        mainpanelLayout.setHorizontalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainpanelLayout.createSequentialGroup()
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(totalduetxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(mainpanelLayout.createSequentialGroup()
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(itemnametxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                                                .addGap(18, 18, 18)
                                                .addComponent(itemcodetxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                                                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGap(18, 18, 18))
                                                        .addGroup(mainpanelLayout.createSequentialGroup()
                                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGap(18, 18, 18)))
                                                    .addGroup(mainpanelLayout.createSequentialGroup()
                                                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)))
                                                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(subtotaltxtfld, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                                                    .addComponent(quantitytxtfld)
                                                    .addComponent(unitpricetxtfld)
                                                    .addComponent(grosssaletxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(discounttxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addGap(148, 148, 148)
                                .addComponent(filterfld, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(searchlbl)))
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(removebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addtobtn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(filterscroll, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(mainpanelLayout.createSequentialGroup()
                        .addComponent(receiptscroll, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))
                    .addGroup(mainpanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(printreceiptbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(discardbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(24, 24, 24)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                .addComponent(loginadmin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mainpanelLayout.setVerticalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(closebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loginadmin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainpanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(receiptscroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(mainpanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(filterfld, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchlbl))
                        .addGap(18, 18, 18)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                                .addComponent(itemcodetxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(itemnametxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(unitpricetxtfld, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quantitytxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(subtotaltxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(addtobtn)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(grosssaletxtfld))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(discounttxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(removebtn)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(filterscroll, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalduetxtfld, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(printreceiptbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(discardbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closebtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closebtnMouseClicked
        if(buyitemstbl.getRowCount()>0){
           int cl=JOptionPane.showConfirmDialog(rootPane, "Close without saving", "Transaction Not Yet Saved!", JOptionPane.YES_NO_OPTION);
           if(cl==JOptionPane.YES_OPTION){
               this.dispose();
           }
        }
        else
            this.dispose();
    }//GEN-LAST:event_closebtnMouseClicked

    private void loginadminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginadminMouseClicked
        this.setEnabled(false);
        new AdminLoginForm(this);
    }//GEN-LAST:event_loginadminMouseClicked

    private void filterfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterfldActionPerformed
        filterItems();
        if(filtertable.getRowCount()==1){
            itemcodetxtfld.setText(filtertable.getValueAt(0, 0).toString());
            itemnametxtfld.setText(filtertable.getValueAt(0, 1).toString());
            unitpricetxtfld.setText(filtertable.getValueAt(0, 2).toString());
            quantitytxtfld.setText("1");
            subtotaltxtfld.setText(String.valueOf(Float.parseFloat(unitpricetxtfld.getText())*Integer.parseInt(quantitytxtfld.getText())));
            quantitytxtfld.requestFocus();
            addtobtn.setEnabled(true);
        }
    }//GEN-LAST:event_filterfldActionPerformed

    private void addtobtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addtobtnActionPerformed
        float totaldue=0.0f;
        float totalsale=0.0f;
        try{
            Integer.parseInt(quantitytxtfld.getText().trim());
            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Invalid Quantity Input");
        }
        try{
            Float.parseFloat(discounttxtfld.getText().trim());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "Invalid Discount Input");
        }
        if(Integer.parseInt(quantitytxtfld.getText().trim())>Integer.parseInt(filtertable.getValueAt(0, 3).toString()))
                JOptionPane.showMessageDialog(rootPane, "Insufficient Stock");
        else{
            updateBuysTable();
            int i=0;
            for(i=0;i<buyitemstbl.getRowCount();i++){
                totalsale=totalsale+Float.valueOf(buyitemstbl.getValueAt(i, 4).toString());
                totaldue=totaldue+Float.valueOf(buyitemstbl.getValueAt(i, 4).toString());
            }
            totaldue=totaldue-Float.valueOf(discounttxtfld.getText());
            totalduetxtfld.setText(String.valueOf(totaldue));
            grosssaletxtfld.setText(String.valueOf(totalsale));
            int left=Integer.parseInt(filtertable.getValueAt(0, 3).toString())-Integer.parseInt(quantitytxtfld.getText());
            filtertable.setValueAt(left, 0, 3);
            addtobtn.setEnabled(false);
            resetflds();
        }
    }//GEN-LAST:event_addtobtnActionPerformed

    private void removebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removebtnActionPerformed
        float totaldue=0.0f;
        float totalsale=0.0f;
        int left;
        left=Integer.parseInt(filtertable.getValueAt(0, 3).toString())+Integer.parseInt(buyitemstbl.getValueAt(buyitemstbl.getSelectedRow(), 1).toString());
        filtertable.setValueAt(left, 0, 3);
        buymdl.removeRow(buyitemstbl.getSelectedRow());
        for(int i=0;i<buyitemstbl.getRowCount();i++){
            totalsale=totalsale+Float.valueOf(buyitemstbl.getValueAt(i, 4).toString());
            totaldue=totaldue+Float.valueOf(buyitemstbl.getValueAt(i, 4).toString());
        }
        totaldue=totaldue-Float.valueOf(discounttxtfld.getText());
        totalduetxtfld.setText(String.valueOf(totaldue));
        grosssaletxtfld.setText(String.valueOf(totalsale));
        removebtn.setEnabled(false);
        filterfld.requestFocus();
    }//GEN-LAST:event_removebtnActionPerformed

    private void searchlblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchlblMouseClicked
        filterItems();
        if(filtertable.getRowCount()==1){
            itemcodetxtfld.setText(filtertable.getValueAt(0, 0).toString());
            itemnametxtfld.setText(filtertable.getValueAt(0, 1).toString());
            unitpricetxtfld.setText(filtertable.getValueAt(0, 2).toString());
            quantitytxtfld.setText("1");
            subtotaltxtfld.setText(String.valueOf(Float.parseFloat(unitpricetxtfld.getText())*Integer.parseInt(quantitytxtfld.getText())));
            quantitytxtfld.requestFocus();
        }
    }//GEN-LAST:event_searchlblMouseClicked

    private void buyitemstblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buyitemstblMouseClicked
        if(buyitemstbl.getSelectedRowCount()==1){
            removebtn.setEnabled(true);
            filterfld.setText(buyitemstbl.getValueAt(buyitemstbl.getSelectedRow(), 0).toString());
            filterItems();
        
        }
    }//GEN-LAST:event_buyitemstblMouseClicked

    private void filterfldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_filterfldFocusGained
        filterfld.setText("");
    }//GEN-LAST:event_filterfldFocusGained

    private void quantitytxtfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantitytxtfldActionPerformed
        subtotaltxtfld.setText(String.valueOf(Float.parseFloat(unitpricetxtfld.getText())*Integer.parseInt(quantitytxtfld.getText())));
        addtobtn.doClick();
        filterfld.requestFocus();
    }//GEN-LAST:event_quantitytxtfldActionPerformed

    private void printreceiptbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printreceiptbtnActionPerformed
        if(buyitemstbl.getRowCount()>0){
            int rcpId=0;
            int itemId=0;
            int stockId=0;
            try{
                pst=conn.prepareStatement("INSERT INTO Receiptstbl(Receipt_DateTime,Gross_Amount,Discount,Total_Due) VALUES(?,?,?,?)");
                pst.setString(1, new SimpleDateFormat("MMMM/dd/yyyy HH:mm:ss").format(new java.util.Date()));
                pst.setFloat(2, Float.parseFloat(grosssaletxtfld.getText()));
                pst.setFloat(3, Float.parseFloat(discounttxtfld.getText()));
                pst.setFloat(4, Float.parseFloat(totalduetxtfld.getText()));
                pst.executeUpdate();
            
                pst=conn.prepareStatement("SELECT Receipt_ID FROM Receiptstbl");
                rs=pst.executeQuery();
                while(rs.next()){
                    if(rcpId<rs.getInt("Receipt_ID"))
                        rcpId=rs.getInt("Receipt_ID");
                }
                receiptno.setText(String.valueOf(rcpId));
                datelbl.setText(new SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date()));
                for(int c=0;c<4;c++){
                    String pr="";
                    String lbl="";

                        if(c==0){
                            pr=".......................";
                            lbl="...........................................";
                        }
                        if(c==1){
                            pr=grosssaletxtfld.getText();
                           lbl="TOTAL SALE:";
                        }
                        if(c==2){
                            pr=discounttxtfld.getText();
                            lbl="DISCOUNT:";
                        }
                        if(c==3){
                            pr=totalduetxtfld.getText();
                            lbl="TOTAL DUE:";
                       }
                    buyitem[0]="";
                    buyitem[1]="";
                    buyitem[2]=lbl;
                    buyitem[3]="";
                    buyitem[4]=pr;
                    buyitem[5]="";
                    buymdl.addRow(buyitem);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        
            for(int i=0;i<buyitemstbl.getRowCount();i++){   
                int sold=0;
                int left=0;
                todbsales[0]=buyitemstbl.getValueAt(i, 0);
                todbsales[1]=buyitemstbl.getValueAt(i, 1);
                todbsales[2]=buyitemstbl.getValueAt(i, 3);
                todbsales[3]=buyitemstbl.getValueAt(i, 4);
                todbsales[4]=buyitemstbl.getValueAt(i, 5);
            
                try{
                    pst=conn.prepareStatement("SELECT Item_ID,Stock_ID FROM Itemstbl WHERE Item_CODE='"+todbsales[0]+"'");
                    rs=pst.executeQuery();
                    if(rs.next()){
                        itemId=rs.getInt("Item_ID");
                        stockId=rs.getInt("Stock_ID");
                
                        pst=conn.prepareStatement("INSERT INTO Salestbl(Item_ID,Quantity,Unit_Price,Total_Amount,Transaction_DateTime,Receipt_ID)"
                            + "VALUES(?,?,?,?,?,?)");
                        pst.setInt(1, itemId);
                        pst.setInt(2,Integer.parseInt(todbsales[1].toString()));
                        pst.setFloat(3, Float.parseFloat(todbsales[2].toString()));
                        pst.setFloat(4, Float.parseFloat(todbsales[3].toString()));
                        pst.setString(5, todbsales[4].toString());
                        pst.setInt(6, rcpId);
                        pst.executeUpdate();
                
                        pst=conn.prepareStatement("SELECT Quantity FROM Salestbl WHERE Item_ID ='"+itemId+"'");
                        rs=pst.executeQuery();
                        while(rs.next()){
                           sold=sold+rs.getInt("Quantity");
                        }
                        pst=conn.prepareStatement("SELECT Total FROM Quantitytbl WHERE Stock_ID ='"+stockId+"'");
                        rs=pst.executeQuery();
                        rs.next();
                        left=rs.getInt("Total")-sold;
                
                        pst=conn.prepareStatement("UPDATE Quantitytbl SET Sold=?,Remaining=? WHERE Stock_ID ='"+stockId+"'");
                        pst.setInt(1, sold);
                        pst.setInt(2, left);
                        pst.executeUpdate();
                    }
                
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            //Export Receipt as Image File
            BufferedImage image = new BufferedImage(receiptpanel.getWidth(), receiptpanel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            receiptpanel.printAll(g);
            new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/").mkdir();
            new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/Receipts/").mkdir();
            File receipt=new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/Receipts/ReceiptNo"+String.valueOf(rcpId)+".png");
            try { 
                ImageIO.write(image, "png", receipt); 
                //Print The Exported Image of Receipt
                PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
                pras.add(new Copies(1));
                PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.PNG, pras);
                if (pss.length == 0)
                    JOptionPane.showMessageDialog(rootPane,"No Printer Availabel");
                PrintService ps = pss[0];
                JOptionPane.showMessageDialog(rootPane,"Printing to " + ps);
                DocPrintJob job = ps.createPrintJob();
                FileInputStream fin;
                fin = new FileInputStream(receipt);
                Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.PNG, null);
                job.print(doc, pras);
                fin.close();
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, "Problem occurred!");
            }
            int keep=JOptionPane.showConfirmDialog(rootPane, "Keep an Image File of this Receipt?","Export As Image",JOptionPane.YES_NO_OPTION);
            
                if(keep==JOptionPane.NO_OPTION){
                    try{
                        Files.delete(Paths.get(receipt.getAbsolutePath()));//to delete exported receipt image
                        }
                    catch(Exception e){
                
                     }
                }
                else
                    JOptionPane.showMessageDialog(rootPane, "Exported File at \n/Documents/Receipts/BarcodeApp/Receipts");
            discardbtn.doClick();
        }
        else{
            JOptionPane.showMessageDialog(rootPane, "Nothing to Print");
        }
    }//GEN-LAST:event_printreceiptbtnActionPerformed

    private void filterfldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_filterfldKeyTyped
        filterItems();
        if(filtertable.getRowCount()==1){
            itemcodetxtfld.setText(filtertable.getValueAt(0, 0).toString());
            itemnametxtfld.setText(filtertable.getValueAt(0, 1).toString());
            unitpricetxtfld.setText(filtertable.getValueAt(0, 2).toString());
            quantitytxtfld.setText("1");
            subtotaltxtfld.setText(String.valueOf(Float.parseFloat(unitpricetxtfld.getText())*Integer.parseInt(quantitytxtfld.getText())));
            quantitytxtfld.requestFocus();
        }
    }//GEN-LAST:event_filterfldKeyTyped

    private void discounttxtfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discounttxtfldActionPerformed
        addtobtn.doClick();
        float discount=0.0f;
        try{
            discount=Float.parseFloat(discounttxtfld.getText());
            if(discount<0){
                discounttxtfld.setText("0.0");
            }
            else{
                float totaldue=Float.parseFloat(grosssaletxtfld.getText())-discount;
                totalduetxtfld.setText(String.valueOf(totaldue));
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(discounttxtfld, "Invalid Input");
        }
    }//GEN-LAST:event_discounttxtfldActionPerformed

    private void quantitytxtfldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_quantitytxtfldFocusGained
        addtobtn.setEnabled(true);
    }//GEN-LAST:event_quantitytxtfldFocusGained

    private void discardbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discardbtnActionPerformed
        buymdl.setRowCount(0);
        itemsmdl.setRowCount(0);
        showFilterTable();
        grosssaletxtfld.setText("0.0");
        discounttxtfld.setText("0.0");
        totalduetxtfld.setText("0.0");
        datelbl.setText("Date");
        receiptno.setText("0000");
        
    }//GEN-LAST:event_discardbtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ScannerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ScannerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ScannerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ScannerForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScannerForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addtobtn;
    private javax.swing.JTable buyitemstbl;
    private javax.swing.JLabel closebtn;
    private javax.swing.JLabel datelbl;
    private javax.swing.JButton discardbtn;
    private javax.swing.JTextField discounttxtfld;
    private javax.swing.JTextField filterfld;
    private javax.swing.JScrollPane filterscroll;
    private javax.swing.JTable filtertable;
    private javax.swing.JTextField grosssaletxtfld;
    private javax.swing.JTextField itemcodetxtfld;
    private javax.swing.JTextField itemnametxtfld;
    private javax.swing.JPanel itemslistpanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel loginadmin;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JButton printreceiptbtn;
    private javax.swing.JTextField quantitytxtfld;
    private javax.swing.JLabel receiptno;
    private javax.swing.JPanel receiptpanel;
    private javax.swing.JScrollPane receiptscroll;
    private javax.swing.JButton removebtn;
    private javax.swing.JLabel searchlbl;
    private javax.swing.JTextField subtotaltxtfld;
    private javax.swing.JTextField totalduetxtfld;
    private javax.swing.JTextField unitpricetxtfld;
    // End of variables declaration//GEN-END:variables
}
