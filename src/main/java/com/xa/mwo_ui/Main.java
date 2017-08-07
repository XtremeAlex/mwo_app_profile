/**
 * 
 */
/**
 * @author XtremeAlex
 *
 */
package com.xa.mwo_ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import com.google.gson.Gson;
import com.xa.mwo.user.service.AnalyzeSource;

public class Main {

	public static void main(String[] args) {

		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		final JFrame frame = new JFrame("JDialog - XtremeAlex");
		final JButton btnLogin = new JButton("Click to login");

		btnLogin.setPreferredSize(new Dimension(275, 50));
		btnLogin.setBackground(Color.green);
		btnLogin.setOpaque(true);

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginDialog loginDlg = new LoginDialog(frame);
				loginDlg.setVisible(true);
				loginDlg.setResizable(false);

				// if logon successfully
				if (loginDlg.isSucceeded()) {
					btnLogin.setEnabled(false);

					CountDown countDown = new CountDown();

					JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					jfc.setDialogTitle(" JFileChooser - XtremeAlex");
					jfc.updateUI();

					new Thread() {
						public void run() {

							AnalyzeSource avviaProcesso = new AnalyzeSource();
							try {
								avviaProcesso.findAllInfo(loginDlg.getUtente());
								btnLogin.setEnabled(true);
							} catch (Exception e2) {
								e2.printStackTrace();
							}

							int returnValue = jfc.showDialog(null, "Salva Json!");
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								System.out.println(jfc.getSelectedFile().getPath());

								try (FileWriter file = new FileWriter(jfc.getSelectedFile().getPath() + ".json")) {

									Gson gson = new Gson();
									file.write(gson.toJson(loginDlg.getUtente().getData()));
									System.out.println("Successfully Copied JSON Object to File... \n"
											+ jfc.getSelectedFile().getPath() + ".json");

								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						}
					}.start();

				}
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 100);
		frame.setLayout(new FlowLayout());
		frame.getContentPane().add(btnLogin);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}

}