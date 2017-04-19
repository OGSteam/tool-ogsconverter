package fr.ogsteam.ogsconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class RCDatas implements OGSConstantes {

	private long[] att_perte_par_ress = { 0, 0, 0 }, att_courante_par_ress = { 0, 0, 0 };
	private long[] def_def_perte_par_ress = { 0, 0, 0 }, def_flt_perte_par_ress = { 0, 0, 0 };
	private int round;
	private long consumption = 0;

	private String _date = "", _attacker[], _attacker_coordinates[], _defender[],
			_defender_coordinates[];
	private String _attacker_weapons[], _defender_weapons[], _attacker_shielding[],
			_defender_shielding[], _attacker_armour[], _defender_armour[];

	private Map[] _attacker_fleet_start, _defender_fleet_start, _defender_defense_start;
	private Map[] _attacker_fleet_end, _defender_fleet_end, _defender_defense_end;
	private String _captured_metal = "0", _captured_cristal = "0", _captured_deuterium = "0";
	private String _harvest_metal = "", _harvest_cristal = "", _moon_probability = "0%";
	private boolean _moon_created = false;

	private long _fire[] = new long[2], _firepower[] = new long[2],
			_shields_absorb[] = new long[2];

	private int att_nb, def_nb;
	private List harvestInfo = new ArrayList();

	public void generateDatas(String text, int speed) {

		String fichier = null, exploded[] = null, tmp[] = null, tmp2[] = null, repl = "", line = "", part = "";
		Configuration langConfig = null, iniConfig;
		String _top1 = "", _top2 = "", _att = "", _def = "", _type = "", _nb = "", _techno = "", _moon = "", _lose = "", _rec_top = "", _inter_round = "", _dest = "", _gain = "";
		int i, j, att_index, def_index, il, k, infinity;
		boolean ok, go_end, harvest;
		int[] att_fleet = null, att_coord = null, def_coord = null;

		int pts[][] = { { 2000, 0, 0 }, { 1500, 500, 0 }, { 6000, 2000, 0 },
				{ 20000, 15000, 2000 }, { 2000, 6000, 0 }, { 50000, 50000, 30000 },
				{ 10000, 10000, 0 }, { 50000, 50000, 0 } };
		int fpts[][] = { { 2000, 2000, 0 }, { 6000, 6000, 0 }, { 3000, 1000, 0 },
				{ 6000, 4000, 0 }, { 20000, 7000, 2000 }, { 45000, 15000, 0 },
				{ 10000, 20000, 10000 }, { 10000, 6000, 2000 }, { 0, 1000, 0 },
				{ 50000, 25000, 15000 }, { 0, 2000, 500 }, { 60000, 50000, 15000 },
				{ 5000000, 4000000, 1000000 }, { 30000, 40000, 15000 } };

		List ifleet = null, idef = null;

		try {
			iniConfig = new Configuration("config.ini");
			fichier = "lang_" + iniConfig.getConfig("active_language") + ".ini";
			langConfig = new Configuration(fichier);

			text = prepareText(text, langConfig);

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
			_inter_round = langConfig.getConfig("INTER_ROUND");
			_dest = langConfig.getConfig("DEST");
			_gain = langConfig.getConfig("GAIN");

			tmp = StringOperation.sansAccent(langConfig.getConfig("FLEET")).toLowerCase().replace('.', ' ').replaceAll(
					"\\s+", "").split("\\|");
			ifleet = new ArrayList(Arrays.asList(tmp));
			tmp = StringOperation.sansAccent(langConfig.getConfig("DEFENCES")).toLowerCase().replace('.', ' ').replaceAll(
					"\\s+", "").split("\\|");
			idef = new ArrayList(Arrays.asList(tmp));

		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}

		exploded = text.split(String.valueOf((char) 10));

		repl = (text.split(_inter_round))[0];
		att_nb = (repl.split(_att + msk_name_coord).length) - 1;
		def_nb = (repl.split(_def + msk_name_coord).length) - 1;

		if (att_nb <= 0)
			att_nb = 1;
		if (def_nb <= 0)
			def_nb = 1;

		// initialisations :
		ok = false;
		il = 0;
		att_index = 0;
		def_index = 0;
		_moon_created = false;
		go_end = false;
		_date = "";
		_attacker = new String[att_nb];
		_defender = new String[def_nb];
		_attacker_coordinates = new String[att_nb];
		_defender_coordinates = new String[def_nb];
		_attacker_weapons = new String[att_nb];
		_defender_weapons = new String[def_nb];
		_attacker_shielding = new String[att_nb];
		_defender_shielding = new String[def_nb];
		_attacker_armour = new String[att_nb];
		_defender_armour = new String[def_nb];
		_attacker_fleet_start = new TreeMap[att_nb];
		_defender_fleet_start = new TreeMap[def_nb];
		_defender_defense_start = new TreeMap[def_nb];
		_attacker_fleet_end = new TreeMap[att_nb];
		_defender_fleet_end = new TreeMap[def_nb];
		_defender_defense_end = new TreeMap[def_nb];
		for (i = 0; i < att_nb; i++) {
			_attacker_fleet_start[i] = new TreeMap();
			_attacker_fleet_end[i] = new TreeMap();
		}
		for (i = 0; i < def_nb; i++) {
			_defender_fleet_start[i] = new TreeMap();
			_defender_defense_start[i] = new TreeMap();
			_defender_fleet_end[i] = new TreeMap();
			_defender_defense_end[i] = new TreeMap();
		}
		_moon_probability = "0%";
		_captured_metal = "0";
		_captured_cristal = "0";
		_captured_deuterium = "0";
		harvest = false;
		def_def_perte_par_ress = new long[3];
		def_flt_perte_par_ress = new long[3];
		att_perte_par_ress = new long[3];
		att_courante_par_ress = new long[3];

		att_fleet = new int[fpts.length];

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
					line = line.replaceAll(msk_date, "$1");
					_date = line;
					ok = true;
					go_end = false;
					part = "attack";
					continue;
				}

				if (ok) {

					if (!go_end
							&& Pattern.compile(_inter_round, Pattern.CASE_INSENSITIVE)
									.matcher(line)
									.find()) {
						tmp = line.replaceFirst("\\D*", "").split("\\D{2,}");
						_fire[0] += Main.exptoint(tmp[0]);
						_firepower[0] += Main.exptoint(tmp[1]);
						if (tmp.length < 3) {
							il++;
							tmp = exploded[il].replaceFirst("\\D*", "").split("\\D{2,}");
							_shields_absorb[0] += Main.exptoint(tmp[0]);
						} else
							_shields_absorb[0] += Main.exptoint(tmp[2]);
						il++;
						while (exploded[il].trim().equals("")) {
							il++;
						}
						tmp = exploded[il].replaceFirst("\\D*", "").split("\\D{2,}");
						_fire[1] += Main.exptoint(tmp[0]);
						_firepower[1] += Main.exptoint(tmp[1]);
						if (tmp.length < 3) {
							il++;
							tmp = exploded[il].replaceFirst("\\D*", "").split("\\D{2,}");
							_shields_absorb[1] += Main.exptoint(tmp[0]);
						} else
							_shields_absorb[1] += Main.exptoint(tmp[2]);
						continue;
					}

					if (Pattern.compile("^" + _att + msk_name_coord1, Pattern.CASE_INSENSITIVE)
							.matcher(line)
							.find()) {
						if (part.equals("def") && !go_end) {
							round = 0;
							for (j = il; j < exploded.length; j++) {
								if (Pattern.compile(_lose, Pattern.CASE_INSENSITIVE).matcher(
										exploded[j]).find())
									break;

								if (Pattern.compile(_inter_round, Pattern.CASE_INSENSITIVE)
										.matcher(exploded[j])
										.find()) {
									tmp = exploded[j].replaceFirst("\\D*", "").split("\\D+");
									_fire[0] += Main.exptoint(tmp[0]);
									_firepower[0] += Main.exptoint(tmp[1]);
									if (tmp.length < 3) {
										j++;
										tmp = exploded[j].replaceFirst("\\D*", "").split(
												"\\D{2,}");
										_shields_absorb[0] += Main.exptoint(tmp[0]);
									} else
										_shields_absorb[0] += Main.exptoint(tmp[2]);
									j++;
									while (exploded[j].trim().equals("")) {
										j++;
									}
									tmp = exploded[j].replaceFirst("\\D*", "").split("\\D+");
									_fire[1] += Main.exptoint(tmp[0]);
									_firepower[1] += Main.exptoint(tmp[1]);
									if (tmp.length < 3) {
										j++;
										tmp = exploded[j].replaceFirst("\\D*", "").split(
												"\\D{2,}");
										_shields_absorb[1] += Main.exptoint(tmp[0]);
									} else
										_shields_absorb[1] += Main.exptoint(tmp[2]);
									continue;
								}

								if (part.equals("def")
										&& Pattern.compile("^" + _att + msk_name_coord1,
												Pattern.CASE_INSENSITIVE)
												.matcher(exploded[j])
												.find()) {
									il = j - 1;
									round++;
									part = "attack";
								} else if (Pattern.compile("^" + _def + msk_name_coord1,
										Pattern.CASE_INSENSITIVE).matcher(exploded[j]).find())
									part = "def";
							}

							part = "attack";
							go_end = true;
							att_index = 0;
							def_index = 0;
							continue;
						}
						tmp = line.split(_type);
						if (tmp.length > 1)
							line = _type + tmp[1];
						_attacker[att_index] = tmp[0].replaceAll(_att + msk_name_coord2, "$1")
								.trim();
						_attacker_coordinates[att_index] = tmp[0].replaceAll(
								_att + msk_name_coord2, "$2").trim();
						if (att_coord == null) {
							tmp = tmp[0].replaceAll(_att + msk_name_coord2, "$2").split(":");
							if (tmp.length >= 3) {
								att_coord = new int[] { (int) Main.exptoint(tmp[0]),
										(int) Main.exptoint(tmp[1]),
										(int) Main.exptoint(tmp[2]) };
							}
						}
						tmp = null;
						att_index++;
					} else if (Pattern.compile("^" + _def + msk_name_coord1,
							Pattern.CASE_INSENSITIVE).matcher(line).find()) {
						part = "def";
						tmp = line.split(_type);
						if (tmp.length > 1)
							line = _type + tmp[1];
						_defender[def_index] = tmp[0].replaceAll(_def + msk_name_coord2, "$1")
								.trim();
						_defender_coordinates[def_index] = tmp[0].replaceAll(
								_def + msk_name_coord2, "$2").trim();
						if (def_coord == null) {
							tmp = tmp[0].replaceAll(_def + msk_name_coord2, "$2").split(":");
							if (tmp.length >= 3) {
								def_coord = new int[] { (int) Main.exptoint(tmp[0]),
										(int) Main.exptoint(tmp[1]),
										(int) Main.exptoint(tmp[2]) };
							}
						}
						tmp = null;
						def_index++;
					} else if (Pattern.compile("^" + _techno + "\\s+" + msk_nb_formated + "%",
							Pattern.CASE_INSENSITIVE).matcher(line).find()) {
						tmp = line.split(_type);
						if (tmp.length > 1)
							line = _type + tmp[1];
						if (part.equals("attack")) {
							_attacker_weapons[att_index - 1] = tmp[0].replaceFirst(
									msk_technology, "$1");
							_attacker_shielding[att_index - 1] = tmp[0].replaceFirst(
									msk_technology, "$2");
							_attacker_armour[att_index - 1] = tmp[0].replaceFirst(
									msk_technology, "$3");
						} else if (part.equals("def")) {
							_defender_weapons[def_index - 1] = tmp[0].replaceFirst(
									msk_technology, "$1");
							_defender_shielding[def_index - 1] = tmp[0].replaceFirst(
									msk_technology, "$2");
							_defender_armour[def_index - 1] = tmp[0].replaceFirst(
									msk_technology, "$3");
						}
						tmp = null;
					}

					if (Pattern.compile("^" + _type, Pattern.CASE_INSENSITIVE)
							.matcher(line)
							.find()) {
						repl = langConfig.getConfig("FLEET") + "|"
								+ langConfig.getConfig("DEFENCES");
						line = StringOperation.sansAccent(line);
						line = line.replace('.', ' ');
						line = line.replaceAll("\\s+", "");
						repl = StringOperation.sansAccent(repl);
						repl = repl.replace('.', ' ');
						repl = repl.replaceAll("\\s+", "");
						line = line.replaceAll("(?i)(" + repl + ")", " $1 ");
						repl = line.replaceAll("(?i)(" + repl + ").*$", "");
						tmp = line.substring(repl.length()).split("\\s{2,}");
						continue;
					} else if (Pattern.compile("^" + _nb, Pattern.CASE_INSENSITIVE).matcher(
							line).find()) {
						line = line.replaceAll("[\\.,']", "");
						tmp2 = line.replaceFirst(msk_nb_line, "$1").split("\\s+");
						for (j = 0; j < tmp2.length; j++) {
							if (tmp2[j].trim().equals(""))
								continue;
							if ((k = ifleet.indexOf(tmp[j].trim().toLowerCase())) >= 0) {
								if (part.equals("def")) {
									if (!go_end) {
										def_flt_perte_par_ress[0] += Main.exptoint(tmp2[j])
												* fpts[k][0];
										def_flt_perte_par_ress[1] += Main.exptoint(tmp2[j])
												* fpts[k][1];
										def_flt_perte_par_ress[2] += Main.exptoint(tmp2[j])
												* fpts[k][2];
										_defender_fleet_start[def_index - 1].put(
												new Integer(k), Main.formatnumber(tmp2[j]));
										_defender_fleet_end[def_index - 1].put(new Integer(k),
												null);
									} else {
										def_flt_perte_par_ress[0] -= Main.exptoint(tmp2[j])
												* fpts[k][0];
										def_flt_perte_par_ress[1] -= Main.exptoint(tmp2[j])
												* fpts[k][1];
										def_flt_perte_par_ress[2] -= Main.exptoint(tmp2[j])
												* fpts[k][2];
										_defender_fleet_end[def_index - 1].put(new Integer(k),
												Main.formatnumber(tmp2[j]));
									}
								} else {
									if (!go_end) {
										att_perte_par_ress[0] += Main.exptoint(tmp2[j])
												* fpts[k][0];
										att_perte_par_ress[1] += Main.exptoint(tmp2[j])
												* fpts[k][1];
										att_perte_par_ress[2] += Main.exptoint(tmp2[j])
												* fpts[k][2];
										att_courante_par_ress[0] += Main.exptoint(tmp2[j])
												* fpts[k][0];
										att_courante_par_ress[1] += Main.exptoint(tmp2[j])
												* fpts[k][1];
										att_courante_par_ress[2] += Main.exptoint(tmp2[j])
												* fpts[k][2];
										_attacker_fleet_start[att_index - 1].put(
												new Integer(k), Main.formatnumber(tmp2[j]));
										_attacker_fleet_end[att_index - 1].put(new Integer(k),
												null);
										att_fleet[k] += (int) Main.exptoint(tmp2[j]);
									} else {
										att_perte_par_ress[0] -= Main.exptoint(tmp2[j])
												* fpts[k][0];
										att_perte_par_ress[1] -= Main.exptoint(tmp2[j])
												* fpts[k][1];
										att_perte_par_ress[2] -= Main.exptoint(tmp2[j])
												* fpts[k][2];
										_attacker_fleet_end[att_index - 1].put(new Integer(k),
												Main.formatnumber(tmp2[j]));
									}
								}

							} else if ((k = idef.indexOf(tmp[j].trim().toLowerCase())) >= 0) {
								if (!go_end && part.equals("def")) {
									def_def_perte_par_ress[0] += Main.exptoint(tmp2[j])
											* pts[k][0];
									def_def_perte_par_ress[1] += Main.exptoint(tmp2[j])
											* pts[k][1];
									def_def_perte_par_ress[2] += Main.exptoint(tmp2[j])
											* pts[k][2];
									_defender_defense_start[def_index - 1].put(new Integer(k),
											Main.formatnumber(tmp2[j]));
									_defender_defense_end[def_index - 1].put(new Integer(k),
											null);
								} else if (part.equals("def")) {
									def_def_perte_par_ress[0] -= Main.exptoint(tmp2[j])
											* pts[k][0];
									def_def_perte_par_ress[1] -= Main.exptoint(tmp2[j])
											* pts[k][1];
									def_def_perte_par_ress[2] -= Main.exptoint(tmp2[j])
											* pts[k][2];
									_defender_defense_end[def_index - 1].put(new Integer(k),
											Main.formatnumber(tmp2[j]));
								}
							}
						}

						tmp = null;
						tmp2 = null;
						continue;
					} else if (Pattern.compile("^" + _dest, Pattern.CASE_INSENSITIVE).matcher(
							line).find()) {
						if (go_end == false) {
							att_perte_par_ress[0] -= att_courante_par_ress[0];
							att_perte_par_ress[1] -= att_courante_par_ress[1];
							att_perte_par_ress[2] -= att_courante_par_ress[2];
							_attacker_fleet_end = _attacker_fleet_start;
						}
						if (def_index >= def_nb)
							go_end = true;
						continue;
					} else if (go_end
							&& Pattern.compile("^" + _gain, Pattern.CASE_INSENSITIVE).matcher(
									line).find()) {
						while (!Pattern.compile(msk_gain, Pattern.CASE_INSENSITIVE).matcher(
								line).find()) {
							il++;
							line = exploded[il];
						}
						_captured_metal = Main.formatnumber(line.replaceFirst(msk_gain, "$1"));
						_captured_cristal = Main.formatnumber(line.replaceFirst(msk_gain, "$2"));
						_captured_deuterium = Main.formatnumber(line.replaceFirst(msk_gain,
								"$3"));
						tmp = line.replaceFirst(msk_gain, "$1:$2:$3").split("\\:");
						continue;
					} else if (go_end
							&& Pattern.compile("^" + _lose, Pattern.CASE_INSENSITIVE).matcher(
									line).find()) {
						repl = "";
						for (; il < exploded.length; il++) {
							line = exploded[il].trim();
							if (Pattern.compile(_rec_top, Pattern.CASE_INSENSITIVE).matcher(
									line).find()) {
								harvest = true;
								break;
							} else if (Pattern.compile(_top1, Pattern.CASE_INSENSITIVE)
									.matcher(line)
									.find()
									|| Pattern.compile(_top2, Pattern.CASE_INSENSITIVE)
											.matcher(line)
											.find()) {
								break;
							} else if (Pattern.compile(msk_user_msg, Pattern.CASE_INSENSITIVE)
									.matcher(line)
									.find()) {
								k = il++;
								while (il < exploded.length && exploded[il].trim().equals(""))
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
							repl += line;
							if (Pattern.compile("^" + _moon, Pattern.CASE_INSENSITIVE)
									.matcher(line)
									.find()) {
								_moon_created = true;
							}
						}

						tmp = null;
						tmp = repl.replaceFirst("\\D+", "").split("\\D{3,}");

						_harvest_metal = Main.formatnumber(tmp[2]);
						_harvest_cristal = Main.formatnumber(tmp[3]);
						if ((_moon_probability = repl.replaceFirst(msk_moon_prob, "$1%")).equals(repl))
							_moon_probability = "0%";
						break;
					}
				}

			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();

			}
		}

		if (harvest) {
			infinity = 0;
			boolean harvest_find = false;
			j = il;

			while (harvest) {
				if (infinity++ > 10000) {
					Main.Print_exception("Infinity loop!");
					break;
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
					}

					line += " " + exploded[il];

					if (il == exploded.length - 1) {
						harvest = false;
						break;
					}
				}
				if (harvest_find) {
					tmp = Main.converterRecy(line, Main.IN_ATT);
					harvestInfo.add(tmp.clone());
				}
				if (il >= exploded.length)
					break;
			}
		}

		// consommation:
		if (att_fleet != null && att_coord != null && def_coord != null) {
			consumption += Main.getConsumption(att_coord, def_coord, att_fleet, speed);
			att_coord = null;
			def_coord = null;
		}
	}

	private String prepareText(String text, Configuration langConfig) throws Exception {

		text = text.replaceAll(langConfig.getConfig("textaera"), "");

		text = text.replaceAll("Bad copy !", "");

		text = text.replaceAll("([^" + String.valueOf((char) 10) + "])"
				+ String.valueOf((char) 13) + "([^" + String.valueOf((char) 10) + "])", "$1"
				+ String.valueOf((char) 10) + "$2");

		text = text.replaceAll(String.valueOf((char) 13), "");

		return text;
	}

	public String[] get_attacker() {
		return _attacker;
	}

	public String[] get_attacker_armour() {
		return _attacker_armour;
	}

	public String[] get_attacker_coordinates() {
		return _attacker_coordinates;
	}

	public Map[] get_attacker_fleet_end() {
		return _attacker_fleet_end;
	}

	public Map[] get_attacker_fleet_start() {
		return _attacker_fleet_start;
	}

	public String[] get_attacker_shielding() {
		return _attacker_shielding;
	}

	public String[] get_attacker_weapons() {
		return _attacker_weapons;
	}

	public String get_captured_cristal() {
		return _captured_cristal;
	}

	public String get_captured_deuterium() {
		return _captured_deuterium;
	}

	public String get_captured_metal() {
		return _captured_metal;
	}

	public String get_date() {
		return _date;
	}

	public String[] get_defender() {
		return _defender;
	}

	public String[] get_defender_armour() {
		return _defender_armour;
	}

	public String[] get_defender_coordinates() {
		return _defender_coordinates;
	}

	public Map[] get_defender_defense_end() {
		return _defender_defense_end;
	}

	public Map[] get_defender_defense_start() {
		return _defender_defense_start;
	}

	public Map[] get_defender_fleet_end() {
		return _defender_fleet_end;
	}

	public Map[] get_defender_fleet_start() {
		return _defender_fleet_start;
	}

	public String[] get_defender_shielding() {
		return _defender_shielding;
	}

	public String[] get_defender_weapons() {
		return _defender_weapons;
	}

	public long[] get_fire() {
		return _fire;
	}

	public long[] get_firepower() {
		return _firepower;
	}

	public String get_harvest_cristal() {
		return _harvest_cristal;
	}

	public String get_harvest_metal() {
		return _harvest_metal;
	}

	public boolean is_moon_created() {
		return _moon_created;
	}

	public String get_moon_probability() {
		return _moon_probability;
	}

	public long[] get_shields_absorb() {
		return _shields_absorb;
	}

	public long[] getAtt_courante_par_ress() {
		return att_courante_par_ress;
	}

	public int getAtt_nb() {
		return att_nb;
	}

	public long[] getAtt_perte_par_ress() {
		return att_perte_par_ress;
	}

	public long[] getDef_def_perte_par_ress() {
		return def_def_perte_par_ress;
	}

	public long[] getDef_flt_perte_par_ress() {
		return def_flt_perte_par_ress;
	}

	public int getDef_nb() {
		return def_nb;
	}

	public int getRound() {
		return round;
	}

	public long getConsumption() {
		return consumption;
	}

	public long getHarvestedMetal() {
		long harvestedMetal = 0;
		int i;
		for (i = 0; i < harvestInfo.size(); i++) {
			harvestedMetal += Main.exptoint(((String[]) harvestInfo.get(i))[6]);
		}
		if (harvestedMetal == 0) {
			try {
				if (Configuration.getConfig("config.ini", "CR_harvested").equals("1"))
					harvestedMetal = Main.exptoint(get_harvest_metal());
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}
		}
		return harvestedMetal;
	}

	public long getHarvestedCrystal() {
		long harvestedCrystal = 0;
		int i;
		for (i = 0; i < harvestInfo.size(); i++) {
			harvestedCrystal += Main.exptoint(((String[]) harvestInfo.get(i))[7]);
		}
		if (harvestedCrystal == 0) {
			try {
				if (Configuration.getConfig("config.ini", "CR_harvested").equals("1"))
					harvestedCrystal = Main.exptoint(get_harvest_cristal());
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}
		}
		return harvestedCrystal;
	}
}
