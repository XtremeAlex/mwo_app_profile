/**
 * 
 */
/**
 * @author XtremeAlex
 *
 */
package com.xa.mwo_ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.xa.mwo.user.service.Utente;

public class LoginDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField tfEmail;
	private JPasswordField pfPassword;
	private JLabel lbEmail;
	private JLabel lbPassword;
	private JButton btnLogin;
	private JButton btnCancel;
	private boolean succeeded;

	public Utente utente = new Utente();

	public LoginDialog(Frame parent) {
		super(parent, "Login", true);
		//

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		lbEmail = new JLabel("Email: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbEmail, cs);

		tfEmail = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(tfEmail, cs);

		lbPassword = new JLabel("Password: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(lbPassword, cs);

		pfPassword = new JPasswordField(20);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		panel.add(pfPassword, cs);
		panel.setBorder(new LineBorder(Color.GRAY));

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				utente.setEmail(getUsername());
				utente.setPassword(getPassword());
				if (Login.authenticate(utente)) {
					JOptionPane.showMessageDialog(LoginDialog.this,
							"Hi " + getUsername() + "! You have successfully logged in.", "Login",
							JOptionPane.INFORMATION_MESSAGE);
					succeeded = true;
					dispose();
				} else {
					JOptionPane.showMessageDialog(LoginDialog.this, "Invalid username or password", "Login",
							JOptionPane.ERROR_MESSAGE);
					// reset username and password
					tfEmail.setText("");
					pfPassword.setText("");
					succeeded = false;

				}
			}
		});
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel bp = new JPanel();
		bp.add(btnLogin);
		bp.add(btnCancel);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);
		setResizable(false);
		setLocationRelativeTo(parent);
		pack();
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public String getUsername() {
		return tfEmail.getText().trim();
	}

	public String getPassword() {
		return new String(pfPassword.getPassword());
	}

	public boolean isSucceeded() {
		return succeeded;
	}
}