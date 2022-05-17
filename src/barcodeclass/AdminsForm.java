/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package barcodeclass;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 *
 * @author Janine
 */
public class AdminsForm extends javax.swing.JFrame {

    /**
     * Creates new form SalesForm
     */
    public AdminsForm() {
        initComponents();
        setAllTables();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        connectDB();
        updateAllTables();
    }
    
    public AdminsForm(ScannerForm scan) {
        this.sf=scan;
        this.openscan=true;
        initComponents();
        setAllTables();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        connectDB();
        updateAllTables();
    }
    
    ScannerForm sf;
    boolean openscan=false;
    DefaultTableModel itemsmodel=new DefaultTableModel();
    DefaultTableModel salesmodel=new DefaultTableModel();
    DefaultTableModel receiptsmodel=new DefaultTableModel();
    DefaultTableModel returneditemsmodel=new DefaultTableModel();
    TableRowSorter itemsorter=new TableRowSorter<DefaultTableModel>(itemsmodel);
    TableRowSorter salesorter=new TableRowSorter<DefaultTableModel>(salesmodel);
    TableRowSorter receiptsorter=new TableRowSorter<DefaultTableModel>(receiptsmodel);
    TableRowSorter returneditemsorter=new TableRowSorter<DefaultTableModel>(returneditemsmodel);
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    void connectDB(){
        try{
            con=DriverManager.getConnection("jdbc:sqlite:src//mainDB.db");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(maintabpane, e);
        }
    }
    void setAllTables(){
        setitemsTable();
        setsalesTable();
        setreceiptsTable();
        setReturnedItemsTable();
    }
    
    void updateAllTables(){
        updateItemsTable();
        updateSalesTable();
        updateReceiptsTable();
        updateReturnedItemsTable();
        
    }
    
    void setitemsTable(){
        String[] itemscolumns={"Item Code","Item Name","Classification","Specification","Buying Price","Selling Price",
                                "Total Stock","Sold","Available","Total Cost","Market Value"
                              };
        itemsmodel.setColumnIdentifiers(itemscolumns);
        itemstbl.setModel(itemsmodel);
        for(int i=0; i<itemstbl.getColumnCount();i++){
            itemstbl.setDefaultEditor(itemstbl.getColumnClass(i), null);
        }
        itemstbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemstbl.setRowSorter(itemsorter);
    }
    
    void setsalesTable(){
        String[] salescolumns={"Sales ID","Item_Code","Item Name","Specification","Quantity","Unit Price","Total","Transaction Date", "Receipt #"};
        salesmodel.setColumnIdentifiers(salescolumns);
        salestbl.setModel(salesmodel);
        for(int i=0; i<salestbl.getColumnCount();i++){
            salestbl.setDefaultEditor(salestbl.getColumnClass(i), null);
        }
        salestbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        salestbl.setRowSorter(salesorter);
    }
    
    void setreceiptsTable(){
        String[] receiptscolumns={"Receipt Number","Date","Gross Amount","Discounts","Total Due"};
        receiptsmodel.setColumnIdentifiers(receiptscolumns);
        receiptstbl.setModel(receiptsmodel);
        for(int i=0; i<receiptstbl.getColumnCount();i++){
            receiptstbl.setDefaultEditor(receiptstbl.getColumnClass(i), null);
        }
        receiptstbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        receiptstbl.setRowSorter(receiptsorter);
    }
    
    void setReturnedItemsTable(){
        String[] rtditemscolumns={"Receipt #","Sales ID","Item Code","Item Name","Date of Buying","# of Unit/s Returned","Unit_Price","Total Refund","Date Returned"};
        returneditemsmodel.setColumnIdentifiers(rtditemscolumns);
        returneditemstbl.setModel(returneditemsmodel);
        for(int i=0; i<returneditemstbl.getColumnCount();i++){
            returneditemstbl.setDefaultEditor(returneditemstbl.getColumnClass(i), null);
        }
        returneditemstbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        returneditemstbl.setRowSorter(returneditemsorter);
    }
    
    void updateItemsTable(){
        Object items[]=new Object[11];
        try{
            pst=con.prepareStatement("Select * from Itemstbl join Pricetbl on Itemstbl.Price_ID=Pricetbl.Price_ID join Quantitytbl on "
                    + "Itemstbl.Stock_ID=Quantitytbl.Stock_ID");
            rs=pst.executeQuery();
            while(rs.next()){
                items[0]=rs.getString("Item_Code");
                items[1]=rs.getString("Item_Name");
                items[2]=rs.getString("Item_Classification");
                items[3]=rs.getString("Item_Specification");
                items[4]=rs.getFloat("Buying_Price");
                items[5]=rs.getFloat("Selling_Price");
                items[6]=rs.getInt("Total");
                items[7]=rs.getInt("Sold");
                items[8]=rs.getInt("Remaining");
                items[9]=(rs.getInt("Remaining")*rs.getFloat("Buying_Price"));
                items[10]=(rs.getInt("Remaining")*rs.getFloat("Selling_Price"));
                itemsmodel.addRow(items);
            }
            showcompute(itemstbl,overallcost,9);
            showcompute(itemstbl, overallmvalue,10);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }
    
    void updateSalesTable(){
        Object sales[]=new Object[9];
        try{
            pst=con.prepareStatement("Select * from Salestbl join Itemstbl on Salestbl.Item_ID =Itemstbl.Item_ID"
                    + " join Pricetbl on Itemstbl.Price_ID=Pricetbl.Price_ID");
            rs=pst.executeQuery();
            while(rs.next()){
                sales[0]=rs.getInt("Sales_ID");
                sales[1]=rs.getString("Item_Code");
                sales[2]=rs.getString("Item_Name");
                sales[3]=rs.getString("Item_Specification");
                sales[4]=rs.getInt("Quantity");
                sales[5]=rs.getFloat("Unit_Price");
                sales[6]=rs.getFloat("Total_Amount");
                sales[7]=rs.getString("Transaction_DateTime");
                sales[8]=rs.getString("Receipt_ID");
                salesmodel.addRow(sales);
            }
            showcompute(salestbl, tsales, 6);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }
    
    void updateReceiptsTable(){
        Object rcpt[]=new Object[5];
        try{
            pst=con.prepareStatement("Select * from Receiptstbl");
            rs=pst.executeQuery();
            while(rs.next()){
                rcpt[0]=rs.getInt("Receipt_ID");
                rcpt[1]=rs.getString("Receipt_DateTime");
                rcpt[2]=rs.getFloat("Gross_Amount");
                rcpt[3]=rs.getFloat("Discount");
                rcpt[4]=rs.getFloat("Total_Due");
                receiptsmodel.addRow(rcpt);
            }
            showcompute(receiptstbl, tsales1, 4);
            showcompute(receiptstbl, tsales2, 2);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
            e.printStackTrace();
        }
    }
    
    void updateReturnedItemsTable(){
        Object ritems[]=new Object[9];
        try{
            pst=con.prepareStatement("Select * from ReturnedItemstbl join Salestbl on ReturnedItemstbl.Sales_ID=Salestbl.Sales_ID"
                    + " join Itemstbl on Salestbl.Item_ID=Itemstbl.Item_ID join Pricetbl on Itemstbl.Price_ID=Pricetbl.Price_ID");
            rs=pst.executeQuery();
            while(rs.next()){
                ritems[0]=rs.getInt("Receipt_ID");
                ritems[1]=rs.getInt("Sales_ID");
                ritems[2]=rs.getString("Item_Code");
                ritems[3]=rs.getString("Item_Name");
                ritems[4]=rs.getString("Transaction_DateTime");
                ritems[5]=rs.getInt("Units_Returned");
                ritems[6]=rs.getFloat("Unit_Price");
                ritems[7]=rs.getFloat("Total_Refund");
                ritems[8]=rs.getString("Date_Returned");
                returneditemsmodel.addRow(ritems);
            }
            showcompute(returneditemstbl, treturnedsales, 7);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, e);
            e.printStackTrace();
        }
    }
    
    void filter(TableRowSorter sorter, JTextField filterfld){
            RowFilter<DefaultTableModel,Object> fltr=null;
            fltr=RowFilter.regexFilter(filterfld.getText().trim());
            sorter.setRowFilter(fltr);
    }
    
    void showcompute(JTable tbl, JTextField fld ,int col){
        int countitems=tbl.getRowCount();
        float allcost=0.0f;
        DecimalFormat df=new DecimalFormat("###,###.##");
        for(int i=0;i<countitems;i++){
           allcost=allcost+Float.parseFloat(tbl.getValueAt(i, col).toString());
        }
        fld.setText(df.format(allcost));
    }
    
     void exportToExcel(JTable table, String path) throws FileNotFoundException, IOException {
        Workbook wb = new XSSFWorkbook(); //Excell workbook
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("ItemsRecord"); //WorkSheet
        Row row = sheet.createRow(1); //Row created at line 1
        TableModel model = table.getModel(); //Table model

        org.apache.poi.ss.usermodel.Font hfont = wb.createFont();
        hfont.setBold(true);
        hfont.setFontHeightInPoints((short)12);
        CellStyle hCellStyle = wb.createCellStyle();
        hCellStyle.setFont(hfont);
        
        Row hRow = sheet.createRow(0);
        for(int headings = 0; headings < model.getColumnCount(); headings++){
            Cell c=hRow.createCell(headings);
            c.setCellValue(model.getColumnName(headings));
            c.setCellStyle(hCellStyle);
        }

        for(int rows = 0; rows < model.getRowCount(); rows++){
            for(int cols = 0; cols < table.getColumnCount(); cols++){
                row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString());
            }
            row = sheet.createRow((rows + 2)); 
        }
        for(int i=0; i<model.getColumnCount();i++)
            sheet.autoSizeColumn(i);
        
        String timecreated=new SimpleDateFormat("MMMM_dd_yyyy_HH_mm_ss").format(new java.util.Date())+".xlsx";
        FileOutputStream output=new FileOutputStream(path+timecreated);
        wb.write(output);
        wb.close();
        JOptionPane.showMessageDialog(rootPane, "Excel file Exported at\n"+path+timecreated);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerpanel = new javax.swing.JPanel();
        titlelbl = new javax.swing.JLabel();
        closelbl = new javax.swing.JLabel();
        minimizelbl = new javax.swing.JLabel();
        newadminbtn = new javax.swing.JLabel();
        tabpanel = new javax.swing.JPanel();
        maintabpane = new javax.swing.JTabbedPane();
        itemspanel = new javax.swing.JPanel();
        itemsscrollpane = new javax.swing.JScrollPane();
        itemstbl = new javax.swing.JTable();
        itemstblfooter = new javax.swing.JPanel();
        overallcost = new javax.swing.JTextField();
        overallmvalue = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        itemstblheader = new javax.swing.JPanel();
        additembtn = new javax.swing.JButton();
        editbtn = new javax.swing.JButton();
        printitemstblbtn = new javax.swing.JButton();
        exportbtn = new javax.swing.JButton();
        itemstblfilter = new javax.swing.JTextField();
        searchlbl = new javax.swing.JLabel();
        salespanel = new javax.swing.JPanel();
        salesscrollpane = new javax.swing.JScrollPane();
        salestbl = new javax.swing.JTable();
        salestablefooter = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tsales = new javax.swing.JTextField();
        salestableheader = new javax.swing.JPanel();
        printsalestblbtn = new javax.swing.JButton();
        exportbtn2 = new javax.swing.JButton();
        salestblfilter = new javax.swing.JTextField();
        searchlbl1 = new javax.swing.JLabel();
        receiptspanel = new javax.swing.JPanel();
        receiptsscrollpane = new javax.swing.JScrollPane();
        receiptstbl = new javax.swing.JTable();
        receiptstablefooter = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        tsales1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tsales2 = new javax.swing.JTextField();
        receiptstableheader1 = new javax.swing.JPanel();
        printreceiptstblbtn1 = new javax.swing.JButton();
        exportbtn3 = new javax.swing.JButton();
        receiptstblfilter = new javax.swing.JTextField();
        searchlbl2 = new javax.swing.JLabel();
        ReturnedItems = new javax.swing.JPanel();
        returnedscrollpane = new javax.swing.JScrollPane();
        returneditemstbl = new javax.swing.JTable();
        returnedtablefooter = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        treturnedsales = new javax.swing.JTextField();
        returnedtableheader = new javax.swing.JPanel();
        printreturneditemstblbtn2 = new javax.swing.JButton();
        exportbtn4 = new javax.swing.JButton();
        returneditemstblfilter = new javax.swing.JTextField();
        searchlbl3 = new javax.swing.JLabel();
        addreturneditembtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(902, 500));

        headerpanel.setBackground(new java.awt.Color(0, 0, 0));
        headerpanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(153, 0, 255), new java.awt.Color(102, 0, 102)));
        headerpanel.setPreferredSize(new java.awt.Dimension(983, 45));

        titlelbl.setBackground(new java.awt.Color(0, 0, 0));
        titlelbl.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        titlelbl.setForeground(new java.awt.Color(255, 204, 0));
        titlelbl.setText("Administrator Panel");

        closelbl.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        closelbl.setForeground(new java.awt.Color(255, 0, 0));
        closelbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        closelbl.setText("X");
        closelbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closelblMouseClicked(evt);
            }
        });

        minimizelbl.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        minimizelbl.setForeground(new java.awt.Color(0, 0, 255));
        minimizelbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimizelbl.setText("__");
        minimizelbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimizelblMouseClicked(evt);
            }
        });

        newadminbtn.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        newadminbtn.setForeground(new java.awt.Color(51, 255, 255));
        newadminbtn.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        newadminbtn.setText("+");
        newadminbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newadminbtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout headerpanelLayout = new javax.swing.GroupLayout(headerpanel);
        headerpanel.setLayout(headerpanelLayout);
        headerpanelLayout.setHorizontalGroup(
            headerpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerpanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(newadminbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(titlelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(minimizelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(closelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        headerpanelLayout.setVerticalGroup(
            headerpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(titlelbl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(headerpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(closelbl, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addComponent(minimizelbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(headerpanelLayout.createSequentialGroup()
                .addComponent(newadminbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(headerpanel, java.awt.BorderLayout.PAGE_START);

        tabpanel.setBackground(new java.awt.Color(102, 0, 255));
        tabpanel.setPreferredSize(new java.awt.Dimension(455, 500));

        maintabpane.setBackground(new java.awt.Color(0, 204, 102));
        maintabpane.setMinimumSize(new java.awt.Dimension(86, 62));
        maintabpane.setOpaque(true);

        itemspanel.setBackground(new java.awt.Color(204, 51, 255));
        itemspanel.setPreferredSize(new java.awt.Dimension(981, 483));
        itemspanel.setLayout(new java.awt.BorderLayout());

        itemstbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        itemstbl.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        itemstbl.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        itemstbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemstblMouseClicked(evt);
            }
        });
        itemsscrollpane.setViewportView(itemstbl);

        itemspanel.add(itemsscrollpane, java.awt.BorderLayout.CENTER);

        itemstblfooter.setBackground(new java.awt.Color(204, 0, 255));
        itemstblfooter.setMinimumSize(new java.awt.Dimension(981, 40));
        itemstblfooter.setPreferredSize(new java.awt.Dimension(981, 40));

        overallcost.setEditable(false);
        overallcost.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        overallcost.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        overallmvalue.setEditable(false);
        overallmvalue.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        overallmvalue.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Overall Market Value:  P");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Overall Cost:  P");

        javax.swing.GroupLayout itemstblfooterLayout = new javax.swing.GroupLayout(itemstblfooter);
        itemstblfooter.setLayout(itemstblfooterLayout);
        itemstblfooterLayout.setHorizontalGroup(
            itemstblfooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, itemstblfooterLayout.createSequentialGroup()
                .addContainerGap(416, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overallcost, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overallmvalue, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        itemstblfooterLayout.setVerticalGroup(
            itemstblfooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, itemstblfooterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(itemstblfooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(overallcost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(overallmvalue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        itemspanel.add(itemstblfooter, java.awt.BorderLayout.PAGE_END);

        itemstblheader.setBackground(new java.awt.Color(204, 51, 255));
        itemstblheader.setPreferredSize(new java.awt.Dimension(981, 40));

        additembtn.setBackground(new java.awt.Color(102, 255, 255));
        additembtn.setText("Add Item");
        additembtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                additembtnActionPerformed(evt);
            }
        });

        editbtn.setBackground(new java.awt.Color(255, 153, 51));
        editbtn.setText("Edit Info");
        editbtn.setEnabled(false);
        editbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editbtnActionPerformed(evt);
            }
        });

        printitemstblbtn.setBackground(new java.awt.Color(51, 51, 255));
        printitemstblbtn.setText("Print Table");
        printitemstblbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printitemstblbtnActionPerformed(evt);
            }
        });

        exportbtn.setBackground(new java.awt.Color(51, 255, 51));
        exportbtn.setText("Export As Excel");
        exportbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportbtnActionPerformed(evt);
            }
        });

        itemstblfilter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        itemstblfilter.setMinimumSize(new java.awt.Dimension(0, 20));
        itemstblfilter.setPreferredSize(new java.awt.Dimension(0, 20));
        itemstblfilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemstblfilterActionPerformed(evt);
            }
        });
        itemstblfilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemstblfilterKeyTyped(evt);
            }
        });

        searchlbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/searchlogo.png"))); // NOI18N
        searchlbl.setIconTextGap(0);

        javax.swing.GroupLayout itemstblheaderLayout = new javax.swing.GroupLayout(itemstblheader);
        itemstblheader.setLayout(itemstblheaderLayout);
        itemstblheaderLayout.setHorizontalGroup(
            itemstblheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemstblheaderLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(itemstblfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(searchlbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                .addComponent(additembtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(editbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(printitemstblbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(exportbtn)
                .addGap(18, 18, 18))
        );
        itemstblheaderLayout.setVerticalGroup(
            itemstblheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, itemstblheaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemstblheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemstblfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchlbl)
                    .addGroup(itemstblheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(additembtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(printitemstblbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(exportbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        itemspanel.add(itemstblheader, java.awt.BorderLayout.PAGE_START);

        maintabpane.addTab("ItemsRecord", itemspanel);

        salespanel.setBackground(new java.awt.Color(204, 51, 255));
        salespanel.setLayout(new java.awt.BorderLayout());

        salestbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        salesscrollpane.setViewportView(salestbl);

        salespanel.add(salesscrollpane, java.awt.BorderLayout.CENTER);

        salestablefooter.setBackground(new java.awt.Color(204, 0, 255));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel3.setText("Total Sales:  P");

        tsales.setEditable(false);
        tsales.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        tsales.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        javax.swing.GroupLayout salestablefooterLayout = new javax.swing.GroupLayout(salestablefooter);
        salestablefooter.setLayout(salestablefooterLayout);
        salestablefooterLayout.setHorizontalGroup(
            salestablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, salestablefooterLayout.createSequentialGroup()
                .addContainerGap(722, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tsales, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        salestablefooterLayout.setVerticalGroup(
            salestablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, salestablefooterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(salestablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tsales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
        );

        salespanel.add(salestablefooter, java.awt.BorderLayout.PAGE_END);

        salestableheader.setBackground(new java.awt.Color(204, 0, 255));

        printsalestblbtn.setBackground(new java.awt.Color(51, 51, 255));
        printsalestblbtn.setText("Print Table");
        printsalestblbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printsalestblbtnActionPerformed(evt);
            }
        });

        exportbtn2.setBackground(new java.awt.Color(51, 255, 51));
        exportbtn2.setText("Export As Excel");
        exportbtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportbtn2ActionPerformed(evt);
            }
        });

        salestblfilter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        salestblfilter.setMinimumSize(new java.awt.Dimension(0, 20));
        salestblfilter.setPreferredSize(new java.awt.Dimension(0, 20));
        salestblfilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                salestblfilterKeyTyped(evt);
            }
        });

        searchlbl1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/searchlogo.png"))); // NOI18N
        searchlbl1.setIconTextGap(0);

        javax.swing.GroupLayout salestableheaderLayout = new javax.swing.GroupLayout(salestableheader);
        salestableheader.setLayout(salestableheaderLayout);
        salestableheaderLayout.setHorizontalGroup(
            salestableheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, salestableheaderLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(salestblfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(searchlbl1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 392, Short.MAX_VALUE)
                .addComponent(printsalestblbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(exportbtn2)
                .addGap(18, 18, 18))
        );
        salestableheaderLayout.setVerticalGroup(
            salestableheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salestableheaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(salestableheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(salestblfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchlbl1)
                    .addGroup(salestableheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(printsalestblbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(exportbtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        salespanel.add(salestableheader, java.awt.BorderLayout.PAGE_START);

        maintabpane.addTab("SalesRecord", salespanel);

        receiptspanel.setBackground(new java.awt.Color(204, 51, 255));
        receiptspanel.setLayout(new java.awt.BorderLayout());

        receiptstbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        receiptsscrollpane.setViewportView(receiptstbl);

        receiptspanel.add(receiptsscrollpane, java.awt.BorderLayout.CENTER);

        receiptstablefooter.setBackground(new java.awt.Color(204, 0, 255));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("Net Sales:  P");

        tsales1.setEditable(false);
        tsales1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        tsales1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel5.setText("Total Sales:  P");

        tsales2.setEditable(false);
        tsales2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        tsales2.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        javax.swing.GroupLayout receiptstablefooterLayout = new javax.swing.GroupLayout(receiptstablefooter);
        receiptstablefooter.setLayout(receiptstablefooterLayout);
        receiptstablefooterLayout.setHorizontalGroup(
            receiptstablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, receiptstablefooterLayout.createSequentialGroup()
                .addContainerGap(475, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tsales2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tsales1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        receiptstablefooterLayout.setVerticalGroup(
            receiptstablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, receiptstablefooterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(receiptstablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(receiptstablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tsales2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(receiptstablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tsales1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
                .addContainerGap())
        );

        receiptspanel.add(receiptstablefooter, java.awt.BorderLayout.PAGE_END);

        receiptstableheader1.setBackground(new java.awt.Color(204, 0, 255));

        printreceiptstblbtn1.setBackground(new java.awt.Color(51, 51, 255));
        printreceiptstblbtn1.setText("Print Table");
        printreceiptstblbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printreceiptstblbtn1ActionPerformed(evt);
            }
        });

        exportbtn3.setBackground(new java.awt.Color(51, 255, 51));
        exportbtn3.setText("Export As Excel");
        exportbtn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportbtn3ActionPerformed(evt);
            }
        });

        receiptstblfilter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        receiptstblfilter.setMinimumSize(new java.awt.Dimension(0, 20));
        receiptstblfilter.setPreferredSize(new java.awt.Dimension(0, 20));
        receiptstblfilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                receiptstblfilterKeyTyped(evt);
            }
        });

        searchlbl2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/searchlogo.png"))); // NOI18N
        searchlbl2.setIconTextGap(0);

        javax.swing.GroupLayout receiptstableheader1Layout = new javax.swing.GroupLayout(receiptstableheader1);
        receiptstableheader1.setLayout(receiptstableheader1Layout);
        receiptstableheader1Layout.setHorizontalGroup(
            receiptstableheader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, receiptstableheader1Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(receiptstblfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(searchlbl2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 392, Short.MAX_VALUE)
                .addComponent(printreceiptstblbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(exportbtn3)
                .addGap(18, 18, 18))
        );
        receiptstableheader1Layout.setVerticalGroup(
            receiptstableheader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(receiptstableheader1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(receiptstableheader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(receiptstblfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchlbl2)
                    .addGroup(receiptstableheader1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(printreceiptstblbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(exportbtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        receiptspanel.add(receiptstableheader1, java.awt.BorderLayout.PAGE_START);

        maintabpane.addTab("AllReceipts", receiptspanel);

        ReturnedItems.setBackground(new java.awt.Color(204, 51, 255));
        ReturnedItems.setLayout(new java.awt.BorderLayout());

        returneditemstbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        returnedscrollpane.setViewportView(returneditemstbl);

        ReturnedItems.add(returnedscrollpane, java.awt.BorderLayout.CENTER);

        returnedtablefooter.setBackground(new java.awt.Color(204, 0, 255));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Total Refund:  P");

        treturnedsales.setEditable(false);
        treturnedsales.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        treturnedsales.setHorizontalAlignment(javax.swing.JTextField.TRAILING);

        javax.swing.GroupLayout returnedtablefooterLayout = new javax.swing.GroupLayout(returnedtablefooter);
        returnedtablefooter.setLayout(returnedtablefooterLayout);
        returnedtablefooterLayout.setHorizontalGroup(
            returnedtablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, returnedtablefooterLayout.createSequentialGroup()
                .addContainerGap(681, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(treturnedsales, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        returnedtablefooterLayout.setVerticalGroup(
            returnedtablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, returnedtablefooterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(returnedtablefooterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(treturnedsales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );

        ReturnedItems.add(returnedtablefooter, java.awt.BorderLayout.PAGE_END);

        returnedtableheader.setBackground(new java.awt.Color(204, 0, 255));

        printreturneditemstblbtn2.setBackground(new java.awt.Color(51, 51, 255));
        printreturneditemstblbtn2.setText("Print Table");
        printreturneditemstblbtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printreturneditemstblbtn2ActionPerformed(evt);
            }
        });

        exportbtn4.setBackground(new java.awt.Color(51, 255, 51));
        exportbtn4.setText("Export As Excel");
        exportbtn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportbtn4ActionPerformed(evt);
            }
        });

        returneditemstblfilter.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        returneditemstblfilter.setMinimumSize(new java.awt.Dimension(0, 20));
        returneditemstblfilter.setPreferredSize(new java.awt.Dimension(0, 20));
        returneditemstblfilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                returneditemstblfilterKeyTyped(evt);
            }
        });

        searchlbl3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/searchlogo.png"))); // NOI18N
        searchlbl3.setIconTextGap(0);

        addreturneditembtn.setBackground(new java.awt.Color(102, 255, 255));
        addreturneditembtn.setText("Add Record");
        addreturneditembtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addreturneditembtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout returnedtableheaderLayout = new javax.swing.GroupLayout(returnedtableheader);
        returnedtableheader.setLayout(returnedtableheaderLayout);
        returnedtableheaderLayout.setHorizontalGroup(
            returnedtableheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, returnedtableheaderLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(returneditemstblfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(searchlbl3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 279, Short.MAX_VALUE)
                .addComponent(addreturneditembtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(printreturneditemstblbtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(exportbtn4)
                .addGap(18, 18, 18))
        );
        returnedtableheaderLayout.setVerticalGroup(
            returnedtableheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(returnedtableheaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(returnedtableheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(returneditemstblfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchlbl3)
                    .addGroup(returnedtableheaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(printreturneditemstblbtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(exportbtn4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addreturneditembtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ReturnedItems.add(returnedtableheader, java.awt.BorderLayout.PAGE_START);

        maintabpane.addTab("ReturnedItems", ReturnedItems);

        javax.swing.GroupLayout tabpanelLayout = new javax.swing.GroupLayout(tabpanel);
        tabpanel.setLayout(tabpanelLayout);
        tabpanelLayout.setHorizontalGroup(
            tabpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(maintabpane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabpanelLayout.setVerticalGroup(
            tabpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabpanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(maintabpane, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))
        );

        getContentPane().add(tabpanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closelblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closelblMouseClicked
        if(openscan=true && sf!=null){
            sf.setEnabled(true);
            sf.requestFocus();
        }
        this.dispose(); 
    }//GEN-LAST:event_closelblMouseClicked

    private void minimizelblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimizelblMouseClicked
        this.setState(ICONIFIED);
    }//GEN-LAST:event_minimizelblMouseClicked

    private void additembtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_additembtnActionPerformed
        new AddItemDlg(this,false);
        this.setEnabled(false);
        editbtn.setEnabled(false);
    }//GEN-LAST:event_additembtnActionPerformed

    private void itemstblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemstblMouseClicked
        if(itemstbl.getSelectedRowCount()==1){
            editbtn.setEnabled(true);
        }
    }//GEN-LAST:event_itemstblMouseClicked

    private void editbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editbtnActionPerformed
        String[] toedit={
            itemstbl.getValueAt(itemstbl.getSelectedRow(), 0).toString(),
            itemstbl.getValueAt(itemstbl.getSelectedRow(), 1).toString(),
            itemstbl.getValueAt(itemstbl.getSelectedRow(), 2).toString(),
            itemstbl.getValueAt(itemstbl.getSelectedRow(), 3).toString(),
            itemstbl.getValueAt(itemstbl.getSelectedRow(), 4).toString(),
            itemstbl.getValueAt(itemstbl.getSelectedRow(), 5).toString(),
            itemstbl.getValueAt(itemstbl.getSelectedRow(), 6).toString(),    
        };
        new AddItemDlg(this,false,toedit);
        editbtn.setEnabled(false);
        this.setEnabled(false);
    }//GEN-LAST:event_editbtnActionPerformed

    private void printitemstblbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printitemstblbtnActionPerformed
        MessageFormat head=new MessageFormat("Printing......");
        MessageFormat foor=new MessageFormat("Page [0, number, integer]");
        try{
            itemstbl.print(JTable.PrintMode.NORMAL);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "No Printer Found!");
        }
    }//GEN-LAST:event_printitemstblbtnActionPerformed

    private void exportbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportbtnActionPerformed
        new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/").mkdir();
        new File(System.getProperty("user.home")+"/Documents/BarcodeApp/Exported_Tables/").mkdir();
        String addr=System.getProperty("user.home")+"/Documents/BarcodeApp/Exported_Tables/Items_";
        try{
            exportToExcel(itemstbl, addr);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_exportbtnActionPerformed

    private void printsalestblbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printsalestblbtnActionPerformed
        MessageFormat head=new MessageFormat("Printing......");
        MessageFormat foor=new MessageFormat("Page [0, number, integer]");
        try{
            salestbl.print(JTable.PrintMode.NORMAL);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "No Printer Found!");
        }
    }//GEN-LAST:event_printsalestblbtnActionPerformed

    private void exportbtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportbtn2ActionPerformed
        new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/").mkdir();
        new File(System.getProperty("user.home")+"/Documents/BarcodeApp/Exported_Tables/").mkdir();
        String addr=System.getProperty("user.home")+"/Documents/BarcodeApp/Exported_Tables/Sales_";
        try{
            exportToExcel(salestbl, addr);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_exportbtn2ActionPerformed

    private void printreceiptstblbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printreceiptstblbtn1ActionPerformed
        MessageFormat head=new MessageFormat("Printing......");
        MessageFormat foor=new MessageFormat("Page [0, number, integer]");
        try{
            receiptstbl.print(JTable.PrintMode.NORMAL);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "No Printer Found!");
        }
    }//GEN-LAST:event_printreceiptstblbtn1ActionPerformed

    private void exportbtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportbtn3ActionPerformed
        new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/").mkdir();
        new File(System.getProperty("user.home")+"/Documents/BarcodeApp/Exported_Tables/").mkdir();
        String addr=System.getProperty("user.home")+"/Documents/BarcodeApp/Exported_Tables/Receipts_";
        try{
            exportToExcel(receiptstbl, addr);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_exportbtn3ActionPerformed

    private void newadminbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newadminbtnMouseClicked
       new AddAdminForm(this);
       this.setEnabled(false);
    }//GEN-LAST:event_newadminbtnMouseClicked

    private void receiptstblfilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_receiptstblfilterKeyTyped
        filter(receiptsorter, receiptstblfilter);
    }//GEN-LAST:event_receiptstblfilterKeyTyped

    private void salestblfilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_salestblfilterKeyTyped
        filter(salesorter, salestblfilter);
    }//GEN-LAST:event_salestblfilterKeyTyped

    private void itemstblfilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemstblfilterKeyTyped
        filter(itemsorter, itemstblfilter);
    }//GEN-LAST:event_itemstblfilterKeyTyped

    private void printreturneditemstblbtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printreturneditemstblbtn2ActionPerformed
        MessageFormat head=new MessageFormat("Printing......");
        MessageFormat foor=new MessageFormat("Page [0, number, integer]");
        try{
            returneditemstbl.print(JTable.PrintMode.NORMAL);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(rootPane, "No Printer Found!");
        }
    }//GEN-LAST:event_printreturneditemstblbtn2ActionPerformed

    private void exportbtn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportbtn4ActionPerformed
       new File(System.getProperty("user.home").toString()+"/Documents/BarcodeApp/").mkdir();
        new File(System.getProperty("user.home")+"/Documents/BarcodeApp/Exported_Tables/").mkdir();
        String addr=System.getProperty("user.home")+"/Documents/BarcodeApp/Exported_Tables/ReturnedItems_";
        try{
            exportToExcel(returneditemstbl, addr);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }//GEN-LAST:event_exportbtn4ActionPerformed

    private void returneditemstblfilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returneditemstblfilterKeyTyped
        filter(returneditemsorter, returneditemstblfilter);
    }//GEN-LAST:event_returneditemstblfilterKeyTyped

    private void addreturneditembtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addreturneditembtnActionPerformed
        new AddReturnedItemForm(this);
        this.setEnabled(false);
    }//GEN-LAST:event_addreturneditembtnActionPerformed

    private void itemstblfilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemstblfilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemstblfilterActionPerformed

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
            java.util.logging.Logger.getLogger(AdminsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminsForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ReturnedItems;
    private javax.swing.JButton additembtn;
    private javax.swing.JButton addreturneditembtn;
    private javax.swing.JLabel closelbl;
    private javax.swing.JButton editbtn;
    private javax.swing.JButton exportbtn;
    private javax.swing.JButton exportbtn2;
    private javax.swing.JButton exportbtn3;
    private javax.swing.JButton exportbtn4;
    private javax.swing.JPanel headerpanel;
    private javax.swing.JPanel itemspanel;
    private javax.swing.JScrollPane itemsscrollpane;
    private javax.swing.JTable itemstbl;
    private javax.swing.JTextField itemstblfilter;
    private javax.swing.JPanel itemstblfooter;
    private javax.swing.JPanel itemstblheader;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTabbedPane maintabpane;
    private javax.swing.JLabel minimizelbl;
    private javax.swing.JLabel newadminbtn;
    private javax.swing.JTextField overallcost;
    private javax.swing.JTextField overallmvalue;
    private javax.swing.JButton printitemstblbtn;
    private javax.swing.JButton printreceiptstblbtn1;
    private javax.swing.JButton printreturneditemstblbtn2;
    private javax.swing.JButton printsalestblbtn;
    private javax.swing.JPanel receiptspanel;
    private javax.swing.JScrollPane receiptsscrollpane;
    private javax.swing.JPanel receiptstablefooter;
    private javax.swing.JPanel receiptstableheader1;
    private javax.swing.JTable receiptstbl;
    private javax.swing.JTextField receiptstblfilter;
    private javax.swing.JTable returneditemstbl;
    private javax.swing.JTextField returneditemstblfilter;
    private javax.swing.JScrollPane returnedscrollpane;
    private javax.swing.JPanel returnedtablefooter;
    private javax.swing.JPanel returnedtableheader;
    private javax.swing.JPanel salespanel;
    private javax.swing.JScrollPane salesscrollpane;
    private javax.swing.JPanel salestablefooter;
    private javax.swing.JPanel salestableheader;
    private javax.swing.JTable salestbl;
    private javax.swing.JTextField salestblfilter;
    private javax.swing.JLabel searchlbl;
    private javax.swing.JLabel searchlbl1;
    private javax.swing.JLabel searchlbl2;
    private javax.swing.JLabel searchlbl3;
    private javax.swing.JPanel tabpanel;
    private javax.swing.JLabel titlelbl;
    private javax.swing.JTextField treturnedsales;
    private javax.swing.JTextField tsales;
    private javax.swing.JTextField tsales1;
    private javax.swing.JTextField tsales2;
    // End of variables declaration//GEN-END:variables
}
