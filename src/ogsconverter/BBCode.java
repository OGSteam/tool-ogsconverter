/*
 * BBCode.java
 *
 * Created on 15 avril 2006, 17:26
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package ogsconverter;

/**
 * 
 * @author Ben.12
 */
abstract class BBCode {

	/** Creates a new instance of BBCode */
	/*
	 * public BBCode() { setBBCode(); }
	 */

	/**
	 * Initialise le BBCode a utiliser selon les preference dans le fichier
	 * config.ini
	 */
	public static void setBBCode() {
		try {
			Configuration config = new Configuration("config.ini");

			String[] tmp = config.getConfig("BBCode_center").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			center = tmp[1].split("<\\|>");
			fcenter = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_size").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			size = tmp[1].split("<\\|>");
			fsize = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_bold").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			bold = tmp[1].split("<\\|>");
			fbold = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_ita").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			ita = tmp[1].split("<\\|>");
			fita = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_under").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			under = tmp[1].split("<\\|>");
			funder = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_bcolor").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			bcolor = tmp[1].split("<\\|>");
			fbcolor = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_code").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			code = tmp[1].split("<\\|>");
			fcode = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_quote").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			quote = tmp[1].split("<\\|>");
			fquote = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_font").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			font = tmp[1].split("<\\|>");
			ffont = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_img").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			img = tmp[1].split("<\\|>");
			fimg = tmp[2].split("<\\|>");

			tmp = config.getConfig("BBCode_url").split("<<\\|>>");
			if (tmp.length != 3)
				tmp = new String[] { tmp[0], "", "" };
			url = tmp[1].split("<\\|>");
			furl = tmp[2].split("<\\|>");

			index[0] = Integer.parseInt(config.getConfig("code"));
			index[1] = Integer.parseInt(config.getConfig("center"));
			index[2] = Integer.parseInt(config.getConfig("size"));
			index[3] = Integer.parseInt(config.getConfig("bold"));
			index[4] = Integer.parseInt(config.getConfig("ita"));
			index[5] = Integer.parseInt(config.getConfig("under"));
			index[6] = Integer.parseInt(config.getConfig("bcolor"));
			index[7] = Integer.parseInt(config.getConfig("quote"));
			index[8] = Integer.parseInt(config.getConfig("font"));
			index[9] = Integer.parseInt(config.getConfig("img"));
			index[10] = Integer.parseInt(config.getConfig("url"));

			if (index[0] >= code.length)
				index[0] = 0;
			if (index[1] >= center.length)
				index[1] = 0;
			if (index[2] >= size.length)
				index[2] = 0;
			if (index[3] >= bold.length)
				index[3] = 0;
			if (index[4] >= ita.length)
				index[4] = 0;
			if (index[5] >= under.length)
				index[5] = 0;
			if (index[6] >= bcolor.length)
				index[6] = 0;
			if (index[7] >= quote.length)
				index[7] = 0;
			if (index[8] >= font.length)
				index[8] = 0;
			if (index[9] >= img.length)
				index[9] = 0;
			if (index[10] >= url.length)
				index[10] = 0;

		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static String BBtoHTML(String text) {

		String tmp, tmp2;
		int i, j, jj;
		int infinity;
		setBBCode();

		String original = text;

		if (index[0] != 0) {
			j = infinity = 0;
			while ((jj = text.indexOf(code[index[0]], j)) != -1) {
				if (infinity > 10000) {
					ExceptionAlert.createExceptionAlert(new Exception(
							"Error of bbcode to html conversion:\n" + original));
					break;
				}
				infinity++;
				j = jj;
				tmp = text.substring(j + code[index[0]].length(), j
						+ text.substring(j).indexOf(fcode[index[0]]));
				tmp2 = tmp.replaceAll(" ", "&nbsp;");
				text = text.replaceFirst("\\Q" + code[index[0]] + tmp + fcode[index[0]]
						+ "\\E", code[index[0]] + tmp2 + fcode[index[0]]);
				j += (code[index[0]] + tmp2 + fcode[index[0]]).length();
			}
		}

		if (index[1] != 0) {
			text = text.replaceAll("(?s)\\Q" + center[index[1]] + "\\E", "<center>");
			text = text.replaceAll("(?s)\\Q" + fcenter[index[1]] + "\\E", "</center>");
		}
		if (index[2] != 0) {
			text = text.replaceAll("(?s)\\Q"
					+ size[index[2]].replaceAll("\\$1", "\\\\E((?:\\\\d|\\\\w)+)\\\\Q")
					+ "\\E", "[[size$1size]]");
		}
		if (index[3] != 0) {
			text = text.replaceAll("(?s)\\Q" + bold[index[3]] + "\\E", "<b>");
			text = text.replaceAll("(?s)\\Q" + fbold[index[3]] + "\\E", "</b>");
		}
		if (index[4] != 0) {
			text = text.replaceAll("(?s)\\Q" + ita[index[4]] + "\\E", "<i>");
			text = text.replaceAll("(?s)\\Q" + fita[index[4]] + "\\E", "</i>");
		}
		if (index[5] != 0) {
			text = text.replaceAll("(?s)\\Q" + under[index[5]] + "\\E", "<u>");
			text = text.replaceAll("(?s)\\Q" + funder[index[5]] + "\\E", "</u>");
		}
		if (index[6] != 0) {
			text = text.replaceAll("(?s)\\Q"
					+ bcolor[index[6]].replaceAll("\\$1", "\\\\E((?:\\\\d|\\\\w)+)\\\\Q")
					+ "\\E", "<font color='\\#$1'>");
			text = text.replaceAll("(?s)\\Q"
					+ fbcolor[index[6]].replaceAll("\\$1", "\\\\E((?:\\\\d|\\\\w)+)\\\\Q")
					+ "\\E", "</font>");
		}
		if (index[0] != 0) {
			text = text.replaceAll(
					"(?s)\\Q" + code[index[0]] + "\\E",
					"<table border='1'><tr><td class='ogsconverterColor'>code:<br><font face='Courier,Courier New'>");
			text = text.replaceAll("(?s)\\Q" + fcode[index[0]] + "\\E",
					"</font></td></tr></table>");
		}
		if (index[7] != 0) {
			text = text.replaceAll("(?s)\\Q" + quote[index[7]] + "\\E",
					"<table border='1'><tr><td class='ogsconverterColor'>quote:<br>");
			text = text.replaceAll("(?s)\\Q" + fquote[index[7]] + "\\E", "</td></tr></table>");
		}
		if (index[8] != 0) {
			text = text.replaceAll(
					"(?s)\\Q"
							+ font[index[8]].replaceAll("\\$1",
									"\\\\E((?:\\\\d|\\\\w|\\\\s|,)+)\\\\Q") + "\\E",
					"<font face='$1'>");
			text = text.replaceAll("(?s)\\Q"
					+ ffont[index[8]].replaceAll("\\$1",
							"\\\\E((?:\\\\d|\\\\w|\\\\s|,)+)\\\\Q") + "\\E", "</font>");
		}
		if (index[9] != 0) {
			if (img[index[9]].indexOf("$1") > -1)
				text = text.replaceAll("(?s)\\Q"
						+ img[index[9]].replaceAll("\\$1", "\\\\E(\\\\S+)\\\\Q") + "\\E",
						"<img src='$1");
			else
				text = text.replaceAll("(?s)\\Q" + img[index[9]] + "\\E", "<img src='");
			if (fimg[index[9]].indexOf("$1") > -1)
				text = text.replaceAll("(?s)\\Q"
						+ fimg[index[9]].replaceAll("\\$1", "\\\\E(\\\\S+)\\\\Q") + "\\E",
						"$1' alt='img' >");
			else
				text = text.replaceAll("(?s)\\Q" + fimg[index[9]] + "\\E", "' alt='img' >");
		}
		if (index[10] != 0) {
			text = text.replaceAll("(?s)\\Q"
					+ url[index[10]].replaceAll("\\$1", "\\\\E(\\\\S+)\\\\Q") + "\\E",
					"<a href='$1'>");
			text = text.replaceAll("(?s)\\Q"
					+ furl[index[10]].replaceAll("\\$1", "\\\\E(\\\\S+)\\\\Q") + "\\E", "</a>");
		}

		infinity = 0;
		while (text.indexOf("[[size") != -1) {
			if (infinity > 10000) {
				ExceptionAlert.createExceptionAlert(new Exception(
						"Error of bbcode to html conversion:\n" + original));
				break;
			}
			infinity++;
			tmp = text.substring(text.indexOf("[[size") + 6, text.indexOf("size]]"));
			i = Integer.parseInt(tmp);
			if (i < 8) {
				text = text.replaceFirst("\\[\\[size\\d+size\\]\\]", "<font size='" + tmp
						+ "'>");
				text = text.replaceAll("(?s)\\Q"
						+ fsize[index[2]].replaceAll("\\$1", "\\\\E((?:\\\\d|\\\\w)+)\\\\Q")
						+ "\\E", "</font>");
			} else if (i > 30) {
				text = text.replaceFirst("\\[\\[size(\\d+)size\\]\\]",
						"<span style='font-size:" + i + "%;'>");
				text = text.replaceAll("(?s)\\Q"
						+ fsize[index[2]].replaceAll("\\$1", "\\\\E((?:\\\\d|\\\\w)+)\\\\Q")
						+ "\\E", "</span>");
			} else {
				text = text.replaceFirst("\\[\\[size(\\d+)size\\]\\]",
						"<span style='font-size:" + i + "px;'>");
				text = text.replaceAll("(?s)\\Q"
						+ fsize[index[2]].replaceAll("\\$1", "\\\\E((?:\\\\d|\\\\w)+)\\\\Q")
						+ "\\E", "</span>");
			}
		}

		text = text.replaceAll("\\n", "<br>\n");

		return text;
	}

	public static String addcenter(int e) {

		if (index[1] == 0)
			return "";

		if (e == 0)
			return center[index[1]];
		else
			return fcenter[index[1]];
	}

	public static String addsize(int e, String sz) {

		if (index[2] == 0)
			return "";

		if (e == 0 && size[index[2]].indexOf("$1") < 0 && fsize[index[2]].indexOf("$1") < 0)
			return size[index[2]] + sz.replaceAll("\\\\", "");

		if (e == 0)
			return size[index[2]].replaceAll("\\$1", sz);
		else
			return fsize[index[2]].replaceAll("\\$1", sz);
	}

	public static String addbold(int e) {

		if (index[3] == 0)
			return "";

		if (e == 0)
			return bold[index[3]];
		else
			return fbold[index[3]];
	}

	public static String addita(int e) {

		if (index[4] == 0)
			return "";

		if (e == 0)
			return ita[index[4]];
		else
			return fita[index[4]];
	}

	public static String addunder(int e) {

		if (index[5] == 0)
			return "";

		if (e == 0)
			return under[index[5]];
		else
			return funder[index[5]];
	}

	public static String addbcolor(int e, String c) {

		if (index[6] == 0)
			return "";

		if (e == 0 && bcolor[index[6]].indexOf("$1") < 0
				&& fbcolor[index[6]].indexOf("$1") < 0)
			return bcolor[index[6]] + c.replaceAll("\\\\", "");

		if (e == 0)
			return bcolor[index[6]].replaceAll("\\$1", c);
		else
			return fbcolor[index[6]].replaceAll("\\$1", c);
	}

	public static String addcode(int e) {

		if (index[0] == 0)
			return "";

		if (e == 0)
			return code[index[0]];
		else
			return fcode[index[0]];
	}

	public static String addquote(int e) {

		if (index[7] == 0)
			return "";

		if (e == 0)
			return quote[index[7]];
		else
			return fquote[index[7]];
	}

	public static String addfont(int e, String c) {

		if (index[8] == 0)
			return "";

		if (e == 0 && font[index[8]].indexOf("$1") < 0 && ffont[index[8]].indexOf("$1") < 0)
			return font[index[8]] + c.replaceAll("\\\\", "");

		if (e == 0)
			return font[index[8]].replaceAll("\\$1", c);
		else
			return ffont[index[8]].replaceAll("\\$1", c);
	}

	public static String addimg(int e, String c) {

		if (index[9] == 0)
			return "";

		if (e == 0 && img[index[9]].indexOf("$1") < 0 && fimg[index[9]].indexOf("$1") < 0)
			return img[index[9]] + c.replaceAll("\\\\", "");

		if (e == 0)
			return img[index[9]].replaceAll("\\$1", c);
		else
			return fimg[index[9]].replaceAll("\\$1", c);
	}

	public static String addurl(int e, String c) {

		if (index[10] == 0)
			return "";

		if (e == 0 && url[index[10]].indexOf("$1") < 0 && furl[index[10]].indexOf("$1") < 0)
			return url[index[10]] + c.replaceAll("\\\\", "");

		if (e == 0)
			return url[index[10]].replaceAll("\\$1", c);
		else
			return furl[index[10]].replaceAll("\\$1", c);
	}

	public static String[] center;
	public static String[] size;
	public static String[] bold;
	public static String[] ita;
	public static String[] under;
	public static String[] bcolor;
	public static String[] code;
	public static String[] quote;

	public static String[] font;
	public static String[] img;
	public static String[] url;

	public static String[] fcenter;
	public static String[] fsize;
	public static String[] fbold;
	public static String[] fita;
	public static String[] funder;
	public static String[] fbcolor;
	public static String[] fcode;
	public static String[] fquote;

	public static String[] ffont;
	public static String[] fimg;
	public static String[] furl;

	public static int index[] = new int[11];
}
