/*
 * Fenetre.java
 *
 * Created on 31 mars 2006, 23:29
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package fr.ogsteam.ogsconverter;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * 
 * @author MOREAU Beno�t
 */
public class Fenetre extends JFrame implements WindowListener, ActionListener, MouseListener,
		ItemListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of Fenetre */
	public Fenetre(String mdl) {
		super();

		int i, j;
		String tmp, tmp2, lang_name = "";
		String MyMdls[] = null, mdls[] = null;

		try {
			tmp = Configuration.getConfig("config.ini", "skin");
			setskin(tmp, false);
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		this.getToolkit().setDynamicLayout(true);
		this.getRootPane().setMinimumSize(new Dimension(10, 10));

		try {
			lang_name = Configuration.getConfig("config.ini", "active_language");
			lang = "lang_" + lang_name + ".ini";
			model = Configuration.getConfig("config.ini", "active_model");
			setTitle("OGSConverter v" + Configuration.getConfig("config.ini", "v"));
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
		if (Main.JWS) {
			setSize(800, 600);
		} else {
			i = Main.prefs.getInt("width", 700);
			j = Main.prefs.getInt("height", 500);
			setSize((i == 0 ? 700 : i), (j == 0 ? 500 : j));
		}

		setDefaultCloseOperation(0);
		addWindowListener(this);
		setIconImage(new ImageIcon(this.getClass().getResource("images/icone.png")).getImage());

		Configuration configL;

		JMenuBar menubar = null;
		JMenu fichier = null;
		JMenu edit = null;
		JMenu option = null;
		JMenu opt_skin = null;
		JMenu help = null;
		JMenuItem item = null;

		JPanel panel = new JPanel();
		JPanel sudpanel = new JPanel();
		JPanel sudpanel2 = new JPanel();
		BorderLayout border = new BorderLayout();
		GridLayout sud = new GridLayout(1, 3);
		BorderLayout sudsud = new BorderLayout();
		JScrollPane scrollpane = new JScrollPane();
		JScrollPane scrollpane2 = new JScrollPane();
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollpane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollpane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		sp.setResizeWeight(0.5);
		sp.setOneTouchExpandable(true);
		menubar = new JMenuBar();
		ButtonGroup group;
		TitledBorder cadre;

		popup_menu = new JPopupMenu();

		try {
			configL = new Configuration(lang);

			popup_cut = new JMenuItem(configL.getConfig("menu_popup_cut"));
			popup_menu.add(popup_cut);
			popup_cut.addActionListener(this);

			popup_copy = new JMenuItem(configL.getConfig("copier_button"));
			popup_menu.add(popup_copy);
			popup_copy.addActionListener(this);

			popup_paste = new JMenuItem(configL.getConfig("menu_popup_paste"));
			popup_menu.add(popup_paste);
			popup_paste.addActionListener(this);

			popup_selectAll = new JMenuItem(configL.getConfig("menu_popup_selectAll"));
			popup_menu.add(popup_selectAll);
			popup_selectAll.addActionListener(this);

			popup_meff = new JMenuItem(configL.getConfig("menu_edit_meff"));
			popup_menu.add(popup_meff);
			popup_meff.addActionListener(this);

			text = new JTextArea(configL.getConfig("textaera"));
			text_formated = new JTextArea();

			scrollpane.setViewportView(text);
			scrollpane2.setViewportView(text_formated);

			cadre = BorderFactory.createTitledBorder(configL.getConfig("copy_text"));
			scrollpane.setBorder(cadre);

			cadre = BorderFactory.createTitledBorder(configL.getConfig("result_text"));
			scrollpane2.setBorder(cadre);

			sp.setLeftComponent(scrollpane);
			sp.setRightComponent(scrollpane2);

			convert = new JButton(configL.getConfig("convert_button")) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public Point getToolTipLocation(MouseEvent em) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return new Point(em.getX() + 10, em.getY() - 15);
				}
			};
			convert.setIcon(new ImageIcon(this.getClass().getResource("images/convert.png")));
			copier = new JButton(configL.getConfig("copier_button")) {
				private static final long serialVersionUID = 1L;

				public Point getToolTipLocation(MouseEvent em) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return new Point(em.getX() + 10, em.getY() - 15);
				}
			};
			copier.setIcon(new ImageIcon(this.getClass().getResource("images/copy.png")));
			effacer = new JButton(configL.getConfig("menu_edit_meff")) {
				private static final long serialVersionUID = 1L;

				public Point getToolTipLocation(MouseEvent em) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return new Point(em.getX() + 10, em.getY() - 15);
				}
			};
			effacer.setIcon(new ImageIcon(this.getClass().getResource("images/delete.png")));
			coller = new JButton(configL.getConfig("menu_popup_paste"));
			coller.setIcon(new ImageIcon(this.getClass().getResource(
					"icons/menu_paste_s16.gif")));
			previsualise = new JButton(configL.getConfig("previsu_button")) {
				private static final long serialVersionUID = 1L;

				public Point getToolTipLocation(MouseEvent em) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return new Point(em.getX() + 10, em.getY() - 15);
				}
			};
			previsualise.setIcon(new ImageIcon(this.getClass().getResource(
					"images/preview.png")));

			stats = new JButton(configL.getConfig("stats_button")) {
				private static final long serialVersionUID = 1L;

				public Point getToolTipLocation(MouseEvent em) {
					try {
						Thread.sleep(500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return new Point(em.getX() + 10, em.getY() - 15);
				}
			};
			stats.setIcon(new ImageIcon(this.getClass().getResource("images/graph.png")));

			fichier = new JMenu(configL.getConfig("menu_file"));
			edit = new JMenu(configL.getConfig("menu_edit"));
			option = new JMenu(configL.getConfig("menu_option"));
			help = new JMenu(configL.getConfig("menu_help"));

			quit = new JMenuItem(configL.getConfig("menu_file_quit"), KeyEvent.VK_Q);
			quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, mask));
			sauver_sous = new JMenuItem(configL.getConfig("menu_file_saveas"), KeyEvent.VK_S);
			sauver_sous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, mask));
			fichier.add(sauver_sous);
			fichier.addSeparator();
			fichier.add(quit);
			quit.addActionListener(this);
			sauver_sous.addActionListener(this);

			mcopier = new JMenuItem(configL.getConfig("copier_button"), KeyEvent.VK_K);
			mcopier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, mask));
			edit.add(mcopier);
			mcopier.addActionListener(this);

			meff = new JMenuItem(configL.getConfig("menu_edit_meff"), KeyEvent.VK_DELETE);
			meff.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, mask));
			edit.add(meff);
			meff.addActionListener(this);

			opt_lang = new JMenu(configL.getConfig("menu_opt_lang"));

			group = new ButtonGroup();

			String lgs[] = Configuration.getConfig("config.ini", "possible_language").split(
					",");
			langs = new JRadioButtonMenuItem[lgs.length];
			for (i = 0; i < lgs.length; i++) {
				langs[i] = new JRadioButtonMenuItem(lgs[i], false);
				if (lang_name.equals(lgs[i]))
					langs[i].setSelected(true);
				langs[i].addItemListener(this);
				opt_lang.add(langs[i]);
				group.add(langs[i]);
			}

			if (!Main.JWS) {
				opt_addlang = new JMenuItem(configL.getConfig("menu_opt_addlang"));
				opt_lang.addSeparator();
				opt_lang.add(opt_addlang);
				opt_dellang = new JMenuItem(configL.getConfig("menu_opt_dellang"));
				opt_lang.add(opt_dellang);
			}

			opt_model = new JMenu(configL.getConfig("menu_opt_model"));

			popup_menu.addSeparator();
			popup_menu.add(new JLabel("<html>&nbsp;&nbsp;<u><font color='#203020'>"
					+ configL.getConfig("menu_popup_convert") + "</font></u></html>"));

			mdls = Configuration.getConfig("config.ini", "possible_model").split(",");
			if (Main.JWS) {
				MyMdls = OGSConnection.getModelList(false);
				this.MyMdls = MyMdls;
			}

			models = new JRadioButtonMenuItem[mdls.length
					+ (MyMdls == null ? 0 : MyMdls.length)];
			group = new ButtonGroup();
			popup_convert = new JMenuItem[mdls.length + (MyMdls == null ? 0 : MyMdls.length)];
			for (i = 0; i < mdls.length; i++) {
				models[i] = new JRadioButtonMenuItem(mdls[i], false);
				popup_convert[i] = new JMenuItem(mdls[i]);
				opt_model.add(models[i]);
				if (model.equals(mdls[i]))
					models[i].setSelected(true);
				models[i].addItemListener(this);
				popup_convert[i].addActionListener(this);
				popup_menu.add(popup_convert[i]);
				group.add(models[i]);
			}

			if (!Main.JWS) {
				opt_addmodel = new JMenuItem(configL.getConfig("menu_opt_addmodel"));
				opt_model.addSeparator();
				opt_model.add(opt_addmodel);

				opt_delmodel = new JMenuItem(configL.getConfig("menu_opt_delmodel"));
				opt_model.add(opt_delmodel);
			} else {
				if (MyMdls != null) {
					for (i = mdls.length; i < MyMdls.length + mdls.length; i++) {
						models[i] = new JRadioButtonMenuItem(MyMdls[i - mdls.length], false);
						popup_convert[i] = new JMenuItem(MyMdls[i - mdls.length]);
						opt_model.add(models[i]);
						if (model.equals(MyMdls[i - mdls.length]))
							models[i].setSelected(true);
						models[i].addItemListener(this);
						popup_convert[i].addActionListener(this);
						popup_menu.add(popup_convert[i]);
						group.add(models[i]);
					}
				}
			}

			group = new ButtonGroup();
			tmp = Configuration.getConfig("config.ini", "skin");
			opt_skin = new JMenu("skin");
			opt_skin.getPopupMenu().setLayout(new GridLayout(20, 0));
			skinItem = new JRadioButtonMenuItem[Main.skin.size()];
			Iterator keyIterator = Main.skin.keySet().iterator();
			for (i = 0; i < Main.skin.size(); i++) {
				tmp2 = (String) keyIterator.next();
				opt_skin.add(skinItem[i] = new JRadioButtonMenuItem(tmp2, tmp.equals(tmp2)));
				group.add(skinItem[i]);
				skinItem[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						try {
							String tmp = ((JRadioButtonMenuItem) (event.getSource())).getText();
							setskin(tmp, true);
							Configuration.setConfig("config.ini", "skin",
									((JRadioButtonMenuItem) (event.getSource())).getText());
						} catch (Exception e) {
							ExceptionAlert.createExceptionAlert(e);
							e.printStackTrace();
						}
					}
				});
			}

			about = new JMenuItem("About");
			about.addActionListener(this);
			this.help = new JMenuItem("Help");
			this.help.addActionListener(this);

			help.add(this.help);
			help.addSeparator();
			help.add(about);

			MAJ = new JCheckBoxMenuItem(configL.getConfig("menu_maj"), false);
			if (Configuration.getConfig("config.ini", "MAJ").equals("1"))
				MAJ.setState(true);
			MAJ.addItemListener(this);

			clipbord = new JCheckBoxMenuItem(configL.getConfig("menu_clipbord"), false);
			if (Configuration.getConfig("config.ini", "clipbord").equals("1"))
				clipbord.setState(true);
			clipbord.addItemListener(this);

			auto_convert = new JCheckBoxMenuItem(configL.getConfig("menu_auto_convert"), false);
			if (Configuration.getConfig("config.ini", "auto_convert").equals("1"))
				auto_convert.setState(true);
			auto_convert.addItemListener(this);

			auto_convert.setEnabled(clipbord.getState());
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		sudpanel2.setLayout(sud);
		sudpanel2.add(convert);
		sudpanel2.add(copier);
		sudpanel2.add(coller);
		sudpanel2.add(effacer);
		sudpanel2.add(previsualise);
		sudpanel2.add(stats);

		convert.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+W");
		copier.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+K");
		previsualise.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+P");
		effacer.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+E");
		stats.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+G");

		sudpanel.setLayout(sudsud);
		sudpanel.add(sudpanel2, BorderLayout.CENTER);
		JPanel pan = new JPanel();
		pan.setPreferredSize(new Dimension(1000, 25));
		setStateBar(pan);
		sudpanel.add(pan, BorderLayout.PAGE_END);

		panel.setLayout(border);
		panel.add(sp, BorderLayout.CENTER);
		panel.add(sudpanel, BorderLayout.SOUTH);

		setFastConfig();
		panel.add(fastPanel, BorderLayout.PAGE_START);

		option.add(opt_model);
		option.add(opt_lang);
		if (!Main.JWS)
			option.add(opt_skin);
		option.add(item = new JMenuItem("Upload Images"));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Main.openurl("http://www.imageshack.us/");
			}
		});
		option.add(clipbord);
		option.add(auto_convert);
		if (!Main.JWS)
			option.add(MAJ);
		menubar.add(fichier);
		menubar.add(edit);
		menubar.add(option);
		menubar.add(help);

		setJMenuBar(menubar);

		stats.addActionListener(this);
		copier.addActionListener(this);
		convert.addActionListener(this);
		previsualise.addActionListener(this);
		effacer.addActionListener(this);
		coller.addActionListener(this);
		if (!Main.JWS) {
			opt_addlang.addActionListener(this);
			opt_dellang.addActionListener(this);
			opt_addmodel.addActionListener(this);
			opt_delmodel.addActionListener(this);
		}

		text.addMouseListener(this);

		tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Converter", null, panel, "Converter");
		tabbedPane.addTab("Config", null, configs = new Configs(), "Config");
		if (!Main.JWS)
			tabbedPane.addTab("Editor", null, editTab = new EditorTab(mdl), "Model Editor");

		tabbedPane.setSelectedIndex(mdl == null ? 0 : 2);

		getContentPane().add(tabbedPane);

		if (!Main.JWS)
			setLocation(Main.prefs.getInt("x_location", 50), Main.prefs.getInt("y_location",
					50));
		else
			setLocationRelativeTo(getParent());

		if (Main.JWS || !(new File("OGSConverter.exe")).exists()) {
			SplashWindow.stopSplashWindow();
		}

		this.setVisible(true);

		initKey();
	}

	public void setFastConfig() {
		Configuration iniConfig = null;
		String tmp[], setting;
		Border cadre;
		int id;

		try {
			iniConfig = new Configuration("config.ini");

			if (fastPanel == null) {
				fastPanel = new JPanel();
				cadre = BorderFactory.createTitledBorder("Fast Config");

				fastBBCode = new JComboBox() {
					private static final long serialVersionUID = 7020760898703412672L;

					public Dimension getPreferredSize() {
						Dimension dim = (Dimension) super.getPreferredSize();
						if (dim.getWidth() < this.getMinimumSize().getWidth())
							dim.width = (int) this.getMinimumSize().getWidth();
						if (dim.getWidth() > this.getMaximumSize().getWidth())
							dim.width = (int) this.getMaximumSize().getWidth();
						if (dim.getHeight() < this.getMinimumSize().getHeight())
							dim.height = (int) this.getMinimumSize().getHeight();
						if (dim.getHeight() > this.getMaximumSize().getHeight())
							dim.height = (int) this.getMaximumSize().getHeight();
						return dim;
					}
				};
				fastBBCode.setFont(new Font(fastBBCode.getFont().getName(), Font.PLAIN, 10));
				fastBBCode.setMinimumSize(new Dimension(70, 15));
				fastBBCode.setMaximumSize(new Dimension(100, 25));

				fastColor = new JComboBox() {
					private static final long serialVersionUID = 7279145606234640746L;

					public Dimension getPreferredSize() {
						Dimension dim = (Dimension) super.getPreferredSize();
						if (dim.getWidth() < this.getMinimumSize().getWidth())
							dim.width = (int) this.getMinimumSize().getWidth();
						if (dim.getWidth() > this.getMaximumSize().getWidth())
							dim.width = (int) this.getMaximumSize().getWidth();
						if (dim.getHeight() < this.getMinimumSize().getHeight())
							dim.height = (int) this.getMinimumSize().getHeight();
						if (dim.getHeight() > this.getMaximumSize().getHeight())
							dim.height = (int) this.getMaximumSize().getHeight();
						return dim;
					}
				};
				fastColor.setFont(new Font(fastColor.getFont().getName(), Font.PLAIN, 10));
				fastColor.setMinimumSize(new Dimension(70, 15));
				fastColor.setMaximumSize(new Dimension(100, 25));

				fastModel = new JComboBox() {
					private static final long serialVersionUID = 2318708052080592907L;

					public Dimension getPreferredSize() {
						Dimension dim = (Dimension) super.getPreferredSize();
						if (dim.getWidth() < this.getMinimumSize().getWidth())
							dim.width = (int) this.getMinimumSize().getWidth();
						if (dim.getWidth() > this.getMaximumSize().getWidth())
							dim.width = (int) this.getMaximumSize().getWidth();
						if (dim.getHeight() < this.getMinimumSize().getHeight())
							dim.height = (int) this.getMinimumSize().getHeight();
						if (dim.getHeight() > this.getMaximumSize().getHeight())
							dim.height = (int) this.getMaximumSize().getHeight();
						return dim;
					}
				};
				fastModel.setFont(new Font(fastModel.getFont().getName(), Font.PLAIN, 10));
				fastModel.setMinimumSize(new Dimension(70, 15));
				fastModel.setMaximumSize(new Dimension(100, 25));

				fastUniverse = new JComboBox() {
					private static final long serialVersionUID = 6241726851881490722L;

					public Dimension getPreferredSize() {
						Dimension dim = (Dimension) super.getPreferredSize();
						if (dim.getWidth() < this.getMinimumSize().getWidth())
							dim.width = (int) this.getMinimumSize().getWidth();
						if (dim.getWidth() > this.getMaximumSize().getWidth())
							dim.width = (int) this.getMaximumSize().getWidth();
						if (dim.getHeight() < this.getMinimumSize().getHeight())
							dim.height = (int) this.getMinimumSize().getHeight();
						if (dim.getHeight() > this.getMaximumSize().getHeight())
							dim.height = (int) this.getMaximumSize().getHeight();
						return dim;
					}
				};
				fastUniverse.setFont(new Font(fastUniverse.getFont().getName(), Font.PLAIN, 10));
				fastUniverse.setMinimumSize(new Dimension(70, 15));
				fastUniverse.setMaximumSize(new Dimension(100, 25));

				speed = new JComboBox(new String[] { "10", "20", "30", "40", "50", "60", "70",
						"80", "90", "100" }) {
					private static final long serialVersionUID = 8845154097966824401L;

					public Dimension getPreferredSize() {
						Dimension dim = (Dimension) super.getPreferredSize();
						if (dim.getWidth() < this.getMinimumSize().getWidth())
							dim.width = (int) this.getMinimumSize().getWidth();
						if (dim.getWidth() > this.getMaximumSize().getWidth())
							dim.width = (int) this.getMaximumSize().getWidth();
						if (dim.getHeight() < this.getMinimumSize().getHeight())
							dim.height = (int) this.getMinimumSize().getHeight();
						if (dim.getHeight() > this.getMaximumSize().getHeight())
							dim.height = (int) this.getMaximumSize().getHeight();
						return dim;
					}
				};
				speed.setSelectedIndex(9);
				speed.setFont(new Font(speed.getFont().getName(), Font.PLAIN, 10));
				speed.setMinimumSize(new Dimension(50, 15));

				JScrollPane jsp = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jsp.getHorizontalScrollBar().addComponentListener(new ComponentListener() {

					public void componentHidden(ComponentEvent e) {
						Dimension dim = fastPanel.getPreferredSize();
						dim.height -= 20;
						fastPanel.setPreferredSize(dim);
					}

					public void componentMoved(ComponentEvent e) {
					}

					public void componentResized(ComponentEvent e) {
					}

					public void componentShown(ComponentEvent e) {
						Dimension dim = fastPanel.getPreferredSize();
						dim.height += 20;
						fastPanel.setPreferredSize(dim);
					}

				});

				JPanel scollablePane = new JPanel();
				// pan.setLayout(new GridLayout(1, 0));
				scollablePane.setLayout(new FlowLayout());
				scollablePane.add(fastBBCode);
				scollablePane.add(new JLabel(" Color:"));
				scollablePane.add(fastColor);
				scollablePane.add(new JLabel(" Model:"));
				scollablePane.add(fastModel);
				scollablePane.add(new JLabel(" Universe:"));
				scollablePane.add(fastUniverse);
				scollablePane.add(new JLabel(" speed:"));
				scollablePane.add(speed);
				scollablePane.add(new JLabel("%"));

				jsp.setViewportView(scollablePane);
				jsp.setBorder(null);

				fastPanel.setBorder(cadre);
				fastPanel.setLayout(new BorderLayout());
				fastPanel.add(jsp);

				Dimension dim = fastPanel.getPreferredSize();
				dim.height += 20;
				fastPanel.setPreferredSize(dim);
			}

			fastBBCode.removeItemListener(this);
			fastBBCode.removeAllItems();
			tmp = iniConfig.getConfig("BBname").split("\\|");
			fastBBCode.addItem("BBCode...");
			for (id = 0; id < tmp.length; id++)
				fastBBCode.addItem(tmp[id]);

			fastColor.removeItemListener(this);
			fastColor.removeAllItems();
			tmp = iniConfig.getConfig("user_color_name").split("\\|");
			fastColor.addItem("dark background");
			fastColor.addItem("light background");
			for (id = 0; id < tmp.length; id++)
				fastColor.addItem(tmp[id]);
			setting = iniConfig.getConfig("color");
			if (setting.equals("01"))
				fastColor.setSelectedIndex(0);
			else if (setting.equals("02"))
				fastColor.setSelectedIndex(1);
			else
				fastColor.setSelectedIndex(Integer.parseInt(setting) + 1);

			fastModel.removeItemListener(this);
			fastModel.removeAllItems();
			tmp = iniConfig.getConfig("possible_model").split(",");
			for (id = 0; id < tmp.length; id++) {
				fastModel.addItem(tmp[id]);
				if (iniConfig.getConfig("active_model").equals(tmp[id]))
					fastModel.setSelectedIndex(id);
			}
			if (Main.JWS) {
				if (MyMdls != null) {
					for (id = tmp.length; id < MyMdls.length + tmp.length; id++) {
						fastModel.addItem(MyMdls[id - tmp.length]);
						if (iniConfig.getConfig("active_model")
								.equals(MyMdls[id - tmp.length]))
							fastModel.setSelectedIndex(id);
					}
				}
			}

			fastUniverse.removeItemListener(this);
			fastUniverse.removeAllItems();
			tmp = iniConfig.getConfig("universes").split("\\|\\|");
			for (id = 0; id < tmp.length; id++)
				fastUniverse.addItem(tmp[id]);
			setting = iniConfig.getConfig("universe_selected");
			fastUniverse.setSelectedIndex(Integer.parseInt(setting));

		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		fastBBCode.addItemListener(this);
		fastColor.addItemListener(this);
		fastModel.addItemListener(this);
		fastUniverse.addItemListener(this);
	}

	public void setStateBar(JPanel pan) {

		Configuration iniConfig = null;
		String setting;
		Border cadre;
		int id;

		if (pan != null) {
			cadre = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			iamatt = new JLabel();
			iamatt.setBorder(cadre);
			cadre = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			systemG = new JLabel();
			systemG.setBorder(cadre);
			cadre = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			uniSpeed = new JLabel();
			uniSpeed.setBorder(cadre);
			cadre = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			actColor = new JLabel();
			actColor.setBorder(cadre);
			cadre = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			actModel = new JLabel();
			actModel.setBorder(cadre);
			cadre = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
			universCell = new JLabel();
			universCell.setBorder(cadre);

			pan.setLayout(new FlowLayout());
			pan.add(iamatt);
			pan.add(universCell);
			pan.add(systemG);
			pan.add(uniSpeed);
			pan.add(actColor);
			pan.add(actModel);
		}

		try {
			iniConfig = new Configuration("config.ini");

			setting = iniConfig.getConfig("color");

			if (setting.equals("01")) {
				setting = "dark color";
			} else if (setting.equals("02")) {
				setting = "light color";
			} else {
				setting = iniConfig.getConfig("user_color_name").split("\\|")[(int) Main.exptoint(setting) - 1];
			}

			iamatt.setText(" "
					+ (iniConfig.getConfig("iamatt").equals("1") ? "I am attacker"
							: "I am defender") + " ");
			id = Integer.parseInt(iniConfig.getConfig("universe_selected"));
			universCell.setText(" Universe: "
					+ iniConfig.getConfig("universes").split("\\|\\|")[id] + " ");
			systemG.setText(" S/G: " + iniConfig.getConfig("system").split("\\|")[id] + " ");
			uniSpeed.setText(" Uni speed: " + iniConfig.getConfig("speed").split("\\|")[id]
					+ " ");
			actColor.setText(" Color: " + setting + " ");
			actModel.setText(" Model: " + iniConfig.getConfig("active_model") + " ");
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public void add_language(String fichier) {
		int i;
		boolean ok = true;
		try {
			String lang = Configuration.getConfig("config.ini", "possible_language");
			String lgs[] = lang.split(",");

			if (config.islangfile(fichier)) {

				String newlang[] = fichier.split("(lang_|.ini)");

				for (i = 0; i < lgs.length; i++) {
					if (lgs[i].equals(newlang[1])) {
						ok = false;
						JOptionPane.showMessageDialog(this, fichier + " already exists",
								"ERREUR", JOptionPane.NO_OPTION);
					}
				}

				if (ok) {
					Configuration.setConfig("config.ini", "possible_language", lang + ","
							+ newlang[1]);
					lang = Configuration.getConfig("config.ini", "possible_language");
					ButtonGroup group = new ButtonGroup();
					lgs = lang.split(",");
					opt_lang.removeAll();
					langs = null;
					langs = new JRadioButtonMenuItem[lgs.length];
					for (i = 0; i < lgs.length; i++) {
						langs[i] = new JRadioButtonMenuItem(lgs[i], false);
						group.add(langs[i]);
						opt_lang.add(langs[i]);
						if (this.lang.equals(lgs[i]))
							langs[i].setSelected(true);
						langs[i].addItemListener(this);
					}
					opt_lang.addSeparator();
					opt_lang.add(opt_addlang);
					opt_lang.add(opt_dellang);
				}
			}
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public void add_model(String fichier) {
		int i;
		boolean ok = true;
		try {
			String model = Configuration.getConfig("config.ini", "possible_model");
			String mdls[] = model.split(",");

			if (config.ismodelfile(fichier)) {

				String newmdl = fichier.substring(0, fichier.length() - 5);

				for (i = 0; i < mdls.length; i++) {
					if (mdls[i].equals(newmdl)) {
						ok = false;
						JOptionPane.showMessageDialog(this, fichier + " already exists",
								"ERREUR", JOptionPane.NO_OPTION);
					}
				}

				if (ok) {
					Configuration.setConfig("config.ini", "possible_model", model + ","
							+ newmdl);

					model = Configuration.getConfig("config.ini", "possible_model");
					mdls = model.split(",");
					opt_model.removeAll();
					models = null;
					ButtonGroup group = new ButtonGroup();
					models = new JRadioButtonMenuItem[mdls.length];
					for (i = 0; i < mdls.length; i++) {
						models[i] = new JRadioButtonMenuItem(mdls[i], false);
						group.add(models[i]);
						opt_model.add(models[i]);
						if (this.model.equals(mdls[i]))
							models[i].setSelected(true);
						models[i].addItemListener(this);
					}
					opt_model.addSeparator();
					opt_model.add(opt_addmodel);
					opt_model.add(opt_delmodel);

					JMenuItem[] popup_convert = new JMenuItem[mdls.length];
					System.arraycopy(this.popup_convert, 0, popup_convert, 0, mdls.length - 1);
					this.popup_convert = null;
					this.popup_convert = popup_convert;
					this.popup_convert[this.popup_convert.length - 1] = new JMenuItem(
							mdls[mdls.length - 1]);
					this.popup_convert[this.popup_convert.length - 1].addActionListener(this);
					popup_menu.add(this.popup_convert[mdls.length - 1]);
					SwingUtilities.updateComponentTreeUI(this.popup_menu);
				}
			}
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public void CloseWindow() {
		if (!Main.JWS && editTab != null) {
			boolean modified = ((EditorTab) editTab).isModified();
			if (modified) {
				if (JOptionPane.showConfirmDialog(Main.getParentFrame(),
						"The active document in the editor tab has been modified.\nDo you want save it?") == JOptionPane.OK_OPTION)
					((EditorTab) editTab).save();
			}
		}

		if (this.getExtendedState() != JFrame.ICONIFIED
				&& this.getExtendedState() != JFrame.MAXIMIZED_BOTH && !Main.JWS) {
			Main.prefs.putInt("width", this.getWidth());
			Main.prefs.putInt("height", this.getHeight());
			Main.prefs.putInt("x_location", (int) this.getLocationOnScreen().getX());
			Main.prefs.putInt("y_location", (int) this.getLocationOnScreen().getY());
		}
		if (Main.JWS) {
			OGSConnection.saveConfig(false);
		}
		dispose();
		if (Main.menu != null)
			System.setProperty("apple.laf.useScreenMenuBar", Main.menu);
		System.exit(0);
	}

	public void windowClosing(WindowEvent e) {
		if (e.getSource().equals(this)) {
			this.CloseWindow();
		} else {
			Main.closeFrame((JFrame) e.getSource());
		}
	}

	public void windowOpened(WindowEvent windowevent) {

		if (!windowevent.getSource().equals(this))
			return;
/*//Controle de Mise � jour d�sactiv� car serveur indisponible
		try {
			if (Configuration.getConfig("config.ini", "MAJ").equals("1")) {
				Thread t = new Thread() {
					public void run() {
						String tmp[];
						String version = Main.getIpFrom("http://update.ogsconverter.com:80/OGSConv_version.php?pass=ogsconv_version2");
						if (version != null) {
							tmp = version.split("\\+\\+");
							try {
								if (!Configuration.getConfig("config.ini", "v").equals(tmp[0])) {
									new Update(tmp[0], tmp[1]);
								}
							} catch (Exception e) {
								ExceptionAlert.createExceptionAlert(e);
								e.printStackTrace();
							}
						}
					}
				};
				t.start();
			}
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}*/
	}

	public void windowClosed(WindowEvent windowevent) {
	}

	public void windowIconified(WindowEvent windowevent) {
	}

	public void windowDeiconified(WindowEvent windowevent) {
	}

	public void windowActivated(WindowEvent windowevent) {
	}

	public void windowDeactivated(WindowEvent windowevent) {
	}

	public void actionPerformed(ActionEvent e) {

		int i;
		String choose_model = null, last_model = null, textToConv;

		for (i = 0; i < models.length; i++) {
			if (e.getSource().equals(popup_convert[i])) {
				choose_model = popup_convert[i].getText();
				e.setSource(convert);
				break;
			}
		}

		if (tabbedPane.getSelectedIndex() == 0)
			text.requestFocus();

		if (e.getSource().equals(convert)) {
			textToConv = text.getText().replaceAll("\\\\(\\'|\\\")", "$1");

			try {
				String newtext = "Bad copy !";
				BBCode.setBBCode();
				Configuration configL = new Configuration("lang_"
						+ Configuration.getConfig("config.ini", "active_language") + ".ini");

				if (choose_model != null) {
					last_model = Configuration.getConfig("config.ini", "active_model");
					Configuration.setConfig("config.ini", "active_model", choose_model);
				}

				if (Pattern.compile(configL.getConfig("TOP"), Pattern.CASE_INSENSITIVE)
						.matcher(text.getText())
						.find()
						|| Pattern.compile(configL.getConfig("TOP2"), Pattern.CASE_INSENSITIVE)
								.matcher(text.getText())
								.find()
						|| (Configuration.getConfig("config.ini", "CR_end").equals("1") && (Pattern.compile(
								configL.getConfig("END1"), Pattern.CASE_INSENSITIVE)
								.matcher(text.getText())
								.find()
								|| Pattern.compile(configL.getConfig("END2"),
										Pattern.CASE_INSENSITIVE)
										.matcher(text.getText())
										.find() || Pattern.compile(configL.getConfig("END3"),
								Pattern.CASE_INSENSITIVE).matcher(text.getText()).find()))) {
					if (Configuration.getConfig("config.ini", "active_model").equals(
							"no model"))
						newtext = BBCode.addquote(0)
								+ Main.converter(textToConv,
										speed.getSelectedIndex() * 10 + 10)
								+ BBCode.addquote(1);
					else
						newtext = Main.personnal_converter(textToConv,
								speed.getSelectedIndex() * 10 + 10);
				} else if (Pattern.compile(configL.getConfig("REC_TOP"),
						Pattern.CASE_INSENSITIVE).matcher(text.getText()).find()) {
					newtext = BBCode.addquote(0)
							+ (Main.converterRecy(textToConv, Main.OUT_ATT))[0]
							+ BBCode.addquote(1);
				} else if (Pattern.compile(configL.getConfig("ER_TOP"),
						Pattern.CASE_INSENSITIVE).matcher(text.getText()).find()) {
					newtext = BBCode.addquote(0) + Main.converterER(textToConv)
							+ BBCode.addquote(1);
				} else if (Pattern.compile(configL.getConfig("ALLY_TOP"),
						Pattern.CASE_INSENSITIVE).matcher(text.getText()).find()) {
					newtext = BBCode.addquote(0) + Main.converterALLY(textToConv)
							+ BBCode.addquote(1);
				} else if (Pattern.compile(configL.getConfig("EMP_TOP"),
						Pattern.CASE_INSENSITIVE).matcher(text.getText()).find()) {
					newtext = BBCode.addquote(0) + Main.converterEMP(textToConv)
							+ BBCode.addquote(1);
				} else if (Pattern.compile(
						Main.MSK_FLOTTE_TOP.replaceFirst("\\[fleet\\]",
								configL.getConfig("ER_FLEET"))).matcher(text.getText()).find()) {
					newtext = BBCode.addquote(0) + Main.converterFLEET(textToConv)
							+ BBCode.addquote(1);
				}

				if (newtext.startsWith("Bad copy")
						&& !(auto_convert.getState() && clipbord.getState())) {
					newtext = Main.converterNotRecognized(textToConv);
				}
				if (newtext != null)
					text_formated.setText(newtext);

				if (choose_model != null && last_model != null)
					Configuration.setConfig("config.ini", "active_model", last_model);
				repaint();

				if (auto_convert.getState() && clipbord.getState() && newtext != null) {
					run_pause = true;
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
							new StringSelection(newtext), null);
					content = newtext;
					run_pause = false;
				}

			} catch (Exception exception) {
				ExceptionAlert.createExceptionAlert(exception);
				exception.printStackTrace();
			}
		} else if ((e.getSource().equals(copier) || e.getSource().equals(mcopier))
				&& tabbedPane.getSelectedIndex() == 0) {
			run_pause = true;
			text_formated.selectAll();
			text_formated.copy();
			if (clipbord.getState())
				content = text_formated.getText();
			run_pause = false;
		} else if (e.getSource().equals(opt_addlang)) {
			JFileChooser chooser = new JFileChooser(Main.USER_HOME);
			chooser.setFileFilter(new MonFiltre(new String[] { "lang_" },
					new String[] { "ini" }, "lang_**.ini"));
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String filename = chooser.getSelectedFile().getName();
				if (Main.USER_HOME.equals(chooser.getSelectedFile().getParent()))
					add_language(filename);
				else {
					JOptionPane.showMessageDialog(this, "You must copy the language file in "
							+ Main.USER_HOME, "ERREUR", JOptionPane.NO_OPTION);
				}
			}
		} else if (e.getSource().equals(opt_dellang)) {
			String lang = "", langs = "";
			try {
				lang = Configuration.getConfig("config.ini", "active_language");
				langs = Configuration.getConfig("config.ini", "possible_language");
			} catch (Exception exception) {
				ExceptionAlert.createExceptionAlert(exception);
				exception.printStackTrace();
			}
			if (lang.equals("fr")) {
				JOptionPane.showMessageDialog(this, "You can not delete the frensh language.",
						"ERREUR", JOptionPane.NO_OPTION);
			} else {
				langs = langs.replaceFirst("\\Q," + lang + "\\E", "");
				try {
					Configuration.setConfig("config.ini", "possible_language", langs);
					Configuration.setConfig("config.ini", "active_language", "fr");
					JOptionPane.showMessageDialog(this,
							"You must re-start OGSConverter to apply the new language.",
							"Language", JOptionPane.NO_OPTION);
				} catch (Exception exception) {
					ExceptionAlert.createExceptionAlert(exception);
					exception.printStackTrace();
				}
			}
		} else if (e.getSource().equals(opt_addmodel)) {
			JFileChooser chooser = new JFileChooser(Main.USER_HOME);
			chooser.setFileFilter(new MonFiltre(new String[] { "ogsc" }, "*.ogsc"));
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String filename = chooser.getSelectedFile().getName();
				if (Main.USER_HOME.equals(chooser.getSelectedFile().getParent()))
					add_model(filename);
				else {
					JOptionPane.showMessageDialog(this, "You must copy the model file in "
							+ Main.USER_HOME, "ERREUR", JOptionPane.NO_OPTION);
				}
			}
			setFastConfig();
		} else if (e.getSource().equals(opt_delmodel)) {
			String model = "", models = "";
			try {
				model = Configuration.getConfig("config.ini", "active_model");
				models = Configuration.getConfig("config.ini", "possible_model");
				if (!model.equals("no model")) {
					models = models.replaceFirst("\\Q," + model + "\\E", "");
					Configuration.setConfig("config.ini", "possible_model", models);
					Configuration.setConfig("config.ini", "active_model", "no model");
					for (i = 0; i < this.models.length; i++) {
						if (this.models[i].getText().equals(model)) {
							this.models[i].setSelected(false);
							this.models[i].setEnabled(false);
							this.models[i].removeItemListener(this);
							this.models[i].setVisible(false);
							this.popup_convert[i].setSelected(false);
							this.popup_convert[i].setEnabled(false);
							this.popup_convert[i].removeActionListener(this);
							this.popup_convert[i].setVisible(false);
						}
					}
					this.models[0].setSelected(true);
				}
			} catch (Exception exception) {
				ExceptionAlert.createExceptionAlert(exception);
				exception.printStackTrace();
			}
			setFastConfig();
		} else if (e.getSource().equals(quit)) {
			CloseWindow();
		} else if (e.getSource().equals(effacer)
				|| (e.getSource().equals(meff) || e.getSource().equals(popup_meff))
				&& tabbedPane.getSelectedIndex() == 0) {
			text.setText("");
		} else if (e.getSource().equals(sauver_sous) && tabbedPane.getSelectedIndex() == 0) {
			saveas();
		} else if (e.getSource().equals(previsualise)) {
			previsualisation(text_formated.getText());
		} else if (e.getSource().equals(about)) {
			Help h = new Help(Help.ABOUT);
			h.addWindowListener(this);
		} else if (e.getSource().equals(help)) {
			Help h = new Help(Help.HELP);
			h.addWindowListener(this);
		} else if (e.getSource().equals(popup_paste)) {
			text.paste();
		} else if (e.getSource().equals(coller)) {
			text.append(Main.enter);
			text.setCaretPosition(text.getText().length());
			text.paste();
		} else if (e.getSource().equals(popup_cut)) {
			run_pause = true;
			if (clipbord.getState())
				content = text.getSelectedText();
			text.cut();
			run_pause = false;
		} else if (e.getSource().equals(popup_selectAll)) {
			text.selectAll();
		} else if (e.getSource().equals(popup_copy)) {
			run_pause = true;
			text.copy();
			if (clipbord.getState())
				content = text.getSelectedText();
			run_pause = false;
		} else if (e.getSource().equals(stats)) {
			Main.sendForStats(text.getText(), speed.getSelectedIndex() * 10 + 10);
		}

		setStateBar(null);
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger() && e.getSource().equals(text)) {
			popup_menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (tabbedPane.getSelectedIndex() == 0)
			text.requestFocus();

		if (SwingUtilities.isLeftMouseButton(e) && e.getSource().equals(text)) {
			try {
				if (text.getText().equals(Configuration.getConfig(lang, "textaera")))
					text.setText("");
				else if (text.getText().equals("Bad copy !"))
					text.setText("");
			} catch (Exception ex) {
				ExceptionAlert.createExceptionAlert(ex);
				ex.printStackTrace();
			}
		} else if (e.isPopupTrigger() && e.getSource().equals(text)) {
			popup_menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void itemStateChanged(ItemEvent e) {
		int i;
		String group = "";

		if (tabbedPane != null && tabbedPane.getSelectedIndex() == 0)
			text.requestFocus();

		for (i = 0; i < langs.length; i++) {
			if (e.getSource().equals(langs[i]))
				group = "langs";
		}

		for (i = 0; i < models.length; i++) {
			if (e.getSource().equals(models[i]))
				group = "models";
		}

		try {

			if (group.equals("langs")) {
				if (((JRadioButtonMenuItem) (e.getSource())).isSelected()) {
					Configuration.setConfig("config.ini", "active_language",
							((JRadioButtonMenuItem) (e.getSource())).getText());

					if (!Main.JWS) {
						JOptionPane.showMessageDialog(this,
								"OGSConverter will be started again.", "Language",
								JOptionPane.INFORMATION_MESSAGE);

						File f = new File("OGSConverter.exe");

						if (f.exists()) {
							if (System.getProperties().getProperty("os.name").startsWith(
									"Windows 9")
									|| System.getProperties()
											.getProperty("os.name")
											.startsWith("Windows ME")) {
								Runtime.getRuntime().exec("command /C start OGSConverter");
							} else if (System.getProperties()
									.getProperty("os.name")
									.startsWith("Windows")) {
								Runtime.getRuntime().exec("cmd /c start OGSConverter");
							} else {
								Runtime.getRuntime().exec("OGSConverter");
							}
						} else {
							Runtime.getRuntime().exec("java -jar OGSConverter.jar");
						}
						System.exit(0);
					} else {
						JOptionPane.showMessageDialog(this,
								"You must re-start OGSConverter to apply the new language.",
								"Language", JOptionPane.YES_OPTION);
					}
				}
			} else if (group.equals("models")) {
				if (((JRadioButtonMenuItem) (e.getSource())).isSelected()) {
					Configuration.setConfig("config.ini", "active_model",
							((JRadioButtonMenuItem) (e.getSource())).getText());
					fastModel.setSelectedItem(((JRadioButtonMenuItem) (e.getSource())).getText());
				}
			} else if (e.getSource().equals(MAJ)) {
				Configuration.setConfig("config.ini", "MAJ", MAJ.getState());
			} else if (e.getSource().equals(clipbord)) {
				run_pause = true;
				auto_convert.setEnabled(clipbord.getState());
				Configuration.setConfig("config.ini", "clipbord", clipbord.getState());
				temp = "";
				content = "";
				run_pause = false;
			} else if (e.getSource().equals(fastBBCode)) {
				if (fastBBCode.getSelectedIndex() > 0)
					configs.updateBBCode(fastBBCode.getSelectedIndex() - 1);
			} else if (e.getSource().equals(fastColor)) {
				i = fastColor.getSelectedIndex();
				if (i == 0)
					Configuration.setConfig("config.ini", "color", "01");
				else if (i == 1)
					Configuration.setConfig("config.ini", "color", "02");
				else if (i > 1)
					Configuration.setConfig("config.ini", "color", Integer.toString(i - 1));

				configs.init_colorConfigList(1);
			} else if (e.getSource().equals(fastUniverse)) {
				i = fastUniverse.getSelectedIndex();
				if (i >= 0) {
					Configuration.setConfig("config.ini", "universe_selected",
							Integer.toString(i));
					configs.updateUniversData();
				}
			} else if (e.getSource().equals(fastModel)) {
				group = (String) fastModel.getSelectedItem();
				if (group != null) {
					Configuration.setConfig("config.ini", "active_model", group);
					models[fastModel.getSelectedIndex()].setSelected(true);
				}
			}

		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}

		setStateBar(null);
	}

	public void initKey() {
		InputMap rootin = this.getRootPane().getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		// InputMap rootintext =
		// text.getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		ActionMap rootact = getRootPane().getActionMap();
		// ActionMap rootacttext = getRootPane().getActionMap();

		rootin.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, mask), "preview");
		rootin.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, mask), "convert");
		rootin.put(KeyStroke.getKeyStroke(KeyEvent.VK_G, mask), "stats");
		rootin.put(KeyStroke.getKeyStroke(KeyEvent.getKeyText(KeyEvent.VK_F1)), "skin++");

		rootact.put("preview", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				previsualisation(text.getText());
			}
		});
		rootact.put("convert", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				convert.doClick();
			}
		});
		rootact.put("stats", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				stats.doClick();
			}
		});
		rootact.put("skin++", new AbstractAction() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				int i;
				String name;
				Iterator keyIterator = Main.skin.keySet().iterator();
				for (i = 0; i < Main.skin.size(); i++) {
					try {
						name = (String) keyIterator.next();
						if (name.equals(Configuration.getConfig("config.ini", "skin"))) {
							skinItem[((i + 1 < Main.skin.size()) ? i + 1 : 0)].doClick();
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void saveas() {
		String temp;
		JFileChooser chooser = new JFileChooser(directorie);
		chooser.setFileFilter(new MonFiltre(new String[] { "txt" }, "*.txt"));
		if (chooser.showSaveDialog(Main.getParentFrame()) == JFileChooser.APPROVE_OPTION) {
			String filename = chooser.getSelectedFile().getAbsolutePath();
			directorie = chooser.getSelectedFile().getParent();
			try {
				int reponse = JOptionPane.OK_OPTION;
				if (!filename.endsWith(".txt"))
					filename = filename + ".txt";
				File file = new File(filename);
				if (file.isFile()) {
					reponse = JOptionPane.showConfirmDialog(this,
							"This file exist!\n You want overwrite it?", "Wanning",
							JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
				}
				if (reponse == JOptionPane.OK_OPTION) {
					FileWriter filewriter = new FileWriter(filename, false);
					temp = text_formated.getText();
					temp = temp.replaceAll("\\n", Main.osenter);
					filewriter.write(temp);
					filewriter.close();
					JOptionPane.showMessageDialog(this, "Saved as: '" + filename, "Saved",
							JOptionPane.NO_OPTION);
				}
			} catch (Exception exception) {
				ExceptionAlert.createExceptionAlert(exception);
				exception.printStackTrace();
			}
		}
	}

	public void previsualisation(String text) {

		try {
			String setting = Configuration.getConfig("config.ini", "color");

			if (setting.equals("01")) {
				Main.color = Main.DARKCOLOR;
			} else if (setting.equals("02")) {
				Main.color = Main.LIGHTCOLOR;
			} else {
				Main.color = Configuration.getConfig("config.ini", "user_color" + setting)
						.split(",");
			}
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
			return;
		}

		String prevuText = "";
		boolean lightColor = false;

		text = BBCode.BBtoHTML(text);
		JFrame previsu = new JFrame("OGSConverter Preview");
		previsu.setDefaultCloseOperation(0);
		if (!Main.JWS) {
			previsu.setSize(Main.prefs.getInt("OGSConverter Preview" + "width", 600),
					Main.prefs.getInt("OGSConverter Preview" + "height", 500));
			previsu.setLocation(Main.prefs.getInt("OGSConverter Preview" + "x_location", 10),
					Main.prefs.getInt("OGSConverter Preview" + "y_location", 10));
		} else {
			previsu.setSize(700, 600);
			previsu.setLocationRelativeTo(getParent());
		}

		prevu = new JTextPane();
		prevu.setEditable(false);
		prevu.setContentType("text/html");

		prevuText = "<html><head></head><body style='background-color:#"
				+ Main.color[Main.color.length - 1] + ";'>";

		lightColor = (Main.getcolor(Main.color[Main.color.length - 1]) < Main.getcolor("555555"));

		prevuText += "<center><table border='0'><tr><td"
				+ (lightColor ? " style='color:#C0C0C0'" : "")
				+ "><div style='background-color:#" + Main.color[Main.color.length - 1]
				+ ";border:0px;'>" + text + "</div></td></tr></table></center></body></html>";
		prevu.setText(prevuText);
		prevu.setAutoscrolls(true);
		prevu.setFont(new Font("Default", Font.PLAIN, 14));

		JToolBar tb = new JToolBar();
		tb.setFloatable(false);
		tb.setLayout(new FlowLayout());

		JButton b = new JButton();
		b.setIcon(new ImageIcon(this.getClass().getResource("icons/menu_save_16.gif")));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = prevu.getText();
				JFileChooser chooser = new JFileChooser(directorie);
				chooser.setFileFilter(new MonFiltre(new String[] { "html" }, "*.html"));
				if (chooser.showSaveDialog(Main.getParentFrame()) == JFileChooser.APPROVE_OPTION) {
					String filename = chooser.getSelectedFile().getAbsolutePath();
					directorie = chooser.getSelectedFile().getParent();
					try {
						int reponse = JOptionPane.OK_OPTION;
						if (!filename.endsWith(".html"))
							filename = filename + ".html";
						File file = new File(filename);
						if (file.isFile()) {
							reponse = JOptionPane.showConfirmDialog(Main.getParentFrame(),
									"This file exist!\n You want overwrite it?", "Wanning",
									JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
						}
						if (reponse == JOptionPane.OK_OPTION) {
							FileWriter filewriter = new FileWriter(filename, false);
							text = text.replaceAll("\\n", Main.osenter);
							filewriter.write(text);
							filewriter.close();
						}
					} catch (Exception exception) {
						ExceptionAlert.createExceptionAlert(exception);
						exception.printStackTrace();
					}
				}
			}
		});
		tb.add(b);

		b = new JButton();
		b.setIcon(new ImageIcon(this.getClass().getResource("icons/menu_copy_s16.gif")));
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String newtext = prevu.getText();
				newtext = newtext.replaceAll("(?s)<\\/?(html|body|head)[^>]*?>", "").trim();
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
						new StringSelection(newtext), null);
			}
		});
		tb.add(b);

		JScrollPane scrollpane = new JScrollPane(prevu);
		scrollpane.getViewport().setBackground(
				new Color(Main.getcolor(Main.color[Main.color.length - 1])));
		previsu.getContentPane().setLayout(new BorderLayout());
		previsu.getContentPane().add(scrollpane);
		previsu.getContentPane().add(tb, BorderLayout.PAGE_START);

		previsu.setIconImage(new ImageIcon(this.getClass().getResource("images/icone.png")).getImage());

		previsu.setVisible(true);
		previsu.addWindowListener(this);
	}

	public void run() {
		clip_bord = Toolkit.getDefaultToolkit().getSystemClipboard();
		while (true) {
			if (clipbord.getState() && !run_pause) {
				try {
					if (clip_bord.getContents(this).isDataFlavorSupported(
							DataFlavor.stringFlavor))
						temp = clip_bord.getContents(this).getTransferData(
								DataFlavor.stringFlavor).toString();

					if (!temp.equals(content)) {
						content = temp;
						if (!text.getText().equals(content)) {
							text.setText(content);

							if (auto_convert.getState()) {
								convert.doClick();
							}
						}
					}
				} catch (Exception e) {
					ExceptionAlert.createExceptionAlert(e);
					e.printStackTrace();
				}
			}

			try {
				Thread.sleep(1000L);
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}
		}
	}

	public void setskin(String key, boolean setVisible) {
		String slash = File.separator;

		if (Main.JWS) {
			if (setVisible)
				this.setVisible(true);
			return;
		}

		String skin = (String) Main.skin.get(key);
		Method m, m2;

		try {
			if (skin.endsWith(".zip")) {
				URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();
				URL u = new File((String) Main.USER_URL + File.separator + "lib"
						+ File.separator + "skinlf.jar").toURI().toURL();
				m = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
				m.setAccessible(true);
				m.invoke(loader, new Object[] { u });

				Class Skin = loader.loadClass("com.l2fprod.gui.plaf.skin.Skin");
				Class SkinLookAndFeel = loader.loadClass("com.l2fprod.gui.plaf.skin.SkinLookAndFeel");

				m = SkinLookAndFeel.getDeclaredMethod("setSkin", new Class[] { Skin });
				m2 = SkinLookAndFeel.getDeclaredMethod("loadThemePack",
						new Class[] { String.class });

				m.invoke(loader, new Object[] { m2.invoke(loader,
						new Object[] { Main.USER_HOME + slash + "theme" + slash + skin }) });

				UIManager.setLookAndFeel((LookAndFeel) SkinLookAndFeel.newInstance());
			} else if (skin.equals("default")) {
				MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} else
				getSkin(key);

			if (!this.isUndecorated()) {
				if (UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
					this.dispose();
					this.setUndecorated(true);
				}
			} else if (!UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
				this.dispose();
				this.setUndecorated(false);
			}

			SwingUtilities.updateComponentTreeUI(this);
			if (this.popup_menu != null)
				SwingUtilities.updateComponentTreeUI(this.popup_menu);

		} catch (Exception e) {
			Main.Print_exception("The skin (" + key + " : " + skin + ") can't be loaded.");
			e.printStackTrace();
		}

		if (!this.isVisible() && setVisible)
			this.setVisible(true);
	}

	private void getSkin(String key) throws Exception {

		File f;
		Class c;
		URL u;
		Method m;

		f = new File((String) Main.USER_URL + File.separator + "theme" + File.separator
				+ Main.skinJAR.get(key));
		if (!f.exists()) {
			UIManager.setLookAndFeel((String) Main.skin.get(key));
			return;
		}

		u = f.toURI().toURL();

		URLClassLoader loader = (URLClassLoader) ClassLoader.getSystemClassLoader();

		m = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { u.getClass() });
		m.setAccessible(true);
		m.invoke(loader, new Object[] { u });

		if (key.startsWith("jgoodies-") && Main.skin.containsKey(key)) {
			String tmp2 = key.substring("jgoodies-".length());
			Class theme = loader.loadClass("com.jgoodies.looks.plastic.theme." + tmp2);
			Class PlasticLookAndFeel = loader.loadClass("com.jgoodies.looks.plastic.PlasticLookAndFeel");
			Class PlasticTheme = loader.loadClass("com.jgoodies.looks.plastic.PlasticTheme");
			m = PlasticLookAndFeel.getDeclaredMethod("setPlasticTheme",
					new Class[] { PlasticTheme });
			m.invoke(PlasticLookAndFeel.newInstance(), new Object[] { theme.newInstance() });
		}

		c = loader.loadClass((String) Main.skin.get(key));

		UIManager.setLookAndFeel((LookAndFeel) c.newInstance());
	}

	private Configuration config = new Configuration();

	private JTextArea text;

	private JTextArea text_formated;

	private JTabbedPane tabbedPane;

	private JButton convert;

	private JButton copier;

	private JButton effacer;

	private JButton coller;

	private JButton previsualise;

	private JButton stats;

	private JPopupMenu popup_menu;

	public JMenuItem popup_paste;

	public JMenuItem popup_cut;

	public JMenuItem popup_meff;

	public JMenuItem popup_selectAll;

	public JMenuItem popup_copy;

	public JMenuItem[] popup_convert;

	private JMenu opt_lang;

	private JMenu opt_model;

	private JRadioButtonMenuItem[] langs;

	private JRadioButtonMenuItem[] models;

	private JMenuItem sauver_sous;

	private JMenuItem quit;

	private JMenuItem mcopier;

	private JMenuItem meff;

	private JMenuItem opt_addlang;

	private JMenuItem opt_dellang;

	private JMenuItem opt_addmodel;

	private JMenuItem opt_delmodel;

	private JMenuItem about;

	private JMenuItem help;

	public JCheckBoxMenuItem MAJ;

	public JCheckBoxMenuItem clipbord;

	public JCheckBoxMenuItem auto_convert;

	public JRadioButtonMenuItem[] skinItem;

	private String directorie = null;

	private String lang;

	private String model;

	private Clipboard clip_bord;

	private String temp = "";

	private String content = "";

	private JTextPane prevu;

	private boolean run_pause = false;

	int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	private JLabel iamatt;

	private JLabel systemG;

	private JLabel uniSpeed;

	private JLabel actColor;

	private JLabel actModel;

	private JLabel universCell;

	private JComboBox speed;

	private Configs configs;

	private JComboBox fastColor;

	private JComboBox fastBBCode;

	private JComboBox fastUniverse;

	private JComboBox fastModel;

	private JPanel fastPanel = null;

	JPanel editTab = null;

	private String[] MyMdls = null;
}
