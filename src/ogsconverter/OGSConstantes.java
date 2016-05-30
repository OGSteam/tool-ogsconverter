package ogsconverter;

public interface OGSConstantes {

	// Couleur fond foncé
	static String darkcolor[] = { "00CC00", "FF6666", "FF9900", "00FF00", "33FF99", "FF00FF",
			"00FFFF", "FFCC00", "FFFF00", "0099FF", "EEC273", "FF0099", "00FF99", "00B0B0",
			"B000B0", "FFFF90", "A0B0FF", "A0FF99", "FF99A0", "99FFA0", "99A0FF", "9900FF",
			"FFCC99", "FFCC99", "FFCC00", "FF0000", "1F273C" };

	// Couleur fond claire
	static String lightcolor[] = { "00CC00", "FF0000", "AA3300", "116600", "995500", "AA0000",
			"446633", "0000BB", "007799", "331100", "000044", "442200", "330044", "443300",
			"AA0000", "0070A0", "004477", "004411", "0000AA", "002233", "330000", "002200",
			"002255", "002255", "0055AA", "FF0000", "F0F0F0" };

	static String ogsc_stats_url = "http://www.ogsconverter.com/modules/battle_stats/";

	static String msk_nb_formated = "-?\\d+(?:[\\.,']\\d{3})*";

	/**
	 * OGSConverter URL
	 */
	// static String ogsc_url =
	// "http://www.ogsteam.fr/forums/forum-17-ogsconverter";

	/*
	 * Masques sur les rapports de combats
	 * 
	 * msk_date msk_name_coord1 msk_name_coord2 msk_technology msk_nb_line
	 * msk_gain msk_moon_prob
	 * 
	 */

	/**
	 * date et heure
	 */
	static String msk_date = "^.+\\s(\\d{2,4}\\-.*?:\\d{1,2})\\s.*$";
	static String msk_date_only = "\\s(\\d{2,4}\\-.*?:\\d{1,2})\\s";

	/**
	 * "player [x:xx:xx]" compatible IE et version dragosim:
	 */
	static String msk_name_coord = "(.*?\\(\\[?[\\s\\S]*?\\]?\\)|\\s+\\d)";
	static String msk_name_coord1 = msk_name_coord + ".*$"; /*
															 * nom + coordonnées +
															 * éventuelle ligne
															 * suivante (IE)
															 */
	static String msk_name_coord2 = "(.*?)(?:\\(\\[?(.*?)\\]?\\)|\\d(\\s*))$";

	/**
	 * technologies
	 */
	static String msk_technology = "\\D+(" + msk_nb_formated + ")%\\D+(" + msk_nb_formated
			+ ")%\\D+(" + msk_nb_formated + ")%\\D*$";

	/**
	 * nombre de vaisseaux/défences
	 */
	static String msk_nb_line = "^(?:\\D+\\s+)+((?:" + msk_nb_formated + "\\s*)+)$";

	/**
	 * ligne de gain "Il emporte ..."
	 */
	static String msk_gain = "^\\D*(" + msk_nb_formated + ")\\D+(" + msk_nb_formated
			+ ")\\D+(" + msk_nb_formated + ")\\D*$";

	/**
	 * probabilité de création de lune
	 */
	static String msk_moon_prob = "^[^%]*?\\s(\\d+)\\s?%.*$";

	/*
	 * Masque sur les rapports de recyclages
	 * 
	 * msk_user_msg
	 * 
	 */

	static String msk_user_msg = "^(?i)(\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s+\\w+\\s+(.*?)(?:\\s+\\[(\\d{1,2}:\\d{1,3}:\\d{1,2})\\])?$";

	/*
	 * Masques sur les rapports d'espionnage
	 * 
	 * msk_name_coord_date msk_is_value
	 * 
	 */

	/**
	 * Ligne avec le Nom de planet + coordonnées + date
	 */
	static String msk_name_coord_date = 	"(.*?)\\[(.*?)\\](.+)(\\d{2}\\-.*?:\\d{1,2})(\\D*?)$";

	/**
	 * New version:
	 */
	static String msk_name_coord_date_new = "(.*?)\\[(.*?)\\]\\s+(\\([^']+?'[^']*?'\\))(?:(.*?)(\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}))?(\\D*?)?$";
	
	/**
	 * Ligne contenant une valeur
	 */
	static String msk_is_value = "^\\D+\\s+" + msk_nb_formated + "(\\s+\\D+\\s+"
			+ msk_nb_formated + ")?\\s*$";

	/*
	 * Masques sur la liste des membres
	 * 
	 * msk_legende msk_data_member
	 * 
	 */

	/**
	 * Ligne de la legende des colonnes
	 */
	static String msk_legende = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*.*";

	/**
	 * Données sur un membre
	 */
	static String msk_data_member = "^(\\d+)\\s+((?:\\s?\\S+)+)\\s\\s+((?:\\s?\\D+)+)\\s+("
			+ msk_nb_formated
			+ ")\\s+\\[(\\d{1,2}:\\d{1,3}:\\d{1,2})\\]\\s+(\\S+\\s\\S+)\\s?.*$";

	/**
	 * Données sur la date pour savoir s'il s'agit d'une copie Ogame
	 */
	static String mk_members_date = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\s+\\d";

	/*
	 * Masques sur l'empire
	 * 
	 * msk_emp_coord msk_emp_data msk_emp_fields msk_emp_goods1 msk_emp_goods2
	 * 
	 */

	/**
	 * Coordonnées
	 */
	static String msk_emp_coord = "^\\S+\\s+(\\s*\\[\\d{1,2}:\\d{1,3}:\\d{1,2}\\])+";

	/**
	 * données avec constructions et constructions en attentes
	 */
	static String msk_emp_data = "(" + msk_nb_formated + ")(?:\\s" + msk_nb_formated
			+ "|\\s\\(" + msk_nb_formated + "\\))*";

	/**
	 * Cases
	 */
	static String msk_emp_fields = "^\\S+\\s+(\\s*" + msk_nb_formated + "\\/\\d+)+";

	/**
	 * ressources
	 */
	static String msk_emp_goods1 = "^(\\S+)\\s+(\\s*" + msk_nb_formated + "\\s/\\s"
			+ msk_nb_formated + ")+";
	static String msk_emp_goods2 = msk_nb_formated + "\\s/\\s" + msk_nb_formated;

	/*
	 * Masques sur la vue flotte
	 * 
	 * msk_flotte_top msk_flotte_start msk_flotte_data
	 * 
	 */

	/**
	 * Détection de la copy de la flotte. [fleet] est à replacer par le maske
	 * des noms complets des vaisseaux.
	 */
	static String msk_flotte_top = "(?m)(?u)(?i)^[^\\-]+?(?:\\s+\\-){2}\\s*^([fleet])\\s+"
			+ msk_nb_formated + "\\s+[\\S\\s]+?^";

	/**
	 * Détection de la ligne précédent une liste de vaisseaux
	 */
	static String msk_flotte_start = "(?u)^[^\\-]+?(?:\\s+\\-){2}\\s*$";

	/**
	 * Récupération des nombres de vaiseaux.
	 */
	static String msk_flotte_data = "(?i)(?u)^([\\S\\s]+?)\\s+(" + msk_nb_formated
			+ ")\\s+[\\S\\s]+?$";
}
