/*
 * BBManage.java
 *
 * Created on 9 mai 2006, 10:52
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package ogsconverter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import ogsconverter.widgets.JLimiterTextField;

/**
 * 
 * @author MOREAU Benoît
 */
public class BBManage extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BBManage() {
		super();

		TitledBorder b;
		String tmp[];
		int i;

		JPanel Genepan = new JPanel();

		JPanel adder = new JPanel();
		adder.setLayout(new BorderLayout());

		JPanel deleter = new JPanel();
		deleter.setLayout(new BorderLayout());

		JPanel mainpan = new JPanel();
		mainpan.setLayout(new BorderLayout());

		JPanel BBname = new JPanel();
		BBname.setLayout(new BorderLayout());

		setDefaultCloseOperation(0);
		setIconImage(new ImageIcon(this.getClass().getResource("images/icone.png")).getImage());

		try {
			Configuration configC = new Configuration("config.ini");
			lang = configC.getConfig("active_language");

			Configuration configL = new Configuration("lang_" + lang + ".ini");

			tmp = ((configC.getConfig("BBCode_center").split("<<\\|>>"))[0]).split("<\\|>");
			center = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "center"));
			if (i <= (center.getItemCount() - 1))
				center.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_size").split("<<\\|>>"))[0]).split("<\\|>");
			size = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "size"));
			if (i <= (size.getItemCount() - 1))
				size.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_bold").split("<<\\|>>"))[0]).split("<\\|>");
			bold = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "bold"));
			if (i <= (bold.getItemCount() - 1))
				bold.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_ita").split("<<\\|>>"))[0]).split("<\\|>");
			ita = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "ita"));
			if (i <= (ita.getItemCount() - 1))
				ita.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_under").split("<<\\|>>"))[0]).split("<\\|>");
			under = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "under"));
			if (i <= (under.getItemCount() - 1))
				under.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_bcolor").split("<<\\|>>"))[0]).split("<\\|>");
			bcolor = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "bcolor"));
			if (i <= (bcolor.getItemCount() - 1))
				bcolor.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_code").split("<<\\|>>"))[0]).split("<\\|>");
			code = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "code"));
			if (i <= (code.getItemCount() - 1))
				code.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_quote").split("<<\\|>>"))[0]).split("<\\|>");
			quote = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "quote"));
			if (i <= (quote.getItemCount() - 1))
				quote.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_font").split("<<\\|>>"))[0]).split("<\\|>");
			font = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "font"));
			if (i <= (font.getItemCount() - 1))
				font.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_img").split("<<\\|>>"))[0]).split("<\\|>");
			img = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "img"));
			if (i <= (img.getItemCount() - 1))
				img.setSelectedIndex(i);
			tmp = ((configC.getConfig("BBCode_url").split("<<\\|>>"))[0]).split("<\\|>");
			url = new JComboBox(tmp);
			i = Integer.parseInt(Configuration.getConfig("config.ini", "url"));
			if (i <= (url.getItemCount() - 1))
				url.setSelectedIndex(i);

			Genepan.setLayout(new GridLayout(0, 3));
			Genepan.add(center);
			Genepan.add(size);
			Genepan.add(bold);
			Genepan.add(ita);
			Genepan.add(under);
			Genepan.add(bcolor);
			Genepan.add(code);
			Genepan.add(quote);
			Genepan.add(font);
			Genepan.add(img);
			Genepan.add(url);

			MBBcode = new JComboBox();
			tmp = configC.getConfig("BBname").split("\\|");
			for (i = 1; i < tmp.length; i++)
				MBBcode.addItem(tmp[i]);

			BBname.add(new JLabel(configL.getConfig("l_name") + ": "),
					BorderLayout.BEFORE_LINE_BEGINS);
			BBname.add(name = new JLimiterTextField(), BorderLayout.CENTER);
			((JLimiterTextField) name).setDisallowed(",|");

			b = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0)),
					"ADD");
			b.setTitleFont(new Font("Default", Font.ITALIC, 12));
			adder.setBorder(b);

			b = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0)),
					"DELETE");
			b.setTitleFont(new Font("Default", Font.ITALIC, 12));
			deleter.setBorder(b);

			deleter.add(MBBcode, BorderLayout.CENTER);
			deleter.add(del = new JButton("delete"), BorderLayout.AFTER_LINE_ENDS);
			del.addActionListener(this);
			del.setIcon(new ImageIcon(this.getClass().getResource("images/delete.png")));

			adder.add(BBname, BorderLayout.NORTH);
			adder.add(Genepan, BorderLayout.CENTER);
			adder.add(add = new JButton("add"), BorderLayout.SOUTH);
			add.addActionListener(this);
			add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));

			mainpan.add(deleter, BorderLayout.NORTH);
			mainpan.add(adder, BorderLayout.CENTER);

			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Config forum", null, mainpan, "Config forum");
			tabbedPane.addTab("Add BBCodes", null, new BBCodeAdder(this), "Add BBCodes");

			getContentPane().add(tabbedPane);

			setTitle("OGSConverter - BBManager");

			if (Main.JWS) {
				setSize(400, 280);
				this.setLocationRelativeTo(getParent());
			} else {
				prefs = Preferences.userNodeForPackage(Fenetre.class);
				setSize(prefs.getInt("OGSConverter - BBManager" + "width", 400), prefs.getInt(
						"OGSConverter - BBManager" + "height", 280));
				setLocation(prefs.getInt("OGSConverter - BBManager" + "x_location", 50),
						prefs.getInt("OGSConverter - BBManager" + "y_location", 50));
			}

			setVisible(true);

		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public BBManage(Configs configFrame) {
		this();
		this.configFrame = configFrame;
	}

	public void update() {
		String tmp[];
		int i;

		configFrame.init_text();

		try {
			Configuration configC = new Configuration("config.ini");

			center.removeAllItems();
			tmp = ((configC.getConfig("BBCode_center").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				center.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("center"));
			if (i <= (center.getItemCount() - 1))
				center.setSelectedIndex(i);

			size.removeAllItems();
			tmp = ((configC.getConfig("BBCode_size").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				size.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("size"));
			if (i <= (size.getItemCount() - 1))
				size.setSelectedIndex(i);

			bold.removeAllItems();
			tmp = ((configC.getConfig("BBCode_bold").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				bold.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("bold"));
			if (i <= (bold.getItemCount() - 1))
				bold.setSelectedIndex(i);

			ita.removeAllItems();
			tmp = ((configC.getConfig("BBCode_ita").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				ita.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("ita"));
			if (i <= (ita.getItemCount() - 1))
				ita.setSelectedIndex(i);

			under.removeAllItems();
			tmp = ((configC.getConfig("BBCode_under").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				under.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("under"));
			if (i <= (under.getItemCount() - 1))
				under.setSelectedIndex(i);

			bcolor.removeAllItems();
			tmp = ((configC.getConfig("BBCode_bcolor").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				bcolor.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("bcolor"));
			if (i <= (bcolor.getItemCount() - 1))
				bcolor.setSelectedIndex(i);

			code.removeAllItems();
			tmp = ((configC.getConfig("BBCode_code").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				code.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("code"));
			if (i <= (code.getItemCount() - 1))
				code.setSelectedIndex(i);

			quote.removeAllItems();
			tmp = ((configC.getConfig("BBCode_quote").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				quote.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("quote"));
			if (i <= (quote.getItemCount() - 1))
				quote.setSelectedIndex(i);

			font.removeAllItems();
			tmp = ((configC.getConfig("BBCode_font").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				font.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("font"));
			if (i <= (font.getItemCount() - 1))
				font.setSelectedIndex(i);

			img.removeAllItems();
			tmp = ((configC.getConfig("BBCode_img").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				img.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("img"));
			if (i <= (img.getItemCount() - 1))
				img.setSelectedIndex(i);

			url.removeAllItems();
			tmp = ((configC.getConfig("BBCode_url").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				url.addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("url"));
			if (i <= (url.getItemCount() - 1))
				url.setSelectedIndex(i);

			MBBcode.removeAllItems();
			tmp = configC.getConfig("BBname").split("\\|");
			for (i = 1; i < tmp.length; i++)
				MBBcode.addItem(tmp[i]);

		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		String tmp[], tmp2[], tmp3 = "", tmp4 = "";
		int i;

		try {
			if (e.getSource().equals(del) && MBBcode.getItemCount() > 0) {
				tmp = Configuration.getConfig("config.ini", "BBname").split("\\|");
				tmp2 = Configuration.getConfig("config.ini", "BBcodes").split("\\|");
				for (i = 0; i < tmp.length; i++) {
					if ((i - 1) != MBBcode.getSelectedIndex()) {
						tmp3 += "|" + tmp[i];
						tmp4 += "|" + tmp2[i];
					}
				}
				Configuration.setConfig("config.ini", "BBname", tmp3.substring(1));
				Configuration.setConfig("config.ini", "BBcodes", tmp4.substring(1));
				MBBcode.removeItemAt(MBBcode.getSelectedIndex());
				configFrame.init_text();
				Main.fen.setFastConfig();
			} else if (e.getSource().equals(add) && !name.getText().trim().equals("")) {
				tmp3 += center.getSelectedIndex();
				tmp3 += "," + size.getSelectedIndex();
				tmp3 += "," + bold.getSelectedIndex();
				tmp3 += "," + ita.getSelectedIndex();
				tmp3 += "," + under.getSelectedIndex();
				tmp3 += "," + bcolor.getSelectedIndex();
				tmp3 += "," + code.getSelectedIndex();
				tmp3 += "," + quote.getSelectedIndex();
				tmp3 += "," + font.getSelectedIndex();
				tmp3 += "," + img.getSelectedIndex();
				tmp3 += "," + url.getSelectedIndex();
				Configuration.setConfig("config.ini", "BBcodes", Configuration.getConfig(
						"config.ini", "BBcodes")
						+ "|" + tmp3);
				Configuration.setConfig("config.ini", "BBname", Configuration.getConfig(
						"config.ini", "BBname")
						+ "|" + name.getText().trim());
				MBBcode.addItem(name.getText().trim());
				configFrame.init_text();
				Main.fen.setFastConfig();
			}
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}
	}

	String lang;

	public JButton add;
	public JButton del;
	private Configs configFrame;

	public JTextField name;

	public JComboBox center;
	public JComboBox size;
	public JComboBox bold;
	public JComboBox ita;
	public JComboBox under;
	public JComboBox bcolor;
	public JComboBox code;
	public JComboBox quote;
	public JComboBox font;
	public JComboBox img;
	public JComboBox url;
	public JComboBox MBBcode;

	public Preferences prefs;
}
