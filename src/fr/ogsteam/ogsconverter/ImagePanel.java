/*
 * ImagePanel.java
 *
 * Created on 17 avril 2006, 08:45
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package fr.ogsteam.ogsconverter;

/**
 *
 * @author MC-20-EG-2003
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

public class ImagePanel extends JComponent {

   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Image bg;

   public ImagePanel(Image i) {
      super();
      bg=i;
      
      this.setPreferredSize(new Dimension(i.getWidth(this), i.getHeight(this)));
      this.setSize(i.getWidth(this), i.getHeight(this));
      
      this.setVisible(true);
      
   }

   public void paintComponent(Graphics g) {
      g.drawImage(bg, 0, 0, null);
   }
}
