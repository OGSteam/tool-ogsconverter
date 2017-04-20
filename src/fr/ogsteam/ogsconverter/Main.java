/*
 * Main.java
 *
 * Created on 31 mars 2006, 22:56
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package fr.ogsteam.ogsconverter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MOREAU Benoît
 */
public class Main extends OGSConstantes {

    /**
     * Creates a new instance of Main
     */
    public Main(boolean jnlp, String[] args) {
        int i;
        String tmp[];
        String mdl = null;

        if (JWS || !(new File("OGSConverter.exe")).exists()) {
            SplashWindow.startSplashWindow(this.getClass().getResource("images/ban1.png"),
                    3000);
        }

        menu = System.getProperty("apple.laf.useScreenMenuBar");
        if (menu != null && !menu.equals("true"))
            System.setProperty("apple.laf.useScreenMenuBar", "true");

        osenter = System.getProperty("line.separator");
        if (osenter == null || jnlp)
            osenter = String.valueOf((char) 13) + String.valueOf((char) 10);

        JWS = jnlp;

        if (JWS) {
            USER_URL = this.getClass().getResource("").toString();
            USER_HOME = System.getProperty("user.home");
            USER_LOADER = this.getClass().getClassLoader();
            ogsc_url = "http://www.ogsconverter.com";
        } else {
            USER_URL = System.getProperty("user.dir");
            USER_HOME = System.getProperty("user.dir");
            prefs = Preferences.userNodeForPackage(Fenetre.class);
        }

        Configuration config = null;

        try {
            config = new Configuration("config.ini");

            if (JWS) {
                if (args != null) {
                    for (i = 1; i < args.length; i++) {
                        tmp = args[i].split("_value_");
                        if (tmp.length >= 2) {
                            config.setConfig(htmlspecialchars_decode(new String(
                                            tmp[0].getBytes(), "UTF-8")),
                                    htmlspecialchars_decode(new String(tmp[1].getBytes(),
                                            "UTF-8")));
                        } else if (tmp.length == 1) {
                            config.setConfig(htmlspecialchars_decode(new String(
                                    tmp[0].getBytes(), "UTF-8")), "");
                        }
                    }
                }
            } else if (args != null) {
                mdl = args[0];
            }
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e);
            e.printStackTrace();
        }

        try {
            if (config.getConfig("update").equals("1")) {
                Thread.sleep(1000);

                File f = new File("OGSUpdate.jar");
                if (f.exists())
                    f.delete();

                f = new File("ICE_JNIRegistry.dll");
                if (f.exists())
                    f.delete();

                config.setConfig("update", "0");
            }
        } catch (Exception e) {
        }

        try {

            if (!config.fileisok("config.ini")) {
                System.exit(0);
            } else if (!config.fileisok("lang_" + config.getConfig("active_language") + ".ini")) {
                if (config.getConfig("active_language").equals("fr"))
                    System.exit(0);
            } else if (!config.fileisok("lang_fr.ini")) {
                System.exit(0);
            } else {
                if (!JWS) {
                    loadskin();
                    java.lang.reflect.Method method = JFrame.class.getMethod(
                            "setDefaultLookAndFeelDecorated", new Class[]{boolean.class});
                    method.invoke(null, new Object[]{Boolean.TRUE});

                    method = JDialog.class.getMethod("setDefaultLookAndFeelDecorated",
                            new Class[]{boolean.class});
                    method.invoke(null, new Object[]{Boolean.TRUE});
                } else {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                fen = new Fenetre(mdl);
                fen.run();
            }
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Quick conversion!
     */
    public Main(String[] args) {
        Map originalConfigs = new HashMap();
        String report = "";
        Clipboard clip_bord = Toolkit.getDefaultToolkit().getSystemClipboard();
        Configuration configC = null, configL = null;

        JWS = false;
        USER_URL = System.getProperty("user.dir");
        USER_HOME = System.getProperty("user.dir");

        osenter = System.getProperty("line.separator");
        if (osenter == null)
            osenter = String.valueOf((char) 13) + String.valueOf((char) 10);

        try {
            configC = new Configuration("config.ini");
            configL = new Configuration("lang_" + configC.getConfig("active_language")
                    + ".ini");

            // Vérifie la version pour la compatibilité
            if (args.length > 1 && !args[1].equals("1.1")) {
                String[] oldArgs = args;
                int newSize = 2 + ((oldArgs.length > 1 && !oldArgs[1].equals("0")) ? 1 : 0)
                        + ((oldArgs.length > 2 && !oldArgs[2].equals("0")) ? 1 : 0);
                args = new String[newSize];
                args[1] = "2.0";
                if (oldArgs.length > 1 && !oldArgs[1].equals("0")) {
                    args[2] = "-Dcolor=" + oldArgs[1];
                }
                if (oldArgs.length > 2 && !oldArgs[2].equals("0")) {
                    args[4] = "-Dactive_model=" + oldArgs[2];
                }
            }

            // init des configs
            for (int i = 2; i < args.length; i++) {
                String cKey = "", cValue = "";
                if (args[i].startsWith("-D")) {
                    int equalId = args[i].indexOf('=');
                    cKey = args[i].substring(2, equalId);
                    cValue = args[i].substring(equalId + 1);
                } else {
                    continue;
                }

                // Cas particulier:
                if (cKey.equals("bbNameChoosen")) {
                    String[] bbConfigs = new String[]{"center", "size", "bold", "ita",
                            "under", "bcolor", "code", "quote", "font", "img", "url"};
                    String[] bbNames = configC.getConfig("BBname").split("\\|");
                    for (int j = 0; i < bbNames.length; j++) {
                        if (cValue.equals(bbNames[j])) {
                            String[] bbIndexesChoosen = configC.getConfig("BBcodes").split(
                                    "\\|");
                            bbIndexesChoosen = bbIndexesChoosen[j].split(",");
                            for (int bbIndex = 0; bbIndex < bbConfigs.length; bbIndex++) {
                                String originalValue = configC.getConfig(bbConfigs[bbIndex]);
                                configC.setConfig(bbConfigs[bbIndex],
                                        bbIndexesChoosen[bbIndex]);
                                originalConfigs.put(bbConfigs[bbIndex], originalValue);
                            }
                        }
                    }
                    continue;
                }
                String originalValue = configC.getConfig(cKey);
                configC.setConfig(cKey, cValue);
                originalConfigs.put(cKey, originalValue);
            }

            if (clip_bord.getContents(this).isDataFlavorSupported(DataFlavor.stringFlavor))
                report = clip_bord.getContents(this)
                        .getTransferData(DataFlavor.stringFlavor)
                        .toString();

            if (Pattern.compile(configL.getConfig("TOP"), Pattern.CASE_INSENSITIVE).matcher(
                    report).find()
                    || Pattern.compile(configL.getConfig("TOP2"), Pattern.CASE_INSENSITIVE)
                    .matcher(report)
                    .find()
                    || (configC.getConfig("CR_end").equals("1") && (Pattern.compile(
                    configL.getConfig("END1"), Pattern.CASE_INSENSITIVE).matcher(
                    report).find()
                    || Pattern.compile(configL.getConfig("END2"),
                    Pattern.CASE_INSENSITIVE).matcher(report).find() || Pattern.compile(
                    configL.getConfig("END3"), Pattern.CASE_INSENSITIVE)
                    .matcher(report)
                    .find()))) {

                if (configC.getConfig("active_model").equals("no model"))
                    report = BBCode.addquote(0) + Main.converter(report, 100)
                            + BBCode.addquote(1);
                else
                    report = Main.personnal_converter(report, 100);
            } else if (Pattern.compile(configL.getConfig("REC_TOP"), Pattern.CASE_INSENSITIVE)
                    .matcher(report)
                    .find()) {
                report = BBCode.addquote(0) + (Main.converterRecy(report, Main.OUT_ATT))[0]
                        + BBCode.addquote(1);
            } else if (Pattern.compile(configL.getConfig("ER_TOP"), Pattern.CASE_INSENSITIVE)
                    .matcher(report)
                    .find()) {
                report = BBCode.addquote(0) + Main.converterER(report) + BBCode.addquote(1);
            } else if (Pattern.compile(configL.getConfig("ALLY_TOP"), Pattern.CASE_INSENSITIVE)
                    .matcher(report)
                    .find()) {
                report = BBCode.addquote(0) + Main.converterALLY(report) + BBCode.addquote(1);
            } else if (Pattern.compile(configL.getConfig("EMP_TOP"), Pattern.CASE_INSENSITIVE)
                    .matcher(report)
                    .find()) {
                report = BBCode.addquote(0) + Main.converterEMP(report) + BBCode.addquote(1);
            } else if (Pattern.compile(
                    MSK_FLOTTE_TOP.replaceFirst("\\[fleet\\]", configL.getConfig("ER_FLEET")))
                    .matcher(report)
                    .find()) {
                report = BBCode.addquote(0) + Main.converterFLEET(report) + BBCode.addquote(1);
            }

            clip_bord.setContents(new StringSelection(report), null);
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e);
            e.printStackTrace();
        } finally {
            try {
                Set entry = originalConfigs.entrySet();
                Iterator it = entry.iterator();
                while (it.hasNext()) {
                    Entry config = (Entry) it.next();
                    configC.setConfig((String) config.getKey(), (String) config.getValue());
                }
            } catch (Exception e) {
                ExceptionAlert.createExceptionAlert(e);
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("jnlp"))
            new Main(true, args);
        else if (args.length > 0 && args[0].equals("quick"))
            new Main(args);
        else
            new Main(false, args.length > 0 ? args : null);
    }

    public static String personnal_converter(String text, int speed) {
        // long t2, t1 = Calendar.getInstance().get(Calendar.MINUTE) * 1000000
        // + Calendar.getInstance().get(Calendar.SECOND) * 1000
        // + Calendar.getInstance().get(Calendar.MILLISECOND);

        String setting = "0", repl, part = "", fichier = "", tmp[] = null, tmp2[] = null, line, exploded[];
        String charset = null;
        int val[] = null;
        StringBuffer textchanged = new StringBuffer();
        String[] fleet_complete = null, def_complete = null;
        List fleet = null, def = null, ifleet = null, idef = null;
        int i, il = 0, j, k, start, round = 0;
        boolean ok, go_end = false, att_destr[], def_destr[], harvest = false, link_open = false, link_close = false;
        int pts[][] = {{2000, 0, 0}, {1500, 500, 0}, {6000, 2000, 0},
                {20000, 15000, 2000}, {2000, 6000, 0}, {50000, 50000, 30000},
                {10000, 10000, 0}, {50000, 50000, 0}};
        int fpts[][] = {{2000, 2000, 0}, {6000, 6000, 0}, {3000, 1000, 0},
                {6000, 4000, 0}, {20000, 7000, 2000}, {45000, 15000, 0},
                {10000, 20000, 10000}, {10000, 6000, 2000}, {0, 1000, 0},
                {50000, 25000, 15000}, {0, 2000, 500}, {60000, 50000, 15000},
                {5000000, 4000000, 1000000}, {30000, 40000, 15000}};
        long defpts = 0, courante_attgain = 0;
        long attgain = 0, defgain = 0, tt_cdr = 0, tt_pris = 0;
        String _top1 = "", _top2 = "", _att = "", _def = "", _type = "", _nb = "", _techno = "", _moon = "", _lose = "", _rec_top = "", _iamatt = "", _inter_round = "", _afterbattle = "", _end1 = "", _end2 = "", _end3 = "", _dest = "", _gain = "";
        BBCode.setBBCode();
        String model = "";
        long icdr, ipris;
        long _att_true_losed = 0, _def_true_losed = 0;
        long[] att_perte_par_ress = {0, 0, 0}, att_courante_par_ress = {0, 0, 0};
        long[] def_def_perte_par_ress = {0, 0, 0}, def_flt_perte_par_ress = {0, 0, 0};
        long jl, kl;
        int infinity, t, s;
        String _justrcend = "", _incolumn = "", _disprenta = "", _justmyrenta = "", _CR_harvested = "";
        Set _diff_att = new HashSet();
        Set _diff_def = new HashSet();
        Configuration iniConfig = null;
        Configuration langConfig = null;

        StringBuffer modelString = new StringBuffer("");

        try {
            iniConfig = new Configuration("config.ini");

            setting = iniConfig.getConfig("color");

            if (setting.equals("01")) {
                color = DARKCOLOR;
            } else if (setting.equals("02")) {
                color = LIGHTCOLOR;
            } else {
                color = iniConfig.getConfig("user_color" + setting).split(",");
            }

            setting = setting.substring(0, 1);

            fichier = "lang_" + iniConfig.getConfig("active_language") + ".ini";

            langConfig = new Configuration(fichier);

            text = text.replaceAll(langConfig.getConfig("textaera"), "");
            text = text.replaceAll("Bad copy !", "");

            tmp = langConfig.getConfig("FLEET").split("\\|");
            fleet = new ArrayList(Arrays.asList(tmp));
            tmp = langConfig.getConfig("DEFENCES").split("\\|");
            def = new ArrayList(Arrays.asList(tmp));

            tmp = StringOperation.sansAccent(langConfig.getConfig("FLEET"))
                    .toLowerCase()
                    .replace('.', ' ')
                    .replaceAll("\\s+", "")
                    .split("\\|");
            ifleet = new ArrayList(Arrays.asList(tmp));
            tmp = StringOperation.sansAccent(langConfig.getConfig("DEFENCES"))
                    .toLowerCase()
                    .replace('.', ' ')
                    .replaceAll("\\s+", "")
                    .split("\\|");
            idef = new ArrayList(Arrays.asList(tmp));

            fleet_complete = langConfig.getConfig("ER_FLEET").split("\\|");
            def_complete = langConfig.getConfig("ER_DEFENCES").split("\\|");

            _top1 = langConfig.getConfig("TOP");
            _top2 = langConfig.getConfig("TOP2");
            _att = langConfig.getConfig("ATT");
            _def = langConfig.getConfig("DEF");
            _type = langConfig.getConfig("TYPE");
            _nb = langConfig.getConfig("NB");
            _techno = langConfig.getConfig("TECHNO");
            _moon = langConfig.getConfig("MOONF");
            _lose = langConfig.getConfig("LOSE");
            _rec_top = langConfig.getConfig("REC_TOP");
            _iamatt = iniConfig.getConfig("iamatt");
            _inter_round = langConfig.getConfig("INTER_ROUND");
            _afterbattle = iniConfig.getConfig("afterbattle");
            _end1 = langConfig.getConfig("END1");
            _end2 = langConfig.getConfig("END2");
            _end3 = langConfig.getConfig("END3");
            _dest = langConfig.getConfig("DEST");
            _gain = langConfig.getConfig("GAIN");

            _justrcend = iniConfig.getConfig("CR_end");
            _incolumn = iniConfig.getConfig("column");
            _disprenta = iniConfig.getConfig("cr_taux");
            _justmyrenta = iniConfig.getConfig("myrenta");
            _CR_harvested = iniConfig.getConfig("CR_harvested");

            model = iniConfig.getConfig("active_model") + ".ogsc";

        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e);
            e.printStackTrace();
        }

        exText = text;
        text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])", "$1"
                + String.valueOf((char) 10) + "$2");
        text = text.replaceAll(String.valueOf((char) 13), "");
        text = text.replaceAll("(?s)[^\\n\\r](" + _top1 + "|" + _top2 + "|" + _rec_top + ")",
                "\n$1");

        exploded = text.split(String.valueOf((char) 10));

        // nb de RC:
        tmp = ("0 " + text).split("(" + _top1 + "|" + _top2 + ")");
        int RC_nb = tmp.length - 1;

        // nb d'attaquant / RC:
        int att_nb[] = new int[RC_nb], def_nb[] = new int[RC_nb];
        j = 0;
        for (i = 0; i < tmp.length; i++) {
            if (!Pattern.compile(_att + MSK_NAME_COORD, Pattern.CASE_INSENSITIVE).matcher(
                    tmp[i]).find())
                continue;
            repl = (tmp[i].split(_inter_round))[0];
            att_nb[j] = (repl.split(_att + MSK_NAME_COORD).length) - 1;
            def_nb[j] = (repl.split(_def + MSK_NAME_COORD).length) - 1;
            j++;
        }

        char c;
        String _date = "xx.xx.xxxx xx:xx:xx", _attacker[][] = new String[RC_nb][], _attacker_coordinates[], _defender[][] = new String[RC_nb][], _defender_coordinates[];
        String _attacker_weapons[], _defender_weapons[], _attacker_shielding[], _defender_shielding[], _attacker_armour[], _defender_armour[];
        Map[] _attacker_fleet_start, _defender_fleet_start, _defender_defense_start;
        Map[] _attacker_fleet_end, _defender_fleet_end, _defender_defense_end;
        String _round = "0", _result = "";
        String _captured_metal = "0", _captured_cristal = "0", _captured_deuterium = "0";
        String _end_cr = "";
        String aa[] = new String[RC_nb + 1], as[] = new String[RC_nb + 1], da[] = new String[RC_nb + 1], ds[] = new String[RC_nb + 1];
        String _attacker_losed = "", _defender_losed = "", _harvest_metal = "", _harvest_cristal = "", _moon_probability = "0%";
        boolean _moon_created = false;
        int RC_index;
        String _harvests_reports = "";
        long cdr[] = new long[RC_nb], pris[] = new long[RC_nb];
        long _total_captured_metal = 0, _total_captured_cristal = 0, _total_captured_deut = 0;
        long _total_attacker_lose[] = new long[3];
        long _total_defender_lose[] = new long[3];
        long _total_harvest_metal = 0, _total_harvest_cristal = 0;
        long consumption[] = new long[RC_nb], consumption_total = 0;
        boolean are_harvest;
        int att_index = 0, def_index = 0, repeat_att_index, repeat_def_index;
        boolean AGS = true;

        for (j = 0; j < RC_nb; j++) {
            if (att_nb[j] <= 0)
                att_nb[j] = 1;
            if (def_nb[j] <= 0)
                def_nb[j] = 1;
        }

        tmp = null;

        if (Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE).matcher(text).find())
            are_harvest = true;
        else
            are_harvest = false;

        long _fire[][] = new long[RC_nb][2], _firepower[][] = new long[RC_nb][2], _shields_absorb[][] = new long[RC_nb][2];

        List modeloption = new ArrayList();
        List modelget = new ArrayList();
        List modelgetted = new ArrayList();
        List modeldefCalcul = new ArrayList();
        List modeldefCalculValue = new ArrayList();

        List repeat_att = new ArrayList();
        List repeat_def = new ArrayList();
        StringBuffer repeat = new StringBuffer();

        List harvestInfo = new ArrayList();

        boolean no_harvest = false, multi_cr = true, zero_round = true;
        repeat_att_index = -1;
        repeat_def_index = -1;
        i = 0;

        BufferedReader Buffer = getModelBuffer(model, defaultCharset);

        if (Buffer == null) {
            return "Not Found Model\n";
        }

        try {
            il = 1;
            while ((line = Buffer.readLine()) != null) {
                repl = "";
                while (line.endsWith("\\") && !line.endsWith("\\\\") && repl != null) {
                    line = line.substring(0, line.length() - 1);
                    repl = Buffer.readLine();
                    repl = repl.replaceAll("^\\s*(\\S)", "$1");
                    line += repl == null ? "" : repl;
                }

                line = line.replaceAll("^\\s*(\\S)?", "$1");
                line = line.replaceAll("\\/\\*.*?\\*\\/", "");
                line = line.replaceAll("\\\\([\"'])", "$1");

                infinity = 0;
                k = -7;
                while ((k = line.indexOf("#fleet_defense_", k + 7)) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop!\n\nL'erreur peut être dans cette ligne:\n"
                                + line);
                        return exText;
                    }
                    repl = line.substring(k + 15).replaceAll("^(0?[1-9]|1[0-9]|2[0-1])\\].*$",
                            "$1");
                    line = line.replaceAll("#fleet_defense_" + repl, "#"
                            + color[(int) exptoint(repl) + 1]);
                }
                line = line.replaceAll("#number", "#" + color[fleet.size() + def.size() + 2]);
                line = line.replaceAll("#great number", "#"
                        + color[fleet.size() + def.size() + 3]);
                line = line.replaceAll("#attacker", "#" + color[0]);
                line = line.replaceAll("#defender", "#" + color[1]);
                line = line.replaceAll("#background", "#" + color[color.length - 1]);

                if (line.equals(""))
                    continue;
                if (line.indexOf("[option]") != -1)
                    i = 1;
                if (line.indexOf("[/option]") != -1)
                    i = 0;
                if ((j = line.indexOf("[repeat attackers]")) != -1) {
                    repeat_att.add(new StringBuffer());
                    repeat_att_index++;
                    if (j != 0) {
                        modelString.append(line.substring(0, j));
                        line = line.substring(j);
                    }
                    i = 4;
                }
                if ((j = line.indexOf("[/repeat attackers]")) != -1 && i == 2) {
                    if (j != 0) {
                        ((StringBuffer) repeat_att.get(repeat_att_index)).append(line.substring(
                                0, j));
                        line = line.substring(j);
                    }
                    i = 0;
                }
                if ((j = line.indexOf("[repeat defenders]")) != -1) {
                    repeat_def.add(new StringBuffer());
                    repeat_def_index++;
                    if (j != 0) {
                        modelString.append(line.substring(0, j));
                        line = line.substring(j);
                    }
                    i = 5;
                }
                if ((j = line.indexOf("[/repeat defenders]")) != -1 && i == 3) {
                    if (j != 0) {
                        ((StringBuffer) repeat_def.get(repeat_def_index)).append(line.substring(
                                0, j));
                        line = line.substring(j);
                    }
                    i = 0;
                }
                line = line.replaceAll("\\[\\/?option\\]", "");
                line = line.replaceAll("\\[\\/?repeat attackers\\]", "");
                line = line.replaceAll("\\[\\/?repeat defenders\\]", "");
                if (line.equals(""))
                    continue;

                switch (i) {
                    case 0:
                        modelString.append(line);
                        break;
                    case 1:
                        if (Pattern.compile("^\\s*set\\s*:\\s*no harvest\\s*$",
                                Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                            no_harvest = true;
                            are_harvest = false;
                        }
                        if (Pattern.compile("^\\s*set\\s*:\\s*no multi CR\\s*$",
                                Pattern.CASE_INSENSITIVE).matcher(line).find())
                            multi_cr = false;
                        if (Pattern.compile("^\\s*set\\s*:\\s*no CR with zero round\\s*$",
                                Pattern.CASE_INSENSITIVE).matcher(line).find())
                            zero_round = false;
                        if (Pattern.compile("^\\s*set\\s*:\\s*no AGS\\s*$",
                                Pattern.CASE_INSENSITIVE).matcher(line).find())
                            AGS = false;

                        if (Pattern.compile("^\\s*define\\s*:\\s*", Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            line = line.replaceFirst(
                                    "^\\s*define\\s*:\\s*(\\S[^\\=]*\\=[\\S\\s]+)$", "$1");
                            tmp = line.split("\\=");
                            if (tmp.length < 2)
                                continue;
                            modeloption.add((Object) (line));
                        }

                        if (Pattern.compile("^\\s*defCalcul\\s*:\\s*", Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            tmp = line.replaceFirst(
                                    "^\\s*defCalcul\\s*:\\s*(\\S[^\\|]*\\|[^\\|]+\\|[\\S\\s]*\\S)\\s*$",
                                    "$1")
                                    .split("\\|");
                            if (tmp.length != 3)
                                continue;
                            modeldefCalcul.add((Object) (tmp[0] + "|" + tmp[1] + "|" + tmp[2]));
                            modeldefCalculValue.add((Object) (new Long(exptoint(tmp[2]))));
                        }

                        if (Pattern.compile("^\\s*get\\s*:\\s*", Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            tmp = line.replaceFirst(
                                    "^\\s*get\\s*:\\s*(\\S[^\\|]*\\|(?:text\\(\\d+\\)|number\\(\\d+\\))\\|[^\\|]*\\|[\\S\\s]+)$",
                                    "$1")
                                    .split("\\|");
                            if (tmp.length != 4)
                                continue;
                            modelget.add((Object) (tmp[0] + "|" + tmp[1] + "|" + tmp[2] + "|" + tmp[3]));
                        }
                        if (charset == null
                                && Pattern.compile("^\\s*charset\\s*:\\s*",
                                Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                            charset = line.replaceFirst("^\\s*charset\\s*:\\s*(.*)$", "$1");
                            Buffer.close();

                            Buffer = getModelBuffer(model, charset);

                            il = 1;
                            i = 0;
                            modelString = new StringBuffer();
                            repeat_att.clear();
                            repeat_def.clear();
                            modelget.clear();
                            modeldefCalculValue.clear();
                            modeldefCalcul.clear();
                            modeloption.clear();
                            continue;
                        }
                        break;
                    case 4:
                        modelString.append("[repeat attackers]");
                        i = 2;
                    case 2:
                        ((StringBuffer) repeat_att.get(repeat_att_index)).append((Object) line);
                        break;
                    case 5:
                        modelString.append("[repeat defenders]");
                        i = 3;
                    case 3:
                        ((StringBuffer) repeat_def.get(repeat_def_index)).append((Object) line);
                        break;
                }
                il++;
            }
            Buffer.close();

            if (i > 1)
                Print_exception("[repeat attackers] or [repeat defenders] not closed");

        } catch (Exception e) {
            Except ex = new Except(e);
            Print_exception("Error in read of model at line " + il + "\n\n"
                    + ex.ExceptoString());
            e.printStackTrace();
        }

        if (modeldefCalcul.size() > 0) {
            for (i = 0; i < modeldefCalcul.size(); i++) {
                tmp = modeldefCalcul.get(i).toString().split("\\s*\\|\\s*");
                modelString.append("<getCalcul").append(i).append(">[").append(tmp[0]).append(
                        "]</getCalcul>");
            }
        }

        il = 0;

        for (RC_index = 0; RC_index < RC_nb; RC_index++) {

            if (!multi_cr && RC_index != 0) {
                Print_exception("This model don't accept multi RCs.");
                break;
            }

            if (!AGS && (att_nb[RC_index] > 1 || def_nb[RC_index] > 1)) {
                Print_exception("This model don't accept AGS RC.");
                break;
            }

            ok = false;
            att_index = 0;
            def_index = 0;
            _moon_created = false;
            go_end = false;
            _date = "xx.xx.xxxx xx:xx:xx";
            _attacker[RC_index] = new String[att_nb[RC_index]];
            _defender[RC_index] = new String[def_nb[RC_index]];
            _attacker_coordinates = new String[att_nb[RC_index]];
            _defender_coordinates = new String[def_nb[RC_index]];
            _attacker_weapons = new String[att_nb[RC_index]];
            _defender_weapons = new String[def_nb[RC_index]];
            _attacker_shielding = new String[att_nb[RC_index]];
            _defender_shielding = new String[def_nb[RC_index]];
            _attacker_armour = new String[att_nb[RC_index]];
            _defender_armour = new String[def_nb[RC_index]];
            def_destr = new boolean[def_nb[RC_index]];
            att_destr = new boolean[att_nb[RC_index]];
            _attacker_fleet_start = new TreeMap[att_nb[RC_index]];
            _defender_fleet_start = new TreeMap[def_nb[RC_index]];
            _defender_defense_start = new TreeMap[def_nb[RC_index]];
            _attacker_fleet_end = new TreeMap[att_nb[RC_index]];
            _defender_fleet_end = new TreeMap[def_nb[RC_index]];
            _defender_defense_end = new TreeMap[def_nb[RC_index]];
            for (i = 0; i < att_nb[RC_index]; i++) {
                _attacker_weapons[i] = "xx";
                _attacker_shielding[i] = "xx";
                _attacker_armour[i] = "xx";
                att_destr[i] = false;
                _attacker_fleet_start[i] = new TreeMap();
                _attacker_fleet_end[i] = new TreeMap();
            }
            for (i = 0; i < def_nb[RC_index]; i++) {
                _defender_weapons[i] = "xx";
                _defender_shielding[i] = "xx";
                _defender_armour[i] = "xx";
                def_destr[i] = false;
                _defender_fleet_start[i] = new TreeMap();
                _defender_defense_start[i] = new TreeMap();
                _defender_fleet_end[i] = new TreeMap();
                _defender_defense_end[i] = new TreeMap();
            }
            _moon_probability = "0%";
            _round = "0";
            _captured_metal = "0";
            _captured_cristal = "0";
            _captured_deuterium = "0";
            defpts = 0;
            courante_attgain = 0;
            attgain = 0;
            defgain = 0;
            harvest = false;
            def_def_perte_par_ress = new long[3];
            def_flt_perte_par_ress = new long[3];
            att_perte_par_ress = new long[3];
            att_courante_par_ress = new long[3];
            harvestInfo = new ArrayList();

            for (; il < exploded.length; il++) {
                line = exploded[il].trim();
                if (line.equals(""))
                    continue;
                try {

                    if (!ok
                            && (Pattern.compile(_top1, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find() || Pattern.compile(_top2, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find())) {
                        line = line.replaceAll(MSK_DATE, "$1");
                        if (iniConfig.getConfig("RC_date").equals("1")
                                && !line.equals(exploded[il].trim()))
                            _date = line;
                        ok = true;
                        go_end = false;
                        part = "attack";
                        courante_attgain = 0;
                        continue;
                    }

                    if (ok) {

                        if (!go_end
                                && Pattern.compile(_inter_round, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            tmp = line.replaceFirst("\\D*", "").split("\\D{2,}");
                            _fire[RC_index][0] += exptoint(tmp[0]);
                            _firepower[RC_index][0] += exptoint(tmp[1]);
                            if (tmp.length < 3) {
                                il++;
                                tmp = exploded[il].replaceFirst("\\D*", "").split("\\D{2,}");
                                _shields_absorb[RC_index][0] += exptoint(tmp[0]);
                            } else
                                _shields_absorb[RC_index][0] += exptoint(tmp[2]);
                            il++;
                            while (exploded[il].trim().equals("")) {
                                il++;
                            }
                            tmp = exploded[il].replaceFirst("\\D*", "").split("\\D{2,}");
                            _fire[RC_index][1] += exptoint(tmp[0]);
                            _firepower[RC_index][1] += exptoint(tmp[1]);
                            if (tmp.length < 3) {
                                il++;
                                tmp = exploded[il].replaceFirst("\\D*", "").split("\\D{2,}");
                                _shields_absorb[RC_index][1] += exptoint(tmp[0]);
                            } else
                                _shields_absorb[RC_index][1] += exptoint(tmp[2]);
                            continue;
                        }

                        if (Pattern.compile("^" + _att + MSK_NAME_COORD_1,
                                Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                            if (part.equals("def") && !go_end) {
                                round = 0;
                                for (j = il; j < exploded.length; j++) {
                                    if (Pattern.compile(_lose, Pattern.CASE_INSENSITIVE)
                                            .matcher(exploded[j])
                                            .find())
                                        break;

                                    if (Pattern.compile(_inter_round, Pattern.CASE_INSENSITIVE)
                                            .matcher(exploded[j])
                                            .find()) {
                                        exploded[j] = exploded[j].replaceFirst("\\.", "");
                                        tmp = exploded[j].replaceFirst("\\D*", "").split(
                                                "\\D+");
                                        _fire[RC_index][0] += exptoint(tmp[0]);
                                        _firepower[RC_index][0] += exptoint(tmp[1]);
                                        if (tmp.length < 3) {
                                            j++;
                                            exploded[j] = exploded[j].replaceFirst("\\.", "");
                                            tmp = exploded[j].replaceFirst("\\D*", "").split(
                                                    "\\D{2,}");
                                            _shields_absorb[RC_index][0] += exptoint(tmp[0]);
                                        } else
                                            _shields_absorb[RC_index][0] += exptoint(tmp[2]);
                                        j++;
                                        while (exploded[j].trim().equals("")) {
                                            j++;
                                        }
                                        exploded[j] = exploded[j].replaceFirst("\\.", "");
                                        tmp = exploded[j].replaceFirst("\\D*", "").split(
                                                "\\D+");
                                        _fire[RC_index][1] += exptoint(tmp[0]);
                                        _firepower[RC_index][1] += exptoint(tmp[1]);
                                        if (tmp.length < 3) {
                                            j++;
                                            exploded[j] = exploded[j].replaceFirst("\\.", "");
                                            tmp = exploded[j].replaceFirst("\\D*", "").split(
                                                    "\\D{2,}");
                                            _shields_absorb[RC_index][1] += exptoint(tmp[0]);
                                        } else
                                            _shields_absorb[RC_index][1] += exptoint(tmp[2]);
                                        continue;
                                    }

                                    if (part.equals("def")
                                            && Pattern.compile("^" + _att + MSK_NAME_COORD_1,
                                            Pattern.CASE_INSENSITIVE).matcher(
                                            exploded[j]).find()) {
                                        il = j - 1;
                                        round++;
                                        part = "attack";
                                    } else if (Pattern.compile("^" + _def + MSK_NAME_COORD_1,
                                            Pattern.CASE_INSENSITIVE)
                                            .matcher(exploded[j])
                                            .find())
                                        part = "def";
                                }

                                if (!zero_round && round == 0) {
                                    Print_exception("Ce modèle n'accepte pas les RC à 0 round.");
                                    break;
                                }

                                _round = "" + round;
                                part = "attack";
                                go_end = true;
                                att_index = 0;
                                def_index = 0;
                                continue;
                            }
                            tmp = line.split(_type);
                            if (tmp.length > 1)
                                line = _type + tmp[1];
                            if (iniConfig.getConfig("attname").equals("1"))
                                _attacker[RC_index][att_index] = tmp[0].replaceAll(
                                        _att + MSK_NAME_COORD_2, "$1").trim();
                            else
                                _attacker[RC_index][att_index] = iniConfig.getConfig("att_rpl_name");
                            _attacker_coordinates[att_index] = tmp[0].replaceAll(
                                    _att + MSK_NAME_COORD_2, "$2").trim();
                            if (att_index == 0)
                                _diff_att.add(_attacker[RC_index][att_index]);
                            tmp = null;
                            att_index++;
                        } else if (Pattern.compile("^" + _def + MSK_NAME_COORD_1,
                                Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                            part = "def";
                            tmp = line.split(_type);
                            if (tmp.length > 1)
                                line = _type + tmp[1];
                            if (iniConfig.getConfig("defname").equals("1"))
                                _defender[RC_index][def_index] = tmp[0].replaceAll(
                                        _def + MSK_NAME_COORD_2, "$1").trim();
                            else
                                _defender[RC_index][def_index] = iniConfig.getConfig("def_rpl_name");
                            _defender_coordinates[def_index] = tmp[0].replaceAll(
                                    _def + MSK_NAME_COORD_2, "$2").trim();
                            if (def_index == 0)
                                _diff_def.add(_defender[RC_index][def_index]);
                            tmp = null;
                            def_index++;
                        } else if (Pattern.compile(
                                "^" + _techno + "\\s+" + MSK_NB_FORMATED + "%",
                                Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                            tmp = line.split(_type);
                            if (tmp.length > 1)
                                line = _type + tmp[1];
                            if (iniConfig.getConfig("Techno").equals("1")
                                    && part.equals("attack")) {
                                _attacker_weapons[att_index - 1] = tmp[0].replaceFirst(
                                        MSK_TECHNOLOGY, "$1");
                                _attacker_shielding[att_index - 1] = tmp[0].replaceFirst(
                                        MSK_TECHNOLOGY, "$2");
                                _attacker_armour[att_index - 1] = tmp[0].replaceFirst(
                                        MSK_TECHNOLOGY, "$3");
                            } else if (iniConfig.getConfig("Techno").equals("1")
                                    && part.equals("def")) {
                                _defender_weapons[def_index - 1] = tmp[0].replaceFirst(
                                        MSK_TECHNOLOGY, "$1");
                                _defender_shielding[def_index - 1] = tmp[0].replaceFirst(
                                        MSK_TECHNOLOGY, "$2");
                                _defender_armour[def_index - 1] = tmp[0].replaceFirst(
                                        MSK_TECHNOLOGY, "$3");
                            }
                            tmp = null;
                        }

                        if (Pattern.compile("^" + _type, Pattern.CASE_INSENSITIVE).matcher(
                                line).find()) {
                            repl = langConfig.getConfig("FLEET") + "|"
                                    + langConfig.getConfig("DEFENCES");
                            line = StringOperation.sansAccent(line);
                            line = line.toLowerCase();
                            line = line.replace('.', ' ');
                            line = line.replaceAll("\\s+", "");
                            repl = StringOperation.sansAccent(repl);
                            repl = repl.toLowerCase();
                            repl = repl.replace('.', ' ');
                            repl = repl.replaceAll("\\s+", "");
                            line = line.replaceAll("(" + repl + ")", " $1 ");
                            repl = line.replaceAll("(" + repl + ").*$", "");
                            tmp = line.substring(repl.length()).split("\\s{2,}");
                            continue;
                        } else if (Pattern.compile("^" + _nb, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            line = line.replaceAll("[\\.,']", "");
                            tmp2 = line.replaceFirst(MSK_NB_LINE, "$1").split("\\s+");
                            for (j = 0; j < tmp2.length; j++) {
                                if (tmp2[j].trim().equals(""))
                                    continue;
                                if ((k = ifleet.indexOf(tmp[j].trim())) >= 0) {
                                    if (part.equals("def")) {
                                        if (!go_end) {
                                            def_flt_perte_par_ress[0] += exptoint(tmp2[j])
                                                    * fpts[k][0];
                                            def_flt_perte_par_ress[1] += exptoint(tmp2[j])
                                                    * fpts[k][1];
                                            def_flt_perte_par_ress[2] += exptoint(tmp2[j])
                                                    * fpts[k][2];
                                            defgain -= exptoint(tmp2[j])
                                                    * (fpts[k][0] + fpts[k][1] + fpts[k][2]);
                                            _defender_fleet_start[def_index - 1].put(
                                                    new Integer(k), formatnumber(tmp2[j]));
                                            _defender_fleet_end[def_index - 1].put(
                                                    new Integer(k), null);
                                        } else {
                                            def_flt_perte_par_ress[0] -= exptoint(tmp2[j])
                                                    * fpts[k][0];
                                            def_flt_perte_par_ress[1] -= exptoint(tmp2[j])
                                                    * fpts[k][1];
                                            def_flt_perte_par_ress[2] -= exptoint(tmp2[j])
                                                    * fpts[k][2];
                                            defgain += exptoint(tmp2[j])
                                                    * (fpts[k][0] + fpts[k][1] + fpts[k][2]);
                                            _defender_fleet_end[def_index - 1].put(
                                                    new Integer(k), formatnumber(tmp2[j]));
                                        }
                                    } else {
                                        if (!go_end) {
                                            att_perte_par_ress[0] += exptoint(tmp2[j])
                                                    * fpts[k][0];
                                            att_perte_par_ress[1] += exptoint(tmp2[j])
                                                    * fpts[k][1];
                                            att_perte_par_ress[2] += exptoint(tmp2[j])
                                                    * fpts[k][2];
                                            att_courante_par_ress[0] += exptoint(tmp2[j])
                                                    * fpts[k][0];
                                            att_courante_par_ress[1] += exptoint(tmp2[j])
                                                    * fpts[k][1];
                                            att_courante_par_ress[2] += exptoint(tmp2[j])
                                                    * fpts[k][2];
                                            attgain -= exptoint(tmp2[j])
                                                    * (fpts[k][0] + fpts[k][1] + fpts[k][2]);
                                            courante_attgain += exptoint(tmp2[j])
                                                    * (fpts[k][0] + fpts[k][1] + fpts[k][2]);
                                            _attacker_fleet_start[att_index - 1].put(
                                                    new Integer(k), formatnumber(tmp2[j]));
                                            _attacker_fleet_end[att_index - 1].put(
                                                    new Integer(k), null);
                                        } else {
                                            att_perte_par_ress[0] -= exptoint(tmp2[j])
                                                    * fpts[k][0];
                                            att_perte_par_ress[1] -= exptoint(tmp2[j])
                                                    * fpts[k][1];
                                            att_perte_par_ress[2] -= exptoint(tmp2[j])
                                                    * fpts[k][2];
                                            attgain += exptoint(tmp2[j])
                                                    * (fpts[k][0] + fpts[k][1] + fpts[k][2]);
                                            _attacker_fleet_end[att_index - 1].put(
                                                    new Integer(k), formatnumber(tmp2[j]));
                                        }
                                    }

                                } else if ((k = idef.indexOf(tmp[j].trim())) >= 0) {
                                    if (!go_end && part.equals("def")) {
                                        def_def_perte_par_ress[0] += exptoint(tmp2[j])
                                                * pts[k][0];
                                        def_def_perte_par_ress[1] += exptoint(tmp2[j])
                                                * pts[k][1];
                                        def_def_perte_par_ress[2] += exptoint(tmp2[j])
                                                * pts[k][2];
                                        defpts -= exptoint(tmp2[j])
                                                * (pts[k][0] + pts[k][1] + pts[k][2]);
                                        _defender_defense_start[def_index - 1].put(
                                                new Integer(k), formatnumber(tmp2[j]));
                                        _defender_defense_end[def_index - 1].put(
                                                new Integer(k), null);
                                    } else if (part.equals("def")) {
                                        def_def_perte_par_ress[0] -= exptoint(tmp2[j])
                                                * pts[k][0];
                                        def_def_perte_par_ress[1] -= exptoint(tmp2[j])
                                                * pts[k][1];
                                        def_def_perte_par_ress[2] -= exptoint(tmp2[j])
                                                * pts[k][2];
                                        defpts += exptoint(tmp2[j])
                                                * (pts[k][0] + pts[k][1] + pts[k][2]);
                                        _defender_defense_end[def_index - 1].put(
                                                new Integer(k), formatnumber(tmp2[j]));
                                    }
                                }
                            }

                            tmp = null;
                            tmp2 = null;
                            continue;
                        } else if (Pattern.compile("^" + _dest, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            if (go_end == false) {
                                attgain += courante_attgain;
                                att_perte_par_ress[0] -= att_courante_par_ress[0];
                                att_perte_par_ress[1] -= att_courante_par_ress[1];
                                att_perte_par_ress[2] -= att_courante_par_ress[2];
                                _round = "0";
                                _attacker_fleet_end = _attacker_fleet_start;
                                def_destr[def_index - 1] = true;
                            } else if (part.equals("attack"))
                                att_destr[att_index - 1] = true;
                            else
                                def_destr[def_index - 1] = true;
                            if (def_index >= def_nb[RC_index])
                                go_end = true;
                            continue;
                        } else if (go_end
                                && (Pattern.compile("^" + _end1, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()
                                || Pattern.compile("^" + _end2,
                                Pattern.CASE_INSENSITIVE).matcher(line).find() || Pattern.compile(
                                "^" + _end3, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find())) {
                            _result = line;
                            continue;
                        } else if (go_end
                                && Pattern.compile("^" + _gain, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            while (!Pattern.compile(MSK_GAIN, Pattern.CASE_INSENSITIVE)
                                    .matcher(line)
                                    .find()) {
                                il++;
                                line = exploded[il];
                            }
                            _captured_metal = formatnumber(line.replaceFirst(MSK_GAIN, "$1"));
                            _captured_cristal = formatnumber(line.replaceFirst(MSK_GAIN, "$2"));
                            _captured_deuterium = formatnumber(line.replaceFirst(MSK_GAIN,
                                    "$3"));
                            tmp = line.replaceFirst(MSK_GAIN, "$1:$2:$3").split("\\:");
                            continue;
                        } else if (go_end
                                && Pattern.compile("^" + _lose, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            repl = "";
                            _end_cr = "";
                            for (; il < exploded.length; il++) {
                                line = exploded[il].trim();
                                if (Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE)
                                        .matcher(line)
                                        .find()) {
                                    harvest = true;
                                    break;
                                } else if (Pattern.compile(_top1, Pattern.CASE_INSENSITIVE)
                                        .matcher(line)
                                        .find()
                                        || Pattern.compile(_top2, Pattern.CASE_INSENSITIVE)
                                        .matcher(line)
                                        .find()) {
                                    break;
                                } else if (Pattern.compile(MSK_USER_MSG,
                                        Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                                    k = il++;
                                    while (il < exploded.length
                                            && exploded[il].trim().equals(""))
                                        il++;

                                    if (Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE)
                                            .matcher(exploded[il])
                                            .find()) {
                                        il = k;
                                        harvest = true;
                                        break;
                                    }
                                }

                                if (line.equals(""))
                                    continue;
                                tmp2 = line.split("(\\D{3,}|(\\s*\\%\\s*))");
                                repl += line;
                                if (Pattern.compile("^" + _moon, Pattern.CASE_INSENSITIVE)
                                        .matcher(line)
                                        .find()) {
                                    line = line.replaceAll("\\Q"
                                                    + langConfig.getConfig("MOON") + "\\E",
                                            BBCode.addbold(0)
                                                    + BBCode.addsize(0,
                                                    iniConfig.getConfig("user_sz_rss"
                                                            + setting))
                                                    + BBCode.addbcolor(0, color[fleet.size()
                                                    + def.size() + 3])
                                                    + langConfig.getConfig("MOON")
                                                    + BBCode.addbcolor(1, color[fleet.size()
                                                    + def.size() + 3])
                                                    + BBCode.addsize(1, null)
                                                    + BBCode.addbold(1))
                                            + enter;
                                    _moon_created = true;
                                }
                                for (k = 0; k < tmp2.length; k++) {
                                    if (tmp2[k].equals(""))
                                        continue;
                                    start = line.lastIndexOf(']');
                                    if (start < 0)
                                        start = 0;
                                    line = line.substring(0, start)
                                            + line.substring(start)
                                            .replaceFirst(
                                                    "\\Q" + tmp2[k] + "\\E",
                                                    BBCode.addbold(0)
                                                            + BBCode.addsize(
                                                            0,
                                                            iniConfig.getConfig("user_sz_rss"
                                                                    + setting))
                                                            + BBCode.addbcolor(
                                                            0,
                                                            color[fleet.size()
                                                                    + def.size()
                                                                    + 3])
                                                            + formatnumber(tmp2[k])
                                                            + BBCode.addbcolor(
                                                            1,
                                                            color[fleet.size()
                                                                    + def.size()
                                                                    + 3])
                                                            + BBCode.addsize(1, null)
                                                            + BBCode.addbold(1));
                                }
                                _end_cr += line + enter;
                                tmp2 = null;
                            }

                            _att_true_losed = -1 * attgain;
                            _def_true_losed = (-1 * defgain) - defpts;
                            if (tmp != null && tmp.length >= 3)
                                attgain += exptoint(tmp[0]) + exptoint(tmp[1])
                                        + exptoint(tmp[2]);
                            tmp = null;
                            tmp = repl.replaceFirst("\\D+", "").split("\\D{3,}");
                            defgain += (long) (0.3 * (double) (defpts));
                            cdr[RC_index] = exptoint(tmp[2]) + exptoint(tmp[3]);
                            tt_cdr += cdr[RC_index];
                            defpts = 0;

                            _attacker_losed = formatnumber(tmp[0]);
                            _defender_losed = formatnumber(tmp[1]);
                            _harvest_metal = formatnumber(tmp[2]);
                            _harvest_cristal = formatnumber(tmp[3]);
                            if ((_moon_probability = repl.replaceFirst(MSK_MOON_PROB, "$1%")).equals(repl))
                                _moon_probability = "0%";
                            break;
                        }
                    }

                } catch (Exception e) {
                    ExceptionAlert.createExceptionAlert(e, exText);
                    e.printStackTrace();
                }
            }

            if (harvest && !no_harvest) {
                _harvests_reports = "";
                boolean harvest_find = false;
                infinity = 0;
                j = il;

                while (harvest) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop!");
                        return exText;
                    }
                    line = "";
                    harvest_find = false;
                    for (il = j; il < exploded.length; il++) {
                        if (Pattern.compile(_top1, Pattern.CASE_INSENSITIVE).matcher(
                                exploded[il]).find()
                                || Pattern.compile(_top2, Pattern.CASE_INSENSITIVE).matcher(
                                exploded[il]).find()) {
                            harvest = false;
                            break;
                        } else if (Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE)
                                .matcher(exploded[il])
                                .find()) {
                            if (harvest_find) {
                                break;
                            }
                            harvest_find = true;
                            j = il + 1;
                        } else if (Pattern.compile(MSK_USER_MSG, Pattern.CASE_INSENSITIVE)
                                .matcher(exploded[il])
                                .find()) {
                            if (harvest_find) {
                                break;
                            }
                        }

                        line += " " + exploded[il];

                        if (il == exploded.length - 1) {
                            harvest = false;
                            break;
                        }
                    }
                    if (harvest_find) {
                        tmp = converterRecy(line, IN_ATT);
                        tt_pris += exptoint(tmp[1]);
                        pris[RC_index] += exptoint(tmp[1]);
                        _harvests_reports += enter + tmp[0];
                        _harvests_reports += "_______________________________" + enter;
                        harvestInfo.add(tmp.clone());
                    }
                    if (il >= exploded.length)
                        break;
                }
                il--;
                harvest = true;
            } else if (harvest)
                harvest = false;

            // consomation
            val = new int[14];
            for (att_index = 0; att_index < _attacker_fleet_start.length; att_index++) {
                Iterator it = _attacker_fleet_start[att_index].keySet().iterator();
                for (k = 0; k < _attacker_fleet_start[att_index].size(); k++) {
                    j = ((Integer) it.next()).intValue();
                    val[j] += (int) exptoint((String) _attacker_fleet_start[att_index].get(new Integer(
                            j)));
                }
            }

            tmp = _attacker_coordinates[0].split(":");
            tmp2 = _defender_coordinates[0].split(":");

            try {
                if (iniConfig.getConfig("cr_consumption").equals("1") && tmp.length >= 3
                        && tmp2.length >= 3) {
                    consumption[RC_index] = Main.getConsumption(new int[]{
                            (int) exptoint(tmp[0]), (int) exptoint(tmp[1]),
                            (int) exptoint(tmp[2])}, new int[]{(int) exptoint(tmp2[0]),
                            (int) exptoint(tmp2[1]), (int) exptoint(tmp2[2])}, val, speed);
                    consumption_total += consumption[RC_index];
                } else {
                    consumption[RC_index] = 0;
                    consumption_total = 0;
                }
            } catch (Exception e) {
                ExceptionAlert.createExceptionAlert(e, text);
                e.printStackTrace();

                consumption[RC_index] = 0;
                consumption_total = 0;
            }

            try {

                if (cdr[RC_index] < pris[RC_index]) {
                    jl = pris[RC_index] - cdr[RC_index];
                    pris[RC_index] = cdr[RC_index];
                    if (_iamatt.equals("1")) {
                        attgain += jl;
                    } else {
                        defgain += jl;
                    }
                }

                aa[RC_index] = formatnumber(cdr[RC_index] + attgain);
                as[RC_index] = formatnumber(attgain);
                da[RC_index] = formatnumber(cdr[RC_index] + defgain);
                ds[RC_index] = formatnumber(defgain);

                as[RC_nb] = "" + (exptoint(as[RC_nb]) + attgain);
                ds[RC_nb] = "" + (exptoint(ds[RC_nb]) + defgain);

                if (!are_harvest) {
                    aa[RC_nb] = "" + (exptoint(aa[RC_nb]) + cdr[RC_index] + attgain);
                    da[RC_nb] = "" + (exptoint(da[RC_nb]) + cdr[RC_index] + defgain);
                } else {
                    aa[RC_nb] = "" + (exptoint(aa[RC_nb]) + attgain);
                    da[RC_nb] = "" + (exptoint(da[RC_nb]) + defgain);
                }

                if (harvest && _iamatt.equals("1")) {
                    aa[RC_index] = formatnumber(exptoint(aa[RC_index]) + pris[RC_index]
                            - cdr[RC_index]);
                    aa[RC_nb] = "" + (exptoint(aa[RC_nb]) + pris[RC_index]);
                    da[RC_index] = formatnumber(exptoint(da[RC_index]) - pris[RC_index]);
                    da[RC_nb] = "" + (exptoint(da[RC_nb]) + cdr[RC_index] - pris[RC_index]);
                } else if (harvest) {
                    aa[RC_nb] = "" + (exptoint(aa[RC_nb]) + cdr[RC_index] - pris[RC_index]);
                    aa[RC_index] = formatnumber(exptoint(aa[RC_index]) - pris[RC_index]);
                    da[RC_nb] = "" + (pris[RC_index] + exptoint(da[RC_nb]));
                    da[RC_index] = formatnumber(exptoint(da[RC_index]) + pris[RC_index]
                            - cdr[RC_index]);
                }

                _total_captured_metal += exptoint(_captured_metal);
                _total_captured_cristal += exptoint(_captured_cristal);
                _total_captured_deut += exptoint(_captured_deuterium);
                _total_attacker_lose[0] += att_perte_par_ress[0];
                _total_attacker_lose[1] += att_perte_par_ress[1];
                _total_attacker_lose[2] += att_perte_par_ress[2];
                _total_defender_lose[0] += def_flt_perte_par_ress[0]
                        + def_def_perte_par_ress[0];
                _total_defender_lose[1] += def_flt_perte_par_ress[1]
                        + def_def_perte_par_ress[1];
                _total_defender_lose[2] += def_flt_perte_par_ress[2]
                        + def_def_perte_par_ress[2];
                _total_harvest_metal += exptoint(_harvest_metal);
                _total_harvest_cristal += exptoint(_harvest_cristal);

            } catch (Exception e) {
                ExceptionAlert.createExceptionAlert(e, exText);
                e.printStackTrace();
            }

            tmp = null;
            tmp2 = null;

            modelgetted = new ArrayList();

            for (i = 0; i < modelget.size(); i++) {
                tmp = modelget.get(i).toString().split("\\s*\\|\\s*");
                tmp2 = tmp[1].split("(\\(|\\))");
                repl = JOptionPane.showInputDialog(getParentFrame(), tmp[3], tmp[2]);
                if (repl != null) {
                    switch (tmp2[0].charAt(0)) {
                        case 't':
                            if (repl.length() <= Integer.parseInt(tmp2[1]))
                                modelgetted.add((Object) repl);
                            else {
                                Print_exception("Bab datas!");
                                i--;
                            }
                            break;
                        case 'n':
                            if (Pattern.compile("^\\-?\\d+$", Pattern.CASE_INSENSITIVE).matcher(
                                    repl).find()
                                    && repl.length() <= Integer.parseInt(tmp2[1]))
                                modelgetted.add((Object) repl);
                            else {
                                Print_exception("Bab datas!");
                                i--;
                            }
                            break;
                        default:
                            Print_exception("Option 'get:' must be 't' or 'n' type.");
                    }
                } else
                    i--;
            }

            repeat_att_index = 0;
            repeat_def_index = 0;
            att_index = 0;
            def_index = 0;
            repeat = new StringBuffer();

            try {

                line = modelString.toString();
                line = line.replaceAll("\\/\\*.*?\\*\\/", "");

                try {

                    for (repeat_att_index = 0; repeat_att_index < repeat_att.size(); repeat_att_index++) {
                        repeat = new StringBuffer();
                        repl = "";

                        for (att_index = 0; att_index < att_nb[RC_index]; att_index++) {
                            repl = ((StringBuffer) (repeat_att.get(repeat_att_index))).toString();
                            repl = repl.replaceAll("\\/\\*.*?\\*\\/", "");

                            for (i = 0; i < modeloption.size(); i++) {
                                k = modeloption.get(i).toString().indexOf('=');
                                tmp = new String[2];
                                tmp[0] = modeloption.get(i).toString().substring(0, k).trim();
                                tmp[1] = modeloption.get(i).toString().substring(k + 1).trim();
                                repl = repl.replaceAll("\\Q[" + tmp[0] + "]\\E", tmp[1]);
                            }

                            if (!att_destr[att_index]) {
                                repl = repl.replaceAll("\\[\\/?attacker not destroyed\\]", "");
                                repl = repl.replaceAll(
                                        "\\[attacker destroyed\\].*?\\[\\/attacker destroyed\\]",
                                        "");
                            } else {
                                repl = repl.replaceAll("\\[\\/?attacker destroyed\\]", "");
                                repl = repl.replaceAll(
                                        "\\[attacker not destroyed\\].*?\\[\\/attacker not destroyed\\]",
                                        "");
                            }

                            infinity = 0;
                            while ((j = repl.indexOf("[attacker fleet start]")) != -1
                                    && (k = repl.indexOf("[/attacker fleet start]")) != -1) {
                                if (infinity++ > 10000) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                                tmp = new String[3];
                                tmp[0] = repl.substring(0, j);
                                tmp[1] = repl.substring(j + "[attacker fleet start]".length(),
                                        k);
                                tmp[2] = repl.substring(k + "[/attacker fleet start]".length());
                                repl = type_chang(tmp, _attacker_fleet_start[att_index],
                                        _attacker_fleet_end[att_index], fleet, fleet_complete,
                                        "fleet", "start");
                                if (repl == null) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                            }

                            infinity = 0;
                            while ((j = repl.indexOf("[attacker fleet end]")) != -1
                                    && (k = repl.indexOf("[/attacker fleet end]")) != -1) {
                                if (infinity++ > 10000) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                                tmp = new String[3];
                                tmp[0] = repl.substring(0, j);
                                tmp[1] = repl.substring(j + "[attacker fleet end]".length(), k);
                                tmp[2] = repl.substring(k + "[/attacker fleet end]".length());
                                repl = type_chang(tmp, _attacker_fleet_end[att_index],
                                        _attacker_fleet_start[att_index], fleet,
                                        fleet_complete, "fleet", "end");
                                if (repl == null) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                            }

                            if (iniConfig.getConfig("attcoord").equals("1"))
                                repl = repl.replaceAll("\\[attacker coordinates\\]",
                                        _attacker_coordinates[att_index]);
                            else
                                repl = repl.replaceAll("\\[attacker coordinates\\]",
                                        iniConfig.getConfig("att_rpl_coord"));

                            repl = repl.replaceAll("\\[attacker weapons\\]",
                                    _attacker_weapons[att_index]);
                            repl = repl.replaceAll("\\[attacker shielding\\]",
                                    _attacker_shielding[att_index]);
                            repl = repl.replaceAll("\\[attacker armour\\]",
                                    _attacker_armour[att_index]);
                            repl = repl.replaceAll("\\[attacker\\]",
                                    _attacker[RC_index][att_index]);

                            repeat.append(repl);
                        }

                        line = line.replaceFirst("\\[repeat attackers\\]", repeat.toString());
                    }
                } catch (Exception ex) {
                    Except exc = new Except(ex);
                    Print_exception("Error around '[repeat attackers]' n°"
                            + (repeat_att_index + 1) + " for attacker n°" + (att_index + 1)
                            + "\n\n" + exc.ExceptoString());
                    ex.printStackTrace();
                }

                try {
                    for (repeat_def_index = 0; repeat_def_index < repeat_def.size(); repeat_def_index++) {
                        repeat = new StringBuffer();
                        repl = "";

                        for (def_index = 0; def_index < def_nb[RC_index]; def_index++) {
                            repl = ((StringBuffer) (repeat_def.get(repeat_def_index))).toString();
                            repl = repl.replaceAll("\\/\\*.*?\\*\\/", "");

                            for (i = 0; i < modeloption.size(); i++) {
                                k = modeloption.get(i).toString().indexOf('=');
                                tmp = new String[2];
                                tmp[0] = modeloption.get(i).toString().substring(0, k).trim();
                                tmp[1] = modeloption.get(i).toString().substring(k + 1).trim();
                                repl = repl.replaceAll("\\Q[" + tmp[0] + "]\\E", tmp[1]);
                            }

                            if (!def_destr[def_index]) {
                                repl = repl.replaceAll("\\[\\/?defender not destroyed\\]", "");
                                repl = repl.replaceAll(
                                        "\\[defender destroyed\\].*?\\[\\/defender destroyed\\]",
                                        "");
                            } else {
                                repl = repl.replaceAll("\\[\\/?defender destroyed\\]", "");
                                repl = repl.replaceAll(
                                        "\\[defender not destroyed\\].*?\\[\\/defender not destroyed\\]",
                                        "");
                            }

                            if (def_destr[def_index] && _round.equals("0"))
                                repl = repl.replaceAll("\\[\\/?defender destroyed start\\]",
                                        "");
                            else
                                repl = repl.replaceAll(
                                        "\\[defender destroyed start\\].*?\\[\\/defender destroyed start\\]",
                                        "");

                            if (!_round.equals("0"))
                                repl = repl.replaceAll(
                                        "\\[\\/?defender not destroyed start\\]", "");
                            else
                                repl = repl.replaceAll(
                                        "\\[defender not destroyed start\\].*?\\[\\/defender not destroyed start\\]",
                                        "");

                            infinity = 0;
                            while ((j = repl.indexOf("[defender fleet start]")) != -1
                                    && (k = repl.indexOf("[/defender fleet start]")) != -1) {
                                if (infinity++ > 10000) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                                tmp = new String[3];
                                tmp[0] = repl.substring(0, j);
                                tmp[1] = repl.substring(j + "[defender fleet start]".length(),
                                        k);
                                tmp[2] = repl.substring(k + "[/defender fleet start]".length());
                                repl = type_chang(tmp, _defender_fleet_start[def_index],
                                        _defender_fleet_end[def_index], fleet, fleet_complete,
                                        "fleet", "start");
                                if (repl == null) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                            }

                            infinity = 0;
                            while ((j = repl.indexOf("[defender fleet end]")) != -1
                                    && (k = repl.indexOf("[/defender fleet end]")) != -1) {
                                if (infinity++ > 10000) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                                tmp = new String[3];
                                tmp[0] = repl.substring(0, j);
                                tmp[1] = repl.substring(j + "[defender fleet end]".length(), k);
                                tmp[2] = repl.substring(k + "[/defender fleet end]".length());
                                repl = type_chang(tmp, _defender_fleet_end[def_index],
                                        _defender_fleet_start[def_index], fleet,
                                        fleet_complete, "fleet", "end");
                                if (repl == null) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                            }

                            infinity = 0;
                            while ((j = repl.indexOf("[defender defense end]")) != -1
                                    && (k = repl.indexOf("[/defender defense end]")) != -1) {
                                if (infinity++ > 10000) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                                tmp = new String[3];
                                tmp[0] = repl.substring(0, j);
                                tmp[1] = repl.substring(j + "[defender defense end]".length(),
                                        k);
                                tmp[2] = repl.substring(k + "[/defender defense end]".length());
                                repl = type_chang(tmp, _defender_defense_end[def_index],
                                        _defender_defense_start[def_index], def, def_complete,
                                        "defense", "end");
                                if (repl == null) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                            }

                            infinity = 0;
                            while ((j = repl.indexOf("[defender defense start]")) != -1
                                    && (k = repl.indexOf("[/defender defense start]")) != -1) {
                                if (infinity++ > 10000) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                                tmp = new String[3];
                                tmp[0] = repl.substring(0, j);
                                tmp[1] = repl.substring(j
                                        + "[defender defense start]".length(), k);
                                tmp[2] = repl.substring(k
                                        + "[/defender defense start]".length());
                                repl = type_chang(tmp, _defender_defense_start[def_index],
                                        _defender_defense_end[def_index], def, def_complete,
                                        "defense", "start");
                                if (repl == null) {
                                    Print_exception("Infinity loop!");
                                    return exText;
                                }
                            }

                            if (iniConfig.getConfig("defcoord").equals("1"))
                                repl = repl.replaceAll("\\[defender coordinates\\]",
                                        _defender_coordinates[def_index]);
                            else
                                repl = repl.replaceAll("\\[defender coordinates\\]",
                                        iniConfig.getConfig("def_rpl_coord"));

                            repl = repl.replaceAll("\\[defender weapons\\]",
                                    _defender_weapons[def_index]);
                            repl = repl.replaceAll("\\[defender shielding\\]",
                                    _defender_shielding[def_index]);
                            repl = repl.replaceAll("\\[defender armour\\]",
                                    _defender_armour[def_index]);
                            repl = repl.replaceAll("\\[defender\\]",
                                    _defender[RC_index][def_index]);

                            repeat.append(repl);
                        }

                        line = line.replaceFirst("\\[repeat defenders\\]", repeat.toString());
                    }
                } catch (Exception ex) {
                    Except exc = new Except(ex);
                    Print_exception("Error around '[repeat defenders]' n°"
                            + (repeat_def_index + 1) + " for attacker n°" + (def_index + 1)
                            + "\n\n" + exc.ExceptoString());
                    ex.printStackTrace();
                }

                repeat_att_index = 0;
                repeat_def_index = 0;
                att_index = 0;
                def_index = 0;
                repeat = new StringBuffer();
                repl = "";

                tmp = null;
                tmp2 = null;

                for (i = 0; i < modeloption.size(); i++) {
                    k = modeloption.get(i).toString().indexOf('=');
                    tmp = new String[2];
                    tmp[0] = modeloption.get(i).toString().substring(0, k).trim();
                    tmp[1] = modeloption.get(i).toString().substring(k + 1).trim();
                    line = line.replaceAll("\\Q[" + tmp[0] + "]\\E", tmp[1]);
                }

                if (!def_destr[def_index]) {
                    line = line.replaceAll("\\[\\/?defender not destroyed\\]", "");
                    line = line.replaceAll(
                            "\\[defender destroyed\\].*?\\[\\/defender destroyed\\]", "");
                } else {
                    line = line.replaceAll("\\[\\/?defender destroyed\\]", "");
                    line = line.replaceAll(
                            "\\[defender not destroyed\\].*?\\[\\/defender not destroyed\\]",
                            "");
                }

                if (!att_destr[att_index]) {
                    line = line.replaceAll("\\[\\/?attacker not destroyed\\]", "");
                    line = line.replaceAll(
                            "\\[attacker destroyed\\].*?\\[\\/attacker destroyed\\]", "");
                } else {
                    line = line.replaceAll("\\[\\/?attacker destroyed\\]", "");
                    line = line.replaceAll(
                            "\\[attacker not destroyed\\].*?\\[\\/attacker not destroyed\\]",
                            "");
                }

                if (def_destr[def_index] && _round.equals("0"))
                    line = line.replaceAll("\\[\\/?defender destroyed start\\]", "");
                else
                    line = line.replaceAll(
                            "\\[defender destroyed start\\].*?\\[\\/defender destroyed start\\]",
                            "");

                if (RC_nb > 1 && (RC_nb - 1) == RC_index) {
                    if (_diff_att.size() > 1)
                        line = line.replaceAll("\\[\\/?more one attacker\\]", "");
                    else
                        line = line.replaceAll(
                                "\\[more one attacker\\].*?\\[\\/more one attacker\\]", "");

                    if (_diff_def.size() > 1)
                        line = line.replaceAll("\\[\\/?more one defender\\]", "");
                    else
                        line = line.replaceAll(
                                "\\[more one defender\\].*?\\[\\/more one defender\\]", "");

                    line = line.replaceAll("\\[\\/?end conversion\\]", "");

                } else
                    line = line.replaceAll("\\[end conversion\\].*?\\[\\/end conversion\\]",
                            "");

                if (!_round.equals("0"))
                    line = line.replaceAll("\\[\\/?defender not destroyed start\\]", "");
                else
                    line = line.replaceAll(
                            "\\[defender not destroyed start\\].*?\\[\\/defender not destroyed start\\]",
                            "");

                if (RC_nb == 1)
                    line = line.replaceAll("\\[\\/?one CR\\]", "");
                else
                    line = line.replaceAll("\\[one CR\\].*?\\[\\/one CR\\]", "");

                if (cdr[RC_index] - pris[RC_index] > 0)
                    line = line.replaceAll("\\[\\/?harvest\\]", "");
                else
                    line = line.replaceAll("\\[harvest\\].*?\\[\\/harvest\\]", "");

                if (tt_cdr - tt_pris > 0)
                    line = line.replaceAll("\\[\\/?completely not harvested\\]", "");
                else
                    line = line.replaceAll(
                            "\\[completely not harvested\\].*?\\[\\/completely not harvested\\]",
                            "");

                if (_moon_created)
                    line = line.replaceAll("\\[\\/?moon created\\]", "");
                else
                    line = line.replaceAll("\\[moon created\\].*?\\[\\/moon created\\]", "");

                if (harvest)
                    line = line.replaceAll("\\[\\/?harvest report\\]", "");
                else
                    line = line.replaceAll("\\[harvest report\\].*?\\[\\/harvest report\\]",
                            "");

                if (tt_pris != 0)
                    line = line.replaceAll("\\[\\/?harvest before\\]", "");
                else
                    line = line.replaceAll("\\[harvest before\\].*?\\[\\/harvest before\\]",
                            "");

                for (i = 0; i < modeldefCalcul.size(); i++) {
                    tmp = modeldefCalcul.get(i).toString().split("\\s*\\|\\s*");
                    line = line.replaceAll("\\Q[" + tmp[0].trim() + "]\\E", "[calculate]"
                            + tmp[1].trim() + "[/calculate]");
                    line = line.replaceAll("\\Q[" + tmp[0].trim() + "]\\E",
                            ((Long) modeldefCalculValue.get(i)).toString());
                }

                for (i = 0; i < modelget.size(); i++) {
                    tmp = modelget.get(i).toString().split("\\s*\\|\\s*");
                    line = line.replaceAll("\\Q[" + tmp[0].trim() + "]\\E",
                            (String) modelgetted.get(i));
                }

                infinity = 0;
                while ((j = line.indexOf("[print harvest]")) != -1
                        && (k = line.indexOf("[/print harvest]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[print harvest]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[print harvest]".length(), k);
                    tmp[2] = line.substring(k + "[/print harvest]".length());

                    line = tmp[0];

					/* Rapport de recyclage personnalisé */
                    for (i = 0; i < harvestInfo.size(); i++) {
                        repl = tmp[1];
                        if (i == harvestInfo.size() - 1 && harvestInfo.size() - 1 != 0) {
                            while ((k = repl.indexOf("[/last]")) != -1) {
                                if (infinity++ > 10000) {
                                    Print_exception("Infinity loop in '[print harvest]' around '[last]'");
                                    return exText;
                                }
                                repl = repl.substring(repl.indexOf("[last]") + 6, k);
                            }
                        } else
                            repl = repl.replaceAll("\\[last\\].*?\\[\\/last\\]", "");

                        if (i == 0) {
                            while ((k = repl.indexOf("[/first]")) != -1) {
                                if (infinity++ > 10000) {
                                    Print_exception("Infinity loop in '[print harvest]' around '[first]'");
                                    return exText;
                                }
                                repl = repl.substring(repl.indexOf("[first]") + 7, k);
                            }
                        } else
                            repl = repl.replaceAll("\\[first\\].*?\\[\\/first\\]", "");

                        tmp2 = (String[]) harvestInfo.get(i);

                        repl = repl.replaceAll("\\[recycler nb\\]", formatnumber(tmp2[2]));
                        repl = repl.replaceAll("\\[recycler capacity\\]",
                                formatnumber(tmp2[3]));
                        repl = repl.replaceAll("\\[metal\\]", formatnumber(tmp2[4]));
                        repl = repl.replaceAll("\\[crystal\\]", formatnumber(tmp2[5]));
                        repl = repl.replaceAll("\\[metal taken\\]", formatnumber(tmp2[6]));
                        repl = repl.replaceAll("\\[crystal taken\\]", formatnumber(tmp2[7]));
                        repl = repl.replaceAll("\\[rate\\]", tmp2[8]);

                        repl = repl.replaceAll("\\[harvest date\\]", tmp2[9]);
                        repl = repl.replaceAll("\\[harvest coordinates\\]", tmp2[10]);

                        line += repl;
                    }

                    line += tmp[2];
                }

                infinity = 0;
                while ((j = line.indexOf("[attacker fleet start]")) != -1
                        && (k = line.indexOf("[/attacker fleet start]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[attacker fleet start]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[attacker fleet start]".length(), k);
                    tmp[2] = line.substring(k + "[/attacker fleet start]".length());
                    line = type_chang(tmp, _attacker_fleet_start[att_index],
                            _attacker_fleet_end[att_index], fleet, fleet_complete, "fleet",
                            "start");
                    if (line == null) {
                        Print_exception("Infinity loop in '[attacker fleet start]'");
                        return exText;
                    }
                }

                infinity = 0;
                while ((j = line.indexOf("[attacker fleet end]")) != -1
                        && (k = line.indexOf("[/attacker fleet end]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[attacker fleet end]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[attacker fleet end]".length(), k);
                    tmp[2] = line.substring(k + "[/attacker fleet end]".length());
                    line = type_chang(tmp, _attacker_fleet_end[att_index],
                            _attacker_fleet_start[att_index], fleet, fleet_complete, "fleet",
                            "end");
                    if (line == null) {
                        Print_exception("Infinity loop in '[attacker fleet start]'");
                        return exText;
                    }
                }

                infinity = 0;
                while ((j = line.indexOf("[defender fleet start]")) != -1
                        && (k = line.indexOf("[/defender fleet start]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[defender fleet start]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[defender fleet start]".length(), k);
                    tmp[2] = line.substring(k + "[/defender fleet start]".length());
                    line = type_chang(tmp, _defender_fleet_start[def_index],
                            _defender_fleet_end[def_index], fleet, fleet_complete, "fleet",
                            "start");
                    if (line == null) {
                        Print_exception("Infinity loop in '[attacker fleet start]'");
                        return exText;
                    }
                }

                infinity = 0;
                while ((j = line.indexOf("[defender fleet end]")) != -1
                        && (k = line.indexOf("[/defender fleet end]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[defender fleet end]");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[defender fleet end]".length(), k);
                    tmp[2] = line.substring(k + "[/defender fleet end]".length());
                    line = type_chang(tmp, _defender_fleet_end[def_index],
                            _defender_fleet_start[def_index], fleet, fleet_complete, "fleet",
                            "end");
                    if (line == null) {
                        Print_exception("Infinity loop in '[defender fleet end]");
                        return exText;
                    }
                }

                infinity = 0;
                while ((j = line.indexOf("[defender defense end]")) != -1
                        && (k = line.indexOf("[/defender defense end]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[defender fleet end]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[defender defense end]".length(), k);
                    tmp[2] = line.substring(k + "[/defender defense end]".length());
                    line = type_chang(tmp, _defender_defense_end[def_index],
                            _defender_defense_start[def_index], def, def_complete, "defense",
                            "end");
                    if (line == null) {
                        Print_exception("Infinity loop in '[defender fleet end]'");
                        return exText;
                    }
                }

                infinity = 0;
                while ((j = line.indexOf("[defender defense start]")) != -1
                        && (k = line.indexOf("[/defender defense start]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[defender defense start]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[defender defense start]".length(), k);
                    tmp[2] = line.substring(k + "[/defender defense start]".length());
                    line = type_chang(tmp, _defender_defense_start[def_index],
                            _defender_defense_end[def_index], def, def_complete, "defense",
                            "start");
                    if (line == null) {
                        Print_exception("Infinity loop in '[defender defense start]'");
                        return exText;
                    }
                }

                infinity = 0;
                while (RC_nb > 1 && (RC_nb - 1) == RC_index
                        && (j = line.indexOf("[defenders]")) != -1
                        && (k = line.indexOf("[/defenders]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[defenders]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[defenders]".length(), k);
                    tmp[2] = line.substring(k + "[/defenders]".length());
                    if (tmp.length > 0)
                        line = tmp[0];
                    if (tmp.length > 1 && (RC_nb - 1) == RC_index) {
                        tmp2 = (String[]) _diff_def.toArray(new String[_diff_def.size()]);
                        Arrays.sort(tmp2, Collator.getInstance(Locale.getDefault()));
                        for (i = 0; i < tmp2.length; i++) {
                            icdr = 0;
                            ipris = 0;
                            val = new int[2];
                            for (k = 0; k < RC_nb; k++) {
                                if (_defender[k][0].equals(tmp2[i])) {
                                    val[0] += exptoint(da[k]);
                                    val[1] += exptoint(ds[k]);
                                    icdr += cdr[k];
                                    ipris += pris[k];
                                }
                            }
                            repl = tmp[1];
                            if (i == tmp2.length - 1 && tmp2.length - 1 != 0) {
                                while ((k = repl.indexOf("[/last]")) != -1) {
                                    if (infinity++ > 10000) {
                                        Print_exception("Infinity loop in '[defenders]' around '[last]'");
                                        return exText;
                                    }
                                    repl = repl.substring(repl.indexOf("[last]") + 6, k);
                                }
                            } else
                                repl = repl.replaceAll("\\[last\\].*?\\[\\/last\\]", "");

                            if (i == 0) {
                                while ((k = repl.indexOf("[/first]")) != -1) {
                                    if (infinity++ > 10000) {
                                        Print_exception("Infinity loop in '[defenders]' around '[first]'");
                                        return exText;
                                    }
                                    repl = repl.substring(repl.indexOf("[first]") + 7, k);
                                }
                            } else
                                repl = repl.replaceAll("\\[first\\].*?\\[\\/first\\]", "");

                            if (icdr - ipris > 0)
                                repl = repl.replaceAll("\\[\\/?individual harvest\\]", "");
                            else
                                repl = repl.replaceAll(
                                        "\\[individual harvest\\].*?\\[\\/individual harvest\\]",
                                        "");

                            if (ipris > 0)
                                repl = repl.replaceAll("\\[\\/?individual harvest before\\]",
                                        "");
                            else
                                repl = repl.replaceAll(
                                        "\\[individual harvest before\\].*?\\[\\/individual harvest before\\]",
                                        "");

                            repl = repl.replaceAll("\\[individual rentability with\\]",
                                    formatnumber(val[0]));
                            repl = repl.replaceAll("\\[individual rentability without\\]",
                                    formatnumber(val[1]));
                            repl = repl.replaceAll("\\[individual harvested\\]",
                                    formatnumber(ipris));
                            repl = repl.replaceAll("\\[individual harvestable\\]",
                                    formatnumber(icdr));
                            repl = repl.replaceAll("\\[defender\\]", (String) tmp2[i]);
                            line += repl;
                        }
                    }
                    if (tmp.length > 2)
                        line += tmp[2];
                }

                infinity = 0;
                while (RC_nb > 1 && (RC_nb - 1) == RC_index
                        && (j = line.indexOf("[attackers]")) != -1
                        && (k = line.indexOf("[/attackers]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[attackers]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[attackers]".length(), k);
                    tmp[2] = line.substring(k + "[/attackers]".length());
                    if (tmp.length > 0)
                        line = tmp[0];
                    if (tmp.length > 1 && RC_nb > 1 && (RC_nb - 1) == RC_index) {
                        tmp2 = (String[]) _diff_att.toArray(new String[_diff_att.size()]);
                        Arrays.sort(tmp2, Collator.getInstance(Locale.getDefault()));
                        for (i = 0; i < tmp2.length; i++) {
                            icdr = 0;
                            ipris = 0;
                            val = new int[2];
                            for (k = 0; k < RC_nb; k++) {
                                if (_attacker[k][0].equals(tmp2[i])) {
                                    val[0] += exptoint(aa[k]);
                                    val[1] += exptoint(as[k]);
                                    icdr = cdr[k];
                                    ipris = pris[k];
                                }
                            }
                            repl = tmp[1];
                            if (i == tmp2.length - 1 && tmp2.length - 1 != 0) {
                                while ((k = repl.indexOf("[/last]")) != -1) {
                                    if (infinity++ > 10000) {
                                        Print_exception("Infinity loop around '[attackers]' around '[last]'");
                                        return exText;
                                    }
                                    repl = repl.substring(repl.indexOf("[last]") + 6, k);
                                }
                            } else
                                repl = repl.replaceAll("\\[last\\].*?\\[\\/last\\]", "");

                            if (i == 0) {
                                while ((k = repl.indexOf("[/first]")) != -1) {
                                    if (infinity++ > 10000) {
                                        Print_exception("Infinity loop around '[attackers]' around '[first]'");
                                        return exText;
                                    }
                                    repl = repl.substring(repl.indexOf("[first]") + 7, k);
                                }
                            } else
                                repl = repl.replaceAll("\\[first\\].*?\\[\\/first\\]", "");

                            if (icdr - ipris > 0)
                                repl = repl.replaceAll("\\[\\/?individual harvest\\]", "");
                            else
                                repl = repl.replaceAll(
                                        "\\[individual harvest\\].*?\\[\\/individual harvest\\]",
                                        "");

                            if (ipris > 0)
                                repl = repl.replaceAll("\\[\\/?individual harvest before\\]",
                                        "");
                            else
                                repl = repl.replaceAll(
                                        "\\[individual harvest before\\].*?\\[\\/individual harvest before\\]",
                                        "");

                            repl = repl.replaceAll("\\[individual rentability with\\]",
                                    formatnumber(val[0]));
                            repl = repl.replaceAll("\\[individual rentability without\\]",
                                    formatnumber(val[1]));
                            repl = repl.replaceAll("\\[individual harvested\\]",
                                    formatnumber(ipris));
                            repl = repl.replaceAll("\\[individual harvestable\\]",
                                    formatnumber(icdr));
                            repl = repl.replaceAll("\\[attacker\\]", (String) tmp2[i]);
                            line += repl;
                        }
                    }
                    if (tmp.length > 2)
                        line += tmp[2];
                }

                line = line.replaceAll("\\[date\\]", _date);
                if (iniConfig.getConfig("attcoord").equals("1"))
                    line = line.replaceAll("\\[attacker coordinates\\]",
                            _attacker_coordinates[att_index]);
                else
                    line = line.replaceAll("\\[attacker coordinates\\]",
                            iniConfig.getConfig("att_rpl_coord"));
                if (iniConfig.getConfig("defcoord").equals("1"))
                    line = line.replaceAll("\\[defender coordinates\\]",
                            _defender_coordinates[def_index]);
                else
                    line = line.replaceAll("\\[defender coordinates\\]",
                            iniConfig.getConfig("def_rpl_coord"));
                line = line.replaceAll("\\[attacker weapons\\]", _attacker_weapons[att_index]);
                line = line.replaceAll("\\[attacker shielding\\]",
                        _attacker_shielding[att_index]);
                line = line.replaceAll("\\[attacker armour\\]", _attacker_armour[att_index]);
                line = line.replaceAll("\\[defender weapons\\]", _defender_weapons[def_index]);
                line = line.replaceAll("\\[defender shielding\\]",
                        _defender_shielding[def_index]);
                line = line.replaceAll("\\[defender armour\\]", _defender_armour[def_index]);
                line = line.replaceAll("\\[round\\]", _round);
                line = line.replaceAll("\\[after battle\\]", _afterbattle.replaceAll("!n",
                        _round));
                line = line.replaceAll("\\[result\\]", _result);
                line = line.replaceAll("\\[captured metal\\]", _captured_metal);
                line = line.replaceAll("\\[captured cristal\\]", _captured_cristal);
                line = line.replaceAll("\\[captured deuterium\\]", _captured_deuterium);
                line = line.replaceAll("\\[total captured metal\\]",
                        formatnumber(_total_captured_metal));
                line = line.replaceAll("\\[total captured cristal\\]",
                        formatnumber(_total_captured_cristal));
                line = line.replaceAll("\\[total captured deuterium\\]",
                        formatnumber(_total_captured_deut));
                line = line.replaceAll("\\[harvest metal\\]", _harvest_metal);
                line = line.replaceAll("\\[harvest cristal\\]", _harvest_cristal);
                line = line.replaceAll("\\[total harvest metal\\]",
                        formatnumber(_total_harvest_metal));
                line = line.replaceAll("\\[total harvest cristal\\]",
                        formatnumber(_total_harvest_cristal));
                line = line.replaceAll("\\[attacker losed\\]", _attacker_losed);
                line = line.replaceAll("\\[defender losed\\]", _defender_losed);
                line = line.replaceAll("\\[attacker true losed\\]",
                        formatnumber(_att_true_losed));
                line = line.replaceAll("\\[defender true losed\\]",
                        formatnumber(_def_true_losed));
                line = line.replaceAll("\\[attacker metal losed\\]",
                        formatnumber(att_perte_par_ress[0]));
                line = line.replaceAll("\\[attacker cristal losed\\]",
                        formatnumber(att_perte_par_ress[1]));
                line = line.replaceAll("\\[attacker deuterium losed\\]",
                        formatnumber(att_perte_par_ress[2]));
                line = line.replaceAll("\\[consumption\\]",
                        formatnumber(consumption[RC_index]));
                line = line.replaceAll("\\[total consumption\\]",
                        formatnumber(consumption_total));
                line = line.replaceAll("\\[defender metal losed\\]",
                        formatnumber(def_flt_perte_par_ress[0] + def_def_perte_par_ress[0]));
                line = line.replaceAll("\\[defender cristal losed\\]",
                        formatnumber(def_flt_perte_par_ress[1] + def_def_perte_par_ress[1]));
                line = line.replaceAll("\\[defender deuterium losed\\]",
                        formatnumber(def_flt_perte_par_ress[2] + def_def_perte_par_ress[2]));
                line = line.replaceAll("\\[defender def metal losed\\]",
                        formatnumber(def_def_perte_par_ress[0]));
                line = line.replaceAll("\\[defender def cristal losed\\]",
                        formatnumber(def_def_perte_par_ress[1]));
                line = line.replaceAll("\\[defender def deuterium losed\\]",
                        formatnumber(def_def_perte_par_ress[2]));
                line = line.replaceAll("\\[defender flt metal losed\\]",
                        formatnumber(def_flt_perte_par_ress[0]));
                line = line.replaceAll("\\[defender flt cristal losed\\]",
                        formatnumber(def_flt_perte_par_ress[1]));
                line = line.replaceAll("\\[defender flt deuterium losed\\]",
                        formatnumber(def_flt_perte_par_ress[2]));
                line = line.replaceAll("\\[total attacker metal losed\\]",
                        formatnumber(_total_attacker_lose[0]));
                line = line.replaceAll("\\[total attacker cristal losed\\]",
                        formatnumber(_total_attacker_lose[1]));
                line = line.replaceAll("\\[total attacker deuterium losed\\]",
                        formatnumber(_total_attacker_lose[2]));
                line = line.replaceAll("\\[total defender metal losed\\]",
                        formatnumber(_total_defender_lose[0]));
                line = line.replaceAll("\\[total defender cristal losed\\]",
                        formatnumber(_total_defender_lose[1]));
                line = line.replaceAll("\\[total defender deuterium losed\\]",
                        formatnumber(_total_defender_lose[2]));
                line = line.replaceAll("\\[moon probability\\]", _moon_probability);
                line = line.replaceAll("\\[harvested\\]", formatnumber(tt_pris));
                line = line.replaceAll("\\[harvestable\\]", formatnumber(tt_cdr));
                line = line.replaceAll("\\[harvests reports\\]", _harvests_reports.replaceAll(
                        enter, "[line]"));
                line = line.replaceAll("\\[end cr\\]", _end_cr);

                line = line.replaceAll("\\[attacker fire\\]", formatnumber(_fire[RC_index][0]));
                line = line.replaceAll("\\[attacker firepower\\]",
                        formatnumber(_firepower[RC_index][0]));
                line = line.replaceAll("\\[attacker shields absorb\\]",
                        formatnumber(_shields_absorb[RC_index][0]));
                line = line.replaceAll("\\[defender fire\\]", formatnumber(_fire[RC_index][1]));
                line = line.replaceAll("\\[defender firepower\\]",
                        formatnumber(_firepower[RC_index][1]));
                line = line.replaceAll("\\[defender shields absorb\\]",
                        formatnumber(_shields_absorb[RC_index][1]));

                line = line.replaceAll("\\[attacker\\]", _attacker[RC_index][att_index]);
                line = line.replaceAll("\\[defender\\]", _defender[RC_index][def_index]);
                line = line.replaceAll("\\[rentability defender with\\]", da[RC_index]);
                line = line.replaceAll("\\[rentability attacker with\\]", aa[RC_index]);
                line = line.replaceAll("\\[rentability attacker without\\]", as[RC_index]);
                line = line.replaceAll("\\[rentability defender without\\]", ds[RC_index]);

                line = line.replaceAll("\\[rentability defenders with\\]",
                        formatnumber(da[RC_nb]));
                line = line.replaceAll("\\[rentability attackers with\\]",
                        formatnumber(aa[RC_nb]));
                line = line.replaceAll("\\[rentability attackers without\\]",
                        formatnumber(as[RC_nb]));
                line = line.replaceAll("\\[rentability defenders without\\]",
                        formatnumber(ds[RC_nb]));

                line = line.replaceAll("\\[just cr end\\]", _justrcend);
                line = line.replaceAll("\\[in column\\]", _incolumn);
                line = line.replaceAll("\\[i am attacker\\]", _iamatt);
                line = line.replaceAll("\\[display rentability\\]", _disprenta);
                line = line.replaceAll("\\[just my rentability\\]", _justmyrenta);
                line = line.replaceAll("\\[harvested cr\\]", _CR_harvested);

                line = line.replaceAll("\\[CR index\\]", "" + RC_index);

                // calculate
                tmp = new String[3];
                infinity = 0;
                while ((i = line.indexOf("[calculate]")) != -1
                        && line.indexOf("[/calculate]") != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[calculate]'");
                        return exText;
                    }
                    j = 1;
                    k = i;
                    while (j > 0) {
                        if (infinity++ > 10000) {
                            Print_exception("Infinity loop around '[calculate]'");
                            return exText;
                        }
                        start = line.indexOf("[calculate]", k + 11);
                        jl = line.indexOf("[/calculate]", k + 11);
                        if (start < 0)
                            start = Integer.MAX_VALUE;
                        if (jl < 0)
                            jl = Integer.MAX_VALUE;
                        j += start < jl ? 1 : -1;
                        k = (int) (start < jl ? start : jl);
                    }

                    tmp[0] = line.substring(i + 11, k);
                    tmp[1] = tmp[0];
                    tmp[1] = tmp[1].replaceAll("\\[calculate\\]", "(");
                    tmp[1] = tmp[1].replaceAll("\\[\\/calculate\\]", ")");
                    tmp[2] = Calculate.expression(tmp[1]);
                    line = line.replaceFirst("\\Q[calculate]" + tmp[0] + "[/calculate]\\E",
                            tmp[2]);
                }

                infinity = 0;
                while ((i = line.indexOf("<getCalcul")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop!");
                        return exText;
                    }
                    tmp = line.replaceFirst("(?i).*?<getCalcul(\\d+)>(.*?)<\\/getCalcul>.*",
                            "$1<::>$2").split("<::>");
                    modeldefCalculValue.set((int) exptoint(tmp[0]), (Object) (new Long(
                            exptoint(tmp[1]))));
                    line = line.replaceFirst("(?i)<getCalcul" + tmp[0] + ">" + tmp[1]
                            + "</getCalcul>", "");
                }

                // [if x>y]..[else]..[endif]
                infinity = 0;
                while ((i = line.indexOf("[if ")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[if ]...[else]...[endif]'");
                        return exText;
                    }

                    repl = line.substring(i);
                    tmp = new String[5];
                    tmp[0] = repl.replaceFirst(
                            "^\\[if\\s+(\\-?\\d+(?:\\.\\d+)*|\"[^\"]*\"|'[^']*')\\s*(?:\\<|\\>|i|s|\\!?\\=)\\s*(?:\\-?\\d+(?:\\.\\d+)*|\"[^\"]*\"|'[^']*').*$",
                            "$1");
                    tmp[1] = repl.replaceFirst(
                            "^\\[if\\s+(?:\\-?\\d+(?:\\.\\d+)*|\"[^\"]*\"|'[^']*')\\s*(\\<|\\>|i|s|\\!?\\=)\\s*(?:\\-?\\d+(?:\\.\\d+)*|\"[^\"]*\"|'[^']*').*$",
                            "$1");
                    tmp[2] = repl.replaceFirst(
                            "^\\[if\\s+(?:\\-?\\d+(?:\\.\\d+)*|\"[^\"]*\"|'[^']*')\\s*(?:\\<|\\>|i|s|\\!?\\=)\\s*(\\-?\\d+(?:\\.\\d+)*|\"[^\"]*\"|'[^']*').*$",
                            "$1");

                    j = 2;
                    start = 0;
                    t = 0;
                    while (j != 0) {
                        if (infinity++ > 10000) {
                            Print_exception("Infinity loop around '[if ]...[else]...[endif]'\n\n"
                                    + repl.substring(0, 50));
                            return exText;
                        }
                        jl = repl.length();
                        s = -1;
                        if (0 < (kl = repl.indexOf("[if ", start + 1))) {
                            jl = kl;
                            s = 2;
                        }
                        if (jl > (kl = repl.indexOf("[else]", start + 1))) {
                            if (kl > 0) {
                                s = -1;
                                jl = kl;
                            }
                        }
                        if (jl > (kl = repl.indexOf("[endif]", start + 1))) {
                            if (kl > 0) {
                                s = -1;
                                jl = kl;
                            }
                        }

                        j += s;
                        start = (int) jl;

                        if (j == 1 && t == 0
                                && repl.substring(start, start + 6).equals("[else]")) {
                            tmp[3] = repl.substring(
                                    repl.replaceFirst(
                                            "^(\\[if\\s+(?:\\-?\\d+(?:\\.\\d+)*|\"[^\"]*\"|'[^']*')\\s*(?:\\<|\\>|i|s|\\!?\\=)\\s*(?:\\-?\\d+(?:\\.\\d+)*|\"[^\"]*\"|'[^']*')\\s*\\]).*$",
                                            "$1")
                                            .length(), start);
                            t = start + 6;
                        }
                    }
                    tmp[4] = repl.substring(t, start);
                    repl = repl.substring(0, start + 7);

                    c = tmp[1].charAt(0);
                    if (Pattern.compile(MSK_NB_FORMATED, Pattern.CASE_INSENSITIVE).matcher(
                            tmp[0]).find()
                            && Pattern.compile(MSK_NB_FORMATED, Pattern.CASE_INSENSITIVE)
                            .matcher(tmp[2])
                            .find()) {
                        jl = exptoint(tmp[0]);
                        kl = exptoint(tmp[2]);
                        switch (c) {
                            case '>':
                                if (jl > kl)
                                    j = 3;
                                else
                                    j = 4;
                                break;
                            case '<':
                                if (jl < kl)
                                    j = 3;
                                else
                                    j = 4;
                                break;
                            case '=':
                                if (jl == kl)
                                    j = 3;
                                else
                                    j = 4;
                                break;
                            case '!':
                                if (jl != kl)
                                    j = 3;
                                else
                                    j = 4;
                                break;
                            case 's':
                                if (jl >= kl)
                                    j = 3;
                                else
                                    j = 4;
                                break;
                            case 'i':
                                if (jl <= kl)
                                    j = 3;
                                else
                                    j = 4;
                                break;
                            default:
                                j = 3;
                        }
                    } else {
                        tmp[0] = tmp[0].substring(1, tmp[0].length() - 1);
                        tmp[2] = tmp[2].substring(1, tmp[2].length() - 1);
                        switch (c) {
                            case '=':
                                if (tmp[0].equals(tmp[2]))
                                    j = 3;
                                else
                                    j = 4;
                                break;
                            case '!':
                                if (!tmp[0].equals(tmp[2]))
                                    j = 3;
                                else
                                    j = 4;
                                break;
                            default:
                                j = 3;
                        }
                    }
                    line = line.replaceFirst("\\Q" + repl + "\\E", tmp[j]);
                }

                // création des cellules du tableau après les boucles et
                // les variables mais avant les balises:
                infinity = 0;
                while ((i = line.indexOf("[cell ")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[cell ]...[/cell]'");
                        return exText;
                    }
                    repl = line.substring(i, line.indexOf("[/cell]"));
                    j = repl.length() + 7;
                    tmp = new String[6];
                    if ((tmp[0] = repl.replaceFirst(
                            "^\\[cell .*wid?th=['\"](\\d+)['\"][^\\]]*?\\].*", "$1")).equals(repl))
                        tmp[0] = "10";
                    if ((tmp[1] = repl.replaceFirst(
                            "^\\[cell .*motif=['\"](.{1})['\"][^\\]]*?\\].*", "$1")).equals(repl))
                        tmp[1] = "_";
                    if ((tmp[2] = repl.replaceFirst(
                            "^\\[cell .*align=['\"](left|right|center)['\"][^\\]]*?\\].*",
                            "$1")).equals(repl))
                        tmp[2] = "left";
                    if ((tmp[3] = repl.replaceFirst(
                            "^\\[cell .*color=['\"]#([\\dA-F]{6}|none)['\"][^\\]]*?\\].*",
                            "$1")).equals(repl))
                        tmp[3] = "none";
                    if ((tmp[5] = repl.replaceFirst(
                            "^\\[cell .*font=['\"]([\\d\\w\\s,_\\-]+)['\"][^\\]]*?\\].*", "$1")).equals(repl))
                        tmp[5] = "Courier,Courier New";
                    tmp[4] = repl = repl.substring(repl.indexOf("]") + 1);
                    repl = repl.replaceAll(
                            "(\\[\\/?url_ogsconverter\\]|\\[\\/?b\\]|\\[\\/?i\\]|\\[\\/?u\\]|\\[\\/?center\\]|\\[\\/?code\\]|\\[\\/?quote\\]|\\[\\/?size=\\d+\\]|\\[\\/?font=(\\d|\\w|\\s)+\\]|\\[img\\]http:\\/\\/(\\d|\\w|-|_|\\.|\\/)+\\[\\/img\\]|\\[\\/?url=http:\\/\\/(\\d|\\w|-|_|\\.|\\/)+\\]|\\[\\/?color=#(\\d|\\w){6}\\]|\\[line\\]|\\[\\/?wordcolor=#(\\d|\\w){6}\\-to\\-#(\\d|\\w){6}(\\-to\\-#(\\d|\\w){6})?\\])",
                            "");
                    k = repl.length();
                    if (k < exptoint(tmp[0])) {
                        if (tmp[2].equals("left")) {
                            if (tmp[3].length() == 6)
                                tmp[4] += BBCode.addbcolor(0, tmp[3]);
                            for (start = k; start < exptoint(tmp[0]); start++) {
                                tmp[4] += tmp[1];
                            }
                            if (tmp[3].length() == 6)
                                tmp[4] += BBCode.addbcolor(1, tmp[3]);
                        } else if (tmp[2].equals("right")) {
                            if (tmp[3].length() == 6)
                                tmp[4] = BBCode.addbcolor(1, tmp[3]) + tmp[4];
                            for (start = k; start < exptoint(tmp[0]); start++) {
                                tmp[4] = tmp[1] + tmp[4];
                            }
                            if (tmp[3].length() == 6)
                                tmp[4] = BBCode.addbcolor(0, tmp[3]) + tmp[4];
                        } else if (tmp[2].equals("center")) {
                            if (tmp[3].length() == 6)
                                tmp[4] += BBCode.addbcolor(0, tmp[3]);
                            kl = k;
                            for (start = 0; start < (int) ((float) (exptoint(tmp[0]) - k) / 2); start++) {
                                tmp[4] += tmp[1];
                                kl++;
                            }
                            if (tmp[3].length() == 6)
                                tmp[4] = BBCode.addbcolor(1, tmp[3]) + tmp[4]
                                        + BBCode.addbcolor(1, tmp[3]);
                            for (start = (int) kl; start < exptoint(tmp[0]); start++) {
                                tmp[4] = tmp[1] + tmp[4];
                            }
                            if (tmp[3].length() == 6)
                                tmp[4] = BBCode.addbcolor(0, tmp[3]) + tmp[4];
                        }
                    } else if (k == exptoint(tmp[0]))
                        ;
                    else if ((part = tmp[4].replaceFirst("\\Q" + repl + "\\E", repl.substring(
                            0, Integer.parseInt(tmp[0]) - 1)
                            + ".")).equals(tmp[4]))
                        tmp[4] = repl.substring(0, Integer.parseInt(tmp[0]) - 1) + ".";
                    else
                        tmp[4] = part;

                    if (!tmp[5].equals("none"))
                        tmp[4] = BBCode.addfont(0, tmp[5]) + tmp[4]
                                + BBCode.addfont(1, tmp[5]);
                    line = line.replaceAll("\\Q" + line.substring(i, i + j) + "\\E", tmp[4]);
                }

                infinity = 0;
                while ((i = line.indexOf("[wordcolor=#")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[wordcolor=#XXXXXX]'");
                        return exText;
                    }
                    repl = line.substring(i, i + 1 + line.substring(i).indexOf(']'));
                    if (Pattern.compile("\\[wordcolor\\=#[\\dA-F]{6}\\-to\\-#[\\dA-F]{6}\\]",
                            Pattern.CASE_INSENSITIVE).matcher(repl).find()) {
                        tmp = new String[7];
                        tmp[0] = repl.substring(12, 14);
                        tmp[1] = repl.substring(14, 16);
                        tmp[2] = repl.substring(16, 18);
                        tmp[3] = repl.substring(23, 25);
                        tmp[4] = repl.substring(25, 27);
                        tmp[5] = repl.substring(27, 29);
                        tmp[6] = line.substring(i + 30, line.indexOf("[/wordcolor="));
                        if (tmp[6].equals(""))
                            line = line.replaceAll("\\Q" + repl + tmp[6] + "[/"
                                    + repl.substring(1) + "\\E", "");
                        line = line.replaceAll("\\Q" + repl + tmp[6] + "[/"
                                + repl.substring(1) + "\\E", degrade(tmp[6], hextoint(tmp[0]),
                                hextoint(tmp[1]), hextoint(tmp[2]), hextoint(tmp[3]),
                                hextoint(tmp[4]), hextoint(tmp[5])));
                    } else if (Pattern.compile(
                            "\\[wordcolor\\=#[\\dA-F]{6}\\-to\\-#[\\dA-F]{6}\\-to\\-#[\\dA-F]{6}\\]",
                            Pattern.CASE_INSENSITIVE)
                            .matcher(repl)
                            .find()) {
                        tmp = new String[10];
                        tmp[0] = repl.substring(12, 14);
                        tmp[1] = repl.substring(14, 16);
                        tmp[2] = repl.substring(16, 18);
                        tmp[3] = repl.substring(23, 25);
                        tmp[4] = repl.substring(25, 27);
                        tmp[5] = repl.substring(27, 29);
                        tmp[6] = repl.substring(34, 36);
                        tmp[7] = repl.substring(36, 38);
                        tmp[8] = repl.substring(38, 40);
                        tmp[9] = line.substring(i + 41, line.indexOf("[/wordcolor="));
                        if (tmp[9].equals(""))
                            line = line.replaceAll("\\Q" + repl + tmp[9] + "[/"
                                    + repl.substring(1) + "\\E", "");
                        line = line.replaceFirst("\\Q" + repl
                                + tmp[9].substring(0, tmp[9].length() / 2) + "\\E", degrade(
                                tmp[9].substring(0, tmp[9].length() / 2), hextoint(tmp[0]),
                                hextoint(tmp[1]), hextoint(tmp[2]), hextoint(tmp[3]),
                                hextoint(tmp[4]), hextoint(tmp[5])));
                        line = line.replaceFirst("\\Q" + tmp[9].substring(tmp[9].length() / 2)
                                + "[/" + repl.substring(1) + "\\E", degrade(
                                tmp[9].substring(tmp[9].length() / 2 - 1), hextoint(tmp[3]),
                                hextoint(tmp[4]), hextoint(tmp[5]), hextoint(tmp[6]),
                                hextoint(tmp[7]), hextoint(tmp[8])).substring(32));
                    }
                }

                if (Pattern.compile("\\[url_ogsconverter\\]\\s*\\S+").matcher(line).find()
                        && !Pattern.compile(
                        "\\[url_ogsconverter\\](\\s|\\[\\/?b\\]|\\[\\/?i\\]|\\[\\/?u\\]|\\[\\/?center\\]|\\[\\/?code\\]|\\[\\/?quote\\]|\\[\\/?size=\\d+\\]|\\[\\/?font=(\\d|\\w|\\s)+\\]|\\[img\\]http:\\/\\/(\\d|\\w|-|_|\\.|\\/)+\\[\\/img\\]|\\[\\/?url=http:\\/\\/(\\d|\\w|-|_|\\.|\\/)+\\]|\\[\\/?color=#(\\d|\\w){6}\\]|\\[line\\])*\\[\\/url_ogsconverter\\]")
                        .matcher(line)
                        .find())
                    link_open = true;
                if (Pattern.compile("\\[\\/url_ogsconverter\\]").matcher(line).find())
                    link_close = true;

                line = line.replaceAll("\\[b\\]", BBCode.addbold(0));
                line = line.replaceAll("\\[\\/b\\]", BBCode.addbold(1));
                line = line.replaceAll("\\[i\\]", BBCode.addita(0));
                line = line.replaceAll("\\[\\/i\\]", BBCode.addita(1));
                line = line.replaceAll("\\[center\\]", BBCode.addcenter(0));
                line = line.replaceAll("\\[\\/center\\]", BBCode.addcenter(1));
                line = line.replaceAll("\\[u\\]", BBCode.addunder(0));
                line = line.replaceAll("\\[\\/u\\]", BBCode.addunder(1));
                line = line.replaceAll("\\[code\\]", BBCode.addcode(0));
                line = line.replaceAll("\\[\\/code\\]", BBCode.addcode(1));
                line = line.replaceAll("\\[quote\\]", BBCode.addquote(0));
                line = line.replaceAll("\\[\\/quote\\]", BBCode.addquote(1));

                line = line.replaceAll("\\[size=(\\d+)\\]", BBCode.addsize(0, "\\$1"));
                line = line.replaceAll("\\[\\/size=(\\d+)\\]", BBCode.addsize(1, "\\$1"));
                line = line.replaceAll("\\[font=([\\d\\w\\s,_\\-]+)\\]", BBCode.addfont(0,
                        "\\$1"));
                line = line.replaceAll("\\[\\/font=([\\d\\w\\s,_\\-]+)\\]", BBCode.addfont(1,
                        "\\$1"));
                line = line.replaceAll("\\[img\\](http:\\/\\/[\\d\\w-_\\.\\/%]+)\\[\\/img\\]",
                        BBCode.addimg(0, "\\$1") + BBCode.addimg(1, "\\$1"));
                line = line.replaceAll("\\[url=(http:\\/\\/[\\d\\w-_\\.\\/\\=]+)\\]",
                        BBCode.addurl(0, "\\$1"));
                line = line.replaceAll("\\[\\/url=(http:\\/\\/[\\d\\w-_\\.\\/\\=]+)\\]",
                        BBCode.addurl(1, "\\$1"));
                line = line.replaceAll("\\[color=#([\\dA-Fa-f]{6})\\]", BBCode.addbcolor(0,
                        "\\$1"));
                line = line.replaceAll("\\[\\/color=#([\\dA-Fa-f]{6})\\]", BBCode.addbcolor(1,
                        "\\$1"));

                line = line.replaceAll("\\[url_ogsconverter\\]", BBCode.addurl(0, ogsc_url));
                line = line.replaceAll("\\[\\/url_ogsconverter\\]", BBCode.addurl(1, ogsc_url));

                infinity = 0;
                while ((j = line.indexOf("[lower case]")) != -1
                        && (k = line.indexOf("[/lower case]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[lower case]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[lower case]".length(), k);
                    tmp[2] = line.substring(k + "[/lower case]".length());

                    line = tmp[0] + tmp[1].toLowerCase() + tmp[2];
                }

                infinity = 0;
                while ((j = line.indexOf("[upper case]")) != -1
                        && (k = line.indexOf("[/upper case]")) != -1) {
                    if (infinity++ > 10000) {
                        Print_exception("Infinity loop around '[upper case]'");
                        return exText;
                    }
                    tmp = new String[3];
                    tmp[0] = line.substring(0, j);
                    tmp[1] = line.substring(j + "[upper case]".length(), k);
                    tmp[2] = line.substring(k + "[/upper case]".length());

                    line = tmp[0] + tmp[1].toUpperCase() + tmp[2];
                }

                line = line.replaceAll("\\[line\\]", enter);

                textchanged.append(line);

            } catch (Exception e) {
                ExceptionAlert.createExceptionAlert(e, exText);
                e.printStackTrace();
            }

        }

        if (!link_open || !link_close)
            textchanged.append(" " + BBCode.addcenter(0) + BBCode.addsize(0, "10")
                    + BBCode.addurl(0, ogsc_url) + BBCode.addbcolor(0, "66FFCC")
                    + "Created by OGSconverter" + BBCode.addbcolor(1, "66FFCC")
                    + BBCode.addurl(1, ogsc_url) + BBCode.addsize(1, "10")
                    + BBCode.addcenter(1));

        // t2 = Calendar.getInstance().get(Calendar.MINUTE) * 1000000
        // + Calendar.getInstance().get(Calendar.SECOND) * 1000
        // + Calendar.getInstance().get(Calendar.MILLISECOND);
        // Print_exception("" + Long.toString(t2 - t1));

        line = textchanged.toString();
        return line;
    }

    public static void sendForStats(String text, int speed) {
        Configuration configC = null;
        Configuration configL = null;
        RCDatas rcDatas = null;
        long id;

        try {
            configC = new Configuration("config.ini");
            configL = new Configuration("lang_" + configC.getConfig("active_language")
                    + ".ini");

            text = Main.prepareText(text, configL);

            if (!Pattern.compile(configL.getConfig("TOP"), Pattern.CASE_INSENSITIVE).matcher(
                    text).find()
                    && !Pattern.compile(configL.getConfig("TOP2"), Pattern.CASE_INSENSITIVE)
                    .matcher(text)
                    .find()) {
                Print_exception("Statistics are available only for Combat Reports.");
                return;
            }

            text = "ok \n" + text;

            if (text.split(configL.getConfig("TOP")).length > 2
                    || text.split(configL.getConfig("TOP2")).length > 2) {
                Print_exception("Please, only one report for statistics.");
                return;
            }

            rcDatas = new RCDatas();
            rcDatas.generateDatas(text, speed);

            id = OGSConnection.sendDataStats(OGSC_STATS_URL + "getStats.php", rcDatas);

            if (id >= 0) {
                Main.openurl(OGSC_STATS_URL + "report.php?statsID=" + id);
            }

        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, text);
            e.printStackTrace();
        }
    }

    public static String converter(String text, int speed) {

        String setting = "0", repl, part = "", fichier = "", textchanged = "", tmp[] = null, tmp2[] = null, line, exploded[];
        String[] fleet = null, def = null, ifleet = null, idef = null;
        int i, j, k, start, round = 0;
        boolean ok = false, go_end = false, bar = false/* , att_destr = false */;
        boolean RC_end = false, CR_harvested = false;
        int pts[] = {2000, 2000, 8000, 37000, 8000, 130000, 20000, 100000};
        int fpts[] = {4000, 12000, 4000, 10000, 29000, 60000, 40000, 18000, 1000, 90000,
                2500, 125000, 10000000, 85000};
        long defpts = 0, courante_attgain = 0;
        long attgain = 0, defgain = 0, cdr = 0, tt_pris = 0;
        String _top1 = "", _top2 = "", _att = "", _def = "", _type = "", _nb = "", _techno = "", _moon = "", _lose = "", _rec_top = "", _iamatt = "", _myrenta = "";
        BBCode.setBBCode();
        int[] att_fleet = null, att_coord = null, def_coord = null;
        long consumption = 0;

        Configuration configC = null;
        Configuration configL = null;

        try {
            configC = new Configuration("config.ini");

            setting = configC.getConfig("color");

            if (configC.getConfig("CR_end").equals("1")) {
                RC_end = true;
            }

            if (setting.equals("01")) {
                color = DARKCOLOR;
            } else if (setting.equals("02")) {
                color = LIGHTCOLOR;
            } else {
                color = configC.getConfig("user_color" + setting).split(",");
            }

            setting = setting.substring(0, 1);

            fichier = "lang_" + configC.getConfig("active_language") + ".ini";
            configL = new Configuration(fichier);

            text = text.replaceAll(configL.getConfig("textaera"), "");
            text = text.replaceAll("Bad copy !", "");

            fleet = configL.getConfig("FLEET").split("\\|");
            def = configL.getConfig("DEFENCES").split("\\|");

            ifleet = StringOperation.sansAccent(configL.getConfig("FLEET"))
                    .toLowerCase()
                    .replace('.', ' ')
                    .replaceAll("\\s+", "")
                    .split("\\|");
            idef = StringOperation.sansAccent(configL.getConfig("DEFENCES"))
                    .toLowerCase()
                    .replace('.', ' ')
                    .replaceAll("\\s+", "")
                    .split("\\|");

            _top1 = configL.getConfig("TOP");
            _top2 = configL.getConfig("TOP2");
            _att = configL.getConfig("ATT");
            _def = configL.getConfig("DEF");
            _type = configL.getConfig("TYPE");
            _nb = configL.getConfig("NB");
            _techno = configL.getConfig("TECHNO");
            _moon = configL.getConfig("MOONF");
            _lose = configL.getConfig("LOSE");
            _rec_top = configL.getConfig("REC_TOP");
            _iamatt = configC.getConfig("iamatt");
            _myrenta = configC.getConfig("myrenta");

            if (configC.getConfig("CR_harvested").equals("1"))
                CR_harvested = true;

            text = text.replaceAll("(" + _top1 + ")", enter + "$1");
            text = text.replaceAll("(" + _top2 + ")", enter + "$1");
            text = text.replaceAll("(" + _rec_top + ")", enter + "$1");
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, text);
            e.printStackTrace();
        }

        exText = text;
        text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])", "$1"
                + String.valueOf((char) 10) + "$2");
        text = text.replaceAll(String.valueOf((char) 13), "");
        exploded = text.split(String.valueOf((char) 10));

        textchanged += BBCode.addcenter(0);

        for (i = 0; i < exploded.length; i++) {
            line = exploded[i].trim();
            if (line.equals(""))
                continue;
            try {
                if (Pattern.compile(MSK_USER_MSG, Pattern.CASE_INSENSITIVE)
                        .matcher(line)
                        .find()
                        || Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE)
                        .matcher(line)
                        .find()) {
                    boolean harvest_find = Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find();
                    for (i++; i < exploded.length; i++) {
                        if (Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE).matcher(
                                exploded[i]).find()
                                || Pattern.compile(MSK_USER_MSG, Pattern.CASE_INSENSITIVE)
                                .matcher(exploded[i])
                                .find()
                                || Pattern.compile(_top1, Pattern.CASE_INSENSITIVE).matcher(
                                exploded[i]).find()
                                || Pattern.compile(_top2, Pattern.CASE_INSENSITIVE).matcher(
                                exploded[i]).find()) {
                            if (harvest_find)
                                break;
                            harvest_find = true;
                        }
                        line += " " + exploded[i];
                    }
                    // pour le ++ du for.
                    i--;
                    tmp = converterRecy(line, IN_ATT);
                    tt_pris += exptoint(tmp[1]);
                    if (bar)
                        textchanged += "_______________________________" + enter;
                    textchanged += tmp[0];
                    bar = true;
                    continue;
                }

                if (!ok
                        && (Pattern.compile(_top1, Pattern.CASE_INSENSITIVE)
                        .matcher(line)
                        .find() || Pattern.compile(_top2, Pattern.CASE_INSENSITIVE)
                        .matcher(line)
                        .find())) {

                    // consommation:
                    if (att_fleet != null && att_coord != null && def_coord != null) {
                        consumption += Main.getConsumption(att_coord, def_coord, att_fleet,
                                speed);
                        att_coord = null;
                        def_coord = null;
                    }

                    att_fleet = new int[fleet.length];

                    if (bar)
                        textchanged += "_______________________________" + enter;
                    line = line.replaceAll(MSK_DATE_ONLY, BBCode.addbold(0) + " $1 "
                            + BBCode.addbold(1))
                            + enter;
                    line = line.replaceAll("\\.:", ":");
                    line = line.replaceAll("::", ":");
                    if (configC.getConfig("RC_date").equals("1") && !RC_end)
                        textchanged += line;
                    ok = true;
                    go_end = false;
                    part = "attack";
                    bar = true;
                    courante_attgain = 0;
                    continue;
                }

                if (ok || RC_end) {

                    if (Pattern.compile("^" + _att + MSK_NAME_COORD_1, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find()) {
                        if (part.equals("def") && !go_end) {
                            round = 0;
                            for (j = i; j < exploded.length; j++) {
                                if (Pattern.compile(_lose, Pattern.CASE_INSENSITIVE).matcher(
                                        exploded[j]).find())
                                    break;

                                if (part.equals("def")
                                        && Pattern.compile("^" + _att + MSK_NAME_COORD_1,
                                        Pattern.CASE_INSENSITIVE)
                                        .matcher(exploded[j])
                                        .find()) {
                                    i = j - 1;
                                    round++;
                                    part = "attack";
                                } else if (Pattern.compile("^" + _def + MSK_NAME_COORD_1,
                                        Pattern.CASE_INSENSITIVE).matcher(exploded[j]).find())
                                    part = "def";
                            }
                            if (!RC_end)
                                textchanged += enter
                                        + BBCode.addita(0)
                                        + configC.getConfig("afterbattle").replaceAll("!n",
                                        String.valueOf(round)) + BBCode.addita(1)
                                        + enter;
                            part = "attack";
                            go_end = true;
                            continue;
                        }
                        if (configC.getConfig("attname").equals("1"))
                            repl = _att + BBCode.addbold(0) + BBCode.addbcolor(0, color[0])
                                    + "$1" + BBCode.addbcolor(1, color[0]) + BBCode.addbold(1)
                                    + "\\(" + BBCode.addbold(0) + "$2" + BBCode.addbold(1)
                                    + "\\)";
                        else
                            repl = _att + BBCode.addbold(0) + BBCode.addbcolor(0, color[0])
                                    + " " + configC.getConfig("att_rpl_name") + " "
                                    + BBCode.addbcolor(1, color[0]) + BBCode.addbold(1)
                                    + "\\(" + BBCode.addbold(0) + "$2" + BBCode.addbold(1)
                                    + "\\)";
                        if (!configC.getConfig("attcoord").equals("1"))
                            repl = repl.replaceAll("\\$2", configC.getConfig("att_rpl_coord"));
                        tmp = line.split(_type);
                        if (tmp.length > 1)
                            line = _type + tmp[1];
                        if (!RC_end)
                            textchanged += enter
                                    + tmp[0].replaceAll(_att + MSK_NAME_COORD_2, repl) + enter;
                        if (att_coord == null) {
                            tmp = tmp[0].replaceAll(_att + MSK_NAME_COORD_2, "$2").split(":");
                            if (tmp.length >= 3) {
                                att_coord = new int[]{(int) exptoint(tmp[0]),
                                        (int) exptoint(tmp[1]), (int) exptoint(tmp[2])};
                            }
                        }
                        tmp = null;
                    } else if (Pattern.compile("^" + _def + MSK_NAME_COORD_1,
                            Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                        part = "def";
                        if (configC.getConfig("defname").equals("1"))
                            repl = _def + BBCode.addbold(0) + BBCode.addbcolor(0, color[1])
                                    + "$1" + BBCode.addbcolor(1, color[1]) + BBCode.addbold(1)
                                    + "\\(" + BBCode.addbold(0) + "$2" + BBCode.addbold(1)
                                    + "\\)";
                        else
                            repl = _def + BBCode.addbold(0) + BBCode.addbcolor(0, color[1])
                                    + " " + configC.getConfig("def_rpl_name") + " "
                                    + BBCode.addbcolor(1, color[1]) + BBCode.addbold(1)
                                    + "\\(" + BBCode.addbold(0) + "$2" + BBCode.addbold(1)
                                    + "\\)";
                        if (!configC.getConfig("defcoord").equals("1"))
                            repl = repl.replaceAll("\\$2", configC.getConfig("def_rpl_coord"));
                        tmp = line.split(_type);
                        if (tmp.length > 1)
                            line = _type + tmp[1];
                        if (!RC_end)
                            textchanged += enter
                                    + tmp[0].replaceAll(_def + MSK_NAME_COORD_2, repl) + enter;
                        if (def_coord == null) {
                            tmp = tmp[0].replaceAll(_def + MSK_NAME_COORD_2, "$2").split(":");
                            if (tmp.length >= 3) {
                                def_coord = new int[]{(int) exptoint(tmp[0]),
                                        (int) exptoint(tmp[1]), (int) exptoint(tmp[2])};
                            }
                        }
                        tmp = null;
                    } else if (Pattern.compile("^" + _techno + "\\s+" + MSK_NB_FORMATED + "%",
                            Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                        tmp = line.split(_type);
                        if (tmp.length > 1)
                            line = _type + tmp[1];
                        if (configC.getConfig("Techno").equals("1") && !RC_end)
                            textchanged += tmp[0].replaceAll("(" + MSK_NB_FORMATED + ")%",
                                    BBCode.addbold(0) + "$1" + BBCode.addbold(1) + "%")
                                    + enter;
                        tmp = null;
                    }

                    if (Pattern.compile("^" + _type, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find()) {
                        repl = configL.getConfig("FLEET") + "|"
                                + configL.getConfig("DEFENCES");
                        repl = StringOperation.sansAccent(repl);
                        repl = repl.toLowerCase();
                        repl = repl.replaceAll("[\\s\\.]+", "");
                        line = StringOperation.sansAccent(line);
                        line = line.toLowerCase();
                        line = line.replaceAll("[\\s\\.]+", "");
                        line = line.replaceAll("(" + repl + ")", " $1 ");
                        repl = line.replaceAll("(" + repl + ").*$", "");
                        tmp = line.substring(repl.length()).split("\\s{2,}");
                        if (!configC.getConfig("column").equals("1")) {
                            if (!RC_end)
                                textchanged += _type + ":";
                            start = 0;
                            for (j = 0; j < tmp.length; j++) {
                                if (tmp[j].trim().equals(""))
                                    continue;
                                for (k = start; k < (fleet.length + def.length); k++) {
                                    if (k < fleet.length && tmp[j].trim().equals(ifleet[k])) {
                                        repl = fleet[k];
                                        break;
                                    } else if (k > (fleet.length - 1)
                                            && k < (fleet.length + def.length)
                                            && tmp[j].trim().equals(idef[k - fleet.length])) {
                                        repl = def[k - fleet.length];
                                        break;
                                    }
                                }
                                if (start != 0 && k == (fleet.length + def.length)) {
                                    j--;
                                    start = 0;
                                    continue;
                                } else if (k == (fleet.length + def.length)) {
                                    Print_exception("Ship or défense not recognized: \""
                                            + tmp[j] + "\"");
                                    repl = tmp[j];
                                }
                                if (!RC_end)
                                    textchanged += BBCode.addbcolor(0, color[k + 2]) + " "
                                            + repl + BBCode.addbcolor(1, color[k + 2]);
                                start = k;
                            }
                            if (!RC_end)
                                textchanged += enter;
                        }
                        continue;
                    } else if (Pattern.compile("^" + _nb, Pattern.CASE_INSENSITIVE).matcher(
                            line).find()) {
                        line = line.replaceAll("[\\.,]", "");
                        tmp2 = line.replaceAll(MSK_NB_LINE, "$1").split("\\s+");
                        if (!RC_end)
                            textchanged += _nb + ":";
                        start = 0;
                        for (j = 0; j < tmp2.length; j++) {
                            if (tmp2[j].trim().equals(""))
                                continue;
                            repl = "";
                            for (k = start; k < (fleet.length + def.length); k++) {
                                if (k < fleet.length
                                        && tmp[j].trim().equalsIgnoreCase(ifleet[k])) {
                                    if (!go_end && part.equals("def"))
                                        defgain -= exptoint(tmp2[j]) * fpts[k];
                                    else if (go_end && part.equals("def"))
                                        defgain += exptoint(tmp2[j]) * fpts[k];
                                    else if (!go_end && part.equals("attack")) {
                                        attgain -= exptoint(tmp2[j]) * fpts[k];
                                        courante_attgain += exptoint(tmp2[j]) * fpts[k];
                                        att_fleet[k] += (int) exptoint(tmp2[j]);
                                    } else if (go_end && part.equals("attack"))
                                        attgain += exptoint(tmp2[j]) * fpts[k];
                                    repl = fleet[k];
                                    break;
                                } else if (k > (fleet.length - 1)
                                        && k < (fleet.length + def.length)
                                        && tmp[j].trim().equalsIgnoreCase(
                                        idef[k - fleet.length])) {
                                    if (!go_end && part.equals("def"))
                                        defpts -= exptoint(tmp2[j]) * pts[k - fleet.length];
                                    else if (part.equals("def"))
                                        defpts += exptoint(tmp2[j]) * pts[k - fleet.length];
                                    repl = def[k - fleet.length];
                                    break;
                                }
                            }
                            if (start != 0 && k == (fleet.length + def.length)) {
                                j--;
                                start = 0;
                                continue;
                            } else if (k == (fleet.length + def.length)) {
                                Print_exception("Ship or défense not recognized: \"" + tmp[j]
                                        + "\"");
                                repl = tmp[j];
                            }
                            if (configC.getConfig("column").equals("0") && !RC_end)
                                textchanged += BBCode.addbcolor(0, color[k + 2]) + " "
                                        + formatnumber(tmp2[j])
                                        + BBCode.addbcolor(1, color[k + 2]);
                            else if (!RC_end)
                                textchanged += enter + BBCode.addbcolor(0, color[k + 2])
                                        + repl + " " + formatnumber(tmp2[j])
                                        + BBCode.addbcolor(1, color[k + 2]);
                            start = k;
                        }
                        if (!RC_end)
                            textchanged += enter;

                        tmp = null;
                        tmp2 = null;
                        continue;
                    } else if (Pattern.compile("^" + configL.getConfig("DEST"),
                            Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                        if (!RC_end)
                            textchanged += BBCode.addbold(0) + BBCode.addbcolor(0, color[0])
                                    + line + BBCode.addbcolor(1, color[0]) + BBCode.addbold(1)
                                    + enter;
                        if (go_end == false) {
                            attgain += courante_attgain;
                        }// else if (part.equals("attack"))
                        // att_destr = true;
                        go_end = true;
                        continue;
                    } else if (go_end
                            && (Pattern.compile("^" + configL.getConfig("END1"),
                            Pattern.CASE_INSENSITIVE).matcher(line).find()
                            || Pattern.compile("^" + configL.getConfig("END2"),
                            Pattern.CASE_INSENSITIVE).matcher(line).find() || Pattern.compile(
                            "^" + configL.getConfig("END3"), Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find())) {
                        textchanged += enter + BBCode.addbold(0) + line + BBCode.addbold(1)
                                + enter;
                        ok = true;
                        continue;
                    } else if (ok
                            && go_end
                            && Pattern.compile("^" + configL.getConfig("GAIN"),
                            Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                        if (Pattern.compile("^" + configL.getConfig("GAIN") + "$",
                                Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                            line = line + enter + exploded[i + 1];
                        }
                        tmp = line.replaceFirst("\\D+", "").split("\\D{3,}");
                        line = line + enter;
                        for (k = 0; k < tmp.length; k++) {
                            start = line.lastIndexOf(']');
                            if (start < 0)
                                start = 0;
                            line = line.substring(0, start)
                                    + line.substring(start).replaceFirst(
                                    tmp[k].replaceAll("\\.", "\\\\.").replaceAll(
                                            "\\+", "\\\\+"),
                                    BBCode.addbold(0)
                                            + BBCode.addbcolor(0, color[fleet.length
                                            + def.length + 2])
                                            + formatnumber(tmp[k])
                                            + BBCode.addbcolor(1, color[fleet.length
                                            + def.length + 2])
                                            + BBCode.addbold(1));
                        }
                        textchanged += line + enter;
                        // tmp = null;
                        continue;
                    } else if (ok
                            && go_end
                            && Pattern.compile("^" + _lose, Pattern.CASE_INSENSITIVE).matcher(
                            line).find()) {
                        textchanged += enter;
                        repl = "";
                        ok = true;
                        for (j = i; j < exploded.length; j++) {
                            if (!ok || exploded[j].trim().equals("")) {
                                ok = false;
                                continue;
                            }
                            if (Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE).matcher(
                                    exploded[j]).find()
                                    || Pattern.compile(_top1, Pattern.CASE_INSENSITIVE)
                                    .matcher(exploded[j])
                                    .find()
                                    || Pattern.compile(_top2, Pattern.CASE_INSENSITIVE)
                                    .matcher(exploded[j])
                                    .find()) {
                                i = j - 1;
                                ok = false;
                                break;
                            }
                            tmp2 = exploded[j].split("(\\D{3,}|(\\s*\\%\\s*))");
                            repl += exploded[j];
                            line = exploded[j];
                            if (Pattern.compile("^" + _moon, Pattern.CASE_INSENSITIVE)
                                    .matcher(line)
                                    .find()) {
                                line = exploded[j].replaceAll(configL.getConfig("MOON"),
                                        BBCode.addbold(0)
                                                + BBCode.addsize(0,
                                                configC.getConfig("user_sz_rss"
                                                        + setting))
                                                + BBCode.addbcolor(0, color[fleet.length
                                                + def.length + 3])
                                                + configL.getConfig("MOON")
                                                + BBCode.addbcolor(1, color[fleet.length
                                                + def.length + 3])
                                                + BBCode.addsize(1, null) + BBCode.addbold(1))
                                        + enter;
                            } else {
                                for (k = 0; k < tmp2.length; k++) {
                                    if (tmp2[k].equals(""))
                                        continue;
                                    start = line.lastIndexOf(']');
                                    if (start < 0)
                                        start = 0;
                                    line = line.substring(0, start)
                                            + line.substring(start)
                                            .replaceFirst(
                                                    tmp2[k].replaceAll("\\.", "\\\\.")
                                                            .replaceAll("\\+", "\\\\+"),
                                                    BBCode.addbold(0)
                                                            + BBCode.addsize(
                                                            0,
                                                            configC.getConfig("user_sz_rss"
                                                                    + setting))
                                                            + BBCode.addbcolor(
                                                            0,
                                                            color[fleet.length
                                                                    + def.length
                                                                    + 3])
                                                            + formatnumber(tmp2[k])
                                                            + BBCode.addbcolor(
                                                            1,
                                                            color[fleet.length
                                                                    + def.length
                                                                    + 3])
                                                            + BBCode.addsize(1, null)
                                                            + BBCode.addbold(1));
                                }
                            }
                            textchanged += line + enter;
                            tmp2 = null;
                        }
                        if (configC.getConfig("cr_taux").equals("1")) {
                            if (tmp != null && tmp.length >= 3)
                                attgain += exptoint(tmp[0]) + exptoint(tmp[1])
                                        + exptoint(tmp[2]);
                            tmp = null;
                            tmp = repl.replaceFirst("\\D+", "").split("\\D{3,}");
                            // attgain -= exptoint(tmp[0]);
                            defgain += (int) (0.3 * (double) (defpts));
                            cdr += exptoint(tmp[2]) + exptoint(tmp[3]);

                            defpts = 0;
                        }

                        continue;
                    }
                }

            } catch (Exception e) {
                ExceptionAlert.createExceptionAlert(e, exText);
                e.printStackTrace();
            }
        }

        try {

            // consommation:
            if (att_fleet != null && att_coord != null && def_coord != null) {
                consumption += Main.getConsumption(att_coord, def_coord, att_fleet, speed);
                att_coord = null;
                def_coord = null;
            }

            if (configC.getConfig("cr_consumption").equals("1")) {
                textchanged += "_______________________________" + enter;
                textchanged += enter + BBCode.addunder(0) + BBCode.addbold(0)
                        + configL.getConfig("consumption") + ":" + BBCode.addbold(1)
                        + BBCode.addunder(1)
                        + BBCode.addbcolor(0, color[fleet.length + def.length + 2]) + " "
                        + formatnumber(consumption)
                        + BBCode.addbcolor(1, color[fleet.length + def.length + 2]) + enter;
            }

            if (configC.getConfig("cr_taux").equals("1")) {
                String aa, as, da, ds;

                if (_iamatt.equals("1")) {
                    aa = formatnumber(cdr + attgain);
                    as = formatnumber(attgain + tt_pris);
                    da = formatnumber(cdr + defgain - tt_pris);
                    ds = formatnumber(defgain);
                } else {
                    aa = formatnumber(cdr + attgain - tt_pris);
                    as = formatnumber(attgain);
                    da = formatnumber(cdr + defgain);
                    ds = formatnumber(tt_pris + defgain);
                }

                textchanged += "_______________________________" + enter;
                textchanged += enter + BBCode.addunder(0) + BBCode.addbold(0)
                        + configL.getConfig("rentability") + BBCode.addbold(1)
                        + BBCode.addunder(1) + enter;
                tmp = null;
                tmp2 = null;
                if (round == 0 && !RC_end) {
                    textchanged += _att + ": "
                            + BBCode.addbcolor(0, color[fleet.length + def.length + 3]) + as
                            + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                            + enter;
                } else if (tt_pris == 0 || CR_harvested) {
                    tmp = configL.getConfig("with").split("\\s+");
                    tmp2 = tmp[0].split("\\/");
                    if ((_myrenta.equals("1") && _iamatt.equals("1")) || _myrenta.equals("0")) {
                        if (!as.equals(aa) && !CR_harvested)
                            textchanged += _att
                                    + " "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + tmp2[0]
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + "/"
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + tmp2[1]
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + " "
                                    + tmp[1]
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + aa
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + "/"
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + as
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + enter;
                        else if ((CR_harvested && _iamatt.equals("0")) || !CR_harvested)
                            textchanged += _att
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + as
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + enter;
                        else
                            textchanged += _att
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + aa
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + enter;
                    }
                    if ((_myrenta.equals("1") && _iamatt.equals("0")) || _myrenta.equals("0")) {
                        if (!ds.equals(da) && !CR_harvested)
                            textchanged += _def
                                    + " "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + tmp2[0]
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + "/"
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + tmp2[1]
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + " "
                                    + tmp[1]
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + da
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + "/"
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + ds
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + enter;
                        else if ((CR_harvested && _iamatt.equals("1")) || !CR_harvested)
                            textchanged += _def
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + ds
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + enter;
                        else
                            textchanged += _def
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + da
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + enter;
                    }
                    tmp = null;
                    tmp2 = null;
                } else if (_iamatt.equals("1")) {
                    textchanged += _att + ": "
                            + BBCode.addbcolor(0, color[fleet.length + def.length + 3]) + as
                            + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                            + enter;
                    if (_myrenta.equals("0")) {
                        if (!ds.equals(da) && cdr > tt_pris) {
                            tmp = configL.getConfig("with").split("\\s+");
                            tmp2 = tmp[0].split("\\/");
                            textchanged += _def
                                    + " "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + tmp2[0]
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + "/"
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + tmp2[1]
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + " "
                                    + tmp[1]
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + da
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + "/"
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + ds
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + enter;
                            tmp = null;
                            tmp2 = null;
                        } else
                            textchanged += _def
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + ds
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + enter;
                    }
                } else {
                    if (_myrenta.equals("0")) {
                        if (!as.equals(aa) && cdr > tt_pris) {
                            tmp = configL.getConfig("with").split("\\s+");
                            tmp2 = tmp[0].split("\\/");
                            textchanged += _att
                                    + " "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + tmp2[0]
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + "/"
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + tmp2[1]
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + " "
                                    + tmp[1]
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 3])
                                    + aa
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                                    + "/"
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + as
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + enter;
                            tmp = null;
                            tmp2 = null;
                        } else
                            textchanged += _att
                                    + ": "
                                    + BBCode.addbcolor(0, color[fleet.length + def.length + 2])
                                    + as
                                    + BBCode.addbcolor(1, color[fleet.length + def.length + 2])
                                    + enter;
                    }
                    textchanged += _def + ": "
                            + BBCode.addbcolor(0, color[fleet.length + def.length + 3]) + ds
                            + BBCode.addbcolor(1, color[fleet.length + def.length + 3])
                            + enter;
                }
            }

            textchanged += enter + BBCode.addsize(0, "10") + BBCode.addurl(0, ogsc_url)
                    + BBCode.addbcolor(0, "66FFCC") + "Created by OGSconverter"
                    + BBCode.addbcolor(1, "66FFCC") + BBCode.addurl(1, ogsc_url)
                    + BBCode.addsize(1, "10");
            textchanged += BBCode.addcenter(1);
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, exText);
            e.printStackTrace();
        }
        return textchanged;
    }

    public static String[] converterRecy(String text, int methode) {
        String textchanged = "", tmp[], line[], pris[] = null, cdr[] = null, date = "", coord = "";
        int k, i, j, clrindex;
        long tt_pris = 0, t_pris = 0, t_cdr = 0;
        long m_pris = 0, c_pris = 0, cap = 0, m_cap = 0, c_cap = 0;
        int recyNb = 0;
        int taux = 0, start;
        boolean ok = false;

        if (methode == OUT_ATT)
            exText = text;

        BBCode.setBBCode();

        Configuration configC = null;
        Configuration configL = null;

        try {
            configC = new Configuration("config.ini");

            String setting = configC.getConfig("color");
            String fichier = "lang_" + configC.getConfig("active_language") + ".ini";
            configL = new Configuration(fichier);

            if (setting.equals("01")) {
                color = DARKCOLOR;
            } else if (setting.equals("02")) {
                color = LIGHTCOLOR;
            } else {
                color = configC.getConfig("user_color" + setting).split(",");
            }

            text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                            + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])",
                    "$1" + String.valueOf((char) 10) + "$2");
            text = text.replaceAll(String.valueOf((char) 13), "");
            text = text.replaceAll("(" + configL.getConfig("REC_TOP") + ")", enter + "$1");
            text = text.replaceAll(configL.getConfig("textaera"), "");
            text = text.replaceAll("Bad copy !", "");

            if (methode == OUT_ATT)
                textchanged = BBCode.addcenter(0);
            line = text.split("(\\.\\s+|[\\n\\r]+)");

            clrindex = configL.getConfig("FLEET").split("\\|").length;
            clrindex += configL.getConfig("DEFENCES").split("\\|").length + 2;

            String time = "";

            for (j = 0; j < line.length; j++) {
                line[j] = line[j].trim();
                if (line[j].equals(""))
                    continue;

                if (Pattern.compile(MSK_USER_MSG, Pattern.CASE_INSENSITIVE)
                        .matcher(line[j])
                        .find()) {
                    date = line[j].replaceFirst(MSK_USER_MSG, "$1");
                    coord = line[j].replaceFirst(MSK_USER_MSG, "$3");

                    line[j] = line[j].replaceFirst(MSK_USER_MSG, (configC.getConfig(
                            "harvestDate").equals("1") ? (BBCode.addbold(0) + date
                            + BBCode.addbold(1) + " - ") : "")
                            + "$2 "
                            + (!coord.equals("") ? "["
                            + BBCode.addbcolor(0, color[2])
                            + BBCode.addbold(0)
                            + (configC.getConfig("harvestCoord").equals("1") ? coord
                            : configC.getConfig("harvest_rpl_coord"))
                            + BBCode.addbold(1) + BBCode.addbcolor(1, color[2]) + "]"
                            : ""));
                    time = line[j] + enter;
                    continue;
                }

                if (Pattern.compile(configL.getConfig("REC_TOP"), Pattern.CASE_INSENSITIVE)
                        .matcher(line[j])
                        .find()) {
                    if (ok)
                        textchanged += enter + "____________________________________" + enter;
                    textchanged += time;
                    time = "";
                    for (i = 0; i < 3; i++) {
                        line[i + j] = line[i + j].trim();

                        tmp = line[i + j].replaceFirst("^\\D+", "").split("\\D{3,}");

                        if (i == 0) {
                            recyNb += exptoint(tmp[0]);
                            cap += exptoint(tmp[1]);
                        }
                        if (i == 1) {
                            m_cap += exptoint(tmp[0]);
                            c_cap += exptoint(tmp[1]);
                        }
                        if (i == 2) {
                            pris = (String[]) tmp.clone();
                            m_pris += exptoint(tmp[0]);
                            c_pris += exptoint(tmp[1]);
                        }

                        for (k = 0; k < tmp.length; k++) {
                            if (!tmp[k].equals("")) {
                                start = line[i + j].lastIndexOf(']');
                                if (start < 0)
                                    start = 0;
                                if (i == 2) {
                                    line[i + j] = line[i + j].substring(0, start)
                                            + line[i + j].substring(start).replaceFirst(
                                            tmp[k].replaceAll("\\.", "\\\\.")
                                                    .replaceAll("\\+", "\\\\+"),
                                            BBCode.addbold(0)
                                                    + BBCode.addbcolor(0,
                                                    color[clrindex + 1])
                                                    + formatnumber(tmp[k])
                                                    + BBCode.addbcolor(1,
                                                    color[clrindex + 1])
                                                    + BBCode.addbold(1));
                                    continue;
                                }
                                cdr = tmp;
                                line[i + j] = line[i + j].substring(0, start)
                                        + line[i + j].substring(start).replaceFirst(
                                        tmp[k].replaceAll("\\.", "\\\\.").replaceAll(
                                                "\\+", "\\\\+"),
                                        BBCode.addbold(0)
                                                + BBCode.addbcolor(0, color[clrindex])
                                                + formatnumber(tmp[k])
                                                + BBCode.addbcolor(1, color[clrindex])
                                                + BBCode.addbold(1));
                            }
                        }
                        textchanged += line[i + j];
                        if (i != 2)
                            textchanged += "." + enter;
                    }
                    taux = 0;
                    t_pris = 0;
                    t_cdr = 0;
                    j += 2;
                    ok = true;

                    for (k = 0; k < pris.length; k++) {
                        if (!pris[k].equals(""))
                            t_pris += exptoint(pris[k]);
                    }

                    tt_pris += t_pris;

                    for (k = 0; k < cdr.length; k++) {
                        if (!cdr[k].equals(""))
                            t_cdr += exptoint(cdr[k]);
                    }

                    taux = (int) (((double) t_pris / (double) t_cdr) * 100);
                    if (configC.getConfig("cdr_taux").equals("1"))
                        textchanged += enter + enter + configL.getConfig("REC_TX")
                                + BBCode.addbcolor(0, color[clrindex]) + taux + "%"
                                + BBCode.addbcolor(1, color[clrindex]);
                } else {
                    if (ok)
                        textchanged += enter + "____________________________________" + enter;
                    // textchanged += line[j] + enter;
                    ok = false;
                }

            }
            if (methode == OUT_ATT) {
                textchanged += enter + enter + BBCode.addsize(0, "10")
                        + BBCode.addurl(0, ogsc_url) + BBCode.addbcolor(0, "66FFCC")
                        + "Created by OGSconverter" + BBCode.addbcolor(1, "66FFCC")
                        + BBCode.addurl(1, ogsc_url) + BBCode.addsize(1, "10");
                textchanged += BBCode.addcenter(1);
            } else
                textchanged += enter;

        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, exText);
            e.printStackTrace();
        }

        tmp = new String[11];
        tmp[0] = textchanged;
        tmp[1] = "" + tt_pris;
        tmp[2] = "" + recyNb;
        tmp[3] = "" + cap;
        tmp[4] = "" + m_cap;
        tmp[5] = "" + c_cap;
        tmp[6] = "" + m_pris;
        tmp[7] = "" + c_pris;
        tmp[8] = "" + taux;
        tmp[9] = date;
        tmp[10] = coord;

        return tmp;
    }

    /**
     * Function used to parse RE Report
     *
     * @param text
     * @return
     */
    public static String converterER(String text) {
        String line = "", exploded[], tmp[], fichier, repl, activity = null;
        String sl_rss[], sl_flt[], sl_def[], sl_bld[], sl_techno[], name = "";
        boolean report_ok = false;
        int i, j, k = 0, l, clrindex, id;
        StringBuffer textchanged = new StringBuffer();

        int fpts[][] = {{2000, 2000, 0}, {6000, 6000, 0}, {3000, 1000, 0},
                {6000, 4000, 0}, {20000, 7000, 2000}, {45000, 15000, 0},
                {10000, 20000, 10000}, {10000, 6000, 2000}, {0, 1000, 0},
                {50000, 25000, 15000}, {0, 2000, 500}, {60000, 50000, 15000},
                {5000000, 4000000, 1000000}, {30000, 40000, 15000}};

        BBCode.setBBCode();

        Configuration configC = null;
        Configuration configL = null;

        try {
            configC = new Configuration("config.ini");
            String setting = configC.getConfig("color");

            if (setting.equals("01")) {
                color = DARKCOLOR;
            } else if (setting.equals("02")) {
                color = LIGHTCOLOR;
            } else {
                color = configC.getConfig("user_color" + setting).split(",");
            }

            setting = setting.substring(0, 1);

            fichier = "lang_" + configC.getConfig("active_language") + ".ini";
            configL = new Configuration(fichier);

            if (Pattern.compile(configL.getConfig("textaera"), Pattern.CASE_INSENSITIVE)
                    .matcher(text)
                    .find()) {
                text = text.replaceAll(configL.getConfig("textaera"), "");
            }

            exText = text;
            text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                            + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])",
                    "$1" + String.valueOf((char) 10) + "$2");
            text = text.replaceAll(String.valueOf((char) 13), "");
            text = text.replaceAll(configL.getConfig("textaera"), "");
            text = text.replaceAll("Bad copy !", "");

            exploded = text.split(String.valueOf((char) 10));

            clrindex = configL.getConfig("FLEET").split("\\|").length;
            clrindex += configL.getConfig("DEFENCES").split("\\|").length + 2;

            sl_rss = configC.getConfig("ER_GOODS" + setting).split("\\|");
            sl_flt = configC.getConfig("ER_FLEET" + setting).split("\\|");
            sl_def = configC.getConfig("ER_DEFENCES" + setting).split("\\|");
            sl_bld = configC.getConfig("ER_BUILDINGS" + setting).split("\\|");
            sl_techno = configC.getConfig("ER_TECHNO" + setting).split("\\|");

            String good_name[] = configL.getConfig("ER_GOODS").split("\\|");
            String flt_name[] = configL.getConfig("ER_FLEET").split("\\|");
            String def_name[] = configL.getConfig("ER_DEFENCES").split("\\|");
            String bld_name[] = configL.getConfig("ER_BUILDINGS").split("\\|");
            String techno_name[] = configL.getConfig("ER_TECHNO").split("\\|");

            double metalBldID = 0, metalLvl = 0;
            double crystalBldID = 1, crystalLvl = 0;
            double deutBldID = 2, deutLvl = 0;
            double phalangeBldID = 15, phalangeLvl = 0;
            double RITechnoID = 8, RILvl = 0;
            int MIPDefID = 9, MIPNb = 0;
            int temperature = 0;
            int galaxy = 0;
            int system = 0;

            long cdr[] = new long[2];

            boolean table = configC.getConfig("ER_table").equals("1");
            i = Integer.parseInt(configC.getConfig("universe_selected"));
            int systemMax = Integer.parseInt(configC.getConfig("system").split("\\|")[i]);
            int speedUnivers = Integer.parseInt(configC.getConfig("speed").split("\\|")[i]);

            for (i = 0; i < exploded.length; i++) {
                line = exploded[i].trim();
                if (line.equals(""))
                    continue;

                if (report_ok && !Pattern.compile(MSK_IS_VALUE).matcher(line).find()) {
                    // Pattern pour récupérer l'heure dans les RE
                    if (Pattern.compile("\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2}:\\d{2}")
                            .matcher(line)
                            .find()) {
                        activity = line;
                        // A quoi ça sert ?
                    } else if (Pattern.compile("\\s\\d+\\s*[^%]").matcher(line).find()) {
                        activity = line;
                    }
                }

                if (report_ok == false
                        && Pattern.compile("^" + configL.getConfig("ER_TOP"),
                        Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                    report_ok = true;
                    if (Pattern.compile(MSK_NAME_COORD_DATE_NEW, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find()) {
                        while ((i + 1) < exploded.length) {
                            if (!line.equals(exploded[++i].trim()))
                                break;
                        }
                        line += " " + exploded[i];
                    }

                    repl = line.replaceAll(configL.getConfig("ER_TOP") + MSK_NAME_COORD_DATE,
                            "$2");
                    tmp = repl.split("\\:");
                    if (tmp.length > 0)
                        galaxy = (int) exptoint(tmp[0]);
                    if (tmp.length > 1)
                        system = (int) exptoint(tmp[1]);
                    if (tmp.length > 2)
                        switch ((int) exptoint(tmp[2])) {
                            case 1:
                            case 2:
                            case 3:
                                temperature = 123;
                                break;
                            case 4:
                            case 5:
                            case 6:
                                temperature = 65;
                                break;
                            case 7:
                            case 8:
                            case 9:
                                temperature = 35;
                                break;
                            case 10:
                            case 11:
                            case 12:
                                temperature = 15;
                                break;
                            case 13:
                            case 14:
                            case 15:
                                temperature = -40;
                                break;
                            default:
                                temperature = 35;
                        }
                    if (Pattern.compile(MSK_NAME_COORD_DATE_NEW, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find()) {
                        if (configC.getConfig("ER_planet").equals("1"))
                            repl = configL.getConfig("ER_TOP") + BBCode.addbold(0) + "$1"
                                    + BBCode.addbold(1) + "\\[" + BBCode.addbold(0)
                                    + BBCode.addbcolor(0, color[0]) + "$2"
                                    + BBCode.addbcolor(1, color[0]) + BBCode.addbold(1)
                                    + "\\]" + " " + "$3" + " $4" + BBCode.addbold(0) + "$5"
                                    + BBCode.addbold(1) + "$6:";
                        else
                            repl = configL.getConfig("ER_TOP") + BBCode.addbold(0)
                                    + configC.getConfig("ER_planetNameRepl")
                                    + BBCode.addbold(1) + "\\[" + BBCode.addbold(0)
                                    + BBCode.addbcolor(0, color[0]) + "$2"
                                    + BBCode.addbcolor(1, color[0]) + BBCode.addbold(1)
                                    + "\\]" + " " + "$3" + " $4" + BBCode.addbold(0) + "$5"
                                    + BBCode.addbold(1) + "$6:";
                        if (!configC.getConfig("ER_coordonnate").equals("1"))
                            repl = repl.replace("$2", configC.getConfig("ER_coordinateRepl"));
                        if (!configC.getConfig("ER_player_name").equals("1"))
                            repl = repl.replace(" $3", "");
                        textchanged.append(
                                line.replaceAll(configL.getConfig("ER_TOP")
                                        + MSK_NAME_COORD_DATE_NEW, repl)).append(enter);
                    } else {
                        if (configC.getConfig("ER_planet").equals("1"))
                            repl = configL.getConfig("ER_TOP") + BBCode.addbold(0) + "$1"
                                    + BBCode.addbold(1) + "\\[" + BBCode.addbold(0)
                                    + BBCode.addbcolor(0, color[0]) + "$2"
                                    + BBCode.addbcolor(1, color[0]) + BBCode.addbold(1)
                                    + "\\]" + "$3" + BBCode.addbold(0) + "$4"
                                    + BBCode.addbold(1) + "$5:";
                        else
                            repl = configL.getConfig("ER_TOP") + BBCode.addbold(0)
                                    + configC.getConfig("ER_planetNameRepl")
                                    + BBCode.addbold(1) + "\\[" + BBCode.addbold(0)
                                    + BBCode.addbcolor(0, color[0]) + "$2"
                                    + BBCode.addbcolor(1, color[0]) + BBCode.addbold(1)
                                    + "\\]" + "$3" + BBCode.addbold(0) + "$4"
                                    + BBCode.addbold(1) + "$5:";
                        if (!configC.getConfig("ER_coordonnate").equals("1"))
                            repl = repl.replaceAll("\\$2",
                                    configC.getConfig("ER_coordinateRepl"));
                        textchanged.append(
                                line.replaceAll(configL.getConfig("ER_TOP")
                                        + MSK_NAME_COORD_DATE, repl)).append(enter);
                    }

                    if (configC.getConfig("ER_stock").equals("1")) {
                        textchanged.append(enter)
                                .append(BBCode.addbcolor(0, color[0]))
                                .append(BBCode.addunder(0))
                                .append(configL.getConfig("ER_RESS"))
                                .append(":")
                                .append(BBCode.addunder(1))
                                .append(BBCode.addbcolor(1, color[0]))
                                .append(enter);
                        if (table)
                            textchanged.append(BBCode.addfont(0, "Courier,Courier New"));
                        l = 0;
                        while ((i + 1) < exploded.length) {
                            line = exploded[i + 1].trim();
                            if (!line.equals("")
                                    && !Pattern.compile(MSK_IS_VALUE).matcher(line).find())
                                break;
                            i++;
                            tmp = line.split("\\s+");
                            for (j = 0; j < tmp.length; j++) {
                                if (Pattern.compile(MSK_NB_FORMATED).matcher(tmp[j]).find()) {
                                    if (table) {
                                        name = Main.getTextForTableFont(name, 30, 0);
                                        textchanged.append(Main.getTableFont(name, 30, 0,
                                                name.length(), true));
                                    } else
                                        textchanged.append(name);
                                    if (exptoint(tmp[j]) < exptoint(sl_rss[l])) {
                                        if (table) {
                                            textchanged.append(Main.getTableFont(
                                                    BBCode.addbcolor(0, color[clrindex])
                                                            + formatnumber(tmp[j])
                                                            + BBCode.addbcolor(1,
                                                            color[clrindex]), 10, 1,
                                                    formatnumber(tmp[j]).length(), true));
                                        } else
                                            textchanged.append(" ")
                                                    .append(
                                                            BBCode.addbcolor(0,
                                                                    color[clrindex]))
                                                    .append(formatnumber(tmp[j]))
                                                    .append(
                                                            BBCode.addbcolor(1,
                                                                    color[clrindex]))
                                                    .append(enter);
                                    } else {
                                        if (table) {
                                            textchanged.append(Main.getTableFont(
                                                    BBCode.addbcolor(0, color[clrindex + 1])
                                                            + formatnumber(tmp[j])
                                                            + BBCode.addbcolor(1,
                                                            color[clrindex + 1]), 10,
                                                    1, formatnumber(tmp[j]).length(), true));
                                        } else
                                            textchanged.append(" ")
                                                    .append(
                                                            BBCode.addsize(
                                                                    0,
                                                                    configC.getConfig("user_sz_rss"
                                                                            + setting)))
                                                    .append(
                                                            BBCode.addbcolor(0,
                                                                    color[clrindex + 1]))
                                                    .append(formatnumber(tmp[j]))
                                                    .append(
                                                            BBCode.addbcolor(1,
                                                                    color[clrindex + 1]))
                                                    .append(BBCode.addsize(1, null))
                                                    .append(enter);
                                    }
                                    k = 0;
                                    l++;
                                    if (table && l % 2 == 0)
                                        textchanged.append(enter);
                                } else if (k == 1)
                                    name += " " + tmp[j];
                                else {
                                    name = tmp[j];
                                    k = 1;
                                }
                            }
                        }
                        if (table) {
                            textchanged.append(BBCode.addfont(1, "Courier,Courier New"));
                            if (l % 2 != 0)
                                textchanged.append(enter);
                        }
                    }
                    continue;
                } else if (report_ok == true
                        && configC.getConfig("ER_fleet").equals("1")
                        && Pattern.compile("^" + configL.getConfig("ER_FLT"),
                        Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                    textchanged.append(enter).append(BBCode.addbcolor(0, color[0])).append(
                            BBCode.addunder(0)).append(line).append(":").append(
                            BBCode.addunder(1)).append(BBCode.addbcolor(1, color[0])).append(
                            enter);
                    if (table)
                        textchanged.append(BBCode.addfont(0, "Courier,Courier New"));
                    id = 0;
                    while ((i + 1) < exploded.length) {
                        line = exploded[i + 1].trim();
                        if (!line.equals("")
                                && !Pattern.compile(MSK_IS_VALUE).matcher(line).find())
                            break;
                        i++;
                        tmp = line.split("\\s+");
                        for (j = 0; j < tmp.length; j++) {
                            if (Pattern.compile(MSK_NB_FORMATED).matcher(tmp[j]).find()) {
                                for (l = 0; l < flt_name.length; l++) {
                                    if (name.equalsIgnoreCase(flt_name[l]))
                                        break;
                                }
                                if (l >= sl_flt.length)
                                    l = sl_flt.length - 1;

                                cdr[0] += fpts[l][0] * exptoint(tmp[j]);
                                cdr[1] += fpts[l][1] * exptoint(tmp[j]);

                                if (table) {
                                    name = Main.getTextForTableFont(name, 30, 0);
                                    textchanged.append(Main.getTableFont(name, 30, 0,
                                            name.length(), true));
                                } else
                                    textchanged.append(name);

                                if (exptoint(tmp[j]) < exptoint(sl_flt[l])) {
                                    if (table)
                                        textchanged.append(Main.getTableFont(BBCode.addbcolor(
                                                0, color[clrindex])
                                                        + formatnumber(tmp[j])
                                                        + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                                formatnumber(tmp[j]).length(), true));
                                    else
                                        textchanged.append(" ").append(
                                                BBCode.addbcolor(0, color[clrindex])).append(
                                                formatnumber(tmp[j])).append(
                                                BBCode.addbcolor(1, color[clrindex])).append(
                                                enter);
                                } else {
                                    if (table)
                                        textchanged.append(Main.getTableFont(BBCode.addbcolor(
                                                0, color[clrindex + 1])
                                                        + formatnumber(tmp[j])
                                                        + BBCode.addbcolor(1, color[clrindex + 1]),
                                                10, 1, formatnumber(tmp[j]).length(), true));
                                    else
                                        textchanged.append(" ")
                                                .append(
                                                        BBCode.addsize(
                                                                0,
                                                                configC.getConfig("user_sz_fleet"
                                                                        + setting)))
                                                .append(
                                                        BBCode.addbcolor(0,
                                                                color[clrindex + 1]))
                                                .append(formatnumber(tmp[j]))
                                                .append(
                                                        BBCode.addbcolor(1,
                                                                color[clrindex + 1]))
                                                .append(BBCode.addsize(1, null))
                                                .append(enter);
                                }
                                k = 0;
                                id++;
                                if (table && id % 2 == 0)
                                    textchanged.append(enter);
                            } else if (k == 1) {
                                name += " " + tmp[j];
                            } else {
                                name = tmp[j];
                                k = 1;
                            }
                        }
                    }
                    if (table) {
                        textchanged.append(BBCode.addfont(1, "Courier,Courier New"));
                        if (id % 2 != 0)
                            textchanged.append(enter);
                    }
                    continue;
                } else if (report_ok == true
                        && configC.getConfig("ER_def").equals("1")
                        && Pattern.compile("^" + configL.getConfig("ER_DEF"),
                        Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + line + ":")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]))
                            .append(enter);
                    if (table)
                        textchanged.append(BBCode.addfont(0, "Courier,Courier New"));
                    id = 0;
                    while ((i + 1) < exploded.length) {
                        line = exploded[i + 1].trim();
                        if (!line.equals("")
                                && !Pattern.compile(MSK_IS_VALUE).matcher(line).find())
                            break;
                        i++;
                        tmp = line.split("\\s+");
                        for (j = 0; j < tmp.length; j++) {
                            if (Pattern.compile(MSK_NB_FORMATED).matcher(tmp[j]).find()) {
                                for (l = 0; l < def_name.length; l++) {
                                    if (name.equalsIgnoreCase(def_name[l]))
                                        break;
                                }
                                if (l >= sl_def.length)
                                    l = sl_def.length - 1;
                                if (l == MIPDefID) {
                                    MIPNb = (int) exptoint(tmp[j]);
                                }
                                if (table) {
                                    name = Main.getTextForTableFont(name, 30, 0);
                                    textchanged.append(Main.getTableFont(name, 30, 0,
                                            name.length(), true));
                                } else
                                    textchanged.append(name);
                                if (exptoint(tmp[j]) < exptoint(sl_def[l])) {
                                    if (table)
                                        textchanged.append(Main.getTableFont(BBCode.addbcolor(
                                                0, color[clrindex])
                                                        + formatnumber(tmp[j])
                                                        + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                                formatnumber(tmp[j]).length(), true));
                                    else
                                        textchanged.append(" ").append(
                                                BBCode.addbcolor(0, color[clrindex])).append(
                                                formatnumber(tmp[j])).append(
                                                BBCode.addbcolor(1, color[clrindex])).append(
                                                enter);
                                } else {
                                    if (table)
                                        textchanged.append(Main.getTableFont(BBCode.addbcolor(
                                                0, color[clrindex + 1])
                                                        + formatnumber(tmp[j])
                                                        + BBCode.addbcolor(1, color[clrindex + 1]),
                                                10, 1, formatnumber(tmp[j]).length(), true));
                                    else
                                        textchanged.append(" ")
                                                .append(
                                                        BBCode.addsize(
                                                                0,
                                                                configC.getConfig("user_sz_def"
                                                                        + setting)))
                                                .append(
                                                        BBCode.addbcolor(0,
                                                                color[clrindex + 1]))
                                                .append(formatnumber(tmp[j]))
                                                .append(
                                                        BBCode.addbcolor(1,
                                                                color[clrindex + 1]))
                                                .append(BBCode.addsize(1, null))
                                                .append(enter);
                                }
                                k = 0;
                                id++;
                                if (table && id % 2 == 0)
                                    textchanged.append(enter);
                            } else if (k == 1) {
                                name += " " + tmp[j];
                            } else {
                                k = 1;
                                name = tmp[j];
                            }
                        }
                    }
                    if (table) {
                        textchanged.append(BBCode.addfont(1, "Courier,Courier New"));
                        if (id % 2 != 0)
                            textchanged.append(enter);
                    }
                    continue;
                } else if (report_ok == true
                        && configC.getConfig("ER_building").equals("1")
                        && Pattern.compile("^" + configL.getConfig("ER_BLD"),
                        Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + line + ":")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]))
                            .append(enter);
                    if (table)
                        textchanged.append(BBCode.addfont(0, "Courier,Courier New"));
                    id = 0;
                    while ((i + 1) < exploded.length) {
                        line = exploded[i + 1].trim();
                        if (!line.equals("")
                                && !Pattern.compile(MSK_IS_VALUE).matcher(line).find())
                            break;
                        i++;
                        tmp = line.split("\\s+");
                        for (j = 0; j < tmp.length; j++) {
                            if (Pattern.compile(MSK_NB_FORMATED).matcher(tmp[j]).find()) {
                                for (l = 0; l < bld_name.length; l++) {
                                    if (name.equalsIgnoreCase(bld_name[l]))
                                        break;
                                }
                                if (l >= sl_bld.length)
                                    l = sl_bld.length - 1;
                                if (l == metalBldID) {
                                    metalLvl = (int) exptoint(tmp[j]);
                                } else if (l == crystalBldID) {
                                    crystalLvl = (int) exptoint(tmp[j]);
                                } else if (l == deutBldID) {
                                    deutLvl = (int) exptoint(tmp[j]);
                                } else if (l == phalangeBldID) {
                                    phalangeLvl = (int) exptoint(tmp[j]);
                                }
                                if (table) {
                                    name = Main.getTextForTableFont(name, 30, 0);
                                    textchanged.append(Main.getTableFont(name, 30, 0,
                                            name.length(), true));
                                } else
                                    textchanged.append(name);
                                if (exptoint(tmp[j]) < exptoint(sl_bld[l])) {
                                    if (table)
                                        textchanged.append(Main.getTableFont(BBCode.addbcolor(
                                                0, color[clrindex])
                                                        + formatnumber(tmp[j])
                                                        + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                                formatnumber(tmp[j]).length(), true));
                                    else
                                        textchanged.append(" ").append(
                                                BBCode.addbcolor(0, color[clrindex])).append(
                                                formatnumber(tmp[j])).append(
                                                BBCode.addbcolor(1, color[clrindex])).append(
                                                enter);
                                } else {
                                    if (table)
                                        textchanged.append(Main.getTableFont(BBCode.addbcolor(
                                                0, color[clrindex + 1])
                                                        + formatnumber(tmp[j])
                                                        + BBCode.addbcolor(1, color[clrindex + 1]),
                                                10, 1, formatnumber(tmp[j]).length(), true));
                                    else
                                        textchanged.append(" ")
                                                .append(
                                                        BBCode.addsize(
                                                                0,
                                                                configC.getConfig("user_sz_bld"
                                                                        + setting)))
                                                .append(
                                                        BBCode.addbcolor(0,
                                                                color[clrindex + 1]))
                                                .append(formatnumber(tmp[j]))
                                                .append(
                                                        BBCode.addbcolor(1,
                                                                color[clrindex + 1]))
                                                .append(BBCode.addsize(1, null))
                                                .append(enter);
                                }
                                k = 0;
                                id++;
                                if (table && id % 2 == 0)
                                    textchanged.append(enter);
                            } else if (k == 1) {
                                name += " " + tmp[j];
                            } else {
                                k = 1;
                                name = tmp[j];
                            }
                        }
                    }
                    if (table) {
                        textchanged.append(BBCode.addfont(1, "Courier,Courier New"));
                        if (id % 2 != 0)
                            textchanged.append(enter);
                    }
                    continue;
                } else if (report_ok == true
                        && configC.getConfig("ER_techno").equals("1")
                        && Pattern.compile("^" + configL.getConfig("ER_THN"),
                        Pattern.CASE_INSENSITIVE).matcher(line).find()) {
                    textchanged.append(enter).append(BBCode.addbcolor(0, color[0])).append(
                            BBCode.addunder(0)).append(line).append(":").append(
                            BBCode.addunder(1)).append(BBCode.addbcolor(1, color[0])).append(
                            enter);
                    if (table)
                        textchanged.append(BBCode.addfont(0, "Courier,Courier New"));
                    id = 0;
                    while ((i + 1) < exploded.length) {
                        line = exploded[i + 1].trim();
                        if (!line.equals("")
                                && !Pattern.compile(MSK_IS_VALUE).matcher(line).find())
                            break;
                        i++;
                        tmp = line.split("\\s+");
                        for (j = 0; j < tmp.length; j++) {
                            if (Pattern.compile(MSK_NB_FORMATED).matcher(tmp[j]).find()) {
                                for (l = 0; l < techno_name.length; l++) {
                                    if (name.equalsIgnoreCase(techno_name[l]))
                                        break;
                                }
                                if (l >= sl_techno.length)
                                    l = sl_techno.length - 1;
                                if (l == RITechnoID) {
                                    RILvl = exptoint(tmp[j]);
                                }
                                if (table) {
                                    name = Main.getTextForTableFont(name, 30, 0);
                                    textchanged.append(Main.getTableFont(name, 30, 0,
                                            name.length(), true));
                                } else
                                    textchanged.append(name);
                                if (exptoint(tmp[j]) < exptoint(sl_techno[l])) {
                                    if (table)
                                        textchanged.append(Main.getTableFont(BBCode.addbcolor(
                                                0, color[clrindex])
                                                        + formatnumber(tmp[j])
                                                        + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                                formatnumber(tmp[j]).length(), true));
                                    else
                                        textchanged.append(" ").append(
                                                BBCode.addbcolor(0, color[clrindex])).append(
                                                formatnumber(tmp[j])).append(
                                                BBCode.addbcolor(1, color[clrindex])).append(
                                                enter);
                                } else {
                                    if (table)
                                        textchanged.append(Main.getTableFont(BBCode.addbcolor(
                                                0, color[clrindex + 1])
                                                        + formatnumber(tmp[j])
                                                        + BBCode.addbcolor(1, color[clrindex + 1]),
                                                10, 1, formatnumber(tmp[j]).length(), true));
                                    else
                                        textchanged.append(" ")
                                                .append(
                                                        BBCode.addsize(
                                                                0,
                                                                configC.getConfig("user_sz_techno"
                                                                        + setting)))
                                                .append(
                                                        BBCode.addbcolor(0,
                                                                color[clrindex + 1]))
                                                .append(formatnumber(tmp[j]))
                                                .append(
                                                        BBCode.addbcolor(1,
                                                                color[clrindex + 1]))
                                                .append(BBCode.addsize(1, null))
                                                .append(enter);
                                }
                                k = 0;
                                id++;
                                if (table && id % 2 == 0)
                                    textchanged.append(enter);
                            } else if (k == 1) {
                                name += " " + tmp[j];
                            } else {
                                k = 1;
                                name = tmp[j];
                            }
                        }
                    }
                    if (table) {
                        textchanged.append(BBCode.addfont(1, "Courier,Courier New"));
                        if (id % 2 != 0)
                            textchanged.append(enter);
                    }
                    continue;
                }
            }

            if (configC.getConfig("ER_act").equals("1") && activity != null) {
                textchanged.append(enter);
                textchanged.append(activity.replaceFirst("(\\s\\d+\\s)", BBCode.addbcolor(0,
                        color[clrindex + 1])
                        + "$1" + BBCode.addbcolor(1, color[clrindex + 1])));
                textchanged.append(enter);
            }

            if (configC.getConfig("ER_prod").equals("1")
                    && (metalLvl != 0 || crystalLvl != 0 || deutLvl != 0)) {
                metalLvl = (30 * metalLvl * Math.pow(1.1, metalLvl) * speedUnivers) + 30;
                crystalLvl = (20 * crystalLvl * Math.pow(1.1, crystalLvl) * speedUnivers) + 20;
                deutLvl = 10 * deutLvl
                        * Math.pow(1.1, deutLvl * ((-0.002 * temperature) + 1.28))
                        * speedUnivers;

                // heure
                if (table) {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + configL.getConfig("ER_PROD"))
                            .append("/")
                            .append(configL.getConfig("HOUR"))
                            .append(" :")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]))
                            .append(enter)
                            .append(BBCode.addfont(0, "Courier,Courier New"));
                    line = formatnumber(Math.round(metalLvl));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[0], 12, 0),
                                    12, 0, good_name[0].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    line = formatnumber(Math.round(crystalLvl));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[1], 12, 0),
                                    12, 0, good_name[1].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    line = formatnumber(Math.round(deutLvl));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[2], 12, 0),
                                    12, 0, good_name[2].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    textchanged.append(BBCode.addfont(1, "Courier,Courier New"));
                } else {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + configL.getConfig("ER_PROD"))
                            .append("/")
                            .append(configL.getConfig("HOUR"))
                            .append(" :")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]))
                            .append(" ");
                    textchanged.append(good_name[0]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(metalLvl))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                    textchanged.append(", ").append(good_name[1]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(crystalLvl))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                    textchanged.append(", ").append(good_name[2]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(deutLvl))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                }
                // jour
                if (table) {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + configL.getConfig("ER_PROD"))
                            .append("/")
                            .append(configL.getConfig("DAY"))
                            .append(" :")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]) + enter)
                            .append(BBCode.addfont(0, "Courier,Courier New"));
                    line = formatnumber(Math.round(metalLvl * 24));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[0], 12, 0),
                                    12, 0, good_name[0].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    line = formatnumber(Math.round(crystalLvl * 24));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[1], 12, 0),
                                    12, 0, good_name[1].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    line = formatnumber(Math.round(deutLvl * 24));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[2], 12, 0),
                                    12, 0, good_name[2].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    textchanged.append(BBCode.addfont(1, "Courier,Courier New"));
                } else {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + configL.getConfig("ER_PROD"))
                            .append("/")
                            .append(configL.getConfig("DAY"))
                            .append(" :")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]))
                            .append(" ");
                    textchanged.append(good_name[0]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(metalLvl * 24))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                    textchanged.append(", ").append(good_name[1]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(crystalLvl * 24))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                    textchanged.append(", ").append(good_name[2]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(deutLvl * 24))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                }
                // semaine
                if (table) {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + configL.getConfig("ER_PROD"))
                            .append("/")
                            .append(configL.getConfig("WEEK"))
                            .append(" :")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]))
                            .append(enter)
                            .append(BBCode.addfont(0, "Courier,Courier New"));
                    line = formatnumber(Math.round(metalLvl * 24 * 7));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[0], 12, 0),
                                    12, 0, good_name[0].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    line = formatnumber(Math.round(crystalLvl * 24 * 7));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[1], 12, 0),
                                    12, 0, good_name[1].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    line = formatnumber(Math.round(deutLvl * 24 * 7));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[2], 12, 0),
                                    12, 0, good_name[2].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    textchanged.append(BBCode.addfont(1, "Courier,Courier New")).append(enter);
                } else {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + configL.getConfig("ER_PROD"))
                            .append("/")
                            .append(configL.getConfig("WEEK"))
                            .append(" :")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]))
                            .append(" ");
                    textchanged.append(good_name[0]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(metalLvl * 24 * 7))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                    textchanged.append(", ").append(good_name[1]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(crystalLvl * 24 * 7))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                    textchanged.append(", ").append(good_name[2]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(deutLvl * 24 * 7))).append(
                            BBCode.addbcolor(1, color[clrindex])).append(enter);
                }
            }

            if (configC.getConfig("ER_cdr").equals("1") && (cdr[0] != 0 || cdr[1] != 0)) {
                if (table) {
                    textchanged.append(enter)
                            .append(BBCode.addbcolor(0, color[0]))
                            .append(BBCode.addunder(0) + configL.getConfig("ER_CDR"))
                            .append(" :")
                            .append(BBCode.addunder(1))
                            .append(BBCode.addbcolor(1, color[0]))
                            .append(enter)
                            .append(BBCode.addfont(0, "Courier,Courier New"));
                    line = formatnumber(Math.round(cdr[0] * 0.3));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[0], 12, 0),
                                    12, 0, good_name[0].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    line = formatnumber(Math.round(cdr[1] * 0.3));
                    textchanged.append(
                            Main.getTableFont(Main.getTextForTableFont(good_name[1], 12, 0),
                                    12, 0, good_name[1].length(), true)).append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex]) + line
                                            + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                                    line.length(), true));
                    textchanged.append(BBCode.addfont(1, "Courier,Courier New")).append(enter);
                } else {
                    textchanged.append(enter).append(BBCode.addbcolor(0, color[0])).append(
                            BBCode.addunder(0)).append(configL.getConfig("ER_CDR")).append(
                            " :").append(BBCode.addunder(1)).append(
                            BBCode.addbcolor(1, color[0])).append(" ");
                    textchanged.append(good_name[0]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(cdr[0] * 0.3))).append(
                            BBCode.addbcolor(1, color[clrindex]));
                    textchanged.append(", ").append(good_name[1]).append(" ").append(
                            BBCode.addbcolor(0, color[clrindex])).append(
                            formatnumber(Math.round(cdr[1] * 0.3))).append(
                            BBCode.addbcolor(1, color[clrindex])).append(enter);
                }
            }

            if (configC.getConfig("ER_mip").equals("1")
                    && (galaxy != 0 && system != 0 && RILvl != 0 && MIPNb != 0)) {
                textchanged.append(enter).append(BBCode.addbcolor(0, color[0])).append(
                        BBCode.addunder(0) + configL.getConfig("ER_MIP")).append("(").append(
                        formatnumber(MIPNb)).append(")").append(" :").append(
                        BBCode.addunder(1)).append(BBCode.addbcolor(1, color[0])).append(" ");
                textchanged.append(BBCode.addbcolor(0, color[clrindex]))
                        .append(galaxy)
                        .append(":")
                        .append(
                                Integer.toString((int) ((system - (5 * RILvl - 1)) < 1 ? 1
                                        : (system - (5 * RILvl - 1)))))
                        .append(" -> ")
                        .append(galaxy)
                        .append(":")
                        .append(
                                Integer.toString((int) ((system + (5 * RILvl - 1)) > systemMax ? systemMax
                                        : (system + (5 * RILvl - 1)))))
                        .append(BBCode.addbcolor(1, color[clrindex]))
                        .append(enter);
            }

            if (configC.getConfig("ER_phlg").equals("1")
                    && (galaxy != 0 && system != 0 && phalangeLvl != 0)) {
                textchanged.append(enter)
                        .append(BBCode.addbcolor(0, color[0]))
                        .append(BBCode.addunder(0) + configL.getConfig("ER_PHLG"))
                        .append(" :")
                        .append(BBCode.addunder(1))
                        .append(BBCode.addbcolor(1, color[0]))
                        .append(" ");
                textchanged.append(BBCode.addbcolor(0, color[clrindex]))
                        .append(galaxy)
                        .append(":")
                        .append(
                                Integer.toString((int) ((system - (Math.pow(phalangeLvl, 2) - 1)) < 1 ? 1
                                        : (system - (Math.pow(phalangeLvl, 2) - 1)))))
                        .append(" -> ")
                        .append(galaxy)
                        .append(":")
                        .append(
                                Integer.toString((int) ((system + (Math.pow(phalangeLvl, 2) - 1)) > systemMax ? systemMax
                                        : (system + (Math.pow(phalangeLvl, 2) - 1)))))
                        .append(BBCode.addbcolor(1, color[clrindex]))
                        .append(enter);
            }

            textchanged.append(enter).append(BBCode.addsize(0, "10")).append(
                    BBCode.addurl(0, ogsc_url)).append(BBCode.addbcolor(0, "66FFCC")).append(
                    "Created by OGSconverter").append(BBCode.addbcolor(1, "66FFCC")).append(
                    BBCode.addurl(1, ogsc_url)).append(BBCode.addsize(1, "10"));
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, exText);
            e.printStackTrace();
        }

        return textchanged.toString();
    }

    public static String converterALLY(String text) {
        String textchanged = "", exploded[], line, tmp[], ps;
        String tmp2[] = new String[0];
        int i, j, p, offset;
        boolean ok = false, leg = false;
        Map points = new Hashtable();
        List results = new ArrayList();
        List progs = new ArrayList();
        List lpoints = new ArrayList();
        double mprog = 0, mpoint = 0;
        int mprogid = 0, mpointid = 0, progmax = Integer.MIN_VALUE, progmin = Integer.MAX_VALUE, pointmax = Integer.MIN_VALUE, pointmin = Integer.MAX_VALUE;
        Matcher matcher;

        exText = text;
        text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])", "$1"
                + String.valueOf((char) 10) + "$2");
        text = text.replaceAll(String.valueOf((char) 13), "");

        BBCode.setBBCode();

        Configuration configC = null;
        Configuration configL = null;

        try {
            configC = new Configuration("config.ini");
            String setting = configC.getConfig("color");
            String lang = configC.getConfig("active_language");
            configL = new Configuration("lang_" + lang + ".ini");

            text = text.replaceAll(configL.getConfig("textaera"), "");
            text = text.replaceAll("Bad copy !", "");

            if (setting.equals("01")) {
                color = DARKCOLOR;
            } else if (setting.equals("02")) {
                color = LIGHTCOLOR;
            } else {
                color = configC.getConfig("user_color" + setting).split(",");
            }

            setting = setting.substring(0, 1);

            text = text.replaceAll(configL.getConfig("MSG"), " ");
            exploded = text.trim().split(String.valueOf((char) 10));

            boolean _name = configC.getConfig("ml_name").equals("1");
            boolean _statut = configC.getConfig("ml_statut").equals("1");
            boolean _points = configC.getConfig("ml_points").equals("1");
            boolean _coord = configC.getConfig("ml_coord").equals("1");
            boolean _adhes = configC.getConfig("ml_adhesion").equals("1");
            boolean table = configC.getConfig("ml_table").equals("1");
            i = p = 0;

            matcher = Pattern.compile("(?s)" + configL.getConfig("ALLY_TOP")).matcher(text);
            while (matcher.find(i)) {
                p++;
                i = matcher.end();
            }

            i = 0;

            if (p > 1) {

                for (i = 0; i < exploded.length; i++) {
                    line = exploded[i].trim();

                    if (Pattern.compile(configL.getConfig("ALLY_TOP")).matcher(line).find()) {
                        if (ok)
                            break;
                        ok = true;
                        continue;
                    }

                    if (ok) {
                        if (Pattern.compile(MK_MEMBERS_DATE).matcher(line).find())
                            offset = 2;
                        else
                            offset = 1;

                        tmp = line.replaceFirst("^_", "").split("[\\s_]+");

                        for (j = tmp.length - 1; j >= 0
                                && !Pattern.compile("^" + MSK_NB_FORMATED + "$").matcher(
                                tmp[j]).find(); j--)
                            ;

                        offset--;
                        if (offset > 0)
                            for (j--; j >= 0
                                    && !Pattern.compile("^" + MSK_NB_FORMATED + "$").matcher(
                                    tmp[j]).find(); j--)
                                ;

                        if (j > 0) {
                            p = j;
                            ps = "";
                            for (; j >= 0
                                    && !(j == 0 && Pattern.compile("^\\d+\\.?$").matcher(
                                    tmp[j]).find()); j--)
                                ps = tmp[j] + " " + ps;
                            points.put(ps.trim(), tmp[p]);
                        }
                    }
                }

            }

            if (ok) {
                ps = text.replaceFirst(
                        "(?s)^.*?([\\w\\.]+, \\d{1,2} \\w+ \\d{4} \\d{1,2}:\\d{1,2}).*?$",
                        " ($1.)");
                if (ps.equals(text))
                    ps = "";
                tmp2 = (String[]) points.keySet().toArray(new String[points.size()]);
            } else
                ps = "";

            ok = false;

            SimpleDateFormat sfd = new SimpleDateFormat("EEE, d MMM yyyy HH:mm",
                    Locale.getDefault());
            textchanged += sfd.format(new GregorianCalendar().getTime()) + ps + enter;

            for (; i < exploded.length; i++) {
                line = exploded[i].trim();

                if (Pattern.compile(configL.getConfig("ALLY_TOP")).matcher(line).find()) {
                    ok = true;
                    textchanged += line + enter;
                    textchanged += (table ? BBCode.addfont(0, "Courier,Courier New") : "");
                    continue;
                }

                if (ok) {
                    if (Pattern.compile(MSK_DATA_MEMBER, Pattern.CASE_INSENSITIVE).matcher(
                            line).find()) {
                        tmp = line.replaceFirst(MSK_DATA_MEMBER,
                                "$1<|>$2<|>$3<|>$4<|>$5<|>$6<|>.").split("<\\|>");

                        for (p = 0; p < tmp2.length
                                && !tmp2[p].trim().startsWith(
                                tmp[1].replaceAll("_+", " ").trim()); p++)
                            ;

                        if (p < tmp2.length) {
                            p = points.get(tmp2[p]) != null ? (int) exptoint(tmp[3])
                                    - (int) exptoint(points.get(tmp2[p]).toString()) : 0;
                            tmp[6] = (p > 0 ? "(+" : "(") + formatnumber(p) + ")";
                            progs.add(new Integer(p));
                            mprogid++;
                            mprog = mprogid > 1 ? ((mprog * (double) (mprogid - 1)) + (double) p)
                                    / mprogid
                                    : p;
                            progmax = progmax < p ? p : progmax;
                            progmin = progmin > p ? p : progmin;
                        } else {
                            tmp[6] = "";
                            progs.add(null);
                        }

                        p = (int) exptoint(tmp[3]);
                        lpoints.add(new Integer(p));
                        mpointid++;
                        mpoint = mpointid > 1 ? ((mpoint * (double) (mpointid - 1)) + (double) p)
                                / mpointid
                                : p;
                        pointmax = pointmax < p ? p : pointmax;
                        pointmin = pointmin > p ? p : pointmin;

                        results.add(tmp.clone());

                        continue;
                    }

                    if (!leg && Pattern.compile(MSK_LEGENDE).matcher(line).find()) {
                        tmp = line.replaceFirst(MSK_LEGENDE, "$1<|>$2<|>$3<|>$4<|>$5<|>$6")
                                .split("<\\|>");
                        if (table) {
                            tmp[0] = Main.getTableFont(BBCode.addunder(0)
                                            + BBCode.addbcolor(0, color[0])
                                            + Main.getTextForTableFont(tmp[0], 5, 0)
                                            + BBCode.addbcolor(1, color[0]) + BBCode.addunder(1), 5,
                                    0, tmp[0].length(), false);
                            tmp[1] = _name ? Main.getTableFont(BBCode.addunder(0)
                                            + BBCode.addbcolor(0, color[1])
                                            + Main.getTextForTableFont(tmp[1], 20, 0)
                                            + BBCode.addbcolor(1, color[1]) + BBCode.addunder(1), 20,
                                    0, tmp[1].length(), false) : "";
                            tmp[2] = _statut ? Main.getTableFont(BBCode.addunder(0)
                                            + BBCode.addbcolor(0, color[2])
                                            + Main.getTextForTableFont(tmp[2], 20, 0)
                                            + BBCode.addbcolor(1, color[2]) + BBCode.addunder(1), 20,
                                    0, tmp[2].length(), false) : "";
                            tmp[3] = _points ? Main.getTableFont(BBCode.addunder(0)
                                            + BBCode.addbcolor(0, color[3])
                                            + Main.getTextForTableFont(tmp[3], 16, 0)
                                            + BBCode.addbcolor(1, color[3]) + BBCode.addunder(1), 16,
                                    0, tmp[3].length(), false) : "";
                            if (points.size() > 0) {
                                tmp[3] += _points ? Main.getTableFont("", 13, 0, 0, false)
                                        : "";
                            }
                            tmp[4] = _coord ? Main.getTableFont(BBCode.addunder(0)
                                            + BBCode.addbcolor(0, color[4])
                                            + Main.getTextForTableFont(tmp[4], 12, 0)
                                            + BBCode.addbcolor(1, color[4]) + BBCode.addunder(1), 12,
                                    0, tmp[4].length(), false) : "";
                            tmp[5] = _adhes ? Main.getTableFont(BBCode.addunder(0)
                                            + BBCode.addbcolor(0, color[5])
                                            + Main.getTextForTableFont(tmp[5], 20, 0)
                                            + BBCode.addbcolor(1, color[5]) + BBCode.addunder(1), 20,
                                    0, tmp[5].length(), false) : "";
                        } else {
                            tmp[0] = BBCode.addbold(0) + BBCode.addbcolor(0, color[0])
                                    + tmp[0] + " " + BBCode.addbcolor(1, color[0])
                                    + BBCode.addbold(1);
                            tmp[1] = _name ? BBCode.addbold(0) + BBCode.addbcolor(0, color[1])
                                    + tmp[1] + " " + BBCode.addbcolor(1, color[1])
                                    + BBCode.addbold(1) : "";
                            tmp[2] = _statut ? BBCode.addbold(0)
                                    + BBCode.addbcolor(0, color[2]) + tmp[2] + " "
                                    + BBCode.addbcolor(1, color[2]) + BBCode.addbold(1) : "";
                            tmp[3] = _points ? BBCode.addbold(0)
                                    + BBCode.addbcolor(0, color[3]) + tmp[3] + " "
                                    + BBCode.addbcolor(1, color[3]) + BBCode.addbold(1) : "";
                            tmp[4] = _coord ? BBCode.addbold(0)
                                    + BBCode.addbcolor(0, color[4]) + tmp[4] + " "
                                    + BBCode.addbcolor(1, color[4]) + BBCode.addbold(1) : "";
                            tmp[5] = _adhes ? BBCode.addbold(0)
                                    + BBCode.addbcolor(0, color[5]) + tmp[5] + " "
                                    + BBCode.addbcolor(1, color[5]) + BBCode.addbold(1) : "";
                        }
                        textchanged += tmp[0] + tmp[1] + tmp[2] + tmp[3] + tmp[4] + tmp[5]
                                + enter;
                        leg = true;
                        continue;
                    }
                }
            }

            p = (int) Math.abs((double) (progmax - progmin) * (double) 0.05);
            j = (int) Math.abs((double) (pointmax - pointmin) * (double) 0.05);

            for (i = 0; i < results.size(); i++) {
                tmp = (String[]) results.get(i);
                tmp2 = new String[2];

                if (progs.get(i) != null) {
                    if (((Integer) progs.get(i)).intValue() > (mprog + p))
                        tmp2[0] = BBCode.addbcolor(0, "00FF00") + "+"
                                + BBCode.addbcolor(1, "00FF00");
                    else if (((Integer) progs.get(i)).intValue() < (mprog - p))
                        tmp2[0] = BBCode.addbcolor(0, "FF0000") + "-"
                                + BBCode.addbcolor(1, "FF0000");
                    else
                        tmp2[0] = "=";
                } else
                    tmp2[0] = "X";

                if (((Integer) lpoints.get(i)).intValue() > (mpoint + j))
                    tmp2[1] = BBCode.addbcolor(0, "00FF00") + "+"
                            + BBCode.addbcolor(1, "00FF00");
                else if (((Integer) lpoints.get(i)).intValue() < (mpoint - j))
                    tmp2[1] = BBCode.addbcolor(0, "FF0000") + "-"
                            + BBCode.addbcolor(1, "FF0000");
                else
                    tmp2[1] = "=";

                if (table) {
                    tmp[0] = Main.getTableFont(BBCode.addbcolor(0, color[0])
                            + Main.getTextForTableFont(tmp[0], 5, 0)
                            + BBCode.addbcolor(1, color[0]), 5, 0, tmp[0].length(), true);
                    tmp[1] = _name ? Main.getTableFont(BBCode.addbcolor(0, color[1])
                            + Main.getTextForTableFont(tmp[1], 20, 0)
                            + BBCode.addbcolor(1, color[1]), 20, 0, tmp[1].length(), true)
                            : "";
                    tmp[2] = _statut ? Main.getTableFont(BBCode.addbcolor(0, color[2])
                            + Main.getTextForTableFont(tmp[2], 20, 0)
                            + BBCode.addbcolor(1, color[2]), 20, 0, tmp[2].length(), true)
                            : "";
                    tmp[3] = _points ? Main.getTableFont(BBCode.addbcolor(0, color[3])
                                    + formatnumber(tmp[3]) + BBCode.addbcolor(1, color[3]), 15, 0,
                            formatnumber(tmp[3]).length(), true) : "";
                    tmp[3] += _points ? tmp2[1] : "";
                    if (points.size() > 0) {
                        tmp[3] += _points ? Main.getTableFont(tmp[6], 12, 0, tmp[6].length(),
                                true) : "";
                        tmp[3] += _points ? tmp2[0] : "";
                    }
                    tmp[4] = _coord ? Main.getTableFont(BBCode.addbcolor(0, color[4])
                            + Main.getTextForTableFont("[" + tmp[4] + "]", 12, 0)
                            + BBCode.addbcolor(1, color[4]), 12, 0, tmp[4].length() + 2, true)
                            : "";
                    tmp[5] = _adhes ? Main.getTableFont(BBCode.addbcolor(0, color[5])
                            + Main.getTextForTableFont(tmp[5], 20, 0)
                            + BBCode.addbcolor(1, color[5]), 20, 0, tmp[5].length(), true)
                            : "";
                } else {
                    tmp[0] = BBCode.addbcolor(0, color[0]) + tmp[0] + ". "
                            + BBCode.addbcolor(1, color[0]);
                    tmp[1] = _name ? BBCode.addbcolor(0, color[1]) + tmp[1] + " "
                            + BBCode.addbcolor(1, color[1]) : "";
                    tmp[2] = _statut ? BBCode.addbcolor(0, color[2]) + tmp[2] + " "
                            + BBCode.addbcolor(1, color[2]) : "";
                    tmp[3] = _points ? BBCode.addbcolor(0, color[3]) + formatnumber(tmp[3])
                            + BBCode.addbcolor(1, color[3]) + " " + tmp2[1] + " "
                            + BBCode.addbcolor(0, color[3]) + tmp[6]
                            + BBCode.addbcolor(1, color[3]) : "";
                    if (points.size() > 0) {
                        tmp[3] += _points ? " " + tmp2[0] + " " : "";
                    }
                    tmp[4] = _coord ? BBCode.addbcolor(0, color[4]) + "[" + tmp[4] + "] "
                            + BBCode.addbcolor(1, color[4]) : "";
                    tmp[5] = _adhes ? BBCode.addbcolor(0, color[5]) + tmp[5] + " "
                            + BBCode.addbcolor(1, color[5]) : "";
                }

                textchanged += tmp[0] + tmp[1] + tmp[2] + tmp[3] + tmp[4] + tmp[5] + enter;
            }

            textchanged += (table ? BBCode.addfont(1, "Courier,Courier New") : "");

            if (_points) {
                textchanged += enter + configL.getConfig("AVG_POINT") + " "
                        + formatnumber((long) mpoint) + enter;
                if (points.size() > 0)
                    textchanged += configL.getConfig("AVG_PROG") + " "
                            + formatnumber((long) mprog) + enter;
                textchanged += enter + " (" + BBCode.addbcolor(0, "FF0000") + "-"
                        + BBCode.addbcolor(1, "FF0000") + ") p < avg-5%*(max-min)";
                textchanged += enter + " (=) p = avg±5%*(max-min)";
                textchanged += enter + " (" + BBCode.addbcolor(0, "00FF00") + "+"
                        + BBCode.addbcolor(1, "00FF00") + ") p < avg+5%*(max-min)" + enter;
            }
            textchanged += enter + BBCode.addsize(0, "10") + BBCode.addurl(0, ogsc_url)
                    + BBCode.addbcolor(0, "66FFCC") + "Created by OGSconverter"
                    + BBCode.addbcolor(1, "66FFCC") + BBCode.addurl(1, ogsc_url)
                    + BBCode.addsize(1, "10");
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, exText);
            e.printStackTrace();
        }

        return textchanged;
    }

    /**
     * @param text
     */
    public static String converterEMP(String text) {
        String exploded[], line, fichier, search = "", tmp[], masq = "", repl = "", repl2 = "";
        int i, j, size = 0, clrindex, en;
        int type = -2;
        String[] rssName = new String[3];
        boolean ok = false;
        StringBuffer textchanged = new StringBuffer();
        /* LUDO */
        int[][] prod = new int[3][9];
		/* LUDO */
        exText = text;
        text = text.replaceAll("\\.", "");
        text = text.replaceAll("\\s\\-", " 0");
        text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])", "$1"
                + String.valueOf((char) 10) + "$2");
        text = text.replaceAll(String.valueOf((char) 13), "");

        BBCode.setBBCode();

        Configuration configC = null;
        Configuration configL = null;

        try {
            configC = new Configuration("config.ini");
            String setting = configC.getConfig("color");

            fichier = "lang_" + configC.getConfig("active_language") + ".ini";
            configL = new Configuration(fichier);

            text = text.replaceAll(configL.getConfig("textaera"), "");
            text = text.replaceAll("Bad copy !", "");
            exploded = text.trim().split(String.valueOf((char) 10));

            if (setting.equals("01")) {
                color = DARKCOLOR;
            } else if (setting.equals("02")) {
                color = LIGHTCOLOR;
            } else {
                color = configC.getConfig("user_color" + setting).split(",");
            }

            setting = setting.substring(0, 1);

            clrindex = configL.getConfig("FLEET").split("\\|").length;
            clrindex += configL.getConfig("DEFENCES").split("\\|").length + 2;

            i = Main.hextoint(color[clrindex].substring(0, 2)) - 40;
            j = Main.hextoint(color[clrindex].substring(2, 4)) - 40;
            en = Main.hextoint(color[clrindex].substring(4)) - 40;
            String CLine = inttohex(i) + inttohex(j) + inttohex(en);

            boolean first = true;
            boolean table = configC.getConfig("EMP_tfont").equals("1");

            SimpleDateFormat sfd = new SimpleDateFormat("EEE, d MMM yyyy HH:mm",
                    Locale.getDefault());
            textchanged.append(sfd.format(new GregorianCalendar().getTime())).append(enter);

            if (table)
                textchanged.append(BBCode.addfont(0, "Courier,Courier New"));
            else
                textchanged.append(BBCode.addcode(0));

            for (i = 0; i < exploded.length; i++) {
                line = exploded[i].trim();

                if (Pattern.compile(configL.getConfig("EMP_TOP")).matcher(line).find()) {
                    ok = true;
                    continue;
                }

                if (ok) {
                    if (Pattern.compile(MSK_EMP_COORD, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find()) {
                        tmp = line.substring(line.indexOf('[') + 1).split("\\]+\\s*\\[*");
                        size = tmp.length;
                        if (table) {
                            textchanged.append("+");
                            fillBuff("-", textchanged, size * 10 + 30 + 1);
                            textchanged.append("+").append(enter);
                        } else
                            textchanged.append(addLine(size).replaceAll("\\-\\+\\-", "---"));
                        if (configC.getConfig("EMP_coord").equals("1")) {
                            line = line.replaceFirst("(\\S+)\\s.*", "$1");
                            if (table)
                                textchanged.append("|").append(
                                        Main.getTableFont(
                                                Main.getTextForTableFont(line, 30, 0), 30, 0,
                                                line.length(), true)).append("|");
                            else
                                textchanged.append(addcellleft(line, 0, size));
                            for (j = 0; j < tmp.length; j++) {
                                if (table)
                                    textchanged.append(Main.getTableFont(tmp[j], 10, 2,
                                            tmp[j].length(), true));
                                else
                                    textchanged.append(addcellcenter(tmp[j], 0, size));
                            }
                            textchanged.append("|").append(enter);
                            first = false;
                        }
                        tmp = null;
                        masq = "^((?:\\s?\\S+)+)\\s+";
                        repl = "$1:" + enter;
                        repl2 += "$1";
                        for (j = 0; j < (size - 1); j++) {
                            masq += MSK_EMP_DATA + "\\s{2,}";
                            repl += " " + BBCode.addbcolor(0, color[j + 2]) + "$" + (j + 2)
                                    + BBCode.addbcolor(1, color[j + 2]);
                            repl2 += "|$" + (j + 2);
                        }
                        masq += MSK_EMP_DATA + ".*$";
                        repl += " " + BBCode.addbcolor(0, color[j + 2]) + "$" + (j + 2)
                                + BBCode.addbcolor(1, color[j + 2]);
                        repl2 += "|$" + (j + 2);
                        continue;
                    } else if (configC.getConfig("EMP_field").equals("1")
                            && Pattern.compile(MSK_EMP_FIELDS, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find()) {
                        type++;
                        tmp = line.split("\\s+");
                        if (!first) {
                            if (table) {
                                textchanged.append("+");
                                fillBuff("-", textchanged, 30);
                                textchanged.append("+");
                                fillBuff("-", textchanged, size * 10);
                                textchanged.append("+").append(enter);
                            } else
                                textchanged.append(addLine(size));
                        }
                        if (table)
                            textchanged.append("|").append(
                                    Main.getTableFont(Main.getTextForTableFont(tmp[0], 30, 0),
                                            30, 0, tmp[0].length(), true)).append("|");
                        else
                            textchanged.append(addcellleft(tmp[0], 0, size));
                        for (j = 1; j < tmp.length && j <= size; j++) {
                            if (table)
                                textchanged.append(Main.getTableFont(tmp[j], 10, 2,
                                        tmp[j].length(), true));
                            else
                                textchanged.append(addcellcenter(tmp[j], 0, size));
                        }
                        textchanged.append("|").append(enter);
                        tmp = null;
                        first = false;
                        continue;
						/* LUDO */
                    } else if (configC.getConfig("EMP_prod").equals("1")
                            && Pattern.compile(MSK_EMP_GOODS_1, Pattern.CASE_INSENSITIVE)
                            .matcher(line)
                            .find()) {
                        type++;
                        if (type >= 0 && type <= 2) {
                            rssName[type] = line.replaceAll(MSK_EMP_GOODS_1, "$1");
                            Matcher ressMatcher = Pattern.compile(MSK_EMP_GOODS_2)
                                    .matcher(line);
                            int index = 0;
                            while (ressMatcher.find() && index < size) {
                                String ressTemp = ressMatcher.group();
                                String prodUnit = ressTemp.substring(
                                        ressTemp.lastIndexOf(" ") + 1).replaceAll("\\.", "");
                                prod[type][index] = Integer.parseInt(prodUnit);
                                index++;
                            }
                        }
                        continue;
						/* LUDO */
                    } else if ((configC.getConfig("EMP_bld").equals("1") && (line.equals(configL.getConfig("EMP_BLD")) || line.equals(configL.getConfig("OGSPY_EMP_BLD"))))
                            || (configC.getConfig("EMP_ship").equals("1") && line.equals(configL.getConfig("EMP_FLEET")))
                            || (configC.getConfig("EMP_def").equals("1") && (line.equals(configL.getConfig("EMP_DEF")) || line.equals(configL.getConfig("OGSPY_EMP_DEF"))))) {
                        if (!first) {
                            if (table) {
                                textchanged.append("+");
                                fillBuff("-", textchanged, size * 10 + 30 + 1);
                                textchanged.append("+").append(enter);
                            } else
                                textchanged.append(addLine(size).replaceAll("\\-\\+\\-", "---"));
                        }
                        if (table)
                            textchanged.append("|").append(
                                    Main.getTableFont(line.toUpperCase(), 30 + 10 * size + 1,
                                            2, line.length(), true)).append("|").append(enter);
                        else
                            textchanged.append(addcellcenter(line, 1, size))
                                    .append("|")
                                    .append(enter);
                        if ((i + 1) < exploded.length)
                            line = exploded[i + 1].trim();
                        else
                            continue;
                        j = en = 0;
                        while (Pattern.compile(masq, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            i++;
                            en++;
                            tmp = line.replaceAll(masq, repl2).split("\\|");
                            if (j == 0) {
                                if (table) {
                                    textchanged.append("+");
                                    fillBuff("-", textchanged, size * 10 + 30 + 1);
                                    textchanged.append("+").append(enter);
                                    textchanged.append("|")
                                            .append(
                                                    Main.getTableFont(
                                                            Main.getTextForTableFont(tmp[0],
                                                                    30, 0), 30, 0,
                                                            tmp[0].length(), true))
                                            .append("|");
                                } else
                                    textchanged.append(
                                            addLine(size).replaceAll("\\-\\+\\-", "---"))
                                            .append(addcellleft(tmp[0], 0, size));
                            } else {
                                if (table) {
                                    textchanged.append("|")
                                            .append(
                                                    Main.getTableFont(
                                                            Main.getTextForTableFont(tmp[0],
                                                                    30, 0), 30, 0,
                                                            tmp[0].length(), true))
                                            .append("|");
                                } else
                                    textchanged.append(addLine(size)).append(
                                            addcellleft(tmp[0], 0, size));
                            }
                            for (j = 1; j < tmp.length; j++) {
                                if (table) {
                                    line = Main.formatnumber(tmp[j]);
                                    textchanged.append(Main.getTableFont(BBCode.addbcolor(0,
                                            en % 2 == 0 ? color[clrindex] : CLine)
                                                    + line
                                                    + BBCode.addbcolor(1,
                                            en % 2 == 0 ? color[clrindex] : CLine),
                                            10, 2, line.length(), true));
                                } else
                                    textchanged.append(addcellcenter(tmp[j], 0, size));
                            }
                            textchanged.append("|").append(enter);
                            if ((i + 1) < exploded.length)
                                line = exploded[i + 1].trim();
                            else
                                break;
                        }
                        first = false;
                        continue;
                    } else if (configC.getConfig("EMP_research").equals("1")
                            && (line.equals(configL.getConfig("EMP_RESEARCH")) || line.equals(configL.getConfig("OGSPY_EMP_RESEARCH")))) {
                        search = enter + BBCode.addbold(0) + line + BBCode.addbold(1) + enter;
                        if ((i + 1) < exploded.length)
                            line = exploded[i + 1].trim();
                        else
                            continue;
                        en = 0;
                        if (table)
                            search += BBCode.addfont(0, "Courier,Courier New");
                        while (Pattern.compile(masq, Pattern.CASE_INSENSITIVE)
                                .matcher(line)
                                .find()) {
                            i++;
                            tmp = line.replaceAll(masq, repl2).split("\\|");
                            for (j = 1; j < (tmp.length); j++) {
                                if (!tmp[j].equals("0"))
                                    break;
                            }
                            if (j >= tmp.length)
                                j = tmp.length - 1;
                            if (table) {
                                search += Main.getTableFont(Main.getTextForTableFont(tmp[0]
                                        + ":", 37, 0), 37, 0, (tmp[0] + ":").length(), true);

                                search += Main.getTableFont(BBCode.addbcolor(0,
                                        color[clrindex])
                                                + formatnumber(tmp[j])
                                                + BBCode.addbcolor(1, color[clrindex]), 5, 1,
                                        formatnumber(tmp[j]).length(), true);
                                if (++en % 2 == 0)
                                    search += enter;
                            } else {
                                search += tmp[0] + ": " + BBCode.addbcolor(0, color[clrindex])
                                        + tmp[j] + BBCode.addbcolor(1, color[clrindex])
                                        + enter;
                            }
                            if ((i + 1) < exploded.length)
                                line = exploded[i + 1].trim();
                            else
                                break;
                        }
                        if (table)
                            search += BBCode.addfont(1, "Courier,Courier New");
                        if (search.equals(BBCode.addbold(0)
                                + configL.getConfig("EMP_RESEARCH") + BBCode.addbold(1)
                                + enter))
                            search = "";
                        continue;
                    }
                }
            }
            if (table) {
                textchanged.append("+");
                fillBuff("-", textchanged, size * 10 + 30 + 1);
                textchanged.append("+").append(enter);
                textchanged.append(BBCode.addfont(1, "Courier,Courier New")).append(search);
            } else {
                textchanged.append(addLine(size).replaceAll("\\-\\+\\-", "---"));
                textchanged.append(BBCode.addcode(1)).append(search);
            }

			/* LUDO */
            if (configC.getConfig("EMP_prod").equals("1") && rssName[0] != null
                    && rssName[1] != null && rssName[2] != null) {
                int cellSize = 20;

                int metal = compteRessource(prod[0]);
                int cristal = compteRessource(prod[1]);
                int deut = compteRessource(prod[2]);

                textchanged.append(enter);

                if (!table) {
                    textchanged.append(BBCode.addcode(0)).append(enter);
                    textchanged.append(getRessourceLine(cellSize));
                    textchanged.append(getRessourceCell(cellSize, "", 1));
                    textchanged.append(getRessourceCell(cellSize, rssName[0], 1));
                    textchanged.append(getRessourceCell(cellSize, rssName[1], 1));
                    textchanged.append(getRessourceCell(cellSize, rssName[2], 1));
                    textchanged.append(getRessourceCell(cellSize, "Equ. Points", 1));
                    textchanged.append("|").append(enter);

                    textchanged.append(getRessourceLine(cellSize));
                    textchanged.append(getRessourceCell(cellSize, configL.getConfig("HOUR"), 0));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt(metal), 1));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt(cristal), 1));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt(deut), 1));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt((metal
                            + cristal + deut) / 1000), 1));
                    textchanged.append("|").append(enter);

                    textchanged.append(getRessourceLine(cellSize));
                    textchanged.append(getRessourceCell(cellSize, configL.getConfig("DAY"), 0));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt(metal * 24), 1));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt(cristal * 24),
                            1));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt(deut * 24), 1));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt((metal
                            + cristal + deut) * 24 / 1000), 1));
                    textchanged.append("|").append(enter);

                    textchanged.append(getRessourceLine(cellSize));
                    textchanged.append(getRessourceCell(cellSize, configL.getConfig("WEEK"), 0));
                    textchanged.append(getRessourceCell(cellSize,
                            getViewForInt(metal * 24 * 7), 1));
                    textchanged.append(getRessourceCell(cellSize,
                            getViewForInt(cristal * 24 * 7), 1));
                    textchanged.append(getRessourceCell(cellSize,
                            getViewForInt(deut * 24 * 7), 1));
                    textchanged.append(getRessourceCell(cellSize, getViewForInt((metal
                            + cristal + deut) * 24 * 7 / 1000), 1));
                    textchanged.append("|").append(enter);

                    textchanged.append(getRessourceLine(cellSize));
                    textchanged.append(BBCode.addcode(1));
                } else {
                    textchanged.append(BBCode.addfont(0, "Courier,Courier New")).append(enter);
                    textchanged.append(getRessourceLine(cellSize));
                    textchanged.append("|")
                            .append(Main.getTableFont("", cellSize, 0, 0, true));
                    textchanged.append("|").append(
                            Main.getTableFont(rssName[0], cellSize, 2, rssName[0].length(),
                                    true));
                    textchanged.append("|").append(
                            Main.getTableFont(rssName[1], cellSize, 2, rssName[1].length(),
                                    true));
                    textchanged.append("|").append(
                            Main.getTableFont(rssName[2], cellSize, 2, rssName[2].length(),
                                    true));
                    textchanged.append("|").append(
                            Main.getTableFont("Equ. Points", cellSize, 2,
                                    "Equ. Points".length(), true));
                    textchanged.append("|").append(enter);

                    textchanged.append(getRessourceLine(cellSize));
                    textchanged.append("|").append(
                            Main.getTableFont(configL.getConfig("HOUR"), cellSize, 0,
                                    configL.getConfig("HOUR").length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex])
                                            + getViewForInt(metal)
                                            + BBCode.addbcolor(1, color[clrindex]), cellSize, 2,
                                    getViewForInt(metal).length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex])
                                            + getViewForInt(cristal)
                                            + BBCode.addbcolor(1, color[clrindex]), cellSize, 2,
                                    getViewForInt(cristal).length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex])
                                            + getViewForInt(deut)
                                            + BBCode.addbcolor(1, color[clrindex]), cellSize, 2,
                                    getViewForInt(deut).length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex])
                                            + getViewForInt((metal + cristal + deut) / 1000)
                                            + BBCode.addbcolor(1, color[clrindex]), cellSize, 2,
                                    getViewForInt((metal + cristal + deut) / 1000).length(),
                                    true));
                    textchanged.append("|").append(enter);

                    textchanged.append("|").append(
                            Main.getTableFont(configL.getConfig("DAY"), cellSize, 0,
                                    configL.getConfig("DAY").length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, CLine)
                                            + getViewForInt(metal * 24) + BBCode.addbcolor(1, CLine),
                                    cellSize, 2, getViewForInt(metal * 24).length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(
                                    BBCode.addbcolor(0, CLine) + getViewForInt(cristal * 24)
                                            + BBCode.addbcolor(1, CLine), cellSize, 2,
                                    getViewForInt(cristal * 24).length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, CLine)
                                            + getViewForInt(deut * 24) + BBCode.addbcolor(1, CLine),
                                    cellSize, 2, getViewForInt(deut * 24).length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, CLine)
                                    + getViewForInt((metal + cristal + deut) * 24 / 1000)
                                    + BBCode.addbcolor(1, CLine), cellSize, 2, getViewForInt(
                                    (metal + cristal + deut) * 24 / 1000).length(), true));
                    textchanged.append("|").append(enter);

                    textchanged.append("|").append(
                            Main.getTableFont(configL.getConfig("WEEK"), cellSize, 0,
                                    configL.getConfig("WEEK").length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex])
                                            + getViewForInt(metal * 24 * 7)
                                            + BBCode.addbcolor(1, color[clrindex]), cellSize, 2,
                                    getViewForInt(metal * 24 * 7).length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex])
                                            + getViewForInt(cristal * 24 * 7)
                                            + BBCode.addbcolor(1, color[clrindex]), cellSize, 2,
                                    getViewForInt(cristal * 24 * 7).length(), true));
                    textchanged.append("|").append(
                            Main.getTableFont(BBCode.addbcolor(0, color[clrindex])
                                            + getViewForInt(deut * 24 * 7)
                                            + BBCode.addbcolor(1, color[clrindex]), cellSize, 2,
                                    getViewForInt(deut * 24 * 7).length(), true));
                    textchanged.append("|")
                            .append(
                                    Main.getTableFont(
                                            BBCode.addbcolor(0, color[clrindex])
                                                    + getViewForInt((metal + cristal + deut) * 24 * 7 / 1000)
                                                    + BBCode.addbcolor(1, color[clrindex]),
                                            cellSize,
                                            2,
                                            getViewForInt(
                                                    (metal + cristal + deut) * 24 * 7 / 1000).length(),
                                            true));
                    textchanged.append("|").append(enter);

                    textchanged.append(getRessourceLine(cellSize));
                    textchanged.append(BBCode.addfont(1, "Courier,Courier New"));
                }
            }
			/* LUDO */

            textchanged.append(enter).append(BBCode.addsize(0, "10"));
            textchanged.append(BBCode.addurl(0, ogsc_url));
            textchanged.append(BBCode.addbcolor(0, "66FFCC"))
                    .append("Created by OGSconverter");
            textchanged.append(BBCode.addbcolor(1, "66FFCC"));
            textchanged.append(BBCode.addurl(1, ogsc_url)).append(BBCode.addsize(1, "10"));
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, exText);
            e.printStackTrace();
        }

        return textchanged.toString();
    }

    public static String converterFLEET(String text) {

        String fichier, exploded[], tmp[], line;
        int clrindex, i, j, fleetNb[];
        boolean ok = false;
        StringBuffer textchanged = new StringBuffer();
        List fleetName;

        exText = text;
        text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])", "$1"
                + String.valueOf((char) 10) + "$2");
        text = text.replaceAll(String.valueOf((char) 13), "");

        BBCode.setBBCode();

        Configuration configC = null;
        Configuration configL = null;

        try {
            configC = new Configuration("config.ini");
            String setting = configC.getConfig("color");

            fichier = "lang_" + configC.getConfig("active_language") + ".ini";
            configL = new Configuration(fichier);

            text = text.replaceAll(configL.getConfig("textaera"), "");
            text = text.replaceAll("Bad copy !", "");
            exploded = text.trim().split(String.valueOf((char) 10));

            if (setting.equals("01")) {
                color = DARKCOLOR;
            } else if (setting.equals("02")) {
                color = LIGHTCOLOR;
            } else {
                color = configC.getConfig("user_color" + setting).split(",");
            }

            setting = setting.substring(0, 1);

            boolean table = configC.getConfig("ER_table").equals("1");

            clrindex = (tmp = configL.getConfig("ER_FLEET").split("\\|")).length;
            clrindex += configL.getConfig("DEFENCES").split("\\|").length + 2;

            fleetNb = new int[tmp.length];
            fleetName = new ArrayList(Arrays.asList(tmp));

            SimpleDateFormat sfd = new SimpleDateFormat("EEE, d MMM yyyy HH:mm",
                    Locale.getDefault());
            textchanged.append(sfd.format(new GregorianCalendar().getTime())).append(enter);
            textchanged.append(enter);
            textchanged.append(BBCode.addunder(0));
            textchanged.append(BBCode.addbcolor(0, color[0]));
            textchanged.append(configL.getConfig("EMP_FLEET")).append(" :");
            textchanged.append(BBCode.addbcolor(1, color[0]));
            textchanged.append(BBCode.addunder(1));
            textchanged.append(enter);

            for (i = 0; i < exploded.length; i++) {
                line = exploded[i].trim();

                if (line.length() <= 0)
                    continue;

                if (!ok && Pattern.compile(MSK_FLOTTE_START).matcher(line).find()) {
                    ok = true;
                    continue;
                }

                if (ok) {
                    if (Pattern.compile(MSK_FLOTTE_DATA).matcher(line).find()) {
                        tmp = line.replaceFirst(MSK_FLOTTE_DATA, "$1::$2").split("::");

                        j = fleetName.indexOf(tmp[0].trim());
                        if (j >= 0)
                            fleetNb[j] += exptoint(tmp[1]);
                        continue;
                    } else {
                        ok = false;
                        continue;
                    }
                }

            }

            if (table)
                textchanged.append(BBCode.addfont(0, "Courier,Courier New"));

            for (i = 0; i < fleetNb.length; i++) {
                if (fleetNb[i] == 0)
                    continue;
                if (table) {
                    textchanged.append(Main.getTableFont(Main.getTextForTableFont(
                            (String) fleetName.get(i), 25, 0), 25, 0,
                            ((String) fleetName.get(i)).length(), true));
                    line = formatnumber(fleetNb[i]);
                    textchanged.append(Main.getTableFont(BBCode.addbcolor(0, color[clrindex])
                                    + line + BBCode.addbcolor(1, color[clrindex]), 10, 1,
                            line.length(), true));
                    textchanged.append(enter);
                } else {
                    textchanged.append(fleetName.get(i));
                    textchanged.append(" ");
                    textchanged.append(BBCode.addbcolor(0, color[clrindex]));
                    textchanged.append(formatnumber(fleetNb[i]));
                    textchanged.append(BBCode.addbcolor(1, color[clrindex]));
                    textchanged.append(enter);
                }
            }

            if (table)
                textchanged.append(BBCode.addfont(1, "Courier,Courier New"));

        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, exText);
            e.printStackTrace();
        }

        return textchanged.toString();
    }

    public static String converterNotRecognized(String text) {

        String setting, lang, exploded[] = null, line, linebis;
        StringBuffer textOut = new StringBuffer("");
        int rep = 0, i, d, f, m;
        Matcher matcher;

        exText = text;
        text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])", "$1"
                + String.valueOf((char) 10) + "$2");
        text = text.replaceAll(String.valueOf((char) 13), "");

        BBCode.setBBCode();

        Configuration configC = null;
        Configuration configL = null;

        try {
            configC = new Configuration("config.ini");
            setting = configC.getConfig("color");
            lang = configC.getConfig("active_language");
            configL = new Configuration("lang_" + lang + ".ini");

            if (setting.equals("01")) {
                color = DARKCOLOR;
            } else if (setting.equals("02")) {
                color = LIGHTCOLOR;
            } else {
                color = configC.getConfig("user_color" + setting).split(",");
            }

            rep = JOptionPane.showConfirmDialog(
                    getParentFrame(),
                    "Your copy is not recognized by OGSConverter like a ogame report.\n\nDo you want to format the numbers of this copy?",
                    "Not recognized", JOptionPane.YES_NO_OPTION);
            if (rep != JOptionPane.YES_OPTION)
                return null;

            text = text.replaceAll(configL.getConfig("textaera"), "");
            text = text.replaceAll("Bad copy !", "");

            exploded = text.split(String.valueOf((char) 10));
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e, exText);
            e.printStackTrace();
        }

        for (i = 0; i < exploded.length; i++) {
            line = exploded[i];
            linebis = line;
            matcher = Pattern.compile(MSK_NB_FORMATED).matcher(line);
            d = f = m = 0;
            while (matcher.find(f)) {
                f = matcher.end();
                d = matcher.start();
                linebis = linebis.substring(0, d + m)
                        + BBCode.addbcolor(0, color[color.length - 2])
                        + Main.formatnumber(line.substring(d, f))
                        + BBCode.addbcolor(1, color[color.length - 2]) + line.substring(f);
                m = linebis.length() - line.length();
            }

            textOut.append(linebis).append(enter);
        }

        return textOut.toString();
    }

    public static String prepareText(String text, Configuration langConfig) throws Exception {

        text = text.replaceAll(langConfig.getConfig("textaera"), "");

        text = text.replaceAll("Bad copy !", "");

        text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
                + String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])", "$1"
                + String.valueOf((char) 10) + "$2");

        text = text.replaceAll(String.valueOf((char) 13), "");

        return text;
    }

    /* LUDO */
    public static int compteRessource(int[] prod) {
        int prodTotale = 0;
        for (int i = 0; i < prod.length; i++) {
            prodTotale += prod[i];
        }
        return prodTotale;
    }

    public static String getViewForInt(int value) {
        String view = "";
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,##0", dfs);
        view = df.format(value);
        return view;
    }

    public static String getRessourceLine(int cellSize) {
        StringBuffer sbLine = new StringBuffer();
        StringBuffer borderBuff = new StringBuffer();
        fillBuff("-", borderBuff, cellSize);
        for (int i = 0; i < 5; i++) {
            sbLine.append("+" + borderBuff);
        }
        sbLine.append("+" + enter);
        return sbLine.toString();
    }

    public static StringBuffer fillBuff(String value, StringBuffer target, int nbOccurs) {
        for (int i = 0; i < nbOccurs; i++) {
            target.append(value);
        }
        return target;
    }

    /**
     * @param cellSize
     * @param content
     * @param align    :
     *                 0 Left, 1 Center, 2 Right (si autre => Center)
     * @return
     */

    public static String getRessourceCell(int cellSize, String content, int align) {
        StringBuffer sbCell = new StringBuffer("|");
        boolean shouldFill = content.length() < cellSize;
        if (!shouldFill) {
            if (content.length() == cellSize) {
                sbCell.append(content);
            } else {
                sbCell.append(content.subSequence(0, cellSize - 1) + ".");
            }
        } else {
            int nbMissing = cellSize - content.length();
            if (align == 0) {
                sbCell.append(content);
                fillBuff(" ", sbCell, nbMissing);
            } else if (align == 2) {
                fillBuff(" ", sbCell, nbMissing);
                sbCell.append(content);
            } else {
                if (nbMissing % 2 == 0) {
                    fillBuff(" ", sbCell, nbMissing / 2);
                    sbCell.append(content);
                    fillBuff(" ", sbCell, nbMissing / 2);
                } else {
                    fillBuff(" ", sbCell, nbMissing / 2 + 1);
                    sbCell.append(content);
                    fillBuff(" ", sbCell, nbMissing / 2);
                }
            }

        }
        return sbCell.toString();
    }

	/* LUDO */

    public static String addLine(int col) {
        int i;
        String out = "+-----------------------------";
        for (i = 0; i < col; i++) {
            out += "+--------";
        }
        out += "+" + enter;
        return out;
    }

    public static String addcellcenter(String val, int type, int col) {
        int i, end = 8;
        String out = "|";
        if (type == 1)
            end = 9 * col + 29;
        if (end < val.length())
            val = val.substring(0, end - 1) + ".";
        int s = (int) Math.ceil((double) (end - val.length()) / 2);
        if (s < 0)
            s = 0;
        for (i = 0; i < end; i++) {
            if (i == s) {
                out += val;
                i += val.length() - 1;
            } else
                out += " ";
        }
        return out;
    }

    public static String addcellleft(String val, int type, int col) {
        int i, end = 29;
        if (type == 1)
            end += 9 * col;
        if (end < val.length())
            val = val.substring(0, end - 1) + ".";
        String out = "|" + val;
        for (i = val.length(); i < end; i++) {
            out += " ";
        }
        return out;
    }

    private static String getTableFont(String text, int length, int align, int realLengthText,
                                       boolean background) {
        int k = realLengthText, start;
        StringBuffer buff = new StringBuffer(text);

        if (align == 0) {
            if (background)
                buff.append(BBCode.addbcolor(0, color[color.length - 1]));
            if (background)
                buff.insert(0, BBCode.addbcolor(1, color[color.length - 1]));
            buff.insert(0, "_");
            if (background)
                buff.insert(0, BBCode.addbcolor(0, color[color.length - 1]));
            for (start = k + 1; start < length; start++) {
                buff.append("_");
            }
            if (background)
                buff.append(BBCode.addbcolor(1, color[color.length - 1]));
        } else if (align == 1) {
            if (background)
                buff.insert(0, BBCode.addbcolor(1, color[color.length - 1]));
            for (start = k; start < length; start++) {
                buff.insert(0, "_");
            }
            if (background)
                buff.insert(0, BBCode.addbcolor(0, color[color.length - 1]));
        } else {
            if (background)
                buff.append(BBCode.addbcolor(0, color[color.length - 1]));
            int il = k;
            for (start = 0; start < (int) ((float) (length - k) / 2); start++) {
                buff.append("_");
                il++;
            }
            if (background)
                buff.insert(0, BBCode.addbcolor(1, color[color.length - 1]));
            if (background)
                buff.append(BBCode.addbcolor(1, color[color.length - 1]));
            for (start = il; start < length; start++) {
                buff.insert(0, "_");
            }
            if (background)
                buff.insert(0, BBCode.addbcolor(0, color[color.length - 1]));
        }

        return buff.toString();
    }

    private static String getTextForTableFont(String text, int length, int align) {
        int k = text.length();
        String out = new String(text);

        if (k > length - ((align == 0) ? 1 : 0))
            out = out.substring(0, length - 1 - ((align == 0) ? 1 : 0)) + ".";

        return out;
    }

    public static long[] exptoint(String[] text) {
        int i, l;
        long[] ret;

        l = text.length;
        ret = new long[l];
        for (i = 0; i < l; i++)
            ret[i] = exptoint(text[i]);

        return ret;
    }

    public static long exptoint(String text) {
        int k = 0, n, s = 1;

        if (text == null)
            return 0;

        text = text.trim();

        if (Pattern.compile("^\\-?\\d+([\\.,]\\d+)+$", Pattern.CASE_INSENSITIVE)
                .matcher(text)
                .find())
            text = text.replaceAll("[\\.,]", "");

        if (!Pattern.compile("^\\-?\\d+(\\.\\d*e\\+\\d+)?$", Pattern.CASE_INSENSITIVE)
                .matcher(text)
                .find())
            return 0;

        if (Pattern.compile("^\\-\\d+", Pattern.CASE_INSENSITIVE).matcher(text).find()) {
            s = (-1);
            text = text.substring(1);
        }

        if (Pattern.compile("\\d+\\.*\\d*e\\+\\d+", Pattern.CASE_INSENSITIVE)
                .matcher(text)
                .find()) {
            String[] nb = text.split("e\\+");
            String exp = nb[1];

            nb = nb[0].split("\\.");
            text = nb[0];
            if (nb.length > 1) {
                text += nb[1];
                k = nb[1].length();
            }

            for (n = k; n < Integer.parseInt(exp); n++) {
                text += "0";
            }
        }

        return (s * Long.parseLong(text));
    }

    public static String formatnumber(String text) {

        long copie = exptoint(text);

        return NumberFormat.getInstance(Locale.GERMANY).format(copie);
    }

    public static String formatnumber(long nb) {
        return NumberFormat.getInstance(Locale.GERMANY).format(nb);
    }

    public static String getIpFrom(String adresse) {
        String toreturn = null;
        try {
            // creation d'un objet URL
            URL url = new URL(adresse);
            // on etablie une connection a cette url
            URLConnection uc = url.openConnection();
            // on y cree un flux de lecture
            InputStream in = uc.getInputStream();
            // on lit le premier bit
            int c = in.read();
            // on cree un StringBuilder pour par la suite y ajouter tout les bit
            // lus
            StringBuffer build = new StringBuffer();
            // tant que c n'est pas egale au bit indiquant la fin d'un flux...
            while (c != -1) {
                build.append((char) c);
                // ...on l'ajoute dans le StringBuilder...
                c = in.read();
                // ...on lit le suivant
            }
            // on retourne le code de la page
            toreturn = build.toString();

        } catch (MalformedURLException e) {
            Print_exception("Je n'arrive pas à vérifier la version.");
            return null;
        } catch (IOException e) {
            Print_exception("Je n'arrive pas à vérifier la version.");
            return null;
        }
        return toreturn;
    }

    public static void openurl(String add) {
        Properties sys = System.getProperties();
        String os = sys.getProperty("os.name");
        Runtime r = Runtime.getRuntime();
        try {
            if (os.startsWith("Windows 9") || os.startsWith("Windows ME"))
                r.exec("command /C " + add);
            else if (os.startsWith("Windows"))
                r.exec("cmd /c start " + add);
            else if (os.indexOf("mac") != -1)
                r.exec("open " + add);
            else if (os.indexOf("Linux") != -1)
                r.exec("konqueror " + add); // disont que c'est le plus répendu
                // ^^.
            else
                r.exec("start " + add);

        } catch (IOException e) {
            ExceptionAlert.createExceptionAlert(e);
            e.printStackTrace();
        }
    }

    private static String type_chang(String tmp[], Map type_nb, Map type_nb2, List type_name,
                                     String type_name_complete[], String type, String moment) {
        String repl = "", tmp2[] = null, tmp3[] = null, part;
        StringBuffer line = new StringBuffer();
        int j, i, l, k, dcolor[][] = null, d3color[][] = null, start;
        Integer id;
        int ci = 0, infinity;
        Iterator it;
        if (type.equals("defense"))
            ci = 13;

        if (tmp.length > 0)
            line.append(tmp[0]);
        if (tmp.length < 2)
            return line.toString();

        if (type_nb.size() == 0) {
            if (tmp.length > 2)
                line.append(tmp[2]);
            return line.toString();
        }

        j = -1;
        if (Pattern.compile("\\[color=#[\\dA-F]{6}_to_#[\\dA-F]{6}\\]",
                Pattern.CASE_INSENSITIVE).matcher(tmp[1]).find()) {
            j = type_nb.size();
            tmp2 = null;
            tmp2 = tmp[1].split("_to_");
            dcolor = null;
            dcolor = new int[(int) ((tmp2.length - 1) / 2)][6];
            for (i = 0; i < (tmp2.length - 1); i++) {
                if (i % 2 == 0)
                    tmp2[i] = tmp2[i].substring(tmp2[i].length() - 6);
                else
                    tmp2[i] = tmp2[i].substring(1, 7);
                dcolor[(int) (i / 2)][(i % 2) * 3] = hextoint(tmp2[i].substring(0, 2));
                dcolor[(int) (i / 2)][(i % 2) * 3 + 1] = hextoint(tmp2[i].substring(2, 4));
                dcolor[(int) (i / 2)][(i % 2) * 3 + 2] = hextoint(tmp2[i].substring(4));
            }
        }
        if (Pattern.compile(
                "\\[3color=#[\\dA-F]{6}\\-to\\-#[\\dA-F]{6}\\-to\\-#[\\dA-F]{6}\\]",
                Pattern.CASE_INSENSITIVE).matcher(tmp[1]).find()) {
            if (j == -1)
                j = type_nb.size();

            tmp3 = null;
            tmp3 = tmp[1].split("-to-");
            d3color = null;
            d3color = new int[(int) ((tmp3.length - 2) / 3)][9];
            tmp3 = new String[tmp3.length - 2];
            i = k = 0;
            infinity = 0;
            while ((i = tmp[1].substring(i).indexOf("[3color=")) != -1) {
                if (infinity++ > 10000)
                    return null;
                tmp3[3 * k] = tmp[1].substring(i)
                        .replaceFirst(
                                "^\\[3color=#([\\dA-F]{6})\\-to\\-#[\\dA-F]{6}\\-to\\-#[\\dA-F]{6}\\].*$",
                                "$1");
                tmp3[3 * k + 1] = tmp[1].substring(i)
                        .replaceFirst(
                                "^\\[3color=#[\\dA-F]{6}\\-to\\-#([\\dA-F]{6})\\-to\\-#[\\dA-F]{6}\\].*$",
                                "$1");
                tmp3[3 * k + 2] = tmp[1].substring(i)
                        .replaceFirst(
                                "^\\[3color=#[\\dA-F]{6}\\-to\\-#[\\dA-F]{6}\\-to\\-#([\\dA-F]{6})\\].*$",
                                "$1");
                d3color[k][0] = hextoint(tmp3[3 * k].substring(0, 2));
                d3color[k][1] = hextoint(tmp3[3 * k].substring(2, 4));
                d3color[k][2] = hextoint(tmp3[3 * k].substring(4));
                d3color[k][3] = hextoint(tmp3[3 * k + 1].substring(0, 2));
                d3color[k][4] = hextoint(tmp3[3 * k + 1].substring(2, 4));
                d3color[k][5] = hextoint(tmp3[3 * k + 1].substring(4));
                d3color[k][6] = hextoint(tmp3[3 * k + 2].substring(0, 2));
                d3color[k][7] = hextoint(tmp3[3 * k + 2].substring(2, 4));
                d3color[k][8] = hextoint(tmp3[3 * k + 2].substring(4));
                k++;
                i += 29;
            }
        }

        it = type_nb.keySet().iterator();
        for (i = 0; i < type_nb.size(); i++) {
            id = ((Integer) it.next());
            k = id.intValue();
            if (type_nb.get(id) == null)
                continue;
            repl = tmp[1];
            if (i == type_nb.size() - 1) {
                if ((l = repl.indexOf("[/last]")) != -1)
                    repl = repl.substring(repl.indexOf("[last]") + 6, l);
            } else
                repl = repl.replaceAll("\\[last\\].*?\\[\\/last\\]", "");

            if (i == 0) {
                if ((l = repl.indexOf("[/first]")) != -1)
                    repl = repl.substring(repl.indexOf("[first]") + 7, l);
            } else
                repl = repl.replaceAll("\\[first\\].*?\\[\\/first\\]", "");

            repl = repl.replaceAll("\\[n\\]", "" + i);
            repl = repl.replaceAll("\\[" + type + "\\]", (String) type_name.get(k));
            repl = repl.replaceAll("\\[" + type + " complete name\\]", type_name_complete[k]);
            repl = repl.replaceAll("\\[" + type + " nb\\]", (String) type_nb.get(id));
            repl = repl.replaceAll("\\[" + type + " lose\\]",
                    formatnumber(Math.abs(exptoint((String) type_nb.get(id))
                            - exptoint((String) type_nb2.get(id)))));
            if (moment.equals("start")) {
                repl = repl.replaceAll("\\[" + type + " start nb\\]", (String) type_nb.get(id));
                if ((String) type_nb2.get(id) != null)
                    repl = repl.replaceAll("\\[" + type + " end nb\\]",
                            (String) type_nb2.get(id));
                else
                    repl = repl.replaceAll("\\[" + type + " end nb\\]", "0");
            } else {
                repl = repl.replaceAll("\\[" + type + " start nb\\]",
                        (String) type_nb2.get(id));
                repl = repl.replaceAll("\\[" + type + " end nb\\]", (String) type_nb.get(id));
            }
            repl = repl.replaceAll("#" + type, "#" + color[k + ci + 2]);
            if (dcolor != null && j != -1) {
                for (start = 0; start < dcolor.length; start++) {
                    if (j > 1)
                        part = inttohex(dcolor[start][0]
                                + i
                                * (int) (((float) dcolor[start][3] - (float) dcolor[start][0]) / ((float) j - 1)))
                                + inttohex(dcolor[start][1]
                                + i
                                * (int) (((float) dcolor[start][4] - (float) dcolor[start][1]) / ((float) j - 1)))
                                + inttohex(dcolor[start][2]
                                + i
                                * (int) (((float) dcolor[start][5] - (float) dcolor[start][2]) / ((float) j - 1)));
                    else
                        part = tmp2[2 * start];
                    repl = repl.replaceAll("\\[color=#" + tmp2[start * 2] + "_to_#"
                            + tmp2[start * 2 + 1] + "\\]", "[color=#" + part + "]");
                    repl = repl.replaceAll("\\[\\/color=#" + tmp2[start * 2] + "_to_#"
                            + tmp2[start * 2 + 1] + "\\]", "[/color=#" + part + "]");
                }
            }
            if (d3color != null && j != -1) {
                for (start = 0; start < d3color.length; start++) {
                    if (j > 2 && (float) k < (float) (j / 2 - 1))
                        part = inttohex(d3color[start][0]
                                + 2
                                * i
                                * (int) (((float) d3color[start][3] - (float) d3color[start][0]) / ((float) j - 2)))
                                + inttohex(d3color[start][1]
                                + 2
                                * i
                                * (int) (((float) d3color[start][4] - (float) d3color[start][1]) / ((float) j - 2)))
                                + inttohex(d3color[start][2]
                                + 2
                                * i
                                * (int) (((float) d3color[start][5] - (float) d3color[start][2]) / ((float) j - 2)));
                    else if (j > 2 && (float) k >= (float) (j / 2 - 1))
                        part = inttohex(d3color[start][3]
                                + (int) (((float) i - (float) j / 2 + 1) * ((float) (d3color[start][6] - (float) d3color[start][3]) / ((float) j / 2))))
                                + inttohex(d3color[start][4]
                                + (int) (((float) i - (float) j / 2 + 1) * (((float) d3color[start][7] - (float) d3color[start][4]) / ((float) j / 2))))
                                + inttohex(d3color[start][5]
                                + (int) (((float) i - (float) j / 2 + 1) * (((float) d3color[start][8] - (float) d3color[start][5]) / ((float) j / 2))));
                    else if (j == 2)
                        part = tmp3[3 * start + 2];
                    else
                        part = tmp3[3 * start];
                    repl = repl.replaceAll("\\[3color=#" + tmp3[start * 3] + "\\-to\\-#"
                                    + tmp3[start * 3 + 1] + "\\-to\\-#" + tmp3[start * 3 + 2] + "\\]",
                            "[color=#" + part + "]");
                    repl = repl.replaceAll("\\[\\/3color=#" + tmp3[start * 3] + "\\-to\\-#"
                                    + tmp3[start * 3 + 1] + "\\-to\\-#" + tmp3[start * 3 + 2] + "\\]",
                            "[/color=#" + part + "]");
                }
            }
            line.append(repl);
        }
        if (tmp.length > 2)
            line.append(tmp[2]);

        return line.toString();
    }

    static String degrade(String word, int r1, int v1, int b1, int r2, int v2, int b2) {
        int j = word.length(), i;
        String color, outword = "";

        for (i = 0; i < j; i++) {
            if (j > 1)
                color = inttohex(r1 + i * (int) (((float) r2 - r1) / ((float) j - 1)))
                        + inttohex(v1 + i
                        * (int) (((float) v2 - (float) v1) / ((float) j - 1)))
                        + inttohex(b1 + i
                        * (int) (((float) b2 - (float) b1) / ((float) j - 1)));
            else
                color = inttohex(r1) + inttohex(v1) + inttohex(b1);
            if (i < j - 1)
                outword += "[color=#" + color + "]" + word.substring(i, i + 1) + "[/color=#"
                        + color + "]";
            else
                outword += "[color=#" + color + "]" + word.substring(i) + "[/color=#" + color
                        + "]";
        }

        return outword;
    }

    static int hextoint(String h) {
        if (h.length() != 2)
            return 255;
        int out = 0;

        char hi = h.charAt(0);
        char lo = h.charAt(1);

        out = toint(hi) * 16 + toint(lo);

        return out;
    }

    static String inttohex(int i) {
        if (i >= 255)
            return "FF";
        if (i <= 0)
            return "00";
        String out = "";

        int hi = (int) (i / 16);
        int lo = i % 16;

        out = tohex(hi) + tohex(lo);

        return out;
    }

    static int toint(char h) {
        switch (h) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'A':
                return 10;
            case 'B':
                return 11;
            case 'C':
                return 12;
            case 'D':
                return 13;
            case 'E':
                return 14;
            case 'F':
                return 15;
            default:
                return 0;
        }
    }

    static String tohex(int i) {
        switch (i) {
            case 0:
                return "0";
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "A";
            case 11:
                return "B";
            case 12:
                return "C";
            case 13:
                return "D";
            case 14:
                return "E";
            case 15:
                return "F";
            default:
                return "0";
        }
    }

    static long getConsumption(int[] start, int[] end, int[] fleet, int pspd) {
        int tech[], i;
        long time2;
        double spd_fleet = 110000000000D, consumption = 0, spd2, enf, num, c1;
        int[][] speed = {{1, 5000, 10}, // PT - 0
                {1, 7500, 50}, // GT - 1
                {1, 12500, 20}, // Cle - 2
                {2, 10000, 75}, // Clo - 3
                {2, 15000, 300}, // Cr - 4
                {3, 10000, 500}, // VB - 5
                {2, 2500, 1000}, // Colo - 6
                {1, 2000, 300}, // Rec - 7
                {1, 100000000, 1}, // Sonde - 8
                {2, 4000, 1000}, // Bomb - 9
                {1, 0, 0}, // Sat - 10
                {3, 5000, 1000}, // Destr - 11
                {3, 100, 1}, // EDLM - 12
                {3, 10000, 250} // Tr - 13
        };
        double[] spd = new double[14];

        tech = new int[3];

        try {
            Configuration configC = new Configuration("config.ini");
            i = (int) exptoint(configC.getConfig("universe_selected"));
            tech[0] = (int) exptoint(configC.getConfig("combustion").split("\\|")[i]);
            tech[1] = (int) exptoint(configC.getConfig("impulse").split("\\|")[i]);
            tech[2] = (int) exptoint(configC.getConfig("hyperspace").split("\\|")[i]);
            configC = null;
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e);
            e.printStackTrace();
        }

        if (start[0] != end[0]) {
            enf = 20000 * Math.abs(start[0] - end[0]);
        } else {
            if (start[1] != end[1]) {
                enf = 95 * Math.abs(start[1] - end[1]) + 2700;
            } else {
                if (start[2] != end[2]) {
                    enf = 5 * Math.abs(start[2] - end[2]) + 1000;
                } else {
                    enf = 5;
                }
            }
        }

        for (i = 0; i < speed.length; i++) {
            spd[i] = (double) speed[i][1]
                    * (1.0 + (double) tech[speed[i][0] - 1] * (double) speed[i][0] / 10.0); // normal

            if (i == 0 && tech[1] > 4)
                spd[i] = 10000.0 * (1.0 + tech[1] * 2.0 / 10.0); // PT
            if (i == 9 && tech[2] > 7)
                spd[i] = 5000.0 * (1.0 + tech[2] * 3.0 / 10.0); // Bomb
        }

        for (i = 0; i < speed.length; i++)
            if (fleet[i] > 0)
                spd_fleet = Math.min(spd_fleet, spd[i]);

        time2 = Math.round(10.0 + (35000.0 / (double) pspd * Math.sqrt(enf * 1000.0
                / spd_fleet)));

        for (i = 0; i < fleet.length; i++) {
            num = fleet[i];
            if (num != 0) {
                spd2 = 35000.0 / ((double) time2 - 10.0)
                        * Math.sqrt((double) enf * 10.0 / spd[i]);
                c1 = (double) num
                        * ((double) speed[i][2] + (i == 0 && tech[1] > 4 ? 10.0 : 0.0));
                consumption += c1 * enf / 35000.0 * Math.pow(spd2 / 10 + 1, 2);
            }
        }

        return Math.round(consumption);
    }

    static void Print_exception(String except) {
        JOptionPane.showMessageDialog(getParentFrame(), except, "Alert Message!",
                JOptionPane.WARNING_MESSAGE);
    }

    static int getcolor(String c) {
        int out = 0;

        if (c.length() < 6)
            return 0;

        String r = c.substring(0, 2);
        String v = c.substring(2, 4);
        String b = c.substring(4);

        out = hextoint(r) * 65536;
        out += hextoint(v) * 256;
        out += hextoint(b);

        return out;
    }

    static void loadskin() {
        if (JWS)
            return;

        String slash = File.separator;
        String pathTheme = System.getProperty("user.dir") + slash + "theme" + slash;
        File f, skins[];
        int i;
        try {
            skinJAR = new TreeMap();
            skin = new TreeMap();
            skin.put("default", "default");

            f = new File(pathTheme + "looks.jar");
            if (f.exists()) {
                skinJAR.put("jgoodies-BrownSugar", "looks.jar");
                skinJAR.put("jgoodies-DarkStar", "looks.jar");
                skinJAR.put("jgoodies-DesertBlue", "looks.jar");
                skinJAR.put("jgoodies-DesertBluer", "looks.jar");
                skinJAR.put("jgoodies-DesertGreen", "looks.jar");
                skinJAR.put("jgoodies-DesertRed", "looks.jar");
                skinJAR.put("jgoodies-DesertYellow", "looks.jar");
                skinJAR.put("jgoodies-ExperienceBlue", "looks.jar");
                skinJAR.put("jgoodies-ExperienceGreen", "looks.jar");
                skinJAR.put("jgoodies-ExperienceRoyale", "looks.jar");
                skinJAR.put("jgoodies-LightGray", "looks.jar");
                skinJAR.put("jgoodies-Silver", "looks.jar");
                skinJAR.put("jgoodies-SkyBlue", "looks.jar");
                skinJAR.put("jgoodies-SkyBluer", "looks.jar");
                skinJAR.put("jgoodies-SkyGreen", "looks.jar");
                skinJAR.put("jgoodies-SkyKrupp", "looks.jar");
                skinJAR.put("jgoodies-SkyPink", "looks.jar");
                skinJAR.put("jgoodies-SkyRed", "looks.jar");
                skinJAR.put("jgoodies-SkyYellow", "looks.jar");
                skin.put("jgoodies-BrownSugar",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-DarkStar",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-DesertBlue",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-DesertBluer",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-DesertGreen",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-DesertRed",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-DesertYellow",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-ExperienceBlue",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-ExperienceGreen",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-ExperienceRoyale",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-LightGray",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-Silver", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-SkyBlue", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-SkyBluer",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-SkyGreen",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-SkyKrupp",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-SkyPink", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-SkyRed", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
                skin.put("jgoodies-SkyYellow",
                        "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
            }

            f = new File(pathTheme + "CLooks.jar");
            if (f.exists()) {
                skinJAR.put("compiere", "CLooks.jar");
                skin.put("compiere", "org.compiere.plaf.CompiereLookAndFeel");
            }

            f = new File(pathTheme + "liquidlnf.jar");
            if (f.exists()) {
                skinJAR.put("birosoft", "liquidlnf.jar");
                skin.put("birosoft", "com.birosoft.liquid.LiquidLookAndFeel");
            }

            f = new File(pathTheme + "nimrodlf.jar");
            if (f.exists()) {
                skinJAR.put("nimrod", "nimrodlf.jar");
                skin.put("nimrod", "com.nilo.plaf.nimrod.NimRODLookAndFeel");
            }

            f = new File(pathTheme + "PgsLookAndFeel.jar");
            if (f.exists()) {
                skinJAR.put("pagosoft", "PgsLookAndFeel.jar");
                skin.put("pagosoft", "com.pagosoft.plaf.PgsLookAndFeel");
            }

            f = new File(pathTheme + "metouia.jar");
            if (f.exists()) {
                skinJAR.put("metouia", "metouia.jar");
                skin.put("metouia", "net.sourceforge.mlf.metouia.MetouiaLookAndFeel");
            }

            f = new File(pathTheme + "gtkswing.jar");
            if (f.exists()) {
                skinJAR.put("gtk", "gtkswing.jar");
                skin.put("gtk", "org.gtk.java.swing.plaf.gtk.GtkLookAndFeel");
            }

            f = new File(pathTheme + "squareness.jar");
            if (f.exists()) {
                skinJAR.put("squareness", "squareness.jar");
                skin.put("squareness", "net.beeger.squareness.SquarenessLookAndFeel");
            }

            f = new File(pathTheme + "OfficeLnFs.jar");
            if (f.exists()) {
                skinJAR.put("office2003", "OfficeLnFs.jar");
                skinJAR.put("officexp", "OfficeLnFs.jar");
                skinJAR.put("visualstudio2005", "OfficeLnFs.jar");
                skin.put("office2003", "org.fife.plaf.Office2003.Office2003LookAndFeel");
                skin.put("officexp", "org.fife.plaf.OfficeXP.OfficeXPLookAndFeel");
                skin.put("visualstudio2005",
                        "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel");
            }

            getLookAndFeels();

            f = new File(pathTheme);
            if (f.exists() && f.isDirectory()) {
                skins = f.listFiles();
                for (i = 0; skins != null && i < skins.length; i++) {
                    if (skins[i].getName().endsWith(".zip")) {
                        skin.put(skins[i].getName().substring(0,
                                skins[i].getName().length() - 4).toLowerCase(),
                                skins[i].getName());
                    }
                }
            }
        } catch (Exception e) {
            Print_exception("The skin list can't be loaded.");
            e.printStackTrace();
        }
    }

    static int getLookAndFeels() {
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        int i;
        String nomName, nomClasse;
        for (i = 0; i < info.length; i++) {
            nomClasse = info[i].getClassName();
            nomName = info[i].getName();
            if (!nomClasse.equals("javax.swing.plaf.metal.MetalLookAndFeel")) {
                if (!nomName.startsWith("jgoodies-"))
                    nomName = nomName.toLowerCase();
                skin.put(nomName, nomClasse);
            }
        }
        return info.length;
    }

    static String htmlspecialchars_decode(String text) {
        text = text.replaceAll("&amp;", "&");
        text = text.replaceAll("&lt;", "<");
        text = text.replaceAll("&gt;", ">");

        return text;
    }

    static JFrame getParentFrame() {
        if (fen != null && fen.isVisible()) {
            return fen;
        } else {
            JFrame f = new JFrame();
            f.getToolkit().setDynamicLayout(true);
            f.getRootPane().setMinimumSize(new Dimension(10, 10));
            f.setIconImage(new ImageIcon(Main.class.getResource("images/icone.png")).getImage());
            return f;
        }
    }

    static void closeFrame(JFrame f) {
        if (f.getExtendedState() != JFrame.ICONIFIED
                && f.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            String name = f.getTitle();
            if (!Main.JWS) {
                prefs.putInt(name + "width", f.getWidth());
                prefs.putInt(name + "height", f.getHeight());
                prefs.putInt(name + "x_location", (int) f.getLocationOnScreen().getX());
                prefs.putInt(name + "y_location", (int) f.getLocationOnScreen().getY());
            }
        }
        f.dispose();
        f.removeAll();
    }

    public static BufferedReader getModelBuffer(String model, String charset) {
        BufferedReader buffer;
        String OnLineModel = null;

        if (JWS && model.startsWith("-> ")) {
            OnLineModel = OGSConnection.getModel(model.substring(3, model.length() - 5), null,
                    false);
            if (OnLineModel == null) {
                return null;
            }
        }

        try {
            if (JWS) {
                if (!model.startsWith("-> ")) {
                    buffer = new BufferedReader(new InputStreamReader(
                            USER_LOADER.getResourceAsStream(model), charset));
                } else {
                    OnLineModel = OGSConnection.getModel(
                            model.substring(3, model.length() - 5), charset, false);
                    if (OnLineModel == null) {
                        return null;
                    }
                    buffer = new BufferedReader(new StringReader(OnLineModel));
                }
            } else {
                buffer = new BufferedReader(new InputStreamReader(new FileInputStream(
                        new File(USER_URL + File.separator + model)), charset));
            }
        } catch (Exception e) {
            ExceptionAlert.createExceptionAlert(e);
            e.printStackTrace();
            buffer = null;
        }

        return buffer;
    }

    static String color[] = {"00CC00", "FF6666", "CC99FF", "FF00FF", "00FF00", "00CC99",
            "00FFFF", "FFCC00", "FFFF00", "0099FF", "FF0099", "00FF99", "00B0B0", "CCCCFF",
            "A0FF99", "FFFF90", "FF99A0", "99FFA0", "00CC00", "99A0FF", "CCFFCC", "999900",
            "FFCC99", "FFCC99", "FFCC00", "FF0000", "888888"};

    static String ogsc_url = "https://www.ogsteam.fr/forums/forum-17-ogsconverter";

    static String osenter;

    static String enter = String.valueOf((char) 10);

    static String exText = "";

    static int OUT_ATT = 0;

    static int IN_ATT = 1;

    static String menu;

    static Map skin = new TreeMap();

    public static Fenetre fen = null;

    public static String USER_URL;

    public static String USER_HOME;

    public static ClassLoader USER_LOADER;

    public static boolean JWS = false;

    public static Map skinJAR;

    public static Preferences prefs;

    public static String defaultCharset;

    static {
        InputStreamReader isr = new InputStreamReader(System.in);
        defaultCharset = isr.getEncoding();
        try {
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isr = null;
    }
}
