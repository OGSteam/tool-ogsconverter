package fr.ogsteam.ogsconverter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.HttpURLConnection;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ExceptionAlert extends JDialog {

	private static final String REPORT_URL = "http://update.ogsconverter.com:80/report_bug.php";

	private ExceptionAlert(Exception e, final String report) {
		super(Main.getParentFrame());

		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.setTitle("Error!");
		this.setMinimumSize(new Dimension(400, 100));

		final Except ex = new Except(e);

		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());

		JPanel details = new JPanel();
		details.setLayout(new BorderLayout());

		JLabel message = new JLabel();

		String error = ex.getMessage();
		if (error.length() > 100) {
			error = error.substring(0, 97) + "...";
		}

		StringBuffer text = new StringBuffer();
		text.append("<html><b>Error! It may be a bug...</b><br/>");
		text.append("<br/>message: " + error);
		text.append("<br/></html>");

		message.setText(text.toString());

		content.add(message, BorderLayout.BEFORE_FIRST_LINE);

		final JButton detailsButton = new JButton("show details >>");
		JButton reportButton = new JButton("Report this bug");
		final JCheckBox reportAdded = new JCheckBox("Join report", true);
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		if (report != null)
			buttons.add(reportAdded);
		buttons.add(reportButton);
		buttons.add(detailsButton);

		details.add(buttons, BorderLayout.NORTH);

		JTextArea exception = new JTextArea();
		exception.setText(ex.ExceptoString());
		exception.setEditable(false);

		JScrollPane exceptionScroll = new JScrollPane(exception);
		exceptionScroll.setPreferredSize(new Dimension(0, 150));

		final JPanel detailsPanel = new JPanel();
		detailsPanel.setLayout(new BorderLayout());
		detailsPanel.add(exceptionScroll);
		detailsPanel.setVisible(false);

		details.add(detailsPanel, BorderLayout.CENTER);

		detailsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				detailsPanel.setVisible(!detailsPanel.isVisible());
				if (detailsPanel.isVisible())
					detailsButton.setText("hide details <<");
				else
					detailsButton.setText("show details >>");
				pack();
			}
		});

		reportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (reportAdded.isSelected())
					reportException(ex, report);
				else
					reportException(ex, null);
				dispose();
			}
		});

		content.add(details);

		this.setContentPane(content);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void reportException(Except ex, String report) {
		String comment = JOptionPane.showInputDialog(this, "Add a comment, please:",
				"Comments?", JOptionPane.PLAIN_MESSAGE);

		HttpURLConnection conn = null;
		StringBuffer donnees = new StringBuffer();

		try {
			Configuration config = new Configuration("config.ini");

			OGSConnection.addData(donnees, "comment", comment + "\n\nVersion:"
					+ config.getConfig("v"));
			OGSConnection.addData(donnees, "exception", ex.ExceptoString().trim());
			if (report != null)
				OGSConnection.addData(donnees, "report", report);

			conn = OGSConnection.getConnection(REPORT_URL, donnees);
			int rep = conn.getResponseCode();

			if (rep >= 400 || rep < 0) {
				System.out.println("Error, report not sended.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createExceptionAlert(Exception e) {
		new ExceptionAlert(e, null);
	}

	public static void createExceptionAlert(Exception e, String report) {
		new ExceptionAlert(e, report);
	}
}
