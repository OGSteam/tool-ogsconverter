package fr.ogsteam.ogsconverter;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

public class ConfigColorPane extends JTabbedPane implements ActionListener, MouseListener,
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of Setcolor */
	public ConfigColorPane(int c, String name) {

		int colornb = 0, i;
		String button_name;
		JButton b;
		JPanel panel = new JPanel();
		JPanel panelbottom = new JPanel();
		GridLayout grid;

		user_color = c;

		Configuration config;
		Configuration configL;

		MaskFormatter formatter = null;
		try {
			formatter = new MaskFormatter("HHHHHH");
			formatter.setAllowsInvalid(false);
			formatter.setPlaceholderCharacter('0');
		} catch (ParseException e1) {
		}

		try {
			config = new Configuration("config.ini");

			clrs = config.getConfig("user_color" + c).split(",");
			colornb = clrs.length;
			lang = config.getConfig("active_language");

			configL = new Configuration("lang_" + lang + ".ini");

			String[] fleet = configL.getConfig("FLEET").split("\\|");
			String[] def = configL.getConfig("DEFENCES").split("\\|");

			panel.setLayout(new BorderLayout());
			paneltop = new JPanel();
			grid = new GridLayout(0, 4, 1, 1);
			paneltop.setLayout(grid);
			l = new JLabel[colornb - 1];
			lc = new JTextField[colornb];

			for (i = 0; i < (colornb - 1); i++) {
				if (i > 1 && fleet.length > i - 2)
					button_name = fleet[i - 2];
				else if (i > 1 && def.length > i - fleet.length - 2)
					button_name = def[i - fleet.length - 2];
				else
					button_name = configL.getConfig("color" + (i > 1 ? i - 1 : i));
				if (def.length > i - fleet.length - 2)
					l[i] = new JLabel("<html><font color='" + clrs[i] + "'>" + button_name
							+ "</font></html>");
				else if (i == def.length + fleet.length + 2)
					l[i] = new JLabel("<html><font color='" + clrs[i]
							+ "'>12.345</font></html>");
				else
					l[i] = new JLabel("<html><font color='" + clrs[i]
							+ "'> 12.345 </font></html>");
				l[i].setHorizontalAlignment(JLabel.CENTER);
				l[i].setBorder(BorderFactory.createLineBorder(new Color(Main.getcolor(clrs[i]))));
				l[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				l[i].addMouseListener(this);
				paneltop.add(l[i]);
				try {
					lc[i] = new JFormattedTextField((MaskFormatter) formatter.clone());
				} catch (Exception e) {
					lc[i] = new JTextField();
				}
				lc[i].setText(clrs[i]);
				lc[i].addActionListener(this);
				lc[i].addKeyListener(this);
				lc[i].setForeground(new Color(0, 100, 0));
				paneltop.add(lc[i]);
			}

			paneltop.setBackground(new Color(Main.getcolor(clrs[colornb - 1])));

		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		panelbottom.setLayout(new GridLayout(1, 0));
		panelbottom.add(b = new JButton("Background"));
		b.addActionListener(this);
		try {
			lc[colornb - 1] = new JFormattedTextField((MaskFormatter) formatter.clone());
		} catch (Exception e) {
			lc[colornb - 1] = new JTextField();
		}
		lc[colornb - 1].setText(clrs[colornb - 1]);
		lc[colornb - 1].addActionListener(this);
		lc[colornb - 1].addKeyListener(this);
		lc[colornb - 1].setForeground(new Color(0, 100, 0));
		panelbottom.add(lc[colornb - 1]);

		panel.add(paneltop, BorderLayout.CENTER);
		panel.add(panelbottom, BorderLayout.SOUTH);

		this.addTab("Colors (" + name + ")", null, panel, "Init Colors" + c);
		this.addTab("Upgrade (" + name + ")", null, new UpgradeData(c), "Init Upgrade Data"
				+ c);

		setSize(prefs.getInt("OGSconverter Setting " + c + "width", 400), prefs.getInt(
				"OGSconverter Setting " + c + "height", 540));
		setLocation(prefs.getInt("OGSconverter Setting " + c + "x_location", 50),
				prefs.getInt("OGSconverter Setting " + c + "y_location", 50));

		setVisible(true);
	}

	public int getConfigID() {
		return user_color;
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {
		int index;
		try {
			for (index = 0; index < (clrs.length - 1); index++) {
				if (((JLabel) e.getSource()).getText().equals(l[index].getText()))
					break;
			}
			Color initcolor = new Color(Main.getcolor(clrs[index]));
			Color newColor = JColorChooser.showDialog(this, "OGSconverter ColorChooser",
					initcolor);
			if (newColor != null) {
				setnewclr(newColor, index);
				l[index].setText(l[index].getText().replaceAll("color='.{6}",
						"color='" + clrs[index]));
				l[index].setBorder(BorderFactory.createLineBorder(new Color(
						Main.getcolor(clrs[index]))));
				lc[index].setText(clrs[index]);
				lc[index].setForeground(new Color(0, 100, 0));
			}
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		int i;
		String c;
		try {
			for (i = 0; i < clrs.length; i++) {
				if (e.getSource().equals(lc[i])) {
					c = lc[i].getText();
					if (Pattern.compile("^[0-9A-F]{6}$").matcher(c).find()) {
						setnewclr(new Color(Main.getcolor(c)), i);
						if (i < l.length) {
							l[i].setText(l[i].getText().replaceAll("color='.{6}",
									"color='" + clrs[i]));
							l[i].setBorder(BorderFactory.createLineBorder(new Color(
									Main.getcolor(clrs[i]))));
						}
						if (i == clrs.length - 1) {
							paneltop.setBackground(new Color(Main.getcolor(clrs[i])));
						}
						lc[i].setForeground(new Color(0, 100, 0));
					} else
						lc[i].setForeground(new Color(255, 0, 0));
					return;
				}
			}

			if ((e.getSource() instanceof JButton)
					&& ((JButton) e.getSource()).getText().equals("Background")) {
				Color initcolor = new Color(Main.getcolor(clrs[clrs.length - 1]));
				Color newColor = JColorChooser.showDialog(this, "OGSconverter ColorChooser",
						initcolor);
				if (newColor != null) {
					setnewclr(newColor, clrs.length - 1);
					paneltop.setBackground(new Color(Main.getcolor(clrs[clrs.length - 1])));
					lc[clrs.length - 1].setText(clrs[clrs.length - 1]);
					lc[clrs.length - 1].setForeground(new Color(0, 100, 0));
				}
			}
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}
	}

	public void setnewclr(Color c, int index) {
		int i, j;
		String newcolor = "";

		int r = c.getRed();
		int v = c.getGreen();
		int b = c.getBlue();

		j = b % 16;
		b = (int) Math.floor(b / 16);
		newcolor = Main.tohex(b) + Main.tohex(j) + newcolor;

		j = v % 16;
		v = (int) Math.floor(v / 16);
		newcolor = Main.tohex(v) + Main.tohex(j) + newcolor;

		j = r % 16;
		r = (int) Math.floor(r / 16);
		newcolor = Main.tohex(r) + Main.tohex(j) + newcolor;

		clrs[index] = newcolor;
		newcolor = "";
		for (i = 0; i < clrs.length; i++) {
			newcolor += clrs[i];
			if (i != clrs.length - 1)
				newcolor += ",";
		}

		try {
			Configuration.setConfig("config.ini", "user_color" + user_color, newcolor);
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}

	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		((JTextField) e.getSource()).postActionEvent();
	}

	public void keyTyped(KeyEvent e) {

	}

	JPanel paneltop;
	JLabel[] l;
	JTextField[] lc;

	String clrs[];
	String lang;
	int user_color = 1;

	Preferences prefs = Preferences.userNodeForPackage(Fenetre.class);
}
