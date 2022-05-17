/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package barcodeclass;
import java.awt.Font;
import java.io.File;
import java.sql.*;
import javax.swing.JOptionPane;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
/**
 *
 * @author Janine
 */
public class AddItemDlg extends javax.swing.JDialog {

    /**
     * Creates new form AddItemDlg
     */
    public AddItemDlg(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.ad=(AdminsForm) parent;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public AddItemDlg(java.awt.Frame parent, boolean modal, String[] edit) {
        super(parent, modal);
        this.ad=(AdminsForm) parent;
        this.originalstock=Integer.parseInt(edit[6]);
        initComponents();
        setTextFlds(edit);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    AdminsForm ad;
    int originalstock=0;
    
    void setTextFlds(String[] v){
        itemcodefld.setText(v[0]);
        itemnamefld.setText(v[1]);
        classfld.setText(v[2]);
        specfld.setText(v[3]);
        buyfld.setText(v[4]);
        sellfld.setText(v[5]);
        qntyfld.setText(v[6]);
        addbtn.setText("Update");
    }
    void addNewItem(){
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        int priceId=0;
        int stockId=0;
        int itemId=0;
        
        
        String itemname=itemnamefld.getText().trim();
        String classification=classfld.getText().trim();
        String specification=specfld.getText().trim();
        float buy=0.0f;
        float sell=0.0f;
        boolean save1=false;
        boolean save2=false;
        boolean save3=false;
        int quantity=0;
         if(itemname.equals("")){
                JOptionPane.showMessageDialog(rootPane, "Item Name is Required!");
            }
            else if(classification.equals("")){
                JOptionPane.showMessageDialog(rootPane, "Classification is Required!");
            }
            else if(specification.equals("")){
                JOptionPane.showMessageDialog(rootPane, "Specification is Required!");
            }
            else{
                try{
                    try{
                        buy=Float.parseFloat(buyfld.getText().trim());
                        save1=true;
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(rootPane, "Invalid Buying Price!");
                    }
                    try{
                        sell=Float.parseFloat(sellfld.getText().trim());
                        save2=true;
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(rootPane, "Invalid Selling Price!");
                    }
                    try{
                        quantity=Integer.parseInt(qntyfld.getText());
                        save3=true;
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(rootPane, "Invalid Quantity");
                    }
                    if(save1 && save2 && save3){
                        con=DriverManager.getConnection("jdbc:sqlite:src//mainDB.db");
                        
                        String qry="INSERT INTO Itemstbl(Item_Name,Item_Classification,Item_Specification) VALUES(?,?,?)";
                        pst=con.prepareStatement(qry);
                        pst.setString(1, itemname);
                        pst.setString(2, classification);
                        pst.setString(3, specification);
                        pst.executeUpdate();
            
                        qry="INSERT INTO Pricetbl(Buying_Price,Selling_Price) VALUES(?,?)";
                        pst=con.prepareStatement(qry);
                        pst.setFloat(1, buy);
                        pst.setFloat(2, sell);
                        pst.executeUpdate();
                    
                        qry="INSERT INTO Quantitytbl(Total,Sold,Remaining) VALUES(?,?,?)";
                        pst=con.prepareStatement(qry);
                        pst.setInt(1, quantity);
                        pst.setInt(2,0);
                        pst.setInt(3,quantity);
                        pst.executeUpdate();
                    
                        qry="SELECT Price_ID FROM Pricetbl";
                        pst=con.prepareStatement(qry);
                        rs=pst.executeQuery();
                        while(rs.next()){
                            if(priceId<rs.getInt("Price_ID")){
                                priceId=rs.getInt("Price_ID");
                            }
                        }
                    
                        qry="SELECT Stock_ID FROM Quantitytbl";
                        pst=con.prepareStatement(qry);
                        rs=pst.executeQuery();
                        while(rs.next()){
                            if(stockId<rs.getInt("Stock_ID")){
                                stockId=rs.getInt("Stock_ID");
                            }
                        }
                    
                        qry="SELECT Item_ID FROM Itemstbl";
                        pst=con.prepareStatement(qry);
                        rs=pst.executeQuery();
                        while(rs.next()){
                            if(itemId<rs.getInt("Item_ID")){
                               itemId=rs.getInt("Item_ID");
                            }
                        }
                    
                        String code=itemId+"#"+itemname;
                        while(code.length()<10)
                            code=code+"0";
                        if(code.length()>10){
                            code=code.substring(0, 9);
                        }
                    
                        qry="UPDATE Itemstbl SET Item_Code='"+code+"',Price_ID='"+priceId+"',Stock_ID='"+stockId+"' WHERE Item_ID='"+itemId+"'";
                        pst=con.prepareStatement(qry);
                        pst.executeUpdate();
                        con.close();
                        
                        JOptionPane.showMessageDialog(rootPane, "Added Succcessfully!");
                        itemcodefld.setText(code);
                        addbtn.setText("Add New Item");
                        itemnamefld.setEditable(false);
                        classfld.setEditable(false);
                        specfld.setEditable(false);
                        buyfld.setEditable(false);
                        sellfld.setEditable(false);
                        qntyfld.setEditable(false);
                        barcodebtn.setEnabled(true);
                        ad.itemsmodel.setRowCount(0);
                        ad.updateItemsTable();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
    }
    void updateRecord(){
        Connection con;
        PreparedStatement pst;
        ResultSet rs;
        int priceId=0;
        int stockId=0;
        
        String itemname=itemnamefld.getText().trim();
        String classification=classfld.getText().trim();
        String specification=specfld.getText().trim();
        float buy=0.0f;
        float sell=0.0f;
        boolean save1=false;
        boolean save2=false;
        boolean save3=false;
        int quantity=0;
        int sold=0;
            if(itemname.equals("")){
                JOptionPane.showMessageDialog(rootPane, "Item Name is Required!");
            }
            else if(classification.equals("")){
                JOptionPane.showMessageDialog(rootPane, "Classification is Required!");
            }
            else if(specification.equals("")){
                JOptionPane.showMessageDialog(rootPane, "Specification is Required!");
            }
            else{
                try{
                    try{
                        buy=Float.parseFloat(buyfld.getText().trim());
                        save1=true;
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(rootPane, "Invalid Buying Price!");
                    }
                    try{
                        sell=Float.parseFloat(sellfld.getText().trim());
                        save2=true;
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(rootPane, "Invalid Selling Price!");
                    }
                    try{
                        quantity=Integer.parseInt(qntyfld.getText());
                        save3=true;
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(rootPane, "Invalid Quantity");
                    }
                    if(save1 && save2 && save3){
                        con=DriverManager.getConnection("jdbc:sqlite:src//mainDB.db");
                        
                        String qry="UPDATE Itemstbl SET Item_Name=?,Item_Classification=?,Item_Specification=? WHERE Item_Code='"+itemcodefld.getText()+"'";
                        pst=con.prepareStatement(qry);
                        pst.setString(1, itemname);
                        pst.setString(2, classification);
                        pst.setString(3, specification);
                        pst.executeUpdate();
                        
                        qry="SELECT Price_ID,Stock_ID FROM Itemstbl WHERE Item_Code='"+itemcodefld.getText()+"'";
                        pst=con.prepareStatement(qry);
                        rs=pst.executeQuery();
                        rs.next();
                        priceId=rs.getInt("Price_ID");
                        stockId=rs.getInt("Stock_ID");
                        
                        qry="SELECT Sold FROM Quantitytbl WHERE Stock_ID='"+stockId+"'";
                        pst=con.prepareStatement(qry);
                        rs=pst.executeQuery();
                        rs.next();
                        sold=rs.getInt("Sold"); 
                        
                        qry="UPDATE Pricetbl SET Buying_Price=?,Selling_Price=? WHERE Price_ID='"+priceId+"'";
                        pst=con.prepareStatement(qry);
                        pst.setFloat(1, buy);
                        pst.setFloat(2, sell);
                        pst.executeUpdate();
                        
                        qry="UPDATE Quantitytbl SET Total=?,Remaining=? WHERE Stock_ID='"+stockId+"'";
                        pst=con.prepareStatement(qry);
                        if(quantity>=sold){
                            pst.setInt(1, quantity);
                            pst.setInt(2, quantity-sold);
                        }
                        else{
                            quantity=sold;
                            pst.setInt(1, quantity);
                            pst.setInt(2, quantity-sold);
                        }
                        pst.executeUpdate();
                        con.close();

                        JOptionPane.showMessageDialog(rootPane, "Succcessfully Updated!");
                        addbtn.setText("Add New Item");
                        itemnamefld.setEditable(false);
                        classfld.setEditable(false);
                        specfld.setEditable(false);
                        buyfld.setEditable(false);
                        sellfld.setEditable(false);
                        qntyfld.setEditable(false);
                        barcodebtn.setEnabled(true);
                        ad.itemsmodel.setRowCount(0);
                        ad.updateItemsTable();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        
    }
    void exportAsImage(String barcodename){
        new File(System.getProperty("user.home").toString()+"/Documents/Generated_Barcodes/").mkdir();
        try {
            Barcode barcode = BarcodeFactory.createEAN128(barcodename);  
            File barcodepic=new File(System.getProperty("user.home").toString()+"/Documents/Generated_Barcodes/"+barcodename+".png");
            barcode.setBarWidth(1);
            barcode.setBarHeight(20);
            barcode.setFont(new Font(Font.DIALOG,Font.PLAIN, 10));
            BarcodeImageHandler.savePNG(barcode, barcodepic);
        }           
        catch (Exception e) {

        }
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
        itemcodefld = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        itemnamefld = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        classfld = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        specfld = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        buyfld = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        sellfld = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        qntyfld = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        addbtn = new javax.swing.JButton();
        closeAdditemdlg = new javax.swing.JLabel();
        barcodebtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        mainpanel.setBackground(new java.awt.Color(204, 51, 255));

        itemcodefld.setEditable(false);
        itemcodefld.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel1.setText("Item Name:");

        itemnamefld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemnamefldActionPerformed(evt);
            }
        });

        jLabel2.setText("Classification:");

        classfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classfldActionPerformed(evt);
            }
        });

        jLabel3.setText("Specification:");

        specfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                specfldActionPerformed(evt);
            }
        });

        jLabel4.setText("Buying Price");

        buyfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buyfldActionPerformed(evt);
            }
        });

        jLabel5.setText("Selling Price");

        sellfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellfldActionPerformed(evt);
            }
        });

        jLabel6.setText("Quantity:");

        qntyfld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qntyfldActionPerformed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Item Code");

        addbtn.setBackground(new java.awt.Color(51, 255, 51));
        addbtn.setText("Add Item");
        addbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnActionPerformed(evt);
            }
        });

        closeAdditemdlg.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        closeAdditemdlg.setForeground(new java.awt.Color(255, 0, 0));
        closeAdditemdlg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        closeAdditemdlg.setText("X");
        closeAdditemdlg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeAdditemdlgMouseClicked(evt);
            }
        });

        barcodebtn.setBackground(new java.awt.Color(51, 51, 255));
        barcodebtn.setText("Generate Barcode");
        barcodebtn.setEnabled(false);
        barcodebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainpanelLayout = new javax.swing.GroupLayout(mainpanel);
        mainpanel.setLayout(mainpanelLayout);
        mainpanelLayout.setHorizontalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                .addComponent(closeAdditemdlg, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainpanelLayout.createSequentialGroup()
                            .addGap(50, 50, 50)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(itemnamefld, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(mainpanelLayout.createSequentialGroup()
                            .addGap(190, 190, 190)
                            .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(itemcodefld)
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))))
                    .addGroup(mainpanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(sellfld, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(buyfld, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(specfld, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(classfld, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainpanelLayout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainpanelLayout.createSequentialGroup()
                                        .addComponent(addbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(barcodebtn))
                                    .addComponent(qntyfld, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(50, 50, 50))
        );
        mainpanelLayout.setVerticalGroup(
            mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(closeAdditemdlg, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemcodefld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemnamefld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(classfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(specfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buyfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sellfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(qntyfld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(26, 26, 26)
                .addGroup(mainpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(barcodebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnActionPerformed
        if(addbtn.getText().equals("Add Item")){
           addNewItem();
        }
        else if(addbtn.getText().equals("Add New Item")){
            itemcodefld.setText("");
            itemnamefld.setText("");
            classfld.setText("");
            specfld.setText("");
            buyfld.setText("");
            sellfld.setText("");
            qntyfld.setText("");
            itemnamefld.setEditable(true);
            classfld.setEditable(true);
            specfld.setEditable(true);
            buyfld.setEditable(true);
            sellfld.setEditable(true);
            qntyfld.setEditable(true);
            barcodebtn.setEnabled(false);
            addbtn.setText("Add Item");
        } 
        else{
            updateRecord();
        }
    }//GEN-LAST:event_addbtnActionPerformed

    private void closeAdditemdlgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeAdditemdlgMouseClicked
        ad.setEnabled(true);
        ad.itemsmodel.setRowCount(0);
        ad.updateItemsTable();
        ad.requestFocus();
        this.dispose();
    }//GEN-LAST:event_closeAdditemdlgMouseClicked

    private void barcodebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodebtnActionPerformed
        new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/").mkdir();
        new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/Generated_Barcodes/").mkdir();
        String barcodename=itemcodefld.getText();
        try {
            Barcode barcode = BarcodeFactory.createCode128(barcodename);  
            File barcodepic=new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/Generated_Barcodes/"+barcodename+".jpeg");
            barcode.setBarWidth(1);
            barcode.setBarHeight(20);
            barcode.setLabel(barcodename);
            barcode.setFont(new Font(Font.DIALOG,Font.PLAIN, 10));
            BarcodeImageHandler.saveJPEG(barcode, barcodepic);
            new PrintBarcodeDlg(this,barcodepic,qntyfld.getText());
            this.setEnabled(false);
        }           
        catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Failed to Generate Barcode!");
            e.printStackTrace();
        }
        
    }//GEN-LAST:event_barcodebtnActionPerformed

    private void itemnamefldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemnamefldActionPerformed
        classfld.requestFocus();
    }//GEN-LAST:event_itemnamefldActionPerformed

    private void classfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classfldActionPerformed
        specfld.requestFocus();
    }//GEN-LAST:event_classfldActionPerformed

    private void specfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_specfldActionPerformed
        buyfld.requestFocus();
    }//GEN-LAST:event_specfldActionPerformed

    private void buyfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buyfldActionPerformed
        sellfld.requestFocus();
    }//GEN-LAST:event_buyfldActionPerformed

    private void sellfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellfldActionPerformed
        qntyfld.requestFocus();
    }//GEN-LAST:event_sellfldActionPerformed

    private void qntyfldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qntyfldActionPerformed
        addbtn.doClick();
    }//GEN-LAST:event_qntyfldActionPerformed

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
            java.util.logging.Logger.getLogger(AddItemDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddItemDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddItemDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddItemDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                /*AddItemDlg dialog = new AddItemDlg(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);*/
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addbtn;
    private javax.swing.JButton barcodebtn;
    private javax.swing.JTextField buyfld;
    private javax.swing.JTextField classfld;
    private javax.swing.JLabel closeAdditemdlg;
    private javax.swing.JTextField itemcodefld;
    private javax.swing.JTextField itemnamefld;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JTextField qntyfld;
    private javax.swing.JTextField sellfld;
    private javax.swing.JTextField specfld;
    // End of variables declaration//GEN-END:variables
}
