package net.miginfocom.examples;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikael Grev, MiG InfoCom AB
 *         Date: Dec 15, 2008
 *         Time: 7:04:50 PM
 */
public class BugTestApp
{
	private static JPanel createPanel()
	{
		JPanel c = new JPanel();
		c.setBackground(new Color(200, 255, 200));
		c.setLayout(new MigLayout("debug"));

		JLabel lab = new JLabel("Qwerty");
		lab.setFont(lab.getFont().deriveFont(30f));
		c.add(lab, "pos 0%+5 0%+5 50%-5  50%-5");
		c.add(new JTextField("Qwerty"));

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new MigLayout());
		f.add(c, "w 400, h 100");
		f.setLocationRelativeTo(null);
		f.pack();
		f.setVisible(true);
		return null;
	}

	private static JPanel createPanel2()
	{
		JFrame tst = new JFrame();
		tst.setLayout(new MigLayout("debug, fillx","",""));

		tst.add(new JTextField(),"span 2, grow, wrap");
		tst.add(new JTextField(10));
		tst.add(new JLabel("End"));

		tst.setSize(600, 400);
		tst.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tst.setVisible(true);
		return null;
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				createPanel();
				
//				JFrame frame = new JFrame("Bug Test App");
//				frame.getContentPane().add(createPanel2());
//				frame.pack();
//				frame.setLocationRelativeTo(null);
//				frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//				frame.setVisible(true);
			}
		});
	}
}
