/*
 * UpgradeData.java
 *
 * Created on 11 avril 2006, 10:15
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package ogsconverter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

/**
 * 
 * @author Benoit Moreau
 */
public class UpgradeData extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of UpgradeData */
	public UpgradeData(int c) {
		super();
		JLabel l;
		// GridLayout grid;
		JPanel pan;
		JButton val;

		confignb = c;
		String fichier;

		NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
		formatter.setAllowsInvalid(false);

		try {
			sz_rss = new JFormattedTextField((NumberFormatter) formatter.clone());
			sz_fleet = new JFormattedTextField((NumberFormatter) formatter.clone());
			sz_def = new JFormattedTextField((NumberFormatter) formatter.clone());
			sz_bld = new JFormattedTextField((NumberFormatter) formatter.clone());
			sz_techno = new JFormattedTextField((NumberFormatter) formatter.clone());
		} catch (Exception e) {
			sz_rss = new JTextField();
			sz_fleet = new JTextField();
			sz_def = new JTextField();
			sz_bld = new JTextField();
			sz_techno = new JTextField();
		}

		this.setLayout(new BorderLayout());

		Configuration configC = null;
		Configuration configL = null;

		try {
			configC = new Configuration("config.ini");
			fichier = "lang_" + configC.getConfig("active_language") + ".ini";
			configL = new Configuration(fichier);

			this.add(l = new JLabel("<html><center><font color='#FF0000'><u>"
					+ configL.getConfig("color24") + ":</u></font></center></html>"),
					BorderLayout.NORTH);
			l.setHorizontalAlignment(JLabel.CENTER);

			pan = new JPanel();
			pan.setLayout(new BorderLayout());

			sl_rss = newJTextField("ER_GOODS");
			sl_fleet = newJTextField("ER_FLEET");
			sl_def = newJTextField("ER_DEFENCES");
			sl_bld = newJTextField("ER_BUILDINGS");
			sl_techno = newJTextField("ER_TECHNO");

			final JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
			tabbedPane.addTab(configL.getConfig("ER_RESS"), null, newpart(sz_rss, sl_rss,
					configC.getConfig("user_sz_rss" + c), "ER_GOODS"));
			tabbedPane.addTab(configL.getConfig("ER_FLT"), null, newpart(sz_fleet, sl_fleet,
					configC.getConfig("user_sz_fleet" + c), "ER_FLEET"));
			tabbedPane.addTab(configL.getConfig("ER_DEF"), null, newpart(sz_def, sl_def,
					configC.getConfig("user_sz_def" + c), "ER_DEFENCES"));
			tabbedPane.addTab(configL.getConfig("ER_BLD"), null, newpart(sz_bld, sl_bld,
					configC.getConfig("user_sz_def" + c), "ER_BUILDINGS"));
			tabbedPane.addTab(configL.getConfig("ER_THN"), null, newpart(sz_techno, sl_techno,
					configC.getConfig("user_sz_def" + c), "ER_TECHNO"));

			pan.add(tabbedPane);

			this.add(pan, BorderLayout.CENTER);
			this.add(val = new JButton(configL.getConfig("validate_button")),
					BorderLayout.SOUTH);
			val.addActionListener(this);
			val.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));

		} catch (final Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public JPanel newpart(final JTextField size, final JTextField[] seuil, final String sz,
			final String partie) {
		final JPanel pan = new JPanel();
		JLabel l;
		// TitledBorder b;
		String s_sz = "", s_sl = "", name[] = null;
		int s = 0, i;
		Configuration configC = null;
		Configuration configL = null;

		try {
			configC = new Configuration("config.ini");
			String fichier = "lang_" + configC.getConfig("active_language") + ".ini";
			configL = new Configuration(fichier);
			s_sz = configL.getConfig("size");
			s_sl = configL.getConfig("threshold");
			name = configL.getConfig(partie).split("\\|");
			s = seuil.length;
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		pan.setLayout(new GridLayout(s + 1, 2));
		l = new JLabel(s_sz);
		l.setMinimumSize(new Dimension(10, 10));
		pan.add(l);
		size.setText(sz);
		pan.add(size);
		size.addActionListener(this);

		for (i = 0; i < s; i++) {
			l = new JLabel(name[i] + " => " + s_sl);
			l.setMinimumSize(new Dimension(10, 10));
			pan.add(l);
			pan.add(seuil[i]);
		}

		return pan;
	}

	public JTextField[] newJTextField(final String partie) {
		JTextField[] seuil;

		String sl[] = null;
		int s = 0, i;
		Configuration configC = null;
		Configuration configL = null;

		NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
		formatter.setAllowsInvalid(false);

		try {
			configC = new Configuration("config.ini");
			String fichier = "lang_" + configC.getConfig("active_language") + ".ini";
			configL = new Configuration(fichier);
			sl = configC.getConfig(partie + confignb).split("\\|");
			s = configL.getConfig(partie).split("\\|").length;
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		seuil = new JTextField[s];

		for (i = 0; i < s; i++) {
			try {
				seuil[i] = new JFormattedTextField((NumberFormatter) formatter.clone());
			} catch (CloneNotSupportedException e) {
				seuil[i] = new JTextField();
			}
			if (i < sl.length)
				seuil[i].setText(sl[i]);
			else
				seuil[i].setText(sl[sl.length - 1]);
			seuil[i].addActionListener(this);
		}

		return seuil;
	}

	public void actionPerformed(final ActionEvent e) {
		int ii;
		String tmp = "";
		Configuration configC = null;

		try {
			configC = new Configuration("config.ini");
			if (isNumeric(sz_rss.getText()))
				configC.setConfig("user_sz_rss" + confignb, sz_rss.getText());
			configC.setConfig("user_sz_fleet" + confignb, sz_fleet.getText());
			configC.setConfig("user_sz_def" + confignb, sz_def.getText());
			configC.setConfig("user_sz_bld" + confignb, sz_bld.getText());
			configC.setConfig("user_sz_techno" + confignb, sz_techno.getText());

			for (ii = 0; ii < sl_rss.length; ii++) {
				if (isNumeric(sl_rss[ii].getText()))
					tmp += sl_rss[ii].getText() + "|";
				else
					tmp += (configC.getConfig("ER_GOODS" + confignb).split("\\|"))[ii] + "|";
			}
			configC.setConfig("ER_GOODS" + confignb, tmp.substring(0, tmp.length() - 1));
			tmp = "";

			for (ii = 0; ii < sl_fleet.length; ii++) {
				if (isNumeric(sl_fleet[ii].getText()))
					tmp += sl_fleet[ii].getText() + "|";
				else
					tmp += (configC.getConfig("ER_FLEET" + confignb).split("\\|"))[ii] + "|";
			}
			configC.setConfig("ER_FLEET" + confignb, tmp.substring(0, tmp.length() - 1));
			tmp = "";

			for (ii = 0; ii < sl_def.length; ii++) {
				if (isNumeric(sl_def[ii].getText()))
					tmp += sl_def[ii].getText() + "|";
				else
					tmp += (configC.getConfig("ER_DEFENCES" + confignb).split("\\|"))[ii]
							+ "|";
			}
			configC.setConfig("ER_DEFENCES" + confignb, tmp.substring(0, tmp.length() - 1));
			tmp = "";

			for (ii = 0; ii < sl_bld.length; ii++) {
				if (isNumeric(sl_bld[ii].getText()))
					tmp += sl_bld[ii].getText() + "|";
				else
					tmp += (configC.getConfig("ER_BUILDINGS" + confignb).split("\\|"))[ii]
							+ "|";
			}
			configC.setConfig("ER_BUILDINGS" + confignb, tmp.substring(0, tmp.length() - 1));
			tmp = "";

			for (ii = 0; ii < sl_techno.length; ii++) {
				if (isNumeric(sl_techno[ii].getText()))
					tmp += sl_techno[ii].getText() + "|";
				else
					tmp += (configC.getConfig("ER_TECHNO" + confignb).split("\\|"))[ii] + "|";
			}
			configC.setConfig("ER_TECHNO" + confignb, tmp.substring(0, tmp.length() - 1));
			tmp = "";

		} catch (final Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}
	}

	public boolean isNumeric(final String num) {
		int i;
		for (i = 0; i < num.length(); i++) {
			if (num.substring(i, i + 1).compareTo("0") > 9)
				return false;
		}
		return true;
	}

	private JTextField sz_rss;

	private JTextField sl_rss[];

	private JTextField sz_fleet;

	private JTextField sl_fleet[];

	private JTextField sz_def;

	private JTextField sl_def[];

	private JTextField sz_bld;

	private JTextField sl_bld[];

	private JTextField sz_techno;

	private JTextField sl_techno[];

	private int confignb;

}
