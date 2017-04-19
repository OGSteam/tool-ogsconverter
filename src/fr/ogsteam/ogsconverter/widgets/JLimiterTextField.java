package fr.ogsteam.ogsconverter.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;

/**
 * JTextField with characters and/or sequences forbidden.
 * 
 * @see #setDisallowed(String)
 * @author ben.12
 */
public class JLimiterTextField extends JTextField implements UndoableEditListener {

	String[] disallowed = new String[0];

	public JLimiterTextField() {
		this(null, null, 0);
	}

	public JLimiterTextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		setDocumentUndoableEditListener();
	}

	public JLimiterTextField(int columns) {
		this(null, null, columns);
	}

	public JLimiterTextField(String text, int columns) {
		this(null, text, columns);
	}

	public JLimiterTextField(String text) {
		this(null, text, 0);
	}

	public JLimiterTextField(String text, String disallowed) {
		this(null, text, 0);
		setDisallowed(disallowed);
	}

	private void setDocumentUndoableEditListener() {
		getDocument().addUndoableEditListener(this);
		this.addPropertyChangeListener("document", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				((Document) evt.getOldValue()).removeUndoableEditListener(JLimiterTextField.this);
				((Document) evt.getNewValue()).addUndoableEditListener(JLimiterTextField.this);
			}
		});
	}

	public void undoableEditHappened(UndoableEditEvent e) {
		for (int i = 0; i < disallowed.length; i++) {
			if (getText().indexOf(disallowed[i].toString()) >= 0) {
				if (e.getEdit().canUndo()) {
					e.getEdit().undo();
					// petit son
					UIManager.getLookAndFeel().provideErrorFeedback(this);
				}
			}
		}
	}

	/**
	 * Disallowed mask pattern:<br>
	 * <br>
	 * "abc" -> disallow characters "a", "b" and "c".<br>
	 * "ab(seq)" -> disallow characters "a", "b" and sequence "seq".<br>
	 * "ab\\(seq\\)" -> disallow characters "a", "b" , "(", "s", "e", "q" and
	 * ")".<br>
	 * 
	 * @param disallowed
	 *            string mask
	 */
	public void setDisallowed(String disallowed) {
		for (int i = 0; i < disallowed.length(); i++) {
			int index = this.disallowed.length;
			String[] seq = this.disallowed;
			this.disallowed = new String[index + 1];
			System.arraycopy(seq, 0, this.disallowed, 0, index);
			this.disallowed[index] = "";
			if (disallowed.charAt(i) == '(') {
				for (i++; i < disallowed.length() && disallowed.charAt(i) != ')'; i++) {
					if (disallowed.charAt(i) == '\\') {
						i++;
						if (i >= disallowed.length())
							break;
					}
					this.disallowed[index] += disallowed.charAt(i);
				}
				continue;
			}
			if (disallowed.charAt(i) == '\\') {
				i++;
				if (i >= disallowed.length())
					break;
			}
			this.disallowed[index] += disallowed.charAt(i);
		}
	}
}
