package gui;

import javax.swing.JFrame;

import domain.Traveler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MonederoGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	
	private JButton btnExit = new JButton("Volver");
	private JTextField textDinero = new JTextField();


	private JLabel lblSaldoActual = new JLabel("Saldo actual: ");
	private JButton btnAddCash = new JButton("Añadir al monedero");
	private final JLabel lbltext = new JLabel("Saldo a añadir: ");
	private final JLabel lblError = new JLabel("");

	
	public MonederoGUI(Traveler traveler) {
		setTitle("Monedero");
		this.setSize(495, 290);
		getContentPane().setLayout(null);
		
		DecimalFormat df = new DecimalFormat("0.00");
		lblSaldoActual.setText("Saldo actual: "+String.valueOf(df.format(traveler.getCash())+"€"));
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TravelerGUI T = new TravelerGUI(traveler);
				T.setLocationRelativeTo(null);
				T.setVisible(true);
				dispose();
			}
		});
		btnExit.setBounds(372, 219, 97, 21);
		getContentPane().add(btnExit);
		
		
		lblSaldoActual.setBounds(159, 69, 178, 14);
		getContentPane().add(lblSaldoActual);
		
		
		textDinero.setBounds(250, 106, 62, 20);
		getContentPane().add(textDinero);
		textDinero.setColumns(10);
		
		
		btnAddCash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					float dinero = 	Float.parseFloat(textDinero.getText());
					if(dinero>=0 && dinero<=1000) {
						traveler.addCash((float)dinero);
						DecimalFormat df = new DecimalFormat("0.00");
						lblSaldoActual.setText("Saldo actual: "+String.valueOf(df.format(traveler.getCash())+"€"));
						textDinero.setText(null);
						lblError.setText(null);
					}
					else throw new NumberFormatException();
				}
				catch (NumberFormatException e1) {
					lblError.setText("El saldo a añadir debe ser un numero entre 0 y 1000");
				}
			}
		});
		
		
		btnAddCash.setBounds(123, 158, 214, 41);
		getContentPane().add(btnAddCash);
		
		lbltext.setBounds(159, 108, 97, 14);
		getContentPane().add(lbltext);
		
		lblError.setBounds(10, 222, 352, 14);
		getContentPane().add(lblError);
		
		
		
	}
}
