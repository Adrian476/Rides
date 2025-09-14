package gui;

/**
 * @author Software Engineering teachers
 */


import javax.swing.*;

import businessLogic.BLFacade;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;
	private JButton jButtonLogin = null;
	private JButton jButtonRegister = null;

    private static BLFacade appFacadeInterface;
	
	public static BLFacade getBusinessLogic(){
		return appFacadeInterface;
	}
	 
	public static void setBussinessLogic (BLFacade afi){
		appFacadeInterface=afi;
	}
	protected JLabel jLabelSelectOption;
	
	/**
	 * This is the default constructor
	 */
	public MainGUI() {
		super();
		
		// this.setSize(271, 295);
		this.setSize(495, 290);
		jLabelSelectOption = new JLabel("Bienvenido");
		jLabelSelectOption.setBounds(0, 0, 479, 62);
		jLabelSelectOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		jLabelSelectOption.setForeground(Color.BLACK);
		jLabelSelectOption.setHorizontalAlignment(SwingConstants.CENTER);
		
		jButtonLogin = new JButton();
		jButtonLogin.setBounds(155, 95, 177, 29);
		jButtonLogin.setText("Inicia sesion");
		jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame login = new LoginGUI(); 
				login.setLocationRelativeTo(null);
				login.setVisible(true);
				dispose();
			}
		});
		
		jButtonRegister = new JButton();
		jButtonRegister.setBounds(155, 166, 177, 29);
		jButtonRegister.setText("Registrarse");
		jButtonRegister.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				JFrame register = new RegisterGUI();
				register.setLocationRelativeTo(null);
				register.setVisible(rootPaneCheckingEnabled);
				dispose();
			}
		});
		
		jContentPane = new JPanel();
		jContentPane.setLayout(null);
		jContentPane.add(jLabelSelectOption);
		jContentPane.add(jButtonLogin);
		jContentPane.add(jButtonRegister);
		
		
		setContentPane(jContentPane);
		setTitle("Ventana principal");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
	}
} // @jve:decl-index=0:visual-constraint="0,0"

