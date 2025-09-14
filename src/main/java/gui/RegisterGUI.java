package gui;

import javax.swing.JFrame;

import domain.Driver;
import domain.Traveler;
import domain.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import businessLogic.BLFacade;

import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JButton;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.SwingConstants;
import java.awt.Color;

public class RegisterGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JLabel lblEmail = new JLabel("Correo electronico");
	private JLabel lblName = new JLabel("Nombre de usuario");
	private JLabel lblPassword = new JLabel("Contraseña");
	private JLabel lblConfirmPassword = new JLabel("Confirmar contraseña");
	private JLabel lblErrorMessage = new JLabel("");

	private JTextField flEmailRegister = new JTextField();
	private JTextField flNameRegister = new JTextField();
	private JPasswordField flPasswordRegister = new JPasswordField();
	private JPasswordField flConfirmPasswordRegister = new JPasswordField();
	
	private JRadioButton rdbtnTraveler = new JRadioButton("Soy viajero");
	private JRadioButton rdbtnDriver = new JRadioButton("Soy conductor");
	
	private JButton btnRegister = new JButton("Registrarse");
	private JButton btnVolver = new JButton("Volver");
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	public RegisterGUI() {
		this.setSize(495, 290);
		getContentPane().setLayout(null);	
		setTitle("Ventana registro");
		
		lblEmail.setBounds(71, 44, 126, 13);
		getContentPane().add(lblEmail);
		
		lblName.setBounds(71, 70, 126, 13);
		getContentPane().add(lblName);
		
		lblPassword.setBounds(71, 96, 126, 13);
		getContentPane().add(lblPassword);
		
		lblConfirmPassword.setBounds(71, 122, 126, 13);
		getContentPane().add(lblConfirmPassword);
		lblErrorMessage.setForeground(new Color(255, 0, 0));
		
		lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);	
		lblErrorMessage.setBounds(71, 19, 305, 13);
		getContentPane().add(lblErrorMessage);
		
		
		flEmailRegister.setBounds(240, 42, 136, 16);
		getContentPane().add(flEmailRegister);
		flEmailRegister.setColumns(10);
		
		flNameRegister.setBounds(240, 68, 136, 16);
		getContentPane().add(flNameRegister);
		flNameRegister.setColumns(10);
		
		flPasswordRegister.setBounds(240, 94, 136, 16);
		getContentPane().add(flPasswordRegister);
		
		flConfirmPasswordRegister.setBounds(240, 120, 136, 16);
		getContentPane().add(flConfirmPasswordRegister);

		
		rdbtnTraveler.addActionListener(new ActionListener(){	
			public void actionPerformed(ActionEvent e) {btnRegister.setEnabled(true);}}); 
		buttonGroup.add(rdbtnTraveler);
		rdbtnTraveler.setBounds(71, 157, 103, 21);
		getContentPane().add(rdbtnTraveler);

		
		rdbtnDriver.addActionListener(new ActionListener(){	
			public void actionPerformed(ActionEvent e) {btnRegister.setEnabled(true);}}); 
		buttonGroup.add(rdbtnDriver);
		rdbtnDriver.setBounds(240, 157, 136, 21);
		getContentPane().add(rdbtnDriver);
		
		
		btnRegister.setBounds(71, 204, 126, 21);
		btnRegister.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				String email = flEmailRegister.getText();
				String name = flNameRegister.getText();
				String passw1 = new String(flPasswordRegister.getPassword());
				String passw2 = new String(flConfirmPasswordRegister.getPassword());
				if(email.equals("") || name.equals("") || passw1.equals("") || passw2.equals(""))
					lblErrorMessage.setText("Rellena todos los campos");
				else {		
					if(!passw1.equals(passw2))
						lblErrorMessage.setText("Las contraseñas no coinciden");
					else {	
						String type = "";
						if(rdbtnTraveler.isSelected())
							type = "T";
						else if(rdbtnDriver.isSelected())
							type = "D";
						
						BLFacade facade = MainGUI.getBusinessLogic();
						User user = facade.registerUser(email, name, passw1, type);
						if(user != null)
							lblErrorMessage.setText("Correo ya registrado");	
						else {
							MainGUI M = new MainGUI();
							M.setLocationRelativeTo(null);
							M.jLabelSelectOption.setText("Registro completado");
							M.setVisible(true);
							dispose();
						}
					}
				}
			}			
		});
		getContentPane().add(btnRegister);
		btnRegister.setEnabled(false);
		
		
		btnVolver.setBounds(240, 204, 136, 21);
		btnVolver.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				MainGUI a = new MainGUI();
				a.setLocationRelativeTo(null);
				a.setVisible(true);
				dispose();
			}}
		);
		getContentPane().add(btnVolver);
	}
}