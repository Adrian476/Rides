package gui;

import javax.swing.JFrame;

import domain.Traveler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class TravelerGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JButton btnBookRide = new JButton("Solicitar reserva de viaje");
	private JButton btnExit = new JButton("Cerrar sesión");
	private JButton btnMonedero = new JButton("Monedero");
	//private JButton btnBookedRides = new JButton("Visualizar viajes reservados");
	
	public TravelerGUI(Traveler traveler) {
		setTitle("Ventana viajero");
		this.setSize(495, 290);
		getContentPane().setLayout(null);
		
		btnBookRide.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				BookRideGUI B = new BookRideGUI(traveler);
				B.setLocationRelativeTo(null);
				B.setVisible(true);
				dispose();
			}
		});
		btnBookRide.setBounds(140, 52, 202, 49);
		getContentPane().add(btnBookRide);
		
		btnMonedero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MonederoGUI Mo = new MonederoGUI(traveler);
				Mo.setLocationRelativeTo(null);
				Mo.setVisible(true);
				dispose();
			}
		});
		btnMonedero.setBounds(140, 112, 202, 49);
		getContentPane().add(btnMonedero);

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGUI M = new MainGUI();
				M.setLocationRelativeTo(null);
				M.setVisible(true);
				dispose();
			}
		});
		btnExit.setBounds(181, 199, 120, 21);
		getContentPane().add(btnExit);
		
		
		
		
	}
}
