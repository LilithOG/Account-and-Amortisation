package userinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;

import java.awt.Insets;

import javax.swing.*;

import java.awt.GridLayout;

import javax.swing.GroupLayout.Alignment;

import resources.Account;
import resources.Amortisation;
import resources.Planet;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class Main {

    private String[] columnNames = {"", "", "", ""};
    private JScrollPane myTableScroll;
    private JFrame frame, frameAmortisation;
    private final int numVersorgungRows = 10;
    private Object[] versorgungFirstColumn = {"Name","Metmine","Krismine","Deutmine","Metlager","Krislager","Deuttank","SKW","FKW","Sats"};
    private Object[][] data;
    private DefaultTableModel tabV_model, tabA_model, tabD_model, tabF_model, tabN_model;
    private TableModelListener tabV_tableCellChanged, tabA_tableCellChanged, tabD_tableCellChanged, tabF_tableCellChanged, tabN_tableCellChanged;
    private JLabel tabV_lblNewLabel, tabA_lblNewLabel, tabD_lblNewLabel, tabV_lInvertColors;
    private JPanel tabV_leftPanel, tabA_leftPanel, tabD_leftPanel, tabF_leftPanel, tabV_rightPanel, tabA_rightPanel, tabD_rightPanel, tabF_rightPanel, tabN_leftPanel, tabN_rightPanel;
    private JButton tabV_bSave, tabA_bSave, tabD_bSave, tabV_bLoad, tabA_bLoad, tabD_bLoad, tabF_bSave, tabF_bLoad, tabV_bInvertColors, tabN_bLoad, tabN_bSave;
    private JButton astroliste;
    private JTable tabV_table, tabA_table, tabD_table, tabF_table, tabN_table;
    private JPanel tabVersorgung, tabAnlagen, tabVerteidigung, tabForschung, tabNeuerPlanet;
    private JTabbedPane tabbedPane;
    private JPanel top, bottom, panelAmortisation;
    private JScrollPane scrollPaneAmortisation;
    private JTextPane textAmortisation;
    private JTextField currencyM, currencyC, currencyD;
    private Integer[] numPlanetsArrDropdown = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
    private String[] selectLanguage = {"Deutsch", "English"};
    private JComboBox tabV_numPlanetsDropdown, tabA_numPlanetsDropdown, tabD_numPlanetsDropdown, tabV_selectLanguage;
    private Account acc;
    private boolean ignoreFirstTabInit;
    private boolean tabVsilentDropdownSet, tabAsilentDropdownSet, tabDsilentDropdownSet;
    private boolean tableVsilentChange, tableAsilentChange, tableDsilentChange, tableFsilentChange;
    private int currentTabIndex, previousTabIndex;
    private JLabel textOutProduction;
    private JCheckBox checkGeologe;
    private boolean invertColors;
    private Color colorFrameBackBu, colorFrameForeBu;
    private Font fontAmort, fontTables;
    private Amortisation amo;
    private ResourceBundle strings;
    private Locale locale;
    
    public Main() {
    	locale = new Locale("de");
    	strings = ResourceBundle.getBundle("Strings",locale);
    	
    	invertColors = false;
    	fontAmort = new Font(Font.MONOSPACED,Font.PLAIN,18);
    	fontTables = new Font(Font.SERIF, Font.PLAIN, 16);
    	UIManager.put("Table.font", new Font(Font.SANS_SERIF, Font.PLAIN, 18));
    	tableVsilentChange = tableAsilentChange = tableDsilentChange = tableFsilentChange = true;
        GridBagConstraints gbc_pane = new GridBagConstraints();
        gbc_pane.fill = GridBagConstraints.HORIZONTAL;
        gbc_pane.anchor = GridBagConstraints.WEST;
        gbc_pane.insets = new Insets(0, 0, 5, 0);
        gbc_pane.gridx = 2;
        gbc_pane.gridy = 0;
        ActionListener actionSaveButton = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		previousTabIndex = currentTabIndex;
        		storeModificationsToAcc();
        		acc.saveToFile();
        	}
        };
        ActionListener actionLoadButton = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		acc = new Account();
        		acc.loadFromFile();
        		redrawFrame();
        	}
        };
        ActionListener actionAmortlisteButton = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		double m = 0, c = 0, d = 0;
        		m = Double.valueOf(currencyM.getText());
        		System.out.printf("m: %f c: %f d: %f\n",m,c,d);
        		c = Double.valueOf(currencyC.getText());
        		d = Double.valueOf(currencyD.getText());
        		if(m > 0.0 && c > 0.0 && d > 0.0)
        			amo = new Amortisation(acc, m, c, d);
        		else
        			amo = new Amortisation(acc, 3.0, 2.0, 1.0);
        		ArrayList<String> amoList = amo.getAmortisationsliste();
        		StringBuilder sb = new StringBuilder();
        		for(String x : amoList)
        			sb.append(x+"<br>");
                frameAmortisation = new JFrame();
                frameAmortisation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                panelAmortisation = new JPanel();
                panelAmortisation.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
                textAmortisation = new JTextPane();
                textAmortisation.setBounds(0, 0, 100, 100);
                textAmortisation.setEditable(false);
                scrollPaneAmortisation = new JScrollPane(textAmortisation, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPaneAmortisation.setPreferredSize(new Dimension(1000, 600));
                textAmortisation.setContentType("text/html");
                textAmortisation.setText("<html><body>"+sb.toString()+"</body></html>");
                if(!invertColors)
                	setJTextPaneFont(textAmortisation, fontAmort, Color.WHITE, Color.BLACK);
                else
                	setJTextPaneFont(textAmortisation, fontAmort, Color.BLACK, Color.WHITE);

                panelAmortisation.add(scrollPaneAmortisation);
                frameAmortisation.getContentPane().add(panelAmortisation);
	        	if(invertColors){
	        			Color backgroundColor = Color.BLACK;
			            SimpleAttributeSet background = new SimpleAttributeSet();
			            StyleConstants.setBackground(background, backgroundColor);
			            textAmortisation.getStyledDocument().setParagraphAttributes(0, 30, background, false);
	        		frameAmortisation.setBackground(Color.BLACK);
	        		panelAmortisation.setBackground(Color.BLACK);
	        		scrollPaneAmortisation.setBackground(Color.BLACK);
	        	}
                frameAmortisation.pack();
                frameAmortisation.setVisible(true);
        	}
        };
        
        tabV_tableCellChanged = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tme) {
            	if(tableVsilentChange)
            		return;
       			if (tme.getType() != TableModelEvent.UPDATE)
       				return;
            	//System.out.printf("tabV_tableCellChanged.\n");
        		 int col = tabV_table.getSelectedColumn();
        		 //System.out.printf("Selected column=%d\n", col);
                 int[] colVal = new int[12];
                 for(int i = 1; i < 12; i++)
                	 colVal[i] = Integer.parseInt((String) tabV_table.getValueAt(i, col));
                 acc.getPlanets().get(col-1).setName((String) tabV_table.getValueAt(0, col));
                 acc.getPlanets().get(col-1).setVersorgung(colVal);
                 updateProduction();
             }
        };
    	tabA_tableCellChanged = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tme) {
            	if(tableAsilentChange)
            		return;
       			if (tme.getType() != TableModelEvent.UPDATE)
       				return;
            	//System.out.printf("tabA_tableCellChanged.\n");
       			int col = tabA_table.getSelectedColumn();
       		 //System.out.printf("Selected column=%d\n", col);
       			int[] colVal = new int[8];
       			for(int i = 1; i < 8; i++)
       				colVal[i] = Integer.parseInt((String) tabA_table.getValueAt(i, col));
         		acc.getPlanets().get(col-1).setName((String) tabA_table.getValueAt(0, col));
       			acc.getPlanets().get(col-1).setAnlagen(colVal);
            }
        };
       	tabD_tableCellChanged = new TableModelListener() {
       		@Override
       		public void tableChanged(TableModelEvent tme) {
       			if(tableDsilentChange)
       				return;
       			if (tme.getType() != TableModelEvent.UPDATE)
       				return;
            	//System.out.printf("tabD_tableCellChanged.\n");
       			int col = tabD_table.getSelectedColumn();
       		 //System.out.printf("Selected column=%d\n", col);
         		int[] colVal = new int[9];
         		for(int i = 1; i < 9; i++)
         			colVal[i] = Integer.parseInt((String) tabD_table.getValueAt(i, col));
         		acc.getPlanets().get(col-1).setName((String) tabD_table.getValueAt(0, col));
              	acc.getPlanets().get(col-1).setVerteidigung(colVal);
              }
        };
       	tabF_tableCellChanged = new TableModelListener() {
       		@Override
       		public void tableChanged(TableModelEvent tme) {
       			if(tableFsilentChange)
       				return;
       			if (tme.getType() != TableModelEvent.UPDATE)
       				return;
            	//System.out.printf("tabF_tableCellChanged.\n");
         		int[] colVal = new int[16];
         		for(int i = 0; i < 16; i++)
         			colVal[i] = Integer.parseUnsignedInt(tabF_table.getValueAt(i, 1).toString());
              	acc.setTechnologien(colVal);
              }
        };
       	tabN_tableCellChanged = new TableModelListener() {
       		@Override
       		public void tableChanged(TableModelEvent tme) {
       			if(tableFsilentChange)
       				return;
       			if (tme.getType() != TableModelEvent.UPDATE)
       				return;
            	//System.out.printf("tabN_tableCellChanged.\n");
         		int[] colVal = new int[20];
         		for(int i = 0; i < 20; i++)
         			colVal[i] = Integer.parseUnsignedInt(tabN_table.getValueAt(i, 1).toString());
              	acc.setNeuerPlanet(colVal);
              }
        };
    	acc = new Account();
		//System.out.printf("after acc = new acc: planets size %d\n", acc.getPlanets().size());
        frame = new JFrame();
    	colorFrameBackBu = frame.getContentPane().getBackground();
    	colorFrameForeBu = frame.getContentPane().getForeground();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        
        tabV_table = new JTable(){
        	@Override
        	public boolean isCellEditable(int row, int col){if(col == 0) return false; else	return true;} };
        tabV_table.setColumnSelectionAllowed(true);
        tabV_table.setCellSelectionEnabled(true);
        tabV_table.setFillsViewportHeight(true);
        tabV_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabV_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabV_table.setPreferredScrollableViewportSize(tabV_table.getPreferredSize());
        tabV_table.setTableHeader(null);
        tabV_table.putClientProperty("terminateEditOnFocusLost", true);
        tabV_table.setFillsViewportHeight(true);
        //tabV_table.setFont(new Font("",0,tabV_table.getFont().getSize()+6));
        
        tabA_table = new JTable(){
        	@Override
        	public boolean isCellEditable(int row, int col){if(col == 0) return false; else	return true;} };
        tabA_table.setColumnSelectionAllowed(true);
        tabA_table.setCellSelectionEnabled(true);
        tabA_table.setFillsViewportHeight(true);
        tabA_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabA_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabA_table.setPreferredScrollableViewportSize(tabA_table.getPreferredSize());
        tabA_table.setTableHeader(null);
        tabA_table.putClientProperty("terminateEditOnFocusLost", true);
        tabA_table.setFillsViewportHeight(true);
        
        tabD_table = new JTable(){
        	@Override
        	public boolean isCellEditable(int row, int col){if(col == 0) return false; else	return true;} };
        tabD_table.setColumnSelectionAllowed(true);
        tabD_table.setCellSelectionEnabled(true);
        tabD_table.setFillsViewportHeight(true);
        tabD_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabD_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabD_table.setPreferredScrollableViewportSize(tabD_table.getPreferredSize());
        tabD_table.setTableHeader(null);
        tabD_table.putClientProperty("terminateEditOnFocusLost", true);
        tabD_table.setFillsViewportHeight(true);
        
        tabF_table = new JTable(){
        	@Override
        	public boolean isCellEditable(int row, int col){if(col == 0) return false; else	return true;} };
        tabF_table.setColumnSelectionAllowed(true);
        tabF_table.setCellSelectionEnabled(true);
        tabF_table.setFillsViewportHeight(true);
        tabF_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabF_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabF_table.setPreferredScrollableViewportSize(tabF_table.getPreferredSize());
        tabF_table.setTableHeader(null);
        tabF_table.putClientProperty("terminateEditOnFocusLost", true);
        tabF_table.setFillsViewportHeight(true);
        
        tabN_table = new JTable(){
        	@Override
        	public boolean isCellEditable(int row, int col){if(col == 0) return false; else	return true;} };
        tabN_table.setCellSelectionEnabled(true);
        tabN_table.setFillsViewportHeight(true);
        tabN_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabN_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabN_table.setPreferredScrollableViewportSize(tabN_table.getPreferredSize());
        tabN_table.setTableHeader(null);
        tabN_table.putClientProperty("terminateEditOnFocusLost", true);
        tabN_table.setFillsViewportHeight(true);

        tabV_lblNewLabel = new JLabel(strings.getString("numPlanets"));
        tabVersorgung = new JPanel();
        tabV_leftPanel = new JPanel();
        tabV_leftPanel.setLayout(new GridLayout(0, 1, 0, 0));
        tabV_bInvertColors = new JButton(strings.getString("invertColors"));
        tabV_bInvertColors.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		invertColors = !invertColors;
        		redrawFrame();
        	}
        });
        tabV_bSave = new JButton(strings.getString("Save"));
        tabV_bSave.addActionListener(actionSaveButton);
        tabV_bLoad = new JButton(strings.getString("Load"));
        tabV_bLoad.addActionListener(actionLoadButton);
        tabV_leftPanel.add(tabV_bInvertColors);
        tabV_leftPanel.add(tabV_bSave);
        tabV_leftPanel.add(tabV_bLoad);
        tabV_leftPanel.add(tabV_lblNewLabel);
        tabV_rightPanel = new JPanel();
        //tabV_rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        tabV_rightPanel.setLayout(new BorderLayout(0, 0));
        tabV_rightPanel.add(tabV_table, BorderLayout.CENTER);
        //tabV_rightPanel.add(tabV_table);
        tabVersorgung.setLayout(new BorderLayout(0,0));
        tabVersorgung.add(tabV_leftPanel, BorderLayout.WEST); 
        tabVersorgung.add(tabV_rightPanel, BorderLayout.CENTER);
        
        //tabA_lblNewLabel = new JLabel(strings.getString("numPlanets"));
        tabAnlagen = new JPanel();
        tabA_leftPanel = new JPanel();
        tabA_leftPanel.setLayout(new GridLayout(0, 1, 0, 0));
        tabA_bSave = new JButton(strings.getString("Save"));
        tabA_bSave.addActionListener(actionSaveButton);
        tabA_bLoad = new JButton(strings.getString("Load"));
        tabA_bLoad.addActionListener(actionLoadButton);
        tabA_leftPanel.add(tabA_bSave);
        tabA_leftPanel.add(tabA_bLoad);
        //tabA_leftPanel.add(tabA_lblNewLabel);
        tabA_rightPanel = new JPanel();
        tabA_rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        tabA_rightPanel.add(tabA_table);
        tabAnlagen.add(tabA_leftPanel);
        tabAnlagen.add(tabA_rightPanel);
        
        //tabD_lblNewLabel = new JLabel(strings.getString("numPlanets"));
        tabVerteidigung = new JPanel();
        tabD_leftPanel = new JPanel();
        tabD_leftPanel.setLayout(new GridLayout(0, 1, 0, 0));
        tabD_bSave = new JButton(strings.getString("Save"));
        tabD_bSave.addActionListener(actionSaveButton);
        tabD_bLoad = new JButton(strings.getString("Load"));
        tabD_bLoad.addActionListener(actionLoadButton);
        tabD_leftPanel.add(tabD_bSave);
        tabD_leftPanel.add(tabD_bLoad);
        //tabD_leftPanel.add(tabD_lblNewLabel);
        tabD_rightPanel = new JPanel();
        tabD_rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        tabD_rightPanel.add(tabD_table);
        tabVerteidigung.add(tabD_leftPanel);
        tabVerteidigung.add(tabD_rightPanel);

        tabForschung = new JPanel();
        tabF_leftPanel = new JPanel();
        tabF_leftPanel.setLayout(new GridLayout(0, 1, 0, 0));
        tabF_bSave = new JButton(strings.getString("Save"));
        tabF_bSave.addActionListener(actionSaveButton);
        tabF_bLoad = new JButton(strings.getString("Load"));
        tabF_bLoad.addActionListener(actionLoadButton);
        tabF_leftPanel.add(tabF_bSave);
        tabF_leftPanel.add(tabF_bLoad);
        //tabF_leftPanel.add(tabF_lblNewLabel);
        tabF_rightPanel = new JPanel();
        tabF_rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        tabF_rightPanel.add(tabF_table);
        tabForschung.add(tabF_leftPanel);
        tabForschung.add(tabF_rightPanel);
        
        tabNeuerPlanet = new JPanel();
        tabN_leftPanel = new JPanel();
        tabN_leftPanel.setLayout(new GridLayout(0, 1, 0, 0));
        tabN_bSave = new JButton(strings.getString("Save"));
        tabN_bSave.addActionListener(actionSaveButton);
        tabN_bLoad = new JButton(strings.getString("Load"));
        tabN_bLoad.addActionListener(actionLoadButton);
        tabN_leftPanel.add(tabN_bSave);
        tabN_leftPanel.add(tabN_bLoad);
        //tabN_leftPanel.add(tabN_lblNewLabel);
        tabN_rightPanel = new JPanel();
        tabN_rightPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        tabN_rightPanel.add(tabN_table);
        tabNeuerPlanet.add(tabN_leftPanel);
        tabNeuerPlanet.add(tabN_rightPanel);
        
        tabV_numPlanetsDropdown = new JComboBox(numPlanetsArrDropdown);
        tabV_numPlanetsDropdown.setToolTipText(strings.getString("numPlanets"));        
        tabV_numPlanetsDropdown.setVisible(true);
        tabV_leftPanel.add(tabV_numPlanetsDropdown);
        tabV_numPlanetsDropdown.addItemListener(new ItemListener() {
        	@Override
            public void itemStateChanged(ItemEvent event) {
        		if (!(event.getStateChange() == ItemEvent.SELECTED))
            	   return;
        		if(tabVsilentDropdownSet == true){
        			tabVsilentDropdownSet = false;
        			return;
        		}
        		else{
	        		//System.out.println("tabVactionPerformed\n");
	        		int newNumPlanets = tabV_numPlanetsDropdown.getSelectedIndex();
	        		//System.out.printf("newNumPlanets=%d\n", newNumPlanets);
	        		int oldNumPlanets = acc.getPlanets().size();
	        		if(oldNumPlanets > newNumPlanets){
	        			for(int i = 0; i < oldNumPlanets-newNumPlanets; i++)
	        				acc.removePlanet();
	            		redrawFrame();
	        		}
	        		else if(oldNumPlanets < newNumPlanets){
	        			for(int i = 0; i < newNumPlanets-oldNumPlanets; i++)
	        				acc.addPlanet();
	            		redrawFrame();
	        		}
	        		else
	        			return;
        		}
        	}
        });
        
        // Currency
        JPanel panelCurrency = new JPanel();
        panelCurrency.setToolTipText("Currency");
        currencyM = new JTextField("3.0");
        currencyC = new JTextField("2.0");
        currencyD = new JTextField("1.0");
        panelCurrency.setLayout(new GridLayout(0, 3, 0, 0));
        panelCurrency.add(currencyM);
        panelCurrency.add(currencyC);
        panelCurrency.add(currencyD);
        panelCurrency.invalidate();
        tabV_leftPanel.add(panelCurrency);
        
        
        
        // Tabs: Mines, Facilities, Defence, Research, New Planet
        tabbedPane.addTab(strings.getString("Mines"), null, tabVersorgung, null);
        tabbedPane.addTab(strings.getString("Facilities"), null, tabAnlagen, null);
        tabbedPane.addTab(strings.getString("Defence"), null, tabVerteidigung, null);
        tabbedPane.addTab(strings.getString("Research"), null, tabForschung, null);
        tabbedPane.addTab(strings.getString("newPlanet"), null, tabNeuerPlanet, null);
        tabbedPane.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent changeEvent) {
                previousTabIndex = currentTabIndex;
                currentTabIndex = tabbedPane.getSelectedIndex();
                //System.out.println("Tab changed to: " + currentTabIndex);
        		if(ignoreFirstTabInit == true){
        			//System.out.println("First Tab Init, ignoring.");
        			ignoreFirstTabInit = false;
        			return;
        		}        			
        		//System.out.println("Tab changed! Redrawing Frame\n");
        		storeModificationsToAcc();
        		redrawFrame();
        	}
        });
        ignoreFirstTabInit = false;
        tabbedPane.setSelectedIndex(0);

        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        top = new JPanel();
        top.setLayout(new BorderLayout(0,0));
        checkGeologe = new JCheckBox(strings.getString("geologist"));
        checkGeologe.addActionListener(new ActionListener(){
        	@Override
            public void actionPerformed(ActionEvent e) {
       		acc.setGeologe(checkGeologe.isSelected());
            redrawFrame();
        }
        });
        bottom = new JPanel();
        bottom.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        bottom.add(checkGeologe);
        top.add(tabbedPane, BorderLayout.CENTER);
        frame.getContentPane().add(top, BorderLayout.NORTH);
        frame.getContentPane().add(bottom, BorderLayout.SOUTH);
        
        textOutProduction = new JLabel(strings.getString("dailyProd"));
        bottom.add(textOutProduction);
        astroliste = new JButton("Amortisation");
        astroliste.addActionListener(actionAmortlisteButton);
        bottom.add(astroliste);
        /* Table */
        //data  = new Object[][]{{"Name"},{"Metmine"},{"Krismine"},{"Deutmine"},{"Metlager"},{"Krislager"},{"Deuttank"},{"SKW"},{"FKW"},{"Sats"}};
		tabVsilentDropdownSet = false;
        tabV_numPlanetsDropdown.setSelectedIndex(acc.getPlanets().size());
        redrawFrame();
        frame.pack();
        frame.setVisible(true);
    }

    private void redrawFrame(){
	    
    	//System.out.printf("Selected tab: %d\n", tabbedPane.getSelectedIndex());
    	tableVsilentChange = tableAsilentChange = tableDsilentChange = tableFsilentChange = true;
    	setData();
    	tableVsilentChange = tableAsilentChange = tableDsilentChange = tableFsilentChange = false;

    	if(invertColors){
    		for(Component child : frame.getContentPane().getComponents()){
    			child.setBackground(Color.BLACK);
    			child.setForeground(Color.WHITE);
    			if(child instanceof JPanel){
    				for(Component schild : ((JPanel) child).getComponents()){
    	    			schild.setBackground(Color.BLACK);
    	    			schild.setForeground(Color.WHITE);
    	    		}
    			}
    		}
    		for(Component child : tabbedPane.getComponents()){
    			child.setBackground(Color.BLACK);
    			child.setForeground(Color.WHITE);
    			if(child instanceof JPanel){
    				for(Component schild : ((JPanel) child).getComponents()){
    	    			schild.setBackground(Color.BLACK);
    	    			schild.setForeground(Color.WHITE);
    	    			if(schild instanceof JPanel){
    	    				for(Component sschild : ((JPanel) schild).getComponents()){
    	    	    			sschild.setBackground(Color.BLACK);
    	    	    			sschild.setForeground(Color.WHITE);
    	    	    		}
    	    			}
    	    		}
    			}
    		}
    	}
		else{
    		for(Component child : frame.getContentPane().getComponents()){
    			child.setBackground(colorFrameBackBu);
    			child.setForeground(colorFrameForeBu);
    			if(child instanceof JPanel){
    				for(Component schild : ((JPanel) child).getComponents()){
    	    			schild.setBackground(colorFrameBackBu);
    	    			schild.setForeground(colorFrameForeBu);
    	    		}
    			}
    		}
    		for(Component child : tabbedPane.getComponents()){
    			child.setBackground(colorFrameBackBu);
    			child.setForeground(colorFrameForeBu);
    			if(child instanceof JPanel){
    				for(Component schild : ((JPanel) child).getComponents()){
    	    			schild.setBackground(colorFrameBackBu);
    	    			schild.setForeground(colorFrameForeBu);
    	    			if(schild instanceof JPanel){
    	    				for(Component sschild : ((JPanel) schild).getComponents()){
    	    	    			sschild.setBackground(colorFrameBackBu);
    	    	    			sschild.setForeground(colorFrameForeBu);
    	    	    		}
    	    			}
    	    		}
    			}
    		}			
		}
    	switch(tabbedPane.getSelectedIndex())
    	{
	    	case 0:
	        	if(acc.isGeologe())
	    			checkGeologe.setSelected(true);
	    		else
	    			checkGeologe.setSelected(false);
				tabV_table.setModel(tabV_model);
				for(int i = 0; i < tabV_model.getColumnCount(); i++)
					tabV_table.getColumnModel().getColumn(i).setMinWidth(80);

        		tabVersorgung.setVisible(true);
				tabV_table.invalidate();
				tabV_rightPanel.invalidate();
		        updateProduction();
				break;
	    	case 1:
	        	tabA_table.setModel(tabA_model);
				for(int i = 0; i < tabA_model.getColumnCount(); i++)
					tabA_table.getColumnModel().getColumn(i).setMinWidth(80);

        		tabAnlagen.setVisible(true);
				tabA_table.invalidate();
				tabA_rightPanel.invalidate();
				break;
	    	case 2:
	        	tabD_table.setModel(tabD_model);
				for(int i = 0; i < tabD_model.getColumnCount(); i++)
					tabD_table.getColumnModel().getColumn(i).setMinWidth(80);

        		tabVerteidigung.setVisible(true);
				tabD_table.invalidate();
				tabD_rightPanel.invalidate();
				break;
	    	case 3:
	        	tabF_table.setModel(tabF_model);
				for(int i = 0; i < tabF_model.getColumnCount(); i++)
					tabF_table.getColumnModel().getColumn(i).setMinWidth(120);

        		tabForschung.setVisible(true);
				tabF_table.invalidate();
				tabF_rightPanel.invalidate();
				break;
	    	case 4:
	        	tabN_table.setModel(tabN_model);
				for(int i = 0; i < tabN_model.getColumnCount(); i++)
					tabN_table.getColumnModel().getColumn(i).setMinWidth(120);

        		tabNeuerPlanet.setVisible(true);
				tabN_table.invalidate();
				tabN_rightPanel.invalidate();
				break;
	    		
    	}
        tabV_numPlanetsDropdown.setSelectedIndex(acc.getPlanets().size());
        top.invalidate();
		tabbedPane.invalidate();
        frame.pack();
    }
    
    private void setData(){
    	String[] empty = new String[acc.getPlanets().size()+1];
    	switch(currentTabIndex)
    	{
	    	case 0:
	    		if(tabV_model == null){
	    			tabV_model = new DefaultTableModel(){
	    				@Override
	    				public void setValueAt(Object value, int row, int col){
	    					super.setValueAt(value, row, col);
	    					fireTableCellUpdated(row, col);
	    				}
	    			};
	    			tabV_model.addTableModelListener(tabV_tableCellChanged);
	    		}
	    		tabV_model.setDataVector(acc.getVersorgungAllePlaneten(), empty);
	    		break;
	    	case 1:
	    		if(tabA_model == null){
	    			tabA_model = new DefaultTableModel(){
	    				@Override
	    				public void setValueAt(Object value, int row, int col){
	    					super.setValueAt(value, row, col);
	    					fireTableCellUpdated(row, col);
	    				}
	    			};
	    			tabA_model.addTableModelListener(tabA_tableCellChanged);
	    		}
	    		tabA_model.setDataVector(acc.getAnlagenAllePlaneten(), empty);
	    		break;
	    	case 2:
	    		if(tabD_model == null){
	    			tabD_model = new DefaultTableModel(){
	    				@Override
	    				public void setValueAt(Object value, int row, int col){
	    					super.setValueAt(value, row, col);
	    					fireTableCellUpdated(row, col);
	    				}
	    			};
	    			tabD_model.addTableModelListener(tabD_tableCellChanged);
	    		}
	    		tabD_model.setDataVector(acc.getVerteidigungAllePlaneten(), empty);
	    		break;
	    	case 3:
	    		if(tabF_model == null){
	    			tabF_model = new DefaultTableModel(){
	    				@Override
	    				public void setValueAt(Object value, int row, int col){
	    					super.setValueAt(value, row, col);
	    					fireTableCellUpdated(row, col);
	    				}
	    			};
	    			tabF_model.addTableModelListener(tabF_tableCellChanged);
	    		}
	    		tabF_model.setDataVector(acc.getTechnologien(), new String[2]);
	    		break;
	    	case 4:
	    		if(tabN_model == null){
	    			tabN_model = new DefaultTableModel(){
	    				@Override
	    				public void setValueAt(Object value, int row, int col){
	    					super.setValueAt(value, row, col);
	    					fireTableCellUpdated(row, col);
	    				}
	    			};
	    			tabN_model.addTableModelListener(tabN_tableCellChanged);
	    		}
	    		tabN_model.setDataVector(acc.getNeuerPlanet(), new String[2]);
	    		break;
	    		
    	}
    }
    
    private void storeModificationsToAcc(){
    	int tabIndex;
    	tabIndex = tabbedPane.getSelectedIndex();
    	for(int i = 1; i < acc.getPlanets().size()+1; i++)
    	{
    		switch(previousTabIndex)
    		{
	    		case 0:{
	    				/* Versorgungstabelle Eingaben */
	    				//System.out.printf("storeModToAcc. planet %d case %d\n",i,previousTabIndex);
	    	    		int[] colV = new int[12];
			    		for(int j = 1; j < 12; j++){
			    			//System.out.printf("Storing: i=%d j=%d\n", i, j);
			    			if(tabV_table.getValueAt(j, i) == null || tabV_table.getValueAt(j, i).toString().isEmpty())
			    				colV[j] = 0;
			    			else{
			    				if(tabV_table.getValueAt(j, i) == null)
			    					colV[j] = 0;
			    				else
			    					colV[j] = Integer.parseInt(tabV_table.getValueAt(j, i).toString());
			    			}
			    		}
			    		//System.out.printf("storeModToAcc. colV={%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d}\n", colV[0], colV[1], colV[2], colV[3], colV[4], colV[5], colV[6], colV[7], colV[8], colV[9], colV[10], colV[11]);
			    		acc.getPlanets().get(i-1).setVersorgung(colV);
			    		if(!(tabV_table.getValueAt(0, i) == null || tabV_table.getValueAt(0, i).toString().isEmpty()))
			    			acc.getPlanets().get(i-1).setName(tabV_table.getValueAt(0, i).toString());
			    		break;
	    		}
	    		case 1:{
    				//System.out.printf("storeModToAcc. planet %d case %d\n",i,previousTabIndex);
    				int[] colA = new int[8];
		    		for(int j = 1; j < 8; j++){
		    			//System.out.printf("Storing: i=%d j=%d\n", i, j);
		    			if(tabA_table.getValueAt(j, i) == null || tabA_table.getValueAt(j, i)!=null && tabA_table.getValueAt(j, i).toString().isEmpty())
		    				colA[j] = 0;
		    			else{
		    				if(tabA_table.getValueAt(j, i) == null)
		    					colA[j] = 0;
		    				else
		    					colA[j] = Integer.parseInt(tabA_table.getValueAt(j, i).toString());
		    			}
		    				
		    		}
		    		//System.out.printf("storeModToAcc. colA={%d,%d,%d,%d,%d,%d,%d,%d}\n", colA[0], colA[1], colA[2], colA[3], colA[4], colA[5], colA[6], colA[7]);
		    		acc.getPlanets().get(i-1).setAnlagen(colA);
		    		if(!(tabA_table.getValueAt(0, i) == null || tabA_table.getValueAt(0, i).toString().isEmpty()))
		    			acc.getPlanets().get(i-1).setName(tabA_table.getValueAt(0, i).toString());
		    		break;
	    		}
	    		case 2:{
    				//System.out.printf("storeModToAcc. planet %d case %d\n",i,previousTabIndex);
    				int[] colD = new int[9];
		    		for(int j = 1; j < 9; j++){
		    			//System.out.printf("Storing: i=%d j=%d\n", i, j);
		    			if(tabD_table.getValueAt(j, i) == null || tabD_table.getValueAt(j, i).toString().isEmpty())
		    				colD[j] = 0;
		    			else{
		    				if(tabD_table.getValueAt(j, i) == null)
		    					colD[j] = 0;
		    				else
		    					colD[j] = Integer.parseInt(tabD_table.getValueAt(j, i).toString());
		    			}
		    		}
		    		//System.out.printf("storeModToAcc. colD={%d,%d,%d,%d,%d,%d,%d,%d,%d}\n", colD[0], colD[1], colD[2], colD[3], colD[4], colD[5], colD[6], colD[7], colD[8]);
		    		acc.getPlanets().get(i-1).setVerteidigung(colD);
		    		if(!(tabD_table.getValueAt(0, i) == null || tabD_table.getValueAt(0, i).toString().isEmpty()))
		    			acc.getPlanets().get(i-1).setName(tabD_table.getValueAt(0, i).toString());
		    		break;
	    		}
    		}
    	}

    	if(previousTabIndex == 3)
    	{
			int[] colF = new int[16];
			for(int j = 0; j < 16; j++){
				if(tabF_table.getValueAt(j, 1) == null || tabF_table.getValueAt(j, 1).toString().isEmpty())
					colF[j] = 0;
				else{
					if(tabF_table.getValueAt(j, 1) == null)
						colF[j] = 0;
					else
						colF[j] = Integer.parseInt(tabF_table.getValueAt(j, 1).toString());
				}
			}
			acc.setTechnologien(colF);
    	}
    	if(previousTabIndex == 4)
    	{
			int[] colN = new int[20];
			for(int j = 0; j < 20; j++){
				if(tabN_table.getValueAt(j, 1) == null || tabN_table.getValueAt(j, 1).toString().isEmpty())
					colN[j] = 0;
				else{
					if(tabN_table.getValueAt(j, 1) == null)
						colN[j] = 0;
					else
						colN[j] = Integer.parseInt(tabN_table.getValueAt(j, 1).toString());
				}
			}
			acc.setNeuerPlanet(colN);
    	}
    }
    
    public void updateProduction(){
    	textOutProduction.setText(strings.getString("dailyProd")+": "+acc.getSProductionDay());
    }
    public static void main(String args[]) throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
    
    private Color complementaryColor(final Color bgColor) {

        Color complement = new Color(255 - bgColor.getRed(),
                255 - bgColor.getGreen(),
                255 - bgColor.getBlue());

        return complement;
    }
    public static void setJTextPaneFont(JTextPane jtp, Font font, Color back, Color fore) {
        // Start with the current input attributes for the JTextPane. This
        // should ensure that we do not wipe out any existing attributes
        // (such as alignment or other paragraph attributes) currently
        // set on the text area.
        MutableAttributeSet attrs = jtp.getInputAttributes();

        // Set the font family, size, and style, based on properties of
        // the Font object. Note that JTextPane supports a number of
        // character attributes beyond those supported by the Font class.
        // For example, underline, strike-through, super- and sub-script.
        StyleConstants.setFontFamily(attrs, font.getFamily());
        StyleConstants.setFontSize(attrs, font.getSize());
        StyleConstants.setItalic(attrs, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(attrs, (font.getStyle() & Font.BOLD) != 0);

        // Set the font color
        StyleConstants.setForeground(attrs, fore);
        StyleConstants.setBackground(attrs, back);

        // Retrieve the pane's document object
        StyledDocument doc = jtp.getStyledDocument();

        // Replace the style for the entire document. We exceed the length
        // of the document by 1 so that text entered at the end of the
        // document uses the attributes.
        doc.setCharacterAttributes(0, doc.getLength() + 1, attrs, false);
    }
}