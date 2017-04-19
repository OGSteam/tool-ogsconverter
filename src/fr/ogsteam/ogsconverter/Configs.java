package fr.ogsteam.ogsconverter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

import fr.ogsteam.ogsconverter.widgets.JLimiterTextField;

public class Configs extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTabbedPane tabbedConfig = null;

	private JPanel spyCheck = null;

	private JPanel harvest = null;

	private JPanel membrePane = null;

	private JScrollPane empire = null;

	private JComboBox centerCell = null;

	private JComboBox sizeCell = null;

	private JComboBox boldCell = null;

	private JComboBox italicCell = null;

	private JComboBox underCell = null;

	private JComboBox colorCell = null;

	private JComboBox codeCell = null;

	private JComboBox quoteCell = null;

	private JComboBox fontCell = null;

	private JComboBox imageCell = null;

	private JComboBox urlCell = null;

	private JPanel bbcodeCell = null;

	private JPanel bbcodeAdmin = null;

	private JList bbConfigList = null;

	private JButton bbmanage = null;

	private JPanel rcCheckOptions = null;

	private JPanel rcPersonnalText = null;

	private JCheckBox rcDate = null;

	private JCheckBox rcAttCoord = null;

	private JCheckBox rcDefCoord = null;

	private JCheckBox rcAttName = null;

	private JCheckBox rcDefName = null;

	private JCheckBox rcTechnology = null;

	private JCheckBox rcRentability = null;

	private JCheckBox rcJustEnd = null;

	private JCheckBox rcJustMyRenta = null;

	private JCheckBox rcIAmAtt = null;

	private JCheckBox rcHarvested = null;

	private JCheckBox rcColumn = null;

	private JLabel rcAfterBattleLbl = null;

	private JLabel rcAttNameLbl = null;

	private JLabel rcAttCoordLbl = null;

	private JLabel rcDefNameLbl = null;

	private JLabel rcDefCoordLbl = null;

	private JTextField rcAfterBattleRep = null;

	private JTextField rcAttNameRep = null;

	private JTextField rcAttCoordRep = null;

	private JTextField rcDefNameRep = null;

	private JTextField rcDefCoordRep = null;

	private JCheckBox rePlanetName = null;

	private JCheckBox rePlayerName = null;

	private JCheckBox reCoordinate = null;

	private JCheckBox reGoods = null;

	private JCheckBox reFleet = null;

	private JCheckBox reBuilding = null;

	private JCheckBox reDefense = null;

	private JCheckBox reTechno = null;

	private JCheckBox hvtRate = null;

	private JPanel color = null;

	private DefaultListModel bbListModel = null; // @jve:decl-index=0:visual-constraint="606,0"

	private DefaultListModel uniListModel = null; // @jve:decl-index=0:visual-constraint="562,46"

	private JSplitPane bbcode = null;

	private JScrollPane bbcodeScroll = null;

	private JScrollPane rcScrollCheck = null;

	private JSplitPane battle = null;

	private JScrollPane rcScrollText = null;

	private JSplitPane lookConfig = null;

	private JScrollPane configColorListScroll = null;

	private JPanel configColorListPane = null;

	private JPanel configColorAdd = null;

	private JButton addConfig = null;

	private JTextField newConfigName = null;

	private JButton delConfig = null;

	private JPanel configColorList = null;

	private ConfigControl control = new ConfigControl(this);

	private JComboBox delConfigList = null;

	private JCheckBox reProd = null;

	private JCheckBox reCdr = null;

	private JCheckBox reMip = null;

	private JCheckBox rePhlg = null;

	private JSplitPane spy = null;

	private JScrollPane spyScrollG = null;

	private JScrollPane spyScrollD = null;

	private JPanel spyRepl = null;

	private JLabel rePlanetNameLbl = null;

	private JTextField rePlanetNameRepl = null;

	private JLabel reCoordinateLbl = null;

	private JTextField reCoordinateRepl = null;

	private JCheckBox reTable = null;

	private JScrollPane other = null;

	private JPanel otherPan = null;

	private JLabel system = null;

	private JFormattedTextField systemValue = null;

	private JLabel speed = null;

	private JFormattedTextField speedValue = null;

	private JPanel otherPaneContainer = null;

	private JPanel empirePane = null;

	private JCheckBox EmpCoordinate = null;

	private JCheckBox EmpField = null;

	private JCheckBox EmpBuilding = null;

	private JCheckBox EmpShip = null;

	private JCheckBox EmpDefense = null;

	private JCheckBox EmpResearch = null;

	private JCheckBox EmpProduction = null;

	private JCheckBox EmpTableFont = null;

	private JScrollPane membres = null;

	private JCheckBox mbName = null;

	private JCheckBox mbStatut = null;

	private JCheckBox mbPoints = null;

	private JCheckBox mbCoord = null;

	private JCheckBox mbAdhes = null;

	private JCheckBox mbTable = null;

	private JCheckBox hvtDate = null;

	private JCheckBox hvtCoord = null;

	private JLabel hvtCoordLbl = null;

	private JTextField hvtCoordRepl = null;

	private JSplitPane univers = null;

	private JScrollPane universCellScroll = null;

	private JPanel universCellPane = null;

	private JPanel universCellAdder = null;

	private JList universCellList = null;

	private JTextField addUniversNameValue = null;

	private JButton addUniversName = null;

	private JComboBox deleteUniversNames = null;

	private JButton deleteUnivers = null;

	private JLabel combustion = null;

	private JFormattedTextField combustionValue = null;

	private JLabel impulsion = null;

	private JFormattedTextField impulsionValue = null;

	private JLabel hyperespace = null;

	private JFormattedTextField hyperespaceValue = null;

	private JCheckBox rcConsumption = null;

	private JCheckBox reActivity = null;

	/**
	 * This is the default constructor
	 */
	public Configs() {
		super();
		initialize();
		init_text();
		init_listener();
		setLookConfig(1);
		init_colorConfigList(1);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(545, 353);
		this.setLayout(new GridBagLayout());
		this.add(getTabbedConfig(), gridBagConstraints);
	}

	/**
	 * This method initializes tabbedConfig
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getTabbedConfig() {
		if (tabbedConfig == null) {
			tabbedConfig = new JTabbedPane();
			tabbedConfig.addTab("BBCode", null, getBbcode(), null);
			tabbedConfig.addTab("Bataille", null, getBattle(), null);
			tabbedConfig.addTab("Espionnage", null, getSpy(), null);
			tabbedConfig.addTab("Recyclage", null, getHarvest(), null);
			tabbedConfig.addTab("Liste des membres", null, getMembres(), null);
			tabbedConfig.addTab("Empire", null, getEmpire(), null);
			tabbedConfig.addTab("Configuration", null, getColor(), null);
			tabbedConfig.addTab("Others", null, getUnivers(), null);
			tabbedConfig.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}
		return tabbedConfig;
	}

	/**
	 * This method initializes spyCheck
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSpyCheck() {
		if (spyCheck == null) {
			GridBagConstraints gridBagConstraints104 = new GridBagConstraints();
			gridBagConstraints104.gridx = 1;
			gridBagConstraints104.fill = GridBagConstraints.BOTH;
			gridBagConstraints104.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints104.anchor = GridBagConstraints.WEST;
			gridBagConstraints104.gridy = 4;
			GridBagConstraints gridBagConstraints69 = new GridBagConstraints();
			gridBagConstraints69.gridx = 1;
			gridBagConstraints69.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints69.fill = GridBagConstraints.BOTH;
			gridBagConstraints69.anchor = GridBagConstraints.WEST;
			gridBagConstraints69.gridy = 7;
			GridBagConstraints gridBagConstraints64 = new GridBagConstraints();
			gridBagConstraints64.gridx = 1;
			gridBagConstraints64.anchor = GridBagConstraints.WEST;
			gridBagConstraints64.fill = GridBagConstraints.BOTH;
			gridBagConstraints64.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints64.gridy = 3;
			GridBagConstraints gridBagConstraints63 = new GridBagConstraints();
			gridBagConstraints63.gridx = 1;
			gridBagConstraints63.anchor = GridBagConstraints.WEST;
			gridBagConstraints63.fill = GridBagConstraints.BOTH;
			gridBagConstraints63.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints63.gridy = 2;
			GridBagConstraints gridBagConstraints62 = new GridBagConstraints();
			gridBagConstraints62.gridx = 1;
			gridBagConstraints62.fill = GridBagConstraints.BOTH;
			gridBagConstraints62.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints62.anchor = GridBagConstraints.WEST;
			gridBagConstraints62.gridy = 1;
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.gridx = 1;
			gridBagConstraints61.fill = GridBagConstraints.BOTH;
			gridBagConstraints61.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints61.anchor = GridBagConstraints.WEST;
			gridBagConstraints61.gridy = 0;
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.gridx = 0;
			gridBagConstraints30.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints30.fill = GridBagConstraints.BOTH;
			gridBagConstraints30.gridy = 7;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints29.fill = GridBagConstraints.BOTH;
			gridBagConstraints29.gridy = 6;
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 0;
			gridBagConstraints28.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints28.fill = GridBagConstraints.BOTH;
			gridBagConstraints28.gridy = 5;
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 0;
			gridBagConstraints27.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints27.fill = GridBagConstraints.BOTH;
			gridBagConstraints27.gridy = 4;
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 0;
			gridBagConstraints26.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints26.fill = GridBagConstraints.BOTH;
			gridBagConstraints26.gridy = 3;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 0;
			gridBagConstraints25.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints25.fill = GridBagConstraints.BOTH;
			gridBagConstraints25.gridy = 2;
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 0;
			gridBagConstraints24.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints24.fill = GridBagConstraints.BOTH;
			gridBagConstraints24.gridy = 0;
			GridBagConstraints gridBagConstraints105 = new GridBagConstraints();
			gridBagConstraints105.gridx = 0;
			gridBagConstraints105.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints105.fill = GridBagConstraints.BOTH;
			gridBagConstraints105.gridy = 1;
			spyCheck = new JPanel();
			spyCheck.setLayout(new GridBagLayout());
			spyCheck.add(getRePlanetName(), gridBagConstraints24);
			spyCheck.add(getRePlayerName(), gridBagConstraints105);
			spyCheck.add(getReCoordinate(), gridBagConstraints25);
			spyCheck.add(getReGoods(), gridBagConstraints26);
			spyCheck.add(getReFleet(), gridBagConstraints27);
			spyCheck.add(getReBuilding(), gridBagConstraints28);
			spyCheck.add(getReDefense(), gridBagConstraints29);
			spyCheck.add(getReTechno(), gridBagConstraints30);
			spyCheck.add(getReProd(), gridBagConstraints61);
			spyCheck.add(getReCdr(), gridBagConstraints62);
			spyCheck.add(getReMip(), gridBagConstraints63);
			spyCheck.add(getRePhlg(), gridBagConstraints64);
			spyCheck.add(getReTable(), gridBagConstraints69);
			spyCheck.add(getReActivity(), gridBagConstraints104);
		}
		return spyCheck;
	}

	/**
	 * This method initializes harvest
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getHarvest() {
		if (harvest == null) {
			GridBagConstraints gridBagConstraints94 = new GridBagConstraints();
			gridBagConstraints94.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints94.gridy = 3;
			gridBagConstraints94.weightx = 1.0;
			gridBagConstraints94.anchor = GridBagConstraints.WEST;
			gridBagConstraints94.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints94.ipadx = 120;
			gridBagConstraints94.gridx = 1;
			GridBagConstraints gridBagConstraints92 = new GridBagConstraints();
			gridBagConstraints92.gridx = 0;
			gridBagConstraints92.insets = new Insets(2, 120, 2, 2);
			gridBagConstraints92.gridy = 3;
			hvtCoordLbl = new JLabel();
			hvtCoordLbl.setText("JLabel");
			GridBagConstraints gridBagConstraints90 = new GridBagConstraints();
			gridBagConstraints90.gridx = 0;
			gridBagConstraints90.gridwidth = 2;
			gridBagConstraints90.insets = new Insets(0, 120, 0, 2);
			gridBagConstraints90.anchor = GridBagConstraints.WEST;
			gridBagConstraints90.gridy = 2;
			GridBagConstraints gridBagConstraints89 = new GridBagConstraints();
			gridBagConstraints89.gridx = 0;
			gridBagConstraints89.gridwidth = 2;
			gridBagConstraints89.insets = new Insets(0, 120, 0, 2);
			gridBagConstraints89.anchor = GridBagConstraints.WEST;
			gridBagConstraints89.gridy = 1;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new Insets(2, 120, 2, 2);
			gridBagConstraints31.ipadx = 0;
			gridBagConstraints31.ipady = 0;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.gridy = 0;
			harvest = new JPanel();
			harvest.setLayout(new GridBagLayout());
			harvest.add(getHvtRate(), gridBagConstraints31);
			harvest.add(getHvtDate(), gridBagConstraints89);
			harvest.add(getHvtCoord(), gridBagConstraints90);
			harvest.add(hvtCoordLbl, gridBagConstraints92);
			harvest.add(getHvtCoordRepl(), gridBagConstraints94);
		}
		return harvest;
	}

	/**
	 * This method initializes membrePane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMembrePane() {
		if (membrePane == null) {
			GridBagConstraints gridBagConstraints88 = new GridBagConstraints();
			gridBagConstraints88.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints88.anchor = GridBagConstraints.WEST;
			gridBagConstraints88.fill = GridBagConstraints.VERTICAL;
			GridBagConstraints gridBagConstraints87 = new GridBagConstraints();
			gridBagConstraints87.gridx = 1;
			gridBagConstraints87.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints87.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints87.anchor = GridBagConstraints.WEST;
			gridBagConstraints87.gridy = 2;
			GridBagConstraints gridBagConstraints86 = new GridBagConstraints();
			gridBagConstraints86.gridx = 1;
			gridBagConstraints86.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints86.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints86.anchor = GridBagConstraints.WEST;
			gridBagConstraints86.gridy = 1;
			GridBagConstraints gridBagConstraints85 = new GridBagConstraints();
			gridBagConstraints85.gridx = 1;
			gridBagConstraints85.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints85.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints85.anchor = GridBagConstraints.WEST;
			gridBagConstraints85.gridy = 0;
			GridBagConstraints gridBagConstraints84 = new GridBagConstraints();
			gridBagConstraints84.gridx = 0;
			gridBagConstraints84.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints84.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints84.anchor = GridBagConstraints.WEST;
			gridBagConstraints84.gridy = 2;
			GridBagConstraints gridBagConstraints82 = new GridBagConstraints();
			gridBagConstraints82.gridx = 0;
			gridBagConstraints82.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints82.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints82.anchor = GridBagConstraints.WEST;
			gridBagConstraints82.gridy = 1;
			membrePane = new JPanel();
			membrePane.setLayout(new GridBagLayout());
			membrePane.add(getMbName(), gridBagConstraints88);
			membrePane.add(getMbStatut(), gridBagConstraints82);
			membrePane.add(getMbPoints(), gridBagConstraints84);
			membrePane.add(getMbCoord(), gridBagConstraints85);
			membrePane.add(getMbAdhes(), gridBagConstraints86);
			membrePane.add(getMbTable(), gridBagConstraints87);
		}
		return membrePane;
	}

	/**
	 * This method initializes empire
	 * 
	 * @return javax.swing.JPanel
	 */
	private JScrollPane getEmpire() {
		if (empire == null) {
			empire = new JScrollPane();
			empire.setViewportView(getEmpirePane());
		}
		return empire;
	}

	/**
	 * This method initializes centerCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCenterCell() {
		if (centerCell == null) {
			centerCell = new JComboBox();
		}
		return centerCell;
	}

	/**
	 * This method initializes sizeCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getSizeCell() {
		if (sizeCell == null) {
			sizeCell = new JComboBox();
		}
		return sizeCell;
	}

	/**
	 * This method initializes boldCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getBoldCell() {
		if (boldCell == null) {
			boldCell = new JComboBox();
		}
		return boldCell;
	}

	/**
	 * This method initializes italicCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getItalicCell() {
		if (italicCell == null) {
			italicCell = new JComboBox();
		}
		return italicCell;
	}

	/**
	 * This method initializes underCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getUnderCell() {
		if (underCell == null) {
			underCell = new JComboBox();
		}
		return underCell;
	}

	/**
	 * This method initializes colorCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getColorCell() {
		if (colorCell == null) {
			colorCell = new JComboBox();
		}
		return colorCell;
	}

	/**
	 * This method initializes codeCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getCodeCell() {
		if (codeCell == null) {
			codeCell = new JComboBox();
		}
		return codeCell;
	}

	/**
	 * This method initializes quoteCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getQuoteCell() {
		if (quoteCell == null) {
			quoteCell = new JComboBox();
		}
		return quoteCell;
	}

	/**
	 * This method initializes fontCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getFontCell() {
		if (fontCell == null) {
			fontCell = new JComboBox();
		}
		return fontCell;
	}

	/**
	 * This method initializes imageCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getImageCell() {
		if (imageCell == null) {
			imageCell = new JComboBox();
		}
		return imageCell;
	}

	/**
	 * This method initializes urlCell
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getUrlCell() {
		if (urlCell == null) {
			urlCell = new JComboBox();
		}
		return urlCell;
	}

	/**
	 * This method initializes bbcodeCell
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBbcodeCell() {
		if (bbcodeCell == null) {
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.fill = GridBagConstraints.BOTH;
			gridBagConstraints42.gridy = 10;
			gridBagConstraints42.weightx = 1.0;
			gridBagConstraints42.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints42.gridx = 0;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.fill = GridBagConstraints.BOTH;
			gridBagConstraints41.gridy = 9;
			gridBagConstraints41.weightx = 1.0;
			gridBagConstraints41.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints41.gridx = 0;
			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.fill = GridBagConstraints.BOTH;
			gridBagConstraints40.gridy = 8;
			gridBagConstraints40.weightx = 1.0;
			gridBagConstraints40.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints40.gridx = 0;
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.fill = GridBagConstraints.BOTH;
			gridBagConstraints39.gridy = 7;
			gridBagConstraints39.weightx = 1.0;
			gridBagConstraints39.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints39.gridx = 0;
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.fill = GridBagConstraints.BOTH;
			gridBagConstraints38.gridy = 6;
			gridBagConstraints38.weightx = 1.0;
			gridBagConstraints38.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints38.gridx = 0;
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.fill = GridBagConstraints.BOTH;
			gridBagConstraints37.gridy = 5;
			gridBagConstraints37.weightx = 1.0;
			gridBagConstraints37.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints37.gridx = 0;
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.fill = GridBagConstraints.BOTH;
			gridBagConstraints36.gridy = 4;
			gridBagConstraints36.weightx = 1.0;
			gridBagConstraints36.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints36.gridx = 0;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.fill = GridBagConstraints.BOTH;
			gridBagConstraints35.gridy = 3;
			gridBagConstraints35.weightx = 1.0;
			gridBagConstraints35.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints35.gridx = 0;
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.fill = GridBagConstraints.BOTH;
			gridBagConstraints34.gridy = 2;
			gridBagConstraints34.weightx = 1.0;
			gridBagConstraints34.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints34.gridx = 0;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.fill = GridBagConstraints.BOTH;
			gridBagConstraints33.gridy = 1;
			gridBagConstraints33.weightx = 1.0;
			gridBagConstraints33.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints33.gridx = 0;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.fill = GridBagConstraints.BOTH;
			gridBagConstraints32.gridy = 0;
			gridBagConstraints32.weightx = 1.0;
			gridBagConstraints32.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints32.gridx = 0;
			bbcodeCell = new JPanel();
			bbcodeCell.setLayout(new GridBagLayout());
			bbcodeCell.add(getCenterCell(), gridBagConstraints32);
			bbcodeCell.add(getSizeCell(), gridBagConstraints33);
			bbcodeCell.add(getBoldCell(), gridBagConstraints34);
			bbcodeCell.add(getItalicCell(), gridBagConstraints35);
			bbcodeCell.add(getUnderCell(), gridBagConstraints36);
			bbcodeCell.add(getColorCell(), gridBagConstraints37);
			bbcodeCell.add(getCodeCell(), gridBagConstraints38);
			bbcodeCell.add(getQuoteCell(), gridBagConstraints39);
			bbcodeCell.add(getImageCell(), gridBagConstraints40);
			bbcodeCell.add(getFontCell(), gridBagConstraints41);
			bbcodeCell.add(getUrlCell(), gridBagConstraints42);
		}
		return bbcodeCell;
	}

	/**
	 * This method initializes bbcodeAdmin
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBbcodeAdmin() {
		if (bbcodeAdmin == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.gridy = 1;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.BOTH;
			gridBagConstraints13.weighty = 1.0;
			gridBagConstraints13.weightx = 1.0;
			bbcodeAdmin = new JPanel();
			bbcodeAdmin.setLayout(new GridBagLayout());
			bbcodeAdmin.add(getBbConfigList(), gridBagConstraints13);
			bbcodeAdmin.add(getBbmanage(), gridBagConstraints14);
		}
		return bbcodeAdmin;
	}

	/**
	 * This method initializes bbConfigList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getBbConfigList() {
		if (bbConfigList == null) {
			bbConfigList = new JList();
			bbConfigList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return bbConfigList;
	}

	/**
	 * This method initializes bbmanage
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBbmanage() {
		if (bbmanage == null) {
			bbmanage = new JButton("BBManager");
		}
		return bbmanage;
	}

	/**
	 * This method initializes rcCheckOptions
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRcCheckOptions() {
		if (rcCheckOptions == null) {
			GridBagConstraints gridBagConstraints103 = new GridBagConstraints();
			gridBagConstraints103.gridx = 0;
			gridBagConstraints103.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints103.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints103.gridy = 12;
			GridBagConstraints gridBagConstraints55 = new GridBagConstraints();
			gridBagConstraints55.gridx = 0;
			gridBagConstraints55.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints55.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints55.gridy = 11;
			GridBagConstraints gridBagConstraints54 = new GridBagConstraints();
			gridBagConstraints54.gridx = 0;
			gridBagConstraints54.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints54.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints54.gridy = 10;
			GridBagConstraints gridBagConstraints53 = new GridBagConstraints();
			gridBagConstraints53.gridx = 0;
			gridBagConstraints53.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints53.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints53.gridy = 9;
			GridBagConstraints gridBagConstraints52 = new GridBagConstraints();
			gridBagConstraints52.gridx = 0;
			gridBagConstraints52.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints52.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints52.gridy = 8;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.gridx = 0;
			gridBagConstraints51.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints51.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints51.gridy = 7;
			GridBagConstraints gridBagConstraints50 = new GridBagConstraints();
			gridBagConstraints50.gridx = 0;
			gridBagConstraints50.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints50.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints50.gridy = 6;
			GridBagConstraints gridBagConstraints49 = new GridBagConstraints();
			gridBagConstraints49.gridx = 0;
			gridBagConstraints49.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints49.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints49.gridy = 5;
			GridBagConstraints gridBagConstraints48 = new GridBagConstraints();
			gridBagConstraints48.gridx = 0;
			gridBagConstraints48.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints48.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints48.gridy = 4;
			GridBagConstraints gridBagConstraints47 = new GridBagConstraints();
			gridBagConstraints47.gridx = 0;
			gridBagConstraints47.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints47.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints47.gridy = 3;
			GridBagConstraints gridBagConstraints46 = new GridBagConstraints();
			gridBagConstraints46.gridx = 0;
			gridBagConstraints46.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints46.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints46.gridy = 2;
			GridBagConstraints gridBagConstraints45 = new GridBagConstraints();
			gridBagConstraints45.gridx = 0;
			gridBagConstraints45.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints45.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints45.gridy = 1;
			GridBagConstraints gridBagConstraints44 = new GridBagConstraints();
			gridBagConstraints44.gridx = 0;
			gridBagConstraints44.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints44.insets = new Insets(1, 2, 1, 2);
			gridBagConstraints44.gridy = 0;
			rcCheckOptions = new JPanel();
			rcCheckOptions.setLayout(new GridBagLayout());
			rcCheckOptions.setName("rcCheckOptions");
			rcCheckOptions.add(getRcDate(), gridBagConstraints44);
			rcCheckOptions.add(getRcAttCoord(), gridBagConstraints45);
			rcCheckOptions.add(getRcDefCoord(), gridBagConstraints46);
			rcCheckOptions.add(getRcAttName(), gridBagConstraints47);
			rcCheckOptions.add(getRcDefName(), gridBagConstraints48);
			rcCheckOptions.add(getRcTechnology(), gridBagConstraints49);
			rcCheckOptions.add(getRcRentability(), gridBagConstraints50);
			rcCheckOptions.add(getRcJustMyRenta(), gridBagConstraints51);
			rcCheckOptions.add(getRcJustEnd(), gridBagConstraints52);
			rcCheckOptions.add(getRcIAmAtt(), gridBagConstraints53);
			rcCheckOptions.add(getRcHarvested(), gridBagConstraints54);
			rcCheckOptions.add(getRcColumn(), gridBagConstraints55);
			rcCheckOptions.add(getRcConsumption(), gridBagConstraints103);
		}
		return rcCheckOptions;
	}

	/**
	 * This method initializes rcPersonnalText
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRcPersonnalText() {
		if (rcPersonnalText == null) {
			GridBagConstraints gridBagConstraints43 = new GridBagConstraints();
			gridBagConstraints43.fill = GridBagConstraints.BOTH;
			gridBagConstraints43.gridy = -1;
			gridBagConstraints43.weightx = 1.0;
			gridBagConstraints43.weighty = 1.0;
			gridBagConstraints43.gridx = -1;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.gridy = 4;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints23.anchor = GridBagConstraints.WEST;
			gridBagConstraints23.ipady = 3;
			gridBagConstraints23.ipadx = 3;
			gridBagConstraints23.gridx = 1;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.gridy = 3;
			gridBagConstraints22.weightx = 1.0;
			gridBagConstraints22.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.ipady = 3;
			gridBagConstraints22.ipadx = 3;
			gridBagConstraints22.gridx = 1;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints21.gridy = 2;
			gridBagConstraints21.weightx = 1.0;
			gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.ipady = 3;
			gridBagConstraints21.ipadx = 3;
			gridBagConstraints21.gridx = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints20.gridy = 1;
			gridBagConstraints20.weightx = 1.0;
			gridBagConstraints20.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.ipady = 3;
			gridBagConstraints20.ipadx = 3;
			gridBagConstraints20.gridx = 1;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints19.gridy = 0;
			gridBagConstraints19.weightx = 1.0;
			gridBagConstraints19.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.ipady = 3;
			gridBagConstraints19.ipadx = 3;
			gridBagConstraints19.gridx = 1;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.fill = GridBagConstraints.NONE;
			gridBagConstraints18.insets = new Insets(2, 20, 2, 2);
			gridBagConstraints18.anchor = GridBagConstraints.EAST;
			gridBagConstraints18.ipady = 3;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.fill = GridBagConstraints.NONE;
			gridBagConstraints17.ipady = 3;
			gridBagConstraints17.insets = new Insets(2, 20, 2, 2);
			gridBagConstraints17.anchor = GridBagConstraints.EAST;
			gridBagConstraints17.gridy = 4;
			rcDefCoordLbl = new JLabel();
			rcDefCoordLbl.setText("JLabel");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.fill = GridBagConstraints.NONE;
			gridBagConstraints16.ipady = 3;
			gridBagConstraints16.insets = new Insets(2, 20, 2, 2);
			gridBagConstraints16.anchor = GridBagConstraints.EAST;
			gridBagConstraints16.gridy = 3;
			rcDefNameLbl = new JLabel();
			rcDefNameLbl.setText("JLabel");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 0;
			gridBagConstraints15.fill = GridBagConstraints.NONE;
			gridBagConstraints15.ipady = 3;
			gridBagConstraints15.insets = new Insets(2, 20, 2, 2);
			gridBagConstraints15.anchor = GridBagConstraints.EAST;
			gridBagConstraints15.gridy = 2;
			rcAttCoordLbl = new JLabel();
			rcAttCoordLbl.setText("JLabel");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.fill = GridBagConstraints.NONE;
			gridBagConstraints12.ipady = 3;
			gridBagConstraints12.insets = new Insets(2, 20, 2, 2);
			gridBagConstraints12.anchor = GridBagConstraints.EAST;
			gridBagConstraints12.gridy = 1;
			rcAttNameLbl = new JLabel();
			rcAttNameLbl.setText("JLabel");
			rcAfterBattleLbl = new JLabel();
			rcAfterBattleLbl.setText("JLabel");
			rcPersonnalText = new JPanel();
			rcPersonnalText.setLayout(new GridBagLayout());
			rcPersonnalText.setName("rcPersonnalText");
			rcPersonnalText.add(rcAfterBattleLbl, gridBagConstraints18);
			rcPersonnalText.add(rcAttNameLbl, gridBagConstraints12);
			rcPersonnalText.add(rcAttCoordLbl, gridBagConstraints15);
			rcPersonnalText.add(rcDefNameLbl, gridBagConstraints16);
			rcPersonnalText.add(rcDefCoordLbl, gridBagConstraints17);
			rcPersonnalText.add(getRcAfterBattleRep(), gridBagConstraints19);
			rcPersonnalText.add(getRcAttNameRep(), gridBagConstraints20);
			rcPersonnalText.add(getRcAttCoordRep(), gridBagConstraints21);
			rcPersonnalText.add(getRcDefNameRep(), gridBagConstraints22);
			rcPersonnalText.add(getRcDefCoordRep(), gridBagConstraints23);
		}
		return rcPersonnalText;
	}

	/**
	 * This method initializes rcDate
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcDate() {
		if (rcDate == null) {
			rcDate = new JCheckBox();
		}
		return rcDate;
	}

	/**
	 * This method initializes rcAttCoord
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcAttCoord() {
		if (rcAttCoord == null) {
			rcAttCoord = new JCheckBox();
		}
		return rcAttCoord;
	}

	/**
	 * This method initializes rcDefCoord
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcDefCoord() {
		if (rcDefCoord == null) {
			rcDefCoord = new JCheckBox();
		}
		return rcDefCoord;
	}

	/**
	 * This method initializes rcAttName
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcAttName() {
		if (rcAttName == null) {
			rcAttName = new JCheckBox();
		}
		return rcAttName;
	}

	/**
	 * This method initializes rcDefName
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcDefName() {
		if (rcDefName == null) {
			rcDefName = new JCheckBox();
		}
		return rcDefName;
	}

	/**
	 * This method initializes rcTechnology
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcTechnology() {
		if (rcTechnology == null) {
			rcTechnology = new JCheckBox();
		}
		return rcTechnology;
	}

	/**
	 * This method initializes rcRentability
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcRentability() {
		if (rcRentability == null) {
			rcRentability = new JCheckBox();
		}
		return rcRentability;
	}

	/**
	 * This method initializes rcJustEnd
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcJustEnd() {
		if (rcJustEnd == null) {
			rcJustEnd = new JCheckBox();
		}
		return rcJustEnd;
	}

	/**
	 * This method initializes rcJustMyRenta
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcJustMyRenta() {
		if (rcJustMyRenta == null) {
			rcJustMyRenta = new JCheckBox();
		}
		return rcJustMyRenta;
	}

	/**
	 * This method initializes rcIAmAtt
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcIAmAtt() {
		if (rcIAmAtt == null) {
			rcIAmAtt = new JCheckBox();
		}
		return rcIAmAtt;
	}

	/**
	 * This method initializes rcHarvested
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcHarvested() {
		if (rcHarvested == null) {
			rcHarvested = new JCheckBox();
		}
		return rcHarvested;
	}

	/**
	 * This method initializes rcColumn
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcColumn() {
		if (rcColumn == null) {
			rcColumn = new JCheckBox();
		}
		return rcColumn;
	}

	/**
	 * This method initializes rcAfterBattleRep
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRcAfterBattleRep() {
		if (rcAfterBattleRep == null) {
			rcAfterBattleRep = new JTextField();
		}
		return rcAfterBattleRep;
	}

	/**
	 * This method initializes rcAttNameRep
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRcAttNameRep() {
		if (rcAttNameRep == null) {
			rcAttNameRep = new JTextField();
		}
		return rcAttNameRep;
	}

	/**
	 * This method initializes rcAttCoordRep
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRcAttCoordRep() {
		if (rcAttCoordRep == null) {
			rcAttCoordRep = new JTextField();
		}
		return rcAttCoordRep;
	}

	/**
	 * This method initializes rcDefNameRep
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRcDefNameRep() {
		if (rcDefNameRep == null) {
			rcDefNameRep = new JTextField();
		}
		return rcDefNameRep;
	}

	/**
	 * This method initializes rcDefCoordRep
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRcDefCoordRep() {
		if (rcDefCoordRep == null) {
			rcDefCoordRep = new JTextField();
		}
		return rcDefCoordRep;
	}

	/**
	 * This method initializes rePlanetName
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRePlanetName() {
		if (rePlanetName == null) {
			rePlanetName = new JCheckBox();
		}
		return rePlanetName;
	}

	/**
	 * This method initializes reCoordinate
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReCoordinate() {
		if (reCoordinate == null) {
			reCoordinate = new JCheckBox();
		}
		return reCoordinate;
	}

	/**
	 * This method initializes reGoods
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReGoods() {
		if (reGoods == null) {
			reGoods = new JCheckBox();
		}
		return reGoods;
	}

	/**
	 * This method initializes reFleet
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReFleet() {
		if (reFleet == null) {
			reFleet = new JCheckBox();
		}
		return reFleet;
	}

	/**
	 * This method initializes reBuilding
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReBuilding() {
		if (reBuilding == null) {
			reBuilding = new JCheckBox();
		}
		return reBuilding;
	}

	/**
	 * This method initializes reDefense
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReDefense() {
		if (reDefense == null) {
			reDefense = new JCheckBox();
		}
		return reDefense;
	}

	/**
	 * This method initializes reTechno
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReTechno() {
		if (reTechno == null) {
			reTechno = new JCheckBox();
		}
		return reTechno;
	}

	/**
	 * This method initializes hvtRate
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getHvtRate() {
		if (hvtRate == null) {
			hvtRate = new JCheckBox();
		}
		return hvtRate;
	}

	/**
	 * This method initializes color
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getColor() {
		if (color == null) {
			GridBagConstraints gridBagConstraints56 = new GridBagConstraints();
			gridBagConstraints56.fill = GridBagConstraints.BOTH;
			gridBagConstraints56.weighty = 1.0;
			gridBagConstraints56.weightx = 1.0;
			color = new JPanel();
			color.setLayout(new GridBagLayout());
			color.add(getLookConfig(), gridBagConstraints56);
		}
		return color;
	}

	public void init_text() {
		String langFile = null;
		String[] tmp = null;
		int sel = 0;
		int i;

		try {
			Configuration configC = new Configuration("config.ini");
			langFile = configC.getConfig("active_language");
			langFile = "lang_" + langFile + ".ini";

			Configuration configL = new Configuration(langFile);

			// Tab Titles
			this.getTabbedConfig().setTitleAt(1, configL.getConfig("choice_CR"));
			this.getTabbedConfig().setTitleAt(2, configL.getConfig("choice_ER"));
			this.getTabbedConfig().setTitleAt(3, configL.getConfig("choice_Recy"));
			this.getTabbedConfig().setTitleAt(4, configL.getConfig("choice_membersList"));
			this.getTabbedConfig().setTitleAt(5, configL.getConfig("choice_empire"));
			this.getTabbedConfig().setTitleAt(6, configL.getConfig("menu_opt_setting"));

			// BBCode
			getCenterCell().removeAllItems();
			tmp = ((Configuration.getConfig("config.ini", "BBCode_center").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getCenterCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("center"));
			if (i <= (getCenterCell().getItemCount() - 1))
				getCenterCell().setSelectedIndex(i);
			getCenterCell().setName("bbcodecenter");

			getSizeCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_size").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getSizeCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("size"));
			if (i <= (getSizeCell().getItemCount() - 1))
				getSizeCell().setSelectedIndex(i);
			getSizeCell().setName("bbcodesize");

			getBoldCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_bold").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getBoldCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("bold"));
			if (i <= (getBoldCell().getItemCount() - 1))
				getBoldCell().setSelectedIndex(i);
			getBoldCell().setName("bbcodebold");

			getItalicCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_ita").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getItalicCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("ita"));
			if (i <= (getItalicCell().getItemCount() - 1))
				getItalicCell().setSelectedIndex(i);
			getItalicCell().setName("bbcodeita");

			getUnderCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_under").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getUnderCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("under"));
			if (i <= (getUnderCell().getItemCount() - 1))
				getUnderCell().setSelectedIndex(i);
			getUnderCell().setName("bbcodeunder");

			getColorCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_bcolor").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getColorCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("bcolor"));
			if (i <= (getColorCell().getItemCount() - 1))
				getColorCell().setSelectedIndex(i);
			getColorCell().setName("bbcodebcolor");

			getCodeCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_code").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getCodeCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("code"));
			if (i <= (getCodeCell().getItemCount() - 1))
				getCodeCell().setSelectedIndex(i);
			getCodeCell().setName("bbcodecode");

			getQuoteCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_quote").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getQuoteCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("quote"));
			if (i <= (getQuoteCell().getItemCount() - 1))
				getQuoteCell().setSelectedIndex(i);
			getQuoteCell().setName("bbcodequote");

			getFontCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_font").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getFontCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("font"));
			if (i <= (getFontCell().getItemCount() - 1))
				getFontCell().setSelectedIndex(i);
			getFontCell().setName("bbcodefont");

			getImageCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_img").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getImageCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("img"));
			if (i <= (getImageCell().getItemCount() - 1))
				getImageCell().setSelectedIndex(i);
			getImageCell().setName("bbcodeimg");

			getUrlCell().removeAllItems();
			tmp = ((configC.getConfig("BBCode_url").split("<<\\|>>"))[0]).split("<\\|>");
			for (i = 0; i < tmp.length; i++)
				getUrlCell().addItem(tmp[i]);
			i = Integer.parseInt(configC.getConfig("url"));
			if (i <= (getUrlCell().getItemCount() - 1))
				getUrlCell().setSelectedIndex(i);
			getUrlCell().setName("bbcodeurl");

			getBbListModel().removeAllElements();
			tmp = configC.getConfig("BBname").split("\\|");
			for (i = 0; i < tmp.length; i++)
				getBbListModel().addElement(tmp[i]);
			getBbConfigList().setModel(getBbListModel());
			getBbConfigList().setName("bbcode");

			getBbmanage().setName("bbcodeManage");
			getBbmanage().setIcon(
					new ImageIcon(this.getClass().getResource("images/bbmanage.png")));

			// CR Tab
			getRcDate().setText(configL.getConfig("menu_opt_rc_date"));
			if (configC.getConfig("RC_date").equals("1"))
				getRcDate().setSelected(true);
			getRcDate().setName("rcRC_date");

			getRcAttCoord().setText(configL.getConfig("menu_opt_attcoordform"));
			if (configC.getConfig("attcoord").equals("1"))
				getRcAttCoord().setSelected(true);
			getRcAttCoord().setName("rcattcoord");

			getRcDefCoord().setText(configL.getConfig("menu_opt_defcoordform"));
			if (configC.getConfig("defcoord").equals("1"))
				getRcDefCoord().setSelected(true);
			getRcDefCoord().setName("rcdefcoord");

			getRcAttName().setText(configL.getConfig("menu_opt_attnameform"));
			if (configC.getConfig("attname").equals("1"))
				getRcAttName().setSelected(true);
			getRcAttName().setName("rcattname");

			getRcDefName().setText(configL.getConfig("menu_opt_defnameform"));
			if (configC.getConfig("defname").equals("1"))
				getRcDefName().setSelected(true);
			getRcDefName().setName("rcdefname");

			getRcTechnology().setText(configL.getConfig("menu_opt_technoform"));
			if (configC.getConfig("Techno").equals("1"))
				getRcTechnology().setSelected(true);
			getRcTechnology().setName("rcTechno");

			getRcRentability().setText(configL.getConfig("menu_opt_rateform"));
			if (configC.getConfig("cr_taux").equals("1"))
				getRcRentability().setSelected(true);
			getRcRentability().setName("rccr_taux");

			getRcConsumption().setText(configL.getConfig("consumption"));
			if (configC.getConfig("cr_consumption").equals("1"))
				getRcConsumption().setSelected(true);
			getRcConsumption().setName("rccr_consumption");

			getRcJustMyRenta().setText(configL.getConfig("menu_opt_myrenta"));
			if (configC.getConfig("myrenta").equals("1"))
				getRcJustMyRenta().setSelected(true);
			getRcJustMyRenta().setName("rcmyrenta");

			getRcJustEnd().setText(configL.getConfig("menu_opt_endform"));
			if (configC.getConfig("CR_end").equals("1"))
				getRcJustEnd().setSelected(true);
			getRcJustEnd().setName("rcCR_end");

			getRcIAmAtt().setText(configL.getConfig("menu_opt_iamatt"));
			if (configC.getConfig("iamatt").equals("1"))
				getRcIAmAtt().setSelected(true);
			getRcIAmAtt().setName("rciamatt");

			getRcHarvested().setText(configL.getConfig("menu_opt_cr_harvested"));
			if (configC.getConfig("CR_harvested").equals("1"))
				getRcHarvested().setSelected(true);
			getRcHarvested().setName("rcCR_harvested");

			getRcColumn().setText(configL.getConfig("menu_opt_columnform"));
			if (configC.getConfig("column").equals("1"))
				getRcColumn().setSelected(true);
			getRcColumn().setName("rccolumn");

			rcAfterBattleLbl.setText("After battle:");
			rcAttNameLbl.setText("Att name:");
			rcDefNameLbl.setText("Def name:");
			rcAttCoordLbl.setText("Att coord:");
			rcDefCoordLbl.setText("Def coord:");

			getRcAfterBattleRep().setText(configC.getConfig("afterbattle"));
			getRcAfterBattleRep().setName("rcafterbattle");
			getRcAttNameRep().setText(configC.getConfig("att_rpl_name"));
			getRcAttNameRep().setName("rcatt_rpl_name");
			getRcDefNameRep().setText(configC.getConfig("def_rpl_name"));
			getRcDefNameRep().setName("rcdef_rpl_name");
			getRcAttCoordRep().setText(configC.getConfig("att_rpl_coord"));
			getRcAttCoordRep().setName("rcatt_rpl_coord");
			getRcDefCoordRep().setText(configC.getConfig("def_rpl_coord"));
			getRcDefCoordRep().setName("rcdef_rpl_coord");

			// ER Tab

			getRePlanetName().setText(configL.getConfig("menu_opt_er_plnt"));
			if (configC.getConfig("ER_planet").equals("1"))
				getRePlanetName().setSelected(true);
			getRePlanetName().setName("erER_planet");

			getRePlayerName().setText(configL.getConfig("name"));
			if (configC.getConfig("ER_player_name").equals("1"))
				getRePlayerName().setSelected(true);
			getRePlayerName().setName("erER_player_name");

			getReCoordinate().setText(configL.getConfig("menu_opt_er_coord"));
			if (configC.getConfig("ER_coordonnate").equals("1"))
				getReCoordinate().setSelected(true);
			getReCoordinate().setName("erER_coordonnate");

			getReGoods().setText(configL.getConfig("ER_RESS"));
			if (configC.getConfig("ER_stock").equals("1"))
				getReGoods().setSelected(true);
			getReGoods().setName("erER_stock");

			getReFleet().setText(configL.getConfig("ER_FLT"));
			if (configC.getConfig("ER_fleet").equals("1"))
				getReFleet().setSelected(true);
			getReFleet().setName("erER_fleet");

			getReBuilding().setText(configL.getConfig("ER_BLD"));
			if (configC.getConfig("ER_building").equals("1"))
				getReBuilding().setSelected(true);
			getReBuilding().setName("erER_building");

			getReDefense().setText(configL.getConfig("ER_DEF"));
			if (configC.getConfig("ER_def").equals("1"))
				getReDefense().setSelected(true);
			getReDefense().setName("erER_def");

			getReTechno().setText(configL.getConfig("ER_THN"));
			if (configC.getConfig("ER_techno").equals("1"))
				getReTechno().setSelected(true);
			getReTechno().setName("erER_techno");

			getReProd().setText(configL.getConfig("ER_PROD"));
			if (configC.getConfig("ER_prod").equals("1"))
				getReProd().setSelected(true);
			getReProd().setName("erER_prod");

			getReCdr().setText(configL.getConfig("ER_CDR"));
			if (configC.getConfig("ER_cdr").equals("1"))
				getReCdr().setSelected(true);
			getReCdr().setName("erER_cdr");

			getReMip().setText(configL.getConfig("ER_MIP"));
			if (configC.getConfig("ER_mip").equals("1"))
				getReMip().setSelected(true);
			getReMip().setName("erER_mip");

			getRePhlg().setText(configL.getConfig("ER_PHLG"));
			if (configC.getConfig("ER_phlg").equals("1"))
				getRePhlg().setSelected(true);
			getRePhlg().setName("erER_phlg");

			getReActivity().setText(configL.getConfig("ER_ACTIVITY"));
			if (configC.getConfig("ER_act").equals("1"))
				getReActivity().setSelected(true);
			getReActivity().setName("erER_act");

			getReTable().setText(configL.getConfig("TABLE") + " (font)");
			if (configC.getConfig("ER_table").equals("1"))
				getReTable().setSelected(true);
			getReTable().setName("erER_table");

			rePlanetNameLbl.setText(configL.getConfig("menu_opt_er_plnt") + " :");
			getRePlanetNameRepl().setText(configC.getConfig("ER_planetNameRepl"));
			getRePlanetNameRepl().setName("erER_planetNameRepl");

			reCoordinateLbl.setText(configL.getConfig("menu_opt_er_coord") + " :");
			getReCoordinateRepl().setText(configC.getConfig("ER_coordinateRepl"));
			getReCoordinateRepl().setName("erER_coordinateRepl");

			// Harvest Tab

			getHvtRate().setText(configL.getConfig("menu_opt_txrecyform"));
			if (configC.getConfig("cdr_taux").equals("1"))
				getHvtRate().setSelected(true);
			getHvtRate().setName("hvtcdr_taux");

			getHvtDate().setText(configL.getConfig("menu_opt_rc_date"));
			if (configC.getConfig("harvestDate").equals("1"))
				getHvtDate().setSelected(true);
			getHvtDate().setName("hvtharvestDate");

			getHvtCoord().setText(configL.getConfig("menu_opt_er_coord"));
			if (configC.getConfig("harvestCoord").equals("1"))
				getHvtCoord().setSelected(true);
			getHvtCoord().setName("hvtharvestCoord");

			hvtCoordLbl.setText(configL.getConfig("menu_opt_er_coord") + " : ");
			getHvtCoordRepl().setText(configC.getConfig("harvest_rpl_coord"));
			getHvtCoordRepl().setName("hvtharvest_rpl_coord");

			// Membres Tab

			getMbName().setText(configL.getConfig("name"));
			if (configC.getConfig("ml_name").equals("1"))
				getMbName().setSelected(true);
			getMbName().setName("mlml_name");

			getMbStatut().setText(configL.getConfig("statut"));
			if (configC.getConfig("ml_statut").equals("1"))
				getMbStatut().setSelected(true);
			getMbStatut().setName("mlml_statut");

			getMbPoints().setText(configL.getConfig("points"));
			if (configC.getConfig("ml_points").equals("1"))
				getMbPoints().setSelected(true);
			getMbPoints().setName("mlml_points");

			getMbCoord().setText(configL.getConfig("menu_opt_er_coord"));
			if (configC.getConfig("ml_coord").equals("1"))
				getMbCoord().setSelected(true);
			getMbCoord().setName("mlml_coord");

			getMbAdhes().setText(configL.getConfig("adhesion"));
			if (configC.getConfig("ml_adhesion").equals("1"))
				getMbAdhes().setSelected(true);
			getMbAdhes().setName("mlml_adhesion");

			getMbTable().setText(configL.getConfig("TABLE") + " (font)");
			if (configC.getConfig("ml_table").equals("1"))
				getMbTable().setSelected(true);
			getMbTable().setName("mlml_table");

			// Empire Tab

			getEmpCoordinate().setText(configL.getConfig("menu_opt_er_coord"));
			if (configC.getConfig("EMP_coord").equals("1"))
				getEmpCoordinate().setSelected(true);
			getEmpCoordinate().setName("eprEMP_coord");

			getEmpField().setText(configL.getConfig("EMP_FIELD"));
			if (configC.getConfig("EMP_field").equals("1"))
				getEmpField().setSelected(true);
			getEmpField().setName("eprEMP_field");

			getEmpBuilding().setText(configL.getConfig("EMP_BLD"));
			if (configC.getConfig("EMP_bld").equals("1"))
				getEmpBuilding().setSelected(true);
			getEmpBuilding().setName("eprEMP_bld");

			getEmpShip().setText(configL.getConfig("EMP_FLEET"));
			if (configC.getConfig("EMP_ship").equals("1"))
				getEmpShip().setSelected(true);
			getEmpShip().setName("eprEMP_ship");

			getEmpDefense().setText(configL.getConfig("EMP_DEF"));
			if (configC.getConfig("EMP_def").equals("1"))
				getEmpDefense().setSelected(true);
			getEmpDefense().setName("eprEMP_def");

			getEmpResearch().setText(configL.getConfig("EMP_RESEARCH"));
			if (configC.getConfig("EMP_research").equals("1"))
				getEmpResearch().setSelected(true);
			getEmpResearch().setName("eprEMP_research");

			getEmpProduction().setText(configL.getConfig("ER_PROD"));
			if (configC.getConfig("EMP_prod").equals("1"))
				getEmpProduction().setSelected(true);
			getEmpProduction().setName("eprEMP_prod");

			getEmpTableFont().setText(configL.getConfig("TABLE") + " (font)");
			if (configC.getConfig("EMP_tfont").equals("1"))
				getEmpTableFont().setSelected(true);
			getEmpTableFont().setName("eprEMP_tfont");

			// Other Tab
			tmp = configC.getConfig("universes").split("\\|\\|");
			sel = (int) Main.exptoint(configC.getConfig("universe_selected"));

			getUniListModel().removeAllElements();
			getDeleteUniversNames().removeAllItems();
			for (i = 0; i < tmp.length; i++) {
				getUniListModel().addElement(tmp[i]);
				getDeleteUniversNames().addItem(tmp[i]);
			}
			getUniversCellList().setModel(getUniListModel());
			getUniversCellList().setSelectedIndex(sel);
			getUniversCellList().setName("otheruniverse_selected");

			getAddUniversName().setName("otheradd");
			getDeleteUnivers().setName("otherdel");

			tmp = configC.getConfig("system").split("\\|");
			system.setText(configL.getConfig("SYSTEM_GALAXY") + " :");
			getSystemValue().setText(tmp[sel].trim());
			getSystemValue().setName("othersystem");

			tmp = configC.getConfig("speed").split("\\|");
			speed.setText(configL.getConfig("SPEED_UNIVERSE") + " :");
			getSpeedValue().setText(tmp[sel].trim());
			getSpeedValue().setName("otherspeed");

			tmp = configC.getConfig("combustion").split("\\|");
			combustion.setText(configL.getConfig("combustion") + " :");
			getCombustionValue().setText(tmp[sel].trim());
			getCombustionValue().setName("othercombustion");

			tmp = configC.getConfig("impulse").split("\\|");
			impulsion.setText(configL.getConfig("impulse") + " :");
			getImpulsionValue().setText(tmp[sel].trim());
			getImpulsionValue().setName("otherimpulse");

			tmp = configC.getConfig("hyperspace").split("\\|");
			hyperespace.setText(configL.getConfig("hyperspace") + " :");
			getHyperespaceValue().setText(tmp[sel].trim());
			getHyperespaceValue().setName("otherhyperspace");

		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	private void init_listener() {

		// BBCode Tab

		getCenterCell().addItemListener(control);
		getSizeCell().addItemListener(control);
		getBoldCell().addItemListener(control);
		getItalicCell().addItemListener(control);
		getUnderCell().addItemListener(control);
		getColorCell().addItemListener(control);
		getCodeCell().addItemListener(control);
		getQuoteCell().addItemListener(control);
		getFontCell().addItemListener(control);
		getImageCell().addItemListener(control);
		getUrlCell().addItemListener(control);

		getBbConfigList().addMouseListener(control);
		getBbmanage().addActionListener(control);

		// CR Tab
		getRcDate().addItemListener(control);
		getRcAttCoord().addItemListener(control);
		getRcDefCoord().addItemListener(control);
		getRcAttName().addItemListener(control);
		getRcDefName().addItemListener(control);
		getRcTechnology().addItemListener(control);
		getRcRentability().addItemListener(control);
		getRcConsumption().addItemListener(control);
		getRcJustMyRenta().addItemListener(control);
		getRcJustEnd().addItemListener(control);
		getRcIAmAtt().addItemListener(control);
		getRcHarvested().addItemListener(control);
		getRcColumn().addItemListener(control);

		getRcAfterBattleRep().addKeyListener(control);
		getRcAttNameRep().addKeyListener(control);
		getRcDefNameRep().addKeyListener(control);
		getRcAttCoordRep().addKeyListener(control);
		getRcDefCoordRep().addKeyListener(control);
		getRcAfterBattleRep().addActionListener(control);
		getRcAttNameRep().addActionListener(control);
		getRcDefNameRep().addActionListener(control);
		getRcAttCoordRep().addActionListener(control);
		getRcDefCoordRep().addActionListener(control);

		// ER Tab

		getRePlanetName().addItemListener(control);
		getRePlayerName().addItemListener(control);
		getReCoordinate().addItemListener(control);
		getReGoods().addItemListener(control);
		getReFleet().addItemListener(control);
		getReBuilding().addItemListener(control);
		getReDefense().addItemListener(control);
		getReTechno().addItemListener(control);
		getReProd().addItemListener(control);
		getReCdr().addItemListener(control);
		getRePhlg().addItemListener(control);
		getReMip().addItemListener(control);
		getReActivity().addItemListener(control);

		getReTable().addItemListener(control);

		getRePlanetNameRepl().addKeyListener(control);
		getReCoordinateRepl().addKeyListener(control);

		// Harvest Tab

		getHvtRate().addItemListener(control);
		getHvtCoord().addItemListener(control);
		getHvtDate().addItemListener(control);
		getHvtCoordRepl().addKeyListener(control);

		// Memebers Tab

		getMbName().addItemListener(control);
		getMbStatut().addItemListener(control);
		getMbPoints().addItemListener(control);
		getMbCoord().addItemListener(control);
		getMbAdhes().addItemListener(control);
		getMbTable().addItemListener(control);

		// Empire Tab

		getEmpCoordinate().addItemListener(control);
		getEmpField().addItemListener(control);
		getEmpBuilding().addItemListener(control);
		getEmpShip().addItemListener(control);
		getEmpDefense().addItemListener(control);
		getEmpResearch().addItemListener(control);
		getEmpProduction().addItemListener(control);
		getEmpTableFont().addItemListener(control);

		// Configuration Tab

		getAddConfig().setName("cgadd");
		getAddConfig().addActionListener(control);
		getDelConfig().setName("cgdel");
		getDelConfig().addActionListener(control);

		// Other Tab
		getUniversCellList().addMouseListener(control);
		getAddUniversName().addActionListener(control);
		getDeleteUnivers().addActionListener(control);
		getSystemValue().addKeyListener(control);
		getSpeedValue().addKeyListener(control);
		getCombustionValue().addKeyListener(control);
		getImpulsionValue().addKeyListener(control);
		getHyperespaceValue().addKeyListener(control);
	}

	public void updateUniversData() {
		String tmp[];
		int i, sel;
		try {
			Configuration configC = new Configuration("config.ini");

			tmp = configC.getConfig("universes").split("\\|\\|");
			sel = (int) Main.exptoint(configC.getConfig("universe_selected"));

			getUniListModel().removeAllElements();
			getDeleteUniversNames().removeAllItems();
			for (i = 0; i < tmp.length; i++) {
				getUniListModel().addElement(tmp[i]);
				getDeleteUniversNames().addItem(tmp[i]);
			}
			getUniversCellList().setModel(getUniListModel());
			getUniversCellList().setSelectedIndex(sel);
			getUniversCellList().setName("otheruniverse_selected");

			getAddUniversName().setName("otheradd");
			getDeleteUnivers().setName("otherdel");

			tmp = configC.getConfig("system").split("\\|");
			getSystemValue().setText(tmp[sel].trim());

			tmp = configC.getConfig("speed").split("\\|");
			getSpeedValue().setText(tmp[sel].trim());

			tmp = configC.getConfig("combustion").split("\\|");
			getCombustionValue().setText(tmp[sel].trim());

			tmp = configC.getConfig("impulse").split("\\|");
			getImpulsionValue().setText(tmp[sel].trim());

			tmp = configC.getConfig("hyperspace").split("\\|");
			getHyperespaceValue().setText(tmp[sel].trim());

		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public void updateBBCode(int id) {
		String tmp[];
		int index;

		if (id < 0 && getBbConfigList().getSelectedIndex() < 0)
			return;

		try {
			tmp = Configuration.getConfig("config.ini", "BBcodes").split("\\|");
			if (id < 0)
				tmp = tmp[getBbConfigList().getSelectedIndex()].split(",");
			else
				tmp = tmp[id].split(",");
			Configuration.setConfig("config.ini", "center", "" + tmp[0]);
			index = Integer.parseInt(tmp[0]);
			if (index <= (getCenterCell().getItemCount() - 1))
				getCenterCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "size", "" + tmp[1]);
			index = Integer.parseInt(tmp[1]);
			if (index <= (getSizeCell().getItemCount() - 1))
				getSizeCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "bold", "" + tmp[2]);
			index = Integer.parseInt(tmp[2]);
			if (index <= (getBoldCell().getItemCount() - 1))
				getBoldCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "ita", "" + tmp[3]);
			index = Integer.parseInt(tmp[3]);
			if (index <= (getItalicCell().getItemCount() - 1))
				getItalicCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "under", "" + tmp[4]);
			index = Integer.parseInt(tmp[4]);
			if (index <= (getUnderCell().getItemCount() - 1))
				getUnderCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "bcolor", "" + tmp[5]);
			index = Integer.parseInt(tmp[5]);
			if (index <= (getColorCell().getItemCount() - 1))
				getColorCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "code", "" + tmp[6]);
			index = Integer.parseInt(tmp[6]);
			if (index <= (getCodeCell().getItemCount() - 1))
				getCodeCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "quote", "" + tmp[7]);
			index = Integer.parseInt(tmp[7]);
			if (index <= (getQuoteCell().getItemCount() - 1))
				getQuoteCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "font", "" + tmp[8]);
			index = Integer.parseInt(tmp[8]);
			if (index <= (getFontCell().getItemCount() - 1))
				getFontCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "img", "" + tmp[9]);
			index = Integer.parseInt(tmp[9]);
			if (index <= (getImageCell().getItemCount() - 1))
				getImageCell().setSelectedIndex(index);
			Configuration.setConfig("config.ini", "url", "" + tmp[10]);
			index = Integer.parseInt(tmp[10]);
			if (index <= (getUrlCell().getItemCount() - 1))
				getUrlCell().setSelectedIndex(index);
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public void addUniversName() {
		String name, lastnames, lastsystem, lastspeed, lastcombustion, lastimpulse, lasthyperspace;

		name = this.getAddUniversNameValue().getText().trim();
		if (name.length() > 0) {
			try {
				Configuration configC = new Configuration("config.ini");

				lastnames = configC.getConfig("universes");
				lastsystem = configC.getConfig("system");
				lastspeed = configC.getConfig("speed");
				lastcombustion = configC.getConfig("combustion");
				lastimpulse = configC.getConfig("impulse");
				lasthyperspace = configC.getConfig("hyperspace");
				configC.setConfig("universes", lastnames + "||" + name);
				configC.setConfig("system", lastsystem + "|499");
				configC.setConfig("speed", lastspeed + "|1");
				configC.setConfig("combustion", lastcombustion + "|0");
				configC.setConfig("impulse", lastimpulse + "|0");
				configC.setConfig("hyperspace", lasthyperspace + "|0");
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}
		}
	}

	public void deleteUniversName() {
		String lastnames, lastsystem, lastspeed, lastcombustion, lastimpulse, lasthyperspace, tmp[];
		int i, l, index, selectedUni;

		index = this.getDeleteUniversNames().getSelectedIndex();
		try {
			Configuration configC = new Configuration("config.ini");

			lastnames = configC.getConfig("universes");
			lastsystem = configC.getConfig("system");
			lastspeed = configC.getConfig("speed");
			lastcombustion = configC.getConfig("combustion");
			lastimpulse = configC.getConfig("impulse");
			lasthyperspace = configC.getConfig("hyperspace");
			selectedUni = (int) Main.exptoint(configC.getConfig("universe_selected"));

			tmp = lastnames.split("\\|\\|");
			l = tmp.length;
			if (l > 1) {

				if (selectedUni == index || selectedUni >= tmp.length - 1)
					selectedUni = 0;

				lastnames = "";
				for (i = 0; i < l; i++) {
					if (index == i)
						continue;
					else
						lastnames += (lastnames.length() > 0 ? "||" : "") + tmp[i];
				}

				tmp = lastsystem.split("\\|");
				lastsystem = "";
				for (i = 0; i < l; i++) {
					if (index == i)
						continue;
					else
						lastsystem += (lastsystem.length() > 0 ? "|" : "") + tmp[i];
				}

				tmp = lastspeed.split("\\|");
				lastspeed = "";
				for (i = 0; i < l; i++) {
					if (index == i)
						continue;
					else
						lastspeed += (lastspeed.length() > 0 ? "|" : "") + tmp[i];
				}

				tmp = lastcombustion.split("\\|");
				lastcombustion = "";
				for (i = 0; i < l; i++) {
					if (index == i)
						continue;
					else
						lastcombustion += (lastcombustion.length() > 0 ? "|" : "") + tmp[i];
				}

				tmp = lastimpulse.split("\\|");
				lastimpulse = "";
				for (i = 0; i < l; i++) {
					if (index == i)
						continue;
					else
						lastimpulse += (lastimpulse.length() > 0 ? "|" : "") + tmp[i];
				}

				tmp = lasthyperspace.split("\\|");
				lasthyperspace = "";
				for (i = 0; i < l; i++) {
					if (index == i)
						continue;
					else
						lasthyperspace += (lasthyperspace.length() > 0 ? "|" : "") + tmp[i];
				}

				configC.setConfig("universes", lastnames);
				configC.setConfig("system", lastsystem);
				configC.setConfig("speed", lastspeed);
				configC.setConfig("combustion", lastcombustion);
				configC.setConfig("impulse", lastimpulse);
				configC.setConfig("hyperspace", lasthyperspace);
				configC.setConfig("universe_selected", selectedUni);
			}
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public void setUniversData() {
		String lastsystem, lastspeed, lastcombustion, lastimpulse, lasthyperspace, tmp[];
		String newsystem, newspeed, newcombustion, newimpulse, newhyperspace;
		int i, l, index;

		index = this.getUniversCellList().getSelectedIndex();
		newsystem = this.getSystemValue().getText();
		newspeed = this.getSpeedValue().getText();
		newcombustion = this.getCombustionValue().getText();
		newimpulse = this.getImpulsionValue().getText();
		newhyperspace = this.getHyperespaceValue().getText();
		if (newsystem.length() == 0)
			newsystem = " ";
		if (newspeed.length() == 0)
			newspeed = " ";
		if (newcombustion.length() == 0)
			newcombustion = " ";
		if (newimpulse.length() == 0)
			newimpulse = " ";
		if (newhyperspace.length() == 0)
			newhyperspace = " ";
		try {
			Configuration configC = new Configuration("config.ini");

			lastsystem = configC.getConfig("system");
			lastspeed = configC.getConfig("speed");
			lastcombustion = configC.getConfig("combustion");
			lastimpulse = configC.getConfig("impulse");
			lasthyperspace = configC.getConfig("hyperspace");

			tmp = lastsystem.split("\\|");
			l = tmp.length;
			lastsystem = "";
			for (i = 0; i < l; i++) {
				if (index == i)
					lastsystem += (lastsystem.length() > 0 ? "|" : "") + newsystem;
				else
					lastsystem += (lastsystem.length() > 0 ? "|" : "") + tmp[i];
			}

			tmp = lastspeed.split("\\|");
			lastspeed = "";
			for (i = 0; i < l; i++) {
				if (index == i)
					lastspeed += (lastspeed.length() > 0 ? "|" : "") + newspeed;
				else
					lastspeed += (lastspeed.length() > 0 ? "|" : "") + tmp[i];
			}

			tmp = lastcombustion.split("\\|");
			lastcombustion = "";
			for (i = 0; i < l; i++) {
				if (index == i)
					lastcombustion += (lastcombustion.length() > 0 ? "|" : "") + newcombustion;
				else
					lastcombustion += (lastcombustion.length() > 0 ? "|" : "") + tmp[i];
			}

			tmp = lastimpulse.split("\\|");
			lastimpulse = "";
			for (i = 0; i < l; i++) {
				if (index == i)
					lastimpulse += (lastimpulse.length() > 0 ? "|" : "") + newimpulse;
				else
					lastimpulse += (lastimpulse.length() > 0 ? "|" : "") + tmp[i];
			}

			tmp = lasthyperspace.split("\\|");
			lasthyperspace = "";
			for (i = 0; i < l; i++) {
				if (index == i)
					lasthyperspace += (lasthyperspace.length() > 0 ? "|" : "") + newhyperspace;
				else
					lasthyperspace += (lasthyperspace.length() > 0 ? "|" : "") + tmp[i];
			}

			configC.setConfig("system", lastsystem);
			configC.setConfig("speed", lastspeed);
			configC.setConfig("combustion", lastcombustion);
			configC.setConfig("impulse", lastimpulse);
			configC.setConfig("hyperspace", lasthyperspace);
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	public void setLookConfig(int config) {
		if (getLookConfig() != null) {
			Component c = getLookConfig().getLeftComponent();
			try {
				String[] names = Configuration.getConfig("config.ini", "user_color_name")
						.split("\\|");
				if (names != null && names.length >= config
						&& !(names.length == 1 && names[0].trim().length() <= 0)) {
					if (c != null && c instanceof ConfigColorPane
							&& ((ConfigColorPane) c).getConfigID() == config)
						return;
					c = null;

					getLookConfig().setLeftComponent(
							new ConfigColorPane(config, names[config - 1]));
				} else {
					getLookConfig().setLeftComponent(new JLabel("No Configuration Available"));
				}
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}
		}
	}

	public void init_colorConfigList(int select) {
		JPanel pan = this.getConfigColorList();
		JCheckBox cb;
		JToggleButton tb;
		JButton b;
		ButtonGroup gpcb = new ButtonGroup();
		ButtonGroup gptb = new ButtonGroup();
		int i;
		GridBagConstraints gridBagConstraints;

		pan.removeAll();
		getDelConfigList().removeAllItems();

		try {
			Configuration config = new Configuration("config.ini");
			String[] names = config.getConfig("user_color_name").split("\\|");
			int selected = Integer.parseInt(config.getConfig("color").startsWith("0") ? "-"
					+ config.getConfig("color") : config.getConfig("color"));

			gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.CENTER;
			pan.add(new JLabel("Used"), gridBagConstraints);

			gridBagConstraints = (GridBagConstraints) gridBagConstraints.clone();
			gridBagConstraints.gridy = 1;
			gridBagConstraints.gridx = 0;
			pan.add(cb = new JCheckBox(), gridBagConstraints);
			cb.setName("cgcolor01");
			cb.setSelected(selected == -1);
			cb.addItemListener(control);
			gpcb.add(cb);
			gridBagConstraints = (GridBagConstraints) gridBagConstraints.clone();
			gridBagConstraints.gridy = 1;
			gridBagConstraints.gridx = 1;
			pan.add(new JLabel("dark background"), gridBagConstraints);

			gridBagConstraints = (GridBagConstraints) gridBagConstraints.clone();
			gridBagConstraints.gridy = 2;
			gridBagConstraints.gridx = 0;
			pan.add(cb = new JCheckBox(), gridBagConstraints);
			cb.setName("cgcolor02");
			cb.setSelected(selected == -2);
			cb.addItemListener(control);
			gpcb.add(cb);
			gridBagConstraints = (GridBagConstraints) gridBagConstraints.clone();
			gridBagConstraints.gridy = 2;
			gridBagConstraints.gridx = 1;
			pan.add(new JLabel("light background"), gridBagConstraints);

			for (i = 0; i < names.length; i++) {
				if (names[i].trim().length() <= 0)
					break;

				gridBagConstraints = (GridBagConstraints) gridBagConstraints.clone();
				gridBagConstraints.gridy = i + 3;
				gridBagConstraints.gridx = 0;
				pan.add(cb = new JCheckBox(), gridBagConstraints);
				gridBagConstraints = (GridBagConstraints) gridBagConstraints.clone();
				gridBagConstraints.gridy = i + 3;
				gridBagConstraints.gridx = 1;
				pan.add(tb = new JToggleButton(names[i]), gridBagConstraints);
				gridBagConstraints = (GridBagConstraints) gridBagConstraints.clone();
				gridBagConstraints.gridy = i + 3;
				gridBagConstraints.gridx = 2;
				pan.add(b = new JButton("Rename"), gridBagConstraints);
				gpcb.add(cb);
				gptb.add(tb);
				cb.setName("cgcolor" + (i + 1));
				cb.setSelected(selected == i + 1);
				cb.addItemListener(control);
				tb.setName("" + (i + 1));
				if (i + 1 == select)
					tb.setSelected(true);
				tb.addChangeListener(new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						if (((JToggleButton) e.getSource()).isSelected()) {
							setLookConfig(Integer.parseInt(((JToggleButton) e.getSource()).getName()));
						}
					}
				});
				b.setName("" + (i + 1));
				b.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String name = JOptionPane.showInputDialog(Main.getParentFrame(),
								"New Name:", "");
						if (name != null && !name.trim().equals("")) {
							try {
								String[] names = Configuration.getConfig("config.ini",
										"user_color_name").split("\\|");
								int id = Integer.parseInt(((JButton) e.getSource()).getName()) - 1;
								if (id >= 0 && id < names.length)
									names[id] = name;
								name = "";
								for (int i = 0; i < names.length; i++)
									name += "|" + names[i];
								Configuration.setConfig("config.ini", "user_color_name",
										name.substring(1));
								init_colorConfigList(id + 1);
								setLookConfig(id + 1);
							} catch (Exception ee) {
								ExceptionAlert.createExceptionAlert(ee);
								ee.printStackTrace();
							}
						}
					}
				});

				getDelConfigList().addItem(names[i]);
			}
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		this.repaint();
	}

	public int getDelConfigSelected() {
		String[] name;
		int i;
		try {
			name = Configuration.getConfig("config.ini", "user_color_name").split("\\|");
			for (i = 0; i < name.length; i++) {
				if (name[i].equals((String) getDelConfigList().getSelectedItem()))
					return i + 1;
			}
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
		return -1;
	}

	public String getNewConfigNameValue() {
		String name = getNewConfigName().getText();
		getNewConfigName().setText("");
		return name;
	}

	/**
	 * This method initializes bbListModel
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getBbListModel() {
		if (bbListModel == null) {
			bbListModel = new DefaultListModel();
		}
		return bbListModel;
	}

	/**
	 * This method initializes uniListModel
	 * 
	 * @return javax.swing.DefaultListModel
	 */
	private DefaultListModel getUniListModel() {
		if (uniListModel == null) {
			uniListModel = new DefaultListModel();
		}
		return uniListModel;
	}

	/**
	 * This method initializes bbcode
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getBbcode() {
		if (bbcode == null) {
			bbcode = new JSplitPane();
			bbcode.setRightComponent(getBbcodeAdmin());
			bbcode.setOneTouchExpandable(true);
			bbcode.setLeftComponent(getBbcodeScroll());
			bbcode.setResizeWeight(0.5);
		}
		return bbcode;
	}

	/**
	 * This method initializes bbcodeScroll
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getBbcodeScroll() {
		if (bbcodeScroll == null) {
			bbcodeScroll = new JScrollPane();
			bbcodeScroll.setViewportView(getBbcodeCell());
		}
		return bbcodeScroll;
	}

	/**
	 * This method initializes rcScrollCheck
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getRcScrollCheck() {
		if (rcScrollCheck == null) {
			rcScrollCheck = new JScrollPane();
			rcScrollCheck.setViewportView(getRcCheckOptions());
		}
		return rcScrollCheck;
	}

	/**
	 * This method initializes jSplitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getBattle() {
		if (battle == null) {
			battle = new JSplitPane();
			battle.setLeftComponent(getRcScrollCheck());
			battle.setOneTouchExpandable(true);
			battle.setRightComponent(getRcScrollText());
			battle.setResizeWeight(0.5);
		}
		return battle;
	}

	/**
	 * This method initializes rcScrollText
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getRcScrollText() {
		if (rcScrollText == null) {
			rcScrollText = new JScrollPane();
			rcScrollText.setViewportView(getRcPersonnalText());
		}
		return rcScrollText;
	}

	/**
	 * This method initializes lookConfig
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getLookConfig() {
		if (lookConfig == null) {
			lookConfig = new JSplitPane();
			lookConfig.setOneTouchExpandable(true);
			lookConfig.setRightComponent(getConfigColorListScroll());
			lookConfig.setResizeWeight(0.2);
		}
		return lookConfig;
	}

	/**
	 * This method initializes configColorListScroll
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getConfigColorListScroll() {
		if (configColorListScroll == null) {
			configColorListScroll = new JScrollPane();
			configColorListScroll.setViewportView(getConfigColorListPane());
		}
		return configColorListScroll;
	}

	/**
	 * This method initializes configColorListPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getConfigColorListPane() {
		if (configColorListPane == null) {
			configColorListPane = new JPanel();
			configColorListPane.setLayout(new BorderLayout());
			configColorListPane.add(getConfigColorAdd(), BorderLayout.NORTH);
			configColorListPane.add(getConfigColorList(), BorderLayout.CENTER);
		}
		return configColorListPane;
	}

	/**
	 * This method initializes configColorAdd
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getConfigColorAdd() {
		if (configColorAdd == null) {
			GridBagConstraints gridBagConstraints60 = new GridBagConstraints();
			gridBagConstraints60.fill = GridBagConstraints.BOTH;
			gridBagConstraints60.gridy = 0;
			gridBagConstraints60.weightx = 1.0;
			gridBagConstraints60.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints60.gridx = 2;
			GridBagConstraints gridBagConstraints59 = new GridBagConstraints();
			gridBagConstraints59.gridx = 1;
			gridBagConstraints59.fill = GridBagConstraints.BOTH;
			gridBagConstraints59.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints59.gridy = 0;
			GridBagConstraints gridBagConstraints58 = new GridBagConstraints();
			gridBagConstraints58.gridx = 3;
			gridBagConstraints58.fill = GridBagConstraints.BOTH;
			gridBagConstraints58.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints58.gridy = 0;
			GridBagConstraints gridBagConstraints57 = new GridBagConstraints();
			gridBagConstraints57.fill = GridBagConstraints.BOTH;
			gridBagConstraints57.gridy = 0;
			gridBagConstraints57.weightx = 1.0;
			gridBagConstraints57.anchor = GridBagConstraints.WEST;
			gridBagConstraints57.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints57.gridwidth = 1;
			gridBagConstraints57.gridx = 0;
			configColorAdd = new JPanel();
			configColorAdd.setLayout(new GridBagLayout());
			configColorAdd.add(getAddConfig(), gridBagConstraints59);
			configColorAdd.add(getNewConfigName(), gridBagConstraints57);
			configColorAdd.add(getDelConfig(), gridBagConstraints58);
			configColorAdd.add(getDelConfigList(), gridBagConstraints60);
		}
		return configColorAdd;
	}

	/**
	 * This method initializes addConfig
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddConfig() {
		if (addConfig == null) {
			addConfig = new JButton();
			addConfig.setText("add");
			addConfig.setIcon(new ImageIcon(this.getClass().getResource("images/valid.png")));
		}
		return addConfig;
	}

	/**
	 * This method initializes newConfigName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNewConfigName() {
		if (newConfigName == null) {
			newConfigName = new JLimiterTextField("", ",|");
			newConfigName.setPreferredSize(new Dimension(50, 20));
		}
		return newConfigName;
	}

	/**
	 * This method initializes delConfig
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDelConfig() {
		if (delConfig == null) {
			delConfig = new JButton();
			delConfig.setText("delete");
			delConfig.setIcon(new ImageIcon(this.getClass().getResource("images/delete.png")));
		}
		return delConfig;
	}

	/**
	 * This method initializes configColorList
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getConfigColorList() {
		if (configColorList == null) {
			configColorList = new JPanel();
			configColorList.setLayout(new GridBagLayout());
		}
		return configColorList;
	}

	/**
	 * This method initializes delConfigList
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getDelConfigList() {
		if (delConfigList == null) {
			delConfigList = new JComboBox();
		}
		return delConfigList;
	}

	/**
	 * This method initializes reProd
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReProd() {
		if (reProd == null) {
			reProd = new JCheckBox();
		}
		return reProd;
	}

	/**
	 * This method initializes reCdr
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReCdr() {
		if (reCdr == null) {
			reCdr = new JCheckBox();
		}
		return reCdr;
	}

	/**
	 * This method initializes reMip
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReMip() {
		if (reMip == null) {
			reMip = new JCheckBox();
		}
		return reMip;
	}

	/**
	 * This method initializes rePhlg
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRePhlg() {
		if (rePhlg == null) {
			rePhlg = new JCheckBox();
		}
		return rePhlg;
	}

	/**
	 * This method initializes spy2
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getSpy() {
		if (spy == null) {
			spy = new JSplitPane();
			spy.setLeftComponent(getSpyScrollG());

			spy.setResizeWeight(0.5);
			spy.setRightComponent(getSpyScrollD());
			spy.setOneTouchExpandable(true);
		}
		return spy;
	}

	/**
	 * This method initializes spyScrollG
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getSpyScrollG() {
		if (spyScrollG == null) {
			spyScrollG = new JScrollPane();
			spyScrollG.setViewportView(getSpyCheck());
		}
		return spyScrollG;
	}

	/**
	 * This method initializes spyScrollD
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getSpyScrollD() {
		if (spyScrollD == null) {
			spyScrollD = new JScrollPane();
			spyScrollD.setViewportView(getSpyRepl());
		}
		return spyScrollD;
	}

	/**
	 * This method initializes spyRepl
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSpyRepl() {
		if (spyRepl == null) {
			GridBagConstraints gridBagConstraints68 = new GridBagConstraints();
			gridBagConstraints68.fill = GridBagConstraints.BOTH;
			gridBagConstraints68.gridy = 1;
			gridBagConstraints68.weightx = 1.0;
			gridBagConstraints68.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints68.gridx = 1;
			GridBagConstraints gridBagConstraints67 = new GridBagConstraints();
			gridBagConstraints67.gridx = 0;
			gridBagConstraints67.anchor = GridBagConstraints.EAST;
			gridBagConstraints67.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints67.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints67.gridy = 1;
			reCoordinateLbl = new JLabel();
			reCoordinateLbl.setText("JLabel");
			GridBagConstraints gridBagConstraints66 = new GridBagConstraints();
			gridBagConstraints66.fill = GridBagConstraints.BOTH;
			gridBagConstraints66.gridy = 0;
			gridBagConstraints66.weightx = 1.0;
			gridBagConstraints66.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints66.gridx = 1;
			GridBagConstraints gridBagConstraints65 = new GridBagConstraints();
			gridBagConstraints65.gridx = 0;
			gridBagConstraints65.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints65.anchor = GridBagConstraints.EAST;
			gridBagConstraints65.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints65.gridy = 0;
			rePlanetNameLbl = new JLabel();
			rePlanetNameLbl.setText("JLabel");
			spyRepl = new JPanel();
			spyRepl.setLayout(new GridBagLayout());
			spyRepl.add(rePlanetNameLbl, gridBagConstraints65);
			spyRepl.add(getRePlanetNameRepl(), gridBagConstraints66);
			spyRepl.add(reCoordinateLbl, gridBagConstraints67);
			spyRepl.add(getReCoordinateRepl(), gridBagConstraints68);
		}
		return spyRepl;
	}

	/**
	 * This method initializes rePlanetNameRepl
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getRePlanetNameRepl() {
		if (rePlanetNameRepl == null) {
			rePlanetNameRepl = new JTextField();
		}
		return rePlanetNameRepl;
	}

	/**
	 * This method initializes reCoordinateRepl
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getReCoordinateRepl() {
		if (reCoordinateRepl == null) {
			reCoordinateRepl = new JTextField();
		}
		return reCoordinateRepl;
	}

	/**
	 * This method initializes reTable
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReTable() {
		if (reTable == null) {
			reTable = new JCheckBox();
		}
		return reTable;
	}

	/**
	 * This method initializes other
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getOther() {
		if (other == null) {
			other = new JScrollPane();
			other.setViewportView(getOtherPan());
		}
		return other;
	}

	/**
	 * This method initializes otherPan
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOtherPan() {
		if (otherPan == null) {
			GridBagConstraints gridBagConstraints74 = new GridBagConstraints();
			gridBagConstraints74.gridx = 0;
			gridBagConstraints74.ipadx = 100;
			gridBagConstraints74.gridy = 0;
			speed = new JLabel();
			speed.setText("JLabel");
			system = new JLabel();
			system.setText("JLabel");
			otherPan = new JPanel();
			otherPan.setLayout(new GridBagLayout());
			otherPan.add(getOtherPaneContainer(), gridBagConstraints74);
		}
		return otherPan;
	}

	/**
	 * This method initializes systemValue
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSystemValue() {
		if (systemValue == null) {
			NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
			formatter.setAllowsInvalid(false);
			systemValue = new JFormattedTextField(formatter);
		}
		return systemValue;
	}

	/**
	 * This method initializes speedValue
	 * 
	 * @return javax.swing.JTextField
	 */
	private JFormattedTextField getSpeedValue() {
		if (speedValue == null) {
			NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
			formatter.setAllowsInvalid(false);
			speedValue = new JFormattedTextField(formatter);
		}
		return speedValue;
	}

	/**
	 * This method initializes otherPaneContainer
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getOtherPaneContainer() {
		if (otherPaneContainer == null) {
			GridBagConstraints gridBagConstraints102 = new GridBagConstraints();
			gridBagConstraints102.fill = GridBagConstraints.BOTH;
			gridBagConstraints102.gridy = 4;
			gridBagConstraints102.weightx = 1.0;
			gridBagConstraints102.anchor = GridBagConstraints.WEST;
			gridBagConstraints102.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints102.gridx = 1;
			GridBagConstraints gridBagConstraints101 = new GridBagConstraints();
			gridBagConstraints101.gridx = 0;
			gridBagConstraints101.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints101.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints101.gridy = 4;
			hyperespace = new JLabel();
			hyperespace.setText("JLabel");
			GridBagConstraints gridBagConstraints100 = new GridBagConstraints();
			gridBagConstraints100.fill = GridBagConstraints.BOTH;
			gridBagConstraints100.gridy = 3;
			gridBagConstraints100.weightx = 1.0;
			gridBagConstraints100.anchor = GridBagConstraints.WEST;
			gridBagConstraints100.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints100.gridx = 1;
			GridBagConstraints gridBagConstraints99 = new GridBagConstraints();
			gridBagConstraints99.gridx = 0;
			gridBagConstraints99.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints99.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints99.gridy = 3;
			impulsion = new JLabel();
			impulsion.setText("JLabel");
			GridBagConstraints gridBagConstraints98 = new GridBagConstraints();
			gridBagConstraints98.fill = GridBagConstraints.BOTH;
			gridBagConstraints98.gridy = 2;
			gridBagConstraints98.weightx = 1.0;
			gridBagConstraints98.anchor = GridBagConstraints.WEST;
			gridBagConstraints98.insets = new Insets(15, 2, 2, 2);
			gridBagConstraints98.gridx = 1;
			GridBagConstraints gridBagConstraints97 = new GridBagConstraints();
			gridBagConstraints97.gridx = 0;
			gridBagConstraints97.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints97.insets = new Insets(15, 2, 2, 2);
			gridBagConstraints97.gridy = 2;
			combustion = new JLabel();
			combustion.setText("JLabel");
			GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
			gridBagConstraints71.fill = GridBagConstraints.BOTH;
			gridBagConstraints71.gridx = 1;
			gridBagConstraints71.gridy = 0;
			gridBagConstraints71.ipadx = 0;
			gridBagConstraints71.ipady = 0;
			gridBagConstraints71.weightx = 1.0;
			gridBagConstraints71.anchor = GridBagConstraints.WEST;
			gridBagConstraints71.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints73 = new GridBagConstraints();
			gridBagConstraints73.fill = GridBagConstraints.BOTH;
			gridBagConstraints73.gridx = 1;
			gridBagConstraints73.gridy = 1;
			gridBagConstraints73.ipadx = 0;
			gridBagConstraints73.ipady = 0;
			gridBagConstraints73.weightx = 1.0;
			gridBagConstraints73.anchor = GridBagConstraints.WEST;
			gridBagConstraints73.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints72 = new GridBagConstraints();
			gridBagConstraints72.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints72.gridy = 1;
			gridBagConstraints72.ipadx = 0;
			gridBagConstraints72.ipady = 0;
			gridBagConstraints72.anchor = GridBagConstraints.EAST;
			gridBagConstraints72.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints72.gridx = 0;
			GridBagConstraints gridBagConstraints70 = new GridBagConstraints();
			gridBagConstraints70.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints70.gridy = -1;
			gridBagConstraints70.ipadx = 0;
			gridBagConstraints70.ipady = 0;
			gridBagConstraints70.anchor = GridBagConstraints.EAST;
			gridBagConstraints70.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints70.gridx = -1;
			otherPaneContainer = new JPanel();
			otherPaneContainer.setLayout(new GridBagLayout());
			otherPaneContainer.add(system, gridBagConstraints70);
			otherPaneContainer.add(getSystemValue(), gridBagConstraints71);
			otherPaneContainer.add(speed, gridBagConstraints72);
			otherPaneContainer.add(getSpeedValue(), gridBagConstraints73);
			otherPaneContainer.add(combustion, gridBagConstraints97);
			otherPaneContainer.add(getCombustionValue(), gridBagConstraints98);
			otherPaneContainer.add(impulsion, gridBagConstraints99);
			otherPaneContainer.add(getImpulsionValue(), gridBagConstraints100);
			otherPaneContainer.add(hyperespace, gridBagConstraints101);
			otherPaneContainer.add(getHyperespaceValue(), gridBagConstraints102);
		}
		return otherPaneContainer;
	}

	/**
	 * This method initializes empirePane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getEmpirePane() {
		if (empirePane == null) {
			GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
			gridBagConstraints81.gridx = 1;
			gridBagConstraints81.gridy = 4;
			GridBagConstraints gridBagConstraints83 = new GridBagConstraints();
			gridBagConstraints83.anchor = GridBagConstraints.WEST;
			gridBagConstraints83.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints83.fill = GridBagConstraints.VERTICAL;
			GridBagConstraints gridBagConstraints80 = new GridBagConstraints();
			gridBagConstraints80.gridx = 1;
			gridBagConstraints80.anchor = GridBagConstraints.WEST;
			gridBagConstraints80.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints80.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints80.gridy = 1;
			GridBagConstraints gridBagConstraints79 = new GridBagConstraints();
			gridBagConstraints79.gridx = 1;
			gridBagConstraints79.anchor = GridBagConstraints.WEST;
			gridBagConstraints79.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints79.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints79.gridy = 0;
			GridBagConstraints gridBagConstraints78 = new GridBagConstraints();
			gridBagConstraints78.gridx = 0;
			gridBagConstraints78.anchor = GridBagConstraints.WEST;
			gridBagConstraints78.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints78.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints78.gridy = 4;
			GridBagConstraints gridBagConstraints77 = new GridBagConstraints();
			gridBagConstraints77.gridx = 0;
			gridBagConstraints77.anchor = GridBagConstraints.WEST;
			gridBagConstraints77.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints77.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints77.gridy = 3;
			GridBagConstraints gridBagConstraints76 = new GridBagConstraints();
			gridBagConstraints76.gridx = 0;
			gridBagConstraints76.anchor = GridBagConstraints.WEST;
			gridBagConstraints76.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints76.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints76.gridy = 2;
			GridBagConstraints gridBagConstraints75 = new GridBagConstraints();
			gridBagConstraints75.gridx = 0;
			gridBagConstraints75.anchor = GridBagConstraints.WEST;
			gridBagConstraints75.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints75.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints75.gridy = 1;
			empirePane = new JPanel();
			empirePane.setLayout(new GridBagLayout());
			empirePane.add(getEmpCoordinate(), gridBagConstraints83);
			empirePane.add(getEmpField(), gridBagConstraints75);
			empirePane.add(getEmpBuilding(), gridBagConstraints76);
			empirePane.add(getEmpShip(), gridBagConstraints77);
			empirePane.add(getEmpDefense(), gridBagConstraints78);
			empirePane.add(getEmpResearch(), gridBagConstraints79);
			empirePane.add(getEmpProduction(), gridBagConstraints80);
			empirePane.add(getEmpTableFont(), gridBagConstraints81);
		}
		return empirePane;
	}

	/**
	 * This method initializes EmpCoordinate
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEmpCoordinate() {
		if (EmpCoordinate == null) {
			EmpCoordinate = new JCheckBox();
		}
		return EmpCoordinate;
	}

	/**
	 * This method initializes EmpField
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEmpField() {
		if (EmpField == null) {
			EmpField = new JCheckBox();
		}
		return EmpField;
	}

	/**
	 * This method initializes EmpBuilding
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEmpBuilding() {
		if (EmpBuilding == null) {
			EmpBuilding = new JCheckBox();
		}
		return EmpBuilding;
	}

	/**
	 * This method initializes EmpShip
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEmpShip() {
		if (EmpShip == null) {
			EmpShip = new JCheckBox();
		}
		return EmpShip;
	}

	/**
	 * This method initializes EmpDefense
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEmpDefense() {
		if (EmpDefense == null) {
			EmpDefense = new JCheckBox();
		}
		return EmpDefense;
	}

	/**
	 * This method initializes EmpResearch
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEmpResearch() {
		if (EmpResearch == null) {
			EmpResearch = new JCheckBox();
		}
		return EmpResearch;
	}

	/**
	 * This method initializes EmpProduction
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEmpProduction() {
		if (EmpProduction == null) {
			EmpProduction = new JCheckBox();
		}
		return EmpProduction;
	}

	/**
	 * This method initializes EmpTableFont
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getEmpTableFont() {
		if (EmpTableFont == null) {
			EmpTableFont = new JCheckBox();
		}
		return EmpTableFont;
	}

	/**
	 * This method initializes membres
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getMembres() {
		if (membres == null) {
			membres = new JScrollPane();
			membres.setViewportView(getMembrePane());
		}
		return membres;
	}

	/**
	 * This method initializes mbName
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getMbName() {
		if (mbName == null) {
			mbName = new JCheckBox();
		}
		return mbName;
	}

	/**
	 * This method initializes mbStatut
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getMbStatut() {
		if (mbStatut == null) {
			mbStatut = new JCheckBox();
		}
		return mbStatut;
	}

	/**
	 * This method initializes mbPoints
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getMbPoints() {
		if (mbPoints == null) {
			mbPoints = new JCheckBox();
		}
		return mbPoints;
	}

	/**
	 * This method initializes mbCoord
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getMbCoord() {
		if (mbCoord == null) {
			mbCoord = new JCheckBox();
		}
		return mbCoord;
	}

	/**
	 * This method initializes mbAdhes
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getMbAdhes() {
		if (mbAdhes == null) {
			mbAdhes = new JCheckBox();
		}
		return mbAdhes;
	}

	/**
	 * This method initializes mbTable
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getMbTable() {
		if (mbTable == null) {
			mbTable = new JCheckBox();
		}
		return mbTable;
	}

	/**
	 * This method initializes hvtDate
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getHvtDate() {
		if (hvtDate == null) {
			hvtDate = new JCheckBox();
		}
		return hvtDate;
	}

	/**
	 * This method initializes hvtCoord
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getHvtCoord() {
		if (hvtCoord == null) {
			hvtCoord = new JCheckBox();
		}
		return hvtCoord;
	}

	/**
	 * This method initializes hvtCoordRepl
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getHvtCoordRepl() {
		if (hvtCoordRepl == null) {
			hvtCoordRepl = new JTextField();
		}
		return hvtCoordRepl;
	}

	/**
	 * This method initializes univers
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getUnivers() {
		if (univers == null) {
			univers = new JSplitPane();
			univers.setLeftComponent(getOther());
			univers.setRightComponent(getUniversCellScroll());
		}
		return univers;
	}

	/**
	 * This method initializes universCellScroll
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getUniversCellScroll() {
		if (universCellScroll == null) {
			universCellScroll = new JScrollPane();
			universCellScroll.setViewportView(getUniversCellPane());
		}
		return universCellScroll;
	}

	/**
	 * This method initializes universCellPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getUniversCellPane() {
		if (universCellPane == null) {
			universCellPane = new JPanel();
			universCellPane.setLayout(new BorderLayout());
			universCellPane.add(getUniversCellAdder(), BorderLayout.NORTH);
			universCellPane.add(getUniversCellList(), BorderLayout.CENTER);
		}
		return universCellPane;
	}

	/**
	 * This method initializes universCellAdder
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getUniversCellAdder() {
		if (universCellAdder == null) {
			GridBagConstraints gridBagConstraints96 = new GridBagConstraints();
			gridBagConstraints96.gridx = 3;
			gridBagConstraints96.gridy = 0;
			GridBagConstraints gridBagConstraints95 = new GridBagConstraints();
			gridBagConstraints95.fill = GridBagConstraints.BOTH;
			gridBagConstraints95.gridy = 0;
			gridBagConstraints95.weightx = 1.0;
			gridBagConstraints95.gridx = 2;
			GridBagConstraints gridBagConstraints93 = new GridBagConstraints();
			gridBagConstraints93.gridx = 1;
			gridBagConstraints93.gridy = 0;
			GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
			gridBagConstraints91.fill = GridBagConstraints.BOTH;
			gridBagConstraints91.gridy = 0;
			gridBagConstraints91.weightx = 1.0;
			gridBagConstraints91.gridx = 0;
			universCellAdder = new JPanel();
			universCellAdder.setLayout(new GridBagLayout());
			universCellAdder.add(getAddUniversNameValue(), gridBagConstraints91);
			universCellAdder.add(getAddUniversName(), gridBagConstraints93);
			universCellAdder.add(getDeleteUniversNames(), gridBagConstraints95);
			universCellAdder.add(getDeleteUnivers(), gridBagConstraints96);
		}
		return universCellAdder;
	}

	/**
	 * This method initializes universCellList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getUniversCellList() {
		if (universCellList == null) {
			universCellList = new JList();
			universCellList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return universCellList;
	}

	/**
	 * This method initializes addUniversNameValue
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getAddUniversNameValue() {
		if (addUniversNameValue == null) {
			addUniversNameValue = new JLimiterTextField("", ",|");
			addUniversNameValue.setPreferredSize(new Dimension(50, 20));
		}
		return addUniversNameValue;
	}

	/**
	 * This method initializes addUniversName
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddUniversName() {
		if (addUniversName == null) {
			addUniversName = new JButton();
			addUniversName.setText("add");
			addUniversName.setIcon(new ImageIcon(this.getClass().getResource(
					"images/valid.png")));
		}
		return addUniversName;
	}

	/**
	 * This method initializes deleteUniversNames
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getDeleteUniversNames() {
		if (deleteUniversNames == null) {
			deleteUniversNames = new JComboBox();
		}
		return deleteUniversNames;
	}

	/**
	 * This method initializes deleteUnivers
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDeleteUnivers() {
		if (deleteUnivers == null) {
			deleteUnivers = new JButton();
			deleteUnivers.setText("delete");
			deleteUnivers.setIcon(new ImageIcon(this.getClass().getResource(
					"images/delete.png")));
		}
		return deleteUnivers;
	}

	/**
	 * This method initializes combustionValue
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCombustionValue() {
		if (combustionValue == null) {
			NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
			formatter.setAllowsInvalid(false);
			combustionValue = new JFormattedTextField(formatter);
		}
		return combustionValue;
	}

	/**
	 * This method initializes impulsionValue
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getImpulsionValue() {
		if (impulsionValue == null) {
			NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
			formatter.setAllowsInvalid(false);
			impulsionValue = new JFormattedTextField(formatter);
		}
		return impulsionValue;
	}

	/**
	 * This method initializes hyperespaceValue
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getHyperespaceValue() {
		if (hyperespaceValue == null) {
			NumberFormatter formatter = new NumberFormatter(new DecimalFormat("0"));
			formatter.setAllowsInvalid(false);
			hyperespaceValue = new JFormattedTextField(formatter);
		}
		return hyperespaceValue;
	}

	/**
	 * This method initializes rcConsumption
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getRcConsumption() {
		if (rcConsumption == null) {
			rcConsumption = new JCheckBox();
		}
		return rcConsumption;
	}

	/**
	 * This method initializes reActivity
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getReActivity() {
		if (reActivity == null) {
			reActivity = new JCheckBox();
		}
		return reActivity;
	}

	private JCheckBox getRePlayerName() {
		if (rePlayerName == null) {
			rePlayerName = new JCheckBox();
		}
		return rePlayerName;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
