/**
 * 
 */
/**
 * @author XtremeAlex
 *
 */
package com.xa.mwo_ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.tools.ant.taskdefs.Exit;

public class CountDown {

	private JFrame frame;

	public CountDown() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}

				frame = new JFrame("I'm processing...");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new TestPane());
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setResizable(false);
				frame.pack();
			}
		});
	}

	public class TestPane extends JPanel {

		private Timer timer;
		private long startTime = -1;
		private long duration = 20000;

		private JLabel label;

		public TestPane() {
			setLayout(new GridBagLayout());
			timer = new Timer(10, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (startTime < 0) {
						startTime = System.currentTimeMillis();
					}
					long now = System.currentTimeMillis();
					long clockTime = now - startTime;
					if (clockTime >= duration) {
						clockTime = duration;
						timer.stop();
						frame.dispose();
					}
					SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
					label.setText(df.format(duration - clockTime));
					label.setFont(new Font("Serif", Font.PLAIN, 50));

				}
			});
			timer.setInitialDelay(0);
			startTime = -1;
			timer.start();

			label = new JLabel("...");
			label.setForeground(Color.red);
			add(label);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(255, 50);
		}

	}

}