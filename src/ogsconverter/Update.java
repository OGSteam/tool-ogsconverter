package ogsconverter;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class Update {

	private String ZIP = "http://update.ogsconverter.com:80/OGSConverter_update/OGSConverter_update.zip";
	// private String ZIP =
	// "http://localhost/OGSConverter_update/OGSConverter_update.zip";

	private String ZIP_JAR = "http://update.ogsconverter.com:80/OGSConverter_update_jar/OGSConverter_update.zip";
	// private String ZIP_JAR =
	// "http://localhost/OGSConverter_update_jar/OGSConverter_update.zip";

	private String URL = "http://www.ogsteam.fr/forums/sujet-551-ogsconverter-telechargement";

	private JDialog dialog;

	private Object[] options = { "Automatic Updade", "Manual Update", "No !" };

	private JOptionPane op;

	public Update(String v, String changelog) {

		op = new JOptionPane("A new version is available (v" + v + ").\n" + changelog
				+ "\n\nDo you want update now?", JOptionPane.QUESTION_MESSAGE,
				JOptionPane.YES_NO_CANCEL_OPTION, null, options, options[0]);

		dialog = new JDialog(Main.getParentFrame(), "New Version !", true);
		dialog.setAlwaysOnTop(true);
		dialog.setContentPane(op);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
			}
		});
		op.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String prop = e.getPropertyName();

				if (dialog.isVisible() && (e.getSource() == op)
						&& (prop.equals(JOptionPane.VALUE_PROPERTY))) {
					dialog.setVisible(false);
				}
			}
		});
		dialog.pack();
		dialog.setLocationRelativeTo(dialog.getParent());

		dialog.setVisible(true);

		String n = (String) op.getValue();
		if (n.equals(options[0]))
			updateNow();
		else if (n.equals(options[1]))
			Main.openurl(URL);
	}

	/*
	 * public void show() { dialog.setVisible(true);
	 * 
	 * String n = (String) op.getValue(); if (n.equals(options[0])) updateNow();
	 * else if (n.equals(options[1])) Main.openurl(URL); }
	 */
	private void updateNow() {
		String zip_url;
		File f = new File("OGSConverter.exe");
		if (f.exists())
			zip_url = ZIP;
		else
			zip_url = ZIP_JAR;

		if (HTTPGetFile(zip_url)) {

			try {
				UnZip("OGSConverter_update.zip");

				f = new File("OGSConverter_update.zip");
				Thread.sleep(500);
				if (f.exists())
					f.delete();

			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}

			try {
				Runtime.getRuntime().exec("java -jar OGSUpdate.jar");
			} catch (Exception e) {
				ExceptionAlert.createExceptionAlert(e);
				e.printStackTrace();
			}
			System.exit(0);

		} else {
			JOptionPane.showMessageDialog(Main.getParentFrame(),
					"Update imposible, retry later please...", ":/",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private boolean HTTPGetFile(String HOST) {

		try {
			// .... et l'utilise pour initialiser une url
			URL racine = new URL(HOST);
			// appel la methode getFile sur l'url pour commencer a
			// recuperer le fichier
			getFile(racine);
		} catch (MalformedURLException e) {
			System.err.println(HOST + " : URL non comprise.");
			return false;
		} catch (IOException e) {
			System.err.println(e);
			return false;
		}
		// ces ligne sont entoure d'un bloc try au cas ou il y aurait des
		// exeption : par exemple si l'url n'es pas valide
		return true;
	}

	// commencement de get file avec comme argument l'url
	private void getFile(URL u) throws IOException {
		// ouvre la connection
		URLConnection uc = u.openConnection();
		// regarde quel type est le fichier a telecharger
		// String FileType = uc.getContentType();
		// recupere la taille du fichier .....
		int FileLenght = uc.getContentLength();
		// ....pour tester si c'est un fichier valide
		if (FileLenght == -1) {
			throw new IOException("Fichier non valide.");
		}
		// le bloc si dessous jusqu'a entree.close permet de copier le
		// fichier
		// bit par bit ,en utilisant un tableau ,sur l'ordinnateur dans le
		// repertoir local
		InputStream brut = uc.getInputStream();
		InputStream entree = new BufferedInputStream(brut);
		byte[] donnees = new byte[FileLenght];
		int BitRead = 0;
		int deplacement = 0;
		int p, pas = 1500;
		long time = System.currentTimeMillis(), tpas = 2000, readed = 0;
		DecimalFormat format = new DecimalFormat("0.000");
		this.setProgress(0);
		process("Téléchargement des fichiers...");
		System.out.println("Début de téléchargement.");
		while (deplacement < FileLenght) {
			pas = (donnees.length - deplacement < pas) ? (donnees.length - deplacement) : pas;
			BitRead = entree.read(donnees, deplacement, pas);
			// , donnees.length - deplacement);
			if (BitRead == -1)
				break;
			deplacement += BitRead;
			p = (int) Math.round(((double) deplacement / (double) FileLenght) * (double) 100);
			this.setProgress(p);
			readed += BitRead;
			if ((System.currentTimeMillis() - time) > tpas) {
				time = System.currentTimeMillis() - time;
				this.setSpeedValue(format.format((double) readed / (double) time) + " Ko/s");
				time = System.currentTimeMillis();
				readed = 0;
			}
		}
		// ferme le flux
		entree.close();
		// regarder si le fichier est valide
		if (deplacement != FileLenght) {
			throw new IOException("Nous n'avons lu que " + deplacement
					+ " octets au lieu des " + FileLenght + " attendus");
		}
		// recuperation de l'url pour une ....
		String FileName = u.getFile();
		// ....decortication de la chaine pour avoir le nom du fichier
		FileName = FileName.substring(FileName.lastIndexOf('/') + 1);
		// ouverture d'un flux de donnee pour ecrire le fichier
		FileOutputStream WritenFile = new FileOutputStream(FileName);
		// ecrit le fichier sur l'ordinnateur
		WritenFile.write(donnees);
		// vide le tampon au cas ou tout ne serai pas ecris
		WritenFile.flush();
		// ferme le flux
		WritenFile.close();

		this.setProgress(100);
		this.setSpeedValue("");

		System.out.println("Téléchargement terminé.");

	}

	private int progress = 0;

	private int getProcess() {
		return progress;
	}

	private void setProgress(int p) {
		progress = p;
	}

	private String speedValue = "";

	private String getSpeedValue() {
		return speedValue;
	}

	private void setSpeedValue(String s) {
		speedValue = s;
	}

	private void process(String titre) {

		Scroll scroller = new Scroll(titre);
		scroller.start();
	}

	private void UnZip(String fichier) {
		progress = 0;
		process("Extraction des fichiers...");
		System.out.println("Début de l'extraction des fichiers.");
		try {
			ZipEntry entry;
			int BUFFER = 2048;
			int count, p, i;
			long size = 0, unZipped = 0;
			byte data[];
			FileOutputStream restitution;
			File f;
			Configuration config = new Configuration("config.ini");
			String[] langs = config.getConfig("possible_language").split(",");

			ZipInputStream source = new ZipInputStream(new FileInputStream(fichier));

			while ((entry = source.getNextEntry()) != null) {
				size += entry.getSize();
			}

			source.close();

			source = new ZipInputStream(new FileInputStream(fichier));
			while ((entry = source.getNextEntry()) != null) {

				if (entry.getName().startsWith("lang_")) {
					for (i = 0; i < langs.length; i++) {
						if (entry.getName().endsWith(langs[i] + ".ini"))
							break;
					}
					if (i >= langs.length)
						continue;
				}

				System.out.println("Extraction : " + entry.getName());

				if (entry.isDirectory()) {
					f = new File(entry.getName());
					f.mkdir();
					continue;
				}

				restitution = new FileOutputStream(System.getProperty("user.dir")
						+ File.separator + entry.getName());
				data = new byte[BUFFER];
				while ((count = source.read(data, 0, BUFFER)) != -1) {
					restitution.write(data, 0, count);
					unZipped += count;
					p = (int) Math.round(((double) unZipped / (double) size) * (double) 100);
					this.setProgress(p);
				}
				restitution.close();
			}
			source.close();
			this.setProgress(100);
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
		System.out.println("Fin de l'extraction des fichiers.");
	}

	private class Scroll extends Thread {

		JProgressBar b;

		String titre;

		private Scroll(String titre) {
			this.titre = titre;
		}

		public void run() {
			// TODO Raccord de méthode auto-généré
			JDialog f = new JDialog(Main.getParentFrame());
			f.setTitle(titre);
			JProgressBar b = new JProgressBar();
			JLabel speed = new JLabel();
			f.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			b.setValue(0);
			b.setMaximum(100);
			b.setMinimum(0);
			b.setStringPainted(true);
			f.getContentPane().setLayout(new BorderLayout());
			f.getContentPane().add(b);
			f.getContentPane().add(speed, BorderLayout.NORTH);
			f.setSize(500, 70);
			f.setLocationRelativeTo(f.getParent());
			f.setVisible(true);
			while (progress < 100) {
				try {
					Thread.sleep(200);
					b.setValue(getProcess());
					speed.setText(getSpeedValue());
				} catch (Exception e) {
				}
				;
			}
			f.dispose();
			f = null;
		}
	}

}
