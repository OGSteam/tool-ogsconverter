/*
 * ===========================================================================
 * Copyright 2004 by Volker H. Simonis. All rights reserved.
 * ===========================================================================
 */
package ogsconverter.widgets;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ScrollableBar extends JComponent implements SwingConstants {
  
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
// BEGIN putUI
  static {
    UIManager.put("ScrollableBarUI", 
                  "ogsconverter.widgets.ScrollableBarUI");
  }
  // END putUI

  public ScrollableBar(Component comp) {
    this(comp, HORIZONTAL);
  }

  // BEGIN SBConstructor
  public ScrollableBar(Component comp, int orientation) {
    this.comp = comp;
    if (orientation == HORIZONTAL) {
      horizontal = true;
    }
    else {
      horizontal = false;
    }
    small = true; // Arrow size on scroll button.
    inc = 8;      // Scroll width in pixels.
    updateUI();
  }
  // END SBConstructor
  
  public void setOrientation(int orientation) {
	  if (orientation == HORIZONTAL) {
	      horizontal = true;
	    }
	    else {
	      horizontal = false;
	    }
	  
	  updateUI();
  }
  
  // BEGIN updateUI
  public String getUIClassID() {
    return "ScrollableBarUI";
  }

  public void updateUI() {
    setUI(UIManager.getUI(this));
    invalidate();
  }
  // END updateUI

  public Component getComponent() {
    return comp;
  }

  public void setComponent(Component comp) {
    if (this.comp != comp) {
      Component old = this.comp;
      this.comp = comp;
      firePropertyChange("component", old, comp);
    }
  }

  public int getIncrement() {
    return inc;
  }

  public void setIncrement(int inc) {
    if (inc > 0 && inc != this.inc) {
      int old = this.inc;
      this.inc = inc;
      firePropertyChange("increment", old, inc);
    }
  }

  public boolean isHorizontal() {
    return horizontal;
  }
  
  public boolean isSmallArrows() {
    return small;
  }

  public void setSmallArrows(boolean small) {
    if (small != this.small) {
      boolean old = this.small;
      this.small = small;
      firePropertyChange("smallArrows", old, small);
    }
  }

  private Component comp;
  private boolean horizontal, small;
  private int inc;
}
