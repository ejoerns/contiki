/*
 * Copyright (c) 2014, TU Braunschweig.
 * Copyright (c) 2006, Swedish Institute of Computer Science.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the Institute nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE INSTITUTE AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE INSTITUTE OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */

package org.contikios.cooja.interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import org.contikios.cooja.*;
import org.jdom.Element;

/**
 * An LEDInterface represents a number of colored LEDs.
 *
 * LEDs that are turned off are depicted smaller and darker
 * than those that are turned on.
 *
 * @author Fredrik Osterlind
 * @author Enrico Jorns
 */
@ClassDescription("LEDs")
public abstract class LEDInterface extends MoteInterface {

  /**
   * Represents a single LED
   */
  public class LED {

    public Color color;
    public boolean on;

    public LED(Color color) {
      this.color = color;
    }
  }

  /**
   * @return True if any LEDInterface is on, false otherwise
   */
  public abstract boolean isAnyOn();
  
  /**
   * Returns an array of all LEDs of a mote.
   *
   * @return array of all LEDs
   */
  public abstract LED[] getLEDs();

  /**
   * Returns LED with given index.
   *
   * @param idx index
   * @return LED with given index
   */
  public abstract LED getLED(int idx);

  private static final int LED_DIAMETER = 25;
  private static final int LED_X_DISTANCE = 40;

  @Override
  public JPanel getInterfaceVisualizer() {
    final JPanel panel = new JPanel() {
      @Override
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
/*
        if (!probesInserted) {
            for (int i=0;i<leds.leds.length;i++) {
                leds.leds[i].getFSM().insertProbe(ledProbes[i]);
                ledOn[ledMap[i]] = leds.leds[i].getFSM().getCurrentState());
            }
            probesInserted = true;
        }
*/
        int x = 20;
        int y = 4;
        int d = LED_DIAMETER;
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                          RenderingHints.VALUE_ANTIALIAS_ON);
        for (LEDInterface.LED led : getLEDs()) {
          g.setColor(led.color);
          if (led.on) {
            g.fillOval(x, y, d, d);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, d, d);
          } else {
            g.fillOval(x + 4, y + 4, d - 8, d - 8);
            // semi-transparent gray overlay for switched-off LEDs
            g.setColor(new Color(0, 0, 0, 128));
            g.fillOval(x + 4, y + 4, d - 8, d - 8);
          }
          x += LED_X_DISTANCE;
        }
      }
    };

    Observer observer;
    this.addObserver(observer = new Observer() {
      @Override
      public void update(Observable obs, Object obj) {
        panel.repaint();
      }
    });

    // Saving observer reference for releaseInterfaceVisualizer
    panel.putClientProperty("intf_obs", observer);

    panel.setPreferredSize(new Dimension(140, 40));

    return panel;
  }

  @Override
  public void releaseInterfaceVisualizer(JPanel panel) {
    Observer observer = (Observer) panel.getClientProperty("intf_obs");
    if (observer == null) {
      //logger.fatal("Error when releasing panel, observer is null");
      return;
    }
    //leave probes inserted for cooja timeline
/*
    for (int i=0;i<leds.leds.length;i++) {
        leds.leds[i].getFSM().removeProbe(ledProbes[i]);
    }
    probesInserted = false;
*/
    this.deleteObserver(observer);
  }

  public Collection<Element> getConfigXML() {
    // nothing to restore here...
    return null;
  }

  public void setConfigXML(Collection<Element> configXML, boolean visAvailable) {
    // nothing to save here...
  }
}
