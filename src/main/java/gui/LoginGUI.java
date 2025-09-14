package gui;

import javax.swing.JFrame;

import domain.Driver;
import domain.Traveler;
import domain.User;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import businessLogic.BLFacade;
import javax.swing.SwingConstants;
import java.awt.Color;

public class LoginGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private JLabel lblName = new JLabel("Correo electrónico");
	private JLabel lblPassword = new JLabel("Contraseña");
	private JLabel lblErrorMessage = new JLabel("");
	
	private JTextField flNameLogin = new JTextField();
	private JPasswordField flPasswordLogin = new JPasswordField();
	
	private JButton btnLogin = new JButton("Iniciar sesion");
	private JButton btnBack = new JButton("Volver");;
	
	public LoginGUI() {
		this.setSize(495, 290);
		getContentPane().setLayout(null);
		setTitle("Ventana inicio sesion");
		
		lblName.setBounds(71, 79, 126, 13);
		getContentPane().add(lblName);
		
		lblPassword.setBounds(71, 125, 126, 13);
		getContentPane().add(lblPassword);
		lblErrorMessage.setForeground(new Color(255, 0, 0));
		
		lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblErrorMessage.setBounds(71, 39, 305, 13);
		getContentPane().add(lblErrorMessage);
		
		
		flNameLogin.setBounds(240, 76, 136, 16);
		getContentPane().add(flNameLogin);
		flNameLogin.setColumns(10);
		
		flPasswordLogin.setBounds(240, 122, 136, 16);
		getContentPane().add(flPasswordLogin);
		
		
		btnLogin.setBounds(71, 204, 126, 21);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = flNameLogin.getText();
				String passw = new String(flPasswordLogin.getPassword());		
				if(email.equals("") || passw.equals(""))
					lblErrorMessage.setText("Rellena todos los campos");
				else {
					BLFacade facade = MainGUI.getBusinessLogic();
					User user = facade.loginUser(email, passw); 
					if(user != null) {
						if(user.getPassword() != null) {
							if(user.getTraveler() != null) {
								TravelerGUI T = new TravelerGUI(user.getTraveler());
								T.setLocationRelativeTo(null);
								T.setVisible(true);
							}
							else if(user.getDriver() != null) {
								DriverGUI D = new DriverGUI(user.getDriver());
								D.setLocationRelativeTo(null);
								D.setVisible(true);
							}
							dispose();
						}
						else
							lblErrorMessage.setText("Contraseña incorrecta");
					}
					else 
						lblErrorMessage.setText("El correo no existe");
				}
			}
		});
		getContentPane().add(btnLogin);
		
		
		btnBack.setBounds(240, 204, 136, 21);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainGUI a = new MainGUI();
				a.setLocationRelativeTo(null);
				a.setVisible(true);
				dispose();
			}
		});
		getContentPane().add(btnBack);
	}
}
