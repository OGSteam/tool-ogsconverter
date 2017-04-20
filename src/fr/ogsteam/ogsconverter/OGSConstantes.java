package fr.ogsteam.ogsconverter;

public interface OGSConstantes {

    // Couleur fond foncé
    String DARKCOLOR[] = {"00CC00", "FF6666", "FF9900", "00FF00", "33FF99", "FF00FF",
            "00FFFF", "FFCC00", "FFFF00", "0099FF", "EEC273", "FF0099", "00FF99", "00B0B0",
            "B000B0", "FFFF90", "A0B0FF", "A0FF99", "FF99A0", "99FFA0", "99A0FF", "9900FF",
            "FFCC99", "FFCC99", "FFCC00", "FF0000", "1F273C"};

    // Couleur fond claire
    String LIGHTCOLOR[] = {"00CC00", "FF0000", "AA3300", "116600", "995500", "AA0000",
            "446633", "0000BB", "007799", "331100", "000044", "442200", "330044", "443300",
            "AA0000", "0070A0", "004477", "004411", "0000AA", "002233", "330000", "002200",
            "002255", "002255", "0055AA", "FF0000", "F0F0F0"};

    String OGSC_STATS_URL = "http://www.ogsconverter.com/modules/battle_stats/";

    String MSK_NB_FORMATED = "-?\\d+(?:[\\.,']\\d{3})*";

    /**
     * OGSConverter URL
     */
    // static String ogsc_url =
    // "http://www.ogsteam.fr/forums/forum-17-ogsconverter";

	/*
     * Masques sur les rapports de combats
	 * 
	 * MSK_DATE MSK_NAME_COORD_1 MSK_NAME_COORD_2 MSK_TECHNOLOGY MSK_NB_LINE
	 * MSK_GAIN MSK_MOON_PROB
	 * 
	 */

    /*** date et heure */
    String MSK_DATE = "^.+\\s(\\d{2,4}\\-.*?:\\d{1,2})\\s.*$";
    String MSK_DATE_ONLY = "\\s(\\d{2,4}\\-.*?:\\d{1,2})\\s";

    /*** "player [x:xx:xx]" compatible IE et version dragosim: */
    String MSK_NAME_COORD = "(.*?\\(\\[?[\\s\\S]*?\\]?\\)|\\s+\\d)";
    String MSK_NAME_COORD_1 = MSK_NAME_COORD + ".*$";
    /**
     * nom + coordonnées + éventuelle ligne suivante (IE)
     */
    String MSK_NAME_COORD_2 = "(.*?)(?:\\(\\[?(.*?)\\]?\\)|\\d(\\s*))$";

    /*** technologies */
    String MSK_TECHNOLOGY = "\\D+(" + MSK_NB_FORMATED + ")%\\D+(" + MSK_NB_FORMATED + ")%\\D+(" + MSK_NB_FORMATED + ")%\\D*$";

    /*** nombre de vaisseaux/défences */
    String MSK_NB_LINE = "^(?:\\D+\\s+)+((?:" + MSK_NB_FORMATED + "\\s*)+)$";

    /*** ligne de gain "Il emporte ..."*/
    String MSK_GAIN = "^\\D*(" + MSK_NB_FORMATED + ")\\D+(" + MSK_NB_FORMATED + ")\\D+(" + MSK_NB_FORMATED + ")\\D*$";

    /*** probabilité de création de lune */
    String MSK_MOON_PROB = "^[^%]*?\\s(\\d+)\\s?%.*$";

    /*******************************************************************************************************************
     * Masque sur les rapports de recyclages
     *******************************************************************************************************************
     * MSK_USER_MSG
     *******************************************************************************************************************
     */

    String MSK_USER_MSG = "^(?i)(\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2})\\s+\\w+\\s+(.*?)(?:\\s+\\[(\\d{1,2}:\\d{1,3}:\\d{1,2})\\])?$";

    /*******************************************************************************************************************
     * Masques sur les rapports d'espionnage
     *******************************************************************************************************************
	 * MSK_NAME_COORD_DATE MSK_IS_VALUE
     *******************************************************************************************************************
	 */

    /*** Ligne avec le Nom de planet + coordonnées + date */
    String MSK_NAME_COORD_DATE = "(.*?)\\[(.*?)\\](.+)(\\d{2}\\-.*?:\\d{1,2})(\\D*?)$";

    /*** New version: */
    String MSK_NAME_COORD_DATE_NEW = "(.*?)\\[(.*?)\\]\\s+(\\([^']+?'[^']*?'\\))(?:(.*?)(\\d{2}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}))?(\\D*?)?$";

    /*** Ligne contenant une valeur */
    String MSK_IS_VALUE = "^\\D+\\s+" + MSK_NB_FORMATED + "(\\s+\\D+\\s+" + MSK_NB_FORMATED + ")?\\s*$";

	/*******************************************************************************************************************
	 * Masques sur la liste des membres
	 *******************************************************************************************************************
	 * MSK_LEGENDE MSK_DATA_MEMBER
	 *******************************************************************************************************************
	 */

    // Ligne de la legende des colonnes
    String MSK_LEGENDE = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*.*";

    // Données sur un membre
    String MSK_DATA_MEMBER = "^(\\d+)\\s+((?:\\s?\\S+)+)\\s\\s+((?:\\s?\\D+)+)\\s+("+ MSK_NB_FORMATED + ")\\s+\\[(\\d{1,2}:\\d{1,3}:\\d{1,2})\\]\\s+(\\S+\\s\\S+)\\s?.*$";

    /**
     * Données sur la date pour savoir s'il s'agit d'une copie Ogame
     */
    String MK_MEMBERS_DATE = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}\\s+\\d";

	/*
	 * Masques sur l'empire
	 * 
	 * MSK_EMP_COORD MSK_EMP_DATA MSK_EMP_FIELDS MSK_EMP_GOODS_1 MSK_EMP_GOODS_2
	 * 
	 */

    // Coordonnées
    String MSK_EMP_COORD = "^\\S+\\s+(\\s*\\[\\d{1,2}:\\d{1,3}:\\d{1,2}\\])+";

    // Données avec constructions et constructions en attentes */
    String MSK_EMP_DATA = "(" + MSK_NB_FORMATED + ")(?:\\s" + MSK_NB_FORMATED + "|\\s\\(" + MSK_NB_FORMATED + "\\))*";

    /*** Cases */
    String MSK_EMP_FIELDS = "^\\S+\\s+(\\s*" + MSK_NB_FORMATED + "\\/\\d+)+";

    /*** ressources */
    String MSK_EMP_GOODS_1 = "^(\\S+)\\s+(\\s*" + MSK_NB_FORMATED + "\\s/\\s" + MSK_NB_FORMATED + ")+";
    String MSK_EMP_GOODS_2 = MSK_NB_FORMATED + "\\s/\\s" + MSK_NB_FORMATED;

	/*
	 * Masques sur la vue flotte
	 * 
	 * MSK_FLOTTE_TOP MSK_FLOTTE_START MSK_FLOTTE_DATA
	 * 
	 */

    /**
     * Détection de la copy de la flotte. [fleet] est à replacer par le maske
     * des noms complets des vaisseaux.
     */
    String MSK_FLOTTE_TOP = "(?m)(?u)(?i)^[^\\-]+?(?:\\s+\\-){2}\\s*^([fleet])\\s+" + MSK_NB_FORMATED + "\\s+[\\S\\s]+?^";

    /**
     * Détection de la ligne précédent une liste de vaisseaux
     */
    String MSK_FLOTTE_START = "(?u)^[^\\-]+?(?:\\s+\\-){2}\\s*$";

    /**
     * Récupération des nombres de vaiseaux.
     */
    String MSK_FLOTTE_DATA = "(?i)(?u)^([\\S\\s]+?)\\s+(" + MSK_NB_FORMATED + ")\\s+[\\S\\s]+?$";
}
