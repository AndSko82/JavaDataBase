package dBase;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends Frame {
	private static final long serialVersionUID = 1L;
	static DefaultTableModel model;
	static DefaultTableModel model2;
	static int rowNum;
	private Label welcome;
	public void setWelcome(String text) {
		this.welcome.setText(text);
	}
	static JTable table;
	static JTable table2;
	static String[] column= {"Name","Lastname","Adres","Phone","Deposit","Charge","Difference","ID","Department","Charge"};
	static JScrollPane jp;
	static JScrollPane jp2;
	private Button disp;
	private Button disd;
	private Button addc;
	static private Button modc;
	static private Button modf;
	static private Button remc;
	private Button ads;
	private Button crs;
	static int action;
	MainFrame(){
		setLayout(null);
		disp=new Button("customers");
		disd=new Button("department");
		addc=new Button("Add customer");
		modc=new Button("Modify customer");
		modf=new Button("Modify field");
		remc=new Button("Delete customer");
		ads=new Button("Advanced search");
		crs=new Button("Join search");
		welcome=new Label();
		welcome.setBounds(20, 25, 400, 20);
		disp.setBounds(10,100,70,40);
		disd.setBounds(80,100,70,40);
		addc.setBounds(20,160,100,40);
		modc.setBounds(20,220,100,40);
		modc.setEnabled(false);
		modf.setBounds(20,280,100,40);
		modf.setEnabled(false);
		remc.setBounds(20,340,100,40);
		remc.setEnabled(false);
		ads.setBounds(20,420,120,40);
		crs.setBounds(20,470,120,40);
		setSize(800,600);
		setLocation(250,200);
		setBackground(Login.c);
		setVisible(false);
		add(disp);
		add(disd);
		add(addc);
		add(modc);
		add(modf);
		add(remc);	
		add(ads);
		add(crs);
		add(welcome);
		createTable();	
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){		
				System.exit(0);
			}
			
		   });	
			
		disp.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){	
			ManageDB.retrieveData();
			}
		});
		disd.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
			ManageDB.retrieveDep();
			}
		});
		addc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				action=1;
				 ManageDB.addData();					
			}
		});
		modc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if (!table.getSelectionModel().isSelectionEmpty()) {
					action=2;
					ManageDB.modifyData();
				}
			}
		});
		modf.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if (!table.getSelectionModel().isSelectionEmpty()&&table.getSelectedColumn()>0&&table.getSelectedColumn()<7) {
					action=3;
					ManageDB.modifyField();
				}
			}
		});
		remc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if (!table.getSelectionModel().isSelectionEmpty()) {
					ManageDB.deleteData();
				}
			}
		});
		ads.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
					ManageDB.advancedSearch();
				
			}
		});
		crs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
					ManageDB.crossJoin();
				
			}
		});
		
	}
	public static void setTableDep(){
	model2.setColumnCount(0);
	model2.addColumn(column[7]);
	model2.addColumn(column[8]);
	model2.addColumn(column[9]);
	}
	public void createTable(){
		String[] column={"ID","Name","Last name","Adres","Phone","Deposit","Charge","Difference"};	 
	 model = new DefaultTableModel();
	 model2 = new DefaultTableModel();
	for(int i=0;i<8;i++)
	{
	model.addColumn(column[i]);
	}
	table=new JTable(model){
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {                
            return false;               
    };
	};
	table2=new JTable(model2){
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {                
            return false;               
    };
	};
	table2.setRowSelectionAllowed(false);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
	table.getColumnModel().getColumn(0).setPreferredWidth(30);
	table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
        public void valueChanged(ListSelectionEvent event) {
        	table.addMouseListener(new java.awt.event.MouseAdapter() {
        	    public void mouseClicked(java.awt.event.MouseEvent evt) {
        	    	if (!ManageDB.nonSelect&&!table.getSelectionModel().isSelectionEmpty()){
        	    	modc.setEnabled(true);
        	    	remc.setEnabled(true);
        	    	if(table.columnAtPoint(evt.getPoint())>0&&table.columnAtPoint(evt.getPoint())<7){
        	    		modf.setEnabled(true);
        	    	}
        	    	else{
        	    		modf.setEnabled(false);
        	    	}
        	    	}
        	    	else{
        	    		modc.setEnabled(false);
            	    	remc.setEnabled(false);	
            	    	modf.setEnabled(false);
        	    	}
        	        }   	    
        	});
           
        }
    });
	add(table);
	add(table2);
	jp=new JScrollPane(table);
	jp2=new JScrollPane(table2);
	    jp.setBounds(170, 100, 580, 350);
	    jp2.setBounds(170, 100, 580, 87);
	    add(jp);
	    add(jp2);
	}
public static void setButtons(){
	modc.setEnabled(false);
	remc.setEnabled(false);	
	modf.setEnabled(false);
}
}
