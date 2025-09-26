package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Ride;
import domain.Traveler;
import exceptions.MaxBookedRidesNumberReachedException;
import exceptions.NoMoneyException;
import exceptions.SameBookedRideDateException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Rectangle;
import javax.swing.JTextField;


public class BookRideGUI extends JFrame {
	private static final long serialVersionUID = 1L;
		
	private JLabel lblOrigin = new JLabel("Ciudad de origen");
	private JLabel lblDestination = new JLabel("Ciudad de destino");
	
	private JComboBox<String> cbOrigin = new JComboBox<String>();
	DefaultComboBoxModel<String> originCities = new DefaultComboBoxModel<String>();
	
	private JComboBox<String> cbDestination = new JComboBox<String>();
	DefaultComboBoxModel<String> destinationCities = new DefaultComboBoxModel<String>();
	DefaultComboBoxModel<Integer> numberSeats = new DefaultComboBoxModel<Integer>();
	DefaultComboBoxModel<String> throughCities = new DefaultComboBoxModel<String>();
	
	private JLabel lblDate = new JLabel("Selecciona una fecha");
	private JLabel lblAvailableRidesMessage = new JLabel("");

	
	private JButton btnBookRide = new JButton("Reservar viaje");
	
	private JButton btnVolver = new JButton("Volver");
	
	private final JRadioButton btnVerReservas = new JRadioButton("Viajes reservados");
	
	private final JButton btnCancelar = new JButton("Cancelar viaje");

	private JScrollPane tblScrollPaneEvents = new JScrollPane();

	private JTextArea txtAreaVisualizeStops = new JTextArea();

	private JButton btnVisualizeStops = new JButton("Ver paradas concretas");

	private JRadioButton rdbtnVerDisponibles = new JRadioButton("Viajes disponibles");
	
	private JTable tableRides= new JTable();

	private DefaultTableModel tableModelBookedRides;
	
	private String[] columnNamesBookedRides = new String[] {
			"Cod. viaje",
			"Origen",
			"Destino",
			"Parada",
			"Asientos",
			"Precio",
			"Estado"
	};

	private JRadioButton btnVerAceptados = new JRadioButton("Viajes aceptados");
	private JTextField codigoTextField = new JTextField();

	private JLabel lblLabelCodigo = new JLabel("Codigo viaje:");

	private JCalendar calendar = new JCalendar();

	private JLabel lblCash = new JLabel("Monedero: ");
	
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private JLabel lblDiscountNotification = new JLabel("");
	
	public BookRideGUI(Traveler traveler) {
			
			setTitle("Ventana solicitar viaje");
			this.setSize(802, 697);
			getContentPane().setLayout(null);
			
			lblOrigin.setBounds(51, 115, 137, 13);
			getContentPane().add(lblOrigin);
			
			lblDestination.setBounds(51, 170, 123, 13);
			getContentPane().add(lblDestination);
			
			BLFacade facade = MainGUI.getBusinessLogic();
			
			
			calendar.setBounds(300, 65, 184, 153);
			getContentPane().add(calendar);
			
			codigoTextField.setBounds(704, 267, 58, 20);
			getContentPane().add(codigoTextField);
			codigoTextField.setColumns(10);
			
			tblScrollPaneEvents.setBounds(new Rectangle(26, 308, 543, 193));
			
			tblScrollPaneEvents.setViewportView(tableRides);
			tableModelBookedRides = new DefaultTableModel(null, columnNamesBookedRides);
			tableRides.setModel(tableModelBookedRides);
			
			tblScrollPaneEvents.setBounds(50, 294, 543, 267);
			getContentPane().add(tblScrollPaneEvents);
			
			cbOrigin.setBounds(184, 111, 84, 21);
			getContentPane().add(cbOrigin);
			cbOrigin.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					destinationCities.removeAllElements();	
					List<String> originCities2 = facade.getDestinationCities((String)cbOrigin.getSelectedItem());
					for(String o:originCities2)
						destinationCities.addElement(o);
					
					String origin = (String) cbOrigin.getSelectedItem();
					String destination = (String) cbDestination.getSelectedItem();
					if(traveler.hasDiscountRide(origin, destination))
						lblDiscountNotification.setText("Descuento aplicado: de "+origin+" a "+destination);
					else
						lblDiscountNotification.setText("");
					
					updateListRides(traveler);

				}
			});
			
			cbOrigin.setModel(originCities);	
			List<String> origCities = facade.getDepartCities();
			for(String o: origCities)
				originCities.addElement(o);
			
			
			cbDestination.setBounds(184, 166, 87, 21);
			getContentPane().add(cbDestination);
			cbDestination.setModel(destinationCities);
			cbDestination.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {					
					String origin = (String) cbOrigin.getSelectedItem();
					String destination = (String) cbDestination.getSelectedItem();
					if(traveler.hasDiscountRide(origin, destination))
						lblDiscountNotification.setText("Descuento aplicado: de "+origin+" a "+destination);
					else
						lblDiscountNotification.setText("");
					
					updateListRides(traveler);
				}
			});
			numberSeats.addElement(1);
			numberSeats.addElement(2);
			numberSeats.addElement(3);
			numberSeats.addElement(4);
			
			calendar.addPropertyChangeListener(new PropertyChangeListener(){
				public void propertyChange(PropertyChangeEvent propertychangeevent){
					updateListRides(traveler);
				}
			});
			
			lblDate.setHorizontalAlignment(SwingConstants.CENTER);
			lblDate.setBounds(300, 42, 184, 13);
			getContentPane().add(lblDate);
			
			lblAvailableRidesMessage.setHorizontalAlignment(SwingConstants.CENTER);
			lblAvailableRidesMessage.setBounds(105, 581, 488, 13);
			getContentPane().add(lblAvailableRidesMessage);
			buttonGroup.clearSelection();
				

			
			
			//Aqui hay un pequeño error de consistencia!
			btnVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TravelerGUI T = new TravelerGUI(traveler);
					T.setLocationRelativeTo(null);
					T.setVisible(true);
					dispose();
				}
			});
			
			btnBookRide.setBounds(618, 507, 144, 21);
			getContentPane().add(btnBookRide);
			btnBookRide.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){		
					try{
						String origin = (String) cbOrigin.getSelectedItem();
						String destination = (String) cbDestination.getSelectedItem();

						int yr = calendar.getYearChooser().getYear();
						int mth = calendar.getMonthChooser().getMonth();
						int dy = calendar.getDayChooser().getDay();
						Date date = UtilDate.newDate(yr,mth,dy);
						
						Integer cod = Integer.parseInt(codigoTextField.getText());
	
						Ride ride = facade.bookRide(origin, destination, date, cod, traveler);
						if(ride == null)
							lblAvailableRidesMessage.setText("No se encuentra el viaje");
						else if(ride.getPrice() == -1)
							throw new NoMoneyException();
						else {		
							traveler.bookRide(ride);
							if(traveler.hasDiscountRide(ride.getFrom(), ride.getTo()))
								traveler.subCash((float) (ride.getPrice() - ride.getPrice() * 0.1));
							else
								traveler.subCash(ride.getPrice());
							
							DecimalFormat df = new DecimalFormat("0.00");
							
							lblAvailableRidesMessage.setText("Viaje reservado");
							lblCash.setText("Monedero: "+df.format(traveler.getCash())+"€");
	
						}
					}
					
					catch(NumberFormatException e2) {
						lblAvailableRidesMessage.setText("El codigo tiene que ser un numero entero");
					}
					
					catch(NoMoneyException e3) {
						lblAvailableRidesMessage.setText("No tienes suficiente dinero");
					}
					
					updateListRides(traveler);
				}
			
			});
		
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				try {
					String origin = (String)cbOrigin.getSelectedItem();
					String destination = (String)cbDestination.getSelectedItem();
					Date date = UtilDate.trim(calendar.getDate());
					
					Integer cod = Integer.parseInt(codigoTextField.getText());
									
					Ride ride = facade.cancelRide(origin, destination, date, cod, traveler.getEmail());
					if(ride == null)
						lblAvailableRidesMessage.setText("No se encuentra el viaje");
					else {
						traveler.cancelRide(ride);
						DecimalFormat df = new DecimalFormat("0.00");
						lblCash.setText("Monedero: "+df.format(traveler.getCash())+"€");
					}
				}
				catch(NumberFormatException e2) {
					lblAvailableRidesMessage.setText("El codigo tiene que ser un numero entero");
				}
					updateListRides(traveler);
				}
			});
			
		btnCancelar.setBounds(618, 538, 144, 23);
		getContentPane().add(btnCancelar);
		
			
		btnVolver.setBounds(285, 604, 123, 23);	
		getContentPane().add(btnVolver);
		
		txtAreaVisualizeStops.setBounds(618, 328, 144, 169);
		getContentPane().add(txtAreaVisualizeStops);
		btnVisualizeStops.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					txtAreaVisualizeStops.setText("");
					lblAvailableRidesMessage.setText("");
					Integer rn = Integer.parseInt(codigoTextField.getText());
					
					String origin = (String) cbOrigin.getSelectedItem();
					String destination = (String) cbDestination.getSelectedItem();
					Date date = UtilDate.trim(calendar.getDate());
					
					String state;
					
					if(rdbtnVerDisponibles.isSelected()) {
						state = "pendiente";
					}
					else if (btnVerReservas.isSelected()) {
						state = "reservado";
					}
					else 
						state = "confirmado";
					
					String s = facade.getRideStopsByCod(origin, destination, date, state, rn);
					
					txtAreaVisualizeStops.setText(s);
				}
				catch (NumberFormatException e1){
					lblAvailableRidesMessage.setText("El codigo tiene que ser un numero entero");
				}
			}
		});
		
		btnVisualizeStops.setBounds(614, 297, 148, 21);
		getContentPane().add(btnVisualizeStops);
		buttonGroup.add(rdbtnVerDisponibles);
		
		rdbtnVerDisponibles.setBounds(51, 265, 158, 23);
		rdbtnVerDisponibles.setSelected(true);
		btnCancelar.setEnabled(false);
		rdbtnVerDisponibles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateListRides(traveler);
				txtAreaVisualizeStops.setText("");
			}
		});
		getContentPane().add(rdbtnVerDisponibles);
		buttonGroup.add(btnVerReservas);
		
		btnVerReservas.setBounds(211, 265, 152, 23);
		btnVerReservas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateListRides(traveler);
				txtAreaVisualizeStops.setText("");
			}
		});
		getContentPane().add(btnVerReservas);
		buttonGroup.add(btnVerAceptados);
		
		btnVerAceptados.setBounds(365, 265, 123, 23);
		btnVerAceptados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateListRides(traveler);
				txtAreaVisualizeStops.setText("");			
			}
		});
		getContentPane().add(btnVerAceptados);
		
		
		
		
		
		
		lblLabelCodigo.setBounds(618, 273, 87, 14);
		getContentPane().add(lblLabelCodigo);
		
		lblCash.setBounds(51, 52, 123, 13);
		getContentPane().add(lblCash);
		DecimalFormat df = new DecimalFormat("0.00");
		lblCash.setText("Monedero: "+df.format(traveler.getCash())+"€");	

		lblDiscountNotification.setBounds(51, 239, 433, 20);
		getContentPane().add(lblDiscountNotification);
		
		String origin = (String) cbOrigin.getSelectedItem();
		String destination = (String) cbDestination.getSelectedItem();
		if(traveler.hasDiscountRide(origin, destination))
			lblDiscountNotification.setText("Descuento aplicado: de "+origin+" a "+destination);
		else
			lblDiscountNotification.setText("");
	}
	
	public void updateListRides(Traveler t) {
		tableModelBookedRides.setDataVector(null, columnNamesBookedRides);
		tableModelBookedRides.setColumnCount(7);
		
		String origin = (String) cbOrigin.getSelectedItem();
		String destination = (String) cbDestination.getSelectedItem();
		Date date = UtilDate.trim(calendar.getDate());
		
		List<Ride> listRides;
		BLFacade facade = MainGUI.getBusinessLogic();
		if(rdbtnVerDisponibles.isSelected()) {
			listRides = facade.getRides(origin, destination, date, "pendiente");
			btnCancelar.setEnabled(false);
			if(listRides.isEmpty())
				btnBookRide.setEnabled(false);
			else
				btnBookRide.setEnabled(true);
		}
		else if (btnVerReservas.isSelected()) {
			t = facade.getTraveler(t.getEmail());
			listRides = t.getBookedRidesFromDate(origin, destination, date);
			btnBookRide.setEnabled(false);
			btnCancelar.setEnabled(false);
					
		}
		else {
			listRides = facade.getAcceptedRides(origin, destination, date, t.getEmail());
			btnBookRide.setEnabled(false);
			if(listRides.isEmpty())
				btnCancelar.setEnabled(false);
			else
				btnCancelar.setEnabled(true);
		}	
		System.out.println(listRides);
		
		for (Ride ride: listRides){
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
			if(rdbtnVerDisponibles.isSelected()) {
				if(t.hasDiscountRide(ride.getFrom(), ride.getTo()))
					row.add(df.format(ride.getPrice() - ride.getPrice() * 0.1)+"€ (D)");
				else
					row.add(ride.getPrice()+"€");
			}
			else if(ride.hasDiscount())
				row.add(df.format(ride.getPrice() - ride.getPrice() * 0.1)+"€ (D)");
			else
				row.add(ride.getPrice()+"€");
			
			row.add(ride.getState());
			
			tableModelBookedRides.addRow(row);	
		}
	}
}