/*
 * Configuration.java
 *
 * Created on 31 mars 2006, 23:23
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package ogsconverter;

/**
 * 
 * @author MOREAU Benoît
 */
// Les import
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Configuration {
	/**
	 * Methode : getConfig
	 * 
	 * Description : La méthode getConfig va retourner l'information de configuration désirée à
	 * partir d'un fichier de configuration
	 * 
	 * date : 8/05/2004
	 * 
	 * @param fichier
	 * @param key
	 * @return : String représentant la valeur de l'info
	 * @throws java.lang.Exception
	 */

	private String fichier;

	private Properties config = null;

	private static Properties config_file;

	static {
		if (Main.JWS) {
			InputStream fis = Main.USER_LOADER.getResourceAsStream("config.ini");
			config_file = new Properties();
			try {
				config_file.load(fis);
				fis.close();
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public Configuration() {
	}

	public Configuration(String fichier) throws Exception {
		InputStream fis;
		String leFichier = Main.USER_URL + File.separator + fichier;
		this.fichier = fichier;

		if (Main.JWS && fichier.equals("config.ini"))
			config = config_file;
		else {
			if (Main.JWS)
				fis = Main.USER_LOADER.getResourceAsStream(fichier);
			else
				fis = new FileInputStream(leFichier);

			config = new Properties();
			config.load(fis);
			fis.close();
		}

		fis = null;
		leFichier = null;
	}

	public String getConfig(String key) throws Exception {
		String tmp = config.getProperty(key);

		if (tmp == null) {
			// On leve une exeption s'il n'est pas non plus dans une traduction "sûr"
			if (fichier.startsWith("lang_")) {
				String altFile;
				if (!Main.JWS) {
					File f = new File("lang_en.ini");
					altFile = f.exists() ? "lang_en.ini" : "lang_fr.ini";
				} else {
					altFile = "lang_en.ini";
				}
				if (!fichier.equals(altFile))
					tmp = Configuration.getConfig(altFile, key);
			}

			if (tmp == null)
				throw new Exception("La valeur correspondant à '" + key
						+ "' n'existe pas dans le fichier '" + fichier + "'");
		}

		return tmp;
	}

	public void setConfig(String key, String valeur) throws Exception {
		String leFichier = Main.USER_URL + File.separator + fichier;
		FileOutputStream fos;

		config.setProperty(key, valeur);

		if (!Main.JWS) {
			fos = new FileOutputStream(leFichier);
			config.store(fos, "Dernière mise a jour :");
			fos.close();
		}

		fos = null;
		leFichier = null;
	}

	public void setConfig(String key, boolean valeur) throws Exception {
		setConfig(key, valeur ? "1" : "0");
	}

	public void setConfig(String key, int valeur) throws Exception {
		setConfig(key, Integer.toString(valeur));
	}

	public static String getConfig(String fichier, String key) throws Exception {
		Properties config;
		InputStream fis = null;

		// On construit l'adresse du fichier
		String leFichier = Main.USER_URL + File.separator + fichier;

		// On fait pointer notre Properties sur le fichier
		if (Main.JWS && !fichier.equals("config.ini")) {
			fis = Main.USER_LOADER.getResourceAsStream(fichier);
			config = new Properties();
			config.load(fis);
			fis.close();
		} else if (Main.JWS) {
			config = config_file;
		} else {
			fis = new FileInputStream(leFichier);
			config = new Properties();
			config.load(fis);
			fis.close();
		}

		String tmp = config.getProperty(key);

		// C'est important de mettre à null, le garbage collector
		// passe plus vite !
		leFichier = null;
		fis = null;
		config = null;

		if (tmp == null) {
			// On leve une exeption s'il n'est pas non plus dans une traduction "sûr"
			if (fichier.startsWith("lang_")) {
				String altFile;
				if (!Main.JWS) {
					File f = new File("lang_en.ini");
					altFile = f.exists() ? "lang_en.ini" : "lang_fr.ini";
				} else {
					altFile = "lang_en.ini";
				}
				if (!fichier.equals(altFile))
					tmp = Configuration.getConfig(altFile, key);
			}

			if (tmp == null)
				throw new Exception("La valeur correspondant à '" + key
						+ "' n'existe pas dans le fichier '" + fichier + "'");
		}

		return tmp;

	}

	/**
	 * Methode : setConfig
	 * 
	 * Description : La méthode setConfig va mettre à jour/ inserer l'information de configuration
	 * désirée à partir dans un fichier de configuration
	 * 
	 * date : 8/05/2004
	 * 
	 * @param fichier
	 * @param key
	 * @param valeur
	 * @throws java.lang.Exception
	 */
	public static void setConfig(String fichier, String key, String valeur) throws Exception {
		// La petite feinte : Il faur recharger entièrement le fichier
		// et le réecrire.

		InputStream fis;
		FileOutputStream fos;

		// On construit l'adresse du fichier
		String leFichier = Main.USER_URL + File.separator + fichier;

		Properties config;
		// On fait pointer notre Properties sur le fichier
		if (Main.JWS && !fichier.equals("config.ini")) {
			fis = Main.USER_LOADER.getResourceAsStream(fichier);
			config = new Properties();
			config.load(fis);
			fis.close();
		} else if (Main.JWS) {
			config = config_file;
		} else {
			fis = new FileInputStream(leFichier);
			config = new Properties();
			config.load(fis);
			fis.close();
		}

		config.setProperty(key, valeur);

		if (!Main.JWS) {
			fos = new FileOutputStream(leFichier);
			config.store(fos, "Dernière mise a jour :");
			fos.close();
		}
		// C'est important de mettre à null, le garbage collector
		// passe plus vite !

		leFichier = null;
		fos = null;
		fis = null;
		config = null;
	}

	public static Object delConfig(String fichier, String key) throws Exception {
		String leFichier = System.getProperty("user.dir") + File.separator + fichier;
		Object value = null;
		InputStream fis;
		FileOutputStream fos;
		Properties config;

		//		 On fait pointer notre Properties sur le fichier
		if (Main.JWS && !fichier.equals("config.ini")) {
			fis = Main.USER_LOADER.getResourceAsStream(fichier);
			config = new Properties();
			config.load(fis);
			fis.close();
		} else if (Main.JWS) {
			config = config_file;
		} else {
			fis = new FileInputStream(leFichier);
			config = new Properties();
			config.load(fis);
			fis.close();
		}

		value = config.remove(key);

		if (!Main.JWS) {
			fos = new FileOutputStream(leFichier);
			config.store(fos, "Dernière mise a jour :");
			fos.close();
		}

		fos = null;
		fis = null;
		leFichier = null;

		return value;
	}

	public Object delConfig(String key) throws Exception {
		String leFichier = System.getProperty("user.dir") + File.separator + fichier;
		Object value = null;
		FileOutputStream fos;

		value = config.remove(key);

		if (!Main.JWS) {
			fos = new FileOutputStream(leFichier);
			config.store(fos, "Dernière mise a jour :");
			fos.close();
		}

		fos = null;
		leFichier = null;

		return value;
	}

	/**
	 * 
	 * @param fichier
	 * @param key
	 * @param valeur
	 * @throws java.lang.Exception
	 */
	public static void setConfig(String fichier, String key, boolean valeur) throws Exception {
		String value;
		if (valeur)
			value = "1";
		else
			value = "0";

		setConfig(fichier, key, value);
	}

	/**
	 * 
	 * @param fichier
	 */
	public boolean fileisok(String fichier) {

		if (Main.JWS)
			return true;

		File file = new File(Main.USER_URL + File.separator + fichier);

		if (!file.isFile()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I don't find the file "
					+ fichier, "ERREUR", JOptionPane.NO_OPTION);
			return false;
		} else if (!file.canRead()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I cannot read " + fichier,
					"ERREUR", JOptionPane.NO_OPTION);
			return false;
		} else if (!file.canWrite()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I cannot write in "
					+ fichier, "ERREUR", JOptionPane.NO_OPTION);
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param fichier
	 */
	public boolean islangfile(String fichier) {

		if (Main.JWS)
			return true;

		File file = new File(Main.USER_URL + File.separator + fichier);

		if (!file.isFile()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I don't find the file "
					+ fichier, "ERREUR", JOptionPane.NO_OPTION);
			return false;
		} else if (!file.canRead()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I cannot read " + fichier,
					"ERREUR", JOptionPane.NO_OPTION);
			return false;
		}

		if (!fichier.endsWith(".ini")) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "\"" + fichier
					+ "\" is not a .ini", "ERREUR", JOptionPane.NO_OPTION);
			return false;
		}

		if (!fichier.startsWith("lang_")) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "\"" + fichier
					+ "\" is not a lang_xx.ini", "ERREUR", JOptionPane.NO_OPTION);
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param fichier
	 */
	public boolean ismodelfile(String fichier) {

		if (Main.JWS)
			return true;

		File file = new File(Main.USER_URL + File.separator + fichier);

		if (!file.isFile()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I don't find the file "
					+ fichier, "ERREUR", JOptionPane.NO_OPTION);
			return false;
		} else if (!file.canRead()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I cannot read " + fichier,
					"ERREUR", JOptionPane.NO_OPTION);
			return false;
		}

		if (!fichier.endsWith(".ogsc")) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "\"" + fichier
					+ "\" is not a .ogsc", "ERREUR", JOptionPane.NO_OPTION);
			return false;
		}

		return true;
	}

	public boolean ismodelfileEditable(String pathfile) {

		if (Main.JWS)
			return true;

		File file = new File(pathfile);

		if (!file.isFile()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I don't find the file "
					+ pathfile, "ERREUR", JOptionPane.NO_OPTION);
			return false;
		} else if (!file.canRead()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I cannot read " + pathfile,
					"ERREUR", JOptionPane.NO_OPTION);
			return false;
		} else if (!file.canWrite()) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "I cannot write in "
					+ pathfile, "ERREUR", JOptionPane.NO_OPTION);
			return false;
		}

		if (!pathfile.endsWith(".ogsc")) {
			JOptionPane.showMessageDialog(Main.getParentFrame(), "\"" + pathfile
					+ "\" is not a .ogsc", "ERREUR", JOptionPane.NO_OPTION);
			return false;
		}

		return true;
	}
}
