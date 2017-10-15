package dBase;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPasswordField;

public class Login extends Frame {

	private static final long serialVersionUID = 1L;
	final  String log_in="admin";
	final String Pass="1234";
	private Button b;
	private Label order, log, pass;
	private String getlog = new String();
	static Color c = new Color(212, 208, 200);
	final JPasswordField loginTF, passTF;
	public String getUser(){
		if (getlog.equals("admin")){
			return "Administrator";		
	}
	return getlog;
	}
	Login() {
		setLayout(null);
		b = new Button("Submit");
		order = new Label("In order to connect to a database please enter your login and password");
		log = new Label("Login: ");
		pass = new Label("Password: ");
		loginTF = new JPasswordField();
		passTF = new JPasswordField();
		b.setBounds(200, 220, 100, 20);
		order.setBounds(50, 50, 400, 20);
		log.setBounds(155, 100, 40, 20);
		pass.setBounds(130, 150, 60, 20);
		loginTF.setText("");
		loginTF.setBounds(200, 100, 100, 20);
		loginTF.setEchoChar((char) 0);
		passTF.setText("");
		passTF.setBounds(200, 150, 100, 20);
		setLocation(400, 350);
		setSize(500, 300);
		setTitle("Login Screen");
		setBackground(c);
		setVisible(true);
		add(b);
		add(order);
		add(loginTF);
		add(log);
		add(pass);
		add(passTF);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String getpass = new String(passTF.getPassword());
				getlog = new String(loginTF.getPassword());
				if (getlog.equals(log_in) && getpass.equals(Pass)) {
					dispose();
					Main.mainframe.setVisible(true);
					Main.mainframe.setWelcome("Welcome: "+getUser()+". Connected to Database: MYDB");
				} else {
					Frame incorrect = new Frame();
					incorrect.setLayout(null);
					incorrect.setBackground(c);
					Button close = new Button("Ok");
					close.setBounds(75, 100, 50, 30);
					close.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							incorrect.dispose();
						}
					});
					Label inc = new Label("Incorrect login or password.");
					Label again =new Label("Please try again");
					inc.setBounds(25,40,150,20);
					again.setBounds(50,60,100,20);
					incorrect.setSize(200, 150);
					incorrect.setLocation(550, 400);
					incorrect.setVisible(true);
					incorrect.add(inc);
					incorrect.add(again);
					incorrect.add(close);					
					incorrect.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							incorrect.dispose();
						}
					});
					loginTF.setText("");
					passTF.setText("");
				}
			}
		});
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		   });
	}
}