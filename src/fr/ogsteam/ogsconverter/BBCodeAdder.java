/*
 * BBCodeAdder.java
 *
 * Created on 3 juin 2006, 16:34
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package fr.ogsteam.ogsconverter;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fr.ogsteam.ogsconverter.widgets.JLimiterTextField;

/**
 * 
 * @author MOREAU Benoît
 */
public class BBCodeAdder extends JScrollPane implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of BBCodeAdder */
	public BBCodeAdder(BBManage bbm) {
		super();

		JPanel panel = new JPanel();
		this.remove(this.getHorizontalScrollBar());
		this.setViewportView(panel);

		bbmanage = bbm;

		String tmp[];
		int i;

		JPanel adder = new JPanel();
		adder.setLayout(new GridLayout(12, 1));

		JPanel deleter = new JPanel();
		deleter.setLayout(new BorderLayout());

		try {
			Configuration config = new Configuration("config.ini");
			lang = config.getConfig("active_language");
			Configuration configL = new Configuration("lang_" + lang + ".ini");

			ch_delete = new JComboBox();

			tmp = ((config.getConfig("BBCode_center").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_size").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_bold").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_ita").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_under").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_bcolor").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_code").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_quote").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_font").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_img").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);
			tmp = ((config.getConfig("BBCode_url").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 1; i < tmp.length; i++)
				ch_delete.addItem(tmp[i]);

			delete = new JButton("delete");
			delete.addActionListener(this);
			deleter.add(ch_delete, BorderLayout.CENTER);
			deleter.add(delete, BorderLayout.EAST);
			delete.setIcon(new ImageIcon(this.getClass().getResource("images/delete.png")));

			JPanel tmppanel = new JPanel();
			tmppanel.setLayout(new GridLayout(1, 5));
			tmppanel.add(new JLabel(""));
			tmppanel.add(new JLabel(configL.getConfig("l_name")));
			tmppanel.add(new JLabel(configL.getConfig("l_c_enter")));
			tmppanel.add(new JLabel(configL.getConfig("l_c_exit")));
			tmppanel.add(new JLabel(""));
			adder.add(tmppanel);

			center_name = new JLimiterTextField("", ",|");
			center_enter = new JLimiterTextField("", "(<|>)");
			center_output = new JLimiterTextField("", "(<|>)");
			center_add = new JButton("add");
			center_add.addActionListener(this);
			center_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			JPanel center = new JPanel();
			center.setLayout(new GridLayout(1, 5));
			center.add(new JLabel("center:"));
			center.add(center_name);
			center.add(center_enter);
			center.add(center_output);
			center.add(center_add);
			adder.add(center);

			size_name = new JLimiterTextField("", ",|");
			size_enter = new JLimiterTextField("", "(<|>)");
			size_output = new JLimiterTextField("", "(<|>)");
			size_add = new JButton("add");
			size_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			size_add.addActionListener(this);
			JPanel size = new JPanel();
			size.setLayout(new GridLayout(1, 5));
			size.add(new JLabel("size:"));
			size.add(size_name);
			size.add(size_enter);
			size.add(size_output);
			size.add(size_add);
			adder.add(size);

			bold_name = new JLimiterTextField("", ",|");
			bold_enter = new JLimiterTextField("", "(<|>)");
			bold_output = new JLimiterTextField("", "(<|>)");
			bold_add = new JButton("add");
			bold_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			bold_add.addActionListener(this);
			JPanel bold = new JPanel();
			bold.setLayout(new GridLayout(1, 5));
			bold.add(new JLabel("bold:"));
			bold.add(bold_name);
			bold.add(bold_enter);
			bold.add(bold_output);
			bold.add(bold_add);
			adder.add(bold);

			ita_name = new JLimiterTextField("", ",|");
			ita_enter = new JLimiterTextField("", "(<|>)");
			ita_output = new JLimiterTextField("", "(<|>)");
			ita_add = new JButton("add");
			ita_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			ita_add.addActionListener(this);
			JPanel ita = new JPanel();
			ita.setLayout(new GridLayout(1, 5));
			ita.add(new JLabel("italic:"));
			ita.add(ita_name);
			ita.add(ita_enter);
			ita.add(ita_output);
			ita.add(ita_add);
			adder.add(ita);

			under_name = new JLimiterTextField("", ",|");
			under_enter = new JLimiterTextField("", "(<|>)");
			under_output = new JLimiterTextField("", "(<|>)");
			under_add = new JButton("add");
			under_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			under_add.addActionListener(this);
			JPanel under = new JPanel();
			under.setLayout(new GridLayout(1, 5));
			under.add(new JLabel("underline:"));
			under.add(under_name);
			under.add(under_enter);
			under.add(under_output);
			under.add(under_add);
			adder.add(under);

			bcolor_name = new JLimiterTextField("", ",|");
			bcolor_enter = new JLimiterTextField("", "(<|>)");
			bcolor_output = new JLimiterTextField("", "(<|>)");
			bcolor_add = new JButton("add");
			bcolor_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			bcolor_add.addActionListener(this);
			JPanel bcolor = new JPanel();
			bcolor.setLayout(new GridLayout(1, 5));
			bcolor.add(new JLabel("color:"));
			bcolor.add(bcolor_name);
			bcolor.add(bcolor_enter);
			bcolor.add(bcolor_output);
			bcolor.add(bcolor_add);
			adder.add(bcolor);

			code_name = new JLimiterTextField("", ",|");
			code_enter = new JLimiterTextField("", "(<|>)");
			code_output = new JLimiterTextField("", "(<|>)");
			code_add = new JButton("add");
			code_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			code_add.addActionListener(this);
			JPanel code = new JPanel();
			code.setLayout(new GridLayout(1, 5));
			code.add(new JLabel("code:"));
			code.add(code_name);
			code.add(code_enter);
			code.add(code_output);
			code.add(code_add);
			adder.add(code);

			quote_name = new JLimiterTextField("", ",|");
			quote_enter = new JLimiterTextField("", "(<|>)");
			quote_output = new JLimiterTextField("", "(<|>)");
			quote_add = new JButton("add");
			quote_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			quote_add.addActionListener(this);
			JPanel quote = new JPanel();
			quote.setLayout(new GridLayout(1, 5));
			quote.add(new JLabel("quote:"));
			quote.add(quote_name);
			quote.add(quote_enter);
			quote.add(quote_output);
			quote.add(quote_add);
			adder.add(quote);

			font_name = new JLimiterTextField("", ",|");
			font_enter = new JLimiterTextField("", "(<|>)");
			font_output = new JLimiterTextField("", "(<|>)");
			font_add = new JButton("add");
			font_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			font_add.addActionListener(this);
			JPanel font = new JPanel();
			font.setLayout(new GridLayout(1, 5));
			font.add(new JLabel("font:"));
			font.add(font_name);
			font.add(font_enter);
			font.add(font_output);
			font.add(font_add);
			adder.add(font);

			img_name = new JLimiterTextField("", ",|");
			img_enter = new JLimiterTextField("", "(<|>)");
			img_output = new JLimiterTextField("", "(<|>)");
			img_add = new JButton("add");
			img_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			img_add.addActionListener(this);
			JPanel img = new JPanel();
			img.setLayout(new GridLayout(1, 5));
			img.add(new JLabel("img:"));
			img.add(img_name);
			img.add(img_enter);
			img.add(img_output);
			img.add(img_add);
			adder.add(img);

			url_name = new JLimiterTextField("", ",|");
			url_enter = new JLimiterTextField("", "(<|>)");
			url_output = new JLimiterTextField("", "(<|>)");
			url_add = new JButton("add");
			url_add.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
			url_add.addActionListener(this);
			JPanel url = new JPanel();
			url.setLayout(new GridLayout(1, 5));
			url.add(new JLabel("url:"));
			url.add(url_name);
			url.add(url_enter);
			url.add(url_output);
			url.add(url_add);
			adder.add(url);
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		panel.setLayout(new BorderLayout());
		panel.add(adder, BorderLayout.CENTER);
		panel.add(deleter, BorderLayout.NORTH);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			Configuration config = new Configuration("config.ini");

			String[] center = config.getConfig("BBCode_center").split("<<\\|>>");
			String[] size = config.getConfig("BBCode_size").split("<<\\|>>");
			String[] bold = config.getConfig("BBCode_bold").split("<<\\|>>");
			String[] ita = config.getConfig("BBCode_ita").split("<<\\|>>");
			String[] under = config.getConfig("BBCode_under").split("<<\\|>>");
			String[] bcolor = config.getConfig("BBCode_bcolor").split("<<\\|>>");
			String[] code = config.getConfig("BBCode_code").split("<<\\|>>");
			String[] quote = config.getConfig("BBCode_quote").split("<<\\|>>");
			String[] font = config.getConfig("BBCode_font").split("<<\\|>>");
			String[] img = config.getConfig("BBCode_img").split("<<\\|>>");
			String[] url = config.getConfig("BBCode_url").split("<<\\|>>");

			if (center.length != 3)
				center = new String[] { center[0], "", "" };
			if (size.length != 3)
				size = new String[] { size[0], "", "" };
			if (bold.length != 3)
				bold = new String[] { bold[0], "", "" };
			if (ita.length != 3)
				ita = new String[] { ita[0], "", "" };
			if (under.length != 3)
				under = new String[] { under[0], "", "" };
			if (bcolor.length != 3)
				bcolor = new String[] { bcolor[0], "", "" };
			if (code.length != 3)
				code = new String[] { code[0], "", "" };
			if (quote.length != 3)
				quote = new String[] { quote[0], "", "" };
			if (font.length != 3)
				font = new String[] { font[0], "", "" };
			if (img.length != 3)
				img = new String[] { img[0], "", "" };
			if (url.length != 3)
				url = new String[] { url[0], "", "" };

			int icenter = center[0].split("<\\|>").length;
			int isize = size[0].split("<\\|>").length;
			int ibold = bold[0].split("<\\|>").length;
			int iita = ita[0].split("<\\|>").length;
			int iunder = under[0].split("<\\|>").length;
			int ibcolor = bcolor[0].split("<\\|>").length;
			int icode = code[0].split("<\\|>").length;
			int iquote = quote[0].split("<\\|>").length;
			int ifont = font[0].split("<\\|>").length;
			int iimg = img[0].split("<\\|>").length;
			int iurl = url[0].split("<\\|>").length;

			if (e.getSource().equals(delete)) {
				int index = ch_delete.getSelectedIndex();
				if (index < icenter - 1) {
					config.setConfig("BBCode_center", DelBBCode(center, index));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1) {
					config.setConfig("BBCode_size", DelBBCode(size, index - icenter + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1) {
					config.setConfig("BBCode_bold", DelBBCode(bold, index - isize + 1
							- icenter + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1 + iita - 1) {
					config.setConfig("BBCode_ita", DelBBCode(ita, index - isize + 1 - icenter
							+ 1 - ibold + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1 + iita - 1 + iunder - 1) {
					config.setConfig("BBCode_under", DelBBCode(under, index - isize + 1
							- icenter + 1 - ibold + 1 - iita + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1 + iita - 1 + iunder - 1
						+ ibcolor - 1) {
					config.setConfig("BBCode_bcolor", DelBBCode(bcolor, index - isize + 1
							- icenter + 1 - ibold + 1 - iita + 1 - iunder + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1 + iita - 1 + iunder - 1
						+ ibcolor - 1 + icode - 1) {
					config.setConfig("BBCode_code", DelBBCode(code, index - isize + 1
							- icenter + 1 - ibold + 1 - iita + 1 - iunder + 1 - ibcolor + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1 + iita - 1 + iunder - 1
						+ ibcolor - 1 + icode - 1 + iquote - 1) {
					config.setConfig("BBCode_quote", DelBBCode(quote, index - isize + 1
							- icenter + 1 - ibold + 1 - iita + 1 - iunder + 1 - ibcolor + 1
							- icode + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1 + iita - 1 + iunder - 1
						+ ibcolor - 1 + icode - 1 + iquote - 1 + ifont - 1) {
					config.setConfig("BBCode_font", DelBBCode(font, index - isize + 1
							- icenter + 1 - ibold + 1 - iita + 1 - iunder + 1 - ibcolor + 1
							- icode + 1 - iquote + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1 + iita - 1 + iunder - 1
						+ ibcolor - 1 + icode - 1 + iquote - 1 + ifont - 1 + iimg - 1) {
					config.setConfig("BBCode_img", DelBBCode(img, index - isize + 1 - icenter
							+ 1 - ibold + 1 - iita + 1 - iunder + 1 - ibcolor + 1 - icode + 1
							- iquote + 1 - ifont + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				} else if (index < icenter - 1 + isize - 1 + ibold - 1 + iita - 1 + iunder - 1
						+ ibcolor - 1 + icode - 1 + iquote - 1 + ifont - 1 + iimg - 1 + iurl
						- 1) {
					config.setConfig("BBCode_url", DelBBCode(url, index - isize + 1 - icenter
							+ 1 - ibold + 1 - iita + 1 - iunder + 1 - ibcolor + 1 - icode + 1
							- iquote + 1 - ifont + 1 - iimg + 1));
					bbmanage.update();
					ch_delete.removeItemAt(index);
				}
			} else if (e.getSource().equals(center_add)
					&& !center_name.getText().trim().equals("")
					&& !center_enter.getText().trim().equals("")
					&& !center_output.getText().trim().equals("")) {
				config.setConfig("BBCode_center", AddCode(center, center_name.getText(),
						center_enter.getText(), center_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(center_name.getText().trim(), icenter - 1);
			} else if (e.getSource().equals(size_add)
					&& !size_name.getText().trim().equals("")
					&& !size_enter.getText().trim().equals("")
					&& !size_output.getText().trim().equals("")) {
				config.setConfig("BBCode_size", AddCode(size, size_name.getText(),
						size_enter.getText(), size_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(size_name.getText().trim(), icenter - 1 + isize - 1);
			} else if (e.getSource().equals(bold_add)
					&& !bold_name.getText().trim().equals("")
					&& !bold_enter.getText().trim().equals("")
					&& !bold_output.getText().trim().equals("")) {
				config.setConfig("BBCode_bold", AddCode(bold, bold_name.getText(),
						bold_enter.getText(), bold_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(bold_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1);
			} else if (e.getSource().equals(ita_add) && !ita_name.getText().trim().equals("")
					&& !ita_enter.getText().trim().equals("")
					&& !ita_output.getText().trim().equals("")) {
				config.setConfig("BBCode_ita", AddCode(ita, ita_name.getText(),
						ita_enter.getText(), ita_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(ita_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1 + iita - 1);
			} else if (e.getSource().equals(under_add)
					&& !under_name.getText().trim().equals("")
					&& !under_enter.getText().trim().equals("")
					&& !under_output.getText().trim().equals("")) {
				config.setConfig("BBCode_under", AddCode(under, under_name.getText(),
						under_enter.getText(), under_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(under_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1 + iita - 1 + iunder - 1);
			} else if (e.getSource().equals(bcolor_add)
					&& !bcolor_name.getText().trim().equals("")
					&& !bcolor_enter.getText().trim().equals("")
					&& !bcolor_output.getText().trim().equals("")) {
				config.setConfig("BBCode_bcolor", AddCode(bcolor, bcolor_name.getText(),
						bcolor_enter.getText(), bcolor_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(bcolor_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1 + iita - 1 + iunder - 1 + ibcolor - 1);
			} else if (e.getSource().equals(code_add)
					&& !code_name.getText().trim().equals("")
					&& !code_enter.getText().trim().equals("")
					&& !code_output.getText().trim().equals("")) {
				config.setConfig("BBCode_code", AddCode(code, code_name.getText(),
						code_enter.getText(), code_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(code_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1 + iita - 1 + iunder - 1 + ibcolor - 1 + icode - 1);
			} else if (e.getSource().equals(quote_add)
					&& !quote_name.getText().trim().equals("")
					&& !quote_enter.getText().trim().equals("")
					&& !quote_output.getText().trim().equals("")) {
				config.setConfig("BBCode_quote", AddCode(quote, quote_name.getText(),
						quote_enter.getText(), quote_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(quote_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1 + iita - 1 + iunder - 1 + ibcolor - 1 + icode - 1 + iquote
						- 1);
			} else if (e.getSource().equals(font_add)
					&& !font_name.getText().trim().equals("")
					&& !font_enter.getText().trim().equals("")
					&& !font_output.getText().trim().equals("")) {
				config.setConfig("BBCode_font", AddCode(font, font_name.getText(),
						font_enter.getText(), font_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(font_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1 + iita - 1 + iunder - 1 + ibcolor - 1 + icode - 1 + iquote
						- 1 + ifont - 1);
			} else if (e.getSource().equals(img_add) && !img_name.getText().trim().equals("")
					&& !img_enter.getText().trim().equals("")
					&& !img_output.getText().trim().equals("")) {
				config.setConfig("BBCode_img", AddCode(img, img_name.getText(),
						img_enter.getText(), img_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(img_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1 + iita - 1 + iunder - 1 + ibcolor - 1 + icode - 1 + iquote
						- 1 + ifont - 1 + iimg - 1);
			} else if (e.getSource().equals(url_add) && !url_name.getText().trim().equals("")
					&& !url_enter.getText().trim().equals("")
					&& !url_output.getText().trim().equals("")) {
				config.setConfig("BBCode_url", AddCode(url, url_name.getText(),
						url_enter.getText(), url_output.getText()));
				bbmanage.update();
				ch_delete.insertItemAt(url_name.getText().trim(), icenter - 1 + isize - 1
						+ ibold - 1 + iita - 1 + iunder - 1 + ibcolor - 1 + icode - 1 + iquote
						- 1 + ifont - 1 + iimg - 1 + iurl - 1);
			}
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}
	}

	private String AddCode(String[] tmp, String name, String enter, String output) {
		tmp[0] += "<|>" + name.trim();
		tmp[1] += "<|>" + enter.trim();
		tmp[2] += "<|>" + output.trim();
		return tmp[0] + "<<|>>" + tmp[1] + "<<|>>" + tmp[2];
	}

	private String DelBBCode(String[] tmp, int index) {
		int i;
		String[] bb = tmp[0].split("<\\|>");
		tmp[0] = bb[0];
		for (i = 1; i < bb.length; i++) {
			if (index != i - 1)
				tmp[0] += "<|>" + bb[i];
		}
		bb = tmp[1].split("<\\|>");
		tmp[1] = bb[0];
		for (i = 1; i < bb.length; i++) {
			if (index != i - 1)
				tmp[1] += "<|>" + bb[i];
		}
		bb = tmp[2].split("<\\|>");
		tmp[2] = bb[0];
		for (i = 1; i < bb.length; i++) {
			if (index != i - 1)
				tmp[2] += "<|>" + bb[i];
		}
		return tmp[0] + "<<|>>" + tmp[1] + "<<|>>" + tmp[2];
	}

	String lang;

	private BBManage bbmanage;

	public JComboBox ch_delete;
	public JButton delete;

	public JTextField center_name;
	public JTextField center_enter;
	public JTextField center_output;
	public JButton center_add;

	public JTextField size_name;
	public JTextField size_enter;
	public JTextField size_output;
	public JButton size_add;

	public JTextField bold_name;
	public JTextField bold_enter;
	public JTextField bold_output;
	public JButton bold_add;

	public JTextField ita_name;
	public JTextField ita_enter;
	public JTextField ita_output;
	public JButton ita_add;

	public JTextField under_name;
	public JTextField under_enter;
	public JTextField under_output;
	public JButton under_add;

	public JTextField bcolor_name;
	public JTextField bcolor_enter;
	public JTextField bcolor_output;
	public JButton bcolor_add;

	public JTextField code_name;
	public JTextField code_enter;
	public JTextField code_output;
	public JButton code_add;

	public JTextField quote_name;
	public JTextField quote_enter;
	public JTextField quote_output;
	public JButton quote_add;

	public JTextField font_name;
	public JTextField font_enter;
	public JTextField font_output;
	public JButton font_add;

	public JTextField img_name;
	public JTextField img_enter;
	public JTextField img_output;
	public JButton img_add;

	public JTextField url_name;
	public JTextField url_enter;
	public JTextField url_output;
	public JButton url_add;
}
