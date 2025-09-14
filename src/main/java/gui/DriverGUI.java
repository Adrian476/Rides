package gui;

import javax.swing.JFrame;

import domain.Driver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class DriverGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public JButton btnFindRides = new JButton("Gestionar viajes");
	private JButton btnExit = new JButton("Cerrar sesión");
	
	public DriverGUI(Driver driver) {
		setTitle("Ventana conductor");
		this.setSize(495, 290);
		getContentPane().setLayout(null);
		
		btnFindRides.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				ManageRidesGUI G = new ManageRidesGUI(driver);
				G.setLocationRelativeTo(null);
				G.setVisible(true);
				dispose();
			}
		});
		btnFindRides.setBounds(112, 91, 239, 49);
		getContentPane().add(btnFindRides);
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGUI M = new MainGUI();
				M.setLocationRelativeTo(null);
				M.setVisible(true);
				dispose();
			}
		});
		btnExit.setBounds(166, 204, 129, 21);
		getContentPane().add(btnExit);
	}
}
