/*
 * Help.java
 *
 * Created on 17 avril 2006, 08:21
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package ogsconverter;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 
 * @author MC-20-EG-2003
 */
public class Help extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of Help */
	public Help(int part) {
		super();
		setTitle("OGSConverter");
		setDefaultCloseOperation(0);
		setIconImage(new ImageIcon(this.getClass().getResource("images/icone.png")).getImage());// new
																								// ImageIcon("images/icone.png").getImage());
		setSize(470, 280);
		setLocationRelativeTo(getParent());

		JPanel p = new JPanel();
		BorderLayout b = new BorderLayout();
		p.setLayout(b);

		help = new JButton("Aide en ligne");

		switch (part) {
		case 0:
			p.add(about(), BorderLayout.CENTER);
			break;
		case 1:
			p.add(help(), BorderLayout.CENTER);
			p.add(help, BorderLayout.SOUTH);
			help.addActionListener(this);
			break;
		default:
			p.add(about(), BorderLayout.CENTER);
		}

		ImagePanel i = new ImagePanel(new ImageIcon(this.getClass().getResource(
				"images/logo.jpg")).getImage());// Toolkit.getDefaultToolkit().getImage("images/logo.jpg"));
		JPanel imgPan = new JPanel();
		imgPan.setLayout(new FlowLayout());
		imgPan.add(i);
		p.add(imgPan, BorderLayout.NORTH);

		getContentPane().add(p);
		setVisible(true);
	}

	private JLabel about() {
		JLabel l = null;
		try {
			l = new JLabel(
					"<html><h2 style='text-align=\\'center\\';'>About OGSConverter version "
							+ Configuration.getConfig("config.ini", "v")
							+ ":</h2>"
							+ "<center><p>OGSConverter est un outil vous permettant de personnaliser offline, sur votre PC"
							+ " tous vos rapports (espionnage, recyclage, bataille, page membres de l'alliance et votre empire) issus de Ogame. De nombreuses options sont configurables,"
							+ " et sont enregistrées pour ne pas avoir besoins de les redéfinir à chaque fois et deux configurations prédéfinis sont à votre disposition.</p>"
							+ "<br><i>OGSConverter is a OGSTeam Software © 2006<br>by ben.12</i></center>"
							+ "</html>");
		} catch (Exception e) {
			ExceptionAlert.createExceptionAlert(e);
			e.printStackTrace();
		}
		return l;
	}

	private JScrollPane help() {
		JScrollPane scrollpane = new JScrollPane();
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JTextArea t = new JTextArea(
				"Help!\n\nAide en ligne: http://ogsteam.fr/\n\nI. Rapports compatibles:\n\n - Rapport de Bataille (seul, plusieurs avec ou sans recyclage).\n"
						+ " - Rapport de Recyclage.\n - Rapport d'Espionnage.\n - Page membres de l'alliance.\n - Votre empire.\n\n\n"
						+ "II. Comment faire la conversion:\n\nJe copie le rapport dans l'onglet converter et \n"
						+ "je vais cliquer sur le bouton \"convertir\"\n\n\n III. Config:\n\n"
						+ " - Le \"!n\" représente le nombre de rounds de la bataille dans le texte intermédiaire du rapport.\n"
						+ " - Vous pouvez choisir les balises BBCodes pour l'adapter aux forums.\n"
						+ " - Vous pouvez ajouter ou supprimer les balises BBCodes, \"$1\" pour remplace la taille pour size et le code couleur pour la couleur...\n"
						+ "     ATTENTION avant de supprimer une balise BBCode, vérifiez qu'elle n'est pas utilisé dans un modèle de forum.\n"
						+ " - La validation des différents textes (name, coord, ...) est automatique.");
		t.setEditable(false);
		t.setLineWrap(true);
		t.setWrapStyleWord(true);
		scrollpane.setViewportView(t);
		return scrollpane;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(help)) {
			Main.openurl("http://ogsteam.fr/");
		}
	}

	public static int ABOUT = 0;
	public static int HELP = 1;

	public JButton help;
}
