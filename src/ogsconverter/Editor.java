/*
 * Editor.java
 *
 * Created on 2 octobre 2006, 17:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ogsconverter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author MOREAU Benoît
 */
public class Editor extends JTextPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of Editor */
	public Editor() {
		super();
		this.setFont(new Font("Courier", this.getFont().getStyle(), this.getFont().getSize()));
		this.setCaretColor(Color.BLUE);

		try {
			doc = this.getStyledDocument();

			//Style "normal":
			normal = doc.addStyle("normal", null);
			StyleConstants.setForeground(normal, Color.black);
			StyleConstants.setBold(normal, false);

			//Style BBCode:
			BBCodeStyle = doc.addStyle("BBCode", null);
			StyleConstants.setForeground(BBCodeStyle, new Color(0, 102, 153));
			StyleConstants.setBold(BBCodeStyle, true);

			//Style nombre:
			Nombre = doc.addStyle("Nombre", null);
			StyleConstants.setForeground(Nombre, new Color(150, 20, 20));
			StyleConstants.setBold(Nombre, false);

			//Style operateur:
			OperatorStyle = doc.addStyle("Operateur", null);
			StyleConstants.setForeground(OperatorStyle, Color.black);
			StyleConstants.setBold(OperatorStyle, true);

			//Style commentaire:
			CommentStyle = doc.addStyle("Comment", null);
			StyleConstants.setForeground(CommentStyle, Color.gray);
			StyleConstants.setBold(CommentStyle, false);

			//Style RCVar:
			RCVarStyle = doc.addStyle("RCVar", null);
			StyleConstants.setForeground(RCVarStyle, new Color(80, 170, 230));
			StyleConstants.setBold(RCVarStyle, true);

			//Style Special:
			SpecialStyle = doc.addStyle("Special", null);
			StyleConstants.setForeground(SpecialStyle, new Color(0, 170, 102));
			StyleConstants.setBold(SpecialStyle, true);

			//Style Boucles:
			BouclesStyle = doc.addStyle("Boucles", null);
			StyleConstants.setForeground(BouclesStyle, new Color(0, 120, 80));
			StyleConstants.setBold(BouclesStyle, true);

			//Style Conditions:
			ConditionsStyle = doc.addStyle("Conditions", null);
			StyleConstants.setForeground(ConditionsStyle, new Color(153, 102, 255));
			StyleConstants.setBold(ConditionsStyle, true);

			//Style Literal:
			Literal = doc.addStyle("Literal", null);
			StyleConstants.setForeground(Literal, new Color(153, 0, 204));
			StyleConstants.setBold(Literal, false);

			//Style Param:
			Param = doc.addStyle("Param", null);
			StyleConstants.setForeground(Param, new Color(20, 20, 204));
			StyleConstants.setBold(Param, false);

			//Style Option:
			Option = doc.addStyle("Option", null);
			StyleConstants.setForeground(Option, new Color(255, 132, 0));
			StyleConstants.setBold(Option, false);

			//Style Config:
			Config = doc.addStyle("Config", null);
			StyleConstants.setForeground(Config, new Color(100, 100, 70));
			StyleConstants.setBold(Config, true);

			//Style Erreur:
			Erreur = doc.addStyle("Erreur", null);
			StyleConstants.setBackground(Erreur, new Color(255, 180, 180));

			//UserCode Style
			UserCodeStyle = doc.addStyle("UserCode", null);
			StyleConstants.setForeground(UserCodeStyle, new Color(255, 132, 0));
			StyleConstants.setItalic(UserCodeStyle, true);

			this.colorise(0, doc.getLength(), doc.getText(0, doc.getLength()));

			courant = doc.getText(0, doc.getLength());
			//this.addKeyListener(this);

		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}

	public void colorise(int length) {
		try {
			String text = doc.getText(0, doc.getLength());
			int curs, i, j, k;

			if (!courant.equals(text)) {

				curs = this.getCaretPosition();
				k = length;
				if ((i = curs - 50) < 0)
					i = 0;
				if ((j = curs + 50) > text.length())
					j = text.length();

				if (k > 0)
					if ((i -= k) < 0)
						i = 0;

				for (; i > 0; i--) {
					if (text.charAt(i) == '\n')
						break;
					else if (text.charAt(i) == '\r')
						break;
				}
				k = text.indexOf("\n", j);
				if (k == -1)
					k = text.indexOf("\r", j);
				if (k == -1)
					k = text.length();
				j = k;

				this.colorise(i, j, text.substring(i, j));

				this.setStyledDocument(doc);
				this.setCaretPosition(curs);
			}

			courant = doc.getText(0, doc.getLength());
		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}

	public void colorise(boolean all) {
		try {
			String text = doc.getText(0, doc.getLength());
			int curs, i, j, k;
			String tmp = courant;
			courant = text;

			if (!tmp.equals(text) && !all) {

				curs = this.getCaretPosition();

				if (doc.getText(
						curs - 2 > 0 ? curs - 2 : 0,
						2 < text.length() - (curs - 2 > 0 ? curs - 2 : 0) ? 2 : text.length()
								- (curs - 2 > 0 ? curs - 2 : 0)).equals("*/")) {
					this.colorise(true);
					return;
				}

				k = text.length() - tmp.length();

				if (k > 0
						&& doc.getText(
								(curs - k - 5 > 0 ? curs - k - 5 : 0),
								k + 10 < text.length() - (curs - k - 5 > 0 ? curs - k - 5 : 0) ? k + 10
										: text.length()
												- (curs - k - 5 > 0 ? curs - k - 5 : 0))
								.indexOf("*/") != -1) {
					this.colorise(true);
					return;
				}

				if ((i = curs - 50) < 0)
					i = 0;
				if ((j = curs + 50) > text.length())
					j = text.length();

				if (k > 0)
					if ((i -= k) < 0)
						i = 0;

				for (; i > 0; i--) {
					if (text.charAt(i) == '\n')
						break;
					else if (text.charAt(i) == '\r')
						break;
				}
				k = text.indexOf("\n", j);
				if (k == -1)
					k = text.indexOf("\r", j);
				if (k == -1)
					k = text.length();
				j = k;

				this.colorise(i, j, text.substring(i, j));

				this.setStyledDocument(doc);
				this.setCaretPosition(curs);
			} else if (all)
				this.colorise(0, doc.getLength(), text);

		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}

	public void colorise(int start, int end, String text) {
		int i = 0, j, k, d, f;
		String mot, tmp[], masq, masq2 = ".";
		Matcher matcher;

		doc.setCharacterAttributes(start, end - start, normal, true);

		colorise(Operator, OperatorStyle, text, start);
		colorise(RCVar, RCVarStyle, text, start);
		colorise(Special, SpecialStyle, text, start);
		colorise(Boucles, BouclesStyle, text, start);
		colorise(Conditions, ConditionsStyle, text, start);
		colorise(Configuration, Config, text, start);

		for (k = 0; k < BBCode2.length; k++) {
			mot = BBCode2[k];
			tmp = BBCode2Masq[k].split("<<\\|>>");
			masq = tmp[0];
			if (tmp.length > 1)
				masq2 = tmp[1];
			i = j = 0;
			while ((j = text.indexOf(mot, i)) != -1) {
				i = j;
				if ((j = text.indexOf("]", i)) != -1) {
					if (Pattern.compile("^\\" + mot + "=" + masq + "\\]$", Pattern.MULTILINE)
							.matcher(text.substring(i, j + 1))
							.find()) {
						doc.setCharacterAttributes(i + start, mot.length() + 1, BBCodeStyle,
								true);
						doc.setCharacterAttributes(j + start, 1, BBCodeStyle, true);
						i += mot.length() + 1;
						matcher = Pattern.compile("(" + masq + ")", Pattern.MULTILINE)
								.matcher(text.substring(i, j));
						d = f = 0;
						while (matcher.find(f)) {
							f = matcher.end();
							d = matcher.start();
							doc.setCharacterAttributes(d + start + i, f - d, Nombre, true);
						}
						if (tmp.length > 1) {
							matcher = Pattern.compile("(" + masq2 + ")", Pattern.MULTILINE)
									.matcher(text.substring(i, j));
							d = f = 0;
							while (matcher.find(f)) {
								f = matcher.end();
								d = matcher.start();
								doc.setCharacterAttributes(d + start + i, f - d,
										OperatorStyle, true);
							}
						}
						i = j;
					} else {
						doc.setCharacterAttributes(i + start, mot.length(), BBCodeStyle, true);
						doc.setCharacterAttributes(i + start, mot.length(), Erreur, false);
						i += mot.length();
					}
				} else
					i += mot.length();

			}
		}

		colorise(BBCode, BBCodeStyle, text, start);

		mot = "[cell ";
		masq = "(\\s|(wid?th=['\"]([^\\r\\n]+?)['\"])?|motif=['\"]([^\\r\\n]{1})['\"]|align=['\"](left|right|center)['\"]|color=['\"](#(attacker|great number|number|background|defender|fleet|defense|fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|(\\d|[A-F]){6})|none)['\"]|font=['\"]([\\d\\w\\s,_\\-]+)['\"])*";
		i = j = 0;
		while ((j = text.indexOf(mot, i)) != -1) {
			i = j;
			if ((j = text.indexOf("]", i)) != -1) {
				if (Pattern.compile("^\\" + mot + masq + "\\]$", Pattern.MULTILINE).matcher(
						text.substring(i, j + 1)).find()) {
					doc.setCharacterAttributes(i + start, mot.length(), SpecialStyle, true);
					doc.setCharacterAttributes(j + start, 1, SpecialStyle, true);
					i += mot.length();
					matcher = Pattern.compile("(['\"].*?['\"])", Pattern.MULTILINE).matcher(
							text.substring(i, j));
					d = f = 0;
					while (matcher.find(f)) {
						f = matcher.end();
						d = matcher.start();
						doc.setCharacterAttributes(d + start + i, f - d, Literal, true);
					}
					matcher = Pattern.compile("(wid?th|motif|align|color|font)",
							Pattern.MULTILINE).matcher(text.substring(i, j));
					d = f = 0;
					while (matcher.find(f)) {
						f = matcher.end();
						d = matcher.start();
						doc.setCharacterAttributes(d + start + i, f - d, Param, true);
					}
					i = j;
				} else {
					doc.setCharacterAttributes(i + start, mot.length(), SpecialStyle, true);
					doc.setCharacterAttributes(i + start, mot.length(), Erreur, false);
					i += mot.length();
				}
			} else
				i += mot.length();
		}

		mot = "[if ";
		masq = "\\s*(?:"
				+ "(?:\\d+|\\[[^\\r\\n]+?\\])\\s*(?:\\<|\\>|i\\s*(?=\\[|\\d|-\\d)|s\\s*(?=\\[|\\d|-\\d)|\\!?\\=)\\s*(?:\\d+|\\[[^\\r\\n]+?\\])|"
				+ "(?:'[^\\r\\n']*?'|\"[^\\r\\n\"]*?\")(?:=|!=)(?:'[^\\r\\n']*?'|\"[^\\r\\n\"]*?\")"
				+ ")\\s*";
		i = j = 0;
		while ((j = text.indexOf(mot, i)) != -1) {
			i = j;
			matcher = Pattern.compile("^(\\" + mot + masq + "\\])", Pattern.MULTILINE)
					.matcher(text.substring(i));
			if (matcher.find()) {
				j = matcher.end() + i - 1;
				doc.setCharacterAttributes(i + start, mot.length(), SpecialStyle, true);
				doc.setCharacterAttributes(j + start, 1, SpecialStyle, true);
				i += mot.length();
				matcher = Pattern.compile(
						"(\\<|\\>|i\\s*(?=\\[|\\d|-\\d)|s\\s*(?=\\[|\\d|-\\d)|\\!?\\=)",
						Pattern.MULTILINE).matcher(text.substring(i, j));
				d = f = 0;
				while (matcher.find(f)) {
					f = matcher.end();
					d = matcher.start();
					doc.setCharacterAttributes(d + start + i, f - d, OperatorStyle, true);
				}
				matcher = Pattern.compile("(\\d+)", Pattern.MULTILINE).matcher(
						text.substring(i, j));
				d = f = 0;
				while (matcher.find(f)) {
					f = matcher.end();
					d = matcher.start();
					doc.setCharacterAttributes(d + start + i, f - d, Nombre, true);
				}
				i = j;
			} else {
				doc.setCharacterAttributes(i + start, mot.length(), SpecialStyle, true);
				doc.setCharacterAttributes(i + start, mot.length(), Erreur, false);
				i += mot.length();
			}
		}

		matcher = Pattern.compile(
				"(get\\s*:\\s*|set\\s*:\\s*(no harvest|no multi CR|no CR with zero round|no AGS)\\s*[\\n\\r]|define\\s*:\\s*|defCalcul\\s*:\\s*|charset\\s*:\\s*)",
				Pattern.MULTILINE)
				.matcher(text);
		d = f = 0;
		while (matcher.find(f)) {
			f = matcher.end();
			d = matcher.start();
			doc.setCharacterAttributes(d + start, f - d, Option, true);
		}

		if (d != 0 && f != 0)
			setUserCode();

		tmp = new String[UserCode.size()];
		for (f = 0; f < UserCode.size(); f++)
			tmp[f] = (String) UserCode.get(f);
		colorise(tmp, UserCodeStyle, courant, 0);

		matcher = Pattern.compile("get\\s*:\\s*[^\\|]*?\\|(text|number)\\((\\d+)\\)",
				Pattern.MULTILINE).matcher(text);
		d = f = 0;
		while (matcher.find(f)) {
			if (matcher.groupCount() >= 2) {
				f = matcher.end(1);
				d = matcher.start(1);
				doc.setCharacterAttributes(d + start, f - d, RCVarStyle, true);
				f = matcher.end(2);
				d = matcher.start(2);
				doc.setCharacterAttributes(d + start, f - d, Nombre, true);
			} else
				break;
		}

		coloriseComments();
	}

	public void coloriseComments() {
		int i, j;
		Matcher matcher;
		String text = "";
		try {
			text = doc.getText(0, doc.getLength());
		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}

		matcher = Pattern.compile("(\\/\\*.*?\\*\\/)", Pattern.MULTILINE | Pattern.DOTALL)
				.matcher(text);
		i = j = 0;
		while (matcher.find(i)) {
			i = matcher.end();
			j = matcher.start();
			doc.setCharacterAttributes(j, i - j, CommentStyle, true);
		}
	}

	public void colorise(String[] tab, Style style, String text, int start) {
		String mot;
		int k, i, j;
		if (tab == null)
			return;
		for (k = 0; k < tab.length; k++) {
			mot = tab[k];
			i = j = 0;
			while ((j = text.indexOf(mot, i)) != -1) {
				i = j;
				doc.setCharacterAttributes(i + start, mot.length(), style, true);
				i += mot.length();
			}
		}
	}

	public void setUserCode() {
		Matcher matcher;
		int d, f;
		String[] tmp;
		tmp = new String[UserCode.size()];
		for (f = 0; f < UserCode.size(); f++)
			tmp[f] = (String) UserCode.get(f);

		UserCode.clear();

		matcher = Pattern.compile("(?:get|defCalcul)\\s*\\:[\\t ]*([^\\n\\r]+?)\\|",
				Pattern.MULTILINE).matcher(courant);
		d = f = 0;
		while (matcher.find(f)) {
			if (matcher.groupCount() > 0) {
				f = matcher.end(1);
				d = matcher.start(1);
				UserCode.add("[" + courant.substring(d, f).trim() + "]");
			}
		}
		matcher = Pattern.compile("define\\s*\\:[\\t ]*([^\\n\\r]+?)\\=", Pattern.MULTILINE)
				.matcher(courant);
		d = f = 0;
		while (matcher.find(f)) {
			if (matcher.groupCount() > 0) {
				f = matcher.end(1);
				d = matcher.start(1);
				UserCode.add("[" + courant.substring(d, f).trim() + "]");
			}
		}

		colorise(tmp, normal, courant, 0);
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public void setSize(Dimension d) {
		if (d.width < getParent().getSize().width) {
			d.width = getParent().getSize().width;
		}
		super.setSize(d);
	}

	public void search(String text) {
		int carret = this.getCaretPosition();
		int pos = courant.indexOf(text, carret);
		if (pos != -1) {
			this.select(pos, pos + text.length());
		} else {
			carret = 0;
			if ((pos = courant.indexOf(text, carret)) != -1) {
				this.select(pos, pos + text.length());
			}
		}
	}

	public void updateCourant() {
		try {
			this.courant = doc.getText(0, doc.getLength());
		} catch (BadLocationException ble) {
			ble.printStackTrace();
		}
	}

	Style BBCodeStyle;
	Style normal;
	Style Nombre;
	Style OperatorStyle;
	Style CommentStyle;
	Style RCVarStyle;
	Style SpecialStyle;
	Style BouclesStyle;
	Style ConditionsStyle;
	Style Param;
	Style Option;
	Style Literal;
	Style Erreur;
	Style Config;
	Style UserCodeStyle;

	StyledDocument doc;
	String courant;
	List UserCode = new ArrayList();

	String[] BBCode = { "[b]", "[center]", "[i]", "[u]", "[code]", "[quote]", "[img]",
			"[url_ogsconverter]", "[/b]", "[/center]", "[/i]", "[/u]", "[/code]", "[/quote]",
			"[/img]", "[/url_ogsconverter]" };
	String[] BBCode2 = { "[size", "[/size", "[font", "[/font", "[url", "[/url", "[color",
			"[/color", "[wordcolor", "[/wordcolor", "[3color", "[/3color" };
	String[] BBCode2Masq = {
			"[^\\]\\[]+",
			"[^\\]\\[]+?", // size
			"[^\\]\\[]+",
			"[^\\]\\[]+?", // font
			"[\\w\\d\\-\\%\\.\\/\\:&\\?\\=\\\\]+?",
			"[\\w\\d\\-\\%\\.\\/\\:&\\?\\=\\\\]+?", // url
			"(#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})|#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})_to_#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6}))<<|>>_to_", // [color=
			"(#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})|#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})_to_#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6}))<<|>>_to_", // (/color=
			"(#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})-to-){1,2}#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})<<|>>-to-", // [wordcolor=
			"(#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})-to-){1,2}#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})<<|>>-to-", // [/wordcolor=
			"(#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})-to-){2}#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})<<|>>-to-", // [3color=
			"(#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})-to-){2}#(fleet_defense_(?:1[0-9]|2[0-1]|0?[1-9])|attacker|great number|number|background|defender|fleet|defense|(\\d|[A-F]){6})<<|>>-to-" }; // [/3color=
	String[] RCVar = { "[date]", "[attacker]", "[attacker coordinates]", "[defender]",
			"[defender coordinates]", "[attacker weapons]", "[defender weapons]",
			"[attacker shielding]", "[defender shielding]", "[attacker armour]",
			"[defender armour]", "[n]", "[fleet]", "[fleet complete name]", "[fleet nb]",
			"[fleet start nb]", "[fleet end nb]", "[defense]", "[defense complete name]",
			"[defense nb]", "[defense start nb]", "[defense end nb]", "[fleet lose]",
			"[defense lose]", "[round]", "[after battle]", "[result]", "[captured metal]",
			"[captured cristal]", "[captured deuterium]", "[total captured metal]",
			"[total captured cristal]", "[total captured deuterium]", "[attacker losed]",
			"[defender losed]", "[attacker true losed]", "[defender true losed]",
			"[attacker metal losed]", "[attacker cristal losed]",
			"[attacker deuterium losed]", "[defender metal losed]",
			"[defender cristal losed]", "[defender deuterium losed]",
			"[defender def metal losed]", "[defender def cristal losed]",
			"[defender def deuterium losed]", "[defender flt metal losed]",
			"[defender flt cristal losed]", "[defender flt deuterium losed]",
			"[total attacker metal losed]", "[total attacker cristal losed]",
			"[total attacker deuterium losed]", "[total defender metal losed]",
			"[total defender cristal losed]", "[total defender deuterium losed]",
			"[harvest metal]", "[harvest cristal]", "[total harvest metal]",
			"[total harvest cristal]", "[moon probability]", "[end cr]", "[attacker fire]",
			"[attacker firepower]", "[attacker shields absorb]", "[defender fire]",
			"[defender firepower]", "[defender shields absorb]",
			"[rentability attacker with]", "[rentability defender with]",
			"[rentability attacker without]", "[rentability defender without]",
			"[rentability attackers with]", "[rentability defenders with]",
			"[rentability attackers without]", "[rentability defenders without]",
			"[individual rentability with]", "[individual rentability without]",
			"[harvested]", "[harvestable]", "[harvests reports]", "[individual harvested]",
			"[individual harvestable]", "[harvest date]", "[harvest coordinates]",
			"[recycler nb]", "[recycler capacity]", "[metal]", "[crystal]", "[metal taken]",
			"[crystal taken]", "[rate]", "[CR index]", "[consumption]", "[total consumption]" };
	String[] Special = { "[line]", "[upper case]", "[/upper case]", "[lower case]", "[/lower case]", "[/cell]", "[last]", "[/last]", "[first]", "[/first]",
			"[calculate]", "[/calculate]", "[else]", "[endif]", "[option]", "[/option]" };
	String[] Operator = { "^", "(", ")", "+", "-", "*", "/", "|" };
	String[] Boucles = { "[repeat attackers]", "[/repeat attackers]", "[repeat defenders]",
			"[/repeat defenders]", "[attacker fleet start]", "[/attacker fleet start]",
			"[defender fleet start]", "[/defender fleet start]", "[defender defense start]",
			"[/defender defense start]", "[attacker fleet end]", "[/attacker fleet end]",
			"[defender fleet end]", "[/defender fleet end]", "[defender defense end]",
			"[/defender defense end]", "[attackers]", "[/attackers]", "[defenders]",
			"[/defenders]", "[print harvest]", "[/print harvest]" };
	String[] Conditions = { "[attacker destroyed]", "[/attacker destroyed]",
			"[attacker not destroyed]", "[/attacker not destroyed]",
			"[defender destroyed start]", "[/defender destroyed start]",
			"[defender not destroyed start]", "[/defender not destroyed start]",
			"[defender destroyed]", "[/defender destroyed]", "[defender not destroyed]",
			"[/defender not destroyed]", "[moon created]", "[/moon created]", "[harvest]",
			"[/harvest]", "[end conversion]", "[/end conversion]", "[one CR]", "[/one CR]",
			"[completely not harvested]", "[/completely not harvested]",
			"[more one attacker]", "[/more one attacker]", "[more one defender]",
			"[/more one defender]", "[individual harvest]", "[/individual harvest]",
			"[harvest report]", "[/harvest report]", "[harvest before]", "[/harvest before]",
			"[individual harvest before]", "[/individual harvest before]" };
	String[] Configuration = { "[just cr end]", "[in column]", "[i am attacker]",
			"[display rentability]", "[just my rentability]", "[harvested cr]" };

}
