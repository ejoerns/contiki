/*
 * Copyright (c) 2009, Swedish Institute of Computer Science.
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

package org.contikios.cooja.plugins.skins;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.Logger;

import org.contikios.cooja.ClassDescription;
import org.contikios.cooja.Mote;
import org.contikios.cooja.Simulation;
import org.contikios.cooja.SimEventCentral.MoteCountListener;
import org.contikios.cooja.interfaces.LEDInterface;
import org.contikios.cooja.interfaces.Position;
import org.contikios.cooja.plugins.Visualizer;
import org.contikios.cooja.plugins.VisualizerSkin;

/**
 * Visualizer skin for LEDInterfaces.
 *
 * Paints three LEDInterfaces left to each mote.
 *
 * @author Fredrik Osterlind
 * @author Enrico Jorns
 */
@ClassDescription("LEDs")
public class LEDVisualizerSkin implements VisualizerSkin {
  private static Logger logger = Logger.getLogger(LEDVisualizerSkin.class);

  private Simulation simulation = null;
  private Visualizer visualizer = null;

  private Observer ledObserver = new Observer() {
    public void update(Observable obs, Object obj) {
      visualizer.repaint();
    }
  };
  private MoteCountListener newMotesListener = new MoteCountListener() {
    public void moteWasAdded(Mote mote) {
      LEDInterface led = mote.getInterfaces().getLED();
      if (led != null) {
        led.addObserver(ledObserver);
      }
    }
    public void moteWasRemoved(Mote mote) {
      LEDInterface led = mote.getInterfaces().getLED();
      if (led != null) {
        led.deleteObserver(ledObserver);
      }
    }
  };

  public void setActive(Simulation simulation, Visualizer vis) {
    this.simulation = simulation;
    this.visualizer = vis;

    simulation.getEventCentral().addMoteCountListener(newMotesListener);
    for (Mote m: simulation.getMotes()) {
      newMotesListener.moteWasAdded(m);
    }
  }

  public void setInactive() {
    simulation.getEventCentral().removeMoteCountListener(newMotesListener);
    for (Mote m: simulation.getMotes()) {
      newMotesListener.moteWasRemoved(m);
    }
  }

  public Color[] getColorOf(Mote mote) {
    return null;
  }

  public void paintBeforeMotes(Graphics g) {
  }

  private static final int LED_Y_DISTANCE = 8;
  private static final int LED_WIDTH = 8;
  private static final int LED_HEIGHT = 5;
  
  private static final Color TRANSPARENT_COLOR = new Color(255, 255, 255, 128);

  public void paintAfterMotes(Graphics g) {
    /* Paint LEDs left of each mote */
    Mote[] allMotes = simulation.getMotes();
    for (Mote mote: allMotes) {
      LEDInterface.LED[] leds = mote.getInterfaces().getLED().getLEDs();
      if (leds == null) {
        continue;
      }

      Position pos = mote.getInterfaces().getPosition();
      Point pixel = visualizer.transformPositionToPixel(pos);

      int x = pixel.x - Visualizer.MOTE_RADIUS - LED_WIDTH - 3;
      int y = pixel.y - LED_HEIGHT / 2;
      
      y -= (leds.length * LED_Y_DISTANCE / 2);
      
      for (LEDInterface.LED led : leds) {
        g.setColor(TRANSPARENT_COLOR);
        g.fillRect(x, y, LED_WIDTH, LED_HEIGHT);
        g.setColor(led.color);
        g.drawRect(x, y, LED_WIDTH, LED_HEIGHT);
        if (led.on) {
          g.setColor(led.color);
          g.fillRect(x, y, LED_WIDTH, LED_HEIGHT);
        }
        y += LED_Y_DISTANCE;
      }
    }
  }

  public Visualizer getVisualizer() {
    return visualizer;
  }
}
