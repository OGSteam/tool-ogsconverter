package fr.ogsteam.ogsconverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public abstract class OGSConnection {

	static String[] keys = new String[] { "active_language", "CR_end", "active_model", "bold",
			"def_rpl_name", "CR_harvested", "BBCode_bold", "RC_date", "color", "clipbord",
			"url", "defcoord", "defname", "code", "ita", "attcoord", "size", "column",
			"BBCode_under", "iamatt", "img", "BBCode_code", "BBCode_size", "bcolor", "BBname",
			"attname", "BBCode_url", "cdr_taux", "cr_taux", "ER_techno", "ER_stock",
			"BBcodes", "ER_coordonnate", "att_rpl_name", "center", "BBCode_ita",
			"BBCode_quote", "under", "font", "afterbattle", "BBCode_img", "BBCode_bcolor",
			"ER_fleet", "ER_def", "myrenta", "auto_convert", "ER_planet", "BBCode_font",
			"ER_building", "BBCode_center", "quote", "att_rpl_coord", "Techno",
			"def_rpl_coord", "system", "speed", "ER_table", "ER_mip", "ER_coordinateRepl",
			"ER_prod", "ER_phlg", "ER_planetNameRepl", "ER_cdr", "user_color_name", "EMP_def",
			"EMP_research", "EMP_field", "EMP_ship", "EMP_coord", "EMP_tfont", "EMP_prod",
			"EMP_bld", "ml_name", "ml_statut", "ml_points", "ml_coord", "ml_adhesion",
			"ml_table", "harvestCoord", "harvestDate", "harvest_rpl_coord",
			"universe_selected", "universes", "cr_consumption", "hyperspace", "impulse",
			"combustion", "ER_act", "ER_player_name" };

	static String[] colorKeys = new String[] { "ER_FLEET", "ER_GOODS", "user_sz_fleet",
			"ER_DEFENCES", "user_sz_rss", "user_sz_techno", "ER_TECHNO", "ER_BUILDINGS",
			"user_sz_def", "user_sz_bld", "user_color" };

	private static String password = null;

	static void saveConfig(boolean mdp) {
		String jwsURL = "", login = "", value, names[];
		StringBuffer donnees, reponse;
		BufferedReader reader = null;
		Configuration config;
		int i, j;
		donnees = new StringBuffer();
		reponse = new StringBuffer();

		try {
			config = new Configuration("config.ini");
			jwsURL = config.getConfig("jwsURL");
			login = config.getConfig("login");
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
			return;
		}

		if (login.equals("annonymous"))
			return;

		if (mdp) {
			password = OGSConnection.getPassWord();
			if (password == null) {
				return;
			}
		}

		try {
			// encodage des paramètres de la requête
			try {
				OGSConnection.addData(donnees, "login", login);
				if (password != null) {
					OGSConnection.addData(donnees, "pass", password);
				}
				OGSConnection.addData(donnees, "action", "save_config");
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}

			// Configs générales
			for (i = 0; i < keys.length; i++) {
				try {
					value = config.getConfig(keys[i]);
					OGSConnection.addData(donnees, keys[i], value);
				} catch (Exception e) {
					ExceptionAlert.createExceptionAlert(e);
					e.printStackTrace();
				}
			}
			// Configs couleur et mise en valeur.
			try {
				names = config.getConfig("user_color_name").split("\\|");
				for (i = 0; i < names.length; i++) {
					if (i == 0 && names[0].trim().length() <= 0)
						break;
					for (j = 0; j < colorKeys.length; j++) {
						value = config.getConfig(colorKeys[j] + (i + 1));
						OGSConnection.addData(donnees, colorKeys[j] + (i + 1), value);
					}
				}
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}
			// création de la connection
			HttpURLConnection conn = OGSConnection.getConnection(jwsURL, donnees);

			// lecture de la réponse
			value = conn.getResponseMessage();
			if (value.indexOf("OK") >= 0) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),
						"ISO-8859-1"));
				String ligne;
				while ((ligne = reader.readLine()) != null) {
					if (ligne.indexOf("ERREUR") >= 0 || ligne.indexOf("error") >= 0
							|| ligne.indexOf("MDP") >= 0) {
						reponse.append(ligne);
						reponse.append("\n");
					}
				}
			} else
				reponse.append("ERREUR: ").append(value);
		} catch (IOException ioe) {
			if (OGSConnection.errorConnection())
				OGSConnection.saveConfig(mdp);
			return;
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}

		if (reponse.indexOf("ERREUR") >= 0 || reponse.indexOf("error") >= 0) {
			Main.Print_exception(reponse.insert(0, "Save configuration impossible:\n\n")
					.toString());
		} else if (reponse.indexOf("MDP") >= 0) {
			saveConfig(true);
		}

		return;
	}

	static String[] getModelList(boolean mdp) {
		String[] list = null;
		StringBuffer buf = new StringBuffer();
		StringBuffer donnees = new StringBuffer();
		StringBuffer reponse = new StringBuffer();
		Configuration config;
		String jwsURL, login, value;
		HttpURLConnection conn = null;
		BufferedReader reader = null;

		try {
			config = new Configuration("config.ini");
			jwsURL = config.getConfig("jwsURL");
			login = config.getConfig("login");
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
			return null;
		}

		if (login.equals("annonymous"))
			return null;

		if (mdp) {
			password = OGSConnection.getPassWord();
			if (password == null) {
				return null;
			}
		}

		try {
			OGSConnection.addData(donnees, "action", "get_models_list");
			OGSConnection.addData(donnees, "login", login);
			if (password != null) {
				OGSConnection.addData(donnees, "pass", password);
			}

			// création de la connection
			conn = OGSConnection.getConnection(jwsURL, donnees);

		} catch (IOException ioe) {
			if (OGSConnection.errorConnection())
				list = OGSConnection.getModelList(mdp);
			return list;
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
			return null;
		}

		// lecture de la réponse
		try {
			value = conn.getResponseMessage();
			if (value.indexOf("OK") >= 0) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),
						"ISO-8859-1"));
				String ligne;
				while ((ligne = reader.readLine()) != null) {
					if (ligne.indexOf("ERREUR") >= 0 || ligne.indexOf("error") >= 0
							|| ligne.indexOf("MDP") >= 0) {
						reponse.append(ligne);
						reponse.append("\n");
					} else if (ligne.startsWith("model=")) {
						buf.append("-> ");
						buf.append(ligne.substring(6));
						buf.append("\n");
					}
				}
			} else
				reponse.append("ERREUR: ").append(value);
		} catch (IOException ioe) {
			if (OGSConnection.errorConnection())
				list = OGSConnection.getModelList(mdp);
			return list;
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}

		if (reponse.indexOf("ERREUR") >= 0 || reponse.indexOf("error") >= 0) {
			Main.Print_exception(reponse.insert(0, "Load Models list impossible:\n\n")
					.toString());
		} else if (reponse.indexOf("MDP") >= 0) {
			list = OGSConnection.getModelList(true);
		} else if (buf.toString().trim().length() > 0) {
			list = buf.toString().split("\\n");
		}

		return list;
	}

	static String getModel(String model, String charset, boolean mdp) {
		String mdl = null;
		StringBuffer buf = new StringBuffer();
		StringBuffer donnees = new StringBuffer();
		StringBuffer reponse = new StringBuffer();
		Configuration config;
		String jwsURL, login, value;
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		InputStreamReader is = null;
		boolean ok = false;
		if (charset == null) {
			charset = Main.defaultCharset;
		}

		try {
			config = new Configuration("config.ini");
			jwsURL = config.getConfig("jwsURL");
			login = config.getConfig("login");
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
			return null;
		}

		if (login.equals("annonymous"))
			return null;

		if (mdp) {
			password = OGSConnection.getPassWord();
			if (password == null) {
				return null;
			}
		}

		try {
			OGSConnection.addData(donnees, "action", "get_model");
			OGSConnection.addData(donnees, "model_name", model);
			OGSConnection.addData(donnees, "login", login);
			if (password != null) {
				OGSConnection.addData(donnees, "pass", password);
			}

			// création de la connection
			conn = OGSConnection.getConnection(jwsURL, donnees);

		} catch (IOException ioe) {
			if (OGSConnection.errorConnection())
				mdl = OGSConnection.getModel(model, charset, mdp);
			return mdl;
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
			return null;
		}

		// lecture de la réponse
		try {
			value = conn.getResponseMessage();
			if (value.indexOf("OK") >= 0) {
				is = new InputStreamReader(conn.getInputStream(), charset);
				reader = new BufferedReader(is);
				String ligne;
				while ((ligne = reader.readLine()) != null) {

					if (ligne.indexOf("ERREUR") >= 0 || ligne.indexOf("error") >= 0
							|| ligne.indexOf("MDP") >= 0) {
						reponse.append(ligne);
						reponse.append("\n");
					} else if (ligne.indexOf("[{GET_MODEL}]") >= 0) {
						ok = true;
					} else if (ligne.indexOf("[{/GET_MODEL}]") >= 0) {
						ok = false;
					} else if (ok) {
						buf.append(ligne);
						buf.append("\n");
					}
				}
			} else
				reponse.append("ERREUR: ").append(value);
		} catch (IOException ioe) {
			if (OGSConnection.errorConnection())
				mdl = OGSConnection.getModel(model, charset, mdp);
			return mdl;
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}

		if (reponse.indexOf("ERREUR") >= 0 || reponse.indexOf("error") >= 0) {
			Main.Print_exception(reponse.insert(0, "Load Models impossible:\n\n").toString());
		} else if (reponse.indexOf("MDP") >= 0) {
			mdl = OGSConnection.getModel(model, charset, true);
		} else if (buf.toString().trim().length() > 0) {
			mdl = buf.toString();
		}

		return mdl;
	}

	static void addData(StringBuffer datas, String key, String value) throws Exception {

		if (datas.length() > 0) {
			datas.append("&");
		}

		datas.append(URLEncoder.encode(key, "ISO-8859-1"));
		datas.append("=");
		datas.append(URLEncoder.encode(value, "ISO-8859-1"));
	}

	static void addData(StringBuffer datas, String key, String[] values) throws Exception {
		int i;

		for (i = 0; i < values.length; i++)
			OGSConnection.addData(datas, key + i, values[i]);
	}

	static void addData(StringBuffer datas, String key, long[] values) throws Exception {
		int i;

		for (i = 0; i < values.length; i++)
			addData(datas, key + i, Main.formatnumber(values[i]));
	}

	static HttpURLConnection getConnection(String url, StringBuffer datas) throws Exception {
		OutputStreamWriter writer;
		HttpURLConnection conn;
		URL theURL;

		// création de la connection
		theURL = new URL(url);
		conn = (HttpURLConnection) theURL.openConnection();
		conn.setRequestProperty("User-Agent", "OGSConverter Stats");
		conn.setDoOutput(true);

		// envoi de la requête
		writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(datas.toString());
		writer.flush();

		writer.close();

		return conn;
	}

	static String getPassWord() {
		String password = JOptionPane.showInputDialog(new JFrame(), "password: ", null);
		return password;
	}

	static long sendDataStats(String ogsc_stats_url, RCDatas rcDatas) {
		HttpURLConnection conn = null;
		StringBuffer donnees = new StringBuffer(), reponse = new StringBuffer();
		Map[] tmpMap;
		String value;
		int i, j, l;
		long ret = -1;
		BufferedReader reader = null;
		Configuration configC = null;

		try {
			configC = new Configuration("config.ini");

			OGSConnection.addData(donnees, "action", "rcstats");
			addData(donnees, "lang", configC.getConfig("active_language"));
			addData(donnees, "attacker", rcDatas.get_attacker());
			addData(donnees, "defender", rcDatas.get_defender());
			addData(donnees, "captured_metal", rcDatas.get_captured_metal());
			addData(donnees, "captured_cristal", rcDatas.get_captured_cristal());
			addData(donnees, "captured_deuterium",
					rcDatas.get_captured_deuterium());
			addData(donnees, "date", rcDatas.get_date());
			addData(donnees, "attacker_coordinates",
					rcDatas.get_attacker_coordinates());
			addData(donnees, "defender_coordinates",
					rcDatas.get_defender_coordinates());
			addData(donnees, "harvest_cristal", rcDatas.get_harvest_cristal());
			addData(donnees, "harvest_metal", rcDatas.get_harvest_metal());
			addData(donnees, "round", Main.formatnumber(rcDatas.getRound()));
			addData(donnees, "att_perte_par_ress",
					rcDatas.getAtt_perte_par_ress());
			addData(donnees, "def_def_perte_par_ress",
					rcDatas.getDef_def_perte_par_ress());
			addData(donnees, "def_flt_perte_par_ress",
					rcDatas.getDef_flt_perte_par_ress());
			addData(donnees, "iamatt", configC.getConfig("iamatt"));
			addData(donnees, "consommation",
					Main.formatnumber(rcDatas.getConsumption()));
			addData(donnees, "att_techno0_", rcDatas.get_attacker_weapons());
			addData(donnees, "att_techno1_", rcDatas.get_attacker_shielding());
			addData(donnees, "att_techno2_", rcDatas.get_attacker_armour());
			addData(donnees, "def_techno0_", rcDatas.get_defender_weapons());
			addData(donnees, "def_techno1_", rcDatas.get_defender_shielding());
			addData(donnees, "def_techno2_", rcDatas.get_defender_armour());
			addData(donnees, "harvested_metal",
					Main.formatnumber(rcDatas.getHarvestedMetal()));
			addData(donnees, "harvested_cristal",
					Main.formatnumber(rcDatas.getHarvestedCrystal()));
			OGSConnection.addData(donnees, "universe", "1");

			// flotte
			tmpMap = rcDatas.get_attacker_fleet_start();
			for (i = 0; i < 14; i++) {
				l = tmpMap.length;
				for (j = 0; j < l; j++) {
					if (tmpMap[j].containsKey(new Integer(i))
							&& tmpMap[j].get(new Integer(i)) != null)
						OGSConnection.addData(donnees, "attacker_fleet_start_" + j + "_" + i,
								(String) tmpMap[j].get(new Integer(i)));
					else
						OGSConnection.addData(donnees, "attacker_fleet_start_" + j + "_" + i,
								"0");
				}
			}
			tmpMap = rcDatas.get_defender_fleet_start();
			for (i = 0; i < 14; i++) {
				l = tmpMap.length;
				for (j = 0; j < l; j++) {
					if (tmpMap[j].containsKey(new Integer(i))
							&& tmpMap[j].get(new Integer(i)) != null)
						OGSConnection.addData(donnees, "defender_fleet_start_" + j + "_" + i,
								(String) tmpMap[j].get(new Integer(i)));
					else
						OGSConnection.addData(donnees, "defender_fleet_start_" + j + "_" + i,
								"0");
				}
			}
			tmpMap = rcDatas.get_attacker_fleet_end();
			for (i = 0; i < 14; i++) {
				l = tmpMap.length;
				for (j = 0; j < l; j++) {
					if (tmpMap[j].containsKey(new Integer(i))
							&& tmpMap[j].get(new Integer(i)) != null)
						OGSConnection.addData(donnees, "attacker_fleet_end_" + j + "_" + i,
								(String) tmpMap[j].get(new Integer(i)));
					else
						OGSConnection.addData(donnees, "attacker_fleet_end_" + j + "_" + i,
								"0");
				}
			}
			tmpMap = rcDatas.get_defender_fleet_end();
			for (i = 0; i < 14; i++) {
				l = tmpMap.length;
				for (j = 0; j < l; j++) {
					if (tmpMap[j].containsKey(new Integer(i))
							&& tmpMap[j].get(new Integer(i)) != null)
						OGSConnection.addData(donnees, "defender_fleet_end_" + j + "_" + i,
								(String) tmpMap[j].get(new Integer(i)));
					else
						OGSConnection.addData(donnees, "defender_fleet_end_" + j + "_" + i,
								"0");
				}
			}

			// défenses
			tmpMap = rcDatas.get_defender_defense_start();
			for (i = 0; i < 8; i++) {
				l = tmpMap.length;
				for (j = 0; j < l; j++) {
					if (tmpMap[j].containsKey(new Integer(i))
							&& tmpMap[j].get(new Integer(i)) != null)
						OGSConnection.addData(donnees,
								"defender_defense_start_" + j + "_" + i,
								(String) tmpMap[j].get(new Integer(i)));
					else
						OGSConnection.addData(donnees,
								"defender_defense_start_" + j + "_" + i, "0");
				}
			}
			tmpMap = rcDatas.get_defender_defense_end();
			for (i = 0; i < 8; i++) {
				l = tmpMap.length;
				for (j = 0; j < l; j++) {
					if (tmpMap[j].containsKey(new Integer(i))
							&& tmpMap[j].get(new Integer(i)) != null)
						OGSConnection.addData(donnees, "defender_defense_end_" + j + "_" + i,
								(String) tmpMap[j].get(new Integer(i)));
					else
						OGSConnection.addData(donnees, "defender_defense_end_" + j + "_" + i,
								"0");
				}
			}

			// création de la connection
			conn = OGSConnection.getConnection(ogsc_stats_url, donnees);

		} catch (IOException ioe) {
			ret = -1;
			if (OGSConnection.errorConnection())
				ret = OGSConnection.sendDataStats(ogsc_stats_url, rcDatas);
			return ret;
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
			return -1;
		}

		// lecture de la réponse
		try {
			value = conn.getResponseMessage();
			if (value.indexOf("OK") >= 0) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),
						"ISO-8859-1"));
				String ligne;
				while ((ligne = reader.readLine()) != null) {
					reponse.append(ligne).append('\n');
				}
			} else {
				ret = -1;
				if (OGSConnection.errorConnection())
					ret = OGSConnection.sendDataStats(ogsc_stats_url, rcDatas);
				return ret;
			}

			reponse.deleteCharAt(reponse.length() - 1);

			if (!reponse.toString().matches("^\\d+$")) {
				Main.Print_exception(reponse.toString());
				return -1;
			}

			ret = Main.exptoint(reponse.toString());

		} catch (IOException ioe) {
			ret = -1;
			if (OGSConnection.errorConnection())
				ret = OGSConnection.sendDataStats(ogsc_stats_url, rcDatas);
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}

		return ret;
	}

	static boolean errorConnection() {
		boolean retry = false;
		int rep;

		rep = JOptionPane.showConfirmDialog(Main.getParentFrame(),
				"Connexion error!\n\nDo you want retry?", "Connexion error",
				JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if (rep == JOptionPane.YES_OPTION)
			retry = true;

		return retry;
	}
}
