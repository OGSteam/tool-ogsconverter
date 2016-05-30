/*
 * EditorTab.java
 *
 * Created on 4 octobre 2006, 18:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ogsconverter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

import ogsconverter.widgets.ScrollableBar;

/**
 * 
 * @author MOREAU Benoît
 */
public class EditorTab extends JPanel implements UndoableEditListener, ActionListener,
		KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of EditorTab */
	public EditorTab(String mdl) {
		super();
		this.setLayout(new BorderLayout());

		newButton.setIcon(new ImageIcon(this.getClass().getResource("icons/menu_new_16.gif")));
		newButton.setPreferredSize(new Dimension(30, 20));
		newButton.addActionListener(this);
		openButton.setIcon(new ImageIcon(this.getClass().getResource("icons/tree_open.gif")));
		openButton.setPreferredSize(new Dimension(30, 20));
		openButton.addActionListener(this);
		saveButton.setIcon(new ImageIcon(this.getClass().getResource("icons/menu_save_16.gif")));
		saveButton.setPreferredSize(new Dimension(30, 20));
		saveButton.addActionListener(this);
		saveButton.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+S");
		saveasButton.setIcon(new ImageIcon(this.getClass().getResource(
				"icons/menu_saveas_s16.gif")));
		saveasButton.setPreferredSize(new Dimension(30, 20));
		saveasButton.addActionListener(this);
		undoButton.setIcon(new ImageIcon(this.getClass().getResource("icons/menu_undo_16.gif")));
		undoButton.setPreferredSize(new Dimension(30, 20));
		undoButton.addActionListener(this);
		undoButton.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+Z");
		redoButton.setIcon(new ImageIcon(this.getClass().getResource("icons/menu_redo_16.gif")));
		redoButton.setPreferredSize(new Dimension(30, 20));
		redoButton.addActionListener(this);
		redoButton.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+Y");
		copyButton.setIcon(new ImageIcon(this.getClass()
				.getResource("icons/menu_copy_s16.gif")));
		copyButton.setPreferredSize(new Dimension(30, 20));
		copyButton.addActionListener(this);
		copyButton.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+C");
		cutButton.setIcon(new ImageIcon(this.getClass().getResource("icons/menu_cut_s16.gif")));
		cutButton.setPreferredSize(new Dimension(30, 20));
		cutButton.addActionListener(this);
		cutButton.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+X");
		pasteButton.setIcon(new ImageIcon(this.getClass().getResource(
				"icons/menu_paste_s16.gif")));
		pasteButton.setPreferredSize(new Dimension(30, 20));
		pasteButton.addActionListener(this);
		pasteButton.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+V");
		searchButton.setIcon(new ImageIcon(this.getClass().getResource(
				"icons/menu_find_s16.gif")));
		searchButton.setPreferredSize(new Dimension(30, 20));
		searchButton.addActionListener(this);
		searchButton.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+F");
		replaceButton.setIcon(new ImageIcon(this.getClass().getResource(
				"icons/menu_replace_s16.gif")));
		replaceButton.setPreferredSize(new Dimension(30, 20));
		replaceButton.addActionListener(this);
		replaceButton.setToolTipText(KeyEvent.getKeyModifiersText(mask) + "+R");

		Arrays.sort(BBCodes);
		BBCodeChoise = new JComboBox(BBCodes);
		BBCodeChoise.addActionListener(this);
		BBCodeChoise.setFont(new Font(BBCodeChoise.getFont().getName(), BBCodeChoise.getFont()
				.getStyle(), 11));
		BBCodeChoise.setPreferredSize(new Dimension(90, 20));

		Arrays.sort(RCVars);
		RCVarChoise = new JComboBox(RCVars);
		RCVarChoise.addActionListener(this);
		RCVarChoise.setFont(new Font(RCVarChoise.getFont().getName(), RCVarChoise.getFont()
				.getStyle(), 11));
		RCVarChoise.setPreferredSize(new Dimension(90, 20));

		Arrays.sort(Loop);
		LoopChoise = new JComboBox(Loop);
		LoopChoise.addActionListener(this);
		LoopChoise.setFont(new Font(LoopChoise.getFont().getName(), LoopChoise.getFont()
				.getStyle(), 11));
		LoopChoise.setPreferredSize(new Dimension(90, 20));

		Arrays.sort(Condition);
		ConditionChoise = new JComboBox(Condition);
		ConditionChoise.addActionListener(this);
		ConditionChoise.setFont(new Font(ConditionChoise.getFont().getName(),
				ConditionChoise.getFont().getStyle(), 11));
		ConditionChoise.setPreferredSize(new Dimension(90, 20));

		Arrays.sort(Special);
		SpecialChoise = new JComboBox(Special);
		SpecialChoise.addActionListener(this);
		SpecialChoise.setFont(new Font(SpecialChoise.getFont().getName(),
				SpecialChoise.getFont().getStyle(), 11));
		SpecialChoise.setPreferredSize(new Dimension(90, 20));

		Arrays.sort(Configuration);
		ConfigChoise = new JComboBox(Configuration);
		ConfigChoise.addActionListener(this);
		ConfigChoise.setFont(new Font(ConfigChoise.getFont().getName(), ConfigChoise.getFont()
				.getStyle(), 11));
		ConfigChoise.setPreferredSize(new Dimension(90, 20));

		commentButton.addActionListener(this);
		// commentButton.setPreferredSize(new Dimension(50,20));

		modified.setPreferredSize(new Dimension(10, 20));
		modified.setForeground(Color.red);
		modified.setFont(new Font(modified.getFont().getName(), Font.BOLD, modified.getFont()
				.getSize()));

		toolBar = new JToolBar();

		toolBar.add(newButton);
		toolBar.add(openButton);
		toolBar.add(saveButton);
		toolBar.add(saveasButton);
		toolBar.add(modified);
		toolBar.addSeparator();
		toolBar.add(undoButton);
		toolBar.add(redoButton);
		toolBar.addSeparator();
		toolBar.add(copyButton);
		toolBar.add(cutButton);
		toolBar.add(pasteButton);
		toolBar.addSeparator();
		toolBar.add(searchButton);
		toolBar.add(replaceButton);
		toolBar.addSeparator();
		toolBar.add(BBCodeChoise);
		toolBar.add(RCVarChoise);
		toolBar.add(LoopChoise);
		toolBar.add(ConditionChoise);
		toolBar.add(SpecialChoise);
		toolBar.add(ConfigChoise);
		toolBar.add(commentButton);

		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		toolContainer = new JToolBar();
		toolContainer.add(scrollBar = new ScrollableBar(toolBar));
		toolContainer.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				if (toolBar.getOrientation() != toolContainer.getOrientation()) {
					toolBar.setOrientation(toolContainer.getOrientation());
					scrollBar.setOrientation(toolContainer.getOrientation());
				}
			}
		});

		undoManager.setLimit(10000);

		editor.doc.addUndoableEditListener(this);
		editor.addKeyListener(this);
		editor.setMargin(new Insets(5, 5, 5, 5));

		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setViewportView(editor);

		lineNumbers = new JTextPane();
		lineNumbers.setFont(editor.getFont());
		lineNumbers.setMargin(new Insets(5, 2, 5, 2));

		final JPanel lineView = new JPanel();
		lineView.setLayout(new GridLayout(0, 1));

		lineNumbers.setText("1");
		lineNumbers.setEditable(false);
		lineNumbers.setEnabled(false);
		lineView.add(lineNumbers);
		lineView.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		editor.doc.addDocumentListener(documentListener = new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				lineNumberThreadWaiting = false;
			}

			public void insertUpdate(DocumentEvent e) {
				lineNumberThreadWaiting = false;
			}

			public void removeUpdate(DocumentEvent e) {
				lineNumberThreadWaiting = false;
			}
		});

		lineNumberThreadUpdate.start();

		scrollpane.setRowHeaderView(lineView);

		this.add(scrollpane);
		this.add(toolContainer, BorderLayout.NORTH);
		updateButtons();

		if (mdl != null) {
			pathfile = mdl;
			open(pathfile, Main.defaultCharset);
		}
	}

	public void updateButtons() {
		undoButton.setEnabled(undoManager.canUndo());
		redoButton.setEnabled(undoManager.canRedo());
	}

	public void undoableEditHappened(UndoableEditEvent e) {
		if (e.getEdit().isSignificant() && undoManager.isInProgress()) {
			undoManager.addEdit(e.getEdit());
			updateButtons();
			modified.setText("*");
		}
	}

	public void undo() {
		try {
			while (undoManager.canUndo()
					&& editor.doc.getText(0, editor.doc.getLength()).equals(editor.courant)) {
				undoManager.undo();
			}
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}
		updateButtons();
		editor.updateCourant();
	}

	public void redo() {
		int i = 0;
		try {
			while (undoManager.canRedo() && i < 2) {
				undoManager.redo();
				if (!editor.doc.getText(0, editor.doc.getLength()).equals(editor.courant)) {
					editor.updateCourant();
					i++;
				}
			}
			if (i >= 2)
				undoManager.undo();
		} catch (Exception ex) {
			ExceptionAlert.createExceptionAlert(ex);
			ex.printStackTrace();
		}
		updateButtons();
		editor.updateCourant();
	}

	public void actionPerformed(ActionEvent e) {
		String temp, temp2;
		// int i;
		editor.requestFocus();
		if (e.getSource().equals(undoButton))
			undo();
		else if (e.getSource().equals(redoButton))
			redo();
		else if (e.getSource().equals(newButton)) {
			if (isModified())
				if (JOptionPane.showConfirmDialog(Main.getParentFrame(),
						"The active document has been modified.\nDo you want save it?") == JOptionPane.OK_OPTION)
					save();
			pathfile = "";
			open(pathfile, Main.defaultCharset);
			undoManager.discardAllEdits();
			updateButtons();
		} else if (e.getSource().equals(openButton)) {
			if (isModified())
				if (JOptionPane.showConfirmDialog(Main.getParentFrame(),
						"The active document has been modified.\nDo you want save it?") == JOptionPane.OK_OPTION)
					save();
			String directory = Main.USER_HOME;
			JFileChooser chooser = new JFileChooser(directory);
			chooser.removeChoosableFileFilter(chooser.getFileFilter());
			chooser.addChoosableFileFilter(new MonFiltre(new String[] { "ogsc" }, "*.ogsc"));
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				pathfile = chooser.getSelectedFile().getAbsolutePath();
				open(pathfile, Main.defaultCharset);
				undoManager.discardAllEdits();
				updateButtons();
			}
		} else if (e.getSource().equals(saveButton)) {
			save();
		} else if (e.getSource().equals(saveasButton)) {
			saveas();
		} else if (e.getSource().equals(copyButton)) {
			editor.copy();
		} else if (e.getSource().equals(cutButton)) {
			editor.cut();
			editor.colorise(false);
		} else if (e.getSource().equals(pasteButton)) {
			editor.paste();
			editor.colorise(false);
		} else if (e.getSource().equals(searchButton)) {
			new Searcher(editor);
		} else if (e.getSource().equals(replaceButton)) {
			new SearcherReplacer(editor);
		} else if (e.getSource().equals(ConfigChoise) || e.getSource().equals(RCVarChoise)
				|| e.getSource().equals(SpecialChoise)) {
			if (((JComboBox) e.getSource()).getSelectedIndex() == 0)
				return;
			temp2 = ((JComboBox) e.getSource()).getSelectedItem().toString();
			editor.replaceSelection(temp2);
			editor.colorise(false);
			((JComboBox) e.getSource()).setSelectedIndex(0);
		} else if (e.getSource().equals(ConditionChoise) || e.getSource().equals(LoopChoise)
				|| e.getSource().equals(BBCodeChoise)) {
			if (((JComboBox) e.getSource()).getSelectedIndex() == 0)
				return;
			temp2 = ((JComboBox) e.getSource()).getSelectedItem().toString().substring(1);
			temp = editor.getSelectedText() != null ? editor.getSelectedText() : "";
			editor.replaceSelection("[" + temp2 + temp + "[/" + temp2);
			editor.colorise(("[" + temp2 + temp + "[/" + temp2).length());
			((JComboBox) e.getSource()).setSelectedIndex(0);
		} else if (e.getSource().equals(commentButton)) {
			temp = editor.getSelectedText() != null ? editor.getSelectedText() : "";
			editor.replaceSelection("/* " + temp + " */");
			editor.colorise(("/* " + temp + " */").length());
		}
	}

	public void open(String pathfile, String charset) {
		FileInputStream fileStream = null;
		InputStreamReader isr = null;
		try {
			if (!pathfile.equals("") && config.ismodelfileEditable(pathfile)) {
				File file = new File(pathfile);
				fileStream = new FileInputStream(file);
				isr = new InputStreamReader(fileStream, charset);
				editor.doc.removeUndoableEditListener(this);
				editor.read(isr, editor.doc);
				editor.doc = editor.getStyledDocument();
				editor.doc.addDocumentListener(documentListener);
				editor.courant = editor.doc.getText(0, editor.doc.getLength());
				int firstOption = editor.courant.indexOf("[option]");
				if (firstOption > 0) {
					String[] tmp = editor.courant.split("\\[\\/?option\\]");
					for (int i = 1; i < tmp.length; i += 2) {
						if (tmp[i].matches("(?s).*?\\n\\s*charset\\s*:\\s*.*?\\n")) {
							docCharset = tmp[i].replaceAll(
									"(?s).*?\\n\\s*charset\\s*:\\s*(.*?)[\\n$].*", "$1")
									.trim();
							if (!docCharset.equals(charset)) {
								open(pathfile, docCharset);
								return;
							}
						}
					}
				}
				editor.colorise(true);
				editor.doc.addUndoableEditListener(this);
				modified.setText("");
			} else if (pathfile.equals("")) {
				editor.doc.removeUndoableEditListener(this);
				String start = "[option]\n charset : " + charset + "\n[/option]\n";
				editor.doc.remove(0, editor.doc.getLength());
				editor.doc.insertString(0, start, null);
				editor.courant = start;
				editor.colorise(true);
				editor.doc.addUndoableEditListener(this);
				modified.setText("");
			}
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		} finally {
			try {
				if (isr != null)
					isr.close();
			} catch (IOException e) {
			}
			try {
				if (fileStream != null)
					fileStream.close();
			} catch (IOException e) {
			}
		}
	}

	public boolean isModified() {
		return !modified.getText().equals("");
	}

	public void save() {
		if (!pathfile.equals(""))
			save(pathfile);
		else
			saveas();
	}

	public void save(String pathfile) {
		PrintWriter pw = null;
		BufferedReader br = null;
		try {
			String line, text = editor.getText();
			br = new BufferedReader(new StringReader(text));
			File file = new File(pathfile);
			pw = new PrintWriter(file, docCharset);
			while ((line = br.readLine()) != null) {
				pw.println(line);
			}
			modified.setText("");
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		} finally {
			try {
				pw.close();
				br.close();
			} catch (Exception e) {
			}
		}
	}

	public void saveas() {
		String directory = Main.USER_HOME;
		JFileChooser chooser = new JFileChooser(directory);
		chooser.removeChoosableFileFilter(chooser.getFileFilter());
		chooser.addChoosableFileFilter(new MonFiltre(new String[] { "ogsc" }, "*.ogsc"));
		if (chooser.showSaveDialog(Main.getParentFrame()) == JFileChooser.APPROVE_OPTION) {
			pathfile = chooser.getSelectedFile().getAbsolutePath();
			if (!pathfile.endsWith(".ogsc"))
				pathfile += ".ogsc";
			save(pathfile);
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getModifiers() == mask) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_Z:
				if (undoManager.canUndo()) {
					undo();
					modified.setText("*");
				}
				break;

			case KeyEvent.VK_Y:
				if (undoManager.canRedo()) {
					redo();
					modified.setText("*");
				}
				break;

			case KeyEvent.VK_S:
				save();
				break;

			case KeyEvent.VK_F:
				new Searcher(editor);
				break;

			case KeyEvent.VK_R:
				new SearcherReplacer(editor);
				break;
			}
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_F5)
			editor.colorise(true);

		if (e.getModifiers() != mask)
			editor.colorise(false);
		else if (e.getKeyCode() == KeyEvent.VK_V || e.getKeyCode() == KeyEvent.VK_X)
			editor.colorise(false);
	}

	private UndoManager undoManager = new UndoManager();
	private JButton undoButton = new JButton();
	private JButton redoButton = new JButton();
	private JButton newButton = new JButton();
	private JButton openButton = new JButton();
	private JButton saveButton = new JButton();
	private JButton saveasButton = new JButton();
	private JButton cutButton = new JButton();
	private JButton copyButton = new JButton();
	private JButton pasteButton = new JButton();
	private JButton searchButton = new JButton();
	// private JButton nextsearchButton = new JButton();
	private JButton replaceButton = new JButton();

	private JComboBox BBCodeChoise;
	private JComboBox RCVarChoise;
	private JComboBox LoopChoise;
	private JComboBox ConditionChoise;
	private JComboBox SpecialChoise;
	private JComboBox ConfigChoise;
	private JButton commentButton = new JButton("/* */");

	private String docCharset = Main.defaultCharset;

	private String[] BBCodes = { "BBCodes", "[b]", "[center]", "[i]", "[u]", "[code]",
			"[quote]", "[img]", "[url_ogsconverter]", "[size=]", "[font=]", "[url=http://]",
			"[color=#]", "[wordcolor=#]", "[3color=#]" };
	private String[] RCVars = { "CR datas", "[date]", "[attacker]", "[attacker coordinates]",
			"[defender]", "[defender coordinates]", "[attacker weapons]",
			"[defender weapons]", "[attacker shielding]", "[defender shielding]",
			"[attacker armour]", "[defender armour]", "[n]", "[fleet]",
			"[fleet complete name]", "[fleet nb]", "[fleet start nb]", "[fleet end nb]",
			"[defense]", "[defense complete name]", "[defense nb]", "[defense start nb]",
			"[defense end nb]", "[fleet lose]", "[defense lose]", "[round]", "[after battle]",
			"[result]", "[captured metal]", "[captured cristal]", "[captured deuterium]",
			"[total captured metal]", "[total captured cristal]",
			"[total captured deuterium]", "[attacker losed]", "[defender losed]",
			"[attacker true losed]", "[defender true losed]", "[attacker metal losed]",
			"[attacker cristal losed]", "[attacker deuterium losed]",
			"[defender metal losed]", "[defender cristal losed]",
			"[defender deuterium losed]", "[total attacker metal losed]",
			"[total attacker cristal losed]", "[total attacker deuterium losed]",
			"[total defender metal losed]", "[total defender cristal losed]",
			"[total defender deuterium losed]", "[harvest metal]", "[harvest cristal]",
			"[total harvest metal]", "[total harvest cristal]", "[moon probability]",
			"[end cr]", "[attacker fire]", "[attacker firepower]",
			"[attacker shields absorb]", "[defender fire]", "[defender firepower]",
			"[defender shields absorb]", "[rentability attacker with]",
			"[rentability defender with]", "[rentability attacker without]",
			"[rentability defender without]", "[rentability attackers with]",
			"[rentability defenders with]", "[rentability attackers without]",
			"[rentability defenders without]", "[individual rentability with]",
			"[individual rentability without]", "[harvested]", "[harvestable]",
			"[harvests reports]", "[individual harvested]", "[individual harvestable]",
			"[harvest date]", "[harvest coordinates]", "[recycler nb]", "[recycler capacity]",
			"[metal]", "[crystal]", "[metal taken]", "[crystal taken]", "[rate]",
			"[CR index]", "[consumption]", "[total consumption]" };
	private String[] Loop = { "Loops", "[repeat attackers]", "[repeat defenders]",
			"[attacker fleet start]", "[defender fleet start]", "[defender defense start]",
			"[attacker fleet end]", "[defender fleet end]", "[defender defense end]",
			"[attackers]", "[defenders]", "[print harvest]" };
	private String[] Condition = { "Conditions", "[attacker destroyed]",
			"[attacker not destroyed]", "[defender destroyed start]",
			"[defender not destroyed start]", "[defender destroyed]",
			"[defender not destroyed]", "[moon created]", "[harvest]", "[end conversion]",
			"[one CR]", "[completely not harvested]", "[more one attacker]",
			"[more one defender]", "[individual harvest]", "[harvest report]",
			"[harvest before]", "[individual harvest before]", "[last]", "[first]" };
	private String[] Special = { "Others", "[line]", "[upper case][/upper case]",
			"[lower case][/lower case]", "[cell ][/cell]", "[calculate][/calculate]",
			"[if ][else][endif]", "[option][/option]", "get :", "set :no harvest",
			"set :no multi CR", "set :no CR with zero round", "set :no AGS", "define :",
			"defCalcul :", "charset : " + Main.defaultCharset };
	private String[] Configuration = { "Configurations", "[just cr end]", "[in column]",
			"[i am attacker]", "[display rentability]", "[just my rentability]",
			"[harvested cr]" };

	private int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
	private String pathfile = "";

	private Configuration config = new Configuration();
	private Editor editor = new Editor();
	private JLabel modified = new JLabel("");

	private JToolBar toolContainer;
	private JToolBar toolBar;
	private ScrollableBar scrollBar;

	private JTextPane lineNumbers;
	private volatile boolean lineNumberThreadWaiting = true;
	private DocumentListener documentListener;

	private Thread lineNumberThreadUpdate = new Thread() {
		public void run() {

			while (true) {

				for (int i = 0; lineNumberThreadWaiting && i < 50; i++) {
					try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				try {
					sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				try {
					int length = lineNumbers.getStyledDocument().getLength();
					String editorText = editor.doc.getText(0, editor.doc.getLength());
					String lineNumbersText = lineNumbers.getStyledDocument()
							.getText(0, length);

					int currLine = editorText.replaceAll("[^\\n]", "").length() + 1;
					int lines = lineNumbersText.replaceAll("[^\\n]", "").length() + 1;

					if (lines < currLine) {
						StringBuffer text = new StringBuffer();
						for (int i = lines + 1; i <= currLine; i++) {
							text.append("\n" + i);
						}
						lineNumbers.getStyledDocument().insertString(length, text.toString(),
								null);
					} else if (lines > currLine) {
						int id = lineNumbersText.lastIndexOf("\n" + (currLine + 1));
						if (id > 0) {
							lineNumbers.getStyledDocument().remove(id, length - id);
						}
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}

				lineNumberThreadWaiting = true;
			}
		}
	};

}

final class Searcher extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Searcher(Editor edit) {
		super();
		editor = edit;
		setTitle("OGSConverter - Search");
		setDefaultCloseOperation(0);
		setIconImage(new ImageIcon(this.getClass().getResource("images/icone.png")).getImage());// new
		// ImageIcon("images/icone.png").getImage());
		setSize(300, 150);
		setLocationRelativeTo(getParent());

		text = new JTextPane();
		text.setText(editor.getSelectedText());
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setViewportView(text);

		search = new JButton("Search");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					StyledDocument doc = text.getStyledDocument();
					if (!doc.getText(0, doc.getLength()).equals(""))
						editor.search(doc.getText(0, doc.getLength()));
					search.setText("next");
				} catch (Exception ex) {
					ExceptionAlert.createExceptionAlert(ex);
					ex.printStackTrace();
				}
			}
		});

		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		pan.add(scrollpane);
		pan.add(search, BorderLayout.SOUTH);
		this.getContentPane().add(pan);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				removeAll();
				dispose();
			}
		});

		this.setVisible(true);
	}

	private JButton search;
	private Editor editor;
	private JTextPane text;

}

final class SearcherReplacer extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearcherReplacer(Editor edit) {
		super();
		editor = edit;
		setTitle("OGSConverter - Search");
		setDefaultCloseOperation(0);
		setIconImage(new ImageIcon(this.getClass().getResource("images/icone.png")).getImage());// new
		// ImageIcon("images/icone.png").getImage());
		setSize(300, 150);
		setLocationRelativeTo(getParent());

		text = new JTextPane();
		text.setText(editor.getSelectedText());
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpane.setViewportView(text);

		replacement = new JTextPane();
		JScrollPane scrollpaneReplacement = new JScrollPane();
		scrollpaneReplacement.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollpaneReplacement.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneReplacement.setViewportView(replacement);

		search = new JButton("Search");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					StyledDocument doc = text.getStyledDocument();
					if (!doc.getText(0, doc.getLength()).equals(""))
						editor.search(doc.getText(0, doc.getLength()));
					search.setText("next");
				} catch (Exception ex) {
					ExceptionAlert.createExceptionAlert(ex);
					ex.printStackTrace();
				}
			}
		});

		replace = new JButton("Replace");
		replace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					StyledDocument doc = text.getStyledDocument();
					int l = doc.getLength();
					String s = doc.getText(0, l);
					String selected = editor.getSelectedText() == null ? ""
							: editor.getSelectedText();
					if (l != 0) {
						if (!selected.equals(s)) {
							editor.search(s);
						}
						selected = editor.getSelectedText() == null ? ""
								: editor.getSelectedText();
						if (selected.equals(s)) {
							editor.replaceSelection(replacement.getStyledDocument().getText(0,
									replacement.getStyledDocument().getLength()));
							editor.colorise(false);
						}
						editor.search(s);
						search.setText("next");
					}

				} catch (Exception ex) {
					ExceptionAlert.createExceptionAlert(ex);
					ex.printStackTrace();
				}
			}
		});

		JPanel pan4 = new JPanel();
		pan4.setLayout(new GridLayout(1, 2));
		pan4.add(search);
		pan4.add(replace);

		JPanel pan2 = new JPanel();
		pan2.setLayout(new GridLayout(2, 1));
		pan2.add(scrollpane);
		pan2.add(scrollpaneReplacement);

		JPanel pan3 = new JPanel();
		pan3.setLayout(new GridLayout(2, 1));
		pan3.add(new JLabel("Research:"));
		pan3.add(new JLabel("Replacement:"));

		JPanel pan1 = new JPanel();
		pan1.setLayout(new BorderLayout());
		pan1.add(pan2);
		pan1.add(pan4, BorderLayout.SOUTH);
		pan1.add(pan3, BorderLayout.WEST);
		this.getContentPane().add(pan1);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				removeAll();
				dispose();
			}
		});

		this.setVisible(true);
	}

	private JButton search;
	private JButton replace;
	private Editor editor;
	private JTextPane text;
	private JTextPane replacement;

}
