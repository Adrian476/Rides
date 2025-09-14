package gui;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Driver;
import domain.Ride;


import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;


public class ManageRidesGUI extends JFrame {
	private static final long serialVersionUID = 1L;


	private JComboBox<String> jComboBoxOrigin = new JComboBox<String>();
	DefaultComboBoxModel<String> originCities = new DefaultComboBoxModel<String>();

	private JComboBox<String> jComboBoxDestination = new JComboBox<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();

	private JComboBox<Integer> cbSeats = new JComboBox<Integer>();
	DefaultComboBoxModel<Integer> nSeats = new DefaultComboBoxModel<Integer>();
	
	private JComboBox<String> jComboBoxStops = new JComboBox<String>();
	DefaultComboBoxModel<String> throughCities = new DefaultComboBoxModel<String>();

	

	private JLabel lblOrigin = new JLabel("Origen");
	private JLabel lblDestination = new JLabel("Destino");
	private JLabel lblSeats = new JLabel("N. asientos");
	private final JLabel lblEventDate = new JLabel("Selecciona una fecha");
	private JLabel lblPrice = new JLabel("Precio");
	private JLabel lblStop = new JLabel("Parada");
	
	private JTextField txtFieldPrice = new JTextField();
	private JTextField txtFieldStop = new JTextField();
	
	private JTextArea txtAreaListStops = new JTextArea();
	private JTextArea txtAreaVisualizeStops = new JTextArea();

	private JLabel lblRideNumber = new JLabel("Código viaje");
	private JButton btnAcceptRide = new JButton("Aceptar reserva");

	private JButton btnAddStop = new JButton("Añadir parada");
	private JButton btnCreateRide = new JButton("Crear viaje");
	private JButton btnClose = new JButton("Cerrar");
	private JButton btnVisualizeStops = new JButton("Ver paradas");

	// Code for JCalendar
	private JCalendar jCalendar1 = new JCalendar();
	private Calendar calendarAnt = null;
	private Calendar calendarAct = null;
	private JScrollPane scrollPaneEvents = new JScrollPane();

	private List<Date> datesWithRidesCurrentMonth = new Vector<Date>();

	private JTable tableRides= new JTable();

	private DefaultTableModel tableModelBookedRides;


	private String[] columnNamesBookedRides = new String[] {
			"Cod. viaje",
			"Origen",
			"Destino",
			"Paradas",
			"Asientos",
			"Precio",
			"Estado"
	};
	private final JLabel lblMessage = new JLabel("");

	private List<String> stopsList = new ArrayList<String>();
	private final JTextField txtFieldRideNumber = new JTextField();
	
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnPending = new JRadioButton("Viajes creados");
	private JRadioButton rdbtnBooked = new JRadioButton("Viajes reservados");
	private JRadioButton rdbtnAccepted = new JRadioButton("Viajes confirmados");

	private JLabel lblMessageRideCreated = new JLabel("");

	private Driver driver;

	public ManageRidesGUI(Driver d)
	{
		driver = d;
		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(795, 725));
		this.setTitle("Ventana visualizar viajes");
		lblEventDate.setHorizontalAlignment(SwingConstants.CENTER);

		lblEventDate.setBounds(new Rectangle(500, 27, 225, 13));

		this.getContentPane().add(lblEventDate, null);

		btnClose.setBounds(new Rectangle(302, 638, 128, 25));

		btnClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				DriverGUI D = new DriverGUI(driver);
				D.setLocationRelativeTo(null);
				D.setVisible(true);
				dispose();
			}
		});
		BLFacade facade = MainGUI.getBusinessLogic();
		List<String> origins=facade.getDepartCities();
		
		for(String location:origins) originCities.addElement(location);
		
		lblOrigin.setBounds(new Rectangle(26, 50, 92, 20));
		lblDestination.setBounds(26, 80, 61, 20);
		getContentPane().add(lblOrigin);

		getContentPane().add(lblDestination);

		jComboBoxOrigin.setModel(originCities);
		jComboBoxOrigin.setBounds(new Rectangle(145, 50, 130, 20));
		
		String origin = (String)jComboBoxOrigin.getSelectedItem();
				
		List<String> aCities=facade.getDestinationCities(origin);
		for(String aciti:aCities) {
			destinationCities.addElement(aciti);
		}
		
		
		jComboBoxOrigin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				lblMessage.setText("");
				String origin = (String)jComboBoxOrigin.getSelectedItem();
				String destination = (String)jComboBoxDestination.getSelectedItem();
				destinationCities.removeAllElements();

				Date selectedDate = jCalendar1.getDate();

				datesWithRidesCurrentMonth = facade.getThisMonthDatesWithRides(origin, destination, selectedDate);
				paintDaysWithEvents(jCalendar1, datesWithRidesCurrentMonth, Color.WHITE);
				
				
				BLFacade facade = MainGUI.getBusinessLogic();
				List<String> aCities=facade.getDestinationCities(origin);
				for(String aciti:aCities) 
					destinationCities.addElement(aciti);	
	
				throughCities.removeAllElements();
				List<String> throughCities2 = facade.getThroughCities(origin, destination);	
				for(String t: throughCities2)
					if(t !=null) throughCities.addElement(t);
				
				txtAreaListStops.setText("Sin paradas");
				txtAreaVisualizeStops.setText("");
				lblMessageRideCreated.setText("");
				
				btnAddStop.setEnabled(true);
				
				updateListRides(driver);
						
				stopsList = new ArrayList<String>();
			}
		});
		
		jComboBoxDestination.setModel(destinationCities);
		jComboBoxDestination.setBounds(new Rectangle(145, 80, 130, 20));
		jComboBoxDestination.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
			
				lblMessage.setText("");
				BLFacade facade = MainGUI.getBusinessLogic();

				String origin = (String)jComboBoxOrigin.getSelectedItem();
				String destination = (String)jComboBoxDestination.getSelectedItem();
				Date selectedDate = jCalendar1.getDate();
				
				datesWithRidesCurrentMonth = facade.getThisMonthDatesWithRides(origin, destination, selectedDate);	
				paintDaysWithEvents(jCalendar1, datesWithRidesCurrentMonth, Color.CYAN);
				
				throughCities.removeAllElements();
				List<String> throughCities2 = facade.getThroughCities(origin, destination);	
				for(String t: throughCities2)
					if(t !=null) throughCities.addElement(t);
				
				txtAreaListStops.setText("Sin paradas");
				txtAreaVisualizeStops.setText("");
				lblMessageRideCreated.setText("");
				
				btnAddStop.setEnabled(true);
				
				updateListRides(driver);
				
				stopsList = new ArrayList<String>();

			}
		});
		
		String destination = (String)jComboBoxDestination.getSelectedItem();

		Date selectedDate = jCalendar1.getDate();
		datesWithRidesCurrentMonth = facade.getThisMonthDatesWithRides(origin, destination, selectedDate);
		paintDaysWithEvents(jCalendar1, datesWithRidesCurrentMonth, Color.WHITE);
		

		this.getContentPane().add(btnClose, null);
		this.getContentPane().add(jComboBoxOrigin, null);

		this.getContentPane().add(jComboBoxDestination, null);


		jCalendar1.setBounds(new Rectangle(500, 50, 225, 150));


		// Code for JCalendar
		jCalendar1.addPropertyChangeListener(new PropertyChangeListener()
		{
			public void propertyChange(PropertyChangeEvent propertychangeevent)
			{

				if (propertychangeevent.getPropertyName().equals("locale"))
				{
					jCalendar1.setLocale((Locale) propertychangeevent.getNewValue());
				}
				else if (propertychangeevent.getPropertyName().equals("calendar"))
				{
					calendarAnt = (Calendar) propertychangeevent.getOldValue();
					calendarAct = (Calendar) propertychangeevent.getNewValue();
					

					

					int monthAnt = calendarAnt.get(Calendar.MONTH);
					int monthAct = calendarAct.get(Calendar.MONTH);

					if (monthAct!=monthAnt) {
						if (monthAct==monthAnt+2) {
							// Si en JCalendar está 30 de enero y se avanza al mes siguiente, devolvería 2 de marzo (se toma como equivalente a 30 de febrero)
							// Con este código se dejará como 1 de febrero en el JCalendar
							calendarAct.set(Calendar.MONTH, monthAnt+1);
							calendarAct.set(Calendar.DAY_OF_MONTH, 1);
						}						

						jCalendar1.setCalendar(calendarAct);

					}
					
					try {
						tableModelBookedRides.setDataVector(null, columnNamesBookedRides);
						tableModelBookedRides.setColumnCount(7);
						
						lblMessage.setText("");
						String origin = (String)jComboBoxOrigin.getSelectedItem();
						String destination = (String)jComboBoxDestination.getSelectedItem();
						Date selectedDate = jCalendar1.getDate();
						
						BLFacade facade = MainGUI.getBusinessLogic();

						txtAreaVisualizeStops.setText("");
						lblMessageRideCreated.setText("");
						
						datesWithRidesCurrentMonth = facade.getThisMonthDatesWithRides(origin, destination, selectedDate);
						paintDaysWithEvents(jCalendar1, datesWithRidesCurrentMonth, Color.CYAN);
						
						updateListRides(driver);
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			} 
			
		});
		this.getContentPane().add(jCalendar1, null);

		scrollPaneEvents.setBounds(new Rectangle(26, 337, 571, 272));

		scrollPaneEvents.setViewportView(tableRides);
		tableModelBookedRides = new DefaultTableModel(null, columnNamesBookedRides);
		tableRides.setModel(tableModelBookedRides);

		this.getContentPane().add(scrollPaneEvents, null);
		
		Date date = UtilDate.trim(jCalendar1.getDate());
		
		datesWithRidesCurrentMonth = facade.getThisMonthDatesWithRides(origin, destination, date);
		paintDaysWithEvents(jCalendar1, datesWithRidesCurrentMonth, Color.CYAN);
		
		
		lblPrice.setBounds(26, 140, 45, 20);
		getContentPane().add(lblPrice);
		
		txtFieldPrice.setBounds(145, 140, 54, 20);
		getContentPane().add(txtFieldPrice);
		txtFieldPrice.setColumns(10);
		
		lblRideNumber.setBounds(622, 311, 75, 20);
		getContentPane().add(lblRideNumber);

		
		jCalendar1.setDate(date);
		
		btnCreateRide.setBounds(274, 240, 197, 30);
		getContentPane().add(btnCreateRide);
		btnCreateRide.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {				
					BLFacade facade = MainGUI.getBusinessLogic();
					if(txtFieldPrice.getText().equals("")) 
						lblMessageRideCreated.setText("Rellena todos los campos");
					else {				
						String origin = (String)jComboBoxOrigin.getSelectedItem();
						String destination = (String)jComboBoxDestination.getSelectedItem();
						Date date = UtilDate.trim(jCalendar1.getDate());
						Integer seats = (Integer)cbSeats.getSelectedItem();
						boolean pric = txtFieldPrice.getText().matches("[+-]?\\d*(\\.\\d+)?");
						float price = 0;
						if(!pric) 
							lblMessageRideCreated.setText("El precio debe ser un número");
						else{
							price = Float.parseFloat(txtFieldPrice.getText());
							if(price <= 0.0) throw new NumberFormatException();	
							
							Ride r = facade.createRide(origin, destination, stopsList, date, seats, price, driver);
							driver.addCreatedRide(r);
							updateListRides(driver);	
	
							txtAreaListStops.setText("Sin paradas");
							stopsList = new ArrayList<String>();
	
							lblMessageRideCreated.setText("Viaje creado");
							lblMessage.setText("Viajes encontrados:");
							
							btnAddStop.setEnabled(true);
						}	
					}	
				}
				catch (NumberFormatException e1) {
					lblMessageRideCreated.setText("El precio debe ser mayor que 0");
				}
			}
		});

		
		lblMessage.setHorizontalAlignment(SwingConstants.LEFT);
		lblMessage.setBounds(26, 280, 699, 18);	
		getContentPane().add(lblMessage);
		
		cbSeats.setBounds(145, 110, 54, 20);
		getContentPane().add(cbSeats);
		cbSeats.setModel(nSeats);
		nSeats.addElement(1);
		nSeats.addElement(2);
		nSeats.addElement(3);
		nSeats.addElement(4);
		nSeats.addElement(5);
		
		lblSeats.setBounds(26, 110, 92, 20);
		getContentPane().add(lblSeats);
		
		txtAreaListStops.setBounds(302, 48, 128, 152);
		getContentPane().add(txtAreaListStops);
		txtAreaListStops.setText("Sin paradas");
		
		btnAcceptRide.setBounds(622, 579, 128, 30);
		getContentPane().add(btnAcceptRide);
		btnAcceptRide.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try{
					String origin = (String)jComboBoxOrigin.getSelectedItem();
					String destination = (String)jComboBoxDestination.getSelectedItem();
					Date date = UtilDate.trim(jCalendar1.getDate());
				
					Integer rideNumber = Integer.parseInt(txtFieldRideNumber.getText());
				
					Ride ride = facade.acceptRide(origin, destination, date, rideNumber, driver.getEmail());	
					if(ride == null)
						lblMessageRideCreated.setText("No se encuentra el viaje");
					else {
						lblMessageRideCreated.setText("Viaje aceptado");
						driver = ride.getDriverInfo();
						updateListRides(driver);
					}
				}
				catch(NumberFormatException e2) {
					lblMessageRideCreated.setText("El codigo tiene que ser un numero entero");
				}
					
			}
		});
		btnAcceptRide.setEnabled(false);
		
		lblStop.setBounds(26, 170, 92, 20);
		getContentPane().add(lblStop);
		
		jComboBoxStops.setBounds(145, 170, 130, 19);
		getContentPane().add(jComboBoxStops);
		jComboBoxStops.setModel(throughCities);
		jComboBoxStops.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(stopsList.contains((String)jComboBoxStops.getSelectedItem()))
					btnAddStop.setEnabled(false);
				else
					btnAddStop.setEnabled(true);
			}	
		});
		
		List<String> throughCities2 = facade.getThroughCities(origin, destination);	
		for(String t: throughCities2)
			if(t !=null) throughCities.addElement(t);
		
		btnAddStop.setBounds(26, 200, 151, 21);
		getContentPane().add(btnAddStop);
		
		getContentPane().add(txtFieldRideNumber);
		btnAddStop.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				String stop = (String)throughCities.getSelectedItem();
				if(txtAreaListStops.getText().equals("Sin paradas"))
					txtAreaListStops.setText("");
				txtAreaListStops.setText(txtAreaListStops.getText()+stop+"\n");
				stopsList.add(stop);
				btnAddStop.setEnabled(false);
			}
		});
		
		txtFieldRideNumber.setBounds(705, 312, 45, 19);
		txtFieldRideNumber.setColumns(10);
		
		txtAreaVisualizeStops.setBounds(622, 386, 128, 170);
		getContentPane().add(txtAreaVisualizeStops);
		txtAreaVisualizeStops.setText("");
		
		btnVisualizeStops.setBounds(622, 346, 128, 21);
		getContentPane().add(btnVisualizeStops);
		
		lblMessageRideCreated.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessageRideCreated.setBounds(244, 217, 257, 13);
		getContentPane().add(lblMessageRideCreated);
		btnVisualizeStops.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				try {
					lblMessageRideCreated.setText("");
					txtAreaVisualizeStops.setText("");
					String origin = (String)jComboBoxOrigin.getSelectedItem();
					String destination = (String)jComboBoxDestination.getSelectedItem();
					Integer code = Integer.parseInt(txtFieldRideNumber.getText());
					Date date = UtilDate.trim(jCalendar1.getDate());
					String state = "";
					if(rdbtnPending.isSelected())
						state = "p";
					else if(rdbtnBooked.isSelected())
						state = "b";
					else
						state = "a";
					
					driver = facade.getDriver(driver.getEmail());
					txtAreaVisualizeStops.setText(driver.getStopsFromDate(origin, destination, code, date, state));
				}
				catch (NumberFormatException e1) {
					lblMessageRideCreated.setText("El codigo tiene que ser un numero entero");
				}
			}
		});
		
		
		buttonGroup.add(rdbtnPending);
		rdbtnPending.setBounds(26, 310, 140, 21);
		getContentPane().add(rdbtnPending);
		rdbtnPending.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				updateListRides(driver);
			}
		});
		rdbtnPending.setSelected(true);
		
		buttonGroup.add(rdbtnBooked);
		rdbtnBooked.setBounds(176, 310, 128, 21);
		getContentPane().add(rdbtnBooked);
		rdbtnBooked.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				updateListRides(driver);
			}
		});
		
		buttonGroup.add(rdbtnAccepted);		
		rdbtnAccepted.setBounds(336, 311, 165, 21);
		getContentPane().add(rdbtnAccepted);
		rdbtnAccepted.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				updateListRides(driver);
			}
		});
		
		updateListRides(driver);	
		
	}
	public static void paintDaysWithEvents(JCalendar jCalendar,List<Date> datesWithEventsCurrentMonth, Color color) {
		//		// For each day with events in current month, the background color for that day is changed to cyan.


		Calendar calendar = jCalendar.getCalendar();

		int month = calendar.get(Calendar.MONTH);
		int today=calendar.get(Calendar.DAY_OF_MONTH);
		int year=calendar.get(Calendar.YEAR);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int offset = calendar.get(Calendar.DAY_OF_WEEK);

		if (Locale.getDefault().equals(new Locale("es")))
			offset += 4;
		else
			offset += 5;


		for (Date d:datesWithEventsCurrentMonth){

			calendar.setTime(d);


			// Obtain the component of the day in the panel of the DayChooser of the
			// JCalendar.
			// The component is located after the decorator buttons of "Sun", "Mon",... or
			// "Lun", "Mar"...,
			// the empty days before day 1 of month, and all the days previous to each day.
			// That number of components is calculated with "offset" and is different in
			// English and Spanish
			//			    		  Component o=(Component) jCalendar.getDayChooser().getDayPanel().getComponent(i+offset);; 
			Component o = (Component) jCalendar.getDayChooser().getDayPanel()
					.getComponent(calendar.get(Calendar.DAY_OF_MONTH) + offset);
			o.setBackground(color);
		}

		calendar.set(Calendar.DAY_OF_MONTH, today);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		
	}
	
	public void updateListRides(Driver d) {
		tableModelBookedRides.setDataVector(null, columnNamesBookedRides);
		tableModelBookedRides.setColumnCount(7);
		

		String origin = (String)jComboBoxOrigin.getSelectedItem();
		String destination = (String)jComboBoxDestination.getSelectedItem();
		Date date = UtilDate.trim(jCalendar1.getDate());
		
		List<Ride> rides;
		BLFacade facade = MainGUI.getBusinessLogic();
		d = facade.getDriver(d.getEmail());
		
		if(rdbtnPending.isSelected()) 
			rides = d.getCreatedRidesFromDate(origin, destination, date);
		else if(rdbtnBooked.isSelected())
			rides = d.getBookedRidesFromDate(origin, destination, date);
		else
			rides = d.getAcceptedRidesFromDate(origin, destination, date);
		
		d = facade.getDriver(d.getEmail());
		
		if (rides.isEmpty()) {
			lblMessage.setText("No hay viajes en esa fecha");
			btnAcceptRide.setEnabled(false);
		}
		else {
			if(rdbtnBooked.isSelected()) 
				btnAcceptRide.setEnabled(true);
			else
				btnAcceptRide.setEnabled(false);
			
			lblMessage.setText("Viajes encontrados:");
			for (Ride ride: rides){
				Vector<Object> row = new Vector<Object>();
				row.add(ride.getRideNumber());
				row.add(ride.getFrom());
				row.add(ride.getTo());
				if(ride.getStops().isEmpty())
					row.add("No");
				else 
					row.add(ride.getStops().size());
				
				row.add(ride.getnSeats());
				DecimalFormat df = new DecimalFormat("0.00");
				if(ride.hasDiscount())
					row.add(df.format(ride.getPrice() - ride.getPrice() * 0.1)+"€ (D)");
				else
					row.add(ride.getPrice()+"€");
				
				row.add(ride.getState());
				
				tableModelBookedRides.addRow(row);	
			}
		}
	}
}