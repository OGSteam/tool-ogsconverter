package ogsconverter;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextField;

public class ConfigControl implements ActionListener, ItemListener, MouseListener, KeyListener {

	private Configs myConfigs;

	public ConfigControl(Configs myConfigs) {
		this.myConfigs = myConfigs;
	}

	public void actionPerformed(ActionEvent e) {
		Configuration config = null;
		String action = ((Component) e.getSource()).getName();

		try {
			config = new Configuration("config.ini");

			// BBCode Config
			if (action.startsWith("bbcode")) {
				if (e.getSource() instanceof JButton) {
					JFrame f = new BBManage(myConfigs);
					f.addWindowListener(new WindowAdapter() {
						public void windowClosing(WindowEvent e) {
							Main.closeFrame((JFrame) e.getSource());
						}
					});
				}
			}
			// RC Configs
			else if (action.startsWith("rc")) {
			}
			// RE Configs
			else if (action.startsWith("re")) {
			}
			// Harvest Configs
			else if (action.startsWith("hvt")) {
			}
			// Members list Configs
			else if (action.startsWith("ml")) {
			}
			// Empire Configs
			else if (action.startsWith("epr")) {
			}
			// Configuration Configs
			else if (action.startsWith("cg")) {
				if (action.equals("cgadd")) {
					String name = myConfigs.getNewConfigNameValue();
					if (name.trim().equals(""))
						return;
					String names = config.getConfig("user_color_name");
					names += ((names.trim().length() <= 0) ? "" : "|") + name.trim();
					config.setConfig("user_color_name", names);
					int l = names.split("\\|").length;
					name = "";
					for (int i = 0; i < Main.darkcolor.length; i++)
						name += "," + Main.darkcolor[i];
					config.setConfig("user_color" + l, name.substring(1));
					config.setConfig("ER_FLEET" + l,
							"200|200|1000|1000|500|500|500|500|500|500|1000|500|30|500");
					config.setConfig("ER_TECHNO" + l,
							"15|15|15|15|15|12|10|15|15|8|11|6|8|1|1");
					config.setConfig("ER_GOODS" + l, "100000|100000|100000|10000");
					config.setConfig("ER_DEFENCES" + l,
							"1000|1000|1000|500|1000|500|2|2|40|40");
					config.setConfig("ER_BUILDINGS" + l,
							"25|25|25|25|20|15|5|12|12|12|12|12|5|10|6|6|1");
					config.setConfig("user_sz_fleet" + l, 16);
					config.setConfig("user_sz_techno" + l, 16);
					config.setConfig("user_sz_rss" + l, 16);
					config.setConfig("user_sz_def" + l, 16);
					config.setConfig("user_sz_bld" + l, 16);
					Main.fen.setFastConfig();
					myConfigs.init_colorConfigList(l);
					myConfigs.setLookConfig(l);
				} else if (action.equals("cgdel")) {
					int id = myConfigs.getDelConfigSelected();
					String[] names = config.getConfig("user_color_name").split("\\|");
					if (names == null || names.length < id
							|| (names.length == 1 && names[0].trim().length() <= 0))
						return;
					String name = "";
					for (int i = 1; i < names.length + 1; i++) {
						if (i != id) {
							name += "|" + names[i - 1];
							if (i > id) {
								config.setConfig("user_color" + (i - 1),
										config.getConfig("user_color" + i));
								config.setConfig("ER_FLEET" + (i - 1),
										config.getConfig("ER_FLEET" + i));
								config.setConfig("ER_TECHNO" + (i - 1),
										config.getConfig("ER_TECHNO" + i));
								config.setConfig("ER_GOODS" + (i - 1),
										config.getConfig("ER_GOODS" + i));
								config.setConfig("ER_DEFENCES" + (i - 1),
										config.getConfig("ER_DEFENCES" + i));
								config.setConfig("ER_BUILDINGS" + (i - 1),
										config.getConfig("ER_BUILDINGS" + i));
								config.setConfig("user_sz_fleet" + (i - 1),
										config.getConfig("user_sz_fleet" + i));
								config.setConfig("user_sz_techno" + (i - 1),
										config.getConfig("user_sz_techno" + i));
								config.setConfig("user_sz_rss" + (i - 1),
										config.getConfig("user_sz_rss" + i));
								config.setConfig("user_sz_def" + (i - 1),
										config.getConfig("user_sz_def" + i));
								config.setConfig("user_sz_bld" + (i - 1),
										config.getConfig("user_sz_bld" + i));
							}
						}
					}
					config.setConfig("user_color_name",
							name.substring(((name.trim().length() <= 0) ? 0 : 1)));

					config.delConfig("user_color" + names.length);
					config.delConfig("ER_FLEET" + names.length);
					config.delConfig("ER_TECHNO" + names.length);
					config.delConfig("ER_GOODS" + names.length);
					config.delConfig("ER_DEFENCES" + names.length);
					config.delConfig("ER_BUILDINGS" + names.length);
					config.delConfig("user_sz_fleet" + names.length);
					config.delConfig("user_sz_techno" + names.length);
					config.delConfig("user_sz_rss" + names.length);
					config.delConfig("user_sz_def" + names.length);
					config.delConfig("user_sz_bld" + names.length);
					if (id == Integer.parseInt(config.getConfig("color")))
						config.setConfig("color", "01");

					myConfigs.init_colorConfigList(1);
					myConfigs.setLookConfig(1);
					Main.fen.setFastConfig();
				}
			} else if (action.startsWith("other")) {

				if (action.equals("otheradd")) {
					myConfigs.addUniversName();
				} else if (action.equals("otherdel")) {
					myConfigs.deleteUniversName();
				}

				myConfigs.updateUniversData();
				Main.fen.setFastConfig();
			}
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}

		Main.fen.setStateBar(null);
	}

	public void itemStateChanged(ItemEvent e) {
		Configuration config = null;
		String action = ((Component) e.getSource()).getName();

		try {
			config = new Configuration("config.ini");

			// BBCode Config
			if (action.startsWith("bbcode")) {
				if (e.getSource() instanceof JComboBox) {
					if (((JComboBox) e.getSource()).getItemCount() > 1)
						config.setConfig(action.substring(6),
								((JComboBox) e.getSource()).getSelectedIndex());
				}
			}
			// RC Configs
			else if (action.startsWith("rc")) {
				if (e.getSource() instanceof JCheckBox)
					config.setConfig(action.substring(2),
							((JCheckBox) e.getSource()).isSelected());
			}
			// RE Configs
			else if (action.startsWith("er")) {
				if (e.getSource() instanceof JCheckBox)
					config.setConfig(action.substring(2),
							((JCheckBox) e.getSource()).isSelected());
			}
			// Harvest Configs
			else if (action.startsWith("hvt")) {
				if (e.getSource() instanceof JCheckBox)
					config.setConfig(action.substring(3),
							((JCheckBox) e.getSource()).isSelected());
			}
			// Members list Configs
			else if (action.startsWith("ml")) {
				if (e.getSource() instanceof JCheckBox)
					config.setConfig(action.substring(2),
							((JCheckBox) e.getSource()).isSelected());
			}
			// Empire Configs
			else if (action.startsWith("epr")) {
				if (e.getSource() instanceof JCheckBox)
					config.setConfig(action.substring(3),
							((JCheckBox) e.getSource()).isSelected());
			}
			// Colors Config
			else if (action.startsWith("cg")) {
				if (e.getSource() instanceof JCheckBox) {
					if (action.startsWith("cgcolor"))
						config.setConfig("color", action.substring("cgcolor".length()));
					Main.fen.setFastConfig();
				}
			}

		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}

		Main.fen.setStateBar(null);
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
		Configuration config = null;
		String action = ((Component) e.getSource()).getName();

		try {
			config = new Configuration("config.ini");
			// BBCode Config
			if (action.startsWith("bbcode")) {
				if (e.getSource() instanceof JList) {
					myConfigs.updateBBCode(-1);
					Main.fen.setFastConfig();
				}
			} else if (action.startsWith("other")) {
				if (e.getSource() instanceof JList) {
					config.setConfig(action.substring(5),
							((JList) e.getSource()).getSelectedIndex());
					myConfigs.updateUniversData();
					Main.fen.setFastConfig();
				}
			}
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}

		Main.fen.setStateBar(null);
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		Configuration config = null;
		String action = ((Component) e.getSource()).getName();

		try {
			config = new Configuration("config.ini");
			// RC Config
			if (action.startsWith("rc")) {
				if (e.getSource() instanceof JTextField) {
					config.setConfig(action.substring(2),
							((JTextField) e.getSource()).getText());
				}
			}
			// Spy Config
			else if (action.startsWith("er")) {
				if (e.getSource() instanceof JTextField) {
					config.setConfig(action.substring(2),
							((JTextField) e.getSource()).getText());
				}
			}
			// Harvest Config
			else if (action.startsWith("hvt")) {
				if (e.getSource() instanceof JTextField) {
					config.setConfig(action.substring(3),
							((JTextField) e.getSource()).getText());
				}
			}
			// Other Config
			else if (action.startsWith("other")) {
				if (e.getSource() instanceof JTextField) {
					myConfigs.setUniversData();
				}
			}
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}

		Main.fen.setStateBar(null);
	}

	public void keyTyped(KeyEvent e) {
	}

}
